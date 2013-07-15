
package methods.BasicTypes;

/**
 *
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */

public abstract class Constraint {

	private Criterium criterium;
	public enum ConstrainType { 
		UPPER, LOWER
	}
	private Constraint.ConstrainType constraintType;
        
	private double value;
        
	public Constraint(Criterium criterium, Constraint.ConstrainType constraintType, double value) {
		this.criterium = criterium;
                this.constraintType = constraintType;
                this.value = value;
	}

        public Criterium getCriterium() {
            return criterium;
        }

        public void setCriterium(Criterium criterium) {
            this.criterium = criterium;
        }

        public ConstrainType getConstraintType() {
            return constraintType;
        }

        public void setConstraintType(ConstrainType constraintType) {
            this.constraintType = constraintType;
        }

        public double getValue() {
            return value;
        }

    public void setValue(double value) {
        this.value = value;
    }
        
        
}
