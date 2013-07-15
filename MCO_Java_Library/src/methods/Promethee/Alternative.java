package methods.Promethee;

import java.util.LinkedList;

/**
 *
 * @author mateuszkrasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double mpf_plus;
        private double mpf_minus;
        private double mpf;
        
        public Alternative() {
		super();
                mpf_plus = 0;
                mpf_minus = 0;
                mpf = 0;
	}
        
       
	public Alternative(String name) {
		super(name);
                mpf_plus = 0;
                mpf_minus = 0;
                mpf = 0;
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                mpf_plus = 0;
                mpf_minus = 0;
                mpf = 0;
	}

        public double getMpf_plus() {
            return mpf_plus;
        }

        protected void setMpf_plus(double mpf_plus) {
            this.mpf_plus = mpf_plus;
        }

        public double getMpf_minus() {
            return mpf_minus;
        }

        protected void setMpf_minus(double mpf_minus) {
            this.mpf_minus = mpf_minus;
        }

        public double getMpf() {
            return mpf;
        }

        protected void setMpf(double mpf) {
            this.mpf = mpf;
        }
        
        
    
}
