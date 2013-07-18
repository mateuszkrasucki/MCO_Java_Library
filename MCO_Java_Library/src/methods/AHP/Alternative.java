package methods.AHP;

import java.util.LinkedList;

/**
 *
 * @author mateuszkrasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double ahpScore;
    
    
	public Alternative() {
		super();
	}
        
      
	public Alternative(String name) {
		super(name);
	}
        
        public double getScore() {
            return ahpScore;
        }

        protected void setScore(double ahpScore) {
            this.ahpScore = ahpScore;
        }
        
              
}
