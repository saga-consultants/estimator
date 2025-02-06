/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kwang
 */
public class SplitReplaceTester {
    public SplitReplaceTester(){}
    
    public static void  main(String[] args){
        
    }
    
    private void test(){
        String str="";
        List<String> std_merits = null;
        
        String x = str;
            if( x!=null && !x.isEmpty()){               
                x = x.replaceAll(", ",  ",");
                std_merits = Arrays.asList(x.split(","));
            }else{
                //keep merits as null or empty?
                if( std_merits==null)std_merits = new ArrayList<String>();
            }
    }
}
/*
 
    public List<String> getStd_merits() {
        if(std_merits==null || std_merits.isEmpty()){
            String x = stud.getStudentSMerit();
            if( x!=null && !x.isEmpty()){               
                x = x.replaceAll(", ",  ",");
                std_merits = Arrays.asList(x.split(","));
            }else{
                //keep merits as null or empty?
                if( std_merits==null)std_merits = new ArrayList<String>();
            }
        }
        return std_merits;
    }

    public void setStd_merits(List<String> std_merits) {
        log.info(" setting merits as >>%s<<", std_merits==null?"":std_merits.toString());
        this.std_merits = std_merits;
        if( std_merits==null || std_merits.size()==0){
            stud.setStudentSMerit("");
        }else{
            String[] x = (String[]) std_merits.toArray(new String[0]);
            
            //max 50
            String max = Arrays.toString(x);            
            int len = max.length();
            if( len>=2){ //get rid of '[' and ']'
                max = max.substring(1, len-1);
                len -=2;
            }
            log.info(" transfered merits to string >>%s<<, length==%d", max, len);
            
            len = len>50? 50:len;
            stud.setStudentSMerit( max.substring(0, len) );
            
        }
        log.info(" double check stud obj merits string >>%s<<", stud.getStudentSMerit());
    }

 */