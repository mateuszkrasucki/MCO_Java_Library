package methods.Promethee;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import org.ejml.simple.SimpleMatrix;
import java.util.HashMap;
import java.util.Map;
/**
 * Abstract Promethee class on which Promethee1, Promethee2 and Promethee5 classes are based. 
 * @author Mateusz Krasucki
 */
public abstract class Promethee {
    /**
     * LinkedList containing all the criteria in MCO problem represented by Promethee method object.
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Criterium> criteria;
    /**
     * LinkedList containing all the alternatives in MCO problem represented by Promethee method object.
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Alternative> alternatives;
    /**
     * LinkedList containing all the constraint in MCO problem represented by Promethee5 method object. 
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Constraint> constraints;
    
    /**
     * LinkedList containing all the alternatives in MCO problem represented by Promethee method object ordered by their score calculated by Promethee method.
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Alternative> ranking;
    
    /**
     * Simple matrix object containing aggregated preference indices as calculated by Promethee family method.
     */
    protected SimpleMatrix mpd;
    
        /**
	* Promethee class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
        * It should be structured as shown in example csv file in dataFileExamples/promethee.csv.
	*/
	public Promethee(String filename) {	
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                constraints = new LinkedList<Constraint>();
                ranking = new LinkedList<Alternative>();
			
		BufferedReader br = null;
		String line = "";
		int altsCount = 0;
                int altsIterationCount = 0;
                int criteriaCount = 0;
                Map<String, Criterium> criteriaByName = new HashMap<String, Criterium>();
                
                String[] values;
		
		try {
                    br = new BufferedReader(new FileReader(filename));
                    line = br.readLine(); 
                    while(line != null)    {
                        values = line.split(",");
                        if(values[0].contentEquals("Criterium") && values.length>=5 && altsCount == 0)   {
                                PreferenceFunction preferenceFunction = new LinearPreferenceFunction();
                                if(values[4].contentEquals("LINEAR")) {                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new LinearPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else if(values.length == 7) {
                                        preferenceFunction = new LinearPreferenceFunction(Double.parseDouble(values[5]),Double.parseDouble(values[6]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else if(values[4].contentEquals("THRESHOLD")) {
                                    preferenceFunction = new ThresholdPreferenceFunction();
                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new ThresholdPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else if(values[4].contentEquals("GAUSSIAN")) {
                                    preferenceFunction = new GaussianPreferenceFunction();
                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new GaussianPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else if(values[4].contentEquals("LEVEL")) {
                                    preferenceFunction = new LevelPreferenceFunction();
                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new LevelPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else if(values.length == 7) {
                                        preferenceFunction = new LevelPreferenceFunction(Double.parseDouble(values[5]),Double.parseDouble(values[6]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }

                                    Criterium criterium;
                                    if(values[3].contentEquals("MAX"))  {
                                        criterium = new Criterium(values[1],Criterium.Direction.MAX,Double.parseDouble(values[2]),preferenceFunction);
                                    }
                                    else if(values[3].contentEquals("MIN"))  {
                                        criterium = new Criterium(values[1],Criterium.Direction.MIN,Double.parseDouble(values[2]),preferenceFunction);
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }
                                    this.addCriterium(criterium);
                                    criteriaByName.put(values[1], criterium);  
                                    criteriaCount++;
                        }
                        else if(values[0].contentEquals("Constraint") && values.length == 4 && altsCount == 0 && criteriaCount!=0)   {
                                    if(criteriaByName.containsKey(values[1]))   {
                                        Criterium criterium = criteriaByName.get(values[1]);
                                        if(values[2].contentEquals("UPPER"))   {
                                            Constraint constraint = new Constraint(criterium, Constraint.ConstrainType.UPPER, Double.parseDouble(values[3]));
                                            this.addConstraint(constraint);
                                        }
                                        else if(values[2].contentEquals("BOTTOM"))   {
                                            Constraint constraint = new Constraint(criterium, Constraint.ConstrainType.BOTTOM, Double.parseDouble(values[3]));
                                            this.addConstraint(constraint);
                                        }
                                        else    {
                                                throw new Exception("Wrong file format");
                                        }
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }        
                        }
                        else if (values[0].contentEquals("Alternatives") && altsCount == 0 && criteriaCount != 0)   {                          
                            for(int i=1; i<values.length;i++)   {
                                Alternative alternative = new Alternative(values[i]);
                                this.addAlternative(alternative);
                                altsCount++;
                            }
                        } 
                        else if(altsCount!=0 && criteriaCount!=0 && altsIterationCount < altsCount && values.length == criteriaCount)   {
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
     * Basic Promethee class constructor.
     * The Promethee object created by this constructor is empty (no alternatives, criteria or constraints set).
     */
    public Promethee() {
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                constraints = new LinkedList<Constraint>();
                ranking = new LinkedList<Alternative>();
    }
    
    
     /**
     * Adds criterium to Promethee method object.
     * @param criterium Criterium object.
     */
    public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
    }
    
    
     /**
     * Adds alternative to Promethee method object.
     * @param alternative Alternative object.
     */
    public void addAlternative(Alternative alternative)   {
       alternative.setId(alternatives.size()+1);
       alternatives.add(alternative);
    }
   
     /**
     * Adds constraint to Promethee method object.
     * Here it is private cause it is used by common constructor methods. It is made public only in Promethee5 child class because constraints are used in this method.
     * @param constraint Constraint object.
     */
    private void addConstraint(Constraint constraint)  {
        if(criteria.indexOf(constraint.getCriterium())!=-1)  {
            constraints.add(constraint);
        }
    }
    
    /**
     * Normalizes weight of criteria added to Promethee method object to ensure that sum of all the criteria weights equals 1.
     */
    public void normalizeWeights()  {
        double sum = 0;
        for(int i=0; i<criteria.size();i++) {
            sum = sum + criteria.get(i).getWeight();
        }
        for(int i=0; i<criteria.size();i++) {
            criteria.get(i).setWeight(criteria.get(i).getWeight()/sum);
        }
    }   
    
      /**
     * Performs Promethee method calculations on data added to Promethee object.
     * Abstract method, implemented in child classes which represent specific Promethee family methods.
     */
    public abstract void calculate();
    
   
    /**
     * Calculates aggregated preference indices matrix (MPD matrix).
     */
    protected void calculateMPD()  {
        mpd = new SimpleMatrix(this.getAlternativesNum(),this.getAlternativesNum());
        mpd.zero();

        for(int i = 0; i<this.getAlternativesNum(); i++)  {
            for(int j=0; j<this.getAlternativesNum(); j++)    {
                if(i!=j)    {
                    for(int r=0; r<this.getCriteriaNum(); r++)    {
                        double pd = criteria.get(r).calculatePreference(alternatives.get(i).getCriteriumValue(r), alternatives.get(j).getCriteriumValue(r));
                        mpd.set(i, j, mpd.get(i, j) + criteria.get(r).getWeight() * pd);
                    }
                }
            }
        }
    }
    
    /**
     * Calculates multicriteria preference flows (MPF+, MPF- and MPF).
     */
    protected void calculateMPF()  {
        SimpleMatrix mpf_plus = new SimpleMatrix(this.getAlternativesNum(),1);
        mpf_plus.zero();
        SimpleMatrix mpf_minus = new SimpleMatrix(this.getAlternativesNum(),1);
        mpf_minus.zero();

        for(int i=0; i<this.getAlternativesNum();i++)    {
            for(int j=0; j<this.getAlternativesNum(); j++)   {
                if(i!=j)    {
                    mpf_plus.set(i, 0, mpf_plus.get(i, 0) + mpd.get(i, j));
                    mpf_minus.set(i, 0, mpf_minus.get(i, 0) + mpd.get(j, i));
                }
            }
            mpf_plus.set(i,0,mpf_plus.get(i,0)/(this.getAlternativesNum()-1));
            mpf_minus.set(i,0,mpf_minus.get(i,0)/(this.getAlternativesNum()-1));
            
            alternatives.get(i).setMpfPlus(mpf_plus.get(i, 0));
            alternatives.get(i).setMpfMinus(mpf_minus.get(i, 0));
            alternatives.get(i).setMpf(alternatives.get(i).getMpfPlus() -  alternatives.get(i).getMpfMinus());
        }
        
    }
      /**
     * Returns list of all the criteria in Promethee object.
     * @return LinkedList containing Criterium objects.
     */
    public LinkedList<Criterium> getCriteria() {
        return criteria;
    }
    
      /**
     * Returns Criterium with the i-th order number. 
     * @param i Criterium order number.
     * @return I-th criterium object.
     */
    public Criterium getCriterium(int i)    {
        return criteria.get(i);
    }

        /**
     * Sets criteria in Promethee object to LinkedList provided as a parameter.
     * @param criteria LinkedList object containing Criterium objects.
     */
    public void setCriteria(LinkedList<Criterium> criteria) {
        this.criteria = criteria;
    }

        /**
    * Return all the alternatives stored in Promethee object.
     * @return LinkedList containing Alternative objects.
     */
    public LinkedList<Alternative> getAlternatives() {
        return alternatives;
    }
    
        /**
     * Returns Alternative with the i-th order number.
     * @param i Alternative order number.
     * @return Alternative object.
     */
    public Alternative getAlternative(int i)    {
        return alternatives.get(i);
    }


        /**
     * Sets alternatives in Promethee object to LinkedList provided as parameter.
     * @param alternatives LinkedList object containing Alternative objects.
     */
    public void setAlternatives(LinkedList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }
    

        /**
     * Returns ranking - all the alternatives in Promethee object ordered by their score calculated by Promethee method. 
     * @return LinkedList object containing Alternative objects ordered by their Promethee score. 
     */
    public LinkedList<Alternative> getRanking() {
        return ranking;
    }
            
    /**
     * Returns alternative with specific rank in ranking calculated by Promethee method.
     * @param rank Rank number of wanted alternative.
     * @return Alternative object of alternative with wanted rank.
     */
    public Alternative getAlternativeByRank(int rank)    {
        return ranking.get(rank-1);
    }        
    
    /**
     * Returns number of criteria in Promethee object.
     * @return Number of criteria in Promethee object.
     */
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    /**
     * Returns number of criteria in Promethee object.
     * @return Number of criteria in Promethee object.
     */
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}
