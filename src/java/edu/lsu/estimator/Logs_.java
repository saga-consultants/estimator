package edu.lsu.estimator;

import edu.lsu.estimator.Logs;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Logs.class)
public class Logs_ {
  public static volatile SingularAttribute<Logs, String> result;
  
  public static volatile SingularAttribute<Logs, Date> whattime;
  
  public static volatile SingularAttribute<Logs, String> what;
  
  public static volatile SingularAttribute<Logs, String> cat;
  
  public static volatile SingularAttribute<Logs, String> location;
  
  public static volatile SingularAttribute<Logs, String> type;
  
  public static volatile SingularAttribute<Logs, String> who;
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Logs_.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */