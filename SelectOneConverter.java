/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


/**
 *
 * @author kwang
 */
@FacesConverter("lasu.selectone.converter")
public class SelectOneConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        //throw new UnsupportedOperationException("Not supported yet.");
        try {
            //int id = Integer.parseInt(value);             
            return Integer.valueOf(value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return ((Integer) value).toString();
    }
    
}
