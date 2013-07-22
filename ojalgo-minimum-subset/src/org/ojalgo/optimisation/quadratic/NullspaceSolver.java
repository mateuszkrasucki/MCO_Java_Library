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

import org.ojalgo.function.PrimitiveFunction;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.decomposition.SingularValueDecomposition;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.optimisation.ExpressionsBasedModel;

final class NullspaceSolver extends QuadraticSolver {

    public NullspaceSolver(final ExpressionsBasedModel aModel, final Options solverOptions, final QuadraticSolver.Builder matrices) {
        super(aModel, solverOptions, matrices);
    }

    @Override
    protected boolean initialise(final Result kickStart) {
        return true;
    }

    @Override
    protected boolean needsAnotherIteration() {
        return false;
    }

    @Override
    protected void performIteration() {

        final MatrixStore<Double> tmpAE = this.getAE();
        final MatrixStore<Double> tmpBE = this.getBE();

        final SingularValue<Double> tmpQR = SingularValueDecomposition.makePrimitive();

        QuadraticSolver.Builder tmpBuilder = new Builder();
        tmpBuilder.equalities(tmpAE, tmpBE);
        final QuadraticSolver tmpFeasibleSolver = tmpBuilder.build();
        final Result tmpFeasibleResult = tmpFeasibleSolver.solve();

        if (tmpFeasibleResult.getState().isFeasible()) {

            this.logDebug("Feasible Solution: " + tmpFeasibleResult);
            this.logDebug("Body: " + tmpAE.multiplyRight(tmpFeasibleSolver.getSolutionX()).copy().toString());
            this.logDebug("RHS: " + tmpBE.copy().toString());
            final PhysicalStore<Double> tmpResidual = tmpBE.copy();
            tmpResidual.fillMatching(tmpBE, PrimitiveFunction.SUBTRACT, tmpAE.multiplyRight(tmpFeasibleSolver.getSolutionX()));
            this.logDebug("Residual RHS: " + tmpResidual.toString());

            tmpQR.compute(tmpAE, false, true);

            final PhysicalStore<Double> tmpNullspaceQR = tmpQR.getQ2().builder().columns(tmpQR.getRank(), tmpAE.getColDim()).build().copy();

            final MatrixStore<Double> tmpSubAE = this.getQ().multiplyLeft(tmpNullspaceQR.transpose()).multiplyRight(tmpNullspaceQR);
            final PhysicalStore<Double> tmpCopy = this.getQ().multiplyRight(tmpFeasibleSolver.getSolutionX()).copy();
            tmpCopy.fillMatching(tmpCopy, PrimitiveFunction.ADD, this.getC());
            tmpCopy.modifyAll(PrimitiveFunction.NEGATE);
            final MatrixStore<Double> tmpSubBE = tmpCopy.multiplyLeft(tmpNullspaceQR.transpose());

            tmpBuilder = new Builder();
            tmpBuilder.equalities(tmpSubAE, tmpSubBE);
            final QuadraticSolver tmpMainSolver = tmpBuilder.build();
            final Result tmpMainResult = tmpFeasibleSolver.solve();

            if (tmpMainResult.getState().isFeasible()) {

                this.getX().fillMatching(tmpFeasibleSolver.getSolutionX(), PrimitiveFunction.ADD, tmpNullspaceQR.multiplyRight(tmpMainSolver.getSolutionX()));

                this.setState(State.OPTIMAL);

            } else {

                this.resetX();
                this.resetLE();
                this.resetLI();

                this.setState(State.FAILED);
            }

        } else {

            this.resetX();
            this.resetLE();
            this.resetLI();

            this.setState(State.INFEASIBLE);
        }

    }

    @Override
    protected boolean validate() {

        final boolean retVal = true;
        this.setState(State.VALID);

        return retVal;
    }

}
