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
package org.schorn.ella;

/**
 * A class implements the {@code Renewable} interface to indicate that it will
 * create a new instance that has the same member values as <code>this</code>
 * except for those members that can be identified in the varargs parameters.
 * <p>
 * The intent of this interface is to allow an object to become its own factory.
 * By having the object be its own factory means there will no
 * <code>ClassNotFoundException</code>, so we can remove the throws on the
 * instance creation.
 *
 * @author schorn
 */
public interface Renewable<T> {

    /**
     * The renew method will iterate through the varargs parameter
     * <code>params</code> looking for <code>instanceof</code> matches with the
     * members of <code>this</code>.
     * <p>
     * Example Interface
     * <pre>
     * {@code
     *     interface Contestant extends Renewable<Contestant> {
     *     }
     * }
     * </pre> Example Implementation
     * <p>
     * <
     * pre> null     {@code
     *
     *    class ContestantImpl implements Contestant {
     *         private String name;
     *         private Integer age;
     *
     *         public ContestantImpl(String name, Integer age) {
     *    	      this.name = name;
     *            this.age = age;
     *         }
     *
     *         public Contestant renew(Object...params) {
     *            String name = this.name;
     *            Integer age = this.age;
     *            for (Object param : params) {
     *        	      if (param instanceof String) {
     *        		      name = (String) param;
     *                } else if (param instanceof Integer) {
     *                    age = (Integer) param;
     *                }
     *            }
     *            return new ContestantImpl(name, age);
     * }
     * }
     * }
     * </pre>
     * <p>
     * Example Usage
     * <pre>
     * {@code
     *
     *     final Contestant factory;
     *     try {
     *          factory = ReflectionBasedFactory.create("Contestant");
     *     } catch (ClassNotFoundException ex) {
     *          // handle exception
     *     }
     *
     *     // Later on we can use the previously created <code>factory</code>
     *     // instance without the try/catch.
     *
     *
     *     // collections of 30 year olds
     *     List<Contest> thirtyYearOldContestants = new ArrayList<>();
     *     String[] thirtyYearOldNames = new String[]{"John","Sally","David","Megan"};
     *
     *     for (String name : thirtyYearOldNames) {
     *         thirtyYearOldContestant.add(factory.renew(name, 30));
     *     }
     *
     *     // collection of Joes
     *     List<Contest> contestantsNamedJoe = new ArrayList<>();
     *     Integer[] joeAges = new Integer[]{12,17,23,25,34,43,56,61};
     *
     *     for (Integer age : joeAges) {
     *         contestantsNamedJoe.add(factory.renew("Joe", age));
     *     }
     * }
     * </pre>
     *
     *
     *
     * @param params should match the constructor's parameters
     * @return a new instance
     */
    T renew(Object... params);
}
