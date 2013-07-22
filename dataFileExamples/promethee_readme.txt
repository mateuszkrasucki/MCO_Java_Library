Description and example of PROMETHEE datafile structure (for working example see promethee.csv):

Criterium,NAME OF THIS CRITERIUM,WEIGHT OF THIS CRITERIUM,CRITERIUM OPTIMIZATION DIRECTION,PREFERENCE FUNCTION TYPE,Preference function parameters (number can vary, look at preference function constructors to determine what parameters are needed)
Criterium,NAME OF NEXT CRITERIUM,WEIGHT OF THIS CRITERIUM,CRITERIUM OPTIMIZATION DIRECTION,PREFERENCE FUNCTION TYPE,Preference function parameters (number can vary, look at preference function constructors to determine what parameters are needed)
...
//Constraints are used only in Promethee5 method:
Constraint,NAME OF THIS CONSTRAINT,TYPE OF THIS CONSTRAINT(BOTTOM/UPPER),CONSTRAIN VALUE
...
Alternatives,ALTERNATIVES NAMES LIST
ALTERNATIVES CRITERIA VALUES MATRIX (each row is one alternative)

EXAMPLE:

Criterium,c1,0.14285,MIN,GAUSSIAN,0.5
Criterium,c2,0.14285,MIN,LINEAR,0.05,0.2
Criterium,c3,0.14285,MAX,LINEAR,0.2
Criterium,c4,0.5714,MAX,THRESHOLD,0.2
Constraint,c1,UPPER,65
Alternatives,Car1,Car2,Car3,Car4
8.75,6.2,1.0,30.0
13.75,7.5,1.0,50.0
25.0,8.0,3.0,80.0
62.5,20.0,2.0,120.0