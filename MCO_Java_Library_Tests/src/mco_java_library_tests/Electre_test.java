package mco_java_library_tests;


import methods.Electre.ElectreI;
import methods.Electre.ElectreIII;
import methods.Electre.ElectreIv;
import methods.Electre.ElectreIs;
import methods.Electre.ElectreII;
import methods.Electre.ElectreTri;

public class Electre_test {

	
	public static void testFromFile()   {
		

        System.out.println(">>>>> TEST ElectreI <<<<<");
        ElectreI electreI_test = new ElectreI(Electre_test.class.getResource("/datafileExamples/electre.csv").getPath());
        electreI_test.calculate();

        for(int i=0; i<electreI_test.getRanking().size(); i++)  {
        	 System.out.println(electreI_test.getAlternatives().get(i).getName() + ": " + electreI_test.getAlternatives().get(i).getScore());
        }  
        System.out.println();
        System.out.println(">>>>> TEST ElectreIv <<<<<");
        ElectreIv electreIv_test = new ElectreIv(Electre_test.class.getResource("/datafileExamples/electre.csv").getPath());
        electreIv_test.calculate();

        for(int i=0; i<electreIv_test.getRanking().size(); i++)  {
        	 System.out.println(electreIv_test.getAlternatives().get(i).getName() + ": " + electreIv_test.getAlternatives().get(i).getScore());
        }  
        System.out.println();

        System.out.println(">>>>> TEST ElectreIs <<<<<");
        ElectreIs electreIs_test = new ElectreIs(Electre_test.class.getResource("/datafileExamples/electre.csv").getPath());
        electreIs_test.calculate();

        for(int i=0; i<electreIs_test.getRanking().size(); i++)  {
        	 System.out.println(electreIs_test.getAlternatives().get(i).getName() + ": " + electreIs_test.getAlternatives().get(i).getScore());
        }  
        System.out.println();

        System.out.println(">>>>> TEST ElectreII <<<<<");
        ElectreII electreII_test = new ElectreII(Electre_test.class.getResource("/datafileExamples/electre.csv").getPath());
        electreII_test.calculate();

        for(int i=0; i<electreII_test.getRanking().size(); i++)  {
        	 System.out.println(electreII_test.getAlternatives().get(i).getName() + ": " + electreII_test.getAlternatives().get(i).getScore());
        }  
        System.out.println();

        System.out.println(">>>>> TEST ElectreIII <<<<<");
        ElectreIII electreIII_test = new ElectreIII(Electre_test.class.getResource("/datafileExamples/electre.csv").getPath());
        electreIII_test.calculate();

        for(int i=0; i<electreIII_test.getRanking().size(); i++)  {
        	 System.out.println(electreIII_test.getAlternatives().get(i).getName() + ": " + electreIII_test.getAlternatives().get(i).getScore());
        }  
        System.out.println();
        System.out.println(">>>>> TEST ElectreTri <<<<<");
        ElectreTri electreTri_test = new ElectreTri(Electre_test.class.getResource("/datafileExamples/electre.csv").getPath());
        electreTri_test.calculate();

        for(int i=0; i<electreTri_test.getRanking().size(); i++)  {
        	 System.out.println(electreTri_test.getAlternatives().get(i).getName() + ": " + electreTri_test.getAlternatives().get(i).getScore());
        }  
        System.out.println();

        
	}
}

