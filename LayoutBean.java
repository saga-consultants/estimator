/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author kwang
 * WELD-000072 Managed bean declaring a passivating scope must be passivation capable.  Bean:  Managed Bean [class edu.lsu.estimator.LayoutBean] with qualifiers [@Any @Default @Named].
 */
@Named @SessionScoped
public class LayoutBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String theme = "aristo";
    
    public LayoutBean(){}
    
    public String getTheme()    {
        return theme;
    }
}

//this bean can be used in any xhtml/jsf page/template as:
//<link rel="stylesheet" href="#{request.contextPath}/themes/#{layoutBean.theme}/skin.css" />
// inside <h:head>

//or in web.xml
/*<context-param>   
  <param-name>primefaces.THEME</param-name>
  <param-value>aristo</param-value> 
</context-param> 
 */