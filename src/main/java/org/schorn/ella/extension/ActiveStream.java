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
package org.schorn.ella.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.StructData;

/**
 *
 *
 * @author schorn
 *
 */
public interface ActiveStream {

    /**
     * Stream: children
     *
     * @return
     */
    default Stream<ActiveData> stream() {
        if (this instanceof ActiveData) {
            return Impl.stream((ActiveData) this);
        }
        return Impl.emptyStream();
    }

    /**
     * Stream: all descendants
     *
     * @return
     */
    default Stream<ActiveData> deepStream() {
        if (this instanceof ActiveData) {
            return Impl.deepStream((ActiveData) this);
        }
        return Impl.emptyStream();
    }

    /*
     * 
     */
    static class Impl {

        static Stream<ActiveData> emptyStream() {
            Stream.Builder<ActiveData> builder = Stream.builder();
            return builder.build();

        }

        /*
    	 * 
         */
        static Stream<ActiveData> stream(ActiveData activeData) {
            if (activeData instanceof StructData) {
                StructData structData = (StructData) activeData;
                return structData.nodes().stream();
            } else {
                Stream.Builder<ActiveData> builder = Stream.builder();
                builder.add((ActiveData) activeData);
                return builder.build();
            }
        }

        /*
		 * 
         */
        static Stream<ActiveData> deepStream(ActiveData activeData) {
            //Stream.Builder<ActiveData> builder = Stream.builder();
            List<ActiveData> builder = new ArrayList<>(100);
            Impl.deepStream0(activeData, builder);
            /*
	        switch (activeData.role()) {
	        case Value:
				builder.add((ActiveData) activeData);
        		break;
	        case Object:
	        case Array:
	        	Impl.deepStream0((StructData) activeData, builder);
	        	break;
	        default:
	        	break;
	        }
             */
            return builder.stream();
        }

        /*
		 * 
         */
        static void deepStream0(ActiveData activeData, List<ActiveData> builder) {
            switch (activeData.role()) {
                case Value:
                    builder.add(activeData);
                    break;
                case Object:
                case Array:
                    StructData structData = (StructData) activeData;
                    structData.nodes().stream()
                            .filter(ad -> ad != null)
                            .forEach(ad -> deepStream0(ad, builder));
                    break;
                default:
                    break;
            }
        }
    }
}
