package methods.MAUT;

/**
 * Abstract Criterium class specific to MAUT method class.
 * Extends methods.BasicTypes.Criterium. It is extended by GroupCriterium class and NormalCriterium class.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Criterium
 */
public abstract class Criterium extends methods.BasicTypes.Criterium{
   
    /**
     * Provides information if this criterium is GroupCriterium or NormalCriterium. 
     * @return false - NormalCriterium, true - GroupCriterium.
     */
    public abstract boolean isGroup();    
   
        
    /**
     * Basic constructor.
     * Sets every parameter to default.
     */
    public Criterium() {
        super();
    }
    
    /**
     * Criterium class constructor with criterium name as parameter.
     * @param name Criterium name.
     */
    public Criterium(String name) {
        super(name);
    }  
    
    /**
     * Criterium class constructor with criterium name and criterium weight value as parameters.
     * @param name Criterium name.
     * @param weight Criterium weight.
     */
    public Criterium(String name, double weight) {
        super(name,weight);
    }    
      
}

