/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import org.joda.time.DateMidnight;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

/**
 *
 * @author kwang
 */

@WebServlet(name="echo", urlPatterns={"/echo"}, loadOnStartup=1)
public class NetFeeder extends HttpServlet{
    
    private  static final Logger log = LoggerFactory.getLogger();
    
    //@Inject 
    //private Student gstud = new Student(); //default stud, gives default numbers at first ??? --- where to set and keep the default settings????  
    //entity, all fields are private. new td() will call its setInitVals()
    
    private DateFormat dfl = new SimpleDateFormat("MMddyyyy"); //mm is minutes DD is days in year
    private DateFormat dfs = new SimpleDateFormat("MMddyy"); /*
For parsing with the abbreviated year pattern ("y" or "yy"), SimpleDateFormat must interpret the abbreviated year relative to some century. 
    It does this by adjusting dates to be within 80 years before and 20 years after the time the SimpleDateFormat instance is created. For example, using a pattern of "MM/dd/yy" and 
    a SimpleDateFormat instance created on Jan 1, 1997, the string "01/11/12" would be interpreted as Jan 11, 2012 while the string "05/04/64" would be interpreted as May 4, 1964. 
    During parsing, only strings consisting of exactly two digits, as defined by Character.isDigit(char), will be parsed into the default century. 
    Any other numeric string, such as a one digit string, a three or more digit string, or a two digit string that isn't all digits (for example, "-1"), is interpreted literally. So "01/02/3" or "01/02/003" are parsed, 
    using the same pattern, as Jan 2, 3 AD. Likewise, "01/02/-3" is parsed as Jan 2, 4 BC.
Otherwise, calendar system specific forms are applied. For both formatting and parsing, if the number of pattern letters is 4 or more, a calendar specific long form is used. Otherwise, 
    a calendar specific short or abbreviated form is used.
    */
    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    
    //auto calc and show only  "eanonlsu_perc"--"studentAiEduAllowPer",
    //direct map: studentAfFamilyContrib
    // stud.setStudentAgNonlsuAllowrance(  std_eanonlsu? (new BigDecimal(1)):(new BigDecimal(0)));  === stud.setStudentAiEduAllowPer
    // dup" "indEalsu", "indEanonlsu", 
    //mad studentMMarry Single or no
    
    //
    //"ea_lsu_perc", "ea_nonlsu_perc", "puser_id", "term", "term_load",
    private String[] para_name ={"std_lsuid", "std_sex", "std_fname", "std_lname", "std_dob", "std_email", "std_phone",
                                 "std_street", "std_city", "std_state", "std_zip", "std_country", "std_apt",    "std_intl", "std_marry", "std_sda", "std_indept", "std_returning", "std_nc",
                                 "std_preshcool", "std_gpa", "std_sat_math", "std_act", "std_merit", "std_major", "std_grade", "std_aluminus", "std_aliminumb",                                 
                                 "std_fafsa", "std_calgrant", "std_ealsu", "std_eanonlsu", "std_dorm", "std_efc", "std_noloans",
                                 
                                 "fisy","ea_lsu_perc", "ea_nonlsu_perc", "reviewerid", 
                                 "std_homeincome", "std_homeasset", "std_homemems", "efc_amt", "noncal_notes", "noncal_amt", "nonlsu_notes", "nonlsu_amt",
                                 
                                 "ship1note", "ship1amt", "ship2note", "ship2amt", 
                                 "ship3note", "ship3amt", "ship4note", "ship4amt", 
                                 "ship5note", "ship5amt", "ship6note", "ship6amt", 
                                 "std_calgranta", "std_calgrantb", "calgrant_adjust", "notes_pub", "notes_pri", "inc_subloan", "inc_unsubloan", "inc_fws"
    };
    
    // 0=0/1  1=int    2=bigdecimal    3=string(!sex merit grade)    4=str yes/no  5=str true/false  6=date in 'mm/dd/yyyy'   7=date dob=str  yes/no=str (converted from/to boolean in bean)
    //all para can be stranger/null/empty/undefined
    //std_eanonlsu --0/1
    private int[] para_type    ={3,  3,  3,  3,  6,  3,  3,
                                 3,  3,  3,  3,  3,  3,  5,  5,  5,  5,  5,  5,
                                 3,  2,  1,  1,  3,  3,  3,  3,  1,
                                 5,  5,  5,  5,  5,  5,  5,
                                 
                                 1,  1,  1,  3,
                                 2,  2,  1,  1,  3,  1,  3,  1,
                                 
                                 3,  1,  3,  1,
                                 3,  1,  3,  1,
                                 3,  1,  3,  1,
                                 1,  1,  5,  3,  3,  5,  5,  5
    };
    //private Object[] para_val  = new Object[para_type.length];
    
    private int[] attr_type    ={3,  3,  3,  3,  6,  3,  3,
                                 3,  3,  3,  3,  3,  3,  4,  4,  4,  4,  0,  0,
                                 3,  2,  1,  1,  3,  3,  3,  3,  1,
                                 4,  4,  4,  2,  4,  4,  4,
                                 
                                 1,  1,  1,  3,
                                 2,  2,  1,  1,  3,  1,  3,  1,
                                 
                                 3,  1,  3,  1,
                                 3,  1,  3,  1,
                                 3,  1,  3,  1,
                                 1,  1,  4,  3,  3,  4,  4,  4
    };
    private String[] fields    ={"studentALsuid",  "sex", "studentCFirstname", "studentBLastname",  "studentDDob", "studentEEmail", "studentFPhone", 
                                 "studentGStreet", "studentHCity", "studentIState", "studentJZip", "studentKCountry", "homeAddrApt",      "studentLIntlStud", "studentMMarry", "studentNSda","studentYIndept", "returnStdInd", "ncStdInd",
                                 "studentOLastSchool", "studentPGpa", "studentQSat",  "studentRAct", "studentSMerit", "studentTMajor", "studentUAcademic",     "studentVFamily", "homecostudies", 
                                 "studentXFafsa",  "studentZCalgrant",    "studentAhLsuAllowrance", "studentAgNonlsuAllowrance",  "studentWDorm", "indEfc", "indExcloans",                                 
                                 
                                 "studentFisy", "ea_lsu_perc", "ea_nonlsu_perc", "puser_id", 
                                 "studentAdFamilyIncome", "studentAeFamilyAsset", "studentAcFamilySize", "studentAfFamilyContrib", 
                                 "studentAjHomeState", "studentAkNoncalGrant", "studentAlOutScholarships",  "studentAmOutScholarshipAmt", 
                                   
                                 
                                 "studentAtScholarship1Note", "studentAuScholarship1Amt",  "studentAwScholarship2Note", "studentAxScholarship2Amt", 
                                  "studentAzScholarship3Note", "studentBaScholarship3Amt",  "studentBcScholarship4Note", "studentBdScholarship4Amt", 
                                  "studentBfScholarship5Note", "studentBgScholarship5Amt", "studentBiScholarship6Note", "studentBjScholarship6Amt",  
                                 "studentAaCalgrantA", "studentAbCalgrantB", "adjCalgrantInd", "studentAnPubNotes", "studentAoPriNotes", "studentApSubLoans", "studentAqUnsubLoans", "studentArFws" 
    };//"studentFisy",  "pickupInd", "estmNumb", "tzdoe", "tzdom", "tzup", "tzdown", "clientId", "counselorId"}; 
                                //"recid" "studentNumb", "studentByDom", "studentBvDoe", "studentStudType",
                                //"studentBtSupercounselor", "studentBwProgress", "studentBzUploaded", "studentCbBanner", "studentPassword", "studentTermStart",    "studentTermEnd",
                                
                                /* "studentQSatV", "studentAsScholarship1Name", "studentAvScholarship2Name", "studentAyScholarship3Name","studentBbScholarship4Name", "studentBeScholarship5Name",  "studentBhScholarship6Name",
                                 "studentBkScholarship7Name", "studentBlScholarship7Note", "studentBmScholarship7Amt", 
                                 "studentBnScholarship8Name", "studentBoScholarship8Note", "studentBpScholarship8Amt", "studentBqScholarship9Name", "studentBrScholarship9Note", 
                                 "studentBsScholarship9Amt", 
                                   "fund1id", "fund2id", "fund3id", "fund4id", "fund5id", "fund6id", "fund7id", "fund8id",  "fund9id",  
                                */    
    
    //private int[] para_low     ={0,  0,  0,  0,  0};
    //private int[] para_high    ={6,  1, 50, 50, 10};  
    
    int v_type;
    int v_low;
    int v_high;
    String v_name;
    Object v_val;
    
    
    String emp_acct;    
    StringBuilder ss = new StringBuilder(120000);
    
    @Inject AppReference ref;
    //@Inject private  PackFunctions calc;   
    private  PackFunctions calc;  //IS NULL from est????????
    
    @Inject private  Estimator  est;
    
    // change on the page
    // trigger AJAX caall to serverside proc
    // the proc may update stud obj, then call: calc.refreshCalc(stud);
    // the ajax returns, trigger web page update/refresh, using new data from stud obj #### calc.init() -> refreshCalc(std) = initAndShowPellGrantAmt() and compare results
    
    @Override    
    protected void service( HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        long start = System.nanoTime();        
        
        //Student stud = new Student();  
        est.newPEstimate();
        Student stud = est.getStud();
        
        
        calc = est.getCalc(); 
        
        ref.setStdFixInstAids(stud);        
        stud.setStudentFisy(ref.getFiscal_year());
         
        HashMap<String, Field>  fmap = new HashMap<>(300);
        Class stdcls = stud.getClass();
        Field[] attrs = stdcls.getDeclaredFields();
        for( Field one : attrs){
            fmap.put( one.getName(), one);
        }
        
 //       PackFunctions calc = new PackFunctions();        
        PrintWriter writer = res.getWriter();
        
        StringBuilder sb = new StringBuilder(102400);
        int i=0,v=0, pos=0, test_ind=0;
        
        int save_ind=0;
        String save_msg="";
        
        HashMap<Integer, Object>  map= new HashMap<>(300);
        HashMap<String, String>  vmap= new HashMap<>(300);
        String pv = null;
        
        for( Enumeration f = req.getParameterNames(); f.hasMoreElements(); ){
            String pn = new String (f.nextElement().toString());
            pv = req.getParameter( pn );
            
            if(pn.equalsIgnoreCase("test")) test_ind=1;
            if(pn.equalsIgnoreCase("save_sfs") && pv.trim().equals("1"))save_ind=1;
            
            if( (pos=chkParaByName(pn))<0 || map.containsKey(pos)) continue;
            if( chkParaByVal(pos, pv, map)==0) continue;   //set val at pos
            vmap.put(pn.toLowerCase(), pv.trim());
            v++;
            /*
            if( i>0 ) sb.append('|');
            sb.append( pn ).append('=').append( pv);
            i++;*/
        }
        //writer.write(sb.toString());
        syncParaToAttr(map, fmap, stud);
        
        //TODO
        //mimic event lisenter, put val from para or converted or calculated, into STUD obj --USING REFLECTION
        //if( stud.getStudentYIndept().equals("no")){
        pv = vmap.get("std_indept");
        if( pv!=null && pv.equalsIgnoreCase("false")){
            pv = vmap.get("std_marry");
            if( pv !=null && pv.equals("true") ) stud.setStudentYIndept("yes");
            
            pv = stud.getStudentDDob();
            if( pv!=null && !pv.isEmpty()){
                DateMidnight dm=null; 
                try {
                    dm = new DateMidnight( df.parse( pv)  );
                    DateMidnight due = new DateMidnight(ref.getIndept_dob());
                    //if( dm.isBefore(due) ){
                    if( !dm.isAfter( due) ){                    
                        stud.setStudentYIndept("yes");                
                    }
                } catch (ParseException ex) {                    
                }                
            }  
        }
        
        //family size
        pv = vmap.get("std_homemems");
        if( pv!=null && !pv.isEmpty()){
            int n = Integer.parseInt(pv);
            if( n<1) n = 1;
            stud.setStudentAcFamilySize(n);
        }else{
            stud.setStudentAcFamilySize(1);
        }
        
        //ea perc by dorm and lsuea
        pv = vmap.get("std_dorm");
        String pv2 = vmap.get("std_eanonlsu");
        if( pv2 !=null && pv2.equalsIgnoreCase("true")){
            stud.setStudentAgNonlsuAllowrance(  new BigDecimal(1) );
            stud.setStudentAiEduAllowPer( new BigDecimal(calc.getEaNonLsuPercentageByDorm( (pv!=null && pv.equalsIgnoreCase("true")) ? true : false)).divide( new BigDecimal(100)));  
            stud.setIndEanonlsu("yes");
        }else{
            stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
            stud.setStudentAgNonlsuAllowrance(  new BigDecimal(0) );
            stud.setIndEanonlsu("no");
        }
        
        
        //noloans
        pv = vmap.get("std_noloans");
        if( pv!=null && pv.equalsIgnoreCase("true")){
            stud.setStudentApSubLoans( "No");
            stud.setStudentAqUnsubLoans( "No");
        }else{
            stud.setStudentApSubLoans(  "Yes");
            stud.setStudentAqUnsubLoans( "Yes");
        }        
                
        
        //efc -> fafsa (intl)
        pv = vmap.get("std_efc");
        pv2 = vmap.get("std_fafsa");
        if( pv!=null && pv.equalsIgnoreCase("true")){              
            if( pv2!=null && pv2.equalsIgnoreCase("false")){
                stud.setStudentXFafsa("yes");
            }
        }
        if( (pv!=null && pv.equalsIgnoreCase("true")) || (pv2!=null && pv2.equalsIgnoreCase("true")) ){  
            pv = vmap.get("std_intl");
            if( pv!=null && pv.equalsIgnoreCase("true")){
                stud.setStudentLIntlStud("no");
            }
        }    
        
        
        pv = vmap.get("amt_efc");
        if( pv!=null && !pv.isEmpty()){
            int n = Integer.parseInt(pv);
            if( n<0 ){
                n = 0;
                stud.setStudentAfFamilyContrib(n);
            }            
        }
         
        
        pv = vmap.get("std_gpa");
        if( pv==null){
            stud.setStudentPGpa(BigDecimal.ZERO);
        }
        
        pv = vmap.get("std_grade");
        if( pv==null){
            stud.setStudentUAcademic("FR");
        }
        
        pv = vmap.get("std_sex");
        if( pv==null){
            stud.setSex("N");
        }
        
        
        calc.init();
        
        //cala and calb
        pv = vmap.get("std_calgrant");
        if( pv!=null && pv.equals("true")){
            pv = vmap.get("calgrant_adjust");
            if( pv!=null && pv.equals("true")){                
                calc.setAdjustCalGrantAmtInd(true);
                pv=vmap.get("std_calgranta");
                if( pv!=null && !pv.isEmpty()){
                     stud.setStudentAaCalgrantA( Integer.parseInt(pv) );
                        stud.setStudentAbCalgrantB(0);
                }else{
                    pv=vmap.get("std_calgrantb");
                    if( pv!=null && !pv.isEmpty()){
                        stud.setStudentAbCalgrantB( Integer.parseInt(pv) );
                        stud.setStudentAaCalgrantA(0);
                    }
                }                
            }
        }        
        
               
        
        
        calc.refreshCalc(stud);
        
        //############ the EFC is empty???
        
        //TODO
        //compose res string by fetching values from cacl        
        writer.write( echoRes(calc, stud) );
        
        //2014-03-03, check all accpeted prara values, for possible saving into DB
        if( save_ind>0){
            //check all required fields
            String[] save_needs = {"std_lsuid",  "std_fname", "std_lname", "std_dob", "std_email", "std_phone", "std_state", "std_zip", "std_gpa", "fisy", "reviewerid" };
            String[] save_need1stmsg={"Student ID", "First Name", "Last Name", "Date of Birth", "E-mail", "Phone NUmber", "State", "ZIP Code", "GPA", "FISY", "Reviewer ID"};
            String one, stmp;
            int f=0;
            for(int m =0; m< save_needs.length; m++){//String one : save_needs){
                one = save_needs[m];
                if( !vmap.containsKey(one)){
                    save_msg = "SAVE_SFS FAILED. missed parameter "+save_need1stmsg[m];
                    f = 1;
                    break;
                }
            } 
            if( f==0){ //check std_ealsu=true ea_lsu_perc
                try{
                    int fisy = Integer.valueOf( vmap.get("fisy") );
                    if( ref.getFiscal_year()!= fisy){
                        save_msg = "SAVE_SFS FAILED. wrong FISY value";
                        f++;
                    }
                }catch(Exception e){
                    save_msg = "SAVE_SFS FAILED. invalid FISY value";
                    f++;
                }
                 
                
                if(f==0 && vmap.containsKey("std_ealsu") && (!vmap.containsKey("ea_lsu_perc") || Integer.valueOf( vmap.get("ea_lsu_perc"))>100 || Integer.valueOf( vmap.get("ea_lsu_perc"))<0) ){
                    save_msg = "SAVE_SFS FAILED. missed LASU EA percentage.";
                    f++;
                }
                if( f==0){
                    stmp = vmap.get("std_sex");
                    if(stmp !=null && !stmp.equalsIgnoreCase("F")&& !stmp.equalsIgnoreCase("M") &&!stmp.equalsIgnoreCase("N")){
                        save_msg = "SAVE_SFS FAILED. invalid gender value.";
                        f++;
                    }
                }
            }                      
            
            // the Hibernate validator will check the length, etc 
            if( f==0){
                est.setModStud(null);
                est.setModflag(0);

                stud.setPrtTimes(0); ////////////////////////////////
                stud.setPdfs(null);        
                stud.setCounselorId( ref.getSys_counselor_id());//login.getCurrentUser().getUserid());
                stud.setCounselorOrig( ref.getSys_counselor_id());//login.getCurrentUser().getUserid());
                stud.setStudentBuOrigCounselor("MYCAMPUS PORTAL");//login.getCurrentUser().getUsername());
                
                
                est.setStd_fn(stud.getStudentCFirstname());
                est.setStd_ln(stud.getStudentBLastname());
                est.setStd_dob(stud.getStudentDDob());
                save_msg = est.savepstudinfo();
                if( !save_msg.equalsIgnoreCase("SAVED")){
                    save_msg = "SAVE_SFS FAILED. "+save_msg;
                }
            }
            writer.write("\n==="+save_msg);                    
        }
        
        
        if( test_ind>0){

            writer.write("\n<<<<<<< obj fafsa\n");
            writer.write(stud.getStudentXFafsa());
            writer.write("\n<<<<<<< obj no_loans\n");
            writer.write(stud.getIndExcloans());
            writer.write("\n<<<<<<< obj fws\n");
            writer.write(stud.getStudentArFws());
            writer.write("\n<<<<<<<\n\n");
            
            writer.write("\n<<<<<<<\n");
            pv = vmap.get("std_noloans") ;
            writer.write( "para std_noloans="+ (pv==null? "null" : pv) ); //writer can not write null

            writer.write("\n<<<<<<<\n\n");
            writer.write("obj no_subloan="+stud.getStudentApSubLoans());
            writer.write("\n<<<<<<<\n");
            writer.write("obj no_unsubloan="+stud.getStudentAqUnsubLoans());        

        
            sb.setLength(0);        
            writer.write("\n\n");               
            
            pos = 0;
            for( String key : vmap.keySet()){
                pos=chkParaByName(key);
                writer.write(" parameter name="+String.format("%-20s","["+ key+"]")+" val="+ String.format("%-20s","["+vmap.get(key)+"]")+" mapped to attr="+fields[pos]+"\n");                  
            }
            
            
            
            writer.write("\n\n");   
            for( String fn : fields){
                try {
                    Field x = stdcls.getDeclaredField(fn);
                    x.setAccessible(true);                                 
                    writer.write(" attr name="+ String.format("%-30s", x.getName())+" val="+ x.get(stud)+"\n");
                    //writer.write(" declared field type="+ x.getType()+"\n");
                    //writer.write(" attr val ="+ x.get(stud)+"\n\n");                        
                } catch (NoSuchFieldException ex) {       
                    log.info("setAttr nsfield: ", ex);
                } catch (SecurityException ex) {
                    log.info("setAttr security: ", ex);
                } catch (IllegalArgumentException ex) {
                    log.info("setAttr argument: ", ex);
                } catch (IllegalAccessException ex) {
                    log.info("setAttr access: ", ex);
                }           
            }        
            writer.write("\n<<<<<<<\n\n");
            writer.write( echoRes2(calc, stud) );
        }
        
        
        long end = System.nanoTime();
        //writer.write("\n\n eclipsed: "+(end-start)/1000000 +" ms");
        //    calc.showMaxAidAmt() calc.getUse_need_ind()
        log.info( new StringBuilder(250).append(" <<< >>> NetEcho from ").append(req.getRemoteAddr()).append( " for [").append(vmap.get("std_lsuid")).append("] by [").append(vmap.get("reviewerid"))
                .append("] with [test=").append(test_ind).append(" save_sfs=").append(save_ind).append("] get tot-aid=").append(calc.showMaxAidAmt()).append(" need-based=").append(calc.getUse_need_ind())
                .append(" in ").append((end-start)/1000000).append(" ms, save_msg=").append(save_msg).toString() );
    }
    
    
    private int chkParaByName(String pn){
        int res=-1;
        if( pn!=null && pn.trim().length()>0){
            String pnn = pn.trim().toLowerCase();
            //for( String one : para_name){
            for( int p=0; p< para_name.length; p++){
                if( para_name[p].equals(pnn)){
                    res = p;
                    break;
                }
            }
        }
        return res;
    }
    
    
    private int isNumeric( String str){
        if( str==null || str.isEmpty()) return 0;
        int res = 0;
        String s = str.trim();
        try{
            BigDecimal big = new BigDecimal(s);
             if( big.toPlainString().equals(s)) res = 1;
        }catch(Exception e){            
        }
        return res;        
    }
    /*
    // Create a DecimalFormat that fits your requirements
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        // parse the string
        BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse("10,692,467,440,017.120");
        System.out.println(bigDecimal);
    */
    //If you are building an application with I18N support you should use DecimalFormatSymbols(Locale)
    //create a NumberFormat and call setParseBigDecimal(true). Then parse( will give you a BigDecimal without worrying about manually formatting.
    
    
    /*
    import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class Main
{
    public static void main(String[] args)
    {
        final BigDecimal numberA;
        final BigDecimal numberB;

        numberA = stringToBigDecimal("1,000,000,000.999999999999999", Locale.CANADA);
        numberB = stringToBigDecimal("1.000.000.000,999999999999999", Locale.GERMANY);
        System.out.println(numberA);
        System.out.println(numberB);
    }

    private static BigDecimal stringToBigDecimal(final String formattedString,
                                                 final Locale locale)
    {
        final DecimalFormatSymbols symbols;
        final char                 groupSeparatorChar;
        final String               groupSeparator;
        final char                 decimalSeparatorChar;
        final String               decimalSeparator;
        String                     fixedString;
        final BigDecimal           number;

        symbols              = new DecimalFormatSymbols(locale);
        groupSeparatorChar   = symbols.getGroupingSeparator();
        decimalSeparatorChar = symbols.getDecimalSeparator();

        if(groupSeparatorChar == '.')
        {
            groupSeparator = "\\" + groupSeparatorChar;
        }
        else
        {
            groupSeparator = Character.toString(groupSeparatorChar);
        }

        if(decimalSeparatorChar == '.')
        {
            decimalSeparator = "\\" + decimalSeparatorChar;
        }
        else
        {
            decimalSeparator = Character.toString(decimalSeparatorChar);
        }

        fixedString = formattedString.replaceAll(groupSeparator , "");
        fixedString = fixedString.replaceAll(decimalSeparator , ".");
        number      = new BigDecimal(fixedString);

        return (number);
    }
}
    */
    
    /*
    (locale need to be obtained dynamically)

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Locale;

class TestBigDecimal {
    public static void main(String[] args) {

        String str = "0,00";
        Locale in_ID = new Locale("in","ID");
        //Locale in_ID = new Locale("en","US");

        DecimalFormat nf = (DecimalFormat)NumberFormat.getInstance(in_ID);
        nf.setParseBigDecimal(true);

        BigDecimal bd = (BigDecimal)nf.parse(str, new ParsePosition(0));

        System.out.println("bd value : " + bd);
    }
}
    */
    
    private int chkParaByVal(int pos, String pv, HashMap<Integer, Object> map)  {
        int res=0;
        if( pos>=0 && pos< para_name.length && pv!=null && !pv.isEmpty() ){
            int t = para_type[pos];
            int i=0;
            BigDecimal big = null;
            Date dt = null;
            String s  = pv.trim().toLowerCase();
            String s2 = pv.trim();
            if( s.equals("undefined") || s.equals("null")) return res;
            
            switch (t) {
                case 0: if( isNumeric(s)>0 ){ 
                            i = Integer.parseInt(s)   ;  
                            if( i!=0) i=1;
                            res = 1;
                            map.put(pos, i);
                        }
                       break;
                case 1: if( isNumeric(s)>0 ){
                            i =  Integer.parseInt(s)  ;                 
                            map.put(pos, i);
                            res = 1;
                         }
                       break;
                case 2: if( isNumeric(s)>0 && s.equals( (big = new BigDecimal(s)).toString())  ) {
                            res=1;
                            map.put(pos, big);
                        }
                       break;
                case 3:res=1;
                       map.put(pos, s2);
                       break;
                case 4: if( s.equals("yes") || s.equals("no")){
                            res = 1;
                            map.put(pos, s);
                        }
                       break;
                case 5:if( s.equals("true") || s.equals("false")){
                            res = 1;
                            map.put(pos, s);
                        }
                       break;
                case 6:s = s.replaceAll("[^0-9]+", ""); // or .replaceAll("\\D+", "");
                       if(s.length()==6){
                            try {
                                dt = dfs.parse(s);
                                res = 1;
                                map.put(pos, df.format(dt));
                            } catch (ParseException ex) {
                                
                            }
                       }else if( s.length()==8){
                           try {
                                dt = dfl.parse(s);                                
                                res=1;
                                map.put(pos, df.format(dt));
                            } catch (ParseException ex) {                                
                            }
                       }                           
                       break;
                default: break;
            }
            //java.lang.Integer cannot be cast to java.lang.String (String)Int   or Int.toString()
//            log.info("+++++++++++++ map put in data, type=%d, k=%d, v=[%s], os=%s, ns=%s", t, pos, String.valueOf( map.get(pos) ), s, s2 );
        }
        /* replace works on char data type but replaceAll works on String datatype and both replace the all occurrences of first argument with second argumen */
        //Uncompilable source code - Erroneous tree type: <any>
        
        return res;
    }
    
    private void syncParaToAttr(HashMap<Integer, Object> map, HashMap<String, Field> fmap, Student std){
        for( Integer pos : map.keySet()){            
            int t = attr_type[pos];
            Object pv = map.get(pos);
            String sv = String.valueOf(pv).toLowerCase();
            
            int pt = para_type[pos];
            String pn = para_name[pos];
                        
            String an = fields[pos];
            Field f = fmap.get(an);
            //Class stdcls = std.getClass();
            
            switch (pt){
                case 0: 
                case 1: 
                case 2:                 
                case 4:
                case 6: setAttr(std, f, pv, pt);
                        break;    
                case 3: if( pn.equals("std_merit")){
                            switch (sv) {
                                case "c": pv = "MC"; break;
                                case "s": pv = "MS"; break;
                                case "f": pv = "MF"; break;
                                default: pv="";
                                         break;
                            }   
                            //pv = "M"+((String)pv).toUpperCase(); // will come MX M*
                            
                        }else if( pn.equals("std_sex")){
                            pv = String.valueOf(pv).toUpperCase();
                        }  
                        setAttr(std, f, pv, pt);
                        break;
                case 5: switch (pn){
                    case "std_eanonlsu":
                           setAttr(std, f, sv.equals("true") ? new BigDecimal(1) : new BigDecimal(0),  2); //studentAgNonlsuAllowrance not Int, but BigDecimal
                           break;
                    case "std_returning":
                    case "std_nc": 
                            setAttr(std, f, sv.equals("true") ? 1 : 0,  0); //studentAgNonlsuAllowrance not Int, but BigDecimal
                            break;
                    default:
                            setAttr(std, f, sv.equals("true") ? "yes" : "no",  4);
                            break;
                        }
                        break;
                default: break;    
            }            
            //true/false -> yes/no            
            //paras: std_ealsu, std_retuning std_nc true/false -> 0/1
            //merit C/S/F -> MC/MS/MF            
        }//end of for
        
        //then list all event lisenter's converting bahaviors, try to repeat them here. like indept can be calculated, even if not chosen by user.
    }
    
    private void setAttr(Student std, Field f, Object obj, int t){        
        try {            
            //Field f = cls.getDeclaredField(fn);            
            f.setAccessible(true);                               
            switch (t){
                case 0:
                case 1: f.set(std, (Integer)obj);
                        break;
                case 2: f.set(std, (BigDecimal)obj);
                        break;
                case 3:
                case 4:
                case 5:
                case 6: f.set(std, String.valueOf(obj) );
                        break;
                default: break;
            }                                  
        //} catch (NoSuchFieldException ex) {            
        } catch (SecurityException ex) {
            log.info("setAttr security: ", ex);
        } catch (IllegalArgumentException ex) {
            log.info("setAttr argument: ", ex);
        } catch (IllegalAccessException ex) {
            log.info("setAttr access: ", ex);
        } 
    }
    
    private String echoRes(PackFunctions calc, Student std){
        StringBuilder sb = new StringBuilder(120000);
        sb.append("OK@Version ").append(  ref.getClientverions()).append("^");
        /*
                <tr>
                    <th  colspan="2">Maximum Possible Aid</th><!-- =====================(living cost: #{estimator.calc.COL})============ -->
                </tr>                
                <tr class="#{estimator.sumrow(1)}">
                    <td class="item">Tuition &amp; Fees:</td><td class="numb">#{estimator.calc.init()}#{estimator.calc.showTuitionFees()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">+Additional Expenses</td><td class="numb">#{estimator.calc.showAddlExpense()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">-Expected Family Contribution (EFC)</td><td class="numb">#{estimator.calc.showEFC()}</td>
                </tr><!-- <h:outputText value="#{estimator.std_intl eq true? 0: estimator.stud.studentAfFamilyContrib}"><f:convertNumber pattern="$#,###" /></h:outputText> -->
                <tr>
                    <td class="item">&nbsp;</td><td class="tot">#{estimator.calc.showNeedAmt()}</td>
                </tr>
        */
        sb.append("Maximum Possible Aid@Tuition and Fees=").append(calc.showTuitionFees()).append("|Maximum Possible Aid@Additional Expenses=").append(calc.showAddlExpense());
        sb.append("|Maximum Possible Aid@-Expected Family Contribution (EFC)=").append(calc.showEFC());
        sb.append("|Maximum Possible Aid@tot=").append(calc.showNeedAmt()).append("^");        
        
             
                /*                
                <tr>
                    <th  colspan="2">Financial Aid   #{estimator.calc.refreshCalc(estimator.stud)}(#{estimator.calc.use_need_ind gt 0 ? 'need-based':'non-need based'}. top aid: #{estimator.calc.showMPA()})</th><!-- ===================================================================================== -->
                </tr>
                <tr class="subheader">
                    <td class="itemt">#{ref.padStr("External Grants","-",-1,2,35)}</td><td class="numb"></td> <!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
                </tr>
                <tr class="#{estimator.sumrow(1)}">
                    <td class="item">PELL Grant</td><td class="numb">#{estimator.calc.showPellGrantAmt()}</td> <!--.initAndShowPellGrantAmt()-->
                </tr>        
                <tr class="#{estimator.sumrow()}">
                    <td class="item">Cal Grant A</td><td class="numb">#{estimator.calc.showCalGrantA()}</td> <!--<h:outputText value="#{estimator.stud.studentAaCalgrantA}"><f:convertNumber pattern="$#,###" /></h:outputText> -->
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">Cal Grant B</td><td class="numb">#{estimator.calc.showCalGrantB()}</td> <!--<h:outputText value="#{estimator.stud.studentAbCalgrantB}"><f:convertNumber pattern="$#,###" /></h:outputText> -->
                </tr>
                
                <tr class="#{estimator.sumrow()}">
                    <td class="item">FSEOG</td><td class="numb">#{estimator.calc.showFseogAmt()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">External Educational Allowance</td><td class="numb">#{estimator.calc.showExtAllowanceAmt()}</td>
                </tr>
         */
        
        String based = calc.getUse_need_ind() > 0 ? "need-based" : "non-need based";
        based = new StringBuilder(200).append("Financial Aid (").append(based).append(", top aid: ").append( calc.showMPA()).append( ")").toString();   
        sb.append(based).append("@PELL Grant =").append(calc.showPellGrantAmt());//.append("\n");  
        sb.append("|").append(based).append("@Cal Grant A=").append(calc.showCalGrantA());//.append("\n");     
        sb.append("|").append(based).append("@Cal Grant B=").append(calc.showCalGrantB());//.append("\n");  
        sb.append("|").append(based).append("@FSEOG=").append(calc.showFseogAmt());//.append("\n");  
        sb.append("|").append(based).append("@External Allowance=").append(calc.showExtAllowanceAmt());//.append("\n");
        
        /*
                <f:subview id="opt_noncagrant" rendered="#{estimator.calc.getNonCalGrantAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.getNonCalGrantDesc()}</td>
                        <td class="numb">#{estimator.calc.showNonCalGrantAmt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_outside" rendered="#{estimator.calc.getOutsideAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.getOutsideDesc()}</td>
                        <td class="numb">#{estimator.calc.showOutsideAmt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_churchbase" rendered="#{estimator.calc.getChurchBaseAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showChurchBaseDesc()}</td>
                        <td class="numb">#{estimator.calc.showChurchBaseAmt()}</td>
                    </tr>
                </f:subview>
         */
        if( calc.getNonCalGrantAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.getNonCalGrantDesc()).append("=").append(calc.showNonCalGrantAmt());
        }
        if( calc.getOutsideAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.getOutsideDesc()).append("=").append(calc.showOutsideAmt());
        }
        if( calc.getChurchBaseAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.showChurchBaseDesc()).append("=").append(calc.showChurchBaseAmt());
        }
        /*                                
                <tr class="subheader">
                    <td class="itemt">#{ref.padStr(estimator.calc.show_sum_lasu_aid(),"-",-1,2,35)}</td><td class="numb"></td><!-- ~~~~"La Sierra Awards"~~~#{estimator.calc.sum_lasu_aid}~~~~~ -->
                </tr>
                <tr class="#{estimator.sumrow(1)}">
                    <td class="item">La Sierra Educational Allowance</td><td class="numb">#{estimator.calc.showLsuAllowanceAmt()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">SDA Award</td><td class="numb">#{estimator.calc.showSdaAwardAmt()}</td>
                </tr>
                
                
                <tr class="#{estimator.sumrow()}">
                    <td class="item">La Sierra Need Grant</td><td class="numb">#{estimator.calc.showLsuNeedGrantAmt()}</td>
                </tr>
                
                
                <tr class="#{estimator.sumrow()}">
                    <td class="item">Family Discount</td><td class="numb">#{estimator.calc.showFamilyDiscountAmt()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">National Merit Scholarship</td><td class="numb">#{estimator.calc.showNationalMeritAmt()}</td>
                </tr>
                
                
                <tr class="#{estimator.sumrow()}">
                    <td class="item">La Sierra Achievement Award</td><td class="numb">#{estimator.calc.showLsuAchievementAmt()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">La Sierra 4-year Renewable Scholarship</td><td class="numb">#{estimator.calc.showLsu4yRenewableAmt()}</td>
                </tr>
          */
        sb.append("|").append(based).append("@La Sierra Educational Allowance=").append(calc.showLsuAllowanceAmt());
        sb.append("|").append(based).append("@SDA Award=").append(calc.showSdaAwardAmt());
        sb.append("|").append(based).append("@La Sierra Need Grant=").append(calc.showLsuNeedGrantAmt());
        sb.append("|").append(based).append("@Family Discount=").append(calc.showFamilyDiscountAmt());
        sb.append("|").append(based).append("@National Merit Scholarship=").append(calc.showNationalMeritAmt());
        sb.append("|").append(based).append("@La Sierra Achievement Award=").append(calc.showLsuAchievementAmt());
        sb.append("|").append(based).append("@La Sierra 4-year Renewable Scholarship=").append(calc.showLsu4yRenewableAmt());
        sb.append("|").append(based).append("@La Sierra Univ. Grant=").append(calc.showLasuGrantAmt());
        /*      
                <f:subview id="opt_award1" rendered="#{estimator.calc.getScholarship1Amt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showScholarship1Desc()}</td>
                        <td class="numb">#{estimator.calc.showScholarship1Amt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_award2" rendered="#{estimator.calc.getScholarship2Amt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showScholarship2Desc()}</td>
                        <td class="numb">#{estimator.calc.showScholarship2Amt()}</td>
                    </tr>
                </f:subview>          
                <f:subview id="opt_churchmatch" rendered="#{estimator.calc.getChurchMatchAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showChurchMatchDesc()}</td>
                        <td class="numb">#{estimator.calc.showChurchMatchAmt()}</td>
                    </tr>
                </f:subview>                
                <f:subview id="opt_litEvan" rendered="#{estimator.calc.getLitEvanMatchAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showLitEvanMatchDesc()}</td>
                        <td class="numb">#{estimator.calc.showLitEvanMatchAmt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_pacCampMatch" rendered="#{estimator.calc.getPacificCampMatchAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showPacificCampMatchDesc()}</td>
                        <td class="numb">#{estimator.calc.showPacificCampMatchAmt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_nonPacCampMatch" rendered="#{estimator.calc.getNonPacificCampMatchAmt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showNonPacificCampMatchDesc()}</td>
                        <td class="numb">#{estimator.calc.showNonPacificCampMatchAmt()}</td>
                    </tr>
                </f:subview>  
        */
        if( calc.getScholarship1Amt() > 0){
            sb.append("|").append(based).append("@").append(calc.showScholarship1Desc()).append("=").append(calc.showScholarship1Amt());
        }
        if( calc.getScholarship2Amt() > 0){
            sb.append("|").append(based).append("@").append(calc.showScholarship2Desc()).append("=").append(calc.showScholarship2Amt());
        }
        if( calc.getChurchMatchAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.showChurchMatchDesc()).append("=").append(calc.showChurchMatchAmt());
        }
        if( calc.getLitEvanMatchAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.showLitEvanMatchDesc()).append("=").append(calc.showLitEvanMatchAmt());
        }
        if( calc.getPacificCampMatchAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.showPacificCampMatchDesc()).append("=").append(calc.showPacificCampMatchAmt());
        }
        if( calc.getNonPacificCampMatchAmt() > 0){
            sb.append("|").append(based).append("@").append(calc.showNonPacificCampMatchDesc()).append("=").append(calc.showNonPacificCampMatchAmt());
        }
        /*
                
                <f:subview id="opt_award7" rendered="#{estimator.calc.getScholarship7Amt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showScholarship7Desc()}</td>
                        <td class="numb">#{estimator.calc.showScholarship7Amt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_award8" rendered="#{estimator.calc.getScholarship8Amt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showScholarship8Desc()}</td>
                        <td class="numb">#{estimator.calc.showScholarship8Amt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_award9" rendered="#{estimator.calc.getScholarship9Amt() gt 0 }">
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showScholarship9Desc()}</td>
                        <td class="numb">#{estimator.calc.showScholarship9Amt()}</td>
                    </tr>
                </f:subview>                
        */
        if( calc.getScholarship7Amt() > 0){
            sb.append("|").append(based).append("@").append(calc.showScholarship7Desc()).append("=").append(calc.showScholarship7Amt());
        }
        if( calc.getScholarship8Amt() > 0){
            sb.append("|").append(based).append("@").append(calc.showScholarship8Desc()).append("=").append(calc.showScholarship8Amt());
        }
        if( calc.getScholarship9Amt() > 0){
            sb.append("|").append(based).append("@").append(calc.showScholarship9Desc()).append("=").append(calc.showScholarship9Amt());
        }
        /*
                
                <tr class="subheader">
                    <td class="itemt">#{ref.padStr("Loans","-",-1,2,35)}</td><td class="numb"></td><!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
                </tr>
                <f:subview id="exlude_loans" rendered="#{estimator.std_fafsa eq false or estimator.std_noloans eq true }">
                    <tr class="#{estimator.sumrow(1)}">
                        <td  class="item">#{estimator.std_fafsa eq false ? "not FAFSA eligible":"Option [exclude loans] is checked"}</td>
                        <td class="numb">n/a</td>
                    </tr>
                </f:subview>
                
                                
                <f:subview id="include_loans" rendered="#{estimator.std_noloans eq false and estimator.std_fafsa eq true}">
                    <tr class="#{estimator.sumrow(1)}">
                        <td class="item">Subsidized Direct Loan (borrow #{estimator.calc.showAmt( estimator.calc.getOrg_loan_amt_sub() )})</td><td class="numb">#{estimator.in_subloan eq true? estimator.calc.showSubdirectAmt() : 'excluded'}</td>
                    </tr>
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">Perkins Loan (borrow #{estimator.calc.showAmt( estimator.calc.getOrg_loan_amt_perkins() )})</td><td class="numb">#{estimator.calc.showPerkinLoanAmt()}</td>
                    </tr>
                    <tr class="#{estimator.sumrow()}">
                        <td class="item">Unsubsidized Direct Loan (borrow #{estimator.calc.showAmt( estimator.calc.getOrg_loan_amt_unsub() )})</td><td class="numb">#{estimator.in_unsubloan eq true? estimator.calc.showUnsubdirectAmt() : 'excluded'}</td>
                    </tr>
                </f:subview>                
                                
                
                <f:subview id="opt_fws" rendered="#{estimator.in_fws eq true and estimator.calc.getFwsAmt() gt 0}" >  
                    <!--<tr><td colspan="2">&nbsp;</td></tr> -->
                    <tr class="subheader">
                        <td class="itemt">#{ref.padStr("Federal Work-study","-",-1,2,35)}</td><td class="numb"></td><!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
                    </tr>
                    <tr class="#{estimator.sumrow(1)}">
                        <td class="item">Federal Work-study</td><td class="numb">#{estimator.calc.showFwsAmt()}</td>
                    </tr>                    
                </f:subview>
                
                <tr>
                    <td class="item">&nbsp;</td><td class="tot">#{estimator.calc.showMaxAidAmt()}</td>
                </tr>
        */
        if( std.getStudentXFafsa().equalsIgnoreCase("yes") && std.getIndExcloans().equalsIgnoreCase("no")){
            sb.append("|").append(based).append("@Subsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_sub())).append(")=").append( std.getStudentApSubLoans().equalsIgnoreCase("yes")? calc.showSubdirectAmt() : "excluded" );
            sb.append("|").append(based).append("@Perkins Loan (borrow ").append( calc.showAmt(calc.getOrg_loan_amt_perkins())).append(")=").append( calc.showPerkinLoanAmt());
            sb.append("|").append(based).append("@Unsubsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_unsub())).append(")=").append( std.getStudentAqUnsubLoans().equalsIgnoreCase("yes")? calc.showUnsubdirectAmt() : "excluded");
        }
        if( std.getStudentArFws().equalsIgnoreCase("yes") && calc.getFwsAmt() > 0){
            sb.append("|").append(based).append("@Federal Work-study=").append(calc.showFwsAmt());
        }
        
        sb.append("|").append(based).append("@tot=").append(calc.showMaxAidAmt()).append("^");
        
        /* Geoff wanted to express the percentage of discount to student on his portal 2013-10-22 */        
        int cost = calc.getTuitionAndFees() + calc.getRoomAndBoard();
        int loan = calc.getPerkinsLoanAmt() + calc.getUnsubDirectAmt() + calc.getSubDirectAmt();
        int aid = calc.getFaidExtAmt();
        if( cost >0){
            sb.append("|").append(based).append("@Cost Discount Percentage=").append( Math.round(( (aid-loan)*10000/cost))/100.0 ).append("%");
            
            sb.append("|").append(based).append("@Total Discount Percentage=").append( Math.round((aid*10000/cost) )/100.0).append("%");
            
        }        
        
        /*
                
                <!-- #{estimator.calc.showSubdirectAmt()}/#{estimator.calc.showPerkinLoanAmt()}/#{estimator.calc.showUnsubdirectAmt()} -->
                <tr>
                    <th  colspan="2">Amount Due Calculation </th><!-- ===================================================================================== -->
                </tr>
                <tr class="#{estimator.sumrow(1)}">
                    <td class="item">Tuition &amp; Fees</td><td class="numb">#{estimator.calc.showTuitionFees()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">+Room &amp; Board</td><td class="numb">#{estimator.calc.initAndShowRoomBoardAmt()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">-Financial Aid <font size="smaller">#{estimator.calc.showFaidExtDesc()}</font></td><td class="numb">#{estimator.calc.showFaidAmt()}</td>
                </tr>
                
                
                <f:subview id="opt_duePacCampBase" rendered="#{estimator.calc.getPacificCampBase() gt 0}" >
                     <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showPacificCampBaseDesc()}</td>
                        <td class="numb">#{estimator.calc.showPacificCampBaseAmt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_dueNonPacCampBase" rendered="#{estimator.calc.getNonPacificCampBaseAmt() gt 0}" >
                     <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showNonPacificCampBaseDesc()}</td>
                        <td class="numb">#{estimator.calc.showNonPacificCampBaseAmt()}</td>
                    </tr>
                </f:subview>
                <f:subview id="opt_dueLitEvanBase" rendered="#{estimator.calc.getLitEvanBaseAmt() gt 0}" >
                     <tr class="#{estimator.sumrow()}">
                        <td class="item">#{estimator.calc.showLitEvanBaseDesc()}</td>
                        <td class="numb">#{estimator.calc.showLitEvanBaseAmt()}</td>
                    </tr>
                </f:subview>
                
                
                <tr>
                    <td class="item">&nbsp;</td><td class="tot">#{estimator.calc.showDueAmt()}</td>
                </tr>
        */
        sb.append( "Amount Due Calculation@Tuition and Fees=").append(calc.showTuitionFees());
        sb.append("|Amount Due Calculation@+Room and Board=").append(calc.initAndShowRoomBoardAmt());
        sb.append("|Amount Due Calculation@-Financial Aid").append(calc.showFaidExtDesc()).append("=").append(calc.showFaidAmt());
        if( calc.getPacificCampBase() > 0){
            sb.append("|Amount Due Calculation@").append(calc.showPacificCampBaseDesc()).append("=").append(calc.showPacificCampBaseAmt());
        }
        if( calc.getNonPacificCampBaseAmt() > 0){
            sb.append("|Amount Due Calculation@").append(calc.showNonPacificCampBaseDesc()).append("=").append(calc.showNonPacificCampBaseAmt());
        }
        if( calc.getLitEvanBaseAmt() > 0){
            sb.append("|Amount Due Calculation@").append(calc.showLitEvanBaseDesc()).append("=").append(calc.showLitEvanBaseAmt());
        }
        sb.append("|Amount Due Calculation@tot=").append(calc.showDueAmt()).append("^");
        /*    
                
                <tr>
                    <th  colspan="2">Payment Options</th><!-- ===================================================================================== -->
                </tr>
                <tr class="#{estimator.sumrow(1)}">
                    <td class="item">Year in Advance option <font size="smaller">(7% off)</font></td><td class="numb">#{estimator.calc.initAndShowYIA()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">Quarterly in Advance option <font size="smaller">(2% off)</font></td><td class="numb">#{estimator.calc.showQIA()}</td>
                </tr>
                <tr class="#{estimator.sumrow()}">
                    <td class="item">Monthly option <font size="smaller">($90 annual fee included)</font></td><td class="numb">#{estimator.calc.showMonthlyOption()}</td>
                </tr>
                <!--tr><td colspan="2">&nbsp;</td></tr-->
            </table>
                */
        sb.append( "Payment Options@Year in Advance option (7% off)=").append(calc.initAndShowYIA());
        if( !std.getStudentUAcademic().equalsIgnoreCase("CJ")){
            sb.append("|Payment Options@Quarterly in Advance option (2% off)=").append(calc.showQIA());
        }
        sb.append("|Payment Options@Monthly option ($90 annual fee included)=").append(calc.showMonthlyOption());
        
        
        return sb.toString();
    }
    
    
    private String echoRes2(PackFunctions calc, Student std){
        StringBuilder sb = new StringBuilder(120000);
        sb.append("\n\n");
        
        sb.append("Maximum Possible Aid").append("\n");
        sb.append("Tuition and Fees=").append(calc.showTuitionFees()).append("\n");
        sb.append("Additional Expenses=").append(calc.showAddlExpense()).append("\n");
        sb.append("-Expected Family Contribution (EFC)=").append(calc.showEFC()).append("\n");
        sb.append("tot=").append(calc.showNeedAmt()).append("\n\n");       
        
             
        
        String based = calc.getUse_need_ind() > 0 ? "need-based" : "non-need based";
        based = new StringBuilder(200).append("Financial Aid (").append(based).append(", top aid: ").append( calc.showMPA()).append( ")").toString();   
        sb.append(based).append("\n");
        sb.append("PELL Grant =").append(calc.showPellGrantAmt()).append("\n");
        sb.append("Cal Grant A=").append(calc.showCalGrantA()).append("\n");;//.append("\n");     
        sb.append("Cal Grant B=").append(calc.showCalGrantB()).append("\n");;//.append("\n");  
        sb.append("FSEOG=").append(calc.showFseogAmt()).append("\n");;//.append("\n");  
        sb.append("External Allowance=").append(calc.showExtAllowanceAmt()).append("\n");;//.append("\n");
        
        
        if( calc.getNonCalGrantAmt() > 0){
            sb.append(calc.getNonCalGrantDesc()).append("=").append(calc.showNonCalGrantAmt()).append("\n");
        }
        if( calc.getOutsideAmt() > 0){
            sb.append(calc.getOutsideDesc()).append("=").append(calc.showOutsideAmt()).append("\n");
        }
        if( calc.getChurchBaseAmt() > 0){
            sb.append(calc.showChurchBaseDesc()).append("=").append(calc.showChurchBaseAmt()).append("\n");
        }
        
        sb.append("La Sierra Educational Allowance=").append(calc.showLsuAllowanceAmt()).append("\n");
        sb.append("SDA Award=").append(calc.showSdaAwardAmt()).append("\n");
        sb.append("La Sierra Need Grant=").append(calc.showLsuNeedGrantAmt()).append("\n");
        sb.append("Family Discount=").append(calc.showFamilyDiscountAmt()).append("\n");
        sb.append("National Merit Scholarship=").append(calc.showNationalMeritAmt()).append("\n");
        sb.append("La Sierra Achievement Award=").append(calc.showLsuAchievementAmt()).append("\n");
        sb.append("La Sierra 4-year Renewable Scholarship=").append(calc.showLsu4yRenewableAmt()).append("\n");
        
        
        if( calc.getScholarship1Amt() > 0){
            sb.append(calc.showScholarship1Desc()).append("=").append(calc.showScholarship1Amt()).append("\n");
        }
        if( calc.getScholarship2Amt() > 0){
            sb.append(calc.showScholarship2Desc()).append("=").append(calc.showScholarship2Amt()).append("\n");
        }
        if( calc.getChurchMatchAmt() > 0){
            sb.append(calc.showChurchMatchDesc()).append("=").append(calc.showChurchMatchAmt()).append("\n");
        }
        if( calc.getLitEvanMatchAmt() > 0){
            sb.append(calc.showLitEvanMatchDesc()).append("=").append(calc.showLitEvanMatchAmt()).append("\n");
        }
        if( calc.getPacificCampMatchAmt() > 0){
            sb.append(calc.showPacificCampMatchDesc()).append("=").append(calc.showPacificCampMatchAmt()).append("\n");
        }
        if( calc.getNonPacificCampMatchAmt() > 0){
            sb.append(calc.showNonPacificCampMatchDesc()).append("=").append(calc.showNonPacificCampMatchAmt()).append("\n");
        }
        
        if( calc.getScholarship7Amt() > 0){
            sb.append(calc.showScholarship7Desc()).append("=").append(calc.showScholarship7Amt()).append("\n");
        }
        if( calc.getScholarship8Amt() > 0){
            sb.append(calc.showScholarship8Desc()).append("=").append(calc.showScholarship8Amt()).append("\n");
        }
        if( calc.getScholarship9Amt() > 0){
            sb.append(calc.showScholarship9Desc()).append("=").append(calc.showScholarship9Amt()).append("\n");
        }
        
        if( std.getStudentXFafsa().equalsIgnoreCase("yes") && std.getIndExcloans().equalsIgnoreCase("no")){
            sb.append("Subsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_sub())).append(")=").append( std.getStudentApSubLoans().equalsIgnoreCase("yes")? calc.showSubdirectAmt() : "excluded" ).append("\n");
            sb.append("Perkins Loan (borrow ").append( calc.showAmt(calc.getOrg_loan_amt_perkins())).append(")=").append( calc.showPerkinLoanAmt()).append("\n");
            sb.append("Unsubsidized Direct Loan (borrow ").append(calc.showAmt(calc.getOrg_loan_amt_unsub())).append(")=").append( std.getStudentAqUnsubLoans().equalsIgnoreCase("yes")? calc.showUnsubdirectAmt() : "excluded").append("\n");
        }
        if( std.getStudentArFws().equalsIgnoreCase("yes") && calc.getFwsAmt() > 0){
            sb.append("Federal Work-study=").append(calc.showFwsAmt()).append("\n");
        }
        
        sb.append("tot=").append(calc.showMaxAidAmt()).append("\n\n");//.append("^");
        
                
        sb.append( "Amount Due Calculation").append("\n");
        sb.append("Tuition and Fees=").append(calc.showTuitionFees()).append("\n");
        sb.append("+Room and Board=").append(calc.initAndShowRoomBoardAmt()).append("\n");
        sb.append("-Financial Aid").append(calc.showFaidExtDesc()).append("=").append(calc.showFaidAmt()).append("\n");
        if( calc.getPacificCampBase() > 0){
            sb.append(calc.showPacificCampBaseDesc()).append("=").append(calc.showPacificCampBaseAmt()).append("\n");
        }
        if( calc.getNonPacificCampBaseAmt() > 0){
            sb.append(calc.showNonPacificCampBaseDesc()).append("=").append(calc.showNonPacificCampBaseAmt()).append("\n");
        }
        if( calc.getLitEvanBaseAmt() > 0){
            sb.append(calc.showLitEvanBaseDesc()).append("=").append(calc.showLitEvanBaseAmt()).append("\n");
        }
        sb.append("tot=").append(calc.showDueAmt()).append("\n\n");//.append("^");
        
        /* Geoff wanted to express the percentage of discount to student on his portal 2013-10-22 */        
        int cost = calc.getTuitionAndFees() + calc.getRoomAndBoard() ;
        int loan = calc.getPerkinsLoanAmt() + calc.getUnsubDirectAmt() + calc.getSubDirectAmt();
        int aid = calc.getFaidExtAmt();
        if( cost >0){
             //--sb.append("p1=").append( (aid-loan)/cost);
            sb.append("\n").append(based).append("Cost Discount Percentage=").append( Math.round(( (aid-loan)*10000/cost))/100.0 ).append("%");
            //--sb.append("p2=").append(aid/cost);
            sb.append("\n").append(based).append("Total Discount Percentage=").append( Math.round((aid*10000/cost) )/100.0).append("%");
        }    
        sb.append("\n").append("cost=").append(cost).append(" loan=").append(loan).append(" aid=").append(aid);
        sb.append("\n\n");
                        
        
        sb.append( "Payment Options").append("\n");
        sb.append("Year in Advance option (7% off)=").append(calc.initAndShowYIA()).append("\n");
        if( !std.getStudentUAcademic().equalsIgnoreCase("CJ")){
            sb.append("Quarterly in Advance option (2% off)=").append(calc.showQIA()).append("\n");
        }
        sb.append("Monthly option ($90 annual fee included)=").append(calc.showMonthlyOption()).append("\n");
        
        
        return sb.toString();
    }
}

/*
try {            
            Field f = stdcls.getDeclaredField("studentMMarry");
            f.setAccessible(true);
            
            writer.write("init val=");
            writer.write( (String)f.get(stud));
            f.set(stud, "reflection");
            writer.write("\nmod val=");
            writer.write( (String)f.get(stud));
            
        } catch (NoSuchFieldException ex) {
            java.util.logging.Logger.getLogger(NetFeeder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(NetFeeder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(NetFeeder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NetFeeder.class.getName()).log(Level.SEVERE, null, ex);
        } 
*/


/*
        for( Field x : stdcls.getDeclaredFields()){
            x.setAccessible(true);
            //writer.write("\""+ x.getName()+"\", ");
            
            for(String fn : fields){
                if( x.getName().equals(fn) ){
                    writer.write(" declared field name="+ x.getName()+"\n");
                    writer.write(" declared field type="+ x.getType()+"\n");
                    try {
                        writer.write(" declared field valu="+ x.get(stud)+"\n");
                    } catch (IllegalArgumentException ex) {
                        java.util.logging.Logger.getLogger(NetFeeder.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(NetFeeder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    writer.write(" \n");
                    break;
                }
            }            
        }*/