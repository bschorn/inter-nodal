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
package org.schorn.ella.impl.load;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.schorn.ella.load.ActiveTabularLoad.ActiveObjectAssembler;
import org.schorn.ella.load.ActiveTabularLoad.ActiveTypeValue;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;

/**
 *
 * @author schorn
 *
 */
public class ActiveObjectAssemblerImpl implements ActiveObjectAssembler {

    private final Function<ActiveTypeValue, ActiveData> fieldCreator;
    private final Predicate<ActiveData> dataChecker;
    private final Function<List<ActiveData>, ObjectData> objectCreator;
    private final Predicate<ObjectData> objectChecker;
    private final Consumer<ObjectData> submitter;

    public ActiveObjectAssemblerImpl(Function<ActiveTypeValue, ActiveData> fieldCreator,
            Predicate<ActiveData> dataChecker,
            Function<List<ActiveData>, ObjectData> objectCreator,
            Predicate<ObjectData> objectChecker,
            Consumer<ObjectData> submitter
    ) {
        this.fieldCreator = fieldCreator;
        this.dataChecker = dataChecker;
        this.objectCreator = objectCreator;
        this.objectChecker = objectChecker;
        this.submitter = submitter;
    }

    @Override
    public Function<ActiveTypeValue, ActiveData> fieldCreator() {
        return this.fieldCreator;
    }

    @Override
    public Predicate<ActiveData> dataChecker() {
        return this.dataChecker;
    }

    @Override
    public Function<List<ActiveData>, ObjectData> objectCreator() {
        return this.objectCreator;
    }

    @Override
    public Predicate<ObjectData> objectChecker() {
        return this.objectChecker;
    }

    @Override
    public Consumer<ObjectData> submitter() {
        return this.submitter;
    }

}
