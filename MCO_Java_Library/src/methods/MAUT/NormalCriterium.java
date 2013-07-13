package methods.MAUT;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public class NormalCriterium implements Criterium {

	private String name;
	public enum Direction {
		MIN, MAX
	}
	private NormalCriterium.Direction direction;
	private double weight;
        
        public enum UtilityFunctionType {
            LINEAR, EXPONENTIAL
        }
        private Utility utilityFunction;
        
       	
	public NormalCriterium(String name, Direction direction, double weight, UtilityFunctionType utilityFunctionType, double worst, double best) {
		this.name = name;
		this.direction=this.direction;
		this.weight = weight;
                
                if(utilityFunctionType == UtilityFunctionType.LINEAR)    {
                    this.utilityFunction = new LinearUtility(worst,best);
                }
                else if(utilityFunctionType == UtilityFunctionType.EXPONENTIAL) {
                    this.utilityFunction = new ExponentialUtility(worst,best);
                }
        }
        
	public NormalCriterium(String name, Direction direction, double weight, double worst, double best, double c) {
		this.name = name;
		this.direction=this.direction;
		this.weight = weight;
                this.utilityFunction = new ExponentialUtility(worst,best,c);
        }        
        
	public NormalCriterium(String name, Direction direction, double weight, Utility utilityFunction) {
		this.name = name;
		this.direction=this.direction;
		this.weight = weight;
                this.utilityFunction = utilityFunction;
        }    
        
        public boolean isGroup()    {
            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public Utility getUtilityFunction() {
            return utilityFunction;
        }

        public void setUtilityFunction(Utility utilityFunction) {
            this.utilityFunction = utilityFunction;
        }
        
        
        
   }
