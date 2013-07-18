
package mco_java_library_tests;

import java.util.LinkedList;
import methods.MAUT.*;

/**
 *
 * @author Mateusz Krasucki
 */
public class MAUT_test {
    
    private static void showGroupCriterium(GroupCriterium groupCriterium)  {
        for(int i=0;i<groupCriterium.getInnerCriteriaCount(); i++)    {
            System.out.println(groupCriterium.getInnerCriterium(i).getName() + " " + groupCriterium.getInnerCriterium(i).getWeight());
            if(groupCriterium.getInnerCriterium(i).isGroup())  {
                GroupCriterium tmp = (GroupCriterium)groupCriterium.getInnerCriterium(i);
                showGroupCriterium(tmp);
            }
        }        
    }
    
    private static void showCriteria(MAUT maut)  {
        for(int i=0;i<maut.getCriteria().size(); i++)    {
            System.out.println(maut.getCriterium(i).getName() + " " + maut.getCriterium(i).getWeight());
            if(maut.getCriterium(i).isGroup())  {
                GroupCriterium groupCriterium = (GroupCriterium)maut.getCriterium(i);
                showGroupCriterium(groupCriterium);
            }
        }
    }
    
    public static void test()  {
        MAUT maut_test = new MAUT();
        
        GroupCriterium c1 = new GroupCriterium("c1", 0.5);
        
        GroupCriterium c11 = new GroupCriterium("c11", 0.7);
        NormalCriterium c111 = new NormalCriterium("c111", 0.6, NormalCriterium.UtilityFunctionType.LINEAR, 1, 10);
        NormalCriterium c112 = new NormalCriterium("c112", 0.8, NormalCriterium.UtilityFunctionType.LINEAR, 1, 10);
        c11.addInnerCriterium(c111);
        c11.addInnerCriterium(c112);
        
        c1.addInnerCriterium(c11);
        
        NormalCriterium c12 = new NormalCriterium("c12", 0.3, NormalCriterium.UtilityFunctionType.LINEAR, 2, 4);
        
        c1.addInnerCriterium(c12);
        
        NormalCriterium c2 = new NormalCriterium("c2", 0.5, NormalCriterium.UtilityFunctionType.LINEAR, 1, 9);

        maut_test.addCriterium(c1);
        maut_test.addCriterium(c2);
        
        Alternative alt1 = new Alternative("alt1");
        LinkedList<Double> alt1_val = new LinkedList<Double>();
        alt1_val.add(8.0);
        alt1_val.add(9.0);
        alt1_val.add(3.0);
        alt1_val.add(8.0);
        alt1.setCriteriaValues(alt1_val);
        maut_test.addAlternative(alt1);
        
        Alternative alt2 = new Alternative("alt2");
        LinkedList<Double> alt2_val = new LinkedList<Double>();
        alt2_val.add(8.0);
        alt2_val.add(9.0);
        alt2_val.add(3.0);
        alt2_val.add(8.0);
        alt2.setCriteriaValues(alt2_val);
        maut_test.addAlternative(alt2);
        
        Alternative alt3 = new Alternative("alt3");
        LinkedList<Double> alt3_val = new LinkedList<Double>();
        alt3_val.add(8.0);
        alt3_val.add(9.0);
        alt3_val.add(3.0);
        alt3_val.add(8.0);
        alt3.setCriteriaValues(alt3_val);
        maut_test.addAlternative(alt3);
        
        maut_test.calculate();
        
        showCriteria(maut_test);
        
        for(int i=0; i<maut_test.getAlternatives().size(); i++)  {
            System.out.println(maut_test.getAlternatives().get(i).getName() + " " + maut_test.getAlternatives().get(i).getScore());
        }
        
        
        
    }
    
    
}
