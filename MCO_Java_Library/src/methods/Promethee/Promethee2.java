package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Mateusz Krasucki
 */
public class Promethee2 {
    
    private LinkedList<Criterium> criteria;
    private LinkedList<Alternative> alternatives;
    
    private LinkedList<Alternative> ranking;

    private int criteriaNum_; 
    private int alternativesNum_;
    
    private SimpleMatrix mpd;
    
    public Promethee2(int criteriaNum) {
                alternativesNum_ = 0;
		criteriaNum_ = criteriaNum;
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                ranking = new LinkedList<Alternative>();
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
    
    public void calculate()  { 
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
                        if(criteria.get(r).getDirection()  == Criterium.Direction.MAX)    {
                            d = alternatives.get(i).getCriteriumValue(r) - alternatives.get(j).getCriteriumValue(r);
                        }
                        else if(criteria.get(r).getDirection() == Criterium.Direction.MIN)    {
                            d = -1 * (alternatives.get(i).getCriteriumValue(r) - alternatives.get(j).getCriteriumValue(r));
                        }
                        
                        if(d<=criteria.get(r).getQ())    {
                            pd = 0; // jesli roznica jest mniejsza lub rowna progowi obojetnosci dla kryterium r wartosc relacji preferencji = 0
                        }
                        else if(d<=criteria.get(r).getP())    {
                            pd = (d - criteria.get(r).getQ())/(criteria.get(r).getP()-criteria.get(r).getQ()); // jesli roznica jest wieksza od progu obojetnosci i mniejsza lub rĂłwna progowi scislej preferencji dla kryterium r wyznacz na podstawie funkcji liniowej wartosc relacji preferencji
                        }
                        else    {
                            pd = 1; // jesli roznica jest wieksza od progu scislej preferencji dla kryterium r wartosc relacji preferencji = 1
                        }
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
            
            alternatives.get(i).setMpf_plus(mpf_plus.get(i, 0));
            alternatives.get(i).setMpf_minus(mpf_minus.get(i, 0));
            alternatives.get(i).setMpf(alternatives.get(i).getMpf_plus() -  alternatives.get(i).getMpf_minus());
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
