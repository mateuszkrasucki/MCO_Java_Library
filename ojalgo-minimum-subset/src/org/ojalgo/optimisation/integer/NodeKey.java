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
package org.ojalgo.optimisation.integer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.ojalgo.array.ArrayUtils;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.integer.IntegerSolver.BranchAndBoundNodeTask;

final class NodeKey {

    private final int[] myLowerBounds;
    private final int[] myUpperBounds;

    NodeKey(final BranchAndBoundNodeTask aNodeTask) {
        this(aNodeTask.getKey());
    }

    NodeKey(final ExpressionsBasedModel integerModel) {

        super();

        final List<Variable> tmpIntegerVariables = integerModel.getIntegerVariables();
        final int tmpLength = tmpIntegerVariables.size();

        myLowerBounds = new int[tmpLength];
        myUpperBounds = new int[tmpLength];
        Arrays.fill(myLowerBounds, Integer.MIN_VALUE);
        Arrays.fill(myUpperBounds, Integer.MAX_VALUE);

        for (int i = 0; i < tmpLength; i++) {

            final Variable tmpVariable = tmpIntegerVariables.get(i);

            final BigDecimal tmpLowerLimit = tmpVariable.getLowerLimit();
            if (tmpLowerLimit != null) {
                myLowerBounds[i] = tmpLowerLimit.setScale(0, RoundingMode.FLOOR).intValue();
            }

            final BigDecimal tmpUpperLimit = tmpVariable.getUpperLimit();
            if (tmpUpperLimit != null) {
                myUpperBounds[i] = tmpUpperLimit.setScale(0, RoundingMode.CEILING).intValue();
            }
        }
    }

    NodeKey(final NodeKey aNodeKey) {

        super();

        myLowerBounds = aNodeKey.getLowerBounds();
        myUpperBounds = aNodeKey.getUpperBounds();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NodeKey)) {
            return false;
        }
        final NodeKey other = (NodeKey) obj;
        if (!Arrays.equals(myLowerBounds, other.myLowerBounds)) {
            return false;
        }
        if (!Arrays.equals(myUpperBounds, other.myUpperBounds)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Arrays.hashCode(myLowerBounds);
        result = (prime * result) + Arrays.hashCode(myUpperBounds);
        return result;
    }

    @Override
    public String toString() {

        final StringBuilder retVal = new StringBuilder('[');

        if (myLowerBounds.length > 0) {
            this.append(retVal, 0);
        }

        for (int i = 1; i < myLowerBounds.length; i++) {
            retVal.append(',');
            retVal.append(' ');
            this.append(retVal, i);
        }

        return retVal.append(']').toString();
    }

    private void append(final StringBuilder aBuilder, final int index) {
        aBuilder.append(index);
        aBuilder.append('=');
        aBuilder.append(myLowerBounds[index]);
        aBuilder.append('<');
        aBuilder.append(myUpperBounds[index]);
    }

    private int[] getLowerBounds() {
        return ArrayUtils.copyOf(myLowerBounds);
    }

    private int[] getUpperBounds() {
        return ArrayUtils.copyOf(myUpperBounds);
    }

    double getFeasibleIntegerFraction(final int index, final double value) {

        final double tmpFeasibleValue = Math.min(Math.max(myLowerBounds[index], value), myUpperBounds[index]);
        //final double tmpFeasibleValue = value;

        return Math.abs(tmpFeasibleValue - Math.rint(tmpFeasibleValue));
    }

    BigDecimal getLowerBound(final int integerIndex) {
        return new BigDecimal(myLowerBounds[integerIndex]);
    }

    BigDecimal getUpperBound(final int integerIndex) {
        return new BigDecimal(myUpperBounds[integerIndex]);
    }

    boolean isValid(final int index, final double value, final double tolerance) {
        return ((myLowerBounds[index] - tolerance) <= value) && (value <= (myUpperBounds[index] + tolerance));
    }

    void setLowerBound(final int integerIndex, final double tooLowValue) {
        myLowerBounds[integerIndex] = (int) Math.ceil(tooLowValue);
    }

    void setUpperBound(final int integerIndex, final double tooHighValue) {
        myUpperBounds[integerIndex] = (int) Math.floor(tooHighValue);
    }

}
