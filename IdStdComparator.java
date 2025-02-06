/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import java.util.Comparator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author kwang
 */
@ApplicationScoped
@Named("IdStdComparator")
public class IdStdComparator implements Comparator<Student> {
    
  @Inject AppReference ref;   
    
  @Override
  public int compare(Student std1, Student std2){
            //(Car car1, Car car2) {	
    //return -1, 0 , 1 if car1 is less than, equal to or greater than car2
        int res=0;
        //studentALsuid
        String id1 = std1.getStudentALsuid();
        id1 = ref.isEmp(id1)? "null" : id1.trim();//.toUpperCase();
        String id2 = std2.getStudentALsuid();
        id2 = ref.isEmp(id2)? "null" : id2.trim();
        
        res = id1.compareToIgnoreCase(id2); 
        if( res==0){
            //one.ddom gt 0 ? one.ddom: one.ddoe
            long dom1 = std1.getDdom();
            long doe1 = std1.getDdoe();
            long dom2 = std1.getDdom();
            long doe2 = std1.getDdoe();
            long last1 = dom1 >0 ? dom1 : doe1;
            long last2 = dom2 >0 ? dom2 : doe2;
            res = (int)(last1 - last2);
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
