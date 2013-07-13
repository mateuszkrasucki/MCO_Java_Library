package methods.MAUT;


/**
 *
 * @author Mateusz Krasucki
 */
public class LinearUtility implements Utility {
        
    private double a_;
    private double b_;
  
    public LinearUtility(double worst, double best)  {
        a_ = -worst/(best-worst);
        b_ = 1/(best-worst);     
    }
        
    public double calculate(double value)   {
        double result = a_ + b_ * value;
        if(result < 0)  {
            return 0;
        }
        else if(result > 1) {
            return 1;
        }
        
        return result; 
    }
}
