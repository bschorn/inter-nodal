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

import java.util.List;

import org.schorn.ella.Mingleton;
import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.util.StringCached;

/**
 *
 * @author schorn
 *
 */
public interface RDBMS {


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *								Dialect
	 *	
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLDialect {

        int id();

        String description();
    }

    /**
     *
     *
     */
    public enum Dialects implements SQLDialect {
        TSQL("MS SQLServer - Transact-SQL"),
        DB2("IBM DB2 - SQL PL"),;
        private final String description;

        Dialects(String description) {
            this.description = description;
        }

        public String description() {
            return this.description;
        }

        @Override
        public String toString() {
            return String.format("%s [%s]", this.name(), this.description);
        }

        @Override
        public int id() {
            return this.ordinal();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *								Command Type
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLCommandType {

        String description();
    }

    /**
     *
     *
     */
    public enum CommandTypes implements SQLCommandType {
        DCL("Data Control Language"),
        DML("Data Manipulation Language"),
        TCL("Transaction Control Language"),
        DDL("Data Definition Language"),
        PTL("Phrasal Template Language"), // schorn made this up
        ;

        private final String description;

        CommandTypes(String description) {
            this.description = description;
        }

        public String description() {
            return this.description;
        }

        @Override
        public String toString() {
            return String.format("%s - %s", this.name(), this.description);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *								Directive Type
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLDirectiveType {
    }

    /**
     *
     *
     */
    public enum DirectiveTypes implements SQLDirectiveType {
        COMMAND,
        STATEMENT,
        COMBINE,
        TRANSACTION,
        CUSTOM,;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *									Directive
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLDirective {

        DirectiveTypes directiveType();
    }

    /**
     *
     *
     */
    public enum Custom implements SQLDirective {
        FORWARD,
        REVERSE,
        TYPE_LEVEL,
        DATA_LEVEL,;

        @Override
        public DirectiveTypes directiveType() {
            return DirectiveTypes.CUSTOM;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *									Command
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLCommand extends SQLDirective {

        CommandTypes commandType();

        /**
         *
         * @return
         */
        default DirectiveTypes directiveType() {
            return DirectiveTypes.COMMAND;
        }
    }

    /**
     *
     *
     *
     */
    public enum Commands implements SQLCommand {
        INSERT(CommandTypes.DML),
        DELETE(CommandTypes.DML),
        UPDATE(CommandTypes.DML),
        SELECT(CommandTypes.DCL),

        /**
         *
         */
        DISTINCT(CommandTypes.DCL),
        UNION(CommandTypes.DCL),
        UNION_ALL(CommandTypes.DCL),
        WHERE(CommandTypes.DCL),
        LIKE(CommandTypes.DCL),
        AND(CommandTypes.DCL),
        OR(CommandTypes.DCL),
        ORDER_BY(CommandTypes.DCL),
        GROUP_BY(CommandTypes.DCL),
        HAVING(CommandTypes.DCL),
        TRANSACTION(CommandTypes.TCL), // 
        COMMIT(CommandTypes.TCL),

        /**
         *
         */
        ROLLBACK(CommandTypes.TCL),;

        private final CommandTypes cmd_type;

        Commands(CommandTypes sqlGroup) {
            this.cmd_type = sqlGroup;
        }

        @Override
        public CommandTypes commandType() {
            return this.cmd_type;
        }

        @Override
        public String toString() {
            return String.format("%s [%s]", this.name(), this.cmd_type.toString());
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *									Template Tag
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLTemplateTag {

        /**
         *
         * @return
         */
        String tag();
    }

    /**
     *
     *
     */
    public enum TemplateTags implements SQLTemplateTag {
        TGT_SCHEMA("<tgt-schema>"),
        TGT_TABLE("<tgt-table>"),
        WHERE_KEYS_EQUAL_VALUES("<where-tgt-keys-equal-values>"),
        TGT_COLUMN_NAMES("<tgt-column-names>"),
        TGT_KEY_NAMES("<tgt-key-names"),

        /**
         *
         */
        TGT_KEY_NAME("<tgt-key-name>"),

        /**
         *
         */
        TGT_COLUMN_VALUES("<tgt-column-values>"),
        TGT_JOIN_ON_KEYS("<tgt-join-on-keys>"),
        SET_BY_COLUMN_NAMES("<set-by-column-names>"),
        TGT_ALIAS("<tgt-alias>"),
        SRC_ALIAS("<src-alias>"),
        TGT_TABLE_SPECIAL_KEY("<tgt-table-special-key>"),;

        private final String tag;

        TemplateTags(String tag) {
            this.tag = tag;
        }

        @Override
        public String tag() {
            return this.tag;
        }

    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *							Template Tags Transducer
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface TemplateTagsTransducer extends Mingleton {

        String transduce(TemplateTags tag, String content, Object object);

        static public TemplateTagsTransducer get(AppContext context) {
            return SQLProvider.provider().getMingleton(TemplateTagsTransducer.class, context);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *								Phrasal Template
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLPhrasalTemplate extends SQLDirective {

        String sql(SQLDialect dialect);

        default DirectiveTypes directiveType() {
            return DirectiveTypes.STATEMENT;
        }
    }

    /**
     *
     * Predefined Common Statements
     *
     */
    public enum PhrasalTemplates implements SQLPhrasalTemplate {
        DELETE_ROW_TARGET_TABLE(CommandTypes.DML,
                "DELETE <tgt-schema>.<tgt-table> WHERE <where-tgt-keys-equal-values>", ""),
        INSERT_ROW_TARGET_TABLE(CommandTypes.DML,
                "INSERT INTO <tgt-schema>.<tgt-table> ( <tgt-column-names> ) VALUES ( <tgt-column-values> )", ""),
        CREATE_TEMP_TARGET_TABLE(CommandTypes.DDL,
                "DECLARE GLOBAL TEMPORARY TABLE SESSION.<tgt-table> AS ( SELECT * FROM <tgt-schema>.<tgt-table> ) DEFINITION ONLY NOT LOGGED WITH REPLACE ON COMMIT PRESERVE ROWS", ""),
        //CREATE_TEMP_TARGET_TABLE_UPDATES(CommandTypes.DDL,
        //	"DECLARE GLOBAL TEMPORARY TABLE SESSION.<tgt-table>_updates AS ( SELECT * FROM <tgt-schema>.<tgt-table> ) DEFINITION ONLY NOT LOGGED WITH REPLACE ON COMMIT PRESERVE ROWS",""),
        //INSERT_ROW_TEMP_TARGET_TABLE_BY_VALUES(CommandTypes.DML,
        //	"INSERT INTO SESSION.<tgt-table> ( <tgt-column-names> ) VALUES ( <tgt-column-values> )",""),
        INSERT_ROWS_TEMP_TARGET_TABLE_BY_SELECT(CommandTypes.DML,
                "INSERT INTO SESSION.<tgt-table> ( <tgt-column-names> )", ""),
        SELECT_PSEUSDO_ROW_FROM_DUAL(CommandTypes.DML,
                "SELECT <tgt-column-values> FROM DUAL", ""),
        //DELETE_TARGET_TABLE_WHERE_NOTIN_TEMP(CommandTypes.DML,"",""),
        UPDATE_TARGET_TABLE_FROM_TEMP(CommandTypes.DML,
                //"UPDATE <tgt-schema>.<tgt-table> <tgt-alias> SET ( <tgt-column-names> ) = (SELECT <tgt-column-names> FROM SESSION.<tgt-table> <src-alias> WHERE <tgt-join-on-keys> )",""),
                "MERGE INTO <tgt-schema>.<tgt-table> <tgt-alias> USING ( SELECT * FROM SESSION.<tgt-table> ) <src-alias> ON ( <tgt-join-on-keys> ) WHEN MATCHED THEN UPDATE SET <set-by-column-names>", ""),
        DELETE_ROWS_TEMP_TARGET_TABLE_BY_JOIN_TARGET_TABLE(CommandTypes.DML,
                "DELETE FROM SESSION.<tgt-table> WHERE <tgt-table-special-key> IN ( SELECT <src-alias>.<tgt-table-special-key> FROM SESSION.<tgt-table> <src-alias> JOIN <tgt-schema>.<tgt-table> <tgt-alias> ON <tgt-join-on-keys> )", ""),
        INSERT_ROWS_TARGET_TABLE_FROM_TEMP(CommandTypes.DML,
                "INSERT INTO <tgt-schema>.<tgt-table> ( <tgt-column-names> ) SELECT <tgt-column-names> FROM SESSION.<tgt-table>", ""),
        DROP_TEMP_TARGET_TABLE(CommandTypes.DDL,
                "DROP TABLE SESSION.<tgt-table>", ""),
        DROP_TEMP_TARGET_TABLE_KEY_ONLY(CommandTypes.DDL,
                "DROP TABLE SESSION.<tgt-table>_keys", ""),;

        private final CommandTypes sqlGroup;
        private final StringCached[] sqls = new StringCached[Dialects.values().length];

        PhrasalTemplates(CommandTypes sqlGroup, String db2, String tsql) {
            this.sqlGroup = sqlGroup;
            this.sqls[Dialects.DB2.id()] = new StringCached(db2);
            this.sqls[Dialects.TSQL.id()] = new StringCached(tsql);
        }

        public CommandTypes sqlGroup() {
            return this.sqlGroup;
        }

        public String db2() {
            return this.sqls[Dialects.DB2.id()].toString();
        }

        public String tsql() {
            return this.sqls[Dialects.TSQL.id()].toString();
        }

        @Override
        public String sql(SQLDialect lang) {
            return this.sqls[lang.id()].toString();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *								Compound (Directive)
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLCompound extends SQLDirective {

        SQLDirective[] directives();

        default DirectiveTypes directiveType() {
            return DirectiveTypes.COMBINE;
        }
    }

    /**
     *
     *
     */
    static class CompoundImpl implements SQLCompound {

        private final SQLDirective[] directives;

        CompoundImpl(SQLDirective[] directives) {
            this.directives = directives;
        }

        @Override
        public SQLDirective[] directives() {
            return this.directives;
        }
    }

    /**
     *
     * @param directives
     * @return 
     */
    static public SQLCompound Combine(SQLDirective... directives) {
        return new CompoundImpl(directives);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *							 Transaction (Directive)
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     *
     */
    interface SQLTransaction extends SQLCompound {

        SQLDirective[] directives();

        default DirectiveTypes directiveType() {
            return DirectiveTypes.TRANSACTION;
        }
    }

    /**
     *
     *
     */
    static class TransactionImpl extends CompoundImpl implements SQLTransaction {

        TransactionImpl(SQLDirective[] directives) {
            super(directives);
        }
    }

    /**
     *
     */
    static public SQLCompound Transaction(SQLDirective... directives) {
        return new TransactionImpl(directives);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *							
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface SQLProgramCode {

        SQLDialect dialect();

        SQLDirective[] directives();
    }

    interface SQLProgramData {

        List<ObjectType> orderedTypes();

        List<ObjectType> reverseOrderTypes();

        List<ObjectData> getOrderedDataList(ObjectType objectType);
    }

    interface SQLProgramOutput extends JDBCExecutable, Renewable<SQLProgramOutput> {

        static public SQLProgramOutput create(AppContext context) {
            return SQLProvider.provider().getRenewable(SQLProgramOutput.class, context);
        }
    }

    interface JDBCExecutable {

        List<JDBCExecuteStep> steps();
    }

    interface JDBCExecuteStep extends Renewable<JDBCExecuteStep> {

        public enum StepType {
            SET_SAVEPOINT(false),
            COMMIT(false),
            ROLLBACK(false),
            EXECUTE_UPDATE(true, String.class),
            ADD_BATCH(true, String.class),
            EXECUTE_BATCH(false),;
            private final boolean hasParameter;
            private final Class<?>[] classTypes;

            StepType(boolean hasParameter, Class<?>... classTypes) {
                this.hasParameter = hasParameter;
                this.classTypes = classTypes;
            }

            boolean hasParameter() {
                return this.hasParameter;
            }

            Class<?>[] parameterClasses() {
                return this.classTypes;
            }

            public JDBCExecuteStep createStep(AppContext context, Object... parameters) {
                return SQLProvider.provider().getRenewable(JDBCExecuteStep.class, context, this, parameters);
            }
        }

        StepType stepType();

        Object[] getParameters();

    }

    interface SQLInterpreter extends Mingleton {

        JDBCExecutable compile(SQLProgramCode code, SQLProgramData data);

        static public SQLInterpreter create(AppContext context) {
            return SQLProvider.provider().getMingleton(SQLInterpreter.class, context);
        }
    }

}
