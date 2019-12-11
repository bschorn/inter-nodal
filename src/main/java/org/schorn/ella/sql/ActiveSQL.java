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

import java.util.function.Consumer;

import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveOperator;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.Operators;
import org.schorn.ella.sql.RDBMS.JDBCExecutable;

/**
 *
 * @author schorn
 *
 */
public interface ActiveSQL {

    /**
     *
     *
     */
    interface ExecutableGenerator extends Consumer<ObjectData> {

        void accept(ObjectData objectData);

        JDBCExecutable getExecutable();
    }

    /**
     *
     */
    interface SynchronizeObjects extends ExecutableGenerator, Renewable<SynchronizeObjects> {

        interface Stable extends SynchronizeObjects {
        }

        interface Experimental extends SynchronizeObjects {
        }

        static public ExecutableGenerator createExperimental(AppContext context, RDBMS.SQLDialect dialect) {
            return SQLProvider.provider().getRenewable(Experimental.class, context, dialect);
        }

        static public ExecutableGenerator createStable(AppContext context, RDBMS.SQLDialect dialect) {
            return SQLProvider.provider().getRenewable(Stable.class, context, dialect);
        }
    }

    /**
     *
     *
     * @param classFor
     * @param keyName
     * @param activeType
     * @return
     */
    static String getPropertyKey(Class<?> classFor, String keyName, ActiveType activeType) {
        return String.format("%s.%s.%s", classFor.getSimpleName(), keyName, activeType.name());
    }

    /**
     *
     */
    public enum SQLOperators implements ActiveOperator {
        EQUALS(Operators.EQUALS, "="),
        NOT_EQUALS(Operators.NOT_EQUALS, "<>"),
        LESS_THAN(Operators.LESS_THAN, "<"),
        GREATER_THAN(Operators.GREATER_THAN, ">"),
        BETWEEN_OR_EQUALS(Operators.BETWEEN_OR_EQUALS, "BETWEEN"),
        EQUALS_IN(Operators.EQUALS_IN, "IN"),
        GREATER_THAN_OR_EQUALS(Operators.GREATER_THAN_OR_EQUALS, ">="),
        LESS_THAN_OR_EQUALS(Operators.LESS_THAN_OR_EQUALS, "<="),
        NOT_EQUALS_IN(Operators.NOT_EQUALS_IN, "NOT IN"),;

        Operators operator;
        String expression;

        SQLOperators(Operators operator, String expression) {
            this.operator = operator;
            this.expression = expression;
        }

        @Override
        public String opstr() {
            return this.expression;
        }

        @Override
        public String valsep() {
            switch (this.dimension()) {
                case BIVAL:
                    return " AND ";
                case MULTIVAL:
                    return ",";
                default:
                    return "";
            }
        }

        @Override
        public String[] valends() {
            switch (this.dimension()) {
                case MULTIVAL:
                    return new String[]{"(", ")"};
                default:
                    return new String[]{"", ""};
            }
        }

        /**
         *
         * @return
         */
        @Override
        public Dimension dimension() {
            return this.operator.dimension();
        }

        @Override
        public Operators mnemonic() {
            return this.operator;
        }

        static public SQLOperators parse(String operator) {
            for (SQLOperators sqlOperator : SQLOperators.values()) {
                if (sqlOperator.opstr().equals(operator)) {
                    return sqlOperator;
                }
            }
            for (SQLOperators sqlOperator : SQLOperators.values()) {
                if (sqlOperator.mnemonic().name().equals(operator)) {
                    return sqlOperator;
                }
            }
            return null;
        }
    }

}
