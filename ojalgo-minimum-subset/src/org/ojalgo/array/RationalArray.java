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

import static org.ojalgo.constant.PrimitiveMath.*;

import java.util.Arrays;

import org.ojalgo.access.Access1D;
import org.ojalgo.function.BinaryFunction;
import org.ojalgo.function.ParameterFunction;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.aggregator.AggregatorFunction;
import org.ojalgo.scalar.RationalNumber;
import org.ojalgo.type.TypeUtils;

/**
 * A one- and/or arbitrary-dimensional array of {@linkplain org.ojalgo.scalar.RationalNumber}.
 * 
 * @see PrimitiveArray
 * @author apete
 */
public class RationalArray extends BasicArray<RationalNumber> {

    protected static void exchange(final RationalNumber[] aData, final int aFirstA, final int aFirstB, final int aStep, final int aCount) {

        int tmpIndexA = aFirstA;
        int tmpIndexB = aFirstB;

        RationalNumber tmpVal;

        for (int i = 0; i < aCount; i++) {

            tmpVal = aData[tmpIndexA];
            aData[tmpIndexA] = aData[tmpIndexB];
            aData[tmpIndexB] = tmpVal;

            tmpIndexA += aStep;
            tmpIndexB += aStep;
        }
    }

    protected static void fill(final RationalNumber[] aData, final Access1D<?> anArg) {
        final int tmpLimit = Math.min(aData.length, anArg.size());
        for (int i = 0; i < tmpLimit; i++) {
            aData[i] = TypeUtils.toRationalNumber(anArg.get(i));
        }
    }

    protected static void fill(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep, final RationalNumber aNmbr) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aNmbr;
        }
    }

    protected static void invoke(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<RationalNumber> aLeftArg,
            final BinaryFunction<RationalNumber> aFunc, final Access1D<RationalNumber> aRightArg) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(aLeftArg.get(i), aRightArg.get(i));
        }
    }

    protected static void invoke(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<RationalNumber> aLeftArg,
            final BinaryFunction<RationalNumber> aFunc, final RationalNumber aRightArg) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(aLeftArg.get(i), aRightArg);
        }
    }

    protected static void invoke(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<RationalNumber> anArg,
            final ParameterFunction<RationalNumber> aFunc, final int aParam) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(anArg.get(i), aParam);
        }
    }

    protected static void invoke(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep, final Access1D<RationalNumber> anArg,
            final UnaryFunction<RationalNumber> aFunc) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(anArg.get(i));
        }
    }

    protected static void invoke(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep,
            final AggregatorFunction<RationalNumber> aVisitor) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aVisitor.invoke(aData[i]);
        }
    }

    protected static void invoke(final RationalNumber[] aData, final int aFirst, final int aLimit, final int aStep, final RationalNumber aLeftArg,
            final BinaryFunction<RationalNumber> aFunc, final Access1D<RationalNumber> aRightArg) {
        for (int i = aFirst; i < aLimit; i += aStep) {
            aData[i] = aFunc.invoke(aLeftArg, aRightArg.get(i));
        }
    }

    private final RationalNumber[] myData;

    protected RationalArray(final int aLength) {

        super(aLength);

        myData = new RationalNumber[aLength];

        this.fill(0, aLength, 1, RationalNumber.ZERO);
    }

    protected RationalArray(final RationalNumber[] anArray) {

        super(anArray.length);

        myData = anArray;
    }

    public final double doubleValue(final long index) {
        return myData[(int) index].doubleValue();
    }

    @Override
    public boolean equals(final Object anObj) {
        if (anObj instanceof RationalArray) {
            return Arrays.equals(myData, ((RationalArray) anObj).data());
        } else {
            return super.equals(anObj);
        }
    }

    public final void fillAll(final RationalNumber aNmbr) {
        RationalArray.fill(myData, 0, myData.length, 1, aNmbr);
    }

    public void fillRange(final long first, final long limit, final RationalNumber value) {
        RationalArray.fill(myData, (int) first, (int) limit, 1, value);
    }

    public RationalNumber get(final long index) {
        return myData[(int) index];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(myData);
    }

    public final boolean isAbsolute(final long index) {
        return RationalNumber.isAbsolute(myData[(int) index]);
    }

    public final boolean isInfinite(final long index) {
        return RationalNumber.isInfinite(myData[(int) index]);
    }

    public final boolean isNaN(final long index) {
        return RationalNumber.isNaN(myData[(int) index]);
    }

    public final boolean isPositive(final long index) {
        return RationalNumber.isPositive(myData[(int) index]);
    }

    public final boolean isReal(final long index) {
        return RationalNumber.IS_REAL;
    }

    public final boolean isZero(final long index) {
        return RationalNumber.isZero(myData[(int) index]);
    }

    /**
     * @deprecated Use {@link #set(long,double)} instead
     */
    @Deprecated
    public final void set(final int index, final double aNmbr) {
        this.set(index, aNmbr);
    }

    @Override
    public final RationalNumber set(final int index, final Number value) {
        final RationalNumber retVal = myData[index];
        myData[index] = TypeUtils.toRationalNumber(value);
        return retVal;
    }

    public final void set(final long index, final double value) {
        myData[(int) index] = new RationalNumber(value);
    }

    public void set(final long index, final Number value) {
        myData[(int) index] = TypeUtils.toRationalNumber(value);
    }

    protected final RationalNumber[] copyOfData() {
        return ArrayUtils.copyOf(myData);
    }

    protected final RationalNumber[] data() {
        return myData;
    }

    @Override
    protected final void exchange(final int aFirstA, final int aFirstB, final int aStep, final int aCount) {
        RationalArray.exchange(myData, aFirstA, aFirstB, aStep, aCount);
    }

    @Override
    protected void fill(final Access1D<?> anArg) {
        RationalArray.fill(myData, anArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final Access1D<RationalNumber> aLeftArg, final BinaryFunction<RationalNumber> aFunc,
            final Access1D<RationalNumber> aRightArg) {
        RationalArray.invoke(myData, aFirst, aLimit, 1, aLeftArg, aFunc, aRightArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final Access1D<RationalNumber> aLeftArg, final BinaryFunction<RationalNumber> aFunc,
            final RationalNumber aRightArg) {
        RationalArray.invoke(myData, aFirst, aLimit, 1, aLeftArg, aFunc, aRightArg);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final int aStep, final RationalNumber aNmbr) {
        RationalArray.fill(myData, aFirst, aLimit, aStep, aNmbr);
    }

    @Override
    protected final void fill(final int aFirst, final int aLimit, final RationalNumber aLeftArg, final BinaryFunction<RationalNumber> aFunc,
            final Access1D<RationalNumber> aRightArg) {
        RationalArray.invoke(myData, aFirst, aLimit, 1, aLeftArg, aFunc, aRightArg);
    }

    @Override
    protected final int getIndexOfLargest(final int aFirst, final int aLimit, final int aStep) {

        int retVal = aFirst;
        double tmpLargest = ZERO;
        double tmpValue;

        for (int i = aFirst; i < aLimit; i += aStep) {
            tmpValue = myData[i].getModulus();
            if (tmpValue > tmpLargest) {
                tmpLargest = tmpValue;
                retVal = i;
            }
        }

        return retVal;
    }

    @Override
    protected final boolean isReal(final int index) {
        return myData[index].isReal();
    }

    @Override
    protected final boolean isZero(final int index) {
        return myData[index].isZero();
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
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final Access1D<RationalNumber> aLeftArg,
            final BinaryFunction<RationalNumber> aFunc) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, aLeftArg, aFunc, this);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final BinaryFunction<RationalNumber> aFunc,
            final Access1D<RationalNumber> aRightArg) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc, aRightArg);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final BinaryFunction<RationalNumber> aFunc, final RationalNumber aRightArg) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc, aRightArg);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final ParameterFunction<RationalNumber> aFunc, final int aParam) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc, aParam);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final RationalNumber aLeftArg, final BinaryFunction<RationalNumber> aFunc) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, aLeftArg, aFunc, this);
    }

    @Override
    protected final void modify(final int aFirst, final int aLimit, final int aStep, final UnaryFunction<RationalNumber> aFunc) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, this, aFunc);
    }

    /**
     * @see org.ojalgo.array.BasicArray#searchAscending(java.lang.Number)
     */
    @Override
    protected final int searchAscending(final RationalNumber aNmbr) {
        return Arrays.binarySearch(myData, aNmbr);
    }

    @Override
    protected final void sortAscending() {
        Arrays.sort(myData);
    }

    @Override
    protected final RationalNumber toScalar(final int index) {
        return myData[index];
    }

    @Override
    protected final void visit(final int aFirst, final int aLimit, final int aStep, final AggregatorFunction<RationalNumber> aVisitor) {
        RationalArray.invoke(myData, aFirst, aLimit, aStep, aVisitor);
    }

}
