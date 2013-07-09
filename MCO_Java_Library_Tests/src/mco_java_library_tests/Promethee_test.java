/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mco_java_library_tests;
import methods.Alternative;
import methods.Criterium;
import methods.Promethee.Promethee;


/**
 *
 * @author mateuszkrasucki
 */
public class Promethee_test {
    public static void test()   {
        Promethee promethee = new Promethee(4);
        
        Criterium c1 = new Criterium();
        c1.weight = 1.0/7.0;
        c1.direction = Criterium.Direction.MIN;
        c1.q = 0.05;
        c1.p = 0.2;
        c1.name = "c1";
        
        Criterium c2 = new Criterium();
        c2.weight = 1.0/7.0;
        c2.direction = Criterium.Direction.MIN;
        c2.q = 0.05;
        c2.p = 0.2;
        c2.name = "c2";
        
        Criterium c3 = new Criterium();
        c3.weight = 1.0/7.0;
        c3.direction = Criterium.Direction.MAX;
        c3.q = 0.05;
        c3.p = 0.2;
        c3.name = "c1";
        
        Criterium c4 = new Criterium();
        c4.weight = 4.0/7.0;
        c4.direction = Criterium.Direction.MAX;
        c4.q = 0.05;
        c4.p = 0.2;
        c4.name = "c4";
        
        promethee.addCriterium(c1);
        promethee.addCriterium(c2);
        promethee.addCriterium(c3);
        promethee.addCriterium(c4);
        
        Alternative car1 = new Alternative();
        car1.name = "Car1";
        car1.criteriaValues.add(8.75);
        car1.criteriaValues.add(6.2);
        car1.criteriaValues.add(1.0);
        car1.criteriaValues.add(30.0);
        
        Alternative car2 = new Alternative();
        car2.name = "Car2";
        car2.criteriaValues.add(13.75);
        car2.criteriaValues.add(7.5);
        car2.criteriaValues.add(1.0);
        car2.criteriaValues.add(50.0);
        
        Alternative car3 = new Alternative();
        car3.name = "Car3";
        car3.criteriaValues.add(25.00);
        car3.criteriaValues.add(8.0);
        car3.criteriaValues.add(3.0);
        car3.criteriaValues.add(80.0);
        
        Alternative car4 = new Alternative();
        car4.name = "Car4";
        car4.criteriaValues.add(62.5);
        car4.criteriaValues.add(20.0);
        car4.criteriaValues.add(2.0);
        car4.criteriaValues.add(120.0);
        
        promethee.addAlternative(car1);
        promethee.addAlternative(car2);
        promethee.addAlternative(car3);
        promethee.addAlternative(car4);
        
        promethee.calculatePromethee2();
        
        for(int i=0; i<promethee.alternatives.size(); i++)  {
            System.out.print(promethee.alternatives.get(i).id);
            System.out.print(". ");
            System.out.print(promethee.alternatives.get(i).name);
            System.out.print(" ");
            System.out.println(promethee.alternatives.get(i).mpf_plus);
            System.out.print(" ");
            System.out.print(promethee.alternatives.get(i).mpf_minus);
            System.out.print(" ");
            System.out.println(promethee.alternatives.get(i).mpf);
        }
        
        
    }    
}
