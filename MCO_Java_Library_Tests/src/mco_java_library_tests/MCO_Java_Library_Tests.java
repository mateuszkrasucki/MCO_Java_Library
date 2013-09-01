package mco_java_library_tests;


/**
 *
 * @author Mateusz Krasucki
 */
public class MCO_Java_Library_Tests {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AHP_test.test();
        System.out.println();
        AHP_test.testFromFile();
        System.out.println();
        MAUT_test.test();
        System.out.println();
        MAUT_test.testFromFile();
        System.out.println();
        Promethee1_test.test();
        System.out.println();
        Promethee1_test.testFromFile();
        System.out.println();
        Promethee2_test.test();
        System.out.println();
        Promethee2_test.testFromFile();
        System.out.println();
        Promethee5_test.testFromFile();
        System.out.println();
        Promethee5_test.testFromFile();
        System.out.println();
        UTASTAR_test.test();
        System.out.println();
        UTASTAR_test.testFromFile();
        System.out.println();
        Electre_test.testFromFile();
    }
}

