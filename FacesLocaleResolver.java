package edu.lsu.estimator;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Locale;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kwang
 */

public class FacesLocaleResolver {
   @Inject
   FacesContext facesContext;

   public boolean isActive() {
      return (facesContext != null) && (facesContext.getCurrentPhaseId() != null);
   }

   @Produces //@Faces
   public Locale getLocale() {
      if (facesContext.getViewRoot() != null) 
         return facesContext.getViewRoot().getLocale();
      else
         return facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
   }
}
