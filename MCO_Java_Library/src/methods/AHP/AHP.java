package methods.AHP;

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
