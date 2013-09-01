package methods.Electre;

import java.util.HashMap;

public class ElectreII extends Electre {


	private Double[] concordance_dominant_;
	private Double[] discordance_dominant_;

	/**
	* ElectreII class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
	* It should be structured as shown in example csv file in dataFileExamples/electre.csv.
	*/
	public ElectreII(String filename) {
		super(filename);
		concordance_dominant_ = new Double[altsCount_];
		discordance_dominant_ = new Double[altsCount_];
		}

    /**
	* ElectreII class constructor
	*/
    public ElectreII() {
    	super();
		concordance_dominant_ = new Double[altsCount_];
		discordance_dominant_ = new Double[altsCount_];
    }

	/**
	 * Public method calculating all the matrices and filling the ranking lists
	 */
	public void calculate() {
		normalize();
		calculate_concordance_matrix();
		calculate_discordance_matrix();
		calculate_concordance_dominant();
		calculate_discordance_dominant();
		calculate_ranking();
	}

	/**
	 * Computes array of discordance dominants for each alternative
	 */
	void calculate_discordance_dominant() {

		for (int i=0; i< altsCount_; i++) {
			
			Double p1 = 0.0;
			Double p2 = 0.0;

			for (int j=0; j< altsCount_; j++) 
				if (i!=j)
					p1 += discordanceMatrix_.get(i, j);

			for (int j=0; j< altsCount_; j++) 
				if (i!=j)
					p2 += discordanceMatrix_.get(j, i);
			
			discordance_dominant_[i] = p1-p2;
		}
	}

	/**
	 * Computes array of concordance dominants for each alternative
	 */
	void calculate_concordance_dominant() {

		for (int i=0; i< altsCount_; i++) {
			
			Double p1 = 0.0;
			Double p2 = 0.0;

			for (int j=0; j< altsCount_; j++) 
				if (i!=j)
					p1 += concordanceMatrix_.get(i, j);

			for (int j=0; j< altsCount_; j++) 
				if (i!=j)
					p2 += concordanceMatrix_.get(j, i);
			
			concordance_dominant_[i] = p1-p2;
		}
	}

	/**
	 * Method calculating the final ranking of alternatives - different for each Electre method
	 */
	private void calculate_ranking() {
		
		HashMap<Integer,Integer> rankMap = new HashMap<Integer,Integer>();
		
		for (int i=0; i< altsCount_; i++)
			{
			rankMap.put(i, (int) (concordance_dominant_[i] + discordance_dominant_[i]) / 2);
			}
        createAlternativesRanking(rankMap);		
	}	
}
