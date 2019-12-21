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
package org.schorn.ella.convert;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.schorn.ella.node.ActiveNode.TypeConversion;

/**
 * Usage: LocalDate myDate = TypeConverter.recast(String.class, LocalDate.class,
 * "2019-07-15"); T t = (T) TypeConverter.recast(Long.class, classForT, 100L);
 *
 * ConversionLibrary contains all the conversions available. To add or modify:
 * provide a Function<T,R> lambda to do a conversion and call:
 * TypeConverter.add(T.class, R.class, Function<T,R>, String description);
 *
 * @author bschorn
 */
public class TypeConverter {

    public TypeConverter() {
        ConversionLibrary.load(this);
    }

    static private TypeConverter INST = new TypeConverter();

    static public <R> R recast(Class<?> classFrom, Class<R> classTo, Object valueFrom) throws Exception {
        return INST.convert(classFrom, classTo, valueFrom);
    }

    @SuppressWarnings("unchecked")
    public <R> R convert(Class<?> classFrom, Class<R> classTo, Object valueFrom) throws Exception {
        if (classFrom == classTo) {
            return (R) valueFrom;
        }
        TypeConversion<?, R> typeConversion = this.get(classFrom, classTo);
        if (typeConversion != null) {
            return typeConversion.apply(valueFrom);
        }
        throw new Exception(String.format("%s.convert() - no conversion for %s to %s (value of '%s')",
                this.getClass().getSimpleName(), classFrom.getSimpleName(),
                classTo.getSimpleName(), valueFrom.toString()
        ));
    }

    /**
     * Member
     */
    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, Map<Class<?>, TypeConversion>> map = new HashMap<>();

    /**
     *
     * @param classOfT
     * @param classOfR
     * @return
     */
    @SuppressWarnings("rawtypes")
    <T, R> TypeConversion get(Class<T> classOfT, Class<R> classOfR) {
        Map<Class<?>, TypeConversion> subMap = this.map.get(classOfT);
        if (subMap != null) {
            return subMap.get(classOfR);
        }
        return null;
    }

    /**
     *
     * @param translations
     * @return
     */
    @SuppressWarnings("rawtypes")
    public TypeConversion add(TypeConversion translations) {
        Map<Class<?>, TypeConversion> subMap = this.map.get(translations.sourceClass());
        if (subMap == null) {
            subMap = new HashMap<>();
            this.map.put(translations.sourceClass(), subMap);
        }
        return subMap.put(translations.targetClass(), translations);
    }

    /**
     *
     * @param sourceClass
     * @param targetClass
     * @param function
     * @param description
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T, R> TypeConversion add(Class<T> sourceClass, Class<R> targetClass,
            Function<T, R> function, String description) {
        TypeConversion translations = new TypeConversionImpl(sourceClass, targetClass,
                function, description);
        return add(translations);
    }

    /*
	 * Add Custom Enum Type Conversions
	 * (by replicating the Enum -> String conversion and replacing 'Enum.class' with 'enumClass')
     */

    /**
     *
     * @param enumClass
     */

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addEnumConversion(Class<? extends Enum> enumClass) {
        TypeConversion enumConversion = get(Enum.class, String.class);
        add(enumClass, String.class, enumConversion.function(),
                enumConversion.description().replace("Enum", enumClass.getSimpleName()));
    }

}
