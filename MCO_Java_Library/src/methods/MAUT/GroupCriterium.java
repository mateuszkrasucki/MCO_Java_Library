package methods.MAUT;

import java.util.LinkedList;

/**
 * Criterium class specific for MAUT method class, represents criterium which is group of other, lower level criteria. 
 * Extends abstract class methods.MAUT.Criterium.
 * @author Mateusz Krasucki
 * @see methods.MAUT.Criterium
 */
public class GroupCriterium extends Criterium{
    private LinkedList<Criterium> innerCriteria;
    
    
    /**
     * Basic constructor.
     * Sets every parameter to default.
     */
    public GroupCriterium() {
		super();
                innerCriteria = new LinkedList<Criterium>();
    }      
    
    /**
     * GroupCriterium class constructor with criterium name as parameter.
     * @param name Criterium name.
     */
    public GroupCriterium(String name) {
		super(name);
                innerCriteria = new LinkedList<Criterium>();
    }    
    
    /**
     * GroupCriterium class constructor with criterium name and criterium weight value as parameters.
     * @param name Criterium name.
     * @param weight Criterium weight.
     */
    public GroupCriterium(String name, double weight) {
		super(name,weight);
                innerCriteria = new LinkedList<Criterium>();
    }
    
    /**
     * Group criterium class constructor with criterium name, criterium weight value and inner criteria list as parameters.
     * @param name Criterium name.
     * @param weight Criterium weight.
     * @param innerCriteria LinkedList containing inner critieria of this criterium.
     */
    public GroupCriterium(String name, double weight, LinkedList<Criterium> innerCriteria) {
		super(name,weight);
                this.innerCriteria = innerCriteria;
    }
    
    public boolean isGroup()    {
        return true;
    }

    /**
     * Returns inner criteria list of this Criterium.
     * @return LinkedList containing inner critieria of this criterium.
     */
    public LinkedList<Criterium> getInnerCriteria() {
        return innerCriteria;
    }

    /**
     * Sets inner criteria list of this criterium to the list provided as paramater. 
     * @param innerCriteria New inner criteria LinkedList object.
     */
    public void setInnerCriteria(LinkedList<Criterium> innerCriteria) {
        this.innerCriteria = innerCriteria;
    }
     
    /**
     * Returns i-th inner criterium. 
     * @param i Order number of wanted criterium. 
     * @return Criterium class object of i-th inner criterium.
     */
    public Criterium getInnerCriterium(int i) {
        return innerCriteria.get(i);
    }

    /**
     * Adds criterium to inner criteria list of this criterium.
     * @param innerCriterium Criterium object which will be added to inner criteria list of this criterium.
     */
    public void addInnerCriterium(Criterium innerCriterium) {
        this.innerCriteria.add(innerCriterium);
    }
    
    /**
     * Returns number of inner criteria of this criterium.
     * @return Number of inner criteria of this criterium.
     */
    public int getInnerCriteriaCount()  {
        return this.innerCriteria.size();
    }
    
    
}
