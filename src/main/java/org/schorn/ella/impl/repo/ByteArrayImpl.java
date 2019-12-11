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
package org.schorn.ella.impl.repo;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.schorn.ella.repo.ByteArray;

/**
 *
 * @author schorn
 *
 */
public class ByteArrayImpl<T> implements ByteArray<T> {

    int offset;
    int capacity;
    byte[] data;
    ReadWrite<T> readWrite;

    ByteArrayImpl(int offset, int capacity) {
        this.offset = offset;
        this.capacity = capacity;
        this.data = new byte[offset * capacity];
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(this.data);
        out.flush();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.read(this.data);
    }

    @Override
    public int write(T t) {
        this.readWrite.write(t);
        return 0;
    }

    @Override
    public T read(int index) {
        int start_pos = this.offset * index;
        byte[] buf = new byte[this.offset];
        for (int i = 0; i < offset; i += 1) {
            buf[i] = this.data[start_pos + i];
        }
        return (T) this.readWrite.read(buf);
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public int offset() {
        return this.offset;
    }

    @Override
    public void compress() {
    }

}
