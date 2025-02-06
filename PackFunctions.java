/*
 * To change this template, choose Tools | Templates
 * && open the template in the editor.
 */
package edu.lsu.estimator;

////import org.apache.log4j.Logger;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;

import static edu.lsu.estimator.PackValues.*;

//import static java.lang.Math
/**
 *
 * @author kwang
 */
//@SessionScoped
//Old one not to use this 
@Dependent
public class PackFunctions implements Serializable {

    private static final long serialVersionUID = 1L;
    private NumberFormat fmt = new DecimalFormat("$#,###");
    private Student std;
    ////// private transient  FormatLogger log = new FormatLogger (Logger.getLogger(this.getClass().getName()));
        private static final Logger log = LoggerFactory.getLogger();
    @Inject
    AppReference ref;

    //2013-07-30 Geoff asked to combine the netecho servelet (open to public) with lestimator, so no more login info available here
    //       @Inject Login login;
    private int SAVE_STUDENT_FISY = 2012;//"#dateFormat(Now(),'yyyy')+1#"; //WILL BE SET BY STUD FISY
    private int SAVE_STUDENT_NUMBER = 9999;//"#TRIM(Session.CFID)#";
    private String SAVE_STUDENT_A_LSUID = "";
    private String SAVE_STUDENT_B_LASTNAME = "";
    private String SAVE_STUDENT_C_FIRSTNAME = "";
//	private Date   SAVE_STUDENT_D_DOB 			= new Date();
    private String SAVE_STUDENT_D_DOB = "";

    private String SAVE_STUDENT_E_EMAIL = "";
    private String SAVE_STUDENT_F_PHONE = "";
    private String SAVE_STUDENT_G_STREET = "";
    private String SAVE_STUDENT_H_CITY = "";
    private String SAVE_STUDENT_I_STATE = "";
    private String SAVE_STUDENT_J_ZIP = "";
    private String SAVE_STUDENT_K_COUNTRY = "";
    private String SAVE_STUDENT_L_INTL_STUD = "No";
    private String SAVE_STUDENT_M_MARRY = "Single";
    private String SAVE_STUDENT_N_SDA = "No";
    private String SAVE_STUDENT_O_LAST_SCHOOL = "";
    private String SAVE_STUDENT_P_GPA = "0.00";
    private String SAVE_STUDENT_Q_SAT = "0";
    private String SAVE_STUDENT_Q_SAT_V = "0";
    private String SAVE_STUDENT_R_ACT = "0";
    private String SAVE_STUDENT_S_MERIT = "";
    private String SAVE_STUDENT_T_MAJOR = "";
    private String SAVE_STUDENT_U_ACADEMIC = "FR";
    private String SAVE_STUDENT_V_FAMILY = "";
    private String SAVE_STUDENT_W_DORM = "No";
    private String SAVE_STUDENT_X_FAFSA = "No";
    private String SAVE_STUDENT_Y_INDEPT = "No";
    private String SAVE_STUDENT_Z_CALGRANT = "No";
    private String SAVE_STUDENT_AA_CALGRANT_A = "0";
    private String SAVE_STUDENT_AB_CALGRANT_B = "0";
    private String SAVE_STUDENT_AC_FAMILY_SIZE = "2";
    private String SAVE_STUDENT_AD_FAMILY_INCOME = "0";
    private String SAVE_STUDENT_AE_FAMILY_ASSET = "0";
    private String SAVE_STUDENT_AF_FAMILY_CONTRIB = "0";
    private String SAVE_STUDENT_AG_NONLSU_ALLOWRANCE = "0";
    private String SAVE_STUDENT_AH_LSU_ALLOWRANCE = "No";
    private String SAVE_STUDENT_AI_EDU_ALLOW_PER = "0";
    private String SAVE_STUDENT_AJ_HOME_STATE = "";
    private String SAVE_STUDENT_AK_NONCAL_GRANT = "0";
    private String SAVE_STUDENT_AL_OUT_SCHOLARSHIPS = "";
    private String SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT = "0";
    private String SAVE_STUDENT_AN_PUB_NOTES = "";
    private String SAVE_STUDENT_AO_PRI_NOTES = "";
    private String SAVE_STUDENT_AP_SUB_LOANS = "Yes";
    private String SAVE_STUDENT_AQ_UNSUB_LOANS = "Yes";
    private String SAVE_STUDENT_AR_FWS = "Yes";

    //original hard coded funds, willbe overrided by initial methods by data from stud obj
    private String SAVE_STUDENT_AS_SCHOLARSHIP1_NAME = "Church Funds Match";
    private String SAVE_STUDENT_AT_SCHOLARSHIP1_NOTE = "";
    private String SAVE_STUDENT_AU_SCHOLARSHIP1_AMT = "0";
    private String SAVE_STUDENT_AV_SCHOLARSHIP2_NAME = "Summer Camp Earnings (Match)";
    private String SAVE_STUDENT_AW_SCHOLARSHIP2_NOTE = "";
    private String SAVE_STUDENT_AX_SCHOLARSHIP2_AMT = "0";
    private String SAVE_STUDENT_AY_SCHOLARSHIP3_NAME = "Summer Camp Earnings (Match)";
    private String SAVE_STUDENT_AZ_SCHOLARSHIP3_NOTE = "";
    private String SAVE_STUDENT_BA_SCHOLARSHIP3_AMT = "0";
    private String SAVE_STUDENT_BB_SCHOLARSHIP4_NAME = "Literature Evangelist Earnings (Match)";
    private String SAVE_STUDENT_BC_SCHOLARSHIP4_NOTE = "";
    private String SAVE_STUDENT_BD_SCHOLARSHIP4_AMT = "0";

    //added by Ken, to reflect the new order of funds (max 9).  the above funds' new mapping: 1--3  2--5  3--6  4--4
    /*
        studentBgScholarship5Amt=0; 
        studentBjScholarship6Amt=0;
        studentBmScholarship7Amt=0; 
        studentBpScholarship8Amt=0; 
        studentBsScholarship9Amt=0;
        
        this.studentBeScholarship5Name="";
        this.studentBhScholarship6Name="";  
        this.studentBkScholarship7Name=""; 
        this.studentBnScholarship8Name="";
        this.studentBqScholarship9Name="";
        
        this.studentBfScholarship5Note=" ";
        this.studentBiScholarship6Note=" ";
        this.studentBlScholarship7Note=" ";
        this.studentBoScholarship8Note=" ";
        this.studentBrScholarship9Note=" ";
     */
    private String SAVE_SCHOLARSHIP1_NAME = "";
    private String SAVE_SCHOLARSHIP1_NOTE = "";
    private String SAVE_SCHOLARSHIP1_AMT = "0";

    private String SAVE_SCHOLARSHIP2_NAME = "";
    private String SAVE_SCHOLARSHIP2_NOTE = "";
    private String SAVE_SCHOLARSHIP2_AMT = "0";

    private String SAVE_SCHOLARSHIP7_NAME = "";
    private String SAVE_SCHOLARSHIP7_NOTE = "";
    private String SAVE_SCHOLARSHIP7_AMT = "0";

    private String SAVE_SCHOLARSHIP8_NAME = "";
    private String SAVE_SCHOLARSHIP8_NOTE = "";
    private String SAVE_SCHOLARSHIP8_AMT = "0";

    private String SAVE_SCHOLARSHIP9_NAME = "";
    private String SAVE_SCHOLARSHIP9_NOTE = "";
    private String SAVE_SCHOLARSHIP9_AMT = "0";

    private String SAVE_STUDENT_BT_SUPERCOUNSELOR = "";
    private String SAVE_STUDENT_BU_ORIG_COUNSELOR = " x ";
    /////////////////////////////////////////////////////
	private Date SAVE_STUDENT_BV_DOE = new Date();//dateFormat(Now(),"yyyy-01-01");
    private String SAVE_STUDENT_BW_PROGRESS = "4";
    ///////////////////////////////////////////////////
	private String SAVE_STUDENT_BX_MOD_COUNSELOR = "";
    private Date SAVE_STUDENT_BY_DOM = new Date();
    private String SAVE_STUDENT_BZ_UPLOADED = "No";
    private String SAVE_STUDENT_CB_BANNER = "No";
    private String SAVE_STUDENT_USER_NAME = "";//MID(SAVE_STUDENT_C_FIRSTNAME,1,1) & MID(SAVE_STUDENT_B_LASTNAME,1,3);
    private String SAVE_STUDENT_PASSWORD = "auto";
    private String SAVE_STUDENT_STUD_TYPE = "UGFY";

//2012-02-14 moved those non-final static variables to here, and use getter()/setter to access their values
    private int efc = efcInit; //2012-01-12 Esther said EFC should default max=99999, min==0
    private int sdaAward = sdaInit; //not final, , needs to be changed/assigned value in coldfusion fucntion
    //- Family Discount ---;
    private int familyDiscount = familyDisctInit; //not final, needs to be changed/assigned value in coldfusion fucntion

    ///////////////////////////////////in coldfusion function enforceLSULimits(), global coldfusion var,  needs to be reaad and changed/assigned value in coldfusion fucntion    
        private String _nonCaGrantDesc = "";
    private int _nonCaGrantAmt = 0;
    private String _outsideScholarship = "";
    private int _outsideScholarshipAmt = 0;

    private int calGrantA = 0;
    private int calGrantB = 0;
    private int lsuAllowance = 0;
    //    private int   sdaAward=0;  //already defined
    private int lsuPerformance = 0;
    //     private int  familyDiscount=0; //already defined
    private int nationalMerit = 0;
    private int churchMatch = 0;
    private int pacificCampMatch = 0;
    private int nonPacificCampMatch = 0;
    private int litEvanMatch = 0;

    //in coldfusion function enforceOverallLimits()
    private int pellGrant = 0;
    private int fseogAmt = 0;
    private int extAllowance = 0;
    private int nonCaGrantAmt = 0;
    private int outsideAmt = 0;
    private int churchBase = 0;
    private int subDirect = 0;
    private int perkinsLoan = 0;
    private int fwsAmount = 0;
    private int unsubDirect = 0;

    //in coldfusion function getCOA()
    private int needAmt = 0;

    //================= used in summary.cfm
    private int tuitionAndFees = 0;
    private int addlExp = 0;

    //some vars defined in estimatorStudentData.cfm
    private String excludeNote = "exclude loans";

    //some vars defined in estimatorSummary.cfm, and only used in that file.
    private int lsuLimitSubtotal = 0;
    private int err = 0;
    private String nonCaGrantDesc = "";
    private String outsideDesc = "";
    private boolean lsuOverallSubtotal = false;
    private int maxAid = 0;

    private int roomAndBoard = 0;
    private int pacificCampBase = 0;
    private int nonPacificCampBase = 0;
    private int litEvanBase = 0;
    private int amtDue = 0;

    private int yearInAdvanceOption = 0;
    private int quarterInAdvanceOption = 0;
    private int monthlyOption = 0;

    //for education allowance percentage
    private int ea_lsu_per = 100;
    private int ea_nonlsu_per = 35;
    private int ea_nonlsu_dorm_per = 70;

    ///for estimator to set adjust_cal_grant_amt indicator
        private boolean adjust_calgrant_amt_ind = false;

    ////////added to hold the scholarship amount for #7 ~ #9, #1 and #2
        private int scholarship_amt_1 = 0;
    private int scholarship_amt_2 = 0;

    private int scholarship_amt_7 = 0;
    private int scholarship_amt_8 = 0;
    private int scholarship_amt_9 = 0;

    //2012-02-06
    private int lsu4yRenewable = 0;
    private int universityHousingGrant = 0;
    private int lsuAchievement = 0;
    private int lsuAchievementInit = 10000;
    private int lsuNeedGrant = 0;
    //201602
    private int lasuGrantAmt = 0;

    //2012-02-15
    private int initdone_ind = 0;
    private int use_need_ind = -1; //-1: default rule  0:no need-based aid   1:allow need-based aid

    private int sum_tuition_aid = 0;
    private int sum_total_aid = 0;
    private int sum_lasu_aid = 0;

    //2013-02: loan has 1% origination fee deduction
    private int org_loan_amt_sub = 0;
    private int org_loan_amt_unsub = 0;
    private int org_loan_amt_perkins = 0;

    private final int yearInAdvanceDiscountPerc = yearInAdvanceDiscount.movePointRight(2).intValue();
    private final int quarterInAdvanceDiscountPerc = quarterInAdvanceDiscount.movePointRight(2).intValue();

    public void resetValues() {
        //- SDA Membership ---;
        sdaAward = sdaInit; //not final, , needs to be changed/assigned value in coldfusion fucntion

        //- Family Discount ---;
        familyDiscount = familyDisctInit; //not final, needs to be changed/assigned value in coldfusion fucntion

        ///////////////////////////////////in coldfusion function enforceLSULimits(), global coldfusion var,  needs to be reaad and changed/assigned value in coldfusion fucntion    
        _nonCaGrantDesc = "";
        _nonCaGrantAmt = 0;
        _outsideScholarship = "";
        _outsideScholarshipAmt = 0;

        calGrantA = 0;
        calGrantB = 0;
        lsuAllowance = 0;
        //    sdaAward=0;  //already defined
        lsuPerformance = 0;
        //      familyDiscount=0; //already defined
        nationalMerit = 0;
        churchMatch = 0;
        pacificCampMatch = 0;
        nonPacificCampMatch = 0;
        litEvanMatch = 0;

        //in coldfusion function enforceOverallLimits()
        pellGrant = 0;
        fseogAmt = 0;
        extAllowance = 0;
        nonCaGrantAmt = 0;
        outsideAmt = 0;
        churchBase = 0;
        subDirect = 0;
        perkinsLoan = 0;
        fwsAmount = 0;
        unsubDirect = 0;

        //in coldfusion function getCOA()
        needAmt = 0;

        //================= used in summary.cfm
        tuitionAndFees = 0;
        addlExp = 0;
        efc = efcInit;//99999;

        //some vars defined in estimatorStudentData.cfm
        excludeNote = "exclude loans";

        //some vars defined in estimatorSummary.cfm, and only used in that file.
        lsuLimitSubtotal = 0;
        err = 0;
//        nonCaGrantDesc = "";
        outsideDesc = "";
        lsuOverallSubtotal = false;
        maxAid = 0;

        roomAndBoard = 0;
        pacificCampBase = 0;
        nonPacificCampBase = 0;
        litEvanBase = 0;
        amtDue = 0;

        yearInAdvanceOption = 0;
        quarterInAdvanceOption = 0;
        monthlyOption = 0;

        //for education allowance percentage
        ea_lsu_per = 100;
        ea_nonlsu_per = 35;
        ea_nonlsu_dorm_per = 70;

        ///for estimator to set adjust_cal_grant_amt indicator
        adjust_calgrant_amt_ind = false;

        //2012-02-06
        lsu4yRenewable = 0;
        universityHousingGrant = 0;
        lsuAchievement = 0;
        lsuNeedGrant = 0;
        //201602
        lasuGrantAmt = 0;

        ////////added to hold the scholarship amount for #7 ~ #9, #1 and #2
        scholarship_amt_1 = 0;//= std.getStudentAuScholarship1Amt();//std.getStudentBgSchol
        scholarship_amt_2 = std.getStudentAxScholarship2Amt();//std.getStudentBjSchol

        scholarship_amt_7 = std.getStudentBmScholarship7Amt();
        scholarship_amt_8 = std.getStudentBpScholarship8Amt();
        scholarship_amt_9 = std.getStudentBsScholarship9Amt();

        /*
        scholarship_amt_1=0;
        scholarship_amt_2=0;

        scholarship_amt_7=0;
        scholarship_amt_8=0;
        scholarship_amt_9=0;      
         */
        //2012-02-15
        initdone_ind = 0;
        use_need_ind = -1;

        sum_tuition_aid = 0;
        sum_total_aid = 0;
        sum_lasu_aid = 0;

        org_loan_amt_sub = 0;
        org_loan_amt_unsub = 0;
        org_loan_amt_perkins = 0;
    }

    public PackFunctions() {
        this.std = new Student();
    }

    public PackFunctions(Student std) { //old init function, called by old actor
        setStd(std);
    }

    public void newPackFunctions(Student std) {
        this.setStd(std);
    }

    public void setStd(Student std) {
        this.std = std;
        RefreshStudData(std);
        //resetGlobalVar();
        resetValues();
    }

    public void RefreshStudData(Student std) {
        this.SAVE_STUDENT_FISY = std.getStudentFisy(); //=  TRIM(getStudent.STUDENT_FISY)>
        //        log.info("=====stud is null? %s, studnumb=%d", std==null? "true":"false", std.getStudentNumb());
        this.SAVE_STUDENT_NUMBER = 0;//std.getStudentNumb(); //java.lang.NullPointerException \?????? //=  TRIM(getStudent.STUDENT_NUMBER)>
        this.SAVE_STUDENT_A_LSUID = std.getStudentALsuid(); //=  TRIM(getStudent.STUDENT_A_LSUID)>
        this.SAVE_STUDENT_B_LASTNAME = std.getStudentBLastname(); //=  TRIM(getStudent.STUDENT_B_LASTNAME)>
        this.SAVE_STUDENT_C_FIRSTNAME = std.getStudentCFirstname(); //=  TRIM(getStudent.STUDENT_C_FIRSTNAME)>
        this.SAVE_STUDENT_D_DOB = std.getStudentDDob(); //dateFormat(TRIM(getStudent.STUDENT_D_DOB),"mm/dd/yyyy")>
        this.SAVE_STUDENT_E_EMAIL = std.getStudentEEmail(); //=  TRIM(getStudent.STUDENT_E_EMAIL)>
        this.SAVE_STUDENT_F_PHONE = std.getStudentFPhone(); //=  TRIM(getStudent.STUDENT_F_PHONE)>
        this.SAVE_STUDENT_G_STREET = std.getStudentGStreet(); //=  TRIM(getStudent.STUDENT_G_STREET)>
        this.SAVE_STUDENT_H_CITY = std.getStudentGStreet(); //=  TRIM(getStudent.STUDENT_H_CITY)>
        this.SAVE_STUDENT_I_STATE = std.getStudentIState(); //=  TRIM(getStudent.STUDENT_I_STATE)>
        this.SAVE_STUDENT_J_ZIP = std.getStudentJZip(); //=  TRIM(getStudent.STUDENT_J_ZIP)>
        this.SAVE_STUDENT_K_COUNTRY = std.getStudentKCountry(); //=  TRIM(getStudent.STUDENT_K_COUNTRY)>
        this.SAVE_STUDENT_L_INTL_STUD = std.getStudentLIntlStud(); //=  TRIM(getStudent.STUDENT_L_INTL_STUD)>
        this.SAVE_STUDENT_M_MARRY = std.getStudentMMarry(); //=  TRIM(getStudent.STUDENT_M_MARRY)>
        this.SAVE_STUDENT_N_SDA = std.getStudentNSda(); //=  TRIM(getStudent.STUDENT_N_SDA)>
        this.SAVE_STUDENT_O_LAST_SCHOOL = std.getStudentOLastSchool(); //=  TRIM(getStudent.STUDENT_O_LAST_SCHOOL)>
        this.SAVE_STUDENT_P_GPA = std.getStudentPGpa().toString(); //=  TRIM(getStudent.STUDENT_P_GPA)>
/*	<CFIF LEN(SAVE_STUDENT_P_GPA) == 3>
		  this.SAVE_STUDENT_P_GPA = SAVE_STUDENT_P_GPA & "0">
	<CFELSEIF LEN(SAVE_STUDENT_P_GPA) == 1>
		  this.SAVE_STUDENT_P_GPA = SAVE_STUDENT_P_GPA & ".00">
	</CFIF> */
        this.SAVE_STUDENT_Q_SAT = String.valueOf(std.getStudentQSat()); //=  TRIM(getStudent.STUDENT_Q_SAT)>
        this.SAVE_STUDENT_Q_SAT_V = String.valueOf(std.getStudentQSatV()); //=  TRIM(getStudent.STUDENT_Q_SAT_V)>
        this.SAVE_STUDENT_R_ACT = String.valueOf(std.getStudentRAct()); //=  TRIM(getStudent.STUDENT_R_ACT)>
        this.SAVE_STUDENT_S_MERIT = std.getStudentSMerit(); //=  TRIM(getStudent.STUDENT_S_MERIT)>
        this.SAVE_STUDENT_T_MAJOR = std.getStudentTMajor(); //=  TRIM(getStudent.STUDENT_T_MAJOR)>
        this.SAVE_STUDENT_U_ACADEMIC = std.getStudentUAcademic(); //=  TRIM(getStudent.STUDENT_U_ACADEMIC)>
        this.SAVE_STUDENT_V_FAMILY = std.getStudentVFamily();//=  TRIM(getStudent.STUDENT_V_FAMILY)>
        this.SAVE_STUDENT_W_DORM = std.getStudentWDorm();; //=  TRIM(getStudent.STUDENT_W_DORM)>
        this.SAVE_STUDENT_X_FAFSA = std.getStudentXFafsa().trim(); //=  TRIM(getStudent.STUDENT_X_FAFSA)>
        this.SAVE_STUDENT_Y_INDEPT = std.getStudentYIndept(); //=  TRIM(getStudent.STUDENT_Y_INDEPT)>
        this.SAVE_STUDENT_Z_CALGRANT = std.getStudentZCalgrant(); //=  TRIM(getStudent.STUDENT_Z_CALGRANT)>
        this.SAVE_STUDENT_AA_CALGRANT_A = String.valueOf(std.getStudentAaCalgrantA()); //=  TRIM(getStudent.STUDENT_AA_CALGRANT_A)>
        this.SAVE_STUDENT_AB_CALGRANT_B = String.valueOf(std.getStudentAbCalgrantB()); //=  TRIM(getStudent.STUDENT_AB_CALGRANT_B)>
        this.SAVE_STUDENT_AC_FAMILY_SIZE = String.valueOf(std.getStudentAcFamilySize()); //=  TRIM(getStudent.STUDENT_AC_FAMILY_SIZE)>
        this.SAVE_STUDENT_AD_FAMILY_INCOME = String.valueOf(std.getStudentAdFamilyIncome()); //=  TRIM(getStudent.STUDENT_AD_FAMILY_INCOME)>
        this.SAVE_STUDENT_AE_FAMILY_ASSET = String.valueOf(std.getStudentAeFamilyAsset()); //=  TRIM(getStudent.STUDENT_AE_FAMILY_ASSET)>
        this.SAVE_STUDENT_AF_FAMILY_CONTRIB = String.valueOf(std.getStudentAfFamilyContrib()); //=  TRIM(getStudent.STUDENT_AF_FAMILY_CONTRIB)>
        this.SAVE_STUDENT_AG_NONLSU_ALLOWRANCE = String.valueOf(std.getStudentAgNonlsuAllowrance()); //=  TRIM(getStudent.STUDENT_AG_NONLSU_ALLOWRANCE)>
        this.SAVE_STUDENT_AH_LSU_ALLOWRANCE = std.getStudentAhLsuAllowrance(); //=  TRIM(getStudent.STUDENT_AH_LSU_ALLOWRANCE)>
        this.SAVE_STUDENT_AI_EDU_ALLOW_PER = std.getStudentAiEduAllowPer().toString(); //=  TRIM(getStudent.STUDENT_AI_EDU_ALLOW_PER)>
        //<CFIF TRIM(SAVE_STUDENT_AI_EDU_ALLOW_PER) <> "">
        this.SAVE_STUDENT_AI_EDU_ALLOW_PER = std.getStudentAiEduAllowPer().multiply(new BigDecimal(100)).toString();
        //</CFIF>
        this.SAVE_STUDENT_AJ_HOME_STATE = std.getStudentAjHomeState(); //=  TRIM(getStudent.STUDENT_AJ_HOME_STATE)>
        this.SAVE_STUDENT_AK_NONCAL_GRANT = String.valueOf(std.getStudentAkNoncalGrant());//.toString(); //=  TRIM(getStudent.STUDENT_AK_NONCAL_GRANT)>
        this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS = std.getStudentAlOutScholarships(); //=  TRIM(getStudent.STUDENT_AL_OUT_SCHOLARSHIPS)>
        this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT = String.valueOf(std.getStudentAmOutScholarshipAmt());//.toString(); //=  TRIM(getStudent.STUDENT_AM_OUT_SCHOLARSHIP_AMT)>
        this.SAVE_STUDENT_AN_PUB_NOTES = std.getStudentAnPubNotes(); //=  TRIM(getStudent.STUDENT_AN_PUB_NOTES)>
        this.SAVE_STUDENT_AO_PRI_NOTES = std.getStudentAoPriNotes(); //=  TRIM(getStudent.STUDENT_AO_PRI_NOTES)>
        this.SAVE_STUDENT_AP_SUB_LOANS = std.getStudentApSubLoans(); //=  TRIM(getStudent.STUDENT_AP_SUB_LOANS)>
        this.SAVE_STUDENT_AQ_UNSUB_LOANS = std.getStudentAqUnsubLoans(); //=  TRIM(getStudent.STUDENT_AQ_UNSUB_LOANS)>
        this.SAVE_STUDENT_AR_FWS = std.getStudentArFws(); //=  TRIM(getStudent.STUDENT_AR_FWS)>

        this.SAVE_STUDENT_AS_SCHOLARSHIP1_NAME = std.getStudentAyScholarship3Name();//std.getStudentAsScholarship1Name(); //=  TRIM(getStudent.STUDENT_AS_SCHOLARSHIP1_NAME)>
        this.SAVE_STUDENT_AT_SCHOLARSHIP1_NOTE = std.getStudentAzScholarship3Note();//std.getStudentAtScholarship1Note(); //=  TRIM(getStudent.STUDENT_AT_SCHOLARSHIP1_NOTE)>
        this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT = String.valueOf(std.getStudentBaScholarship3Amt());//String.valueOf(std.getStudentAuScholarship1Amt()); //=  TRIM(getStudent.STUDENT_AU_SCHOLARSHIP1_AMT)>

        this.SAVE_STUDENT_AV_SCHOLARSHIP2_NAME = std.getStudentBeScholarship5Name();//std.getStudentAvScholarship2Name(); //=  TRIM(getStudent.STUDENT_AV_SCHOLARSHIP2_NAME)>
        this.SAVE_STUDENT_AW_SCHOLARSHIP2_NOTE = std.getStudentBfScholarship5Note();//std.getStudentAwScholarship2Note(); //=  TRIM(getStudent.STUDENT_AW_SCHOLARSHIP2_NOTE)>
        this.SAVE_STUDENT_AX_SCHOLARSHIP2_AMT = String.valueOf(std.getStudentBgScholarship5Amt());//String.valueOf(std.getStudentAxScholarship2Amt()); //=  TRIM(getStudent.STUDENT_AX_SCHOLARSHIP2_AMT)>

        this.SAVE_STUDENT_AY_SCHOLARSHIP3_NAME = std.getStudentBhScholarship6Name();//std.getStudentAyScholarship3Name(); //=  TRIM(getStudent.STUDENT_AY_SCHOLARSHIP3_NAME)>
        this.SAVE_STUDENT_AZ_SCHOLARSHIP3_NOTE = std.getStudentBiScholarship6Note();//std.getStudentAzScholarship3Note(); //=  TRIM(getStudent.STUDENT_AZ_SCHOLARSHIP3_NOTE)>
        this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT = String.valueOf(std.getStudentBjScholarship6Amt());//std.getStudentBaScholarship3Amt()); //=  TRIM(getStudent.STUDENT_BA_SCHOLARSHIP3_AMT)>

        this.SAVE_STUDENT_BB_SCHOLARSHIP4_NAME = std.getStudentBbScholarship4Name(); //=  TRIM(getStudent.STUDENT_BB_SCHOLARSHIP4_NAME)>
        this.SAVE_STUDENT_BC_SCHOLARSHIP4_NOTE = std.getStudentBcScholarship4Note(); //=  TRIM(getStudent.STUDENT_BC_SCHOLARSHIP4_NOTE)>
        this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT = String.valueOf(std.getStudentBdScholarship4Amt()); //=  TRIM(getStudent.STUDENT_BD_SCHOLARSHIP4_AMT)>

        this.SAVE_SCHOLARSHIP1_NAME = std.getStudentAsScholarship1Name();
        this.SAVE_SCHOLARSHIP1_NOTE = std.getStudentAtScholarship1Note();
        this.SAVE_SCHOLARSHIP1_AMT = String.valueOf(std.getStudentAuScholarship1Amt());

        this.SAVE_SCHOLARSHIP2_NAME = std.getStudentAvScholarship2Name();
        this.SAVE_SCHOLARSHIP2_NOTE = std.getStudentAwScholarship2Note();
        this.SAVE_SCHOLARSHIP2_AMT = String.valueOf(std.getStudentAxScholarship2Amt());

        this.SAVE_SCHOLARSHIP7_NAME = std.getStudentBkScholarship7Name();
        this.SAVE_SCHOLARSHIP7_NOTE = std.getStudentBlScholarship7Note();
        this.SAVE_SCHOLARSHIP7_AMT = String.valueOf(std.getStudentBmScholarship7Amt());

        this.SAVE_SCHOLARSHIP8_NAME = std.getStudentBnScholarship8Name();
        this.SAVE_SCHOLARSHIP8_NOTE = std.getStudentBoScholarship8Note();
        this.SAVE_SCHOLARSHIP8_AMT = String.valueOf(std.getStudentBpScholarship8Amt());

        this.SAVE_SCHOLARSHIP9_NAME = std.getStudentBqScholarship9Name();
        this.SAVE_SCHOLARSHIP9_NOTE = std.getStudentBrScholarship9Note();
        this.SAVE_SCHOLARSHIP9_AMT = String.valueOf(std.getStudentBsScholarship9Amt());

        this.SAVE_STUDENT_BT_SUPERCOUNSELOR = std.getStudentBtSupercounselor(); //=  TRIM(getStudent.STUDENT_BT_SUPERCOUNSELOR)>
        this.SAVE_STUDENT_BU_ORIG_COUNSELOR = std.getStudentBuOrigCounselor(); //=  TRIM(getStudent.STUDENT_BU_ORIG_COUNSELOR)>
        this.SAVE_STUDENT_BV_DOE = std.getStudentBvDoe(); //dateFormat(getStudent.STUDENT_BV_DOE,"dd-mmm-yy")>
        this.SAVE_STUDENT_BW_PROGRESS = std.getStudentBwProgress().toString(); //=  TRIM(getStudent.STUDENT_BW_PROGRESS)>
        this.SAVE_STUDENT_BX_MOD_COUNSELOR = std.getStudentBxModCounselor(); //=  TRIM(getStudent.STUDENT_BX_MOD_COUNSELOR)>
        this.SAVE_STUDENT_BY_DOM = std.getStudentByDom(); //=  TRIM(getStudent.STUDENT_BY_DOM)>
        this.SAVE_STUDENT_BZ_UPLOADED = std.getStudentBzUploaded(); //=  TRIM(getStudent.STUDENT_BZ_UPLOADED)>
        this.SAVE_STUDENT_CB_BANNER = std.getStudentCbBanner(); //=  TRIM(getStudent.STUDENT_CB_BANNER)>
        this.SAVE_STUDENT_USER_NAME = std.getStudentUserName(); //=  TRIM(getStudent.STUDENT_USER_NAME)>
        this.SAVE_STUDENT_PASSWORD = std.getStudentPassword(); //=  TRIM(getStudent.STUDENT_PASSWORD)>
        this.SAVE_STUDENT_STUD_TYPE = std.getStudentStudType(); //=  TRIM(getStudent.STUDENT_STUD_TYPE)>                    
    }

    //=======================================================================================
    public int getLoanAmtAfterOrigination(int loanAmt) {
        //return  loanAmt *(100- ref.getLoan_origination_perc()) /100;

        //Esther infomed that the new fee rate is 1.051%  2013-07-25
        return loanAmt * (100000 - ref.getLoan_origination_perc()) / 100000;
    }

    public int divAmtToFirstTwoTerms(int amt) {
        return amt * 1 / 3;
    }

    public int divAmtToLastTerm(int amt) {
        return amt - amt * 2 / 3;
    }

    public final String TRIM(String str) {
        return str == null ? "" : str.trim();
    }

    public int round(float f) {
        return Math.round(f);
    }

    public String MID(String str, int start, int end) { //couldfusion starts with 1(yes)? include the end? the end is count(yes) or the pos?
        if (str == null || str.trim().isEmpty() || start <= 0 || end <= 0) {
            return "";
        }
        if ((start > (str.length() + 1))) {
            return "";
        }
        if ((start + end) <= (str.length())) {
            return str.substring((start - 1), start + end - 1);
        } else {
            return str.substring(start - 1);
        }
    }
    //=======================================================================================

    /**
     * ******** Template Function*****
     */
    /**
     * ******** Generic Helper Functions*****
     */
    public final String getInternationalStatus() {
        //       log.info(" getInternationalStatus() got SAVE_STUDENT_L_INTL_STUD=%s, SAVE_STUDENT_X_FAFSA=%s", SAVE_STUDENT_L_INTL_STUD, SAVE_STUDENT_X_FAFSA);
        if (TRIM(SAVE_STUDENT_L_INTL_STUD).equalsIgnoreCase("Yes") && SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("No")) {
            return "International";
        } else {
            return "Domestic";
        }
    }

    public final String getIndependentStatus() {
//        log.info(" getIndependentStatus() got SAVE_STUDENT_Y_INDEPT=%s", SAVE_STUDENT_Y_INDEPT);
        if (TRIM(SAVE_STUDENT_Y_INDEPT).equalsIgnoreCase("Yes")) {
            return "Independent";
        } else {
            return "Dependent";
        }
    }

    public final int getRoomAndBoard() {
//        log.info("  getRoomAndBoard() got SAVE_STUDENT_W_DORM=%s", SAVE_STUDENT_W_DORM);
        if (TRIM(SAVE_STUDENT_W_DORM).equalsIgnoreCase("Yes")) {
            return yRoomAndBoard;
        } else {
            return 0;
        }
    }

    public final int getEFC() {
        int _familyContrib = 0;
        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")) {
            /*
                     _familyContrib = REreplace(SAVE_STUDENT_AF_FAMILY_CONTRIB,"\D","","ALL");
                    if(  TRIM(_familyContrib).equalsIgnoreCase("")){
                             _familyContrib = 0;
                    }*/
            _familyContrib = Integer.parseInt(SAVE_STUDENT_AF_FAMILY_CONTRIB);
        } else {
            _familyContrib = 0;//"";
        }
        return _familyContrib;
    }

    /**
     * ******** Online Estimator Functions: Charges*****
     */
    public final int getTuitionAndFees() {
        int _tAndF = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_U_ACADEMIC#
         * <br />#SAVE_STUDENT_L_INTL_STUD#
         * <br />#SAVE_STUDENT_X_FAFSA#
         * <br />#SAVE_STUDENT_W_DORM# </CFOUTPUT
    *********
         */
        if (getInternationalStatus().equalsIgnoreCase("Domestic")) {
            if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                _tAndF = yTotalTuitionAndFeesDomesticFr;
            } else {
                _tAndF = yTotalTuitionAndFeesDomestic;
            }
        } else {
            if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                _tAndF = yTotalTuitionAndFeesInternationalFr;
            } else {
                _tAndF = yTotalTuitionAndFeesInternational;
            }
        }
        return _tAndF;
    }

    public final int getCOA() {
        if (needAmt > 0) {
            return needAmt;
        } else {
            return 0;
        }
    }

    public final int getCOL() { //cost of living
        return getOtherExpenses();
    }

    public final int getMPA() { //most/max possible aid amount
        if (use_need_ind > 0) {
            int amt = getOtherExpenses() - getEFC();
            return amt > 0.01 ? amt : 0;
        } else {
            return getOtherExpenses();
        }
    }

    public String showMPA() {
        return fmt.format(getMPA());
    }

    public final int getFallOrientationFee() {
        int _fallOrFee = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_U_ACADEMIC#
         * <br />#qFallOrientation# </CFOUTPUT
    *********
         */
        if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) { //#SAVE_STUDENT_U_ACADEMIC#
            _fallOrFee = qFallOrientation;
        } else {
            _fallOrFee = 0;
        }
        return _fallOrFee;
    }

    public final int getOtherExpenses() {
        int _otherExp = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_W_DORM#
         * <br />#SAVE_STUDENT_U_ACADEMIC#
         * <br />#yCoaCommunityFr# </CFOUTPUT
    *********
         */
        if (TRIM(SAVE_STUDENT_L_INTL_STUD).equalsIgnoreCase("no")) {
            if (TRIM(SAVE_STUDENT_W_DORM).equalsIgnoreCase("Yes")) {
                if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                    _otherExp = yCoaDormFr;
                } else {
                    _otherExp = yCoaDorm;
                }
            } else {
                if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                    _otherExp = yCoaCommunityFr;
                } else {
                    _otherExp = yCoaCommunity;
                }
            }
        } else {
            if (TRIM(SAVE_STUDENT_W_DORM).equalsIgnoreCase("Yes")) {
                if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                    _otherExp = yCoaDormFrIntl;
                } else {
                    _otherExp = yCoaDormIntl;
                }
            } else {
                if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                    _otherExp = yCoaCommunityFrIntl;
                } else {
                    _otherExp = yCoaCommunityIntl;
                }
            }
        }
        return _otherExp;
    }

    public int getLasuGrantAmt() { //for pdfGen, which will invoke calc.refresh(std) first
        int _lasugrant = 0;
        boolean zerogpa_ind = std.getStudentPGpa().equals(BigDecimal.ZERO);
        String _gpa = String.format("%3.2f", std.getStudentPGpa());
        int _efc = this.getEFC();
        if (_gpa.compareTo("2.00") >= 0 && _gpa.compareTo("2.49") <= 0 && _efc <= 12000) {
            _lasugrant = 1000;
        }
        return _lasugrant;
    }

    public String showLasuGrantAmt() { //for pdfGen, which will invoke calc.refresh(std) first
        //if(!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES"))return "n/a";
        return fmt.format(lasuGrantAmt);
    }

    /**
     * ******** Online Estimator Functions: Aid*****
     */
    public final int getPell() {
        //return getPell1718();
        return getPell1920();
    }

    private final int getPell1920() {
        int _pell = 0, _efcBase = 0, _pellBase = 0;
        //      log.info(" getPell() got SAVE_STUDENT_X_FAFSA=%s", SAVE_STUDENT_X_FAFSA);
        int _coa = this.getCOA(); //needAmt

        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= pellCOAbase && _coa <= pellCOAtop) {
            int _efc = this.getEFC();
            if (_efc == 0) {
                _pell = pellBase;
            } else if (_efc >= 5501 && _efc <= 5576) {//(_efc>4800 && _efc<4996){
                _pell = 657;//606;//598;//626;//602;//602;
            } else if (_efc >= 5577) {//5199//5158){//(_efc>=4996){
                _pell = 0;
            } else {
                int j;
                j = (int) Math.floor((_efc - 1) / 100) * 100;
                _pell = 6145 - j;

            }
        }
        return _pell;
    }

    private final int getPell1718() {
        int _pell = 0, _efcBase = 0, _pellBase = 0;
        //      log.info(" getPell() got SAVE_STUDENT_X_FAFSA=%s", SAVE_STUDENT_X_FAFSA);
        int _coa = this.getCOA(); //needAmt

        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= pellCOAbase && _coa <= pellCOAtop) {
            int _efc = this.getEFC();
            if (_efc == 0) {
                _pell = pellBase;
            } else if (_efc >= 5301 && _efc <= 5328) {//(_efc>4800 && _efc<4996){
                _pell = 606;//598;//626;//602;//602;
            } else if (_efc >= 5329) {//5199//5158){//(_efc>=4996){
                _pell = 0;
            } else {
                int j;
                j = (int) Math.floor((_efc - 1) / 100) * 100;
                _pell = 5870 - j;

            }
        }
        return _pell;
    }

    private final int getPell1617() {
        int _pell = 0, _efcBase = 0, _pellBase = 0;
        //      log.info(" getPell() got SAVE_STUDENT_X_FAFSA=%s", SAVE_STUDENT_X_FAFSA);
        int _coa = this.getCOA(); //needAmt

        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= pellCOAbase && _coa <= pellCOAtop) {
            int _efc = this.getEFC();
            if (_efc == 0) {
                _pell = pellBase;
            } else if (_efc >= 5201 && _efc <= 5234) {//(_efc>4800 && _efc<4996){
                _pell = 598;//626;//602;//602;
            } else if (_efc >= 5235) {//5199//5158){//(_efc>=4996){
                _pell = 0;
            } else {
                int j;
                j = (int) Math.floor((_efc - 1) / 100) * 100;
                _pell = 5765 - j;

            }
        }
        return _pell;
    }

    private final int getPell1516() {
        int _pell = 0, _efcBase = 0, _pellBase = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AF_FAMILY_CONTRIB# </CFOUTPUT
    *********
         */
        //      log.info(" getPell() got SAVE_STUDENT_X_FAFSA=%s", SAVE_STUDENT_X_FAFSA);
        int _coa = this.getCOA(); //needAmt

        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= pellCOAbase && _coa <= pellCOAtop) {
            int _efc = this.getEFC();
            if (_efc == 0) {
                _pell = pellBase;
            } else if (_efc >= 5101 && _efc <= 5198) {//(_efc>4800 && _efc<4996){
                _pell = 626;//602;//602;
            } else if (_efc >= 5199) {//5158){//(_efc>=4996){
                _pell = 0;
            } else {
                int j;
                /*
                j = _efc % 100;
                if( j>0){
                    _pell = 5680 - _efc + j ;//(_efc-j);
                }else{
                    _pell = 5680 - _efc + 100; //(_efc/100 -1)*100;
                }
                 */
                j = (int) Math.floor((_efc - 1) / 100) * 100;
                _pell = 5725 - j;

            }
        }

        /*
            if(  !SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")){
                     _pell = 0;
            }else{
                    if(  !getInternationalStatus().equalsIgnoreCase("Domestic")){
                             _pell = 0;
                    }else{
                             _efcBase =   Math.round(( std.getStudentAfFamilyContrib()-51)/100)*100; //round((SAVE_STUDENT_AF_FAMILY_CONTRIB-51)/100)*100;
                            if(  std.getStudentAfFamilyContrib()==0) { //TRIM(SAVE_STUDENT_AF_FAMILY_CONTRIB) == 0 ){
                                     _pell = pellMax0;
                            }else if( _efcBase <= 5000 ){
                                     _pellBase = 5500;
                                     _pell = _pellBase - _efcBase;
                            }else{	
                                    if(  _efcBase  < 5273 ){
                                             _pell = pellMax5273;
                                    }else{
                                             _pell = 0;
                                    }
                            }
                    }
            }*/
        return _pell;
    }

    public final int getCalGrantA() {
        int _aCalGrantAmt = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />0)FAFSA: #SAVE_STUDENT_X_FAFSA#
         * <br />1)GPA: #SAVE_STUDENT_P_GPA#
         * <br />2)Ind Status: #getIndependentStatus()#
         * <br />3)Married: #SAVE_STUDENT_M_MARRY#
         * <br />4)Size: #SAVE_STUDENT_AC_FAMILY_SIZE#
         * <br />5)Income: #SAVE_STUDENT_AD_FAMILY_INCOME#
         * <br />6)Asset: #SAVE_STUDENT_AE_FAMILY_ASSET# </CFOUTPUT
    *********
         */
        if (adjust_calgrant_amt_ind == true && std.getStudentZCalgrant().equalsIgnoreCase("yes")) {
            return std.getStudentAaCalgrantA();//SAVE_STUDENT_AA_CALGRANT_A;    
        }
        //if(  !SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")){
        if (!SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("Yes")) {
            _aCalGrantAmt = 0;
        } else {
            if (SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("Yes") && std.getStudentPGpa().compareTo(new BigDecimal(3.00)) >= 0 && std.getStudentAcFamilySize() > 0) {//SAVE_STUDENT_P_GPA >= 3 && SAVE_STUDENT_AC_FAMILY_SIZE  > 0 ){
                if (getIndependentStatus().equalsIgnoreCase("Independent") && std.getStudentAeFamilyAsset() <= aCalGrantAssetCeilingInd) {//SAVE_STUDENT_AE_FAMILY_ASSET <= aCalGrantAssetCeilingInd){
                    if (MID(SAVE_STUDENT_M_MARRY, 1, 1).equalsIgnoreCase("S")) {
                        if (std.getStudentAcFamilySize() <= 1 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndSingleFamily1) {// SAVE_STUDENT_AC_FAMILY_SIZE <= 1  && SAVE_STUDENT_AD_FAMILY_INCOME <= aCalGrantIncomeCeilingIndSingleFamily1){
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 2 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndSingleFamily2) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 3 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndSingleFamily3) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 4 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndSingleFamily4) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 5 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndSingleFamily5) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() > 5 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndSingleFamily6) {
                            _aCalGrantAmt = aCalGrant;
                        } else {
                            _aCalGrantAmt = 0;
                        }
                    } else {
                        if (std.getStudentAcFamilySize() == 1 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndMarriedFamily1) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 2 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndMarriedFamily2) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 3 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndMarriedFamily3) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 4 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndMarriedFamily4) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() == 5 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndMarriedFamily5) {
                            _aCalGrantAmt = aCalGrant;
                        } else if (std.getStudentAcFamilySize() > 5 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingIndMarriedFamily6) {
                            _aCalGrantAmt = aCalGrant;
                        } else {
                            _aCalGrantAmt = 0;
                        }
                    }
                } else if (getIndependentStatus().equalsIgnoreCase("Dependent") && std.getStudentAeFamilyAsset() <= aCalGrantAssetCeilingOther) {// SAVE_STUDENT_AE_FAMILY_ASSET <= aCalGrantAssetCeilingOther){
                    if (std.getStudentAcFamilySize() == 1 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingOtherFamily1) {
                        _aCalGrantAmt = aCalGrant;
                    } else if (std.getStudentAcFamilySize() == 2 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingOtherFamily2) {
                        _aCalGrantAmt = aCalGrant;
                    } else if (std.getStudentAcFamilySize() == 3 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingOtherFamily3) {
                        _aCalGrantAmt = aCalGrant;
                    } else if (std.getStudentAcFamilySize() == 4 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingOtherFamily4) {
                        _aCalGrantAmt = aCalGrant;
                    } else if (std.getStudentAcFamilySize() == 5 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingOtherFamily5) {
                        _aCalGrantAmt = aCalGrant;
                    } else if (std.getStudentAcFamilySize() > 5 && std.getStudentAdFamilyIncome() <= aCalGrantIncomeCeilingOtherFamily6) {
                        _aCalGrantAmt = aCalGrant;
                    } else {
                        _aCalGrantAmt = 0;
                    }
                } else {
                    _aCalGrantAmt = 0;
                }
            } else {
                _aCalGrantAmt = 0;
            }
        }
        return _aCalGrantAmt;
    }

    public final int getCalGrantB() {
        int _bCalGrantAmt = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_P_GPA#
         * <br />#getIndependentStatus()#
         * <br />#SAVE_STUDENT_M_MARRY#
         * <br />#SAVE_STUDENT_AC_FAMILY_SIZE#
         * <br />#SAVE_STUDENT_AD_FAMILY_INCOME#
         * <br />#SAVE_STUDENT_AE_FAMILY_ASSET# </CFOUTPUT
    *********
         */
        if (adjust_calgrant_amt_ind == true && std.getStudentZCalgrant().equalsIgnoreCase("yes")) {
            return std.getStudentAbCalgrantB();
        }

        String _bCalGrantAmtAward = "No";

        if (!SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("Yes")
                || getCalGrantA() > 0 || std.getStudentPGpa().compareTo(new BigDecimal(2)) < 0 || std.getStudentPGpa().compareTo(new BigDecimal(3)) > 0) {//
            //SAVE_STUDENT_P_GPA  < 2               || SAVE_STUDENT_P_GPA  > 3 ){
            _bCalGrantAmt = 0;
        } else {
            if (getIndependentStatus().equalsIgnoreCase("Independent")) {
                if (MID(SAVE_STUDENT_M_MARRY, 1, 1).equalsIgnoreCase("S")) {
                    // it was getStudentAdFamilyIncome by 2014-02-20
                    if (std.getStudentAeFamilyAsset() <= bCalGrantAssetCeilingIndSingle) {// SAVE_STUDENT_AD_FAMILY_ASSET <= bCalGrantAssetCeilingIndSingle){
                        if (std.getStudentAcFamilySize() <= 1) {// SAVE_STUDENT_FAMILY_SIZE <= 1){
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndSingleFamily1) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() == 2) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndSingleFamily2) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() == 3) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndSingleFamily3) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() == 4) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndSingleFamily4) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() == 5) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndSingleFamily5) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() >= 6) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndSingleFamily6) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        }
                    }
                } else {
                    if (std.getStudentAeFamilyAsset() <= bCalGrantAssetCeilingIndMarried) {// SAVE_STUDENT_AD_FAMILY_ASSET <= bCalGrantAssetCeilingIndMarried){
                        if (std.getStudentAcFamilySize() <= 1) {// SAVE_STUDENT_FAMILY_SIZE <= 1){
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndMarriedFamily1) {//  SAVE_STUDENT_AD_FAMILY_INCOME <= bCalGrantIncomeCeilingIndMarriedFamily1){
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() <= 2) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndMarriedFamily2) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() <= 3) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndMarriedFamily3) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() <= 4) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndMarriedFamily4) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() <= 5) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndMarriedFamily5) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        } else if (std.getStudentAcFamilySize() >= 6) {
                            if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingIndMarriedFamily6) {
                                _bCalGrantAmtAward = "Yes";
                            }
                        }
                    }
                }
            } else {
                if (std.getStudentAeFamilyAsset() <= bCalGrantAssetCeilingOther) {// SAVE_STUDENT_AE_FAMILY_ASSET <= bCalGrantAssetCeilingOther){
                    if (std.getStudentAcFamilySize() <= 1) {
                        if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingOtherFamily1) {
                            _bCalGrantAmtAward = "Yes";
                        }
                    } else if (std.getStudentAcFamilySize() == 2) {
                        if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingOtherFamily2) {
                            _bCalGrantAmtAward = "Yes";
                        }
                    } else if (std.getStudentAcFamilySize() == 3) {
                        if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingOtherFamily3) {
                            _bCalGrantAmtAward = "Yes";
                        }
                    } else if (std.getStudentAcFamilySize() == 4) {
                        if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingOtherFamily4) {
                            _bCalGrantAmtAward = "Yes";
                        }
                    } else if (std.getStudentAcFamilySize() == 5) {
                        if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingOtherFamily5) {
                            _bCalGrantAmtAward = "Yes";
                        }
                    } else if (std.getStudentAcFamilySize() > 5) {
                        if (std.getStudentAdFamilyIncome() <= bCalGrantIncomeCeilingOtherFamily6) {
                            _bCalGrantAmtAward = "Yes";
                        }
                    }
                }
            }
        }

        if (_bCalGrantAmtAward.equalsIgnoreCase("Yes")) {
            if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR")) {
                _bCalGrantAmt = bCalGrantFr;
            } else if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("F2")) {
                _bCalGrantAmt = bCalGrantFr2;
            } else if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SO")) {
                _bCalGrantAmt = bCalGrantSoJrSr;
            } else if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) {
                _bCalGrantAmt = bCalGrantSoJrSr;
            } else if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) {
                _bCalGrantAmt = bCalGrantSoJrSr;
            } else if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("S2")) {
                _bCalGrantAmt = bCalGrantSoJrSr;
            } else {
                _bCalGrantAmt = 0;
            }
        } else {
            _bCalGrantAmt = 0;
        }
        return _bCalGrantAmt;
    }

    public final int getFseog() {
        int _fseogAmt = 0;

        //2015 by esther
        //return _fseogAmt;
        /**
         * ********
         * <CFOUTPUT
         * <br />CalGrant A: #getCalGrantA()#
         * <br />CalGrant B: #getCalGrantB()# < #bCalGrantSoJrSr#
         * <br />External: #getExternalAllowance()#
         * <br />Merit: #SAVE_STUDENT_S_MERIT#
         * <br />National Merit: #getNationalMerit()#
         * <br />LSU Performance: #getLsuPerformance()#
         * <br />LSU Allowance: #getLsuAllowance()#
         * <br />EFC: #SAVE_STUDENT_AF_FAMILY_CONTRIB#
         * <br />FAFSA: #SAVE_STUDENT_X_FAFSA# </CFOUTPUT
    *********
         */
        if (!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")) {
            _fseogAmt = 0;
        } else {
            if (getCalGrantB() > 1551 || getCalGrantA() > 1551 || getExternalAllowance() > 0 || getLsuAllowance() > 0 || getNationalMerit() > 0) {
                _fseogAmt = 0;
            } else {
                if (std.getStudentAfFamilyContrib() < 1000) {
                    _fseogAmt = standardFseogEfcSub1000;
                } else if (std.getStudentAfFamilyContrib() < 2000) {
                    _fseogAmt = standardFseogEfcSub2000;
                } else {
                    _fseogAmt = 0;
                }

                /*
                        if(  getCalGrantA()  > 0
                        || getCalGrantB()  > 1551
                        || getExternalAllowance()  > 0
                        || (getNationalMerit()  > getLsuPerformance()
                        &&  !TRIM(MID(SAVE_STUDENT_S_MERIT,1,1)).equalsIgnoreCase(""))){
                                if( std.getStudentAfFamilyContrib()<1000 ){// SAVE_STUDENT_AF_FAMILY_CONTRIB  < 1000){
                                        _fseogAmt = fseogWithCalGrantEdAllowOrNatMeritEfcSub1000;
                                }else{
                                        _fseogAmt = 0;
                                }
                        }else{
                                if(  std.getStudentAfFamilyContrib()  < 1000){
                                        _fseogAmt = standardFseogEfcSub1000;
                                }else if( std.getStudentAfFamilyContrib()  < 2000){
                                        _fseogAmt = standardFseogEfcSub2000;
                                }else{
                                        _fseogAmt = 0;
                                }
                        }*/
            }
        }
        return _fseogAmt;
    }

    public final int getLsuAllowance() {
        int _edAllowance = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />Tuition & Fees: #getTuitionAndFees()#
         * <br />Orientation: #yFrOrientation#
         * <br />Allowance PCT: #SAVE_STUDENT_AI_EDU_ALLOW_PER#/100
         * <br />LSU_ALLOWANCE: #SAVE_STUDENT_AH_LSU_ALLOWRANCE#
         * <br />ACADEMIC: #SAVE_STUDENT_U_ACADEMIC#
         * <br />_edAllowance1 = #getTuitionAndFees()-yFrOrientation# *
         * #SAVE_STUDENT_AI_EDU_ALLOW_PER/100# </CFOUTPUT
    *********
         */
        //        log.info("eaeaeaeaeae getLsuAllowance() got SAVE_STUDENT_AH_LSU_ALLOWRANCE=%s, ui_value=%s", SAVE_STUDENT_AH_LSU_ALLOWRANCE, std.getStudentAhLsuAllowrance());
        if (TRIM(SAVE_STUDENT_AH_LSU_ALLOWRANCE).equalsIgnoreCase("Yes")) {
            //if( std.getStudentAiEduAllowPer().compareTo(new BigDecimal(0))>0){// SAVE_STUDENT_AI_EDU_ALLOW_PER  > 0){
            if (SAVE_STUDENT_U_ACADEMIC == null) {
                SAVE_STUDENT_U_ACADEMIC = "FR";
            }
            if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR")) {  //NPE ???????
                //_edAllowance = new BigDecimal(getTuitionAndFees()-yFrOrientation ).multiply(std.getStudentAiEduAllowPer()).intValue()/100;  //(getTuitionAndFees()-yFrOrientation) * SAVE_STUDENT_AI_EDU_ALLOW_PER/100;
                _edAllowance = (new BigDecimal(getTuitionAndFees() - yFrOrientation).multiply(new BigDecimal(std.getEa_lsu_perc())).divide(new BigDecimal(100))).intValue();
                  
            ///100;
                            }else{
                                 // _edAllowance =  new BigDecimal(getTuitionAndFees() ).multiply(std.getStudentAiEduAllowPer()).intValue()/100;//getTuitionAndFees() * SAVE_STUDENT_AI_EDU_ALLOW_PER/100;
                                _edAllowance = new BigDecimal(getTuitionAndFees()).intValue();
              
        
            ///100;
                            }
                    //}else{
                    //         _edAllowance = 0;//"0){
                    //}
            }else{
                     _edAllowance = 0;//"0){
        }
        return _edAllowance;
    }

    public final int getExternalAllowance() {
        int _extAllowance = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AG_NONLSU_ALLOWRANCE#
         * <br />#SAVE_STUDENT_AI_EDU_ALLOW_PER#
         * <br />#SAVE_STUDENT_U_ACADEMIC# </CFOUTPUT
    *********
         */
        //       log.info("eaeaeaeaeeaea getExternalAllowance() got SAVE_STUDENT_AG_NONLSU_ALLOWRANCE=[%s], perc=%s, dorm=%s", SAVE_STUDENT_AG_NONLSU_ALLOWRANCE, std.getStudentAiEduAllowPer(), std.getStudentWDorm());
        if (!std.getStudentAgNonlsuAllowrance().equals(BigDecimal.ZERO)) {
            //if(  TRIM(SAVE_STUDENT_AG_NONLSU_ALLOWRANCE).equalsIgnoreCase("1")){ //not "Yes"    , but init load set it as "1.00"   
            //                   log.info("eaeaeaeaeeaea getExternalAllowance() checked");
            if (std.getStudentAiEduAllowPer().compareTo(new BigDecimal(0)) > 0) {//  SAVE_STUDENT_AI_EDU_ALLOW_PER  > 0){
                //                          log.info("eaeaeaeaeeaea getExternalAllowance() std.getStudentAiEduAllowPer() > 0 while tuition_fees=%d", getTuitionAndFees());
                // no .divide(new BigDecimal(100))
                _extAllowance = new BigDecimal(getTuitionAndFees()).multiply(std.getStudentAiEduAllowPer()).intValue();//round( getTuitionAndFees() * SAVE_STUDENT_AI_EDU_ALLOW_PER/100);
            } else {
                _extAllowance = 0;//"0){
            }
        } else {
            _extAllowance = 0;//"0){
        }
        return _extAllowance;
    }

    public final int getNonCaGrant() {
        //String _nonCaGrantDesc="";
        //int _nonCaGrantAmt=0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AJ_HOME_STATE#
         * <br />#SAVE_STUDENT_AK_NONCAL_GRANT# </CFOUTPUT
    *********
         */
        if (!TRIM(SAVE_STUDENT_AJ_HOME_STATE).equalsIgnoreCase("")) {
            _nonCaGrantDesc = TRIM(SAVE_STUDENT_AJ_HOME_STATE);
            _nonCaGrantAmt = Integer.parseInt(SAVE_STUDENT_AK_NONCAL_GRANT);// REreplace(SAVE_STUDENT_AK_NONCAL_GRANT,"\D","","ALL");
            /*  if(  _nonCaGrantAmt.equalsIgnoreCase("")){
                             _nonCaGrantAmt = 0;
                    }*/
        } else {
            _nonCaGrantDesc = "No Non-CA State Grant";
            _nonCaGrantAmt = 0;
        }
        return _nonCaGrantAmt;
    }

    public final int getOutsideScholarship() {
        //String _outsideScholarship="";
        //int _outsideScholarshipAmt=0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AL_OUT_SCHOLARSHIPS#
         * <br />#SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT# </CFOUTPUT
    *********
         */
        if (!TRIM(SAVE_STUDENT_AL_OUT_SCHOLARSHIPS).equalsIgnoreCase("")) {
            _outsideScholarship = SAVE_STUDENT_AL_OUT_SCHOLARSHIPS;
            _outsideScholarshipAmt = Integer.parseInt(SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT);// REreplace(SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT,"\D","","ALL");
            /*  if(  _outsideScholarshipAmt.equalsIgnoreCase("")){ */
            if (_outsideScholarshipAmt == 0) {
                _nonCaGrantAmt = 0;
            }
        } else {
            _outsideScholarship = "No Outside Scholarship";
            _outsideScholarshipAmt = 0;
        }
        return _outsideScholarshipAmt;
    }

    public final int getSdaAward() {
        int _sdaAmt = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_N_SDA# </CFOUTPUT
    *********
         */
        //      log.info(" getSdaAward() got SAVE_STUDENT_N_SDA=%s, trim(SAVE_STUDENT_N_SDA)=%s", SAVE_STUDENT_N_SDA, TRIM(SAVE_STUDENT_N_SDA));

        if (TRIM(SAVE_STUDENT_N_SDA).equalsIgnoreCase("Yes")) {
            _sdaAmt = sdaAward;
            //                    log.info(" getSdaAward() says SAVE... == YES");
        } else {
            _sdaAmt = 0;
            //                   log.info(" getSdaAward() says SAVE... != YES");
        }
        return _sdaAmt;
    }

    //2012-02-06 new Grant
    public final int getLsuNeedGrantAmt() { //lsuNeedGrant //not for off-campus (CJ /DL) or reduced tuition (ESL / ELC/ ACCESS)
        int _needGrantAmt = 0;
        String _grd = std.getStudentUAcademic();//.toUpperCase(); NPE
        if (_grd == null || _grd.isEmpty()) {
            _grd = "FR";
        } else {
            _grd = _grd.toUpperCase();
        }

        if (std.getStudentStudType().equalsIgnoreCase("UGFY") || _grd.equals("FR") || _grd.equals("F2") || _grd.equals("SO") || _grd.equals("JR") || _grd.equals("SR")) {
            int _efcAmt = std.getStudentAfFamilyContrib();
            if (std.getIndEfc().equalsIgnoreCase("Yes") && _efcAmt >= 0 && _efcAmt <= 12000) {
                _needGrantAmt = 3000;//2100;//2060;
            }
        }
        return lsuNeedGrant = _needGrantAmt;
    }

    //2012-02-06  new rule splits lsuperformance into two (or) awards: archievement(score based) and 4y-renewable (GPA based)
    public final int getLsuAchievement() {
        int _achieve = 0;
        //log.info("xxxxxxxx getLsuAchievement() got SAVE_STUDENT_U_ACADEMIC=%s, std=%s, std.level=%s, merit=%d", SAVE_STUDENT_U_ACADEMIC, std==null?"NULL":"OBJ", std==null?"NULL":std.getStudentUAcademic(), getNationalMerit());   
        //got SAVE_STUDENT_U_ACADEMIC=null, std=OBJ, std.level=null, merit=0
        if (std == null) {
            return 0;
        }
        if (SAVE_STUDENT_U_ACADEMIC == null) {
            if (std.getStudentUAcademic() == null) {
                std.setStudentUAcademic("FR");
            }
            SAVE_STUDENT_U_ACADEMIC = std.getStudentUAcademic();
        }

        //Esther 2017-02 SAT criteria has changed from 1900 to 1270 (the SAT scores no longer include a writing component
        if (!SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR") || (std.getStudentQSat() < 1270 && std.getStudentRAct() < 29) || getNationalMerit() > 0) {  //java.lang.NullPointerException
            return _achieve;
        } else {
            return lsuAchievementInit;
        }
    }

    public final int getLsu4yRenewable() {//std either get this award or one of(merits, achievement), which is higher
        int _4yrenewable = 0;
        boolean zerogpa_ind = std.getStudentPGpa().equals(BigDecimal.ZERO);
        String _gpa = String.format("%3.2f", std.getStudentPGpa());
        /*
BigDecimal bd = new BigDecimal("3.789");//("3.7");//no scale for strign init val
println String.format("%3.2f",bd);//auto scale, no matter string or dig init val
//bd.setScale(2, RoundingMode.HALF_UP);//BigDecimal.ROUND_HALF_UP ); //RoundingMode.HALF_UP
std.getStudentPGpa().setScale(2, RoundingMode.HALF_UP).toString(); //String is faster ???
* */
        if (SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR") && zerogpa_ind) {
            if (std.getStudentLIntlStud().equalsIgnoreCase("YES")) {
                _gpa = "3.25";//new BigDecimal("3.25");
            } else {
                _gpa = "2.75";//new BigDecimal("2.75");
            }
        }
        if (_gpa.compareTo("2.50") < 0) {
            /*}else if ( _gpa.compareTo( "2.00")>=0 && _gpa.compareTo("2.49")<=0 ){
            _4yrenewable = 1000;*/
        } else if (_gpa.compareTo("2.50") >= 0 && _gpa.compareTo("2.74") <= 0) {
            _4yrenewable = 2200;//2150;//2100;//2060;//1500;
        } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("2.99") <= 0) {
            _4yrenewable = 3280;//3200;//3150;//3090;//2500;
        } else if (_gpa.compareTo("3.00") >= 0 && _gpa.compareTo("3.24") <= 0) {
            _4yrenewable = 4410;//4300;//4200;//4120;//3500;
        } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
            _4yrenewable = 5540;//5400;//5250;//5150;//4500;
        } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
            _4yrenewable = 6670;//6500;//6300;//6180;//5500;
        } else if (_gpa.compareTo("3.75") >= 0) {//&& _gpa.compareTo("4.00")<=0 ){
            _4yrenewable = 8300;//8100;//7880;//7725;//7000;
        }

        return _4yrenewable;
    }

    public final int getLsuPerformance() {
        return 0;
    }

    public final int getFamilyDiscount() {
        int _familyDiscount = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_V_FAMILY# </CFOUTPUT
    *********
         */
        if (TRIM(SAVE_STUDENT_V_FAMILY).length() > 3) {
            _familyDiscount = familyDiscount;
        } else {
            _familyDiscount = 0;
        }
        return _familyDiscount;
    }

    public final int getNationalMerit() {
        int _nationalMeritAmt = 0;
        int _nationalMeritBase = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_S_MERIT#
         * <br />#getTuitionAndFees()# </CFOUTPUT
    *********
         */
        if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
            _nationalMeritAmt = 0;
        } else {
            /**
             * ******** Fall Orientation Fee is zero for non-FR students*****
             */
            _nationalMeritBase = getTuitionAndFees() - getFallOrientationFee();
            /**
             * ********* merits should be multiple options, not radio/single
             * options************************ if(
             * TRIM(SAVE_STUDENT_S_MERIT).equalsIgnoreCase("MC")){
             * _nationalMeritAmt = new BigDecimal(_nationalMeritBase
             * ).multiply(nationalMeritMC).intValue(); // _nationalMeritBase *
             * nationalMeritMC; }else if(
             * TRIM(SAVE_STUDENT_S_MERIT).equalsIgnoreCase("MS")){
             * _nationalMeritAmt = new BigDecimal(_nationalMeritBase
             * ).multiply(nationalMeritMS).intValue(); //_nationalMeritBase *
             * nationalMeritMS; }else if(
             * TRIM(SAVE_STUDENT_S_MERIT).equalsIgnoreCase("MF")){
             * _nationalMeritAmt = new BigDecimal(_nationalMeritBase
             * ).multiply(nationalMeritMF).intValue(); //_nationalMeritBase *
             * nationalMeritMF; }else{ _nationalMeritAmt = 0;
                    }**************
             */
            if (TRIM(SAVE_STUDENT_S_MERIT).indexOf("MF") >= 0) {
                _nationalMeritAmt = new BigDecimal(_nationalMeritBase).multiply(nationalMeritMF).intValue(); //_nationalMeritBase * nationalMeritMF;
            } else if (TRIM(SAVE_STUDENT_S_MERIT).indexOf("MS") >= 0) {
                _nationalMeritAmt = new BigDecimal(_nationalMeritBase).multiply(nationalMeritMS).intValue(); //_nationalMeritBase * nationalMeritMS;
            } else if (TRIM(SAVE_STUDENT_S_MERIT).indexOf("MC") >= 0) {
                _nationalMeritAmt = new BigDecimal(_nationalMeritBase).multiply(nationalMeritMC).intValue(); // _nationalMeritBase * nationalMeritMC;
            } else {
                _nationalMeritAmt = 0;
            }
        }
        return _nationalMeritAmt;
    }

    public final int getSubDirect() {
        int _subDirect = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_U_ACADEMIC# </CFOUTPUT
    *********
         */
        if (!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") || std.getIndExcloans().equalsIgnoreCase("YES") || std.getStudentApSubLoans().equalsIgnoreCase("no")) {
            _subDirect = 0;
        } else if (use_need_ind > 0) {
            if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                _subDirect = subDirectLoanMaxFr;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")) {
                _subDirect = subDirectLoanMaxFr2;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")) {
                _subDirect = subDirectLoanMaxSo;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")) {
                _subDirect = subDirectLoanMaxJr;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
                _subDirect = subDirectLoanMaxSr;
            } else {
                _subDirect = 0;
            }
        }
        return _subDirect;
    }

    public final int getPerkinsLoan() {
        int _perkins = 0;

        //by esther 1516
        // return _perkins;
        //2016-02 Esther said no more perkins loan
        if (1 == 1) {
            return _perkins;
        }

        if (1 == 1 || !SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") || std.getIndExcloans().equalsIgnoreCase("YES") || use_need_ind == 0
                || getCalGrantA() > 0
                || getCalGrantB() > 1551
                || getExternalAllowance() > 0
                || (getNationalMerit() > 0)) {
            _perkins = 0;
            return _perkins;
        }

        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")
                || TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")
                || TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR"))) {
            if (std.getStudentAfFamilyContrib() <= perkinsSubEfc1) {
                _perkins = perkinsSubVal1;
            } else if (std.getStudentAfFamilyContrib() <= perkinsSubEfc2) {
                _perkins = perkinsSubVal2;
            }
        }

        return _perkins;
    }

    public final int getFWS() {
        int _fws = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_X_FAFSA#
         * <br />#SAVE_STUDENT_AF_FAMILY_CONTRIB# </CFOUTPUT
    *********
         */
        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && std.getStudentArFws().equalsIgnoreCase("yes") && use_need_ind > 0) {
            if (std.getStudentAfFamilyContrib() < 4996) {// SAVE_STUDENT_AF_FAMILY_CONTRIB  < 2000){
                _fws = fwsSub4996;
            } else if (std.getStudentAfFamilyContrib() < 12001) {// SAVE_STUDENT_AF_FAMILY_CONTRIB  < 12000){
                _fws = fwsSub12001;
            } else {//SAVE_STUDENT_AF_FAMILY_CONTRIB <= yTuition){
                _fws = fwsSub100000;
            }
        }

        /*
            if(  TRIM(SAVE_STUDENT_X_FAFSA).equalsIgnoreCase("Yes")    && std.getStudentArFws().equalsIgnoreCase("yes")){
                    if( std.getStudentAfFamilyContrib()<2000){// SAVE_STUDENT_AF_FAMILY_CONTRIB  < 2000){
                             _fws = fwsSub2000;
                    }else if( std.getStudentAfFamilyContrib()<12000){// SAVE_STUDENT_AF_FAMILY_CONTRIB  < 12000){
                             _fws = fwsSub12000;
                    }else if( std.getStudentAfFamilyContrib() < yTuition){//SAVE_STUDENT_AF_FAMILY_CONTRIB <= yTuition){
                             _fws = fwsSub100000;
                    }else{
                             _fws = 0;
                    }
            }else{
                     _fws = 0;
            }*/
        return _fws;
    }

    public final int getUnSubDirect() {
        int _unsubDirect = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_U_ACADEMIC#
         * <br />#getIndependentStatus()# </CFOUTPUT
    *********
         */
        if (!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") || std.getIndExcloans().equalsIgnoreCase("YES") || std.getStudentAqUnsubLoans().equalsIgnoreCase("no")) {
            _unsubDirect = 0;
        } else if (getIndependentStatus().equalsIgnoreCase("Independent")) {
            if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                _unsubDirect = unsubDirectLoanMaxIndepFr;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")) {
                _unsubDirect = unsubDirectLoanMaxIndepFr2;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")) {
                _unsubDirect = unsubDirectLoanMaxIndepSo;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")) {   //why was FR ?
                _unsubDirect = unsubDirectLoanMaxIndepJr;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
                _unsubDirect = unsubDirectLoanMaxIndepSr;
            } else {
                _unsubDirect = 0;
            }
        } else {
            if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                _unsubDirect = unsubDirectLoanMaxDepFr;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")) {
                _unsubDirect = unsubDirectLoanMaxDepFr2;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")) {
                _unsubDirect = unsubDirectLoanMaxDepSo;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")) {
                _unsubDirect = unsubDirectLoanMaxDepJr;
            } else if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
                _unsubDirect = unsubDirectLoanMaxDepSr;
            } else {
                _unsubDirect = 0;
            }
        }
        int _unsubDirectAmt = _unsubDirect - getSubDirect();
        if (_unsubDirectAmt < 1) {
            _unsubDirectAmt = 0;
        }
        return _unsubDirectAmt;//TRIM(_unsubDirectAmt)
    }

    public final int getChurchBase() {
        int _churchBase = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AU_SCHOLARSHIP1_AMT# </CFOUTPUT
    *********
         */
        _churchBase = Integer.parseInt(SAVE_STUDENT_AU_SCHOLARSHIP1_AMT);// REreplace(SAVE_STUDENT_AU_SCHOLARSHIP1_AMT,"\D","","ALL")
        /*  if(  TRIM(_churchBase).equalsIgnoreCase("){
                     _churchBase = 0
            }*/
        return _churchBase;// round(_churchBase);
    }

    public final int getChurchMatch() {
        int _churchMatch = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AU_SCHOLARSHIP1_AMT# </CFOUTPUT
    *********
         */
        //2013-02: Esther: if get external ed allowence, no church matching
        if (extAllowance > 0) {
            return 0;
        }

        _churchMatch = getChurchBase();
        if (_churchMatch > 1000) {
            _churchMatch = 1000;
        }
        return _churchMatch;
    }

    public final int getPacificCampBase() {
        int _pacificCampBase = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AX_SCHOLARSHIP2_AMT# </CFOUTPUT
    *********
         */
        _pacificCampBase = Integer.parseInt(SAVE_STUDENT_AX_SCHOLARSHIP2_AMT);// REreplace(SAVE_STUDENT_AX_SCHOLARSHIP2_AMT,"\D","","ALL")
        /*     if(  TRIM(_pacificCampBase).equalsIgnoreCase("){
                     _pacificCampBase = 0
            } */
        return _pacificCampBase;//round(_pacificCampBase)
    }

    public final int getPacificCampMatch() {
        int _pacificCampMatch = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_AX_SCHOLARSHIP2_AMT# </CFOUTPUT
    *********
         */
        _pacificCampMatch = getPacificCampBase();
        if (_pacificCampMatch > 2000) {
            _pacificCampMatch = 2000;
        }
        return _pacificCampMatch;
    }

    public final int getNonPacificCampBase() {
        int _nonPacificCampBase = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_BA_SCHOLARSHIP3_AMT# </CFOUTPUT
    *********
         */
        _nonPacificCampBase = Integer.parseInt(SAVE_STUDENT_BA_SCHOLARSHIP3_AMT);// REreplace(SAVE_STUDENT_BA_SCHOLARSHIP3_AMT,"\D","","ALL")
        /*  if(  TRIM(_nonPacificCampBase).equalsIgnoreCase("){
                     _nonPacificCampBase = 0
            }*/
        return _nonPacificCampBase;// round(_nonPacificCampBase)
    }

    public final int getNonPacificCampMatch() {
        int _noPacificCampMatch = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_BA_SCHOLARSHIP3_AMT# </CFOUTPUT
    *********
         */
        _noPacificCampMatch = Math.round(getNonPacificCampBase() / 2);
        if (_noPacificCampMatch > 1500) {
            _noPacificCampMatch = 1500;
        }
        return _noPacificCampMatch;
    }

    public final int getLitEvanBase() {
        int _litEvanBase = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_BD_SCHOLARSHIP4_AMT# </CFOUTPUT
    *********
         */
        _litEvanBase = Integer.parseInt(SAVE_STUDENT_BD_SCHOLARSHIP4_AMT);// REreplace(SAVE_STUDENT_BD_SCHOLARSHIP4_AMT,"\D","","ALL")
        /*   if(  TRIM(_litEvanBase).equalsIgnoreCase("){
                     _litEvanBase = 0
            }*/
        return _litEvanBase;// round(_litEvanBase)
    }

    public final int getLitEvanMatch() {
        int _litEvanMatch = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_BD_SCHOLARSHIP4_AMT# </CFOUTPUT
    *********
         */
        _litEvanMatch = getLitEvanBase();
        if (_litEvanMatch > 3000) {
            _litEvanMatch = 3000;
        }
        return _litEvanMatch;// round(_litEvanMatch)
    }

    public final int enforceCJLimits() {
        int _res = 0;
        int aidid = 0;
        String fundcode = "";
        if (std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
            /*
            * Esther said only these LASU aid can be applied:
            
            * SDA (OSDA)
            * Athletics (OSPORT <=2000)
            * VA yelloe ribbon (OVAYR)
            * year-in-advance 7% (OYRADV)  not the 2% for quarter-in-advance
            * 
            * two CJ-only scholarships
            * CJTS and CJLM, only by Khary Joknson
             */

            lsuAllowance = 0;
            //sdaAward            = 0;
            lsuPerformance = 0;
            lsuNeedGrant = 0;
            //201602
            lasuGrantAmt = 0;

            lsuAchievement = 0;
            nationalMerit = 0;
            lsu4yRenewable = 0;

            familyDiscount = 0;

            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;

            /*    
            scholarship_amt_7   = 0;
            scholarship_amt_8   = 0;
            scholarship_amt_9   = 0;
             */
            aidid = std.getFund7id();
            if (aidid > 0) {
                fundcode = ref.getFundCodeById(aidid);
                if (!fundcode.equals("N/A")) {
                    fundcode = fundcode.substring(0, fundcode.indexOf("|"));
                    if (validAidsCJ.containsKey(fundcode)) {
                        std.setStudentBmScholarship7Amt(0);
                        scholarship_amt_7 = 0;
                    }
                }
            }

            aidid = std.getFund8id();
            if (aidid > 0) {
                fundcode = ref.getFundCodeById(aidid);
                if (!fundcode.equals("N/A")) {
                    fundcode = fundcode.substring(0, fundcode.indexOf("|"));
                    if (validAidsCJ.containsKey(fundcode)) {
                        std.setStudentBpScholarship8Amt(0);
                        scholarship_amt_8 = 0;
                    }
                }
            }

            aidid = std.getFund9id();
            if (aidid > 0) {
                fundcode = ref.getFundCodeById(aidid);
                if (!fundcode.equals("N/A")) {
                    fundcode = fundcode.substring(0, fundcode.indexOf("|"));
                    if (validAidsCJ.containsKey(fundcode)) {
                        std.setStudentBsScholarship9Amt(0);
                        scholarship_amt_9 = 0;
                    }
                }
            }
        }

        return _res;
    }

    public final int enforceLsuLimits() {
        int _lsuSubtotal = 0, _lsuLimit = 0, _lsuLimitSubtotal = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br /> _lsuLimit = #getTuitionAndFees()# - #getFallOrientationFee()#
         * <br />#calGrantA#
         * <br />#calGrantB#
         * <br />#lsuAllowance#
         * <br />#sdaAward#
         * <br />#lsuPerformance#
         * <br />#familyDiscount#
         * <br />#nationalMerit#
         * <br />#churchMatch#
         * <br />#pacificCampMatch#
         * <br />#nonPacificCampMatch#
         * <br />#litEvanMatch# </CFOUTPUT
    *********
         */

        _lsuSubtotal = round(calGrantA
                + calGrantB
                + lsuAllowance
                + sdaAward
                //+ lsuPerformance
                + lsuNeedGrant + lasuGrantAmt
                + lsuAchievement + lsu4yRenewable
                + familyDiscount
                + nationalMerit
                + churchMatch
                + pacificCampMatch
                + nonPacificCampMatch
                + litEvanMatch // ############# other 5 funds
                + scholarship_amt_1
                + scholarship_amt_2
                + scholarship_amt_7
                + scholarship_amt_8
                + scholarship_amt_9
        );

        if (use_need_ind > 0 && (calGrantA > 0 || calGrantB > 1551)) { //what is the magic number 1551????
            _lsuLimit = getTuitionAndFees() - getFallOrientationFee();//+ 1000; //what is the magic number 1000 ???
        } else {
            _lsuLimit = getTuitionAndFees() - getFallOrientationFee();
        }
        _lsuLimitSubtotal = 0;

        if (use_need_ind > 0) {
            _lsuLimitSubtotal = _lsuLimitSubtotal + calGrantA;
            if (_lsuLimitSubtotal > _lsuLimit) {
                //                 log.info("....enforceLsuLimits() set sdaAward to 0 ...since _lsuLimitSubtotal  > _lsuLimit after sum calGrantA");
                calGrantA = _lsuLimit;
                calGrantB = 0;
                lsuAllowance = 0;
                sdaAward = 0;
                //lsuPerformance      = 0;
                lsuNeedGrant = 0;
                //201602
                lasuGrantAmt = 0;

                lsuAchievement = 0;
                lsu4yRenewable = 0;

                familyDiscount = 0;
                nationalMerit = 0;
                churchMatch = 0;
                pacificCampMatch = 0;
                nonPacificCampMatch = 0;
                litEvanMatch = 0;
                scholarship_amt_1 = 0;
                scholarship_amt_2 = 0;
                scholarship_amt_7 = 0;
                scholarship_amt_8 = 0;
                scholarship_amt_9 = 0;

                return _lsuSubtotal;
            }

            _lsuLimitSubtotal = _lsuLimitSubtotal + calGrantB;
            if (_lsuLimitSubtotal > _lsuLimit) {
                //              log.info("....enforceLsuLimits() set sdaAward to 0 ...since _lsuLimitSubtotal  > _lsuLimit after sum calGrantB");
                calGrantB = calGrantB - (_lsuLimitSubtotal - _lsuLimit);
                lsuAllowance = 0;
                sdaAward = 0;
                //lsuPerformance      = 0;
                lsuNeedGrant = 0;
                //201602
                lasuGrantAmt = 0;

                lsuAchievement = 0;
                lsu4yRenewable = 0;

                familyDiscount = 0;
                nationalMerit = 0;
                churchMatch = 0;
                pacificCampMatch = 0;
                nonPacificCampMatch = 0;
                litEvanMatch = 0;
                scholarship_amt_1 = 0;
                scholarship_amt_2 = 0;
                scholarship_amt_7 = 0;
                scholarship_amt_8 = 0;
                scholarship_amt_9 = 0;
                return _lsuSubtotal;
            }
        } else {
            calGrantA = 0;
            calGrantB = 0;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + lsuAllowance;
        if (_lsuLimitSubtotal > _lsuLimit) {
            //               log.info("....enforceLsuLimits() set sdaAward to 0 ...since _lsuLimitSubtotal  > _lsuLimit after sum lsuAllowance");
            lsuAllowance = lsuAllowance - (_lsuLimitSubtotal - _lsuLimit);
            sdaAward = 0;
            //lsuPerformance      = 0;
            lsuNeedGrant = 0;
            //201602
            lasuGrantAmt = 0;

            lsuAchievement = 0;
            lsu4yRenewable = 0;

            familyDiscount = 0;
            nationalMerit = 0;
            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + sdaAward;
        if (_lsuLimitSubtotal > _lsuLimit) {
//                log.info("....enforceLsuLimits() to change sdaAward (%d)...since _lsuLimitSubtotal  > _lsuLimit after sum sdaAward", sdaAward);
            sdaAward = sdaAward - (_lsuLimitSubtotal - _lsuLimit);
            //                    log.info("....enforceLsuLimits() set sdaAward =%d ...since _lsuLimitSubtotal  > _lsuLimit after sum sdaAward", sdaAward);
            //lsuPerformance      = 0;
            lsuNeedGrant = 0;
            //201602
            lasuGrantAmt = 0;

            lsuAchievement = 0;
            lsu4yRenewable = 0;

            familyDiscount = 0;
            nationalMerit = 0;
            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        if (use_need_ind > 0) {
            _lsuLimitSubtotal = _lsuLimitSubtotal + lsuNeedGrant;
            if (_lsuLimitSubtotal > _lsuLimit) {
                lsuNeedGrant = lsuNeedGrant - (_lsuLimitSubtotal - _lsuLimit);

                //201602
                lasuGrantAmt = 0;

                lsuAchievement = 0;
                lsu4yRenewable = 0;

                familyDiscount = 0;
                nationalMerit = 0;
                churchMatch = 0;
                pacificCampMatch = 0;
                nonPacificCampMatch = 0;
                litEvanMatch = 0;
                scholarship_amt_1 = 0;
                scholarship_amt_2 = 0;
                scholarship_amt_7 = 0;
                scholarship_amt_8 = 0;
                scholarship_amt_9 = 0;
                return _lsuSubtotal;
            }
        } else {
            lsuNeedGrant = 0;
        }

        //201602
        //lasuGrantAmt
        if (use_need_ind > 0) {
            _lsuLimitSubtotal = _lsuLimitSubtotal + lasuGrantAmt;
            if (_lsuLimitSubtotal > _lsuLimit) {
                lasuGrantAmt = lasuGrantAmt - (_lsuLimitSubtotal - _lsuLimit);

                lsuAchievement = 0;
                lsu4yRenewable = 0;

                familyDiscount = 0;
                nationalMerit = 0;
                churchMatch = 0;
                pacificCampMatch = 0;
                nonPacificCampMatch = 0;
                litEvanMatch = 0;
                scholarship_amt_1 = 0;
                scholarship_amt_2 = 0;
                scholarship_amt_7 = 0;
                scholarship_amt_8 = 0;
                scholarship_amt_9 = 0;
                return _lsuSubtotal;
            }
        } else {
            lasuGrantAmt = 0;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + familyDiscount;
        if (_lsuLimitSubtotal > _lsuLimit) {
            familyDiscount = familyDiscount - (_lsuLimitSubtotal - _lsuLimit);
            nationalMerit = 0;

            lsuAchievement = 0;
            lsu4yRenewable = 0;

            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + nationalMerit;
        if (_lsuLimitSubtotal > _lsuLimit) {
            nationalMerit = nationalMerit - (_lsuLimitSubtotal - _lsuLimit);

            lsuAchievement = 0;
            lsu4yRenewable = 0;

            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + lsuAchievement;
        if (_lsuLimitSubtotal > _lsuLimit) {
            lsuAchievement = lsuAchievement - (_lsuLimitSubtotal - _lsuLimit);

            lsu4yRenewable = 0;

            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }
        _lsuLimitSubtotal = _lsuLimitSubtotal + lsu4yRenewable;
        if (_lsuLimitSubtotal > _lsuLimit) {
            lsu4yRenewable = lsu4yRenewable - (_lsuLimitSubtotal - _lsuLimit);

            churchMatch = 0;
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + churchMatch;
        if (_lsuLimitSubtotal > _lsuLimit) {
            churchMatch = churchMatch - (_lsuLimitSubtotal - _lsuLimit);
            pacificCampMatch = 0;
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + pacificCampMatch;
        if (_lsuLimitSubtotal > _lsuLimit) {
            pacificCampMatch = pacificCampMatch - (_lsuLimitSubtotal - _lsuLimit);
            /*
                     pacificCampMatch    = _lsuLimit         
                                               - lsuAllowance 
                                               - sdaAward 
                                               - lsuPerformance 
                                               - familyDiscount 
                                               - nationalMerit
                                               - churchMatch; */
            nonPacificCampMatch = 0;
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + nonPacificCampMatch;
        if (_lsuLimitSubtotal > _lsuLimit) {
            nonPacificCampMatch = nonPacificCampMatch - (_lsuLimitSubtotal - _lsuLimit);
            litEvanMatch = 0;
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + litEvanMatch;
        if (_lsuLimitSubtotal > _lsuLimit) {
            litEvanMatch = litEvanMatch - (_lsuLimitSubtotal - _lsuLimit);
            scholarship_amt_1 = 0;
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
            return _lsuSubtotal;
        }

//################################################ count other 5 possible funds ???      
        _lsuLimitSubtotal = _lsuLimitSubtotal + scholarship_amt_1;
        if (_lsuLimitSubtotal > _lsuLimit) {
            scholarship_amt_1 = scholarship_amt_1 - (_lsuLimitSubtotal - _lsuLimit);
            if (scholarship_amt_1 < 0) {
                scholarship_amt_1 = 0;
            }
            scholarship_amt_2 = 0;
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + scholarship_amt_2;
        if (_lsuLimitSubtotal > _lsuLimit) {
            scholarship_amt_2 = scholarship_amt_2 - (_lsuLimitSubtotal - _lsuLimit);
            if (scholarship_amt_2 < 0) {
                scholarship_amt_2 = 0;
            }
            scholarship_amt_7 = 0;
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + scholarship_amt_7;
        if (_lsuLimitSubtotal > _lsuLimit) {
            scholarship_amt_7 = scholarship_amt_7 - (_lsuLimitSubtotal - _lsuLimit);
            if (scholarship_amt_7 < 0) {
                scholarship_amt_7 = 0;
            }
            scholarship_amt_8 = 0;
            scholarship_amt_9 = 0;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + scholarship_amt_8;
        if (_lsuLimitSubtotal > _lsuLimit) {
            scholarship_amt_8 = scholarship_amt_8 - (_lsuLimitSubtotal - _lsuLimit);
            if (scholarship_amt_8 < 0) {
                scholarship_amt_8 = 0;
            }
            scholarship_amt_9 = 0;
        }

        _lsuLimitSubtotal = _lsuLimitSubtotal + scholarship_amt_9;
        if (_lsuLimitSubtotal > _lsuLimit) {
            scholarship_amt_9 = scholarship_amt_9 - (_lsuLimitSubtotal - _lsuLimit);
            if (scholarship_amt_9 < 0) {
                scholarship_amt_9 = 0;
            }
        }

        return _lsuLimitSubtotal;
    }

    public final boolean enforceOverallLimits() {//only use EFC 100% or 0%
        int _lsuOverallLimit = 0, _lsuOverallSubtotal = 0;
        boolean _reduceRemaining = false;
        int _efcAmt = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />Pell: #pellGrant#
         * <br />CalGrantA: #calGrantA#
         * <br />CalGrantB: #calGrantB#
         * <br />FSEOG: #fseogAmt#
         * <br />External %: #extAllowance#
         * <br />Non CA State Grant: #nonCaGrantAmt#
         * <br />Ouside Subsidy: #outsideAmt#
         * <br />Church: #churchBase#
         * <br />LSU Allowance: #lsuAllowance#
         * <br />SDA Membership: #sdaAward#
         * <br />LSU Performance: #lsuPerformance#
         * <br />Family Disc: #familyDiscount#
         * <br />National Merit: #nationalMerit#
         * <br />Church Match: #churchMatch#
         * <br />Pacific Camp Match: #pacificCampMatch#
         * <br />Non-Pacific Camp Match: #nonPacificCampMatch#
         * <br />Lit Evan Match: #litEvanMatch#
         * <br />SubDirect: #subDirect#
         * <br />Perkins: #perkinsLoan#
         * <br />UnsubDirect: #unsubDirect#
         * <br />Need Amt: #getCOA()# </CFOUTPUT
    *********
         */
        _lsuOverallLimit = getMPA();//getCOA();
        //            log.info("--------- enforceOverallLimits() init _lsuOverallLimit to MPA==%d, while need_ind=%d, FAFSA=%s", _lsuOverallLimit, use_need_ind, std.getStudentXFafsa());
        /*
             if( use_need_ind < 1 && std.getStudentXFafsa().equalsIgnoreCase("YES")){ //no use of need-based-aid, so EFC should not be a limit
                 _lsuOverallLimit += getEFC();
                 log.info("--------- enforceOverallLimits() adjust _lsuOverallLimit to %d", _lsuOverallLimit);                 
             }*/

        if (_lsuOverallLimit <= 0.01) {
            _lsuOverallLimit = 0;
        }

        _lsuOverallSubtotal = pellGrant
                + calGrantA
                + calGrantB
                + fseogAmt
                + extAllowance
                + nonCaGrantAmt
                + outsideAmt
                + churchBase
                //====================== use external aid first, then internal aid==================

                + lsuAllowance
                + sdaAward
                //+ lsuPerformance
                + lsuNeedGrant + lasuGrantAmt
                + lsuAchievement + lsu4yRenewable
                + familyDiscount
                + nationalMerit
                + churchMatch
                + pacificCampMatch
                + nonPacificCampMatch
                + litEvanMatch //############################### other 5 funds ????  it is 17th,  18th is the next one
                + scholarship_amt_1
                + scholarship_amt_2
                + scholarship_amt_7
                + scholarship_amt_8
                + scholarship_amt_9
                + subDirect // it was 18th, but new order is 23rd
                + perkinsLoan
                + fwsAmount
                + unsubDirect;

        /**
         * ********  <h1>LSU Overall Limit: #_lsuOverallLimit#</h1>*********
         */
        /**
         * ********  <h1>LSU Overall Subtotal: #_lsuOverallSubtotal#</h1>*********
         */
        boolean reducedAid = false;
        //int  _lsuOverallLimitCheck = pellGrant;
        int _lsuOverallLimitCheck = 0;

        if (use_need_ind > 0) {
//         log.info("--------- enforceOverallLimits() counts in gov aid since use_need_ind>0");

            _lsuOverallLimitCheck = pellGrant;

            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                pellGrant = pellGrant - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (pellGrant < 0) {
                    pellGrant = 0;
                }
                _reduceRemaining = reduceAid(2);
                return true;
            }

            _lsuOverallLimitCheck = _lsuOverallLimitCheck + calGrantA;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                calGrantA = calGrantA - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (calGrantA < 0) {
                    calGrantA = 0;
                }
                _reduceRemaining = reduceAid(3);
                return true;
            }

            _lsuOverallLimitCheck = _lsuOverallLimitCheck + calGrantB;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                calGrantB = calGrantB - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (calGrantB < 0) {
                    calGrantB = 0;
                }
                _reduceRemaining = reduceAid(4);
                return true;
            }
            _lsuOverallLimitCheck = _lsuOverallLimitCheck + fseogAmt;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                fseogAmt = fseogAmt - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (fseogAmt < 0) {
                    fseogAmt = 0;
                }
                _reduceRemaining = reduceAid(5);
                return true;
            }
        } else {
//       log.info("--------- enforceOverallLimits() zeros   gov aid since use_need_ind <=0");
            pellGrant = 0;
            calGrantA = 0;
            calGrantB = 0;
            fseogAmt = 0;
        }

        _lsuOverallLimitCheck = _lsuOverallLimitCheck + extAllowance;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            extAllowance = extAllowance - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (extAllowance < 0) {
                extAllowance = 0;
            }
            _reduceRemaining = reduceAid(6);
            return true;
        }

        if (use_need_ind > 0) {
            //       log.info("--------- enforceOverallLimits() counts in non-ca grant since use_need_ind>0");
            _lsuOverallLimitCheck = _lsuOverallLimitCheck + nonCaGrantAmt;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                nonCaGrantAmt = nonCaGrantAmt - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (nonCaGrantAmt < 0) {
                    nonCaGrantAmt = 0;
                }
                _reduceRemaining = reduceAid(7);
                return true;
            }
        } else {
            //       log.info("--------- enforceOverallLimits() zeros non-ca grant since use_need_ind <= 0");
            nonCaGrantAmt = 0;
        }

        _lsuOverallLimitCheck = _lsuOverallLimitCheck + outsideAmt;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            outsideAmt = outsideAmt - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (outsideAmt < 0) {
                outsideAmt = 0;
            }
            _reduceRemaining = reduceAid(8);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + churchBase;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            churchBase = churchBase - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (churchBase < 0) {
                churchBase = 0;
            }
            _reduceRemaining = reduceAid(9);
            return true;
        }

        //========================== external ends. ======================================
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + lsuAllowance;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            lsuAllowance = lsuAllowance - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (lsuAllowance < 0) {
                lsuAllowance = 0;
            }
            _reduceRemaining = reduceAid(10);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + sdaAward;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            //                  log.info("...enforceOverallLimits() will recalc sadAward(%d) since _lsuOverallLimitCheck (%d)  > _lsuOverallLimit(%d)", sdaAward, _lsuOverallLimitCheck, _lsuOverallLimit) ;
            sdaAward = sdaAward - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (sdaAward < 0) {
                sdaAward = 0;
            }
            //                    log.info("...enforceOverallLimits() set sdaAward=%d", sdaAward);
            _reduceRemaining = reduceAid(11);
            return true;
        }

        /*
            _lsuOverallLimitCheck = _lsuOverallLimitCheck + lsuPerformance;
            if(  _lsuOverallLimitCheck  > _lsuOverallLimit ){
                     lsuPerformance = lsuPerformance - (_lsuOverallLimitCheck - _lsuOverallLimit);
                     _reduceRemaining = reduceAid(12);
                    return  true;
            } */
        if (use_need_ind > 0) {
            //       log.info("--------- enforceOverallLimits() counts in lasu_need_grant since use_need_ind>0");

            _lsuOverallLimitCheck = _lsuOverallLimitCheck + lsuNeedGrant;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                lsuNeedGrant = lsuNeedGrant - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (lsuNeedGrant < 0) {
                    lsuNeedGrant = 0;
                }
                _reduceRemaining = reduceAid(12);
                return true;
            }
        } else {
//        log.info("--------- enforceOverallLimits() zeros lasu_need_grant since use_need_ind <= 0");
            lsuNeedGrant = 0;
        }

        //201602 lasuGrantAmt
        if (use_need_ind > 0) {
            //       log.info("--------- enforceOverallLimits() counts in lasuGrantAmt since use_need_ind>0");

            _lsuOverallLimitCheck = _lsuOverallLimitCheck + lasuGrantAmt;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                lasuGrantAmt = lasuGrantAmt - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (lasuGrantAmt < 0) {
                    lasuGrantAmt = 0;
                }
                _reduceRemaining = reduceAid(13);
                return true;
            }
        } else {
//        log.info("--------- enforceOverallLimits() zeros lasu_need_grant since use_need_ind <= 0");
            lasuGrantAmt = 0;
        }

        _lsuOverallLimitCheck = _lsuOverallLimitCheck + familyDiscount;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            familyDiscount = familyDiscount - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (familyDiscount < 0) {
                familyDiscount = 0;
            }
            _reduceRemaining = reduceAid(14);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + nationalMerit;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            nationalMerit = nationalMerit - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (nationalMerit < 0) {
                nationalMerit = 0;
            }
            _reduceRemaining = reduceAid(15);
            return true;
        }

        _lsuOverallLimitCheck = _lsuOverallLimitCheck + lsuAchievement;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            lsuAchievement = lsuAchievement - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (lsuAchievement < 0) {
                lsuAchievement = 0;
            }
            _reduceRemaining = reduceAid(16);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + lsu4yRenewable;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            lsu4yRenewable = lsu4yRenewable - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (lsu4yRenewable < 0) {
                lsu4yRenewable = 0;
            }
            _reduceRemaining = reduceAid(17);
            return true;
        }

        _lsuOverallLimitCheck = _lsuOverallLimitCheck + churchMatch;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            churchMatch = churchMatch - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (churchMatch < 0) {
                churchMatch = 0;
            }
            _reduceRemaining = reduceAid(18);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + pacificCampMatch;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            pacificCampMatch = pacificCampMatch - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (pacificCampMatch < 0) {
                pacificCampMatch = 0;
            }
            _reduceRemaining = reduceAid(19);
            return true;
        }
        //   <CFOUTPUT
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + nonPacificCampMatch;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            nonPacificCampMatch = nonPacificCampMatch - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (nonPacificCampMatch < 0) {
                nonPacificCampMatch = 0;
            }
            _reduceRemaining = reduceAid(20);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + litEvanMatch;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            litEvanMatch = litEvanMatch - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (litEvanMatch < 0) {
                litEvanMatch = 0;
            }
            _reduceRemaining = reduceAid(21);
            return true;
        }

        //################### other 5 funds ###########################
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + scholarship_amt_1;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            scholarship_amt_1 = scholarship_amt_1 - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (scholarship_amt_1 < 0) {
                scholarship_amt_1 = 0;
            }
            _reduceRemaining = reduceAid(22);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + scholarship_amt_2;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            scholarship_amt_2 = scholarship_amt_2 - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (scholarship_amt_2 < 0) {
                scholarship_amt_2 = 0;
            }
            _reduceRemaining = reduceAid(23);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + scholarship_amt_7;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            scholarship_amt_7 = scholarship_amt_7 - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (scholarship_amt_7 < 0) {
                scholarship_amt_7 = 0;
            }
            _reduceRemaining = reduceAid(24);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + scholarship_amt_8;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            scholarship_amt_8 = scholarship_amt_8 - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (scholarship_amt_8 < 0) {
                scholarship_amt_8 = 0;
            }
            _reduceRemaining = reduceAid(25);
            return true;
        }
        _lsuOverallLimitCheck = _lsuOverallLimitCheck + scholarship_amt_9;
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            scholarship_amt_9 = scholarship_amt_9 - (_lsuOverallLimitCheck - _lsuOverallLimit);
            if (scholarship_amt_9 < 0) {
                scholarship_amt_9 = 0;
            }
            _reduceRemaining = reduceAid(26);
            return true;
        }

        if (use_need_ind > 0) {
//      log.info("--------- enforceOverallLimits() counts in sub_load, perkins, and fws since use_need_ind>0");
            _lsuOverallLimitCheck = _lsuOverallLimitCheck + subDirect;

            System.out.println("enforceOverallLimits() got subDirect=" + subDirect);

            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                subDirect = subDirect - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (subDirect < 0) {
                    subDirect = 0;
                }

                System.out.println("enforceOverallLimits() max subDirect=" + subDirect);

                _reduceRemaining = reduceAid(27); //19
                return true;
            }
            _lsuOverallLimitCheck = _lsuOverallLimitCheck + perkinsLoan;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                perkinsLoan = perkinsLoan - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (perkinsLoan < 0) {
                    perkinsLoan = 0;
                }
                _reduceRemaining = reduceAid(28); //20
                return true;
            }
            _lsuOverallLimitCheck = _lsuOverallLimitCheck + fwsAmount;
            if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                fwsAmount = fwsAmount - (_lsuOverallLimitCheck - _lsuOverallLimit);
                if (fwsAmount < 0) {
                    fwsAmount = 0;
                }
                _reduceRemaining = reduceAid(29); //21
                return true;
            }
        } else {
            //     log.info("--------- enforceOverallLimits() zeros sub_load, perkins, and fws since use_need_ind <= 0");
            subDirect = 0;
            perkinsLoan = 0;
            fwsAmount = 0;
        }

        _lsuOverallLimitCheck = _lsuOverallLimitCheck + unsubDirect;

        //System.out.println("enforceOverallLimits() got unsubDirect="+unsubDirect+", while subdirect="+subDirect);
        if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            int _efcAmount = getEFC();
            //                  if(use_need_ind<1)_efcAmount=0;

            if (reducedAid == false) {
                ////// ????????????????????????????? nobody changes it
                            //if(  TRIM(_efcAmount).!equalsIgnoreCase("){
                                     unsubDirect = unsubDirect - (_lsuOverallLimitCheck - _lsuOverallLimit - _efcAmount); //                                                                           
                //System.out.println("enforceOverallLimits() first adjust unsubDirect="+unsubDirect);             
                //}else{
                //         unsubDirect = unsubDirect - (_lsuOverallLimitCheck - _lsuOverallLimit);
                //}
            } else {
                // if(  !TRIM(_efcAmount).equalsIgnoreCase("")){
                if (_efcAmount > 0) {
                    if (unsubDirect > _efcAmount) {
                        unsubDirect = _efcAmount;
                        //System.out.println("enforceOverallLimits() adjust unsubDirect="+unsubDirect+" since old val >=EFC "+_efcAmount);
                    }
                }
            }
            if (unsubDirect > getUnSubDirect()) {
                unsubDirect = getUnSubDirect();
                //System.out.println("enforceOverallLimits() last adjust unsubDirect="+unsubDirect+" since old val > getUnsubDirect()");
            }
            if (unsubDirect < 0) {
                unsubDirect = 0;
            }
        }
        //  </CFOUTPUT
        return true;//_lsuOverallSubtotal;
    }

    public final boolean reduceAid_new(String awdstr, String[] awds) {//int _clearVar){  //called by overalllimit()
        //<CFARGUMENT NAME="_clearVar" REQUIRED="true){
        boolean reducedAid = true;

        int _clearVar = Arrays.asList(awds).indexOf(awdstr) + 1;
        if (_clearVar <= 0) {
            return false;
        }

        switch (_clearVar) {
            case 1:
                pellGrant = 0;
            case 2:
                calGrantA = 0;
            case 3:
                calGrantB = 0;
            case 4:
                fseogAmt = 0;
            case 5:
                extAllowance = 0;
            case 6:
                nonCaGrantAmt = 0;
            case 7:
                outsideAmt = 0;
            case 8:
                churchBase = 0;
            case 9:
                lsuAllowance = 0;
            case 10:
                sdaAward = 0;
            case 11:
                lsuNeedGrant = 0;
            case 12:
                lasuGrantAmt = 0;

            case 13:
                familyDiscount = 0;
            case 14:
                nationalMerit = 0;
            case 15:
                lsuAchievement = 0;
            case 16:
                lsu4yRenewable = 0;
            case 17:
                churchMatch = 0;
            case 18:
                pacificCampMatch = 0;
            case 19:
                nonPacificCampMatch = 0;
            case 20:
                litEvanMatch = 0;
            case 21:
                scholarship_amt_1 = 0;
            case 22:
                scholarship_amt_2 = 0;
            case 23:
                scholarship_amt_7 = 0;
            case 24:
                scholarship_amt_8 = 0;
            case 25:
                scholarship_amt_9 = 0;
            case 26:
                subDirect = 0;
            case 27:
                perkinsLoan = 0;
            case 28:
                fwsAmount = 0;
            case 29:
                int _efcAmount = getEFC();
                if (_efcAmount > 0) {
                    if (unsubDirect > _efcAmount) {
                        unsubDirect = _efcAmount;
                    } else {
                        unsubDirect = unsubDirect + _efcAmount;
                    }
                }
        }
        if (unsubDirect > getUnSubDirect()) {
            unsubDirect = getUnSubDirect();
        }
        return true;
    }

    public final boolean reduceAid(int _clearVar) {  //called by overalllimit()
        //<CFARGUMENT NAME="_clearVar" REQUIRED="true){
        boolean reducedAid = true;
        if (_clearVar <= 1) {
            pellGrant = 0;
        }
        if (_clearVar <= 2) {
            calGrantA = 0;
        }
        if (_clearVar <= 3) {
            calGrantB = 0;
        }
        if (_clearVar <= 4) {
            fseogAmt = 0;
        }
        if (_clearVar <= 5) {
            extAllowance = 0;
        }
        if (_clearVar <= 6) {
            nonCaGrantAmt = 0;
        }
        if (_clearVar <= 7) {
            outsideAmt = 0;
        }
        if (_clearVar <= 8) {
            churchBase = 0;
        }
        if (_clearVar <= 9) {
            lsuAllowance = 0;
        }
        if (_clearVar <= 10) {
            //               log.info(" .... reduceAid(%d) set sdaAward to 0 since _clearVar <= 10 ");
            sdaAward = 0;
        }

        if (_clearVar <= 11) {
            lsuNeedGrant = 0;
        }

        if (_clearVar <= 12) {
            lasuGrantAmt = 0;
        }

        if (_clearVar <= 13) {
            familyDiscount = 0;
        }
        if (_clearVar <= 14) {
            nationalMerit = 0;
        }

        //added achievement  4y-renewable . so all followings' order number will +2
        if (_clearVar <= 15) {
            lsuAchievement = 0;
        }
        if (_clearVar <= 16) {
            lsu4yRenewable = 0;
        }

        if (_clearVar <= 17) {
            churchMatch = 0;
        }
        if (_clearVar <= 18) {
            pacificCampMatch = 0;
        }
        if (_clearVar <= 19) {
            nonPacificCampMatch = 0;
        }
        if (_clearVar <= 20) {
            litEvanMatch = 0;
        }
        //############# the other 5 funds #####################
        if (_clearVar <= 21) {
            //subDirect = 0;
            scholarship_amt_1 = 0;
        }
        if (_clearVar <= 22) {
            scholarship_amt_2 = 0;
        }
        if (_clearVar <= 23) {
            scholarship_amt_7 = 0;
        }
        if (_clearVar <= 24) {
            scholarship_amt_8 = 0;
        }
        if (_clearVar <= 25) {
            scholarship_amt_9 = 0;
        }

        if (_clearVar <= 26) {//18                    
            //System.out.println("reduceAid() reduces subDirect to zero from "+subDirect);
            subDirect = 0;
        }
        if (_clearVar <= 27) {  //19
            perkinsLoan = 0;
        }
        if (_clearVar <= 28) { //20
            fwsAmount = 0;
        }
        if (_clearVar <= 29) { //21
            int _efcAmount = getEFC();
//            if(use_need_ind<1)_efcAmount=0;

            //if(  TRIM(_efcAmount).!equalsIgnoreCase("")){
            if (_efcAmount > 0) {
                if (unsubDirect > _efcAmount) {
                    //System.out.println("reduceAid() reduces unsubDirect to efc="+_efcAmount+" from "+subDirect);
                    unsubDirect = _efcAmount;
                } else {
                    unsubDirect = unsubDirect + _efcAmount;
                    //System.out.println("reduceAid() enlarge unsubDirect with efc="+_efcAmount+", now== "+subDirect);
                }
            }
        }
        if (unsubDirect > getUnSubDirect()) {
            //System.out.println("reduceAid() reduces unsubDirect from "+subDirect+" to getUnSubDirect()== "+getUnSubDirect());
            unsubDirect = getUnSubDirect();
        }
        return true;
    }

    //################### added to allow JSF modify SAVE_xxxx from back bean or event lisenter. otherwise, those SAVE_xxx is only set once (either initilization or  setStd() - refreshStud() )
    //################## the HSF program should also set stud obj, and here invoke set_SAVExxx(), incase the copied_and_mod couldfusion program use both
    public void setSAVE_STUDENT_L_INTL_STUD(String newStrVal) {
        SAVE_STUDENT_L_INTL_STUD = newStrVal;
    }

    /*
    public void setSAVE_STUDENT_X_FAFSA(String newStrVal){
        SAVE_STUDENT_X_FAFSA = newStrVal;
    }
    
    public void setSAVE_STUDENT_Z_CALGRANT(String newStrVal){
        SAVE_STUDENT_Z_CALGRANT = newStrVal;        
    }*/

    public void setSAVE_STUDENT_M_MARRY(String SAVE_STUDENT_M_MARRY) {
        this.SAVE_STUDENT_M_MARRY = SAVE_STUDENT_M_MARRY;
    }

    public void setSAVE_STUDENT_N_SDA(String SAVE_STUDENT_N_SDA) {
        this.SAVE_STUDENT_N_SDA = SAVE_STUDENT_N_SDA;
    }

    public void setSAVE_STUDENT_P_GPA(String SAVE_STUDENT_P_GPA) {
        this.SAVE_STUDENT_P_GPA = SAVE_STUDENT_P_GPA;
    }

    public void setSAVE_STUDENT_Q_SAT(String SAVE_STUDENT_Q_SAT) {
        this.SAVE_STUDENT_Q_SAT = SAVE_STUDENT_Q_SAT;
    }

    public void setSAVE_STUDENT_Q_SAT_V(String SAVE_STUDENT_Q_SAT_V) {
        this.SAVE_STUDENT_Q_SAT_V = SAVE_STUDENT_Q_SAT_V;
    }

    public void setSAVE_STUDENT_R_ACT(String SAVE_STUDENT_R_ACT) {
        this.SAVE_STUDENT_R_ACT = SAVE_STUDENT_R_ACT;
    }

    public void setSAVE_STUDENT_S_MERIT(String SAVE_STUDENT_S_MERIT) {
        this.SAVE_STUDENT_S_MERIT = SAVE_STUDENT_S_MERIT;
    }

    public void setSAVE_STUDENT_U_ACADEMIC(String SAVE_STUDENT_U_ACADEMIC) {
        this.SAVE_STUDENT_U_ACADEMIC = SAVE_STUDENT_U_ACADEMIC;
    }

    public void setSAVE_STUDENT_V_FAMILY(String SAVE_STUDENT_V_FAMILY) {
        this.SAVE_STUDENT_V_FAMILY = SAVE_STUDENT_V_FAMILY;
    }

    public void setSAVE_STUDENT_W_DORM(String SAVE_STUDENT_W_DORM) {
        this.SAVE_STUDENT_W_DORM = SAVE_STUDENT_W_DORM;
    }

    public void setSAVE_STUDENT_X_FAFSA(String SAVE_STUDENT_X_FAFSA) {
        this.SAVE_STUDENT_X_FAFSA = SAVE_STUDENT_X_FAFSA;
    }

    public void setSAVE_STUDENT_Y_INDEPT(String SAVE_STUDENT_Y_INDEPT) {
        this.SAVE_STUDENT_Y_INDEPT = SAVE_STUDENT_Y_INDEPT;
    }

    public void setSAVE_STUDENT_Z_CALGRANT(String SAVE_STUDENT_Z_CALGRANT) {
        this.SAVE_STUDENT_Z_CALGRANT = SAVE_STUDENT_Z_CALGRANT;
    }

    public void setSAVE_STUDENT_AA_CALGRANT_A(String SAVE_STUDENT_AA_CALGRANT_A) {
        this.SAVE_STUDENT_AA_CALGRANT_A = SAVE_STUDENT_AA_CALGRANT_A;
    }

    public void setSAVE_STUDENT_AB_CALGRANT_B(String SAVE_STUDENT_AB_CALGRANT_B) {
        this.SAVE_STUDENT_AB_CALGRANT_B = SAVE_STUDENT_AB_CALGRANT_B;
    }

    public void setSAVE_STUDENT_AC_FAMILY_SIZE(String SAVE_STUDENT_AC_FAMILY_SIZE) {
        this.SAVE_STUDENT_AC_FAMILY_SIZE = SAVE_STUDENT_AC_FAMILY_SIZE;
    }

    public void setSAVE_STUDENT_AD_FAMILY_INCOME(String SAVE_STUDENT_AD_FAMILY_INCOME) {
        this.SAVE_STUDENT_AD_FAMILY_INCOME = SAVE_STUDENT_AD_FAMILY_INCOME;
    }

    public void setSAVE_STUDENT_AE_FAMILY_ASSET(String SAVE_STUDENT_AE_FAMILY_ASSET) {
        this.SAVE_STUDENT_AE_FAMILY_ASSET = SAVE_STUDENT_AE_FAMILY_ASSET;
    }

    public void setSAVE_STUDENT_AF_FAMILY_CONTRIB(String SAVE_STUDENT_AF_FAMILY_CONTRIB) {
        this.SAVE_STUDENT_AF_FAMILY_CONTRIB = SAVE_STUDENT_AF_FAMILY_CONTRIB;
    }

    public void setSAVE_STUDENT_AG_NONLSU_ALLOWRANCE(String SAVE_STUDENT_AG_NONLSU_ALLOWRANCE) {
        this.SAVE_STUDENT_AG_NONLSU_ALLOWRANCE = SAVE_STUDENT_AG_NONLSU_ALLOWRANCE;
    }

    public void setSAVE_STUDENT_AH_LSU_ALLOWRANCE(String SAVE_STUDENT_AH_LSU_ALLOWRANCE) {
        this.SAVE_STUDENT_AH_LSU_ALLOWRANCE = SAVE_STUDENT_AH_LSU_ALLOWRANCE;
    }

    public void setSAVE_STUDENT_AI_EDU_ALLOW_PER(String SAVE_STUDENT_AI_EDU_ALLOW_PER) {
        this.SAVE_STUDENT_AI_EDU_ALLOW_PER = SAVE_STUDENT_AI_EDU_ALLOW_PER;
    }

    public void setSAVE_STUDENT_AJ_HOME_STATE(String SAVE_STUDENT_AJ_HOME_STATE) {
        this.SAVE_STUDENT_AJ_HOME_STATE = SAVE_STUDENT_AJ_HOME_STATE;
    }

    public void setSAVE_STUDENT_AK_NONCAL_GRANT(String SAVE_STUDENT_AK_NONCAL_GRANT) {
        this.SAVE_STUDENT_AK_NONCAL_GRANT = SAVE_STUDENT_AK_NONCAL_GRANT;
    }

    public void setSAVE_STUDENT_AL_OUT_SCHOLARSHIPS(String SAVE_STUDENT_AL_OUT_SCHOLARSHIPS) {
        this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS = SAVE_STUDENT_AL_OUT_SCHOLARSHIPS;
    }

    public void setSAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT(String SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT) {
        this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT = SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT;
    }

    public void setSAVE_STUDENT_AQ_UNSUB_LOANS(String SAVE_STUDENT_AQ_UNSUB_LOANS) {
        this.SAVE_STUDENT_AQ_UNSUB_LOANS = SAVE_STUDENT_AQ_UNSUB_LOANS;
    }

    public void setSAVE_STUDENT_AR_FWS(String SAVE_STUDENT_AR_FWS) {
        this.SAVE_STUDENT_AR_FWS = SAVE_STUDENT_AR_FWS;
    }

    public void setSAVE_STUDENT_AU_SCHOLARSHIP1_AMT(String SAVE_STUDENT_AU_SCHOLARSHIP1_AMT) {
        this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT = SAVE_STUDENT_AU_SCHOLARSHIP1_AMT;
    }

    public void setSAVE_STUDENT_AX_SCHOLARSHIP2_AMT(String SAVE_STUDENT_AX_SCHOLARSHIP2_AMT) {
        this.SAVE_STUDENT_AX_SCHOLARSHIP2_AMT = SAVE_STUDENT_AX_SCHOLARSHIP2_AMT;
    }

    public void setSAVE_STUDENT_BA_SCHOLARSHIP3_AMT(String SAVE_STUDENT_BA_SCHOLARSHIP3_AMT) {
        this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT = SAVE_STUDENT_BA_SCHOLARSHIP3_AMT;
    }

    public void setSAVE_STUDENT_BD_SCHOLARSHIP4_AMT(String SAVE_STUDENT_BD_SCHOLARSHIP4_AMT) {
        this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT = SAVE_STUDENT_BD_SCHOLARSHIP4_AMT;
    }

    public void setSdaAward(int sdaAward) {
        this.sdaAward = sdaAward;
    }

    public void setFamilyDiscount(int familyDiscount) {
        this.familyDiscount = familyDiscount;
    }

    public String getNonCaGrantDesc() {
        return _nonCaGrantDesc;
    }

    public void setNonCaGrantDesc(String nonCaGrantDesc) {
        this._nonCaGrantDesc = nonCaGrantDesc;
    }

    public int getNonCaGrantAmt() {
        return _nonCaGrantAmt;
    }

    public void setNonCaGrantAmt(int nonCaGrantAmt) {
        this._nonCaGrantAmt = nonCaGrantAmt;
    }

    public void setOutsideScholarship(String outsideScholarship) {
        this._outsideScholarship = outsideScholarship;
    }

    public int getOutsideScholarshipAmt() {
        return _outsideScholarshipAmt;
    }

    public void setOutsideScholarshipAmt(int outsideScholarshipAmt) {
        this._outsideScholarshipAmt = outsideScholarshipAmt;
    }

    public void setCalGrantA(int calGrantA) {
        this.calGrantA = calGrantA;
    }

    public void setCalGrantB(int calGrantB) {
        this.calGrantB = calGrantB;
    }

    public void setLsuAllowance(int lsuAllowance) {
        this.lsuAllowance = lsuAllowance;
    }

    public void setLsuPerformance(int lsuPerformance) {
        this.lsuPerformance = lsuPerformance;
    }

    public void setNationalMerit(int nationalMerit) {
        this.nationalMerit = nationalMerit;
    }

    public void setChurchMatch(int churchMatch) {
        this.churchMatch = churchMatch;
    }

    public void setPacificCampMatch(int pacificCampMatch) {
        this.pacificCampMatch = pacificCampMatch;
    }

    public void setNonPacificCampMatch(int nonPacificCampMatch) {
        this.nonPacificCampMatch = nonPacificCampMatch;
    }

    public void setLitEvanMatch(int litEvanMatch) {
        this.litEvanMatch = litEvanMatch;
    }

    public int getPellGrant() {
        return pellGrant;
    }

    public void setPellGrant(int pellGrant) {
        this.pellGrant = pellGrant;
    }

    public int getFseogAmt() {
        return fseogAmt;
    }

    public void setFseogAmt(int fseogAmt) {
        this.fseogAmt = fseogAmt;
    }

    public int getExtAllowance() {
        return extAllowance;
    }

    public void setExtAllowance(int extAllowance) {
        this.extAllowance = extAllowance;
    }

    public int getNonCalGrantAmt() {
        return nonCaGrantAmt;
    }

    public void setNonCalGrantAmt(int nonCaGrantAmt) {
        this.nonCaGrantAmt = nonCaGrantAmt;
    }

    public int getOutsideAmt() {
        return outsideAmt;
    }

    public void setOutsideAmt(int outsideAmt) {
        this.outsideAmt = outsideAmt;
    }

    public void setChurchBase(int churchBase) {
        this.churchBase = churchBase;
    }

    public void setSubDirect(int subDirect) {
        this.subDirect = subDirect;
    }

    public void setPerkinsLoan(int perkinsLoan) {
        this.perkinsLoan = perkinsLoan;
    }

    public int getFwsAmount() {
        return fwsAmount;
    }

    public void setFwsAmount(int fwsAmount) {
        this.fwsAmount = fwsAmount;
    }

    public int getUnsubDirect() {
        return unsubDirect;
    }

    public int getUnsubDirectAmt() {
        return unsubDirect;
    }

    public int getSubDirectAmt() {
        return subDirect;
    }

    public void setUnsubDirect(int unsubDirect) {
        this.unsubDirect = unsubDirect;
    }

    public int getNeedAmt() {
        return needAmt;
    }

    public void setNeedAmt(int needAmt) {
        this.needAmt = needAmt;
        if (needAmt < 0) {
            this.needAmt = 0;
        }
    }

    public void setTuitionAndFees(int tuitionAndFees) {
        this.tuitionAndFees = tuitionAndFees;
    }

    public int getAddlExp() {
        return addlExp;
    }

    public void setAddlExp(int addlExp) {
        this.addlExp = addlExp;
    }

    public int getEfcInit() {
        return efcInit;
    }

    public int getEfc() {
        return efc;
    }

    public void setEfc(int efc) {
        this.efc = efc;
    }

    public String getExcludeNote() {
        return excludeNote;
    }

    public void setExcludeNote(String excludeNote) {
        this.excludeNote = excludeNote;
    }

    public int getLsuLimitSubtotal() {
        return lsuLimitSubtotal;
    }

    public void setLsuLimitSubtotal(int lsuLimitSubtotal) {
        this.lsuLimitSubtotal = lsuLimitSubtotal;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    /*
    public String getNonCaGrantDesc() {
        return nonCaGrantDesc;
    }

    public void setNonCaGrantDesc(String nonCaGrantDesc) {
        this.nonCaGrantDesc = nonCaGrantDesc;
    }
     */

    public void setOutsideDesc(String outsideDesc) {
        this.outsideDesc = outsideDesc;
    }

    public boolean isLsuOverallSubtotal() {
        return lsuOverallSubtotal;
    }

    public void setLsuOverallSubtotal(boolean lsuOverallSubtotal) {
        this.lsuOverallSubtotal = lsuOverallSubtotal;
    }

    public int getMaxAid() {
        return maxAid;
    }

    public void setMaxAid(int maxAid) {
        this.maxAid = maxAid;
    }

    public void setRoomAndBoard(int roomAndBoard) {
        this.roomAndBoard = roomAndBoard;
    }

    public void setPacificCampBase(int pacificCampBase) {
        this.pacificCampBase = pacificCampBase;
    }

    public void setNonPacificCampBase(int nonPacificCampBase) {
        this.nonPacificCampBase = nonPacificCampBase;
    }

    public void setLitEvanBase(int litEvanBase) {
        this.litEvanBase = litEvanBase;
    }

    public int getAmtDue() {
        return amtDue;
    }

    public void setAmtDue(int amtDue) {
        this.amtDue = amtDue;
        if (amtDue < 0) {
            this.amtDue = 0;
        }
    }

    public int getYearInAdvanceOption() {
        return yearInAdvanceOption;
    }

    public void setYearInAdvanceOption(int yearInAdvanceOption) {
        this.yearInAdvanceOption = yearInAdvanceOption;
    }

    public int getQuarterInAdvanceOption() {
        return quarterInAdvanceOption;
    }

    public void setQuarterInAdvanceOption(int quarterInAdvanceOption) {
        this.quarterInAdvanceOption = quarterInAdvanceOption;
    }

    public int getMonthlyOption() {
        return monthlyOption;
    }

    public void setMonthlyOption(int monthlyOption) {
        this.monthlyOption = monthlyOption;
    }

    public int getEa_lsu_per() {
        return ea_lsu_per;
    }

    public void setEa_lsu_per(int ea_lsu_per) {
        this.ea_lsu_per = ea_lsu_per;
    }

    public int getEa_nonlsu_per() {
        return ea_nonlsu_per;
    }

    public void setEa_nonlsu_per(int ea_nonlsu_per) {
        this.ea_nonlsu_per = ea_nonlsu_per;
    }

    public int getEa_nonlsu_dorm_per() {
        return ea_nonlsu_dorm_per;
    }

    public void setEa_nonlsu_dorm_per(int ea_nonlsu_dorm_per) {
        this.ea_nonlsu_dorm_per = ea_nonlsu_dorm_per;
    }

    public boolean isAdjust_calgrant_amt_ind() {
        return adjust_calgrant_amt_ind;
    }

    public void setAdjust_calgrant_amt_ind(boolean adjust_calgrant_amt_ind) {
        this.adjust_calgrant_amt_ind = adjust_calgrant_amt_ind;
    }

    public int getScholarship_amt_1() {
        return scholarship_amt_1;
    }

    public void setScholarship_amt_1(int scholarship_amt_1) {
        this.scholarship_amt_1 = scholarship_amt_1;
    }

    public int getScholarship_amt_2() {
        return scholarship_amt_2;
    }

    public void setScholarship_amt_2(int scholarship_amt_2) {
        this.scholarship_amt_2 = scholarship_amt_2;
    }

    public int getScholarship_amt_7() {
        return scholarship_amt_7;
    }

    public void setScholarship_amt_7(int scholarship_amt_7) {
        this.scholarship_amt_7 = scholarship_amt_7;
    }

    public int getScholarship_amt_8() {
        return scholarship_amt_8;
    }

    public void setScholarship_amt_8(int scholarship_amt_8) {
        this.scholarship_amt_8 = scholarship_amt_8;
    }

    public int getScholarship_amt_9() {
        return scholarship_amt_9;
    }

    public void setScholarship_amt_9(int scholarship_amt_9) {
        this.scholarship_amt_9 = scholarship_amt_9;
    }

    public void setLsu4yRenewable(int lsu4yRenewable) {
        this.lsu4yRenewable = lsu4yRenewable;
    }

    public void setLsuAchievement(int lsuAchievement) {
        this.lsuAchievement = lsuAchievement;
    }

    public int getLsuAchievementInit() {
        return lsuAchievementInit;
    }

    public void setLsuAchievementInit(int lsuAchievementInit) {
        this.lsuAchievementInit = lsuAchievementInit;
    }

    public int getLsuNeedGrant() {
        return lsuNeedGrant;
    }

    public void setLsuNeedGrant(int lsuNeedGrant) {
        this.lsuNeedGrant = lsuNeedGrant;
    }

    //================================== merge old calc into this actor ==============================================================
    public void setStud(Student std) {
        this.std = std;
        //this.actor = new PackFunctions(std);
        newPackFunctions(std);
    }

    public void refreshCalc(Student newstd) {
        //this.actor = new PackFunctions(std);
        this.std = newstd; //---w/o this, the award scholarship names and notes can not be updated
        this.setStd(newstd);

        //2012-02-15 need to compare two due amounts with fafsa on and off (actually, need-based aid==0 or not), and choose the one with less due
        initdone_ind = 0;

        //==============non-changing values===================
        int LASU_COA = getOtherExpenses();//; //tuition_fees + additional expenses + room/board (if) //old getCOA() returns needAmt==tuition_fees +(getOtherExpenses() - tuitionAndFees) - EFC
        //getTuitionAndFees()==yTotalTuitionAndFeesDomestic/international(fr)
        //getOtherExpenses()==yCoaDorm/Community(Fr)
        int LASU_TUITION = getTuitionAndFees();
        int LASU_NEED = LASU_COA - getEFC(); //COA-EFC
        if (LASU_NEED < 0) {
            LASU_NEED = 0;
        }

        //=============== changable values=====================        
        //int NEED_BASED_IND=0; //if std used any need-based aid, then max aid <=NEED (maybe 0). otherwise, he can get lsu aid (<=tuition) + external aid (COA- lsu aid).
        int LASU_MAXAID = 0;
        //check stud data to set NEED_BASED_IND LASU_MAXAID
        if (std.getStudentLIntlStud().equalsIgnoreCase("No") && (std.getStudentAkNoncalGrant() > 0 || std.getStudentZCalgrant().equalsIgnoreCase("YES") && std.getAdjCalgrantInd().equalsIgnoreCase("YES") && (std.getStudentAaCalgrantA() + std.getStudentAbCalgrantB()) > 0)) { //any ded/stat aid (pell, perkins, cal grant a/b, sub loan, fws, fseog), or lasu need grant. (except unsub loan)
            use_need_ind = 1;
            //           log.info("\n>>>>>>>>>>>>>> refreshCalc() found non-ca grant=%d, cal_grant_opt=%s, adj_cal_grant=%s, adj_amt=%d >>>>>>>>>>>>", std.getStudentAkNoncalGrant(), std.getStudentZCalgrant(),std.getAdjCalgrantInd(), std.getStudentAaCalgrantA()+std.getStudentAbCalgrantB() );
            LASU_MAXAID = LASU_NEED;
        } else {
            use_need_ind = 0;
            LASU_MAXAID = LASU_COA;
        }
        //use_need_ind = NEED_BASED_IND;

        //       log.info("\n>>>>>>>>>>>>>> refreshCalc() invokes default caculation, use_need=[%d] >>>>>>>>>>>>", use_need_ind);
        initAndShowPellGrantAmt();

//        log.info("\n  <<<<<<< doc[%s] by [%d] refreshCalc() got 1st due amount. use_need=%d, and get due=%d, needAmt=%d, maxAid=%d, fws=%d",std.getRecid()==null? "new": std.getRecid(), login.getCurrentUser().getUserid(), use_need_ind, amtDue, needAmt, maxAid, fwsAmount);
        log.info("\n<<<<<<< doc[%s] by [n/a] refreshCalc() got 1st due amount after initAndShowPellGrantAmt(). use_need=%d, and get due=%d, needAmt=%d, maxAid=%d, fws=%d", std.getRecid() == null ? "new" : std.getRecid(), use_need_ind, amtDue, needAmt, maxAid, fwsAmount);

        if (use_need_ind < 1 && std.getStudentLIntlStud().equalsIgnoreCase("No")) {//use_need_ind>0: no choice, but all aid <= EFC            
            //can compare (with or w/o need) to pick the lowest due for student
            //step1: save the already gotten due amount1, w/o need-based amount 
            int due1 = amtDue;
            int loan1 = unsubDirect;
            int maxaid1 = maxAid;

            //step2: get the due amount2 with need-based aids
            this.setStd(newstd); //init();  also reset sum_tuition_aid, sum_total_aid
            use_need_ind = 1;
            //          log.info(">>>>>>> refreshCalc() invokes 2nd caculation, use_need=[%d]", use_need_ind);
            initAndShowPellGrantAmt();
            int due2 = amtDue;
            int loan2 = unsubDirect;
            int maxaid2 = maxAid;
            log.info("\n ++++++++ refreshCalc() got 2nd amount after initAndShowPellGrantAmt() (w/need) to compare. use_need=%d, due=%d, needAmt=%d, maxAid=%d", use_need_ind, due2, needAmt, maxAid);

            //step3: if amount2 is less, stop.
            if (maxaid2 >= maxaid1) {//due2 <= due1){
                ;
                //               log.info("/////////////refreshCalc() found the 2nd aid amount is equal or better. kept it.");
            } //step4: if amount1 is less, reverse to amount1
            else {
                this.setStd(newstd); //init();  also reset sum_tuition_aid, sum_total_aid
                use_need_ind = 0;
                //              log.info(">>>>>>> refreshCalc() restores default caculation which has better aid, use_need=[%d]", use_need_ind);
                initAndShowPellGrantAmt();
                log.info("<<<<<<< refreshCalc() found the 1st aid amount is better. restored via run 3rd initAndShowPellGrantAmt().");
            }
        }

    }

    public void init() {
        this.resetValues();
    }

    private PackFunctions actor = this;

    public String showPellGrantAmt() { //for pdfGen, which will invoke calc.refresh(std) first
        if (!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        return fmt.format(pellGrant);
    }

    private void adjustLoanAmt() {
        /*
        subDirect = actor.getSubDirect();
        perkinsLoan = actor.getPerkinsLoan();
        unsubDirect = actor.getUnSubDirect();         
         */
        org_loan_amt_sub = subDirect;
        org_loan_amt_unsub = unsubDirect;
        org_loan_amt_perkins = perkinsLoan;

        subDirect = getLoanAmtAfterOrigination(subDirect);

        //Esther claimed Perkins loan had no initiation fee 2013-07-25
// perkinsLoan = getLoanAmtAfterOrigination(perkinsLoan);
        unsubDirect = getLoanAmtAfterOrigination(unsubDirect);
    }

    /*
     * ========================== Finance Aid (external grant) =============================
     */
    public String initAndShowPellGrantAmt() {
        if (use_need_ind < 0) {//load web page, not from refreshCalc
            if (std.getStudentAkNoncalGrant() > 0 || std.getStudentZCalgrant().equalsIgnoreCase("YES") && std.getAdjCalgrantInd().equalsIgnoreCase("YES") && (std.getStudentAaCalgrantA() + std.getStudentAbCalgrantB()) > 0) { //any ded/stat aid (pell, perkins, cal grant a/b, sub loan, fws, fseog), or lasu need grant. (except unsub loan)
                use_need_ind = 1;
            } else if (pellGrant > 0 || calGrantA > 0 || calGrantB > 0 || fseogAmt > 0 || nonCaGrantAmt > 0 || _nonCaGrantAmt > 0 || lsuNeedGrant > 0 || lasuGrantAmt > 0) {
                use_need_ind = 1;
            } else {
                use_need_ind = 0;
            }
            log.info("==== initAndShowPellGrantAmt() convert use_need_ind from -1 to [%d]", use_need_ind);
        }

        //      log.info("====initAndShowPellGrantAmt() is invoked. need-based=[%d] =======", use_need_ind); 
        //needAmt will be calc showEFC(), which is after this whole method initAndShowPellGrantAmt(). so it is always 0 !!!!
        needAmt = getMPA();

        pellGrant = actor.getPell();
        /*
        if( initdone_ind>0){
            if(!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES"))return "n/a";
            return fmt.format(pellGrant);
        } */

        adjust_calgrant_amt_ind = std.getStudentZCalgrant().equalsIgnoreCase("YES") ? (std.getAdjCalgrantInd().equalsIgnoreCase("YES") ? true : false) : false;

        calGrantA = actor.getCalGrantA();
        calGrantB = actor.getCalGrantB();
        fseogAmt = actor.getFseog();
        extAllowance = actor.getExternalAllowance();

        err = actor.getNonCaGrant(); //err only show up here 1/2 time
        nonCaGrantDesc = _nonCaGrantDesc; //only shows in this file
        nonCaGrantAmt = _nonCaGrantAmt;

        err = actor.getOutsideScholarship(); //err only show up here 2/2 time
        outsideDesc = _outsideScholarship;  //only shows in this file
        outsideAmt = _outsideScholarshipAmt;

        churchBase = actor.getChurchBase();
        sdaAward = actor.getSdaAward();
        //       log.info("====== after init, sdaAward=%d", sdaAward);

        lsuAllowance = actor.getLsuAllowance();
        familyDiscount = actor.getFamilyDiscount();

        lsuNeedGrant = actor.getLsuNeedGrantAmt();
        //lsuPerformance = actor.getLsuPerformance();

        //201602
        lasuGrantAmt = actor.getLasuGrantAmt();

        nationalMerit = actor.getNationalMerit();
        /*
        if(lsuPerformance > nationalMerit ){
            nationalMerit = 0;
        }else{
            lsuPerformance = 0;
        }*/
        if (nationalMerit > 0) {
            lsuAchievement = 0;
        } else {
            lsuAchievement = actor.getLsuAchievement();
        }

        lsu4yRenewable = actor.getLsu4yRenewable();
        //universityHousingGrant = actor.();
        ////log.info(".....initAndShowPellGrantAmt() before maxout, got nationalMerit=%d, lsuAchievement=%d, lsu4yRenewable=%d", nationalMerit, lsuAchievement, lsu4yRenewable);
        
        if (lsu4yRenewable > (lsuAchievement + nationalMerit)) {
            lsuAchievement = 0;
            nationalMerit = 0;
        } else {
            lsu4yRenewable = 0;
        }

        churchMatch = actor.getChurchMatch();
        pacificCampMatch = actor.getPacificCampMatch();
        nonPacificCampMatch = actor.getNonPacificCampMatch();
        litEvanMatch = actor.getLitEvanMatch();

        //========================
        if (std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
            enforceCJLimits();
        }

        maxAid = pellGrant + calGrantA + calGrantB + fseogAmt + extAllowance + nonCaGrantAmt + outsideAmt + churchBase + lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + subDirect + perkinsLoan + fwsAmount + unsubDirect;
        maxAid += scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        ////       log.debug("initAndShowPellGrantAmt()... get maxaid=%d while fwsAmt=%d, subloan=%d, unsub=%d, perkins=%d", maxAid, fwsAmount, subDirect, unsubDirect, perkinsLoan);
        
        sum_lasu_aid = lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        sum_tuition_aid = sum_lasu_aid + calGrantA + calGrantB;
        amtDue = needAmt - maxAid;
        ////       log.debug("..... initAndShowPellGrantAmt() before enforceLsuLimits() got maxAid=%d, needAmt=%d, dueAmt=%d, fwsAmount=%d ......", maxAid, needAmt, amtDue, fwsAmount);
    
        
  lsuLimitSubtotal = actor.enforceLsuLimits();  //returned is a constant: lsuSubTotal !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        maxAid = pellGrant + calGrantA + calGrantB + fseogAmt + extAllowance + nonCaGrantAmt + outsideAmt + churchBase + lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + subDirect + perkinsLoan + fwsAmount + unsubDirect;
        maxAid += scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        ////         log.debug("initAndShowPellGrantAmt()...<after enforcing LASU limit %d>... get maxaid=%d while  fwsAmt=%d, subloan=%d, unsub=%d, perkins=%d", lsuLimitSubtotal, maxAid, fwsAmount, subDirect, unsubDirect, perkinsLoan);
        
        sum_lasu_aid = lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        sum_tuition_aid = sum_lasu_aid + calGrantA + calGrantB;
        amtDue = needAmt - maxAid;
        ////       log.debug("..... initAndShowPellGrantAmt() after enforceLsuLimits() got maxAid=%d, needAmt=%d, dueAmt=%d, fwsAmount=%d ......", maxAid, needAmt, amtDue, fwsAmount);
    
        
  
        subDirect = actor.getSubDirect();
        perkinsLoan = actor.getPerkinsLoan();
        unsubDirect = actor.getUnSubDirect();
        //2013 by Ken
        adjustLoanAmt();

        //<CFSET excludeNote = "Please exclude loans"> defined in estimatorStudentData.cfm
        //<CFIF SAVE_STUDENT_AN_PUB/PRI_NOTES contains "#excludeNote#">checked</CFIF> > I wish to exclude loans from this estimate
        //if( std.getStudentAnPubNotes()!=null && std.getStudentAnPubNotes().indexOf(excludeNote)>=0 ){
        if (std.getIndExcloans().equalsIgnoreCase("Yes")) { //.getInd_noloans()
            subDirect = 0;
            perkinsLoan = 0;
            unsubDirect = 0;
        } else {
            if (std.getStudentApSubLoans().equalsIgnoreCase("no")) {
                subDirect = 0;
            }
            if (std.getStudentAqUnsubLoans().equalsIgnoreCase("no")) {
                unsubDirect = 0;
            }
        }
        //if( std.getStudentArFws().equalsIgnoreCase("no")){
        //        fwsAmount = 0;
        //}
        fwsAmount = actor.getFWS();

        maxAid = pellGrant + calGrantA + calGrantB + fseogAmt + extAllowance + nonCaGrantAmt + outsideAmt + churchBase + lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + subDirect + perkinsLoan + fwsAmount + unsubDirect;
        maxAid += scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        ////      log.debug("initAndShowPellGrantAmt()...<after countin loans and fws>... get maxaid=%d while fwsAmt=%d, noloans?%s, subloan=%d (opt=%s), unsub=%d(opt=%s), perkins=%d", maxAid, fwsAmount, std.getIndExcloans(), subDirect,std.getStudentApSubLoans(), unsubDirect, std.getStudentAqUnsubLoans(), perkinsLoan);
                        
        sum_lasu_aid = lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        sum_tuition_aid = sum_lasu_aid + calGrantA + calGrantB;
        amtDue = needAmt - maxAid;
        //       log.debug("..... initAndShowPellGrantAmt() ###before enforceOverallLimits()### got maxAid=%d, needAmt=%d, dueAmt=%d, fwsAmount=%d ......", maxAid, needAmt, amtDue, fwsAmount);

        String[] awd_name = {"pellGrant", "calGrantA", "calGrantB", "fseogAmt", "extAllowance", "nonCaGrantAmt", "outsideAmt", "churchBase", "lsuAllowance", "sdaAward", 
            "lsuNeedGrant", "lasuGrantAmt", "lsuAchievement", "lsu4yRenewable", "familyDiscount", "nationalMerit", "churchMatch", "pacificCampMatch", "nonPacificCampMatch", 
            "litEvanMatch", "subDirect", "perkinsLoan", "fwsAmount", "unsubDirect", 
            "scholarship_amt_1", "scholarship_amt_2", "scholarship_amt_7", "scholarship_amt_8", "scholarship_amt_9"};

        int[] awd_val = {pellGrant, calGrantA, calGrantB, fseogAmt, extAllowance, nonCaGrantAmt, outsideAmt, churchBase, lsuAllowance, sdaAward, 
            lsuNeedGrant, lasuGrantAmt, lsuAchievement, lsu4yRenewable, familyDiscount, nationalMerit, churchMatch, pacificCampMatch, nonPacificCampMatch, 
            litEvanMatch, subDirect, perkinsLoan, fwsAmount, unsubDirect, 
            scholarship_amt_1, scholarship_amt_2, scholarship_amt_7, scholarship_amt_8, scholarship_amt_9};

        /*
        System.out.println(" .");
        for(int i=0; i<awd_val.length; i++){
            if( awd_val[i] >0){
                System.out.println(awd_name[i] +"\t = "+awd_val[i]);
            }
        }*/
        lsuOverallSubtotal = actor.enforceOverallLimits(); //getCOA <--needAMt <-- getEFC (tuitionfees+addexp-EFC) <-- showAddlExpense() <-- getOtherExpenses (ycoa - tuitionfees)

        //----------- 2016-05-05 added to enforce fee deduction
        //System.out.println("initAndShowPellGrantAmt().###<after enforcing overall limit>###, unsubDirect="+unsubDirect);
        if (unsubDirect == this.org_loan_amt_unsub) {
            //System.out.println("initAndShowPellGrantAmt().###<after enforcing overall limit>###, unsubDirect==org_loan_amt_unsub, need to deduct fees"); 
            unsubDirect = getLoanAmtAfterOrigination(unsubDirect);
        }

        maxAid = pellGrant + calGrantA + calGrantB + fseogAmt + extAllowance + nonCaGrantAmt + outsideAmt + churchBase + lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + subDirect + perkinsLoan + fwsAmount + unsubDirect;

        maxAid += scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        log.debug("initAndShowPellGrantAmt().###<after enforcing overall limit>### get maxaid=%d while fwsAmt=%d, subloan=%d, unsub=%d, perkins=%d", maxAid, fwsAmount, subDirect, unsubDirect, perkinsLoan);

        int[] awd_val2 = {pellGrant, calGrantA, calGrantB, fseogAmt, extAllowance, nonCaGrantAmt, outsideAmt, churchBase, lsuAllowance, sdaAward, 
            lsuNeedGrant, lasuGrantAmt, lsuAchievement, lsu4yRenewable, familyDiscount, nationalMerit, churchMatch, pacificCampMatch, nonPacificCampMatch, 
            litEvanMatch, subDirect, perkinsLoan, fwsAmount, unsubDirect, 
            scholarship_amt_1, scholarship_amt_2, scholarship_amt_7, scholarship_amt_8, scholarship_amt_9};
        /*
        System.out.println(".");
        for(int i=0; i<awd_val2.length; i++){
            if( awd_val2[i] >0){
                System.out.println(awd_name[i] +"\t = "+awd_val2[i]);
            }
        }*/

        sum_lasu_aid = lsuAllowance + sdaAward
                + lsuNeedGrant + lasuGrantAmt + lsuAchievement + lsu4yRenewable + familyDiscount + nationalMerit + churchMatch + pacificCampMatch + nonPacificCampMatch
                + litEvanMatch + scholarship_amt_1 + scholarship_amt_2 + scholarship_amt_7 + scholarship_amt_8 + scholarship_amt_9;
        sum_tuition_aid = sum_lasu_aid + calGrantA + calGrantB;

        //amtDue = amtDue - maxAid;        
        amtDue = needAmt - maxAid;
        //       log.debug("..... initAndShowPellGrantAmt() after enforceOverallLimits() got maxAid=%d, needAmt=%d, dueAmt=%d, fwsAmount=%d ......", maxAid, needAmt, amtDue, fwsAmount);

        amtDue = amtDue < 0 ? 0 : amtDue;

//        log.debug(".....initAndShowPellGrantAmt() after maxout, got nationalMerit=%d, lsuAchievement=%d, lsu4yRenewable=%d", nationalMerit, lsuAchievement, lsu4yRenewable);
        if (!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        return fmt.format(pellGrant);
    }

    public int needbased() {
        return use_need_ind;
    }

    public String showTuitionFees() {
        int val = actor.getTuitionAndFees();
        tuitionAndFees = val;
        return fmt.format(val);
    }

    public String showAddlExpense() {
        int val = actor.getOtherExpenses() - tuitionAndFees;
        addlExp = val;
        return fmt.format(val);
    }

    public String showEFC() {
        int val = actor.getEFC();
        efc = val;

        if (val == 0) {
            needAmt = tuitionAndFees + addlExp;
        } else {
            needAmt = tuitionAndFees + addlExp - val; //efc
            if (needAmt < 0) {
                needAmt = 0;
            }
        }

        if (actor.getInternationalStatus().equalsIgnoreCase("International") || !SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        } else {
            return fmt.format(val);
        }
    }

    public String showNeedAmt() {
        return fmt.format(needAmt);
    }

    public String showCalGrantA() {
        if (!SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        return fmt.format(calGrantA);
    }

    public String showCalGrantB() {
        if (!SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        return fmt.format(calGrantB);
    }

    public String showFseogAmt() {
        if (!SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        return fmt.format(fseogAmt);
    }

    public String showExtAllowanceAmt() {
        return fmt.format(extAllowance);
    }

    public String getNonCalGrantDesc() {
        if (nonCaGrantDesc != null && nonCaGrantDesc.length() > 40) {
            return nonCaGrantDesc.substring(0, 37) + "...";
        } else {
            return nonCaGrantDesc == null ? "n/a" : nonCaGrantDesc;
        }
    }

    public String showNonCalGrantAmt() {
        return fmt.format(nonCaGrantAmt);
    }

    public String getOutsideDesc() {
        if (outsideDesc != null && outsideDesc.length() > 40) {
            return outsideDesc.substring(0, 37) + "...";
        } else {
            return outsideDesc == null ? "n/a" : outsideDesc;
        }
    }

    public String showOutsideAmt() {
        return fmt.format(outsideAmt);

    }

    public int getChurchBaseAmt() {
        return churchBase;
    }

    public String showChurchBaseDesc() {
        return std.getStudentAyScholarship3Name();//.getStudentAtScholarship1Note();
    }

    public String showChurchBaseAmt() {
        return fmt.format(churchBase);
    }

    /*
     * ========================== Finance Aid (LSU awards) =============================
     */
    public String showLsuAllowanceAmt() {
        return fmt.format(lsuAllowance);
    }

    public String showSdaAwardAmt() {
//        log.info("========showSdaAwardAmt() is invoked.===");
        return fmt.format(sdaAward);
    }

    public String showLsuPerformanceAmt() {
        return fmt.format(lsuPerformance);
    }

    public int getLsuAchievementAmt() {
        return lsuAchievement;
    }

    public String showLsuAchievementAmt() {
        return fmt.format(lsuAchievement);
    }

    public int getLsu4yRenewableAmt() {
        return lsu4yRenewable;
    }

    public String showLsu4yRenewableAmt() {
        return fmt.format(lsu4yRenewable);
    }

    /*    
    public int getLsuNeedGrantAmt(){
        return lsuNeedGrant;
    }*/
    public String showLsuNeedGrantAmt() {
        return fmt.format(lsuNeedGrant);
    }

    public String showFamilyDiscountAmt() {
        return fmt.format(familyDiscount);
    }

    public String showNationalMeritAmt() {
        return fmt.format(nationalMerit);
    }

    public int getChurchMatchAmt() {
        return churchMatch;
    }

    public String showChurchMatchDesc() {
        return cutBarStr2(std.getStudentAyScholarship3Name());//getStudentAtScholarship1Note() +" (Match)"; 
    }

    public String showChurchMatchAmt() {
        return fmt.format(churchMatch);
    }

    public int getPacificCampMatchAmt() {
        return pacificCampMatch;
    }

    public String showPacificCampMatchDesc() {
        return cutBarStr2(std.getStudentBeScholarship5Name());//( std.getStudentAwScholarship2Note()) + " (Match)";
    }

    public String showPacificCampMatchAmt() {
        return fmt.format(pacificCampMatch);
    }

    public int getNonPacificCampMatchAmt() {
        return nonPacificCampMatch;
    }

    public String showNonPacificCampMatchDesc() {
        return cutBarStr2(std.getStudentBhScholarship6Name());//( std.getStudentAzScholarship3Note()) + " (Match)";
    }

    public String showNonPacificCampMatchAmt() {
        return fmt.format(nonPacificCampMatch);
    }

    public int getLitEvanMatchAmt() {
        return litEvanMatch;
    }

    public String showLitEvanMatchDesc() {
        return cutBarStr2(std.getStudentBbScholarship4Name());//( std.getStudentBcScholarship4Note()) + " (Match)";
    }

    public String showLitEvanMatchAmt() {
        return fmt.format(litEvanMatch);
    }

    public String showLsuLimitSubtotal() { //---not used
        return fmt.format(lsuLimitSubtotal);
    }

    /*
     * ========================== Finance Aid (Loans) =============================
     */
    public String showSubdirectAmt() {
        return fmt.format(subDirect);
    }

    public String showPerkinLoanAmt() {
        return fmt.format(perkinsLoan);
    }

    public int getPerkinsLoanAmt() {
        return perkinsLoan;
    }

    public String showUnsubdirectAmt() {
        if (unsubDirect == this.org_loan_amt_unsub) {
            unsubDirect = getLoanAmtAfterOrigination(unsubDirect);
        }
        return fmt.format(unsubDirect);
    }

    /*
     * ========================== Finance Aid (Federal Work-study) =============================
     */
    public int getFwsAmt() {
        return fwsAmount;
    }

    public String showFwsAmt() {
        //if fafsa==no, its amount is zero, and will not show, SO no need to return "n/a" if fafsa=no here
        return fmt.format(fwsAmount);
    }

    /*
     * ========================== Finance Aid (sum) =============================
     */
    public String showMaxAidAmt() {
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
    public String initAndShowRoomBoardAmt() {
        roomAndBoard = actor.getRoomAndBoard();
        pacificCampBase = actor.getPacificCampBase();
        nonPacificCampBase = actor.getNonPacificCampBase();
        litEvanBase = actor.getLitEvanBase();
        amtDue = tuitionAndFees + roomAndBoard - maxAid + fwsAmount - pacificCampBase - nonPacificCampBase - litEvanBase;     // -maxAid ==0 at first after init      
        //       log.info("****** initAndShowRoomBoardAmt() calc amtDue=%d while maxAid=%d", amtDue, maxAid);

        if (amtDue < 0) {
            amtDue = 0;
        }

        return fmt.format(roomAndBoard);
    }

    public String showFaidExtDesc() {
        return fwsAmount > 0 || std.getStudentArFws().equalsIgnoreCase("yes") ? " (FWS excluded)" : " (FWS not Counted)";
    }

    public String showFaidAmt() {
        //      log.info("****** showFaidAmt() get maxAid=%d, fwsAmount=%d", maxAid, fwsAmount);
        return fmt.format(maxAid - fwsAmount);
    }

    public int getFaidExtAmt() {
        return maxAid - fwsAmount;
    }

    public String showPacificCampBaseDesc() {
        return "- " + cutBarStr2(std.getStudentBeScholarship5Name()) + " (Earnings)"; //getStudentAwScholarship2Note()
    }

    public String showPacificCampBaseAmt() {
        return fmt.format(pacificCampBase);
    }

    public int getNonPacificCampBaseAmt() {
        return nonPacificCampBase;
    }

    public String showNonPacificCampBaseDesc() {
        return "- " + cutBarStr2(std.getStudentBhScholarship6Name()) + " (Earnings)"; //getStudentAzScholarship3Note()
    }

    public String showNonPacificCampBaseAmt() {
        return fmt.format(nonPacificCampBase);
    }

    public int getLitEvanBaseAmt() {
        return litEvanBase;
    }

    public String showLitEvanBaseDesc() {
        return "- " + cutBarStr2(std.getStudentBbScholarship4Name()) + " (Earnings)";  //getStudentBcScholarship4Note()
    }

    public String showLitEvanBaseAmt() {
        return fmt.format(litEvanBase);
    }

    public String showDueAmt() {
        return fmt.format(amtDue);
    }

    /*
     * ========================== Payment Option =============================
     */
    public String initAndShowYIA() {
        if (amtDue > 0) {
            yearInAdvanceOption = amtDue - (new BigDecimal(amtDue).multiply(yearInAdvanceDiscount).intValue());
            quarterInAdvanceOption = (amtDue - (new BigDecimal(amtDue).multiply(quarterInAdvanceDiscount).intValue())) / 3;
            monthlyOption = (amtDue + monthlyOptionFees) / monthlyOptionMonthes;
        } else {
            yearInAdvanceOption = 0;
            quarterInAdvanceOption = 0;
            monthlyOption = 0;
        }
        return fmt.format(yearInAdvanceOption);
    }

    public String showQIA() {
        return fmt.format(quarterInAdvanceOption);
    }

    public String showMonthlyOption() {
        return fmt.format(monthlyOption);
    }

    //for estimator to read static constant values 
    public int getEaLsuPercentage() {
        return ea_lsu_per;
    }

    public int getEaNonLsuPercentage() {
        return ea_nonlsu_per;
    }

    public int getEaNonLsuPercentageByDorm(boolean in_dorm) {    // by Ken 2011-10-31    
        return in_dorm ? ea_nonlsu_dorm_per : ea_nonlsu_per;
    }

    ///for estimator to set adjust_cal_grant_amt indicator
    public void setAdjustCalGrantAmtInd(boolean ind) {
        adjust_calgrant_amt_ind = ind;
    }

    /////////////////// added to show scholarship #5 ~ #9
    public int getScholarship1Amt() {
        return scholarship_amt_1;
    }

    public String showScholarship1Amt() {
        return fmt.format(scholarship_amt_1);
    }

    public String showScholarship1Desc() {
        return cutBarStr(std.getStudentAsScholarship1Name());//.getStudentBeScholarship5Name());
    }

    public String showScholarship5Desc() {
        return cutBarStr(std.getStudentBeScholarship5Name());
    }

    public int getScholarship2Amt() {
        return scholarship_amt_2;
    }

    public String showScholarship2Amt() {
        return fmt.format(scholarship_amt_2);
    }

    public String showScholarship2Desc() {
        return cutBarStr(std.getStudentAvScholarship2Name());// std.getStudentBhScholarship6Name());
    }

    public String showScholarship6Desc() {
        return cutBarStr(std.getStudentBhScholarship6Name());
    }

    public int getScholarship7Amt() {
        return scholarship_amt_7;
    }

    public String showScholarship7Amt() {
        return fmt.format(scholarship_amt_7);
    }

    public String showScholarship7Desc() {
        return cutBarStr(std.getStudentBkScholarship7Name());
    }

    public int getScholarship8Amt() {
        return scholarship_amt_8;
    }

    public String showScholarship8Amt() {
        return fmt.format(scholarship_amt_8);
    }

    public String showScholarship8Desc() {
        return cutBarStr(std.getStudentBnScholarship8Name());
    }

    public int getScholarship9Amt() {
        return scholarship_amt_9;
    }

    public String showScholarship9Amt() {
        return fmt.format(scholarship_amt_9);
    }

    public String showScholarship9Desc() {
        return cutBarStr(std.getStudentBqScholarship9Name());
    }

    public String cutStr(String str, int max, String makeup) {
        if (str == null) {
            return "";
        }
        int i = str.length();
        if (i <= max) {
            return str;
        }
        return str.subSequence(0, max) + makeup;
    }

    public String cutBarStr(String str) {
        return cutStr(str, 40, "...");
    }

    public String cutBarStr2(String str) {
        return cutStr(str, 30, "...");
    }

    public String show_sum_lasu_aid() {
        return "La Sierra Awards (" + fmt.format(sum_lasu_aid) + ")";
    }

    //delegate of actor's func, since actor is private
/*    
    public void setSAVE_STUDENT_L_INTL_STUD(String newStrVal){
        actor.setSAVE_STUDENT_L_INTL_STUD( newStrVal );
    }
     */
 /*
    public void setSAVE_STUDENT_X_FAFSA(String newStrVal){
        actor.SAVE_STUDENT_X_FAFSA( newStrVal );
    }
    
    public void setSAVE_STUDENT_Z_CALGRANT(String newStrVal){
        actor.SAVE_STUDENT_Z_CALGRANT( newStrVal );        
    }*/

 /*    
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
     */
    public final int getLsuPerformanceOrg() {
        int _efc = 0, _lsuPerformance = 0;
        /**
         * ********
         * <CFOUTPUT
         * <br />#SAVE_STUDENT_P_GPA#
         * <br />#SAVE_STUDENT_Q_SAT#
         * <br />#SAVE_STUDENT_R_ACT#
         * <br />#SAVE_STUDENT_U_ACADEMIC#
         * <br />#SAVE_STUDENT_X_FAFSA#
         * <br />#SAVE_STUDENT_AF_FAMILY_CONTRIB#
         * <br />#getInternationalStatus()# </CFOUTPUT
    *********
         */
        if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")) {
            _efc = Integer.parseInt(SAVE_STUDENT_AF_FAMILY_CONTRIB);// REreplace(SAVE_STUDENT_AF_FAMILY_CONTRIB,"\D","","ALL");
            /* if(  TRIM(_efc).equalsIgnoreCase("")){
                             _efc = 0;
                    } */
        } else {
            _efc = 0;//"";
        }

        if (!TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")
                || std.getStudentPGpa().compareTo(new BigDecimal(2)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2 ){
            _lsuPerformance = 0;
        } else {
            /**
             * ********  <h1>Qualified</h1>*********
             */
            if ((TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")
                    || TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")
                    && getInternationalStatus().equalsIgnoreCase("Domestic"))) {
                /**
                 * ********  <h1>Domestic Freshmen (<CFOUTPUT>#TRIM(SAVE_STUDENT_U_ACADEMIC)#</CFOUTPUT>)</h1>*********
                 */
                if (std.getStudentQSat() < 950 && std.getStudentRAct() < 18) {//   TRIM(SAVE_STUDENT_Q_SAT)  < 950 && TRIM(SAVE_STUDENT_R_ACT)  < 18 ){
                    /**
                     * ********  <h1>Sub 950 / 18 Level</h1>*********
                     */
                    if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 4617) {
                        /**
                         * ********  <h1>Valid EFC at or below 4617</h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub4617Sub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {//  TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub4617Sub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub4617Sub380;
                        } else {
                            _lsuPerformance = performanceSubSat950Act18FrSub4617Sub401;
                        }
                    } else if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 12000) {
                        /**
                         * ********  <h1>Valid EFC at or below 12000</h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub12000Sub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub12000Sub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub12000Sub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub12000Sub380;
                        } else {
                            _lsuPerformance = performanceSubSat950Act18FrSub12000Sub401;
                        }
                    } else if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc > 12000) {
                        /**
                         * ********  <h1>Valid EFC above 12,000</h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub100000Sub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub100000Sub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub100000Sub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceSubSat950Act18FrSub100000Sub380;
                        } else {
                            _lsuPerformance = performanceSubSat950Act18FrSub100000Sub401;
                        }
                    } else {
                        /**
                         * ********  <h1>No valid EFC</h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSubNothingSub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceSubSat950Act18FrSubNothingSub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceSubSat950Act18FrSubNothingSub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceSubSat950Act18FrSubNothingSub380;
                        } else {
                            _lsuPerformance = performanceSubSat950Act18FrSubNothingSub401;
                        }
                    }
                } else {
                    /**
                     * ********  <h1>Domestic Freshmen above 950 / 18 Level</h1>*********
                     */
                    if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 4617) {
                        /**
                         * ********  <h1>Valid EFC at or below 4617 </h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub4617Sub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub4617Sub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub4617Sub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub4617Sub380;
                        } else {
                            _lsuPerformance = performanceMinSat950Act18FrSub4617Sub401;
                        }
                    } else if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 12000) {
                        /**
                         * ********  <h1>Valid EFC at or below 12000 </h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub12000Sub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub12000Sub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub12000Sub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub12000Sub380;
                        } else {
                            _lsuPerformance = performanceMinSat950Act18FrSub12000Sub401;
                        }
                    } else if (SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc < 100000) {
                        /**
                         * ********  <h1>Valid EFC above 12000 </h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub100000Sub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub100000Sub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub100000Sub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceMinSat950Act18FrSub100000Sub380;
                        } else {
                            _lsuPerformance = performanceMinSat950Act18FrSub100000Sub401;
                        }
                    } else {
                        /**
                         * ********  <h1>No valid EFC</h1>*********
                         */
                        if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSubNothingSub250;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                            _lsuPerformance = performanceMinSat950Act18FrSubNothingSub300;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                            _lsuPerformance = performanceMinSat950Act18FrSubNothingSub350;
                        } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                            _lsuPerformance = performanceMinSat950Act18FrSubNothingSub380;
                        } else {
                            _lsuPerformance = performanceMinSat950Act18FrSubNothingSub401;
                        }
                    }
                }
            } else if (getInternationalStatus().equalsIgnoreCase("Domestic")) {
                /**
                 * ********  <h1>Domestic So, Jr or Sr</h1>*********
                 */
                if (_efc <= 4617) { //!TRIM(_efc).equalsIgnoreCase("")  &&
                    /**
                     * ********  <h1>Valid EFC at or below 4617</h1>*********
                     */
                    if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                        _lsuPerformance = performanceSub4617Sub250;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                        _lsuPerformance = performanceSub4617Sub300;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                        _lsuPerformance = performanceSub4617Sub350;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                        _lsuPerformance = performanceSub4617Sub380;
                    } else {
                        _lsuPerformance = performanceSub4617Sub401;
                    }
                } else if (_efc <= 12000) { //!TRIM(_efc).equalsIgnoreCase("") &&
                    /**
                     * ********  <h1>Valid EFC at or below 12,000</h1>*********
                     */
                    if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                        _lsuPerformance = performanceSub12000Sub250;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                        _lsuPerformance = performanceSub12000Sub300;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                        _lsuPerformance = performanceSub12000Sub350;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                        _lsuPerformance = performanceSub12000Sub380;
                    } else {
                        _lsuPerformance = performanceSub12000Sub401;
                    }
                } else if (_efc <= 100000) { // !TRIM(_efc).equalsIgnoreCase("")  &&
                    /**
                     * ********  <h1>Valid EFC above 12,000</h1>*********
                     */
                    if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                        _lsuPerformance = performanceSub100000Sub250;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                        _lsuPerformance = performanceSub100000Sub300;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                        _lsuPerformance = performanceSub100000Sub350;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                        _lsuPerformance = performanceSub100000Sub380;
                    } else {
                        _lsuPerformance = performanceSub100000Sub401;
                    }
                } else {
                    /**
                     * ********  <h1>No valid EFC</h1>*********
                     */
                    if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                        _lsuPerformance = performanceSubNothingSub250;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                        _lsuPerformance = performanceSubNothingSub300;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                        _lsuPerformance = performanceSubNothingSub350;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                        _lsuPerformance = performanceSubNothingSub380;
                    } else {
                        _lsuPerformance = performanceSubNothingSub401;
                    }
                }
            } else {
                /**
                 * ********  <h1>International Student (<CFOUTPUT>#getInternationalStatus()#</CFOUTPUT>)</h1>*********
                 */
                if (TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                    /**
                     * ********  <h1>First-Time College</h1>*********
                     */
                    if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                        _lsuPerformance = performanceInternationalFrSub250;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                        _lsuPerformance = performanceInternationalFrSub300;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                        _lsuPerformance = performanceInternationalFrSub350;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {//TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                        _lsuPerformance = performanceInternationalFrSub380;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) >= 0) {// TRIM(SAVE_STUDENT_P_GPA) >= 3.8 ){
                        _lsuPerformance = performanceInternationalFrSub401;
                    }
                } else {
                    /**
                     * ********  <h1>Continuing College</h1>*********
                     */
                    if (std.getStudentPGpa().compareTo(new BigDecimal(2.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 2.5 ){
                        _lsuPerformance = performanceInternationalSub250;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.0)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.0 ){
                        _lsuPerformance = performanceInternationalSub300;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.5)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.5 ){
                        _lsuPerformance = performanceInternationalSub350;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) < 0) {// TRIM(SAVE_STUDENT_P_GPA)  < 3.8 ){
                        _lsuPerformance = performanceInternationalSub380;
                    } else if (std.getStudentPGpa().compareTo(new BigDecimal(3.8)) >= 0) {// TRIM(SAVE_STUDENT_P_GPA) >= 3.8 ){
                        _lsuPerformance = performanceInternationalSub401;
                    }
                }
            }
        }

        /**
         * ******** LSU Performance is only for undergraduates working on their
         * 1st degree*****
         */
        if (!TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")
                && !TRIM(SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
            _lsuPerformance = 0;
        }

        return _lsuPerformance;
    }

    public void resetGlobalVarXXX() {
        _nonCaGrantDesc = "";
        _nonCaGrantAmt = 0;
        _outsideScholarship = "";
        _outsideScholarshipAmt = 0;

        calGrantA = 0;
        calGrantB = 0;
        lsuAllowance = 0;

        //lsuPerformance      =0;
        lsuNeedGrant = 0;
        //201602
        lasuGrantAmt = 0;

        lsuAchievement = 0;
        lsu4yRenewable = 0;

        nationalMerit = 0;
        churchMatch = 0;
        pacificCampMatch = 0;
        nonPacificCampMatch = 0;
        litEvanMatch = 0;

        //in coldfusion function enforceOverallLimits()
        pellGrant = 0;
        fseogAmt = 0;
        extAllowance = 0;
        nonCaGrantAmt = 0;
        outsideAmt = 0;
        churchBase = 0;
        subDirect = 0;
        perkinsLoan = 0;
        fwsAmount = 0;
        unsubDirect = 0;

        //in coldfusion function getCOA()
        needAmt = 0;

        //================= used in summary.cfm
        tuitionAndFees = 0;
        addlExp = 0;
        efc = efcInit;//  0;

        //some vars defined in estimatorStudentData.cfm
        excludeNote = "exclude loans";

        //some vars defined in estimatorSummary.cfm, and only used in that file.
        lsuLimitSubtotal = 0;
        err = 0;
//        nonCaGrantDesc      = "";
        outsideDesc = "";
        lsuOverallSubtotal = false;
        maxAid = 0;

        roomAndBoard = 0;
        pacificCampBase = 0;
        nonPacificCampBase = 0;
        litEvanBase = 0;
        amtDue = 0;

        yearInAdvanceOption = 0;
        quarterInAdvanceOption = 0;
        monthlyOption = 0;

        /// for scholarship #7 ~ #9  #1 and #2
      scholarship_amt_1 = std.getStudentAuScholarship1Amt();//std.getStudentBgScholarship5Amt();
        scholarship_amt_2 = std.getStudentAxScholarship2Amt();//std.getStudentBjScholarship6Amt();

        scholarship_amt_7 = std.getStudentBmScholarship7Amt();
        scholarship_amt_8 = std.getStudentBpScholarship8Amt();
        scholarship_amt_9 = std.getStudentBsScholarship9Amt();
    }

    public int getUse_need_ind() {
        return use_need_ind;
    }

    public int getSum_tuition_aid() {
        return sum_tuition_aid;
    }

    public int getSum_total_aid() {
        return sum_total_aid;
    }

    public int getSum_lasu_aid() {
        return sum_lasu_aid;
    }

    /**
     * @return the org_loan_amt_sub
     */
    public int getOrg_loan_amt_sub() {
        return org_loan_amt_sub;
    }

    /**
     * @return the org_loan_amt_unsub
     */
    public int getOrg_loan_amt_unsub() {
        return org_loan_amt_unsub;
    }

    /**
     * @return the org_loan_amt_perkins
     */
    public int getOrg_loan_amt_perkins() {
        return org_loan_amt_perkins;
    }

    public String showAmt(int in_amt) {
        return fmt.format(in_amt);
    }

    public int getYearInAdvanceDiscountPerc() {
        return yearInAdvanceDiscountPerc;
    }

    public int getQuarterInAdvanceDiscountPerc() {
        return quarterInAdvanceDiscountPerc;
    }
}

/*     
        if( std.getStudentXFafsa().equalsIgnoreCase("YES")){ //qualify for unsubdirect loans            
            initAndShowPellGrantAmt();
            int  due1 = amtDue;
            int loan1 = unsubDirect;
            
            std.setStudentXFafsa("No");
            int orgEfc = std.getStudentAfFamilyContrib();
            std.setStudentAfFamilyContrib(0);
            String orgEfcInd = std.getIndEfc();
            std.setIndEfc("No");            
            initAndShowPellGrantAmt();
            int  due2 = amtDue;            
            
            std.setStudentXFafsa("Yes");
            std.setStudentAfFamilyContrib(orgEfc);
            std.setIndEfc(orgEfcInd);
                
            if( due1 > due2-loan1){ //better chose method2
                ; //keep it?                
                efc = orgEfc;
                showEFC();// to calc needAmt
                //needAmt = 
                initdone_ind =1;
            }else{ //undo changes, or let it run (from start to the end) and rewrite everything, like nothing has happened                
                //initAndShowPellGrantAmt();
                initdone_ind=0;
            }            
        }else{            
        }
 */
