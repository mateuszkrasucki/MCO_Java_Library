/**
 *
 * @author Gabriela Pastuszka
 */
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


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;


public class Electre1 {

	public enum Direction {
		MIN, MAX
	}
	private RealMatrix concordanceMatrix_;
	private RealMatrix discordanceMatrix_;	
	private RealMatrix normalized_;
	private LinkedList<Double[]> alternatives_;
	private LinkedList<Electre1.Direction> directions_;
	private LinkedList<Double> weights_;
	private Integer[] ranking_;
	private Integer[] rankingPoints_;
	private int altCount_;
	private Double s_;
	private Double veto_;
	
	/**
	* 
	* Cosntructor for Electre1 method
	* @param filename Filename where data can be read from
	* @param s a double value of threshold used for concordance test (should be over 0.5)
	* @param veto a double value of threshold used for disconcordance test
	*/
	public Electre1(String filename, Double s, Double veto) {
		
		directions_ = new LinkedList<Electre1.Direction>();
		weights_ = new LinkedList<Double>();
		alternatives_ = new LinkedList<Double[]>();
		s_=s;
		veto_=veto;
		
		BufferedReader br = null;
		String line = "";
		altCount_ = 0;
		
		try {
			br = new BufferedReader(new FileReader(filename));
			int i=0;
			while ((line = br.readLine()) != null) {
				
				String[] criterium = line.split(";");
				
				if (i==0) {
					altCount_=criterium.length-2;
				}
				// the same amount of criteria in each alternative
				else if (altCount_!=criterium.length-2)
					throw new Exception();
				
				if (criterium[0].compareToIgnoreCase("max")==0)
					directions_.add(Electre1.Direction.MAX);
				else
					directions_.add(Electre1.Direction.MIN);
				
				weights_.add(Double.valueOf(criterium[1]));
				alternatives_.add(new Double[altCount_]);
				
				for (int j=2; j<criterium.length; j++) {
					alternatives_.get(i)[j-2] = Double.valueOf(criterium[j]);
				}
				i++;
			}
			br.close();
		}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		concordanceMatrix_ = new Array2DRowRealMatrix(new double[altCount_][altCount_]);
		discordanceMatrix_ = new Array2DRowRealMatrix(new double[altCount_][altCount_]);
		normalized_ = new Array2DRowRealMatrix(new double[weights_.size()][altCount_]);
		ranking_ = new Integer[altCount_];
		rankingPoints_ = new Integer[altCount_];
		}
	
	
	/**
	 * Ranking list getter
	 * @return Integer array of alternatives starting from the best one
	 */
	public Integer[] getRanking() {
		return ranking_;
	}

	/**
	 * Ranking points list getter
	 * @return Integer array of ranking points of each alternative
	 */
	public Integer[] getRankingPoints_() {
		return rankingPoints_;
	}
	

	/**
	 * Public method calculating all the matrices and filling the ranking lists
	 */
	public void calculate() {
		calculate_concordance_matrix();
		normalize();
		calculate_ranking();
	}

	/**
	 * Helper method
	 * @param a alternative a index
	 * @param b alternative b index
	 * @param criterium Criterium index
	 * @return bool value whether is alternative a better than alternative b
	 */
	private Boolean is_alternative_preferred(int a, int b, int criterium) {
		if (directions_.get(criterium) == Electre1.Direction.MAX) 
			return (alternatives_.get(criterium)[a] >= alternatives_.get(criterium)[b]);
		
		else return (alternatives_.get(criterium)[a] <= alternatives_.get(criterium)[b]);
	}

	/**
	 * Helper method
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return concordance value of alternative a over alternative b
	 */
	private double concordance_index(int a, int b) {
		  double w = 0;
	        for (int i=0; i<weights_.size(); i++)
	        	if (is_alternative_preferred(a, b, i))
	            	w += weights_.get(i);
	        return w;
	  }
	/**
	 * Method calculating corcondance matrix for every pair of alternatives
	 */
	private void calculate_concordance_matrix() {
		
		for (int i=0; i< altCount_; i++)
			 for (int j=0; j<altCount_; j++) 
				 if (i!=j) 
					 concordanceMatrix_.setEntry(i, j, concordance_index(i, j));
				 else 
					 concordanceMatrix_.setEntry(i, j, 0);
		
	}
/**
 * Method preparing the matrix with normalized values of all alternatives, for calculating discordance factor
 */
	public void normalize() {
		
		for (int cri=0; cri<weights_.size(); cri++)
			for (int alt=0; alt<altCount_; alt++)
				normalized_.setEntry(cri, alt, weights_.get(cri) * alternatives_.get(cri)[alt] / Math.sqrt(sum_of_sq_for_crit(cri)));
		
	}
	
	 private double sum_of_sq_for_crit(int cri) {
		 double sum=0;
		 for (int i=0; i<altCount_; i++) {
			 sum += alternatives_.get(cri)[i]*alternatives_.get(cri)[i];
		 }
		 return sum;
	 }

	/**
	 * Discordance value for a given pair of alternatives
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return discordance value of alternative a with alternative b
	 */
	private double discordance_index(int a, int b) {
		Double max = 0.0;
		for (int i=0; i<weights_.size(); i++) {
			if (is_alternative_preferred(b, a, i)) {
				
				
				Double diff = Math.abs(normalized_.getEntry(i, b) - normalized_.getEntry(i, a));
				if (diff > max)
				max = diff;
			}
		}
		return max;
	  }
//
//	private void calculate_discordance_matrix() {
//		
//		for (int i=0; i< altCount_; i++)
//			 for (int j=0; j<altCount_; j++) 
//				 if (i!=j) 
//					 discordanceMatrix_.setEntry(i, j, discordance_index(i, j));
//				 else 
//					 discordanceMatrix_.setEntry(i, j, 0);
//		
//		for (int i=0; i<altCount_; i++){
//			for (int j=0; j<altCount_; j++)
//				System.out.print(discordanceMatrix_.getEntry(i, j)+" ");
//			System.out.println();
//		}
//		
//		}

	
	/**
	 * Method conducting concordance and discordance test for every pair of alternatives, 
	 * putting the results into ranking matrix and sorting it by ranking points amount
	 */
	private void calculate_ranking() {
		System.out.println(altCount_);
		Integer[][] rankingM= new Integer[altCount_][altCount_];
		
		for (int a=0; a<altCount_; a++) {
			for (int b=0; b<altCount_; b++){
				if (a!=b) {
					//concordance test
					if ((concordanceMatrix_.getEntry(a, b) >= s_) && (discordance_index( a,  b) <= veto_))
					{
						rankingM[a][b]=1;
					}
					else 
						rankingM[a][b]=0;
				}
				else 
					rankingM[a][b]=0;
			}
		}
		for (int i=0; i<altCount_; i++){
			for (int j=0; j<altCount_; j++)
				System.out.print(rankingM[i][j]+" ");
			System.out.println();
		}
		
		//preparing map containing order number and ranking points
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		for (int a=0; a< altCount_; a++)
			{
			rankMap.put(a+1, 0);
			for (int i=0; i<altCount_; i++) {
				rankMap.put(a+1, rankMap.get(a+1)+rankingM[a][i]);
			}
		}
		//sorting map by values - ranking points
        LinkedHashMap<Integer,Integer> sorted_map = sortHashMapByValuesD(rankMap);
        ranking_=sorted_map.keySet().toArray(new Integer[altCount_]);
        rankingPoints_ = sorted_map.values().toArray(new Integer[altCount_]);
		
	}
	

	/**
	 * Method used for sorting maps by value
	 * @param passedMap Integer map to sort by value
	 * @return Integer map sorted by value
	 */
	private LinkedHashMap<Integer, Integer> sortHashMapByValuesD(HashMap<Integer, Integer> passedMap) {
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
