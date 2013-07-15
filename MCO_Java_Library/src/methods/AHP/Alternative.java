package methods.AHP;

import java.util.LinkedList;

/**
 *
 * @author mateuszkrasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double AHPscore;
    
    
	public Alternative() {
		super();
	}
        
      
	public Alternative(String name) {
		super(name);
	}
        
        public double getScore() {
            return AHPscore;
        }

        protected void setScore(double AHPscore) {
            this.AHPscore = AHPscore;
        }
        
              
}
