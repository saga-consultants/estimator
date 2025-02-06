/*      */ package edu.lsu.estimator;
/*      */ 
/*      */ import com.kingombo.slf5j.Logger;
/*      */ import com.kingombo.slf5j.LoggerFactory;
/*      */ import edu.lsu.estimator.AppReference;
/*      */ import edu.lsu.estimator.Estimator;
/*      */ import edu.lsu.estimator.PackFunctions;
/*      */ import edu.lsu.estimator.Student;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.Field;
/*      */ import java.math.BigDecimal;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import javax.inject.Inject;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.annotation.WebServlet;
/*      */ import javax.servlet.http.HttpServlet;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.joda.time.DateMidnight;
/*      */ import org.joda.time.ReadableInstant;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @WebServlet(name = "echo", urlPatterns = {"/echo"}, loadOnStartup = 1)
/*      */ public class NetFeeder<E>
/*      */   extends HttpServlet
/*      */ {
/*   38 */   private static final Logger log = LoggerFactory.getLogger();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   44 */   private DateFormat dfl = new SimpleDateFormat("MMddyyyy");
/*   45 */   private DateFormat dfs = new SimpleDateFormat("MMddyy");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   55 */   private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   65 */   private String[] para_name = new String[] { "std_lsuid", "std_sex", "std_fname", "std_lname", "std_dob", "std_email", "std_phone", "std_street", "std_city", "std_state", "std_zip", "std_country", "std_apt", "std_intl", "std_marry", "std_sda", "std_indept", "std_returning", "std_nc", "std_preshcool", "std_gpa", "std_sat_math", "std_act", "std_merit", "std_major", "std_grade", "std_aluminus", "std_aliminumb", "std_fafsa", "std_calgrant", "std_ealsu", "std_eanonlsu", "std_dorm", "std_efc", "std_noloans", "fisy", "ea_lsu_perc", "ea_nonlsu_perc", "reviewerid", "std_homeincome", "std_homeasset", "std_homemems", "efc_amt", "noncal_notes", "noncal_amt", "nonlsu_notes", "nonlsu_amt", "ship1note", "ship1amt", "ship2note", "ship2amt", "ship3note", "ship3amt", "ship4note", "ship4amt", "ship5note", "ship5amt", "ship6note", "ship6amt", "std_calgranta", "std_calgrantb", "calgrant_adjust", "notes_pub", "notes_pri", "inc_subloan", "inc_unsubloan", "inc_fws" };
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
/*   82 */   private int[] para_type = new int[] { 3, 3, 3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 3, 2, 1, 1, 3, 3, 3, 3, 1, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 3, 2, 2, 1, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 1, 1, 5, 3, 3, 5, 5, 5 };
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
/*   97 */   private int[] attr_type = new int[] { 3, 3, 3, 3, 6, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 0, 0, 3, 2, 1, 1, 3, 3, 3, 3, 1, 4, 4, 4, 2, 4, 4, 4, 1, 1, 1, 3, 2, 2, 1, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 1, 1, 4, 3, 3, 4, 4, 4 };
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
/*  110 */   private String[] fields = new String[] { "studentALsuid", "sex", "studentCFirstname", "studentBLastname", "studentDDob", "studentEEmail", "studentFPhone", "studentGStreet", "studentHCity", "studentIState", "studentJZip", "studentKCountry", "homeAddrApt", "studentLIntlStud", "studentMMarry", "studentNSda", "studentYIndept", "returnStdInd", "ncStdInd", "studentOLastSchool", "studentPGpa", "studentQSat", "studentRAct", "studentSMerit", "studentTMajor", "studentUAcademic", "studentVFamily", "homecostudies", "studentXFafsa", "studentZCalgrant", "studentAhLsuAllowrance", "studentAgNonlsuAllowrance", "studentWDorm", "indEfc", "indExcloans", "studentFisy", "ea_lsu_perc", "ea_nonlsu_perc", "puser_id", "studentAdFamilyIncome", "studentAeFamilyAsset", "studentAcFamilySize", "studentAfFamilyContrib", "studentAjHomeState", "studentAkNoncalGrant", "studentAlOutScholarships", "studentAmOutScholarshipAmt", "studentAtScholarship1Note", "studentAuScholarship1Amt", "studentAwScholarship2Note", "studentAxScholarship2Amt", "studentAzScholarship3Note", "studentBaScholarship3Amt", "studentBcScholarship4Note", "studentBdScholarship4Amt", "studentBfScholarship5Note", "studentBgScholarship5Amt", "studentBiScholarship6Note", "studentBjScholarship6Amt", "studentAaCalgrantA", "studentAbCalgrantB", "adjCalgrantInd", "studentAnPubNotes", "studentAoPriNotes", "studentApSubLoans", "studentAqUnsubLoans", "studentArFws" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int v_type;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int v_low;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int v_high;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String v_name;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object v_val;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String emp_acct;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   StringBuilder ss = new StringBuilder(120000);
/*      */ 
/*      */   
/*      */   @Inject
/*      */   AppReference ref;
/*      */ 
/*      */   
/*      */   private PackFunctions calc;
/*      */ 
/*      */   
/*      */   @Inject
/*      */   private Estimator est;
/*      */ 
/*      */   
/*      */   protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
/*  161 */     long start = System.nanoTime();
/*      */ 
/*      */     
/*  164 */     this.est.newPEstimate();
/*  165 */     Student stud = this.est.getStud();
/*      */ 
/*      */     
/*  168 */     this.calc = this.est.getCalc();
/*      */     
/*  170 */     this.ref.setStdFixInstAids(stud);
/*  171 */     stud.setStudentFisy(this.ref.getFiscal_year());
/*      */     
/*  173 */     HashMap<String, Field> fmap = new HashMap<>(300);
/*  174 */     Class<?> stdcls = stud.getClass();
/*  175 */     Field[] attrs = stdcls.getDeclaredFields();
/*  176 */     for (Field one : attrs) {
/*  177 */       fmap.put(one.getName(), one);
/*      */     }
/*      */ 
/*      */     
/*  181 */     PrintWriter writer = res.getWriter();
/*      */     
/*  183 */     StringBuilder sb = new StringBuilder(102400);
/*  184 */     int i = 0, v = 0, pos = 0, test_ind = 0;
/*      */     
/*  186 */     int save_ind = 0;
/*  187 */     String save_msg = "";
/*      */     
/*  189 */     HashMap<Integer, Object> map = new HashMap<>(300);
/*  190 */     HashMap<String, String> vmap = new HashMap<>(300);
/*  191 */     String pv = null;
/*      */     
/*  193 */     for (Enumeration<E> f = (Enumeration<E>) req.getParameterNames(); f.hasMoreElements(); ) {
/*  194 */       String pn = new String(f.nextElement().toString());
/*  195 */       pv = req.getParameter(pn);
/*      */       
/*  197 */       if (pn.equalsIgnoreCase("test")) test_ind = 1; 
/*  198 */       if (pn.equalsIgnoreCase("save_sfs") && pv.trim().equals("1")) save_ind = 1;
/*      */       
/*  200 */       if ((pos = chkParaByName(pn)) < 0 || map.containsKey(Integer.valueOf(pos)) || 
/*  201 */         chkParaByVal(pos, pv, map) == 0)
/*  202 */         continue;  vmap.put(pn.toLowerCase(), pv.trim());
/*  203 */       v++;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  210 */     syncParaToAttr(map, fmap, stud);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  215 */     pv = vmap.get("std_indept");
/*  216 */     if (pv != null && pv.equalsIgnoreCase("false")) {
/*  217 */       pv = vmap.get("std_marry");
/*  218 */       if (pv != null && pv.equals("true")) stud.setStudentYIndept("yes");
/*      */       
/*  220 */       pv = stud.getStudentDDob();
/*  221 */       if (pv != null && !pv.isEmpty()) {
/*  222 */         DateMidnight dm = null;
/*      */         try {
/*  224 */           dm = new DateMidnight(this.df.parse(pv));
/*  225 */           DateMidnight due = new DateMidnight(this.ref.getIndept_dob());
/*      */           
/*  227 */           if (!dm.isAfter((ReadableInstant)due)) {
/*  228 */             stud.setStudentYIndept("yes");
/*      */           }
/*  230 */         } catch (ParseException parseException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  236 */     pv = vmap.get("std_homemems");
/*  237 */     if (pv != null && !pv.isEmpty()) {
/*  238 */       int n = Integer.parseInt(pv);
/*  239 */       if (n < 1) n = 1; 
/*  240 */       stud.setStudentAcFamilySize(Integer.valueOf(n));
/*      */     } else {
/*  242 */       stud.setStudentAcFamilySize(Integer.valueOf(1));
/*      */     } 
/*      */ 
/*      */     
/*  246 */     pv = vmap.get("std_dorm");
/*  247 */     String pv2 = vmap.get("std_eanonlsu");
/*  248 */     if (pv2 != null && pv2.equalsIgnoreCase("true")) {
/*  249 */       stud.setStudentAgNonlsuAllowrance(new BigDecimal(1));
/*  250 */       stud.setStudentAiEduAllowPer((new BigDecimal(this.calc.getEaNonLsuPercentageByDorm((pv != null && pv.equalsIgnoreCase("true"))))).divide(new BigDecimal(100)));
/*  251 */       stud.setIndEanonlsu("yes");
/*      */     } else {
/*  253 */       stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
/*  254 */       stud.setStudentAgNonlsuAllowrance(new BigDecimal(0));
/*  255 */       stud.setIndEanonlsu("no");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  260 */     pv = vmap.get("std_noloans");
/*  261 */     if (pv != null && pv.equalsIgnoreCase("true")) {
/*  262 */       stud.setStudentApSubLoans("No");
/*  263 */       stud.setStudentAqUnsubLoans("No");
/*      */     } else {
/*  265 */       stud.setStudentApSubLoans("Yes");
/*  266 */       stud.setStudentAqUnsubLoans("Yes");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  271 */     pv = vmap.get("std_efc");
/*  272 */     pv2 = vmap.get("std_fafsa");
/*  273 */     if (pv != null && pv.equalsIgnoreCase("true") && 
/*  274 */       pv2 != null && pv2.equalsIgnoreCase("false")) {
/*  275 */       stud.setStudentXFafsa("yes");
/*      */     }
/*      */     
/*  278 */     if ((pv != null && pv.equalsIgnoreCase("true")) || (pv2 != null && pv2.equalsIgnoreCase("true"))) {
/*  279 */       pv = vmap.get("std_intl");
/*  280 */       if (pv != null && pv.equalsIgnoreCase("true")) {
/*  281 */         stud.setStudentLIntlStud("no");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  286 */     pv = vmap.get("amt_efc");
/*  287 */     if (pv != null && !pv.isEmpty()) {
/*  288 */       int n = Integer.parseInt(pv);
/*  289 */       if (n < 0) {
/*  290 */         n = 0;
/*  291 */         stud.setStudentAfFamilyContrib(Integer.valueOf(n));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  296 */     pv = vmap.get("std_gpa");
/*  297 */     if (pv == null) {
/*  298 */       stud.setStudentPGpa(BigDecimal.ZERO);
/*      */     }
/*      */     
/*  301 */     pv = vmap.get("std_grade");
/*  302 */     if (pv == null) {
/*  303 */       stud.setStudentUAcademic("FR");
/*      */     }
/*      */     
/*  306 */     pv = vmap.get("std_sex");
/*  307 */     if (pv == null) {
/*  308 */       stud.setSex("N");
/*      */     }
/*      */ 
/*      */     
/*  312 */     this.calc.init();
/*      */ 
/*      */     
/*  315 */     pv = vmap.get("std_calgrant");
/*  316 */     if (pv != null && pv.equals("true")) {
/*  317 */       pv = vmap.get("calgrant_adjust");
/*  318 */       if (pv != null && pv.equals("true")) {
/*  319 */         this.calc.setAdjustCalGrantAmtInd(true);
/*  320 */         pv = vmap.get("std_calgranta");
/*  321 */         if (pv != null && !pv.isEmpty()) {
/*  322 */           stud.setStudentAaCalgrantA(Integer.valueOf(Integer.parseInt(pv)));
/*  323 */           stud.setStudentAbCalgrantB(Integer.valueOf(0));
/*      */         } else {
/*  325 */           pv = vmap.get("std_calgrantb");
/*  326 */           if (pv != null && !pv.isEmpty()) {
/*  327 */             stud.setStudentAbCalgrantB(Integer.valueOf(Integer.parseInt(pv)));
/*  328 */             stud.setStudentAaCalgrantA(Integer.valueOf(0));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  337 */     this.calc.refreshCalc(stud);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  343 */     writer.write(echoRes(this.calc, stud));
/*      */ 
/*      */     
/*  346 */     if (save_ind > 0) {
/*      */       
/*  348 */       String[] save_needs = { "std_lsuid", "std_fname", "std_lname", "std_dob", "std_email", "std_phone", "std_state", "std_zip", "std_gpa", "fisy", "reviewerid" };
/*  349 */       String[] save_need1stmsg = { "Student ID", "First Name", "Last Name", "Date of Birth", "E-mail", "Phone NUmber", "State", "ZIP Code", "GPA", "FISY", "Reviewer ID" };
/*      */       
/*  351 */       int j = 0;
/*  352 */       for (int m = 0; m < save_needs.length; m++) {
/*  353 */         String one = save_needs[m];
/*  354 */         if (!vmap.containsKey(one)) {
/*  355 */           save_msg = "SAVE_SFS FAILED. missed parameter " + save_need1stmsg[m];
/*  356 */           j = 1;
/*      */           break;
/*      */         } 
/*      */       } 
/*  360 */       if (j == 0) {
/*      */         try {
/*  362 */           int fisy = Integer.valueOf(vmap.get("fisy")).intValue();
/*  363 */           if (this.ref.getFiscal_year() != fisy) {
/*  364 */             save_msg = "SAVE_SFS FAILED. wrong FISY value";
/*  365 */             j++;
/*      */           } 
/*  367 */         } catch (Exception e) {
/*  368 */           save_msg = "SAVE_SFS FAILED. invalid FISY value";
/*  369 */           j++;
/*      */         } 
/*      */ 
/*      */         
/*  373 */         if (j == 0 && vmap.containsKey("std_ealsu") && (!vmap.containsKey("ea_lsu_perc") || Integer.valueOf((String)vmap.get("ea_lsu_perc")).intValue() > 100 || Integer.valueOf(vmap.get("ea_lsu_perc")).intValue() < 0)) {
/*  374 */           save_msg = "SAVE_SFS FAILED. missed LASU EA percentage.";
/*  375 */           j++;
/*      */         } 
/*  377 */         if (j == 0) {
/*  378 */           String stmp = vmap.get("std_sex");
/*  379 */           if (stmp != null && !stmp.equalsIgnoreCase("F") && !stmp.equalsIgnoreCase("M") && !stmp.equalsIgnoreCase("N")) {
/*  380 */             save_msg = "SAVE_SFS FAILED. invalid gender value.";
/*  381 */             j++;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  387 */       if (j == 0) {
/*  388 */         this.est.setModStud(null);
/*  389 */         this.est.setModflag(0);
/*      */         
/*  391 */         stud.setPrtTimes(Integer.valueOf(0));
/*  392 */         stud.setPdfs(null);
/*  393 */         stud.setCounselorId(Integer.valueOf(this.ref.getSys_counselor_id()));
/*  394 */         stud.setCounselorOrig(Integer.valueOf(this.ref.getSys_counselor_id()));
/*  395 */         stud.setStudentBuOrigCounselor("MYCAMPUS PORTAL");
/*      */ 
/*      */         
/*  398 */         this.est.setStd_fn(stud.getStudentCFirstname());
/*  399 */         this.est.setStd_ln(stud.getStudentBLastname());
/*  400 */         this.est.setStd_dob(stud.getStudentDDob());
/*  401 */         save_msg = this.est.savepstudinfo();
/*  402 */         if (!save_msg.equalsIgnoreCase("SAVED")) {
/*  403 */           save_msg = "SAVE_SFS FAILED. " + save_msg;
/*      */         }
/*      */       } 
/*  406 */       writer.write("\n===" + save_msg);
/*      */     } 
/*      */ 
/*      */     
/*  410 */     if (test_ind > 0) {
/*      */       
/*  412 */       writer.write("\n<<<<<<< obj fafsa\n");
/*  413 */       writer.write(stud.getStudentXFafsa());
/*  414 */       writer.write("\n<<<<<<< obj no_loans\n");
/*  415 */       writer.write(stud.getIndExcloans());
/*  416 */       writer.write("\n<<<<<<< obj fws\n");
/*  417 */       writer.write(stud.getStudentArFws());
/*  418 */       writer.write("\n<<<<<<<\n\n");
/*      */       
/*  420 */       writer.write("\n<<<<<<<\n");
/*  421 */       pv = vmap.get("std_noloans");
/*  422 */       writer.write("para std_noloans=" + ((pv == null) ? "null" : pv));
/*      */       
/*  424 */       writer.write("\n<<<<<<<\n\n");
/*  425 */       writer.write("obj no_subloan=" + stud.getStudentApSubLoans());
/*  426 */       writer.write("\n<<<<<<<\n");
/*  427 */       writer.write("obj no_unsubloan=" + stud.getStudentAqUnsubLoans());
/*      */ 
/*      */       
/*  430 */       sb.setLength(0);
/*  431 */       writer.write("\n\n");
/*      */       
/*  433 */       pos = 0;
/*  434 */       for (String key : vmap.keySet()) {
/*  435 */         pos = chkParaByName(key);
/*  436 */         writer.write(" parameter name=" + String.format("%-20s", new Object[] { "[" + key + "]" }) + " val=" + String.format("%-20s", new Object[] { "[" + (String)vmap.get(key) + "]" }) + " mapped to attr=" + this.fields[pos] + "\n");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  441 */       writer.write("\n\n");
/*  442 */       for (String fn : this.fields) {
/*      */         try {
/*  444 */           Field x = stdcls.getDeclaredField(fn);
/*  445 */           x.setAccessible(true);
/*  446 */           writer.write(" attr name=" + String.format("%-30s", new Object[] { x.getName() }) + " val=" + x.get(stud) + "\n");
/*      */         
/*      */         }
/*  449 */         catch (NoSuchFieldException ex) {
/*  450 */           log.info("setAttr nsfield: ", ex);
/*  451 */         } catch (SecurityException ex) {
/*  452 */           log.info("setAttr security: ", ex);
/*  453 */         } catch (IllegalArgumentException ex) {
/*  454 */           log.info("setAttr argument: ", ex);
/*  455 */         } catch (IllegalAccessException ex) {
/*  456 */           log.info("setAttr access: ", ex);
/*      */         } 
/*      */       } 
/*  459 */       writer.write("\n<<<<<<<\n\n");
/*  460 */       writer.write(echoRes2(this.calc, stud));
/*      */     } 
/*      */ 
/*      */     
/*  464 */     long end = System.nanoTime();
/*      */ 
/*      */     
/*  467 */     log.info((new StringBuilder(250)).append(" <<< >>> NetEcho from ").append(req.getRemoteAddr()).append(" for [").append(vmap.get("std_lsuid")).append("] by [").append(vmap.get("reviewerid"))
/*  468 */         .append("] with [test=").append(test_ind).append(" save_sfs=").append(save_ind).append("] get tot-aid=").append(this.calc.showMaxAidAmt()).append(" need-based=").append(this.calc.getUse_need_ind())
/*  469 */         .append(" in ").append((end - start) / 1000000L).append(" ms, save_msg=").append(save_msg).toString());
/*      */   }
/*      */ 
/*      */   
/*      */   private int chkParaByName(String pn) {
/*  474 */     int res = -1;
/*  475 */     if (pn != null && pn.trim().length() > 0) {
/*  476 */       String pnn = pn.trim().toLowerCase();
/*      */       
/*  478 */       for (int p = 0; p < this.para_name.length; p++) {
/*  479 */         if (this.para_name[p].equals(pnn)) {
/*  480 */           res = p;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  485 */     return res;
/*      */   }
/*      */ 
/*      */   
/*      */   private int isNumeric(String str) {
/*  490 */     if (str == null || str.isEmpty()) return 0; 
/*  491 */     int res = 0;
/*  492 */     String s = str.trim();
/*      */     try {
/*  494 */       BigDecimal big = new BigDecimal(s);
/*  495 */       if (big.toPlainString().equals(s)) res = 1; 
/*  496 */     } catch (Exception exception) {}
/*      */     
/*  498 */     return res;
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
/*      */   private int chkParaByVal(int pos, String pv, HashMap<Integer, Object> map) {
/*  605 */     int res = 0;
/*  606 */     if (pos >= 0 && pos < this.para_name.length && pv != null && !pv.isEmpty()) {
/*  607 */       int t = this.para_type[pos];
/*  608 */       int i = 0;
/*  609 */       BigDecimal big = null;
/*  610 */       Date dt = null;
/*  611 */       String s = pv.trim().toLowerCase();
/*  612 */       String s2 = pv.trim();
/*  613 */       if (s.equals("undefined") || s.equals("null")) return res;
/*      */       
/*  615 */       switch (t) { case 0:
/*  616 */           if (isNumeric(s) > 0) {
/*  617 */             i = Integer.parseInt(s);
/*  618 */             if (i != 0) i = 1; 
/*  619 */             res = 1;
/*  620 */             map.put(Integer.valueOf(pos), Integer.valueOf(i));
/*      */           }  break;
/*      */         case 1:
/*  623 */           if (isNumeric(s) > 0) {
/*  624 */             i = Integer.parseInt(s);
/*  625 */             map.put(Integer.valueOf(pos), Integer.valueOf(i));
/*  626 */             res = 1;
/*      */           }  break;
/*      */         case 2:
/*  629 */           if (isNumeric(s) > 0 && s.equals((big = new BigDecimal(s)).toString())) {
/*  630 */             res = 1;
/*  631 */             map.put(Integer.valueOf(pos), big);
/*      */           }  break;
/*      */         case 3:
/*  634 */           res = 1;
/*  635 */           map.put(Integer.valueOf(pos), s2); break;
/*      */         case 4:
/*  637 */           if (s.equals("yes") || s.equals("no")) {
/*  638 */             res = 1;
/*  639 */             map.put(Integer.valueOf(pos), s);
/*      */           }  break;
/*      */         case 5:
/*  642 */           if (s.equals("true") || s.equals("false")) {
/*  643 */             res = 1;
/*  644 */             map.put(Integer.valueOf(pos), s);
/*      */           }  break;
/*      */         case 6:
/*  647 */           s = s.replaceAll("[^0-9]+", "");
/*  648 */           if (s.length() == 6) {
/*      */             try {
/*  650 */               dt = this.dfs.parse(s);
/*  651 */               res = 1;
/*  652 */               map.put(Integer.valueOf(pos), this.df.format(dt));
/*  653 */             } catch (ParseException parseException) {}
/*      */             break;
/*      */           } 
/*  656 */           if (s.length() == 8) {
/*      */             try {
/*  658 */               dt = this.dfl.parse(s);
/*  659 */               res = 1;
/*  660 */               map.put(Integer.valueOf(pos), this.df.format(dt));
/*  661 */             } catch (ParseException parseException) {}
/*      */           }
/*      */           break; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/*  673 */     return res;
/*      */   }
/*      */   
/*      */   private void syncParaToAttr(HashMap<Integer, Object> map, HashMap<String, Field> fmap, Student std) {
/*  677 */     for (Integer pos : map.keySet()) {
/*  678 */       int t = this.attr_type[pos.intValue()];
/*  679 */       Object pv = map.get(pos);
/*  680 */       String sv = String.valueOf(pv).toLowerCase();
/*      */       
/*  682 */       int pt = this.para_type[pos.intValue()];
/*  683 */       String pn = this.para_name[pos.intValue()];
/*      */       
/*  685 */       String an = this.fields[pos.intValue()];
/*  686 */       Field f = fmap.get(an);
/*      */ 
/*      */       
/*  689 */       switch (pt) { case 0:
/*      */         case 1:
/*      */         case 2:
/*      */         case 4:
/*      */         case 6:
/*  694 */           setAttr(std, f, pv, pt);
/*      */         case 3:
/*  696 */           if (pn.equals("std_merit")) {
/*  697 */             switch (sv) { case "c":
/*  698 */                 pv = "MC"; break;
/*  699 */               case "s": pv = "MS"; break;
/*  700 */               case "f": pv = "MF"; break;
/*  701 */               default: pv = "";
/*      */                 break; }
/*      */ 
/*      */ 
/*      */           
/*  706 */           } else if (pn.equals("std_sex")) {
/*  707 */             pv = String.valueOf(pv).toUpperCase();
/*      */           } 
/*  709 */           setAttr(std, f, pv, pt);
/*      */         case 5:
/*  711 */           switch (pn) {
/*      */             case "std_eanonlsu":
/*  713 */               setAttr(std, f, sv.equals("true") ? new BigDecimal(1) : new BigDecimal(0), 2);
/*      */               continue;
/*      */             case "std_returning":
/*      */             case "std_nc":
/*  717 */               setAttr(std, f, Integer.valueOf(sv.equals("true") ? 1 : 0), 0);
/*      */               continue;
/*      */           } 
/*  720 */           setAttr(std, f, sv.equals("true") ? "yes" : "no", 4); }
/*      */     
/*      */     } 
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
/*      */   private void setAttr(Student std, Field f, Object obj, int t) {
/*      */     try {
/*  737 */       f.setAccessible(true);
/*  738 */       switch (t) { case 0:
/*      */         case 1:
/*  740 */           f.set(std, obj); break;
/*      */         case 2:
/*  742 */           f.set(std, obj); break;
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/*  747 */           f.set(std, String.valueOf(obj));
/*      */           break; }
/*      */ 
/*      */ 
/*      */     
/*  752 */     } catch (SecurityException ex) {
/*  753 */       log.info("setAttr security: ", ex);
/*  754 */     } catch (IllegalArgumentException ex) {
/*  755 */       log.info("setAttr argument: ", ex);
/*  756 */     } catch (IllegalAccessException ex) {
/*  757 */       log.info("setAttr access: ", ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String echoRes(PackFunctions calc, Student std) {
/*  762 */     StringBuilder sb = new StringBuilder(120000);
/*  763 */     sb.append("OK@Version ").append(this.ref.getClientverions()).append("^");
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
/*  781 */     sb.append("Maximum Possible Aid@Tuition and Fees=").append(calc.showTuitionFees()).append("|Maximum Possible Aid@Additional Expenses=").append(calc.showAddlExpense());
/*  782 */     sb.append("|Maximum Possible Aid@-Expected Family Contribution (EFC)=").append(calc.showEFC());
/*  783 */     sb.append("|Maximum Possible Aid@tot=").append(calc.showNeedAmt()).append("^");
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
/*  811 */     String based = (calc.getUse_need_ind() > 0) ? "need-based" : "non-need based";
/*  812 */     based = (new StringBuilder(200)).append("Financial Aid (").append(based).append(", top aid: ").append(calc.showMPA()).append(")").toString();
/*  813 */     sb.append(based).append("@PELL Grant =").append(calc.showPellGrantAmt());
/*  814 */     sb.append("|").append(based).append("@Cal Grant A=").append(calc.showCalGrantA());
/*  815 */     sb.append("|").append(based).append("@Cal Grant B=").append(calc.showCalGrantB());
/*  816 */     sb.append("|").append(based).append("@FSEOG=").append(calc.showFseogAmt());
/*  817 */     sb.append("|").append(based).append("@External Allowance=").append(calc.showExtAllowanceAmt());
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
/*  839 */     if (calc.getNonCalGrantAmt() > 0) {
/*  840 */       sb.append("|").append(based).append("@").append(calc.getNonCalGrantDesc()).append("=").append(calc.showNonCalGrantAmt());
/*      */     }
/*  842 */     if (calc.getOutsideAmt() > 0) {
/*  843 */       sb.append("|").append(based).append("@").append(calc.getOutsideDesc()).append("=").append(calc.showOutsideAmt());
/*      */     }
/*  845 */     if (calc.getChurchBaseAmt() > 0) {
/*  846 */       sb.append("|").append(based).append("@").append(calc.showChurchBaseDesc()).append("=").append(calc.showChurchBaseAmt());
/*      */     }
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
/*  880 */     sb.append("|").append(based).append("@La Sierra Educational Allowance=").append(calc.showLsuAllowanceAmt());
/*  881 */     sb.append("|").append(based).append("@SDA Award=").append(calc.showSdaAwardAmt());
/*  882 */     sb.append("|").append(based).append("@La Sierra Need Grant=").append(calc.showLsuNeedGrantAmt());
/*  883 */     sb.append("|").append(based).append("@Family Discount=").append(calc.showFamilyDiscountAmt());
/*  884 */     sb.append("|").append(based).append("@National Merit Scholarship=").append(calc.showNationalMeritAmt());
/*  885 */     sb.append("|").append(based).append("@La Sierra Achievement Award=").append(calc.showLsuAchievementAmt());
/*  886 */     sb.append("|").append(based).append("@La Sierra 4-year Renewable Scholarship=").append(calc.showLsu4yRenewableAmt());
/*  887 */     sb.append("|").append(based).append("@La Sierra Univ. Grant=").append(calc.showLasuGrantAmt());
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
/*  926 */     if (calc.getScholarship1Amt() > 0) {
/*  927 */       sb.append("|").append(based).append("@").append(calc.showScholarship1Desc()).append("=").append(calc.showScholarship1Amt());
/*      */     }
/*  929 */     if (calc.getScholarship2Amt() > 0) {
/*  930 */       sb.append("|").append(based).append("@").append(calc.showScholarship2Desc()).append("=").append(calc.showScholarship2Amt());
/*      */     }
/*  932 */     if (calc.getChurchMatchAmt() > 0) {
/*  933 */       sb.append("|").append(based).append("@").append(calc.showChurchMatchDesc()).append("=").append(calc.showChurchMatchAmt());
/*      */     }
/*  935 */     if (calc.getLitEvanMatchAmt() > 0) {
/*  936 */       sb.append("|").append(based).append("@").append(calc.showLitEvanMatchDesc()).append("=").append(calc.showLitEvanMatchAmt());
/*      */     }
/*  938 */     if (calc.getPacificCampMatchAmt() > 0) {
/*  939 */       sb.append("|").append(based).append("@").append(calc.showPacificCampMatchDesc()).append("=").append(calc.showPacificCampMatchAmt());
/*      */     }
/*  941 */     if (calc.getNonPacificCampMatchAmt() > 0) {
/*  942 */       sb.append("|").append(based).append("@").append(calc.showNonPacificCampMatchDesc()).append("=").append(calc.showNonPacificCampMatchAmt());
/*      */     }
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
/*  965 */     if (calc.getScholarship7Amt() > 0) {
/*  966 */       sb.append("|").append(based).append("@").append(calc.showScholarship7Desc()).append("=").append(calc.showScholarship7Amt());
/*      */     }
/*  968 */     if (calc.getScholarship8Amt() > 0) {
/*  969 */       sb.append("|").append(based).append("@").append(calc.showScholarship8Desc()).append("=").append(calc.showScholarship8Amt());
/*      */     }
/*  971 */     if (calc.getScholarship9Amt() > 0) {
/*  972 */       sb.append("|").append(based).append("@").append(calc.showScholarship9Desc()).append("=").append(calc.showScholarship9Amt());
/*      */     }
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
/* 1014 */     if (std.getStudentXFafsa().equalsIgnoreCase("yes") && std.getIndExcloans().equalsIgnoreCase("no")) {
/* 1015 */       sb.append("|").append(based).append("@Subsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_sub())).append(")=").append(std.getStudentApSubLoans().equalsIgnoreCase("yes") ? calc.showSubdirectAmt() : "excluded");
/* 1016 */       sb.append("|").append(based).append("@Perkins Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_perkins())).append(")=").append(calc.showPerkinLoanAmt());
/* 1017 */       sb.append("|").append(based).append("@Unsubsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_unsub())).append(")=").append(std.getStudentAqUnsubLoans().equalsIgnoreCase("yes") ? calc.showUnsubdirectAmt() : "excluded");
/*      */     } 
/* 1019 */     if (std.getStudentArFws().equalsIgnoreCase("yes") && calc.getFwsAmt() > 0) {
/* 1020 */       sb.append("|").append(based).append("@Federal Work-study=").append(calc.showFwsAmt());
/*      */     }
/*      */     
/* 1023 */     sb.append("|").append(based).append("@tot=").append(calc.showMaxAidAmt()).append("^");
/*      */ 
/*      */     
/* 1026 */     int cost = calc.getTuitionAndFees() + calc.getRoomAndBoard();
/* 1027 */     int loan = calc.getPerkinsLoanAmt() + calc.getUnsubDirectAmt() + calc.getSubDirectAmt();
/* 1028 */     int aid = calc.getFaidExtAmt();
/* 1029 */     if (cost > 0) {
/* 1030 */       sb.append("|").append(based).append("@Cost Discount Percentage=").append(Math.round(((aid - loan) * 10000 / cost)) / 100.0D).append("%");
/*      */       
/* 1032 */       sb.append("|").append(based).append("@Total Discount Percentage=").append(Math.round((aid * 10000 / cost)) / 100.0D).append("%");
/*      */     } 
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
/* 1077 */     sb.append("Amount Due Calculation@Tuition and Fees=").append(calc.showTuitionFees());
/* 1078 */     sb.append("|Amount Due Calculation@+Room and Board=").append(calc.initAndShowRoomBoardAmt());
/* 1079 */     sb.append("|Amount Due Calculation@-Financial Aid").append(calc.showFaidExtDesc()).append("=").append(calc.showFaidAmt());
/* 1080 */     if (calc.getPacificCampBase() > 0) {
/* 1081 */       sb.append("|Amount Due Calculation@").append(calc.showPacificCampBaseDesc()).append("=").append(calc.showPacificCampBaseAmt());
/*      */     }
/* 1083 */     if (calc.getNonPacificCampBaseAmt() > 0) {
/* 1084 */       sb.append("|Amount Due Calculation@").append(calc.showNonPacificCampBaseDesc()).append("=").append(calc.showNonPacificCampBaseAmt());
/*      */     }
/* 1086 */     if (calc.getLitEvanBaseAmt() > 0) {
/* 1087 */       sb.append("|Amount Due Calculation@").append(calc.showLitEvanBaseDesc()).append("=").append(calc.showLitEvanBaseAmt());
/*      */     }
/* 1089 */     sb.append("|Amount Due Calculation@tot=").append(calc.showDueAmt()).append("^");
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
/* 1107 */     sb.append("Payment Options@Year in Advance option (7% off)=").append(calc.initAndShowYIA());
/* 1108 */     if (!std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
/* 1109 */       sb.append("|Payment Options@Quarterly in Advance option (2% off)=").append(calc.showQIA());
/*      */     }
/* 1111 */     sb.append("|Payment Options@Monthly option ($90 annual fee included)=").append(calc.showMonthlyOption());
/*      */ 
/*      */     
/* 1114 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String echoRes2(PackFunctions calc, Student std) {
/* 1119 */     StringBuilder sb = new StringBuilder(120000);
/* 1120 */     sb.append("\n\n");
/*      */     
/* 1122 */     sb.append("Maximum Possible Aid").append("\n");
/* 1123 */     sb.append("Tuition and Fees=").append(calc.showTuitionFees()).append("\n");
/* 1124 */     sb.append("Additional Expenses=").append(calc.showAddlExpense()).append("\n");
/* 1125 */     sb.append("-Expected Family Contribution (EFC)=").append(calc.showEFC()).append("\n");
/* 1126 */     sb.append("tot=").append(calc.showNeedAmt()).append("\n\n");
/*      */ 
/*      */ 
/*      */     
/* 1130 */     String based = (calc.getUse_need_ind() > 0) ? "need-based" : "non-need based";
/* 1131 */     based = (new StringBuilder(200)).append("Financial Aid (").append(based).append(", top aid: ").append(calc.showMPA()).append(")").toString();
/* 1132 */     sb.append(based).append("\n");
/* 1133 */     sb.append("PELL Grant =").append(calc.showPellGrantAmt()).append("\n");
/* 1134 */     sb.append("Cal Grant A=").append(calc.showCalGrantA()).append("\n");
/* 1135 */     sb.append("Cal Grant B=").append(calc.showCalGrantB()).append("\n");
/* 1136 */     sb.append("FSEOG=").append(calc.showFseogAmt()).append("\n");
/* 1137 */     sb.append("External Allowance=").append(calc.showExtAllowanceAmt()).append("\n");
/*      */ 
/*      */     
/* 1140 */     if (calc.getNonCalGrantAmt() > 0) {
/* 1141 */       sb.append(calc.getNonCalGrantDesc()).append("=").append(calc.showNonCalGrantAmt()).append("\n");
/*      */     }
/* 1143 */     if (calc.getOutsideAmt() > 0) {
/* 1144 */       sb.append(calc.getOutsideDesc()).append("=").append(calc.showOutsideAmt()).append("\n");
/*      */     }
/* 1146 */     if (calc.getChurchBaseAmt() > 0) {
/* 1147 */       sb.append(calc.showChurchBaseDesc()).append("=").append(calc.showChurchBaseAmt()).append("\n");
/*      */     }
/*      */     
/* 1150 */     sb.append("La Sierra Educational Allowance=").append(calc.showLsuAllowanceAmt()).append("\n");
/* 1151 */     sb.append("SDA Award=").append(calc.showSdaAwardAmt()).append("\n");
/* 1152 */     sb.append("La Sierra Need Grant=").append(calc.showLsuNeedGrantAmt()).append("\n");
/* 1153 */     sb.append("Family Discount=").append(calc.showFamilyDiscountAmt()).append("\n");
/* 1154 */     sb.append("National Merit Scholarship=").append(calc.showNationalMeritAmt()).append("\n");
/* 1155 */     sb.append("La Sierra Achievement Award=").append(calc.showLsuAchievementAmt()).append("\n");
/* 1156 */     sb.append("La Sierra 4-year Renewable Scholarship=").append(calc.showLsu4yRenewableAmt()).append("\n");
/*      */ 
/*      */     
/* 1159 */     if (calc.getScholarship1Amt() > 0) {
/* 1160 */       sb.append(calc.showScholarship1Desc()).append("=").append(calc.showScholarship1Amt()).append("\n");
/*      */     }
/* 1162 */     if (calc.getScholarship2Amt() > 0) {
/* 1163 */       sb.append(calc.showScholarship2Desc()).append("=").append(calc.showScholarship2Amt()).append("\n");
/*      */     }
/* 1165 */     if (calc.getChurchMatchAmt() > 0) {
/* 1166 */       sb.append(calc.showChurchMatchDesc()).append("=").append(calc.showChurchMatchAmt()).append("\n");
/*      */     }
/* 1168 */     if (calc.getLitEvanMatchAmt() > 0) {
/* 1169 */       sb.append(calc.showLitEvanMatchDesc()).append("=").append(calc.showLitEvanMatchAmt()).append("\n");
/*      */     }
/* 1171 */     if (calc.getPacificCampMatchAmt() > 0) {
/* 1172 */       sb.append(calc.showPacificCampMatchDesc()).append("=").append(calc.showPacificCampMatchAmt()).append("\n");
/*      */     }
/* 1174 */     if (calc.getNonPacificCampMatchAmt() > 0) {
/* 1175 */       sb.append(calc.showNonPacificCampMatchDesc()).append("=").append(calc.showNonPacificCampMatchAmt()).append("\n");
/*      */     }
/*      */     
/* 1178 */     if (calc.getScholarship7Amt() > 0) {
/* 1179 */       sb.append(calc.showScholarship7Desc()).append("=").append(calc.showScholarship7Amt()).append("\n");
/*      */     }
/* 1181 */     if (calc.getScholarship8Amt() > 0) {
/* 1182 */       sb.append(calc.showScholarship8Desc()).append("=").append(calc.showScholarship8Amt()).append("\n");
/*      */     }
/* 1184 */     if (calc.getScholarship9Amt() > 0) {
/* 1185 */       sb.append(calc.showScholarship9Desc()).append("=").append(calc.showScholarship9Amt()).append("\n");
/*      */     }
/*      */     
/* 1188 */     if (std.getStudentXFafsa().equalsIgnoreCase("yes") && std.getIndExcloans().equalsIgnoreCase("no")) {
/* 1189 */       sb.append("Subsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_sub())).append(")=").append(std.getStudentApSubLoans().equalsIgnoreCase("yes") ? calc.showSubdirectAmt() : "excluded").append("\n");
/* 1190 */       sb.append("Perkins Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_perkins())).append(")=").append(calc.showPerkinLoanAmt()).append("\n");
/* 1191 */       sb.append("Unsubsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_unsub())).append(")=").append(std.getStudentAqUnsubLoans().equalsIgnoreCase("yes") ? calc.showUnsubdirectAmt() : "excluded").append("\n");
/*      */     } 
/* 1193 */     if (std.getStudentArFws().equalsIgnoreCase("yes") && calc.getFwsAmt() > 0) {
/* 1194 */       sb.append("Federal Work-study=").append(calc.showFwsAmt()).append("\n");
/*      */     }
/*      */     
/* 1197 */     sb.append("tot=").append(calc.showMaxAidAmt()).append("\n\n");
/*      */ 
/*      */     
/* 1200 */     sb.append("Amount Due Calculation").append("\n");
/* 1201 */     sb.append("Tuition and Fees=").append(calc.showTuitionFees()).append("\n");
/* 1202 */     sb.append("+Room and Board=").append(calc.initAndShowRoomBoardAmt()).append("\n");
/* 1203 */     sb.append("-Financial Aid").append(calc.showFaidExtDesc()).append("=").append(calc.showFaidAmt()).append("\n");
/* 1204 */     if (calc.getPacificCampBase() > 0) {
/* 1205 */       sb.append(calc.showPacificCampBaseDesc()).append("=").append(calc.showPacificCampBaseAmt()).append("\n");
/*      */     }
/* 1207 */     if (calc.getNonPacificCampBaseAmt() > 0) {
/* 1208 */       sb.append(calc.showNonPacificCampBaseDesc()).append("=").append(calc.showNonPacificCampBaseAmt()).append("\n");
/*      */     }
/* 1210 */     if (calc.getLitEvanBaseAmt() > 0) {
/* 1211 */       sb.append(calc.showLitEvanBaseDesc()).append("=").append(calc.showLitEvanBaseAmt()).append("\n");
/*      */     }
/* 1213 */     sb.append("tot=").append(calc.showDueAmt()).append("\n\n");
/*      */ 
/*      */     
/* 1216 */     int cost = calc.getTuitionAndFees() + calc.getRoomAndBoard();
/* 1217 */     int loan = calc.getPerkinsLoanAmt() + calc.getUnsubDirectAmt() + calc.getSubDirectAmt();
/* 1218 */     int aid = calc.getFaidExtAmt();
/* 1219 */     if (cost > 0) {
/*      */       
/* 1221 */       sb.append("\n").append(based).append("Cost Discount Percentage=").append(Math.round(((aid - loan) * 10000 / cost)) / 100.0D).append("%");
/*      */       
/* 1223 */       sb.append("\n").append(based).append("Total Discount Percentage=").append(Math.round((aid * 10000 / cost)) / 100.0D).append("%");
/*      */     } 
/* 1225 */     sb.append("\n").append("cost=").append(cost).append(" loan=").append(loan).append(" aid=").append(aid);
/* 1226 */     sb.append("\n\n");
/*      */ 
/*      */     
/* 1229 */     sb.append("Payment Options").append("\n");
/* 1230 */     sb.append("Year in Advance option (7% off)=").append(calc.initAndShowYIA()).append("\n");
/* 1231 */     if (!std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
/* 1232 */       sb.append("Quarterly in Advance option (2% off)=").append(calc.showQIA()).append("\n");
/*      */     }
/* 1234 */     sb.append("Monthly option ($90 annual fee included)=").append(calc.showMonthlyOption()).append("\n");
/*      */ 
/*      */     
/* 1237 */     return sb.toString();
/*      */   }
/*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\NetFeeder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */