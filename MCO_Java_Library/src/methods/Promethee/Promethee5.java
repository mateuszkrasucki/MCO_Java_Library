package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import org.ojalgo.optimisation.integer.IntegerSolver;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.Optimisation;
import java.math.BigDecimal;

/**
 * Promethee V (Promethee5) method class. 
 * Results are based on MPF (net multicriteria preference flow) so basic Promethee method results (e.g. ranking) are the same as for Promethee2 method class. 
 * Promethee5 method allows to set constraints and find best set of alternatives matching those constraints.
 * extends methods.Promethee.Promethee abstract class.
 * @author Mateusz Krasucki
 * @see methods.Promethee.Promethee
 */
public class Promethee5 extends Promethee{
        /**
         * LinkedList containing the best set of alternatives matching all the constraints as calculated by Promethee5 method.
         */
        private LinkedList<Alternative> alternativesBestSet;

        /**
         * Aggregated multicriteria preference flow value of the best set of alternatives matching all the constraints as calculated by Promethee5 method.
         */
        private double bestSetMPF;
    
        /**
	* Promethee5 class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
        * It should be structured as shown in example csv file in dataFileExamples/promethee.csv.
	*/
	public Promethee5(String filename) {		
             super(filename);   
             alternativesBestSet = new LinkedList<Alternative>();
             bestSetMPF = 0;
        }
        
    /**
     * Basic Promethee5 class constructor.
     * The Promethee object created by this constructor is empty (no alternatives and criteria set).
     */
        public Promethee5() {
            super();
            alternativesBestSet = new LinkedList<Alternative>();
             bestSetMPF = 0;
        }
    
        
      /**
     * Performs Promethee5 method calculations on data added to Promethee5 object.
     */
        public void calculate()  { 
            normalizeWeights();

            calculateMPD();
            calculateMPF();
            ranking = new LinkedList<Alternative>(alternatives);
            Collections.sort(ranking, new Comparator<Alternative>() {
             @Override
             public int compare(Alternative o1, Alternative o2) {
                 if(o1.getMpf()<o2.getMpf())    {
                     return 1;
                 }
                 else if(o1.getMpf()>o2.getMpf())   {
                     return -1;
                 }
                 return 0;
             }
             });

            LinkedList<Variable> listOfVariables = new LinkedList<Variable>();
            for(int i=0; i<this.getAlternativesNum();i++)    {
                listOfVariables.add(Variable.makeBinary("x" + i));
            }

            ExpressionsBasedModel model = new ExpressionsBasedModel();
            model.addVariables(listOfVariables);

            for(int i=0; i<constraints.size(); i++) {
                Expression ex = model.addExpression("exp" + i);
                Constraint constraint = constraints.get(i);
                int criteriumIndex = criteria.indexOf(constraint.getCriterium());

                for(int j=0; j<this.getAlternativesNum(); j++)    {
                    ex.setLinearFactor(listOfVariables.get(j), alternatives.get(j).getCriteriumValue(criteriumIndex));
                }

                if(constraint.getConstraintType() == Constraint.ConstrainType.UPPER)  {
                    ex.upper(BigDecimal.valueOf(constraint.getValue()));
                }
                else if(constraint.getConstraintType() == Constraint.ConstrainType.BOTTOM)  {
                    ex.lower(BigDecimal.valueOf(constraint.getValue()));
                }
            }

            Expression ex = model.addExpression("obj");
            for(int i=0; i<this.getAlternativesNum(); i++)    {
                    ex.setLinearFactor(listOfVariables.get(i), alternatives.get(i).getMpf());
            }
            ex.weight(BigDecimal.ONE);
            model.setMaximisation(true);
            IntegerSolver solver = IntegerSolver.make(model);
            Optimisation.Result res = solver.solve();

            bestSetMPF = res.getValue();

            for(int i=0; i<this.getAlternativesNum(); i++)   {
                if(res.get(i).compareTo(BigDecimal.ONE) == 0)   {
                    alternativesBestSet.add(alternatives.get(i));
                }
            }
        }

   
     /**
     * Adds constraint to Promethee method object.
     * @param constraint Constraint object.
     */
    public void addConstraint(Constraint constraint)  {
        if(criteria.indexOf(constraint.getCriterium())!=-1)  {
            constraints.add(constraint);
        }
    }
        
      /**
     * Returns list of all the constraints in Promethee5 object.
     * @return LinkedList containing Constraint objects from Promethee object.
     */
    public LinkedList<Constraint> getConstraints() {
        return constraints;
    }
    
      /**
     * Returns Constraint with the i-th order number. 
     * @param i Constraint order number.
     * @return I-th constraint object.
     */
    public Constraint getConstraint(int i)  {
        return constraints.get(i);
    }

        /**
     * Sets constraints in Promethee5 object to LinkedList provided as a parameter.
     * @param constraints LinkedList object containing Constraint objects.
     */
    public void setConstraints(LinkedList<Constraint> constraints) {
        this.constraints = constraints;
    }

    /**
     * Returns the best set of alternatives matching all the constraints as calculated by Promethee5 method.
     * @return LinkedList containing the best set of alternatives matching all the constraints as calculated by Promethee5 method.
     */ 
    public LinkedList<Alternative> getAlternativesBestSet() {
        return alternativesBestSet;
    }

    /**
     * Returns aggregated multicriteria preference flow value of the best set of alternatives matching all the constraints as calculated by Promethee5 method.
     * @return Aggregated multicriteria preference flow value of the best set of alternatives matching all the constraints as calculated by Promethee5 method.
     */
    public double getBestSetMPF() {
        return bestSetMPF;
    }     
    
        /**
     * Returns Promethee2 score (MPF) of i-th alternative.
     * @param i Alternative order number.
     * @return I-th alternative Promethee2 score (MPF).
     */
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getMpf();
        }
        return 0;
    }

        /**
     * Returns Promethee2 score (MPF) of alternative alt. It has to be one of the alternatives added to the object before running calculate() method.
     * @param alt Alternative object.
     * @return Promethee2 score (MPF) of the alternative object.
     */
    public double getAlternativeValue(Alternative alt)    {
        return alt.getMpf();
    }
    
}
