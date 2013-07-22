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

import org.ojalgo.constant.BigMath;
import org.ojalgo.constant.PrimitiveMath;
import org.ojalgo.function.BigFunction;
import org.ojalgo.type.TypeUtils;
import org.ojalgo.type.context.NumberContext;

public final class BigScalar extends Number implements Scalar<BigDecimal> {

    public static final boolean IS_INFINITE = false;
    public static final boolean IS_NOT_A_NUMBER = false;
    public static final boolean IS_REAL = true;

    public static final BigScalar ONE = new BigScalar(BigMath.ONE);
    public static final BigScalar ZERO = new BigScalar();

    private static final MathContext MATH = MathContext.DECIMAL128;

    public static boolean isAbsolute(final BigDecimal value) {
        return value.compareTo(BigMath.ZERO) >= 0;
    }

    public static boolean isPositive(final BigDecimal value) {
        return value.compareTo(BigMath.ZERO) > 0;
    }

    public static boolean isZero(final BigDecimal value) {
        return value.compareTo(BigMath.ZERO) == 0;
    }

    private final BigDecimal myNumber;

    public BigScalar(final BigDecimal aNmbr) {

        super();

        myNumber = aNmbr;
    }

    public BigScalar(final Number aNmbr) {

        super();

        myNumber = TypeUtils.toBigDecimal(aNmbr);
    }

    private BigScalar() {

        super();

        myNumber = BigMath.ZERO;
    }

    public BigScalar add(final BigDecimal aNmbr) {
        return new BigScalar(myNumber.add(aNmbr));
    }

    public BigScalar add(final double aNmbr) {
        return this.add(new BigDecimal(aNmbr));
    }

    public int compareTo(final BigDecimal reference) {
        return myNumber.compareTo(reference);
    }

    public BigScalar conjugate() {
        return this;
    }

    public BigScalar divide(final BigDecimal aNmbr) {
        return new BigScalar(myNumber.divide(aNmbr, MATH));
    }

    public Scalar<BigDecimal> divide(final double aNmbr) {
        return this.divide(new BigDecimal(aNmbr));
    }

    @Override
    public double doubleValue() {
        return myNumber.doubleValue();
    }

    public BigScalar enforce(final NumberContext aCntxt) {
        return new BigScalar(aCntxt.enforce(myNumber));
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
        final BigDecimal other = ((Scalar<?>) obj).toBigDecimal();
        if (myNumber == null) {
            if (other != null) {
                return false;
            }
        } else if (!myNumber.equals(other)) {
            return false;
        }
        return true;
    }

    @Override
    public float floatValue() {
        return myNumber.floatValue();
    }

    public double getArgument() {
        return myNumber.signum() == -1 ? PrimitiveMath.PI : PrimitiveMath.ZERO;
    }

    public double getImaginary() {
        return PrimitiveMath.ZERO;
    }

    public double getModulus() {
        return myNumber.abs().doubleValue();
    }

    public BigDecimal getNumber() {
        return myNumber;
    }

    public double getReal() {
        return myNumber.doubleValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((myNumber == null) ? 0 : myNumber.hashCode());
        return result;
    }

    @Override
    public int intValue() {
        return myNumber.intValueExact();
    }

    public BigScalar invert() {
        return ONE.divide(myNumber);
    }

    public boolean isAbsolute() {
        return BigScalar.isAbsolute(myNumber);
    }

    public boolean isInfinite() {
        return BigScalar.IS_INFINITE;
    }

    public boolean isNaN() {
        return BigScalar.IS_NOT_A_NUMBER;
    }

    public boolean isPositive() {
        return BigScalar.isPositive(myNumber);
    }

    public boolean isReal() {
        return BigScalar.IS_REAL;
    }

    public boolean isZero() {
        return BigScalar.isZero(myNumber);
    }

    @Override
    public long longValue() {
        return myNumber.longValueExact();
    }

    public BigScalar multiply(final BigDecimal aNmbr) {
        return new BigScalar(myNumber.multiply(aNmbr));
    }

    public Scalar<BigDecimal> multiply(final double aNmbr) {
        return this.multiply(new BigDecimal(aNmbr));
    }

    public BigScalar negate() {
        return new BigScalar(myNumber.negate());
    }

    public BigScalar power(final int anExp) {
        return new BigScalar(BigFunction.POWER.invoke(myNumber, anExp));
    }

    public BigScalar root(final int anExp) {
        return new BigScalar(BigFunction.ROOT.invoke(myNumber, anExp));
    }

    public Scalar<BigDecimal> round(final NumberContext aCntxt) {
        return new BigScalar(aCntxt.round(myNumber));
    }

    public BigScalar signum() {
        return new BigScalar(BigFunction.SIGNUM.invoke(myNumber));
    }

    public BigScalar subtract(final BigDecimal aNmbr) {
        return new BigScalar(myNumber.subtract(aNmbr));
    }

    public Scalar<BigDecimal> subtract(final double aNmbr) {
        return this.subtract(new BigDecimal(aNmbr));
    }

    public BigDecimal toBigDecimal() {
        return myNumber;
    }

    public ComplexNumber toComplexNumber() {
        return ComplexNumber.makeReal(myNumber.doubleValue());
    }

    public String toPlainString(final NumberContext aCntxt) {
        return aCntxt.enforce(myNumber).toPlainString();
    }

    public RationalNumber toRationalNumber() {
        return new RationalNumber(myNumber);
    }

    @Override
    public String toString() {
        return myNumber.toString();
    }

    public String toString(final NumberContext aCntxt) {
        return aCntxt.enforce(myNumber).toString();
    }
}
