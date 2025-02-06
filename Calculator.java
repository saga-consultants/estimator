/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static edu.lsu.estimator.PackValues.quarterInAdvanceDiscount;
import static edu.lsu.estimator.PackValues.yearInAdvanceDiscount;
import static edu.lsu.estimator.PackValuesStatic.*;
/**
 *
 * @author kwang
 */
@Named
@SessionScoped
public class Calculator implements  Serializable{
    private static final long serialVersionUID = 1L;
        
    @Inject
    edu.lsu.estimator.AppReference ref;
 //   @Inject FormatLogger  log; 
    /////private transient  FormatLogger log = new FormatLogger (Logger.getLogger(this.getClass().getName()));
    private  static final Logger log = LoggerFactory.getLogger();
    
    private Student std;
    private PackFunctions actor;
    
    private NumberFormat fmt = new DecimalFormat("$#,###"); //private NumberFormat fmt = new DecimalFormat("$#,###.##");
    
    
    public Calculator(){}
    
    //@PostConstruct//
    public void setStud(Student std){
        this.std = std;
        this.actor = new PackFunctions(std);
    }
   
    public void refreshCalc(Student newstd){
        //this.actor = new PackFunctions(std);
        this.std = newstd; //---w/o this, the award scholarship names and notes can not be updated
        actor.setStd(newstd);        
    }
    
    
    public void init(){ // the actor (functions) will dup resetvalues() when cacl.refreshCalc(stud)
  //      log.info("====================== init calculator, reset values =========================="); 
        PackValues.resetValues(); //should exclude needAmt and efc, addlExp ????  moved from initAndShowPellGrantAmt()
    }
    
    /*
     * ========================== Maximum Aid Calculation =============================
     */
    public String showTuitionFees(){         
        int val = actor.getTuitionAndFees();
        int tuitionAndFees = val;
        return fmt.format(val);
    }
    
    public String showAddlExpense(){
        int val = actor.getOtherExpenses() - tuitionAndFees ;
        int addlExp = val;
        return fmt.format(val );
    }
     
    public String showEFC(){        
        int val = actor.getEFC();
        efc = val;
        if( val==0){
            needAmt = tuitionAndFees + addlExp ;
        }else{
            needAmt = tuitionAndFees + addlExp - val; //efc
        }
        if( actor.getInternationalStatus().equalsIgnoreCase("International") ){
            return "n/a";
        }else{
            return fmt.format(val);
        }
    }
    
    public String showNeedAmt(){
        return fmt.format(needAmt);
    }
    
    /*
     * ========================== Finance Aid (external grant) =============================
     */
    public String initAndShowPellGrantAmt(){
 //       log.info("====initAndShowPellGrantAmt() is invoked=======");
        
        pellGrant = actor.getPell();

        Student stud = null;
        //adjust_calgrant_amt_ind = stud;

        calGrantA = actor.getCalGrantA();
        calGrantB = actor.getCalGrantB();
        fseogAmt  = actor.getFseog();
        extAllowance = actor.getExternalAllowance();
        
        err     = actor.getNonCaGrant(); //err only show up here 1/2 time
        nonCaGrantDesc = _nonCaGrantDesc; //only shows in this file
        nonCaGrantAmt  = _nonCaGrantAmt;
        
        err = actor.getOutsideScholarship(); //err only show up here 2/2 time
        outsideDesc = _outsideScholarship;  //only shows in this file
        outsideAmt  = _outsideScholarshipAmt;
        
        churchBase = actor.getChurchBase();
        sdaAward   = actor.getSdaAward();
 //       log.info("====== after init, sdaAward=%d", sdaAward);
        
        lsuAllowance = actor.getLsuAllowance();
        familyDiscount = actor.getFamilyDiscount();
        
        lsuNeedGrant = actor.getLsuNeedGrantAmt();
        //lsuPerformance = actor.getLsuPerformance();
        
        nationalMerit = actor.getNationalMerit();
        /*
        if(lsuPerformance > nationalMerit ){
            nationalMerit = 0;
        }else{
            lsuPerformance = 0;
        }*/
        if(nationalMerit > 0){
            lsuAchievement = 0;
        }else{
            lsuAchievement = actor.getLsuAchievement();
        }
        
        lsu4yRenewable = actor.getLsu4yRenewable();
        log.info(".....initAndShowPellGrantAmt90 got nationalMerit=%d, lsuAchievement=%d, lsu4yRenewable=%d", nationalMerit, lsuAchievement, lsu4yRenewable);
        
        if( lsu4yRenewable > (lsuAchievement+nationalMerit)){
            lsuAchievement=0;
            nationalMerit=0;
        }else{
            lsu4yRenewable=0;
        }
        
        
        
        
        churchMatch = actor.getChurchMatch();
        pacificCampMatch = actor.getPacificCampMatch();
        nonPacificCampMatch = actor.getNonPacificCampMatch();
        litEvanMatch = actor.getLitEvanMatch();
        
        lsuLimitSubtotal = actor.enforceLsuLimits();  //only defined in this file, not used by others
        subDirect = actor.getSubDirect();
        perkinsLoan = actor.getPerkinsLoan();
        unsubDirect = actor.getUnSubDirect();
        
        //<CFSET excludeNote = "Please exclude loans"> defined in estimatorStudentData.cfm
        //<CFIF SAVE_STUDENT_AN_PUB/PRI_NOTES contains "#excludeNote#">checked</CFIF> > I wish to exclude loans from this estimate
        //if( std.getStudentAnPubNotes()!=null && std.getStudentAnPubNotes().indexOf(excludeNote)>=0 ){
        if( std.getIndExcloans().equalsIgnoreCase("Yes")  ){ //.getInd_noloans()
            subDirect = 0;
            perkinsLoan = 0;
            unsubDirect = 0;
        }else{
            if (std.getStudentApSubLoans().equalsIgnoreCase("no")) {
                subDirect = 0;
            }
            if( std.getStudentAqUnsubLoans().equalsIgnoreCase("no")){
                unsubDirect = 0;
            }            
        } 
        //if( std.getStudentArFws().equalsIgnoreCase("no")){
        //        fwsAmount = 0;
        //}
        fwsAmount = actor.getFWS();
        lsuOverallSubtotal = actor.enforceOverallLimits(); //only defined here, and not used .
        maxAid = pellGrant + calGrantA + calGrantB + fseogAmt + extAllowance + nonCaGrantAmt + outsideAmt + churchBase + lsuAllowance + sdaAward + +lsuNeedGrant + lsuAchievement +lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch;
        maxAid += litEvanMatch + subDirect + perkinsLoan + fwsAmount + unsubDirect;
        
        maxAid += scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
//        log.info("..... initAndShowPellGrantAmt() got maxAid=%d, fwsAmount=%d ......", maxAid, fwsAmount);
        
        amtDue = amtDue - maxAid;
        
        return fmt.format(pellGrant);
    }
    
    public String showCalGrantA(){
        return fmt.format(calGrantA);
    }
    public String showCalGrantB(){
        return fmt.format(calGrantB);
    }
    public String showFseogAmt(){
        return fmt.format(fseogAmt);
    }
    public String showExtAllowanceAmt(){
        return fmt.format(extAllowance);
    }
    
    
    public int getNonCalGrantAmt(){
        return nonCaGrantAmt;
    }
    public String getNonCalGrantDesc(){    
        if( nonCaGrantDesc!=null && nonCaGrantDesc.length()> 40 ){
            return nonCaGrantDesc.substring(0, 37) + "...";
        }else{
            return nonCaGrantDesc==null ? "n/a":nonCaGrantDesc;
        }
    }
    public String showNonCalGrantAmt(){
        return fmt.format(nonCaGrantAmt);
    }
    
    
    
    public int getOutsideAmt(){
        return outsideAmt;
    }
    public String getOutsideDesc(){
        if( outsideDesc!=null && outsideDesc.length()> 40 ){
            return outsideDesc.substring(0, 37) + "...";
        }else{
            return outsideDesc==null ? "n/a":outsideDesc;
        }
    }
    public String showOutsideAmt(){
        return fmt.format(outsideAmt);
        
    }
    
    
    public int getChurchBaseAmt(){
        return churchBase;
    }
    public String showChurchBaseDesc(){
        return std.getStudentAyScholarship3Name();//.getStudentAtScholarship1Note();
    }
    public String showChurchBaseAmt(){
        return fmt.format(churchBase);
    }
    
    /*
     * ========================== Finance Aid (LSU awards) =============================
     */
    public String showLsuAllowanceAmt(){
        return fmt.format(lsuAllowance);
    }    
    public String showSdaAwardAmt(){
//        log.info("========showSdaAwardAmt() is invoked.===");
        return fmt.format(sdaAward);
    }
    
    
    
    public String showLsuPerformanceAmt(){
        return fmt.format(lsuPerformance);
    }
    
    
    public int getLsuAchievementAmt(){
        return lsuAchievement;
    }
    public String showLsuAchievementAmt(){
        return fmt.format(lsuAchievement);
    }
    
    public int getLsu4yRenewableAmt(){
        return lsu4yRenewable;
    }
    public String showLsu4yRenewableAmt(){
        return fmt.format(lsu4yRenewable);
    }
    
    public int getLsuNeedGrantAmt(){
        return lsuNeedGrant;
    }
    public String showLsuNeedGrantAmt(){
        return fmt.format(lsuNeedGrant);
    }
    
    
    
    public String showFamilyDiscountAmt(){
        return fmt.format(familyDiscount);
    }
    public String showNationalMeritAmt(){
        return fmt.format(nationalMerit);
    }
    
    public int getChurchMatchAmt(){
        return churchMatch;
    }
    public String showChurchMatchDesc(){
        return cutBarStr2( std.getStudentAyScholarship3Name());//getStudentAtScholarship1Note() +" (Match)"; 
    }
    public String showChurchMatchAmt(){
        return fmt.format(churchMatch);
    }
    
    
    public int getPacificCampMatchAmt(){
        return pacificCampMatch;
    }
    public String showPacificCampMatchDesc(){
        return cutBarStr2( std.getStudentBeScholarship5Name());//( std.getStudentAwScholarship2Note()) + " (Match)";
    }
    public String showPacificCampMatchAmt(){
        return fmt.format(pacificCampMatch);
    }
    
    public int getNonPacificCampMatchAmt(){
        return nonPacificCampMatch;
    }
    public String showNonPacificCampMatchDesc(){
        return cutBarStr2(std.getStudentBhScholarship6Name());//( std.getStudentAzScholarship3Note()) + " (Match)";
    }
    public String showNonPacificCampMatchAmt(){
        return fmt.format(nonPacificCampMatch);
    }
    
    public int getLitEvanMatchAmt(){
        return litEvanMatch;
    }
    public String showLitEvanMatchDesc(){
        return cutBarStr2( std.getStudentBbScholarship4Name());//( std.getStudentBcScholarship4Note()) + " (Match)";
    }
    public String showLitEvanMatchAmt(){
        return fmt.format(litEvanMatch);
    }
    
    public String showLsuLimitSubtotal(){ //---not used
        return fmt.format(lsuLimitSubtotal);
    }
    
    /*
     * ========================== Finance Aid (Loans) =============================
     */
    public String showSubdirectAmt(){
        return fmt.format(subDirect);
    }
    
    public String showPerkinLoanAmt(){
        return fmt.format(perkinsLoan);
    }
    
    public String showUnsubdirectAmt(){
        return fmt.format(unsubDirect);
    }
    
    /*
     * ========================== Finance Aid (Federal Work-study) =============================
     */
    public int getFwsAmt(){
        return fwsAmount;
    }
    public String showFwsAmt(){
        return fmt.format(fwsAmount);
    }
    
     /*
     * ========================== Finance Aid (sum) =============================
     */
    public String showMaxAidAmt(){
        return fmt.format(maxAid);
    }
    
    /*
    public String showlsuOverallSubtotalAmt(){ ///------------not used
        log.info("....... showlsuOverallSubtotalAmt() got lsuOverallSubtotal=[%s]", lsuOverallSubtotal);
        return fmt.format(lsuOverallSubtotal);//Cannot format given Object as a Number
    }
    */
    
    /*
     * ========================== Amount Due Calculation =============================
     */
    public String initAndShowRoomBoardAmt(){
        roomAndBoard = actor.getRoomAndBoard();
        pacificCampBase = actor.getPacificCampBase();
        nonPacificCampBase = actor.getNonPacificCampBase();
        litEvanBase = actor.getLitEvanBase();        
        amtDue = tuitionAndFees + roomAndBoard -maxAid + fwsAmount -pacificCampBase -nonPacificCampBase -litEvanBase;     // -maxAid ==0 at first after init      
 //       log.info("****** initAndShowRoomBoardAmt() calc amtDue=%d while maxAid=%d", amtDue, maxAid);
        return fmt.format(roomAndBoard);
    }
            
    public String showFaidExtDesc(){
        return fwsAmount>0 || std.getStudentArFws().equalsIgnoreCase("yes") ? " (FWS excluded)" :" (FWS included)" ;       
    }
    
    public String showFaidAmt(){
  //      log.info("****** showFaidAmt() get maxAid=%d, fwsAmount=%d", maxAid, fwsAmount);
        return fmt.format(maxAid - fwsAmount);
    }
    
    
    
    public int getPacificCampBase(){
        return pacificCampBase;
    }    
    public String showPacificCampBaseDesc(){
        return "- "+cutBarStr2( std.getStudentBeScholarship5Name())+" (Earnings)"; //getStudentAwScholarship2Note()
    }
    public String showPacificCampBaseAmt(){
        return fmt.format(pacificCampBase);
    }
    
     
    public int getNonPacificCampBaseAmt(){
        return nonPacificCampBase;
    }
    public String showNonPacificCampBaseDesc(){
        return "- "+cutBarStr2( std.getStudentBhScholarship6Name())+" (Earnings)"; //getStudentAzScholarship3Note()
    }
    public String showNonPacificCampBaseAmt(){
        return fmt.format(nonPacificCampBase);
    }
    
    
    public int getLitEvanBaseAmt(){
        return litEvanBase;
    }
    public String showLitEvanBaseDesc(){
        return "- "+cutBarStr2( std.getStudentBbScholarship4Name())+" (Earnings)";  //getStudentBcScholarship4Note()
    }
    public String showLitEvanBaseAmt(){
        return fmt.format(litEvanBase);
    }
    
    
    public String showDueAmt(){
        return fmt.format(amtDue);
    }
    
    /*
     * ========================== Payment Option =============================
     */
    public String initAndShowYIA(){
        if( amtDue>0){
            yearInAdvanceOption = amtDue - (new BigDecimal( amtDue).multiply(yearInAdvanceDiscount).intValue());
            quarterInAdvanceOption = (amtDue - (new BigDecimal( amtDue).multiply( quarterInAdvanceDiscount).intValue()))/3;
            monthlyOption = (amtDue + 90 )/9;
        }else{
            yearInAdvanceOption =0;
            quarterInAdvanceOption = 0;
            monthlyOption = 0;
        }
        return fmt.format(yearInAdvanceOption);
    }
    
    
    public String showQIA(){
        return fmt.format(quarterInAdvanceOption);
    }
    
    
    public String showMonthlyOption(){
        return fmt.format(monthlyOption);
    }
    
    
    //delegate of actor's func, since actor is private
    public void setSAVE_STUDENT_L_INTL_STUD(String newStrVal){
        actor.setSAVE_STUDENT_L_INTL_STUD( newStrVal );
    }
    /*
    public void setSAVE_STUDENT_X_FAFSA(String newStrVal){
        actor.SAVE_STUDENT_X_FAFSA( newStrVal );
    }
    
    public void setSAVE_STUDENT_Z_CALGRANT(String newStrVal){
        actor.SAVE_STUDENT_Z_CALGRANT( newStrVal );        
    }*/

    public void setSAVE_STUDENT_M_MARRY(String SAVE_STUDENT_M_MARRY) {
        actor.setSAVE_STUDENT_M_MARRY( SAVE_STUDENT_M_MARRY );
    }

    public void setSAVE_STUDENT_N_SDA(String SAVE_STUDENT_N_SDA) {
        actor.setSAVE_STUDENT_N_SDA( SAVE_STUDENT_N_SDA );
    }

    public void setSAVE_STUDENT_P_GPA(String SAVE_STUDENT_P_GPA) {
        actor.setSAVE_STUDENT_P_GPA( SAVE_STUDENT_P_GPA );
    }

    public void setSAVE_STUDENT_Q_SAT(String SAVE_STUDENT_Q_SAT) {
        actor.setSAVE_STUDENT_Q_SAT( SAVE_STUDENT_Q_SAT );
    }

    public void setSAVE_STUDENT_Q_SAT_V(String SAVE_STUDENT_Q_SAT_V) {
        actor.setSAVE_STUDENT_Q_SAT_V( SAVE_STUDENT_Q_SAT_V );
    }

    public void setSAVE_STUDENT_R_ACT(String SAVE_STUDENT_R_ACT) {
        actor.setSAVE_STUDENT_R_ACT( SAVE_STUDENT_R_ACT );
    }

    public void setSAVE_STUDENT_S_MERIT(String SAVE_STUDENT_S_MERIT) {
        actor.setSAVE_STUDENT_S_MERIT( SAVE_STUDENT_S_MERIT );
    }

    public void setSAVE_STUDENT_U_ACADEMIC(String SAVE_STUDENT_U_ACADEMIC) {
        actor.setSAVE_STUDENT_U_ACADEMIC( SAVE_STUDENT_U_ACADEMIC );
    }

    public void setSAVE_STUDENT_V_FAMILY(String SAVE_STUDENT_V_FAMILY) {
        actor.setSAVE_STUDENT_V_FAMILY( SAVE_STUDENT_V_FAMILY );
    }

    public void setSAVE_STUDENT_W_DORM(String SAVE_STUDENT_W_DORM) {
        actor.setSAVE_STUDENT_W_DORM( SAVE_STUDENT_W_DORM );
    }

    public void setSAVE_STUDENT_X_FAFSA(String SAVE_STUDENT_X_FAFSA) {
        actor.setSAVE_STUDENT_X_FAFSA( SAVE_STUDENT_X_FAFSA );
    }

    public void setSAVE_STUDENT_Y_INDEPT(String SAVE_STUDENT_Y_INDEPT) {
        actor.setSAVE_STUDENT_Y_INDEPT( SAVE_STUDENT_Y_INDEPT );
    }

    public void setSAVE_STUDENT_Z_CALGRANT(String SAVE_STUDENT_Z_CALGRANT) {
        actor.setSAVE_STUDENT_Z_CALGRANT( SAVE_STUDENT_Z_CALGRANT );
    }

    public void setSAVE_STUDENT_AA_CALGRANT_A(String SAVE_STUDENT_AA_CALGRANT_A) {
        actor.setSAVE_STUDENT_AA_CALGRANT_A( SAVE_STUDENT_AA_CALGRANT_A );
    }

    public void setSAVE_STUDENT_AB_CALGRANT_B(String SAVE_STUDENT_AB_CALGRANT_B) {
        actor.setSAVE_STUDENT_AB_CALGRANT_B( SAVE_STUDENT_AB_CALGRANT_B );
    }

    public void setSAVE_STUDENT_AC_FAMILY_SIZE(String SAVE_STUDENT_AC_FAMILY_SIZE) {
        actor.setSAVE_STUDENT_AC_FAMILY_SIZE( SAVE_STUDENT_AC_FAMILY_SIZE );
    }

    public void setSAVE_STUDENT_AD_FAMILY_INCOME(String SAVE_STUDENT_AD_FAMILY_INCOME) {
        actor.setSAVE_STUDENT_AD_FAMILY_INCOME( SAVE_STUDENT_AD_FAMILY_INCOME );
    }

    public void setSAVE_STUDENT_AE_FAMILY_ASSET(String SAVE_STUDENT_AE_FAMILY_ASSET) {
        actor.setSAVE_STUDENT_AE_FAMILY_ASSET( SAVE_STUDENT_AE_FAMILY_ASSET );
    }

    public void setSAVE_STUDENT_AF_FAMILY_CONTRIB(String SAVE_STUDENT_AF_FAMILY_CONTRIB) {
        actor.setSAVE_STUDENT_AF_FAMILY_CONTRIB( SAVE_STUDENT_AF_FAMILY_CONTRIB );
    }

    public void setSAVE_STUDENT_AG_NONLSU_ALLOWRANCE(String SAVE_STUDENT_AG_NONLSU_ALLOWRANCE) {
        actor.setSAVE_STUDENT_AG_NONLSU_ALLOWRANCE( SAVE_STUDENT_AG_NONLSU_ALLOWRANCE );
    }

    public void setSAVE_STUDENT_AH_LSU_ALLOWRANCE(String SAVE_STUDENT_AH_LSU_ALLOWRANCE) {
        actor.setSAVE_STUDENT_AH_LSU_ALLOWRANCE( SAVE_STUDENT_AH_LSU_ALLOWRANCE );
    }

    public void setSAVE_STUDENT_AI_EDU_ALLOW_PER(String SAVE_STUDENT_AI_EDU_ALLOW_PER) {
        actor.setSAVE_STUDENT_AI_EDU_ALLOW_PER( SAVE_STUDENT_AI_EDU_ALLOW_PER );
    }

    public void setSAVE_STUDENT_AJ_HOME_STATE(String SAVE_STUDENT_AJ_HOME_STATE) {
        actor.setSAVE_STUDENT_AJ_HOME_STATE( SAVE_STUDENT_AJ_HOME_STATE );
    }

    public void setSAVE_STUDENT_AK_NONCAL_GRANT(String SAVE_STUDENT_AK_NONCAL_GRANT) {
        actor.setSAVE_STUDENT_AK_NONCAL_GRANT( SAVE_STUDENT_AK_NONCAL_GRANT );
    }

    public void setSAVE_STUDENT_AL_OUT_SCHOLARSHIPS(String SAVE_STUDENT_AL_OUT_SCHOLARSHIPS) {
        actor.setSAVE_STUDENT_AL_OUT_SCHOLARSHIPS( SAVE_STUDENT_AL_OUT_SCHOLARSHIPS );
    }

    public void setSAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT(String SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT) {
        actor.setSAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT( SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT );
    }

    public void setSAVE_STUDENT_AQ_UNSUB_LOANS(String SAVE_STUDENT_AQ_UNSUB_LOANS) {
        actor.setSAVE_STUDENT_AQ_UNSUB_LOANS( SAVE_STUDENT_AQ_UNSUB_LOANS );
    }

    public void setSAVE_STUDENT_AR_FWS(String SAVE_STUDENT_AR_FWS) {
        actor.setSAVE_STUDENT_AR_FWS( SAVE_STUDENT_AR_FWS );
    }

    public void setSAVE_STUDENT_AU_SCHOLARSHIP1_AMT(String SAVE_STUDENT_AU_SCHOLARSHIP1_AMT) {
        actor.setSAVE_STUDENT_AU_SCHOLARSHIP1_AMT( SAVE_STUDENT_AU_SCHOLARSHIP1_AMT );
    }

    public void setSAVE_STUDENT_AX_SCHOLARSHIP2_AMT(String SAVE_STUDENT_AX_SCHOLARSHIP2_AMT) {
        actor.setSAVE_STUDENT_AX_SCHOLARSHIP2_AMT( SAVE_STUDENT_AX_SCHOLARSHIP2_AMT );
    }

    public void setSAVE_STUDENT_BA_SCHOLARSHIP3_AMT(String SAVE_STUDENT_BA_SCHOLARSHIP3_AMT) {
        actor.setSAVE_STUDENT_BA_SCHOLARSHIP3_AMT( SAVE_STUDENT_BA_SCHOLARSHIP3_AMT );
    }

    public void setSAVE_STUDENT_BD_SCHOLARSHIP4_AMT(String SAVE_STUDENT_BD_SCHOLARSHIP4_AMT) {
        actor.setSAVE_STUDENT_BD_SCHOLARSHIP4_AMT( SAVE_STUDENT_BD_SCHOLARSHIP4_AMT );
    }
    
    
    //for estimator to read static constant values 
    public int getEaLsuPercentage(){
        return ea_lsu_per;
    }
    public int getEaNonLsuPercentage(){        
        return ea_nonlsu_per;
    }
    public int getEaNonLsuPercentageByDorm(boolean in_dorm){    // by Ken 2011-10-31    
        return in_dorm ? ea_nonlsu_dorm_per : ea_nonlsu_per;
    }
    
    ///for estimator to set adjust_cal_grant_amt indicator
    public void setAdjustCalGrantAmtInd(boolean ind){
        adjust_calgrant_amt_ind = ind;
    }
    
    
    /////////////////// added to show scholarship #5 ~ #9
    public int getScholarship1Amt(){
        return scholarship_amt_1;
    }
    public String showScholarship1Amt(){
        return fmt.format(scholarship_amt_1);
    }
    public String showScholarship1Desc(){
        return cutBarStr( std.getStudentAsScholarship1Name());//.getStudentBeScholarship5Name());
    }
    public String showScholarship5Desc(){
        return cutBarStr( std.getStudentBeScholarship5Name());
    }
    
    
    public int getScholarship2Amt(){
        return scholarship_amt_2;
    }
    public String showScholarship2Amt(){
        return fmt.format(scholarship_amt_2);
    }
    public String showScholarship2Desc(){
        return cutBarStr(std.getStudentAvScholarship2Name());// std.getStudentBhScholarship6Name());
    }
    public String showScholarship6Desc(){
        return cutBarStr( std.getStudentBhScholarship6Name());
    }
    
    
    
    public int getScholarship7Amt(){
        return scholarship_amt_7;
    }
    public String showScholarship7Amt(){
        return fmt.format(scholarship_amt_7);
    }
    public String showScholarship7Desc(){
        return cutBarStr( std.getStudentBkScholarship7Name());
    }
    
    public int getScholarship8Amt(){
        return scholarship_amt_8;
    }
    public String showScholarship8Amt(){
        return fmt.format(scholarship_amt_8);
    }
    public String showScholarship8Desc(){
        return cutBarStr( std.getStudentBnScholarship8Name());
    }
    
    public int getScholarship9Amt(){
        return scholarship_amt_9;
    } 
    public String showScholarship9Amt(){
        return fmt.format(scholarship_amt_9);
    }
    public String showScholarship9Desc(){
        return cutBarStr( std.getStudentBqScholarship9Name());
    }
    
    public String cutStr(String str, int max, String makeup){
        if( str==null )return "";
        int i = str.length();
        if( i<=max)return str;        
        return str.subSequence(0, max)+ makeup;
    }
    
    public String cutBarStr(String str){
        return cutStr(str, 40, "...");
    }
    public String cutBarStr2(String str){
        return cutStr(str, 30, "...");
    }
}


/*
 * #0.00. 
 * The # symbol  means any number but leading zero will not be displayed. 
 * The 0 symbol will display the remaining digit and will display as zero if no digit is available.
NumberFormat formatter = new DecimalFormat("#0.00");

 */