/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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
package org.schorn.ella.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.schorn.ella.antlr.SpecLexer;
import org.schorn.ella.antlr.SpecParser;

/**
 *
 * @author bschorn
 */
public class ActiveSchemaParser {

    private Map schemaMap = null;
    private String context = "model";
    private String jsonMeta = null;

    public ActiveSchemaParser(String context) {
        this.context = context != null ? context : this.context;
    }

    public String getMeta() {
        return this.jsonMeta;
    }

    public void writeMeta(String fileName) throws Exception {
        Path path = Paths.get(fileName);
        Path writePath = Files.write(path, jsonMeta.getBytes());
        System.out.println(String.format("Write to file: %s", writePath.toAbsolutePath().toString()));
    }

    public void parse(String code) {
        ActiveSchemaParserListener listener = new ActiveSchemaParserListener();
        SpecLexer lexer = new SpecLexer(CharStreams.fromString(code));
        SpecParser parser = new SpecParser(new CommonTokenStream(lexer));
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parser.def());
        ActiveSchema schema = listener.schema();
        schema.setContext(this.context);
        try {
            schema.build();
            //System.out.println(schema.toString());
            Map map = schema.metaMap();
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            this.jsonMeta = objectMapper.writeValueAsString(map);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    static public ActiveSchemaParser compile(String context, String specFile) throws Exception {
        //String specFile = "D:/Users/bschorn/documents/GitHub/internodal/src/main/resources/model/jane_core.spec";
        //String context = "jane_bank";
        //String metaFile = String.format("D:/Users/bschorn/documents/GitHub/internodal/src/main/resources/meta/%s.meta.json", context);
        Path path = Paths.get(specFile);
        if (!Files.exists(path)) {
            String user_dir = System.getProperty("user.dir");
            throw new Exception(String.format("The spec file: '%s' was not found starting at: '%s'", specFile, user_dir));
        }
        try (Stream<String> lines = Files.lines(path)) {
            String data = lines.collect(Collectors.joining("\n"));
            ActiveSchemaParser parser = new ActiveSchemaParser(context);
            parser.parse(data);
            //parser.writeMeta(metaFile);
            return parser;
        }
    }
}
