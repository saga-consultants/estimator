/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import javax.faces.component.UIInput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ListenerFor;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
 
/**
 * @author optimus prime
 * 
 * add a different background color like red to highlight invalid input fields when validation fails? 
 * With JSF 2.0, this is a trivial process, here is one way;
.ui-input-invalid {
    background-color:red
}
<h:inputText value="#{bean.property}" required="true" styleClass="#{not component.valid ? 'ui-input-invalid' : ''}" />

 
 * as page author you need to add this condition EL to every input on the application which is a cumbersome task. 
 * Luckily with JSF 2.0 you don’t need to repeat yourself much. Here is a Component system event listener that does this behind the scenes;
 * 
 */
@ListenerFor(sourceClass=javax.faces.component.html.HtmlInputText.class, systemEventClass=javax.faces.event.PostValidateEvent.class)
//@ListenerFor({ sourceClass=javax.faces.component.html.HtmlInputText.class, sourceClass=org.primefaces.component.inputmask.InputMask.class}, systemEventClass=javax.faces.event.PostValidateEvent.class)

public class PostValidationListener implements SystemEventListener {
 /************* log will always be null, and causes NPE
    @Inject FormatLogger log;
    
    @PostConstruct
    public void initPostValidationListener(){
        log.info("---loaded---");
    }*
     * 
     */
    
    public boolean isListenerForSource(Object source) {
        return true;
    }
    
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        UIInput source = (UIInput) event.getSource();
 /*
        if(!source.isValid()) {
            source.getAttributes().put("styleClass", "ui-input-invalid");
        }
   */     
        /* rescue the original style classes (styles) at first and then restore them if the component is valid again.
         * It can happen for view scoped beans e.g. if you put valid values after failed validation and send the request again         
         */        
        //source.getClientId();
 //        System.out.printf("\n--valid lisenter got %s, and it is valid? %s", source.getClientId(), source.isValid()); //NPE
        
        if(!source.isValid()) {
            String os = (String)source.getAttributes().get("styleClass");
  //          System.out.printf("\n---valid lisenter found it invalid, and old style class =%s", os);
            if(os!=null && !os.isEmpty() && !os.equalsIgnoreCase("null") && !os.equalsIgnoreCase("ui-input-invalid")){
                source.getAttributes().put("ORIG_STYLE_SET", os);
                source.getAttributes().put("styleClass", os + " ui-input-invalid");
 //               System.out.printf("\n---valid lisenter add new style class");
            }else{
                 source.getAttributes().put("styleClass", "ui-input-invalid");
  //               System.out.printf("\n---valid lisenter set new style class");
            }
        } else {
            String os = (String)source.getAttributes().get("ORIG_STYLE_SET");
 //           System.out.printf("\n---valid lisenter found it valid, and old style class =%s", os);
            if(os!=null && !os.isEmpty() && !os.equalsIgnoreCase("null")){
                if( os.equalsIgnoreCase("ui-input-invalid")){
                    source.getAttributes().put("styleClass", "");//.remove("styleClass");//.put("styleClass", "");
  //                  System.out.printf("\n---valid lisenter remove old style class which is for invalid obj");
                }else{
                    source.getAttributes().put("styleClass", os);
  //                  System.out.printf("\n---valid lisenter set back to old style class");
                }
                
            }else{// if( source.getAttributes().containsKey("styleClass") )  {
                source.getAttributes().put("styleClass", "");//.remove("styleClass");//.put("styleClass", "");
  //              System.out.printf("\n---valid lisenter remove style class");
            }
        }
    }
    
    
}

/* or define this listener in faces-config.xml:
 * <system-event-listener>
     <source-class>javax.faces.component.html.HtmlInputText</source-class>
     <system-event-class>javax.faces.event.PostValidateEvent</system-event-class>
      <system-event-listener-class>edu.lsu.estimator.PostValidationListener</system-event-listener-class>
</system-event-listener>
 * 
this solution is not work correctly for primefaces components(p:inputmask or p:autocomplete, for example). InputMask extends HtmlInputText(class in the listener)…????

 
 * since 
 * “You cannot use the @ListenerFor annotation to hook into SystemEvents from a UIComponent. 
 * This is because UIComponent implements ComponentSystemEventListener, 
 * and @ListenerFor is designed to ignore the SystemEventListener interface for any class that implements ComponentSystemEventListener.”
 */