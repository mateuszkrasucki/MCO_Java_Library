package methods.Promethee;


/**
 * Gaussian preference function class.
 * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=0 -> result = 0, d>0 -> result = (1-Math.exp(-(Math.pow(d, 2.0)/(2*Math.pow(s,2.0)))));
 * Implements PreferenceFunction interface.
 * Preference function object is part of every criterium in Promethee method. It provides provides calculation method to determine dimensionless one-sided preference value between two values of the criterium.
 * @author Mateusz Krasucki
 * @see PreferenceFunction
 */
public class GaussianPreferenceFunction implements PreferenceFunction {
        
    /**
     * Parameter s of gaussian preference function.
     * d = value1-value2 (MAX) OR value2-value1 (MIN), d<=0 -> result = 0, d>0 -> result = (1-Math.exp(-(Math.pow(d, 2.0)/(2*Math.pow(s,2.0)))));
     */
    private double s;
    
    /**
     * GaussianPreferenceFunction basic constructor.
     * S parameter is set to default 0.5.
     */
    public GaussianPreferenceFunction()   {
        this.s=0.5;
    }
  
    /**
     * GaussianPreferenceFunction constructor with s value provided as a constructor parameter.   
     * @param s Gaussian preference function parameter.
     */
    public GaussianPreferenceFunction(double s)    {
        this.s = s;
    }
    
    /**
     * Returns value of s parameter of the GaussianPreferenceFunction object.
     * @return Value of s parameter of the GaussianPreferenceFunction object.
     */
    public double getS() {
        return this.s;
    }

    /**
     * Sets s gaussian preference function parameter value to the one provided as a method parameter. 
     * @param s New value of s parameter of the GaussianPreferenceFunction object.
     */
    public void setS(double s) {
        this.s = s;
    }
    
    /**
     * Calculates gaussian preference function value for two number provided as parameter in accordance with optimization direction of the criterium which is as well provided as paramater.
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
                        
                if(d<=0)    {
                            return 0;
                }
                else    {
                    return (1-Math.exp(-(Math.pow(d, 2.0)/(2*Math.pow(s,2.0)))));
                }
    }
}
