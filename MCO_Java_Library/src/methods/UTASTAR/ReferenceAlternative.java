package methods.UTASTAR;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class ReferenceAlternative extends Alternative{
        private int preferenceStanding;
        
        protected LinkedList<Double> tmpUTASTARvalues;
    
        
        public ReferenceAlternative() {
		super();
                this.preferenceStanding = 1;
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}
       
	public ReferenceAlternative(String name) {
		super(name);
                this.preferenceStanding = 1;
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}
        
        public ReferenceAlternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                this.tmpUTASTARvalues = new LinkedList<Double>();
                this.preferenceStanding = 1;
	}
        
        public ReferenceAlternative(String name, LinkedList<Double> criteriaValues, int preferenceStanding) {
		super(name, criteriaValues);
                if(preferenceStanding>0)   {
                    this.preferenceStanding = preferenceStanding;
                }
                else    {
                    this.preferenceStanding = 1;
                }
                this.tmpUTASTARvalues = new LinkedList<Double>();
	}


        public int getPreferenceStanding() {
            return preferenceStanding;
        }

        public void setPreferenceStanding(int preferenceStanding) {
            this.preferenceStanding = preferenceStanding;
        }
    
    
}
