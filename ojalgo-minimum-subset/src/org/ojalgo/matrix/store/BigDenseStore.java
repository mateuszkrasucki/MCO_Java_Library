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
package org.ojalgo.matrix.store;

import static org.ojalgo.constant.BigMath.*;
import static org.ojalgo.function.BigFunction.*;

import java.math.BigDecimal;
import java.util.List;

import org.ojalgo.access.Access1D;
import org.ojalgo.access.Access2D;
import org.ojalgo.access.AccessUtils;
import org.ojalgo.array.Array1D;
import org.ojalgo.array.Array2D;
import org.ojalgo.array.BigArray;
import org.ojalgo.array.SimpleArray;
import org.ojalgo.concurrent.DivideAndConquer;
import org.ojalgo.function.BigFunction;
import org.ojalgo.function.BinaryFunction;
import org.ojalgo.function.FunctionSet;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.aggregator.Aggregator;
import org.ojalgo.function.aggregator.AggregatorCollection;
import org.ojalgo.function.aggregator.AggregatorFunction;
import org.ojalgo.function.aggregator.BigAggregator;
import org.ojalgo.matrix.MatrixUtils;
import org.ojalgo.matrix.decomposition.DecompositionStore;
import org.ojalgo.matrix.operation.*;
import org.ojalgo.matrix.transformation.Householder;
import org.ojalgo.matrix.transformation.Rotation;
import org.ojalgo.random.RandomNumber;
import org.ojalgo.scalar.BigScalar;
import org.ojalgo.scalar.ComplexNumber;
import org.ojalgo.scalar.Scalar;
import org.ojalgo.type.TypeUtils;
import org.ojalgo.type.context.NumberContext;

/**
 * A {@linkplain BigDecimal} implementation of {@linkplain PhysicalStore}.
 *
 * @author apete
 */
public final class BigDenseStore extends BigArray implements PhysicalStore<BigDecimal>, DecompositionStore<BigDecimal> {

    public static final DecompositionStore.Factory<BigDecimal, BigDenseStore> FACTORY = new DecompositionStore.Factory<BigDecimal, BigDenseStore>() {

        public BigDenseStore columns(final Access1D<?>... source) {

            final int tmpRowDim = source[0].size();
            final int tmpColDim = source.length;

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            Access1D<?> tmpColumn;
            for (int j = 0; j < tmpColDim; j++) {
                tmpColumn = source[j];
                for (int i = 0; i < tmpRowDim; i++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpColumn.get(i));
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore columns(final double[]... source) {

            final int tmpRowDim = source[0].length;
            final int tmpColDim = source.length;

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            double[] tmpColumn;
            for (int j = 0; j < tmpColDim; j++) {
                tmpColumn = source[j];
                for (int i = 0; i < tmpRowDim; i++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpColumn[i]);
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore columns(final List<? extends Number>... source) {

            final int tmpRowDim = source[0].size();
            final int tmpColDim = source.length;

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            List<? extends Number> tmpColumn;
            for (int j = 0; j < tmpColDim; j++) {
                tmpColumn = source[j];
                for (int i = 0; i < tmpRowDim; i++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpColumn.get(i));
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore columns(final Number[]... source) {

            final int tmpRowDim = source[0].length;
            final int tmpColDim = source.length;

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            Number[] tmpColumn;
            for (int j = 0; j < tmpColDim; j++) {
                tmpColumn = source[j];
                for (int i = 0; i < tmpRowDim; i++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpColumn[i]);
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore conjugate(final Access2D<?> source) {
            return this.transpose(source);
        }

        public BigDenseStore copy(final Access2D<?> source) {

            final BigDenseStore retVal = new BigDenseStore(source.getRowDim(), source.getColDim());

            retVal.fillMatching(source);

            return retVal;
        }

        public AggregatorCollection<BigDecimal> getAggregatorCollection() {
            return BigAggregator.getCollection();
        }

        public FunctionSet<BigDecimal> getFunctionSet() {
            return BigFunction.getSet();
        }

        public BigDecimal getNumber(final double value) {
            return new BigDecimal(value);
        }

        public BigDecimal getNumber(final Number value) {
            return TypeUtils.toBigDecimal(value);
        }

        public BigScalar getStaticOne() {
            return BigScalar.ONE;
        }

        public BigScalar getStaticZero() {
            return BigScalar.ZERO;
        }

        public SimpleArray.Big makeArray(final int length) {
            return SimpleArray.makeBig(length);
        }

        public BigDenseStore makeEye(final long rows, final long columns) {

            final BigDenseStore retVal = this.makeZero(rows, columns);

            retVal.myUtility.fillDiagonal(0, 0, this.getStaticOne().getNumber());

            return retVal;
        }

        public Householder.Big makeHouseholder(final int length) {
            return new Householder.Big(length);
        }

        public BigDenseStore makeRandom(final long rows, final long columns, final RandomNumber distribution) {

            final int tmpRowDim = (int) rows;
            final int tmpColDim = (int) columns;

            final int tmpLength = tmpRowDim * tmpColDim;

            final BigDecimal[] tmpData = new BigDecimal[tmpLength];

            for (int i = 0; i < tmpLength; i++) {
                tmpData[i] = TypeUtils.toBigDecimal(distribution.doubleValue());
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public Rotation.Big makeRotation(final int low, final int high, final BigDecimal cos, final BigDecimal sin) {
            return new Rotation.Big(low, high, cos, sin);
        }

        public Rotation.Big makeRotation(final int low, final int high, final double cos, final double sin) {
            return this.makeRotation(low, high, new BigDecimal(cos), new BigDecimal(sin));
        }

        public BigDenseStore makeZero(final long rows, final long columns) {
            return new BigDenseStore((int) rows, (int) columns);
        }

        public BigDenseStore rows(final Access1D<?>... source) {

            final int tmpRowDim = source.length;
            final int tmpColDim = source[0].size();

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            Access1D<?> tmpRow;
            for (int i = 0; i < tmpRowDim; i++) {
                tmpRow = source[i];
                for (int j = 0; j < tmpColDim; j++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpRow.get(j));
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore rows(final double[]... source) {

            final int tmpRowDim = source.length;
            final int tmpColDim = source[0].length;

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            double[] tmpRow;
            for (int i = 0; i < tmpRowDim; i++) {
                tmpRow = source[i];
                for (int j = 0; j < tmpColDim; j++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpRow[j]);
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore rows(final List<? extends Number>... source) {

            final int tmpRowDim = source.length;
            final int tmpColDim = source[0].size();

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            List<? extends Number> tmpRow;
            for (int i = 0; i < tmpRowDim; i++) {
                tmpRow = source[i];
                for (int j = 0; j < tmpColDim; j++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpRow.get(j));
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigDenseStore rows(final Number[]... source) {

            final int tmpRowDim = source.length;
            final int tmpColDim = source[0].length;

            final BigDecimal[] tmpData = new BigDecimal[tmpRowDim * tmpColDim];

            Number[] tmpRow;
            for (int i = 0; i < tmpRowDim; i++) {
                tmpRow = source[i];
                for (int j = 0; j < tmpColDim; j++) {
                    tmpData[i + (tmpRowDim * j)] = TypeUtils.toBigDecimal(tmpRow[j]);
                }
            }

            return new BigDenseStore(tmpRowDim, tmpColDim, tmpData);
        }

        public BigScalar toScalar(final double value) {
            return new BigScalar(value);
        }

        public BigScalar toScalar(final Number value) {
            return new BigScalar(TypeUtils.toBigDecimal(value));
        }

        public BigDenseStore transpose(final Access2D<?> source) {

            MatrixStore<BigDecimal> tmpSource = new WrapperStore<BigDecimal>(this, source);
            tmpSource = new TransposedStore<BigDecimal>(tmpSource);

            final BigDenseStore retVal = new BigDenseStore(tmpSource.getRowDim(), tmpSource.getColDim());

            retVal.fillMatching(tmpSource);

            return retVal;
        }
    };

    static Householder.Big cast(final Householder<BigDecimal> aTransf) {
        if (aTransf instanceof Householder.Big) {
            return (Householder.Big) aTransf;
        } else if (aTransf instanceof DecompositionStore.HouseholderReference<?>) {
            return ((DecompositionStore.HouseholderReference<BigDecimal>) aTransf).getBigWorker().copy(aTransf);
        } else {
            return new Householder.Big(aTransf);
        }
    }

    static BigDenseStore cast(final MatrixStore<BigDecimal> aStore) {
        if (aStore instanceof BigDenseStore) {
            return (BigDenseStore) aStore;
        } else {
            return FACTORY.copy(aStore);
        }
    }

    static Rotation.Big cast(final Rotation<BigDecimal> aTransf) {
        if (aTransf instanceof Rotation.Big) {
            return (Rotation.Big) aTransf;
        } else {
            return new Rotation.Big(aTransf);
        }
    }

    static void doMultiplyBoth(final BigDecimal[] aProductArray, final Access2D<BigDecimal> aLeftStore, final Access2D<BigDecimal> aRightStore) {

        final int tmpRowDim = aLeftStore.getRowDim();

        if (tmpRowDim > MultiplyBoth.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    MultiplyBoth.invoke(aProductArray, aFirst, aLimit, aLeftStore, aRightStore);
                }
            };

            tmpConquerer.invoke(0, tmpRowDim, MultiplyBoth.THRESHOLD);

        } else {

            MultiplyBoth.invoke(aProductArray, 0, tmpRowDim, aLeftStore, aRightStore);
        }
    }

    static void doMultiplyLeft(final BigDecimal[] aProductArray, final MatrixStore<BigDecimal> aLeftStore, final BigDecimal[] aRightArray) {

        final int tmpRowDim = aLeftStore.getRowDim();

        if (tmpRowDim > MultiplyLeft.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    MultiplyLeft.invoke(aProductArray, aFirst, aLimit, aLeftStore, aRightArray);
                }
            };

            tmpConquerer.invoke(0, tmpRowDim, MultiplyLeft.THRESHOLD);

        } else {

            MultiplyLeft.invoke(aProductArray, 0, tmpRowDim, aLeftStore, aRightArray);
        }
    }

    static void doMultiplyRight(final BigDecimal[] aProductArray, final BigDecimal[] aLeftArray, final MatrixStore<BigDecimal> aRightStore) {

        final int tmpColDim = aRightStore.getColDim();

        if (tmpColDim > MultiplyRight.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    MultiplyRight.invoke(aProductArray, aFirst, aLimit, aLeftArray, aRightStore);
                }
            };

            tmpConquerer.invoke(0, tmpColDim, MultiplyRight.THRESHOLD);

        } else {

            MultiplyRight.invoke(aProductArray, 0, tmpColDim, aLeftArray, aRightStore);
        }
    }

    private final int myColDim;
    private final int myRowDim;
    private final Array2D<BigDecimal> myUtility;

    BigDenseStore(final BigDecimal[] anArray) {

        super(anArray);

        myRowDim = anArray.length;
        myColDim = 1;
        myUtility = this.asArray2D(myRowDim, myColDim);
    }

    BigDenseStore(final int aLength) {

        super(aLength);

        myRowDim = aLength;
        myColDim = 1;
        myUtility = this.asArray2D(myRowDim, myColDim);
    }

    BigDenseStore(final int aRowDim, final int aColDim) {

        super(aRowDim * aColDim);

        myRowDim = aRowDim;
        myColDim = aColDim;
        myUtility = this.asArray2D(myRowDim, myColDim);
    }

    BigDenseStore(final int aRowDim, final int aColDim, final BigDecimal[] anArray) {

        super(anArray);

        myRowDim = aRowDim;
        myColDim = aColDim;
        myUtility = this.asArray2D(myRowDim, myColDim);
    }

    public BigDecimal aggregateAll(final Aggregator aVisitor) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        final AggregatorFunction<BigDecimal> tmpMainAggr = aVisitor.getBigFunction();

        if (tmpColDim > AggregateAll.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {

                    final AggregatorFunction<BigDecimal> tmpPartAggr = aVisitor.getBigFunction();

                    BigDenseStore.this.visit(tmpRowDim * aFirst, tmpRowDim * aLimit, 1, tmpPartAggr);

                    synchronized (tmpMainAggr) {
                        tmpMainAggr.merge(tmpPartAggr.getNumber());
                    }
                }
            };

            tmpConquerer.invoke(0, tmpColDim, AggregateAll.THRESHOLD);

        } else {

            BigDenseStore.this.visit(0, length, 1, tmpMainAggr);
        }

        return tmpMainAggr.getNumber();
    }

    public void applyCholesky(final int iterationPoint, final SimpleArray<BigDecimal> multipliers) {

        final BigDecimal[] tmpData = this.data();
        final BigDecimal[] tmpColumn = ((SimpleArray.Big) multipliers).data;

        if ((myColDim - iterationPoint - 1) > ApplyCholesky.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                protected void conquer(final int aFirst, final int aLimit) {
                    ApplyCholesky.invoke(tmpData, myRowDim, aFirst, aLimit, tmpColumn);
                }
            };

            tmpConquerer.invoke(iterationPoint + 1, myColDim, ApplyCholesky.THRESHOLD);

        } else {

            ApplyCholesky.invoke(tmpData, myRowDim, iterationPoint + 1, myColDim, tmpColumn);
        }
    }

    public void applyLU(final int iterationPoint, final SimpleArray<BigDecimal> multipliers) {

        final BigDecimal[] tmpData = this.data();
        final BigDecimal[] tmpColumn = ((SimpleArray.Big) multipliers).data;

        if ((myColDim - iterationPoint - 1) > ApplyLU.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                protected void conquer(final int aFirst, final int aLimit) {
                    ApplyLU.invoke(tmpData, myRowDim, aFirst, aLimit, tmpColumn, iterationPoint);
                }
            };

            tmpConquerer.invoke(iterationPoint + 1, myColDim, ApplyLU.THRESHOLD);

        } else {

            ApplyLU.invoke(tmpData, myRowDim, iterationPoint + 1, myColDim, tmpColumn, iterationPoint);
        }
    }

    public Array2D<BigDecimal> asArray2D() {
        return myUtility;
    }

    public Array1D<BigDecimal> asList() {
        return myUtility.asArray1D();
    }

    public final MatrixStore.Builder<BigDecimal> builder() {
        return new MatrixStore.Builder<BigDecimal>(this);
    }

    public void caxpy(final BigDecimal aSclrA, final int aColX, final int aColY, final int aFirstRow) {
        CAXPY.invoke(this.data(), aColY * myRowDim, this.data(), aColX * myRowDim, aSclrA, aFirstRow, myRowDim);
    }

    public Array1D<ComplexNumber> computeInPlaceSchur(final PhysicalStore<BigDecimal> aTransformationCollector, final boolean eigenvalue) {
        throw new UnsupportedOperationException();
    }

    public BigDenseStore conjugate() {
        return this.transpose();
    }

    public BigDenseStore copy() {
        return new BigDenseStore(myRowDim, myColDim, this.copyOfData());
    }

    public long countColumns() {
        return myColDim;
    }

    public long countRows() {
        return myRowDim;
    }

    public void divideAndCopyColumn(final int aRow, final int aCol, final SimpleArray<BigDecimal> aDestination) {

        final BigDecimal[] tmpData = this.data();
        final int tmpRowDim = myRowDim;

        final BigDecimal[] tmpDestination = ((SimpleArray.Big) aDestination).data;

        int tmpIndex = aRow + (aCol * tmpRowDim);
        final BigDecimal tmpDenominator = tmpData[tmpIndex];

        for (int i = aRow + 1; i < tmpRowDim; i++) {
            tmpIndex++;
            tmpDestination[i] = tmpData[tmpIndex] = BigFunction.DIVIDE.invoke(tmpData[tmpIndex], tmpDenominator);
        }
    }

    public double doubleValue(final long aRow, final long aCol) {
        return myUtility.doubleValue(aRow, aCol);
    }

    public boolean equals(final MatrixStore<BigDecimal> aStore, final NumberContext aCntxt) {
        return AccessUtils.equals(this, aStore, aCntxt);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object anObj) {
        if (anObj instanceof MatrixStore) {
            return this.equals((MatrixStore<BigDecimal>) anObj, NumberContext.getGeneral(6));
        } else {
            return super.equals(anObj);
        }
    }

    public void exchangeColumns(final int aColA, final int aColB) {
        myUtility.exchangeColumns(aColA, aColB);
    }

    public void exchangeRows(final int aRowA, final int aRowB) {
        myUtility.exchangeRows(aRowA, aRowB);
    }

    public PhysicalStore.Factory<BigDecimal, BigDenseStore> factory() {
        return FACTORY;
    }

    public void fillByMultiplying(final MatrixStore<BigDecimal> aLeftStore, final MatrixStore<BigDecimal> aRightStore) {

        final BigDecimal[] tmpProductData = this.data();

        if (aRightStore instanceof BigDenseStore) {

            BigDenseStore.doMultiplyLeft(tmpProductData, aLeftStore, BigDenseStore.cast(aRightStore).data());

        } else if (aLeftStore instanceof BigDenseStore) {

            this.fillAll(ZERO);

            BigDenseStore.doMultiplyRight(tmpProductData, BigDenseStore.cast(aLeftStore).data(), aRightStore);

        } else {

            BigDenseStore.doMultiplyBoth(tmpProductData, aLeftStore, aRightStore);
        }
    }

    public void fillColumn(final long aRow, final long aCol, final BigDecimal aNmbr) {
        myUtility.fillColumn(aRow, aCol, aNmbr);
    }

    public void fillDiagonal(final long aRow, final long aCol, final BigDecimal aNmbr) {
        myUtility.fillDiagonal(aRow, aCol, aNmbr);
    }

    public void fillMatching(final Access2D<? extends Number> aSource2D) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > FillMatchingSingle.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    FillMatchingSingle.invoke(BigDenseStore.this.data(), tmpRowDim, aFirst, aLimit, aSource2D);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, FillMatchingSingle.THRESHOLD);

        } else {

            FillMatchingSingle.invoke(this.data(), tmpRowDim, 0, tmpColDim, aSource2D);
        }
    }

    public void fillMatching(final BigDecimal aLeftArg, final BinaryFunction<BigDecimal> aFunc, final MatrixStore<BigDecimal> aRightArg) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > FillMatchingRight.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                protected void conquer(final int aFirst, final int aLimit) {
                    BigDenseStore.this.fill(tmpRowDim * aFirst, tmpRowDim * aLimit, aLeftArg, aFunc, aRightArg);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, FillMatchingRight.THRESHOLD);

        } else {

            this.fill(0, tmpRowDim * tmpColDim, aLeftArg, aFunc, aRightArg);
        }
    }

    public void fillMatching(final MatrixStore<BigDecimal> aLeftArg, final BinaryFunction<BigDecimal> aFunc, final BigDecimal aRightArg) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > FillMatchingLeft.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                protected void conquer(final int aFirst, final int aLimit) {
                    BigDenseStore.this.fill(tmpRowDim * aFirst, tmpRowDim * aLimit, aLeftArg, aFunc, aRightArg);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, FillMatchingLeft.THRESHOLD);

        } else {

            this.fill(0, tmpRowDim * tmpColDim, aLeftArg, aFunc, aRightArg);
        }
    }

    public void fillMatching(final MatrixStore<BigDecimal> aLeftArg, final BinaryFunction<BigDecimal> aFunc, final MatrixStore<BigDecimal> aRightArg) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > FillMatchingBoth.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                protected void conquer(final int aFirst, final int aLimit) {
                    BigDenseStore.this.fill(tmpRowDim * aFirst, tmpRowDim * aLimit, aLeftArg, aFunc, aRightArg);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, FillMatchingBoth.THRESHOLD);

        } else {

            this.fill(0, tmpRowDim * tmpColDim, aLeftArg, aFunc, aRightArg);
        }
    }

    public void fillRow(final long aRow, final long aCol, final BigDecimal aNmbr) {
        myUtility.fillRow(aRow, aCol, aNmbr);
    }

    public boolean generateApplyAndCopyHouseholderColumn(final int aRow, final int aCol, final Householder<BigDecimal> aDestination) {
        return GenerateApplyAndCopyHouseholderColumn.invoke(this.data(), myRowDim, aRow, aCol, (Householder.Big) aDestination);
    }

    public boolean generateApplyAndCopyHouseholderRow(final int aRow, final int aCol, final Householder<BigDecimal> aDestination) {
        return GenerateApplyAndCopyHouseholderRow.invoke(this.data(), myRowDim, aRow, aCol, (Householder.Big) aDestination);
    }

    public BigDecimal get(final long aRow, final long aCol) {
        return myUtility.get(aRow, aCol);
    }

    public int getColDim() {
        return myColDim;
    }

    public int getIndexOfLargestInColumn(final int aRow, final int aCol) {
        return myUtility.getIndexOfLargestInColumn(aRow, aCol);
    }

    public int getMinDim() {
        return Math.min(myRowDim, myColDim);
    }

    public int getRowDim() {
        return myRowDim;
    }

    @Override
    public int hashCode() {
        return MatrixUtils.hashCode(this);
    }

    public boolean isAbsolute(final int aRow, final int aCol) {
        return myUtility.isAbsolute(aRow, aCol);
    }

    public boolean isAbsolute(final long row, final long column) {
        return myUtility.isAbsolute(row, column);
    }

    public boolean isInfinite(final long row, final long column) {
        return myUtility.isInfinite(row, column);
    }

    public boolean isLowerLeftShaded() {
        return false;
    }

    public boolean isNaN(final long row, final long column) {
        return myUtility.isNaN(row, column);
    }

    public boolean isPositive(final int aRow, final int aCol) {
        return myUtility.isPositive(aRow, aCol);
    }

    public boolean isPositive(final long row, final long column) {
        return myUtility.isPositive(row, column);
    }

    public boolean isReal(final int aRow, final int aCol) {
        return myUtility.isReal(aRow, aCol);
    }

    public boolean isReal(final long row, final long column) {
        return myUtility.isReal(row, column);
    }

    public boolean isUpperRightShaded() {
        return false;
    }

    public boolean isZero(final int aRow, final int aCol) {
        return myUtility.isZero(aRow, aCol);
    }

    public boolean isZero(final long row, final long column) {
        return myUtility.isZero(row, column);
    }

    public void maxpy(final BigDecimal aSclrA, final MatrixStore<BigDecimal> aMtrxX) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > MAXPY.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    MAXPY.invoke(BigDenseStore.this.data(), tmpRowDim, aFirst, aLimit, aSclrA, aMtrxX);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, MAXPY.THRESHOLD);

        } else {

            MAXPY.invoke(this.data(), tmpRowDim, 0, tmpColDim, aSclrA, aMtrxX);
        }
    }

    @Override
    public void modifyAll(final UnaryFunction<BigDecimal> aFunc) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > ModifyAll.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    BigDenseStore.this.modify(tmpRowDim * aFirst, tmpRowDim * aLimit, 1, aFunc);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, ModifyAll.THRESHOLD);

        } else {

            this.modify(tmpRowDim * 0, tmpRowDim * tmpColDim, 1, aFunc);
        }
    }

    public void modifyColumn(final int aRow, final int aCol, final UnaryFunction<BigDecimal> aFunc) {
        myUtility.modifyColumn(aRow, aCol, aFunc);
    }

    public void modifyColumn(final long row, final long column, final UnaryFunction<BigDecimal> function) {
        myUtility.modifyColumn(row, column, function);
    }

    public void modifyDiagonal(final int aRow, final int aCol, final UnaryFunction<BigDecimal> aFunc) {
        myUtility.modifyDiagonal(aRow, aCol, aFunc);
    }

    public void modifyDiagonal(final long row, final long column, final UnaryFunction<BigDecimal> function) {
        myUtility.modifyDiagonal(row, column, function);
    }

    public void modifyOne(final int aRow, final int aCol, final UnaryFunction<BigDecimal> aFunc) {

        BigDecimal tmpValue = this.get(aRow, aCol);

        tmpValue = aFunc.invoke(tmpValue);

        this.set(aRow, aCol, tmpValue);
    }

    public void modifyRow(final int aRow, final int aCol, final UnaryFunction<BigDecimal> aFunc) {
        myUtility.modifyRow(aRow, aCol, aFunc);
    }

    public void modifyRow(final long row, final long column, final UnaryFunction<BigDecimal> function) {
        myUtility.modifyRow(row, column, function);
    }

    public MatrixStore<BigDecimal> multiplyLeft(final MatrixStore<BigDecimal> aStore) {

        final BigDenseStore retVal = FACTORY.makeZero(aStore.getRowDim(), myColDim);

        BigDenseStore.doMultiplyLeft(retVal.data(), aStore, this.data());

        return retVal;
    }

    public MatrixStore<BigDecimal> multiplyRight(final MatrixStore<BigDecimal> aStore) {

        final BigDenseStore retVal = FACTORY.makeZero(myRowDim, aStore.getColDim());

        BigDenseStore.doMultiplyRight(retVal.data(), this.data(), aStore);

        return retVal;
    }

    public void negateColumn(final int aCol) {
        myUtility.modifyColumn(0, aCol, BigFunction.NEGATE);
    }

    public void raxpy(final BigDecimal aSclrA, final int aRowX, final int aRowY, final int aFirstCol) {
        RAXPY.invoke(this.data(), aRowY, this.data(), aRowX, aSclrA, aFirstCol, myColDim);
    }

    public void rotateRight(final int aLow, final int aHigh, final double aCos, final double aSin) {
        RotateRight.invoke(this.data(), myRowDim, aLow, aHigh, FACTORY.getNumber(aCos), FACTORY.getNumber(aSin));
    }

    public void set(final long aRow, final long aCol, final double aNmbr) {
        myUtility.set(aRow, aCol, aNmbr);
    }

    public void set(final long aRow, final long aCol, final Number aNmbr) {
        myUtility.set(aRow, aCol, aNmbr);
    }

    public void setToIdentity(final int aCol) {
        myUtility.set(aCol, aCol, ONE);
        myUtility.fillColumn(aCol + 1, aCol, ZERO);
    }

    public void substituteBackwards(final Access2D<BigDecimal> aBody, final boolean conjugated) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > SubstituteBackwards.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    SubstituteBackwards.invoke(BigDenseStore.this.data(), tmpRowDim, aFirst, aLimit, aBody, conjugated);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, SubstituteBackwards.THRESHOLD);

        } else {

            SubstituteBackwards.invoke(this.data(), tmpRowDim, 0, tmpColDim, aBody, conjugated);
        }
    }

    public void substituteForwards(final Access2D<BigDecimal> aBody, final boolean onesOnDiagonal, final boolean zerosAboveDiagonal) {

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if (tmpColDim > SubstituteForwards.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    SubstituteForwards.invoke(BigDenseStore.this.data(), tmpRowDim, aFirst, aLimit, aBody, onesOnDiagonal, zerosAboveDiagonal);
                }

            };

            tmpConquerer.invoke(0, tmpColDim, SubstituteForwards.THRESHOLD);

        } else {

            SubstituteForwards.invoke(this.data(), tmpRowDim, 0, tmpColDim, aBody, onesOnDiagonal, zerosAboveDiagonal);
        }
    }

    public Scalar<BigDecimal> toScalar(final int aRow, final int aCol) {
        return new BigScalar(this.get(aRow, aCol));
    }

    public void transformLeft(final Householder<BigDecimal> aTransf, final int aFirstCol) {

        final Householder.Big tmpTransf = BigDenseStore.cast(aTransf);

        final BigDecimal[] tmpData = this.data();

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if ((tmpColDim - aFirstCol) > HouseholderLeft.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    HouseholderLeft.invoke(tmpData, tmpRowDim, aFirst, aLimit, tmpTransf);
                }

            };

            tmpConquerer.invoke(aFirstCol, tmpColDim, HouseholderLeft.THRESHOLD);

        } else {

            HouseholderLeft.invoke(tmpData, tmpRowDim, aFirstCol, tmpColDim, tmpTransf);
        }
    }

    public void transformLeft(final Rotation<BigDecimal> aTransf) {

        final Rotation.Big tmpTransf = BigDenseStore.cast(aTransf);

        final int tmpLow = tmpTransf.low;
        final int tmpHigh = tmpTransf.high;

        if (tmpLow != tmpHigh) {
            if ((tmpTransf.cos != null) && (tmpTransf.sin != null)) {
                RotateLeft.invoke(this.data(), myColDim, tmpLow, tmpHigh, tmpTransf.cos, tmpTransf.sin);
            } else {
                myUtility.exchangeRows(tmpLow, tmpHigh);
            }
        } else {
            if (tmpTransf.cos != null) {
                myUtility.modifyRow(tmpLow, 0, MULTIPLY, tmpTransf.cos);
            } else if (tmpTransf.sin != null) {
                myUtility.modifyRow(tmpLow, 0, DIVIDE, tmpTransf.sin);
            } else {
                myUtility.modifyRow(tmpLow, 0, NEGATE);
            }
        }
    }

    public void transformRight(final Householder<BigDecimal> aTransf, final int aFirstRow) {

        final Householder.Big tmpTransf = BigDenseStore.cast(aTransf);

        final BigDecimal[] tmpData = this.data();

        final int tmpRowDim = myRowDim;
        final int tmpColDim = myColDim;

        if ((tmpRowDim - aFirstRow) > HouseholderRight.THRESHOLD) {

            final DivideAndConquer tmpConquerer = new DivideAndConquer() {

                @Override
                public void conquer(final int aFirst, final int aLimit) {
                    HouseholderRight.invoke(tmpData, aFirst, aLimit, tmpColDim, tmpTransf);
                }

            };

            tmpConquerer.invoke(aFirstRow, tmpRowDim, HouseholderRight.THRESHOLD);

        } else {

            HouseholderRight.invoke(tmpData, aFirstRow, tmpRowDim, tmpColDim, tmpTransf);
        }
    }

    public void transformRight(final Rotation<BigDecimal> aTransf) {

        final Rotation.Big tmpTransf = BigDenseStore.cast(aTransf);

        final int tmpLow = tmpTransf.low;
        final int tmpHigh = tmpTransf.high;

        if (tmpLow != tmpHigh) {
            if ((tmpTransf.cos != null) && (tmpTransf.sin != null)) {
                RotateRight.invoke(this.data(), myRowDim, tmpLow, tmpHigh, tmpTransf.cos, tmpTransf.sin);
            } else {
                myUtility.exchangeColumns(tmpLow, tmpHigh);
            }
        } else {
            if (tmpTransf.cos != null) {
                myUtility.modifyColumn(0, tmpHigh, MULTIPLY, tmpTransf.cos);
            } else if (tmpTransf.sin != null) {
                myUtility.modifyColumn(0, tmpHigh, DIVIDE, tmpTransf.sin);
            } else {
                myUtility.modifyColumn(0, tmpHigh, NEGATE);
            }
        }
    }

    public void transformSymmetric(final Householder<BigDecimal> aTransf) {
        HouseholderHermitian.invoke(this.data(), BigDenseStore.cast(aTransf), new BigDecimal[aTransf.size()]);
    }

    public BigDenseStore transpose() {

        final BigDenseStore retVal = new BigDenseStore(myColDim, myRowDim);

        retVal.fillMatching(new TransposedStore<BigDecimal>(this));

        return retVal;
    }

    public void tred2(final SimpleArray<BigDecimal> mainDiagonal, final SimpleArray<BigDecimal> offDiagonal, final boolean yesvecs) {
        throw new UnsupportedOperationException();
    }

    public void visitColumn(final int aRow, final int aCol, final AggregatorFunction<BigDecimal> aVisitor) {
        myUtility.visitColumn(aRow, aCol, aVisitor);
    }

    public void visitColumn(final long row, final long column, final AggregatorFunction<BigDecimal> visitor) {
        myUtility.visitColumn(row, column, visitor);
    }

    public void visitDiagonal(final int aRow, final int aCol, final AggregatorFunction<BigDecimal> aVisitor) {
        myUtility.visitDiagonal(aRow, aCol, aVisitor);
    }

    public void visitDiagonal(final long row, final long column, final AggregatorFunction<BigDecimal> visitor) {
        myUtility.visitDiagonal(row, column, visitor);
    }

    public void visitRow(final int aRow, final int aCol, final AggregatorFunction<BigDecimal> aVisitor) {
        myUtility.visitRow(aRow, aCol, aVisitor);
    }

    public void visitRow(final long row, final long column, final AggregatorFunction<BigDecimal> visitor) {
        myUtility.visitRow(row, column, visitor);
    }

}
