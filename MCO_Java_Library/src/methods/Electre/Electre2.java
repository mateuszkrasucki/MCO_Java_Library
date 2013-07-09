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


import methods.Criterium;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Electre2 {

	private RealMatrix concordanceMatrix_;
	private RealMatrix discordanceMatrix_;
	private RealMatrix normalized_;
	private LinkedList<Double[]> alternatives_;
	private LinkedList<Criterium> criteria_;
	private Double[] concordance_dominant_;
	private Double[] discordance_dominant_;
	private Integer[] ranking_;
	private Integer[] rankingPoints_;
	
	public Electre2(Data data) {
		alternatives_=data.alternatives_;
		criteria_=data.criteria_;
		concordanceMatrix_ = new Array2DRowRealMatrix(new double[alternatives_.size()][alternatives_.size()]);
		discordanceMatrix_ = new Array2DRowRealMatrix(new double[alternatives_.size()][alternatives_.size()]);
		concordance_dominant_ = new Double[alternatives_.size()];
		discordance_dominant_ = new Double[alternatives_.size()];
		normalized_ = new Array2DRowRealMatrix(new double[alternatives_.size()][criteria_.size()]);
		ranking_ = new Integer[alternatives_.size()];
		rankingPoints_ = new Integer[alternatives_.size()];
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
		calculate_concordance_dominant();
		calculate_discordance_dominant();
		calculate_ranking();
	}
	
	private Boolean is_alternative_preferred(int a, int b, int criterium) {
		
		if (criteria_.get(criterium).direction == Criterium.Direction.MAX) 
			return (alternatives_.get(a)[criterium] >= alternatives_.get(b)[criterium]);
		
		else return (alternatives_.get(a)[criterium] <= alternatives_.get(b)[criterium]);
	}
	
	private double concordance_index(int a, int b) {
		  double w = 0;
	        for (int i=0; i<criteria_.size(); i++)
	        	if (is_alternative_preferred(a, b, i))
	            	w += criteria_.get(i).threshold;

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
				normalized_.setEntry(i, j, criteria_.get(j).threshold * alternatives_.get(i)[j] / Math.sqrt(sum_of_sq_for_crit(j)));
		
	}
	
	 private double sum_of_sq_for_crit(int crIndex) {
		 double sum=0;
		 for (int i=0; i<alternatives_.size(); i++) {
			 sum += alternatives_.get(i)[crIndex]*alternatives_.get(i)[crIndex];
		 }
		 return sum;
	 }
//	 
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
	
	
	void calculate_discordance_dominant() {

		for (int i=0; i< alternatives_.size(); i++) {
			
			Double p1 = 0.0;
			Double p2 = 0.0;

			for (int j=0; j< alternatives_.size(); j++) 
				if (i!=j)
					p1 += discordanceMatrix_.getEntry(i, j);

			for (int j=0; j< alternatives_.size(); j++) 
				if (i!=j)
					p2 += discordanceMatrix_.getEntry(j, i);
			
			discordance_dominant_[i] = p1-p2;
		}
	}
	
	void calculate_concordance_dominant() {

		for (int i=0; i< alternatives_.size(); i++) {
			
			Double p1 = 0.0;
			Double p2 = 0.0;

			for (int j=0; j< alternatives_.size(); j++) 
				if (i!=j)
					p1 += concordanceMatrix_.getEntry(i, j);

			for (int j=0; j< alternatives_.size(); j++) 
				if (i!=j)
					p2 += concordanceMatrix_.getEntry(j, i);
			
			concordance_dominant_[i] = p1-p2;
		}
	}

	
	private void calculate_ranking() {
		
		Double[] rank = new Double[alternatives_.size()];
		
		for (int i=0; i< alternatives_.size(); i++)
		{
			rank[i]=concordance_dominant_[i] + discordance_dominant_[i] / 2;
		}
	
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		
		for (int i=0; i< alternatives_.size(); i++)
			{
			rankMap.put(i+1, (int) (concordance_dominant_[i] + discordance_dominant_[i] / 2));
			}

        LinkedHashMap<Integer,Integer> sorted_map = sortHashMapByValuesD(rankMap);
        ranking_=sorted_map.keySet().toArray(new Integer[alternatives_.size()]);
        rankingPoints_ = sorted_map.values().toArray(new Integer[alternatives_.size()]);
		
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
