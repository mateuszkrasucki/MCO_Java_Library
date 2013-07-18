package methods.UTASTAR;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class Alternative extends methods.BasicTypes.Alternative {
        private double utastarScore;
        
        private int preferenceStanding;
        protected LinkedList<Double> tmpUTASTARvalues;
        
        public Alternative() {
		super();
                this.utastarScore = 0;
                this.preferenceStanding = 1;
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}
       
	public Alternative(String name) {
		super(name);
                this.utastarScore = 0;
                this.preferenceStanding = 1;
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                this.utastarScore = 0;
                this.preferenceStanding = 1;
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues, int preferenceStanding) {
		super(name, criteriaValues);
                if(preferenceStanding>0)   {
                    this.preferenceStanding = preferenceStanding;
                }
                else    {
                    this.preferenceStanding = 1;
                }
                this.utastarScore = 0;
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}
        
        public double getScore() {
            return this.utastarScore;
        }

        protected void setScore(double utastarScore) {
            this.utastarScore = utastarScore;
        }
        
        public int getPreferenceStanding() {
            return preferenceStanding;
        }

        public void setPreferenceStanding(int preferenceStanding) {
            this.preferenceStanding = preferenceStanding;
        }
    
}
