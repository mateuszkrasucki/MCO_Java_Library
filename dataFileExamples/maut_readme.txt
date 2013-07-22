Description and example of MAUT datafile structure (for working example see maut.csv):

FIRST GROUP CRITERIUM,PARENT CRITERIUM OF THIS CRITERIUM (root if has not parent),NAME OF THIS CRITERIUM,WEIGHT OF THIS CRITERIUM
NEXT GROUP CRITERIUM,PARENT CRITERIUM OF THIS CRITERIUM (root if has not parent),NAME OF THIS CRITERIUM,WEIGHT OF THIS CRITERIUM
...
FIRST NORMAL CRITERIUM,PARENT CRITERIUM OF THIS CRITERIUM (root if has not parent),NAME OF THIS CRITERIUM,WEIGHT OF THIS CRITERIUM,LINEAR (UTILITY FUNCTION TYPE),WORST (Value for which function value will be equal to 0),BEST(Value for which function value will be equal to 1)
NEXT NORMAL CRITERIUM,PARENT CRITERIUM OF THIS CRITERIUM (root if has not parent),NAME OF THIS CRITERIUM,WEIGHT OF THIS CRITERIUM,EXPONENTIAL (UTILITY FUNCTION TYPE),WORST (Value for which function value will be equal to 0),BEST(Value for which function value will be equal to 1),[OPTIONAL: c Parameter used control function shape between worst and best values]
...
Alternatives,ALTERNATIVES NAMES LIST
ALTERNATIVES CRITERIA VALUES MATRIX (each row is one alternative)

EXAMPLE:

GroupCriterium,root,c1,0.5
GroupCriterium,c1,c11,0.7
NormalCriterium,c11,c111,0.6,LINEAR,1,10
NormalCriterium,c11,c112,0.8,LINEAR,1,10
NormalCriterium,c1,c12,0.3,LINEAR,2,4
NormalCriterium,root,c2,0.5,LINEAR,1,9
Alternatives,alt1,alt2,alt3
8.0,9.0,3.0,8.0
8.0,9.0,3.0,8.0
8.0,9.0,3.0,8.0