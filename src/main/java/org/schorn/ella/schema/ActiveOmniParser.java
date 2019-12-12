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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.schorn.ella.antlr.OmniLexer;
import org.schorn.ella.antlr.OmniParser;

/**
 *
 * @author bschorn
 */
public class ActiveOmniParser {

    public void parse(String code) {
        ActiveOmniParserListener listener = new ActiveOmniParserListener();
        OmniLexer lexer = new OmniLexer(CharStreams.fromString(code));
        OmniParser parser = new OmniParser(new CommonTokenStream(lexer));
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parser.def());
        //ActiveOmni schema = listener.schema();
        try {
            //schema.build();
            //System.out.println(schema.toString());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
