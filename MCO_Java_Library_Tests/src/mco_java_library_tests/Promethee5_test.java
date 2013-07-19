package mco_java_library_tests;
import java.util.LinkedList;
import methods.Promethee.*;


/**
 *
 * @author Mateusz Krasucki
 */
public class Promethee5_test {
    
    public static void testFromFile()   {
        Promethee5 promethee = new Promethee5("/Users/mateuszkrasucki/Desktop/datafiles/promethee.csv");
        promethee.calculate();
        
         for(int i=0; i<promethee.getAlternatives().size(); i++)  {
            System.out.print(promethee.getAlternative(i).getId());
            System.out.print(". ");
            System.out.print(promethee.getAlternative(i).getName());
            System.out.print(" ");
            System.out.println(promethee.getAlternative(i).getMpfPlus());
            System.out.print(" ");
            System.out.print(promethee.getAlternative(i).getMpfMinus());
            System.out.print(" ");
            System.out.println(promethee.getAlternative(i).getMpf());
        }       
        
        for(int i=0; i<promethee.getAlternativesBestSet().size(); i++)   {
            System.out.println(promethee.getAlternativesBestSet().get(i).getName() + " " + promethee.getAlternativesBestSet().get(i).getMpf());
        }
        System.out.println(promethee.getBestSetMPF());        
    }
    
    public static void test()   {
        
        Promethee5 promethee = new Promethee5();
        
        Criterium c1 = new Criterium();
        c1.setWeight(1.0/7.0);
        c1.setDirection(Criterium.Direction.MIN);
        c1.setPreferenceFunction(new LinearPreferenceFunction(0.05,0.2));
        c1.setName("c1");
        
        Criterium c2 = new Criterium();
        c2.setWeight(1.0/7.0);
        c2.setDirection(Criterium.Direction.MIN);
        c2.setPreferenceFunction(new LinearPreferenceFunction(0.05,0.2));
        c2.setName("c2");
        
        Criterium c3 = new Criterium();
        c3.setWeight(1.0/7.0);
        c3.setDirection(Criterium.Direction.MAX);
        c3.setPreferenceFunction(new LinearPreferenceFunction(0.05,0.2));
        c3.setName("c3");
        
        Criterium c4 = new Criterium();
        c4.setWeight(40/7.0);
        c4.setDirection(Criterium.Direction.MAX);
        c4.setPreferenceFunction(new LinearPreferenceFunction(0.05,0.2));
        c4.setName("c4");
        promethee.addCriterium(c1);
        promethee.addCriterium(c2);
        promethee.addCriterium(c3);
        promethee.addCriterium(c4);
        
        LinkedList<Double> cv1 = new LinkedList<Double>();
        cv1.add(8.75);
        cv1.add(6.2);
        cv1.add(1.0);
        cv1.add(30.0);        
        Alternative car1 = new Alternative("Car1", cv1);
        
        Alternative car2 = new Alternative();
        car2.setName("Car2");
        car2.addCriteriumValue(13.75);
        car2.addCriteriumValue(7.5);
        car2.addCriteriumValue(1.0);
        car2.addCriteriumValue(50.0);
        
        Alternative car3 = new Alternative();
        car3.setName("Car3");
        car3.addCriteriumValue(25.00);
        car3.addCriteriumValue(8.0);
        car3.addCriteriumValue(3.0);
        car3.addCriteriumValue(80.0);
        
        Alternative car4 = new Alternative();
        car4.setName("Car4");
        car4.addCriteriumValue(62.5);
        car4.addCriteriumValue(20.0);
        car4.addCriteriumValue(2.0);
        car4.addCriteriumValue(120.0);
        
        promethee.addAlternative(car1);
        promethee.addAlternative(car2);
        promethee.addAlternative(car3);
        promethee.addAlternative(car4);    
        
        Constraint constraint1 = new Constraint(c1,Constraint.ConstrainType.UPPER,85.0);
        promethee.addConstraint(constraint1);
        promethee.calculate();
        
         for(int i=0; i<promethee.getAlternatives().size(); i++)  {
            System.out.print(promethee.getAlternative(i).getId());
            System.out.print(". ");
            System.out.print(promethee.getAlternative(i).getName());
            System.out.print(" ");
            System.out.println(promethee.getAlternative(i).getMpfPlus());
            System.out.print(" ");
            System.out.print(promethee.getAlternative(i).getMpfMinus());
            System.out.print(" ");
            System.out.println(promethee.getAlternative(i).getMpf());
        }       
        
        for(int i=0; i<promethee.getAlternativesBestSet().size(); i++)   {
            System.out.println(promethee.getAlternativesBestSet().get(i).getName() + " " + promethee.getAlternativesBestSet().get(i).getMpf());
        }
        System.out.println(promethee.getBestSetMPF());        
    }
    
}
