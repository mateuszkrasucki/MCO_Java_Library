package methods.BasicTypes;

/**
 * Abstract class on which all specific to methods Criterium classes are based.
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public abstract class Criterium {

    /** 
     * Criterium name.
     */
	private String name;
        
        /**
         * Criterium weight.
         */
	private double weight;
        	
	/**
     * Basic constructor. 
     * Sets blank name and weight value to 1.
     */
    public Criterium() {
		name="";
        	this.weight = 1; 
	}
        
	/**
     * Constructor with criterium name as parameter.
     * Sets weight value to 1.
     * @param name Criterium name.
     */
    public Criterium(String name) {
		this.name = name;
                this.weight = 1; 
	}
                        
	/**
     * Constructor with criterium name and weight value as parameter.
     * @param name Criterium name.
     * @param weight Criterium weight.
     */
    public Criterium(String name, double weight) {
		this.name = name;
                this.weight = weight;
        }        
                
        /**
     * Return criterium name.
     * @return Criterium name.
     */
    public String getName() {
            return name;
        }

        /**
     * Sets criterium name to the one provided as parameter.
     * @param name Criterium name.
     */
    public void setName(String name) {
            this.name = name;
        }

        /**
     * Returns weight of this criterium. 
     * @return Weight value of this criterium.
     */
    public double getWeight() {
            return weight;
        }

        /**
     * Sets weight value of this criterium to the value provided as a paramater.
     * @param weight New criterium weight value.
     */
    public void setWeight(double weight) {
            this.weight = weight;
        }
        
        
}
