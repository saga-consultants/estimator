package edu.lsu.estimator;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kwang
 */
public class ResourceBundleProducer {
  @Inject       
  public Locale locale;

  @Inject       
  public FacesContext facesContext;

  @Produces
  public ResourceBundle getResourceBundle() {
    return ResourceBundle.getBundle("/JSFmessages", facesContext.getViewRoot().getLocale() ); // what is defined in faces-config.xml <resource-bundle> <base-name>
  }
}


/* usage:
 * 
 * @Inject
  private transient Resourcebundle bundle;

  public void testMethod() {
    bundle.getString("SPECIFIC_BUNDLE_KEY");
  }

 */