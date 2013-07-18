package methods.MAUT;

/**
 *
 * @author Mateusz Krasucki
 */
public abstract class Criterium extends methods.BasicTypes.Criterium{
   
    public abstract boolean isGroup();    
   
        
    public Criterium() {
        super();
    }
    
    public Criterium(String name) {
        super(name);
    }  
    
    public Criterium(String name, double weight) {
        super(name,weight);
    }    
      
}

