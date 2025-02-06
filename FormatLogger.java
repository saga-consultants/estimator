package edu.lsu.estimator;

import ch.qos.logback.classic.Level;
import com.kingombo.slf5j.Logger;

import java.io.Serializable;
//import javassist.tools.rmi.ObjectNotFoundException;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kwang
 */

public class FormatLogger  implements Serializable{ //WELD-000054 Producers cannot produce non-serializable instances for injection into non-transient fields of passivating beans
    private   Logger log; //static ???
    private   String srcClass;
    //transient obj will not be serialized. but when the container obj get deserilzed, the child obj will be null. so you shall use readObject() to reset or asign a new instance of that obj
    /*
    private void readObject(ObjectInputStream in) throws ObjectNotFoundException, ClassNotFoundException{
        log = Logger.getLogger(null);
        in.defaultReadObject();
    }*/
    
    public FormatLogger( Logger log){
        this.log = log;
    }
    public FormatLogger( Logger log, String ClassName){
        this.log = log;
        this.srcClass = ClassName;
    }
    
    public void debug(String formatter, Object... args)  {
    //if( log.isDebugEnabled()){
        log(Level.DEBUG, formatter, args);
    //}
      }
    
      public void info(String formatter, Object... args)  {
         log(Level.INFO, formatter, args);
      }
      public void warn(String formatter, Object... args)  {
         log(Level.WARN, formatter, args);
      }
      public void err(String formatter, Object... args)  {
         log(Level.ERROR, formatter, args);
      }
      /*
      public void fatal(String formatter, Object... args)  {
         log(Level.FATAL, formatter, args);
      }*/


      //? &c. for info, warn; also add overloads to log an exception ?

      public void log(Level level, String formatter, Object... args)  {
        //if (log.isEnabledFor(level)) {
          /*
           * Only now is the message constructed, and each "arg"
           * evaluated by having its toString() method invoked.
           */
       //   log.log(level, String.format(formatter, args));
        //}
      } 
}
