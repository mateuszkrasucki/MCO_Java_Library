package methods.UTASTAR;

import java.util.LinkedList;

import simplex.SimplexTable; /*This Class represents a Simplex Table. A form used in linear programming.
It contains the necessary algorithms for the sollution of such problems.  Created by Andreadis Pavlos <andreadis.paul@yahoo.com> */

/**
 *
 * @author Mateusz Krasucki, based on Andreadis Pavlos work
 */
public class UTASTAR {
    private LinkedList<Criterium> criteria;
    private LinkedList<Alternative> alternatives;
    
    
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}
