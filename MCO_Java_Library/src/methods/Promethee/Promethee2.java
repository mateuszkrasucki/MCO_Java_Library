package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Promethee II (Promethee2) method class. 
 * Results are based on MPF (net multicriteria preference flow).
 * extends methods.Promethee.Promethee abstract class.
 * @author Mateusz Krasucki
 * @see methods.Promethee.Promethee
 */
public class Promethee2 extends Promethee {
    
        /**
	* Promethee2 class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
        * It should be structured as shown in example csv file in dataFileExamples/promethee.csv.
	*/
    public Promethee2(String filename)  {
        super(filename);
    }
    
    /**
     * Basic Promethee2 class constructor.
     * The Promethee object created by this constructor is empty (no alternatives and criteria set).
     */
    public Promethee2() {
                super();
    }
    
      /**
     * Performs Promethee2 method calculations on data added to Promethee2 object.
     */
    public void calculate()  { 
        normalizeWeights();

        calculateMPD();
        calculateMPF();
        
        ranking = new LinkedList<Alternative>(alternatives);
        Collections.sort(ranking, new Comparator<Alternative>() {
         @Override
         public int compare(Alternative o1, Alternative o2) {
             if(o1.getMpf()<o2.getMpf())    {
                 return 1;
             }
             else if(o1.getMpf()>o2.getMpf())   {
                 return -1;
             }
             return 0;
         }
     });
    }
    

        /**
     * Returns Promethee2 score (MPF) of i-th alternative.
     * @param i Alternative order number.
     * @return I-th alternative Promethee2 score (MPF).
     */
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getMpf();
        }
        return 0;
    }
    

        /**
     * Returns Promethee2 score (MPF) of alternative alt. It has to be one of the alternatives added to the object before running calculate() method.
     * @param alt Alternative object.
     * @return Promethee2 score (MPF) of the alternative object.
     */
    public double getAlternativeValue(Alternative alt)    {
        return alt.getMpf();
    }
}
