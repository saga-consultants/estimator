/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

/**
 *
 * @author kwang
 */

@Named("credentials")
@RequestScoped
public class Credentials  {
    private String username   ="";
    private String password   ="";
    
    @NotNull @Length(min=3, max=64)
    @NotEmpty(message="{testBVmsg}")
    public String getUsername() { return username; }
    
    public void setUsername(String username) { this.username = username; }
    
    @NotNull @Length(min=3, max=64)
    @NotEmpty
    public String getPassword() { return password; }
    
    public void setPassword(String password) { this.password = password; }
        
}
