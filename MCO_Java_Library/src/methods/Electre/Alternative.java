package methods.Electre;

import java.util.LinkedList;


public class Alternative extends methods.BasicTypes.Alternative {

	private int rankingPoints_;
	
	public Alternative() {
		super();
		rankingPoints_ = 0;
	}

    public Alternative(String name) {
    	super(name);
		rankingPoints_ = 0;
    }

    /**
	 * Alternative class constructor with alternative name and criteria Values as parameters.
	 * @param name Alternative name.
	 * @param criteriaValues LinkedList containing Double values of each criterion in this alternative. 
	 */
    public Alternative(String name, LinkedList<Double> criteriaValues) {
    	super(name, criteriaValues);
}

	public int getScore() {
		return rankingPoints_;
	}

	public void setRankingPoints_(int rankingPoints) {
		this.rankingPoints_ = rankingPoints;
	}
    
            
    
}
