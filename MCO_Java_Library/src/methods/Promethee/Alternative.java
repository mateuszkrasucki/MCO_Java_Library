package methods.Promethee;

import java.util.LinkedList;

/**
 * Alternative class specific to Promethee method class.
 * Extends methods.BasicTypes.Alternative.
 * @author Mateusz Krasucki
 * @see methods.BasicTypes.Alternative
 */
public class Alternative extends methods.BasicTypes.Alternative {
    /**
     * Alternatvie positive multicriteria preference flow calculated by Promethee family method.
     */
        private double mpfPlus;
    /**
     * Alternatvie negative multicriteria preference flow calculated by Promethee family method.
     */
        private double mpfMinus;
    /**
     * Alternatvie net multicriteria preference flow calculated by Promethee family method.
     */
        private double mpf;
        
	/**
     * Basic constructor of Promethee Alternative class.
     */
    public Alternative() {
		super();
                mpfPlus = 0;
                mpfMinus = 0;
                mpf = 0;
	}
        
       
	/**
     * Promethee Alternative class constructor with alternative name as parameter.
     * @param name Alternative name.
     */
    public Alternative(String name) {
		super(name);
                mpfPlus = 0;
                mpfMinus = 0;
                mpf = 0;
	}
        
	/**
     * Promethee Alternative class constructor with alternative name and criteria values as parameter.
     * @param name Alternative name.
     * @param criteriaValues LinkedList containing criteria values.
     */
    public Alternative(String name, LinkedList<Double> criteriaValues) {
		super(name, criteriaValues);
                mpfPlus = 0;
                mpfMinus = 0;
                mpf = 0;
	}

         /**
     * Returns alternative MPF+ score calcualted by Promethee (if calculated, if not it always equals )
     * @return Alternative MPF+ score calcualted by Promethee 
     */
    public double getMpfPlus() {
            return mpfPlus;
        }

        /**
     * Sets alternative MPF+ score to value provided as parameter, protected becaue it is meant to be used only by Promethee class.
     * @param mpfPlus Alternative MPF+ score.
     */
    protected void setMpfPlus(double mpfPlus) {
            this.mpfPlus = mpfPlus;
        }

         /**
     * Returns alternative MPF- score calcualted by Promethee (if calculated, if not it always equals )
     * @return Alternative MPF- score calcualted by Promethee 
     */
    public double getMpfMinus() {
            return mpfMinus;
        }

        /**
     * Sets alternative MPF- score to value provided as parameter, protected becaue it is meant to be used only by Promethee class.
     * @param mpfMinus Alternative MPF- score.
     */
    protected void setMpfMinus(double mpfMinus) {
            this.mpfMinus = mpfMinus;
        }

         /**
     * Returns alternative MPF score calcualted by Promethee (if calculated, if not it always equals )
     * @return Alternative MPF score calcualted by Promethee 
     */
    public double getMpf() {
            return mpf;
        }

        /**
     * Sets alternative MPF score to value provided as parameter, protected becaue it is meant to be used only by Promethee class.
     * @param mpf Alternative MPF score.
     */
    protected void setMpf(double mpf) {
            this.mpf = mpf;
        }
        
        
    
}
