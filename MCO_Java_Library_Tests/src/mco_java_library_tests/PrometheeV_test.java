/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mco_java_library_tests;
import methods.Promethee.Promethee;
import methods.Constraint;
import java.math.BigDecimal;
import methods.Alternative;
import methods.Criterium;
import org.ojalgo.optimisation.integer.IntegerSolver;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.Optimisation;


/**
 *
 * @author mateuszkrasucki
 */
public class PrometheeV_test {
    
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
        
        
        Constraint constraint1 = new Constraint(c1,Constraint.ConstrainType.UPPER,65.0);
        promethee.addConstraint(constraint1);
        promethee.calculatePromethee5();
        
        for(int i=0; i<promethee.alternativesBestSet.size(); i++)   {
            System.out.println(promethee.alternativesBestSet.get(i).name + " " + promethee.alternativesBestSet.get(i).mpf);
        }
        System.out.println(promethee.bestSetMPF);
       /* Variable x1 = Variable.makeBinary("x1");
        Variable x2 = Variable.makeBinary("x2");
        Variable x3 = Variable.makeBinary("x3");
        Variable x4 = Variable.makeBinary("x4");
        Variable x5 = Variable.makeBinary("x5");
        
        ExpressionsBasedModel model = new ExpressionsBasedModel();
        model.addVariable(x1);
        model.addVariable(x2);
        model.addVariable(x3);
        model.addVariable(x4);
        model.addVariable(x5);
        
               
        
        Expression ex1 = model.addExpression("exp1");
        ex1.setLinearFactor(x1, 1);
        ex1.setLinearFactor(x2, 2);
        ex1.setLinearFactor(x3, 2);
        ex1.setLinearFactor(x4, 1);
        ex1.setLinearFactor(x5, 2);
        ex1.upper(BigDecimal.valueOf(3.0));
        System.out.println(ex1.isConstraint());
  
        Expression ex2 = model.addExpression("exp2");
        ex2.setLinearFactor(x1, 3);
        ex2.setLinearFactor(x2, 4);
        ex2.setLinearFactor(x3, 2);
        ex2.setLinearFactor(x4, 1);
        ex2.setLinearFactor(x5, 2);
        ex2.upper(BigDecimal.valueOf(5.0));
        System.out.println(ex2.isConstraint());

        Expression ex3 = model.addExpression("obj");
        ex3.setLinearFactor(x1, 0.8);
        ex3.setLinearFactor(x2, 0.7);
        ex3.setLinearFactor(x3, 0.6);
        ex3.setLinearFactor(x4, 0.2);
        ex3.setLinearFactor(x5, 0.1);
        ex3.weight(BigDecimal.ONE);
        System.out.println(ex3.isObjective());
        model.setMaximisation(true);
        
        System.out.println("Is max?");
        System.out.println(model.isMaximisation());
        
        IntegerSolver solver = IntegerSolver.make(model);
        Optimisation.Result res = solver.solve();
        System.out.println(model.toString());
        System.out.println(solver.toString());
        System.out.println(res.toString());*/
        

        
    }
    
}
