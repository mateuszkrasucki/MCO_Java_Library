
package methods.Promethee;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public class Criterium {

	public String name;
	public enum Direction {
		MIN, MAX
	}
	public Criterium.Direction direction;
	public double weight;
        
        public double q; //indifference threshold
        public double p; //absolute preference threshold
	
	public Criterium() {
		name="";
		direction=Criterium.Direction.MAX;
		weight = 0;
                q = 0;
                p = 0;
	}
}
