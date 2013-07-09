/**
 *
 * @author Gabriela Pastuszka
 */
package methods.Electre;

import methods.Criterium;
import java.util.LinkedList;


public class Data {
	
	public LinkedList<Double[]> alternatives_;
	public LinkedList<Criterium> criteria_;
	
	public Data() {
		alternatives_ = new LinkedList<Double[]>();
		criteria_ = new LinkedList<Criterium>();
	}
	
}
