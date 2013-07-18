
package methods.BasicTypes;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public abstract class Criterium {

	private String name;
	public enum Direction {
		MIN, MAX
	}
	private Criterium.Direction direction;
	private double weight;
        	
	public Criterium() {
		name="";
		direction=Criterium.Direction.MAX;
		this.weight = 1; 
	}
        
	public Criterium(String name) {
		this.name = name;
		direction=Criterium.Direction.MAX;
		this.weight = 1; 
	}
        
	public Criterium(String name, Criterium.Direction direction, double weight) {
		this.name = name;
		this.direction = direction;
		this.weight = weight;
        }
        
	public Criterium(String name, Criterium.Direction direction) {
		this.name = name;
		this.direction = direction;
		this.weight = 1;  
        }
        
	public Criterium(String name, double weight) {
		this.name = name;
		this.direction = Criterium.Direction.MAX;
		this.weight = weight;
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
        
        
}
