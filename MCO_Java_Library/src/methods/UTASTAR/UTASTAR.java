package methods.UTASTAR;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import simplex.SimplexTable;

/**
* UTASTAR method class. 
* Based on reference alternatives preference standings (ranking) UTASTAR method calculates 
 * @author Mateusz Krasucki, Gabriela Pastuszka and Andreadis Pavlos (author of UTASTARinJava)
 */
public class UTASTAR {
    
    /**
     * LinkedList containing all the criteria in MCO problem represented by UTASTAR method object.
    */
    private LinkedList<Criterium> criteria;
    
    /**
     * LinkedList containing all the reference alternatives on which preference standings (ranking) UTASTAR calculations are based.
    */
    private LinkedList<Alternative> referenceAlternatives;
    
    
    /**
     * LinkedList containing final ranking of reference alternatives after UTASTAR calculations. It is not always the same as preference standings provided by decision maker.
    */
    private LinkedList<Alternative> referenceAlternativesRanking;
    /**
     * LinkedList containing all the alternatives in MCO problem represented by UTASTAR method object. Those alternatives will be scored using information obtained from reference alternatives preference standings (outranking) provided by decision maker.
    */
    private LinkedList<Alternative> alternatives;
    /**
     * LinkedList containing all the alternatives in MCO problem represented by UTASTAR method object ordered by their score calculated by UTASTAR method based on reference alternative preference standings.
    */
    private LinkedList<Alternative> ranking;
    
    /**
     * Alternatives value difference triggering preference. Reference alternatives preference standings are interpreted by algorithm using this value.
     */
    private double preferenceThreshold;
    
    /**
     * Epsilon value is used to determine group of nearly optimal solutions of the problem. If there are solutions in epsilon neighbourhoud of the solution provided by Simplex all of them are averaged and together form final UTASTAR solution.
     */
    private double epsilon;
    
    /**
     * Standard Simplex table form used to find standard (main) solution of the problem.
     */
    private SimplexTable standardForm;
    
    /** 
     * Array of alternate Simplex table forms which allows to search for additional solutions in epislon neighbourhood of standard (main) solution of the UTASTAR problem.
     */
    private SimplexTable[] alternateForms;
    
    /**
     * Three dimensional array with reference alternatives global utility values used in UTASTAR calculations.
     */
    private double[][][] valueFunctions;
    
    /**
     * Three dimensional array with delta function value between reference alternatives alternatives.
     */ 
    private double[][][] deltaValueFunctions;
    
    /**
     * Array containing standard (main) solution of the UTASTAR MCO problem.
     */
    private double standardSolution[];
    
    /**
     * Array containing additional solutions within epsilon neighbourhood.
     */
    private double[][] sensitivityAnalysis;
    
    
    /**
     * Averaged weight matrix (basis of marginal utility function) based on solutions within epsilon neighbourhood of standard (main) solution.
     */    
    private double averageWeightMatrix[][];
   
    
    /**
     * UTASTAR basic constructor. 
     * Preference threshold is set to 0.05 and epsilon is set to 0.00001.
     */
    public UTASTAR() {
                this.criteria = new LinkedList<Criterium>();
                this.referenceAlternatives = new LinkedList<Alternative>();
                this.alternatives = new LinkedList<Alternative>();
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;
    }
    
    /**
     * UTASTAR constructor with criteria and reference alternatives lists provided as parameters.
     * Preference threshold is set to 0.05 and epsilon is set to 0.00001.
     * @param criteria LinkedList of Criterium objects representing criteria in MCO problem.
     * @param referenceAlternatives LinkedList of Alternative objects representing reference alternatives on which UTASTAR decision model will be based. Preference standing paramater should be set properly in all the alternatives.
     */
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives) {
                this.criteria = criteria;
                this.referenceAlternatives = referenceAlternatives;
                this.alternatives = new LinkedList<Alternative>();
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;
    }
    
    
    /**
     * UTASTAR constructor with criteria list, reference alternatives list, preference threshold and epsilon value provided as parameters.
     * @param criteria LinkedList of Criterium objects representing criteria in MCO problem.
     * @param referenceAlternatives LinkedList of Alternative objects representing reference alternatives on which UTASTAR decision model will be based. Preference standing paramater should be set properly in all the alternatives.
     * @param preferenceThreshold Alternatives value difference triggering preference. Reference alternatives preference standings are interpreted by algorithm using this value.
     * @param epsilon Epsilon value is used to determine group of nearly optimal solutions of the problem. If there are solutions in epsilon neighbourhoud of the solution provided by Simplex all of them are averaged and together form final UTASTAR solution.
     */
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, double preferenceThreshold, double epsilon) {
                 this.criteria = criteria;
                 this.referenceAlternatives = referenceAlternatives;
                 this.alternatives = new LinkedList<Alternative>();
                 this.referenceAlternativesRanking = new LinkedList<Alternative>();
                 this.ranking = new LinkedList<Alternative>();
                 this.preferenceThreshold = preferenceThreshold;
                 this.epsilon = epsilon;          
     }
    
    /**
     * UTASTAR constructor with criteria list, reference alternatives list and alternatives list provided as parameters.
     * Preference threshold is set to 0.05 and epsilon is set to 0.00001.
     * @param criteria LinkedList of Criterium objects representing criteria in MCO problem.
     * @param referenceAlternatives LinkedList of Alternative objects representing reference alternatives on which UTASTAR decision model will be based. Preference standing paramater should be set properly in all the alternatives.
     * @param alternatives LinkedList of Alternative objects representing alternatives to which UTASTAR decision model built based on reference alternatives will be applied.
     */
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, LinkedList<Alternative> alternatives) {
                this.criteria = criteria;
                this.referenceAlternatives = referenceAlternatives;
                this.alternatives = alternatives;
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;
    }
      /**
     * UTASTAR constructor with criteria list, reference alternatives list, alternatives list, preference threshold and epsilon value provided as parameters.
     * @param criteria LinkedList of Criterium objects representing criteria in MCO problem.
     * @param referenceAlternatives LinkedList of Alternative objects representing reference alternatives on which UTASTAR decision model will be based. Preference standing paramater should be set properly in all the alternatives.
     * @param alternatives LinkedList of Alternative objects representing alternatives to which UTASTAR decision model built based on reference alternatives will be applied.
     * @param preferenceThreshold Alternatives value difference triggering preference. Reference alternatives preference standings are interpreted by algorithm using this value.
     * @param epsilon Epsilon value is used to determine group of nearly optimal solutions of the problem. If there are solutions in epsilon neighbourhoud of the solution provided by Simplex all of them are averaged and together form final UTASTAR solution.
     */
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, LinkedList<Alternative> alternatives, double preferenceThreshold, double epsilon) {
                 this.criteria = criteria;
                 this.referenceAlternatives = referenceAlternatives;
                 this.alternatives = alternatives;
                 this.referenceAlternativesRanking = new LinkedList<Alternative>();
                 this.ranking = new LinkedList<Alternative>();
                 this.preferenceThreshold = preferenceThreshold;
                 this.epsilon = epsilon;          
     }
    
    /**
    * UTASTAR class constructor with data file, preference threshold and epsilon value provided as parameters.
    * @param filename Path to the file from which data can be read. 
    * It should be structured as csv file in dataFileExamples/utastar.csv.
    * @param preferenceThreshold Alternatives value difference triggering preference. Reference alternatives preference standings are interpreted by algorithm using this value.
    * @param epsilon Epsilon value is used to determine group of nearly optimal solutions of the problem. If there are solutions in epsilon neighbourhoud of the solution provided by Simplex all of them are averaged and together form final UTASTAR solution.
    */
    public UTASTAR(String filename, double preferenceThreshold, double epsilon) {
        this(filename);
        this.preferenceThreshold = preferenceThreshold;
        this.epsilon = epsilon;   
    }
    
    /**
    * UTASTAR class constructor with data file as a parameter.
    * Preference threshold is set to 0.05 and epsilon is set to 0.00001.
    * @param filename Path to the file from which data can be read. 
    * It should be structured as csv file in dataFileExamples/utastar.csv.
    */
    public UTASTAR(String filename) {
                this.criteria = new LinkedList<Criterium>();
                this.referenceAlternatives = new LinkedList<Alternative>();
                this.alternatives = new LinkedList<Alternative>();
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;             
			
		BufferedReader br = null;
		String line = "";
		int refAltsCount = 0;
                int altsCount = 0;
                int refAltsIterationCount = 0;
                int altsIterationCount = 0;
                int criteriaCount = 0;
                
                String[] values;
		
		try {
                    br = new BufferedReader(new FileReader(filename));
                    line = br.readLine(); 
                    while(line != null)    {
                        values = line.split(",");
                        if(values[0].contentEquals("Criterium") && values.length>=3 && altsCount == 0)   {
                                    Criterium criterium = new Criterium(values[1]);
                                    for(int i = 2; i<values.length; i++)    {
                                        criterium.addMufArg(Double.parseDouble(values[i]));
                                    }
                                    this.addCriterium(criterium);
                                    criteriaCount++;
                        }
                        else if (values[0].contentEquals("ReferenceAlternatives") && refAltsCount == 0 && altsCount == 0 && criteriaCount != 0)   {                          
                            for(int i=1; i<values.length;i++)   {
                                Alternative alternative = new Alternative(values[i]);
                                this.addReferenceAlternative(alternative);
                                refAltsCount++;
                            }
                        } 
                        else if (values[0].contentEquals("PreferenceStandings") && refAltsCount != 0 && values.length == refAltsCount+1 && altsCount == 0 && criteriaCount != 0)   {                          
                            for(int i=1; i<values.length;i++)   {
                                this.getReferenceAlternative(i-1).setPreferenceStanding(Integer.parseInt(values[i]));
                            }
                        } 
                        else if(refAltsCount !=0 && altsCount==0 && criteriaCount!=0 && refAltsIterationCount < refAltsCount && values.length == criteriaCount)   {
                            for(int i=0; i<values.length; i++)  {
                                this.getReferenceAlternative(refAltsIterationCount).addCriteriumValue(Double.parseDouble(values[i]));
                            }
                            refAltsIterationCount++;
                        }
                        else if (values[0].contentEquals("Alternatives") && refAltsCount != 0 && altsCount == 0 && criteriaCount != 0)   {                          
                            for(int i=1; i<values.length;i++)   {
                                Alternative alternative = new Alternative(values[i]);
                                this.addAlternative(alternative);
                                altsCount++;
                            }
                        } 
                        else if(refAltsCount !=0 && altsCount!=0 && criteriaCount!=0 && altsIterationCount < altsCount && values.length == criteriaCount)   {
                            for(int i=0; i<values.length; i++)  {
                                this.getAlternative(altsIterationCount).addCriteriumValue(Double.parseDouble(values[i]));
                            }
                            altsIterationCount++;
                        }
                        else    {
                            throw new Exception("Wrong file format");
                        }
                        line = br.readLine(); 
                    }
                   
                    br.close();
                }
                catch (FileNotFoundException e) {
        			e.printStackTrace();
		}
		catch (IOException e) {
				e.printStackTrace();
				
                } catch (Exception e) {
        			e.printStackTrace();
		}       					
    }
   
    /**
     * Creates three dimensional array with reference alternatives global utility values used in UTASTAR calculations.
     */
    private void createValueFunctions()   {
        int i, j, k;

        int multiplier = 0;
        int criterium = 1;
        int valueIndex = 2;

        double x;
        double [][][] tmpValueFunctions = new double[this.getReferenceAlternativesNum()][][];
        double[][] tempVector = new double[this.getCriteriaNum()*2][3];
        int slot;
        for(j = 0; j < this.getReferenceAlternativesNum(); j++)
        {         
         slot = 0;
         for(i = 0; i < this.getCriteriaNum(); i++)
         {
          referenceAlternatives.get(j).tmpCriteriaValues.add(0.0);
          if(referenceAlternatives.get(j).getCriteriumValue(i) <= criteria.get(i).getMufArgs().get(0))
          {
           referenceAlternatives.get(j).tmpCriteriaValues.set(i, criteria.get(i).getMufArgs().get(0));
           tempVector[slot][multiplier] = 1;
           tempVector[slot][criterium] = i+1;
           tempVector[slot][valueIndex] = 1;
           slot++;
          }
          else if(referenceAlternatives.get(j).getCriteriumValue(i) >= criteria.get(i).getMufArgs().get(criteria.get(i).getMufArgs().size() - 1))
          {
           referenceAlternatives.get(j).tmpCriteriaValues.set(i, criteria.get(i).getMufArgs().get(criteria.get(i).getMufArgs().size() - 1));
           tempVector[slot][multiplier] = 1;
           tempVector[slot][criterium] = i+1;
           tempVector[slot][valueIndex] = criteria.get(i).getMufArgs().size();
           slot++;
          }
          else
          {
           for(k = 1; k < criteria.get(i).getMufArgs().size(); k++)
           {
            if(referenceAlternatives.get(j).getCriteriumValue(i) == criteria.get(i).getMufArgs().get(k))
            {
             tempVector[slot][multiplier] = 1;
             tempVector[slot][criterium] = i+1;
             tempVector[slot][valueIndex] = k+1;
             slot++;
             k = criteria.get(i).getMufArgs().size() + 1;
            }
            else if(referenceAlternatives.get(j).getCriteriumValue(i) < criteria.get(i).getMufArgs().get(k))
            {
             /////////DO linear Approximation///////
             x = (referenceAlternatives.get(j).getCriteriumValue(i) - criteria.get(i).getMufArgs().get(k-1))/
                 (criteria.get(i).getMufArgs().get(k) - criteria.get(i).getMufArgs().get(k-1));
             //
             tempVector[slot][multiplier] = 1 - x;
             tempVector[slot][criterium] = i+1;
             tempVector[slot][valueIndex] = k;
             slot++;
             //
             tempVector[slot][multiplier] = x;
             tempVector[slot][criterium] = i+1;
             tempVector[slot][valueIndex] = k+1;
             slot++;

                 //-------------------------------------------
                 // Exit condition
                 k = criteria.get(i).getMufArgs().size() + 1;
                 //------------------------------------------
            }
           }
          }
         }
         tmpValueFunctions[j] = new double[slot][3];
         for(slot = 0; slot < tmpValueFunctions[j].length; slot++)
         {
          tmpValueFunctions[j][slot][multiplier] = tempVector[slot][multiplier];
          tmpValueFunctions[j][slot][criterium] = tempVector[slot][criterium];
          tmpValueFunctions[j][slot][valueIndex] = tempVector[slot][valueIndex];
         }
        }
        
     double multiplier2;
     
     valueFunctions = new double[this.getReferenceAlternativesNum()][this.getCriteriaNum()][];

     for(j = 0; j < this.getReferenceAlternativesNum(); j++)
      for(i = 0; i < this.getCriteriaNum(); i++)
      {
       valueFunctions[j][i] = new double[criteria.get(i).getMufArgs().size()-1];
       for(k = 0; k < valueFunctions[j][i].length; k++)
        valueFunctions[j][i][k] = 0;
      }

     for(j = 0; j < this.getReferenceAlternativesNum(); j++)
     {
      for(slot = 0; slot < tmpValueFunctions[j].length; slot++)
      {
       multiplier2 = tmpValueFunctions[j][slot][0];
       criterium = (int) tmpValueFunctions[j][slot][1];
       valueIndex = (int) tmpValueFunctions[j][slot][2];
       for(k = 0; k < valueIndex-1; k++)
        valueFunctions[j][criterium-1][k] =
        valueFunctions[j][criterium-1][k] + multiplier2;
      }
     }

    }

    /**
     * Creates three dimensional array with delta function value between reference alternatives alternatives.
     */ 
    private void createDeltaValueFunctions(){
     int i, j, k;

     deltaValueFunctions = new double[this.getReferenceAlternativesNum()-1][this.getCriteriaNum()][];

     for(j = 0; j < this.getReferenceAlternativesNum()-1; j++)
      for(i = 0; i < this.getCriteriaNum(); i++)
      {
       deltaValueFunctions[j][i] = new double[criteria.get(i).getMufArgs().size()-1];
       for(k = 0; k < deltaValueFunctions[j][i].length; k++)
        deltaValueFunctions[j][i][k] = 0;
      }

     for(j = 0; j < this.getReferenceAlternativesNum()-1; j++)
      for(i = 0; i < this.getCriteriaNum(); i++)
       for(k = 0; k < deltaValueFunctions[j][i].length; k++)
        deltaValueFunctions[j][i][k] = valueFunctions[j][i][k] - valueFunctions[j+1][i][k];

    }   
    
  /**
     * Creates standard Simplex table form used to find standard (main) solution of the problem.
     */
    private void createSimplexTable(){
     int i, j, k, l;
     int numberOfW = 0;
     int numberOfGreaterThanOrEqualOccurences = 0;
     int greaterThanOrEqualOccurencesIndex = 0;

     int baseSize, numberOfVariables;
     double[] ofm;
     double[][] a;
     int[] base;
     double[] baseValues;

     baseSize = deltaValueFunctions.length + 1;

     numberOfVariables = 0;
     for(i = 0; i < this.getCriteriaNum(); i++)
      for(k = 0; k < deltaValueFunctions[0][i].length; k++)
      {
       numberOfVariables++;
       numberOfW++;
      }
     numberOfVariables = numberOfVariables + this.getReferenceAlternativesNum()*2;

     for(j = 0; j < this.getReferenceAlternativesNum()-1; j++)
      if(referenceAlternatives.get(j).getPreferenceStanding() != referenceAlternatives.get(j+1).getPreferenceStanding())
      {
       numberOfVariables++;
       numberOfGreaterThanOrEqualOccurences++;
      }
     numberOfVariables = numberOfVariables + this.getReferenceAlternativesNum();

     ofm = new double[numberOfVariables];
     for(j = 0; j < numberOfVariables; j++)
      ofm[j] = 0;

     a = new double[baseSize][numberOfVariables];
     for(i = 0; i < baseSize; i++)
      for(j = 0; j < numberOfVariables; j++)
       a[i][j] = 0;

     base = new int[baseSize];
     for(i = 0; i < baseSize; i++)
      base[i] = 0;

     baseValues = new double[baseSize];
     for(i = 0; i < baseSize; i++)
      baseValues[i] = 0;

     //for(j = 0; j < numberOfW; j++)
     // ofm[j] = 0;
     for(j = numberOfW; j < numberOfW + this.getReferenceAlternativesNum()*2; j++)
      ofm[j] = -1;
     //for(j = numberOfW + this.getAlternativesNum()*2; j < numberOfW + this.getAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences; j++)
     // ofm[j] = 0;
     for(j = numberOfW + this.getReferenceAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences; j < numberOfVariables; j++)
      ofm[j] = -2100000000;//minus Infinite

     for(i = 0; i < baseSize-1; i++)
     {
      j = 0;
      for(l = 0; l < this.getCriteriaNum(); l++)
       for(k = 0; k < deltaValueFunctions[0][l].length; k++)
       { 
        a[i][j] = deltaValueFunctions[i][l][k];
        j++;
       }
      a[i][numberOfW + i*2] = -1;
      a[i][numberOfW + i*2 + 1] = 1;
      a[i][numberOfW + i*2 + 2] = 1;
      a[i][numberOfW + i*2 + 3] = -1;
      if(referenceAlternatives.get(i).getPreferenceStanding() != referenceAlternatives.get(i+1).getPreferenceStanding())
      {
       a[i][numberOfW + this.getReferenceAlternativesNum()*2 + greaterThanOrEqualOccurencesIndex] = -1;
       greaterThanOrEqualOccurencesIndex++;
      }
      a[i][numberOfW + this.getReferenceAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences + i] = 1;
     }

     for(j = 0; j < numberOfW; j++)
      a[baseSize-1][j] = 1;
     a[baseSize-1][numberOfVariables-1] = 1;

     for(i = 0; i < baseSize; i++)
      base[i] = numberOfW + this.getReferenceAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences + i;

     for(i = 0; i < baseSize-1; i++)
      if(referenceAlternatives.get(i).getPreferenceStanding() != referenceAlternatives.get(i+1).getPreferenceStanding())
       baseValues[i] = preferenceThreshold;
     baseValues[baseSize-1] = 1;

     standardForm = new SimplexTable(ofm, a, base, baseValues);

    }

    /** 
     * Creates alternate Simplex table forms which allows to search for additional solutions in epislon neighbourhood of standard (main) solution of the UTASTAR problem.
     * @param criteriumIndex Criterium index for which alternate form is created.
     */
    private void createAlternativeFormSimplexTable(int criteriumIndex){
     int i, j, k, l;
     int numberOfW = 0;
     int numberOfGreaterThanOrEqualOccurences = 0;
     int greaterThanOrEqualOccurencesIndex = 0;

     int baseSize, numberOfVariables;
     double[] ofm;
     double[][] a;
     int[] base;
     double[] baseValues;

     baseSize = deltaValueFunctions.length + 1  +1;

     numberOfVariables = 0;
     for(i = 0; i < this.getCriteriaNum(); i++)
      for(k = 0; k < deltaValueFunctions[0][i].length; k++)
      {
       numberOfVariables++;
       numberOfW++;
      }
     numberOfVariables = numberOfVariables + this.getReferenceAlternativesNum()*2;

     for(j = 0; j < this.getReferenceAlternativesNum()-1; j++)
      if(referenceAlternatives.get(j).getPreferenceStanding() != referenceAlternatives.get(j+1).getPreferenceStanding())
      {
       numberOfVariables++;
       numberOfGreaterThanOrEqualOccurences++;
      }
     numberOfVariables = numberOfVariables + this.getReferenceAlternativesNum()  +1;

     ofm = new double[numberOfVariables];
     for(j = 0; j < numberOfVariables; j++)
      ofm[j] = 0;

     a = new double[baseSize][numberOfVariables];
     for(i = 0; i < baseSize; i++)
      for(j = 0; j < numberOfVariables; j++)
       a[i][j] = 0;

     base = new int[baseSize];
     for(i = 0; i < baseSize; i++)
      base[i] = 0;

     baseValues = new double[baseSize];
     for(i = 0; i < baseSize; i++)
      baseValues[i] = 0;

     int start = 0 ;
     for(i = 0; i < criteriumIndex; i++)
       start = start + deltaValueFunctions[0][i].length ;
     int end = start + deltaValueFunctions[0][criteriumIndex].length ;
     for(j = start; j < end; j++)
      ofm[j] = 1;

     for(j = numberOfW + this.getReferenceAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences; j < numberOfVariables; j++)
      ofm[j] = -2100000000;//minus Infinite

     for(i = 0; i < baseSize-1 -1; i++)
     {
      j = 0;
      for(l = 0; l < this.getCriteriaNum(); l++)
       for(k = 0; k < deltaValueFunctions[0][l].length; k++)
       { 
        a[i][j] = deltaValueFunctions[i][l][k];
        j++;
       }
      a[i][numberOfW + i*2] = -1;
      a[i][numberOfW + i*2 + 1] = 1;
      a[i][numberOfW + i*2 + 2] = 1;
      a[i][numberOfW + i*2 + 3] = -1;
      if(referenceAlternatives.get(i).getPreferenceStanding() != referenceAlternatives.get(i+1).getPreferenceStanding())
      {
       a[i][numberOfW + this.getReferenceAlternativesNum()*2 + greaterThanOrEqualOccurencesIndex] = -1;
       greaterThanOrEqualOccurencesIndex++;
      }
      a[i][numberOfW + this.getReferenceAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences + i] = 1;
     }

     for(j = 0; j < numberOfW; j++)
      a[baseSize-1 -1][j] = 1;
     a[baseSize-1 -1][numberOfVariables-1 -1] = 1;

     for(j = numberOfW; j < numberOfW + this.getReferenceAlternativesNum()*2; j++)
       a[baseSize-1][j] = 1;
     a[baseSize-1][numberOfVariables-1] = 1;

     for(i = 0; i < baseSize; i++)
      base[i] = numberOfW + this.getReferenceAlternativesNum()*2 + numberOfGreaterThanOrEqualOccurences + i;

     for(i = 0; i < baseSize-1 -1; i++)
      if(referenceAlternatives.get(i).getPreferenceStanding() != referenceAlternatives.get(i+1).getPreferenceStanding())
       baseValues[i] = preferenceThreshold;
     baseValues[baseSize-1 -1] = 1;
     baseValues[baseSize-1] = standardForm.getProfit() + epsilon;

     alternateForms[criteriumIndex] = new SimplexTable(ofm, a, base, baseValues);

    }
    
    /**
     * Calculates alternative score based on marginal utility function calculated by UTASTAR method.
     * @param alternative Alternative object for which score will be calculated and it will be saved in this object. The alternative criteria values have to be in compliance with UTASTAR object criteria list.
     */
    public void calculateAlternativeScore(Alternative alternative) {
        double score = 0;
        for(int i=0; i<this.getCriteriaNum(); i++)  {
            score = score + this.getCriterium(i).getMarginalUtilityFunctionValue(alternative.getCriteriumValue(i));
        }
        alternative.setScore(score);
    }
    
  
    /**
     * Performs UTASTAR method calculations on data added to UTASTAR object. 
     * Calculates marginal utility function based on reference alternatives list and their preference standings, then calculated reference alternatives and alternatives scores and creates rankings of both sets of alternatives.
     */
    public void calculate(){
        try {
          int i, j, k;

          createValueFunctions();
          createDeltaValueFunctions();
          createSimplexTable();
          standardSolution = standardForm.solve();
          averageWeightMatrix = new double[this.getCriteriaNum()][];


          alternateForms = new SimplexTable[this.getCriteriaNum()] ;
          sensitivityAnalysis = new double[this.getCriteriaNum()][] ;

          for(i = 0; i < this.getCriteriaNum(); i++)
          {          
              this.getCriterium(i).marginalUtilityFunction.clear();
              for(int z=0; z< this.getCriterium(i).getMufArgs().size(); z++)
                    this.getCriterium(i).marginalUtilityFunction.add(0.0);
              
              createAlternativeFormSimplexTable(i);
              sensitivityAnalysis[i] = alternateForms[i].solve();
          }

          double temp[] = new double[sensitivityAnalysis[0].length];
          for(i = 0; i < sensitivityAnalysis[0].length; i++)
          {
              temp[i] = 0;
              for(j = 0; j < sensitivityAnalysis.length; j++)
                temp[i] = temp[i] + sensitivityAnalysis[j][i];
              temp[i] = temp[i]/3;
          }

          for(i = 0; i< this.getCriteriaNum(); i++)
            averageWeightMatrix[i] = new double[deltaValueFunctions[0][i].length];

          k=0;
          for(i = 0; i< this.getCriteriaNum(); i++)
              for(j = 0; j< deltaValueFunctions[0][i].length; j++)
              {
                    averageWeightMatrix[i][j] = temp[k];
                    k = k + 1;
              }

          k=0;
          for(i = 0; i< this.getCriteriaNum(); i++)
          {
              temp[i] = 0;
              for(j = 0; j< deltaValueFunctions[0][i].length; j++)
              {
                    this.getCriterium(i).marginalUtilityFunction.set(j, temp[i]);
                    temp[i] = temp[i] + averageWeightMatrix[i][j];                
              }
              this.getCriterium(i).marginalUtilityFunction.set(j, temp[i]);
          }

          for(int z=0; z<this.getReferenceAlternativesNum(); z++)   {
              this.calculateAlternativeScore(this.getReferenceAlternative(z));
          }
          
          for(int z=0; z<this.getAlternativesNum(); z++)   {
              this.calculateAlternativeScore(this.getAlternative(z));
          }          
          
          
          if(alternatives.size()==0)    {
              alternatives = referenceAlternativesRanking;
          }
      
          this.referenceAlternativesRanking = new LinkedList<Alternative>(referenceAlternatives);
          this.ranking = new LinkedList<Alternative>(alternatives);
          
          
            Collections.sort(this.referenceAlternativesRanking, new Comparator<Alternative>() {
             @Override
             public int compare(Alternative o1, Alternative o2) {
                 if(o1.getScore()<o2.getScore())    {
                     return 1;
                 }
                 else if(o1.getScore()>o2.getScore())  {
                     return -1;
                 }
                 return 0;
             }
            });
            
            Collections.sort(this.ranking, new Comparator<Alternative>() {
             @Override
             public int compare(Alternative o1, Alternative o2) {
                 if(o1.getScore()<o2.getScore())    {
                     return 1;
                 }
                 else if(o1.getScore()>o2.getScore())  {
                     return -1;
                 }
                 return 0;
             }
            });
        }
        catch(Exception e)  {
            e.printStackTrace();
            System.out.println("Something went wrong. Probably you did not add referenceAlternatives or there are some errors in data.");
        }
            
      }
    
     /**
     * Adds criterium to UTASTAR object.
     * @param criterium Criterium object.
     */
    public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
    }
    
    /**
     * Adds reference alternative to UTASTAR object.
     * @param referenceAlternative Reference alternative object.
     */
    public void addReferenceAlternative(Alternative referenceAlternative)   {
       referenceAlternative.setId(referenceAlternatives.size()+1);
       referenceAlternatives.add(referenceAlternative);
    }
    
    
      /**
     * Adds alternative to UTASTAR object.
     * @param alternative Alternative object.
     */
    public void addAlternative(Alternative alternative)   {
       alternative.setId(alternatives.size()+1);
       alternatives.add(alternative);
    }
    
    
      /**
     * Returns list of all the criteria in UTASTAR object.
     * @return LinkedList containing Criterium objects.
     */
    public LinkedList<Criterium> getCriteria() {
        return criteria;
    }
    
      /**
     * Returns Criterium with the i-th order number. 
     * @param i Criterium order number.
     * @return Criterium object.
     */
    public Criterium getCriterium(int i)    {
        if(i>=this.criteria.size()) {
            throw new IndexOutOfBoundsException("There is no criterium with this index.");
        }
        return criteria.get(i);
    }


        /**
     * Sets criteria in UTASTAR object to LinkedList provided as parameter.
     * @param criteria LinkedList object containing Criterium objects.
     */
    public void setCriteria(LinkedList<Criterium> criteria) {
        this.criteria = criteria;
    }

        /**
    * Return all the reference alternatives stored in UTASTAR object.
     * @return LinkedList containing reference alternatives Alternative objects.
     */
    public LinkedList<Alternative> getReferenceAlternatives() {
        return referenceAlternatives;
    }
    
        /**
     * Returns i-th reference alternative.
     * @param i Reference alternative order number.
     * @return Alternative object.
     */
    public Alternative getReferenceAlternative(int i)    {
        if(i>=this.referenceAlternatives.size()) {
            throw new IndexOutOfBoundsException("There is no reference alternative with this index.");
        }
        return referenceAlternatives.get(i);
    }

        /**
     * Sets reference alternatives in UTASTAR object to LinkedList provided as parameter.
     * @param referenceAlternatives LinkedList object containing reference alternatives Alternative objects.
     */
    public void setReferenceAlternatives(LinkedList<Alternative> referenceAlternatives) {
        this.referenceAlternatives = referenceAlternatives;
    }
    
        /**
    * Return all the alternatives stored in UTASTAR object.
     * @return LinkedList containing Alternative objects.
     */
    public LinkedList<Alternative> getAlternatives() {
        return alternatives;
    }
    
        /**
     * Returns i-th alternative.
     * @param i Alternative order number.
     * @return Alternative object.
     */
    public Alternative getAlternative(int i)    {
        if(i>=this.alternatives.size()) {
            throw new IndexOutOfBoundsException("There is no alternative with this index.");
        }
        return alternatives.get(i);
    }

        /**
     * Sets alternatives in UTASTAR object to LinkedList provided as parameter.
     * @param alternatives LinkedList object containing Alternative objects.
     */
    public void setAlternatives(LinkedList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }
    
    
        /**
     * Returns alternatives ranking - all the alternatives in UTASTAR object ordered by their score calculated by UTASTAR method. 
     * @return LinkedList object containing Alternative objects ordered by their UTASTAR score. 
     */
    public LinkedList<Alternative> getRanking() {
        return ranking;
    }
    
        /**
     * Returns reference alternatives ranking - all the reference alternatives in UTASTAR object ordered by their score calculated by UTASTAR method.
     * It is not always the same as preference order provided by decision maker as an input of UTASTAR methos.
     * @return LinkedList object containing reference alternatives Alternative objects ordered by their UTASTAR score. 
     */
    public LinkedList<Alternative> getReferenceAlternativesRanking() {
        return referenceAlternativesRanking;
    }
    
    
         /**
     * Returns UTASTAR score of i-th alternative.
     * @param i Alternative order number.
     * @return i-th alternative UTASTAR score.
     */
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getScore();
        }
        return 0;
    }
    
        /**
     * Returns UTASTAR score of i-th reference alternative.
     * @param i Alternative order number.
     * @return i-th reference alternative UTASTAR score.
     */
    public double getReferenceAlternativeValue(int i)    {
        if(i<referenceAlternatives.size())   {
            return referenceAlternatives.get(i).getScore();
        }
        return 0;
    }   
    
         /**
     * Returns UTASTAR score of alternative alt. It has to be one of the alternatives added to the object before running calculate() method.
     * @param alt Alternative object.
     * @return UTASTAR score of alternative object.
     */
    public double getAlternativeValue(Alternative alt)    {
        return alt.getScore();
    }
    
    /**
     * Returns alternative with specific rank in ranking calculated by UTASTAR method.
     * @param rank Rank number of wanted alternative.
     * @return Alternative object of alternative with wanted rank.
     */
    public Alternative getAlternativeByRank(int rank)    {
        if(rank>=this.ranking.size()) {
            throw new IndexOutOfBoundsException("There is no alternative with this index.");
        }
        return ranking.get(rank-1);
    }    
    
    /**
     * Returns alternative with specific rank in reference alternatives ranking calculated by UTASTAR method.
     * @param rank Rank number of wanted reference alternative.
     * @return Alternative object of reference alternative with wanted rank.
     */
    public Alternative getReferenceAlternativeByRank(int rank)    {
        if(rank>=this.referenceAlternativesRanking.size()) {
            throw new IndexOutOfBoundsException("There is no alternative with this index.");
        }
        return this.referenceAlternativesRanking.get(rank-1);
    }      
    
    /**
     * Returns number of criteria in UTASTAR object.
     * @return Number of criteria in UTASTAR object.
     */
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    /**
     * Returns number of reference alternatives in UTASTAR object.
     * @return Number of reference alternatives in UTASTAR object.
     */
    public int getReferenceAlternativesNum() {
        return this.referenceAlternatives.size();
    }
    
    /**
     * Returns number of alternatives in UTASTAR object.
     * @return Number of alternatives in UTASTAR object.
     */
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
   
    
}
