package methods.MAUT;


/**
 *
 * @author Mateusz Krasucki
 */
public class ExponentialUtilityFunction implements UtilityFunction {
        
    private double a_;
    private double b_;
    private double c_;
   
    public ExponentialUtilityFunction(double worst, double best)  {
        c_ = 1;
        a_ = 1/(1-Math.exp(best-worst));
        b_ = -1/(Math.exp(worst) - Math.exp(best));      
    }
    
    public ExponentialUtilityFunction(double worst, double best, double c)  {
        c_ = c;
        a_ = 1/(1-Math.exp(c_*(best-worst)));
        b_ = -1/(Math.exp(c_*worst) - Math.exp(c_*best));      
    }
    
    public double calculate(double value)   {
        double result = a_ + b_ * Math.exp(c_*value);
        if(result < 0)  {
            return 0;
        }
        else if(result > 1) {
            return 1;
        }
        
        return result; 
    }
}
