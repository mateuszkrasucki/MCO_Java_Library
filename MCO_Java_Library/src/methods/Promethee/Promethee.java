package methods.Promethee;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import org.ejml.simple.SimpleMatrix;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Mateusz Krasucki
 */
public abstract class Promethee {
    protected LinkedList<Criterium> criteria;
    protected LinkedList<Alternative> alternatives;
    protected LinkedList<Constraint> constraints;
    
    protected LinkedList<Alternative> ranking;
    protected LinkedList<Alternative> alternativesBestSet;
    protected double bestSetMPF;
    
    protected SimpleMatrix mpd;
    
        /**
	* 
	* Cosntructor with data read from file
	* @param filename Filename where data can be read from. It should be structured as shown in example csv file in dataFileExamples/promethee.csv
	*/
	public Promethee(String filename) {	
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                constraints = new LinkedList<Constraint>();
                ranking = new LinkedList<Alternative>();
                
                alternativesBestSet = new LinkedList<Alternative>();
                bestSetMPF = 0;
                
			
		BufferedReader br = null;
		String line = "";
		int altsCount = 0;
                int altsIterationCount = 0;
                int criteriaCount = 0;
                Map<String, Criterium> criteriaByName = new HashMap<String, Criterium>();
                
                String[] values;
		
		try {
                    br = new BufferedReader(new FileReader(filename));
                    line = br.readLine(); 
                    while(line != null)    {
                        values = line.split(",");
                        if(values[0].contentEquals("Criterium") && values.length>=5 && altsCount == 0)   {
                                PreferenceFunction preferenceFunction = new LinearPreferenceFunction();
                                if(values[4].contentEquals("LINEAR")) {                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new LinearPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else if(values.length == 7) {
                                        preferenceFunction = new LinearPreferenceFunction(Double.parseDouble(values[5]),Double.parseDouble(values[6]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else if(values[4].contentEquals("THRESHOLD")) {
                                    preferenceFunction = new ThresholdPreferenceFunction();
                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new ThresholdPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else if(values[4].contentEquals("GAUSSIAN")) {
                                    preferenceFunction = new GaussianPreferenceFunction();
                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new GaussianPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else if(values[4].contentEquals("LEVEL")) {
                                    preferenceFunction = new LevelPreferenceFunction();
                                    
                                    if(values.length == 6) {
                                        preferenceFunction = new LevelPreferenceFunction(Double.parseDouble(values[5]));
                                    }
                                    else if(values.length == 7) {
                                        preferenceFunction = new LevelPreferenceFunction(Double.parseDouble(values[5]),Double.parseDouble(values[6]));
                                    }
                                    else  if(values.length!=5)  {
                                        throw new Exception("Wrong file format");
                                    }
                                }
                                else    {
                                    throw new Exception("Wrong file format");
                                }

                                    Criterium criterium;
                                    if(values[3].contentEquals("MAX"))  {
                                        criterium = new Criterium(values[1],Criterium.Direction.MAX,Double.parseDouble(values[2]),preferenceFunction);
                                    }
                                    else if(values[3].contentEquals("MIN"))  {
                                        criterium = new Criterium(values[1],Criterium.Direction.MIN,Double.parseDouble(values[2]),preferenceFunction);
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }
                                    this.addCriterium(criterium);
                                    criteriaByName.put(values[1], criterium);  
                                    criteriaCount++;
                        }
                        else if(values[0].contentEquals("Constraint") && values.length == 4 && altsCount == 0 && criteriaCount!=0)   {
                                    if(criteriaByName.containsKey(values[1]))   {
                                        Criterium criterium = criteriaByName.get(values[1]);
                                        if(values[2].contentEquals("UPPER"))   {
                                            Constraint constraint = new Constraint(criterium, Constraint.ConstrainType.UPPER, Double.parseDouble(values[3]));
                                            this.addConstraint(constraint);
                                        }
                                        else if(values[2].contentEquals("LOWER"))   {
                                            Constraint constraint = new Constraint(criterium, Constraint.ConstrainType.LOWER, Double.parseDouble(values[3]));
                                            this.addConstraint(constraint);
                                        }
                                        else    {
                                                throw new Exception("Wrong file format");
                                        }
                                    }
                                    else    {
                                        throw new Exception("Wrong file format");
                                    }        
                        }
                        else if (values[0].contentEquals("Alternatives") && altsCount == 0 && criteriaCount != 0)   {                          
                            for(int i=1; i<values.length;i++)   {
                                Alternative alternative = new Alternative(values[i]);
                                this.addAlternative(alternative);
                                altsCount++;
                            }
                        } 
                        else if(altsCount!=0 && criteriaCount!=0 && altsIterationCount < altsCount && values.length == criteriaCount)   {
                            for(int i=0; i<values.length; i++)  {
                                alternatives.get(altsIterationCount).addCriteriumValue(Double.parseDouble(values[i]));
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
    
    public Promethee() {
                criteria = new LinkedList<Criterium>();
                alternatives = new LinkedList<Alternative>();
                constraints = new LinkedList<Constraint>();
                ranking = new LinkedList<Alternative>();
                
                alternativesBestSet = new LinkedList<Alternative>();
                bestSetMPF = 0;
    }
    
    public void addCriterium(Criterium criterium)   {
        criteria.add(criterium);
    }
    
    
    public void addAlternative(Alternative alternative)   {
        alternative.setId(alternatives.size()+1);
        alternatives.add(alternative);
    }

    protected void addConstraint(Constraint constraint)  {
        if(criteria.indexOf(constraint.getCriterium())!=-1)  {
            constraints.add(constraint);
        }
    }
    
    public void normalizeWeights()  {
        double sum = 0;
        for(int i=0; i<criteria.size();i++) {
            sum = sum + criteria.get(i).getWeight();
        }
        for(int i=0; i<criteria.size();i++) {
            criteria.get(i).setWeight(criteria.get(i).getWeight()/sum);
        }
    }   
    
    public abstract void calculate();
    
   
    protected void calculateMPD()  {
        mpd = new SimpleMatrix(this.getAlternativesNum(),this.getAlternativesNum());
        mpd.zero();

        for(int i = 0; i<this.getAlternativesNum(); i++)  {
            for(int j=0; j<this.getAlternativesNum(); j++)    {
                if(i!=j)    {
                    for(int r=0; r<this.getCriteriaNum(); r++)    {
                        double pd = criteria.get(r).calculatePreference(alternatives.get(i).getCriteriumValue(r), alternatives.get(j).getCriteriumValue(r));
                        mpd.set(i, j, mpd.get(i, j) + criteria.get(r).getWeight() * pd);
                    }
                }
            }
        }
    }
    
    protected void calculateMPF()  {
        SimpleMatrix mpf_plus = new SimpleMatrix(this.getAlternativesNum(),1);
        mpf_plus.zero();
        SimpleMatrix mpf_minus = new SimpleMatrix(this.getAlternativesNum(),1);
        mpf_minus.zero();

        for(int i=0; i<this.getAlternativesNum();i++)    {
            for(int j=0; j<this.getAlternativesNum(); j++)   {
                if(i!=j)    {
                    mpf_plus.set(i, 0, mpf_plus.get(i, 0) + mpd.get(i, j));
                    mpf_minus.set(i, 0, mpf_minus.get(i, 0) + mpd.get(j, i));
                }
            }
            mpf_plus.set(i,0,mpf_plus.get(i,0)/(this.getAlternativesNum()-1));
            mpf_minus.set(i,0,mpf_minus.get(i,0)/(this.getAlternativesNum()-1));
            
            alternatives.get(i).setMpfPlus(mpf_plus.get(i, 0));
            alternatives.get(i).setMpfMinus(mpf_minus.get(i, 0));
            alternatives.get(i).setMpf(alternatives.get(i).getMpfPlus() -  alternatives.get(i).getMpfMinus());
        }
        
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
            return alternatives.get(i).getMpf();
        }
        return 0;
    }
    
    public double getAlternativeValue(Alternative alt)    {
        return alt.getMpf();
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
