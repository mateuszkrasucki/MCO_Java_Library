package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import org.ejml.simple.SimpleMatrix;


/**
 *
 * @author Mateusz Krasucki
 */
public class Promethee {
    
    public LinkedList<Criterium> criteria;
    public LinkedList<Alternative> alternatives;
    
    private int criteriaNum_; 
    private int alternativesNum_;
    
    private SimpleMatrix mpd;
    
    public Promethee(int criteriaNum) {
                alternativesNum_ = 0;
		criteriaNum_ = criteriaNum;
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
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
    
    public void calculatePromethee2 ()  {
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
