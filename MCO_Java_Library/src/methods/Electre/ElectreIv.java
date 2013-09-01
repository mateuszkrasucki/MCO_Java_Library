package methods.Electre;

import java.util.HashMap;

import methods.Electre.Criterium.Direction;


/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */
public class ElectreIv extends Electre {


	/**
	* ElectreIv class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
	public ElectreIv(String filename) {
		super(filename);
		}

    /**
	* ElectreIv class constructor
	*/
    public ElectreIv() {
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
	 * Helper method
	 * @param a alternative a index
	 * @param b alternative b index
	 * @param criterium Criterium index
	 * @return bool value whether is alternative a better than alternative b
	 */
	private Boolean no_veto_condition(int a, int b, int criterium) {
		
		if (criteria_.get(criterium).getDirection() == Direction.MAX) 
			return (normalized_.get(criterium, a) + criteria_.get(criterium).getVeto()>= 
					normalized_.get(criterium, b)  );
		
		else return (normalized_.get(criterium, a)  - criteria_.get(criterium).getVeto() <= 
				normalized_.get(criterium, b)  );
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
					if (concordanceMatrix_.get(a, b) >= s_) {
						boolean rejected = false;
						for (int i=0; i<criteriaCount_; i++) 
							if (!no_veto_condition(a, b, i)) {
								rejected = true;
								break;
							}
						if (!rejected)
							points++;
					}
				}
			}
			rankMap.put(a, points);
		}
		createAlternativesRanking(rankMap);
	}
	
}
