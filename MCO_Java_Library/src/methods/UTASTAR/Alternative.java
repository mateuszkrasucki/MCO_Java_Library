package methods.UTASTAR;

import java.util.LinkedList;

/**
 * Alternative class specific to UTASTAR method class.
 * Extends methods.BasicTypes.Alternative.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Alternative
 */
public class Alternative extends methods.BasicTypes.Alternative {
    /**
     * Alternatvie score calculated by UTASTAR method.
     */
        private double utastarScore;
        
     /**
      * Alternative preference standing - input of the UTASTAR method. 
      * It informs UTASTAR method what place in reference alternatives ranking has this alternative according to decision maker.
      * It should be set properly for every of the reference alternatives. 
      * It is completely meaningless for normal alternatives that are only scored by UTASTAR method.
      */   
        private int preferenceStanding;
        
     /**
      * Temporary criteria values approximated to marginal utility function arguments in createValueFunctionsOfU() method of UTASTAR method class.
      */   
        protected LinkedList<Double> tmpCriteriaValues;
        
        
     /**
     * Basic constructor of Promethee Alternative class.
     * Preference standing is set to 1.
     */
        public Alternative() {
		super();
                this.utastarScore = 0;
                this.preferenceStanding = 1;
                this.tmpCriteriaValues = new LinkedList<Double>();
	}
       
     /**
     * Promethee Alternative class constructor with alternative name as parameter.
     * Preference standing is set to 1.
     * @param name Alternative name.
     */
	public Alternative(String name) {
		super(name);
                this.utastarScore = 0;
                this.preferenceStanding = 1;
                this.tmpCriteriaValues = new LinkedList<Double>();
	}
     	/**
     * Promethee Alternative class constructor with alternative name and criteria values as parameter.
     * Preference standing is set to 1.
     * @param name Alternative name.
     * @param criteriaValues LinkedList containing criteria values.
     */   
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                this.utastarScore = 0;
                this.preferenceStanding = 1;
                this.tmpCriteriaValues = new LinkedList<Double>();
	}
        
     	/**
     * Promethee Alternative class constructor with alternative name, criteria values and preference standing as parameter. 
     * It should be used only to create reference alternatives as preference standings is meaningful only for this type of alternatives.
     * @param name Alternative name.
     * @param criteriaValues LinkedList containing criteria values.
     * @param preferenceStanding Alternative preference standing (place in reference alternatives ranking according to decision maker).. 
     */
    public Alternative(String name, LinkedList<Double> criteriaValues, int preferenceStanding) {
		super(name, criteriaValues);
                if(preferenceStanding>0)   {
                    this.preferenceStanding = preferenceStanding;
                }
                else    {
                    this.preferenceStanding = 1;
                }
                this.utastarScore = 0;
                this.tmpCriteriaValues = new LinkedList<Double>();
	}
        
        /**
     * Returns alternative score calcualted by UTASTAR (if calculated, if not 0)
     * @return Alternative score calcualted by UTASTAR
     */
    public double getScore() {
            return this.utastarScore;
        }

        /**
     * Sets alternative score to value provided as parameter, protected becaue it is meant to be used only by UTASTAR class.
     * @param score Alternative score.
     */
    protected void setScore(double score) {
            this.utastarScore = score;
        }

        
        
        /**
     * Returns preference standing (place in ranking of reference alternatives)  of this alternative. 
     * Meaningful only for reference alternatives used to create UTASTAR model.
     * @return Alternative preference standing (place in reference alternatives ranking according to decision maker).
     */
    public int getPreferenceStanding() {
            return preferenceStanding;
        }

        /**
     * Sets preference standing (place in ranking of reference alternatives) of this alternative. 
     * Meaningful nly for reference alternatives used to create UTASTAR model. 
     * @param preferenceStanding Alternative preference standing (place in reference alternatives ranking according to decision maker).
     */
    public void setPreferenceStanding(int preferenceStanding) {
            this.preferenceStanding = preferenceStanding;
        }
    
}
