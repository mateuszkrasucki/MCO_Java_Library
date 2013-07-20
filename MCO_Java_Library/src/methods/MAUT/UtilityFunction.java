package methods.MAUT;

/**
 * Utility function interface.
 * Utility functions are part of every criterium in MAUT method. They describe how dimensionless utility value of the criterium between 0 and 1 is calculated from real world criterium values.
 * @author Mateusz Krasucki
 * @see LinearUtilityFunction
 * @see ExponentialUtilityFunction
 */
public interface UtilityFunction {
    public double calculate(double value);
}
