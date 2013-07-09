
package methods;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public class Constraint {

	public Criterium criterium;
	public enum ConstrainType { 
		UPPER, LOWER
	}
	public Constraint.ConstrainType constraintType;
        
	public double value;
	
	public Constraint(Criterium crit, Constraint.ConstrainType type, double val) {
		criterium = crit;
                constraintType = type;
                value = val;
	}
}
