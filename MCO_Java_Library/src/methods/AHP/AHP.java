/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.AHP;

import java.util.LinkedList;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleEVD;


/**
 *
 * @author Gabriela Pastuszka, Mateusz Krasucki
 */
public class AHP {
	
	private LinkedList<SimpleMatrix> altsCriteriaValues_; // alternatives' criteria paiwaise comparisons values matrix
	
	private SimpleMatrix criteriaMatrix_; // criteria importance pairwaise comparisons matrix
        
	private SimpleMatrix criteriaRanking_; // eigenvector
        private SimpleMatrix alternativesValue_; // matrix of eigenvectors
        
        
        private double epsilon_;
        
        private int criteriaNumber_;
        private int altsNumber_;
        

	public AHP() {
		super();
		altsCriteriaValues_ = new LinkedList<SimpleMatrix>();
                epsilon_ = 0.0001;
                criteriaNumber_= 0;
                altsNumber_ = 0;
		// TODO Auto-generated constructor stub
	}
        
        public void setCriteriaMatrix(SimpleMatrix criteriaMatrix, boolean fixMatrix) {
		if (criteriaMatrix.numRows() == criteriaMatrix.numCols())   {
                    criteriaNumber_ = criteriaMatrix.numRows();
                    if(fixMatrix) 
                        criteriaMatrix_ = fixMatrix(criteriaMatrix);
                    else
                        criteriaMatrix_ = criteriaMatrix;
                }
                else
                    System.out.println("Criteria pairwise comparison matrix is not square.");
	}
        
        
	public void addCriterium(SimpleMatrix altsCriteriumValues, boolean fixMatrix) {
		if (altsCriteriumValues.numRows() == altsCriteriumValues.numCols())   {
                    if(altsNumber_== 0)  {
                        altsNumber_ = altsCriteriumValues.numRows();
                    }
                    else if(altsNumber_ == altsCriteriumValues.numRows())   {
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
            criteriaRanking_ = calculateEigenVector(criteriaMatrix_);
            System.out.println(criteriaRanking_);
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
                        System.out.println(eigenVector1);
                        System.out.println(error);
                        } while (error>epsilon_);
                        
                        return eigenVector1;
	}
}
