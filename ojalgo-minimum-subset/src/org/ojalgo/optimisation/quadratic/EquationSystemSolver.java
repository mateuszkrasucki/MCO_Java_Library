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

import org.ojalgo.function.aggregator.AggregatorFunction;
import org.ojalgo.function.aggregator.PrimitiveAggregator;
import org.ojalgo.matrix.decomposition.DecompositionStore;
import org.ojalgo.matrix.decomposition.LU;
import org.ojalgo.matrix.decomposition.LUDecomposition;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.decomposition.SingularValueDecomposition;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;

/**
 * When the KKT matrix is nonsingular, there is a unique optimal primal-dual pair (x,v).
 * 
 * If the KKT matrix is singular, but the KKT system is solvable, any solution yields an optimal pair (x,v).
 * 
 * If the KKT system is not solvable, the quadratic optimization problem is unbounded below or infeasible.
 * 
 * 
 * 
 * @author apete
 */
final class EquationSystemSolver extends QuadraticSolver {

    EquationSystemSolver(final ExpressionsBasedModel aModel, final Optimisation.Options solverOptions, final QuadraticSolver.Builder matrices) {
        super(aModel, solverOptions, matrices);
        //options.debug = System.out;
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

        final MatrixStore<Double> tmpAE = this.getAE();
        final MatrixStore<Double> tmpBE = this.getBE();

        final DecompositionStore<Double> tmpX = (DecompositionStore<Double>) this.getX();

        final LU<Double> tmpLU = LUDecomposition.makePrimitive();

        //        BasicLogger.logDebug("A", tmpAE);
        //        BasicLogger.logDebug("B", tmpBE);

        tmpLU.compute(tmpAE);

        //        BasicLogger.logDebug("X", tmpLU.solve(tmpBE));
        //        BasicLogger.logDebug("L", tmpLU.getL());
        //        BasicLogger.logDebug("U", tmpLU.getU());

        if (tmpLU.isSquareAndNotSingular()) {

            if (this.isDebug()) {
                this.logDebug("LU solvable");
            }

            final MatrixStore<Double> tmpSolution = tmpLU.solve(tmpBE, tmpX);
            this.setState(State.DISTINCT);
            if (tmpSolution != tmpX) {
                this.fillX(tmpSolution);
            }

        } else {

            if (this.isDebug()) {
                this.logDebug("LU not solvable, trying SVD");
            }

            final SingularValue<Double> tmpSVD = SingularValueDecomposition.makePrimitive();

            tmpSVD.compute(tmpAE);

            if (tmpSVD.isSolvable()) {

                if (this.isDebug()) {
                    this.logDebug("SVD solvable");
                }

                final MatrixStore<Double> tmpSolution = tmpSVD.solve(tmpBE, tmpX);
                this.setState(State.OPTIMAL);
                if (tmpSolution != tmpX) {
                    this.fillX(tmpSolution);
                }

                final AggregatorFunction<Double> tmpFrobNormCalc = PrimitiveAggregator.getCollection().norm2();
                final MatrixStore<Double> tmpSlack = this.getSE();
                tmpSlack.visitAll(tmpFrobNormCalc);

                if (!options.solution.isZero(tmpFrobNormCalc.doubleValue())) {

                    if (this.isDebug()) {
                        this.logDebug("Solution not accurate enough!");
                    }

                    this.resetX();
                    this.setState(State.INFEASIBLE);

                } else {

                    //                    BasicLogger.logDebug("LU failed, but SVD succeeded");
                    //                    BasicLogger.logDebug(this.toString());
                }

            } else {

                if (this.isDebug()) {
                    this.logDebug("SVD not solvable");
                }

                this.resetX();
                this.setState(State.INFEASIBLE);

                throw new IllegalArgumentException("Couldn't solve this problem!");
            }
        }

    }

    @Override
    protected boolean validate() {

        final boolean retVal = true;
        this.setState(State.VALID);

        return retVal;
    }

}
