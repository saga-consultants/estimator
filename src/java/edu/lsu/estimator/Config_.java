package edu.lsu.estimator;

import edu.lsu.estimator.Config;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Config.class)
public class Config_ {
  public static volatile SingularAttribute<Config, Short> pingInterval;
  
  public static volatile SingularAttribute<Config, Short> enabledInd;
  
  public static volatile SingularAttribute<Config, Short> clientFscy;
  
  public static volatile SingularAttribute<Config, String> masterport;
  
  public static volatile SingularAttribute<Config, Short> remoteDisableLogin;
  
  public static volatile SingularAttribute<Config, Integer> clientid;
  
  public static volatile SingularAttribute<Config, String> masterVersion;
  
  public static volatile SingularAttribute<Config, String> clientVersion;
  
  public static volatile SingularAttribute<Config, String> mastername;
  
  public static volatile SingularAttribute<Config, String> ldapserver;
  
  public static volatile SingularAttribute<Config, Integer> upgReqInd;
  
  public static volatile SingularAttribute<Config, Short> liveMode;
  
  public static volatile SingularAttribute<Config, Short> pollInterval;
  
  public static volatile SingularAttribute<Config, String> masterurl;
  
  public static volatile SingularAttribute<Config, String> ldapsurl;
  
  public static volatile SingularAttribute<Config, String> masteripv4;
  
  public static volatile SingularAttribute<Config, Integer> ldapport;
  
  public static volatile SingularAttribute<Config, String> masterecho;
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Config_.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */