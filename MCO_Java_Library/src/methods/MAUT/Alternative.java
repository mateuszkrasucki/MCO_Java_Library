package methods.MAUT;

import java.util.LinkedList;

/**
 * Alternative class specific to MAUT method class.
 * Extends methods.BasicTypes.Alternative.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Alternative
 */
public class Alternative extends methods.BasicTypes.Alternative {
    
    /*
     * Alternative score calcualted by MAUT method.
     */
        private double mautScore;         
    
	/**
     * Basic constructor of MAUT Alternative class.
     */
        public Alternative() {
		super();
                mautScore = 0;
	}
	/**
     * MAUT Alternative class constructor with alternative name as parameter.
     * @param name Alternative name.
     */
	public Alternative(String name) {
		super(name);
                mautScore = 0;
	}
	/**
     * MAUT Alternative class constructor with alternative name and criteria values as parameter.
     * @param name Alternative name.
     * @param criteriaValues LinkedList containing criteria values.
     */
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                mautScore = 0;
	}

        /**
     * Returns alternative score calcualted by MAUT (if calculated, if not 0)
     * @return Alternative score calcualted by MAUT 
     */
    public double getScore() {
            return mautScore;
        }

        /**
     * Sets alternative score to value provided as parameter, protected becaue it is meant to be used only by MAUT class.
     * @param score Alternative score.
     */
    protected void setScore(double score) {
            this.mautScore = score;
        }

        
}
