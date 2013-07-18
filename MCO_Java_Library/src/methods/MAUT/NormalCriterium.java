package methods.MAUT;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public class NormalCriterium extends Criterium {
    
        public enum UtilityFunctionType {
            LINEAR, EXPONENTIAL
        }
        private UtilityFunction utilityFunction;
        
        
        public NormalCriterium()    {
            super();
            this.utilityFunction = new LinearUtilityFunction(0,1);
        }
       	
	public NormalCriterium(String name, Direction direction, double weight, UtilityFunctionType utilityFunctionType, double worst, double best) {
		super(name,direction,weight);
                
                if(utilityFunctionType == UtilityFunctionType.LINEAR)    {
                    this.utilityFunction = new LinearUtilityFunction(worst,best);
                }
                else if(utilityFunctionType == UtilityFunctionType.EXPONENTIAL) {
                    this.utilityFunction = new ExponentialUtilityFunction(worst,best);
                }
        }
        
	public NormalCriterium(String name, Direction direction, double weight, double worst, double best, double c) {
		super(name,direction,weight);
                this.utilityFunction = new ExponentialUtilityFunction(worst,best,c);
        }        
        
	public NormalCriterium(String name, Direction direction, double weight, UtilityFunction utilityFunction) {
		super(name,direction,weight);
                this.utilityFunction = utilityFunction;
        }    
        
        public boolean isGroup()    {
            return false;
        }

        public UtilityFunction getUtilityFunction() {
            return utilityFunction;
        }

        public void setUtilityFunction(UtilityFunction utilityFunction) {
            this.utilityFunction = utilityFunction;
        }
        
   }
