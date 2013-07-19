package methods.Promethee;


/**
 *
 * @author Mateusz Krasucki
 */
public class ThresholdPreferenceFunction implements PreferenceFunction {
        
    private double q;
    
    public ThresholdPreferenceFunction()   {
        this.q=0;
    }
  
    public ThresholdPreferenceFunction(double q)    {
        this.q = q;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }
    
    public double calculatePreference(double value1, double value2, Criterium.Direction direction)   {
               double d = 0;
               // wyznaczenie roznicy miedzy wartosciami kryterium r w alternatywie i i alternatywie j (z uwzglednieniem kierunku preferencji)
               if(direction  == Criterium.Direction.MAX)    {
                            d = value1 - value2;
                }
                else if(direction == Criterium.Direction.MIN)    {
                            d = -1 * (value1 - value2);
                }
                        
                if(d<=this.q)    {
                            return 0;
                }
                else    {
                            return 1; 
                }
    }
}
