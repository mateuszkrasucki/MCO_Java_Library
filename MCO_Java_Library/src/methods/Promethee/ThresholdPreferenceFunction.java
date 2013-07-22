package methods.Promethee;


/**
 * Threshold preference function class.
 * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=q -> result = 0, d>q -> result = 1
 * Implements PreferenceFunction interface.
 * Preference function object is part of every criterium in Promethee method. It provides calculation method to determine dimensionless one-sided preference value between two values of the criterium.
 * @author Mateusz Krasucki
 * @see PreferenceFunction
 */
public class ThresholdPreferenceFunction implements PreferenceFunction {
    
    /**
     * Indifference threshold paramater. 
     * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=q -> result = 0, d>q -> result = 1
     */
    private double q;
    
    /**
     * ThresholdPreferenceFunction basic constructor.
     * Indifference threshold (q parameter) is set to 0.
     */
    public ThresholdPreferenceFunction()   {
        this.q=0;
    }
  
    /**
     * LevelPreferenceFunction constructor with indifference threshold (q parameter) provided as a constructor parameter.
     * @param q Indifference threshold (q parameter).
     */
    public ThresholdPreferenceFunction(double q)    {
        this.q = q;
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
     * Calculates threshold preference function value for two number provided as parameter in accordance with optimization direction of the criterium which is as well provided as paramater.
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
                else    {
                            return 1; 
                }
    }
}
