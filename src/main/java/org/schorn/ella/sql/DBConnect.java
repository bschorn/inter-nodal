/* 
 * The MIT License
 *
 * Copyright 2019 Bryan Schorn.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
 /*
 * Copyright (C) 2019 bschorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.schorn.ella.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.io.EndPoint;
import org.schorn.ella.sql.RDBMS.JDBCExecutable;
import org.schorn.ella.sql.RDBMS.JDBCExecuteStep;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public class DBConnect implements AutoCloseable {

    static final Logger LGR = LoggerFactory.getLogger(DBConnect.class);

    static Map<EndPoint.DatabasePoint, Class<? extends DataSource>> REGIS = new HashMap<>();
    static Map<EndPoint.DatabasePoint, DataSource> DS = new HashMap<>();

    static public void register(EndPoint.DatabasePoint dbPoint, Class<? extends DataSource> dataSourceClass) {
        REGIS.put(dbPoint, dataSourceClass);
    }

    static public DBConnect get(EndPoint.DatabasePoint dbPoint) throws InstantiationException, IllegalAccessException {
        return new DBConnect(dbPoint);
    }

    /*
	 * Members
     */
    EndPoint.DatabasePoint dbPoint;
    Connection conn;
    Statement stmt;

    /*
	 * Ctor
     */
    DBConnect(EndPoint.DatabasePoint dbPoint) throws InstantiationException, IllegalAccessException {
        this.dbPoint = dbPoint;
        if (!DS.containsKey(dbPoint)) {
            synchronized (DS) {
                if (!DS.containsKey(dbPoint)) {
                    Class<? extends DataSource> dataSourceClass = REGIS.get(dbPoint);
                    if (dataSourceClass.getSimpleName().equals("DB2DataSource")) {
                        /*
						DB2DataSource ds = (DB2DataSource) dataSourceClass.newInstance();
						ds.setDatabaseServer(dbPoint.server());
						ds.setDatabaseName(dbPoint.schema());
						DS.put(dbPoint, ds);
                         */
                    } else {
                        DataSource ds = (DataSource) dataSourceClass.newInstance();
                        DS.put(dbPoint, ds);
                    }
                }
            }
        }
    }

    /**
     *
     *
     *
     */
    static public class DBTransaction implements Consumer<String>, AutoCloseable {

        static final Logger LGR = LoggerFactory.getLogger(DBTransaction.class);
        private final List<String> strStatements;
        private final Statement stmt;

        DBTransaction(Statement stmt) {
            this.stmt = stmt;
            this.strStatements = new ArrayList<>();
        }

        @Override
        public void close() throws Exception {
            if (!this.stmt.getConnection().getAutoCommit()) {
                this.stmt.getConnection().setAutoCommit(true);
            }
        }

        /**
         *
         */
        @Override
        public void accept(String strStatements) {
            this.strStatements.add(strStatements);
        }

        /**
         *
         * @param strStatements
         * @return
         * @throws Exception
         */
        public boolean execute(List<String> strStatements) throws Exception {
            strStatements.stream().forEach(s -> this.accept(s));
            return this.execute();
        }

        /**
         *
         * @throws Exception
         */
        public boolean execute() throws Exception {
            String lastStatement = "";
            try {
                if (this.strStatements.isEmpty()) {

                    return false;
                }
                if (this.stmt.getConnection().getAutoCommit()) {
                    this.stmt.getConnection().setAutoCommit(false);
                }
                LGR.debug("{}.execute() - SQL STATEMENTS:\n{}",
                        this.getClass().getSimpleName(), this.toString());
                LGR.info("{}.execute() - BEGIN TRANSACTION {calling Connection.setSavepoint()}",
                        this.getClass().getSimpleName());
                this.stmt.getConnection().setSavepoint();
                for (String sqlStatement : this.strStatements) {
                    lastStatement = sqlStatement;
                    this.stmt.executeUpdate(lastStatement);
                }
                LGR.info("{}.execute() - COMMIT TRANSACTION {calling Connection.commit()}",
                        this.getClass().getSimpleName());
                this.stmt.getConnection().commit();
                return true;
            } catch (Exception ex) {
                try {
                    LGR.info("{}.execute() - ROLLBACK TRANSACTION {calling Connection.rollback()}",
                            this.getClass().getSimpleName());
                    this.stmt.getConnection().rollback();
                    LGR.error("{}.execute() - Last Statement:\n{}",
                            this.getClass().getSimpleName(),
                            lastStatement);

                } catch (SQLException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
                throw ex;
            }
        }

        /**
         * Return all statements concatenated with statements separated by an
         * end statement (;)
         */
        public String toString() {
            StringJoiner joiner = new StringJoiner(";\n", "\n", ";\n");
            this.strStatements.stream().forEach(s -> joiner.add(s));
            return joiner.toString();
        }
    }

    /**
     *
     * @return @throws SQLException
     */
    public DBTransaction createTransaction() throws Exception {
        DataSource ds = DS.get(this.dbPoint);
        this.conn = ds.getConnection();
        this.stmt = this.conn.createStatement();
        if (this.stmt.getConnection().getAutoCommit()) {
            this.stmt.getConnection().setAutoCommit(false);
        }
        return new DBTransaction(this.stmt);
    }

    /**
     *
     *
     *
     */
    static public class DBExecutor implements AutoCloseable {

        static final Logger LGR = LoggerFactory.getLogger(DBExecutor.class);
        private final Statement stmt;

        DBExecutor(Statement stmt) {
            this.stmt = stmt;
        }

        public void execute(JDBCExecutable executable) throws Exception {
            boolean transaction = false;
            for (JDBCExecuteStep step : executable.steps()) {
                //LGR.info("{}.execute() - {}", this.getClass().getSimpleName(), step.toString());
                System.out.println(step.toString());

                Object[] params = new Object[0];
                if (step.stepType().hasParameter()) {
                    params = step.getParameters();
                }
                String lastStatement = null;
                try {
                    switch (step.stepType()) {
                        case SET_SAVEPOINT: {
                            //System.out.println("BEGIN TRANSACTION");
                            this.stmt.getConnection().setSavepoint();
                            transaction = true;
                            break;
                        }
                        case COMMIT: {
                            //System.out.println("COMMIT TRANSACTION");
                            this.stmt.getConnection().commit();
                            transaction = false;
                            break;
                        }
                        case EXECUTE_UPDATE: {
                            for (Object param : params) {
                                if (param instanceof String) {
                                    lastStatement = (String) param;
                                    //System.out.println(lastStatement);
                                    this.stmt.executeUpdate(lastStatement);
                                    if (!transaction) {
                                        this.stmt.getConnection().commit();
                                    }
                                }
                            }
                            break;
                        }
                        case ADD_BATCH: {
                            for (Object param : params) {
                                if (param instanceof String) {
                                    lastStatement = (String) param;
                                    this.stmt.addBatch(lastStatement);
                                }
                            }
                            break;
                        }
                        case EXECUTE_BATCH: {
                            this.stmt.executeBatch();
                            if (!transaction) {
                                this.stmt.getConnection().commit();
                            }
                            break;
                        }
                        default:
                            break;
                    }
                } catch (Exception ex) {
                    try {
                        LGR.info("{}.execute() - ROLLBACK TRANSACTION {calling Connection.rollback()}",
                                this.getClass().getSimpleName());
                        if (transaction) {
                            this.stmt.getConnection().rollback();
                        }
                        LGR.error("{}.execute() - Last Statement:\n{}",
                                this.getClass().getSimpleName(),
                                lastStatement);

                    } catch (SQLException rollbackEx) {
                        ex.addSuppressed(rollbackEx);
                    }
                    throw ex;
                }
            }
        }

        @Override
        public void close() throws Exception {
            if (!this.stmt.getConnection().getAutoCommit()) {
                this.stmt.getConnection().setAutoCommit(true);
            }
        }

        public void setAutoCommit(boolean flag) throws Exception {
            if (this.stmt.getConnection().getAutoCommit() != flag) {
                this.stmt.getConnection().setAutoCommit(flag);
            }
        }

    }

    /**
     *
     * @return @throws Exception
     */
    public DBExecutor createExecutor() throws Exception {
        DataSource ds = DS.get(this.dbPoint);
        this.conn = ds.getConnection();
        this.stmt = this.conn.createStatement();
        if (this.stmt.getConnection().getAutoCommit()) {
            this.stmt.getConnection().setAutoCommit(false);
        }
        return new DBExecutor(this.stmt);
    }

    /**
     *
     * @return @throws SQLException
     */
    public Statement createStatement() throws SQLException {
        DataSource ds = DS.get(this.dbPoint);
        try {
            this.conn = ds.getConnection();
            this.stmt = this.conn.createStatement();
            return this.stmt;
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
            throw ex;
        }
    }

    @Override
    public void close() throws Exception {

        if (this.stmt != null) {
            if (!this.stmt.isClosed()) {
                this.stmt.close();
            }
        }
        if (this.conn != null) {
            if (!this.conn.isClosed()) {
                this.conn.close();
            }
        }

    }

}
