/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;


import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import java.util.List;
import edu.lsu.estimator.Student;
/**
 *
 * @author kwang
 */ 
@Stateless
@Named("accessor")
//@SessionScoped //javax.enterprise.context.SessionScoped is not allowed on stateless enterprise beans
//@Dependent
@ApplicationException(rollback=true) //or context.setRollbackOnly() when exception is caught
public class Accessor<UserTransaction> {
    @PersistenceContext    private EntityManager em;
    @Resource private UserTransaction utx;
    @Inject
    edu.lsu.estimator.AppReference ref;
    //public Accessor(){}
    
  //  @Transactional(propagation=Propagation.REQUIRED) //always to use either the MANDATORY or REQUIRED attribute instead of REQUIRES_NEW , to ensure the whole method is in one transaction
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public String saveStudInfo(edu.lsu.estimator.Student stud, edu.lsu.estimator.Student studMod, List<Student>matches, String tz, Integer... p){//List<Student>matches, will set lost_to_local="tmpid", so disable it. markOldDupStud() will do the work
        String msg="";
        //msg.equalsIgnoreCase(msg);
        boolean pu = p.length>0;
        
        try{            
            //em.getTransaction().begin(); //Cannot use an EntityTransaction while using JTA.
            //javax.transaction.UserTransaction
  //          UserTransaction utx = (UserTransaction)new InitialContext().lookup("UserTransaction");
// javax.ejb.EJBContext
 //           utx.begin();
            FacesContext context = FacesContext.getCurrentInstance();
            
            try{// it seems bean validation exception or EJB/DB exception will not be caught by the outer try-catch block. WHY ????? so add this one to 
                this.em.persist(stud);         
                
                for( Student one : matches){
                    one.setPickupInd(0);
                    one.setLostTime(System.currentTimeMillis());
                    one.setLostTz( tz );
                    one.setLostToLocal(null); //stud.getRecid()==tmpid at this time
                    em.merge(one);
                }
               
                //em.createNativeQuery("update student set pickup_ind=0, lost_time=?, lost_tz=?, lost_to=? where pick_up_ind=1 and student_username=?  ")
                //at this time, the recid has not been fetched yet. so do it in another following method
                
                if( pu==false){
                    Counselor me = (Counselor)context.getApplication().evaluateExpressionGet(context, "#{login.currentUser}", Object.class);
                    edu.lsu.estimator.Logs log = new edu.lsu.estimator.Logs( new java.util.Date(), "ESTIMATE", "NEWSTUD", me.getUsername() , "SAVED" ); //getId()
                    log.setResult("ok");
                    em.persist(log);
                }
// em.flush(); //An attempt was made to traverse a relationship using indirection that had a null Session.//This often occurs when an entity with an uninstantiated LAZY relationship is serialized and that lazy relationship is traversed after serialization.
                
            }catch(javax.validation.ConstraintViolationException ve){
  //            ve.printStackTrace();
                msg = msg + "Invalid Data: [";
              for( ConstraintViolation cv :  ve.getConstraintViolations()){
                   System.out.println( "###saveStudInfo### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );
                   
                   /*
                   ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Notice>> constraintViolations = validator.validate(notice);
        for (ConstraintViolation cv : constraintViolations) {
            System.out.println("ValidatationConstraint: " + cv.getConstraintDescriptor().getAnnotation());
            System.out.println("ValidatationConstraint: " + cv.getConstraintDescriptor());
            System.out.println("ValidatationConstraint: " + cv.getMessageTemplate());
            System.out.println("ValidatationConstraint: " + cv.getInvalidValue());
            System.out.println("ValidatationConstraint: " + cv.getLeafBean());
            System.out.println("ValidatationConstraint: " + cv.getRootBeanClass());
            System.out.println("ValidatationConstraint: " + cv.getPropertyPath().toString());
            System.out.println("ValidatationConstraint: " + cv.getMessage());
        }
                   */
                   
                   /*
                   System.out.println( cv.getInvalidValue().toString() ); //NPE
                   System.out.println( cv.getConstraintDescriptor().toString() );
                   System.out.println( cv.getPropertyPath().toString() );
                   System.out.println( cv.getLeafBean().toString() );
                   System.out.println( cv.getLeafBean() );              

                   System.out.println( cv.getRootBean().toString() );
                   System.out.println( cv.getRootBeanClass().getCanonicalName() );
                   System.out.println( cv.getRootBeanClass().getSimpleName() ); */
                   
                   msg = msg + cv.getRootBeanClass().getSimpleName() +" "+ cv.getPropertyPath().toString()+"(val="+cv.getInvalidValue()+"): "+cv.getMessage()+";  ";
               }
              
              //2014-04-24  return specific msg, instead of general violation msg
               //return  msg = "Violation: "+ve.toString(); 
              msg = msg + "]";
              return msg;
              
            }catch(Exception e){
                e.printStackTrace();
                return msg = "Exception: "+e.getMessage();
            }
     
       /****** moved to the dup username clearing method     
            InfoState info = (InfoState)context.getApplication().evaluateExpressionGet(context, "#{info}", Object.class);
            //info.setStudsOfUser(info.getStudsOfUser()+1);
            //info.setTotStuds(info.getTotStuds()+1);
            //info.setNewStudsOfUser(info.getNewStudsOfUser()+1);
            info.sumUp();            
      *****/      
//            utx.commit();
            //em.joinTransaction();
           // em.getTransaction().commit();
        }catch(javax.validation.ConstraintViolationException ve){
 //           ve.printStackTrace();
            for( ConstraintViolation cv :  ve.getConstraintViolations()){
               System.out.println( "###saveStudInfo Path: "+cv.getPropertyPath().toString()+" ### FAILED: "+cv.getMessage() );/*
               System.out.println( cv.getInvalidValue().toString() ); //NPE
               System.out.println( cv.getConstraintDescriptor().toString() );
               System.out.println( cv.getPropertyPath().toString() );
               System.out.println( cv.getLeafBean().toString() );
               System.out.println( cv.getLeafBean() );              
               
               System.out.println( cv.getRootBean().toString() );
               System.out.println( cv.getRootBeanClass().getCanonicalName() );
               System.out.println( cv.getRootBeanClass().getSimpleName() ); */
            }            
            //ve.printStackTrace();
            // getConstraintViolations()
            msg = "Violations2: "+ve.toString();        
            
        }catch( Exception e){
            System.out.println( "########## ");
            msg = "Exceptions2: "+e.getMessage();//.toString();
            e.printStackTrace();           
        }       
        return msg;
    }  
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public String markOldDupStud(Student stud, String tz, Integer... p ){   
    //em.createNativeQuery("update student set pickup_ind=0, lost_time=?, lost_tz=?, lost_to=? where pick_up_ind=1 and student_username=?  ")
        String msg = "";
        boolean pu = p.length>0;
        
        em =  ref.wtfClosedEntityManagerFactory(em, null);
        try{
            em.createNativeQuery("update student set pickup_ind=0, lost_time=?, lost_tz=?, lost_to_local=? where  recid<>? and student_user_name=? and  pickup_ind=1 ")
                    .setParameter(1, System.currentTimeMillis())
                    .setParameter(2, tz)
                    .setParameter(3, stud.getRecid())
                    .setParameter(4, stud.getRecid())
                    .setParameter(5, stud.getStudentUserName())
                    
                    .executeUpdate();
            if(pu==false){
                FacesContext context = FacesContext.getCurrentInstance();
                edu.lsu.estimator.InfoState info = (InfoState)context.getApplication().evaluateExpressionGet(context, "#{info}", Object.class);
                info.sumUp();  
            }
        }catch(Exception e){
            e.printStackTrace();
            msg = e.toString();
        }
        return msg;
    }
                
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public String updateStudInfo(Student stud ){   
            String msg="";
            FacesContext context = FacesContext.getCurrentInstance();
            try{// it seems bean validation exception or EJB/DB exception will not be caught by the outer try-catch block. WHY ????? so add this one to 
                    this.em.merge(stud);       
                    em.flush();
                    Counselor me = (Counselor)context.getApplication().evaluateExpressionGet(context, "#{login.currentUser}", Object.class);
                    edu.lsu.estimator.Logs log = new edu.lsu.estimator.Logs( new java.util.Date(), "ESTIMATE", "UPDATESTUD", me.getUsername() , "SAVED" ); //getId()
                    log.setResult("ok");
                    em.persist(log);
 //                   em.flush(); //statement already closed ???????
             }catch(javax.validation.ConstraintViolationException ve){
    //              ve.printStackTrace();
                    for( ConstraintViolation cv :  ve.getConstraintViolations()){
                        System.out.println( "###updateStudInfo### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );/*
                        System.out.println( cv.getInvalidValue().toString() ); //NPE
                        System.out.println( cv.getConstraintDescriptor().toString() );
                        System.out.println( cv.getPropertyPath().toString() );
                        System.out.println( cv.getLeafBean().toString() );
                        System.out.println( cv.getLeafBean() );              

                        System.out.println( cv.getRootBean().toString() );
                        System.out.println( cv.getRootBeanClass().getCanonicalName() );
                        System.out.println( cv.getRootBeanClass().getSimpleName() ); */
                    }
                    msg = ve.toString(); 
            }     
            return  msg;
    }    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)   
    public void  saveLog(edu.lsu.estimator.Logs log){
        em = ref.wtfClosedEntityManagerFactory(em, null);
        try{
            em.persist(log); // java.lang.ExceptionInInitializerError
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)   
    public String  savePrt(edu.lsu.estimator.Print pdf){
        String msg = "";
        try{
            em.persist(pdf); // java.lang.ExceptionInInitializerError
            //em.flush();
        }catch(javax.validation.ConstraintViolationException ve){
            for( ConstraintViolation cv :  ve.getConstraintViolations()){
               System.out.println( "###savePrt### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );
            }
            msg = ve.toString(); 
        }catch(Exception e){ 
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public String updateStudPrtTimes(Student stud ){ 
        String msg="";
        try{
            ///stud.setPrtTimes( stud.getPrtTimes() + 1); //already spec by caller, set to 1
            
            /********* ID DOES NOT WORK???????? the stud prt_times==0
            if( stud.getPrtTimes()==0)stud.setPrtTimes(1);
            em.merge(stud);  // this one wants to update recid   
            */
            em.createNativeQuery("update student set prt_times=prt_times+1 where recid=?")
                    .setParameter(1, stud.getRecid())
                    .executeUpdate();
            
            
            /*
            Student one = em.find(Student.class, stud.getRecid());
            one.setPrtTimes(  one.getPrtTimes()+1);
            em.merge(one); */
            //em.flush();
        }catch(javax.validation.ConstraintViolationException ve){
            for( ConstraintViolation cv :  ve.getConstraintViolations()){
               System.out.println( "###updateStudPrtTimes### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );
            }
            msg = ve.toString(); 
        }catch(Exception e){ 
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public String updateCounselorPwd(Counselor one ){ 
        String msg="";
        try{/*
            //em.merge(stud);  // this one wants to update recid     
            em.createNativeQuery("update counselors set shadow = ?  where USERID=?")
                    .setParameter(1, one.getShadow())
                    .setParameter(2, one.getUserid())
                    .executeUpdate();
            //em.flush();
            em.flush(); // replace db data with JPA's data in memory ????
            */
            Counselor who = em.find(Counselor.class, one.getUserid());
            who.setShadow(  one.getShadow());
            em.merge(who); //merge after find works.
            //em.refresh(one); //using db data to replace jpa's memory
        }catch(javax.validation.ConstraintViolationException ve){
            for( ConstraintViolation cv :  ve.getConstraintViolations()){
               System.out.println( "###updateCounselorPwd### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );
            }
            msg = ve.toString(); 
        }catch(Exception e){ 
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public String saveMasterStudInfo(Student stud,   List<Student>matches, String tz){        
        String msg="";       
        FacesContext context = FacesContext.getCurrentInstance();

        try{// it seems bean validation exception or EJB/DB exception will not be caught by the outer try-catch block. WHY ????? so add this one to 
            this.em.persist(stud);         

            for( Student one : matches){
                one.setPickupInd(0);
                one.setLostTime(one.getDup());
                one.setLostTz( one.getTzup() );
                one.setLostToMaster(one.getRecid());
                em.merge(one);                
            }

            //em.createNativeQuery("update student set pickup_ind=0, lost_time=?, lost_tz=?, lost_to=? where pick_up_ind=1 and student_username=?  ")
            //at this time, the recid has not been fetched yet. so do it in another following method

            Counselor me = (Counselor)context.getApplication().evaluateExpressionGet(context, "#{login.currentUser}", Object.class);
            edu.lsu.estimator.Logs log = new edu.lsu.estimator.Logs( new java.util.Date(), "ESTIMATE", "MASTERSTUD", me.getUsername() , "SAVED" ); //getId()
            log.setResult("ok");
            em.persist(log);
            em.flush();
        }catch(javax.validation.ConstraintViolationException ve){
//            ve.printStackTrace();
            for( ConstraintViolation cv :  ve.getConstraintViolations()){
                System.out.println( "###saveStudInfo### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );/*
                System.out.println( cv.getInvalidValue().toString() ); //NPE
                System.out.println( cv.getConstraintDescriptor().toString() );
                System.out.println( cv.getPropertyPath().toString() );
                System.out.println( cv.getLeafBean().toString() );
                System.out.println( cv.getLeafBean() );              

                System.out.println( cv.getRootBean().toString() );
                System.out.println( cv.getRootBeanClass().getCanonicalName() );
                System.out.println( cv.getRootBeanClass().getSimpleName() ); */
            }
            return  msg = "Violation: "+ve.toString(); 
        }catch(Exception e){
            e.printStackTrace();
            return msg = "Exception: "+e.getMessage();
        }
        return msg;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void triggerTmpidPrt(){
        em.createNativeQuery(" update prints set prt_id= RTRIM(char(counselor_id))||'.'||RTRIM(char(client_id))||'.'||RTRIM(char(PRT_NUM)) where prt_id='tmpid'").executeUpdate();
   
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void saveSuppliedLasuId(Student std){
        try{
            em.merge(std);
            //name = "Student.findByActiveLsuid", query = "SELECT s FROM Student s WHERE s.studentALsuid = :lsuid and s.studentFisy = :fisy and s.pickupInd=1 and s.recid <> :recid"
            List<Student> dups = em.createNamedQuery("Student.findByActiveLsuid", Student.class)
                                   .setParameter("lsuid", std.getStudentALsuid())
                                   .setParameter("fisy", ref.getFiscal_year())
                                   .setParameter("recid", std.getRecid())
                                   .getResultList();
            for( Student one : dups){
                one.setPickupInd(0);
                one.setLostToLocal(std.getRecid());
                one.setLostTime(System.currentTimeMillis());
                one.setLostTz(ref.getTzSN());
                em.merge(one);
            }
            
            FacesContext context = FacesContext.getCurrentInstance();
            Counselor me = (Counselor)context.getApplication().evaluateExpressionGet(context, "#{login.currentUser}", Object.class);
//            edu.lsu.estimator.Logs modlog = new edu.lsu.estimator.Logs( new java.util.Date(), "ADDID", "MODSTUD", me.getUsername() , "TRIED" );
//            modlog.setLocation(" recid="+std.getRecid()+" lasuid="+std.getStudentALsuid());
//            modlog.setResult("OK");
//            em.persist(modlog);
            
            em.flush();
        }catch(javax.validation.ConstraintViolationException ve){
            for( ConstraintViolation cv :  ve.getConstraintViolations()){
               System.out.println( "###updateCounselorPwd### Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );
            }
            //msg = ve.toString(); 
        }catch(Exception e){ 
            e.printStackTrace();
            //msg = e.getMessage();
        }
    }


}
        
