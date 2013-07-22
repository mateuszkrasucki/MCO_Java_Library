Description and example of AHP datafile structure (for working example see ahp.csv):

EPSILON,CRITERIA NUMBER,ALTERNATIVES NUMBER
ALTERNATIVES NAMES LIST
CRITERIUM NAME,fixMatrix or doNotFixMatrix flag (fixMatrix means that matrix will be recalculated to fir AHP requirements) 
ALTERNATIVES CRITERIUM VALUES PAIRWISE COMPARISON MATRIX
NEXT CRITERIUM NAME... 
...
criteriaMatrix,fix Matrix or doNotFixMatrix flag  (fixMatrix means that matrix will be recalculated to fir AHP requirements) 
CRITERIA PAIRWISE IMPORTANCE COMPARISON MATRIX


EXAMPLE:

0.0001,3,4
car1,car2,car3,car4
style,doNotFixMatrix
1.0000,0.2500,4.0000,0.1667
4.0000,1.0000,4.0000,0.2500
0.2500,0.2500,1.0000,0.2000
6.0000,4.0000,5.0000,1.0000
reliability,doNotFixMatrix
1.0000,2.0000,5.0000,1.0000
0.5000,1.0000,3.0000,2.0000
0.2000,0.3333,1.0000,0.2500
1.0000,0.5000,4.0000,1.0000
fuelEconomy,fixMatrix
1.0000,1.2593,1.4167,1.2143
0.0000,1.0000,1.1250,0.9643
0.0000,0.0000,1.0000,0.8571
0.0000,0.0000,0.0000,1.0000
criteriaMatrix,fixMatrix
1.0000,0.5000,3.0000
0.0000,1.0000,4.0000
0.0000,0.0000,1.0000
