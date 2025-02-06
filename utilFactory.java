package edu.lsu.estimator;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import java.io.Serializable;

//import org.apache.log4j.Logger;
//import org.apache.log4j.Level;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kwang
 */

public class utilFactory implements  Serializable {
    private static final long serialVersionUID = 1L;
    
    //@Produces Logger createLogger(InjectionPoint injectionPoint){
    @Produces  FormatLogger createLogger(InjectionPoint injectionPoint){
        //return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
       // return new FormatLogger( Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName()) );
       
        //return new FormatLogger( Logger.getLogger(injectionPoint.getMember().getDeclaringClass()));
        return null;
    }
    
    @Produces @RequestScoped  // FacesContext does not implement Seriasiable, so no @SessionScoped  or higher scope
    public FacesContext getFacesContext() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null)
         throw new ContextNotActiveException("FacesContext is not active");
        return ctx;
    }
    
    
}
