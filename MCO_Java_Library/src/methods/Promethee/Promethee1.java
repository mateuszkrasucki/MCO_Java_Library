package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Promethee I (Promethee1) method class.
 * Results are based on MPF+ (positive multicriteria preference flow), MPF- (negative multicriteria preference flow). 
 * While using Promethee1 class you should bear in mind that getRanking method returns ranking based on MPF+ value which is not appropriate for original Promethee1 method. Promethee1 method does not provide complete ranking based on MPF+ and MPF-, it provides method to determine preference (outranking) relation between two alternatives (getP1Preference method).
 * extends methods.Promethee.Promethee abstract class.
 * @author Mateusz Krasucki
 * @see methods.Promethee.Promethee
 */
public class Promethee1 extends Promethee {
    
        /**
	* Promethee1 class constructor with data file as an parameter. 
	* @param filename Path to the file from which data can be read. 
        * It should be structured as shown in example csv file in dataFileExamples/promethee.csv.
	*/
    public Promethee1(String filename)  {
        super(filename);
    }
    
    /**
     * Basic Promethee1 class constructor.
     * The Promethee object created by this constructor is empty (no alternatives and criteria set).
     */
    public Promethee1() {
                super();
    }
    
      /**
     * Performs Promethee1 method calculations on data added to Promethee1 object.
     */
    public void calculate()  { 
        normalizeWeights();
        calculateMPD();     
        calculateMPF();
        
        ranking = new LinkedList<Alternative>(alternatives);
        Collections.sort(ranking,new Comparator<Alternative>() {
         @Override
         public int compare(Alternative o1, Alternative o2) {
             if(o1.getMpfPlus()<o2.getMpfPlus())    {
                 return 1;
             }
             else if(o1.getMpfPlus()>o2.getMpfPlus())   {
                 return -1;
             }
             return 0;
         }
     });
    }
    
        /**
     * Returns Promethee1 score (MPF+) of i-th alternative.
     * @param i Alternative order number.
     * @return I-th alternative Promethee1 score (MPF+).
     */
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getMpfPlus();
        }
        return 0;
    }

        /**
     * Returns Promethee1 score (MPF) of alternative alt. It has to be one of the alternatives added to the object before running calculate() method.
     * @param alt Alternative object.
     * @return Promethee1 score (MPF+) of the alternative object.
     */
    public double getAlternativeValue(Alternative alt)    {
        return alt.getMpfPlus();
    }
    
    /**
     * Returns information about preference (outranking) relation betweend two alternatives according to Promethee1 set of rules. Those have to be alternatives which are added to the object before running calculate() method.
     * @param a Alternative object on the left side of the preference (outranking) relation.
     * @param b Alternative object on the right side of the preference (outranking) relation.
     * @return 1 - Alternative a outranks alternative b, 0 - indifference between two alternatives, -1 - Alternative b outranks alternative a, -2 - incombarability
     */
    public int getP1Preference(Alternative a, Alternative b)   {
        if((a.getMpfPlus()>b.getMpfPlus()) && (a.getMpfMinus()<=b.getMpfMinus())) {
            return 1;         
        }
        else if((a.getMpfPlus()==b.getMpfMinus()) && (a.getMpfMinus()<b.getMpfMinus())) {
            return 1;
        }
        else if((a.getMpfPlus()==b.getMpfPlus()) && (a.getMpfMinus() == b.getMpfMinus()))   {
            return 0;
        }
        else if((a.getMpfPlus()>b.getMpfPlus()) && (a.getMpfMinus()>b.getMpfMinus()))   {
            return -2;
        }
        else if ((a.getMpfPlus()<b.getMpfPlus()) && (a.getMpfMinus()<b.getMpfMinus())) {
            return -2;           
        }
        return -1;
    }
}
