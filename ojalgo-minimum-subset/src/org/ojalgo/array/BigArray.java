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

import static org.ojalgo.constant.BigMath.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.ojalgo.access.Access1D;
import org.ojalgo.function.BinaryFunction;
import org.ojalgo.function.ParameterFunction;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.aggregator.AggregatorFunction;
import org.ojalgo.scalar.BigScalar;
import org.ojalgo.scalar.Scalar;
import org.ojalgo.type.TypeUtils;

/**
 * A one- and/or arbitrary-dimensional array of {@linkplain java.math.BigDecimal}.
 * 
 * @see PrimitiveArray
 * @author apete
 */
public class BigArray extends BasicArray<BigDecimal> {

    protected static void exchange(final BigDecimal[] aData, final int aFirstA, final int aFirstB, final int aStep, final int aCount) {

        int tmpIndexA = aFirstA;
        int tmpIndexB = aFirstB;

        BigDecimal tmpVal;

        for (int i = 0; i < aCount; i++) {

            tmpVal = aData[tmpIndexA];
            aData[tmpIndexA] = aData[tmpIndexB];
            aData[tmpIndexB] = tmpVal;

            tmpIndexA += aStep;
            tmpIndexB += aStep;
        }
    }

    protected static void fill(final BigDecimal[] aData, final Access1D<?> anArg) {
        final int tmpLimit = Math.min(aData.length, anArg.size());
        for (int i = 0; i < tmpLimit; i++) {
            aData[i] = TypeUtils.toBigDecimal(anArg.get(i));
        }
    }

    protected static void fill(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final BigDecimal aNmbr) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aNmbr;
        }
    }

    protected static void invoke(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<BigDecimal> aLeftArg,
            final BinaryFunction<BigDecimal> aFunc, final Access1D<BigDecimal> aRightArg) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(aLeftArg.get(i), aRightArg.get(i));
        }
    }

    protected static void invoke(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<BigDecimal> aLeftArg,
            final BinaryFunction<BigDecimal> aFunc, final BigDecimal aRightArg) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(aLeftArg.get(i), aRightArg);
        }
    }

    protected static void invoke(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<BigDecimal> anArg,
            final ParameterFunction<BigDecimal> aFunc, final int aParam) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(anArg.get(i), aParam);
        }
    }

    protected static void invoke(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<BigDecimal> anArg,
            final UnaryFunction<BigDecimal> aFunc) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(anArg.get(i));
        }
    }

    protected static void invoke(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final AggregatorFunction<BigDecimal> aVisitor) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aVisitor.invoke(aData[i]);
        }
    }

    protected static void invoke(final BigDecimal[] aData, final int aFirst, final int aLimit, final int aStep, final BigDecimal aLeftArg,
            final BinaryFunction<BigDecimal> aFunc, final Access1D<BigDecimal> aRightArg) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(aLeftArg, aRightArg.get(i));
        }
    }

    private final BigDecimal[] myData;

    protected BigArray(final BigDecimal[] anArray) {

        super(anArray.length);

        myData = anArray;
    }

    protected BigArray(final int aLength) {

        super(aLength);

        myData = new BigDecimal[aLength];

        this.fill(0, aLength, 1, ZERO);
    }

    public final double doubleValue(final long index) {
        return myData[(int) index].doubleValue();
    }

    @Override
    public boolean equals(final Object anObj) {
        if (anObj instanceof BigArray) {
            return Arrays.equals(myData, ((BigArray) anObj).data());
        } else {
            return super.equals(anObj);
        }
    }

    public final void fillAll(final BigDecimal aNmbr) {
        BigArray.fill(myData, 0, myData.length, 1, aNmbr);
    }

    public void fillRange(final long first, final long limit, final BigDecimal value) {
        BigArray.fill(myData, (int) first, (int) limit, 1, value);
    }

    public final BigDecimal get(final long index) {
        return myData[(int) index];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(myData);
    }

    public final boolean isAbsolute(final long index) {
        return BigScalar.isAbsolute(myData[(int) index]);
    }

    public final boolean isInfinite(final long index) {
        return BigScalar.IS_INFINITE;
    }

    public final boolean isNaN(final long index) {
        return BigScalar.IS_NOT_A_NUMBER;
    }

    public final boolean isPositive(final long index) {
        return BigScalar.isPositive(myData[(int) index]);
    }

    public final boolean isReal(final long index) {
        return BigScalar.IS_REAL;
    }

    public final boolean isZero(final long index) {
        return BigScalar.isZero(myData[(int) index]);
    }

    @Override
    public final BigDecimal set(final int index, final Number value) {
        final BigDecimal retVal = myData[index];
        myData[index] = TypeUtils.toBigDecimal(value);
        return retVal;
    }

    @Override
    public final void set(final long index, final double value) {
        myData[(int) index] = new BigDecimal(value);
    }

    public final void set(final long index, final Number value) {
        myData[(int) index] = TypeUtils.toBigDecimal(value);
    }

    protected final BigDecimal[] copyOfData() {
        return ArrayUtils.copyOf(myData);
    }

    protected final BigDecimal[] data() {
        return myData;
    }

    @Override
    protected final void exchange(final int aFirstA, final int aFirstB, final int aStep, final int aCount) {
        BigArray.exchange(myData, aFirstA, aFirstB, aStep, aCount);
    }

    @Override
    protected void fill(final Access1D<?> anArg) {
        BigArray.fill(myData, anArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final Access1D<BigDecimal> aLeftArg, final BinaryFunction<BigDecimal> aFunc,
            final Access1D<BigDecimal> aRightArg) {
        BigArray.invoke(myData, aFirst, aLimit, 1, aLeftArg, aFunc, aRightArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final Access1D<BigDecimal> aLeftArg, final BinaryFunction<BigDecimal> aFunc,
            final BigDecimal aRightArg) {
        BigArray.invoke(myData, aFirst, aLimit, 1, aLeftArg, aFunc, aRightArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final BigDecimal aLeftArg, final BinaryFunction<BigDecimal> aFunc,
            final Access1D<BigDecimal> aRightArg) {
        BigArray.invoke(myData, aFirst, aLimit, 1, aLeftArg, aFunc, aRightArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final int aStep, final BigDecimal aNmbr) {
        BigArray.fill(myData, aFirst, aLimit, aStep, aNmbr);
    }

    @Override
    protected final int getIndexOfLargest(final int aFirst, final int aLimit, final int aStep) {

        int retVal = aFirst;
        BigDecimal tmpLargest = ZERO;
        BigDecimal tmpValue;

        for (int i = aFirst; i < aLimit; i += aStep) {
            tmpValue = myData[i].abs();
            if (tmpValue.compareTo(tmpLargest) == 1) {
                tmpLargest = tmpValue;
                retVal = i;
            }
        }

        return retVal;
    }

    @Override
    protected final boolean isReal(final int index) {
        return true;
    }

    @Override
    protected final boolean isZero(final int index) {
        return TypeUtils.isZero(myData[index].doubleValue());
    }

    @Override
    protected final boolean isZeros(final int aFirst, final int aLimit, final int aStep) {

        boolean retVal = true;

        for (int i = aFirst; retVal && (i < aLimit); i += aStep) {
            retVal &= this.isZero(i);
        }

        return retVal;
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final Access1D<BigDecimal> aLeftArg, final BinaryFunction<BigDecimal> aFunc) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, aLeftArg, aFunc, this);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final BigDecimal aLeftArg, final BinaryFunction<BigDecimal> aFunc) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, aLeftArg, aFunc, this);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final BinaryFunction<BigDecimal> aFunc,
            final Access1D<BigDecimal> aRightArg) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc, aRightArg);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final BinaryFunction<BigDecimal> aFunc, final BigDecimal aRightArg) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc, aRightArg);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final ParameterFunction<BigDecimal> aFunc, final int aParam) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc, aParam);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final UnaryFunction<BigDecimal> aFunc) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc);
    }

    /**
     * @see org.ojalgo.array.BasicArray#searchAscending(java.lang.Number)
     */
    @Override
    protected final int searchAscending(final BigDecimal aNmbr) {
        return Arrays.binarySearch(myData, aNmbr);
    }

    @Override
    protected final void sortAscending() {
        Arrays.sort(myData);
    }

    @Override
    protected final Scalar<BigDecimal> toScalar(final int index) {
        return new BigScalar(myData[index]);
    }

    @Override
    protected final void visit(final int aFirst, final int aLimit, final int aStep, final AggregatorFunction<BigDecimal> aVisitor) {
        BigArray.invoke(myData, aFirst, aLimit, aStep, aVisitor);
    }

}
