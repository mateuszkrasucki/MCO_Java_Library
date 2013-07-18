package methods.UTASTAR;
/*
SimplexTable.java - "Simplex table" simulator and solver

Copyright (C) 2009 Andreadis Pavlos

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Author:     Andreadis Pavlos <andreadis.paul@yahoo.com>
Date:       14/09/2009
Version:    0.3
*/

/*
This Class represents a Simplex Table. A form used in linear programming.
It contains the necessary algorithms for the sollution of such problems. 

Some General Notes: 
 * a) Does not include support for 'M'(still works if values properly assigned).
 * b) Does not do any initial base validation (problem is assumed correctly given).
 * c) Minor improvements are being considered.
 * d) Most minor functions have been implemented to return an int value of "0" 
      if they were succesfully excecuted or "1" if that is not the case (usually
      because the given indexes are out of bounds).

NEW!! as of Version 0.3:
 * a) public double calculateProfit() now executes properly.
 * b) Future versions should include support for standard formed input. As it is
 * it requires that artificial variables have been formed.
*/

public class SimplexTable{

 public double[] sollution;

 public int baseSize;
 public int numberOfVariables;

 public int stepNumber;

 public int[] baseVector;
 public double[][] alphaTable;

 public int lineIndex;
 public int collumnIndex;

 public double[] objectiveFunctionMultipliers;
 
 public double[] limitedCleanProfits;

 public double[] baseVariableValues;

 public int[] isVariableBlocked;

 public double profit;
 
 //6 Constructors:
 
 public SimplexTable(){
  baseSize = 0;
  numberOfVariables = 0;
  stepNumber = 0;
  lineIndex = 0;
  collumnIndex = 0;
  profit = 0;
 }//Won't function adequately -> will update in time

 public SimplexTable(double[][] a){
  baseSize = a.length;
  numberOfVariables = a[0].length;

  objectiveFunctionMultipliers = new double[numberOfVariables];
  alphaTable = new double[baseSize][numberOfVariables];
  baseVector = new int[baseSize];
  baseVariableValues = new double[baseSize];
  isVariableBlocked = new int[numberOfVariables];
  limitedCleanProfits = new double[numberOfVariables];
  
  int i, j;
  
  for(i = 0; i < baseSize; i++)
  {
   baseVector[i] = -1;
   baseVariableValues[i] = 0;
   for(j = 0; j < numberOfVariables; j++)
   {
    alphaTable[i][j] = a[i][j];
   }
  }
  
  for(j = 0; j < numberOfVariables; j++)
  {
   objectiveFunctionMultipliers[j] = 0;
   isVariableBlocked[j] = 0;
  }

  stepNumber = 0;
  lineIndex = 0;
  collumnIndex = 0;
  profit = 0;

 }

 public SimplexTable(double[] ofm){
  baseSize = 0;
  numberOfVariables = ofm.length;
  
  objectiveFunctionMultipliers = new double[numberOfVariables];
  isVariableBlocked = new int[numberOfVariables];
  limitedCleanProfits = new double[numberOfVariables];

  int j;
  
  for(j = 0; j < numberOfVariables; j++)
  {
   objectiveFunctionMultipliers[j] = ofm[j];
   isVariableBlocked[j] = 0;
  }

  stepNumber = 0;
  lineIndex = 0;
  collumnIndex = 0;
  profit = 0;

 }

 public SimplexTable(double[] ofm, double[][] a){
  baseSize = a.length;
  numberOfVariables = a[0].length;
  
  objectiveFunctionMultipliers = new double[numberOfVariables];
  isVariableBlocked = new int[numberOfVariables];
  limitedCleanProfits = new double[numberOfVariables];
  alphaTable = new double[baseSize][numberOfVariables];
  baseVector = new int[baseSize];
  baseVariableValues = new double[baseSize];
  
  int i, j;
  
  for(i = 0; i < baseSize; i++)
  {
   baseVector[i] = -1;
   baseVariableValues[i] = 0;
   for(j = 0; j < numberOfVariables; j++)
   {
    alphaTable[i][j] = a[i][j];
   }
  }
  
  for(j = 0; j < numberOfVariables; j++)
  {
   objectiveFunctionMultipliers[j] = ofm[j];
   isVariableBlocked[j] = 0;
  }

  stepNumber = 0;
  lineIndex = 0;
  collumnIndex = 0;
  profit = 0;

 }

 public SimplexTable(double[] ofm, double[][] a, int[] base, double[] baseValues){
  baseSize = a.length;
  numberOfVariables = a[0].length;
  
  objectiveFunctionMultipliers = new double[numberOfVariables];
  isVariableBlocked = new int[numberOfVariables];
  limitedCleanProfits = new double[numberOfVariables];
  alphaTable = new double[baseSize][numberOfVariables];
  baseVector = new int[baseSize];
  baseVariableValues = new double[baseSize];
  
  int i, j;
  
  for(i = 0; i < baseSize; i++)
  {
   baseVector[i] = base[i];
   baseVariableValues[i] = baseValues[i];
   for(j = 0; j < numberOfVariables; j++)
   {
    alphaTable[i][j] = a[i][j];
   }
  }
  
  for(j = 0; j < numberOfVariables; j++)
  {
   objectiveFunctionMultipliers[j] = ofm[j];
   isVariableBlocked[j] = 0;
  }

  stepNumber = 0;
  lineIndex = 0;
  collumnIndex = 0;
  profit = 0;

 }

 public SimplexTable(double[] ofm, double[][] a, int[] base, double[] baseValues, int step){
  baseSize = a.length;
  numberOfVariables = a[0].length;
  
  objectiveFunctionMultipliers = new double[numberOfVariables];
  isVariableBlocked = new int[numberOfVariables];
  limitedCleanProfits = new double[numberOfVariables];
  alphaTable = new double[baseSize][numberOfVariables];
  baseVector = new int[baseSize];
  baseVariableValues = new double[baseSize];
  
  int i, j;
  
  for(i = 0; i < baseSize; i++)
  {
   baseVector[i] = base[i];
   baseVariableValues[i] = baseValues[i];
   for(j = 0; j < numberOfVariables; j++)
   {
    alphaTable[i][j] = a[i][j];
   }
  }
  
  for(j = 0; j < numberOfVariables; j++)
  {
   objectiveFunctionMultipliers[j] = ofm[j];
   isVariableBlocked[j] = 0;
  }

  stepNumber = step;
  lineIndex = 0;
  collumnIndex = 0;
  profit = 0;

 }

 //Methods for manipulating the "objective function multipliers" (aka ofm) vector
 
  //Inputs an "ofm" vector (HIGH RISK of DATA LOSS or VECTOR INCOMPATABILITY)
 public void inputOfm(double[] ofm){
  if(numberOfVariables < ofm.length)
   {
    numberOfVariables = ofm.length;
   }
  
  objectiveFunctionMultipliers = new double[numberOfVariables];
  
  int j;
  
  for(j = 0; j < ofm.length; j++)
  {
   objectiveFunctionMultipliers[j] = ofm[j];
  }
 }
  //Sets an "ofm" vector's single cell to a given value.
 public int changeOfm(int variableIndex, double value){
  int error = 1;
  if(variableIndex < numberOfVariables && variableIndex >= 0)
  {
   objectiveFunctionMultipliers[variableIndex] = value;
   error = 0;
  }
  return error;
 }
  //Decreases an "ofm" vector's single cell value by a given ammount
 public int decreaseOfm(int variableIndex, double ammount){
  int error = 1;
  if(variableIndex < numberOfVariables && variableIndex >= 0)
  {
   objectiveFunctionMultipliers[variableIndex] = objectiveFunctionMultipliers[variableIndex] - ammount;
   error = 0;
  }
  return error;
 }
  //Increases an "ofm" vector's single cell value by a given ammount
 public int increaseOfm(int variableIndex, double ammount){
  int error = 1;
  if(variableIndex < numberOfVariables && variableIndex >= 0)
  {
   objectiveFunctionMultipliers[variableIndex] = objectiveFunctionMultipliers[variableIndex] + ammount;
   error = 0;
  }
  return error;
 }

 //Methods for manipulating the "Alpha" table
 
  //Inputs an "Alpha" table
 public int inputAlpha(double[][] a){
  int possibleDataLoss = 0;  
  baseSize = a.length;
  int temp = a[0].length, i;
  for (i = 1; i < baseSize; i++)
  {
   if(a[i].length != temp) possibleDataLoss = 1;
   if(a[i].length < temp) temp = a[i].length;
  }
  numberOfVariables = temp;
  
  alphaTable = new double[baseSize][numberOfVariables];
  
  int j;
  
  for(i = 0; i < baseSize; i++){
   for(j = 0; j < numberOfVariables; j++){
    alphaTable[i][j] = a[i][j];
   }
  }
  return possibleDataLoss;
 }
  //Sets an "Alpha" table's single cell to a given value
 public int changeAlpha(int baseIndex, int variableIndex, double value){
  int error = 1;
  if(baseIndex < baseSize && variableIndex < numberOfVariables && baseIndex >= 0 && variableIndex >= 0)
  {
   alphaTable[baseIndex][variableIndex] = value;
   error = 0;
  }
  return error;
 }
  //Decreases an "Alpha" table's single cell value by a given ammount
 public int decreaseAlpha(int baseIndex, int variableIndex, double ammount){
  int error = 1;
  if(baseIndex < baseSize && variableIndex < numberOfVariables && baseIndex >= 0 && variableIndex >= 0)
  {
   alphaTable[baseIndex][variableIndex] = alphaTable[baseIndex][variableIndex] - ammount;
   error = 0;
  }
  return error;
 }
  //Increases an "Alpha" table's single cell value by a given ammount
 public int increaseAlpha(int baseIndex, int variableIndex, double ammount){
  int error = 1;
  if(baseIndex < baseSize && variableIndex < numberOfVariables && baseIndex >= 0 && variableIndex >= 0)
  {
   alphaTable[baseIndex][variableIndex] = alphaTable[baseIndex][variableIndex] + ammount;
   error = 0;
  }
  return error;
 }
  //Changes an "Alpha" table's single line with a given vector
 public int changeAlphaLine(int baseIndex, double[] line){
  int error = 1;
  int j;   
     
  if(baseIndex < baseSize && line.length >= numberOfVariables && baseIndex >= 0)
  {
   for(j = 0; j < numberOfVariables; j++)
   {
    alphaTable[baseIndex][j] = line[j];
   }
   error = 0;
  }
  return error;
 }

 //Methods for manipulating the "base" vector
 
  //Inputs a "base" vector (HIGH RISK of DATA LOSS or VECTOR INCOMPATABILITY)
 public int inputBase(int[] base, double[] baseValues){
  int error = 1;
  if(base.length == baseValues.length) 
  {
   error = 0;
   baseSize = base.length;
  
   baseVector = new int[baseSize];
   baseVariableValues = new double[baseSize];
  
   int j;
  
   for(j = 0; j < base.length; j++)
   {
    baseVector[j] = base[j];
    baseVariableValues[j] = baseValues[j];
   }
  }
  return error;
 }
  //Sets the variable indicated by a "base" vector's single cell (Can hold more
  //safety nets)
 public int changeBase(int baseIndex, int variable, double value){
  int error = 1;
  if(baseIndex < baseSize && baseIndex >= 0)
  {
   baseVector[baseIndex] = variable;
   baseVariableValues[baseIndex] = value;
   error = 0;
  }
  return error;
 }

 //Methods for retrieving data

  //Returns the number of variables in the base (aka the base vector's size)
 public int getBaseSize(){
   return baseSize;
 }
  //Returns the total number of variables used for the simplex algorithm(aka the alpha table's length)
 public int getNumberOfVariables(){
   return numberOfVariables;
 }
  //Returns the objective function's multiplier's in the form of a vector
 public double[] getOfm(){
   return objectiveFunctionMultipliers;
 }
  //Returns the Alpha table
 public double[][] getAlpha(){
   return alphaTable;
 }
  //Returns the base vector
 public int[] getBase(){
   return baseVector;
 }
  //Returns the base vector's values
 public double[] getBaseValues(){
   return baseVariableValues;
 }
  //Returns a binary value indicating whether or not the collumn in the Alpha table that is related to the variable, as indicated by the 'variableindex', is blocked from the rest of the simplex process.
 public int checkIsVariableBlocked(int variableIndex){
   if(variableIndex < numberOfVariables && variableIndex >= 0) return isVariableBlocked[variableIndex];
   else return 1;
 }
  //Returns the profit calculated so far
 public double getProfit(){
   return profit;
 }
  //Returns THE SOLLUTION (actually the current variable values)
 public double[] getSollution(){
  sollution = new double[numberOfVariables];
  int baseVariableIndex = 0;
 
  int i, j;

  for(j = 0; j < numberOfVariables; j++) sollution[j] = 0;
   
  for(i = 0; i < baseSize; i++)
  {
   baseVariableIndex = baseVector[i];
   sollution[baseVariableIndex] = baseVariableValues[i];
  }
  return sollution;
 }

 //Methods used to solve the linear problem***********************************************************************************************************
  
  //Calculates the limited clean profits
 public double[] calculateLimitedCleanProfits(){
  int i, j, variable;
  double Zj; 
   
  for(j = 0; j < numberOfVariables; j++)
  {
   Zj = 0;
   if(checkIsVariableBlocked(j) == 0)
   {
    for(i = 0; i < baseSize; i++)
    {
     variable = baseVector[i];
     Zj = Zj + alphaTable[i][j]*objectiveFunctionMultipliers[variable];
    }
    limitedCleanProfits[j] = objectiveFunctionMultipliers[j] - Zj;
   }
  }
  return limitedCleanProfits;
 }
  
  //Checks if the current sollution is optimal
 public boolean checkIsSollutionOptimal(){
  int j;
  boolean sollutionIsOptimal = true;
  
  for(j = 0; j < numberOfVariables; j++)
  {
   if(checkIsVariableBlocked(j) == 0)
   {
    if(limitedCleanProfits[j] > 0)
    {
     sollutionIsOptimal = false;
     j = numberOfVariables+1;
    }
   }
  }  
  return sollutionIsOptimal;
 }
  //Checks if the sollution goes infinite
 public boolean checkIsSollutionInfinite(){
  int i, j;
  boolean thereIsPositive;
  boolean sollutionIsInfinite = false;
  
  for(j = 0; j < numberOfVariables; j++)
  {
   if(checkIsVariableBlocked(j) == 0)
   {
    if(limitedCleanProfits[j] > 0)
    {
     thereIsPositive = false;
     for(i = 0; i < baseSize; i++)
     {
      if(alphaTable[i][j] > 0)
      {
       thereIsPositive = true;
       i = baseSize+1;
      }
     }
     if(thereIsPositive == false)
     {
      sollutionIsInfinite = true;
      j = numberOfVariables+1;
     }
    }
   }
  }
  return sollutionIsInfinite; 
 }
  //Locates (& sets) the variable to be added to the base
 public int findVariableIn(){
  int j;
  double temp = 0;
  int tempIndex = -1;
  
  for(j = 0; j < numberOfVariables; j++)
  {
   if(checkIsVariableBlocked(j) == 0 && limitedCleanProfits[j] > 0 && limitedCleanProfits[j] > temp)
   {
    temp = limitedCleanProfits[j];
    tempIndex = j;
   }
  }
  collumnIndex = tempIndex;
  return tempIndex;   
 }
  //Locates (& sets) the variable to be removed from the base (requires 
  //knowledge of the variable to be added to the base)
 public int findVariableOut(){
  int i;
  double temp = 0;
  int tempIndex = -1;
  
  for(i = 0; i < baseSize; i++)
  {
   if(alphaTable[i][collumnIndex] > 0)
   {
    temp = baseVariableValues[i]/alphaTable[i][collumnIndex];
    tempIndex = i;
    i = baseSize+1;
   }
  }
  for(i = tempIndex+1; i < baseSize; i++)
  {
   if(alphaTable[i][collumnIndex] > 0)
   {
    if(baseVariableValues[i]/alphaTable[i][collumnIndex] < temp)
    {
     temp = baseVariableValues[i]/alphaTable[i][collumnIndex];
     tempIndex = i;
    }
   }
  }
  lineIndex = tempIndex;
  return tempIndex;
 }
  //Constructs the next step's simplex table
 public void createNextSimplexTable(){
  int i, j;
  double lineHeader;
  double guideElement = alphaTable[lineIndex][collumnIndex];
    
  baseVector[lineIndex] = collumnIndex;
  
  baseVariableValues[lineIndex] = baseVariableValues[lineIndex]/guideElement;
  for(j = 0; j < numberOfVariables; j++)
  {
   alphaTable[lineIndex][j] = alphaTable[lineIndex][j]/guideElement;
  }
  
  for(i = 0; i < baseSize; i++)
  { 
   if(i != lineIndex)
   {
    lineHeader = alphaTable[i][collumnIndex];
    baseVariableValues[i] = baseVariableValues[i]
                          - lineHeader*baseVariableValues[lineIndex];
    for(j = 0; j < numberOfVariables; j++)
    {
     alphaTable[i][j] = alphaTable[i][j]
                      - lineHeader*alphaTable[lineIndex][j];
    }
   }
  }
 }
  //Calculates the profit attained so far
 public double calculateProfit(){
  int i;   

  profit = 0;
  
  for (i = 0; i < baseVector.length; i++)
      profit = profit + baseVariableValues[i] * objectiveFunctionMultipliers[baseVector[i]];
  
  //System.out.println("\n \n" + "z STAR = : \n" + profit );
  return profit;
 }
  //Executes a simplex computational step
 public int doSimplexStep(){
  if(checkIsSollutionOptimal()) return 1;
  else if (checkIsSollutionInfinite()) return -1;
  else
  {
   findVariableIn();
   findVariableOut();
   stepNumber++;
   createNextSimplexTable();
   calculateProfit();
   calculateLimitedCleanProfits();
   return 0;
  }
 }
   //Solves the linear problem described by the simplex table
  public double[] solve(){
   calculateLimitedCleanProfits();
   int flag = 0;
   while(flag == 0)
   {
    flag = doSimplexStep();
   }
   return getSollution();      
  }
//END OF FILE
}
