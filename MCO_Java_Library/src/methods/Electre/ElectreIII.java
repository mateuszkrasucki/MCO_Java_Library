package methods.Electre;

import java.util.HashMap;
import org.ejml.simple.SimpleMatrix;

public class ElectreIII extends Electre {


	/**
	* ElectreIII class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
	public ElectreIII(String filename) {

		super(filename);
		credibilityMatrix_ = new SimpleMatrix(altsCount_, altsCount_);
	}

    /**
	* ElectreIII class constructor
	*/
    public ElectreIII() {
    	super();
		credibilityMatrix_ = new SimpleMatrix(altsCount_, altsCount_);
    }
    
	/**
	 * Public method calculating all the matrices and filling the ranking lists
	 */
	public void calculate() {
		normalize();
		calculate_concordance_matrix();
		calculate_credibility_matrix();
		calculate_ranking();
	}

	/**
	 * Method calculating concordance matrix for every pair of alternatives
	 */
	public void calculate_concordance_matrix() {
		
		for (int i=0; i< alternatives_.size(); i++)
			 for (int j=0; j<alternatives_.size(); j++) 
				 if (i!=j) 
					 concordanceMatrix_.set(i, j, concordance_index_with_q(i, j));
				 else 
					 concordanceMatrix_.set(i, j, 0);
	}

	/**
	 * Method calculating the final ranking of alternatives - different for each Electre method
	 */
	private void calculate_ranking() {
		
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();

		for (int i=0; i< altsCount_; i++) {
			int points = 0;
			 for (int j=0; j<altsCount_; j++) 
				 if ((i != j) && (credibilityMatrix_.get(i, j) == 1))
					 points++;
			 rankMap.put(i, points);
		}
		createAlternativesRanking(rankMap);
	}
	
}
