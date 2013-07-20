package methods.MAUT;

/**
 * Criterium class specific for MAUT method class, represents normal criterium unlike GroupCriterium class which is group of lower level criteria. 
 * Extends abstract class methods.MAUT.Criterium.
 * @author Mateusz Krasucki
 * @see methods.MAUT.Criterium
 */

public class NormalCriterium extends Criterium {
    
	/**
     * Enum type describing type of utility function applied to criteriu.
     */
    public enum UtilityFunctionType {
            /**
             * Linear utility function.
             */
            LINEAR,
        /**
         * Exponential utility function.
         */
        EXPONENTIAL
        }
        private UtilityFunction utilityFunction;
        
        
    /**
     * Basic constructor.
     * Sets every parameter to default.
     */
    public NormalCriterium()    {
            super();
            this.utilityFunction = new LinearUtilityFunction(0,1);
        }
       	
    /**
     * NormalCriterium class constructor with criterium name as parameter.
     * @param name Criterium name.
     */
    public NormalCriterium(String name)    {
            super(name);
            this.utilityFunction = new LinearUtilityFunction(0,1);
        }   
        
    /**
     * NormalCriterium class constructor with criterium name and criterium weight value as parameters.
     * @param name Criterium name.
     * @param weight Criterium weight.
     */
    public NormalCriterium(String name, double weight)    {
            super(name, weight);
            this.utilityFunction = new LinearUtilityFunction(0,1);
        }          
        
	/**
     * NormalCriterium class constructor with criterium name, criterium weight, utility function type and best/worst values of that function as parameters.
     * @param name Criterium name.
     * @param weight Criterium weight.
     * @param utilityFunctionType Utility function type (LINEAR or EXPONENTIAL)
     * @param worst Value for which function value will be equal to 0.
     * @param best Value for which function value will be equal 1. 
     */
    public NormalCriterium(String name, double weight, UtilityFunctionType utilityFunctionType, double worst, double best) {
		super(name,weight);
                
                if(utilityFunctionType == UtilityFunctionType.LINEAR)    {
                    this.utilityFunction = new LinearUtilityFunction(worst,best);
                }
                else if(utilityFunctionType == UtilityFunctionType.EXPONENTIAL) {
                    this.utilityFunction = new ExponentialUtilityFunction(worst,best);
                }
        }
        
	/**
     * NormalCriterium class constructor with criterium name, criterium weight, best/worst values of that function and c paramater as parameters (exponential utility function).
     * As c parameter is provided this constructor creates object with exponential utility function.
     * @param name Criterium name.
     * @param weight Criterium weight.
     * @param worst Value for which function value will be equal to 0.
     * @param best Value for which function value will be equal 1. 
     * @param c Parameter used control function shape between worst and best values.
     */
    public NormalCriterium(String name, double weight, double worst, double best, double c) {
		super(name,weight);
                this.utilityFunction = new ExponentialUtilityFunction(worst,best,c);
        }        
        
	/**
     *  NormalCriterium class constructor with criterium name, criterium weight and utility function as paramaters. 
     * @param name Criterium name.
     * @param weight Criterium weight.
     * @param utilityFunction Object of the class which implements UtilityFunction interface.
     */
    public NormalCriterium(String name, double weight, UtilityFunction utilityFunction) {
		super(name,weight);
                this.utilityFunction = utilityFunction;
        }    
        
        public boolean isGroup()    {
            return false;
        }

        /**
     * Return utility function object of this criterium.
     * @return Utility function object of this criterium.
     */
    public UtilityFunction getUtilityFunction() {
            return utilityFunction;
        }

        /**
     * Sets utility function object of this criterium to the one provided as parameter.
     * @param utilityFunction New utility function object of this criterium.
     */
    public void setUtilityFunction(UtilityFunction utilityFunction) {
            this.utilityFunction = utilityFunction;
        }
        
   }
