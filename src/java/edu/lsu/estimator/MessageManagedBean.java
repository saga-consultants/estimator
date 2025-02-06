/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.lsu.estimator;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
/**
 *
 * @author 91805
 */
@ManagedBean(name="messageManagedBean")

@SessionScoped
public class MessageManagedBean {
     @Inject
    /*     */ FacesContext facesContext;
    /*     */
 /*     */    @Inject
    /*     */ Credentials credentials;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String doSomeAction(String text,String condition,String serverType){
		if(this.message!=null && this.message.equals("false") ){
                    if(serverType.equalsIgnoreCase("Master"))
                    {FacesContext.getCurrentInstance().addMessage("MessageId", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "<i>Error Message Displayed</i>","<i>Error Message Displayed</i>"));		
                    }
                    else 
                    {
                        FacesContext.getCurrentInstance().addMessage("MessageId", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "<i>Error Message Displayed</i>","<i>Error Message Displayed</i>"));	
                    }
                }	
                else
                {
                    if(serverType.equalsIgnoreCase("Master")){
                     FacesContext.getCurrentInstance().addMessage("MessageId", 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "<i>Master is up</i>","Master is up"));	
                    }
                   
                }
		return ""	;
	}
}