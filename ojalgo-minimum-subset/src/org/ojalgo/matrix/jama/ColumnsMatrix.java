/*
 * Copyright 1997-2013 Optimatika (www.optimatika.se)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.matrix.jama;

import java.util.Iterator;

import org.ojalgo.access.Access2D;
import org.ojalgo.access.Iterator1D;

abstract class ColumnsMatrix implements Access2D<Double> {

    private final int myColumnLength;
    private final double[][] myColumns;

    public ColumnsMatrix(final int aRowDim, final int aColDim) {

        super();

        final int tmpColumnsCount = aColDim;
        final int tmpColumnLength = aRowDim;

        myColumns = new double[tmpColumnsCount][tmpColumnLength];
        myColumnLength = tmpColumnLength;
    }

    @SuppressWarnings("unused")
    private ColumnsMatrix() {
        this(0, 0);
    }

    public double[] column(final int aCol) {
        return myColumns[aCol];
    }

    public Iterator<double[]> columns() {
        return new Iterator<double[]>() {

            private int myNextCol = 0;

            public boolean hasNext() {
                return myNextCol < myColumns.length;
            }

            public double[] next() {
                return myColumns[myNextCol++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

    }

    @Override
    public double doubleValue(final long index) {
        return myColumns[(int) (index / myColumnLength)][(int) (index % myColumnLength)];
    }

    @Override
    public double doubleValue(final long aRow, final long aCol) {
        return myColumns[(int) aCol][(int) aRow];
    }

    @Override
    public Double get(final long aRow, final long aCol) {
        return myColumns[(int) aCol][(int) aRow];
    }

    @Override
    public int getColDim() {
        return myColumns.length;
    }

    @Override
    public int getRowDim() {
        return myColumnLength;
    }

    public Iterator<Double> iterator() {
        return new Iterator1D<Double>(this);
    }

    public int size() {
        return myColumns.length * myColumnLength;
    }

}
