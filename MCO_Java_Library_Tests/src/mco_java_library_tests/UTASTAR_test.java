package mco_java_library_tests;

import methods.UTASTAR.*;

/**
 *
 * @author Mateusz Krasucki, adapted work of Andreadis Pavlos
 */
public class UTASTAR_test {
    
    public static void test()   {
        
        UTASTAR test = new UTASTAR();
        
        Criterium c1 = new Criterium();
        c1.addMufArg(-30);
        c1.addMufArg(-16);
        c1.addMufArg(-2);
        
        Criterium c2 = new Criterium();
        c2.addMufArg(-40);
        c2.addMufArg(-30);
        c2.addMufArg(-20);
        c2.addMufArg(-10);
        
        Criterium c3 = new Criterium();
        c3.addMufArg(0);
        c3.addMufArg(1);
        c3.addMufArg(2);  
        c3.addMufArg(3);   
        
        test.addCriterium(c1);
        test.addCriterium(c2);
        test.addCriterium(c3);
        
        ReferenceAlternative alt1 = new ReferenceAlternative();
        alt1.addCriteriumValue(-3);
        alt1.addCriteriumValue(-10);
        alt1.addCriteriumValue(1);
        alt1.setPreferenceStanding(1);
        
        ReferenceAlternative alt2 = new ReferenceAlternative();
        alt2.addCriteriumValue(-4);
        alt2.addCriteriumValue(-20);
        alt2.addCriteriumValue(2);
        alt2.setPreferenceStanding(2);
        
        ReferenceAlternative alt3 = new ReferenceAlternative();
        alt3.addCriteriumValue(-2);
        alt3.addCriteriumValue(-20);
        alt3.addCriteriumValue(0);
        alt3.setPreferenceStanding(2);
        
        ReferenceAlternative alt4 = new ReferenceAlternative();
        alt4.addCriteriumValue(-6);
        alt4.addCriteriumValue(-40);
        alt4.addCriteriumValue(0);
        alt4.setPreferenceStanding(3);
        
        ReferenceAlternative alt5 = new ReferenceAlternative();
        alt5.addCriteriumValue(-30);
        alt5.addCriteriumValue(-30);
        alt5.addCriteriumValue(3);
        alt5.setPreferenceStanding(4);
        
        test.addReferenceAlternative(alt1);
        test.addReferenceAlternative(alt2);
        test.addReferenceAlternative(alt3);
        test.addReferenceAlternative(alt4);
        test.addReferenceAlternative(alt5);
        
        test.calculate();
        
        String su = "[";
        for(int i=0; i<test.getAverageWeightMatrix().length; i++)   {
            for(int j=0; j<test.getAverageWeightMatrix()[i].length; j++)    {
                su += " " + test.getAverageWeightMatrix()[i][j];
            }
            su += "\n";
        }
        su += "]";
        System.out.println("\n \n \n" + "averageWeightMatrix: \n" + su);
        
        su = "[";
        for(int i=0; i<test.getCriteriaNum(); i++)   {
            for(int j=0; j<test.getCriterium(i).getMufArgs().size(); j++)    {
                su += " " + test.getCriterium(i).getMufArg(j);
            }
            su += "\n";
        }
        su += "]";
        System.out.println("\n \n \n" + "marginalValueFunctions: \n" + su);
        
      
        
        su = "[";
        for(int t = 0; t < test.getReferenceAlternatives().size(); t++)  {
            double score = test.getReferenceAlternative(t).getScore();
            su += " " + score;   
        }
        su += "]";
        System.out.println("\n" + "referenceAlternativesScoring: \n" + su);        
    }   
    
}
