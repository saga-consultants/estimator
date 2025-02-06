/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Comparator;

/**
 *
 * @author kwang
 */
@ApplicationScoped
@Named("IdComparator")
public class IdComparator implements Comparator<String> {
    
  @Inject AppReference ref;   
    
  @Override
  public int compare(String id1, String id2){
            //(Car car1, Car car2) {	
    //return -1, 0 , 1 if car1 is less than, equal to or greater than car2
        int res=0;
        //studentALsuid
        
        id1 = ref.isEmp(id1)? "null" : id1.trim();//.toUpperCase();        
        id2 = ref.isEmp(id2)? "null" : id2.trim();
        
        //set null as least
        if(id1.equals("null") && !id2.equals("null") ){
            res = -1;
        }else if (!id1.equals("null") && id2.equals("null")){
            res = 1;
        }else{        
            res = id1.compareToIgnoreCase(id2); 
            if( res!=0) res = res>0? 1: -1;
        }       
        return res;
    }  
    
      
 /*
  public int compare(Student c1, Student c2) {  
    if (c1.getAge() == c2.getAge()) {
      if (c1.getSurname().compareTo(c2.getSurname()) == 0) {
          return c1.getForename().compareTo(c2.getForename()) {
      } else {
          return c1.getSurname().compareTo(c2.getSurname());
      }
    } else if (c1.getAge() > b2.getAge()) {
        return -1;
    } else {
        return 1;
    }
  }*/
  
  
}
