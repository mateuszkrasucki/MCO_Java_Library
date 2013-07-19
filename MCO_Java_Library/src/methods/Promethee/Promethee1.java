package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Mateusz Krasucki
 */
public class Promethee1 {
    
    private LinkedList<Criterium> criteria;
    private LinkedList<Alternative> alternatives;
    
    private LinkedList<Alternative> ranking;
    
    private int criteriaNum_; 
    private int alternativesNum_;
    
    private SimpleMatrix mpd;
    
    public Promethee1(int criteriaNum) {
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
        Collections.sort(ranking,new Comparator<Alternative>() {
         @Override
         public int compare(Alternative o1, Alternative o2) {
             if(o1.getMpfPlus()<o2.getMpfPlus())    {
                 return 1;
             }
             else if(o1.getMpfPlus()>o2.getMpfPlus())   {
                 return -1;
             }
             return 0;
         }
     });
    }
    
    public int getP1Preference(Alternative a, Alternative b)   {
        if((a.getMpfPlus()>b.getMpfPlus()) && (a.getMpfMinus()<=b.getMpfMinus())) {
            return 1;         
        }
        else if((a.getMpfPlus()==b.getMpfMinus()) && (a.getMpfMinus()<b.getMpfMinus())) {
            return 1;
        }
        else if((a.getMpfPlus()==b.getMpfPlus()) && (a.getMpfMinus() == b.getMpfMinus()))   {
            return 0;
        }
        else if((a.getMpfPlus()>b.getMpfPlus()) && (a.getMpfMinus()>b.getMpfMinus()))   {
            return -1;
        }
        else if ((a.getMpfPlus()<b.getMpfPlus()) && (a.getMpfMinus()<b.getMpfMinus())) {
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
    
    public Alternative getAlternativeByRank(int rank)    {
        return ranking.get(rank);
    }

    public void setAlternatives(LinkedList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }
    
    public LinkedList<Alternative> getRanking() {
        return ranking;
    }
    
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getMpfPlus();
        }
        return 0;
    }
    
    public double getAlternativeValue(Alternative alt)    {
        return alt.getMpfPlus();
    }
    
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}
