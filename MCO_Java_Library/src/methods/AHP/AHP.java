/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.AHP;

import java.util.LinkedList;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleEVD;
import methods.Criterium;
import methods.Alternative;

/**
 *
 * @author Gabriela Pastuszka, Mateusz Krasucki
 */
public class AHP {
        public LinkedList<Criterium> criteria;
        public LinkedList<Alternative> alternatives;
	
	private LinkedList<SimpleMatrix> altsCriteriaValues_; // alternatives' criteria paiwaise comparisons values matrix
	
	private SimpleMatrix criteriaMatrix_; // criteria importance pairwaise comparisons matrix      
        
        private double epsilon_;
              
        private boolean calculated_;
        
        //results:
        private SimpleMatrix criteriaWeights_; // eigenvector
        private SimpleMatrix alternativesCriteriaValues_; // matrix of eigenvectors
        private SimpleMatrix alternativesValues_; // results
        

	public AHP() {
		super();
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
		altsCriteriaValues_ = new LinkedList<SimpleMatrix>();
                epsilon_ = 0.0001;
                calculated_ = false;
	}
        
        public SimpleMatrix getAternativesValues() {
                if(calculated_) {
                    return alternativesValues_;
                }
                return new SimpleMatrix(0,0);
	}
        
        public double getAlternativeValue (int alternativeNum)    {
                if(calculated_) {
                    return alternativesValues_.get(alternativeNum);
                }
                return 0.0; 
        }
        
        public SimpleMatrix getAternativesCriteriaValues() {
                if(calculated_) {
                    return alternativesCriteriaValues_;
                }
                return new SimpleMatrix(0,0);
	}
        
        public double getAlternativeCriteriumValue (int alternativeNum, int criterumNum)    {
                if(calculated_) {
                    return alternativesCriteriaValues_.get(alternativeNum, criterumNum);
                }
                return 0.0; 
        }
        
        public SimpleMatrix getCriteriaWeights() {
                if(calculated_) {
                    return criteriaWeights_;
                }
                return new SimpleMatrix(0,0);
	}
        
        public double getCriteriumWeight(int criterumNum)    {
                if(calculated_) {
                    return criteriaWeights_.get(criterumNum);
                }
                return 0.0; 
        }
        
        public void addAlternative(Alternative alternative)   {
            alternative.id = alternatives.size();
            alternatives.add(alternative);
        }
        
        public void addCriterium(Criterium criterium)   {
            criteria.add(criterium);
        }
        
        public void setCriteriaMatrix(double[][] tmpCriteriaMatrix, boolean fixMatrix) {
         
                SimpleMatrix criteriaMatrix = new SimpleMatrix(tmpCriteriaMatrix);
		if (this.criteria.size() == criteriaMatrix.numRows() && this.criteria.size() == criteriaMatrix.numCols())   {
                   if(fixMatrix) 
                        criteriaMatrix_ = fixMatrix(criteriaMatrix);
                    else
                        criteriaMatrix_ = criteriaMatrix;
                }
                else
                    System.out.println("Criteria pairwise comparison matrix is not square or size is not correct");
	}
        
        
	public void addAltsCriteriumValues(double[][] rawAltsCriteriumValues, boolean fixMatrix) {
                SimpleMatrix altsCriteriumValues = new SimpleMatrix(rawAltsCriteriumValues);
		if (alternatives.size() == altsCriteriumValues.numRows() && alternatives.size() == altsCriteriumValues.numCols())   {
                    if(alternatives.size() == altsCriteriumValues.numRows())   {
                        if(fixMatrix) 
                            altsCriteriaValues_.add(fixMatrix(altsCriteriumValues));
                        else
                            altsCriteriaValues_.add(altsCriteriumValues);
                    }
                    else    
                       System.out.println("Wrong matrix size."); 
                }
                else
                    System.out.println("Matrix is not square.");
	}
       
        public void setEpsilon(double epsilon)  {
            epsilon_ = epsilon; 
        }
        
        public void calculate() {
            criteriaWeights_ = calculateEigenVector(criteriaMatrix_);
           
            alternativesCriteriaValues_ = new SimpleMatrix(alternatives.size(), criteria.size());
            SimpleMatrix tmp;
            int colNum = 0;
            
            for(SimpleMatrix altsCriteriumValues : altsCriteriaValues_)  {
                tmp = calculateEigenVector(altsCriteriumValues);
                for(int r = 0; r < alternatives.size(); r++)    {
                    alternativesCriteriaValues_.set(r, colNum, tmp.get(r, 0));
                }
                colNum++;
            }
           
            alternativesValues_ = alternativesCriteriaValues_.mult(criteriaWeights_);
            
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
	
        
        
	protected SimpleMatrix calculateEigenVector(SimpleMatrix matrix) {
                        SimpleMatrix tmp = matrix;
			SimpleEVD<SimpleMatrix>  decomp;
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
                        } while (error>epsilon_);
                        
                        return eigenVector1;
	}
}
