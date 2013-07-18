package methods.BasicTypes;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public abstract class Criterium {

	private String name;
	private double weight;
        	
	public Criterium() {
		name="";
        	this.weight = 1; 
	}
        
	public Criterium(String name) {
		this.name = name;
                this.weight = 1; 
	}
                        
	public Criterium(String name, double weight) {
		this.name = name;
                this.weight = weight;
        }        
                
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
        
        
}
