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
package org.schorn.ella.services;

import java.util.function.Function;

import org.schorn.ella.FunctionalException;
import org.schorn.ella.Mingleton;
import org.schorn.ella.context.ActiveContext;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveOperator;
import org.schorn.ella.node.ActiveNode.Operators;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;

/**
 * NamedQueryString
 *
 * NamedQuery's queryString
 *
 * http[s]://path/<NamedQuery>?<NamedQueryString>
 *
 * @author schorn
 *
 */
public interface QueryString extends ActiveContext.Contextual {

    /**
     * <ul>
     * <li>EQUAL_TO "Table.Field:Y" (Table.Field equals Y)</li>
     * <li>LESS_THAN "Table.Field<Y" (Table.Field less than Y)</li> <li>EQUAL_TO
     * "Table.Fiel d>Y" (Table.Field greater than Y)</li>
     * <li>LESS_THAN_INCLUSIVE "Table.Field<:Y" (Table.Field less than or equal
     * to Y)</li> <li>GREATER_THAN_INCLUSIVE "Table.Fie ld>:Y" (Table.Field
     * greater than or equal to Y)</li>
     * <li>EQUAL_IN "Table.Field*X Y Z" (Table.Field equals X or Y or Z)</li>
     * <li>BETWEEN "Table.Field|Y Z" (Table.Field between X and Z)</li>
     * </ul>
     *
     */
    public enum QueryStringOperators implements ActiveOperator {
        EQUALS(Operators.EQUALS, ":"),
        NOT_EQUALS(Operators.NOT_EQUALS, "!:"),
        LESS_THAN(Operators.LESS_THAN, "<"),
        GREATER_THAN(Operators.GREATER_THAN, ">"),
        BETWEEN_OR_EQUALS(Operators.BETWEEN_OR_EQUALS, "|"),
        EQUALS_IN(Operators.EQUALS_IN, "*"),
        GREATER_THAN_OR_EQUALS(Operators.GREATER_THAN_OR_EQUALS, ">:"),
        LESS_THAN_OR_EQUALS(Operators.LESS_THAN_OR_EQUALS, "<:"),
        NOT_EQUALS_IN(Operators.NOT_EQUALS_IN, "!*"),;

        Operators operator;
        String expression;

        QueryStringOperators(Operators operator, String expression) {
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
                case MULTIVAL:
                    return " ";
                default:
                    return "";
            }
        }

        @Override
        public String[] valends() {
            switch (this.dimension()) {
                case MULTIVAL:
                    return new String[]{"", ""};
                default:
                    return new String[]{"", ""};
            }
        }

        @Override
        public Dimension dimension() {
            return this.operator.dimension();
        }

        @Override
        public Operators mnemonic() {
            return this.operator;
        }

        public String parseValsep() {
            return this.valsep();
        }

        public String joinDelim() {
            return this.valsep().replace(" ", "+");
        }
    }

    /**
     * Takes pre-parsed: String[] queryStrings = queryString.split("&")
     *
     * queryStrings[i] = <ObjectType>.<ValueType><expression><value>
     */
    interface Where extends Function<String[], ActiveCondition[]>, FunctionalException, Mingleton {

        public ActiveCondition[] apply(String[] queryStrings);

        static public Where get(AppContext context) {
            return ServicesProvider.provider().getMingleton(Where.class, context);
        }
    }

}
