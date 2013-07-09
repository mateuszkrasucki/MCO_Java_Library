/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods;

import java.util.LinkedList;

/**
 *
 * @author Mateusz Krasucki
 */
public class Alternative {
    	public String name;

	public LinkedList<Double> criteriaValues;
        public double mpf_plus;
        public double mpf_minus;
        public double mpf;
        public int id; 
       
	public Alternative() {
		name="";
                criteriaValues = new LinkedList<Double>();
                mpf_plus = 0;
                mpf_minus = 0;
                mpf = 0;
                id = 0;
	}
}
