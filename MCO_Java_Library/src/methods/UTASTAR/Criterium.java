package methods.UTASTAR;

import java.util.LinkedList;


/**
 * Criterium class specific to UTASTAR methods class.
 * Extends methods.BasicTypes.Criterium.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Criterium
 */
public class Criterium extends methods.BasicTypes.Criterium{
    /**
     * Marginal utility function arguments. For those values marginal utility function values are determined, for all the other 
     * function values are approximated. Most often those are related with reference alternatives criterium values.
     */
    protected LinkedList<Double> mufArgs;
    /**
     * Marginal utility function values.
     */
    protected LinkedList<Double> marginalUtilityFunction;
    
        
     /**
     * Criterium class basic constructor.
     * Marginal utility function arguments has to be set afterwards.
     */
    public Criterium() {
            super();
            this.mufArgs = new LinkedList<Double>();
            this.marginalUtilityFunction = new LinkedList<Double>();
    }
    
     /**
     * Criterium class constructor with criterium name as a parameter.
     * Marginal utility function arguments has to be set afterwards.
     * @param name Alternative name.
     */
    public Criterium(String name) {
            super(name);
            this.mufArgs = new LinkedList<Double>();
            this.marginalUtilityFunction = new LinkedList<Double>();
    }
    
    /**
     * Criterium class constructor with criterium name and marginal utility function arguments as a parameter.
     * @param name Alternative name.
     * @param mufArgs Marginal utility function arguments. For those values marginal utility function values are determined, for all the other 
     * function values are approximated. Most often those are related with reference alternatives criterium values.
     */
    public Criterium(String name, LinkedList<Double> mufArgs) {
            super(name);
            this.mufArgs = mufArgs;
            this.marginalUtilityFunction = new LinkedList<Double>();
    }

    /**
     * Returns LinkedList containing marginal utility function arguments for this criterium.
     * @return Marginal utility function arguments. For those values marginal utility function values are determined, for all the other 
     * function values are approximated. Most often those are related with reference alternatives criterium values.
     */
    public LinkedList<Double> getMufArgs() {
        return mufArgs;
    }
    
    /**
     * Return i-th marginal utility function argument.
     * @param i Order number of wanted marginal utility function argument.
     * @return Value of i-th marginal utility function argument.
     */
    public double getMufArg(int i)    {
        return this.mufArgs.get(i);
    }

    /**
     * Sets LinkedList containing marginal utility function arguments for this criterium to the one provided as parameter.
     * @param mufArgs Marginal utility function arguments. For those values marginal utility function values are determined, for all the other 
     * function values are approximated. Most often those are related with reference alternatives criterium values.
     */
    public void setMufArgs(LinkedList<Double> mufArgs) {
        this.mufArgs = mufArgs;
    }
    
    /**
     * Adds next marginal utility function argument
     * @param mufArg Marginal utility function arguments. Among other for this value marginal utility function values is determined, for all values not present in arguments list 
     * function values are approximated. Most often those are related with reference alternatives criterium values.
     */
    public void addMufArg(double mufArg) {
        this.mufArgs.add(mufArg);
    }

    /**
     * Return margunal utility function values for marginal utility function arguments. Available only after UTASTAR method is run with this Criterium in the data.
     * @return LinkedList containing marginal utility function values for marginal utility function arguments.
     */
    public LinkedList<Double> getMarginalUtilityFunction() {
        return marginalUtilityFunction;
    }

  

    /**
     * Returns marginal utility function value for i-th marginal utility function argument  
     * @param i Order number of marginal utility function argument for which function value is wanted.
     * @return Marginal utility function value for i-th marginal utility function argument. 
     */
    public double getMarginalUtilityFunctionValue(int i)  {
        if(i<this.marginalUtilityFunction.size()) {
            return this.marginalUtilityFunction.get(i);
        }
        else    {
            return 0;
        }
    }
    
    /**
     * Returns marginal utility function value for number provided as a merhod parameter. It is approximated based on marginal utility function values for marginal utility function arguments.
     * @param arg Number for which marginal utility function value will be calculated (approximated).
     * @return Marginal utility functtion value for the number provided as a method parameter. 
     */
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
