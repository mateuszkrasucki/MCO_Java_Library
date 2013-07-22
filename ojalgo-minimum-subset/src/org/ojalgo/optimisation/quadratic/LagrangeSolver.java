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
package org.ojalgo.optimisation.quadratic;

import static org.ojalgo.constant.PrimitiveMath.*;
import static org.ojalgo.function.PrimitiveFunction.*;

import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.aggregator.Aggregator;
import org.ojalgo.matrix.decomposition.Cholesky;
import org.ojalgo.matrix.decomposition.CholeskyDecomposition;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.EigenvalueDecomposition;
import org.ojalgo.matrix.store.AboveBelowStore;
import org.ojalgo.matrix.store.LeftRightStore;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.ZeroStore;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;

/**
 * @author apete
 */
final class LagrangeSolver extends QuadraticSolver {

    LagrangeSolver(final ExpressionsBasedModel aModel, final Optimisation.Options solverOptions, final QuadraticSolver.Builder matrices) {
        super(aModel, solverOptions, matrices);
    }

    private QuadraticSolver buildIterationSolver(final boolean addSmallDiagonal) {

        MatrixStore<Double> tmpQ = this.getQ();
        final MatrixStore<Double> tmpC = this.getC();

        if (addSmallDiagonal) {

            final PhysicalStore<Double> tmpCopyQ = tmpQ.copy();

            final double tmpLargest = tmpCopyQ.aggregateAll(Aggregator.LARGEST);
            final double tmpRelativelySmall = MACHINE_DOUBLE_ERROR * tmpLargest;
            final double tmpPracticalLimit = MACHINE_DOUBLE_ERROR + IS_ZERO;
            final double tmpSmallToAdd = Math.max(tmpRelativelySmall, tmpPracticalLimit);

            final UnaryFunction<Double> tmpFunc = ADD.second(tmpSmallToAdd);

            tmpCopyQ.modifyDiagonal(0, 0, tmpFunc);
            tmpQ = tmpCopyQ;
        }

        if (this.hasEqualityConstraints()) {

            final MatrixStore<Double> tmpAE = this.getAE();
            final MatrixStore<Double> tmpBE = this.getBE();

            final int tmpZeroSize = tmpAE.getRowDim();

            final MatrixStore<Double> tmpUpperLeftAE = tmpQ;
            final MatrixStore<Double> tmpUpperRightAE = tmpAE.builder().transpose().build();
            final MatrixStore<Double> tmpLowerLefAE = tmpAE;
            final MatrixStore<Double> tmpLowerRightAE = ZeroStore.makePrimitive(tmpZeroSize, tmpZeroSize);

            final MatrixStore<Double> tmpSubAE = new AboveBelowStore<Double>(new LeftRightStore<Double>(tmpUpperLeftAE, tmpUpperRightAE),
                    new LeftRightStore<Double>(tmpLowerLefAE, tmpLowerRightAE));

            final MatrixStore<Double> tmpUpperBE = tmpC;
            final MatrixStore<Double> tmpLowerBE = tmpBE;

            final MatrixStore<Double> tmpSubBE = new AboveBelowStore<Double>(tmpUpperBE, tmpLowerBE);

            return new Builder().equalities(tmpSubAE, tmpSubBE).build(options);

        } else {

            return new Builder().equalities(tmpQ, tmpC).build(options);
        }
    }

    private void extractSolution(final QuadraticSolver aSolver) {

        final MatrixStore<Double> tmpSolutionX = aSolver.getSolutionX();

        final int tmpCountVariables = this.countVariables();
        final int tmpCountEqualityConstraints = this.countEqualityConstraints();

        for (int i = 0; i < tmpCountVariables; i++) {
            this.setX(i, tmpSolutionX.doubleValue(i));
        }

        for (int i = 0; i < tmpCountEqualityConstraints; i++) {
            this.setLE(i, tmpSolutionX.doubleValue(tmpCountVariables + i));
        }
    }

    @Override
    protected boolean initialise(final Result kickStart) {
        return true;
    }

    @Override
    protected boolean needsAnotherIteration() {
        return this.countIterations() < 1;
    }

    @Override
    protected void performIteration() {

        QuadraticSolver tmpSolver = this.buildIterationSolver(false);

        Optimisation.Result tmpResult = tmpSolver.solve();

        if (tmpResult.getState().isFeasible()) {

            this.extractSolution(tmpSolver);

            this.setState(State.OPTIMAL);

        } else {

            tmpSolver = this.buildIterationSolver(true);
            tmpResult = tmpSolver.solve();

            if (tmpResult.getState().isFeasible()) {

                this.extractSolution(tmpSolver);

                this.setState(State.OPTIMAL);

            } else {

                this.resetX();
                this.setState(State.INFEASIBLE);
            }
        }
    }

    @Override
    protected boolean validate() {

        boolean retVal = true;
        this.setState(State.VALID);

        try {

            final MatrixStore<Double> tmpQ = this.getQ();

            final Cholesky<Double> tmpCholesky = CholeskyDecomposition.makePrimitive();
            tmpCholesky.compute(tmpQ, true);

            if (!tmpCholesky.isSPD()) {
                // Not positive definite. Check if at least positive semidefinite.

                final Eigenvalue<Double> tmpEigenvalue = EigenvalueDecomposition.makePrimitive(true);
                tmpEigenvalue.compute(tmpQ, true);

                final MatrixStore<Double> tmpD = tmpEigenvalue.getD();

                for (int ij = 0; retVal && (ij < tmpD.getMinDim()); ij++) {
                    if (tmpD.doubleValue(ij, ij) < ZERO) {
                        retVal = false;
                        this.setState(State.INVALID);
                    }
                }

            }

            if (retVal) {
                // Q ok, check AE

                //                final MatrixStore<Double> tmpAE = this.getAE();
                //
                //                final LU<Double> tmpLU = LUDecomposition.makePrimitive();
                //                tmpLU.compute(tmpAE);
                //
                //                if (tmpLU.getRank() != tmpAE.getRowDim()) {
                //                    retVal = false;
                //                    this.setState(State.INVALID);
                //                }
            }

        } catch (final Exception ex) {

            retVal = false;
            this.setState(State.FAILED);
        }

        return retVal;
    }

}
