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
package org.schorn.ella.impl.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.sql.RDBMS;
import org.schorn.ella.sql.RDBMS.Commands;
import org.schorn.ella.sql.RDBMS.Custom;
import org.schorn.ella.sql.RDBMS.JDBCExecuteStep;
import org.schorn.ella.sql.RDBMS.PhrasalTemplates;
import org.schorn.ella.sql.RDBMS.SQLCompound;
import org.schorn.ella.sql.RDBMS.SQLDialect;
import org.schorn.ella.sql.RDBMS.SQLDirective;
import org.schorn.ella.sql.RDBMS.SQLInterpreter;
import org.schorn.ella.sql.RDBMS.SQLProgramCode;
import org.schorn.ella.sql.RDBMS.SQLProgramData;
import org.schorn.ella.sql.RDBMS.SQLProgramOutput;
import org.schorn.ella.sql.RDBMS.TemplateTags;
import org.schorn.ella.sql.RDBMS.TemplateTagsTransducer;

/**
 *
 * @author schorn
 *
 */
public class SQLInterpreterImpl extends AbstractContextual implements SQLInterpreter {

    private static final Logger LGR = LoggerFactory.getLogger(SQLInterpreterImpl.class);

    /**
     * Program (Output)
     *
     */
    static public class SQLProgramOutputImpl extends AbstractContextual implements SQLProgramOutput {

        private final List<JDBCExecuteStep> steps;

        protected SQLProgramOutputImpl(AppContext context) {
            super(context);
            this.steps = new ArrayList<>();
        }

        @Override
        public SQLProgramOutput renew(Object... params) {
            return new SQLProgramOutputImpl(this.context());
        }

        void add(JDBCExecuteStep step) {
            this.steps.add(step);
        }

        @Override
        public List<JDBCExecuteStep> steps() {
            return this.steps;
        }

    }

    /**
     * Ctor
     *
     * @param context
     */
    private final TemplateTagsTransducer transducer;

    protected SQLInterpreterImpl(AppContext context) {
        super(context);
        this.transducer = RDBMS.TemplateTagsTransducer.get(this.context());
    }

    /**
     * This is using the call stack to unset flags, but the flags must be added
     * while being used as call a parameter.
     *
     * func(state.add(Flag.DATA), x, y);
     *
     * The callee will get a new State instance with DATA flag added. When call
     * returns the original State instance will still be what it was before DATA
     * was added.
     */
    static class State {

        enum Flag {
            REVERSE,
            DATA,
            MERGE,
            UNIT,;
        }
        private final boolean[] flags;
        private final SQLProgramOutput output;

        private State(SQLProgramOutput output, boolean[] flags) {
            this.output = output;
            this.flags = flags;
        }

        static State create(AppContext context) {
            return new State(SQLProgramOutput.create(context), new boolean[Flag.values().length]);
        }

        State call(Flag... addFlags) {
            boolean[] flags = new boolean[Flag.values().length];
            System.arraycopy(this.flags, 0, flags, 0, Flag.values().length);
            for (Flag addFlag : addFlags) {
                flags[addFlag.ordinal()] = true;
            }
            return new State(this.output, flags);
        }

        void on(Flag flag) {
            this.flags[flag.ordinal()] = true;
        }

        void off(Flag flag) {
            this.flags[flag.ordinal()] = false;
        }

        boolean is(Flag flag) {
            return this.flags[flag.ordinal()];
        }

        SQLProgramOutput output() {
            return this.output;
        }

        void addStep(JDBCExecuteStep step) {
            ((SQLProgramOutputImpl) this.output).add(step);
        }
    }

    /**
     *
     * @param objectData
     * @return
     */
    @Override
    public SQLProgramOutput compile(SQLProgramCode sqlCode, SQLProgramData sqlData) {
        State state = State.create(this.context());
        compile0(state, sqlData, sqlCode.dialect(), sqlCode.directives());
        return state.output();
    }

    /*
	 * [Type]
     */
    protected List<String> singleLevel(SQLProgramData sqlData, SQLDialect dialect,
            PhrasalTemplates typeTemplate) {
        List<String> statements = new ArrayList<>();
        for (ObjectType objectType : sqlData.orderedTypes()) {
            String typeSQL = typeTemplate.sql(dialect);
            for (TemplateTags templateTag : TemplateTags.values()) {
                typeSQL = this.transducer.transduce(templateTag, typeSQL, objectType);
            }
            statements.add(typeSQL);
        }
        return statements;
    }

    /*
	 * [Type->[Data]]
     */
    protected List<String> multiLevel(SQLProgramData sqlData, SQLDialect dialect,
            PhrasalTemplates typeTemplate, PhrasalTemplates dataTemplate, Commands glueCommand) {
        List<String> statements = new ArrayList<>();
        for (ObjectType objectType : sqlData.orderedTypes()) {
            String typeSQL = typeTemplate.sql(dialect);
            for (TemplateTags templateTag : TemplateTags.values()) {
                typeSQL = this.transducer.transduce(templateTag, typeSQL, objectType);
            }
            String glueString = String.format("\n%s\n", glueCommand.name());
            String header = String.format("%s\n", typeSQL.toString());
            StringJoiner statementJoiner = new StringJoiner(glueString, header, "\n");
            for (ObjectData objectData : sqlData.getOrderedDataList(objectType)) {
                String dataSQL = dataTemplate.sql(dialect);
                for (TemplateTags templateTag : TemplateTags.values()) {
                    dataSQL = this.transducer.transduce(templateTag, dataSQL, objectData);
                }
                statementJoiner.add(dataSQL);
            }
            statements.add(statementJoiner.toString());
        }
        return statements;
    }

    /*
	 * Combine
     */
    protected void combine(State state, SQLProgramData data, SQLDialect dialect, SQLDirective[] directives) {
        PhrasalTemplates typeTemplate = null;
        PhrasalTemplates dataTemplate = null;
        State callState = state;
        Commands glueCommand = null;

        for (SQLDirective directive : directives) {
            switch (directive.directiveType()) {
                case STATEMENT:
                    if (typeTemplate == null) {
                        typeTemplate = (PhrasalTemplates) directive;
                    } else if (dataTemplate == null) {
                        dataTemplate = (PhrasalTemplates) directive;
                    }
                    break;
                case CUSTOM:
                    Custom custom = (Custom) directive;
                    if (custom == Custom.DATA_LEVEL) {
                        callState = state.call(State.Flag.DATA);
                    }
                    break;
                case COMMAND:
                    if (glueCommand == null) {
                        glueCommand = (Commands) directive;
                    }
                    break;
                default:
                    LGR.error("Unexpected Directive '{}' at this position in the directives list",
                            directive.directiveType().name());
                    break;
            }
        }
        if (callState.is(State.Flag.DATA)) {
            List<String> statements = multiLevel(data, dialect, typeTemplate, dataTemplate, glueCommand);
            statements.stream().forEach(statement -> state.addStep(JDBCExecuteStep.StepType.EXECUTE_UPDATE.createStep(this.context(), statement)));
        } else {
            LGR.error("what is the use case?");
        }
    }

    /*
	 * 
     */
    protected void transaction(State state, SQLProgramData data, SQLDialect dialect, SQLDirective[] directives) {
        state.addStep(JDBCExecuteStep.StepType.SET_SAVEPOINT.createStep(this.context()));
        compile0(state.call(State.Flag.UNIT), data, dialect, directives);
        state.addStep(JDBCExecuteStep.StepType.COMMIT.createStep(this.context()));
    }

    /*
	 * compile
     */
    protected void compile0(State state, SQLProgramData data, SQLDialect dialect, SQLDirective[] directives) {
        for (RDBMS.SQLDirective directive : directives) {
            switch (directive.directiveType()) {
                case STATEMENT: {
                    List<String> statements = singleLevel(data, dialect, (PhrasalTemplates) directive);
                    statements.stream().forEach(statement -> state.addStep(JDBCExecuteStep.StepType.EXECUTE_UPDATE.createStep(this.context(), statement)));
                    break;
                }
                case COMBINE: {
                    combine(state, data, dialect, ((SQLCompound) directive).directives());
                    break;
                }
                case TRANSACTION: {
                    transaction(state, data, dialect, ((SQLCompound) directive).directives());
                    break;
                }
                default:
                    LGR.error("Unexpected Directive '{}' at this position in the directives list",
                            directive.directiveType().name());
                    break;
            }
        }
    }
}
