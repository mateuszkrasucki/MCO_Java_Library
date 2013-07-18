package methods.Promethee;


/**
 *
 * @author Mateusz Krasucki
 */
public class Criterium extends methods.BasicTypes.Criterium {
	public enum Direction {
		MIN, MAX;
	}
	private Criterium.Direction direction;    
        
        private double q; //indifference threshold
        private double p; //absolute preference threshold
        
        public Criterium() {
		super();
                this.direction = Direction.MAX;
                this.q = 0;
                this.p = 0;
	}
        
        public Criterium(String name) {
		super(name);
                this.direction = Direction.MAX;
                this.q = 0;
                this.p = 0;
	}
        
        public Criterium(String name, double weight) {
		super(name, weight);
                this.direction = Direction.MAX;
                this.q = 0;
                this.p = 0;
	}        
        
        public Criterium(String name, Criterium.Direction direction) {
		super(name);
		this.direction = direction;
                this.q = 0;
                this.p = 0;
        }
        
        public Criterium(String name, Criterium.Direction direction, double weight) {
		super(name, weight);
		this.direction = direction;
                this.q = 0;
                this.p = 0;
        }
                
        public Criterium(String name, Criterium.Direction direction, double weight, double p, double q) {
		super(name, weight);
                this.direction = direction;
                this.p = p;
                this.q = q;
        }

        public double getQ() {
            return q;
        }

        public void setQ(double q) {
            this.q = q;
        }

        public double getP() {
            return p;
        }

        public void setP(double p) {
            this.p = p;
        }
        
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }   
         
}
