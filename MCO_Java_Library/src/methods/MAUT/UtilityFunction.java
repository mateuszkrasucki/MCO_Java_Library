package methods.MAUT;

/**
 * Utility function interface.
 * Utility function is part of every criterium in MAUT method. It describes how dimensionless utility value of the criterium between 0 and 1 is calculated from real world criterium values.
 * @author Mateusz Krasucki
 * @see LinearUtilityFunction
 * @see ExponentialUtilityFunction
 */
public interface UtilityFunction {
    /**
     * Calculates utility function value for number provided as parameter. 
     * @param value Number for which function will be calculated.
     * @return Function value for number provided as parameter.
     */
    public double calculate(double value);
}
