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
 *
 * @author Mateusz Krasucki
 */
public class Promethee5 extends Promethee{
        /**
	* 
	* Cosntructor with data read from file
	* @param filename Filename where data can be read from. It should be structured as shown in example csv file in dataFileExamples/promethee.csv
	*/
	public Promethee5(String filename) {		
             super(filename);   	
        }
    
        public Promethee5() {
            super();
        }
    

        public void addConstraint(Constraint constraint)  {
            super.addConstraint(constraint);
        }
    
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

   
    public LinkedList<Constraint> getConstraints() {
        return constraints;
    }
    
    public Constraint getConstraint(int i)  {
        return constraints.get(i);
    }

    public void setConstraints(LinkedList<Constraint> constraints) {
        this.constraints = constraints;
    }

    public LinkedList<Alternative> getAlternativesBestSet() {
        return alternativesBestSet;
    }

    public void setAlternativesBestSet(LinkedList<Alternative> alternativesBestSet) {
        this.alternativesBestSet = alternativesBestSet;
    }

    public double getBestSetMPF() {
        return bestSetMPF;
    }     
    
}
