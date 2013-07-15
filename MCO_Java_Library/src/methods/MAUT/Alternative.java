package methods.MAUT;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double MAUTscore;
        
        public Alternative() {
		super();
                MAUTscore = 0;
	}
       
	public Alternative(String name) {
		super(name);
                MAUTscore = 0;
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                MAUTscore = 0;
	}

        public double getScore() {
            return MAUTscore;
        }

        protected void setScore(double score) {
            this.MAUTscore = score;
        }

        
}
