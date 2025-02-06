package edu.lsu.estimator.tdo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TDOFund implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public Integer fundId;
  
  public String fundCode;
  
  public String fundDesc;
  
  public String reqNoteInd;
  
  public String hasMatching;
  
  public BigDecimal matchPerc;
  
  public Integer matchTop;
  
  public String instCapExcept;
  
  public int priority;
  
  public String status;
  
  public Integer creator;
  
  public Integer updator;
  
  public Integer syncor;
  
  public long doe;
  
  public String doetz;
  
  public long dom;
  
  public String domtz;
  
  public long dos;
  
  public String dostz;
  
  public Integer auto_ind;
  
  public Integer max_ind;
  
  public String earningsMatch;
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\tdo\TDOFund.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */