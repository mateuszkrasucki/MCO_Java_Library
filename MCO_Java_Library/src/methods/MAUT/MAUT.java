package methods.MAUT;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
/**
 *
 * @author Mateusz Krasucki
 */
public class MAUT {
    
    public LinkedList<Criterium> criteria;
    public LinkedList<Alternative> alternatives;
    
    
    private int iterationCount;
       
    
    public MAUT() {
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
    }
    
    public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
    }
    
    
    public void addAlternative(Alternative alternative)   {
       alternative.setId(alternatives.size()+1);
       alternatives.add(alternative);
    }
    
     private void normalizeWeightsInGroup(GroupCriterium groupCriterium)  {
        double sum = 0;
        for(int i=0; i<groupCriterium.getInnerCriteriaCount(); i++) {
            if(groupCriterium.getInnerCriterium(i).isGroup())   {
                GroupCriterium tmp = (GroupCriterium)groupCriterium.getInnerCriterium(i);
                normalizeWeightsInGroup(tmp);
            }
            sum = sum + groupCriterium.getInnerCriterium(i).getWeight();
        }
        System.out.println(groupCriterium.getName() + " " + sum);
        for(int i=0; i<groupCriterium.getInnerCriteriaCount(); i++) {
            groupCriterium.getInnerCriterium(i).setWeight(groupCriterium.getInnerCriterium(i).getWeight()/sum);
        }
    }
            
    public void normalizeWeights()  {
        double sum = 0;
        for(int i=0; i<criteria.size();i++) {
            if(criteria.get(i).isGroup())   {
                GroupCriterium groupCriterium = (GroupCriterium)criteria.get(i);
                normalizeWeightsInGroup(groupCriterium);
            }
            sum = sum + criteria.get(i).getWeight();
        }
        System.out.println(sum);
        for(int i=0; i<criteria.size();i++) {
            criteria.get(i).setWeight(criteria.get(i).getWeight()/sum);
        }
    }   
    
    private double calculateCriterium(Alternative alternative, Criterium criterium)    {
        if(criterium.isGroup())  {
            GroupCriterium groupCriterium = (GroupCriterium)criterium;
            double sum = 0;
            for(int j=0; j < groupCriterium.getInnerCriteriaCount(); j++) {
                sum = sum + calculateCriterium(alternative, groupCriterium.getInnerCriterium(j));
            }
            return groupCriterium.getWeight() * sum;
        }
        NormalCriterium normalNode = (NormalCriterium)criterium;
        iterationCount++;
        if(iterationCount>alternative.getCriteriaValues().size())   {
               throw new IndexOutOfBoundsException("Alternative object has wrong criteria values list.");
        }
        return normalNode.getUtilityFunction().calculate(alternative.getCriteriaValues().get(iterationCount-1)) * normalNode.getWeight();
     }
    
    
    public void calculateMAUT ()  {     
        normalizeWeights();
        
        for(int i=0; i<alternatives.size(); i++)    {
            iterationCount = 0;
            double tmpScore = 0;
            for(int j=0; j<criteria.size(); j++)    {
                tmpScore = tmpScore + calculateCriterium(alternatives.get(i), criteria.get(j));
            }
            alternatives.get(i).setScore(tmpScore);
        }
        
        Collections.sort(alternatives, new Comparator<Alternative>() {
         @Override
         public int compare(Alternative o1, Alternative o2) {
             if(o1.getScore()<o2.getScore())    {
                 return 1;
             }
             else if(o1.getScore()>o2.getScore())  {
                 return -1;
             }
             return 0;
         }
     });
    
    }
}