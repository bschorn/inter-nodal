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
package org.schorn.ella.app;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import org.schorn.ella.convert.TypeConverter;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;

/**
 *
 * @author schorn
 *
 */
public interface BaseConfig {

    Properties properties();

    String propertyName();

    String propertyKey();

    Class<?> propertyOwner();

    Logger logger();

    String defaultValue();

    default String value() {
        /*
        * Use the stack trace and look for the 'proptery owner' of the value in the
        * call stack. Objective is to keep the usage of the configuration value
        * limited to owner.
         */
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        boolean validAccess = false;
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().equals(this.propertyOwner().getName())) {
                validAccess = true;
                break;
            }
        }
        if (!validAccess) {
            /*
            * log an error anytime the config value is access and the owner
            * is not in the call stack.
            * if the owner has changed, please update propertyOwner member
             */
            this.logger().error("{}.value() was not called by the owner: {}",
                    this.propertyName(), this.propertyOwner().getSimpleName());
        }
        String value = this.properties().getProperty(this.propertyKey(), this.defaultValue());
        if (value != null) {
            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                value = value.replace(String.format("%s", "%" + entry.getKey() + "%"), entry.getValue());
            }
        }
        return value;
    }

    default String[] values(String delimiter) {
        if (this.value() != null && this.value().length() > 0) {
            return this.value().split(delimiter);
        }
        return new String[0];
    }

    default Number valueAsNumber() {
        try {
            String value = this.value();
            if (value != null) {
                BigDecimal valueAsNumber = TypeConverter.recast(String.class, BigDecimal.class, value);
                return (Number) valueAsNumber;
            }
        } catch (Exception ex) {
            this.logger().error("{}.valueAsNumber() - {} caught Exception: {}",
                    this.getClass().getSimpleName(), this.propertyName(),
                    Functions.getStackTraceAsString(ex));
        }
        return null;
    }

    /**
     *
     * @param <T>
     * @param classForT
     * @return
     */
    default <T> T valueAs(Class<T> classForT) {
        try {
            String value = this.value();
            if (value != null) {
                return (T) TypeConverter.recast(String.class, classForT, value);
            }
        } catch (Exception ex) {
            this.logger().error("{}.valueAs() - {} caught Exception: {}",
                    this.getClass().getSimpleName(), this.propertyName(),
                    Functions.getStackTraceAsString(ex));
        }
        return null;

    }
}
