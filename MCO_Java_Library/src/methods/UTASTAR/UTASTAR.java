package methods.UTASTAR;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Mateusz Krasucki, based on UTASTARinjava created by Andreadis Pavlos
 */
public class UTASTAR {
    private LinkedList<Criterium> criteria;
    private LinkedList<Alternative> referenceAlternatives;
    private LinkedList<Alternative> referenceAlternativesRanking;
    
    private LinkedList<Alternative> alternatives;
    private LinkedList<Alternative> ranking;
    
    private double preferenceThreshold;
    private double epsilon;
    
    private SimplexTable standardForm;
    private SimplexTable complementaryForm;
    
    private double[][][] valueFunctionsOfU;
    private double[][][] valueFunctionsOfW;
    private double[][][] deltaValueFunctions;
    
    private double[][] sensitivityAnalysis;
    private SimplexTable[] alternateForms;
    private double standardSolution[];
    
    private double averageWeightMatrix[][];
   
    
    public UTASTAR() {
                this.criteria = new LinkedList<Criterium>();
                this.referenceAlternatives = new LinkedList<Alternative>();
                this.alternatives = new LinkedList<Alternative>();
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;
    }
    
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives) {
                this.criteria = criteria;
                this.referenceAlternatives = referenceAlternatives;
                this.alternatives = new LinkedList<Alternative>();
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;
    }
    
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, double preferenceThreshold) {
                 this.criteria = criteria;
                 this.referenceAlternatives = referenceAlternatives;
                 this.alternatives = new LinkedList<Alternative>();
                 this.referenceAlternativesRanking = new LinkedList<Alternative>();
                 this.ranking = new LinkedList<Alternative>();
                 this.preferenceThreshold = preferenceThreshold;
                 this.epsilon = 0.00001;            
     }
    
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, double preferenceThreshold, double epsilon) {
                 this.criteria = criteria;
                 this.referenceAlternatives = referenceAlternatives;
                 this.alternatives = new LinkedList<Alternative>();
                 this.referenceAlternativesRanking = new LinkedList<Alternative>();
                 this.ranking = new LinkedList<Alternative>();
                 this.preferenceThreshold = preferenceThreshold;
                 this.epsilon = epsilon;          
     }
    
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, LinkedList<Alternative> alternatives) {
                this.criteria = criteria;
                this.referenceAlternatives = referenceAlternatives;
                this.alternatives = alternatives;
                this.referenceAlternativesRanking = new LinkedList<Alternative>();
                this.ranking = new LinkedList<Alternative>();
                this.preferenceThreshold = 0.05;
                this.epsilon = 0.00001;
    }
    
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, LinkedList<Alternative> alternatives, double preferenceThreshold) {
                 this.criteria = criteria;
                 this.referenceAlternatives = referenceAlternatives;
                 this.alternatives = alternatives;
                 this.referenceAlternativesRanking = new LinkedList<Alternative>();
                 this.ranking = new LinkedList<Alternative>();
                 this.preferenceThreshold = preferenceThreshold;
                 this.epsilon = 0.00001;            
     }
    
    public UTASTAR(LinkedList<Criterium> criteria, LinkedList<Alternative> referenceAlternatives, LinkedList<Alternative> alternatives, double preferenceThreshold, double epsilon) {
                 this.criteria = criteria;
                 this.referenceAlternatives = referenceAlternatives;
                 this.alternatives = alternatives;
                 this.referenceAlternativesRanking = new LinkedList<Alternative>();
                 this.ranking = new LinkedList<Alternative>();
                 this.preferenceThreshold = preferenceThreshold;
                 this.epsilon = epsilon;          
     }
    
    public UTASTAR(String filename, double preferenceThreshold, double epsilon) {
        this(filename);
        this.preferenceThreshold = preferenceThreshold;
        this.epsilon = epsilon;   
    }
    
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
   
    private void createValueFunctionsOfU()   {
        int i, j, k;

        int multiplier = 0;
        int criterion = 1;
        int valueIndex = 2;

        double x;
        valueFunctionsOfU = new double[this.getReferenceAlternativesNum()][][];
        double[][] tempVector = new double[this.getCriteriaNum()*2][3];
        int slot;
        for(j = 0; j < this.getReferenceAlternativesNum(); j++)
        {         
         slot = 0;
         for(i = 0; i < this.getCriteriaNum(); i++)
         {
          referenceAlternatives.get(j).tmpUTASTARvalues.add(0.0);
          if(referenceAlternatives.get(j).getCriteriumValue(i) <= criteria.get(i).getMufArgs().get(0))
          {
           referenceAlternatives.get(j).tmpUTASTARvalues.set(i, criteria.get(i).getMufArgs().get(0));
           tempVector[slot][multiplier] = 1;
           tempVector[slot][criterion] = i+1;
           tempVector[slot][valueIndex] = 1;
           slot++;
          }
          else if(referenceAlternatives.get(j).getCriteriumValue(i) >= criteria.get(i).getMufArgs().get(criteria.get(i).getMufArgs().size() - 1))
          {
           referenceAlternatives.get(j).tmpUTASTARvalues.set(i, criteria.get(i).getMufArgs().get(criteria.get(i).getMufArgs().size() - 1));
           tempVector[slot][multiplier] = 1;
           tempVector[slot][criterion] = i+1;
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
             tempVector[slot][criterion] = i+1;
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
             tempVector[slot][criterion] = i+1;
             tempVector[slot][valueIndex] = k;
             slot++;
             //
             tempVector[slot][multiplier] = x;
             tempVector[slot][criterion] = i+1;
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
         valueFunctionsOfU[j] = new double[slot][3];
         for(slot = 0; slot < valueFunctionsOfU[j].length; slot++)
         {
          valueFunctionsOfU[j][slot][multiplier] = tempVector[slot][multiplier];
          valueFunctionsOfU[j][slot][criterion] = tempVector[slot][criterion];
          valueFunctionsOfU[j][slot][valueIndex] = tempVector[slot][valueIndex];
         }
        }
    }
    
 
    private void createValueFunctionsOfW(){
     int i, j, k, slot;
     double multiplier;
     int criterion, valueIndex;

     valueFunctionsOfW = new double[this.getReferenceAlternativesNum()][this.getCriteriaNum()][];

     for(j = 0; j < this.getReferenceAlternativesNum(); j++)
      for(i = 0; i < this.getCriteriaNum(); i++)
      {
       valueFunctionsOfW[j][i] = new double[criteria.get(i).getMufArgs().size()-1];
       for(k = 0; k < valueFunctionsOfW[j][i].length; k++)
        valueFunctionsOfW[j][i][k] = 0;
      }

     for(j = 0; j < this.getReferenceAlternativesNum(); j++)
     {
      for(slot = 0; slot < valueFunctionsOfU[j].length; slot++)
      {
       multiplier = valueFunctionsOfU[j][slot][0];
       criterion = (int) valueFunctionsOfU[j][slot][1];
       valueIndex = (int) valueFunctionsOfU[j][slot][2];
       for(k = 0; k < valueIndex-1; k++)
        valueFunctionsOfW[j][criterion-1][k] =
        valueFunctionsOfW[j][criterion-1][k] + multiplier;
      }
     }

    }

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
        deltaValueFunctions[j][i][k] = valueFunctionsOfW[j][i][k] - valueFunctionsOfW[j+1][i][k];

    }   
    
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

    public void createComplementarySimplexTable(int criterionIndex){
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
     for(i = 0; i < criterionIndex; i++)
       start = start + deltaValueFunctions[0][i].length ;
     int end = start + deltaValueFunctions[0][criterionIndex].length ;
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

     alternateForms[criterionIndex] = new SimplexTable(ofm, a, base, baseValues);

    }
    
    public void calculateAlternativeScore(Alternative alternative) {
        double score = 0;
        for(int i=0; i<this.getCriteriaNum(); i++)  {
            score = score + this.getCriterium(i).getMarginalUtilityFunctionValue(alternative.getCriteriumValue(i));
        }
        alternative.setScore(score);
    }
    
  
    public void calculate(){
        try {
          int i, j, k;

          createValueFunctionsOfU();
          createValueFunctionsOfW();
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
              
              createComplementarySimplexTable(i);
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
    
    public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
    }
    
    public void addReferenceAlternative(Alternative referenceAlternative)   {
       referenceAlternative.setId(referenceAlternatives.size()+1);
       referenceAlternatives.add(referenceAlternative);
    }
    
    
    public void addAlternative(Alternative alternative)   {
       alternative.setId(alternatives.size()+1);
       alternatives.add(alternative);
    }
    
    public LinkedList<Criterium> getCriteria() {
        return criteria;
    }
    
    public Criterium getCriterium(int i)    {
        if(i>=this.criteria.size()) {
            throw new IndexOutOfBoundsException("There is no criterium with this index.");
        }
        return criteria.get(i);
    }

    public void setCriteria(LinkedList<Criterium> criteria) {
        this.criteria = criteria;
    }

    public LinkedList<Alternative> getReferenceAlternatives() {
        return referenceAlternatives;
    }
    
    public Alternative getReferenceAlternative(int i)    {
        if(i>=this.referenceAlternatives.size()) {
            throw new IndexOutOfBoundsException("There is no reference alternative with this index.");
        }
        return referenceAlternatives.get(i);
    }

    public void setReferenceAlternatives(LinkedList<Alternative> referenceAlternatives) {
        this.referenceAlternatives = referenceAlternatives;
    }
    
    public LinkedList<Alternative> getAlternatives() {
        return alternatives;
    }
    
    public Alternative getAlternative(int i)    {
        if(i>=this.alternatives.size()) {
            throw new IndexOutOfBoundsException("There is no alternative with this index.");
        }
        return alternatives.get(i);
    }

    public void setAlternatives(LinkedList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }
    
    public LinkedList<Alternative> getRanking() {
        return ranking;
    }

    public LinkedList<Alternative> getReferenceAlternativesRanking() {
        return referenceAlternativesRanking;
    }
    
    
    public double getAlternativeValue(int i)    {
        if(i<alternatives.size())   {
            return alternatives.get(i).getScore();
        }
        return 0;
    }
    
    public double getReferenceAlternativeValue(int i)    {
        if(i<referenceAlternatives.size())   {
            return referenceAlternatives.get(i).getScore();
        }
        return 0;
    }   
    
    public double getAlternativeValue(Alternative alt)    {
        return alt.getScore();
    }
    
    public Alternative getAlternativeByRank(int rank)    {
        if(rank>=this.ranking.size()) {
            throw new IndexOutOfBoundsException("There is no alternative with this index.");
        }
        return ranking.get(rank);
    }    
    
    public Alternative getReferenceAlternativeByRank(int rank)    {
        if(rank>=this.referenceAlternativesRanking.size()) {
            throw new IndexOutOfBoundsException("There is no alternative with this index.");
        }
        return this.referenceAlternativesRanking.get(rank);
    }      
    
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    public int getReferenceAlternativesNum() {
        return this.referenceAlternatives.size();
    }
    
    public int getAlternativesNum() {
        return this.alternatives.size();
    }

    public double[][] getAverageWeightMatrix() {
        if(this.averageWeightMatrix == null)    {
            throw new IndexOutOfBoundsException("Average Weight Matrix is not calculated.");
        }
        return averageWeightMatrix;
    }
  
    
    
}
