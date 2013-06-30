
/**
 *
 * @author Gabriela Pastuszka
 */
package methods.Electre;

public class Criterium {

	public String name;
	public enum Direction {
		MIN, MAX
	}
	public Criterium.Direction direction;
	public double treshold;
	
	public Criterium() {
		name="";
		direction=Criterium.Direction.MAX;
		treshold=0;
	}
}
