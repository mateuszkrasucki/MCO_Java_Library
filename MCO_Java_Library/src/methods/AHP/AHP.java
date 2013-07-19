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
 *
 * @author Gabriela Pastuszka, Mateusz Krasucki
 */
public class AHP {
        private LinkedList<Criterium> criteria;
        private LinkedList<Alternative> alternatives;
        private LinkedList<Alternative> ranking;
	
	private LinkedList<SimpleMatrix> altsCriteriaValues; // alternatives' criteria pariwaise comparisons values matrix
	
	private SimpleMatrix criteriaMatrix; // criteria importance pairwaise comparisons matrix      
        
        private double epsilon;
              
        
        private boolean calculated_;
        
        //results:
        private SimpleMatrix criteriaWeights; // eigenvector
        private SimpleMatrix alternativesCriteriaValues; // matrix of eigenvectors
        private SimpleMatrix alternativesValues; // results
        
        /**
	* 
	* Cosntructor with data read from file
	* @param filename Filename where data can be read from. It should be structured as csv file in dataFileExamples/ahp.csv
        * In the first line there should be epsilon value followed by criteria number and alternatives number. In the second line you should place alternatives' names. 
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
	
        

	public AHP() {
                this.criteria = new LinkedList<Criterium>();
                this.alternatives = new LinkedList<Alternative>();
		this.altsCriteriaValues = new LinkedList<SimpleMatrix>();
                this.epsilon = 0.0001;
                this.calculated_ = false;
	}
        
        public AHP(LinkedList<Criterium> criteria, LinkedList<Alternative> alternatives, LinkedList<SimpleMatrix> altsCriteriaValues, SimpleMatrix criteriaValues, double epsilon) {
                this.criteria = criteria;
                this.alternatives = alternatives;
		this.altsCriteriaValues = altsCriteriaValues;
                this.criteriaMatrix = criteriaMatrix;
                this.epsilon = epsilon;
                calculated_ = false;
	}
                
        public void addAlternative(Alternative alternative)   {
            alternative.setId(alternatives.size()+1);
            alternatives.add(alternative);
        }
        
        public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
        }
        
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
       
        public void setEpsilon(double epsilon)  {
            this.epsilon = epsilon; 
        }
        
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
        
   
        public LinkedList<Criterium> getCriteria() {
            return criteria;
        }

        public Criterium getCriterium(int i)    {
            return criteria.get(i);
        }

        public void setCriteria(LinkedList<Criterium> criteria) {
            this.criteria = criteria;
        }

        public LinkedList<Alternative> getAlternatives() {
            return alternatives;
        }

        public Alternative getAlternative(int i)    {
            return alternatives.get(i);
        }

        public void setAlternatives(LinkedList<Alternative> alternatives) {
            this.alternatives = alternatives;
        }

        public LinkedList<Alternative> getRanking() {
            return ranking;
        }

        public double getAlternativeValue(int i)    {
            if(i<alternatives.size())   {
                return alternatives.get(i).getScore();
            }
            return 0;
        }

        public double getAlternativeValue(Alternative alt)    {
            return alt.getScore();
        }        
    
        public SimpleMatrix getAlternativesValues() {
                if(calculated_) {
                    return alternativesValues;
                }
                return new SimpleMatrix(0,0);
	}
                
        public SimpleMatrix getAlternativesCriteriaValues() {
                if(calculated_) {
                    return alternativesCriteriaValues;
                }
                return new SimpleMatrix(0,0);
	}
        
        public double getAlternativeCriteriumValue (int alternativeNum, int criterumNum)    {
                if(calculated_) {
                    return alternativesCriteriaValues.get(alternativeNum, criterumNum);
                }
                return 0.0; 
        }
        
        public SimpleMatrix getCriteriaWeights() {
                if(calculated_) {
                    return criteriaWeights;
                }
                return new SimpleMatrix(0,0);
	}
        
        public double getCriteriumWeight(int criterumNum)    {
                if(calculated_) {
                    return criteria.get(criterumNum).getWeight();
                }
                return 0.0; 
        }
        
    public Alternative getAlternativeByRank(int rank)    {
        return ranking.get(rank);
    }        
    
    public int getCriteriaNum() {
        return this.criteria.size();
    }
    
    public int getAlternativesNum() {
        return this.alternatives.size();
    }
}
