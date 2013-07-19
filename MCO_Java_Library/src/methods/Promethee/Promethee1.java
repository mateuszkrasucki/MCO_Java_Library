package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Mateusz Krasucki
 */
public class Promethee1 extends Promethee {
    
    public Promethee1(String filename)  {
        super(filename);
    }
    
    public Promethee1() {
                super();
    }
    
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
            return -1;
        }
        else if ((a.getMpfPlus()<b.getMpfPlus()) && (a.getMpfMinus()<b.getMpfMinus())) {
            return -1;           
        }
        return 0;
    }
}
