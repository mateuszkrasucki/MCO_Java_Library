package methods.UTASTAR;

import java.util.LinkedList;


/**
 *
 * @author Mateusz Krasucki
 */
public class Criterium extends methods.BasicTypes.Criterium{
    protected LinkedList<Double> mufArgs;
    protected LinkedList<Double> marginalUtilityFunction;
    
        
    public Criterium() {
            super();
            this.mufArgs = new LinkedList<Double>();
            this.marginalUtilityFunction = new LinkedList<Double>();
    }
    
    public Criterium(String name) {
            super(name);
            this.mufArgs = new LinkedList<Double>();
            this.marginalUtilityFunction = new LinkedList<Double>();
    }
    
    public Criterium(String name, LinkedList<Double> mufArgs) {
            super(name);
            this.mufArgs = mufArgs;
            this.marginalUtilityFunction = new LinkedList<Double>();
    }

    public LinkedList<Double> getMufArgs() {
        return mufArgs;
    }
    
    public double getMufArg(int i)    {
        return this.mufArgs.get(i);
    }

    public void setMufArgs(LinkedList<Double> valueLimitBox) {
        this.mufArgs = valueLimitBox;
    }
    
    public void addMufArg(double limit) {
        this.mufArgs.add(limit);
    }

    public LinkedList<Double> getMarginalUtilityFunction() {
        return marginalUtilityFunction;
    }

    public void setMarginalUtilityFunction(LinkedList<Double> marginalUtilityFunction) {
        this.marginalUtilityFunction = marginalUtilityFunction;
    }
    
    public double getMarginalUtilityFunctionValue(int i)  {
        if(i<this.marginalUtilityFunction.size()) {
            return this.marginalUtilityFunction.get(i);
        }
        else    {
            return 0;
        }
    }
    
    public double getMarginalUtilityFunctionValue(double arg)  {
        double value;
        if(this.marginalUtilityFunction.size() > 0) {
               if(arg <= this.mufArgs.get(0)) {
                    value = this.marginalUtilityFunction.get(0);
                    return value;
                }
                else if(arg >= this.mufArgs.get(this.marginalUtilityFunction.size()-1)) {
                    value = this.marginalUtilityFunction.get(this.marginalUtilityFunction.size()-1);
                    return value;
                }
                else    {
                    for(int j=0; j<this.mufArgs.size(); j++)  {
                        if(arg == this.mufArgs.get(j))   {
                            value = this.marginalUtilityFunction.get(j);
                            return value;
                        }
                        else if(arg < this.mufArgs.get(j) )   {
                            double tmp = (arg - this.mufArgs.get(j-1))/(this.mufArgs.get(j) - this.mufArgs.get(j-1));
                            value = this.marginalUtilityFunction.get(j-1) + tmp * (this.marginalUtilityFunction.get(j) - this.marginalUtilityFunction.get(j-1));
                            return value;
                        }
                    }
                }
        }
        return 0;
    }
    
}
