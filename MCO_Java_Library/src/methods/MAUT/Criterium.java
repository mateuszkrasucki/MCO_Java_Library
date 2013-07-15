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
    
    public Criterium(String name, double weight) {
        super(name,weight);
    }    
    
    public Criterium(String name, Criterium.Direction direction, double weight) {
	super(name, direction, weight);
    }
    
}

