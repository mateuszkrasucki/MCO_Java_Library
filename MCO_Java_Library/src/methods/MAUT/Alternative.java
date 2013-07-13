package methods.MAUT;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class Alternative {
    	private String name;

	private LinkedList<Double> criteriaValues;
        private int id; 
        
        private double score;
       
	public Alternative(String name) {
		this.name=name;
                criteriaValues = new LinkedList<Double>();
                id = 0;
                score = 0;
	}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LinkedList<Double> getCriteriaValues() {
            return criteriaValues;
        }

        public void setCriteriaValues(LinkedList<Double> criteriaValues) {
            this.criteriaValues = criteriaValues;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        
}
