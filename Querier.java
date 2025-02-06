/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.context.RequestContext;
///import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author kwang
 */

@Named
@SessionScoped 
public class Querier implements Serializable{
    private static final long serialVersionUID = 1L;
    
    //@Inject static FormatLogger log; 
 ///   private static FormatLogger log = new FormatLogger (Logger.getLogger("edu.lsu.estimator.Querier"));//this.getClass().getName()));
    private  static final Logger log = LoggerFactory.getLogger();
    
    @Inject AppReference ref;    
    @Inject FacesContext facesContext;    
    
    @Inject POJOaccessor accessor;
    @PersistenceContext   private EntityManager em;
    @Inject Login login;
    
    // needs to collect user input, perf quering, hoild the result, and do paging/sorting ????
    private String[] studtypes={"ALL"};
    private String typesStr; // show the current types options to the user
    
    //private boolean onlybyme=false;
    private String onlybyme = "all";//my"; //2012-03-05 asked by Geoff to default to "all"
    
    private String optmsg=null;
    private int optstatus; //0:init or good  1:opened, not closed
    private int optchges;    
    
    private String lsuidStr;
    private String fnameStr;
    private String lnameStr;
    private Date dobDate;
 //    private String dobDate;
    private int dobGoodInd ; // if dob typed in and is good, then 1, and the search button is enabled. if none data typed in, list all that match conditions
            
    private int matches=-0; //how many students match the filter and type limit
    private int querychanged=0;
    
    private static final HashMap<String, String> myMap;
    static {
        myMap = new HashMap<>(4);
        myMap.put("autoid", "s.STUDENT_A_LSUID");
        myMap.put("autofn", "s.STUDENT_C_FIRSTNAME");
        myMap.put("autoln", "s.STUDENT_B_LASTNAME");
        myMap.put("autodob", "s.STUDENT_D_DOB");
    }
    private static final HashMap<String, String> myMap2;
    static {
        myMap2 = new HashMap<>(4);
        myMap2.put("autoid", "s.studentALsuid");
        myMap2.put("autofn", "s.studentCFirstname");
        myMap2.put("autoln", "s.studentBLastname");
        myMap2.put("autodob", "s.studentDDob");
    }
    
    private HashMap<String, String> queryby = new HashMap<>();
    
    private List<Student> studs = new ArrayList<>();
    //LazyDataModel<Student> lazyModel ;
    private QueryStudModel dataModel; 
    
    private Student selectedStud;
    private boolean editMode;
    
    //2012-01-17, new var for query conditions
    private boolean onlynew     = false;
    private boolean onlypick    = true; //false //2012-03-05 asked by Geoff to default to "true"
    private boolean onlyprt     = false;
    private boolean onlyclient  = false;
    
    //2013 by Ken
    private boolean allfisy     = false;
    private boolean plusclosed  = false;
    
    //2012/02
    private int rowsPerPage     = 20;//12;
    
    public Querier(){
        dobDate  = null;
        lsuidStr = "";
        fnameStr = "";
        lnameStr = "";
        /*
        lazyModel = new LazyDataModel<Student>() {

            @Override
            public List<Student> load(int i, int i1, String string, SortOrder so, Map<String, String> map) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };*/}
    
    
    public void checkOpts(ActionEvent event){ 
        /*
        FacesContext.getCurrentInstance().getApplication().subscribeToEvent(ExceptionQueuedEvent.class, new SystemEventListener() {

        @Override
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            ExceptionQueuedEventContext content = (ExceptionQueuedEventContext)event.getSource();
            throw new RuntimeException(content.getException());
        }

        @Override
        public boolean isListenerForSource(Object source) {
            return true;
        }
        });

        throw new RuntimeException("test");
         */
        
      log.info("xxxxxxxxxxxxx checkOpts() got types=%s", studtypes==null? "null":Arrays.toString(studtypes));
        FacesMessage msg = null;
        String buttonId = event.getComponent().getClientId();
       // log.info("button id=%s", buttonId);
        //optstatus=0;
        
        if(studtypes==null || studtypes.length==0){
            msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "ChgQueryOpt.NoOpt"); 
            //java.util.MissingResourceException: Can't find bundle for base name msg, locale en_US            
  //          log.info("checkOpts() found empty options. msg=%s", "ChgQueryOpt.NoOpt");
        }else if(studtypes.length>1 && Arrays.toString(studtypes).indexOf("ALL")>-1){
            msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "ChgQueryOpt.DupAll");  
  //          log.info("checkOpts() found conflicting ALL option. msg=%s", "ChgQueryOpt.DupAll"); //java.lang.NullPointerException	at edu.lsu.estimator.Querier.checkOpts(Querier.java:91)            
        }
        
        
        
        if( msg!=null){
            facesContext.addMessage(buttonId, msg); //no UIcomponent ID, so it will be a global message
            if(optstatus>0)
                optstatus++;
            else
                optstatus=1;
        }else{
            optchges = 0;            
            if( optstatus>0)
                optstatus=0;
            else
                optstatus--;
        }        
        optmsg = null;
        log.info("checkOpts() finished with optstatus=%d, optchanges=%d", optstatus, optchges);
    }   
    
    public void stdTypeChanged(ValueChangeEvent e){
        //UIInput nameInput = e.getComponent()
        //String name = nameInput.getValue()
        String opts = e.getNewValue().toString();
        if( opts==null || opts.isEmpty()){
            studtypes = new String[]{"ALL"};
        }else{
            String[] types = opts.split(",");
            studtypes = types;
        }
    }
    
    public void optchanged(javax.faces.event.AjaxBehaviorEvent e){// optchanged(ValueChangeEvent e){
        optchges++;
    }
    
    public void chglsuidStr(AjaxBehaviorEvent e){
        if( lsuidStr==null || lsuidStr.isEmpty() || lsuidStr.trim().isEmpty()){
            queryby.remove("autoid");
            if( queryby.isEmpty() && matches==0) matches=-1;
        }
        querychanged=0;
        //refreshGridInstantly();
    }
    public void chglnameStr(AjaxBehaviorEvent e){
  //      log.info("==== lanst name changed==== %s", lnameStr );
        if( lnameStr==null || lnameStr.isEmpty() || lnameStr.trim().isEmpty()){
            queryby.remove("autoln");
            if( queryby.isEmpty() && matches==0) matches=-1;
        }
        querychanged=0;
    }
    public void chgfnameStr(AjaxBehaviorEvent e){
        if( fnameStr==null || fnameStr.isEmpty() || fnameStr.trim().isEmpty()){
            queryby.remove("autofn");
            if( queryby.isEmpty() && matches==0) matches=-1;
        }
        querychanged=0;
    }
    
    public void dobchanged(javax.faces.event.AjaxBehaviorEvent e){
        //log.info( "dob changed to %s", dobDate);
        List<String> results = new ArrayList<String>();         
       
        String sql = composeSql("autodob");//"select s.STUDENT_A_LSUID from student s where s.STUDENT_A_LSUID like ? ";
        results = em.createNativeQuery(sql).getResultList();        
        matches = results==null? 0: results.size();
        
        if( dobDate==null) queryby.remove("autodob");
        else queryby.put("autodob", "autodob");
        
        if( queryby.isEmpty() && matches==0) matches=-1;
        querychanged=0;
    }
    
    //======================================================== change button value for the first time
    public void querybtnclicked(AjaxBehaviorEvent e){
        if( querychanged==0){
            querychanged++;
        }
    }
    
    //=============================================to refresh the grid on keyup event. ???? no effect.
    public void refreshGridInstantly(){
        String sql = composeSql2(" "); //select null from student s where upper(null) like (?)  and upper(s.STUDENT_C_FIRSTNAME) like upper('First Name')  and s.counselor_id='test'  and s.student_fisy=2012
     //   List<String[]> results = new ArrayList<String[]>();
        List<Student> results = new ArrayList<>();
     //   results = (List<String[]>)em.createNativeQuery(sql).getResultList();  
        results = em.createQuery(sql).getResultList(); 
        int amt = results==null? 0: results.size();
        
        int i=0;
        studs.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
             studs = results;
        }catch(Exception e){
            e.printStackTrace();
        }
 //       log.info("refreshGridInstantly(): queryStuds query=%s\n, results=%d, studs amt=%d", sql, amt, studs.size());
        
        
        if(amt>0 && studs.size()>0){
            Student one  = studs.get(0);
 //           log.info("refreshGridInstantly(): first stud obj fname=%s, lname=%s, id=%s, counselor=%s, dob=%s", one.getStudentCFirstname(), one.getStudentBLastname(), one.getStudentALsuid(), one.getCounselorId(), one.getStudentDDob());
        }
         
        // DataModel must implement org.primefaces.model.SelectableDataModel when selection is enabled.
        //if you want to use the row select feature you must implement a SelectableDataModel
        dataModel = new QueryStudModel(studs);
    }
    
    public void resetQuery(ActionEvent event){
        studs.clear();
        dataModel = new QueryStudModel(studs);
        this.selectedStud = null;
        dobDate = null;
        lsuidStr = "";
        fnameStr="";
        lnameStr="";
        log.info("reset query conditions...");
    }
    
    public void queryStuds(ActionEvent event){
        checkOpts(event);
        if( optstatus>0)return;
        
        if( querychanged==0){
            querychanged++;
        }
        String sql = composeSql2(" "); //select null from student s where upper(null) like (?)  and upper(s.STUDENT_C_FIRSTNAME) like upper('First Name')  and s.counselor_id='test'  and s.student_fisy=2012
     //   List<String[]> results = new ArrayList<String[]>();
        List<Student> results = new ArrayList<>();
     //   results = (List<String[]>)em.createNativeQuery(sql).getResultList();  
        results = em.createQuery(sql).getResultList(); 
        int amt = results==null? 0: results.size();
        
        int i=0;
        studs.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{/*
            //for(String[] one : results){ //.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.String;
            for(int x=0; x<amt; x++){
                String[] one = results.get(x);
                Student std = new Student();
                i=0;
                std.setStudentNumb(Integer.parseInt( one[i++]));
                std.setStudentFisy(Integer.parseInt( one[i++]));
                std.setStudentALsuid(  one[i++]  );
                std.setStudentBLastname( one[i++]);
                std.setStudentCFirstname(one[i++]);
                std.setStudentDDob( one[i]==null||one[i].trim().isEmpty()? null :sdf.parse(one[i])  );
                i++;
                std.setStudentFPhone(one[i++]);
                std.setStudentBvDoe( one[i]==null||one[i].trim().isEmpty()? null: sdf.parse(one[i]));
                i++;
                std.setStudentBzUploaded(one[i++]);
                std.setStudentStudType(one[i++]);
                std.setStudentUploadDate(one[i]==null||one[i].trim().isEmpty()? null : sdf.parse(one[i]));
                i++;
                std.setCounselorId(one[i++]);
                std.setStudentIState(one[i++]);
                std.setStudentJZip(one[i]);             

                studs.add(std);
            }     */
            
            //studs = results;            
            //############ for sorting by ID             
            for(int x=0; x<amt; x++){
                Student one = results.get(x);
            
                if( one.getStudentALsuid()==null) one.setStudentALsuid("");
                studs.add(one);
            } 
            
        }catch(Exception e){
            e.printStackTrace();
        }
        log.info("queryStuds(): query=%s\n, results=%d, studs_list_size=%d", sql, amt, studs.size());
        
 /*       
        if(amt>0 && studs.size()>0){
            Student one  = studs.get(0);
            log.info("queryStuds(): 1st obj: fname=%s, lname=%s, id=%s, counselor=%s, dob=%s", one.getStudentCFirstname(), one.getStudentBLastname(), one.getStudentALsuid(), one.getCounselorId(), one.getStudentDDob());
        }
 */        
        // DataModel must implement org.primefaces.model.SelectableDataModel when selection is enabled.
        //if you want to use the row select feature you must implement a SelectableDataModel
        dataModel = new QueryStudModel(studs);
        this.selectedStud = null;
        
    }
    
    
    private String composeSql2(String col_in){ //used in em.createQuery()
        if(col_in==null)return null;
        String col = col_in.trim();
        
        StringBuilder sql = new StringBuilder(1580);
        //String[] cols = {};
        HashMap<String, String> cols = new HashMap<>();
        //HashMap<String, String> cols = new HashMap<>();
        
        if (!col.isEmpty() && !col.equalsIgnoreCase("autodob")){
            sql.append("select ").append(myMap2.get(col)).append(" from Student s where upper(").append(myMap2.get(col)).append(") like (?) ");
        }else if( !col.isEmpty()){
            sql.append("select ").append(myMap2.get(col)).append(" from Student s where 1=1 ");
        }else{
            sql.append("select s from Student s where 1=1 ");
        }
        
        if(!col.isEmpty() && !col.equalsIgnoreCase("autoid") && lsuidStr!=null && !lsuidStr.isEmpty() ){
            sql.append( " and ").append(myMap2.get("autoid")).append(" like '").append(lsuidStr).append("' ");
        }else if(col.isEmpty() && lsuidStr!=null && !lsuidStr.isEmpty() ){
            sql.append( " and ").append(myMap2.get("autoid")).append(" like '").append(lsuidStr).append("' ");
         
        }
        
        if(!col.isEmpty() &&  !col.equalsIgnoreCase("autofn") && fnameStr!=null && !fnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap2.get("autofn")).append(") like upper('").append(fnameStr).append("') ");
        }else if(col.isEmpty() && fnameStr!=null && !fnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap2.get("autofn")).append(") like upper('").append(fnameStr).append("') ");
        }
        
        if(!col.isEmpty() &&  !col.equalsIgnoreCase("autoln") && lnameStr!=null && !lnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap2.get("autoln")).append(") like upper('").append(lnameStr).append("') ");
        }else if(col.isEmpty() &&  lnameStr!=null && !lnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap2.get("autoln")).append(") like upper('").append(lnameStr).append("') ");
        } 
        
        if(  dobDate!=null ){
            SimpleDateFormat sdf = new SimpleDateFormat(ref.getDateInputShowStr());
            sql.append( " and ").append(myMap2.get("autodob")).append(" = '").append(sdf.format(dobDate) ).append("' ");
            //sql.append( " and ").append(myMap2.get("autodob")).append(" = '").append( dobDate  ).append("' ");
        }
        
        int user = login.getCurrentUser().getUserid(); //.getId() String
        switch( onlybyme){ //my others all
            case "my": sql.append(" and s.counselorId=").append(user).append(" ");
                break;
        /*    case "my new": sql.append(" and s.counselorId=").append(user).append(" ");
                sql.append(" and (s.studentUploadDate is null or s.studentBzUploaded='no') ");
                break; */
            case "others":sql.append(" and s.counselorId<>").append(user).append(" ");
                break;
        }
        if( onlynew){
            sql.append(" and s.dup=0");
        }
        if( onlypick){
            sql.append(" and s.pickupInd=1");
        }
        if( onlyprt){
            sql.append(" and s.prtTimes >0");
        }
        if( onlyclient){
            sql.append(" and s.clientId=").append( ref.getClientid());
        }        
        if( allfisy==false){
            sql.append(" and s.studentFisy=").append(ref.getFiscal_year());
        }
        log.info("************************** QUERIER get para allfisy=%s, while ref fisy=%d", allfisy, ref.getFiscal_year());
        if( plusclosed == false){
            sql.append(" and s.ncStdInd=0");
        }
        if(studtypes!=null && studtypes.length>0 &&!studtypes[0].equals("ALL") ){
            //ref studtype_vals   = {"ALL",  "UD", "UI", "ESL", "CJ", "G", "PG"};  //studentUAcademic=FR F2 ? or studentStudType=UGFY
            sql.append( matchStudAcademicOpt(studtypes));
        }
        
 //      if(log!=null)log.info(" sql for %s==%s", col, sql.toString()); //java.lang.NullPointerException	at edu.lsu.estimator.Querier.composeSql(Querier.java:166)
 //       else System.out.println(sql.toString());
        
        return sql.toString();        
    }
    
    
    
    private String composeSql(String col_in){ //used in em.createNativeQuery()  , String valString
        if(col_in==null)return null;
        String col = col_in.trim();
        
        StringBuilder sql = new StringBuilder(1580);
        //String[] cols = {};
        HashMap<String, String> cols = new HashMap<>();
        //HashMap<String, String> cols = new HashMap<>();
        
        if (!col.isEmpty() && !col.equalsIgnoreCase("autodob")){
            sql.append("select distinct ").append(myMap.get(col)).append(" from student s where upper(").append(myMap.get(col)).append(") like (?) ");
        }else if( !col.isEmpty()){
            sql.append("select distinct ").append(myMap.get(col)).append(" from student s where 1=1 ");
        }else{
            sql.append("select s.STUDENT_NUMB, s.STUDENT_FISY, s.STUDENT_A_LSUID, s.STUDENT_B_LASTNAME, s.STUDENT_C_FIRSTNAME, s.STUDENT_D_DOB, s.STUDENT_F_PHONE, s.STUDENT_BV_DOE, s.STUDENT_BZ_UPLOADED, s.STUDENT_STUD_TYPE, s.STUDENT_UPLOAD_DATE, s.COUNSELOR_ID,s.STUDENT_I_STATE, s.STUDENT_J_ZIP from student s where 1=1 ");
        }
        
        if(!col.isEmpty() && !col.equalsIgnoreCase("autoid") && lsuidStr!=null && !lsuidStr.isEmpty() ){
            sql.append( " and ").append(myMap.get("autoid")).append(" like '").append(lsuidStr).append("' ");
        }else if(col.isEmpty() && lsuidStr!=null && !lsuidStr.isEmpty() ){
            sql.append( " and ").append(myMap.get("autoid")).append(" like '").append(lsuidStr).append("' ");
         
        }
        
        if(!col.isEmpty() &&  !col.equalsIgnoreCase("autofn") && fnameStr!=null && !fnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap.get("autofn")).append(") like upper('").append(fnameStr).append("') ");
        }else if(col.isEmpty() && fnameStr!=null && !fnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap.get("autofn")).append(") like upper('").append(fnameStr).append("') ");
        }
        
        if(!col.isEmpty() &&  !col.equalsIgnoreCase("autoln") && lnameStr!=null && !lnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap.get("autoln")).append(") like upper('").append(lnameStr).append("') ");
        }else if(col.isEmpty() &&  lnameStr!=null && !lnameStr.isEmpty() ){
            sql.append( " and upper(").append(myMap.get("autoln")).append(") like upper('").append(lnameStr).append("') ");
        } 
        
        if(  dobDate!=null ){
            SimpleDateFormat sdf = new SimpleDateFormat(ref.getDateInputShowStr());
            sql.append( " and ").append(myMap.get("autodob")).append(" = '").append(sdf.format(dobDate) ).append("' ");
            //sql.append( " and ").append(myMap.get("autodob")).append(" = '").append( dobDate  ).append("' ");
        }
        //the lamb derby does not support to_char() func as oracle, and even worse, its default date format is "yyyy-mm-dd":
        //select * from student where char(student_d_dob) = '01/02/1978' returns nothing
        //select * from student where student_d_dob = date('01/02/1978') returns one record
        ////select * from student where student_d_dob = '01/02/1978' returns the same record
        //types: "ALL",  "UD", "UI", "ESL", "CJ", "G", "PG" ==studtypes String[]
        //owner: my mynew others ==onlybyme
        int user = login.getCurrentUser().getUserid();
        switch( onlybyme){
            case "my": sql.append(" and s.counselor_id=").append(user).append(" ");
                break;
      /*      case "my new": sql.append(" and s.counselor_id=").append(user).append(" ");
                sql.append(" and (s.student_upload_date is null or s.student_bz_uploaded='no') ");
                break; */
            case "others":sql.append(" and s.counselor_id<>").append(user).append(" ");
                break;
            case "all": break;
        }
        if( onlynew){
            sql.append(" and s.dup=0");
        }
        if( onlypick){
            sql.append(" and s.pickup_Ind=1"); //createNativeQuery
        }
        if( onlyprt){
            sql.append(" and s.prt_Times >0"); //createNativeQuery
        }
        if( onlyclient){
            sql.append(" and s.client_Id=").append( ref.getClientid()); //createNativeQuery
        }        
        if( allfisy==false){
            sql.append(" and s.student_fisy=").append(ref.getFiscal_year());
        }
        if( plusclosed == false){
            sql.append(" and s.nc=0");
        }
        if(studtypes!=null && studtypes.length>0 &&!studtypes[0].equals("ALL") ){
            //ref studtype_vals   = {"ALL",  "UD", "UI", "ESL", "CJ", "G", "PG"};  //studentUAcademic=FR F2 ? or studentStudType=UGFY
            //FR F2 SO JR SR MBA
            sql.append( matchStudAcademicOpt(studtypes));
        }
        
 //      if(log!=null)log.info(" sql for %s==%s", col, sql.toString()); //java.lang.NullPointerException	at edu.lsu.estimator.Querier.composeSql(Querier.java:166)
 //       else System.out.println(sql.toString());
        
        return sql.toString();
    }
    
    private String matchStudAcademicOpt(String[] opts){//chosen, and no ALL
        if( opts==null || opts.length==0 ||opts[0].equalsIgnoreCase("ALL"))return "";
        StringBuilder sb = new StringBuilder(128);
        String[] vals = ref.getStudtype_vals();
        java.util.List<String> list = Arrays.asList(vals);
        int start=0;
        for(String one:opts){
            if( !list.contains(one)) continue;            
            if( start==0)sb.append(" and (  ");      //1=1      
            switch(one){
                case "FR":
                case "F2":
                case "SO":
                case "JR":
                case "SR": 
                case "MBA": sb.append(start>0? " or ": "  ").append(" s.studentUAcademic='").append(one).append("'");
                    break;
                case "UD":  sb.append(start>0? " or ": "  ").append(" UPPER(s.studentLIntlStud)='NO'");
                    break;
                case "UI":  sb.append(start>0? " or ": "  ").append(" UPPER(s.studentLIntlStud)='YES'");
                    break;
                default:    sb.append(start>0? " or ": "  ").append(" s.studentStudType <>'UGFY'");
                    break; //and s.studentStudType='UGFY'
            }
            start++;
        }
        if( start>0){
            sb.append(")");
            return sb.toString();
        }else{
            return "";
        }
    }
    
    public List<String> autolsuidStr(String query) {  
        querychanged=0;
        List<String> results = new ArrayList<String>();         
        if( query !=null && !query.isEmpty() && !query.trim().isEmpty()) {
            query = query.toUpperCase();//.trim()
            if(!query.endsWith("%") && !query.endsWith("_")) query = query+"%";
            queryby.put("autoid", "autoid");
        }else{
            queryby.remove("autoid");
            if( queryby.isEmpty() && matches==0) matches=-1;
            return results;
        }  
        String sql = composeSql("autoid");//"select s.STUDENT_A_LSUID from student s where s.STUDENT_A_LSUID like ? ";
        results = em.createNativeQuery(sql).setParameter(1, query).getResultList();        
        matches = results==null? 0: results.size();
        return results;  
    }  
    public List<String> autofnameStr(String query) {  
        querychanged=0;
        List<String> results = new ArrayList<String>();     
        String para="";
        if( query !=null && !query.isEmpty() && !query.trim().isEmpty()) {
            para = query.toUpperCase(); //trim()
            if(!para.endsWith("%") && !para.endsWith("_")) para = para+"%";
            queryby.put("autofn", "autofn");
        }else{
            queryby.remove("autofn");
            if( queryby.isEmpty() && matches==0) matches=-1;
            return results;
        }        
        String sql = composeSql("autofn");
        results = em.createNativeQuery(sql).setParameter(1, para).getResultList();       
        matches = results==null? 0: results.size();
        return results;  
    }
    public List<String> autolnameStr(String query) {  
        querychanged=0;
 //       log.info("---- auto responser got %s", query);
        List<String> results = new ArrayList<String>();         
        if( query !=null && !query.isEmpty() && !query.trim().isEmpty()) {
            query = query.toUpperCase(); //trim().
            if(!query.endsWith("%") && !query.endsWith("_")) query = query+"%";
            queryby.put("autoln", "autoln");
        }else{
            queryby.remove("autoln");
            if( queryby.isEmpty() && matches==0) matches=-1;
            return results;
        }        
        String sql = composeSql("autoln");
        results = em.createNativeQuery(sql).setParameter(1, query).getResultList();         
        matches = results==null? 0: results.size();
        return results;  
    }
        
    
    public String optmsg(){
        return optmsg;
    }
    
    public String[] getStudtypes() {
        return studtypes;
    }

    public void setStudtypes(String[] studtypes) {
        this.studtypes = studtypes;
    }

    public String getOnlybyme() {
        return onlybyme;
    }

    public void setOnlybyme(String onlybyme) {
        this.onlybyme = onlybyme;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getOptstatus() {
        return optstatus;
    }

    public String getLsuidStr() {
        return lsuidStr;
    }

    public void setLsuidStr(String lsuidStr) {
        this.lsuidStr = lsuidStr;
    }

    public String getFnameStr() {
        return fnameStr;
    }

    public void setFnameStr(String fnameStr) {
        this.fnameStr = fnameStr;
    }

    public String getLnameStr() {
        return lnameStr;
    }

    public void setLnameStr(String lnameStr) {
        this.lnameStr = lnameStr;
    }

    public Date getDobDate() {
        return dobDate;
    }

    public void setDobDate(Date dobDate) {
        this.dobDate = dobDate;
    }

    public int getDobGoodInd() {
        return dobGoodInd;
    }

    public String getTypesStr() {
        //return typesStr;        
        //return "  "+ref.genLabelsofKeys(studtypes) + " of " + onlybyme+" estimates";//(onlybyme ? "mine":"all");
        StringBuilder sb = new StringBuilder(128);
        sb.append(" ").append(ref.genLabelsofKeys(studtypes)).append(" of ").append(onlybyme).append(" estimates ");
        int in=0;
        if( onlynew){
            sb.append( in==0?"(":"").append("not uploaded");
            in++;
        }
        if( onlypick){
            sb.append( in==0?"(":", ").append("active");
            in++;
        }
        if( onlyprt){
            sb.append( in==0?"(":", ").append("delivered");
            in++;
        }
        if( onlyclient){
            sb.append( in==0?"(":", ").append("local");
            in++;
        }
        if(allfisy){
            sb.append( in==0?"(":", ").append("history");
            in++;
        }
        if(plusclosed){
            sb.append( in==0?"(":", ").append("include those not coming");
            in++;
        }
        sb.append( in==0?"":")");
        return sb.toString();
    }

    public int getOptchges() {
        return optchges;
    }

    public void setOptchges(int optchged) {
        this.optchges = optchged;
    }

    public List<Student> getStuds() {
        return studs;
    }

    public Student getSelectedStud() {
  //      log.info(" ..... getSelectedStud(). obj: [%s]", selectedStud==null? "null":new StringBuilder().append(selectedStud.getStudentFisy()).append('.').append(selectedStud.getCounselorId()).append('.').append(selectedStud.getStudentNumb()).toString());
        return selectedStud;
    }

    public void setSelectedStud(Student theSelectedStud) {
        /*
        StringBuilder sb = new StringBuilder(64);             
        if( selectedStud != null){
            sb.append(selectedStud.getStudentFisy()).append('.').append(selectedStud.getCounselorId()).append('.').append(selectedStud.getStudentNumb());        
            log.info("=============== setSelectedStud(), old student obj:[%s], its key=%s ==========", selectedStud==null? "NULL obj":sb.toString(), dataModel.getRowKey(selectedStud));
        }else{
            log.info("=============== setSelectedStud(), old student obj is null");
        } 
         */
        this.selectedStud = theSelectedStud;        
/*        
        if( selectedStud != null){           
            sb.setLength(0);
            sb.append(selectedStud.getStudentFisy()).append('.').append(selectedStud.getCounselorId()).append('.').append(selectedStud.getStudentNumb());        
            log.info("======== setSelectedStud(), new student obj:[%s], its key=%s ==========", selectedStud==null? "NULL obj":sb.toString(), dataModel.getRowKey(selectedStud));
        }else{
            log.info("========  setSelectedStud() to null ");
        }
  //      log.info("========  setSelectedStud() invoked when  model obj is null ? %s", dataModel == null? "yes":"no");
        
        if( dataModel != null){   //javax.faces.model.NoRowAvailableException:/	at javax.faces.model.ListDataModel.getRowData(ListDataModel.java:150)   
            if( theSelectedStud!=null)
            log.info("========  setSelectedStud() invoked when dataModel rowkey of obj==%s ", dataModel.getRowKey(selectedStud));
            
            log.info("========  setSelectedStud() invoked when dataModel row index=%d, ROW_SELECTED? %s,  ", dataModel.getRowIndex(),  dataModel.isRowAvailable());//, dataModel.getRowData()==null?"yes":"no");
            if( selectedStud != null &&  false==dataModel.isRowAvailable() ){
                 log.info("========  setSelectedStud() tries to recover the lost selected row");
                 int g=-1;
                 Student one;
                 for(int x=0; x<dataModel.getRowCount(); x++ ){
                     dataModel.setRowIndex(x);
                     one = dataModel.getRowData();
                     log.info("========--------  looping #%d and its rowkey==%s", x, dataModel.getRowKey(one));
                     if( one==selectedStud ){                     
                         g=x;
                         break;
                     }
                 }
                 dataModel.setRowIndex(g);
                 log.info("========--------  setSelectedStud() set rowindex==%d by matching", g);
            }
        }
        
        if( selectedStud != null && dataModel != null && false==dataModel.isRowAvailable()   ){
             log.info("========  setSelectedStud() found no row is marked as selected in the model. rowdata==null? %s", dataModel.getRowData()==null? "yes":"no");
             //selectedStud = null;
             //dataModel.setRowIndex(-1); 
             int g=-1;
             for(int x=0; x<dataModel.getRowCount(); x++ ){
                 dataModel.setRowIndex(x);
                 if( dataModel.getRowData()==selectedStud ){                     
                     g=x;
                     break;
                 }
             }
             dataModel.setRowIndex(g);
             log.info("========###########  setSelectedStud() set rowindex==%d by matching", g);
        }else{
            log.info("selectedStud==null? %s  dataModel==null? %s  dataModel.isRowAvailable()? %s", selectedStud == null?"Y":"N",  dataModel== null?"Y":"N",   dataModel==null? "N":(false==dataModel.isRowAvailable()? "N":"Y"));
        }
*/        
    }

    public boolean isEditMode() {
        return editMode; 
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public QueryStudModel getDataModel() {
        if( dataModel==null || dataModel.getRowCount()==-1){
            log.info("=========== getDataModel() found null obj");
            if( studs!=null){
                dataModel = new QueryStudModel(studs);
                
                log.info("=========== getDataModel() init obj use existing studs which size=%d", studs.size());
            }else{
                queryStuds(null);//ActionEvent event)
                //dataModel = new QueryStudModel( new ArrayList<Student>());
                log.info("=========== getDataModel() init obj use new studs which size=%d", studs.size());
            }
        } 
        
        return dataModel;
    }

    public int getQuerychanged() {
        return querychanged;
    }
    
    
    public void onRowSelect( SelectEvent event){         
        Student student = (Student)event.getObject();
        this.selectedStud = student;
        //StringBuilder sb = new StringBuilder(32);
        //sb.append(student.getStudentFisy()).append('.').append(student.getCounselorId()).append('.').append(student.getStudentNumb());//.toString();
        //log.info("########## onRowSelect() student: [%s] ##########", sb.toString());
        log.info("########## onRowSelect() student: [%s] ##########", selectedStud.getRecid());
        //FacesMessage msg = new FacesMessage("Student Selected", sb.toString());
        //FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    //public void onRowUnselect(UnselectEvent event) {
    //    FacesMessage msg = new FacesMessage("Car Unselected", ((Car) event.getObject()).getModel());
    //    FacesContext.getCurrentInstance().addMessage(null, msg);
    //}
    
    public int sortStudByFirstName_user_guide(Student std1, Student std2){
        log.info("$$$$$$$$$$$$$$ sorting");
        int i=0;
        if(std1==null){/*
            if( std2==null)return 0;
            if( std2!=null)return -1; */
            if( std2==null){
                i=0;
            }else{
                i = -1;
            }
        }else{/*
            if( std2==null)return 1;
            String str1 = std1.getStudentCFirstname();
            String str2 = std2.getStudentCFirstname();
            if( str1 != null && str2 !=null){
                return str1.compareToIgnoreCase(str2);
            }
            if( str1==null && str2==null) return 0;
            
            if( str1==null)return -1;            else return 1; */
            if( std2==null){
                i=1;
            }else{
                String str1 = std1.getStudentCFirstname();
                String str2 = std2.getStudentCFirstname();
                if( str1 != null && str2 !=null){
                    i = str1.compareToIgnoreCase(str2);
                }else if ( str1==null && str2==null) {
                    i=0;
                }else if( str1==null){
                    i =-1;
                }else{
                    i =1;
                }
            }
        }
        return i;
    }
    
    //Object obj1,Object obj2
    public int sortStudByFirstName(Object obj1, Object obj2){
        log.info("$$$$$$$$$$$$$$ sorting");
        
        int i=0;
        if(obj1==null){/*
            if( std2==null)return 0;
            if( std2!=null)return -1; */
            if( obj2==null){
                i=0;
            }else{
                i = -1;
            }
        }else{/*
            if( std2==null)return 1;
            String str1 = std1.getStudentCFirstname();
            String str2 = std2.getStudentCFirstname();
            if( str1 != null && str2 !=null){
                return str1.compareToIgnoreCase(str2);
            }
            if( str1==null && str2==null) return 0;
            
            if( str1==null)return -1;            else return 1; */            
            
            if( obj2==null){
                i=1;
            }else{
                //Student std1 = (Student)obj1; //java.lang.ClassCastException: java.lang.String cannot be cast to edu.lsu.estimator.Student
                //Student std2 = (Student)obj2;
                //at org.primefaces.component.datatable.DataHelper.sort(DataHelper.java:113)
	        //at org.primefaces.component.datatable.DataHelper.decodeSortRequest(DataHelper.java:97)
                //at org.primefaces.model.BeanPropertyComparator.compare(BeanPropertyComparator.java:75)                
                
                String str1 = (String)obj1;//std1.getStudentCFirstname();
                String str2 = (String)obj2;//std2.getStudentCFirstname();                
                
                if( str1 != null && str2 !=null){
                    i = str1.compareToIgnoreCase(str2);
                }else if ( str1==null && str2==null) {
                    i=0;
                }else if( str1==null){
                    i =-1;
                }else{
                    i =1;
                }
            }
        }
        return i;
    }

    public boolean isOnlynew() {
        return onlynew;
    }

    public void setOnlynew(boolean onlynew) {
        this.onlynew = onlynew;
    }

    public boolean isOnlypick() {
        return onlypick;
    }

    public void setOnlypick(boolean onlypick) {
        this.onlypick = onlypick;
    }

    public boolean isOnlyprt() {
        return onlyprt;
    }

    public void setOnlyprt(boolean onlyprt) {
        this.onlyprt = onlyprt;
    }

    public boolean isOnlyclient() {
        return onlyclient;
    }

    public void setOnlyclient(boolean onlyclient) {
        this.onlyclient = onlyclient;
    }
    
    
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }
    
    public void onPaginate(PageEvent event){ //org.primefaces.event.data.PageEvent 

        //logger.info("I am on page:"+event.getPage());
        //logger.info("Rows per page set:"+((org.primefaces.component.datatable.DataTable)event.getSource()).getRows());
        rowsPerPage = ((org.primefaces.component.datatable.DataTable)event.getSource()).getRows();
    }
    
    //procInfoBtn
    public void procInfoBtn(ActionEvent actionEvent) {  
//        RequestContext context = RequestContext.getCurrentInstance();  
        //context.addCallbackParam("saved", true);    //basic parameter  
        ////context.addCallbackParam("user", user);     //pojo as json  
        //querier.selectedStud
 //       context.addCallbackParam("select", this.selectedStud!=null);
        
        log.info(",,,,,,,, procInfoBtn() action listener got selected stud recid=%s", selectedStud==null? "N/A":selectedStud.getRecid());
        
        //execute javascript oncomplete  
        ////context.execute("alert('Hello from the Backing Bean, in method procInfoBtn().');");  

        //update panel  
        ////context.addPartialUpdateTarget("form:panel");  

        //add facesmessage  
        ////FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success", "Success"));  
        
        dataModel.getRowIndex();
        //rowsPerPage = ((org.primefaces.component.datatable.DataTable)event.getSource()).getRows(); // why paginate can show the list?
    }  
    public void procIdBtn(ActionEvent actionEvent) {  
 //       RequestContext context = RequestContext.getCurrentInstance();        
 //       context.addCallbackParam("select", this.selectedStud!=null);
        log.info(",,,,,,,, procIdBtn() action listener got selected stud recid=%s", selectedStud==null? "N/A":selectedStud.getRecid());
        dataModel.getRowIndex();
    }
    
    //onRowSelect
    public void onRowSelect(ActionEvent actionEvent){
        RequestContext context = RequestContext.getCurrentInstance();  
        
        context.addCallbackParam("select", this.selectedStud==null? 0 : this.selectedStud.getRecid());
        context.execute("rec="+ (this.selectedStud==null? 0 : this.selectedStud.getRecid()) );
                
        //2012-12-11
        //addPartialUpdateTarget(): It has been deprecated in PrimeFaces 3.2 and been removed in PrimeFaces 3.4. You need update() method instead.
        //context.addPartialUpdateTarget("auth-display");  
                
        context.update("auth-display");
    }
    
    public int sortByID2(Student std1, Student std2){
    //(Car car1, Car car2) {	
    //return -1, 0 , 1 if car1 is less than, equal to or greater than car2
        int res=0;
        //studentALsuid
        String id1 = std1.getStudentALsuid();
        id1 = ref.isEmp(id1)? "null" : id1.trim();//.toUpperCase();
        String id2 = std2.getStudentALsuid();
        id2 = ref.isEmp(id2)? "null" : id2.trim();
        
        //set null as least
        if(id1.equals("null") && !id2.equals("null") ){
            res = -1;
        }else if (!id1.equals("null") && id2.equals("null")){
            res = 1;
        }else{        
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
        }
        return res;
    }  
    
     public int sortByID(Object obj1, Object obj2){     
        int res=0;
        //studentALsuid
        String id1 = (String)obj1;
        String id2 = (String)obj2;        
        
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

    /**
     * @return the allfisy
     */
    public boolean isAllfisy() {
        return allfisy;
    }

    /**
     * @param allfisy the allfisy to set
     */
    public void setAllfisy(boolean allfisy) {
        this.allfisy = allfisy;
    }

    /**
     * @return the plusclosed
     */
    public boolean isPlusclosed() {
        return plusclosed;
    }

    /**
     * @param plusclosed the plusclosed to set
     */
    public void setPlusclosed(boolean plusclosed) {
        this.plusclosed = plusclosed;
    }

}
