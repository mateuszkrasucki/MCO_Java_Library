package methods.AHP;


/**
 * Criterium class specific to AHP method class.
 * Extends methods.BasicTypes.Criterium.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Criterium
 */
public class Criterium extends methods.BasicTypes.Criterium {
     /**
     * Basic constructor of AHP Criterium class.
     */
	public Criterium() {
		super();
	}
	/**
     * AHP Criterium class constructor with criterium name as parameter.
     * @param name Criterium name.
     */
	public Criterium(String name) {
		super(name);
	}


}
