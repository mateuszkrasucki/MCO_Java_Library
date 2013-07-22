Description and example of UTASTAR datafile structure (for working example see utastar.csv):

Criterium,NAME OF THIS CRITERIUM,MARGINAL UTILITY FUNCTION ARGUMENTS LIST (For those values marginal utility function values are determined, for all the other function values are approximated. Most often those are related with reference alternatives criterium values.)
Criterium,NAME OF THIS CRITERIUM,MARGINAL UTILITY FUNCTION ARGUMENTS LIST (For those values marginal utility function values are determined, for all the other function values are approximated. Most often those are related with reference alternatives criterium values.)
...
ReferenceAlternatives,REFERENCE ALTERNATIVES NAMES LIST
PreferenceStandings,PREFERENCE STANDINGS LIST FOR MATCHING REFERENCE ALTERNATIVE (It informs UTASTAR method what place in reference alternatives ranking has this alternative according to decision maker and makes marginal utility function calculation possible.)
REFERENCE ALTERNATIVES CRITERIA VALUES MATRIX (each row is one reference alternative)
//OPTIONAL:
Alternatives,ALTERNATIVES NAMES LIST
ALTERNATIVES CRITERIA VALUES MATRIX (each row is one  alternative)

EXAMPLE:

Criterium,c1,-30,-16,-2
Criterium,c2,-40,-30,-20,-20
Criterium,c3,0,1,2,3
ReferenceAlternatives,alt1,alt2,alt3,alt4,alt5
PreferenceStandings,1,2,2,3,4
-3,-10,1
-4,-20,2
-2,-20,0
-6,-40,0
-30,-30,3
Alternatives,alt1,alt2,alt3,alt4,alt5,alt6,alt7
-3,-10,1
-4,-20,2
-2,-20,0
-6,-40,0
-30,-30,3
-8,40,0
-20,-23,2