package methods.Promethee;


/**
 * Criterium class specific to Promethee methods classes.
 * Extends methods.BasicTypes.Criterium.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Criterium
 */
public class Criterium extends methods.BasicTypes.Criterium {
	/**
     * Enum type describing criterium optimization direction (if criterium value should be minimized or maximalized).
     */
    public enum Direction {
		/**
         * Criterium value should be minimized.
         */
        MIN,
            /**
             * Criterium value should be maximized. 
             */
            MAX;
	}
	private Criterium.Direction direction;    
        
        private PreferenceFunction preferenceFunction;
        
    /**
     * Basic constructor.
     * Sets every parameter to default - weight equals 1, criterium will be maximized, linear preference function will be applied.
     */
    public Criterium() {
		super();
                this.direction = Direction.MAX;
                preferenceFunction = new LinearPreferenceFunction();
	}
        
   /**
     * Criterium class constructor with criterium name as a parameter.
     * Sets every other  parameter to default - weight equals 1, criterium will be maximized, linear preference function will be applied.
     * @param name Criterium name.
     */
    public Criterium(String name) {
		super(name);
                this.direction = Direction.MAX;
                preferenceFunction = new LinearPreferenceFunction();
	}
        
   /**
     * Criterium class constructor with criterium name and criterium weight value as parameters.
     * Sets every other parameter to default - criterium will be maximized, linear preference function will be applied.
     * @param name Criterium name.
     * @param weight Criterium weight.
     */
    public Criterium(String name, double weight) {
		super(name, weight);
                this.direction = Direction.MAX;
                preferenceFunction = new LinearPreferenceFunction();
	}        
        
   /**
     * Criterium class constructor with criterium name and criteirum optimization direction.
     * Sets every other parameter to default - weight equals 1, linear preference function will be applied.
     * @param name Criterium name.
     * @param direction Criterium optimization direction.
     */
    public Criterium(String name, Criterium.Direction direction) {
		super(name);
		this.direction = direction;
                preferenceFunction = new LinearPreferenceFunction();
        }
        
   /**
     * Criterium class constructor with criterium name, criterium optimization direction and criterium weight value as parameters.
     * Default, linear preference function will be applied.
     * @param name Criterium name.
     * @param direction Criterium optimization direction.
     * @param weight Criterium weight.
     */
    public Criterium(String name, Criterium.Direction direction, double weight) {
		super(name, weight);
		this.direction = direction;
                preferenceFunction = new LinearPreferenceFunction();
        }
                
   /**
     * Criterium class constructor with criterium name, criterium optimization direction, criterium weight value and preference function as parameters.
     * @param name Criterium name.
     * @param direction Criterium optimization direction.
     * @param weight Criterium weight.
     * @param preferenceFunction Object of class implementing PreferenceFunction interface, allow to calculate dimensionless preference value between two criterium values.
     */
    public Criterium(String name, Criterium.Direction direction, double weight, PreferenceFunction preferenceFunction) {
		super(name, weight);
                this.direction = direction;
                this.preferenceFunction = preferenceFunction;
        }

        
    /**
     * Returns optimization direction of the criterium. 
     * @return Optimization direction of the criterium.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets optimization direction of the criterium to the one provided as parameter.
     * @param direction New optimization direction of the criterium.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }   

        /**
     * Returns preference function object of this criterium.
     * @return Preference function object of this criterium.
     */
    public PreferenceFunction getPreferenceFunction() {
        return preferenceFunction;
    }

        /**
     * Sets preference function object of this criterium to the one provided as parameter.
     * @param preferenceFunction New preference function of the criterium.
     */
    public void setPreferenceFunction(PreferenceFunction preferenceFunction) {
        this.preferenceFunction = preferenceFunction;
    }
    
    /**
     *
     * @param value1
     * @param value2
     * @return
     */
    public double calculatePreference(double value1, double value2) {
        return preferenceFunction.calculatePreference(value1, value2, this.direction);
    }
         
}
