package methods.Promethee;

/**
 *
 * @author Mateusz Krasucki
 */
public interface PreferenceFunction {
    public double calculatePreference(double value1, double value2, Criterium.Direction direction);
}
