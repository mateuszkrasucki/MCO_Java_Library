package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import org.ejml.simple.SimpleMatrix;
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
public class Promethee5 {
    
    private LinkedList<Criterium> criteria;
    private LinkedList<Alternative> alternatives;
    private LinkedList<Constraint> constraints;
    
    private LinkedList<Alternative> ranking;
    private LinkedList<Alternative> alternativesBestSet;
    private double bestSetMPF;
    
    private int criteriaNum_; 
    private int alternativesNum_;
    
    private SimpleMatrix mpd;
    
    public Promethee5(int criteriaNum) {
                alternativesNum_ = 0;
		criteriaNum_ = criteriaNum;
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                constraints = new LinkedList<Constraint>();
                ranking = new LinkedList<Alternative>();
                
                alternativesBestSet = new LinkedList<Alternative>();
                bestSetMPF = 0;
    }
    
    public void addCriterium(Criterium criterium)   {
        if(criteria.size() < criteriaNum_) {
            criteria.add(criterium);
        }
        else    {
            throw new IndexOutOfBoundsException("All the criteria has been already added.");
        }
    }
    
    
    public void addAlternative(Alternative alternative)   {
        alternativesNum_++;
        alternative.setId(alternativesNum_);
        alternatives.add(alternative);
    }

    public void addConstraint(Constraint constraint)  {
        if(criteria.indexOf(constraint.getCriterium())!=-1)  {
            constraints.add(constraint);
        }
    }
    
    public void normalizeWeights()  {
        double sum = 0;
        for(int i=0; i<criteria.size();i++) {
            sum = sum + criteria.get(i).getWeight();
        }
        for(int i=0; i<criteria.size();i++) {
            criteria.get(i).setWeight(criteria.get(i).getWeight()/sum);
        }
    }   
    
    public void calculate()  { 
        normalizeWeights();
        ranking = new LinkedList<Alternative>(alternatives);

        calculateMPD();
        calculateMPF();
        
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
        for(int i=0; i<alternativesNum_;i++)    {
            listOfVariables.add(Variable.makeBinary("x" + i));
        }
        
        ExpressionsBasedModel model = new ExpressionsBasedModel();
        model.addVariables(listOfVariables);
        
        for(int i=0; i<constraints.size(); i++) {
            Expression ex = model.addExpression("exp" + i);
            Constraint constraint = constraints.get(i);
            int criteriumIndex = criteria.indexOf(constraint.getCriterium());
            
            for(int j=0; j<alternativesNum_; j++)    {
                ex.setLinearFactor(listOfVariables.get(j), alternatives.get(j).getCriteriumValue(criteriumIndex));
            }
            
            if(constraint.getConstraintType() == Constraint.ConstrainType.UPPER)  {
                ex.upper(BigDecimal.valueOf(constraint.getValue()));
            }
            else if(constraint.getConstraintType() == Constraint.ConstrainType.LOWER)  {
                ex.lower(BigDecimal.valueOf(constraint.getValue()));
            }
        }
        
        Expression ex = model.addExpression("obj");
        for(int i=0; i<alternativesNum_; i++)    {
                ex.setLinearFactor(listOfVariables.get(i), alternatives.get(i).getMpf());
        }
        ex.weight(BigDecimal.ONE);
        model.setMaximisation(true);
        IntegerSolver solver = IntegerSolver.make(model);
        Optimisation.Result res = solver.solve();
        
        bestSetMPF = res.getValue();
        
        for(int i=0; i<alternativesNum_; i++)   {
            if(res.get(i).compareTo(BigDecimal.ONE) == 0)   {
                alternativesBestSet.add(alternatives.get(i));
            }
        }
    }
    
   
    private void calculateMPD()  {
        mpd = new SimpleMatrix(alternativesNum_,alternativesNum_);
        mpd.zero();
        System.out.println(mpd);

        for(int i = 0; i<alternativesNum_; i++)  {
            for(int j=0; j<alternativesNum_; j++)    {
                if(i!=j)    {
                    for(int r=0; r<criteriaNum_; r++)    {
                        double pd = criteria.get(r).calculatePreference(alternatives.get(i).getCriteriumValue(r), alternatives.get(j).getCriteriumValue(r));
                        mpd.set(i, j, mpd.get(i, j) + criteria.get(r).getWeight() * pd);
                    }
                }
            }
        }
    }
    
    private void calculateMPF()  {
        SimpleMatrix mpf_plus = new SimpleMatrix(alternativesNum_,1);
        mpf_plus.zero();
        SimpleMatrix mpf_minus = new SimpleMatrix(alternativesNum_,1);
        mpf_minus.zero();

        for(int i=0; i<alternativesNum_;i++)    {
            for(int j=0; j<alternativesNum_; j++)   {
                if(i!=j)    {
                    mpf_plus.set(i, 0, mpf_plus.get(i, 0) + mpd.get(i, j));
                    mpf_minus.set(i, 0, mpf_minus.get(i, 0) + mpd.get(j, i));
                }
            }
            mpf_plus.set(i,0,mpf_plus.get(i,0)/(alternativesNum_-1));
            mpf_minus.set(i,0,mpf_minus.get(i,0)/(alternativesNum_-1));
            
            alternatives.get(i).setMpfPlus(mpf_plus.get(i, 0));
            alternatives.get(i).setMpfMinus(mpf_minus.get(i, 0));
            alternatives.get(i).setMpf(alternatives.get(i).getMpfPlus() -  alternatives.get(i).getMpfMinus());
        }
        
    }

    public LinkedList<Criterium> getCriteria() {
        return criteria;
    }
    
    public Criterium getCriterium(int i)    {
        return criteria.get(i);
    }

    public void setCriteria(LinkedList<Criterium> criteria) {
        this.criteria = criteria;
    }

    public LinkedList<Alternative> getAlternatives() {
        return alternatives;
    }
    
    public Alternative getAlternative(int i)    {
        return alternatives.get(i);
    }

    public void setAlternatives(LinkedList<Alternative> alternatives) {
        this.alternatives = alternatives;
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
    
    public LinkedList<Alternative> getRanking() {
        return ranking;
    }
    
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getMpf();
        }
        return 0;
    }
    
    public double getAlternativeValue(Alternative alt)    {
        return alt.getMpf();
    }
    
    public Alternative getAlternativeByRank(int rank)    {
        return ranking.get(rank);
    }    
    
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}
