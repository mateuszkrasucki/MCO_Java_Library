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
    
        private double threshold; //some threshold used in Electee
        
        public Criterium() {
		super();
                threshold = 0;
	}
        
        public Criterium(String name, Criterium.Direction direction, double weight, double threshold) {
		super(name, direction, weight);
                this.threshold = threshold;
        }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
        
        
}
