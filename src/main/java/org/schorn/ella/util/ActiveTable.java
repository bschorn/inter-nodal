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
package org.schorn.ella.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 * @author schorn
 *
 */
public interface ActiveTable {

    static Table table(String name) {
        return new Table(name);
    }

    static class Table implements ActiveTable {

        String name;
        List<Row> rows = new ArrayList<>();
        List<Col> cols = new ArrayList<>();
        Set<Cell> cells = new TreeSet<>();

        Table(String name) {
            this.name = name;
        }

        public Row row() {
            rows.add(new Row(this, rows.size()));
            return rows.get(rows.size() - 1);
        }

        public Col col() {
            cols.add(new Col(this, cols.size()));
            return cols.get(cols.size() - 1);
        }

        public List<Cell> getCells(int startRow, int endRow, int startCol, int endCol) {
            return this.cells.stream()
                    .filter(cell -> cell.row >= startRow && cell.row <= endRow && cell.col >= startCol && cell.col <= endCol)
                    .collect(Collectors.toList());
        }
    }

    static class Cell implements ActiveTable {

        Table table;
        final int row;
        final int col;

        Cell(Table table, int row, int col) {
            this.table = table;
            this.row = row;
            this.col = col;
        }

        public int row() {
            return this.row;
        }

        public int col() {
            return this.col;
        }
    }

    /**
     *
     */
    static class Row implements ActiveTable {

        Table table;
        final int row;

        Row(Table table, int row) {
            this.table = table;
            this.row = row;
        }

        public int pos() {
            return this.row;
        }

        public List<Cell> cells() {
            return this.table.getCells(this.row, this.row, 0, Integer.MAX_VALUE);
        }

        public List<Cell> cells(int start, int end) {
            return this.table.getCells(this.row, this.row, start, end);
        }
    }

    static class Col implements ActiveTable {

        Table table;
        final int col;

        Col(Table table, int col) {
            this.table = table;
            this.col = col;
        }

        public int pos() {
            return this.col;
        }

        public List<Cell> cells() {
            return this.table.getCells(0, Integer.MAX_VALUE, this.col, this.col);
        }

        public List<Cell> cells(int start, int end) {
            return this.table.getCells(start, end, this.col, this.col);
        }
    }

    static int main(String[] args) {
        Table table1 = ActiveTable.table("table1");
        Row row = table1.row();
        Col col = table1.col();

        row.cells();
        col.cells();
        return 0;

    }
}
