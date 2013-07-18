package methods.MAUT;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double mautScore;
        
        public Alternative() {
		super();
                mautScore = 0;
	}
       
	public Alternative(String name) {
		super(name);
                mautScore = 0;
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                mautScore = 0;
	}

        public double getScore() {
            return mautScore;
        }

        protected void setScore(double score) {
            this.mautScore = score;
        }

        
}
