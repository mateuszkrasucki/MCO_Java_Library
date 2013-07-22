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
package org.ojalgo.array;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.ojalgo.access.Access1D;
import org.ojalgo.access.Access2D;
import org.ojalgo.access.Iterator1D;
import org.ojalgo.function.BinaryFunction;
import org.ojalgo.function.ParameterFunction;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.aggregator.AggregatorFunction;
import org.ojalgo.matrix.store.BigDenseStore;
import org.ojalgo.matrix.store.ComplexDenseStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.random.RandomNumber;
import org.ojalgo.scalar.ComplexNumber;
import org.ojalgo.scalar.Scalar;

/**
 * Array2D
 *
 * @author apete
 */
public final class Array2D<N extends Number> implements Access2D<N>, Access2D.Elements, Access2D.Fillable<N>, Access2D.Modifiable<N>, Access2D.Visitable<N>, Serializable {

    public static final Access2D.Factory<Array2D<BigDecimal>> BIG = new Access2D.Factory<Array2D<BigDecimal>>() {

        public Array2D<BigDecimal> columns(final Access1D<?>... source) {
            return BigDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<BigDecimal> columns(final double[]... source) {
            return BigDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<BigDecimal> columns(final List<? extends Number>... source) {
            return BigDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<BigDecimal> columns(final Number[]... source) {
            return BigDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<BigDecimal> copy(final Access2D<?> source) {
            return BigDenseStore.FACTORY.copy(source).asArray2D();
        }

        public Array2D<BigDecimal> makeEye(final long rows, final long columns) {
            return BigDenseStore.FACTORY.makeEye(rows, columns).asArray2D();
        }

        public Array2D<BigDecimal> makeRandom(final long rows, final long columns, final RandomNumber distribution) {
            return BigDenseStore.FACTORY.makeRandom(rows, columns, distribution).asArray2D();
        }

        public Array2D<BigDecimal> makeZero(final long rows, final long columns) {
            return BigDenseStore.FACTORY.makeZero(rows, columns).asArray2D();
        }

        public Array2D<BigDecimal> rows(final Access1D<?>... source) {
            return BigDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<BigDecimal> rows(final double[]... source) {
            return BigDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<BigDecimal> rows(final List<? extends Number>... source) {
            return BigDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<BigDecimal> rows(final Number[]... source) {
            return BigDenseStore.FACTORY.rows(source).asArray2D();
        }

    };

    public static final Factory<Array2D<ComplexNumber>> COMPLEX = new Factory<Array2D<ComplexNumber>>() {

        public Array2D<ComplexNumber> columns(final Access1D<?>... source) {
            return ComplexDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<ComplexNumber> columns(final double[]... source) {
            return ComplexDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<ComplexNumber> columns(final List<? extends Number>... source) {
            return ComplexDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<ComplexNumber> columns(final Number[]... source) {
            return ComplexDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<ComplexNumber> copy(final Access2D<?> source) {
            return ComplexDenseStore.FACTORY.copy(source).asArray2D();
        }

        public Array2D<ComplexNumber> makeEye(final long rows, final long columns) {
            return ComplexDenseStore.FACTORY.makeEye(rows, columns).asArray2D();
        }

        public Array2D<ComplexNumber> makeRandom(final long rows, final long columns, final RandomNumber distribution) {
            return ComplexDenseStore.FACTORY.makeRandom(rows, columns, distribution).asArray2D();
        }

        public Array2D<ComplexNumber> makeZero(final long rows, final long columns) {
            return ComplexDenseStore.FACTORY.makeZero(rows, columns).asArray2D();
        }

        public Array2D<ComplexNumber> rows(final Access1D<?>... source) {
            return ComplexDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<ComplexNumber> rows(final double[]... source) {
            return ComplexDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<ComplexNumber> rows(final List<? extends Number>... source) {
            return ComplexDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<ComplexNumber> rows(final Number[]... source) {
            return ComplexDenseStore.FACTORY.rows(source).asArray2D();
        }

    };

    public static final Access2D.Factory<Array2D<Double>> PRIMITIVE = new Access2D.Factory<Array2D<Double>>() {

        public Array2D<Double> columns(final Access1D<?>... source) {
            return PrimitiveDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<Double> columns(final double[]... source) {
            return PrimitiveDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<Double> columns(final List<? extends Number>... source) {
            return PrimitiveDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<Double> columns(final Number[]... source) {
            return PrimitiveDenseStore.FACTORY.columns(source).asArray2D();
        }

        public Array2D<Double> copy(final Access2D<?> source) {
            return PrimitiveDenseStore.FACTORY.copy(source).asArray2D();
        }

        public Array2D<Double> makeEye(final long rows, final long columns) {
            return PrimitiveDenseStore.FACTORY.makeEye(rows, columns).asArray2D();
        }

        public Array2D<Double> makeRandom(final long rows, final long columns, final RandomNumber distribution) {
            return PrimitiveDenseStore.FACTORY.makeRandom(rows, columns, distribution).asArray2D();
        }

        public Array2D<Double> makeZero(final long rows, final long columns) {
            return PrimitiveDenseStore.FACTORY.makeZero(rows, columns).asArray2D();
        }

        public Array2D<Double> rows(final Access1D<?>... source) {
            return PrimitiveDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<Double> rows(final double[]... source) {
            return PrimitiveDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<Double> rows(final List<? extends Number>... source) {
            return PrimitiveDenseStore.FACTORY.rows(source).asArray2D();
        }

        public Array2D<Double> rows(final Number[]... source) {
            return PrimitiveDenseStore.FACTORY.rows(source).asArray2D();
        }

    };

    private final int myColDim;

    private final BasicArray<N> myDelegate;
    private final int myRowDim;

    @SuppressWarnings("unused")
    private Array2D() {
        this(null, 0, 0);
    }

    Array2D(final BasicArray<N> aDelegate, final int aRowDim, final int aColDim) {

        super();

        myDelegate = aDelegate;

        myRowDim = aRowDim;
        myColDim = aColDim;
    }

    /**
     * Flattens this two dimensional array to a one dimensional array.
     * The (internal/actual) array is not copied, it is just accessed through
     * a different adaptor.
     */
    public Array1D<N> asArray1D() {
        return myDelegate.asArray1D();
    }

    public long count() {
        return myDelegate.count();
    }

    public long countColumns() {
        return myRowDim;
    }

    public long countRows() {
        return myColDim;
    }

    public double doubleValue(final long index) {
        return myDelegate.doubleValue(index);
    }

    public double doubleValue(final long row, final long column) {
        return myDelegate.doubleValue(row + (column * myRowDim));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Array2D) {
            final Array2D<N> tmpObj = (Array2D<N>) obj;
            return (myRowDim == tmpObj.getRowDim()) && (myColDim == tmpObj.getColDim()) && myDelegate.equals(tmpObj.getDelegate());
        } else {
            return super.equals(obj);
        }
    }

    public void exchangeColumns(final int aColA, final int aColB) {
        myDelegate.exchange(aColA * myRowDim, aColB * myRowDim, 1, myRowDim);
    }

    public void exchangeRows(final int aRowA, final int aRowB) {
        myDelegate.exchange(aRowA, aRowB, myRowDim, myColDim);
    }

    public void fillAll(final N value) {
        myDelegate.fill(0, myDelegate.length, 1, value);
    }

    public void fillColumn(final long row, final long column, final N value) {
        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (myRowDim + (column * myRowDim));
        myDelegate.fill(tmpFirst, tmpLimit, 1, value);
    }

    public void fillDiagonal(final long row, final long column, final N value) {

        final long tmpCount = Math.min(myRowDim - row, myColDim - column);

        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (row + tmpCount + ((column + tmpCount) * myRowDim));
        final int tmpStep = 1 + myRowDim;

        myDelegate.fill(tmpFirst, tmpLimit, tmpStep, value);
    }

    public void fillMatching(final Array2D<N> aLeftArg, final BinaryFunction<N> aFunc, final Array2D<N> aRightArg) {
        myDelegate.fill(0, myDelegate.length, aLeftArg.getDelegate(), aFunc, aRightArg.getDelegate());
    }

    public void fillRange(final long first, final long limit, final N value) {
        myDelegate.fill((int) first, (int) limit, 1, value);
    }

    public void fillRow(final long row, final long column, final N value) {
        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (row + (myColDim * myRowDim));
        myDelegate.fill(tmpFirst, tmpLimit, myRowDim, value);
    }

    public N get(final long index) {
        return myDelegate.get(index);
    }

    public N get(final long row, final long column) {
        return myDelegate.get(row + (column * myRowDim));
    }

    public int getColDim() {
        return myColDim;
    }

    public int getIndexOfLargestInColumn(final int row, final int column) {
        return myDelegate.getIndexOfLargest(row + (column * myRowDim), myRowDim + (column * myRowDim), 1) % myRowDim;
    }

    public int getIndexOfLargestInRow(final int row, final int column) {
        return myDelegate.getIndexOfLargest(row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim) / myRowDim;
    }

    public int getMaxDim() {
        return Math.max(myRowDim, myColDim);
    }

    public int getMinDim() {
        return Math.min(myRowDim, myColDim);
    }

    public int getRowDim() {
        return myRowDim;
    }

    @Override
    public int hashCode() {
        return myRowDim * myColDim * myDelegate.hashCode();
    }

    public boolean isAbsolute(final long index) {
        return myDelegate.isAbsolute(index);
    }

    /**
     * @see Scalar#isAbsolute()
     */
    public boolean isAbsolute(final long row, final long column) {
        return myDelegate.isAbsolute(row + (column * myRowDim));
    }

    public boolean isAllZeros() {
        return myDelegate.isZeros(0, myDelegate.length, 1);
    }

    public boolean isColumnZeros(final int row, final int column) {
        return myDelegate.isZeros(row + (column * myRowDim), myRowDim + (column * myRowDim), 1);
    }

    public boolean isDiagonalZeros(final int row, final int column) {

        final int tmpCount = Math.min(myRowDim - row, myColDim - column);

        return myDelegate.isZeros(row + (column * myRowDim), row + tmpCount + ((column + tmpCount) * myRowDim), 1 + myRowDim);
    }

    public boolean isInfinite(final long index) {
        return myDelegate.isInfinite(index);
    }

    public boolean isInfinite(final long row, final long column) {
        return myDelegate.isInfinite(row + (column * myRowDim));
    }

    public boolean isNaN(final long index) {
        return myDelegate.isNaN(index);
    }

    public boolean isNaN(final long row, final long column) {
        return myDelegate.isNaN(row + (column * myRowDim));
    }

    public boolean isPositive(final long index) {
        return myDelegate.isPositive(index);
    }

    public boolean isPositive(final long row, final long column) {
        return myDelegate.isPositive(row + (column * myRowDim));
    }

    public boolean isReal(final int row, final int column) {
        return myDelegate.isReal(row + (column * myRowDim));
    }

    public boolean isReal(final long index) {
        return myDelegate.isReal(index);
    }

    public boolean isReal(final long row, final long column) {
        return myDelegate.isReal(row + (column * myRowDim));
    }

    public boolean isRowZeros(final int row, final int column) {
        return myDelegate.isZeros(row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim);
    }

    public boolean isZero(final int row, final int column) {
        return myDelegate.isZero(row + (column * myRowDim));
    }

    public boolean isZero(final long index) {
        return myDelegate.isZero(index);
    }

    public boolean isZero(final long row, final long column) {
        return myDelegate.isZero(row + (column * myRowDim));
    }

    public Iterator<N> iterator() {
        return new Iterator1D<N>(this);
    }

    public void modifyAll(final BinaryFunction<N> aFunc, final N value) {
        myDelegate.modify(0, myDelegate.length, 1, aFunc, value);
    }

    public void modifyAll(final N value, final BinaryFunction<N> aFunc) {
        myDelegate.modify(0, myDelegate.length, 1, value, aFunc);
    }

    public void modifyAll(final ParameterFunction<N> aFunc, final int aParam) {
        myDelegate.modify(0, myDelegate.length, 1, aFunc, aParam);
    }

    public void modifyAll(final UnaryFunction<N> aFunc) {
        myDelegate.modify(0, myDelegate.length, 1, aFunc);
    }

    public void modifyColumn(final int row, final int column, final BinaryFunction<N> aFunc, final N value) {
        myDelegate.modify(row + (column * myRowDim), myRowDim + (column * myRowDim), 1, aFunc, value);
    }

    public void modifyColumn(final int row, final int column, final N value, final BinaryFunction<N> aFunc) {
        myDelegate.modify(row + (column * myRowDim), myRowDim + (column * myRowDim), 1, value, aFunc);
    }

    public void modifyColumn(final int row, final int column, final ParameterFunction<N> aFunc, final int aParam) {
        myDelegate.modify(row + (column * myRowDim), myRowDim + (column * myRowDim), 1, aFunc, aParam);
    }

    public void modifyColumn(final long row, final long column, final UnaryFunction<N> function) {
        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (myRowDim + (column * myRowDim));
        myDelegate.modify(tmpFirst, tmpLimit, 1, function);
    }

    public void modifyDiagonal(final int row, final int column, final BinaryFunction<N> aFunc, final N value) {

        final int tmpCount = Math.min(myRowDim - row, myColDim - column);

        myDelegate.modify(row + (column * myRowDim), row + tmpCount + ((column + tmpCount) * myRowDim), 1 + myRowDim, aFunc, value);
    }

    public void modifyDiagonal(final int row, final int column, final N value, final BinaryFunction<N> aFunc) {

        final int tmpCount = Math.min(myRowDim - row, myColDim - column);

        myDelegate.modify(row + (column * myRowDim), row + tmpCount + ((column + tmpCount) * myRowDim), 1 + myRowDim, value, aFunc);
    }

    public void modifyDiagonal(final int row, final int column, final ParameterFunction<N> aFunc, final int aParam) {

        final int tmpCount = Math.min(myRowDim - row, myColDim - column);

        myDelegate.modify(row + (column * myRowDim), row + tmpCount + ((column + tmpCount) * myRowDim), 1 + myRowDim, aFunc, aParam);
    }

    public void modifyDiagonal(final long row, final long column, final UnaryFunction<N> function) {

        final long tmpCount = Math.min(myRowDim - row, myColDim - column);

        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (row + tmpCount + ((column + tmpCount) * myRowDim));
        final int tmpStep = 1 + myRowDim;

        myDelegate.modify(tmpFirst, tmpLimit, tmpStep, function);
    }

    public void modifyMatching(final Array2D<N> aLeftArg, final BinaryFunction<N> aFunc) {
        myDelegate.modify(0, myDelegate.length, 1, aLeftArg.getDelegate(), aFunc);
    }

    public void modifyMatching(final BinaryFunction<N> aFunc, final Array2D<N> aRightArg) {
        myDelegate.modify(0, myDelegate.length, 1, aFunc, aRightArg.getDelegate());
    }

    public void modifyRange(final long first, final long limit, final UnaryFunction<N> function) {
        myDelegate.modify((int) first, (int) limit, 1, function);
    }

    public void modifyRow(final int row, final int column, final BinaryFunction<N> aFunc, final N value) {
        myDelegate.modify(row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim, aFunc, value);
    }

    public void modifyRow(final int row, final int column, final N value, final BinaryFunction<N> aFunc) {
        myDelegate.modify(row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim, value, aFunc);
    }

    public void modifyRow(final int row, final int column, final ParameterFunction<N> aFunc, final int aParam) {
        myDelegate.modify(row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim, aFunc, aParam);
    }

    public void modifyRow(final long row, final long column, final UnaryFunction<N> function) {

        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (row + (myColDim * myRowDim));

        myDelegate.modify(tmpFirst, tmpLimit, myRowDim, function);
    }

    /**
     * @deprecated Use {@link #set(long,double)} instead
     */
    @Deprecated
    public void set(final int index, final double value) {
        this.set(index, value);
    }

    public N set(final int index, final Number value) {
        return myDelegate.set(index, value);
    }

    public void set(final long index, final double value) {
        myDelegate.set(index, value);
    }

    public void set(final long row, final long column, final double value) {
        myDelegate.set(row + (column * myRowDim), value);
    }

    public void set(final long row, final long column, final Number value) {
        myDelegate.set(row + (column * myRowDim), value);
    }

    public void set(final long index, final Number value) {
        myDelegate.set(index, value);
    }

    public int size() {
        return myDelegate.length;
    }

    public Array1D<N> sliceColumn(final int row, final int column) {
        return new Array1D<N>(myDelegate, row + (column * myRowDim), myRowDim + (column * myRowDim), 1);
    }

    public Array1D<N> sliceDiagonal(final int row, final int column) {

        final int tmpCount = Math.min(myRowDim - row, myColDim - column);

        return new Array1D<N>(myDelegate, row + (column * myRowDim), row + tmpCount + ((column + tmpCount) * myRowDim), 1 + myRowDim);
    }

    public Array1D<N> sliceRow(final int row, final int column) {
        return new Array1D<N>(myDelegate, row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim);
    }

    /**
     * @return An array of arrays of doubles
     */
    public double[][] toRawCopy() {
        return ArrayUtils.toRawCopyOf(this);
    }

    public Scalar<N> toScalar(final int row, final int column) {
        return myDelegate.toScalar(row + (column * myRowDim));
    }

    @Override
    public String toString() {
        return myDelegate.toString();
    }

    public void visitAll(final AggregatorFunction<N> visitor) {
        myDelegate.visit(0, myDelegate.length, 1, visitor);
    }

    public void visitColumn(final int row, final int column, final AggregatorFunction<N> visitor) {
        myDelegate.visit(row + (column * myRowDim), myRowDim + (column * myRowDim), 1, visitor);
    }

    public void visitColumn(final long row, final long column, final AggregatorFunction<N> visitor) {
        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (myRowDim + (column * myRowDim));
        myDelegate.visit(tmpFirst, tmpLimit, 1, visitor);
    }

    public void visitDiagonal(final int row, final int column, final AggregatorFunction<N> visitor) {

        final int tmpCount = Math.min(myRowDim - row, myColDim - column);

        myDelegate.visit(row + (column * myRowDim), row + tmpCount + ((column + tmpCount) * myRowDim), 1 + myRowDim, visitor);
    }

    public void visitDiagonal(final long row, final long column, final AggregatorFunction<N> visitor) {

        final long tmpCount = Math.min(myRowDim - row, myColDim - column);

        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (row + tmpCount + ((column + tmpCount) * myRowDim));
        myDelegate.visit(tmpFirst, tmpLimit, 1 + myRowDim, visitor);
    }

    public void visitRange(final long first, final long limit, final AggregatorFunction<N> visitor) {
        myDelegate.visit((int) first, (int) limit, 1, visitor);
    }

    public void visitRow(final int row, final int column, final AggregatorFunction<N> visitor) {
        myDelegate.visit(row + (column * myRowDim), row + (myColDim * myRowDim), myRowDim, visitor);
    }

    public void visitRow(final long row, final long column, final AggregatorFunction<N> visitor) {
        final int tmpFirst = (int) (row + (column * myRowDim));
        final int tmpLimit = (int) (row + (myColDim * myRowDim));
        myDelegate.visit(tmpFirst, tmpLimit, myRowDim, visitor);
    }

    BasicArray<N> getDelegate() {
        return myDelegate;
    }

}
