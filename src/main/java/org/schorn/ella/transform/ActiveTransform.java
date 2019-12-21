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
package org.schorn.ella.transform;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.FunctionalAction;
import org.schorn.ella.app.NodeConfig;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.OpenNode;

/**
 * A collection of interfaces for the most likely transformations.
 *
 *
 * @author schorn
 *
 */
public interface ActiveTransform {

    /**
     * Performance of Transform
     *
     */
    public static class Transformance {

        Map<Role, Long> totalElapsed = new HashMap<>();
        Map<Role, Long> totalQuantity = new HashMap<>();

        public Transformance() {
            for (Role role : new Role[]{Role.Value, Role.Object, Role.Array}) {
                this.totalElapsed.put(role, Long.valueOf(0));
                this.totalQuantity.put(role, Long.valueOf(0));
            }
        }

        public void add(Role role, Long timeElapsed) {
            this.totalElapsed.put(role, this.totalElapsed.get(role) + timeElapsed);
            this.totalQuantity.put(role, this.totalQuantity.get(role) + 1);
        }

        public double getAvg(Role role) {
            if (this.totalQuantity.get(role) == 0) {
                return 0.0;
            }
            return ((double) this.getAdjElapsedTime(role)) / this.totalQuantity.get(role).doubleValue();
        }

        public long getQty(Role role) {
            return this.totalQuantity.get(role);
        }

        public long getAdjElapsedTime(Role role) {
            switch (role) {
                case Value:
                    return this.totalElapsed.get(role);
                case Array:
                    return this.totalElapsed.get(role) - this.totalElapsed.get(Role.Value);
                case Object:
                    return this.totalElapsed.get(role) - (this.totalElapsed.get(Role.Value) + this.totalElapsed.get(Role.Array));
                default:
                    return 0;
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("Transformance: ");
            for (Role role : new Role[]{Role.Value, Role.Object, Role.Array}) {
                builder.append(String.format("%s[%d] -> %.3fms (%dms); ", role.name(), getQty(role), getAvg(role), getAdjElapsedTime(role)));
            }
            return builder.toString();
        }
    }

    /**
     * Performance Tracking of Transform
     *
     */
    interface TransformanceTracker {

        void resetTransformance();

        Transformance getTransformance();
    }

    /**
     * Exception Handling of functional interface Transform
     */
    interface TransformException extends TransformanceTracker {

        void setException(Exception exception);

        void clearException();

        /**
         *
         * @throws Exception
         */
        void throwException() throws Exception;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 *                       Transformations 
	 *                  JSON -> ActiveNode -> JSON
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface Transform<T, R> extends FunctionalAction.Mutate<T, R>, TransformException {

        Format sourceFormat();

        /**
         *
         * @return
         */
        Format targetFormat();

        @Override
        R apply(T t); // should put some metric collection inside this function
    }

    /**
     * JSON String --> OpenNode
     */
    interface JSONToOpenNode extends Transform<String, OpenNode> {

        @Override
        default Format sourceFormat() {
            return Format.JSON;
        }

        @Override
        default Format targetFormat() {
            return Format.OpenNode;
        }

        @Override
        OpenNode apply(String jsonString);
    }

    /**
     * OpenNode --> ActiveNode.StructData
     */
    interface OpenNodeToActiveNode extends Transform<OpenNode, StructData> {

        static public final String AUTO_DYNAMIC_TYPE
                = String.format("%s.AUTO_DYNAMIC_TYPE", OpenNodeToActiveNode.class.getSimpleName());

        default boolean isAutoDynamicTypes() throws Exception {
            return NodeConfig.AUTO_DYNAMIC_TYPE.asNumber().intValue() == 1;
        }

        @Override
        default Format sourceFormat() {
            return Format.OpenNode;
        }

        @Override
        default Format targetFormat() {
            return Format.ActiveNode;
        }

        @Override
        StructData apply(OpenNode openNode);
    }

    /**
     * JSON String --> Active.StructData
     *
     */
    interface JSONToActiveNode extends Transform<String, StructData> {

        @Override
        default Format sourceFormat() {
            return Format.JSON;
        }

        @Override
        default Format targetFormat() {
            return Format.ActiveNode;
        }

        @Override
        StructData apply(String jsonString);
    }

    /**
     * JsonReader --> Active.StructData
     */
    interface JsonReaderToActiveNode extends Transform<Reader, StructData> {

        @Override
        default Format sourceFormat() {
            return Format.JsonReader;
        }

        @Override
        default Format targetFormat() {
            return Format.ActiveNode;
        }

        @Override
        StructData apply(Reader reader);
    }

    /**
     * YAML String --> Active.StructData
     *
     */
    interface YAMLToActiveNode extends Transform<String, StructData> {

        @Override
        default Format sourceFormat() {
            return Format.YAML;
        }

        @Override
        default Format targetFormat() {
            return Format.ActiveNode;
        }

        @Override
        StructData apply(String yamlString);
    }

    /**
     * Active.StructData --> StructData
     */
    interface ActiveNodeToActiveNode extends Transform<StructData, StructData> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.ActiveNode;
        }

        @Override
        StructData apply(StructData structData);
    }

    /**
     * Active.StructData --> JSON String
     */
    interface ActiveNodeToJSON extends Transform<StructData, String> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.JSON;
        }

        @Override
        String apply(StructData structData);
    }

    /**
     * Active.StructData --> JSON String
     */
    interface ActiveNodeToJsonRecord extends Transform<StructData, String> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.JsonRecord;
        }

        @Override
        String apply(StructData structData);
    }
    /**
     * Active.StructData --> YAML String
     */
    interface ActiveNodeToYAML extends Transform<StructData, String> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.YAML;
        }

        @Override
        String apply(StructData structData);
    }
    
    /**
     * Active.OpenNode --> JSON String
     */
    interface OpenNodeToJSON extends Transform<OpenNode, String> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.JSON;
        }

        @Override
        String apply(OpenNode openNode);
    }

    /**
     * Active.OpenNode --> JSON String
     */
    interface OpenNodeToYAML extends Transform<OpenNode, String> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.YAML;
        }

        @Override
        String apply(OpenNode openNode);
    }
    
    /**
     * Line (rows) to Tokens (fields)
     *
     *
     */
    interface DSVLineParser extends Transform<String, List<Object>> {

        @Override
        default Format sourceFormat() {
            return Format.Line;
        }

        @Override
        default Format targetFormat() {
            return Format.Tokens;
        }

        @Override
        List<Object> apply(String line);

        static public String getLineParserCSV() {
            return NodeConfig.LINE_PARSER_CSV_PATTERN.asString();
        }
    }

    /**
     * Delimiter Separated Values to OpenNode
     *
     * < This includes JDBC ResultSet >
     *
     */
    interface DSVStringToOpenNode extends Transform<String, OpenNode> {

        @Override
        default Format sourceFormat() {
            return Format.DSV;
        }

        @Override
        default Format targetFormat() {
            return Format.OpenNode;
        }

        @Override
        OpenNode apply(String line);
    }

    /**
     * Delimiter Separated Values to ActiveNode
     *
     * < This includes JDBC ResultSet >
     *
     */
    interface DSVStringToActiveNode extends Transform<String, StructData> {

        @Override
        default Format sourceFormat() {
            return Format.DSV;
        }

        @Override
        default Format targetFormat() {
            return Format.ActiveNode;
        }

        @Override
        StructData apply(String line);
    }

    /**
     * OpenNode to DSV
     *
     */
    interface OpenNodeToDSVString extends Transform<OpenNode, String> {

        @Override
        default Format sourceFormat() {
            return Format.OpenNode;
        }

        @Override
        default Format targetFormat() {
            return Format.DSV;
        }

        @Override
        String apply(OpenNode openNode);
    }

    /**
     * ActiveNode to DSV
     *
     *
     */
    interface StructDataToDSVString extends Transform<StructData, String> {

        @Override
        default Format sourceFormat() {
            return Format.ActiveNode;
        }

        @Override
        default Format targetFormat() {
            return Format.DSV;
        }

        @Override
        String apply(StructData structData);
    }

}
