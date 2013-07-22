package methods.MAUT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * MAUT (Multi-Attribute Utility Theory) method class. 
 * @author Mateusz Krasucki
 */
public class MAUT {
    
        /**
         * LinkedList containing all the criteria in MCO problem represented by UTASTAR method object.
        */
        private LinkedList<Criterium> criteria;
        /**
         * LinkedList containing all the alternatives in MCO problem represented by UTASTAR method object. Those alternatives will be scored using information obtained from reference alternatives preference standings (outranking) provided by decision maker.
        */
        private LinkedList<Alternative> alternatives;
        /**
         * LinkedList containing all the alternatives in MCO problem represented by UTASTAR method object ordered by their score calculated by UTASTAR method based on reference alternative preference standings.
        */
        private LinkedList<Alternative> ranking;
   
        /**
         * Auxillary variable.
         */
        private int iterationCount;
       
    
        /**
	* MAUT class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
        * It should be structured as shown in example csv file in dataFileExamples/maut.csv.
	*/
	public MAUT(String filename) {		
		alternatives = new LinkedList<Alternative>();
                ranking = new LinkedList<Alternative>();
                criteria = new LinkedList<Criterium>();
			
		BufferedReader br = null;
		String line = "";
		int altsCount = 0;
                int altsIterationCount = 0;
                int normalCriteriaCount = 0;
                Map<String, Criterium> root = new HashMap<String, Criterium>();
                Map<String, Criterium> children = new HashMap<String, Criterium>();
                
                String[] values;
		
		try {
                    br = new BufferedReader(new FileReader(filename));
                    line = br.readLine(); 
                    while(line != null)    {
                        values = line.split(",");
                        if(values[0].contentEquals("GroupCriterium") && altsCount == 0)   {
                                if(values.length==4)    {
                                    if(values[1].contentEquals("root"))  {
                                        GroupCriterium criterium = new GroupCriterium(values[2],Double.parseDouble(values[3]));
                                        root.put(values[2], criterium);
                                    }
                                    else    {
                                        if(root.containsKey(values[1]))   {
                                            GroupCriterium criterium = new GroupCriterium(values[2],Double.parseDouble(values[3]));
                                            children.put(values[2], criterium);
                                            GroupCriterium parent = (GroupCriterium)root.get(values[1]);
                                            parent.addInnerCriterium(criterium);
                                        }
                                        else if(children.containsKey(values[1])) {
                                            GroupCriterium criterium = new GroupCriterium(values[2],Double.parseDouble(values[3]));
                                            children.put(values[2], criterium);
                                            GroupCriterium parent = (GroupCriterium)children.get(values[1]);
                                            parent.addInnerCriterium(criterium); 
                                        }
                                        else    {
                                            throw new Exception("Wrong file format");
                                        }
                                    }
                                    
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }
                        }
                        else if(values[0].contentEquals("NormalCriterium") && altsCount == 0)   {
                                if(values.length==7)    {
                                    NormalCriterium criterium;
                                    if(values[4].contentEquals("LINEAR"))   {
                                        criterium = new NormalCriterium(values[2],Double.parseDouble(values[3]),NormalCriterium.UtilityFunctionType.LINEAR,Double.parseDouble(values[5]),Double.parseDouble(values[6]));
                                    }
                                    else if(values[4].contentEquals("EXPONENTIAL"))   {
                                        criterium = new NormalCriterium(values[2],Double.parseDouble(values[3]),NormalCriterium.UtilityFunctionType.EXPONENTIAL,Double.parseDouble(values[5]),Double.parseDouble(values[6]));
                                    }
                                    else    {
                                            throw new Exception("Wrong file format");
                                    }
                                    
                                    if(values[1].contentEquals("root"))   {
                                        root.put(values[2], criterium);
                                        normalCriteriaCount++;
                                    }
                                    else  {
                                        if(root.containsKey(values[1]))   {
                                            GroupCriterium parent = (GroupCriterium)root.get(values[1]);
                                            parent.addInnerCriterium(criterium);
                                            normalCriteriaCount++;
                                        }
                                        else if(children.containsKey(values[1])) {
                                            GroupCriterium parent = (GroupCriterium)children.get(values[1]);
                                            parent.addInnerCriterium(criterium); 
                                            normalCriteriaCount++;
                                        }
                                        else    {
                                            throw new Exception("Wrong file format");
                                        } 
                                    }
                                }
                                else if(values.length==8 && values[4].contentEquals("EXPONENTIAL")) {
                                        NormalCriterium criterium = new NormalCriterium(values[2],Double.parseDouble(values[3]),Double.parseDouble(values[5]),Double.parseDouble(values[6]),Double.parseDouble(values[7]));
                                    if(values[1].contentEquals("root"))   {
                                        root.put(values[2], criterium);
                                        normalCriteriaCount++;
                                    }
                                    else  {
                                        if(root.containsKey(values[1]))   {
                                            GroupCriterium parent = (GroupCriterium)root.get(values[1]);
                                            parent.addInnerCriterium(criterium);
                                            normalCriteriaCount++;
                                        }
                                        else if(children.containsKey(values[1])) {
                                            GroupCriterium parent = (GroupCriterium)children.get(values[1]);
                                            parent.addInnerCriterium(criterium); 
                                            normalCriteriaCount++;
                                        }
                                        else    {
                                            throw new Exception("Wrong file format");
                                        } 
                                    }
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }
                        }
                        else if (values[0].contentEquals("Alternatives") && altsCount == 0 && normalCriteriaCount != 0)   {
                            Iterator it = root.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pairs = (Map.Entry)it.next();
                                this.addCriterium((Criterium)pairs.getValue());
                                it.remove(); 
                            }                             
                            for(int i=1; i<values.length;i++)   {
                                Alternative alternative = new Alternative(values[i]);
                                this.addAlternative(alternative);
                                altsCount++;
                            }
                        } 
                        else if(altsCount!=0 && normalCriteriaCount!=0 && altsIterationCount < altsCount && values.length == normalCriteriaCount)   {
                            for(int i=0; i<values.length; i++)  {
                                alternatives.get(altsIterationCount).addCriteriumValue(Double.parseDouble(values[i]));
                            }
                            altsIterationCount++;
                        }
                        else    {
                            throw new Exception("Wrong file format");
                        }
                        line = br.readLine(); 
                    }
                   
                    br.close();
                }
                catch (FileNotFoundException e) {
        			e.printStackTrace();
		}
		catch (IOException e) {
				e.printStackTrace();
				
                } catch (Exception e) {
        			e.printStackTrace();
		}
               					
    }
	    
    
    /**
     * Basic MAUT class constructor.
     * The MAUT object created by this constructor is empty. Epsilon parameter value is set to default (0.0001).
     */
    public MAUT() {
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                ranking = new LinkedList<Alternative>();
    }
    
     /**
     * MAUT class constructor with all the important data as paramaters.
     * @param criteria LinkedList object containing Criterium objects which represent criteria in MCO problem. 
     * @param alternatives LinkedList object containing Alternatice objects which represent alternatives in MCO problem.
     */
    public MAUT(LinkedList<Criterium> criteria, LinkedList<Alternative> alternatives) {
                this.criteria = criteria;
                this.alternatives = alternatives;
                ranking = new LinkedList<Alternative>();
    }
    
     /**
     * Adds criterium to MAUT method object.
     * @param criterium Criterium object.
     */
    public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
    }
    
    
     /**
     * Adds alternative to MAUT object.
     * @param alternative Alternative object.
     */
    public void addAlternative(Alternative alternative)   {
       alternative.setId(alternatives.size()+1);
       alternatives.add(alternative);
    }
   
    /**
     * Normalize weights within group criterium.
     * @param groupCriterium Group criterium which child criteria weight will be normalized. 
     */
     private void normalizeWeightsInGroup(GroupCriterium groupCriterium)  {
        double sum = 0;
        for(int i=0; i<groupCriterium.getInnerCriteriaCount(); i++) {
            if(groupCriterium.getInnerCriterium(i).isGroup())   {
                GroupCriterium tmp = (GroupCriterium)groupCriterium.getInnerCriterium(i);
                normalizeWeightsInGroup(tmp);
            }
            sum = sum + groupCriterium.getInnerCriterium(i).getWeight();
        }
        for(int i=0; i<groupCriterium.getInnerCriteriaCount(); i++) {
            groupCriterium.getInnerCriterium(i).setWeight(groupCriterium.getInnerCriterium(i).getWeight()/sum);
        }
    }
            
    /**
     * Normalizes weight of criteria added to MAUT method object to ensure that sum of all the criteria weights equals 1.
     */
    public void normalizeWeights()  {
        double sum = 0;
        for(int i=0; i<criteria.size();i++) {
            if(criteria.get(i).isGroup())   {
                GroupCriterium groupCriterium = (GroupCriterium)criteria.get(i);
                normalizeWeightsInGroup(groupCriterium);
            }
            sum = sum + criteria.get(i).getWeight();
        }
        for(int i=0; i<criteria.size();i++) {
            criteria.get(i).setWeight(criteria.get(i).getWeight()/sum);
        }
    }   
    
    /**
     * Calculates value of the specific criterium of the alternative. It is reccurent when nested group criteria are involved.
     * @param alternative Alternative for which criterium value will be calculated.
     * @param criterium Criterium for which value will be calculates.
     */
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
    
    
      /**
     * Performs MAUT method calculations on data added to MAUT object.
     */
    public void calculate ()  {     
        normalizeWeights();
        
        for(int i=0; i<alternatives.size(); i++)    {
            iterationCount = 0;
            double tmpScore = 0;
            for(int j=0; j<criteria.size(); j++)    {
                tmpScore = tmpScore + calculateCriterium(alternatives.get(i), criteria.get(j));
            }
            alternatives.get(i).setScore(tmpScore);
        }
        
        ranking = new LinkedList<Alternative>(alternatives);
        Collections.sort(ranking, new Comparator<Alternative>() {
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
    
      /**
     * Returns list of all the criteria in MAUT object.
     * @return LinkedList containing Criterium objects.
     */
    public LinkedList<Criterium> getCriteria() {
        return criteria;
    }
    
      /**
     * Returns Criterium with the i-th order number. 
     * @param i Criterium order number.
     * @return Criterium object.
     */
    public Criterium getCriterium(int i)    {
        return criteria.get(i);
    }

        /**
     * Sets criteria in MAUT object to LinkedList provided as parameter.
     * @param criteria LinkedList object containing Criterium objects.
     */
    public void setCriteria(LinkedList<Criterium> criteria) {
        this.criteria = criteria;
    }

        /**
    * Return all the alternatives stored in MAUT object.
     * @return LinkedList containing Alternative objects.
     */
    public LinkedList<Alternative> getAlternatives() {
        return alternatives;
    }
    
        /**
     * Returns i-th alternative.
     * @param i Alternative order number.
     * @return Alternative object.
     */
    public Alternative getAlternative(int i)    {
        return alternatives.get(i);
    }


        /**
     * Sets alternatives in MAUT object to LinkedList provided as parameter.
     * @param alternatives LinkedList object containing Alternative objects.
     */
    public void setAlternatives(LinkedList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }
    

        /**
     * Returns ranking - all the alternatives in MAUT object ordered by their score calculated by MAUT method. 
     * @return LinkedList object containing Alternative objects ordered by their MAUT score. 
     */
    public LinkedList<Alternative> getRanking() {
        return ranking;
    }
    
        /**
     * Returns MAUT score of i-th alternative.
     * @param i Alternative order number.
     * @return i-th alternative MAUT score.
     */
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getScore();
        }
        return 0;
    }
    

        /**
     * Returns MAUT score of alternative alt. It has to be one of the alternatives added to the object before running calculate() method.
     * @param alt Alternative object.
     * @return MAUT score of alternative object.
     */
    public double getAlternativeValue(Alternative alt)    {
        return alt.getScore();
    }
        
    /**
     * Returns alternative with specific rank in ranking calculated by MAUT method.
     * @param rank Rank number of wanted alternative.
     * @return Alternative object of alternative with wanted rank.
     */
    public Alternative getAlternativeByRank(int rank)    {
        return ranking.get(rank-1);
    }        
    
    /**
     * Returns number of criteria in MAUT object.
     * @return Number of criteria in MAUT object.
     */
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    /**
     * Returns number of alternatives in MAUT object.
     * @return Number of alternatives in MAUT object.
     */
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}