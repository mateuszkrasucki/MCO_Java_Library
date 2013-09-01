package methods.Electre;

import java.util.HashMap;
import methods.Electre.Criterium.Direction;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */
public class ElectreIs extends Electre {


	/**
	* ElectreIs class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
	public ElectreIs(String filename) {
		super(filename);
		}

    /**
	* ElectreIs class constructor
	*/
    public ElectreIs() {
    	super();
    }
    
	/**
	 * Public method calculating all the matrices and filling the ranking lists
	 */
	public void calculate() {
		normalize();
		calculate_concordance_matrix();
		calculate_ranking();
	}

	/**
	 * Method calculating corcondance matrix for every pair of alternatives
	 */
	protected void calculate_concordance_matrix() {
		
		for (int i=0; i< altsCount_; i++)
			 for (int j=0; j<altsCount_; j++) 
				 if (i!=j) 
					 concordanceMatrix_.set(i, j, concordance_index_with_q(i, j) + concordance_index_with_phi(i, j));
				 else 
					 concordanceMatrix_.set(i, j, 0);
	}

	/**
	 * Discordance value for a given pair of alternatives
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return discordance value of alternative a with alternative b
	 */
	private boolean no_veto_condition(int a, int b) {
		
		boolean notRejected = true;
		
		for (int cri =0; cri<criteriaCount_; cri++) {
			
			Double eta = (1 - concordanceMatrix_.get(a, b) - criteria_.get(cri).getWeight()) / (1 - s_ - criteria_.get(cri).getWeight());
        	if (criteria_.get(cri).getDirection()==Direction.MAX) {
    			if (normalized_.get(cri,a) + criteria_.get(cri).getVeto() <
    					normalized_.get(cri,b) + criteria_.get(cri).getQ()*eta)
        		{
    				notRejected = false;
    				break;
        		}
        	}
        	else {
        		if (normalized_.get(cri,a) - criteria_.get(cri).getVeto() >
        		normalized_.get(cri,b) - criteria_.get(cri).getQ()*eta) 
        		{
        			notRejected = false;
        			break;
        		}
           	}
		}
		return notRejected;
	  }

	/**
	 * Checks if the concordance condition with p and q thresholds is fulfilled
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return bool value whether the concordance condition with p and q thresholds is fulfilled
	 */
	protected Boolean concordance_condition_with_p_q_thresholds(int a, int b, int cri) {
		
		if (criteria_.get(cri).getDirection()==Direction.MAX) {
			return (normalized_.get(cri,b) + criteria_.get(cri).getQ() < normalized_.get(cri,a)) &&
					((normalized_.get(cri,a) <= normalized_.get(cri,b) + criteria_.get(cri).getP()));
		}
		else return (normalized_.get(cri,b) - criteria_.get(cri).getQ() > normalized_.get(cri,a)) &&
				(normalized_.get(cri,a) >= normalized_.get(cri,b) - criteria_.get(cri).getP());
	}
	

	protected double concordance_index_with_phi(int a, int b) {
		  double cIndex = 0;
		  
	        for (int i=0; i<criteriaCount_; i++) {

        		if (concordance_condition_with_p_q_thresholds(a,b,i)) {
        			
        			if (criteria_.get(i).getDirection()==Direction.MAX) 
	        		{
	        			Double fi= ( normalized_.get(i,b) + criteria_.get(i).getP() - normalized_.get(i,b) ) / 
	        					( normalized_.get(i,a) + (criteria_.get(i).getP() - criteria_.get(i).getQ()) ); 
	        			cIndex += fi*criteria_.get(i).getWeight();
	        		}
        			else 
	        		{
	        			Double fi= ( normalized_.get(i,a) + criteria_.get(i).getP() - normalized_.get(i,b) ) / 
	        					( normalized_.get(i,a) + (criteria_.get(i).getP() - criteria_.get(i).getQ()) ); 
	        			cIndex += fi*criteria_.get(i).getWeight();
	        		}
	        	}
	        }
	  return cIndex;
	  }

	/**
	 * Method calculating the final ranking of alternatives - different for each Electre method
	 */
	private void calculate_ranking() {
		
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		
		for (int a=0; a<altsCount_; a++) {
			int points =0;
			for (int b=0; b<altsCount_; b++){
				if (a!=b) {
					if ((concordanceMatrix_.get(a, b) > s_) && (no_veto_condition( a,  b)))
					{
						points++;
					}
				}
			}
			rankMap.put(a, points);
		}
        createAlternativesRanking(rankMap);
	}
}
