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
public class Promethee {
    
    public LinkedList<Criterium> criteria;
    public LinkedList<Alternative> alternatives;
    public LinkedList<Constraint> constraints;
    public LinkedList<Alternative> alternativesBestSet;
    public double bestSetMPF;
    
    private int criteriaNum_; 
    private int alternativesNum_;
    
    private SimpleMatrix mpd;
    
    public Promethee(int criteriaNum) {
                alternativesNum_ = 0;
		criteriaNum_ = criteriaNum;
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                constraints = new LinkedList<Constraint>();
                alternativesBestSet = new LinkedList<Alternative>();
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
        alternative.id = alternativesNum_;
        alternatives.add(alternative);
    }
    
    
    public void calculatePromethee1 ()  {
        double sumWeight = 0;
                for(int i=0; i<criteriaNum_; i++)   {
            sumWeight = sumWeight + criteria.get(i).weight;
        }
        for(int i=0; i<criteriaNum_; i++)   {
            criteria.get(i).weight = criteria.get(i).weight/sumWeight;
        }
        
        calculateMPD();     
        calculateMPF();
        Collections.sort(alternatives, new Comparator<Alternative>() {
         @Override
         public int compare(Alternative o1, Alternative o2) {
             if(o1.mpf_plus<o2.mpf_plus)    {
                 return 1;
             }
             else if(o1.mpf_plus>o2.mpf_plus)   {
                 return -1;
             }
             return 0;
         }
     });
    }
    
    public void calculatePromethee2()  {
        calculateMPD();
        calculateMPF();
        Collections.sort(alternatives, new Comparator<Alternative>() {
         @Override
         public int compare(Alternative o1, Alternative o2) {
             if(o1.mpf<o2.mpf)    {
                 return 1;
             }
             else if(o1.mpf>o2.mpf)   {
                 return -1;
             }
             return 0;
         }
     });
    }
    
    public void addConstraint(Constraint constraint)  {
        if(criteria.indexOf(constraint.criterium)!=-1)  {
            constraints.add(constraint);
        }
    }
    
    public void calculatePromethee5()   {
        LinkedList<Variable> listOfVariables = new LinkedList<Variable>();
        for(int i=0; i<alternativesNum_;i++)    {
            listOfVariables.add(Variable.makeBinary("x" + i));
        }
        
        ExpressionsBasedModel model = new ExpressionsBasedModel();
        model.addVariables(listOfVariables);
        
        for(int i=0; i<constraints.size(); i++) {
            Expression ex = model.addExpression("exp" + i);
            Constraint constraint = constraints.get(i);
            int criteriumIndex = criteria.indexOf(constraint.criterium);
            
            for(int j=0; j<alternativesNum_; j++)    {
                ex.setLinearFactor(listOfVariables.get(j), alternatives.get(j).criteriaValues.get(criteriumIndex));
            }
            
            if(constraint.constraintType == Constraint.ConstrainType.UPPER)  {
                ex.upper(BigDecimal.valueOf(constraint.value));
            }
            else if(constraint.constraintType == Constraint.ConstrainType.LOWER)  {
                ex.lower(BigDecimal.valueOf(constraint.value));
            }
        }
        
        Expression ex = model.addExpression("obj");
        for(int i=0; i<alternativesNum_; i++)    {
                ex.setLinearFactor(listOfVariables.get(i), alternatives.get(i).mpf);
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
    
    public int getP1Preference(Alternative a, Alternative b)   {
        if((a.mpf_plus>b.mpf_plus) && (a.mpf_minus<=b.mpf_minus)) {
            return 1;         
        }
        else if((a.mpf_plus==b.mpf_minus) && (a.mpf_minus<b.mpf_minus)) {
            return 1;
        }
        else if((a.mpf_plus==b.mpf_plus) && (a.mpf_minus == b.mpf_minus))   {
            return 0;
        }
        else if((a.mpf_plus>b.mpf_plus) && (a.mpf_minus>b.mpf_minus))   {
            return -1;
        }
        else if ((a.mpf_plus<b.mpf_plus) && (a.mpf_minus<b.mpf_minus)) {
            return -1;           
        }
        return 0;
    }
    
    private void calculateMPD()  {
        mpd = new SimpleMatrix(alternativesNum_,alternativesNum_);
        mpd.zero();
        System.out.println(mpd);

        for(int i = 0; i<alternativesNum_; i++)  {
            for(int j=0; j<alternativesNum_; j++)    {
                if(i!=j)    {
                    for(int r=0; r<criteriaNum_; r++)    {
                        double d = 0;
                        double pd = 0;
                        // wyznaczenie roznicy miedzy wartosciami kryterium r w alternatywie i i alternatywie j (z uwzglednieniem kierunku preferencji)
                        if(criteria.get(r).direction == Criterium.Direction.MAX)    {
                            d = alternatives.get(i).criteriaValues.get(r) - alternatives.get(j).criteriaValues.get(r);
                        }
                        else if(criteria.get(r).direction == Criterium.Direction.MIN)    {
                            d = -1 * (alternatives.get(i).criteriaValues.get(r) - alternatives.get(j).criteriaValues.get(r));
                        }
                        
                        if(d<=criteria.get(r).q)    {
                            pd = 0; // jesli roznica jest mniejsza lub rowna progowi obojetnosci dla kryterium r wartosc relacji preferencji = 0
                        }
                        else if(d<=criteria.get(r).p)    {
                            pd = (d - criteria.get(r).q)/(criteria.get(r).p-criteria.get(r).q); // jesli roznica jest wieksza od progu obojetnosci i mniejsza lub rĂłwna progowi scislej preferencji dla kryterium r wyznacz na podstawie funkcji liniowej wartosc relacji preferencji
                        }
                        else    {
                            pd = 1; // jesli roznica jest wieksza od progu scislej preferencji dla kryterium r wartosc relacji preferencji = 1
                        }
                        mpd.set(i, j, mpd.get(i, j) + criteria.get(r).weight * pd);
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
            
            alternatives.get(i).mpf_plus = mpf_plus.get(i, 0);
            alternatives.get(i).mpf_minus = mpf_minus.get(i, 0);
            alternatives.get(i).mpf =  alternatives.get(i).mpf_plus -  alternatives.get(i).mpf_minus;
        }
        
    }
    
}
