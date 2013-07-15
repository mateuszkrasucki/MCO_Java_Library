/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.BasicTypes;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public abstract class Alternative {
    	private String name;

	private LinkedList<Double> criteriaValues;
        private int id; 
       
	public Alternative() {
		name="";
                criteriaValues = new LinkedList<Double>();
                id = 0;
	}
        
      
	public Alternative(String name) {
		this.name=name;
                criteriaValues = new LinkedList<Double>();
                id = 0;
	}
        
        public Alternative(String name, LinkedList<Double> criteriaValues) {
		this.name=name;
                this.criteriaValues = criteriaValues;
                id = 0;
	}
        
                
        public void addCriteriumValue(Double value)   {
            this.criteriaValues.add(value);
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
        
        public Double getCriteriumValue(int i)   {
            return criteriaValues.get(i);
        }
        
        public void setCriteriumValue(int i, double value)   {
            criteriaValues.set(i, value);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
}
