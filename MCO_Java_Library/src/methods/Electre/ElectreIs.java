/**
 *
 * @author Gabriela Pastuszka
 */
package methods.Electre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import methods.Electre.Criterium.Direction;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class ElectreIs {

	private RealMatrix concordanceMatrix_;
	private RealMatrix discordanceMatrix_;
	private Double s_;
	private LinkedList<Double[]> alternatives_;
	private LinkedList<Criterium> criteria_;
	private Integer[] ranking_;
	private Integer[] rankingPoints_;
	
	public ElectreIs(Data data, Double s) {
		alternatives_=data.alternatives_;
		criteria_=data.criteria_;
		concordanceMatrix_ = new Array2DRowRealMatrix(new double[alternatives_.size()][alternatives_.size()]);
		discordanceMatrix_ = new Array2DRowRealMatrix(new double[alternatives_.size()][alternatives_.size()]);
		ranking_ = new Integer[alternatives_.size()];
		rankingPoints_ = new Integer[alternatives_.size()];
		s_=s;
	}
	public Integer[] getRanking() {
		return ranking_;
	}

	public Integer[] getRankingPoints_() {
		return rankingPoints_;
	}
	
	public void calculate() {
		calculate_concordance_matrix();
		calculate_disconcordance_matrix();
		calculate_ranking();
	}
	private double concordance_index(int a, int b) {
		  double s = 0;
	        for (int i=0; i<criteria_.size(); i++)
	        	if (criteria_.get(i).direction==Direction.MAX) {
	            	if ( alternatives_.get(b)[i] >= alternatives_.get(a)[i])
	            			s += criteria_.get(i).treshold;
	        	}
	        	else {
	            	if ( alternatives_.get(b)[i] <= alternatives_.get(a)[i])
	            			s += criteria_.get(i).treshold;
	        	}
	        return s;
	  }

	public void calculate_concordance_matrix() {
		
		for (int i=0; i< alternatives_.size(); i++)
			 for (int j=0; j<alternatives_.size(); j++) 
				 if (i!=j) 
					 concordanceMatrix_.setEntry(i, j, concordance_index(i, j));
				 else 
					 concordanceMatrix_.setEntry(i, j, 0);

	}
	
	private double discordance_index(int a, int b) {
		double m = 1;
		for (int i=0; i<criteria_.size(); i++) {
			double v = alternatives_.get(b)[i]*0.4;
			if (criteria_.get(i).direction==Direction.MAX) {
            	if ( alternatives_.get(a)[i] >= alternatives_.get(b)[i] + v)
            			m *= 0;
        	}
        	else {
            	if ( alternatives_.get(a)[i] <= alternatives_.get(b)[i] - v)
            			m *= 0;
        	}
		}
		return 1 - m;
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
				 if ((i != j) && (concordanceMatrix_.getEntry(i, j) >= s_) && (discordanceMatrix_.getEntry(i, j) == 0))
					 rankIndex[i][j]=1;
				 else
					 rankIndex[i][j]=0;
//		for (int i=0; i< alternatives_.size(); i++){
//			 for (int j=0; j<alternatives_.size(); j++) 
//				 	System.out.print(rankIndex[i][j]+ " ");
//			 	System.out.println();
//		}
//		
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		for (int i=0; i< alternatives_.size(); i++)
			{
			rankMap.put(i+1, 0);
			for (int j=0; j<alternatives_.size(); j++) {
				rankMap.put(i+1, rankMap.get(i+1)+rankIndex[i][j]);
			}
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
