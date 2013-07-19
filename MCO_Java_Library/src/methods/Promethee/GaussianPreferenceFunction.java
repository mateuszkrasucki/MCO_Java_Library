package methods.Promethee;


/**
 *
 * @author Mateusz Krasucki
 */
public class GaussianPreferenceFunction implements PreferenceFunction {
        
    private double s;
    
    public GaussianPreferenceFunction()   {
        this.s=0.5;
    }
  
    public GaussianPreferenceFunction(double s)    {
        this.s = s;
    }
    
    public double getS() {
        return this.s;
    }

    public void setS(double s) {
        this.s = s;
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
                        
                if(d<=0)    {
                            return 0;
                }
                else    {
                    return (1-Math.exp(-(Math.pow(d, 2.0)/(2*Math.pow(s,2.0)))));
                }
    }
}
