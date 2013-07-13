/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.MAUT;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class GroupCriterium implements Criterium {
    private String name;
    private double weight;
    private LinkedList<Criterium> innerCriteria;
            
    
    public GroupCriterium(String name, double weight) {
		this.name = name;
		this.weight = weight;
                innerCriteria = new LinkedList<Criterium>();
        }
    
    public boolean isGroup()    {
        return true;
    }
   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Criterium getInnerCriterium(int i) {
        return innerCriteria.get(i);
    }

    public void addInnerCriterium(Criterium innerCriterium) {
        this.innerCriteria.add(innerCriterium);
    }
    
    public int getInnerCriteriaCount()  {
        return this.innerCriteria.size();
    }
    
    
}
