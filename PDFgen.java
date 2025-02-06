/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.joda.time.DateTime;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static edu.lsu.estimator.PackValues.*;
/**
 *
 * @author kwang
 */
//@Named
@SessionScoped
public class PDFgen implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private  final String PDFROOT_UNIX="/var/estimator-files/";
    private  final String PDFROOT_MS = "c:\\";
    private  String TMPLATE = "estimator2012_t.pdf";
    private  String fisy_aidy = "2012-2013";
    private  int    fisy      = 2012;
  
    
    private String   pdfname = null;
    private final String[] modes = {"-", ".", "_", ","}; //=== save, print, email, mackup
    
    //@Inject AppReference ref;
    private static final Logger log = LoggerFactory.getLogger();
    
    public PDFgen(){
        syncFISY(); //since no ref to "ref" obj, do it here to updating fisy
    }
    
    public void syncFISY(){  
        /*
        SimpleDateFormat d = new java.text.SimpleDateFormat("yyyy") ;
        String strFISY = d.format(new Date());
        TMPLATE = "estimator"+ strFISY + "_t.pdf";
        int iFISY = Integer.parseInt(strFISY);
        fisy = iFISY;
        fisy_aidy = strFISY+" ~ " + (iFISY+1);
        */
        
        /*
        TMPLATE = "estimator"+ ref.getFiscal_year() + "_t.pdf";
        fisy = ref.getFiscal_year();
        fisy_aidy = ref.getFaid_year();
        */
    }
    public void resyncFISY(AppReference ref){ 
        TMPLATE = "estimator"+ ref.getFiscal_year() + "_t.pdf";
        fisy = ref.getFiscal_year();
        fisy_aidy = ref.getFaid_year();
    }
    
    public  String getPdfRoot2(String separator){ //file.separator   ( System.getproperties()).list(System.out);        
        if( separator.equals("/")){
            return PDFROOT_UNIX +fisy+separator;
        }else{
            return PDFROOT_MS +fisy+separator;
        }
    }
    
    public  String getPdfRoot(){
        String seperator = System.getProperty("file.separator");
        return getPdfRoot2(seperator);
    }
    /*
    private void servePDF(Student std){ //let the estimator to call two sub procs, to gain more control
        String msg = genPDF(std, null, null, null);
        if( !msg.isEmpty()){
            msg = downloadPDF(); //pdfname
        }else{
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  msg, msg) );
        }
    }
    * 
    * ***** mod PDF content
    PdfContentByte underContent = pdfStamper.getUnderContent(1); //one page after current content    
    PdfContentByte overContent  = pdfStamper.getOverContent(1);  //one page before current content
        underContent.beginText();
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED);
        underContent.setFontAndSize(bf, 12);
        underContent.setColorFill(Color.BLACK);
        underContent.showTextAligned(Element.ALIGN_TOP, " This is footer ", 0, 2, 0); 
        underContent.endText(); 
        pdfStamper.close();
        * 
    * edit template and save a new one
    *PdfStamper stamp1 = new PdfStamper(reader, prFormTemplate, '\0',true); 

reader - the original document. It cannot be reused 
prFormTemplate - the output stream 
'\0' or version - the new pdf version or '\0' to keep the same version as the original document 
true/false - if true appends the document changes as a new revision. This is only useful for multiple signatures as nothing is gained in speed or memory 

* ADD watermark
*
      PdfReader pdfReader = new PdfReader("HelloWorld.pdf");
      PdfStamper pdfStamper = new PdfStamper(pdfReader,            new FileOutputStream("HelloWorld-Stamped.pdf"));

      Image image = Image.getInstance("watermark.png");
      for(int i=1; i<= pdfReader.getNumberOfPages(); i++){

          PdfContentByte content = pdfStamper.getUnderContent(i);
          image.setAbsolutePosition(100f, 700f);
          content.addImage(image);
      }
      pdfStamper.close();
    */
    
    String genPDF(Student std, Login login, AppReference ref, PackFunctions calc, Print pdf,   int mode, Integer... p){ //Calculator calc
        resyncFISY(ref);
        
        boolean pu = p.length>0;
        
        String msg = "";
        File tmpt = new File(getPdfRoot()+TMPLATE);
        if( !tmpt.exists() || !tmpt.isFile() || !tmpt.canRead()){
            msg = "Failed: invalid PDF template";
            log.info(" not found template=%s, path=%s, name=%s ", tmpt.getAbsolutePath(), tmpt.getPath(), tmpt.getName());
            
        }else if( std==null){
            msg = "Failed: no student data";
        }else if( std.getRecid()==null || std.getRecid().isEmpty() || std.getStudentBLastname()==null || std.getStudentBLastname().isEmpty()){
            msg = "Failed: student data is not valid";
        }else{
            int fisy  = std.getStudentFisy();
            String recid = std.getRecid();            
           
            String timeShowFmtStr = "yyyy-MM-dd h:mm:ss a z"; //a: AP PM  //z: PST  //Z: +0800
            SimpleDateFormat sdf = new java.text.SimpleDateFormat(timeShowFmtStr) ;            
            //sdf.format(new Date());
            
            
            String ln = std.getStudentBLastname().trim().replaceAll(" ", "_");
            String fn = std.getStudentCFirstname().trim().replaceAll(" ", "_");
//            pdfname = fisy+"_"+ln+"."+fn+"_"+recid+".pdf"; //+"_"+sdf.format(new Date())
            pdfname = fisy+modes[mode]+ln+"."+fn+modes[mode]+recid+".pdf"; 
            
            String str="", stmp=""; int x=0, amt=0;
            try{
                PdfReader reader = new PdfReader(getPdfRoot()+TMPLATE);//c:\\java\\lsu_dorm_commit.pdf
                PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(getPdfRoot()+pdfname)); //c:\\java\\lsu_dorm_committed_flat.pdf
                AcroFields form = stamp.getAcroFields();
                
                setMetaData(reader, stamp, std, sdf, ref.getClientverions());
                
                if(std.getDdoe()>0 && (new DateTime( std.getDdoe()).getYear())== ref.getFiscal_year() ) {    
                    //log.info(" pdfGEN get pu=%s", pu);
                     str = ref.fmtFullTimeWithZone(std.getDdoe(), std.getTzdoe());
                     if( pu ) //--not likely
                         form.setField("doe", "entered on "+str+ (std.getPuser_id().equals(std.getStudentALsuid())? " by self":"")+ " via MyCampus Portal");
                     else{
                         boolean portal =  std.getPuser_id()!=null ;//|| (std.getModRoot()!=null && std.getModRoot().startsWith("0.") ); //std.getCounselorOrig()==0 ||
                         boolean org_portal = std.getCounselorOrig()==0 || (std.getModRoot()!=null && std.getModRoot().startsWith("0.") ); //
                         form.setField("doe", "entered on "+str+ ( portal ? (std.getPuser_id().equals(std.getStudentALsuid())? " by self":"")+" via MyCampus Portal" : (org_portal ?" via ":" by ")+std.getStudentBuOrigCounselor() ));//"by "+std.getStudentBuOrigCounselor());
        //                 log.info("---set DOE on PDF ---");
                     }
                }    
                if( std.getDdom()>0){
                    str = ref.fmtFullTimeWithZone(std.getDdom(), std.getTzdom()); //NPEE
                    if ( pu ) //--not likely
                        form.setField("dom", "updated on "+str+" by "+"MyCampus Portal");
                    else
                        form.setField("dom", "updated on "+str+" by "+std.getStudentBxModCounselor());
                }
                
                
                
                form.setField("year", ref.getFaid_year()); //"2012-2013"
                
                str = ref.fmtFullNowTimeWithDefaultZone();
                if( pu )
                    form.setField("pdf_time", str+ (std.getPuser_id().equals(std.getStudentALsuid())? " by self":"")+ " via MyCampus Portal");
                else
                    form.setField("pdf_time", str+" by "+login.getCurrentUser().getUsername()); 
                
                form.setField("verions", ref.getClientverions());
                pdf.setVersions(ref.getClientverions() );
                form.setField("recid", std.getRecid());
                pdf.setRecid( std.getRecid());
                
                //log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv   print has recid=%s ", pdf.getRecid());
            //    System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvv   print has recid="+pdf.getRecid());
                
                //2012-03-16 Esther aksed to put first name first
                str = std.getStudentCFirstname()+" "+std.getStudentBLastname(); //std.getStudentBLastname()+" "+std.getStudentCFirstname();
                stmp= std.getStudentALsuid();
                
                if( stmp==null ||stmp.isEmpty())
                    x=0;
                else
                    x=1;
                
                form.setField("name_id", str + (x>0? " ("+std.getStudentALsuid()+")": ""));
                form.setField("street", std.getStudentGStreet());
                form.setField("city_state_zip",  std.getStudentHCity()+ (ref.isEmp(std.getStudentHCity())? "":", ")+std.getStudentIState()+" "+ std.getStudentJZip());
                form.setField("email", std.getStudentEEmail());

                form.setField("gpa", std.getStudentPGpa().toString());            
                form.setField("sat", std.getStudentQSat().toString());  
                form.setField("act", std.getStudentRAct().toString());  
                form.setField("highschool", std.getStudentOLastSchool());
                form.setField("major", std.getStudentTMajor());
                form.setField("phonenumber", std.getStudentFPhone());

                str = std.getStudentAnPubNotes();
                if( !ref.isEmp(str))         form.setField("pub_notes", str);
                else form.setField("pub_notes", "none");
                
                calc.init();//JUST reset static global values                
                calc.initAndShowPellGrantAmt();
                
                str = calc.showTuitionFees();
                pdf.setTuitionFee( ref.convertCurrencyStrToInt(str) );                        
                form.setField("tuition_fee", str);         //lsu_tuition_fees       
                
                //form.setField("coa", "$1234");
                str = calc.showAddlExpense();
                pdf.setOtherExpenses( ref.convertCurrencyStrToInt(str));
                form.setField("expense", str);
                
                str = calc.showEFC();
                pdf.setEfc( ref.convertCurrencyStrToInt(str));
                form.setField("efc", str);
                
                str = calc.showNeedAmt();
                pdf.setMaxNeed(ref.convertCurrencyStrToInt(str) );
                form.setField("need", str);

                int line=1, subtot=0;
                //ref.padStr("External Grants","-",-1,2,36)
                //str = calc.initAndShowPellGrantAmt();
                calc.refreshCalc(std);
                str = calc.showPellGrantAmt();                
                amt = ref.convertCurrencyStrToInt(str);
                pdf.setPell( amt ); //ref.convertCurrencyStrToInt(str)
                if( amt>0)setLineContent(form,  line++, "Federal PELL Grant", str, null);
                subtot += pdf.getPell();
                
                str = calc.showCalGrantA();
                amt = ref.convertCurrencyStrToInt(str);
                pdf.setCalGrantA( amt );
                if(amt>0)setLineContent(form,  line++, "Cal Grant A", str, null);
                subtot += pdf.getCalGrantA();
                
                str = calc.showCalGrantB();
                amt = ref.convertCurrencyStrToInt(str);
                pdf.setCalGrantB(amt );
                if(amt>0)setLineContent(form,  line++, "Cal Grant B", str, null);
                subtot += pdf.getCalGrantB();
                
                str = calc.showFseogAmt();
               amt = ref.convertCurrencyStrToInt(str);
                pdf.setPseog(  amt );
                if(amt>0)setLineContent(form,  line++, "FSEOG", str, null);
                subtot += pdf.getPseog();
                
                str = calc.showExtAllowanceAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setNonLsuEa(  amt );
                if(amt>0)setLineContent(form,  line++, "External Educational Allowance", str, null);
                subtot += pdf.getNonLsuEa();
                
                pdf.setNoncalGrant(calc.getNonCalGrantAmt());
                subtot += pdf.getNoncalGrant();
                if( calc.getNonCalGrantAmt()>0){
                    str = calc.showNonCalGrantAmt();
                    setLineContent(form,  line++, calc.getNonCalGrantDesc(), str, null);
                }
                
                pdf.setNonLsuSship( calc.getOutsideAmt());
                subtot += pdf.getNonLsuSship();
                if( calc.getOutsideAmt() >0){
                    str = calc.showOutsideAmt();
                    setLineContent(form,  line++, calc.getOutsideDesc(), str, null);
                }
                
                pdf.setSship3ExtAmt( calc.getChurchBaseAmt());
                subtot += pdf.getSship3ExtAmt();
                if( calc.getChurchBaseAmt()>0){
                    str = calc.showChurchBaseAmt();
                    setLineContent(form,  line++, calc.showChurchBaseDesc(), str, null);
                }
                
                if(subtot>0){
                    setLineContent(form,  line++, "External Grants Subtotal", null, ref.fmtMoney(subtot) );
                    setLineContent(form,  line++, " ", null, null);
                }                
                subtot = 0;
                
                
                str = calc.showLsuAllowanceAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setLsuEa( amt );                
                if(amt>0)setLineContent(form,  line++, "La Sierra Educational Allowance", str, null);
                subtot += pdf.getLsuEa();
                
                str = calc.showSdaAwardAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setSda( amt );                
                if(amt>0)setLineContent(form,  line++, "SDA Award", str, null);
                subtot += pdf.getSda();
                /*
                str = calc.showLsuPerformanceAmt();
                pdf.setLsuPerf( ref.convertCurrencyStrToInt(str) );
                subtot += pdf.getLsuPerf();
                setLineContent(form,  line++, "La Sierra Performance", str, null);
                */
                str = calc.showLsuNeedGrantAmt();//.showLsuPerformanceAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setNeedGrant( amt );                
                if(amt>0)setLineContent(form,  line++, "La Sierra Need Grant", str, null);
                subtot += pdf.getNeedGrant();//.getLsuPerf();                
                
                str = calc.showFamilyDiscountAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setFamilyDisct(  amt );                
                if(amt>0)setLineContent(form,  line++, "Family Discount", str, null);
                subtot += pdf.getFamilyDisct();
                
                str = calc.showNationalMeritAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setNatlMerit( amt );                
                if(amt>0)setLineContent(form,  line++, "National Merit Scholarship", str, null);
                subtot += pdf.getNatlMerit();
                
                //2012/02/13 add two new awards
                str = calc.showLsuAchievementAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setAchieveAward(  amt );                
                if(amt>0)setLineContent(form,  line++, "La Sierra Achievement Award", str, null);
                subtot += pdf.getAchieveAward();
                
                str = calc.showLsu4yRenewableAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setRenew4y(  amt );                
                if(amt>0)setLineContent(form,  line++, "La Sierra 4-year Renewable Scholarship", str, null);
                subtot += pdf.getRenew4y();
                
                str = calc.showLasuGrantAmt();
               amt = ref.convertCurrencyStrToInt(str); 
                pdf.setLsugrant(  amt );                
                if(amt>0)setLineContent(form,  line++, "La Sierra Univ. Grant", str, null);
                subtot += pdf.getLsugrant();
                
                //04/10/2012  weired scholarship amount <0 issue, even though the stud init them to 0
                //use int var to ensure the min is 0
                int min=0;
                
                min = calc.getScholarship1Amt();
                if( min<0)min=0;                
                pdf.setSship1LsuAmt( min );
                subtot += min;//pdf.getSship1LsuAmt();
                if(  min>0){//calc.getScholarship1Amt()>0){                    
                    setLineContent(form,  line++, calc.showScholarship1Desc(), calc.showScholarship1Amt(), null);
                }
                
                min=calc.getScholarship2Amt();
                if( min<0)min=0;
                pdf.setSship2LsuAmt( min );
                subtot += min;//pdf.getSship2LsuAmt();
                if(  min>0){//calc.getScholarship2Amt()>0){                    
                    setLineContent(form,  line++, calc.showScholarship2Desc(), calc.showScholarship2Amt(), null);
                }
                
                min=calc.getChurchMatchAmt();
                if( min<0)min=0;
                pdf.setSship3LsuAmt( min);
                subtot += min;//pdf.getSship3LsuAmt();
                if( min>0){//calc.getChurchMatchAmt() >0){
                    setLineContent(form,  line++, calc.showChurchMatchDesc(),  calc.showChurchMatchAmt(), null);
                }
                
                min= calc.getLitEvanMatchAmt();
                if( min<0)min=0;
                pdf.setSship4LsuAmt( min);
                subtot += min;//calc.getLitEvanMatchAmt();
                if( min>0){//calc.getLitEvanMatchAmt()>0){
                    setLineContent(form,  line++, calc.showLitEvanMatchDesc(), calc.showLitEvanMatchAmt(), null);
                }
                
                min=calc.getPacificCampMatchAmt();
                if( min<0)min=0;
                pdf.setSship5LsuAmt( min);
                subtot += min;//calc.getPacificCampMatchAmt();
                if( min>0){//calc.getPacificCampMatchAmt()>0){
                    setLineContent(form,  line++, calc.showPacificCampMatchDesc(), calc.showPacificCampMatchAmt(), null);
                }
                
                min=calc.getNonPacificCampMatchAmt();
                if( min<0)min=0;
                pdf.setSship6LsuAmt( min);
                subtot += min;//calc.getNonPacificCampMatchAmt();
                if( min>0){//calc.getNonPacificCampMatchAmt()>0){
                    setLineContent(form,  line++, calc.showNonPacificCampMatchDesc(), calc.showNonPacificCampMatchAmt(), null);
                }
                
                min=calc.getScholarship7Amt();
                if( min<0)min=0;
                pdf.setSship7LsuAmt( min);
                subtot += min;//calc.getScholarship7Amt();
                if( min>0){//calc.getScholarship7Amt()>0){
                    setLineContent(form,  line++, calc.showScholarship7Desc(), calc.showScholarship7Amt(), null);
                }
                
                min=calc.getScholarship8Amt();
                if( min<0)min=0;
                pdf.setSship8LsuAmt( min);
                subtot += min;//calc.getScholarship8Amt();
                if( min>0){//calc.getScholarship8Amt()>0){
                    setLineContent(form,  line++, calc.showScholarship8Desc(), calc.showScholarship8Amt(), null);
                }
                
                min=calc.getScholarship9Amt();
                if( min<0)min=0;
                pdf.setSship9LsuAmt( min);
                subtot += min;//calc.getScholarship9Amt();
                if( min>0){//calc.getScholarship9Amt()>0){
                    setLineContent(form,  line++, calc.showScholarship9Desc(), calc.showScholarship9Amt(), null);
                }                
                
                if(subtot>0){
                    setLineContent(form,  line++, "La Sierra Awards Subtotal", null, ref.fmtMoney(subtot) );
                    setLineContent(form,  line++, " ", null, null);
                }                
                subtot = 0;
                
                if( std.getStudentXFafsa().equalsIgnoreCase("Yes")){
                    str = std.getIndExcloans();
                    if(str==null || str.isEmpty()||str.trim().equalsIgnoreCase("yes")){ //? false : true;
                        setLineContent(form,  line++, "Option 'exclude loans' is checked", "n/a", null);
                        setLineContent(form,  line++, "Loans Subtotal", null, "n/a" );
                    }else{
                        str = std.getStudentApSubLoans();
                        if(str==null || str.isEmpty()||str.trim().equalsIgnoreCase("no")){//? false : true;
                            //setLineContent(form,  line++, "Subsidized Direct Loan", "excluded", null);
                        }else{
                            str = calc.showSubdirectAmt();
                            amt = ref.convertCurrencyStrToInt(str); //calc.getSubDirect();//will redo the calc w/o fee deduction
                            pdf.setSubloan( amt);                        
                            if(amt>0)setLineContent(form,  line++, "Subsidized Direct Loan (borrow "+calc.showAmt(calc.getOrg_loan_amt_sub()) +")", str, null);
                            subtot += pdf.getSubloan();
                        }

                        str = calc.showPerkinLoanAmt();
                        amt =  ref.convertCurrencyStrToInt(str); //since calc.getPerkinsLoan(); will redo the calc w/o fee deduction
                        pdf.setPerkins( amt );                    
                        if(amt>0)setLineContent(form,  line++, "Perkins Loan (borrow "+calc.showAmt( calc.getOrg_loan_amt_perkins() ) +")", str, null);
                        subtot += pdf.getPerkins();

                        str = std.getStudentAqUnsubLoans();
                        if(str==null || str.isEmpty()||str.trim().equalsIgnoreCase("no")){//? false : true; 
                            //setLineContent(form,  line++, "Subsidized Direct Loan", "excluded", null);
                        }else{
                            str = calc.showUnsubdirectAmt();
                            amt =  ref.convertCurrencyStrToInt(str); //calc.getUnSubDirect();// will redo the calc w/o fee deduction
                            pdf.setUnsubloan(  amt );
                            if(amt>0)setLineContent(form,  line++, "Unsubsidized Direct Loan (borrow "+calc.showAmt(calc.getOrg_loan_amt_unsub()) +")", str, null);
                            subtot += pdf.getUnsubloan();
                        }

                        if(subtot>0)setLineContent(form,  line++, "Loans Subtotal", null, ref.fmtMoney(subtot) );                    
                    }
                    setLineContent(form,  line++, " ", null, null);
                    subtot = 0;
                }
                
                
                str = std.getStudentArFws();
                if(str==null || str.isEmpty()||str.trim().equalsIgnoreCase("no")){//? false : true;
                    ;
                }else if( calc.getFwsAmt()<=0){
                    ;
                }else{
                    pdf.setFws(calc.getFwsAmt());
                    //subtot += calc.getFwsAmt();
                    setLineContent(form,  line++, "Federal Work-Study", null, calc.showFwsAmt());
                    setLineContent(form,  line++, " ", null, null);
                }
                
                str = calc.showMaxAidAmt();
                pdf.setTotAid( ref.convertCurrencyStrToInt(str));
                str = calc.showFaidAmt();
                pdf.setTotAidWoWork( ref.convertCurrencyStrToInt(str));
                setLineContent(form,  line++, "Total Financial Aid", null, calc.showMaxAidAmt());
                
                /**** iText 5.1.3 using BaseColor has no use to change the border color
                 form.setFieldProperty("aidname38", "fflags", 0, null);
                 form.setFieldProperty("aidname38", "bordercolor", BaseColor.RED,  null);
                 form.setFieldProperty("aidtot38", "fflags", 0, null);
                 form.setFieldProperty("aidtot38", "bordercolor", BaseColor.RED, null );
                 form.setFieldProperty("aidval38", "bordercolor", BaseColor.RED, null );
                 
                ****** neither 2.1.7
                form.setFieldProperty("aidname38", "fflags", 0, null);
                 form.setFieldProperty("aidname38", "bordercolor", java.awt.Color.BLUE,  null);
                 form.setFieldProperty("aidtot38", "fflags", 0, null);
                 form.setFieldProperty("aidtot38", "bordercolor", java.awt.Color.BLUE, null );
                 form.setFieldProperty("aidamt38", "bordercolor", java.awt.Color.BLUE, null );*/
               // form.setFieldProperty("aidname5", "fflags", 0, null);
               // form.setFieldProperty("aidname5", "bordercolor", GrayColor.LIGHT_GRAY, null);
               /*
boolean	        setFieldProperty(String field, String name, int value, int[] inst) // Sets a field property.
 boolean	setFieldProperty(String field, String name, Object value, int[] inst) //  Sets a field property.
 boolean	setFieldRichValue(String name, String richValue) // Sets the rich value for the given field.  
 boolean	setField(String name, String value, String display) //Sets the field value and the display string.
 String	getFieldRichValue(String name) // Retrieve the rich value for the given field
 * 
 setFieldProperty

public boolean setFieldProperty(String field,
                                String name,
                                Object value,
                                int[] inst)
Sets a field property. Valid property names are:
textfont - sets the text font. The value for this entry is a BaseFont.
textcolor - sets the text color. The value for this entry is a BaseColor.
textsize - sets the text size. The value for this entry is a Float.
bgcolor - sets the background color. The value for this entry is a BaseColor. If null removes the background.
bordercolor - sets the border color. The value for this entry is a BaseColor. If null removes the border.
Parameters:
field - the field name
name - the property name
value - the property value
inst - an array of int indexing into AcroField.Item.merged elements to process. Set to null to process all
Returns:
true if the property exists, false otherwise setFieldProperty

* 
  908        * Sets a field property. Valid property names are:
  909        * <p>
  910        * <ul>
  911        * <li>textfont - sets the text font. The value for this entry is a <CODE>BaseFont</CODE>.<br>
  912        * <li>textcolor - sets the text color. The value for this entry is a <CODE>java.awt.Color</CODE>.<br>
  913        * <li>textsize - sets the text size. The value for this entry is a <CODE>Float</CODE>.
  914        * <li>bgcolor - sets the background color. The value for this entry is a <CODE>java.awt.Color</CODE>.
  915        *     If <code>null</code> removes the background.<br>
  916        * <li>bordercolor - sets the border color. The value for this entry is a <CODE>java.awt.Color</CODE>.
  917        *     If <code>null</code> removes the border.<br>
  918        * </ul>
  919        * 
  920        * @param field the field name
  921        * @param name the property name
  922        * @param value the property value
  923        * @param inst an array of <CODE>int</CODE> indexing into <CODE>AcroField.Item.merged</CODE> elements to process.
  924        * Set to <CODE>null</CODE> to process all
  925        * @return <CODE>true</CODE> if the property exists, <CODE>false</CODE> otherwise 
  927       public boolean setFieldProperty(String field, String name, Object value, int inst[]) {
 
 * 
public boolean setFieldProperty(String field,
                                String name,
                                int value,
                                int[] inst)
Sets a field property. Valid property names are:
flags - a set of flags specifying various characteristics of the field's widget annotation. The value of this entry replaces that of the F entry in the form's corresponding annotation dictionary.
setflags - a set of flags to be set (turned on) in the F entry of the form's corresponding widget annotation dictionary. Bits equal to 1 cause the corresponding bits in F to be set to 1.
clrflags - a set of flags to be cleared (turned off) in the F entry of the form's corresponding widget annotation dictionary. Bits equal to 1 cause the corresponding bits in F to be set to 0.
fflags - a set of flags specifying various characteristics of the field. The value of this entry replaces that of the Ff entry in the form's corresponding field dictionary.
setfflags - a set of flags to be set (turned on) in the Ff entry of the form's corresponding field dictionary. Bits equal to 1 cause the corresponding bits in Ff to be set to 1.
clrfflags - a set of flags to be cleared (turned off) in the Ff entry of the form's corresponding field dictionary. Bits equal to 1 cause the corresponding bits in Ff to be set to 0.
Parameters:
field - the field name
name - the property name
value - the property value
inst - an array of int indexing into AcroField.Item.merged elements to process. Set to null to process all
Returns:  true if the property exists, false otherwise
* 
* Sets a field property. Valid property names are:
 1080        * <p>
 1081        * <ul>
 1082        * <li>flags - a set of flags specifying various characteristics of the field's widget annotation.
 1083        * The value of this entry replaces that of the F entry in the form's corresponding annotation dictionary.<br>
 1084        * <li>setflags - a set of flags to be set (turned on) in the F entry of the form's corresponding
 1085        * widget annotation dictionary. Bits equal to 1 cause the corresponding bits in F to be set to 1.<br>
 1086        * <li>clrflags - a set of flags to be cleared (turned off) in the F entry of the form's corresponding
 1087        * widget annotation dictionary. Bits equal to 1 cause the corresponding
 1088        * bits in F to be set to 0.<br>
 1089        * <li>fflags - a set of flags specifying various characteristics of the field. The value
 1090        * of this entry replaces that of the Ff entry in the form's corresponding field dictionary.<br>
 1091        * <li>setfflags - a set of flags to be set (turned on) in the Ff entry of the form's corresponding
 1092        * field dictionary. Bits equal to 1 cause the corresponding bits in Ff to be set to 1.<br>
 1093        * <li>clrfflags - a set of flags to be cleared (turned off) in the Ff entry of the form's corresponding
 1094        * field dictionary. Bits equal to 1 cause the corresponding bits in Ff
 1095        * to be set to 0.<br>
 1096        * </ul>
 1097        * 
 1098        * @param field the field name
 1099        * @param name the property name
 1100        * @param value the property value
 1101        * @param inst an array of <CODE>int</CODE> indexing into <CODE>AcroField.Item.merged</CODE> elements to process.
 1102        * Set to <CODE>null</CODE> to process all
 1103        * @return <CODE>true</CODE> if the property exists, <CODE>false</CODE> otherwise
 
 1105       public boolean setFieldProperty(String field, String name, int value, int inst[]) {
                */

                //AcroFields.Item item = form.getFieldItem("SomeField");
                //item.widgets
                // Strlng val= form.getField("aidname3");
                //form.setField("roomboard", "5678");
                
                /******************* NO USE, AND NO CHANGE **********************************
                form.setFieldProperty("roomboard", "bordercolor",java.awt.Color.BLUE, null); //BaseColor
                form.setFieldProperty("roomboard", "bgcolor",java.awt.Color.BLUE, null); //BaseColor
               // PdfFormField.getMKColor((Color)value);
                System.out.println(" got roomboard field value="+form.getField("roomboard"));// get field value as string
                //System.out.println(" got roomboard field rich value="+form.getFieldRichValue("roomboard"));// get field value as string
                */
                //NPE
                /*
                for( String one :form.getAppearanceStates("roomboard")){
                    System.out.println(one);
                } */
                
                //int[] to AcroField.Item.merged
                //AcroFields.Item item = form.getFieldItem("aidname38");
                //item.getMerged(0).toString();
                
                //AcroFields.Item	getFieldItem(String name)  //Gets the field structure.
                str = calc.showTuitionFees();
                //pdf.setTuitionFee( ref.convertCurrencyStrToInt(str) );                        
                form.setField("lsu_tuition_fees", str);         //lsu_tuition_fees
                
                str = calc.initAndShowRoomBoardAmt();
                pdf.setRoomBoard( ref.convertCurrencyStrToInt(str));
                form.setField("roomboard", "+"+str);
                
                str = ref.fmtMoney( pdf.getTuitionFee() + pdf.getRoomBoard());
                form.setField("charges", str);
                
                str = calc.showFaidAmt(); //without fws ?
                pdf.setTotAidWoWork(ref.convertCurrencyStrToInt(str));                
                form.setField("totaid", "-"+str);
                
                subtot = calc.getPacificCampBase() + calc.getNonPacificCampBaseAmt() + calc.getLitEvanBaseAmt();
                pdf.setEarnings(subtot);
                pdf.setSship4ExtAmt( calc.getLitEvanBaseAmt());
                pdf.setSship5ExtAmt( calc.getPacificCampBase());
                pdf.setSship6ExtAmt( calc.getNonPacificCampBaseAmt());
                str = ref.fmtMoney(-1*subtot);
                form.setField("earnings", str);
                
                str = calc.showDueAmt();
                pdf.setDue( ref.convertCurrencyStrToInt(str));
                form.setField("due", str);
                
                str = calc.initAndShowYIA();
                pdf.setYearOpt(ref.convertCurrencyStrToInt(str) );
                form.setField("yearly", str);
                
                str = calc.showQIA();
                if( !std.getStudentUAcademic().equalsIgnoreCase("CJ") ){
                    pdf.setQuarterOpt(ref.convertCurrencyStrToInt(str));
                    form.setField("quarterly", str);
                }else{
                    form.setField("quarterly", "n/a");
                }
                
                str = calc.showMonthlyOption();
                pdf.setMonthOpt( ref.convertCurrencyStrToInt(str));
                form.setField("monthly", str);

                //2014-03-13  payment plan strings, calc once, and save, and use for whole year
                //but DCL will not work without volatile
                if( ref.getStrYIA().isEmpty()){
                    synchronized (this){
                        if(ref.getStrYIA().isEmpty()){
                            StringBuilder sbs = new StringBuilder(100);
                            ref.setStrYIA( sbs.append("If you pay by ").append( yearInAdvanceDateStr).append(ref.getFiscal_year()).append(" and receive the ").append(yearInAdvanceDiscount.movePointRight(2).intValueExact()).append("% discount").toString() );
                            sbs.setLength(0);
                            sbs.append("If you pay by ").append( quarterInAdvanceStartMonth).append("/").append(quarterInAdvanceStartDay).append("/").append(ref.getFiscal_year());
                            int m = quarterInAdvanceStartMonth+ quarterInAdvanceIntervalMonthes;
                            sbs.append( ", ").append(m>12 ? m-12 : m ).append("/").append(quarterInAdvanceStartDay).append("/").append(m>12 ? ref.getFiscal_year()+1 : ref.getFiscal_year());
                            m = m + quarterInAdvanceIntervalMonthes;
                            sbs.append( ", and ").append(m>12 ? m-12 : m ).append("/").append(quarterInAdvanceStartDay).append("/").append(m>12 ? ref.getFiscal_year()+1 : ref.getFiscal_year());
                            sbs.append(", and receive the ").append( quarterInAdvanceDiscount.movePointRight(2).intValueExact() ).append("% discount");
                            ref.setStrQIA( sbs.toString() );
                            sbs.setLength(0);
                            sbs.append("If you pay on the first day of each month starting ").append(monthlyOptionDateStr).append(ref.getFiscal_year()).append(" and include the $").append(monthlyOptionFees).append(" fee");
                            ref.setStrMPN( sbs.toString());
                        }
                    }
                }
                
                form.setField("sfs_yia", ref.getStrYIA());
                form.setField("sfs_qia", ref.getStrQIA());
                form.setField("sfs_mpln", ref.getStrMPN());
                
                if( pu==false){
                    stamp.setFormFlattening(true);
                }
                stamp.close();            
                reader.close(); 
                
                msg = pdfname;//"";
            }catch(Exception e){
                e.printStackTrace();
                msg = "Failed. Reason: "+e.getMessage();
            } 
        }
        return msg;
    }
    
    private void setMetaData(PdfReader reader, PdfStamper stamper, Student std, SimpleDateFormat  sdf, String ver){
/*
 New document: 

Document document = new Document(); 
PdfWriter.getInstance(document, new 
FileOutputStream("HelloWorldMetadata.pdf")); 
document.addTitle("Hello World example"); 
document.addSubject("This example shows how to add metadata"); 
document.addKeywords("Metadata, iText, step 3"); 
document.addCreator("My program using iText"); 
document.addAuthor("Bruno Lowagie"); 
document.open(); 
document.add(new Paragraph("Hello World")); 
document.close(); 

Existing document: 

PdfReader reader = new PdfReader("HelloWorldNoMetadata.pdf"); 
PdfStamper stamper = new PdfStamper(reader, new 
FileOutputStream("HelloWorldStampedMetadata.pdf")); 
HashMap info = reader.getInfo(); 
info.put("Subject", "Hello World"); 
info.put("Author", "Bruno Lowagie"); 
info.put("Keywords", "iText in Action, PdfStamper"); 
info.put("Title", "Hello World stamped"); 
info.put("Creator", "Silly standalone example"); 
stamper.setMoreInfo(info); 
stamper.close(); 
 */     
        HashMap<String, String> info = reader.getInfo(); 
        info.put("Subject", "Estimate"); 
        info.put("Author", "La Sierra University Estimator Program Version "+ver); 
        info.put("Keywords", std.getStudentBLastname()+", "+std.getStudentCFirstname()); 
        info.put("Title", "La Sierra University Estimate for "+std.getStudentCFirstname()+" "+ std.getStudentBLastname()); 
        info.put("Creator", std.getStudentBuOrigCounselor()+" at "+sdf.format(new Date()));  //getStudentBxModCounselor
        stamper.setMoreInfo(info); 
        
    }
    
    private void setLineContent(AcroFields form, int linenumb, String name, String val, String subtotal){
        String fieldname = "aidname"+linenumb;
        String fieldval  = "aidamt"+linenumb;
        String fieldtot  = "aidtot"+linenumb;
        int[] t = {0, 1, 2, 3};
        try{
            /*
             form.setFieldProperty("roomboard", "textcolor",java.awt.Color.BLUE, null);
             form.setFieldProperty("roomboard", "bordercolor",java.awt.Color.BLUE, null); 
             */            
            if( subtotal!=null && !subtotal.isEmpty()) {
                form.setFieldProperty( fieldtot, "fflags", 0, null);
                form.setFieldProperty( fieldtot,  "bordercolor", java.awt.Color.BLUE , null); 
                form.setField(fieldtot, subtotal);
                
                form.setFieldProperty( fieldname, "fflags", 0, null);                
                form.setFieldProperty( fieldname, "bordercolor",java.awt.Color.BLUE, null); 
                form.setFieldProperty( fieldval, "fflags", 0, null);
                form.setFieldProperty( fieldval,  "bordercolor",java.awt.Color.BLUE, null);                
            }
            if( name!=null  )  form.setField(fieldname, name);
            if( val!=null && !val.isEmpty()) form.setField(fieldval, val);
            //form.setFieldProperty("text_2", "fflags", 0, null);
            //form.setFieldProperty("text_2", "bordercolor", BaseColor.RED, null);
            //form.setField("text_2", "bruno");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
/*** remove Cell Table border   
pdfptable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
and/or 
pdfpcell.setBorder(PdfPCell.NO_BORDER);  
 */    
    
    //int downloadPDF(String path){
    String downloadPDF(  String filename){
       // String pdfname = getPdfRoot()+ path;
        String path = getPdfRoot()+ filename;//pdfname;
        
        String pdfmsg="";
        File file = new File(path); 
        long filesize = file.length();
        byte[] data = new byte[(int)filesize];
        ByteBuffer buffer = ByteBuffer.allocate(32000);
        int p=0,l=0;
        
        FacesContext fc = FacesContext.getCurrentInstance();
        FileInputStream fis = null;
        FileChannel fic = null;
        try{
            fis = new FileInputStream(path);
            fic = fis.getChannel();
            while( (l=fic.read(buffer)) >0){
                buffer.flip();
                //fuc.write(buffer)
                buffer.get(data, p, l); 
                p+=l;
                buffer.clear();
            }
        }catch(Exception e){
            pdfmsg="Failed to read PDF. Reason: "+e.getMessage();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  pdfmsg, pdfmsg) );
            return pdfmsg;
        }finally{
            try{
                if( fic!=null)fic.close();
                if( fis!=null)fis.close();
            }catch(Exception e){                
            }
        }
        
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        HttpServletRequest request  = (HttpServletRequest)fc.getExternalContext().getRequest();
        
        response.setContentType("application/pdf");
        response.setContentLength((int)filesize);
        /*
        response.setHeader("Pragma", "public");
        response.setHeader("cache-control", "no-cache");
        response.setDateHeader("max-age", 0);
        response.setHeader("Pragma", "no-cache"); */
        response.setHeader("Content-disposition", "attachment;filename="+ filename);//pdfname); //path
        response.setHeader("Refresh", "3; url = estimate-new.jsf");  
  //no diff      response.setHeader("Refresh", "1; url='/estimator/view/estimate-new.jsf'"  ); //request.getRequestURL()
        try{
            ServletOutputStream output = response.getOutputStream();
            output.write(data);
            output.flush();
            
        }catch(IOException e){
            pdfmsg="Failed to feedback PDF content. Reason: "+e.getMessage();
            /////ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");   
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  pdfmsg, pdfmsg) );
            return pdfmsg;
        }finally{}
        
         fc.responseComplete(); //even if works, the message still needs to show. but the 
        return pdfmsg;
    }
}
