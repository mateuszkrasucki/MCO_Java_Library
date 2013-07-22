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
package org.ojalgo.scalar;

import java.math.BigDecimal;
import java.math.MathContext;

import org.ojalgo.constant.PrimitiveMath;
import org.ojalgo.function.ComplexFunction;
import org.ojalgo.type.TypeUtils;
import org.ojalgo.type.context.NumberContext;

/**
 * ComplexNumber is an immutable complex number class. It only
 * implements the most basic complex number operations.
 * {@linkplain org.ojalgo.function.ComplexFunction}
 * implements some of the more complicated ones.
 * 
 * @author apete
 * @see org.ojalgo.function.ComplexFunction
 */
public abstract class ComplexNumber extends Number implements Scalar<ComplexNumber> {

    static final class Polar extends ComplexNumber {

        private final double myArg;
        private transient double myIm = Double.NaN;

        private final double myMod;
        private transient double myRe = Double.NaN;

        Polar(final double mod, final double arg) {
            super();
            myMod = mod;
            myArg = arg;
        }

        @Override
        public ComplexNumber add(final ComplexNumber aNumber) {
            return ComplexNumber.makeRectangular(this.getReal() + aNumber.getReal(), this.getImaginary() + aNumber.getImaginary());
        }

        @Override
        public ComplexNumber add(final double aValue) {
            return ComplexNumber.makeRectangular(this.getReal() + aValue, this.getImaginary());
        }

        @Override
        public ComplexNumber conjugate() {
            return ComplexNumber.makeRectangular(this.getReal(), -this.getImaginary());
        }

        @Override
        public ComplexNumber divide(final double aValue) {
            return new ComplexNumber.Polar(myMod / Math.abs(aValue), aValue >= PrimitiveMath.ZERO ? myArg : myArg - PrimitiveMath.PI);
        }

        @Override
        public double getArgument() {
            return myArg;
        }

        @Override
        public double getImaginary() {

            if (Double.isNaN(myIm)) {

                myIm = PrimitiveMath.ZERO;

                if (myMod != PrimitiveMath.ZERO) {

                    final double tmpSin = Math.sin(myArg);

                    if (tmpSin != PrimitiveMath.ZERO) {
                        myIm = myMod * tmpSin;
                    }
                }
            }

            return myIm;
        }

        @Override
        public double getModulus() {
            return myMod;
        }

        @Override
        public double getReal() {

            if (Double.isNaN(myRe)) {

                myRe = PrimitiveMath.ZERO;

                if (myMod != PrimitiveMath.ZERO) {

                    final double tmpCos = Math.cos(myArg);

                    if (tmpCos != PrimitiveMath.ZERO) {
                        myRe = myMod * tmpCos;
                    }
                }
            }

            return myRe;
        }

        @Override
        public ComplexNumber invert() {
            return new ComplexNumber.Polar(PrimitiveMath.ONE / myMod, -myArg);
        }

        public boolean isInfinite() {
            return Double.isInfinite(myMod);
        }

        public boolean isNaN() {
            return Double.isNaN(myMod) || Double.isNaN(myArg);
        }

        public boolean isReal() {
            return TypeUtils.isZero(myArg % PrimitiveMath.PI, ARGUMENT_TOLERANCE);
        }

        @Override
        public ComplexNumber multiply(final ComplexNumber aNumber) {
            return new ComplexNumber.Polar(myMod * aNumber.getModulus(), myArg + aNumber.getArgument());
        }

        @Override
        public ComplexNumber multiply(final double aValue) {
            return new ComplexNumber.Polar(myMod * Math.abs(aValue), aValue >= PrimitiveMath.ZERO ? myArg : myArg + PrimitiveMath.PI);
        }

        @Override
        public ComplexNumber negate() {
            return ComplexNumber.makeRectangular(-this.getReal(), -this.getImaginary());
        }

        @Override
        public ComplexNumber subtract(final ComplexNumber aNumber) {
            return ComplexNumber.makeRectangular(this.getReal() - aNumber.getReal(), this.getImaginary() - aNumber.getImaginary());
        }

        @Override
        public ComplexNumber subtract(final double aValue) {
            return ComplexNumber.makeRectangular(this.getReal() - aValue, this.getImaginary());
        }

    }

    static final class Real extends ComplexNumber {

        private final double myRe;

        Real(final double re) {
            super();
            myRe = re;
        }

        @Override
        public ComplexNumber add(final ComplexNumber aNumber) {
            return aNumber.add(myRe);
        }

        @Override
        public ComplexNumber add(final double aValue) {
            return new ComplexNumber.Real(myRe + aValue);
        }

        @Override
        public ComplexNumber conjugate() {
            return this;
        }

        @Override
        public ComplexNumber divide(final double aValue) {
            return new ComplexNumber.Real(myRe / aValue);
        }

        @Override
        public double getArgument() {
            return myRe >= PrimitiveMath.ZERO ? PrimitiveMath.ZERO : PrimitiveMath.PI;
        }

        @Override
        public double getImaginary() {
            return PrimitiveMath.ZERO;
        }

        @Override
        public double getModulus() {
            return Math.abs(myRe);
        }

        @Override
        public double getReal() {
            return myRe;
        }

        @Override
        public ComplexNumber invert() {
            return new ComplexNumber.Real(PrimitiveMath.ONE / myRe);
        }

        public boolean isInfinite() {
            return Double.isInfinite(myRe);
        }

        public boolean isNaN() {
            return Double.isNaN(myRe);
        }

        public boolean isReal() {
            return true;
        }

        @Override
        public ComplexNumber multiply(final ComplexNumber aNumber) {
            return aNumber.multiply(myRe);
        }

        @Override
        public ComplexNumber multiply(final double aValue) {
            return new ComplexNumber.Real(myRe * aValue);
        }

        @Override
        public ComplexNumber negate() {
            return new ComplexNumber.Real(-myRe);
        }

        @Override
        public ComplexNumber subtract(final ComplexNumber aNumber) {
            return ComplexNumber.makeRectangular(myRe - aNumber.getReal(), -aNumber.getImaginary());
        }

        @Override
        public ComplexNumber subtract(final double aValue) {
            return new ComplexNumber.Real(myRe - aValue);
        }

    }

    static final class Rectangular extends ComplexNumber {

        private transient double myArg = Double.NaN;
        private final double myIm;

        private transient double myMod = Double.NaN;
        private final double myRe;

        Rectangular(final double re, final double im) {
            super();
            myRe = re;
            myIm = im;
        }

        @Override
        public ComplexNumber add(final ComplexNumber aNumber) {
            return new ComplexNumber.Rectangular(myRe + aNumber.getReal(), myIm + aNumber.getImaginary());
        }

        @Override
        public ComplexNumber add(final double aValue) {
            return new ComplexNumber.Rectangular(myRe + aValue, myIm);
        }

        @Override
        public ComplexNumber conjugate() {
            return new ComplexNumber.Rectangular(myRe, -myIm);
        }

        @Override
        public ComplexNumber divide(final double aValue) {
            return ComplexNumber.makeRectangular(myRe / aValue, myIm / aValue);
        }

        @Override
        public double getArgument() {

            if (Double.isNaN(myArg)) {
                myArg = Math.atan2(myIm, myRe);
            }

            return myArg;
        }

        @Override
        public double getImaginary() {
            return myIm;
        }

        @Override
        public double getModulus() {

            if (Double.isNaN(myMod)) {
                myMod = Math.hypot(myRe, myIm);
            }

            return myMod;
        }

        @Override
        public double getReal() {
            return myRe;
        }

        @Override
        public ComplexNumber invert() {
            return ComplexNumber.makePolar(PrimitiveMath.ONE / this.getModulus(), -this.getArgument());
        }

        public boolean isInfinite() {
            return Double.isInfinite(myRe) || Double.isInfinite(myIm);
        }

        public boolean isNaN() {
            return Double.isNaN(myRe) || Double.isNaN(myIm);
        }

        public boolean isReal() {
            return TypeUtils.isZero(myIm);
        }

        @Override
        public ComplexNumber multiply(final ComplexNumber aNumber) {

            final double tmpRe = aNumber.getReal();
            final double tmpIm = aNumber.getImaginary();

            return new ComplexNumber.Rectangular((myRe * tmpRe) - (myIm * tmpIm), (myRe * tmpIm) + (myIm * tmpRe));
        }

        @Override
        public ComplexNumber multiply(final double aValue) {
            return new ComplexNumber.Rectangular(myRe * aValue, myIm * aValue);
        }

        @Override
        public ComplexNumber negate() {
            return new ComplexNumber.Rectangular(-myRe, -myIm);
        }

        @Override
        public ComplexNumber subtract(final ComplexNumber aNumber) {
            return new ComplexNumber.Rectangular(myRe - aNumber.getReal(), myIm - aNumber.getImaginary());
        }

        @Override
        public ComplexNumber subtract(final double aValue) {
            return new ComplexNumber.Rectangular(myRe - aValue, myIm);
        }

    }

    public static final ComplexNumber I = ComplexNumber.makeRectangular(PrimitiveMath.ZERO, PrimitiveMath.ONE);
    public static final ComplexNumber INFINITY = ComplexNumber.makePolar(Double.POSITIVE_INFINITY, PrimitiveMath.ZERO);
    public static final ComplexNumber NEG = ComplexNumber.makeReal(PrimitiveMath.NEG);
    public static final ComplexNumber ONE = ComplexNumber.makeReal(PrimitiveMath.ONE);
    public static final ComplexNumber TWO = ComplexNumber.makeReal(PrimitiveMath.TWO);
    public static final ComplexNumber ZERO = ComplexNumber.makeReal(PrimitiveMath.ZERO);

    private static final String LEFT = "(";
    private static final String MINUS = " - ";
    private static final String PLUS = " + ";
    private static final String RIGHT = "i)";

    static final double ARGUMENT_TOLERANCE = PrimitiveMath.PI * PrimitiveMath.MACHINE_DOUBLE_ERROR;

    public static boolean isAbsolute(final ComplexNumber value) {
        return value.isAbsolute();
    }

    public static boolean isInfinite(final ComplexNumber value) {
        return value.isInfinite();
    }

    public static boolean isNaN(final ComplexNumber value) {
        return value.isNaN();
    }

    public static boolean isPositive(final ComplexNumber value) {
        return value.isPositive();
    }

    public static boolean isReal(final ComplexNumber value) {
        return value.isReal();
    }

    public static boolean isZero(final ComplexNumber value) {
        return value.isZero();
    }

    public static ComplexNumber makePolar(final double modulus, final double argument) {
        if (TypeUtils.isZero(argument % PrimitiveMath.TWO_PI)) {
            return new ComplexNumber.Real(modulus);
        } else {
            return new ComplexNumber.Polar(modulus, argument);
        }
    }

    public static ComplexNumber makeReal(final double real) {
        return new ComplexNumber.Real(real);
    }

    public static ComplexNumber makeRectangular(final double real, final double imaginary) {
        if (TypeUtils.isZero(imaginary)) {
            return new ComplexNumber.Real(real);
        } else {
            return new ComplexNumber.Rectangular(real, imaginary);
        }
    }

    ComplexNumber() {
        super();
    }

    public abstract ComplexNumber add(final ComplexNumber aNumber);

    public abstract ComplexNumber add(final double aValue);

    public int compareTo(final ComplexNumber reference) {

        int retVal = 0;

        if ((retVal = Double.compare(this.getModulus(), reference.getModulus())) == 0) {
            if ((retVal = Double.compare(this.getReal(), reference.getReal())) == 0) {
                retVal = Double.compare(this.getImaginary(), reference.getImaginary());
            }
        }

        return retVal;
    }

    public abstract ComplexNumber conjugate();

    public ComplexNumber divide(final ComplexNumber aNumber) {

        if (this.getModulus() == aNumber.getModulus()) {

            final double retMod = PrimitiveMath.ONE;
            final double retArg = this.getArgument() - aNumber.getArgument();

            return ComplexNumber.makePolar(retMod, retArg);

        } else if (aNumber.isReal() && (aNumber.getReal() != PrimitiveMath.ZERO)) {

            return this.divide(aNumber.getReal());

        } else if (this.isReal() && (this.getReal() != PrimitiveMath.ZERO)) {

            return aNumber.invert().multiply(this.getReal());

        } else {

            final double retMod = this.getModulus() / aNumber.getModulus();
            final double retArg = this.getArgument() - aNumber.getArgument();

            return ComplexNumber.makePolar(retMod, retArg);
        }
    }

    public abstract ComplexNumber divide(final double aValue);

    /**
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue() {
        if (this.getReal() < PrimitiveMath.ZERO) {
            return -this.getModulus();
        } else {
            return this.getModulus();
        }
    }

    /**
     * Will call {@linkplain NumberContext#enforce(double)} on the real
     * and imaginary parts separately.
     * 
     * @see org.ojalgo.scalar.Scalar#enforce(org.ojalgo.type.context.NumberContext)
     */
    public ComplexNumber enforce(final NumberContext aCntxt) {

        final double tmpRe = aCntxt.enforce(this.getReal());
        final double tmpIm = aCntxt.enforce(this.getImaginary());

        return ComplexNumber.makeRectangular(tmpRe, tmpIm);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Scalar<?>)) {
            return false;
        }
        final ComplexNumber other = ((Scalar<?>) obj).toComplexNumber();
        if (Double.doubleToLongBits(this.getModulus()) != Double.doubleToLongBits(other.getModulus())) {
            return false;
        }
        if (Double.doubleToLongBits(this.getReal()) != Double.doubleToLongBits(other.getReal())) {
            return false;
        }
        if (Double.doubleToLongBits(this.getImaginary()) != Double.doubleToLongBits(other.getImaginary())) {
            return false;
        }
        if (Double.doubleToLongBits(this.getArgument()) != Double.doubleToLongBits(other.getArgument())) {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue() {
        return (float) this.doubleValue();
    }

    public ComplexNumber getNumber() {
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.getArgument());
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.getImaginary());
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.getModulus());
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.getReal());
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue() {
        return (int) this.doubleValue();
    }

    public abstract ComplexNumber invert();

    public boolean isAbsolute() {
        return this.isReal() && (this.getReal() >= PrimitiveMath.ZERO);
    }

    public boolean isPositive() {
        return this.isAbsolute() && !this.isZero();
    }

    public boolean isZero() {
        return TypeUtils.isZero(this.getModulus());
    }

    /**
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue() {
        return (long) this.doubleValue();
    }

    public abstract ComplexNumber multiply(ComplexNumber aNmbr);

    public abstract ComplexNumber multiply(final double aValue);

    public abstract ComplexNumber negate();

    public Scalar<ComplexNumber> power(final int anExp) {
        return ComplexFunction.POWER.invoke(this, anExp);
    }

    public ComplexNumber root(final int anExp) {
        return ComplexFunction.ROOT.invoke(this, anExp);
    }

    /**
     * Will call {@linkplain NumberContext#round(double)} on the real
     * and imaginary parts separately.
     * 
     * @see org.ojalgo.scalar.Scalar#enforce(org.ojalgo.type.context.NumberContext)
     */
    public ComplexNumber round(final NumberContext aCntxt) {

        final double tmpRe = aCntxt.round(this.getReal());
        final double tmpIm = aCntxt.round(this.getImaginary());

        return ComplexNumber.makeRectangular(tmpRe, tmpIm);
    }

    public ComplexNumber signum() {
        if (this.isZero()) {
            return ComplexNumber.makePolar(PrimitiveMath.ONE, PrimitiveMath.ZERO);
        } else {
            return ComplexNumber.makePolar(PrimitiveMath.ONE, this.getArgument());
        }
    }

    public abstract ComplexNumber subtract(final ComplexNumber aNumber);

    public abstract ComplexNumber subtract(final double aValue);

    public BigDecimal toBigDecimal() {
        return new BigDecimal(this.doubleValue(), MathContext.DECIMAL64);
    }

    public ComplexNumber toComplexNumber() {
        return this;
    }

    public String toPlainString(final NumberContext aCntxt) {
        return aCntxt.enforce(this.toBigDecimal()).toPlainString();
    }

    public RationalNumber toRationalNumber() {
        return new RationalNumber(this.toBigDecimal());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder retVal = new StringBuilder(LEFT);

        final double tmpRe = this.getReal();
        final double tmpIm = this.getImaginary();

        retVal.append(Double.toString(tmpRe));

        if (tmpIm < PrimitiveMath.ZERO) {
            retVal.append(MINUS);
        } else {
            retVal.append(PLUS);
        }
        retVal.append(Double.toString(Math.abs(tmpIm)));

        return retVal.append(RIGHT).toString();
    }

    public String toString(final NumberContext aCntxt) {

        final StringBuilder retVal = new StringBuilder(LEFT);

        final BigDecimal tmpRe = aCntxt.enforce(new BigDecimal(this.getReal(), MathContext.DECIMAL64));
        final BigDecimal tmpIm = aCntxt.enforce(new BigDecimal(this.getImaginary(), MathContext.DECIMAL64));

        retVal.append(tmpRe.toString());

        if (tmpIm.signum() < 0) {
            retVal.append(MINUS);
        } else {
            retVal.append(PLUS);
        }
        retVal.append(tmpIm.abs().toString());

        return retVal.append(RIGHT).toString();
    }

}
