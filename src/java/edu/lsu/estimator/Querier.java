/*      */ package edu.lsu.estimator;
/*      */ 
/*      */ import com.kingombo.slf5j.Logger;
/*      */ import com.kingombo.slf5j.LoggerFactory;
/*      */ import edu.lsu.estimator.AppReference;
/*      */ import edu.lsu.estimator.Login;
/*      */ import edu.lsu.estimator.POJOaccessor;
/*      */ import edu.lsu.estimator.QueryStudModel;
/*      */ import edu.lsu.estimator.Student;
/*      */ import java.io.Serializable;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import javax.enterprise.context.SessionScoped;
/*      */ import javax.faces.application.FacesMessage;
/*      */ import javax.faces.context.FacesContext;
/*      */ import javax.faces.event.ActionEvent;
/*      */ import javax.faces.event.AjaxBehaviorEvent;
/*      */ import javax.faces.event.ValueChangeEvent;
/*      */ import javax.inject.Inject;
/*      */ import javax.inject.Named;
/*      */ import javax.persistence.EntityManager;
/*      */ import javax.persistence.PersistenceContext;
/*      */ import org.primefaces.component.datatable.DataTable;
/*      */ import org.primefaces.context.RequestContext;
/*      */ import org.primefaces.event.SelectEvent;
/*      */ import org.primefaces.event.data.PageEvent;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Named
/*      */ @SessionScoped
/*      */ public class Querier
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   51 */   private static final Logger log = LoggerFactory.getLogger(); @Inject
/*      */   AppReference ref;
/*      */   @Inject
/*      */   FacesContext facesContext;
/*      */   @Inject
/*      */   POJOaccessor accessor;
/*      */   @PersistenceContext
/*      */   private EntityManager em;
/*      */   @Inject
/*      */   Login login;
/*   61 */   private String[] studtypes = new String[] { "ALL" };
/*      */   
/*      */   private String typesStr;
/*      */   
/*   65 */   private String onlybyme = "all";
/*      */   
/*   67 */   private String optmsg = null;
/*      */   
/*      */   private int optstatus;
/*      */   
/*      */   private int optchges;
/*      */   
/*      */   private String lsuidStr;
/*      */   private String fnameStr;
/*      */   private String lnameStr;
/*      */   private Date dobDate;
/*      */   private int dobGoodInd;
/*   78 */   private int matches = 0;
/*   79 */   private int querychanged = 0;
/*      */ 
/*      */ 
/*      */   
/*   83 */   private static final HashMap<String, String> myMap = new HashMap<>(4); static {
/*   84 */     myMap.put("autoid", "s.STUDENT_A_LSUID");
/*   85 */     myMap.put("autofn", "s.STUDENT_C_FIRSTNAME");
/*   86 */     myMap.put("autoln", "s.STUDENT_B_LASTNAME");
/*   87 */     myMap.put("autodob", "s.STUDENT_D_DOB");
/*      */   }
/*      */ 
/*      */   
/*   91 */   private static final HashMap<String, String> myMap2 = new HashMap<>(4); static {
/*   92 */     myMap2.put("autoid", "s.studentALsuid");
/*   93 */     myMap2.put("autofn", "s.studentCFirstname");
/*   94 */     myMap2.put("autoln", "s.studentBLastname");
/*   95 */     myMap2.put("autodob", "s.studentDDob");
/*      */   }
/*      */   
/*   98 */   private HashMap<String, String> queryby = new HashMap<>();
/*      */   
/*  100 */   private List<Student> studs = new ArrayList<>();
/*      */   
/*      */   private QueryStudModel dataModel;
/*      */   
/*      */   private Student selectedStud;
/*      */   
/*      */   private boolean editMode;
/*      */   
/*      */   private boolean onlynew = false;
/*      */   
/*      */   private boolean onlypick = true;
/*      */   
/*      */   private boolean onlyprt = false;
/*      */   
/*      */   private boolean onlyclient = false;
/*      */   
/*      */   private boolean allfisy = false;
/*      */   private boolean plusclosed = false;
/*  118 */   private int rowsPerPage = 20;
/*      */   
/*      */   public Querier() {
/*  121 */     this.dobDate = null;
/*  122 */     this.lsuidStr = "";
/*  123 */     this.fnameStr = "";
/*  124 */     this.lnameStr = "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkOpts(ActionEvent event) {
/*  154 */     log.info("xxxxxxxxxxxxx checkOpts() got types=%s", new Object[] { (this.studtypes == null) ? "null" : Arrays.toString((Object[])this.studtypes) });
/*  155 */     FacesMessage msg = null;
/*  156 */     String buttonId = event.getComponent().getClientId();
/*      */ 
/*      */ 
/*      */     
/*  160 */     if (this.studtypes == null || this.studtypes.length == 0) {
/*  161 */       msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "ChgQueryOpt.NoOpt");
/*      */     
/*      */     }
/*  164 */     else if (this.studtypes.length > 1 && Arrays.toString((Object[])this.studtypes).indexOf("ALL") > -1) {
/*  165 */       msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "ChgQueryOpt.DupAll");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  171 */     if (msg != null)
/*  172 */     { this.facesContext.addMessage(buttonId, msg);
/*  173 */       if (this.optstatus > 0) {
/*  174 */         this.optstatus++;
/*      */       } else {
/*  176 */         this.optstatus = 1;
/*      */       }  }
/*  178 */     else { this.optchges = 0;
/*  179 */       if (this.optstatus > 0) {
/*  180 */         this.optstatus = 0;
/*      */       } else {
/*  182 */         this.optstatus--;
/*      */       }  }
/*  184 */      this.optmsg = null;
/*  185 */     log.info("checkOpts() finished with optstatus=%d, optchanges=%d", new Object[] { Integer.valueOf(this.optstatus), Integer.valueOf(this.optchges) });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void stdTypeChanged(ValueChangeEvent e) {
/*  191 */     String opts = e.getNewValue().toString();
/*  192 */     if (opts == null || opts.isEmpty()) {
/*  193 */       this.studtypes = new String[] { "ALL" };
/*      */     } else {
/*  195 */       String[] types = opts.split(",");
/*  196 */       this.studtypes = types;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void optchanged(AjaxBehaviorEvent e) {
/*  201 */     this.optchges++;
/*      */   }
/*      */   
/*      */   public void chglsuidStr(AjaxBehaviorEvent e) {
/*  205 */     if (this.lsuidStr == null || this.lsuidStr.isEmpty() || this.lsuidStr.trim().isEmpty()) {
/*  206 */       this.queryby.remove("autoid");
/*  207 */       if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*      */     } 
/*  209 */     this.querychanged = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void chglnameStr(AjaxBehaviorEvent e) {
/*  214 */     if (this.lnameStr == null || this.lnameStr.isEmpty() || this.lnameStr.trim().isEmpty()) {
/*  215 */       this.queryby.remove("autoln");
/*  216 */       if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*      */     } 
/*  218 */     this.querychanged = 0;
/*      */   }
/*      */   public void chgfnameStr(AjaxBehaviorEvent e) {
/*  221 */     if (this.fnameStr == null || this.fnameStr.isEmpty() || this.fnameStr.trim().isEmpty()) {
/*  222 */       this.queryby.remove("autofn");
/*  223 */       if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*      */     } 
/*  225 */     this.querychanged = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dobchanged(AjaxBehaviorEvent e) {
/*  230 */     List<String> results = new ArrayList<>();
/*      */     
/*  232 */     String sql = composeSql("autodob");
/*  233 */     results = this.em.createNativeQuery(sql).getResultList();
/*  234 */     this.matches = (results == null) ? 0 : results.size();
/*      */     
/*  236 */     if (this.dobDate == null) { this.queryby.remove("autodob"); }
/*  237 */     else { this.queryby.put("autodob", "autodob"); }
/*      */     
/*  239 */     if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*  240 */     this.querychanged = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void querybtnclicked(AjaxBehaviorEvent e) {
/*  245 */     if (this.querychanged == 0) {
/*  246 */       this.querychanged++;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void refreshGridInstantly() {
/*  252 */     String sql = composeSql2(" ");
/*      */     
/*  254 */     List<Student> results = new ArrayList<>();
/*      */     
/*  256 */     results = this.em.createQuery(sql).getResultList();
/*  257 */     int amt = (results == null) ? 0 : results.size();
/*      */     
/*  259 */     int i = 0;
/*  260 */     this.studs.clear();
/*  261 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*      */     try {
/*  263 */       this.studs = results;
/*  264 */     } catch (Exception e) {
/*  265 */       e.printStackTrace();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  270 */     if (amt > 0 && this.studs.size() > 0) {
/*  271 */       Student student = this.studs.get(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  277 */     this.dataModel = new QueryStudModel(this.studs);
/*      */   }
/*      */   
/*      */   public void resetQuery(ActionEvent event) {
/*  281 */     this.studs.clear();
/*  282 */     this.dataModel = new QueryStudModel(this.studs);
/*  283 */     this.selectedStud = null;
/*  284 */     this.dobDate = null;
/*  285 */     this.lsuidStr = "";
/*  286 */     this.fnameStr = "";
/*  287 */     this.lnameStr = "";
/*  288 */     log.info("reset query conditions...");
/*      */   }
/*      */   
/*      */   public void queryStuds(ActionEvent event) {
/*  292 */     checkOpts(event);
/*  293 */     if (this.optstatus > 0)
/*      */       return; 
/*  295 */     if (this.querychanged == 0) {
/*  296 */       this.querychanged++;
/*      */     }
/*  298 */     String sql = composeSql2(" ");
/*      */     
/*  300 */     List<Student> results = new ArrayList<>();
/*      */   log.info(" Sara Create Query >>   "+this.em.createQuery(sql));
/*  302 */     results =this.em.createQuery(sql).getResultList();
 log.info("Sara results >>> "+ results);
/*  303 */     int amt = (results == null) ? 0 : results.size();
/*      */     
/*  305 */     int i = 0;
/*  306 */     this.studs.clear();
/*  307 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  337 */       for (int x = 0; x < amt; x++) {
/*  338 */         Student one = results.get(x);
/*      */         
/*  340 */         if (one.getStudentALsuid() == null) one.setStudentALsuid(""); 
/*  341 */         this.studs.add(one);
/*      */       }
/*      */     
/*  344 */     } catch (Exception e) {
/*  345 */       e.printStackTrace();
/*      */     } 
/*  347 */     log.info("queryStuds(): query=%s\n, results=%d, studs_list_size=%d", new Object[] { sql, Integer.valueOf(amt), Integer.valueOf(this.studs.size()) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  357 */     this.dataModel = new QueryStudModel(this.studs);
/*  358 */     this.selectedStud = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String composeSql2(String col_in) {
/*  364 */     if (col_in == null) return null; 
/*  365 */     String col = col_in.trim();
/*      */     
/*  367 */     StringBuilder sql = new StringBuilder(1580);
/*      */     
/*  369 */     HashMap<String, String> cols = new HashMap<>();
/*      */ 
/*      */     
/*  372 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autodob")) {
/*  373 */       sql.append("select ").append(myMap2.get(col)).append(" from Student s where upper(").append(myMap2.get(col)).append(") like (?) ");
/*  374 */     } else if (!col.isEmpty()) {
/*  375 */       sql.append("select ").append(myMap2.get(col)).append(" from Student s where 1=1 ");
/*      */     } else {
/*  377 */       sql.append("select s from Student s where 1=1 ");
/*      */     } 
/*      */     
/*  380 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autoid") && this.lsuidStr != null && !this.lsuidStr.isEmpty()) {
/*  381 */       sql.append(" and ").append(myMap2.get("autoid")).append(" like '").append(this.lsuidStr).append("' ");
/*  382 */     } else if (col.isEmpty() && this.lsuidStr != null && !this.lsuidStr.isEmpty()) {
/*  383 */       sql.append(" and ").append(myMap2.get("autoid")).append(" like '").append(this.lsuidStr).append("' ");
/*      */     } 
/*      */ 
/*      */     
/*  387 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autofn") && this.fnameStr != null && !this.fnameStr.isEmpty()) {
/*  388 */       sql.append(" and upper(").append(myMap2.get("autofn")).append(") like upper('").append(this.fnameStr).append("') ");
/*  389 */     } else if (col.isEmpty() && this.fnameStr != null && !this.fnameStr.isEmpty()) {
/*  390 */       sql.append(" and upper(").append(myMap2.get("autofn")).append(") like upper('").append(this.fnameStr).append("') ");
/*      */     } 
/*      */     
/*  393 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autoln") && this.lnameStr != null && !this.lnameStr.isEmpty()) {
/*  394 */       sql.append(" and upper(").append(myMap2.get("autoln")).append(") like upper('").append(this.lnameStr).append("') ");
/*  395 */     } else if (col.isEmpty() && this.lnameStr != null && !this.lnameStr.isEmpty()) {
/*  396 */       sql.append(" and upper(").append(myMap2.get("autoln")).append(") like upper('").append(this.lnameStr).append("') ");
/*      */     } 
/*      */     
/*  399 */     if (this.dobDate != null) {
/*  400 */       SimpleDateFormat sdf = new SimpleDateFormat(this.ref.getDateInputShowStr());
/*  401 */       sql.append(" and ").append(myMap2.get("autodob")).append(" = '").append(sdf.format(this.dobDate)).append("' ");
/*      */     } 
/*      */ 
/*      */     
/*  405 */     int user = this.login.getCurrentUser().getUserid().intValue();
/*  406 */     switch (this.onlybyme) { case "my":
/*  407 */         sql.append(" and s.counselorId=").append(user).append(" ");
/*      */         break;
/*      */ 
/*      */       
/*      */       case "others":
/*  412 */         sql.append(" and s.counselorId<>").append(user).append(" ");
/*      */         break; }
/*      */     
/*  415 */     if (this.onlynew) {
/*  416 */       sql.append(" and s.dup=0");
/*      */     }
/*  418 */     if (this.onlypick) {
/*  419 */       sql.append(" and s.pickupInd=1");
/*      */     }
/*  421 */     if (this.onlyprt) {
/*  422 */       sql.append(" and s.prtTimes >0");
/*      */     }
/*  424 */     if (this.onlyclient) {
/*  425 */       sql.append(" and s.clientId=").append(this.ref.getClientid());
/*      */     }
/*  427 */     if (!this.allfisy) {
/*  428 */       sql.append(" and s.studentFisy=").append(this.ref.getFiscal_year());
/*      */     }
/*  430 */     log.info("************************** QUERIER get para allfisy=%s, while ref fisy=%d", new Object[] { Boolean.valueOf(this.allfisy), Integer.valueOf(this.ref.getFiscal_year()) });
///*  431 */     if (!this.plusclosed) { //this logic is not clear 
///*  432 */       sql.append(" and s.ncStdInd=0");
///*      */     }
/*  434 */     if (this.studtypes != null && this.studtypes.length > 0 && !this.studtypes[0].equals("ALL"))
/*      */     {
/*  436 */       sql.append(matchStudAcademicOpt(this.studtypes));
/*      */     }
/*      */ 
/*      */ 
/*      */  //String sql_="select s from Student s where 1=1  and s.studentALsuid like '984517'";
/*      */     log.info("Student Query >>> "+ sql);
 log.info("Student Query2 >>> "+ sql);
/*  442 */     return sql.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String composeSql(String col_in) {
/*  448 */     if (col_in == null) return null; 
/*  449 */     String col = col_in.trim();
/*      */     
/*  451 */     StringBuilder sql = new StringBuilder(1580);
/*      */     
/*  453 */     HashMap<String, String> cols = new HashMap<>();
/*      */ 
/*      */     
/*  456 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autodob")) {
/*  457 */       sql.append("select distinct ").append(myMap.get(col)).append(" from student s where upper(").append(myMap.get(col)).append(") like (?) ");
/*  458 */     } else if (!col.isEmpty()) {
/*  459 */       sql.append("select distinct ").append(myMap.get(col)).append(" from student s where 1=1 ");
/*      */     } else {
/*  461 */       sql.append("select s.STUDENT_NUMB, s.STUDENT_FISY, s.STUDENT_A_LSUID, s.STUDENT_B_LASTNAME, s.STUDENT_C_FIRSTNAME, s.STUDENT_D_DOB, s.STUDENT_F_PHONE, s.STUDENT_BV_DOE, s.STUDENT_BZ_UPLOADED, s.STUDENT_STUD_TYPE, s.STUDENT_UPLOAD_DATE, s.COUNSELOR_ID,s.STUDENT_I_STATE, s.STUDENT_J_ZIP from student s where 1=1 ");
/*      */     } 
/*      */     
/*  464 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autoid") && this.lsuidStr != null && !this.lsuidStr.isEmpty()) {
/*  465 */       sql.append(" and ").append(myMap.get("autoid")).append(" like '").append(this.lsuidStr).append("' ");
/*  466 */     } else if (col.isEmpty() && this.lsuidStr != null && !this.lsuidStr.isEmpty()) {
/*  467 */       sql.append(" and ").append(myMap.get("autoid")).append(" like '").append(this.lsuidStr).append("' ");
/*      */     } 
/*      */ 
/*      */     
/*  471 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autofn") && this.fnameStr != null && !this.fnameStr.isEmpty()) {
/*  472 */       sql.append(" and upper(").append(myMap.get("autofn")).append(") like upper('").append(this.fnameStr).append("') ");
/*  473 */     } else if (col.isEmpty() && this.fnameStr != null && !this.fnameStr.isEmpty()) {
/*  474 */       sql.append(" and upper(").append(myMap.get("autofn")).append(") like upper('").append(this.fnameStr).append("') ");
/*      */     } 
/*      */     
/*  477 */     if (!col.isEmpty() && !col.equalsIgnoreCase("autoln") && this.lnameStr != null && !this.lnameStr.isEmpty()) {
/*  478 */       sql.append(" and upper(").append(myMap.get("autoln")).append(") like upper('").append(this.lnameStr).append("') ");
/*  479 */     } else if (col.isEmpty() && this.lnameStr != null && !this.lnameStr.isEmpty()) {
/*  480 */       sql.append(" and upper(").append(myMap.get("autoln")).append(") like upper('").append(this.lnameStr).append("') ");
/*      */     } 
/*      */     
/*  483 */     if (this.dobDate != null) {
/*  484 */       SimpleDateFormat sdf = new SimpleDateFormat(this.ref.getDateInputShowStr());
/*  485 */       sql.append(" and ").append(myMap.get("autodob")).append(" = '").append(sdf.format(this.dobDate)).append("' ");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  494 */     int user = this.login.getCurrentUser().getUserid().intValue();
/*  495 */     switch (this.onlybyme) { case "my":
/*  496 */         sql.append(" and s.counselor_id=").append(user).append(" ");
/*      */         break;
/*      */ 
/*      */       
/*      */       case "others":
/*  501 */         sql.append(" and s.counselor_id<>").append(user).append(" ");
/*      */         break; }
/*      */ 
/*      */     
/*  505 */     if (this.onlynew) {
/*  506 */       sql.append(" and s.dup=0");
/*      */     }
/*  508 */     if (this.onlypick) {
/*  509 */       sql.append(" and s.pickup_Ind=1");
/*      */     }
/*  511 */     if (this.onlyprt) {
/*  512 */       sql.append(" and s.prt_Times >0");
/*      */     }
/*  514 */     if (this.onlyclient) {
/*  515 */       sql.append(" and s.client_Id=").append(this.ref.getClientid());
/*      */     }
/*  517 */     if (!this.allfisy) {
/*  518 */       sql.append(" and s.student_fisy=").append(this.ref.getFiscal_year());
/*      */     }
/*  520 */     if (!this.plusclosed) {
/*  521 */       sql.append(" and s.nc=0");
/*      */     }
/*  523 */     if (this.studtypes != null && this.studtypes.length > 0 && !this.studtypes[0].equals("ALL"))
/*      */     {
/*      */       
/*  526 */       sql.append(matchStudAcademicOpt(this.studtypes));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  532 */     return sql.toString();
/*      */   }
/*      */   
/*      */   private String matchStudAcademicOpt(String[] opts) {
/*  536 */     if (opts == null || opts.length == 0 || opts[0].equalsIgnoreCase("ALL")) return ""; 
/*  537 */     StringBuilder sb = new StringBuilder(128);
/*  538 */     String[] vals = this.ref.getStudtype_vals();
/*  539 */     List<String> list = Arrays.asList(vals);
/*  540 */     int start = 0;
/*  541 */     for (String one : opts) {
/*  542 */       if (list.contains(one)) {
/*  543 */         if (start == 0) sb.append(" and (  "); 
/*  544 */         switch (one) { case "FR":
/*      */           case "F2":
/*      */           case "SO":
/*      */           case "JR":
/*      */           case "SR":
/*      */           case "MBA":
/*  550 */             sb.append((start > 0) ? " or " : "  ").append(" s.studentUAcademic='").append(one).append("'"); break;
/*      */           case "UD":
/*  552 */             sb.append((start > 0) ? " or " : "  ").append(" UPPER(s.studentLIntlStud)='NO'"); break;
/*      */           case "UI":
/*  554 */             sb.append((start > 0) ? " or " : "  ").append(" UPPER(s.studentLIntlStud)='YES'"); break;
/*      */           default:
/*  556 */             sb.append((start > 0) ? " or " : "  ").append(" s.studentStudType <>'UGFY'");
/*      */             break; }
/*      */         
/*  559 */         start++;
/*      */       } 
/*  561 */     }  if (start > 0) {
/*  562 */       sb.append(")");
/*  563 */       return sb.toString();
/*      */     } 
/*  565 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> autolsuidStr(String query) {
/*  570 */     this.querychanged = 0;
/*  571 */     List<String> results = new ArrayList<>();
/*  572 */     if (query != null && !query.isEmpty() && !query.trim().isEmpty()) {
/*  573 */       query = query.toUpperCase();
/*  574 */       if (!query.endsWith("%") && !query.endsWith("_")) query = query + "%"; 
/*  575 */       this.queryby.put("autoid", "autoid");
/*      */     } else {
/*  577 */       this.queryby.remove("autoid");
/*  578 */       if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*  579 */       return results;
/*      */     } 
/*  581 */     String sql = composeSql("autoid");
/*  582 */     results = this.em.createNativeQuery(sql).setParameter(1, query).getResultList();
/*  583 */     this.matches = (results == null) ? 0 : results.size();
/*  584 */     return results;
/*      */   }
/*      */   public List<String> autofnameStr(String query) {
/*  587 */     this.querychanged = 0;
/*  588 */     List<String> results = new ArrayList<>();
/*  589 */     String para = "";
/*  590 */     if (query != null && !query.isEmpty() && !query.trim().isEmpty()) {
/*  591 */       para = query.toUpperCase();
/*  592 */       if (!para.endsWith("%") && !para.endsWith("_")) para = para + "%"; 
/*  593 */       this.queryby.put("autofn", "autofn");
/*      */     } else {
/*  595 */       this.queryby.remove("autofn");
/*  596 */       if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*  597 */       return results;
/*      */     } 
/*  599 */     String sql = composeSql("autofn");
/*  600 */     results = this.em.createNativeQuery(sql).setParameter(1, para).getResultList();
/*  601 */     this.matches = (results == null) ? 0 : results.size();
/*  602 */     return results;
/*      */   }
/*      */   public List<String> autolnameStr(String query) {
/*  605 */     this.querychanged = 0;
/*      */     
/*  607 */     List<String> results = new ArrayList<>();
/*  608 */     if (query != null && !query.isEmpty() && !query.trim().isEmpty()) {
/*  609 */       query = query.toUpperCase();
/*  610 */       if (!query.endsWith("%") && !query.endsWith("_")) query = query + "%"; 
/*  611 */       this.queryby.put("autoln", "autoln");
/*      */     } else {
/*  613 */       this.queryby.remove("autoln");
/*  614 */       if (this.queryby.isEmpty() && this.matches == 0) this.matches = -1; 
/*  615 */       return results;
/*      */     } 
/*  617 */     String sql = composeSql("autoln");
/*  618 */     results = this.em.createNativeQuery(sql).setParameter(1, query).getResultList();
/*  619 */     this.matches = (results == null) ? 0 : results.size();
/*  620 */     return results;
/*      */   }
/*      */ 
/*      */   
/*      */   public String optmsg() {
/*  625 */     return this.optmsg;
/*      */   }
/*      */   
/*      */   public String[] getStudtypes() {
/*  629 */     return this.studtypes;
/*      */   }
/*      */   
/*      */   public void setStudtypes(String[] studtypes) {
/*  633 */     this.studtypes = studtypes;
/*      */   }
/*      */   
/*      */   public String getOnlybyme() {
/*  637 */     return this.onlybyme;
/*      */   }
/*      */   
/*      */   public void setOnlybyme(String onlybyme) {
/*  641 */     this.onlybyme = onlybyme;
/*      */   }
/*      */   
/*      */   public int getMatches() {
/*  645 */     return this.matches;
/*      */   }
/*      */   
/*      */   public void setMatches(int matches) {
/*  649 */     this.matches = matches;
/*      */   }
/*      */   
/*      */   public int getOptstatus() {
/*  653 */     return this.optstatus;
/*      */   }
/*      */   
/*      */   public String getLsuidStr() {
/*  657 */     return this.lsuidStr;
/*      */   }
/*      */   
/*      */   public void setLsuidStr(String lsuidStr) {
/*  661 */     this.lsuidStr = lsuidStr;
/*      */   }
/*      */   
/*      */   public String getFnameStr() {
/*  665 */     return this.fnameStr;
/*      */   }
/*      */   
/*      */   public void setFnameStr(String fnameStr) {
/*  669 */     this.fnameStr = fnameStr;
/*      */   }
/*      */   
/*      */   public String getLnameStr() {
/*  673 */     return this.lnameStr;
/*      */   }
/*      */   
/*      */   public void setLnameStr(String lnameStr) {
/*  677 */     this.lnameStr = lnameStr;
/*      */   }
/*      */   
/*      */   public Date getDobDate() {
/*  681 */     return this.dobDate;
/*      */   }
/*      */   
/*      */   public void setDobDate(Date dobDate) {
/*  685 */     this.dobDate = dobDate;
/*      */   }
/*      */   
/*      */   public int getDobGoodInd() {
/*  689 */     return this.dobGoodInd;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTypesStr() {
/*  695 */     StringBuilder sb = new StringBuilder(128);
/*  696 */     sb.append(" ").append(this.ref.genLabelsofKeys(this.studtypes)).append(" of ").append(this.onlybyme).append(" estimates ");
/*  697 */     int in = 0;
/*  698 */     if (this.onlynew) {
/*  699 */       sb.append((in == 0) ? "(" : "").append("not uploaded");
/*  700 */       in++;
/*      */     } 
/*  702 */     if (this.onlypick) {
/*  703 */       sb.append((in == 0) ? "(" : ", ").append("active");
/*  704 */       in++;
/*      */     } 
/*  706 */     if (this.onlyprt) {
/*  707 */       sb.append((in == 0) ? "(" : ", ").append("delivered");
/*  708 */       in++;
/*      */     } 
/*  710 */     if (this.onlyclient) {
/*  711 */       sb.append((in == 0) ? "(" : ", ").append("local");
/*  712 */       in++;
/*      */     } 
/*  714 */     if (this.allfisy) {
/*  715 */       sb.append((in == 0) ? "(" : ", ").append("history");
/*  716 */       in++;
/*      */     } 
/*  718 */     if (this.plusclosed) {
/*  719 */       sb.append((in == 0) ? "(" : ", ").append("include those not coming");
/*  720 */       in++;
/*      */     } 
/*  722 */     sb.append((in == 0) ? "" : ")");
/*  723 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public int getOptchges() {
/*  727 */     return this.optchges;
/*      */   }
/*      */   
/*      */   public void setOptchges(int optchged) {
/*  731 */     this.optchges = optchged;
/*      */   }
/*      */   
/*      */   public List<Student> getStuds() {
/*  735 */     return this.studs;
/*      */   }
/*      */ 
/*      */   
/*      */   public Student getSelectedStud() {
/*  740 */     return this.selectedStud;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSelectedStud(Student theSelectedStud) {
/*  753 */     this.selectedStud = theSelectedStud;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEditMode() {
/*  808 */     return this.editMode;
/*      */   }
/*      */   
/*      */   public void setEditMode(boolean editMode) {
/*  812 */     this.editMode = editMode;
/*      */   }
/*      */   
/*      */   public QueryStudModel getDataModel() {
/*  816 */     if (this.dataModel == null || this.dataModel.getRowCount() == -1) {
/*  817 */       log.info("=========== getDataModel() found null obj");
/*  818 */       if (this.studs != null) {
/*  819 */         this.dataModel = new QueryStudModel(this.studs);
/*      */         
/*  821 */         log.info("=========== getDataModel() init obj use existing studs which size=%d", new Object[] { Integer.valueOf(this.studs.size()) });
/*      */       } else {
/*  823 */         queryStuds(null);
/*      */         
/*  825 */         log.info("=========== getDataModel() init obj use new studs which size=%d", new Object[] { Integer.valueOf(this.studs.size()) });
/*      */       } 
/*      */     } 
/*      */     
/*  829 */     return this.dataModel;
/*      */   }
/*      */   
/*      */   public int getQuerychanged() {
/*  833 */     return this.querychanged;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onRowSelect(SelectEvent event) {
/*  838 */     Student student = (Student)event.getObject();
/*  839 */     this.selectedStud = student;
/*      */ 
/*      */ 
/*      */     
/*  843 */     log.info("########## onRowSelect() student: [%s] ##########", new Object[] { this.selectedStud.getRecid() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sortStudByFirstName_user_guide(Student std1, Student std2) {
/*  855 */     log.info("$$$$$$$$$$$$$$ sorting");
/*  856 */     int i = 0;
/*  857 */     if (std1 == null) {
/*      */ 
/*      */       
/*  860 */       if (std2 == null) {
/*  861 */         i = 0;
/*      */       } else {
/*  863 */         i = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  875 */     else if (std2 == null) {
/*  876 */       i = 1;
/*      */     } else {
/*  878 */       String str1 = std1.getStudentCFirstname();
/*  879 */       String str2 = std2.getStudentCFirstname();
/*  880 */       if (str1 != null && str2 != null) {
/*  881 */         i = str1.compareToIgnoreCase(str2);
/*  882 */       } else if (str1 == null && str2 == null) {
/*  883 */         i = 0;
/*  884 */       } else if (str1 == null) {
/*  885 */         i = -1;
/*      */       } else {
/*  887 */         i = 1;
/*      */       } 
/*      */     } 
/*      */     
/*  891 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public int sortStudByFirstName(Object obj1, Object obj2) {
/*  896 */     log.info("$$$$$$$$$$$$$$ sorting");
/*      */     
/*  898 */     int i = 0;
/*  899 */     if (obj1 == null) {
/*      */ 
/*      */       
/*  902 */       if (obj2 == null) {
/*  903 */         i = 0;
/*      */       } else {
/*  905 */         i = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  918 */     else if (obj2 == null) {
/*  919 */       i = 1;
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/*  927 */       String str1 = (String)obj1;
/*  928 */       String str2 = (String)obj2;
/*      */       
/*  930 */       if (str1 != null && str2 != null) {
/*  931 */         i = str1.compareToIgnoreCase(str2);
/*  932 */       } else if (str1 == null && str2 == null) {
/*  933 */         i = 0;
/*  934 */       } else if (str1 == null) {
/*  935 */         i = -1;
/*      */       } else {
/*  937 */         i = 1;
/*      */       } 
/*      */     } 
/*      */     
/*  941 */     return i;
/*      */   }
/*      */   
/*      */   public boolean isOnlynew() {
/*  945 */     return this.onlynew;
/*      */   }
/*      */   
/*      */   public void setOnlynew(boolean onlynew) {
/*  949 */     this.onlynew = onlynew;
/*      */   }
/*      */   
/*      */   public boolean isOnlypick() {
/*  953 */     return this.onlypick;
/*      */   }
/*      */   
/*      */   public void setOnlypick(boolean onlypick) {
/*  957 */     this.onlypick = onlypick;
/*      */   }
/*      */   
/*      */   public boolean isOnlyprt() {
/*  961 */     return this.onlyprt;
/*      */   }
/*      */   
/*      */   public void setOnlyprt(boolean onlyprt) {
/*  965 */     this.onlyprt = onlyprt;
/*      */   }
/*      */   
/*      */   public boolean isOnlyclient() {
/*  969 */     return this.onlyclient;
/*      */   }
/*      */   
/*      */   public void setOnlyclient(boolean onlyclient) {
/*  973 */     this.onlyclient = onlyclient;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRowsPerPage() {
/*  978 */     return this.rowsPerPage;
/*      */   }
/*      */   
/*      */   public void setRowsPerPage(int rowsPerPage) {
/*  982 */     this.rowsPerPage = rowsPerPage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onPaginate(PageEvent event) {
/*  989 */     this.rowsPerPage = ((DataTable)event.getSource()).getRows();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void procInfoBtn(ActionEvent actionEvent) {
/* 1000 */     log.info(",,,,,,,, procInfoBtn() action listener got selected stud recid=%s", new Object[] { (this.selectedStud == null) ? "N/A" : this.selectedStud.getRecid() });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     this.dataModel.getRowIndex();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void procIdBtn(ActionEvent actionEvent) {
/* 1017 */     log.info(",,,,,,,, procIdBtn() action listener got selected stud recid=%s", new Object[] { (this.selectedStud == null) ? "N/A" : this.selectedStud.getRecid() });
/* 1018 */     this.dataModel.getRowIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onRowSelect(ActionEvent actionEvent) {
/* 1023 */     RequestContext context = RequestContext.getCurrentInstance();
/*      */     
/* 1025 */     context.addCallbackParam("select", (this.selectedStud == null) ? Integer.valueOf(0) : this.selectedStud.getRecid());
/* 1026 */     context.execute("rec=" + ((this.selectedStud == null) ? Integer.valueOf(0) : this.selectedStud.getRecid()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1032 */     context.update("auth-display");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int sortByID2(Student std1, Student std2) {
/* 1038 */     int res = 0;
/*      */     
/* 1040 */     String id1 = std1.getStudentALsuid();
/* 1041 */     id1 = this.ref.isEmp(id1) ? "null" : id1.trim();
/* 1042 */     String id2 = std2.getStudentALsuid();
/* 1043 */     id2 = this.ref.isEmp(id2) ? "null" : id2.trim();
/*      */ 
/*      */     
/* 1046 */     if (id1.equals("null") && !id2.equals("null")) {
/* 1047 */       res = -1;
/* 1048 */     } else if (!id1.equals("null") && id2.equals("null")) {
/* 1049 */       res = 1;
/*      */     } else {
/* 1051 */       res = id1.compareToIgnoreCase(id2);
/* 1052 */       if (res == 0) {
/*      */         
/* 1054 */         long dom1 = std1.getDdom();
/* 1055 */         long doe1 = std1.getDdoe();
/* 1056 */         long dom2 = std1.getDdom();
/* 1057 */         long doe2 = std1.getDdoe();
/* 1058 */         long last1 = (dom1 > 0L) ? dom1 : doe1;
/* 1059 */         long last2 = (dom2 > 0L) ? dom2 : doe2;
/* 1060 */         res = (int)(last1 - last2);
/* 1061 */         if (res != 0) res = (res > 0) ? 1 : -1; 
/*      */       } 
/*      */     } 
/* 1064 */     return res;
/*      */   }
/*      */   
/*      */   public int sortByID(Object obj1, Object obj2) {
/* 1068 */     int res = 0;
/*      */     
/* 1070 */     String id1 = (String)obj1;
/* 1071 */     String id2 = (String)obj2;
/*      */     
/* 1073 */     id1 = this.ref.isEmp(id1) ? "null" : id1.trim();
/* 1074 */     id2 = this.ref.isEmp(id2) ? "null" : id2.trim();
/*      */ 
/*      */     
/* 1077 */     if (id1.equals("null") && !id2.equals("null")) {
/* 1078 */       res = -1;
/* 1079 */     } else if (!id1.equals("null") && id2.equals("null")) {
/* 1080 */       res = 1;
/*      */     } else {
/* 1082 */       res = id1.compareToIgnoreCase(id2);
/* 1083 */       if (res != 0) res = (res > 0) ? 1 : -1; 
/*      */     } 
/* 1085 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllfisy() {
/* 1092 */     return this.allfisy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllfisy(boolean allfisy) {
/* 1099 */     this.allfisy = allfisy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPlusclosed() {
/* 1106 */     return this.plusclosed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlusclosed(boolean plusclosed) {
/* 1113 */     this.plusclosed = plusclosed;
/*      */   }
/*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Querier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */