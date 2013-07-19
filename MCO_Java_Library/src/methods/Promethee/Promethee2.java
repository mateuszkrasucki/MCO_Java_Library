package methods.Promethee;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Mateusz Krasucki
 */
public class Promethee2 extends Promethee {
    
    public Promethee2(String filename)  {
        super(filename);
    }
    
    public Promethee2() {
                super();
    }
    
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
}
