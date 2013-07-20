
package methods.BasicTypes;

/**
 * Abstract class on which all specific to methods Constraint classes are based.
 * @author Mateusz Krasucki
 */

public abstract class Constraint {

	private Criterium criterium;
	/**
     * Enum type describing type of Constraint object.
     */
        public enum ConstrainType { 
		/**
             * UPPER Constraint.
             */
            UPPER,
            /**
             * Bottom constraint.
             */
            BOTTOM
	}
	private Constraint.ConstrainType constraintType;
        
	private double value;
        
	/**
     * Constructor of Constraint class.
     * @param criterium Criterium object which will be affaected by this Constraint object.
     * @param constraintType Type of the constraint, provided as ConstraintType enum type object.
     * @param value Value of the constraint.
     */
    public Constraint(Criterium criterium, Constraint.ConstrainType constraintType, double value) {
		this.criterium = criterium;
                this.constraintType = constraintType;
                this.value = value;
	}

        /**
     * Returns Criterium object affected by this Constraint object. 
     * @return Criterium object affected by this Constraint object. 
     */
    public Criterium getCriterium() {
            return criterium;
        }

        /**
     * Sets Criterium object affected by this Constraint object. 
     * @param criterium Criterium object affected by this Constraint object. 
     */
    public void setCriterium(Criterium criterium) {
            this.criterium = criterium;
        }

        /**
     * Returns type of this constraint. 
     * @return ConstraintType enum type object.
     */
    public ConstrainType getConstraintType() {
            return constraintType;
        }

        /**
     * Sets type of this constaint.
     * @param constraintType ConstraintType enum type object.
     */
    public void setConstraintType(ConstrainType constraintType) {
            this.constraintType = constraintType;
        }

        /**
     * Returns value of this constraint.
     * @return Value of this constraint.
     */
    public double getValue() {
            return value;
        }

    /**
     * Sets value of this constraint. 
     * @param value Value of this constraint.
     */
    public void setValue(double value) {
        this.value = value;
    }
        
        
}
