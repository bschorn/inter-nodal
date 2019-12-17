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
package org.schorn.ella.node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.schorn.ella.node.ActiveNode.ValueData.PrimitiveData;
import org.schorn.ella.node.ActiveNode.ValueType.PrimitiveType;
import org.schorn.ella.util.StringCached;

/**
 *
 * @author schorn
 *
 */
public interface Primitive {

    Set<Class<? extends PrimitiveType<?>>> PTYPES = new HashSet<>();

    PrimitiveData<?> newInstance(Object value);

    PrimitiveData<?> newInstance();

    /**
     *
     * Built-in Primitive Types
     *
     */
    public enum Type implements Primitive {
        BOOL(Pbool.class),
        BYTE(Pbyte.class),
        SHORT(Pshort.class),
        INTEGER(Pinteger.class),
        LONG(Plong.class),
        DATE(Pdate.class),
        TIME(Ptime.class),
        TIMESTAMP(Ptimestamp.class),
        DECIMAL(Pdecimal.class),
        TEXT(Ptext.class),
        UUID(Puuid.class),
        DAY_OF_WEEK(Pdayofweek.class),;

        PrimitiveType<?> ptype;
        Class<? extends PrimitiveType<?>> primitiveClass;

        Type(Class<? extends PrimitiveType<?>> primitiveClass) {
            this.primitiveClass = primitiveClass;
            try {
                this.ptype = primitiveClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Class<? extends PrimitiveType<?>> primitiveClass() {
            return this.primitiveClass;
        }

        @Override
        public PrimitiveData<?> newInstance(Object value) {
            try {
                return this.ptype.newInstance(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public PrimitiveData<?> newInstance() {
            try {
                return this.ptype.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
	 * TEXT
     */
    static class Ptext implements PrimitiveType<String>, PrimitiveData<String>, Comparable<Ptext> {

        boolean init = false;
        boolean isBlank = false;
        boolean isNull = false;
        StringCached strCached;

        Ptext(String value) {
            this.strCached = new StringCached(value);
            this.init = true;
        }

        Ptext() {
            this.strCached = new StringCached("");
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(this.strCached.toString());
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.strCached = new StringCached(in.readUTF());
            this.init = true;
        }

        @Override
        public String asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return "";
            }
            return this.strCached.toString();
        }

        @Override
        public int bytes() {
            return 4;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.TEXT;
        }

        @Override
        public Class<String> dataClass() {
            return String.class;
        }

        /*
		 * the heavy lifting of comparing strings already
		 * occurred when the string was converted into
		 * an integer, so equality is simply a check
		 * of two integers.
		 * 
         */
        @Override
        public boolean equals(Object object) {
            if (object instanceof Ptext) {
                Ptext that = (Ptext) object;
                return this.strCached == that.strCached;
            }
            return false;
        }

        @Override
        public int compareTo(Ptext that) {
            return this.asLocal().compareTo(that.asLocal());
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<String> newInstance(Object value) {
            if (value instanceof String) {
                return new Ptext((String) value);
            } else {
                return new Ptext(value.toString());
            }
        }

        @Override
        public PrimitiveData<String> newInstance() throws Exception {
            Ptext ptext = new Ptext();
            ptext.isBlank = true;
            return ptext;
        }

        @Override
        public PrimitiveData<String> nullInstance() throws Exception {
            Ptext ptext = new Ptext();
            ptext.isNull = true;
            return ptext;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Ptext.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * DATE
     */
    static class Pdate implements PrimitiveType<LocalDate>, PrimitiveData<LocalDate>, Comparable<Pdate> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        int year;
        short month;
        short day;

        Pdate(LocalDate localDate) {
            this.year = localDate.getYear();
            this.month = (short) localDate.getMonthValue();
            this.day = (short) localDate.getDayOfMonth();
            this.init = true;
        }

        Pdate() {
            LocalDate localDate = LocalDate.now();
            this.year = localDate.getYear();
            this.month = (short) localDate.getMonthValue();
            this.day = (short) localDate.getDayOfMonth();
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.write(this.year);
            out.writeShort(this.month);
            out.writeShort(this.day);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.year = in.read();
            this.month = in.readShort();
            this.day = in.readShort();
            this.init = true;
        }

        @Override
        public LocalDate asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return LocalDate.of(year, month, day);
        }

        @Override
        public int bytes() {
            return 8;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.DATE;
        }

        @Override
        public Class<LocalDate> dataClass() {
            return LocalDate.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pdate) {
                return this.compareTo((Pdate) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Pdate that) {
            int cmp = (this.year - that.year);
            if (cmp == 0) {
                cmp = (this.month - that.month);
                if (cmp == 0) {
                    cmp = (this.day - that.day);
                }
            }
            return cmp;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<LocalDate> newInstance(Object value) throws Exception {
            if (value instanceof LocalDate) {
                return new Pdate((LocalDate) value);
            } else {
                LocalDate convertedValue = NodeProvider.provider().typeConvert(value.getClass(), LocalDate.class, value);
                return new Pdate(convertedValue);
            }
        }

        @Override
        public PrimitiveData<LocalDate> nullInstance() throws Exception {
            Pdate pdate = new Pdate();
            pdate.isNull = true;
            return pdate;
        }

        @Override
        public PrimitiveData<LocalDate> newInstance() throws Exception {
            Pdate pdate = new Pdate();
            pdate.isBlank = true;
            return pdate;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pdate.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * TIME
     */
    static class Ptime implements PrimitiveType<LocalTime>, PrimitiveData<LocalTime>, Comparable<Ptime> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        byte hour;
        byte minute;
        byte second;
        int nano;

        Ptime(LocalTime localTime) {
            this.hour = (byte) localTime.getHour();
            this.minute = (byte) localTime.getMinute();
            this.second = (byte) localTime.getSecond();
            this.nano = localTime.getNano();
            this.init = true;
        }

        Ptime() {
            LocalTime localTime = LocalTime.now();
            this.hour = (byte) localTime.getHour();
            this.minute = (byte) localTime.getMinute();
            this.second = (byte) localTime.getSecond();
            this.nano = localTime.getNano();
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(this.hour);
            out.writeByte(this.minute);
            out.writeByte(this.second);
            out.write(this.nano);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.hour = in.readByte();
            this.minute = in.readByte();
            this.second = in.readByte();
            this.nano = in.read();
            this.init = true;
        }

        @Override
        public LocalTime asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return LocalTime.of(hour, minute, second, nano);
        }

        @Override
        public int bytes() {
            return 7;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<LocalTime> newInstance(Object value) throws Exception {
            if (value instanceof LocalTime) {
                return new Ptime((LocalTime) value);
            } else {
                LocalTime convertedValue = NodeProvider.provider().typeConvert(value.getClass(), LocalTime.class, value);
                return new Ptime(convertedValue);
            }
        }

        @Override
        public PrimitiveData<LocalTime> nullInstance() throws Exception {
            Ptime ptime = new Ptime();
            ptime.isNull = true;
            return ptime;
        }

        @Override
        public PrimitiveData<LocalTime> newInstance() throws Exception {
            Ptime ptime = new Ptime();
            ptime.isBlank = true;
            return ptime;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.TIME;
        }

        /**
         *
         * @return
         */
        @Override
        public Class<LocalTime> dataClass() {
            return LocalTime.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Ptime) {
                return this.compareTo((Ptime) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Ptime that) {
            int cmp = this.hour - that.hour;
            if (cmp == 0) {
                cmp = this.minute - that.minute;
                if (cmp == 0) {
                    cmp = this.second - that.second;
                    if (cmp == 0) {
                        cmp = this.nano - that.nano;
                    }
                }
            }
            return cmp;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Ptime.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * TIMESTAMP
     */
    static class Ptimestamp implements PrimitiveType<LocalDateTime>, PrimitiveData<LocalDateTime>, Comparable<Ptimestamp> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        int year;
        short month;
        short day;
        byte hour;
        byte minute;
        byte second;
        int nano;

        Ptimestamp(LocalDateTime localDateTime) {
            this.year = localDateTime.getYear();
            this.month = (short) localDateTime.getMonthValue();
            this.day = (short) localDateTime.getDayOfMonth();
            this.hour = (byte) localDateTime.getHour();
            this.minute = (byte) localDateTime.getMinute();
            this.second = (byte) localDateTime.getSecond();
            this.nano = localDateTime.getNano();
            this.init = true;
        }

        Ptimestamp() {
            LocalDateTime localDateTime = LocalDateTime.now();
            this.year = localDateTime.getYear();
            this.month = (short) localDateTime.getMonthValue();
            this.day = (short) localDateTime.getDayOfMonth();
            this.hour = (byte) localDateTime.getHour();
            this.minute = (byte) localDateTime.getMinute();
            this.second = (byte) localDateTime.getSecond();
            this.nano = localDateTime.getNano();
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.write(this.year);
            out.writeShort(this.month);
            out.writeShort(this.day);
            out.writeByte(this.hour);
            out.writeByte(this.minute);
            out.writeByte(this.second);
            out.write(this.nano);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.year = in.read();
            this.month = in.readShort();
            this.day = in.readShort();
            this.hour = in.readByte();
            this.minute = in.readByte();
            this.second = in.readByte();
            this.nano = in.read();
            this.init = true;
        }

        @Override
        public LocalDateTime asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return LocalDateTime.of(this.year, this.month, this.day, this.hour, this.minute, this.second, this.nano);
        }

        @Override
        public int bytes() {
            return 15;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<LocalDateTime> newInstance(Object value) throws Exception {
            if (value instanceof LocalDateTime) {
                return new Ptimestamp((LocalDateTime) value);
            } else {
                LocalDateTime convertedValue = NodeProvider.provider().typeConvert(value.getClass(), LocalDateTime.class, value);
                return new Ptimestamp(convertedValue);
            }
        }

        @Override
        public PrimitiveData<LocalDateTime> nullInstance() throws Exception {
            Ptimestamp ptimestamp = new Ptimestamp();
            ptimestamp.isNull = true;
            return ptimestamp;
        }

        @Override
        public PrimitiveData<LocalDateTime> newInstance() throws Exception {
            Ptimestamp ptimestamp = new Ptimestamp(LocalDateTime.now());
            //ptimestamp.isBlank = true;
            return ptimestamp;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.TIMESTAMP;
        }

        @Override
        public Class<LocalDateTime> dataClass() {
            return LocalDateTime.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Ptimestamp) {
                return this.compareTo((Ptimestamp) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Ptimestamp that) {
            int cmp = (this.year - that.year);
            if (cmp == 0) {
                cmp = (this.month - that.month);
                if (cmp == 0) {
                    cmp = (this.day - that.day);
                    if (cmp == 0) {
                        cmp = this.hour - that.hour;
                        if (cmp == 0) {
                            cmp = this.minute - that.minute;
                            if (cmp == 0) {
                                cmp = this.second - that.second;
                                if (cmp == 0) {
                                    cmp = this.nano - that.nano;
                                }
                            }
                        }
                    }
                }
            }
            return cmp;
        }

        /**
         *
         * @return
         */
        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Ptimestamp.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * SHORT
     */
    static class Pshort implements PrimitiveType<Short>, PrimitiveData<Short>, Comparable<Pshort> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        short val;

        Pshort(Short shortValue) {
            this.val = shortValue.shortValue();
            this.init = true;
        }

        Pshort() {
            this.val = 0;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeShort(this.val);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.val = in.readShort();
            this.init = true;
        }

        @Override
        public Short asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return this.val;
        }

        @Override
        public int bytes() {
            return 2;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<Short> newInstance(Object value) throws Exception {
            if (value instanceof Short) {
                return new Pshort((Short) value);
            } else {
                BigInteger convertedValue = NodeProvider.provider().typeConvert(value.getClass(), BigInteger.class, value);
                return new Pshort(convertedValue.shortValue());
            }
        }

        @Override
        public PrimitiveData<Short> newInstance() throws Exception {
            Pshort pshort = new Pshort();
            pshort.isBlank = true;
            return pshort;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.NUMBER;
        }

        @Override
        public Class<Short> dataClass() {
            return Short.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pshort) {
                return this.compareTo((Pshort) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Pshort that) {
            int cmp = this.val - that.val;
            return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<Short> nullInstance() throws Exception {
            Pshort pshort = new Pshort();
            pshort.isNull = true;
            return pshort;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pshort.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
     * INTEGER
     */
    static class Pinteger implements PrimitiveType<Integer>, PrimitiveData<Integer>, Comparable<Pinteger> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        int val;

        Pinteger(Integer intValue) {
            this.val = intValue.intValue();
            this.init = true;
        }

        Pinteger() {
            this.val = 0;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(this.val);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.val = in.readInt();
            this.init = true;
        }

        @Override
        public Integer asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return this.val;
        }

        @Override
        public int bytes() {
            return 4;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<Integer> newInstance(Object value) throws Exception {
            if (value instanceof Integer) {
                return new Pinteger((Integer) value);
            } else {
                BigInteger convertedValue = NodeProvider.provider().typeConvert(value.getClass(), BigInteger.class, value);
                return new Pinteger(convertedValue.intValue());
            }
        }

        /**
         *
         * @return
         * @throws Exception
         */
        @Override
        public PrimitiveData<Integer> newInstance() throws Exception {
            Pinteger pinteger = new Pinteger();
            pinteger.isBlank = true;
            return pinteger;
        }

        /**
         *
         * @return
         */
        @Override
        public DataGroup dataGroup() {
            return DataGroup.NUMBER;
        }

        @Override
        public Class<Integer> dataClass() {
            return Integer.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pinteger) {
                return this.compareTo((Pinteger) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Pinteger that) {
            long cmp = this.val - that.val;
            return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<Integer> nullInstance() throws Exception {
            Pinteger pinteger = new Pinteger();
            pinteger.isNull = true;
            return pinteger;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pinteger.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * LONG
     */
    static class Plong implements PrimitiveType<Long>, PrimitiveData<Long>, Comparable<Plong> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        long val;

        Plong(Long longValue) {
            this.val = longValue.longValue();
            this.init = true;
        }

        Plong() {
            this.val = 0L;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeLong(this.val);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.val = in.readLong();
            this.init = true;
        }

        /**
         *
         * @return
         */
        @Override
        public Long asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return this.val;
        }

        @Override
        public int bytes() {
            return 8;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<Long> newInstance(Object value) throws Exception {
            if (value instanceof Long) {
                return new Plong((Long) value);
            } else {
                BigInteger convertedValue = NodeProvider.provider().typeConvert(value.getClass(), BigInteger.class, value);
                return new Plong(convertedValue.longValue());
            }
        }

        @Override
        public PrimitiveData<Long> newInstance() throws Exception {
            Plong plong = new Plong();
            plong.isBlank = true;
            return plong;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.NUMBER;
        }

        @Override
        public Class<Long> dataClass() {
            return Long.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Plong) {
                return this.compareTo((Plong) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Plong that) {
            long cmp = this.val - that.val;
            return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<Long> nullInstance() throws Exception {
            Plong plong = new Plong();
            plong.isNull = true;
            return plong;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Plong.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * DECIMAL
	 * -9,223,372,036,854,775,808
	 *       -922,337,203,685,477.5808
	 *            -92,233,720,368.54775808
     */
    static class Pdecimal implements PrimitiveType<BigDecimal>, PrimitiveData<BigDecimal>, Comparable<Pdecimal> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        short size;
        char[] cval;

        Pdecimal(BigDecimal bigDecimal) {
            if (bigDecimal != null) {
                this.cval = bigDecimal.toPlainString().toCharArray();
                this.size = (short) this.cval.length;
            } else {
                this.cval = (new String("0.0")).toCharArray();
                this.size = (short) this.cval.length;
            }
            this.init = true;
        }

        Pdecimal() {
            this.cval = (new String("0.0")).toCharArray();
            this.size = (short) this.cval.length;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeShort(this.size);
            out.writeChars(new String(this.cval));
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.size = in.readShort();
            this.cval = new char[this.size];
            for (short i = 0; i < this.size; i += 1) {
                this.cval[i] = in.readChar();
            }
            this.init = true;
        }

        @Override
        public BigDecimal asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return new BigDecimal(this.cval, 0, this.size);
        }

        @Override
        public int bytes() {
            return 2 + (2 * this.cval.length);
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<BigDecimal> newInstance(Object value) throws Exception {
            if (value instanceof BigDecimal) {
                return new Pdecimal((BigDecimal) value);
            } else {
                BigDecimal convertedValue = NodeProvider.provider().typeConvert(value.getClass(), BigDecimal.class, value);
                return new Pdecimal(convertedValue);
            }
        }

        @Override
        public PrimitiveData<BigDecimal> newInstance() throws Exception {
            Pdecimal pdecimal = new Pdecimal();
            pdecimal.isBlank = true;
            return pdecimal;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.DECIMAL;
        }

        @Override
        public Class<BigDecimal> dataClass() {
            return BigDecimal.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pdecimal) {
                return this.compareTo((Pdecimal) object) == 0;
            }
            return false;
        }

        /*
		 * BigDecimal's compareTo is non-trivial, so we
		 * must 'box' that value back into BigDecimal and use
		 * it's compareTo.
		 * 
         */
        @Override
        public int compareTo(Pdecimal that) {
            return this.asLocal().compareTo(that.asLocal());
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<BigDecimal> nullInstance() throws Exception {
            Pdecimal pdecimal = new Pdecimal();
            pdecimal.isNull = true;
            return pdecimal;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pdecimal.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * BOOL
     */
    static class Pbool implements PrimitiveType<Boolean>, PrimitiveData<Boolean>, Comparable<Pbool> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        byte flag;

        Pbool(Boolean boolValue) {
            this.flag = (byte) (boolValue ? 1 : 0);
            this.init = true;
        }

        Pbool() {
            this.flag = 0;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.write(this.flag);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.flag = in.readByte();
            this.init = true;
        }

        @Override
        public Boolean asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return Boolean.valueOf(this.flag == 0 ? false : true);
        }

        @Override
        public int bytes() {
            return 1;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<Boolean> newInstance(Object value) throws Exception {
            if (value instanceof Boolean) {
                return new Pbool((Boolean) value);
            } else if (value instanceof Number) {
                return new Pbool(Boolean.valueOf(((Number) value).longValue() != 0));
            } else {
                Boolean convertedValue = NodeProvider.provider().typeConvert(value.getClass(), Boolean.class, value);
                return new Pbool(convertedValue);
            }
        }

        @Override
        public PrimitiveData<Boolean> newInstance() throws Exception {
            Pbool pbool = new Pbool();
            pbool.isBlank = true;
            return pbool;
        }

        /**
         *
         * @return
         */
        @Override
        public DataGroup dataGroup() {
            return DataGroup.BOOL;
        }

        @Override
        public Class<Boolean> dataClass() {
            return Boolean.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pbool) {
                return compareTo((Pbool) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Pbool that) {
            return (this.flag == that.flag) ? 0 : (this.flag == 1 ? 1 : -1);
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<Boolean> nullInstance() throws Exception {
            Pbool pbool = new Pbool();
            pbool.isNull = true;
            return pbool;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        /**
         *
         * @return
         */
        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pbool.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * BYTE
     */
    static class Pbyte implements PrimitiveType<Byte>, PrimitiveData<Byte>, Comparable<Pbyte> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        byte val;

        Pbyte(Byte byteValue) {
            this.val = byteValue.byteValue();
            this.init = true;
        }

        Pbyte() {
            this.val = 0;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.write(this.val);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.val = in.readByte();
            this.init = true;
        }

        @Override
        public Byte asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return Byte.valueOf(this.val);
        }

        @Override
        public int bytes() {
            return 1;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<Byte> newInstance(Object value) throws Exception {
            if (value instanceof Byte) {
                return new Pbyte((Byte) value);
            } else if (value instanceof Number) {
                return new Pbyte(((Number) value).byteValue());
            } else {
                Byte convertedValue = NodeProvider.provider().typeConvert(value.getClass(), Byte.class, value);
                return new Pbyte(convertedValue);
            }
        }

        @Override
        public PrimitiveData<Byte> newInstance() throws Exception {
            Pbyte pbyte = new Pbyte();
            pbyte.isBlank = true;
            return pbyte;
        }

        /**
         *
         * @return
         */
        @Override
        public DataGroup dataGroup() {
            return DataGroup.BYTE;
        }

        @Override
        public Class<Byte> dataClass() {
            return Byte.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pbyte) {
                return compareTo((Pbyte) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Pbyte that) {
            return this.val - that.val;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<Byte> nullInstance() throws Exception {
            Pbyte pbyte = new Pbyte();
            pbyte.isNull = true;
            return pbyte;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pbyte.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * UUID
     */
    static public class Puuid implements PrimitiveType<UUID>, PrimitiveData<UUID>, Comparable<Puuid> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        long mostSigBits;
        long leastSigBits;

        Puuid(UUID uuid) {
            this.mostSigBits = uuid.getMostSignificantBits();
            this.leastSigBits = uuid.getLeastSignificantBits();
            this.init = true;
        }

        Puuid() {
            this.mostSigBits = 0L;
            this.leastSigBits = 0L;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeLong(this.mostSigBits);
            out.writeLong(this.leastSigBits);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.mostSigBits = in.readLong();
            this.leastSigBits = in.readLong();
            this.init = true;
        }

        @Override
        public UUID asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return new UUID(this.mostSigBits, this.leastSigBits);
        }

        @Override
        public int bytes() {
            return 16;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        /**
         *
         * @param value
         * @return
         * @throws Exception
         */
        @Override
        public PrimitiveData<UUID> newInstance(Object value) throws Exception {
            if (value instanceof UUID) {
                return new Puuid((UUID) value);
            } else {
                UUID convertedValue = NodeProvider.provider().typeConvert(value.getClass(), UUID.class, value);
                return new Puuid(convertedValue);
            }
        }

        @Override
        public PrimitiveData<UUID> newInstance() throws Exception {
            Puuid puuid = new Puuid(UUID.randomUUID());
            //puuid.isBlank = true;
            return puuid;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.UUID;
        }

        @Override
        public Class<UUID> dataClass() {
            return UUID.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Puuid) {
                return this.compareTo((Puuid) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Puuid that) {
            return (this.mostSigBits < that.mostSigBits ? -1
                    : (this.mostSigBits > that.mostSigBits ? 1
                            : (this.leastSigBits < that.leastSigBits ? -1
                                    : (this.leastSigBits > that.leastSigBits ? 1
                                            : 0))));
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public PrimitiveData<UUID> nullInstance() throws Exception {
            Puuid puuid = new Puuid();
            puuid.isNull = true;
            return puuid;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Puuid.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }

    /*
	 * DAY_OF_WEEK
     */
    static class Pdayofweek implements PrimitiveType<DayOfWeek>, PrimitiveData<DayOfWeek>, Comparable<Pdayofweek> {

        boolean init = false;
        boolean isNull = false;
        boolean isBlank = false;
        int val;

        Pdayofweek(DayOfWeek dayOfWeek) {
            this.val = dayOfWeek.getValue();
            this.init = true;
        }

        Pdayofweek() {
            this.val = 0;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(this.val);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.val = in.readInt();
            this.init = true;
        }

        @Override
        public DayOfWeek asLocal() {
            if (this.isNull) {
                return null;
            }
            if (this.isBlank) {
                return null;
            }
            return DayOfWeek.of(this.val);
        }

        @Override
        public int bytes() {
            return 4;
        }

        @Override
        public boolean initialized() {
            return this.init;
        }

        @Override
        public PrimitiveData<DayOfWeek> newInstance(Object value) throws Exception {
            if (value instanceof DayOfWeek) {
                return new Pdayofweek((DayOfWeek) value);
            } else {
                DayOfWeek convertedValue = NodeProvider.provider().typeConvert(value.getClass(), DayOfWeek.class, value);
                return new Pdayofweek(convertedValue);
            }
        }

        @Override
        public PrimitiveData<DayOfWeek> newInstance() throws Exception {
            Pdayofweek pdayofweek = new Pdayofweek();
            pdayofweek.isBlank = true;
            return pdayofweek;
        }

        @Override
        public DataGroup dataGroup() {
            return DataGroup.NUMBER;
        }

        @Override
        public Class<DayOfWeek> dataClass() {
            return DayOfWeek.class;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Pdayofweek) {
                return this.compareTo((Pdayofweek) object) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Pdayofweek that) {
            long cmp = this.val - that.val;
            return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        /**
         *
         * @return
         * @throws Exception
         */
        @Override
        public PrimitiveData<DayOfWeek> nullInstance() throws Exception {
            Pdayofweek pdayofweek = new Pdayofweek();
            pdayofweek.isNull = true;
            return pdayofweek;
        }

        @Override
        public boolean isNull() {
            return this.isNull;
        }

        @Override
        public boolean isBlank() {
            return this.isBlank;
        }

        @Override
        public int compareToOther(PrimitiveData<?> other) {
            if (other.dataClass().equals(this.dataClass())) {
                return this.compareTo(Pdayofweek.class.cast(other));
            }
            return this.asLocal().toString().compareTo(other.asLocal().toString());
        }
    }
}
