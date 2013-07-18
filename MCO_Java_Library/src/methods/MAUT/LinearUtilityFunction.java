package methods.MAUT;


/**
 *
 * @author Mateusz Krasucki
 */
public class LinearUtilityFunction implements UtilityFunction {
        
    private double a_;
    private double b_;
  
    public LinearUtilityFunction(double worst, double best)  {
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
