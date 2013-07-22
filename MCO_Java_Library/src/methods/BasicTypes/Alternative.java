package methods.BasicTypes;

import java.util.LinkedList;

/**
 * Abstract class on which all specific to methods Alternative classes are based.
 * @author Mateusz Krasucki
 */
public abstract class Alternative {
    /**
     * Alternative name.
     */
    	private String name;

     /**
      * Alternative criteria values.
      */
	private LinkedList<Double> criteriaValues;
        
     /**
      * Alternative id, set by method class object.
      */   
        private int id; 
       
	/**
     * Basic Alternative class constructor. 
     */
    public Alternative() {
		name="";
                criteriaValues = new LinkedList<Double>();
                id = 0;
	}
        
      
	/**
     * Alternative class constructor with alternative name as parameter.
     * @param name Alternative name.
     */
    public Alternative(String name) {
		this.name=name;
                criteriaValues = new LinkedList<Double>();
                id = 0;
	}
        
        /**
     * Alternative class constructor with alternative name and criteria Values as parameters.
     * @param name Alternative name.
     * @param criteriaValues LinkedList containing Double values of each criterion in this alternative. 
     */
    public Alternative(String name, LinkedList<Double> criteriaValues) {
		this.name=name;
                this.criteriaValues = criteriaValues;
                id = 0;
	}
        
                
        /**
     * Adds value to criteria values list.
     * @param value Criterium value.
     */
    public void addCriteriumValue(double value)   {
            this.criteriaValues.add(value);
        }
        

        /**
     * Returns name of this alternative.
     * @return Name of the alternative.
     */
    public String getName() {
            return name;
        }

        /**
     * Sets name of this alternative to the one provided as parameter.
     * @param name New alternative name. 
     */
    public void setName(String name) {
            this.name = name;
        }

        /**
     * Returns LinkedList containing all the criteria values stored in this alternative.
     * @return LinkedList containing all the criteria values stored in this alternative.
     */
    public LinkedList<Double> getCriteriaValues() {
            return criteriaValues;
        }

        /**
     * Sets criteria values for this alternative to the list provided as paramaeter.
     * @param criteriaValues LinkedList containing Double criteriaValues;
     */
    public void setCriteriaValues(LinkedList<Double> criteriaValues) {
            this.criteriaValues = criteriaValues;
        }
        
        /**
     * Returns i-th criterium value for this alternative.
     * @param i Order number of wanted criterium value (starting from 0).
     * @return Double value of i-th criterium for this alternative. 
     */
    public Double getCriteriumValue(int i)   {
            return criteriaValues.get(i);
        }
        
        /**
     * Sets i-th criterium value for this alternative to to the value provided as a paremeter. 
     * @param i  Order number of criterium value to be set (starting from 0);
     * @param value New value of the criterium value;
     */
    public void setCriteriumValue(int i, double value)   {
            criteriaValues.set(i, value);
        }

        /**
     * Returns id of this alternative (which is set by specific MCO methods or manually by setId method).       
     * @return Id of this alternative.
     */
    public int getId() {
            return id;
        }

        /**
     * Sets id of this alternative to number provided as a parameter. 
     * @param id New alternative id.
     */
    public void setId(int id) {
            this.id = id;
        }
        
}
