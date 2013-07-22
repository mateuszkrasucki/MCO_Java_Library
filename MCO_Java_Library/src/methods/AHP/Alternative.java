package methods.AHP;


/**
 * Alternative class specific to AHP method class.
 * Extends methods.BasicTypes.Alternative.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Alternative
 */
public class Alternative extends methods.BasicTypes.Alternative {
    
    /*
     * Alternative score calculated by AHP method.
     */
        private double ahpScore;
    
    
	/**
     * Basic constructor of AHP Alternative class.
     */
    public Alternative() {
		super();
                this.ahpScore = 0;
	}
        
      
	/**
     * AHP Alternative class constructor with alternative name as parameter.
     * @param name Alternative name.
     */
    public Alternative(String name) {
		super(name);
                this.ahpScore = 0;
	}
        
        /**
     * Returns score calculated by AHP method (if calculated).
     * @return Alternative score calculated by AHP method.
     */
    public double getScore() {
            return ahpScore;
        }

        /**
     * Sets score to given value, protected because it is meant to be used by AHP class only.
     * @param ahpScore Value to be set as AHP score of alternative.
     */
    protected void setScore(double ahpScore) {
            this.ahpScore = ahpScore;
        }
        
              
}
