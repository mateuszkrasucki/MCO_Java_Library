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
package org.ojalgo.optimisation;

import static org.ojalgo.constant.BigMath.*;

import java.math.BigDecimal;
import java.util.*;

import org.ojalgo.access.Access1D;
import org.ojalgo.array.Array1D;
import org.ojalgo.constant.PrimitiveMath;
import org.ojalgo.function.multiary.MultiaryFunction;
import org.ojalgo.optimisation.Expression.Index;
import org.ojalgo.optimisation.integer.IntegerSolver;
import org.ojalgo.optimisation.linear.LinearSolver;
import org.ojalgo.optimisation.linear.mps.MathProgSysModel;
import org.ojalgo.optimisation.quadratic.QuadraticSolver;
import org.ojalgo.type.context.NumberContext;

/**
 * <p>
 * Lets you construct optimisation problems by combining mathematical
 * expressions (in terms of variables). Each expression or variable can be a
 * constraint and/or contribute to the objective function. An expression or
 * variable is turned into a constraint by setting a lower and/or upper limit.
 * Use {@linkplain ModelEntity#lower(BigDecimal)},
 * {@linkplain ModelEntity#upper(BigDecimal)} or
 * {@linkplain ModelEntity#level(BigDecimal)}.
 * An expression or variable is made part of (contributing to) the objective
 * function by setting a contribution weight. Use
 * {@linkplain ModelEntity#weight(BigDecimal)}.
 * </p>
 * <p>
 * You may think of variables as simple (the simplest possible) expressions,
 * and of expressions as weighted combinations of variables. They are both
 * model entities and it is as such they can be turned into constraints and set
 * to contribute to the objective function. Alternatively you may choose to
 * disregard the fact that variables are model entities and simply treat them as
 * index values. In this case everything (constraints and objective) needs to be
 * defined using expressions.
 * </p>
 * Basic instructions:
 * <ol>
 * <li>Define (create) a set of variables. Set contribution weights and lower/upper limits as needed.</li>
 * <li>Create a model using that set of variables.</li>
 * <li>Add expressions to the model. The model is the expression
 * factory. Set contribution weights and lower/upper limits as needed.</li>
 * <li>Instantiate a solver using the model. (Call {@linkplain #getDefaultSolver()})</li>
 * <li>Solve!</li>
 * </ol>
 * There are some restrictions on the models you can create:
 * <ul>
 * <li>No quadratic constraints</li>
 * </ul>
 * The plan is that future versions should not have any restrictions like these.
 * 
 * @author apete
 */
public final class ExpressionsBasedModel extends AbstractModel<GenericSolver> {

    private static final String NEW_LINE = "\n";

    private static final String START_END = "############################################\n";

    public static ExpressionsBasedModel make(final MathProgSysModel aModel) {

        final MathProgSysModel.Column[] tmpActCols = aModel.getActivatorVariableColumns();
        final MathProgSysModel.Column[] tmpNegCols = aModel.getNegativeVariableColumns();
        final MathProgSysModel.Column[] tmpPosCols = aModel.getPositiveVariableColumns();
        final MathProgSysModel.Row[] tmpAllRows = aModel.getExpressionRows();

        Arrays.sort(tmpActCols);
        Arrays.sort(tmpNegCols);
        Arrays.sort(tmpPosCols);
        Arrays.sort(tmpAllRows);

        final int tmpCountActCols = tmpActCols.length;
        final int tmpCountNegCols = tmpNegCols.length;
        final int tmpCountPosCols = tmpPosCols.length;
        final int tmpCountAllRows = tmpAllRows.length;

        // Define various local variables
        MathProgSysModel.Row tmpRow;
        MathProgSysModel.Column tmpCol;
        Variable tmpVar;
        Expression tmpExpr;
        int tmpIndex;

        // Create the LinearModel variables
        final Variable[] tmpVariables = new Variable[tmpCountActCols + tmpCountNegCols + tmpCountPosCols];
        for (int i = 0; i < tmpCountActCols; i++) {

            tmpCol = tmpActCols[i];
            tmpVar = Variable.makeBinary(tmpCol.getNameForActivator());

            tmpVariables[i] = tmpVar;
        }
        for (int i = 0; i < tmpCountNegCols; i++) {

            tmpCol = tmpNegCols[i];
            tmpVar = new Variable(tmpCol.getNameForNegativePart());

            final BigDecimal tmpLowerLimit = (tmpCol.isUpperLimitSet() && !tmpCol.needsActivator()) ? tmpCol.getUpperLimit().negate().max(ZERO) : ZERO;
            final BigDecimal tmpUpperLimit = tmpCol.isLowerLimitSet() ? tmpCol.getLowerLimit().negate() : null;

            tmpVar.lower(tmpLowerLimit).upper(tmpUpperLimit).integer(tmpCol.isInteger());

            tmpVariables[tmpCountActCols + i] = tmpVar;
        }
        for (int i = 0; i < tmpCountPosCols; i++) {

            tmpCol = tmpPosCols[i];
            tmpVar = new Variable(tmpCol.getNameForPositivePart());

            final BigDecimal tmpLowerLimit = (tmpCol.isLowerLimitSet() && !tmpCol.needsActivator()) ? tmpCol.getLowerLimit().max(ZERO) : ZERO;
            final BigDecimal tmpUpperLimit = tmpCol.getUpperLimit();

            tmpVar.lower(tmpLowerLimit).upper(tmpUpperLimit).integer(tmpCol.isInteger());

            tmpVariables[tmpCountActCols + tmpCountNegCols + i] = tmpVar;
        }

        // Instantiate the LinearModel
        final ExpressionsBasedModel retVal = new ExpressionsBasedModel(tmpVariables);

        final Expression[] tmpExpressions = new Expression[tmpCountAllRows];
        final String[] tmpExpressionNames = new String[tmpCountAllRows];

        for (int i = 0; i < tmpCountAllRows; i++) {
            tmpRow = tmpAllRows[i];
            tmpExpr = retVal.addExpression(tmpRow.getName());
            tmpExpr.lower(tmpRow.getLowerLimit());
            tmpExpr.upper(tmpRow.getUpperLimit());
            tmpExpr.weight(tmpRow.getContributionWeight());
            tmpExpressions[i] = tmpExpr;
            tmpExpressionNames[i] = tmpExpr.getName();
        }

        final Expression[] tmpActExpressions = new Expression[tmpCountActCols];
        final String[] tmpActExpressionNames = new String[tmpCountActCols];

        for (int i = 0; i < tmpCountActCols; i++) {
            tmpCol = tmpActCols[i];
            tmpExpr = retVal.addExpression(tmpCol.getName());
            tmpExpr.lower(ZERO);
            tmpActExpressions[i] = tmpExpr;
            tmpActExpressionNames[i] = tmpExpr.getName();

            tmpIndex = Arrays.binarySearch(tmpPosCols, tmpCol);
            if (tmpIndex != -1) {
                tmpExpr.setLinearFactor(i, tmpCol.getLowerLimit().negate());
                tmpExpr.setLinearFactor(tmpCountActCols + tmpCountNegCols + tmpIndex, ONE);
            }
        }

        for (int i = 0; i < tmpCountNegCols; i++) {
            tmpCol = tmpNegCols[i];
            tmpVar = tmpVariables[tmpCountActCols + i];
            for (final String tmpRowKey : tmpCol.getElementKeys()) {
                tmpIndex = Arrays.binarySearch(tmpExpressionNames, tmpRowKey);
                if (tmpIndex != -1) {
                    tmpExpressions[tmpIndex].setLinearFactor(tmpCountActCols + i, tmpCol.getRowValue(tmpRowKey).negate());
                }
            }
        }
        for (int i = 0; i < tmpCountPosCols; i++) {
            tmpCol = tmpPosCols[i];
            tmpVar = tmpVariables[tmpCountActCols + tmpCountNegCols + i];
            for (final String tmpRowKey : tmpCol.getElementKeys()) {
                tmpIndex = Arrays.binarySearch(tmpExpressionNames, tmpRowKey);
                if (tmpIndex != -1) {
                    tmpExpressions[tmpIndex].setLinearFactor(tmpCountActCols + tmpCountNegCols + i, tmpCol.getRowValue(tmpRowKey));
                }
            }
        }

        return retVal;
    }

    static final void presolve(final ExpressionsBasedModel model) {

        model.categoriseVariables();

        for (final Expression tmpExpression : model.getExpressions()) {
            if (tmpExpression.simplify()) {
                ExpressionsBasedModel.presolve(model);
                break;
            }
        }
    }

    private final boolean myWorkCopy;
    private final HashMap<String, Expression> myExpressions = new HashMap<String, Expression>();
    private final HashSet<Index> myFixedVariables = new HashSet<Index>();
    private transient int[] myFreeIndeces = null;
    private transient List<Variable> myFreeVariables = null;
    private transient int[] myIntegerIndeces = null;
    private transient List<Variable> myIntegerVariables = null;
    private transient int[] myNegativeIndeces = null;
    private transient List<Variable> myNegativeVariables = null;
    private transient Expression myObjectiveExpression = null;
    private transient MultiaryFunction<Double> myObjectiveFunction = null;
    private transient int[] myPositiveIndeces = null;
    private transient List<Variable> myPositiveVariables = null;
    private final ArrayList<Variable> myVariables = new ArrayList<Variable>();

    public ExpressionsBasedModel() {

        super();

        myWorkCopy = false;
    }

    public ExpressionsBasedModel(final Collection<? extends Variable> someVariables) {

        super();

        for (final Variable tmpVariable : someVariables) {
            this.addVariable(tmpVariable);
        }

        myWorkCopy = false;
    }

    public ExpressionsBasedModel(final Variable[] someVariables) {

        super();

        for (final Variable tmpVariable : someVariables) {
            this.addVariable(tmpVariable);
        }

        myWorkCopy = false;
    }

    @SuppressWarnings("unused")
    private ExpressionsBasedModel(final Options someOptions) {
        this();
    }

    ExpressionsBasedModel(final ExpressionsBasedModel modelToCopy, final boolean workCopy) {

        super(modelToCopy.options);

        this.setMinimisation(modelToCopy.isMinimisation());

        for (final Variable tmpVariable : modelToCopy.getVariables()) {
            this.addVariable(tmpVariable.copy());
        }

        for (final Expression tmpExpression : modelToCopy.getExpressions()) {
            if (workCopy && tmpExpression.isRedundant() && !tmpExpression.isObjective()) {
                // In this case don't copy
            } else {
                myExpressions.put(tmpExpression.getName(), tmpExpression.copy(this, !workCopy));
            }
        }

        if (myWorkCopy = workCopy) {

            myObjectiveExpression = modelToCopy.getObjectiveExpression();
            myObjectiveFunction = modelToCopy.getObjectiveFunction();

            myFixedVariables.addAll(modelToCopy.getFixedVariables());
        }
    }

    public Expression addExpression(final String aName) {

        final Expression retVal = new Expression(aName, this);

        myExpressions.put(aName, retVal);

        return retVal;
    }

    public void addVariable(final Variable aVariable) {
        if (myWorkCopy) {
            throw new IllegalStateException("This model is a copy - its set of variables cannot be modified!");
        } else {
            myVariables.add(aVariable);
            aVariable.setIndex(new Expression.Index(myVariables.size() - 1));
        }
    }

    public void addVariables(final Collection<? extends Variable> someVariables) {
        for (final Variable tmpVariable : someVariables) {
            this.addVariable(tmpVariable);
        }
    }

    public ExpressionsBasedModel copy() {
        return new ExpressionsBasedModel(this, false);
    }

    public int countExpressions() {
        return myExpressions.size();
    }

    public int countVariables() {
        return myVariables.size();
    }

    public GenericSolver getDefaultSolver() {

        if (!myWorkCopy) {
            this.flushCaches();
        }

        ExpressionsBasedModel.presolve(this);

        if (this.isAnyVariableInteger()) {

            return IntegerSolver.make(this);

        } else if (this.isAnyExpressionQuadratic()) {

            return QuadraticSolver.make(this);

        } else {

            return LinearSolver.make(this);
        }
    }

    public Expression getExpression(final String aName) {
        return myExpressions.get(aName);
    }

    public Collection<Expression> getExpressions() {
        return Collections.unmodifiableCollection(myExpressions.values());
    }

    public Set<Index> getFixedVariables() {
        return Collections.unmodifiableSet(myFixedVariables);
    }

    /**
     * @return A list of the variables that are not fixed at a specific value
     */
    public List<Variable> getFreeVariables() {

        if (myFreeVariables == null) {
            this.categoriseVariables();
        }

        return myFreeVariables;
    }

    /**
     * @return A list of the variables that are not fixed at a specific value
     * and are marked as integer variables
     */
    public List<Variable> getIntegerVariables() {

        if (myIntegerVariables == null) {
            this.categoriseVariables();
        }

        return myIntegerVariables;
    }

    /**
     * @return A list of the variables that are not fixed at a specific value
     * and whos range include negative values
     */
    public List<Variable> getNegativeVariables() {

        if (myNegativeVariables == null) {
            this.categoriseVariables();
        }

        return myNegativeVariables;
    }

    public Expression getObjectiveExpression() {

        if (myObjectiveExpression == null) {

            myObjectiveExpression = new Expression("Obj Expr", this);

            Variable tmpVariable;
            for (int i = 0; i < myVariables.size(); i++) {
                tmpVariable = myVariables.get(i);

                if (tmpVariable.isObjective()) {
                    myObjectiveExpression.setLinearFactor(i, tmpVariable.getContributionWeight());
                }
            }

            BigDecimal tmpOldVal = null;
            BigDecimal tmpDiff = null;
            BigDecimal tmpNewVal = null;

            for (final Expression tmpExpression : myExpressions.values()) {

                if (tmpExpression.isObjective()) {

                    final BigDecimal tmpContributionWeight = tmpExpression.getContributionWeight();
                    final boolean tmpNotOne = tmpContributionWeight.compareTo(ONE) != 0; // To avoid multiplication by 1.0

                    if (tmpExpression.isAnyLinearFactorNonZero()) {
                        for (final Expression.Index tmpKey : tmpExpression.getLinearFactorKeys()) {
                            tmpOldVal = myObjectiveExpression.getLinearFactor(tmpKey);
                            tmpDiff = tmpExpression.getLinearFactor(tmpKey);
                            tmpNewVal = tmpOldVal.add(tmpNotOne ? tmpContributionWeight.multiply(tmpDiff) : tmpDiff);
                            myObjectiveExpression.setLinearFactor(tmpKey, tmpNewVal);
                        }
                    }

                    if (tmpExpression.isAnyQuadraticFactorNonZero()) {
                        for (final Expression.RowColumn tmpKey : tmpExpression.getQuadraticFactorKeys()) {
                            tmpOldVal = myObjectiveExpression.getQuadraticFactor(tmpKey);
                            tmpDiff = tmpExpression.getQuadraticFactor(tmpKey);
                            tmpNewVal = tmpOldVal.add(tmpNotOne ? tmpContributionWeight.multiply(tmpDiff) : tmpDiff);
                            myObjectiveExpression.setQuadraticFactor(tmpKey, tmpNewVal);
                        }
                    }
                }
            }
        }

        return myObjectiveExpression;
    }

    public MultiaryFunction<Double> getObjectiveFunction() {

        if (myObjectiveFunction == null) {
            myObjectiveFunction = this.getObjectiveExpression().toFunction();
        }

        return myObjectiveFunction;
    }

    /**
     * @return A list of the variables that are not fixed at a specific value
     * and whos range include positive values and/or zero
     */
    public List<Variable> getPositiveVariables() {

        if (myPositiveVariables == null) {
            this.categoriseVariables();
        }

        return myPositiveVariables;
    }

    public Variable getVariable(final int index) {
        return myVariables.get(index);
    }

    public List<Variable> getVariables() {
        return Collections.unmodifiableList(myVariables);
    }

    public Access1D<BigDecimal> getVariableValues() {

        final int tmpSize = myVariables.size();

        final Array1D<BigDecimal> retVal = Array1D.BIG.makeZero(tmpSize);

        BigDecimal tmpVal;
        for (int i = 0; i < tmpSize; i++) {

            tmpVal = myVariables.get(i).getValue();
            if (tmpVal != null) {
                retVal.set(i, tmpVal);
            } else {
                retVal.set(i, ZERO);
            }
        }

        return retVal;
    }

    public int indexOf(final Variable aVariable) {
        return aVariable.getIndex().index;
    }

    /**
     * @param index General, global, variable index
     * @return Local index among the positive variables. -1 indicates the variable is not a positive variable.
     */
    public int indexOfFreeVariable(final int index) {
        return myFreeIndeces[index];
    }

    public int indexOfFreeVariable(final Variable aVariable) {
        return this.indexOfFreeVariable(this.indexOf(aVariable));
    }

    /**
     * @param index General, global, variable index
     * @return Local index among the integer variables. -1 indicates the variable is not an integer variable.
     */
    public int indexOfIntegerVariable(final int index) {
        return myIntegerIndeces[index];
    }

    public int indexOfIntegerVariable(final Variable aVariable) {
        return this.indexOfIntegerVariable(aVariable.getIndex().index);
    }

    /**
     * @param index General, global, variable index
     * @return Local index among the negative variables. -1 indicates the variable is not a negative variable.
     */
    public int indexOfNegativeVariable(final int index) {
        return myNegativeIndeces[index];
    }

    public int indexOfNegativeVariable(final Variable aVariable) {
        return this.indexOfNegativeVariable(this.indexOf(aVariable));
    }

    /**
     * @param index General, global, variable index
     * @return Local index among the positive variables. -1 indicates the variable is not a positive variable.
     */
    public int indexOfPositiveVariable(final int index) {
        return myPositiveIndeces[index];
    }

    public int indexOfPositiveVariable(final Variable aVariable) {
        return this.indexOfPositiveVariable(this.indexOf(aVariable));
    }

    public boolean isAnyExpressionQuadratic() {

        boolean retVal = false;

        // final int tmpLength = myExpressions.size();

        //        for (int i = 0; !retVal && (i < tmpLength); i++) {
        //            retVal |= myExpressions.get(i).hasQuadratic();
        //        }

        String tmpType;
        for (final Iterator<String> tmpIterator = myExpressions.keySet().iterator(); !retVal && tmpIterator.hasNext();) {
            tmpType = tmpIterator.next();
            retVal |= myExpressions.get(tmpType).isAnyQuadraticFactorNonZero();
        }

        return retVal;
    }

    public boolean isAnyVariableFixed() {
        return myFixedVariables.size() >= 1;
    }

    public boolean isAnyVariableInteger() {

        boolean retVal = false;

        final int tmpLength = myVariables.size();

        for (int i = 0; !retVal && (i < tmpLength); i++) {
            retVal |= myVariables.get(i).isInteger();
        }

        return retVal;
    }

    public boolean isWorkCopy() {
        return myWorkCopy;
    }

    public void markActiveInequalityConstraints(final Collection<ModelEntity<?>> activeInequalityEntities) {

        for (final Variable tmpVariable : myVariables) {
            tmpVariable.setActiveInequalityConstraint(false);
        }

        for (final Expression tmpExpression : myExpressions.values()) {
            tmpExpression.setActiveInequalityConstraint(false);
        }

        for (final ModelEntity<?> tmpModelEntity : activeInequalityEntities) {
            tmpModelEntity.setActiveInequalityConstraint(true);
        }
    }

    public Optimisation.Result maximise() {

        this.setMaximisation(true);

        return this.solve();
    }

    public Optimisation.Result minimise() {

        this.setMinimisation(true);

        return this.solve();
    }

    public ExpressionsBasedModel relax(final boolean inPlace) {

        final ExpressionsBasedModel retVal = inPlace ? this : new ExpressionsBasedModel(this, true);

        for (final Variable tmpVariable : retVal.getVariables()) {
            tmpVariable.relax();
        }

        return retVal;
    }

    /**
     * Linear equality constrained expressions.
     */
    public List<Expression> selectExpressionsLinearEquality() {

        final List<Expression> retVal = new ArrayList<Expression>();

        for (final Expression tmpExpression : myExpressions.values()) {
            if (tmpExpression.isEqualityConstraint() && !tmpExpression.isAnyQuadraticFactorNonZero() && !tmpExpression.isRedundant()) {
                retVal.add(tmpExpression);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    /**
     * Linear lower constrained expressions.
     */
    public List<Expression> selectExpressionsLinearLower() {

        final List<Expression> retVal = new ArrayList<Expression>();

        for (final Expression tmpExpression : myExpressions.values()) {
            if (tmpExpression.isLowerConstraint() && !tmpExpression.isAnyQuadraticFactorNonZero() && !tmpExpression.isRedundant()) {
                retVal.add(tmpExpression);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    /**
     * Linear upper constrained expressions.
     */
    public List<Expression> selectExpressionsLinearUpper() {

        final List<Expression> retVal = new ArrayList<Expression>();

        for (final Expression tmpExpression : myExpressions.values()) {
            if (tmpExpression.isUpperConstraint() && !tmpExpression.isAnyQuadraticFactorNonZero() && !tmpExpression.isRedundant()) {
                retVal.add(tmpExpression);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    /**
     * Quadratic (and/or compound) equality constrained expressions.
     */
    public List<Expression> selectExpressionsQuadraticEquality() {

        final List<Expression> retVal = new ArrayList<Expression>();

        for (final Expression tmpExpression : myExpressions.values()) {
            if (tmpExpression.isEqualityConstraint() && tmpExpression.isAnyQuadraticFactorNonZero() && !tmpExpression.isRedundant()) {
                retVal.add(tmpExpression);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    /**
     * Quadratic (and/or compound) lower constrained expressions.
     */
    public List<Expression> selectExpressionsQuadraticLower() {

        final List<Expression> retVal = new ArrayList<Expression>();

        for (final Expression tmpExpression : myExpressions.values()) {
            if (tmpExpression.isLowerConstraint() && tmpExpression.isAnyQuadraticFactorNonZero() && !tmpExpression.isRedundant()) {
                retVal.add(tmpExpression);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    /**
     * Quadratic (and/or compound) upper constrained expressions.
     */
    public List<Expression> selectExpressionsQuadraticUpper() {

        final List<Expression> retVal = new ArrayList<Expression>();

        for (final Expression tmpExpression : myExpressions.values()) {
            if (tmpExpression.isUpperConstraint() && tmpExpression.isAnyQuadraticFactorNonZero() && !tmpExpression.isRedundant()) {
                retVal.add(tmpExpression);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public List<Variable> selectVariablesFreeLower() {

        final List<Variable> retVal = new ArrayList<Variable>();

        for (final Variable tmpVariable : this.getFreeVariables()) {
            if (tmpVariable.isLowerConstraint()) {
                retVal.add(tmpVariable);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public List<Variable> selectVariablesFreeUpper() {

        final List<Variable> retVal = new ArrayList<Variable>();

        for (final Variable tmpVariable : this.getFreeVariables()) {
            if (tmpVariable.isUpperConstraint()) {
                retVal.add(tmpVariable);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public List<Variable> selectVariablesNegativeLower() {

        final List<Variable> retVal = new ArrayList<Variable>();

        for (final Variable tmpVariable : this.getNegativeVariables()) {
            if (tmpVariable.isLowerConstraint() && (tmpVariable.getLowerLimit().signum() == -1)) {
                retVal.add(tmpVariable);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public List<Variable> selectVariablesNegativeUpper() {

        final List<Variable> retVal = new ArrayList<Variable>();

        for (final Variable tmpVariable : this.getNegativeVariables()) {
            if (tmpVariable.isUpperConstraint() && (tmpVariable.getUpperLimit().signum() == -1)) {
                retVal.add(tmpVariable);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public List<Variable> selectVariablesPositiveLower() {

        final List<Variable> retVal = new ArrayList<Variable>();

        for (final Variable tmpVariable : this.getPositiveVariables()) {
            if (tmpVariable.isLowerConstraint() && (tmpVariable.getLowerLimit().signum() == 1)) {
                retVal.add(tmpVariable);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public List<Variable> selectVariablesPositiveUpper() {

        final List<Variable> retVal = new ArrayList<Variable>();

        for (final Variable tmpVariable : this.getPositiveVariables()) {
            if (tmpVariable.isUpperConstraint() && (tmpVariable.getUpperLimit().signum() == 1)) {
                retVal.add(tmpVariable);
            }
        }

        return Collections.unmodifiableList(retVal);
    }

    public Optimisation.Result solve() {

        Optimisation.Result retVal = null;

        if (this.getFreeVariables().size() > 0) {

            final GenericSolver tmpSolver = this.getDefaultSolver();

            retVal = tmpSolver.solve();

            //            if (options.validate && retVal.getState().isFeasible() && !this.validate(retVal)) {
            //                retVal = new Optimisation.Result(Optimisation.State.FAILED, retVal);
            //            }

        } else {

            final Access1D<BigDecimal> tmpSolution = this.getVariableValues();

            if (this.validate(tmpSolution)) {

                final double tmpValue = this.getObjectiveFunction().invoke(tmpSolution);
                final State tmpState = State.DISTINCT;

                retVal = new Result(tmpState, tmpValue, tmpSolution);

            } else {

                final double tmpValue = PrimitiveMath.NaN;
                final State tmpState = State.INVALID;

                retVal = new Result(tmpState, tmpValue, tmpSolution);
            }
        }

        return retVal;
    }

    @Override
    public String toString() {

        final StringBuilder retVal = new StringBuilder(START_END);

        for (final Variable tmpVariable : myVariables) {
            tmpVariable.appendToString(retVal);
            retVal.append(NEW_LINE);
        }

        for (final Expression tmpExpression : myExpressions.values()) {
            tmpExpression.appendToString(retVal, this.getVariableValues());
            retVal.append(NEW_LINE);
        }

        return retVal.append(START_END).toString();
    }

    public boolean validate() {

        boolean retVal = true;

        for (final Variable tmpVariable : myVariables) {
            retVal &= tmpVariable.validate();
        }

        for (final Expression tmpExpression : myExpressions.values()) {
            retVal &= tmpExpression.validate();
        }

        return retVal;
    }

    public boolean validate(final Access1D<BigDecimal> solution) {
        return this.validate(solution, options.slack);
    }

    public boolean validate(final Access1D<BigDecimal> solution, final NumberContext context) {

        final int tmpSize = myVariables.size();

        boolean retVal = tmpSize == solution.size();

        for (int i = 0; retVal && (i < tmpSize); i++) {
            retVal &= myVariables.get(i).validate(solution.get(i), context);
        }

        for (final Expression tmpExpression : myExpressions.values()) {
            retVal &= retVal && tmpExpression.validate(solution, context);
        }

        return retVal;
    }

    public boolean validate(final NumberContext context) {
        return this.validate(this.getVariableValues(), context);
    }

    private void categoriseVariables() {

        final int tmpLength = myVariables.size();

        myFreeVariables = new ArrayList<Variable>();
        myFreeIndeces = new int[tmpLength];
        Arrays.fill(myFreeIndeces, -1);

        myPositiveVariables = new ArrayList<Variable>();
        myPositiveIndeces = new int[tmpLength];
        Arrays.fill(myPositiveIndeces, -1);

        myNegativeVariables = new ArrayList<Variable>();
        myNegativeIndeces = new int[tmpLength];
        Arrays.fill(myNegativeIndeces, -1);

        myIntegerVariables = new ArrayList<Variable>();
        myIntegerIndeces = new int[tmpLength];
        Arrays.fill(myIntegerIndeces, -1);

        for (int i = 0; i < tmpLength; i++) {

            final Variable tmpVariable = myVariables.get(i);

            if (tmpVariable.isEqualityConstraint()) {

                tmpVariable.setValue(tmpVariable.getLowerLimit());
                myFixedVariables.add(tmpVariable.getIndex());

            } else {

                myFreeVariables.add(tmpVariable);
                myFreeIndeces[i] = myFreeVariables.size() - 1;

                if (!tmpVariable.isUpperLimitSet() || (tmpVariable.getUpperLimit().signum() == 1)) {
                    myPositiveVariables.add(tmpVariable);
                    myPositiveIndeces[i] = myPositiveVariables.size() - 1;
                }

                if (!tmpVariable.isLowerLimitSet() || (tmpVariable.getLowerLimit().signum() == -1)) {
                    myNegativeVariables.add(tmpVariable);
                    myNegativeIndeces[i] = myNegativeVariables.size() - 1;
                }

                if (tmpVariable.isInteger()) {
                    myIntegerVariables.add(tmpVariable);
                    myIntegerIndeces[i] = myIntegerVariables.size() - 1;
                }
            }
        }

        myFreeVariables = Collections.unmodifiableList(myFreeVariables);
        myPositiveVariables = Collections.unmodifiableList(myPositiveVariables);
        myNegativeVariables = Collections.unmodifiableList(myNegativeVariables);
        myIntegerVariables = Collections.unmodifiableList(myIntegerVariables);
    }

    protected void flushCaches() {

        myObjectiveExpression = null;
        myObjectiveFunction = null;

        myFreeVariables = null;
        myFreeIndeces = null;

        myIntegerVariables = null;
        myIntegerIndeces = null;

        myNegativeVariables = null;
        myNegativeIndeces = null;

        myPositiveVariables = null;
        myPositiveIndeces = null;
    }
}
