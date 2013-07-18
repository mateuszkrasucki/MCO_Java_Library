/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mco_java_library_tests;
import methods.Promethee.Alternative;
import methods.Promethee.Criterium;
import methods.Promethee.Promethee2;


/**
 *
 * @author mateuszkrasucki
 */
public class Promethee2_test {
    public static void test()   {
        Promethee2 promethee = new Promethee2(4);
        Criterium c1 = new Criterium();
        c1.setWeight(1.0/7.0);
        c1.setDirection(Criterium.Direction.MIN);
        c1.setQ(0.05);
        c1.setP(0.2);
        c1.setName("c1");
        
        Criterium c2 = new Criterium();
        c2.setWeight(1.0/7.0);
        c2.setDirection(Criterium.Direction.MIN);
        c2.setQ(0.05);
        c2.setP(0.2);
        c2.setName("c2");
        
        Criterium c3 = new Criterium();
        c3.setWeight(1.0/7.0);
        c3.setDirection(Criterium.Direction.MAX);
        c3.setQ(0.05);
        c3.setP(0.2);
        c3.setName("c3");
        
        Criterium c4 = new Criterium();
        c4.setWeight(40/7.0);
        c4.setDirection(Criterium.Direction.MAX);
        c4.setQ(0.05);
        c4.setP(0.2);
        c4.setName("c4");
        
        promethee.addCriterium(c1);
        promethee.addCriterium(c2);
        promethee.addCriterium(c3);
        promethee.addCriterium(c4);
        
        Alternative car1 = new Alternative("Car1");
        car1.addCriteriumValue(8.75);
        car1.addCriteriumValue(6.2);
        car1.addCriteriumValue(1.0);
        car1.addCriteriumValue(30.0);        
       
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
        
        promethee.calculate();
        
         for(int i=0; i<promethee.getRanking().size(); i++)  {
            System.out.print(promethee.getAlternativeByRank(i).getId());
            System.out.print(". ");
            System.out.print(promethee.getAlternativeByRank(i).getName());
            System.out.print(" ");
            System.out.println(promethee.getAlternativeByRank(i).getMpfPlus());
            System.out.print(" ");
            System.out.print(promethee.getAlternativeByRank(i).getMpfMinus());
            System.out.print(" ");
            System.out.println(promethee.getAlternativeByRank(i).getMpf());
        }  
        
        
    }    
}
