package methods.Promethee;


/**
 *
 * @author Mateusz Krasucki
 */
public class Criterium extends methods.BasicTypes.Criterium {
	public enum Direction {
		MIN, MAX;
	}
	private Criterium.Direction direction;    
        
        private PreferenceFunction preferenceFunction;
        
        public Criterium() {
		super();
                this.direction = Direction.MAX;
                preferenceFunction = new LinearPreferenceFunction();
	}
        
        public Criterium(String name) {
		super(name);
                this.direction = Direction.MAX;
                preferenceFunction = new LinearPreferenceFunction();
	}
        
        public Criterium(String name, double weight) {
		super(name, weight);
                this.direction = Direction.MAX;
                preferenceFunction = new LinearPreferenceFunction();
	}        
        
        public Criterium(String name, Criterium.Direction direction) {
		super(name);
		this.direction = direction;
                preferenceFunction = new LinearPreferenceFunction();
        }
        
        public Criterium(String name, Criterium.Direction direction, double weight) {
		super(name, weight);
		this.direction = direction;
                preferenceFunction = new LinearPreferenceFunction();
        }
                
        public Criterium(String name, Criterium.Direction direction, double weight, PreferenceFunction preferenceFunction) {
		super(name, weight);
                this.direction = direction;
                this.preferenceFunction = preferenceFunction;
        }

        
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }   

    public PreferenceFunction getPreferenceFunction() {
        return preferenceFunction;
    }

    public void setPreferenceFunction(PreferenceFunction preferenceFunction) {
        this.preferenceFunction = preferenceFunction;
    }
    
    public double calculatePreference(double value1, double value2) {
        return preferenceFunction.calculatePreference(value1, value2, this.direction);
    }
         
}
