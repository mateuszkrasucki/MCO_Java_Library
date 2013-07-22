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

import static org.ojalgo.constant.PrimitiveMath.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.ojalgo.RecoverableCondition;
import org.ojalgo.array.SimpleArray;
import org.ojalgo.array.SimpleArray.Primitive;
import org.ojalgo.concurrent.DaemonPoolExecutor;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.GenericSolver;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.type.TypeUtils;

/**
 * IntegerSolver
 *
 * @author apete
 */
public final class IntegerSolver extends GenericSolver {

    static final class BranchAndBoundNodeTask extends RecursiveTask<Boolean> {

        private final int myIntegerIndex;
        private final NodeKey myKey;
        private transient ExpressionsBasedModel myModel; // Copied/relaxed model
        private final ExpressionsBasedModel myParentModel;
        private final IntegerSolver mySolver;

        @SuppressWarnings("unused")
        private BranchAndBoundNodeTask() {
            this(null, -1);
        }

        BranchAndBoundNodeTask(final BranchAndBoundNodeTask parentNode, final int integerIndex) {

            super();

            myKey = new NodeKey(parentNode);

            myParentModel = parentNode.getModel();

            myIntegerIndex = integerIndex;

            myModel = null;
            mySolver = parentNode.getSolver();
        }

        BranchAndBoundNodeTask(final ExpressionsBasedModel integerModel, final IntegerSolver aSolver) {

            super();

            myKey = new NodeKey(integerModel);

            myParentModel = integerModel;

            myIntegerIndex = -1;

            myModel = null;
            mySolver = aSolver;
        }

        public BranchAndBoundNodeTask createLowerBranch(final int integerIndex, final double nonIntegerValue, final Optimisation.Result nodeResult) {

            final BranchAndBoundNodeTask retVal = new BranchAndBoundNodeTask(this, integerIndex);

            retVal.getKey().setUpperBound(integerIndex, nonIntegerValue);

            return retVal;
        }

        public BranchAndBoundNodeTask createUpperBranch(final int integerIndex, final double nonIntegerValue, final Optimisation.Result nodeResult) {

            final BranchAndBoundNodeTask retVal = new BranchAndBoundNodeTask(this, integerIndex);

            retVal.getKey().setLowerBound(integerIndex, nonIntegerValue);

            return retVal;
        }

        @Override
        public boolean equals(final Object obj) {
            return myKey.equals(obj);
        }

        @Override
        public int hashCode() {
            return myKey.hashCode();
        }

        @Override
        public String toString() {
            return myKey.toString();
        }

        @Override
        protected Boolean compute() {

            if (mySolver.isDebug()) {
                mySolver.logDebug("\nBranch&Bound Node");
                mySolver.logDebug(myKey.toString());
                mySolver.logDebug(mySolver.toString());
            }

            if (!mySolver.isIterationAllowed() || !mySolver.isIterationNecessary()) {
                if (mySolver.isDebug()) {
                    mySolver.logDebug("Reached iterations or time limit - stop!");
                }
                return false;
            }

            if (mySolver.isExplored(this)) {
                if (mySolver.isDebug()) {
                    mySolver.logDebug("Node previously explored!");
                }
                return true;
            } else {
                mySolver.markAsExplored(this);
            }

            final ExpressionsBasedModel tmpModel = this.getModel();
            final Optimisation.Result tmpNodeResult = tmpModel.solve();

            try {
                mySolver.incrementIterationsCount();
            } catch (final RecoverableCondition anException) {
                return false;
            }

            if (tmpNodeResult.getState().isOptimal()) {
                if (mySolver.isDebug()) {
                    mySolver.logDebug("Node solved to optimality!");
                }

                if (mySolver.options.validate && !tmpModel.validate(tmpNodeResult)) {
                    // This should not be possible. There is a bug somewhere.
                    mySolver.logDebug("Node solution marked as OPTIMAL, but is actually INVALID/INFEASIBLE/FAILED. Stop this branch!");
                    //                    mySolver.logDebug(myKey.toString());
                    //                    mySolver.logDebug(tmpModel.toString());
                    //                    final GenericSolver tmpDefaultSolver = tmpModel.getDefaultSolver();
                    //                    tmpDefaultSolver.solve();
                    //                    mySolver.logDebug(tmpDefaultSolver.toString());
                    return false;
                }

                final int tmpBranchIndex = mySolver.identifyNonIntegerVariable(tmpNodeResult, myKey);
                final double tmpSolutionValue = mySolver.evaluateFunction(tmpNodeResult);

                if (tmpBranchIndex == -1) {
                    if (mySolver.isDebug()) {
                        mySolver.logDebug("Integer solution! Store it among the others, and stop this branch!");
                    }

                    final Optimisation.Result tmpIntegerSolutionResult = new Optimisation.Result(Optimisation.State.FEASIBLE, tmpSolutionValue, tmpNodeResult);

                    mySolver.storeResult(tmpIntegerSolutionResult);

                    if (mySolver.isDebug()) {
                        mySolver.logDebug(mySolver.getBestResultSoFar().toString());
                    }

                } else {
                    if (mySolver.isDebug()) {
                        mySolver.logDebug("Not an Integer Solution: " + tmpSolutionValue);
                    }

                    final double tmpVariableValue = tmpNodeResult.doubleValue(mySolver.getGlobalIndex(tmpBranchIndex));

                    if (mySolver.isGoodEnoughToContinueBranching(tmpSolutionValue)) {
                        if (mySolver.isDebug()) {
                            mySolver.logDebug("Still hope, branching on {} @ {} >>> {}", tmpBranchIndex, tmpVariableValue,
                                    tmpModel.getVariable(mySolver.getGlobalIndex(tmpBranchIndex)));
                        }

                        final BranchAndBoundNodeTask tmpLowerBranchTask = this.createLowerBranch(tmpBranchIndex, tmpVariableValue, tmpNodeResult);
                        final BranchAndBoundNodeTask tmpUpperBranchTask = this.createUpperBranch(tmpBranchIndex, tmpVariableValue, tmpNodeResult);

                        //   return tmpLowerBranchTask.compute() && tmpUpperBranchTask.compute();

                        tmpUpperBranchTask.fork();

                        final boolean tmpLowerBranchValue = tmpLowerBranchTask.compute();

                        if (tmpLowerBranchValue) {
                            return tmpUpperBranchTask.join();
                        } else {
                            tmpUpperBranchTask.tryUnfork();
                            tmpUpperBranchTask.cancel(true);
                            return false;
                        }

                    } else {
                        if (mySolver.isDebug()) {
                            mySolver.logDebug("Can't find better integer solutions - stop this branch!");
                        }
                    }
                }

            } else {
                if (mySolver.isDebug()) {
                    mySolver.logDebug("Failed to solve problem - stop this branch!");
                }
            }

            return true;
        }

        NodeKey getKey() {
            return myKey;
        }

        ExpressionsBasedModel getModel() {

            if (myModel == null) {

                myModel = myParentModel.relax(false);

                if (myIntegerIndex >= 0) {

                    final BigDecimal tmpLowerBound = myKey.getLowerBound(myIntegerIndex);
                    final BigDecimal tmpUpperBound = myKey.getUpperBound(myIntegerIndex);

                    final int tmpGlobalIndex = mySolver.getGlobalIndex(myIntegerIndex);

                    final Variable tmpVariable = myModel.getVariable(tmpGlobalIndex);
                    tmpVariable.lower(tmpLowerBound);
                    tmpVariable.upper(tmpUpperBound);

                    tmpVariable.setValue(tmpVariable.getValue().max(tmpLowerBound).min(tmpUpperBound));
                }
            }

            return myModel;
        }

        IntegerSolver getSolver() {
            return mySolver;
        }

    }

    public static IntegerSolver make(final ExpressionsBasedModel aModel) {
        return new IntegerSolver(aModel, null);
    }

    private volatile Optimisation.Result myBestResultSoFar = null;

    private final Set<NodeKey> myExploredNodes = Collections.synchronizedSet(new HashSet<NodeKey>());
    private final int[] myIntegerIndeces;
    private final AtomicInteger myIntegerSolutionsCount = new AtomicInteger();
    private final boolean myMinimisation;

    IntegerSolver(final ExpressionsBasedModel aModel, final Options solverOptions) {

        super(aModel, solverOptions);

        myMinimisation = aModel.isMinimisation();

        final List<Variable> tmpIntegerVariables = aModel.getIntegerVariables();

        myIntegerIndeces = new int[tmpIntegerVariables.size()];

        for (int i = 0; i < myIntegerIndeces.length; i++) {
            final Variable tmpVariable = tmpIntegerVariables.get(i);
            myIntegerIndeces[i] = aModel.indexOf(tmpVariable);
        }

        //options.debug = System.out;
    }

    public Result solve(final Result kickStart) {

        if (kickStart != null) {
            this.storeResult(kickStart);
        }

        this.resetIterationsCount();

        final BranchAndBoundNodeTask tmpNodeTask = new BranchAndBoundNodeTask(this.getModel(), this);

        final boolean tmpNormalExit = DaemonPoolExecutor.INSTANCE.invoke(tmpNodeTask);

        Optimisation.Result retVal = this.getBestResultSoFar();

        if (retVal.getState().isFeasible()) {

            if (tmpNormalExit) {
                retVal = new Optimisation.Result(State.OPTIMAL, retVal);
            } else {
                retVal = new Optimisation.Result(State.FEASIBLE, retVal);
            }

        } else {

            if (tmpNormalExit) {
                retVal = new Optimisation.Result(State.INFEASIBLE, retVal);
            } else {
                retVal = new Optimisation.Result(State.FAILED, retVal);
            }
        }

        final List<Variable> tmpVariables = this.getModel().getVariables();
        for (int v = 0; v < tmpVariables.size(); v++) {
            tmpVariables.get(v).setValue(TypeUtils.toBigDecimal(retVal.get(v), options.solution));
        }

        return retVal;
    }

    @Override
    public String toString() {
        return TypeUtils.format("Solutions={} Nodes/Iterations={} {}", this.countIntegerSolutions(), this.countExploredNodes(), this.getBestResultSoFar());
    }

    @Override
    protected MatrixStore<Double> extractSolution() {
        return PrimitiveDenseStore.FACTORY.columns(this.getBestResultSoFar());
    }

    @Override
    protected boolean initialise(final Result kickStart) {
        return true;
    }

    @Override
    protected boolean needsAnotherIteration() {
        return !this.getState().isOptimal();
    }

    @Override
    protected boolean validate() {

        boolean retVal = true;
        this.setState(State.VALID);

        try {

            if (!(retVal = this.getModel().validate())) {
                retVal = false;
                this.setState(State.INVALID);
            }

        } catch (final Exception ex) {

            retVal = false;
            this.setState(State.FAILED);
        }

        return retVal;
    }

    int countExploredNodes() {
        return myExploredNodes.size();
    }

    int countIntegerSolutions() {
        return myIntegerSolutionsCount.intValue();
    }

    Optimisation.Result getBestResultSoFar() {

        if (myBestResultSoFar != null) {

            return myBestResultSoFar;

        } else {

            final State tmpSate = State.INVALID;
            final double tmpValue = myMinimisation ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            final Primitive tmpMakePrimitive = SimpleArray.makePrimitive(this.getModel().countVariables());

            return new Optimisation.Result(tmpSate, tmpValue, tmpMakePrimitive);
        }
    }

    int getGlobalIndex(final int integerIndex) {
        return myIntegerIndeces[integerIndex];
    }

    int identifyNonIntegerVariable(final Optimisation.Result aNodeResult, final NodeKey aNodeKey) {

        //final MatrixStore<Double> tmpGradient = this.getGradient(aNodeResult);

        int retVal = -1;

        final double tmpTolerance = options.solution.error();

        double tmpFraction;
        double tmpMaxFraction = ZERO;

        //final int tmpSize = myIntegers.length / 2;

        for (int i = 0; i < myIntegerIndeces.length; i++) {

            tmpFraction = aNodeKey.getFeasibleIntegerFraction(i, aNodeResult.doubleValue(myIntegerIndeces[i]));

            if ((tmpFraction > tmpTolerance) && (tmpFraction > tmpMaxFraction)) {
                retVal = i;
                tmpMaxFraction = tmpFraction;
            }

        }

        return retVal;
    }

    boolean isExplored(final BranchAndBoundNodeTask aNodeTask) {
        return myExploredNodes.contains(aNodeTask.getKey());
    }

    boolean isGoodEnoughToContinueBranching(final double aNonIntegerValue) {
        // return true;
        if (myBestResultSoFar == null) {

            return true;

        } else {

            final double tmpBestIntegerValue = this.getBestResultSoFar().getValue();

            final double tmpMipGap = Math.abs(tmpBestIntegerValue - aNonIntegerValue) / Math.min(Math.abs(tmpBestIntegerValue), Math.abs(aNonIntegerValue));

            if (myMinimisation) {
                return (aNonIntegerValue < tmpBestIntegerValue) && (tmpMipGap > options.mip_gap);
            } else {
                return (aNonIntegerValue > tmpBestIntegerValue) && (tmpMipGap > options.mip_gap);
            }
        }
    }

    boolean isIntegerSolutionFound() {
        return myBestResultSoFar != null;
    }

    boolean isIterationNecessary() {

        if (myBestResultSoFar == null) {

            return true;

        } else {

            final int tmpIterations = this.countIterations();
            final long tmpTime = this.countTime();

            return (tmpTime < options.time_suffice) && (tmpIterations < options.iterations_suffice);
        }
    }

    void markAsExplored(final BranchAndBoundNodeTask aNodeTask) {
        myExploredNodes.add(aNodeTask.getKey());
    }

    synchronized void storeResult(final Optimisation.Result aResult) {

        // BasicLogger.logDebug("Store: " + aResult.toString());

        if (myBestResultSoFar == null) {

            myBestResultSoFar = aResult;

        } else if (myMinimisation && (aResult.getValue() < myBestResultSoFar.getValue())) {

            myBestResultSoFar = aResult;

        } else if (!myMinimisation && (aResult.getValue() > myBestResultSoFar.getValue())) {

            myBestResultSoFar = aResult;
        }

        myIntegerSolutionsCount.incrementAndGet();
    }

}
