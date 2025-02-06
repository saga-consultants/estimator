/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author kwang
 * java.io.NotSerializableException: edu.lsu.estimator.QueryStudModel
 */
public class QueryStudModel extends ListDataModel<Student> implements SelectableDataModel<Student>, Serializable{ //java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to edu.lsu.estimator.Student
	//at edu.lsu.estimator.QueryStudModel.getRowKey(QueryStudModel.java:15)
    private static final long serialVersionUID = 1L;
    
    public QueryStudModel(){}
    
    public QueryStudModel(List<Student> list) {
        super(list);
        this.setWrappedData(list);
    }    

 //   @Override
    public Student getRowData(String rowKey) {
        List<Student> students = (List<Student>)this.getWrappedData();
        StringBuilder sb = new StringBuilder(128);
        for(Student student : students){
            sb.setLength(0);
            sb.append(student.getStudentFisy()).append('.').append(student.getCounselorId()).append('.').append(student.getStudentNumb());
            if(sb.toString().equalsIgnoreCase(rowKey)){
                return student;
            }
        }        
        return null;
    }

 //   @Override
    public Object getRowKey(Student stud) { //ClassCastException: [Ljava.lang.Object; cannot be cast to edu.lsu.estimator.Student
        StringBuilder sb = new StringBuilder(128);
        return sb.append(stud.getStudentFisy()).append('.').append(stud.getCounselorId()).append('.').append(stud.getStudentNumb()).toString();
        //return object.getCounselorId();
    }
    
    public int isAliveFilled(){        
        if( this.isRowAvailable()) return 1; //only true if it is filled and rowIndex falss in [0, size()]
        
        List<Student> students = (List<Student>)this.getWrappedData();
        if( students==null ) return -1;
        else return students.size();        
    }
}
