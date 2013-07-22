package methods.AHP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleEVD;

/**
 * AHP (Analytic Hierarchy Process) method class. 
 * @author Mateusz Krasucki, Gabriela Pastuszka
 */
public class AHP {
    
        /**
         * LinkedList containing all the criteria in MCO problem represented by UTASTAR method object.
        */
        private LinkedList<Criterium> criteria;
        /**
         * LinkedList containing all the alternatives in MCO problem represented by UTASTAR method object. Those alternatives will be scored using information obtained from reference alternatives preference standings (outranking) provided by decision maker.
        */
        private LinkedList<Alternative> alternatives;
        /**
         * LinkedList containing all the alternatives in MCO problem represented by UTASTAR method object ordered by their score calculated by UTASTAR method based on reference alternative preference standings.
        */
        private LinkedList<Alternative> ranking;
	
	private LinkedList<SimpleMatrix> altsCriteriaValues; // alternatives' criteria pariwaise comparisons values matrix
	
	private SimpleMatrix criteriaMatrix; // criteria importance pairwaise comparisons matrix      
        
        private double epsilon; // stop condition for eigenvectors calcualtions.
              
        
        private boolean calculated_;
        
        //results:
        private SimpleMatrix criteriaWeights; // computed as eigenvector of criteriaMatrix
        private SimpleMatrix alternativesCriteriaValues; // computed as eigenvector of subsequent altsCriteriaValues matrices.
        private SimpleMatrix alternativesValues; // final results.
        
        /**
	* AHP class constructor with data file as a parameter. 
	* @param filename Path to the file from which data can be read. 
        * It should be structured as csv file in dataFileExamples/ahp.csv. In the first line there should be epsilon value followed by criteria number and alternatives number. In the second line you should place alternatives' names. 
        * Starting from third line there is place for criteria name and fixing matrix flag (fixMatrix or doNotFixMatrix) followed by alternatives' criterium pariwaise comparisons values matrix.
        * At the end of the data file there should be criteriaMatrix (criteria pairwaise comparison matrix) with fixing matrix flag. It has to be preceded by line with "criteriaMatrix,fixMatrix" or "criteriaMatrix,doNotFixMatrix".
	*/
	public AHP(String filename) {		
		alternatives = new LinkedList<Alternative>();
                ranking = new LinkedList<Alternative>();
                criteria = new LinkedList<Criterium>();
                this.altsCriteriaValues = new LinkedList<SimpleMatrix>();
			
		BufferedReader br = null;
		String line = "";
		int altsCount = 0;
                int criteriaCount = 0;
                
                boolean fixMatrix = false;
                
                String[] values;
		
		try {
                    //first line of datafile
			br = new BufferedReader(new FileReader(filename));
        		line = br.readLine(); 
                        if(line != null)    {
				values = line.split(",");
                                if(values.length==3)  {
                                        try {
                                            this.epsilon = Double.parseDouble(values[0]);
                                            criteriaCount = Integer.parseInt(values[1]);
                                            altsCount = Integer.parseInt(values[2]);
                                        }
                                        catch(Exception e)  {
                                            throw new Exception("Wrong file format");
                                        }
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }                                    
                        }
                        else    {
                            throw new Exception("Wrong file format");
                        }
                        
                     //second line of datafile
        		line = br.readLine(); 
                        if(line != null)    {                        
				values = line.split(",");
                                if(values.length==altsCount)  {
                                        for(int j = 0; j<altsCount; j++) {
                                            try{
                                                Alternative alternative = new Alternative(values[j]);
                                                alternative.setId(j);
                                                alternatives.add(alternative);
                                            }
                                            catch(Exception e)  {
                                                throw new Exception("Wrong file format.");
                                            }
                                        }
                                }
                                else    {
                                        throw new Exception("Wrong file format");
                                }  
                        }
                        else    {
                            throw new Exception("Wrong file format");
                        }
                        
                                               
                        for(int c=0; c<criteriaCount; c++)    {
                            line = br.readLine(); 
                            if(line != null)    {
				values = line.split(",");
                                if(values.length==2)  {
                                    Criterium criterium = new Criterium(values[0]);
                                    
                                    if(values[1].contentEquals("fixMatrix"))  {
                                        fixMatrix = true;
                                    }
                                    else if(values[1].contentEquals("doNotFixMatrix")) {
                                        fixMatrix = false;
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }
                                    
                                    this.criteria.add(criterium);
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }
                            }
                            else    {
                                throw new Exception("Wrong file format");
                            }
                            
                            double[][] altsCriteriumValues = new double[altsCount][altsCount];
                            
                            for(int l=0; l<altsCount;l++) {
                                line = br.readLine(); 
                                if(line != null)    {
                                    values = line.split(",");
                                    if(values.length==altsCount)  {
                                        for(int r=0;r<altsCount;r++)    {
                                            altsCriteriumValues[l][r] = Double.parseDouble(values[r]);
                                        }
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }
                            }
                            this.addAltsCriteriumValues(altsCriteriumValues, fixMatrix);
                                                       
                        }
                        
                        line = br.readLine(); 
                        if(line != null)    {
                            values = line.split(",");
                            if(values.length==2 && values[0].contentEquals("criteriaMatrix"))  {  
                                    if(values[1].contentEquals("fixMatrix"))  {
                                        fixMatrix = true;
                                    }
                                    else if(values[1].contentEquals("doNotFixMatrix")) {
                                        fixMatrix = false;
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }
                                    
                                                                        
                                    double[][] tmpCriteriaMatrix = new double[criteriaCount][criteriaCount];
                            
                                    for(int l=0; l<criteriaCount;l++) {
                                        line = br.readLine(); 
                                        if(line != null)    {
                                            values = line.split(",");
                                            if(values.length==criteriaCount)  {
                                                for(int r=0;r<criteriaCount;r++)    {
                                                    tmpCriteriaMatrix[l][r] = Double.parseDouble(values[r]);
                                                }
                                            }
                                            else    {
                                                throw new Exception("Wrong file format");
                                            }
                                        }
                                        else    {
                                            throw new Exception("Wrong file format");
                                        }
                                    }
                                    this.setCriteriaMatrix(tmpCriteriaMatrix, fixMatrix);
                                    
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }
                            }
                            else    {
                                throw new Exception("Wrong file format");
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
     * Basic AHP class constructor.
     * The AHP object created by this constructor is empty. Epsilon parameter value is set to default (0.0001).
     */
    public AHP() {
                this.criteria = new LinkedList<Criterium>();
                this.alternatives = new LinkedList<Alternative>();
		this.altsCriteriaValues = new LinkedList<SimpleMatrix>();
                this.epsilon = 0.0001;
                this.calculated_ = false;
    }
        
     /**
     * AHP class constructor with all the important data as paramaters.
     * @param criteria LinkedList object containing Criterium objects which represent criteria in MCO problem. 
     * @param alternatives LinkedList object containing Alternatice objects which represent alternatives in MCO problem.
     * @param altsCriteriaValues LinkedList object containing SimpleMatrix objects. Each of those SimpleMatrix objects represents pairwise compar
     * ison value of two alternatives for each criterium. E.g. value(alt1,alt2) = 2.0 for first criterium means that alternative alt1 is twice as better as alternative alt2 according in terms of this criterium according to decision maker.
     * @param criteriaMatrix SimpleMatrix object containing criteria imporance pairwise comparison. E.g. criteriaMatrix(c1,c2) = 2.0 means that criterium c1 is twice as important as criterium c2 according to decision maker.
     * @param epsilon Stop condition for eigenvector calculation.
     */
    public AHP(LinkedList<Criterium> criteria, LinkedList<Alternative> alternatives, LinkedList<SimpleMatrix> altsCriteriaValues, SimpleMatrix criteriaMatrix, double epsilon) {
                this.criteria = criteria;
                this.alternatives = alternatives;
		this.altsCriteriaValues = altsCriteriaValues;
                this.criteriaMatrix = criteriaMatrix;
                this.epsilon = epsilon;
                calculated_ = false;
	}
                
     /**
     * Adds alternative to AHP object.
     * @param alternative Alternative object.
     */
    public void addAlternative(Alternative alternative)   {
            alternative.setId(alternatives.size());
            alternatives.add(alternative);
        }
        
     /**
     * Adds criterium to AHP object.
     * @param criterium Criterium object.
     */
    public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
        }
        
     /**
     * Sets AHP object criteriaMatrix to the one specified as parameter.
     * @param tmpCriteriaMatrix Two dimensional double array ontaining criteria imporance pairwise comparison. E.g. criteriaMatrix(c1,c2) = 2.0 means that criterium c1 is twice as important as criterium c2 according to decision maker.
     * @param fixMatrix Boolean flag allowing to run automatically run method whick adapts criteriaMatrix to AHP method requirements: M(i,i) = 1, if i < j M(i,j) = tmpCriteriaMatrix(i,j) and if j<i M(i,j) = 1/tmpCriteriaMatrix(j,i).	
     */
    public void setCriteriaMatrix(double[][] tmpCriteriaMatrix, boolean fixMatrix) {
         
                SimpleMatrix criteriaMatrix = new SimpleMatrix(tmpCriteriaMatrix);
		if (this.criteria.size() == criteriaMatrix.numRows() && this.criteria.size() == criteriaMatrix.numCols())   {
                   if(fixMatrix) 
                        this.criteriaMatrix = fixMatrix(criteriaMatrix);
                    else
                        this.criteriaMatrix = criteriaMatrix;
                }
                else
                    System.out.println("Criteria pairwise comparison matrix is not square or size is not correct");
	}
        
	/**
     * Adds pairwise comparson matrix of two alternatives for specific criterium. AltsCriteriumValues has to be added in the same as order as respective criteria are added.
     * @param rawAltsCriteriumValues Two dimensional double array representing pairwise comparison value of two alternatives for specific criterium. E.g. value(alt1,alt2) = 2.0 for first criterium means that alternative alt1 is twice as better as alternative alt2 according in terms of this criterium according to decision maker.
     * @param fixMatrix Boolean flag allowing to run automatically run method whick adapts altsCriteriumValuesMatrix to AHP method requirements: M(i,i) = 1, if i < j M(i,j) = tmpCriteriaMatrix(i,j) and if j<i M(i,j) = 1/tmpCriteriaMatrix(j,i).	
     */
    public void addAltsCriteriumValues(double[][] rawAltsCriteriumValues, boolean fixMatrix) {
                SimpleMatrix altsCriteriumValues = new SimpleMatrix(rawAltsCriteriumValues);
		if (alternatives.size() == altsCriteriumValues.numRows() && alternatives.size() == altsCriteriumValues.numCols())   {
                    if(alternatives.size() == altsCriteriumValues.numRows())   {
                        if(fixMatrix) 
                            altsCriteriaValues.add(fixMatrix(altsCriteriumValues));
                        else
                            altsCriteriaValues.add(altsCriteriumValues);
                    }
                    else    
                       System.out.println("Wrong matrix size."); 
                }
                else
                    System.out.println("Matrix is not square.");
	}
       
        /**
     * Sets epsilon value.
     * @param epsilon epsilon Stop condition for eigenvector calculation.
     */
    public void setEpsilon(double epsilon)  {
            this.epsilon = epsilon; 
        }
        
      /**
     * Performs AHP method calculations on data added to AHP object.
     */
    public void calculate() {
            criteriaWeights = calculateEigenVector(criteriaMatrix);
            
            for(int i=0; i<criteria.size(); i++)    {
                criteria.get(i).setWeight(criteriaWeights.get(i));
            }
           
            alternativesCriteriaValues = new SimpleMatrix(alternatives.size(), criteria.size());
            SimpleMatrix tmp;
            int colNum = 0;
            
            for(SimpleMatrix altsCriteriumValues : altsCriteriaValues)  {
                tmp = calculateEigenVector(altsCriteriumValues);
                for(int r = 0; r < alternatives.size(); r++)    {
                    alternativesCriteriaValues.set(r, colNum, tmp.get(r, 0));
                }
                colNum++;
            }
           
            alternativesValues = alternativesCriteriaValues.mult(criteriaWeights);
            
            for(int i=0; i<alternatives.size(); i++)   {
                alternatives.get(i).setScore(alternativesValues.get(i));
            }
            
            ranking = new LinkedList<Alternative>(alternatives);
            Collections.sort(ranking, new Comparator<Alternative>() {
             @Override
             public int compare(Alternative o1, Alternative o2) {
                 if(o1.getScore()<o2.getScore())    {
                     return 1;
                 }
                 else if(o1.getScore()>o2.getScore())   {
                     return -1;
                 }
                 return 0;
             }
            });
            
            calculated_ = true;
        }
        
	
	/**
     * Adapts matrix to AHP method requirements. M(i,i) = 1, if i < j M(i,j) = tmpCriteriaMatrix(i,j) and if j<i M(i,j) = 1/tmpCriteriaMatrix(j,i).
     * @param matrix Simple matrix object which will be adapted to AHP method requirements.
     * @return Adapted SimpleMatrix object.
     */
    protected SimpleMatrix fixMatrix(SimpleMatrix matrix) {
		SimpleMatrix blank = new SimpleMatrix(matrix.numRows(), matrix.numCols());
		for (int i=0; i<matrix.numRows(); i++)
			for (int j=0; j<matrix.numCols(); j++) {
				if (j == i)
					blank.set(i, j, 1);
				if (i < j)
					blank.set(i, j, matrix.get(i, j));
				if (j < i)
					blank.set(i, j, 1/matrix.get(j, i));
			}
		return blank;
	}
	
        
        
	private SimpleMatrix calculateEigenVector(SimpleMatrix matrix) {
                        SimpleMatrix tmp = matrix;
			SimpleEVD  decomp;
                        double error = 0;
                        SimpleMatrix eigenVector1 = new SimpleMatrix(matrix.numRows(),1);
                        SimpleMatrix eigenVector2 = new SimpleMatrix(matrix.numRows(),1);;
                        SimpleMatrix tmp2; 
                        do {
                            error = 0;
                            tmp = tmp.mult(tmp);
                            
                            decomp = tmp.eig();
                            tmp2 = decomp.getEigenVector(decomp.getIndexMax());
                            double sumVector = tmp2.elementSum();
                            
                            for(int i=0; i<matrix.numRows();i++)    {
                                eigenVector2.set(i,0,Math.abs(tmp2.get(i, 0))/Math.abs(sumVector));
                            }   
                            
                            for(int i=0; i<matrix.numRows();i++)    {
                                error = error + Math.abs(eigenVector2.get(i,0) - eigenVector1.get(i,0));
                            }     
                            eigenVector1 = eigenVector2;
                        } while (error>epsilon);
                        
                        return eigenVector1;
	}
        
   
      /**
     * Returns all the criteria stored in AHP object.
     * @return LinkedList containing Criterium objects.
     */
    public LinkedList<Criterium> getCriteria() {
            return criteria;
        }

      /**
     * Returns Criterium with the i order number. 
     * @param i Criterium order number.
     * @return Criterium object.
     */
    public Criterium getCriterium(int i)    {
            return criteria.get(i);
        }

        /**
     * Sets criteria in AHP object to LinkedList provided as parameter.
     * @param criteria LinkedList object containing Criterium objects.
     */
    public void setCriteria(LinkedList<Criterium> criteria) {
            this.criteria = criteria;
        }

        /**
    * Return all the alternatives stored in AHP object.
     * @return LinkedList containing Alternative objects.
     */
    public LinkedList<Alternative> getAlternatives() {
            return alternatives;
        }

        /**
     * Returns Alternative with the i order number.
     * @param i Alternative order number.
     * @return Alternative object.
     */
    public Alternative getAlternative(int i)    {
            return alternatives.get(i);
        }

        /**
     * Sets alternatives in AHP object to LinkedList provided as parameter.
     * @param alternatives LinkedList object containing Alternative objects.
     */
    public void setAlternatives(LinkedList<Alternative> alternatives) {
            this.alternatives = alternatives;
        }

        /**
     * Returns ranking - all the alternatives in AHP object ordered by their score calculated by AHP method. 
     * @return LinkedList object containing Alternative objects ordered by their AHP score. 
     */
    public LinkedList<Alternative> getRanking() {
            return ranking;
        }

        /**
     * Returns AHP score of i-th alternative.
     * @param i Alternative order number.
     * @return i-th alternative AHP score.
     */
    public double getAlternativeValue(int i)    {
            if(i<alternatives.size())   {
                return alternatives.get(i).getScore();
            }
            return 0;
        }

        /**
     * Returns AHP score of alternative alt. It has to be one of the alternatives added to the object before running calculate() method.
     * @param alt Alternative object.
     * @return AHP score of alternative object.
     */
    public double getAlternativeValue(Alternative alt)    {
            return alt.getScore();
        }        
    
        /**
     * Returns SimpleMatrix containing AHP values of all the alternatives in AHP object.
     * @return SimpleMatrix object that contains AHP values of all the alternatives in AHP object.
     */
    public SimpleMatrix getAlternativesValues() {
                if(calculated_) {
                    return alternativesValues;
                }
                return new SimpleMatrix(0,0);
	}
                
        /**
     * Returns SimpleMatrix object containing the relative value of each alternative under each criterium calculated by AHP method.
     * @return  SimpleMatrix object containing the relative value of each alternative under each criterium calculated by AHP method.
     */
    public SimpleMatrix getAlternativesCriteriaValues() {
                if(calculated_) {
                    return alternativesCriteriaValues;
                }
                return new SimpleMatrix(0,0);
	}
        
        /**
     * Returns the relative value of alternativeNum-th alternative under criteriumNum-th criterium.
     * @param alternativeNum Order number of alternative.
     * @param criterumNum Order number of criterium.
     * @return The relative value of alternativeNum-th alternative under criteriumNum-th criterium.
     */
    public double getAlternativeCriteriumValue (int alternativeNum, int criterumNum)    {
                if(calculated_) {
                    return alternativesCriteriaValues.get(alternativeNum, criterumNum);
                }
                return 0.0; 
        }
        
        /**
     * Returns SimpleMatrix object containing weight of each criterium calculated by AHP method.
     * @return SimpleMatrix object containing weight of each criterium calculated by AHP method.
     */
    public SimpleMatrix getCriteriaWeights() {
                if(calculated_) {
                    return criteriaWeights;
                }
                return new SimpleMatrix(0,0);
	}
        
        /**
     * Returns weight of criteriaNum-th criterium calculated by AHP method.
     * @param criterumNum Order number of criterium.
     * @return Weight of criteriaNum-th criterium calculated by AHP method.
     */
    public double getCriteriumWeight(int criterumNum)    {
                if(calculated_) {
                    return criteria.get(criterumNum).getWeight();
                }
                return 0.0; 
        }
        
    /**
     * Returns alternative with specific rank in ranking calculated by AHP method.
     * @param rank Rank number of wanted alternative. 
     * @return Alternative object of alternative with wanted rank.
     */
    public Alternative getAlternativeByRank(int rank)    {
        return ranking.get(rank);
    }        
    
    /**
     * Returns number of criteria in AHP object.
     * @return Number of criteria in AHP object.
     */
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    /**
     * Returns number of criteria in AHP object.
     * @return Number of criteria in AHP object.
     */
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}
