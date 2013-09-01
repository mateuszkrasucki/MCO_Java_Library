package methods.Electre;

import java.util.HashMap;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */
public class ElectreI extends Electre {
	

	/**
	* ElectreI class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
	public ElectreI(String name) {
		super(name);
	}

	/**
	* 
	* Constructor for Electre1 method 
	*/
    public ElectreI() {
    	super();
    }

	/**
	 * Public method calculating all the matrices and filling the ranking lists
	 */
	public void calculate() {
		normalize();
		calculate_concordance_matrix();
		calculate_discordance_matrix();
		calculate_ranking();
	}

	/**
	 * Method calculating the final ranking of alternatives - different for each Electre method
	 */
	private void calculate_ranking() {

		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		
		for (int a=0; a<altsCount_; a++) {
			int points = 0;
			
			for (int b=0; b<altsCount_; b++){
				if (a!=b) {
					if ((concordanceMatrix_.get(a, b) >= s_) && (discordanceMatrix_.get( a,  b) <= v_))
					{
						points ++;
					}
				}
			}
			rankMap.put(a, points);
		}
		createAlternativesRanking(rankMap);
	}
	
}
