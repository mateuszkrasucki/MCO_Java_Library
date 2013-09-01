package methods.Electre;


public class Criterium extends methods.BasicTypes.Criterium{
	/**
     * Enum type describing criterium optimization direction (if criterium value should be minimized or maximalized).
     */
    public enum Direction {
		/**
         * Criterium value should be minimized.
         */
        MIN,
            /**
             * Criterium value should be maximized. 
             */
            MAX;
	}
    /**
     * Criterium optimization direction. 
     */
	private Criterium.Direction direction_;   
	

    /**
     * Criterium optimization veto. 
     */
	private Double veto_;   
    /**
     * Criterium preference threshold
     */
	private Double p_;   
    /**
     * Criterium indifference threshold. 
     */
	private Double q_;   
	
    public Double getVeto() {
		return veto_;
	}

	public void setVeto_(Double veto) {
		this.veto_ = veto;
	}

	/**
     * Basic constructor.
     * Sets every parameter to default - weight equals 1, criterium will be maximized, linear preference function will be applied.
     */
    public Criterium() {
		super();
                this.direction_ = Direction.MAX;
    }
    
     /**
      * Criterium class constructor with criterium name and criterium weight value as parameters.
      * @param name Criterium name.
      * @param weight Criterium weight.
      */
     public Criterium(String name, double weight) {
         super(name,weight);
  		this.direction_ = Direction.MAX;
     }    
          

     /**
      * Criterium class constructor with criterium name and criterium weight value as parameters.
      * @param name Criterium name.
      * @param weight Criterium weight.
      */
     public Criterium(String name, double weight, Direction direction, double veto, double p, double q) {
         super(name,weight);
         this.direction_ = direction;
         this.veto_ = veto;
         this.p_ = p;
         this.q_ = q;
     }    

     public Double getP() {
		return p_;
	}

	public Double getQ() {
		return q_;
	}

	public void setP_(Double p_) {
		this.p_ = p_;
	}

	public void setQ_(Double q_) {
		this.q_ = q_;
	}

	/**
      * Returns optimization direction of the criterium. 
      * @return Optimization direction of the criterium.
      */
     public Direction getDirection() {
         return direction_;
     }

     /**
      * Sets optimization direction of the criterium to the one provided as parameter.
      * @param direction New optimization direction of the criterium.
      */
     public void setDirection(Direction direction) {
         this.direction_ = direction;
     }   

}
