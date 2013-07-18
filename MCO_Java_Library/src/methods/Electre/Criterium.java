/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.Electre;

/**
 *
 * @author Mateusz Krasucki
 */
public class Criterium extends methods.BasicTypes.Criterium {
    
	public enum Direction {
		MIN, MAX;
	}
	private Criterium.Direction direction;
        
        private double threshold; //some threshold used in Electee
        
        public Criterium() {
		super();
                this.direction = Direction.MAX;
                this.threshold = 0;
	}
        
        public Criterium(String name) {
		super(name);
                this.direction = Direction.MAX;
                this.threshold = 0;
	}
        
        public Criterium(String name, double weight) {
		super(name, weight);
                this.direction = Direction.MAX;
                this.threshold = 0;
	}        
        
        public Criterium(String name, Criterium.Direction direction) {
		super(name);
		this.direction = direction;
                this.threshold = 0;
        }
        
	public Criterium(String name, Criterium.Direction direction, double weight) {
		super(name);
		this.direction = direction;
		this.threshold = 0;
        }

        public Criterium(String name, Criterium.Direction direction, double weight, double threshold) {
		super(name, weight);
                this.direction = direction;
                this.threshold = threshold;
        }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }        
        
}
