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
 /*
 * Copyright (C) 2019 bschorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.schorn.ella.sql;

import org.schorn.ella.node.DataGroup;

/**
 *
 * @author schorn
 *
 */
public enum DB2DataTypes implements DBDataType {
    BOOLEAN(DataGroup.BOOL, 0, true),
    TIME(DataGroup.TIME, 0, true),
    TIMESTAMP(DataGroup.TIMESTAMP, 0, true),
    DATE(DataGroup.DATE, 0, true),
    SMALLINT(DataGroup.NUMBER, 0, false),
    INTEGER(DataGroup.NUMBER, 0, true),
    BIGINT(DataGroup.NUMBER, 0, false),
    DECIMAL(DataGroup.DECIMAL, 0, true),
    REAL(DataGroup.DECIMAL, 0, false),
    DOUBLE(DataGroup.DECIMAL, 0, false);

    DataGroup dataGroup;
    int bytes;
    boolean primary;

    DB2DataTypes(DataGroup dataGroup, int bytes, boolean primary) {
        this.dataGroup = dataGroup;
        this.bytes = bytes;
        this.primary = primary;
    }

    public DataGroup dataGroup() {
        return this.dataGroup;
    }

    public int bytes() {
        return this.bytes;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    /**
     *
     * @param dataGroup
     * @return
     */
    static public DBDataType convert(DataGroup dataGroup) {
        for (DB2DataTypes dbDataType : DB2DataTypes.values()) {
            if (dataGroup.equals(dbDataType.dataGroup) && dbDataType.isPrimary()) {
                return dbDataType;
            }
        }
        return null;
    }
}
