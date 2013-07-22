package methods.Promethee;

/**
 * Preference function interface.
 * Preference function object is part of every criterium in Promethee method. It provides provides calculation method to determine dimensionless preference value between two values of the criterium.
 * @author Mateusz Krasucki
 * @see LinearPreferenceFunction
 * @see ThresholdPreferenceFunction
 * @see GaussianPreferenceFunction
 * @see LevelPreferenceFunction
 */
public interface PreferenceFunction {
    /**
     * Calculates preference function value for two number provided as parameter in accordance with optimization direction of the criterium which is as well provided as paramater.
     * @param value1 Criterium value on the left side of preference relation.
     * @param value2 Criterium value on the right side of preference relation.
     * @param direction Optimization direction of the criterium.
     * @return Dimensionless one-sided preference value between value1 and value2.
     */
    public double calculatePreference(double value1, double value2, Criterium.Direction direction);
}
