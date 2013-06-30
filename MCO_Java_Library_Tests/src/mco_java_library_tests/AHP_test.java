package mco_java_library_tests;
import methods.AHP.AHP;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleEVD;


/**
 *
 * @author Mateusz Krasucki
 */
public class AHP_test {

    /**
     * @param args the command line arguments
     */
    public static void test()   {

	double[][] style = {
			{1.0000, 0.2500, 4.0000, 0.1667},
			{4.0000, 1.0000, 4.0000, 0.2500},
			{0.2500, 0.2500, 1.0000, 0.2000},
                        {6.0000, 4.0000, 5.0000, 1.0000}
	};
        
        double[][] reliability = {
			{1.0000, 2.0000, 5.0000, 1.0000},
			{0.5000, 1.0000, 3.0000, 2.0000},
			{0.2000, 0.3333, 1.0000, 0.2500},
                        {1.0000, 0.5000, 4.0000, 1.0000}
	};
        
        double[][] fuelEconomy = {
			{1.0000, 1.2593, 1.4167, 1.2143},
			{0.0000, 1.0000, 1.1250, 0.9643},
			{0.0000, 0.0000, 1.0000, 0.8571},
                        {0.0000, 0.0000, 0.0000, 1.0000}
	};
                
        double[][] criteria = {
			{1.0000, 0.5000, 3.0000},
			{0.0000, 1.0000, 4.0000},
			{0.0000, 0.0000, 1.0000}
	};
        
        AHP ahpTest = new AHP();
        ahpTest.setCriteriaMatrix(criteria, true);
        ahpTest.addCriterium(style, false);
        ahpTest.addCriterium(reliability, false); 
        ahpTest.addCriterium(fuelEconomy, true);
        
        ahpTest.calculate();
        
        System.out.println(ahpTest.getCriteriumWeight(0));
        System.out.println(ahpTest.getCriteriumWeight(1));
        System.out.println(ahpTest.getCriteriumWeight(2));
        
        System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(0, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(0, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(0, 2));
        
                System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(1, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(1, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(1, 2));
        
                System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(2, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(2, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(2, 2));
        
                System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(3, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(3, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(3, 2));
        
        
        System.out.println();
        
        System.out.println(ahpTest.getAlternativeValue(0));
        System.out.println(ahpTest.getAlternativeValue(1));
        System.out.println(ahpTest.getAlternativeValue(2));
        System.out.println(ahpTest.getAlternativeValue(3));
    }
}

