package methods.Promethee;


/**
 *
 * @author Mateusz Krasucki
 */
public class Criterium extends methods.BasicTypes.Criterium {
        private double q; //indifference threshold
        private double p; //absolute preference threshold
        
        public Criterium() {
		super();
                q = 0;
                p = 0;
	}
        
        public Criterium(String name, Criterium.Direction direction, double weight, double p, double q) {
		super(name, direction, weight);
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
        
        
         
}
