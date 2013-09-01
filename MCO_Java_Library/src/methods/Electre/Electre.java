package methods.Electre;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import methods.Electre.Alternative;
import methods.Electre.Criterium;
import methods.Electre.Criterium.Direction;

public abstract class Electre {

    /**
     * LinkedList containing all the criteria in MCO problem represented by Promethee method object.
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Criterium> criteria_;
    
    /**
     * LinkedList containing all the alternatives in MCO problem represented by Promethee method object.
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Alternative> alternatives_;
    
    /**
     * LinkedList containing all the alternatives in MCO problem represented by Promethee method object ordered by their score calculated by Promethee method.
     * It is protected because it is meant to be accesed only by child classes which represent indicidual MCO methods from Promethee methods set.
     */
    protected LinkedList<Alternative> ranking_;
    
    /**
     * Alternatives count for iteration purposes
     */
    protected int altsCount_;
    
    /**
     * Criteria count for iteration purposes
     */
    protected int criteriaCount_;
    
    /**
     * Concordance condition threshold, should be set to between 0.5 - (1-minimal weight). By default it is set to 0.5
     */
    protected Double s_;
    /**
     * Discordance condition threshold. By default it is set to 0.1
     */
    protected Double v_;

    /**
     * Matrix containing concordance indices for each pair of alternatives. 
     */
    protected SimpleMatrix concordanceMatrix_;
    
    /**
     * Matrix containing discordance indices for each pair of alternatives. 
     */
	protected SimpleMatrix discordanceMatrix_;	
    
    /**
     * Matrix used in ElectreIII and ElectreTri. Contains credibility indices for each pair of alternatives.
     */
	protected SimpleMatrix credibilityMatrix_;
    
    /**
     * Matrix computed at the beginning of every Electre method. 
     * Normalizes the values of every criterium for every alternative, regarding criteria weights.
     */
    protected SimpleMatrix normalized_;
    
	/**
	* Electre class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
    @SuppressWarnings("resource")
	public Electre(String filename) {
    	
        criteria_ = new LinkedList<Criterium>();
        alternatives_ = new LinkedList<Alternative>();
        ranking_ = new LinkedList<Alternative>();

		BufferedReader br = null;
		String line = "";
		int altsCount = 0;
        int altsIterationCount = 0;
        int criteriaCount = 0;
		String[] values;
		
		try {
            br = new BufferedReader(new FileReader(filename));
            line = br.readLine(); 
            while(line != null)    {
                values = line.split(",");
                if(altsCount == 0 && values[0].contentEquals("Criterium") && values.length>=5 )   {
                	
                	Criterium cr;
                	Double veto = 0.0, p = 0.0, q = 0.0;
                	if (values.length > 4) {
                		veto = Double.parseDouble(values[4]);
                	}
                	if (values.length > 5) {
                		p = Double.parseDouble(values[5]);
                		q = Double.parseDouble(values[6]);
                	}
                		
                	if(values[3].equalsIgnoreCase("MAX"))  {
                		cr = new Criterium(values[1],Double.parseDouble(values[2]),Criterium.Direction.MAX, veto, p, q);
                	}
	                else if(values[3].equalsIgnoreCase("MIN"))  {
	                	cr = new Criterium(values[1],Double.parseDouble(values[2]),Criterium.Direction.MIN, veto, p, q);
	                }
	                else    {
	                	throw new Exception("Wrong file format");
	                }
                this.addCriterium(cr);
                criteriaCount++;
                }
                else if ( altsCount == 0 && values[0].contentEquals("Alternatives") && criteriaCount != 0)   {                          
                    for(int i=1; i<values.length;i++)   {
                        Alternative alternative = new Alternative(values[i]);
                        this.addAlternative(alternative);
                        altsCount++;
                    }
                } 
                else if(altsCount!=0 && criteriaCount!=0 && altsIterationCount < altsCount && values.length == criteriaCount)   {
                    for(int i=0; i<values.length; i++)  {
                        alternatives_.get(altsIterationCount).addCriteriumValue(Double.parseDouble(values[i]));
                    }
                    altsIterationCount++;
                }
                else    {
                    throw new Exception("Wrong file format");
                }
                line = br.readLine(); 
            }
           
            br.close();
            
            criteriaCount_ = criteriaCount;
            altsCount_ = altsCount;
            
    		concordanceMatrix_ = new SimpleMatrix(altsCount_, altsCount_);
    		discordanceMatrix_ = new SimpleMatrix(altsCount_, altsCount_);
            s_=0.5;
            v_=0.1;
    			
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
	* Electre class constructor
	*/
    public Electre() {
        criteria_ = new LinkedList<Criterium>();
        alternatives_ = new LinkedList<Alternative>();
        ranking_ = new LinkedList<Alternative>();
        s_=0.5;
        v_=0.1;
}

    /**
    * Adds criterium to Electre method object.
    * @param criterium Criterium object.
    */
   public void addCriterium(Criterium criterium)   {
           criteria_.add(criterium);
   }
   
    /**
    * Adds alternative to Electre method object.
    * @param alternative Alternative object.
    */
   public void addAlternative(Alternative alternative)   {
      alternative.setId(alternatives_.size()+1);
      alternatives_.add(alternative);
   }
   
   /**
    * Normalizes weight of criteria added to Electre method object to ensure that sum of all the criteria weights equals 1.
    */
   public void normalizeWeights()  {
       double sum = 0;
       for(int i=0; i<criteria_.size();i++) {
           sum = sum + criteria_.get(i).getWeight();
       }
       for(int i=0; i<criteria_.size();i++) {
           criteria_.get(i).setWeight(criteria_.get(i).getWeight()/sum);
       }
   }   
	
	   /**
	  * Returns list of all the criteria in Electre object.
	  * @return LinkedList containing Criterium objects.
	  */
	 public LinkedList<Criterium> getCriteria() {
	     return criteria_;
	 }
	
	 /**
	* Returns Criterium with the i-th order number. 
	* @param i Criterium order number.
	* @return I-th criterium object.
	*/
	public Criterium getCriterium(int i)    {
	   return criteria_.get(i);
	}
	
	/**
	* Sets criteria in Electre object to LinkedList provided as a parameter.
	* @param criteria LinkedList object containing Criterium objects.
	*/
	public void setCriteria(LinkedList<Criterium> criteria) {
	this.criteria_ = criteria;
	}
	
	/**
	* Return all the alternatives stored in Electre object.
	* @return LinkedList containing Alternative objects.
	*/
	public LinkedList<Alternative> getAlternatives() {
	return alternatives_;
	}
	
	/**
	* Returns Alternative with the i-th order number.
	* @param i Alternative order number.
	* @return Alternative object.
	*/
	public Alternative getAlternative(int i)    {
	return alternatives_.get(i);
	}

	/**
	* Sets alternatives in Electre object to LinkedList provided as parameter.
	* @param alternatives LinkedList object containing Alternative objects.
	*/
	public void setAlternatives(LinkedList<Alternative> alternatives) {
	this.alternatives_ = alternatives;
	}

	/**
	* Returns ranking - all the alternatives in Electre object ordered by their score calculated by Electre method. 
	* @return LinkedList object containing Alternative objects ordered by their Electre score. 
	*/
	public LinkedList<Alternative> getRanking() {
	return ranking_;
	}

    /**
     * Returns alternative with specific rank in ranking calculated by Electre method.
     * @param rank Rank number of wanted alternative.
     * @return Alternative object of alternative with wanted rank.
     */
    public Alternative getAlternativeByRank(int rank)    {
        return ranking_.get(rank-1);
    }        
    
    /**
     * Returns number of criteria in Electre object.
     * @return Number of criteria in Electre object.
     */
    public int getCriteriaNum() {
        return this.criteria_.size();
    }
    
    /**
     * Returns number of criteria in Electre object.
     * @return Number of criteria in Electre object.
     */
    public int getAlternativesNum() {
        return this.alternatives_.size();
    }
    
	/**
	 * Checks whether alternative a is better than alternative b
	 * @param a alternative a index
	 * @param b alternative b index
	 * @param criterium Criterium index
	 * @return true if alternative a is better than alternative b, otherwise false
	 */
	protected Boolean is_alternative_preferred(int a, int b, int criterium) {
		if (criteria_.get(criterium).getDirection() == Criterium.Direction.MAX) 
			return (normalized_.get(criterium,a) >= normalized_.get(criterium,b));
		
		else 
			return (normalized_.get(criterium,a) <= normalized_.get(criterium,b));
	}

	/**
	 * Helper method
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return concordance value of alternative a over alternative b
	 */
	protected double concordance_index(int a, int b) {
		  double w = 0;
	        for (int i=0; i<criteriaCount_; i++)
	        	if (is_alternative_preferred(a, b, i))
	            	w += criteria_.get(i).getWeight();
	        return w;
	  }

	/**
	 * Checks whether alternative a is better than alternative b regarding q threshold of given criterium
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return true if alternative a is better than alternative b, otherwise false
	 */
	protected Boolean concordance_condition_with_q_threshold(int a, int b, int cri) {
		
		if (criteria_.get(cri).getDirection()==Direction.MAX) {
			return (normalized_.get(cri,a) + criteria_.get(cri).getQ() >= normalized_.get(cri,b));
		}
		else return (normalized_.get(cri,a) - criteria_.get(cri).getQ() <= normalized_.get(cri,b));
	}

	/**
	 * Returns concordance index for given alternatives regarding the concordance condition with q threshold
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return concordance value of alternative a over alternative b regarding the concordance condition with q threshold
	 */
	protected double concordance_index_with_q(int a, int b) {
		  double cIndex = 0;
		  
	        for (int i=0; i<criteriaCount_; i++) {
	        	if (concordance_condition_with_q_threshold(a, b, i)) {
	        		
	        			cIndex+=criteria_.get(i).getWeight();
	        	}
	        }
	      return cIndex;
	}

	/**
	 * Method calculating concordance matrix for every pair of alternatives
	 */
	protected void calculate_concordance_matrix() {
		
		for (int i=0; i< altsCount_; i++)
			 for (int j=0; j<altsCount_; j++) 
				 if (i!=j) 
					 concordanceMatrix_.set(i, j, concordance_index(i, j));
				 else 
					 concordanceMatrix_.set(i, j, 0);
	}

	/**
	 * Discordance value for a given pair of alternatives
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return discordance value of alternative a with alternative b
	 */
	protected double discordance_index(int a, int b) {
		Double max = 0.0;
		for (int i=0; i<criteriaCount_; i++) {
			if (is_alternative_preferred(b, a, i)) {
				
				Double diff = Math.abs(normalized_.get(i, b) - normalized_.get(i, a));
				if (diff > max)
				max = diff;
			}
		}
		return max;
	  }

	/**
	 * Method calculating discordance matrix for every pair of alternatives
	 */
	protected void calculate_discordance_matrix() {
		
		for (int i=0; i< altsCount_; i++)
			 for (int j=0; j<altsCount_; j++) 
				 if (i!=j) 
					 discordanceMatrix_.set(i, j, discordance_index(i, j));
				 else 
					 discordanceMatrix_.set(i, j, 0);
		}

	/**
	 * Returns discordance index for given alternatives regarding the q and p threshold of given criterium.
	 * Used to compute credibility index for methods ElectreIII and ElectreTri
	 * @param a alternative a index
	 * @param b alternative b index
	 * @param cri criterium index
	 * @return discordance index for given alternatives regarding the q and p threshold of given criterium
	 */
	protected double discordance_index_with_p_q(int a, int b, int cri) {
		
		if (criteria_.get(cri).getDirection() == Direction.MAX) {
		
			if (normalized_.get(cri, a) + criteria_.get(cri).getP() >= 
					normalized_.get(cri, b))
			{
				return 0.0;
			}
			
			if (normalized_.get(cri, a) + criteria_.get(cri).getVeto() < 
					normalized_.get(cri, b))
			{
				return 1.0;
			}
			
		}
		
		else {
			if (normalized_.get(cri, a) - criteria_.get(cri).getP() <= 
				normalized_.get(cri, b))
			{
				return 0.0;
			}
			
			if (normalized_.get(cri, a) - criteria_.get(cri).getVeto() >
					normalized_.get(cri, b))
			{
				return 1.0;
			}
		}

		return ((normalized_.get(cri, b) - normalized_.get(cri, a) - criteria_.get(cri).getP())/
				(criteria_.get(cri).getVeto() - criteria_.get(cri).getP()));
	  }


	/**
	 * Returns credibility index (ElectreIII and ElectreTri methods) for given alternatives regarding the q and p threshold of given criterium
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return credibility index for given alternatives
	 */
	protected double credibility_index(int a, int b) {
		
		double c_index = concordanceMatrix_.get(a, b);
		double cred_ind = c_index;
		
		for (int i=0; i<criteriaCount_; i++) {
			
			double dis_index = discordance_index_with_p_q(a, b, i);
			if (dis_index > c_index ) {
				cred_ind*= (1- dis_index)/(1-c_index);
			}
		}
		return cred_ind;
	}

	/**
	 * Method calculating credibility matrix for every pair of alternatives
	 */
	protected void calculate_credibility_matrix() {
		
		for (int i=0; i< altsCount_; i++)
			 for (int j=0; j<altsCount_; j++) 
				 credibilityMatrix_.set(i, j, credibility_index(i, j));	 
	}
	
	/**
	 * Method preparing the matrix with normalized values of all alternatives, for calculating discordance factor
	 */
	public void normalize() {
		
		normalizeWeights();
		normalized_ = new SimpleMatrix(criteriaCount_, altsCount_);
		
		double[] squares = new double[criteriaCount_];
		
		// temporary list of squarerooted sums of squares of every criterium value
		for (int cri=0; cri<criteriaCount_; cri++)
		{
			double sum=0;
			 for (int i=0; i<altsCount_; i++) {
				 sum += Math.pow(alternatives_.get(i).getCriteriumValue(cri), 2);
			 }
			squares[cri]=Math.sqrt(sum);
		}
		// actual normalizing of alternative values
		for (int cri=0; cri<criteriaCount_; cri++)
			for (int alt=0; alt<altsCount_; alt++) {
				normalized_.set(cri, alt, criteria_.get(cri).getWeight() * alternatives_.get(alt).getCriteriumValue(cri) / squares[cri]);
				criteria_.get(cri).setP_(criteria_.get(cri).getWeight() * criteria_.get(cri).getP() / squares[cri]);
				criteria_.get(cri).setQ_(criteria_.get(cri).getWeight() * criteria_.get(cri).getQ() / squares[cri]);
				criteria_.get(cri).setVeto_(criteria_.get(cri).getWeight() * criteria_.get(cri).getVeto() / squares[cri]);
			}
	}

	/**
	 * Method preparing the ranking list of alternatives
	 * @param rankMap a map containing ranking points of every alternative
	 */
	protected void createAlternativesRanking(HashMap<Integer,Integer> rankMap) {

		 LinkedHashMap<Integer,Integer> sorted_map = sortHashMapByValuesD(rankMap);
	        Integer[] rank = sorted_map.keySet().toArray(new Integer[altsCount_]);
	        Integer[] rankingPoints = sorted_map.values().toArray(new Integer[altsCount_]);
	        
	        for (int i=0; i<altsCount_; i++) {
	        		
	        	Alternative alt = alternatives_.get(rank[i]);
	        	alt.setRankingPoints_(rankingPoints[i]);
	        	ranking_.add(alt);
	        }
	}
	
	/**
	 * Method used for sorting maps by value
	 * @param passedMap Integer map to sort by value
	 * @return Integer map sorted by value
	 */
	protected LinkedHashMap<Integer, Integer> sortHashMapByValuesD(HashMap<Integer, Integer> passedMap) {
	    List<Integer> mapKeys = new ArrayList<Integer>(passedMap.keySet());
	    List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);
	    Collections.reverse(mapValues);
	
	    LinkedHashMap<Integer, Integer> sortedMap = 
	        new LinkedHashMap<Integer, Integer>();
	    
	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Integer val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();
	        
	        while (keyIt.hasNext()) {
	        	Integer key = keyIt.next();
	            Integer comp1 = passedMap.get(key);
	            Integer comp2 = val;
	            
	            if (comp1.equals(comp2)){
	                passedMap.remove(key);
	                mapKeys.remove(key);
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
}
