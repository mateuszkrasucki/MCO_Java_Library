package methods.MAUT;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class GroupCriterium extends Criterium{
    private LinkedList<Criterium> innerCriteria;
    
    
    public GroupCriterium() {
		super();
                innerCriteria = new LinkedList<Criterium>();
    }      
    
    public GroupCriterium(String name, double weight) {
		super(name,weight);
                innerCriteria = new LinkedList<Criterium>();
    }
    
    public GroupCriterium(String name, double weight, LinkedList<Criterium> innerCriteria) {
		super(name,weight);
                this.innerCriteria = innerCriteria;
    }
    
    public boolean isGroup()    {
        return true;
    }

    public LinkedList<Criterium> getInnerCriteria() {
        return innerCriteria;
    }

    public void setInnerCriteria(LinkedList<Criterium> innerCriteria) {
        this.innerCriteria = innerCriteria;
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
