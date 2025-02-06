/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

/**
 *
 * @author kwang
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
@ManagedBean
@SessionScoped
public class TabBB {
    
    private int tabIndex = 1;
    
    public boolean handleTabChange() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String index = externalContext.getRequestParameterMap().get("tabIndex");
        setTabIndex(Integer.parseInt(index));
        return true;
    }
    
    public int getTabIndex() {
        return tabIndex;
    }
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}