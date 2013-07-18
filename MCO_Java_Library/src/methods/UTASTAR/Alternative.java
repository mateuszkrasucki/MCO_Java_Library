package methods.UTASTAR;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double utastarScore;
        
        public Alternative() {
		super();
                this.utastarScore = 0;
	}
       
	public Alternative(String name) {
		super(name);
                this.utastarScore = 0;
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                this.utastarScore = 0;
	}
        
        public double getScore() {
            return this.utastarScore;
        }

        protected void setScore(double utastarScore) {
            this.utastarScore = utastarScore;
        }
    
}
