package methods.Promethee;


/**
 * Constraint class specific to Promethee method class.
 * Extends methods.BasicTypes.Constraint
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Constraint
 */
public class Constraint extends methods.BasicTypes.Constraint {
	/**
     * Constructor of Constraint class.
     * @param criterium Criterium object which will be affaected by this Constraint object.
     * @param constraintType Type of the constraint, provided as ConstraintType enum type object.
     * @param value Value of the constraint.
     */
    public Constraint(Criterium criterium, Constraint.ConstrainType constraintType, double value) {
		super(criterium, constraintType, value);
	}    
}
