package methods.Electre;

import java.util.HashMap;

import methods.Electre.Criterium.Direction;

import org.ejml.simple.SimpleMatrix;

public class ElectreTri extends Electre {

	/**
	* ElectreTri class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
	public ElectreTri(String filename) {
		
		super(filename);
		credibilityMatrix_ = new SimpleMatrix(altsCount_, altsCount_);
		
	}

    /**
	* ElectreTri class constructor
	*/
	public ElectreTri() {
		
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
	 * Method calculating concordance matrix for every pair of alternatives. Uses weighted concordance index
	 */
	protected void calculate_concordance_matrix() {
		
		for (int i=0; i< altsCount_; i++)
			 for (int j=0; j<altsCount_; j++) 
				 if (i!=j) 
					 concordanceMatrix_.set(i, j, weighted_concordance_index(i, j));
				 else 
					 concordanceMatrix_.set(i, j, 0);
	}
	
	/**
	 * Returns concordance index for given alternatives regarding the q and p threshold of given criterium.
	 * @param a alternative a index
	 * @param b alternative b index
	 * @param cri criterium index
	 * @return concordance index for given alternatives regarding the q and p threshold of given criterium
	 */
	protected double concordance_index(int a, int b, int cri) {

		if (criteria_.get(cri).getDirection() == Direction.MAX) {
		
			if (normalized_.get(cri, a) <= 
					normalized_.get(cri, b) - criteria_.get(cri).getP())
				return 0.0;
			
			if (normalized_.get(cri, b) - criteria_.get(cri).getQ() < 
					normalized_.get(cri, a) )
				return 1.0;
		}
		else {
			if (normalized_.get(cri, a) >= 
					normalized_.get(cri, b) + criteria_.get(cri).getP())
				return 0.0;
			
			if (normalized_.get(cri, b) + criteria_.get(cri).getQ() > 
					normalized_.get(cri, a) )
				return 1.0;
			}
		return (normalized_.get(cri, a) - normalized_.get(cri, b))/
				(criteria_.get(cri).getP()-criteria_.get(cri).getQ());
	}
	
	/**
	 * Returns weighted concordance index for given alternatives.
	 * @param a alternative a index
	 * @param b alternative b index
	 * @return concordance index for given alternatives.
	 */
	protected double weighted_concordance_index(int a, int b) {
		double c_index = 0.0;
		
		for (int i = 0; i<criteriaCount_; i++) {
			
			c_index += criteria_.get(i).getWeight()*concordance_index(a, b);
			
		}
		return c_index;
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
