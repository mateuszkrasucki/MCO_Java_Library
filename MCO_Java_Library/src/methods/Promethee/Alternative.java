package methods.Promethee;

import java.util.LinkedList;

/**
 *
 * @author mateuszkrasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double mpfPlus;
        private double mpfMinus;
        private double mpf;
        
        public Alternative() {
		super();
                mpfPlus = 0;
                mpfMinus = 0;
                mpf = 0;
	}
        
       
	public Alternative(String name) {
		super(name);
                mpfPlus = 0;
                mpfMinus = 0;
                mpf = 0;
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                mpfPlus = 0;
                mpfMinus = 0;
                mpf = 0;
	}

        public double getMpfPlus() {
            return mpfPlus;
        }

        protected void setMpfPlus(double mpfPlus) {
            this.mpfPlus = mpfPlus;
        }

        public double getMpfMinus() {
            return mpfMinus;
        }

        protected void setMpfMinus(double mpfMinus) {
            this.mpfMinus = mpfMinus;
        }

        public double getMpf() {
            return mpf;
        }

        protected void setMpf(double mpf) {
            this.mpf = mpf;
        }
        
        
    
}
