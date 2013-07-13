/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.MAUT;

/**
 *
 * @author Mateusz Krasucki
 */
public interface Criterium {
   public boolean isGroup();
   public double getWeight();
   public void setWeight(double weight);
   public void setName(String name);
   public String getName();
    
}
