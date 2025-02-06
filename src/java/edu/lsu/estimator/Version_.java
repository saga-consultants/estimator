package edu.lsu.estimator;

import edu.lsu.estimator.Version;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Version.class)
public class Version_ {
  public static volatile SingularAttribute<Version, String> srcTz;
  
  public static volatile SingularAttribute<Version, String> module;
  
  public static volatile SingularAttribute<Version, Integer> srcWho;
  
  public static volatile SingularAttribute<Version, Long> dos;
  
  public static volatile SingularAttribute<Version, String> dostz;
  
  public static volatile SingularAttribute<Version, Integer> effInd;
  
  public static volatile SingularAttribute<Version, Integer> version;
  
  public static volatile SingularAttribute<Version, Integer> recid;
  
  public static volatile SingularAttribute<Version, Long> srcTime;
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Version_.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */