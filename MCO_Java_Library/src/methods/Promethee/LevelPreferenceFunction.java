package methods.Promethee;

/**
 * Level preference function class.
 * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=q -> result = 0, d>q AND d<=p -> result = 1/2, d>p -> result = 1
 * Implements PreferenceFunction interface.
 * Preference function object is part of every criterium in Promethee method. It provides calculation method to determine dimensionless one-sided preference value between two values of the criterium.
 * @author Mateusz Krasucki
 * @see PreferenceFunction
 */
public class LevelPreferenceFunction implements PreferenceFunction {
    
    /**
     * Indifference threshold paramater. 
     * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=q -> result = 0, d>q AND d<=p -> result = 1/2, d>p -> result = 1
     */
    private double q;
    /**
     * Preference threshold paramater.  
     * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=q -> result = 0, d>q AND d<=p -> result = 1/2, d>p -> result = 1
     */
    private double p;
    
    
    /**
     * LevelPreferenceFunction basic constructor.
     * Preference threshold (p parameter) is set to 1, indifference threshold (q parameter) is set to 0.
     */
    public LevelPreferenceFunction()   {
        this.q=0;
        this.p=1;
    }
  
    /**
     * LevelPreferenceFunction constructor with preference threshold (p parameter) provided as a constructor parameter.
     * Indifference threshold (q parameter) is set to 0.
     * @param p Preference threshold (p parameter).
     */
    public LevelPreferenceFunction(double p)    {
        this.q = 0;
        this.p= p;
    }
    
    /**
     * LevelPreferenceFunction constructor with preference threshold (p parameter) and indifference threshold provided as a constructor parameters.
     * @param q Indifference threshold (q parameter).
     * @param p Preference threshold (p parameter).
     */
    public LevelPreferenceFunction(double q, double p)  {
        this.p = p;
        this.q = q;
    }

    /**
     * Returns the value of preference threshold paramater. 
     * @return Preference threshold (p parameter).
     */
    public double getP() {
        return p;
    }

    /**
     * Sets preference threshold (p parameter) value to the one provided as a method parameter.
     * @param p New preference threshold (p parameter).
     */
    public void setP(double p) {
        this.p = p;
    }

    /**
     * Returns the value of indifference threshold paramater. 
     * @return Indifference threshold (q parameter).
     */
    public double getQ() {
        return q;
    }

    /**
     * Sets indifference threshold (q parameter) value to the one provided as a method parameter.
     * @param q New indifference threshold (q parameter).
     */
    public void setQ(double q) {
        this.q = q;
    }
    
    /**
     * Calculates level preference function value for two number provided as parameter in accordance with optimization direction of the criterium which is as well provided as paramater.
     * @param value1 Criterium value on the left side of preference relation.
     * @param value2 Criterium value on the right side of preference relation.
     * @param direction Optimization direction of the criterium.
     * @return Dimensionless one-sided preference value between value1 and value2.
     */
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
