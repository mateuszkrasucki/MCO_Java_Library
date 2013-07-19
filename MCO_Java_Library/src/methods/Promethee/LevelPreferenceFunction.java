package methods.Promethee;


/**
 *
 * @author Mateusz Krasucki
 */
public class LevelPreferenceFunction implements PreferenceFunction {
        
    private double p;
    private double q;
    
    public LevelPreferenceFunction()   {
        this.q=0;
        this.p=1;
    }
  
    public LevelPreferenceFunction(double p)    {
        this.q = 0;
        this.p= p;
    }
    
    public LevelPreferenceFunction(double q, double p)  {
        this.p = p;
        this.q = q;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
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
                else if(d<=this.p)    {
                            return 0.5; 
                }
                else    {
                            return 1; // jesli roznica jest wieksza od progu scislej preferencji dla kryterium r wartosc relacji preferencji = 1
                }
    }
}
