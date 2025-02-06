package edu.lsu.estimator.tdo;

import java.io.Serializable;
import java.util.Map;
import javax.crypto.SecretKey;

public class TDOEstimatorReq implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public Map<String, String> c_info;
  
  public SecretKey c_key;
  
  public byte[] aesData;
  
  public int fisy;
  
  public int clientid;
  
  public int counselorid;
  
  public long clientclock;
  
  public String clienttz;
  
  public String clientversions;
  
  public long last_stud_dod;
  
  public long last_prt_dod;
  
  public String clientaddress;
  
  public String clientjava;
  
  public String clienthostname;
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\tdo\TDOEstimatorReq.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */