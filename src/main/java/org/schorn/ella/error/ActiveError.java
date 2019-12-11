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
package org.schorn.ella.error;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ObjectData;

/**
 *
 *
 * @author schorn
 *
 */
public interface ActiveError {

    AppContext context();

    /*
	 * 
	 * EVENT POINTS
	 *
     */
    interface ErrorPoint extends ActiveError {

        /**
         *
         * @return
         */
        default AppContext context() {
            return AppContext.Common;
        }
    }

    public enum ErrorFlag implements ErrorPoint {
        VALUE_TYPE_ERROR,
        VALUE_DATA_ERROR,
        OBJECT_TYPE_ERROR,
        OBJECT_TYPE_BUILDER_ERROR,
        OBJECT_DATA_ERROR,
        OBJECT_DATA_BUILDER_ERROR,
        ARRAY_TYPE_ERROR,
        ARRAY_DATA_ERROR,
        LOGICAL_ERROR,
        EXCEPTION_CAUGHT,;

    }

    /*
	 * 
	 * ERRORS
	 * 
     */
    interface Error<T, F> extends ActiveError {

        LocalDateTime timestamp();

    }

    interface TypeError<T extends ActiveType> extends Error<T, ErrorFlag> {

        ErrorFlag flag();

        T type();
    }

    interface DataError<T extends ActiveData> extends Error<T, ErrorFlag> {

        ErrorFlag flag();

        T data();
    }

    /**
     *
     */
    interface ObjectDataError extends DataError<ObjectData>, Renewable<ObjectDataError> {

        static public ObjectDataError create(AppContext context, ErrorFlag flag, ObjectData data) {
            return ErrorProvider.provider().create(context, ObjectDataError.class, flag, data);
        }

        @Override
        ObjectData data();
    }


    /*
	 * 
	 * RESPONDERS
	 * 
     */
    interface Responder<E, T> extends Predicate<E>, Consumer<T>, ActiveError {
    }

    interface DataResponder<E extends DataError<?>, T extends ActiveData> extends Responder<E, T> {

        @Override
        void accept(T data);
    }

    interface ObjectDataResponder extends DataResponder<ObjectDataError, ObjectData> {

        @Override
        void accept(ObjectData data);
    }


    /*
	 * 
	 * MANAGER
	 * 
     */
    interface Manager<E, R> extends ActiveError {

        void fire(E error);

        void register(R reactor);
    }

    interface DataErrorManager extends Manager<DataError<?>, DataResponder<?, ?>> {

        void fire(DataError<?> event);

        void fire(AppContext context, ErrorFlag flag, ActiveData data);

        void register(DataResponder<?, ?> responder);

        static public DataErrorManager get(AppContext context) {
            return ErrorProvider.provider().dataErrorManager(context);
        }
    }

    /*	
 * 	//Example
	ErrorData errorData = ErrorData.create(this.activeType(), ArrayDataImpl.class,
			new Object(){}.getClass().getEnclosingMethod());
	errorData.setDetail("duplicate");
	DataErrorManager.get(this.arrayType().context()).fire(this.arrayType().context(), ActiveError.ErrorFlag.ARRAY_DATA_ERROR, errorData.asActiveData());
     */
}
