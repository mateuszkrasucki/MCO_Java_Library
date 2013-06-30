/**
 *
 * @author Gabriela Pastuszka
 */
package methods.Electre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import methods.Electre.Criterium.Direction;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Electre1 {

	private RealMatrix concordanceMatrix_;
	private RealMatrix discordanceMatrix_;
	private RealMatrix normalized_;
	private Double p_;
	private Double q_;
	private LinkedList<Double[]> alternatives_;
	private LinkedList<Criterium> criteria_;
	private Integer[] ranking_;
	private Integer[] rankingPoints_;
	
	public Electre1(Data data, Double p, Double q) {
		alternatives_=data.alternatives_;
		criteria_=data.criteria_;
		concordanceMatrix_ = new Array2DRowRealMatrix(new double[alternatives_.size()][alternatives_.size()]);
		discordanceMatrix_ = new Array2DRowRealMatrix(new double[alternatives_.size()][alternatives_.size()]);
		normalized_ = new Array2DRowRealMatrix(new double[alternatives_.size()][criteria_.size()]);
		ranking_ = new Integer[alternatives_.size()];
		rankingPoints_ = new Integer[alternatives_.size()];
		p_=p;
		q_=q;
	}
	public Integer[] getRanking() {
		return ranking_;
	}

	public Integer[] getRankingPoints_() {
		return rankingPoints_;
	}
	public void calculate() {
		calculate_concordance_matrix();
		normalize();
		calculate_disconcordance_matrix();
		calculate_ranking();
	}
	private double concordance_index(int a, int b) {
		  double w = 0;
	        for (int i=0; i<criteria_.size(); i++)
	        	if (is_alternative_preferred(a, b, i))
	            	w += criteria_.get(i).treshold;

	        return w;
	  }

	public void calculate_concordance_matrix() {
		
		for (int i=0; i< alternatives_.size(); i++)
			 for (int j=0; j<alternatives_.size(); j++) 
				 if (i!=j) 
					 concordanceMatrix_.setEntry(i, j, concordance_index(i, j));
				 else 
					 concordanceMatrix_.setEntry(i, j, 0);
		
	}
	
	public void normalize() {
		for (int i=0; i<alternatives_.size(); i++)
			for (int j=0; j<criteria_.size(); j++)
				normalized_.setEntry(i, j, criteria_.get(j).treshold * alternatives_.get(i)[j] / Math.sqrt(sum_of_sq_for_crit(j)));
		
	}
	
	 private double sum_of_sq_for_crit(int crIndex) {
		 double sum=0;
		 for (int i=0; i<alternatives_.size(); i++) {
			 sum += alternatives_.get(i)[crIndex]*alternatives_.get(i)[crIndex];
		 }
		 return sum;
	 }
	 
	private double discordance_index(int a, int b) {
		Double[] tempList = new Double[criteria_.size()];
		for (int i=0; i<criteria_.size(); i++) {
			if (is_alternative_preferred(a, b, i)) 
				tempList[i] = Math.abs(normalized_.getEntry(a, i) - normalized_.getEntry(b, i));
			else
				tempList[i]=(double) 0;
		}
		return Collections.max(Arrays.asList(tempList));
	  }
	
	private void calculate_disconcordance_matrix() {
		
		for (int i=0; i< alternatives_.size(); i++)
			 for (int j=0; j<alternatives_.size(); j++) 
				 if (i!=j) 
					 discordanceMatrix_.setEntry(i, j, discordance_index(i, j));
				 else 
					 discordanceMatrix_.setEntry(i, j, 0);
		}
	
	private void calculate_ranking() {
		
		int[][] rankIndex = new int[alternatives_.size()][alternatives_.size()];

		for (int i=0; i< alternatives_.size(); i++)
			 for (int j=0; j<alternatives_.size(); j++) 
				 if ((i != j) && (concordanceMatrix_.getEntry(i, j) >= p_) && (discordanceMatrix_.getEntry(i, j) <= q_))
					 rankIndex[i][j]=1;
				 else
					 rankIndex[i][j]=0;
		
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		for (int j=0; j< alternatives_.size(); j++)
			{
			rankMap.put(j+1, 0);
			for (int i=0; i<alternatives_.size(); i++) {
				rankMap.put(j+1, rankMap.get(j+1)+rankIndex[i][j]);
			}
		}

        LinkedHashMap<Integer,Integer> sorted_map = sortHashMapByValuesD(rankMap);
        ranking_=sorted_map.keySet().toArray(new Integer[alternatives_.size()]);
        rankingPoints_ = sorted_map.values().toArray(new Integer[alternatives_.size()]);
		
	}
	
	private Boolean is_alternative_preferred(int a, int b, int criterium) {
		
		if (criteria_.get(criterium).direction == Direction.MAX) 
			return (alternatives_.get(a)[criterium] >= alternatives_.get(b)[criterium]);
		
		else return (alternatives_.get(a)[criterium] <= alternatives_.get(b)[criterium]);
				
	}
	
	public LinkedHashMap<Integer, Integer> sortHashMapByValuesD(HashMap<Integer, Integer> passedMap) {
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
