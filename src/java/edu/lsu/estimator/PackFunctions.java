/*      */ package edu.lsu.estimator;

/*      */
 /*      */ import com.kingombo.slf5j.Logger;
/*      */ import com.kingombo.slf5j.LoggerFactory;
/*      */ import edu.lsu.estimator.AppReference;
/*      */ import edu.lsu.estimator.PackValues;
/*      */ import edu.lsu.estimator.Student;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import javax.enterprise.context.Dependent;
/*      */ import javax.inject.Inject;

/*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ @Dependent
/*      */ public class PackFunctions
        /*      */ implements Serializable /*      */ {

    /*      */ private static final long serialVersionUID = 1L;
    /*   37 */    private NumberFormat fmt = new DecimalFormat("$#,###");
    /*      */
 /*      */    private Student std;
    /*   40 */    private static final Logger log = LoggerFactory.getLogger();
    /*      */
 /*      */
 /*      */    @Inject
    /*      */ AppReference ref;
                 
               
    /*      */
 /*   46 */    private int SAVE_STUDENT_FISY = 2012;
    /*   47 */    private int SAVE_STUDENT_NUMBER = 9999;
    /*   48 */    private String SAVE_STUDENT_A_LSUID = "";
    /*   49 */    private String SAVE_STUDENT_B_LASTNAME = "";
    /*   50 */    private String SAVE_STUDENT_C_FIRSTNAME = "";
    /*      */
 /*   52 */    private String SAVE_STUDENT_D_DOB = "";
    /*      */
 /*   54 */    private String SAVE_STUDENT_E_EMAIL = "";
    /*   55 */    private String SAVE_STUDENT_F_PHONE = "";
    /*   56 */    private String SAVE_STUDENT_G_STREET = "";
    /*   57 */    private String SAVE_STUDENT_H_CITY = "";
    /*   58 */    private String SAVE_STUDENT_I_STATE = "";
    /*   59 */    private String SAVE_STUDENT_J_ZIP = "";
    /*   60 */    private String SAVE_STUDENT_K_COUNTRY = "";
    /*   61 */    private String SAVE_STUDENT_L_INTL_STUD = "No";
    /*   62 */    private String SAVE_STUDENT_M_MARRY = "Single";
    /*   63 */    private String SAVE_STUDENT_N_SDA = "No";
    /*   64 */    private String SAVE_STUDENT_O_LAST_SCHOOL = "";
    /*   65 */    private String SAVE_STUDENT_P_GPA = "0.00";
    /*   66 */    private String SAVE_STUDENT_Q_SAT = "0";
    /*   67 */    private String SAVE_STUDENT_Q_SAT_V = "0";
    /*   68 */    private String SAVE_STUDENT_R_ACT = "0";
    /*   69 */    private String SAVE_STUDENT_S_MERIT = "";
    /*   70 */    private String SAVE_STUDENT_T_MAJOR = "";
    /*   71 */    private String SAVE_STUDENT_U_ACADEMIC = "FR";
    /*   72 */    private String SAVE_STUDENT_V_FAMILY = "";
    /*   73 */    private String SAVE_STUDENT_W_DORM = "No";
    /*   74 */    private String SAVE_STUDENT_X_FAFSA = "No";
                  private String SAVE_STUDENT_F_PELLGRANT= "No";
    /*   75 */    private String SAVE_STUDENT_Y_INDEPT = "No";
    /*   76 */    private String SAVE_STUDENT_Z_CALGRANT = "No";
    /*   77 */    private String SAVE_STUDENT_AA_CALGRANT_A = "0";
    /*   78 */    private String SAVE_STUDENT_AB_CALGRANT_B = "0";
    /*   79 */    private String SAVE_STUDENT_AC_FAMILY_SIZE = "2";
    /*   80 */    private String SAVE_STUDENT_AD_FAMILY_INCOME = "0";
    /*   81 */    private String SAVE_STUDENT_AE_FAMILY_ASSET = "0";
    /*   82 */    private String SAVE_STUDENT_AF_FAMILY_CONTRIB = "0";
    /*   83 */    private String SAVE_STUDENT_AG_NONLSU_ALLOWRANCE = "0";
    /*   84 */    private String SAVE_STUDENT_AH_LSU_ALLOWRANCE = "No";
    /*   85 */    private String SAVE_STUDENT_AI_EDU_ALLOW_PER = "0";
    /*   86 */    private String SAVE_STUDENT_AJ_HOME_STATE = "";
    /*   87 */    private String SAVE_STUDENT_AK_NONCAL_GRANT = "0";
    /*   88 */    private String SAVE_STUDENT_AL_OUT_SCHOLARSHIPS = "";
    /*   89 */    private String SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT = "0";
    /*   90 */    private String SAVE_STUDENT_AN_PUB_NOTES = "";
    /*   91 */    private String SAVE_STUDENT_AO_PRI_NOTES = "";
    /*   92 */    private String SAVE_STUDENT_AP_SUB_LOANS = "Yes";
    /*   93 */    private String SAVE_STUDENT_AQ_UNSUB_LOANS = "Yes";
    /*   94 */    private String SAVE_STUDENT_AR_FWS = "Yes";
    /*      */
 /*      */
 /*   97 */    private String SAVE_STUDENT_AS_SCHOLARSHIP1_NAME = "Church Funds Match";
    /*   98 */    private String SAVE_STUDENT_AT_SCHOLARSHIP1_NOTE = "";
    /*   99 */    private String SAVE_STUDENT_AU_SCHOLARSHIP1_AMT = "0";
    /*  100 */    private String SAVE_STUDENT_AV_SCHOLARSHIP2_NAME = "Summer Camp Earnings (Match)";
    /*  101 */    private String SAVE_STUDENT_AW_SCHOLARSHIP2_NOTE = "";
    /*  102 */    private String SAVE_STUDENT_AX_SCHOLARSHIP2_AMT = "0";
    /*  103 */    private String SAVE_STUDENT_AY_SCHOLARSHIP3_NAME = "Summer Camp Earnings (Match)";
    /*  104 */    private String SAVE_STUDENT_AZ_SCHOLARSHIP3_NOTE = "";
    /*  105 */    private String SAVE_STUDENT_BA_SCHOLARSHIP3_AMT = "0";
    /*  106 */    private String SAVE_STUDENT_BB_SCHOLARSHIP4_NAME = "Literature Evangelist Earnings (Match)";
    /*  107 */    private String SAVE_STUDENT_BC_SCHOLARSHIP4_NOTE = "";
    /*  108 */    private String SAVE_STUDENT_BD_SCHOLARSHIP4_AMT = "0";
    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  130 */    private String SAVE_SCHOLARSHIP1_NAME = "";
    /*  131 */    private String SAVE_SCHOLARSHIP1_NOTE = "";
    /*  132 */    private String SAVE_SCHOLARSHIP1_AMT = "0";
    /*      */
 /*  134 */    private String SAVE_SCHOLARSHIP2_NAME = "";
    /*  135 */    private String SAVE_SCHOLARSHIP2_NOTE = "";
    /*  136 */    private String SAVE_SCHOLARSHIP2_AMT = "0";
    /*      */
 /*  138 */    private String SAVE_SCHOLARSHIP7_NAME = "";
    /*  139 */    private String SAVE_SCHOLARSHIP7_NOTE = "";
    /*  140 */    private String SAVE_SCHOLARSHIP7_AMT = "0";
    /*      */
 /*  142 */    private String SAVE_SCHOLARSHIP8_NAME = "";
    /*  143 */    private String SAVE_SCHOLARSHIP8_NOTE = "";
    /*  144 */    private String SAVE_SCHOLARSHIP8_AMT = "0";
    /*      */
 /*  146 */    private String SAVE_SCHOLARSHIP9_NAME = "";
    /*  147 */    private String SAVE_SCHOLARSHIP9_NOTE = "";
    /*  148 */    private String SAVE_SCHOLARSHIP9_AMT = "0";
    /*      */
 /*      */
 /*      */
 /*  152 */    private String SAVE_STUDENT_BT_SUPERCOUNSELOR = "";
    /*  153 */    private String SAVE_STUDENT_BU_ORIG_COUNSELOR = " x ";
    /*  154 */    private Date SAVE_STUDENT_BV_DOE = new Date();
    /*  155 */    private String SAVE_STUDENT_BW_PROGRESS = "4";
    /*  156 */    private String SAVE_STUDENT_BX_MOD_COUNSELOR = "";
    /*  157 */    private Date SAVE_STUDENT_BY_DOM = new Date();
    /*  158 */    private String SAVE_STUDENT_BZ_UPLOADED = "No";
    /*  159 */    private String SAVE_STUDENT_CB_BANNER = "No";
    /*  160 */    private String SAVE_STUDENT_USER_NAME = "";
    /*  161 */    private String SAVE_STUDENT_PASSWORD = "auto";
    /*  162 */    private String SAVE_STUDENT_STUD_TYPE = "UGFY";
    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  169 */    private int efc = 99999;
    /*  170 */    private int sdaAward = PackValues.sdaAwardInit; //_lsuLimitSubtotal //no change 2023
    /*      */
 /*  172 */    private int familyDiscount = 900;
    /*      */
 /*      */
 /*  175 */    private String _nonCaGrantDesc = "";
    /*  176 */    private int _nonCaGrantAmt = 0;
    /*  177 */    private String _outsideScholarship = "";
    /*  178 */    private int _outsideScholarshipAmt = 0;
    /*      */
 /*  180 */    private int calGrantA = 0;
    /*  181 */    private int calGrantB = 0;
    /*  182 */    private int lsuAllowance = 0;
    /*      */
 /*  184 */    private int lsuPerformance = 0;
    /*      */
 /*  186 */    private int nationalMerit = 0;
    /*  187 */    private int churchMatch = 0;
    /*  188 */    private int pacificCampMatch = 0;
    /*  189 */    private int nonPacificCampMatch = 0;
    /*  190 */    private int litEvanMatch = 0;
    /*      */
 /*      */
 /*      */
 /*  194 */    private int pellGrant = 0;
    /*  195 */    private int fseogAmt = 0;
    /*  196 */    private int extAllowance = 0;
    /*  197 */    private int nonCaGrantAmt = 0;
    /*  198 */    private int outsideAmt = 0;
    /*  199 */    private int churchBase = 0;
    /*  200 */    private int subDirect = 0;
    /*  201 */    private int perkinsLoan = 0;
    /*  202 */    private int fwsAmount = 0;
    /*  203 */    private int unsubDirect = 0;
    /*      */
 /*      */
 /*  206 */    private int needAmt = 0;
    /*      */
 /*      */
 /*      */
 /*  210 */       private int tuitionAndFees = 0;
    /*  211 */    private int addlExp = 0;
    /*      */
 /*      */
 /*      */
 /*  215 */    private String excludeNote = "exclude loans";
    /*      */
 /*      */
 /*  218 */    private int lsuLimitSubtotal = 0;
    /*  219 */    private int err = 0;
    /*  220 */    private String nonCaGrantDesc = "";
    /*  221 */    private String outsideDesc = "";
    /*      */    private boolean lsuOverallSubtotal = false;
    /*  223 */    private int maxAid = 0;
    /*      */
 /*  225 */    private int roomAndBoard = 0;
    /*  226 */    private int pacificCampBase = 0;
    /*  227 */    private int nonPacificCampBase = 0;
    /*  228 */    private int litEvanBase = 0;
    /*  229 */    private int amtDue = 0;
    /*      */
 /*  231 */    private int yearInAdvanceOption = 0;
    /*  232 */    private int quarterInAdvanceOption = 0;
    /*  233 */    private int monthlyOption = 0;
    /*      */
 /*      */
 /*      */
 /*  237 */    private int ea_lsu_per = 100;
    /*  238 */    private int ea_nonlsu_per = 35;
    /*  239 */    private int ea_nonlsu_dorm_per = 70;
    /*      */
 /*      */
 /*      */
 /*      */    private boolean adjust_calgrant_amt_ind = false;
    /*      */
 /*      */
 /*  246 */    private int scholarship_amt_1 = 0;
    /*  247 */    private int scholarship_amt_2 = 0;
    /*      */
 /*  249 */    private int scholarship_amt_7 = 0;
    /*  250 */    private int scholarship_amt_8 = 0;
    /*  251 */    private int scholarship_amt_9 = 0;
    /*      */
 /*      */
 /*  254 */    private int lsu4yRenewable = 0;

    public int getUniversityHouseGrant() {
        return universityHouseGrant;
    }

    public void setUniversityHouseGrant(int universityHouseGrant) {
        this.universityHouseGrant = universityHouseGrant;
    }
                private int universityHouseGrant=0;
    /*  255 */    private int lsuAchievement = 0;
    /*  256 */    private int lsuAchievementInit = PackValues.lsuAchievementInit;//10000
    /*  257 */    private int lsuNeedGrant = 0;
    /*      */
 /*  259 */    private int lasuGrantAmt = 0;
    /*      */
 /*      */
 /*  262 */    private int initdone_ind = 0;
    /*  263 */    private int use_need_ind = -1;
    /*      */
 /*  265 */    private int sum_tuition_aid = 0;
    /*  266 */    private int sum_total_aid = 0;
    /*  267 */    private int sum_lasu_aid = 0;
    /*      */
 /*      */
 /*  270 */    private int org_loan_amt_sub = 0;
    /*  271 */    private int org_loan_amt_unsub = 0;
    /*  272 */    private int org_loan_amt_perkins = 0;
    /*      */
 /*  274 */    private final int yearInAdvanceDiscountPerc = PackValues.yearInAdvanceDiscount.movePointRight(2).intValue();
    /*  275 */    private final int quarterInAdvanceDiscountPerc = PackValues.quarterInAdvanceDiscount.movePointRight(2).intValue();
    /*      */    private edu.lsu.estimator.PackFunctions actor;

    /*      */
 /*      */

   
public void resetValues() {
        /*  279 */ this.sdaAward = PackValues.sdaAwardInit;
        /*      */
 /*      */
 /*  282 */ this.familyDiscount =PackValues.familyDisctInit;// 900;
        /*      */
 /*      */  this.universityHouseGrant=0;
 /*  285 */ this._nonCaGrantDesc = "";
        /*  286 */ this._nonCaGrantAmt = 0;
        /*  287 */ this._outsideScholarship = "";
        /*  288 */ this._outsideScholarshipAmt = 0;
        /*      */
 /*  290 */ this.calGrantA = 0;
        /*  291 */ this.calGrantB = 0;
        /*  292 */ this.lsuAllowance = 0;
        /*      */
 /*  294 */ this.lsuPerformance = 0;
        /*      */
 /*  296 */ this.nationalMerit = 0;
        /*  297 */ this.churchMatch = 0;
        /*  298 */ this.pacificCampMatch = 0;
        /*  299 */ this.nonPacificCampMatch = 0;
        /*  300 */ this.litEvanMatch = 0;
        /*      */
 /*      */
 /*  303 */ this.pellGrant = 0;
        /*  304 */ this.fseogAmt = 0;
        /*  305 */ this.extAllowance = 0;
        /*  306 */ this.nonCaGrantAmt = 0;
        /*  307 */ this.outsideAmt = 0;
        /*  308 */ this.churchBase = 0;
        /*  309 */ this.subDirect = 0;
        /*  310 */ this.perkinsLoan = 0;
        /*  311 */ this.fwsAmount = 0;
        /*  312 */ this.unsubDirect = 0;
        /*      */
 /*      */
 /*  315 */ this.needAmt = 0;
        /*      */
 /*      */
 /*  318 */ this.tuitionAndFees = 0;
        /*  319 */ this.addlExp = 0;
        /*  320 */ this.efc = 99999;
        /*      */
 /*      */
 /*  323 */ this.excludeNote = "exclude loans";
        /*      */
 /*      */
 /*  326 */ this.lsuLimitSubtotal = 0;
        /*  327 */ this.err = 0;
        /*      */
 /*  329 */ this.outsideDesc = "";
        /*  330 */ this.lsuOverallSubtotal = false;
        /*  331 */ this.maxAid = 0;
        /*      */
 /*  333 */ this.roomAndBoard = 0;
        /*  334 */ this.pacificCampBase = 0;
        /*  335 */ this.nonPacificCampBase = 0;
        /*  336 */ this.litEvanBase = 0;
        /*  337 */ this.amtDue = 0;
        /*      */
 /*  339 */ this.yearInAdvanceOption = 0;
        /*  340 */ this.quarterInAdvanceOption = 0;
        /*  341 */ this.monthlyOption = 0;
        /*      */
 /*      */
 /*      */
 /*  345 */ this.ea_lsu_per = 100;
        /*  346 */ this.ea_nonlsu_per = 35;
        /*  347 */ this.ea_nonlsu_dorm_per = 70;
        /*      */
 /*      */
 /*  350 */ this.adjust_calgrant_amt_ind = false;
        /*      */
 /*      */
 /*      */
 this.universityHouseGrant=0;
 /*  354 */ this.lsu4yRenewable = 0;
        /*  355 */ this.lsuAchievement = 0;
        /*  356 */ this.lsuNeedGrant = 0;
        /*      */
 /*  358 */ this.lasuGrantAmt = 0;
        /*      */
 /*      */
 /*      */
 /*  362 */ this.scholarship_amt_1 = this.std.getStudentAuScholarship1Amt()!=null ? this.std.getStudentAuScholarship1Amt().intValue() :this.scholarship_amt_1 ;
        /*  363 */ this.scholarship_amt_2 = this.std.getStudentAxScholarship2Amt()!=null ? this.std.getStudentAxScholarship2Amt().intValue() :this.scholarship_amt_2 ;
        /*      */
 /*  365 */ this.scholarship_amt_7 = this.std.getStudentBmScholarship7Amt() !=null ? this.std.getStudentBmScholarship7Amt().intValue(): this.scholarship_amt_7 ;
        /*  366 */ this.scholarship_amt_8 = this.std.getStudentBpScholarship8Amt() !=null ? this.std.getStudentBpScholarship8Amt().intValue() : this.scholarship_amt_8 ;
        /*  367 */ this.scholarship_amt_9 =  this.std.getStudentBsScholarship9Amt() !=null ? this.std.getStudentBsScholarship9Amt().intValue() : this.scholarship_amt_9;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  380 */ this.initdone_ind = 0;
        /*  381 */ this.use_need_ind = -1;
        /*      */
 /*  383 */ this.sum_tuition_aid = 0;
        /*  384 */ this.sum_total_aid = 0;
        /*  385 */ this.sum_lasu_aid = 0;
        /*      */
 /*  387 */ this.org_loan_amt_sub = 0;
        /*  388 */ this.org_loan_amt_unsub = 0;
        /*  389 */ this.org_loan_amt_perkins = 0;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void newPackFunctions(Student std) {
        /*  401 */ setStd(std);
        /*      */    }

    /*      */
 /*      */ public void setStd(Student std) {
        /*  405 */ this.std = std;
        /*  406 */ RefreshStudData(std);
        /*      */
 /*  408 */ resetValues();
        /*      */    }

    /*      */
 /*      */
 /*      */ public void RefreshStudData(Student std) {
        /*  413 */ this.SAVE_STUDENT_FISY = std.getStudentFisy();
        /*      */
 /*  415 */ this.SAVE_STUDENT_NUMBER = 0;
        /*  416 */ this.SAVE_STUDENT_A_LSUID = std.getStudentALsuid();
        /*  417 */ this.SAVE_STUDENT_B_LASTNAME = std.getStudentBLastname();
        /*  418 */ this.SAVE_STUDENT_C_FIRSTNAME = std.getStudentCFirstname();
        /*  419 */ this.SAVE_STUDENT_D_DOB = std.getStudentDDob();
        /*  420 */ this.SAVE_STUDENT_E_EMAIL = std.getStudentEEmail();
        /*  421 */ this.SAVE_STUDENT_F_PHONE = std.getStudentFPhone();
        /*  422 */ this.SAVE_STUDENT_G_STREET = std.getStudentGStreet();
        /*  423 */ this.SAVE_STUDENT_H_CITY = std.getStudentGStreet();
        /*  424 */ this.SAVE_STUDENT_I_STATE = std.getStudentIState();
        /*  425 */ this.SAVE_STUDENT_J_ZIP = std.getStudentJZip();
        /*  426 */ this.SAVE_STUDENT_K_COUNTRY = std.getStudentKCountry();
        /*  427 */ this.SAVE_STUDENT_L_INTL_STUD = std.getStudentLIntlStud();
        /*  428 */ this.SAVE_STUDENT_M_MARRY = std.getStudentMMarry();
        /*  429 */ this.SAVE_STUDENT_N_SDA = std.getStudentNSda();
        /*  430 */ this.SAVE_STUDENT_O_LAST_SCHOOL = std.getStudentOLastSchool();
        /*  431 */ this.SAVE_STUDENT_P_GPA = std.getStudentPGpa()!=null ? std.getStudentPGpa().toString(): this.SAVE_STUDENT_P_GPA;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  437 */ this.SAVE_STUDENT_Q_SAT = String.valueOf(std.getStudentQSat());
        /*  438 */ this.SAVE_STUDENT_Q_SAT_V = String.valueOf(std.getStudentQSatV());
        /*  439 */ this.SAVE_STUDENT_R_ACT = String.valueOf(std.getStudentRAct());
        /*  440 */ this.SAVE_STUDENT_S_MERIT = std.getStudentSMerit();
        /*  441 */ this.SAVE_STUDENT_T_MAJOR = std.getStudentTMajor();
        /*  442 */ this.SAVE_STUDENT_U_ACADEMIC = std.getStudentUAcademic();
        /*  443 */ this.SAVE_STUDENT_V_FAMILY = std.getStudentVFamily();
        /*  444 */ this.SAVE_STUDENT_W_DORM = std.getStudentWDorm();
        /*  445 */ this.SAVE_STUDENT_X_FAFSA = std.getStudentXFafsa()!=null?std.getStudentXFafsa().trim():this.SAVE_STUDENT_X_FAFSA;
                   this.SAVE_STUDENT_F_PELLGRANT= std.getStudentFPellGrant() !=null? std.getStudentFPellGrant().trim():this.SAVE_STUDENT_F_PELLGRANT;
        /*  446 */ this.SAVE_STUDENT_Y_INDEPT = std.getStudentYIndept();
        /*  447 */ this.SAVE_STUDENT_Z_CALGRANT = std.getStudentZCalgrant();
        /*  448 */ this.SAVE_STUDENT_AA_CALGRANT_A = std.getStudentAaCalgrantA()!=null ? String.valueOf(std.getStudentAaCalgrantA()): this.SAVE_STUDENT_AA_CALGRANT_A;
        /*  449 */ this.SAVE_STUDENT_AB_CALGRANT_B = std.getStudentAbCalgrantB() !=null ? String.valueOf(std.getStudentAbCalgrantB()):this.SAVE_STUDENT_AB_CALGRANT_B;
        /*  450 */ this.SAVE_STUDENT_AC_FAMILY_SIZE = std.getStudentAcFamilySize()!=null ? String.valueOf(std.getStudentAcFamilySize()):this.SAVE_STUDENT_AC_FAMILY_SIZE;
        /*  451 */ this.SAVE_STUDENT_AD_FAMILY_INCOME = std.getStudentAdFamilyIncome()!=null ? String.valueOf(std.getStudentAdFamilyIncome()):this.SAVE_STUDENT_AD_FAMILY_INCOME;
        /*  452 */ this.SAVE_STUDENT_AE_FAMILY_ASSET = std.getStudentAeFamilyAsset() !=null ? String.valueOf(std.getStudentAeFamilyAsset()) :this.SAVE_STUDENT_AE_FAMILY_ASSET ;
        /*  453 */ this.SAVE_STUDENT_AF_FAMILY_CONTRIB = std.getStudentAfFamilyContrib() !=null ? String.valueOf(std.getStudentAfFamilyContrib()):this.SAVE_STUDENT_AF_FAMILY_CONTRIB;
        /*  454 */ this.SAVE_STUDENT_AG_NONLSU_ALLOWRANCE = std.getStudentAgNonlsuAllowrance() !=null ? String.valueOf(std.getStudentAgNonlsuAllowrance()) : this.SAVE_STUDENT_AG_NONLSU_ALLOWRANCE;
        /*  455 */ this.SAVE_STUDENT_AH_LSU_ALLOWRANCE = std.getStudentAhLsuAllowrance();
        /*  456 */ this.SAVE_STUDENT_AI_EDU_ALLOW_PER =  std.getStudentAiEduAllowPer()!=null ? std.getStudentAiEduAllowPer().toString() : this.SAVE_STUDENT_AI_EDU_ALLOW_PER ;
        /*      */
 /*  458 */ this.SAVE_STUDENT_AI_EDU_ALLOW_PER = std.getStudentAiEduAllowPer() !=null ? std.getStudentAiEduAllowPer().multiply(new BigDecimal(100)).toString() : this.SAVE_STUDENT_AI_EDU_ALLOW_PER ;
        /*      */
 /*  460 */ this.SAVE_STUDENT_AJ_HOME_STATE = std.getStudentAjHomeState();
        /*  461 */ this.SAVE_STUDENT_AK_NONCAL_GRANT = std.getStudentAkNoncalGrant() !=null ? String.valueOf(std.getStudentAkNoncalGrant()) : this.SAVE_STUDENT_AK_NONCAL_GRANT;
        /*  462 */ this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS = std.getStudentAlOutScholarships();
        /*  463 */ this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT = std.getStudentAmOutScholarshipAmt() !=null ? String.valueOf(std.getStudentAmOutScholarshipAmt()) : this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT; 
        /*  464 */ this.SAVE_STUDENT_AN_PUB_NOTES = std.getStudentAnPubNotes();
        /*  465 */ this.SAVE_STUDENT_AO_PRI_NOTES = std.getStudentAoPriNotes();
        /*  466 */ this.SAVE_STUDENT_AP_SUB_LOANS = std.getStudentApSubLoans();
        /*  467 */ this.SAVE_STUDENT_AQ_UNSUB_LOANS = std.getStudentAqUnsubLoans();
        /*  468 */ this.SAVE_STUDENT_AR_FWS = std.getStudentArFws();
        /*      */
 /*  470 */ this.SAVE_STUDENT_AS_SCHOLARSHIP1_NAME = std.getStudentAyScholarship3Name();
        /*  471 */ this.SAVE_STUDENT_AT_SCHOLARSHIP1_NOTE = std.getStudentAzScholarship3Note();
        /*  472 */ this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT = std.getStudentBaScholarship3Amt() !=null ? String.valueOf(std.getStudentBaScholarship3Amt()) :  this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT;
        /*      */
 /*  474 */ this.SAVE_STUDENT_AV_SCHOLARSHIP2_NAME = std.getStudentBeScholarship5Name()!=null ? std.getStudentBeScholarship5Name():this.SAVE_STUDENT_AV_SCHOLARSHIP2_NAME;
        /*  475 */ this.SAVE_STUDENT_AW_SCHOLARSHIP2_NOTE = std.getStudentBfScholarship5Note() !=null ?std.getStudentBfScholarship5Note():this.SAVE_STUDENT_AW_SCHOLARSHIP2_NOTE;
        /*  476 */ this.SAVE_STUDENT_AX_SCHOLARSHIP2_AMT = std.getStudentBgScholarship5Amt()!=null ? String.valueOf(std.getStudentBgScholarship5Amt()) : this.SAVE_STUDENT_AX_SCHOLARSHIP2_AMT;
        /*      */
 /*  478 */ this.SAVE_STUDENT_AY_SCHOLARSHIP3_NAME = std.getStudentBhScholarship6Name();
        /*  479 */ this.SAVE_STUDENT_AZ_SCHOLARSHIP3_NOTE = std.getStudentBiScholarship6Note();
        /*  480 */ this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT = std.getStudentBjScholarship6Amt() !=null ? String.valueOf(std.getStudentBjScholarship6Amt()) : this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT;
        /*      */
 /*  482 */ this.SAVE_STUDENT_BB_SCHOLARSHIP4_NAME = std.getStudentBbScholarship4Name();
        /*  483 */ this.SAVE_STUDENT_BC_SCHOLARSHIP4_NOTE = std.getStudentBcScholarship4Note();
        /*  484 */ this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT = std.getStudentBdScholarship4Amt()!=null ? String.valueOf(std.getStudentBdScholarship4Amt()) : this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT;
        /*      */
 /*  486 */ this.SAVE_SCHOLARSHIP1_NAME = std.getStudentAsScholarship1Name();
        /*  487 */ this.SAVE_SCHOLARSHIP1_NOTE = std.getStudentAtScholarship1Note();
        /*  488 */ this.SAVE_SCHOLARSHIP1_AMT =std.getStudentAuScholarship1Amt()!=null ? String.valueOf(std.getStudentAuScholarship1Amt()): this.SAVE_SCHOLARSHIP1_AMT ;
        /*      */
 /*  490 */ this.SAVE_SCHOLARSHIP2_NAME = std.getStudentAvScholarship2Name();
        /*  491 */ this.SAVE_SCHOLARSHIP2_NOTE = std.getStudentAwScholarship2Note();
        /*  492 */ this.SAVE_SCHOLARSHIP2_AMT = std.getStudentAxScholarship2Amt()!=null ?  String.valueOf(std.getStudentAxScholarship2Amt()) :  this.SAVE_SCHOLARSHIP2_AMT;
        /*      */
 /*  494 */ this.SAVE_SCHOLARSHIP7_NAME = std.getStudentBkScholarship7Name();
        /*  495 */ this.SAVE_SCHOLARSHIP7_NOTE = std.getStudentBlScholarship7Note();
        /*  496 */ this.SAVE_SCHOLARSHIP7_AMT = std.getStudentBmScholarship7Amt() !=null ? String.valueOf(std.getStudentBmScholarship7Amt()) : this.SAVE_SCHOLARSHIP7_AMT;
        /*      */
 /*  498 */ this.SAVE_SCHOLARSHIP8_NAME = std.getStudentBnScholarship8Name();
        /*  499 */ this.SAVE_SCHOLARSHIP8_NOTE = std.getStudentBoScholarship8Note();
        /*  500 */ this.SAVE_SCHOLARSHIP8_AMT = std.getStudentBpScholarship8Amt() !=null ? String.valueOf(std.getStudentBpScholarship8Amt()) : this.SAVE_SCHOLARSHIP8_AMT ;
        /*      */
 /*  502 */ this.SAVE_SCHOLARSHIP9_NAME = std.getStudentBqScholarship9Name();
        /*  503 */ this.SAVE_SCHOLARSHIP9_NOTE = std.getStudentBrScholarship9Note();
        /*  504 */ this.SAVE_SCHOLARSHIP9_AMT = std.getStudentBsScholarship9Amt()!=null ? String.valueOf(std.getStudentBsScholarship9Amt()) : this.SAVE_SCHOLARSHIP9_AMT ;
        /*      */
 /*  506 */ this.SAVE_STUDENT_BT_SUPERCOUNSELOR = std.getStudentBtSupercounselor();
        /*  507 */ this.SAVE_STUDENT_BU_ORIG_COUNSELOR = std.getStudentBuOrigCounselor();
        /*  508 */ this.SAVE_STUDENT_BV_DOE = std.getStudentBvDoe();
        /*  509 */ this.SAVE_STUDENT_BW_PROGRESS = std.getStudentBwProgress() !=null ? std.getStudentBwProgress().toString() :this.SAVE_STUDENT_BW_PROGRESS;
        /*  510 */ this.SAVE_STUDENT_BX_MOD_COUNSELOR = std.getStudentBxModCounselor();
        /*  511 */ this.SAVE_STUDENT_BY_DOM = std.getStudentByDom();
        /*  512 */ this.SAVE_STUDENT_BZ_UPLOADED = std.getStudentBzUploaded();
        /*  513 */ this.SAVE_STUDENT_CB_BANNER = std.getStudentCbBanner();
        /*  514 */ this.SAVE_STUDENT_USER_NAME = std.getStudentUserName();
        /*  515 */ this.SAVE_STUDENT_PASSWORD = std.getStudentPassword();
        /*  516 */ this.SAVE_STUDENT_STUD_TYPE = std.getStudentStudType();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public int getLoanAmtAfterOrigination(int loanAmt) {
        /*  525 */ return loanAmt * (100000 - this.ref.getLoan_origination_perc().intValue()) / 100000;
        /*      */    }

    /*      */ public int divAmtToFirstTwoTerms(int amt) {
        /*  528 */ return amt * 1 / 3;
        /*      */    }

    /*      */ public int divAmtToLastTerm(int amt) {
        /*  531 */ return amt - amt * 2 / 3;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final String TRIM(String str) {
        /*  536 */ return (str == null) ? "" : str.trim();
        /*      */    }

    /*      */
 /*      */ public int round(float f) {
        /*  540 */ return Math.round(f);
        /*      */    }

    /*      */
 /*      */ public String MID(String str, int start, int end) {
        /*  544 */ if (str == null || str.trim().isEmpty() || start <= 0 || end <= 0) {
            return "";
        }
        /*  545 */ if (start > str.length() + 1) {
            return "";
        }
        /*  546 */ if (start + end <= str.length()) {
            /*  547 */ return str.substring(start - 1, start + end - 1);
            /*      */        }
        /*  549 */ return str.substring(start - 1);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final String getInternationalStatus() {
        /*  559 */ if (TRIM(this.SAVE_STUDENT_L_INTL_STUD).equalsIgnoreCase("Yes") && this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("No")) {
            /*  560 */ return "International";
            /*      */        }
        /*  562 */ return "Domestic";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final String getIndependentStatus() {
        /*  568 */ if (TRIM(this.SAVE_STUDENT_Y_INDEPT).equalsIgnoreCase("Yes")) {
            /*  569 */ return "Independent";
            /*      */        }
        /*  571 */ return "Dependent";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getRoomAndBoard() {
        /*  577 */ if (TRIM(this.SAVE_STUDENT_W_DORM).equalsIgnoreCase("Yes")) {
            /*  578 */ return PackValues.roomAndBoard;//return 8790;
            /*      */        }
        /*  580 */ return 0;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getEFC() {
        /*  585 */ int _familyContrib =Integer.parseInt(this.SAVE_STUDENT_AF_FAMILY_CONTRIB);
                   
        /*  586 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") &&  _familyContrib>0) {
         
 /*  592 */                 return _familyContrib;
            /*      */        } 
        /*  596 */ return 0;
        /*      */    }

    /*      */
 /*      */
 
 //Tutition Fees should go here
 /*      */ public final int getTuitionAndFees() {
        /*  601 */ int _tAndF = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  610 */ if (getInternationalStatus()!=null && getInternationalStatus().equalsIgnoreCase("Domestic")) {
            /*  611 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                /*  612 */ _tAndF = PackValues.yTotalTuitionAndFeesDomesticFr;//36130; //new fees with tution & general fees  old - 34438;
                /*      */            } else {
                /*  614 */ _tAndF = PackValues.yTotalTuitionAndFeesDomestic;//35910;//old values 34218
                /*      */            }
            /*      */
 /*  617 */        } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
            /*  618 */ _tAndF = PackValues.yTotalTuitionAndFeesInternationalFr;//37338;
            /*      */        } else {
            /*  620 */ _tAndF = PackValues.yTotalTuitionAndFeesInternational;//;/37118;
            /*      */        }
        /*      */
 /*  623 */ return _tAndF;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getCOA() {
        /*  628 */ if (this.needAmt > 0) {
            /*  629 */ return this.needAmt;
            /*      */        }
        /*  631 */ return 0;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getCOL() {
        /*  636 */ return getOtherExpenses();
        /*      */    }

    /*      */ public final int getMPA() {
        /*  639 */ if (this.use_need_ind > 0) {
            /*  640 */ int amt = getOtherExpenses() - getEFC();
            /*  641 */ return (amt > 0.01D) ? amt : 0;
            /*      */        }
        /*  643 */ return getOtherExpenses();
        /*      */    }

    /*      */
 /*      */ public String showMPA() {
        /*  647 */ return this.fmt.format(getMPA());
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getFallOrientationFee() {
        /*  652 */ int _fallOrFee = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  659 */ if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
            /*  660 */ _fallOrFee = 220;
            /*      */        } else {
            /*  662 */ _fallOrFee = 0;
            /*      */        }
        /*  664 */ return _fallOrFee;
        /*      */    }

    /*      */
 /*      */ public final int getOtherExpenses() {
        /*  668 */ int _otherExp = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  676 */ if (TRIM(this.SAVE_STUDENT_L_INTL_STUD).equalsIgnoreCase("no")) {
            /*  677 */ if (TRIM(this.SAVE_STUDENT_W_DORM).equalsIgnoreCase("Yes")) {
                /*  678 */ if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                    /*  679 */ _otherExp = PackValues.yCoaDormFr;
                    /*      */                } else {
                    /*  681 */ _otherExp = PackValues.yCoaDorm;
                    /*      */                }
                /*      */
 /*  684 */            } else if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                /*  685 */ _otherExp = PackValues.yCoaCommunityFr;
                /*      */            } else {
                /*  687 */ _otherExp = PackValues.yCoaCommunity;
                /*      */            }
            /*      */
 /*      */        } /*  691 */ else if (TRIM(this.SAVE_STUDENT_W_DORM).equalsIgnoreCase("Yes")) {
            /*  692 */ if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                /*  693 */ _otherExp = PackValues.yCoaDormFrIntl;
                /*      */            } else {
                /*  695 */ _otherExp = PackValues.yCoaDormIntl;
                /*      */            }
            /*      */
 /*  698 */        } else if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
            /*  699 */ _otherExp = PackValues.yCoaCommunityFrIntl;
            /*      */        } else {
            /*  701 */ _otherExp = PackValues.yCoaCommunityIntl;
            /*      */        }
        /*      */
 /*      */
 /*  705 */ return _otherExp;
        /*      */    }

    /*      */
 /*      */ public int getLasuGrantAmt() {
        /*  709 */ int _lasugrant = 0;
        /*  710 */ boolean zerogpa_ind = this.std.getStudentPGpa()!=null ? this.std.getStudentPGpa().equals(BigDecimal.ZERO):false;
        /*  711 */ String _gpa = String.format("%3.2f", new Object[]{this.std.getStudentPGpa()});
        /*  712 */ int _efc = getEFC();
        /*  713 */ if (_gpa.compareTo("2.00") >= 0 && _gpa.compareTo("2.49") <= 0 && _efc <= 12000) {
            /*  714 */ _lasugrant = 1000;
            /*      */        }
        /*  716 */ return _lasugrant;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showLasuGrantAmt() {
        /*  721 */ return this.fmt.format(this.lasuGrantAmt);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getPell() {
        /*  730 */ //return getPell2021();
        return getPell2425();
        /*      */    }

    /*      */
 /*      */
 //Currently working on 2023 present
 private final int getPell2023()
 {
     //have minimum and maximum imit here 
      int _pell = 0, _efcBase = 0, _pellBase = 0;
      int _coa = getCOA();
 /*  739 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= 6195 && _coa <= 999999) {
            /*  740 */ int _efc = getEFC();
            /*  741 */ if (_efc == 0) {
                /*  742 */ _pell =7395;// 6345;
                /*  743 */            } else if (_efc >= 6646 && _efc <= 6656) { //TBD
                /*  744 */ _pell =750;// 639;
                /*  745 */            } else if (_efc >= 6656) {
                /*  746 */ _pell = 0;
                /*      */            } else {
                /*      */
 /*  749 */ int j = (int) Math.floor(((_efc - 1) / 100)) * 100;
                /*  750 */ _pell = 7345 - j;
                /*      */            }
            /*      */        }
        /*      */
       
        this.setActor(this.actor);
         this.setPellGrant(_pell);
 /*  754 */ return _pell;
 }
 
  private final int getPell2425()
 {
     //have minimum and maximum imit here 
      int _pell = 0, _efcBase = 0, _pellBase = 0 ,pellMax_=PackValues.pellMax,pellMin_=PackValues.pellMin;
      int _coa = getCOA();
 /*  739 */ if (this.SAVE_STUDENT_F_PELLGRANT.equalsIgnoreCase("Yes") 
         && this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") 
         && _coa >= 6195 && _coa <= 999999) {
            /*  740 */ int _efc = getEFC();
            /*  741 */ if (_efc <= 0 ) {
                /*  742 */ _pell =pellMax_;// 6345;
                /*  743 */            } else if (_efc >0 && _efc<PackValues.pellSAILimit) { //TBD
                /*  744 */ _pell =Math.round((pellMax_-_efc)/5)*5;// 639;
               
                /*  745 */            }  else {
                         //  _pell=pellMin_;
                            _pell=0;
                /*      */            }
            /*      */        }
        /*      */
       
 /*  754 */ return _pell;
 }
 
// /*      */ private final int getPell2021() {
//        /*  735 */ int _pell = 0, _efcBase = 0, _pellBase = 0;
//        /*      */
// /*  737 */ int _coa = getCOA();
//        /*      */
// /*  739 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= 6195 && _coa <= 999999) {
//            /*  740 */ int _efc = getEFC();
//            /*  741 */ if (_efc == 0) {
//                /*  742 */ _pell = 6345;
//                /*  743 */            } else if (_efc >= 5701 && _efc <= 5711) {
//                /*  744 */ _pell = 639;
//                /*  745 */            } else if (_efc >= 5712) {
//                /*  746 */ _pell = 0;
//                /*      */            } else {
//                /*      */
// /*  749 */ int j = (int) Math.floor(((_efc - 1) / 100)) * 100;
//                /*  750 */ _pell = 6295 - j;
//                /*      */            }
//            /*      */        }
//        /*      */
// /*  754 */ return _pell;
//        /*      */    }
//
//    /*      */
// /*      */
// /*      */ private final int getPell1920() {
//        /*  759 */ int _pell = 0, _efcBase = 0, _pellBase = 0;
//        /*      */
// /*  761 */ int _coa = getCOA();
//        /*      */
// /*  763 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= 6195 && _coa <= 999999) {
//            /*  764 */ int _efc = getEFC();
//            /*  765 */ if (_efc == 0) {
//                /*  766 */ _pell = 6345;
//                /*  767 */            } else if (_efc >= 5501 && _efc <= 5576) {
//                /*  768 */ _pell = 657;
//                /*  769 */            } else if (_efc >= 5577) {
//                /*  770 */ _pell = 0;
//                /*      */            } else {
//                /*      */
// /*  773 */ int j = (int) Math.floor(((_efc - 1) / 100)) * 100;
//                /*  774 */ _pell = 6145 - j;
//                /*      */            }
//            /*      */        }
//        /*      */
// /*  778 */ return _pell;
//        /*      */    }
//
//    /*      */
// /*      */
// /*      */ private final int getPell1718() {
//        /*  783 */ int _pell = 0, _efcBase = 0, _pellBase = 0;
//        /*      */
// /*  785 */ int _coa = getCOA();
//        /*      */
// /*  787 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= 6195 && _coa <= 999999) {
//            /*  788 */ int _efc = getEFC();
//            /*  789 */ if (_efc == 0) {
//                /*  790 */ _pell = 6345;
//                /*  791 */            } else if (_efc >= 5301 && _efc <= 5328) {
//                /*  792 */ _pell = 606;
//                /*  793 */            } else if (_efc >= 5329) {
//                /*  794 */ _pell = 0;
//                /*      */            } else {
//                /*      */
// /*  797 */ int j = (int) Math.floor(((_efc - 1) / 100)) * 100;
//                /*  798 */ _pell = 5870 - j;
//                /*      */            }
//            /*      */        }
//        /*      */
// /*  802 */ return _pell;
//        /*      */    }
//
//    /*      */
// /*      */ private final int getPell1617() {
//        /*  806 */ int _pell = 0, _efcBase = 0, _pellBase = 0;
//        /*      */
// /*  808 */ int _coa = getCOA();
//        /*      */
// /*  810 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= 6195 && _coa <= 999999) {
//            /*  811 */ int _efc = getEFC();
//            /*  812 */ if (_efc == 0) {
//                /*  813 */ _pell = 6345;
//                /*  814 */            } else if (_efc >= 5201 && _efc <= 5234) {
//                /*  815 */ _pell = 598;
//                /*  816 */            } else if (_efc >= 5235) {
//                /*  817 */ _pell = 0;
//                /*      */            } else {
//                /*      */
// /*  820 */ int j = (int) Math.floor(((_efc - 1) / 100)) * 100;
//                /*  821 */ _pell = 5765 - j;
//                /*      */            }
//            /*      */        }
//        /*      */
// /*  825 */ return _pell;
//        /*      */    }
//
//    /*      */
// /*      */ private final int getPell1516() {
//        /*  829 */ int _pell = 0, _efcBase = 0, _pellBase = 0;
//        /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*  836 */ int _coa = getCOA();
//        /*      */
// /*  838 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _coa >= 6195 && _coa <= 999999) {
//            /*  839 */ int _efc = getEFC();
//            /*  840 */ if (_efc == 0) {
//                /*  841 */ _pell = 6345;
//                /*  842 */            } else if (_efc >= 5101 && _efc <= 5198) {
//                /*  843 */ _pell = 626;
//                /*  844 */            } else if (_efc >= 5199) {
//                /*  845 */ _pell = 0;
//                /*      */
// /*      */
// /*      */
// /*      */
// /*      */            } /*      */ else {
//                /*      */
// /*      */
// /*      */
// /*      */
// /*  856 */ int j = (int) Math.floor(((_efc - 1) / 100)) * 100;
//                /*  857 */ _pell = 5725 - j;
//                /*      */            }
//            /*      */        }
//        /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*      */
// /*  885 */ return _pell;
//        /*      */    }

    /*      */
 /*      */
 /*      */ 
     public final int getCalGrantA() {
         /*  890 */ int _aCalGrantAmt = 0;

         /*  902 */ if (this.adjust_calgrant_amt_ind == true && this.std.getStudentZCalgrant() != null 
                 && this.std.getStudentZCalgrant().equalsIgnoreCase("yes")) {
             return this.std.getStudentAaCalgrantA().intValue();
         }
         /*      */
         //39200 >>> 46200 39000 46000 
         //9084 >> 99209220 9358  95700>> 112900 98000>>$115,600 106500 > 125600  114100 > 134600 123100 > 145200
         /*      */
 /*  905 */ if (this.SAVE_STUDENT_Z_CALGRANT != null && !this.SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("Yes")) {
             /*  906 */ _aCalGrantAmt = 0;
             /*      */         } /*  908 */ else if (this.SAVE_STUDENT_Z_CALGRANT != null && this.SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("Yes")
                 && this.std.getStudentPGpa() != null && this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) >= 0
                 && this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() > 0) {

             /*  909 */ if (getIndependentStatus() != null && getIndependentStatus().equalsIgnoreCase("Independent") 
                     && this.std.getStudentAeFamilyAsset() != null
                     && this.std.getStudentAeFamilyAsset().intValue() <= 50000) {

                 /*  910 */ if (MID(this.SAVE_STUDENT_M_MARRY, 1, 1) != null 
                         && MID(this.SAVE_STUDENT_M_MARRY, 1, 1).equalsIgnoreCase("S")) {
                       //Independent ,Single
                     /*  911 */ if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() <= 1 
                             && this.std.getStudentAdFamilyIncome() != null
                             && this.std.getStudentAdFamilyIncome().intValue() <= 49800) {
                         /*  912 */ _aCalGrantAmt = 9358;
                         /*  913 */                     } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 2
                             && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 122100) {
                         /*  914 */ _aCalGrantAmt = 9358;
                         /*  915 */                     } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 3
                             && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 125100) {
                         /*  916 */ _aCalGrantAmt = 9358;
                         /*  917 */                     } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 4 
                                 && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 135900) {
                         /*  918 */ _aCalGrantAmt = 9358;
                         /*  919 */                     } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 5 
                                 && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 145700) {
                         /*  920 */ _aCalGrantAmt = 9358;
                         /*  921 */                     } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() > 5
                                 && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 157100) {
                         /*  922 */ _aCalGrantAmt = 9358;
                         /*      */                     } else {
                         /*  924 */ _aCalGrantAmt = 0;
                         /*      */                     }
                     /*      */
 /*  927 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 1 
         && this.std.getStudentAdFamilyIncome() != null
                         && this.std.getStudentAdFamilyIncome().intValue() <= 55000) {
                     /*  928 */ _aCalGrantAmt = 9358;
                     /*  929 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 2
                         && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 122100) {
                     /*  930 */ _aCalGrantAmt = 9358;
                     /*  931 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 3
                         && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 125100) {
                     /*  932 */ _aCalGrantAmt = 9358;
                     /*  933 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 4
                         && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 135900) {
                     /*  934 */ _aCalGrantAmt = 9358;
                     /*  935 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 5
                         && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 145700) {
                     /*  936 */ _aCalGrantAmt = 9358;
                     /*  937 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() > 5
                         && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 157100) {
                     /*  938 */ _aCalGrantAmt = 9358;
                     /*      */                 } else {
                     /*  940 */ _aCalGrantAmt = 0;
                     /*      */                 }
                 /*      */
 /*  943 */             } else if (getIndependentStatus() != null && getIndependentStatus().equalsIgnoreCase("Dependent") 
         && this.std.getStudentAeFamilyAsset() != null
                     && this.std.getStudentAeFamilyAsset().intValue() <= 105200) {
                 /*  944 */ if (this.std.getStudentAcFamilySize().intValue() == 1 && this.std.getStudentAdFamilyIncome().intValue() <= 0) {
                     /*  945 */ _aCalGrantAmt = 9358;
                     /*  946 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 2 
                             && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 122100) {
                     /*  947 */ _aCalGrantAmt = 9358;
                     /*  948 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 3 
                             && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 125100) {
                     /*  949 */ _aCalGrantAmt = 9358;
                     /*  950 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 4 
                             && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 135900) {
                     /*  951 */ _aCalGrantAmt = 9358;
                     /*  952 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() == 5 
                             && this.std.getStudentAcFamilySize() != null && this.std.getStudentAdFamilyIncome().intValue() <= 145700) {
                     /*  953 */ _aCalGrantAmt = 9358;
                     /*  954 */                 } else if (this.std.getStudentAcFamilySize() != null && this.std.getStudentAcFamilySize().intValue() > 5 
                             && this.std.getStudentAdFamilyIncome() != null && this.std.getStudentAdFamilyIncome().intValue() <= 157100) {
                     /*  955 */ _aCalGrantAmt = 9358;
                     /*      */                 } else {
                     /*  957 */ _aCalGrantAmt = 0;
                     /*      */                 }
                 /*      */             } else {
                 /*  960 */ _aCalGrantAmt = 0;
                 /*      */             }
             /*      */         } else {
             /*  963 */ _aCalGrantAmt = 0;
             /*      */         }
         /*      */
 /*  966 */ return _aCalGrantAmt;
         /*      */     }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getCalGrantB() {
        /*  972 */ int _bCalGrantAmt = 0;

 /*      */
 /*  983 */ if (this.adjust_calgrant_amt_ind == true && this.std.getStudentZCalgrant()!=null && this.std.getStudentZCalgrant().equalsIgnoreCase("yes")) {
            return this.std.getStudentAbCalgrantB().intValue();
        }
        /*      */
 /*  985 */ String _bCalGrantAmtAward = "No";
 

        /*      */
 /*  987 */ if (this.SAVE_STUDENT_Z_CALGRANT!=null && !this.SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("Yes")
                || /*  988 */ getCalGrantA() > 0 || (this.std.getStudentPGpa()!=null && this.std.getStudentPGpa().compareTo(new BigDecimal(2)) < 0 )|| 
         (this.std.getStudentPGpa()!=null && this.std.getStudentPGpa().compareTo(new BigDecimal(3)) > 0)) {
            /*      */
 /*  990 */ _bCalGrantAmt = 0;
            /*      */        } /*  992 */ else if (getIndependentStatus()!=null && getIndependentStatus().equalsIgnoreCase("Independent")) {
            /*  993 */ if (MID(this.SAVE_STUDENT_M_MARRY, 1, 1)!=null && MID(this.SAVE_STUDENT_M_MARRY, 1, 1).equalsIgnoreCase("S")) {
                /*      */
                //Independent ,Single
 /*  995 */ if (this.std.getStudentAeFamilyAsset()!=null && this.std.getStudentAeFamilyAsset().intValue() <= 50000) {
                    /*  996 */ if (this.std.getStudentAcFamilySize()!=null && this.std.getStudentAcFamilySize().intValue() <= 1) {
                        /*  997 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 49800) {
                            /*  998 */ _bCalGrantAmtAward = "Yes";
                            /*      */                        }
                        /* 1000 */                    } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() == 2) {
                        /* 1001 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 57000) {
                            /* 1002 */ _bCalGrantAmtAward = "Yes";
                            /*      */                        }
                        /* 1004 */                    } else if (this.std.getStudentAcFamilySize()!=null && this.std.getStudentAcFamilySize().intValue() == 3) {
                        /* 1005 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 64200) {
                            /* 1006 */ _bCalGrantAmtAward = "Yes";
                            /*      */                        }
                        /* 1008 */                    } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() == 4) {
                        /* 1009 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 71500) {
                            /* 1010 */ _bCalGrantAmtAward = "Yes";
                            /*      */                        }
                        /* 1012 */                    } else if (this.std.getStudentAcFamilySize()!=null && this.std.getStudentAcFamilySize().intValue() == 5) {
                        /* 1013 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 79900) {
                            /* 1014 */ _bCalGrantAmtAward = "Yes";
                            /*      */                        }
                        /* 1016 */                    } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() >= 6
                            && /* 1017 */ this.std.getStudentAdFamilyIncome().intValue() <= 86300) {
                        /* 1018 */ _bCalGrantAmtAward = "Yes";
                        /*      */                    }
                    /*      */
 /*      */                }
                /*      */            } /* 1023 */ else if (this.std.getStudentAeFamilyAsset()!=null && this.std.getStudentAeFamilyAsset().intValue() <= 50000) {
                    //Independent Married
                /* 1024 */ if (this.std.getStudentAcFamilySize()!=null && this.std.getStudentAcFamilySize().intValue() <= 1) {
                    /* 1025 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 57000) {
                        /* 1026 */ _bCalGrantAmtAward = "Yes";
                        /*      */                    }
                    /* 1028 */                } else if (this.std.getStudentAcFamilySize()!=null && this.std.getStudentAcFamilySize().intValue() <= 2) {
                    /* 1029 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 57000) {
                        /* 1030 */ _bCalGrantAmtAward = "Yes";
                        /*      */                    }
                    /* 1032 */                } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() <= 3) {
                    /* 1033 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 64200) {
                        /* 1034 */ _bCalGrantAmtAward = "Yes";
                        /*      */                    }
                    /* 1036 */                } else if (this.std.getStudentAcFamilySize()!=null && this.std.getStudentAcFamilySize().intValue() <= 4) {
                    /* 1037 */ if (this.std.getStudentAdFamilyIncome()!=null && this.std.getStudentAdFamilyIncome().intValue() <= 71500) {
                        /* 1038 */ _bCalGrantAmtAward = "Yes";
                        /*      */                    }
                    /* 1040 */                } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() <= 5) {
                    /* 1041 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 79900) {
                        /* 1042 */ _bCalGrantAmtAward = "Yes";
                        /*      */                    }
                    /* 1044 */                } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() >= 6
                        && /* 1045 */ this.std.getStudentAdFamilyIncome().intValue() <= 86300) {
                    /* 1046 */ _bCalGrantAmtAward = "Yes";
                    /*      */                }
                /*      */
 /*      */            }
            /*      */
 /*      */        } /* 1052 */ else if (this.std.getStudentAeFamilyAsset() !=null && this.std.getStudentAeFamilyAsset().intValue() <= 105200) {
            /* 1053 */ if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() <= 1) {
                /* 1054 */ if (this.std.getStudentAdFamilyIncome() !=null && this.std.getStudentAdFamilyIncome().intValue() <= 0) {
                    /* 1055 */ _bCalGrantAmtAward = "Yes";
                    /*      */                }
                /* 1057 */            } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() == 2) {
                /* 1058 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 57000) {
                    /* 1059 */ _bCalGrantAmtAward = "Yes";
                    /*      */                }
                /* 1061 */            } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() == 3) {
                /* 1062 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 64200) {
                    /* 1063 */ _bCalGrantAmtAward = "Yes";
                    /*      */                }
                /* 1065 */            } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() == 4) {
                /* 1066 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 71500) {
                    /* 1067 */ _bCalGrantAmtAward = "Yes";
                    /*      */                }
                /* 1069 */            } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() == 5) {
                /* 1070 */ if (this.std.getStudentAdFamilyIncome().intValue() <= 79900) {
                    /* 1071 */ _bCalGrantAmtAward = "Yes";
                    /*      */                }
                /* 1073 */            } else if (this.std.getStudentAcFamilySize() !=null && this.std.getStudentAcFamilySize().intValue() > 5
                    && /* 1074 */ this.std.getStudentAdFamilyIncome().intValue() <= 86300) {
                /* 1075 */ _bCalGrantAmtAward = "Yes";
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /* 1082 */ if (_bCalGrantAmtAward !=null && _bCalGrantAmtAward.equalsIgnoreCase("Yes")) {
            /* 1083 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR")) {
                /* 1084 */ _bCalGrantAmt = PackValues.bCalGrantFr;
                /* 1085 */            } else if (this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("F2")) {
                /* 1086 */ _bCalGrantAmt = PackValues.bCalGrantFr2;
                /* 1087 */            } else if (this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SO")) {
                /* 1088 */ _bCalGrantAmt = PackValues.bCalGrantSoJrSr;
                /* 1089 */            } else if (this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) {
                /* 1090 */ _bCalGrantAmt = PackValues.bCalGrantSoJrSr;
                /* 1091 */            } else if (this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) {
                /* 1092 */ _bCalGrantAmt = PackValues.bCalGrantSoJrSr;;
                /* 1093 */            } else if (this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("S2")) {
                /* 1094 */ _bCalGrantAmt = PackValues.bCalGrantSoJrSr;
                /*      */            } else {
                /* 1096 */ _bCalGrantAmt = 0;
                /*      */            }
            /*      */        } else {
            /* 1099 */ _bCalGrantAmt = 0;
            /*      */        }
        /* 1101 */ return _bCalGrantAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getFseog() {
        /* 1107 */ int _fseogAmt = 0;
 //0 to 1999
 /* 1124 */ if (this.SAVE_STUDENT_X_FAFSA !=null && !this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")) {
            /* 1125 */ _fseogAmt = 0;
            /*      */        } /* 1127 */ else if (getCalGrantB() > 1551 || getCalGrantA() > 1551 || getExternalAllowance() > 0 || getLsuAllowance() > 0 
                    || getNationalMerit() > 0) {
            /* 1128 */ _fseogAmt = 0;
            /*      */        } /* 1130 */ 
            
             else if (this.std.getStudentAfFamilyContrib() !=null && this.std.getStudentAfFamilyContrib().intValue() <= 0) {
                       _fseogAmt = 1500;// _fseogAmt = 1350;
            /* 1132 */        } 
            else if (this.std.getStudentAfFamilyContrib() !=null && this.std.getStudentAfFamilyContrib().intValue() >= 1  
                    && this.std.getStudentAfFamilyContrib().intValue()<= 6625 ) {
            /* 1133 */_fseogAmt = 750;                 //_fseogAmt = 600;
            /*      */        }
            else {
            /* 1135 */ _fseogAmt = 0;
            /*      */        }
//            2023 data
//            else if (this.std.getStudentAfFamilyContrib() !=null && this.std.getStudentAfFamilyContrib().intValue() < 1000) {
//                       _fseogAmt = 1500;// _fseogAmt = 1350;
//            /* 1132 */        } else if (this.std.getStudentAfFamilyContrib() !=null && this.std.getStudentAfFamilyContrib().intValue() < 2000) {
//            /* 1133 */_fseogAmt = 1000;                 //_fseogAmt = 600;
//            /*      */        }
//            else if (this.std.getStudentAfFamilyContrib() !=null && this.std.getStudentAfFamilyContrib().intValue() >= 2000  
//                    && this.std.getStudentAfFamilyContrib().intValue()<= 6656 ) {
//            /* 1133 */_fseogAmt = 750;                 //_fseogAmt = 600;
//            /*      */        }
//            else {
//            /* 1135 */ _fseogAmt = 0;
//            /*      */        }

 /* 1160 */ return _fseogAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getLsuAllowance() {
        /* 1167 */ int _edAllowance = 0;
     

 /* 1179 */ if (this.SAVE_STUDENT_AH_LSU_ALLOWRANCE!=null && TRIM(this.SAVE_STUDENT_AH_LSU_ALLOWRANCE).equalsIgnoreCase("Yes")) {
            /*      */
 /* 1181 */ if (this.SAVE_STUDENT_U_ACADEMIC == null) {
                /* 1182 */ this.SAVE_STUDENT_U_ACADEMIC = "FR";
                /*      */            }
            /* 1184 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR")) {
                /*      */
 /* 1186 */ _edAllowance = this.std.getEa_lsu_perc()!=null ? (new BigDecimal(getTuitionAndFees() - 220)).multiply(new BigDecimal(this.std.getEa_lsu_perc().intValue())).divide(new BigDecimal(100)).intValue():0;
                /*      */            } else {
                /*      */
 /* 1189 */ _edAllowance = (new BigDecimal(getTuitionAndFees())).intValue();
                /*      */            }
            /*      */
 /*      */        } /*      */ else {
            /*      */
 /* 1195 */ _edAllowance = 0;
            /*      */        }
        /* 1197 */ return _edAllowance;
        /*      */    }

    /*      */
 /*      */ public final int getExternalAllowance() {
        /* 1201 */ int _extAllowance = 0;

if (this.std.getStudentAgNonlsuAllowrance()!=null && !this.std.getStudentAgNonlsuAllowrance().equals(BigDecimal.ZERO)) {

 if (this.std.getStudentAiEduAllowPer()!=null && this.std.getStudentAiEduAllowPer().compareTo(new BigDecimal(0)) > 0) {

 _extAllowance = this.std.getStudentAiEduAllowPer()!=null ? (new BigDecimal(getTuitionAndFees())).multiply(this.std.getStudentAiEduAllowPer()).intValue():0;
                          } else {
            _extAllowance = 0;
                         }
                } else {
            _extAllowance = 0;
                   }
      return _extAllowance;
         }

  
 /*      */ public final int getNonCaGrant() {
        /* 1237 */ if (this.SAVE_STUDENT_AJ_HOME_STATE!=null && !TRIM(this.SAVE_STUDENT_AJ_HOME_STATE).equalsIgnoreCase("")) {
            /* 1238 */ this._nonCaGrantDesc = this.SAVE_STUDENT_AJ_HOME_STATE!=null ? TRIM(this.SAVE_STUDENT_AJ_HOME_STATE):this.SAVE_STUDENT_AJ_HOME_STATE;
            /* 1239 */ this._nonCaGrantAmt = this.SAVE_STUDENT_AK_NONCAL_GRANT!=null ? Integer.parseInt(this.SAVE_STUDENT_AK_NONCAL_GRANT):0;
            /*      */
 /*      */        } /*      */ else {
            /*      */
 /* 1244 */ this._nonCaGrantDesc = "No Non-CA State Grant";
            /* 1245 */ this._nonCaGrantAmt = 0;
            /*      */        }
        /* 1247 */ return this._nonCaGrantAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getOutsideScholarship() {
        /* 1261 */ if (this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS !=null && !TRIM(this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS).equalsIgnoreCase("")) {
            /* 1262 */ this._outsideScholarship = this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS;
            /* 1263 */ this._outsideScholarshipAmt = this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT!=null ? Integer.parseInt(this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT):0;
            /*      */
 /* 1265 */ if (this._outsideScholarshipAmt == 0) {
                /* 1266 */ this._nonCaGrantAmt = 0;
                /*      */            }
            /*      */        } else {
            /* 1269 */ this._outsideScholarship = "No Outside Scholarship";
            /* 1270 */ this._outsideScholarshipAmt = 0;
            /*      */        }
        /* 1272 */ return this._outsideScholarshipAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getSdaAward() {
        /* 1280 */ int _sdaAmt = 0;

 if (this.SAVE_STUDENT_N_SDA !=null && TRIM(this.SAVE_STUDENT_N_SDA).equalsIgnoreCase("Yes")) {
            _sdaAmt = this.sdaAward;
                   } else {
          
 _sdaAmt = 0;
            /*      */        }
      
 /* 1295 */ return _sdaAmt;
        }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getLsuNeedGrantAmt() {
        /* 1301 */ int _needGrantAmt = 0;
        /* 1302 */ String _grd = this.std.getStudentUAcademic();
        /* 1303 */ if (_grd == null || _grd.isEmpty()) {
            /* 1304 */ _grd = "FR";
            /*      */        } else {
            /* 1306 */ _grd = _grd.toUpperCase();
            /*      */        }
        /*      */
 /* 1309 */ if (this.std.getStudentStudType()!=null && this.std.getStudentStudType().equalsIgnoreCase("UGFY") || _grd.equals("FR") || _grd.equals("F2") || _grd.equals("SO") || _grd.equals("JR") || _grd.equals("SR")) {
            /* 1310 */ int _efcAmt = this.std.getStudentAfFamilyContrib() !=null ? this.std.getStudentAfFamilyContrib().intValue() :0;
            // if (this.std.getIndEfc() !=null && this.std.getIndEfc().equalsIgnoreCase("Yes") && _efcAmt >= 0 && _efcAmt <= 12000) {
                if (this.std.getIndEfc() !=null && this.std.getIndEfc().equalsIgnoreCase("Yes") &&  _efcAmt <= 12000) {
                /* 1312 */ //_needGrantAmt = 3000;
                 _needGrantAmt = PackValues.lsuNeedGrant;
                /*      */            }
            /*      */        }
        /* 1315 */ return this.lsuNeedGrant = _needGrantAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getLsuAchievement() {
        /* 1321 */ int _achieve = 0;
        /*      */
 /*      */
 /* 1324 */ if (this.std == null) {
            return 0;
        }
        /* 1325 */ if (this.SAVE_STUDENT_U_ACADEMIC == null) {
            /* 1326 */ if (this.std.getStudentUAcademic() == null) {
                /* 1327 */ this.std.setStudentUAcademic("FR");
                /*      */            }
            /* 1329 */ this.SAVE_STUDENT_U_ACADEMIC = this.std.getStudentUAcademic();
            /*      */        }
        /*      */
 /*      */
 /* 1333 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null 
         && !this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR") 
         || (this.std.getStudentQSat()!=null 
         && this.std.getStudentQSat().intValue() < 1270 
         && this.std.getStudentRAct().intValue() < 29) || getNationalMerit() > 0) {
                /* 1334 */ return _achieve;
            /*      */        }
        /* 1336 */ return this.lsuAchievementInit;
        /*      */    }

    /*      */
 /*      */
 
 public final int getUnivHousingGrant(){
 
      boolean student_freshmen= this.std.getStd_1st_freshmen()==1?true:false;
      boolean isAdventist= this.std.getStudentNSda().equalsIgnoreCase("yes")?true:false;
      boolean isLiveInRes = this.std.getStudentWDorm().equalsIgnoreCase("yes")?true:false;
      
      if(student_freshmen && isAdventist && isLiveInRes ){
      return PackValues.universityHouseGrant;
      }
     return 0;
 }
 /*      */ public final int getLsu4yRenewable() {
     
     
     //showEFC
     int sai=0;
                  String na=this.showEFC();
                  if(!na.equalsIgnoreCase("n/a")){
                      
        na = na.replace("$", "").replace(",", ""); // Remove "$" and commas
        
        NumberFormat numberFormat = NumberFormat.getInstance();
        try{
        Number parsedNumber = numberFormat.parse(na);
        sai=parsedNumber.intValue();
        }catch(Exception  ex){}
         
                      
                  }
     //converting this function to FreshMan and FreshMan transfer
        /* 1341 */ int _4yrenewable = 0;
        /* 1342 */ boolean zerogpa_ind = this.std.getStudentPGpa() !=null ? this.std.getStudentPGpa().equals(BigDecimal.ZERO):false;
        /* 1343 */ String _gpa = String.format("%3.2f", new Object[]{this.std.getStudentPGpa()});
        /*      */
 /*      */    boolean student_freshMan_transfer= this.std.getStd_transfer_ind()==1?true:false;
 /*      */    boolean student_freshMan_returning= this.std.getReturnStdInd()==1 ? true:false;
 /*      */
 /*      */
 /*      */
 /* 1350 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null && (this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("FR") || 
         this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("F2")) && zerogpa_ind) {
            /* 1351 */ if (this.std.getStudentLIntlStud()!=null && this.std.getStudentLIntlStud().equalsIgnoreCase("YES")) {
                /* 1352 */ _gpa = "2.75";//3.25
                /*      */            }
//            else {
//                /* 1354 */ _gpa = "2.75";
//                /*      */            }
            /*      */        }
 if(na.equalsIgnoreCase("n/a")){
 
     /* 1357 */ if (_gpa.compareTo("2.0") >= 0 && !student_freshMan_transfer ) /* 2.50     */
        {
           
            
 /* 1360 */ if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableFrGpa2_2De74_sai_3;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa2De75_3De24_sai_3;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De25_3De49_sai_3;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De50_3De74_sai_3;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De75_4_sai_3;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableFrGpa3De75_4_sai_3 ;//8500;//TBD
                /*      */            }
            /*      */        }
        else if(_gpa.compareTo("2.0") >= 0 && student_freshMan_transfer)
        {
            if(this.SAVE_STUDENT_U_ACADEMIC !=null && student_freshMan_transfer)
            {
                 if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableFrTfGpa2_2De74_sai_3;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa2De75_3De24_sai_3;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De25_3De49_sai_3;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De50_3De74_sai_3;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De75_4_sai_3;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableFrTfGpa3De75_4_sai_3 ;//8500;//TBD
                /*      */            }
                
            }
        
        }else
        {
            if (_gpa.compareTo("2.0") >= 0 && student_freshMan_returning){
        if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableRGpa2_2De74_sai_3;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa2De75_3De24_sai_3;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De25_3De49_sai_3;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De50_3De74_sai_3;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De75_4_sai_3;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableRGpa3De75_4_sai_3 ;//8500;//TBD
                /*      */            }
            }
            
        }
     //no sai
     
 }
 
 else if(sai>=-1500 && sai<=6550){
        /* 1357 */ if (_gpa.compareTo("2.0") >= 0 && !student_freshMan_transfer ) /* 2.50     */
        {
           
            
 /* 1360 */ if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableFrGpa2_2De74_sai_1;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa2De75_3De24_sai_1;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De25_3De49_sai_1;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De50_3De74_sai_1;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De75_4_sai_1;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableFrGpa3De75_4_sai_1 ;//8500;//TBD
                /*      */            }
            /*      */        }
        else if(_gpa.compareTo("2.0") >= 0 && student_freshMan_transfer)
        {
            if(this.SAVE_STUDENT_U_ACADEMIC !=null && student_freshMan_transfer)
            {
                 if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableFrTfGpa2_2De74_sai_1;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa2De75_3De24_sai_1;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De25_3De49_sai_1;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De50_3De74_sai_1;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De75_4_sai_1;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableFrTfGpa3De75_4_sai_1 ;//8500;//TBD
                /*      */            }
                
            }
        
        }else
        {
            if (_gpa.compareTo("2.0") >= 0 && student_freshMan_returning){
        if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableRGpa2_2De74_sai_1;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa2De75_3De24_sai_1;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De25_3De49_sai_1;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De50_3De74_sai_1;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De75_4_sai_1;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableRGpa3De75_4_sai_1 ;//8500;//TBD
                /*      */            }
            }
            
        }
 }else if(sai>=6551){
     /* 1357 */ if (_gpa.compareTo("2.0") >= 0 && !student_freshMan_transfer ) /* 2.50     */
        {
           
            
 /* 1360 */ if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableFrGpa2_2De74_sai_2;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa2De75_3De24_sai_2;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De25_3De49_sai_2;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De50_3De74_sai_2;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableFrGpa3De75_4_sai_2;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableFrGpa3De75_4_sai_2 ;//8500;//TBD
                /*      */            }
            /*      */        }
        else if(_gpa.compareTo("2.0") >= 0 && student_freshMan_transfer)
        {
            if(this.SAVE_STUDENT_U_ACADEMIC !=null && student_freshMan_transfer)
            {
                 if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableFrTfGpa2_2De74_sai_2;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa2De75_3De24_sai_2;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De25_3De49_sai_2;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De50_3De74_sai_2;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableFrTfGpa3De75_4_sai_2;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableFrTfGpa3De75_4_sai_2 ;//8500;//TBD
                /*      */            }
                
            }
        
        }else
        {
            if (_gpa.compareTo("2.0") >= 0 && student_freshMan_returning){
        if (_gpa.compareTo("2.0") >= 0 && _gpa.compareTo("2.74") <= 0) {
                /* 1361 */ _4yrenewable =  PackValues.Lsu4yRenewableRGpa2_2De74_sai_2;//2500;
                /* 1362 */            } else if (_gpa.compareTo("2.75") >= 0 && _gpa.compareTo("3.24") <= 0) { //"2.99"
                /* 1363 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa2De75_3De24_sai_2;//3500;
                /* 1364 */            } else if (_gpa.compareTo("3.25") >= 0 && _gpa.compareTo("3.49") <= 0) {
                /* 1365 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De25_3De49_sai_2;//4500;
                /* 1366 */            } else if (_gpa.compareTo("3.50") >= 0 && _gpa.compareTo("3.74") <= 0) {
                /* 1367 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De50_3De74_sai_2;//6000;
                /* 1368 */            } else if (_gpa.compareTo("3.75") >= 0 && _gpa.compareTo("4.0") <= 0) {
                /* 1369 */ _4yrenewable = PackValues.Lsu4yRenewableRGpa3De75_4_sai_2;//7000;
                /* 1370 */            } else if (_gpa.compareTo("4.0") >= 0) {
                /* 1371 */ _4yrenewable =PackValues.Lsu4yRenewableRGpa3De75_4_sai_2 ;//8500;//TBD
                /*      */            }
            }
            
        }
 
 }
 
        
        /* 1374 */ return _4yrenewable;
        /*      */    }

    /*      */
 /*      */ public final int getLsuPerformance() {
        /* 1378 */ return 0;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getFamilyDiscount() {
        /* 1385 */ int _familyDiscount = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1391 */ if (this.SAVE_STUDENT_V_FAMILY!=null && TRIM(this.SAVE_STUDENT_V_FAMILY).length() > 3) {
            /* 1392 */ _familyDiscount = this.familyDiscount;
            /*      */        } else {
            /* 1394 */ _familyDiscount = 0;
            /*      */        }
        /* 1396 */ return _familyDiscount;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getNationalMerit() {
        /* 1402 */ int _nationalMeritAmt = 0;
        /* 1403 */ int _nationalMeritBase = 0;


 /* 1410 */ if (this.std.getStudentPGpa()!=null && this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
            /* 1411 */ _nationalMeritAmt = 0;
            /*      */        } else {

 /* 1414 */ _nationalMeritBase = getTuitionAndFees() - getFallOrientationFee();

 /* 1425 */ if (this.SAVE_STUDENT_S_MERIT!=null && TRIM(this.SAVE_STUDENT_S_MERIT).indexOf("MF") >= 0) {
                /* 1426 */ _nationalMeritAmt = (new BigDecimal(_nationalMeritBase)).multiply(PackValues.nationalMeritMF).intValue();
                /* 1427 */            } else if (TRIM(this.SAVE_STUDENT_S_MERIT).indexOf("MS") >= 0) {
                /* 1428 */ _nationalMeritAmt = (new BigDecimal(_nationalMeritBase)).multiply(PackValues.nationalMeritMS).intValue();
                /* 1429 */            } else if (TRIM(this.SAVE_STUDENT_S_MERIT).indexOf("MC") >= 0) {
                /* 1430 */ _nationalMeritAmt = (new BigDecimal(_nationalMeritBase)).multiply(PackValues.nationalMeritMC).intValue();
                /*      */            } else {
                /* 1432 */ _nationalMeritAmt = 0;
                /*      */            }
            /*      */        }
        /* 1435 */ return _nationalMeritAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getSubDirect() {
        /* 1441 */ int _subDirect = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1447 */ if ((this.SAVE_STUDENT_X_FAFSA!=null && !this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") )|| (this.std.getIndExcloans()!=null && this.std.getIndExcloans().equalsIgnoreCase("YES") )|| (this.std.getStudentApSubLoans()!=null && this.std.getStudentApSubLoans().equalsIgnoreCase("no"))) {
            /* 1448 */ _subDirect = 0;
            /* 1449 */        } else if (this.use_need_ind > 0) {
            /* 1450 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                /* 1451 */ _subDirect = PackValues._subDirectFr_F2;
                /* 1452 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")) {
                /* 1453 */ _subDirect = PackValues._subDirectFr_F2;
                /* 1454 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")) {
                /* 1455 */ _subDirect = PackValues._subDirectSO;
                /* 1456 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")) {
                /* 1457 */ _subDirect = PackValues._subDirectSr;
                /* 1458 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
                /* 1459 */ _subDirect = PackValues._subDirectSr;
                /*      */            } else {
                /* 1461 */ _subDirect = 0;
                /*      */            }
            /*      */        }
        /* 1464 */ return _subDirect;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getPerkinsLoan() {
        int _perkins = 0;
         String _gpa_ = String.format("%3.2f", new Object[]{this.std.getStudentPGpa()});
        float _f_gpa_= Float.parseFloat(_gpa_);
        //use for loan to scholarships
        // this.perkinsLoan
       // if( std.getStudentAfFamilyContrib()<4996)
          int is_Transfer_Std=  std.getStd_transfer_ind();
          
         if( is_Transfer_Std==0) {
        if(_f_gpa_>= 2.75 && _f_gpa_<= 3.24)
       {
           if(std.getStudentAfFamilyContrib()<= 12000){
               if(this.SAVE_STUDENT_U_ACADEMIC !=null && is_Transfer_Std==0 &&  this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SO")){
       
      _perkins=1300;
       } 
               else if((this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) 
                       || (this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) )
               {
                   _perkins=1300;
               }
      
       }
           else
       {
       
           if(this.SAVE_STUDENT_U_ACADEMIC !=null && is_Transfer_Std==0 &&   this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SO")){
       
      _perkins=1300;
       } 
               else if((this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) || (this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) )
               {
                   _perkins=1300;
               }
       }
       }
        else if(_f_gpa_>= 3.25 && _f_gpa_<= 4)
        {
             if(std.getStudentAfFamilyContrib()<=12000){
               if(this.SAVE_STUDENT_U_ACADEMIC !=null && is_Transfer_Std==0 &&  this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SO")){
       
      _perkins=4000;
       } 
               else if((this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) 
                       || (this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) )
               {
                   _perkins=4000;
               }
      
       }
           else
       {
       
           if(this.SAVE_STUDENT_U_ACADEMIC !=null && is_Transfer_Std==0 &&  this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SO")){
       
      _perkins=2000;
       } 
               else if((this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) 
                       || (this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) )
               {
                   _perkins=2000;
               }
       }
            
        }
        
 }
         else
         {
         if((this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("SR")) 
                       || (this.SAVE_STUDENT_U_ACADEMIC !=null && this.SAVE_STUDENT_U_ACADEMIC.equalsIgnoreCase("JR")) )
         
         {
           if(std.getStudentAfFamilyContrib()<= 12000){
               
               if(_f_gpa_>= 2 && _f_gpa_<= 2.74)
               {
               _perkins=1500;
               
               }
               else if(_f_gpa_>= 2.4 && _f_gpa_<= 3.49)
               {
               _perkins=2000;
               }
               else if(_f_gpa_>= 3.5 && _f_gpa_<=3.74)
               {
               _perkins=2500;
               }
                else if(_f_gpa_>= 3.75 && _f_gpa_<= 4) 
               {
                 _perkins=1500;
               }
           
           }
           else
           {
                if(_f_gpa_>= 2 && _f_gpa_<= 2.74)
               {
               _perkins=0;
               
               }
                else if(_f_gpa_>= 2.4 && _f_gpa_<= 3.49)
               {
               _perkins=1500;
               }
               else if(_f_gpa_>= 3.5 && _f_gpa_<=3.74)
               {
               _perkins=2000;
               }
                 else if(_f_gpa_>= 3.75 && _f_gpa_<= 4) 
               {
                 _perkins=1000;
               }
           
           }
         
         
         }
         }
       
       
      
return _perkins;
           }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getFWS() {
        /* 1503 */ int _fws = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1510 */ if (this.SAVE_STUDENT_X_FAFSA!=null &&  this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && this.std.getStudentArFws()!=null && 
         this.std.getStudentArFws().equalsIgnoreCase("yes") && this.use_need_ind > 0) {
//            if (this.std.getStudentAfFamilyContrib()!=null && this.std.getStudentAfFamilyContrib().intValue() < 4996) {
//            _fws = 2000;
//                      } else if (this.std.getStudentAfFamilyContrib()!=null && this.std.getStudentAfFamilyContrib().intValue() < 12001) {
//               _fws = 2500;
//                         } else {
//           _fws = 3000;
//                        }
//its always 4000 for 2023
// it remains same for 2024 as well
_fws=4000;
                   }
     

 /* 1534 */ return _fws;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getUnSubDirect() {
        /* 1540 */ int _unsubDirect = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1547 */ if (this.SAVE_STUDENT_X_FAFSA!=null && !this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") || this.std.getIndExcloans()!=null && this.std.getIndExcloans().equalsIgnoreCase("YES") || this.std.getStudentAqUnsubLoans()!=null && this.std.getStudentAqUnsubLoans().equalsIgnoreCase("no")) {
            /* 1548 */ _unsubDirect = 0;
            /* 1549 */        } else if (getIndependentStatus()!=null && getIndependentStatus().equalsIgnoreCase("Independent")) {
            /* 1550 */ if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
                /* 1551 */ _unsubDirect = PackValues._unsubDirectFr_F2_independent;
                /* 1552 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")) {
                /* 1553 */ _unsubDirect = PackValues._unsubDirectFr_F2_independent;
                /* 1554 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")) {
                /* 1555 */ _unsubDirect = PackValues._unsubDirectSO_independent;
                /* 1556 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")) {
                /* 1557 */ _unsubDirect = PackValues._unsubDirectSr_independent;
                /* 1558 */            } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
                /* 1559 */ _unsubDirect = PackValues._unsubDirectSr_independent;
                /*      */            } else {
                /* 1561 */ _unsubDirect = 0;
                /*      */            }
            /*      */
 /* 1564 */        } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
            /* 1565 */ _unsubDirect = PackValues._unsubDirectFr_F2_dependent;
            /* 1566 */        } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")) {
            /* 1567 */ _unsubDirect =PackValues._unsubDirectFr_F2_dependent;
            /* 1568 */        } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")) {
            /* 1569 */ _unsubDirect = PackValues._unsubDirectSO_dependent;
            /* 1570 */        } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")) {
            /* 1571 */ _unsubDirect = PackValues._unsubDirectSr_dependent;
            /* 1572 */        } else if (this.SAVE_STUDENT_U_ACADEMIC!=null && TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
            /* 1573 */ _unsubDirect =  PackValues._unsubDirectSr_dependent;
            /*      */        } else {
            /* 1575 */ _unsubDirect = 0;
            /*      */        }
        /*      */
 /* 1578 */ int _unsubDirectAmt = _unsubDirect - getSubDirect();
        /* 1579 */ if (_unsubDirectAmt < 1) {
            /* 1580 */ _unsubDirectAmt = 0;
            /*      */        }
            /* 1582 */ return _unsubDirectAmt;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getChurchBase() {
        /* 1587 */ int _churchBase = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1593 */ _churchBase = this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT!=null ? Integer.parseInt(this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT):0;
        /*      */
 /*      */
 /*      */
 /* 1597 */ return _churchBase;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getChurchMatch() {
        /* 1603 */ int _churchMatch = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1610 */ if (this.extAllowance > 0) {
            return 0;
        }
        /*      */
 /* 1612 */ _churchMatch = getChurchBase();
        /* 1613 */ if (_churchMatch > 1000) {
            /* 1614 */ _churchMatch = 1000;
            /*      */        }
        /* 1616 */ return _churchMatch;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getPacificCampBase() {
        /* 1621 */ int _pacificCampBase = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1627 */ _pacificCampBase = Integer.parseInt(this.SAVE_STUDENT_AX_SCHOLARSHIP2_AMT);
        /*      */
 /*      */
 /*      */
 /* 1631 */ return _pacificCampBase;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getPacificCampMatch() {
        /* 1637 */ int _pacificCampMatch = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1643 */ _pacificCampMatch = getPacificCampBase();
        /* 1644 */ if (_pacificCampMatch > 2000) {
            /* 1645 */ _pacificCampMatch = 2000;
            /*      */        }
        /* 1647 */ return _pacificCampMatch;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getNonPacificCampBase() {
        /* 1652 */ int _nonPacificCampBase = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1658 */ _nonPacificCampBase = this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT!=null ? Integer.parseInt(this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT):0;
        /*      */
 /*      */
 /*      */
 /* 1662 */ return _nonPacificCampBase;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final int getNonPacificCampMatch() {
        /* 1668 */ int _noPacificCampMatch = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1674 */ _noPacificCampMatch = Math.round((getNonPacificCampBase() / 2));
        /* 1675 */ if (_noPacificCampMatch > 1500) {
            /* 1676 */ _noPacificCampMatch = 1500;
            /*      */        }
        /* 1678 */ return _noPacificCampMatch;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getLitEvanBase() {
        /* 1683 */ int _litEvanBase = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1689 */ _litEvanBase =this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT!=null ? Integer.parseInt(this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT):0;
        /*      */
 /*      */
 /*      */
 /* 1693 */ return _litEvanBase;
        /*      */    }

    /*      */
 /*      */
 /*      */ public final int getLitEvanMatch() {
        /* 1698 */ int _litEvanMatch = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1704 */ _litEvanMatch = getLitEvanBase();
        /* 1705 */ if (_litEvanMatch > 3000) {
            /* 1706 */ _litEvanMatch = 3000;
            /*      */        }
        /* 1708 */ return _litEvanMatch;
        /*      */    }

    /*      */
 /*      */ public final int enforceCJLimits() {
        /* 1712 */ int _res = 0;
        /* 1713 */ int aidid = 0;
        /* 1714 */ String fundcode = "";
        /* 1715 */ if (this.std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
         
 /* 1728 */ this.lsuAllowance = 0;
            /*      */
 /* 1730 */ this.lsuPerformance = 0;
            /* 1731 */ this.lsuNeedGrant = 0;
            /*      */
 /* 1733 */ this.lasuGrantAmt = 0;
            /*      */
 /* 1735 */ this.lsuAchievement = 0;
            /* 1736 */ this.nationalMerit = 0;
            /* 1737 */ this.lsu4yRenewable = 0;
            /*      */
 /* 1739 */ this.familyDiscount = 0;
            /*      */
 /* 1741 */ this.churchMatch = 0;
            /* 1742 */ this.pacificCampMatch = 0;
            /* 1743 */ this.nonPacificCampMatch = 0;
            /* 1744 */ this.litEvanMatch = 0;
            /* 1745 */ this.scholarship_amt_1 = 0;
            /* 1746 */ this.scholarship_amt_2 = 0;
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1754 */ aidid = this.std.getFund7id().intValue();
            /* 1755 */ if (aidid > 0) {
                /* 1756 */ fundcode = this.ref.getFundCodeById(aidid);
                /* 1757 */ if (!fundcode.equals("N/A")) {
                    /* 1758 */ fundcode = fundcode.substring(0, fundcode.indexOf("|"));
                    /* 1759 */ if (PackValues.validAidsCJ.containsKey(fundcode)) {
                        /* 1760 */ this.std.setStudentBmScholarship7Amt(Integer.valueOf(0));
                        /* 1761 */ this.scholarship_amt_7 = 0;
                        /*      */                    }
                    /*      */                }
                /*      */            }
            /*      */
 /* 1766 */ aidid = this.std.getFund8id().intValue();
            /* 1767 */ if (aidid > 0) {
                /* 1768 */ fundcode = this.ref.getFundCodeById(aidid);
                /* 1769 */ if (!fundcode.equals("N/A")) {
                    /* 1770 */ fundcode = fundcode.substring(0, fundcode.indexOf("|"));
                    /* 1771 */ if (PackValues.validAidsCJ.containsKey(fundcode)) {
                        /* 1772 */ this.std.setStudentBpScholarship8Amt(Integer.valueOf(0));
                        /* 1773 */ this.scholarship_amt_8 = 0;
                        /*      */                    }
                    /*      */                }
                /*      */            }
            /*      */
 /* 1778 */ aidid = this.std.getFund9id().intValue();
            /* 1779 */ if (aidid > 0) {
                /* 1780 */ fundcode = this.ref.getFundCodeById(aidid);
                /* 1781 */ if (!fundcode.equals("N/A")) {
                    /* 1782 */ fundcode = fundcode.substring(0, fundcode.indexOf("|"));
                    /* 1783 */ if (PackValues.validAidsCJ.containsKey(fundcode)) {
                        /* 1784 */ this.std.setStudentBsScholarship9Amt(Integer.valueOf(0));
                        /* 1785 */ this.scholarship_amt_9 = 0;
                        /*      */                    }
                    /*      */                }
                /*      */            }
            /*      */        }
        /*      */
 /* 1791 */ return _res;
        /*      */    }

    /*      */
 /*      */ public final int enforceLsuLimits() {
        /* 1795 */ int _lsuSubtotal = 0, _lsuLimit = 0, _lsuLimitSubtotal = 0;

 /* 1813 */ _lsuSubtotal = round((this.calGrantA + this.calGrantB + this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9));

 /* 1834 */ if (this.use_need_ind > 0 && (this.calGrantA > 0 || this.calGrantB > 1551)) {
            /* 1835 */ _lsuLimit = getTuitionAndFees() - getFallOrientationFee(); //+ 1000;
            /*      */        } else {
            /* 1837 */ _lsuLimit = getTuitionAndFees() - getFallOrientationFee();
            /*      */        }
        /* 1839 */ _lsuLimitSubtotal = 0;
        /*      */
 /* 1841 */ if (this.use_need_ind > 0) {
            /* 1842 */ _lsuLimitSubtotal += this.calGrantA;
            /* 1843 */ if (_lsuLimitSubtotal > _lsuLimit) {
                /*      */
 /* 1845 */ this.calGrantA = _lsuLimit;
                /* 1846 */ this.calGrantB = 0;
                /* 1847 */ this.lsuAllowance = 0;
                /* 1848 */ this.sdaAward = 0;
                /*      */
 /* 1850 */ this.lsuNeedGrant = 0;
                /*      */
 /* 1852 */ this.lasuGrantAmt = 0;
                /*      */
 /* 1854 */ this.lsuAchievement = 0;
                /* 1855 */ this.lsu4yRenewable = 0;
                /*      */
 /* 1857 */ this.familyDiscount = 0;
                /* 1858 */ this.nationalMerit = 0;
                /* 1859 */ this.churchMatch = 0;
                /* 1860 */ this.pacificCampMatch = 0;
                /* 1861 */ this.nonPacificCampMatch = 0;
                /* 1862 */ this.litEvanMatch = 0;
                /* 1863 */ this.scholarship_amt_1 = 0;
                /* 1864 */ this.scholarship_amt_2 = 0;
                /* 1865 */ this.scholarship_amt_7 = 0;
                /* 1866 */ this.scholarship_amt_8 = 0;
                /* 1867 */ this.scholarship_amt_9 = 0;
                /*      */
 /* 1869 */ return _lsuSubtotal;
                /*      */            }
            /*      */
 /* 1872 */ _lsuLimitSubtotal += this.calGrantB;
            /* 1873 */ if (_lsuLimitSubtotal > _lsuLimit) {
                /*      */
 /* 1875 */ this.calGrantB -= _lsuLimitSubtotal - _lsuLimit;
                /* 1876 */ this.lsuAllowance = 0;
                /* 1877 */ this.sdaAward = 0;
                /*      */
 /* 1879 */ this.lsuNeedGrant = 0;
                /*      */
 /* 1881 */ this.lasuGrantAmt = 0;
                /*      */
 /* 1883 */ this.lsuAchievement = 0;
                /* 1884 */ this.lsu4yRenewable = 0;
                /*      *///this.universityHouseGrant=0;
 /* 1886 */ this.familyDiscount = 0;
                /* 1887 */ this.nationalMerit = 0;
                /* 1888 */ this.churchMatch = 0;
                /* 1889 */ this.pacificCampMatch = 0;
                /* 1890 */ this.nonPacificCampMatch = 0;
                /* 1891 */ this.litEvanMatch = 0;
                /* 1892 */ this.scholarship_amt_1 = 0;
                /* 1893 */ this.scholarship_amt_2 = 0;
                /* 1894 */ this.scholarship_amt_7 = 0;
                /* 1895 */ this.scholarship_amt_8 = 0;
                /* 1896 */ this.scholarship_amt_9 = 0;
                /* 1897 */ return _lsuSubtotal;
                /*      */            }
            /*      */        } else {
            /* 1900 */ this.calGrantA = 0;
            /* 1901 */ this.calGrantB = 0;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 1906 */ _lsuLimitSubtotal += this.lsuAllowance;
        /* 1907 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /*      */
 /* 1909 */ this.lsuAllowance -= _lsuLimitSubtotal - _lsuLimit;
            /* 1910 */ this.sdaAward = 0;
            /*      */
 /* 1912 */ this.lsuNeedGrant = 0;
            /*      */
 /* 1914 */ this.lasuGrantAmt = 0;
            /*      */
 /* 1916 */ this.lsuAchievement = 0;
            /* 1917 */ this.lsu4yRenewable = 0;
            /*      *///this.universityHouseGrant=0;
 /* 1919 */ this.familyDiscount = 0;
            /* 1920 */ this.nationalMerit = 0;
            /* 1921 */ this.churchMatch = 0;
            /* 1922 */ this.pacificCampMatch = 0;
            /* 1923 */ this.nonPacificCampMatch = 0;
            /* 1924 */ this.litEvanMatch = 0;
            /* 1925 */ this.scholarship_amt_1 = 0;
            /* 1926 */ this.scholarship_amt_2 = 0;
            /* 1927 */ this.scholarship_amt_7 = 0;
            /* 1928 */ this.scholarship_amt_8 = 0;
            /* 1929 */ this.scholarship_amt_9 = 0;
            /* 1930 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /* 1933 */ _lsuLimitSubtotal += this.sdaAward;
        /* 1934 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /*      */
 /* 1936 */ this.sdaAward -= _lsuLimitSubtotal - _lsuLimit;
            /*      */
 /*      */
 /* 1939 */ this.lsuNeedGrant = 0;
            /*      */
 /* 1941 */ this.lasuGrantAmt = 0;
            /*      */
 /* 1943 */ this.lsuAchievement = 0;
            /* 1944 */ this.lsu4yRenewable = 0;
            /*      *///this.universityHouseGrant=0;
 /* 1946 */ this.familyDiscount = 0;
            /* 1947 */ this.nationalMerit = 0;
            /* 1948 */ this.churchMatch = 0;
            /* 1949 */ this.pacificCampMatch = 0;
            /* 1950 */ this.nonPacificCampMatch = 0;
            /* 1951 */ this.litEvanMatch = 0;
            /* 1952 */ this.scholarship_amt_1 = 0;
            /* 1953 */ this.scholarship_amt_2 = 0;
            /* 1954 */ this.scholarship_amt_7 = 0;
            /* 1955 */ this.scholarship_amt_8 = 0;
            /* 1956 */ this.scholarship_amt_9 = 0;
            /* 1957 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /*      */
 /* 1961 */ if (this.use_need_ind > 0) {
            /* 1962 */ _lsuLimitSubtotal += this.lsuNeedGrant;
            /* 1963 */ if (_lsuLimitSubtotal > _lsuLimit) {
                /* 1964 */ this.lsuNeedGrant -= _lsuLimitSubtotal - _lsuLimit;
                /*      */
 /*      */
 /* 1967 */ this.lasuGrantAmt = 0;
                /*      */
 /* 1969 */ this.lsuAchievement = 0;
                /* 1970 */ this.lsu4yRenewable = 0;
                /*      *///this.universityHouseGrant=0;
 /* 1972 */ this.familyDiscount = 0;
                /* 1973 */ this.nationalMerit = 0;
                /* 1974 */ this.churchMatch = 0;
                /* 1975 */ this.pacificCampMatch = 0;
                /* 1976 */ this.nonPacificCampMatch = 0;
                /* 1977 */ this.litEvanMatch = 0;
                /* 1978 */ this.scholarship_amt_1 = 0;
                /* 1979 */ this.scholarship_amt_2 = 0;
                /* 1980 */ this.scholarship_amt_7 = 0;
                /* 1981 */ this.scholarship_amt_8 = 0;
                /* 1982 */ this.scholarship_amt_9 = 0;
                /* 1983 */ return _lsuSubtotal;
                /*      */            }
            /*      */        } else {
            /* 1986 */ this.lsuNeedGrant = 0;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /* 1992 */ if (this.use_need_ind > 0) {
            /* 1993 */ _lsuLimitSubtotal += this.lasuGrantAmt;
            /* 1994 */ if (_lsuLimitSubtotal > _lsuLimit) {
                /* 1995 */ this.lasuGrantAmt -= _lsuLimitSubtotal - _lsuLimit;
                /*      */
 /* 1997 */ this.lsuAchievement = 0;
                /* 1998 */ this.lsu4yRenewable = 0;
                /*      *///this.universityHouseGrant=0;
 /* 2000 */ this.familyDiscount = 0;
                /* 2001 */ this.nationalMerit = 0;
                /* 2002 */ this.churchMatch = 0;
                /* 2003 */ this.pacificCampMatch = 0;
                /* 2004 */ this.nonPacificCampMatch = 0;
                /* 2005 */ this.litEvanMatch = 0;
                /* 2006 */ this.scholarship_amt_1 = 0;
                /* 2007 */ this.scholarship_amt_2 = 0;
                /* 2008 */ this.scholarship_amt_7 = 0;
                /* 2009 */ this.scholarship_amt_8 = 0;
                /* 2010 */ this.scholarship_amt_9 = 0;
                /* 2011 */ return _lsuSubtotal;
                /*      */            }
            /*      */        } else {
            /* 2014 */ this.lasuGrantAmt = 0;
            /*      */        }
        /*      */
 /* 2017 */ _lsuLimitSubtotal += this.familyDiscount;
        /* 2018 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2019 */ this.familyDiscount -= _lsuLimitSubtotal - _lsuLimit;
            /* 2020 */ this.nationalMerit = 0;
            /*      */
 /* 2022 */ this.lsuAchievement = 0;
            /* 2023 */ this.lsu4yRenewable = 0;
            /*      *///this.universityHouseGrant=0;
 /* 2025 */ this.churchMatch = 0;
            /* 2026 */ this.pacificCampMatch = 0;
            /* 2027 */ this.nonPacificCampMatch = 0;
            /* 2028 */ this.litEvanMatch = 0;
            /* 2029 */ this.scholarship_amt_1 = 0;
            /* 2030 */ this.scholarship_amt_2 = 0;
            /* 2031 */ this.scholarship_amt_7 = 0;
            /* 2032 */ this.scholarship_amt_8 = 0;
            /* 2033 */ this.scholarship_amt_9 = 0;
            /* 2034 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /* 2037 */ _lsuLimitSubtotal += this.nationalMerit;
        /* 2038 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2039 */ this.nationalMerit -= _lsuLimitSubtotal - _lsuLimit;
            /*      */
 /* 2041 */ this.lsuAchievement = 0;
            /* 2042 */ this.lsu4yRenewable = 0;
            /*      *///this.universityHouseGrant=0;
 /* 2044 */ this.churchMatch = 0;
            /* 2045 */ this.pacificCampMatch = 0;
            /* 2046 */ this.nonPacificCampMatch = 0;
            /* 2047 */ this.litEvanMatch = 0;
            /* 2048 */ this.scholarship_amt_1 = 0;
            /* 2049 */ this.scholarship_amt_2 = 0;
            /* 2050 */ this.scholarship_amt_7 = 0;
            /* 2051 */ this.scholarship_amt_8 = 0;
            /* 2052 */ this.scholarship_amt_9 = 0;
            /* 2053 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /* 2056 */ _lsuLimitSubtotal += this.lsuAchievement;
        /* 2057 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2058 */ this.lsuAchievement -= _lsuLimitSubtotal - _lsuLimit;
            /*      */
 /* 2060 */ this.lsu4yRenewable = 0;
            /*      *///this.universityHouseGrant=0;
 /* 2062 */ this.churchMatch = 0;
            /* 2063 */ this.pacificCampMatch = 0;
            /* 2064 */ this.nonPacificCampMatch = 0;
            /* 2065 */ this.litEvanMatch = 0;
            /* 2066 */ this.scholarship_amt_1 = 0;
            /* 2067 */ this.scholarship_amt_2 = 0;
            /* 2068 */ this.scholarship_amt_7 = 0;
            /* 2069 */ this.scholarship_amt_8 = 0;
            /* 2070 */ this.scholarship_amt_9 = 0;
            /* 2071 */ return _lsuSubtotal;
            /*      */        }
        /* 2073 */ _lsuLimitSubtotal += this.lsu4yRenewable;
        /* 2074 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2075 */ this.lsu4yRenewable -= _lsuLimitSubtotal - _lsuLimit;
            /*      */
 /* 2077 */ this.churchMatch = 0;
            /* 2078 */ this.pacificCampMatch = 0;
            /* 2079 */ this.nonPacificCampMatch = 0;
            /* 2080 */ this.litEvanMatch = 0;
            /* 2081 */ this.scholarship_amt_1 = 0;
            /* 2082 */ this.scholarship_amt_2 = 0;
            /* 2083 */ this.scholarship_amt_7 = 0;
            /* 2084 */ this.scholarship_amt_8 = 0;
            /* 2085 */ this.scholarship_amt_9 = 0;
            /* 2086 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 2091 */ _lsuLimitSubtotal += this.churchMatch;
        /* 2092 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2093 */ this.churchMatch -= _lsuLimitSubtotal - _lsuLimit;
            /* 2094 */ this.pacificCampMatch = 0;
            /* 2095 */ this.nonPacificCampMatch = 0;
            /* 2096 */ this.litEvanMatch = 0;
            /* 2097 */ this.scholarship_amt_1 = 0;
            /* 2098 */ this.scholarship_amt_2 = 0;
            /* 2099 */ this.scholarship_amt_7 = 0;
            /* 2100 */ this.scholarship_amt_8 = 0;
            /* 2101 */ this.scholarship_amt_9 = 0;
            /* 2102 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /* 2105 */ _lsuLimitSubtotal += this.pacificCampMatch;
        /* 2106 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2107 */ this.pacificCampMatch -= _lsuLimitSubtotal - _lsuLimit;
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 2116 */ this.nonPacificCampMatch = 0;
            /* 2117 */ this.litEvanMatch = 0;
            /* 2118 */ this.scholarship_amt_1 = 0;
            /* 2119 */ this.scholarship_amt_2 = 0;
            /* 2120 */ this.scholarship_amt_7 = 0;
            /* 2121 */ this.scholarship_amt_8 = 0;
            /* 2122 */ this.scholarship_amt_9 = 0;
            /* 2123 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /* 2126 */ _lsuLimitSubtotal += this.nonPacificCampMatch;
        /* 2127 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2128 */ this.nonPacificCampMatch -= _lsuLimitSubtotal - _lsuLimit;
            /* 2129 */ this.litEvanMatch = 0;
            /* 2130 */ this.scholarship_amt_1 = 0;
            /* 2131 */ this.scholarship_amt_2 = 0;
            /* 2132 */ this.scholarship_amt_7 = 0;
            /* 2133 */ this.scholarship_amt_8 = 0;
            /* 2134 */ this.scholarship_amt_9 = 0;
            /* 2135 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /* 2138 */ _lsuLimitSubtotal += this.litEvanMatch;
        /* 2139 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2140 */ this.litEvanMatch -= _lsuLimitSubtotal - _lsuLimit;
            /* 2141 */ this.scholarship_amt_1 = 0;
            /* 2142 */ this.scholarship_amt_2 = 0;
            /* 2143 */ this.scholarship_amt_7 = 0;
            /* 2144 */ this.scholarship_amt_8 = 0;
            /* 2145 */ this.scholarship_amt_9 = 0;
            /* 2146 */ return _lsuSubtotal;
            /*      */        }
        /*      */
 /*      */
 /* 2150 */ _lsuLimitSubtotal += this.scholarship_amt_1;
        /* 2151 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2152 */ this.scholarship_amt_1 -= _lsuLimitSubtotal - _lsuLimit;
            /* 2153 */ if (this.scholarship_amt_1 < 0) {
                this.scholarship_amt_1 = 0;
            }
            /* 2154 */ this.scholarship_amt_2 = 0;
            /* 2155 */ this.scholarship_amt_7 = 0;
            /* 2156 */ this.scholarship_amt_8 = 0;
            /* 2157 */ this.scholarship_amt_9 = 0;
            /*      */        }
        /*      */
 /* 2160 */ _lsuLimitSubtotal += this.scholarship_amt_2;
        /* 2161 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2162 */ this.scholarship_amt_2 -= _lsuLimitSubtotal - _lsuLimit;
            /* 2163 */ if (this.scholarship_amt_2 < 0) {
                this.scholarship_amt_2 = 0;
            }
            /* 2164 */ this.scholarship_amt_7 = 0;
            /* 2165 */ this.scholarship_amt_8 = 0;
            /* 2166 */ this.scholarship_amt_9 = 0;
            /*      */        }
        /*      */
 /* 2169 */ _lsuLimitSubtotal += this.scholarship_amt_7;
        /* 2170 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2171 */ this.scholarship_amt_7 -= _lsuLimitSubtotal - _lsuLimit;
            /* 2172 */ if (this.scholarship_amt_7 < 0) {
                this.scholarship_amt_7 = 0;
            }
            /* 2173 */ this.scholarship_amt_8 = 0;
            /* 2174 */ this.scholarship_amt_9 = 0;
            /*      */        }
        /*      */
 /* 2177 */ _lsuLimitSubtotal += this.scholarship_amt_8;
        /* 2178 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2179 */ this.scholarship_amt_8 -= _lsuLimitSubtotal - _lsuLimit;
            /* 2180 */ if (this.scholarship_amt_8 < 0) {
                this.scholarship_amt_8 = 0;
            }
            /* 2181 */ this.scholarship_amt_9 = 0;
            /*      */        }
        /*      */
 /* 2184 */ _lsuLimitSubtotal += this.scholarship_amt_9;
        /* 2185 */ if (_lsuLimitSubtotal > _lsuLimit) {
            /* 2186 */ this.scholarship_amt_9 -= _lsuLimitSubtotal - _lsuLimit;
            /* 2187 */ if (this.scholarship_amt_9 < 0) {
                this.scholarship_amt_9 = 0;
            }
            /*      */
 /*      */        }
        /* 2190 */ return _lsuLimitSubtotal;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final boolean enforceOverallLimits() {
        /* 2197 */ int _lsuOverallLimit = 0, _lsuOverallSubtotal = 0;
        /* 2198 */ boolean _reduceRemaining = false;
        /* 2199 */ int _efcAmt = 0;

 /* 2225 */ _lsuOverallLimit = getMPA();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 2233 */ if (_lsuOverallLimit <= 0.01D) {
            /* 2234 */ _lsuOverallLimit = 0;
            /*      */        }
        /*      */
 /* 2237 */ _lsuOverallSubtotal = this.pellGrant + this.calGrantA + this.calGrantB + this.fseogAmt + this.extAllowance + this.nonCaGrantAmt + this.outsideAmt + this.churchBase + this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable + this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9 + this.subDirect + this.perkinsLoan + this.fwsAmount + this.unsubDirect;
 
 /* 2275 */ boolean reducedAid = false;
        /*      */
 /* 2277 */ int _lsuOverallLimitCheck = 0;
        /*      */
 /* 2279 */ if (this.use_need_ind > 0) {
            /*      */
 /*      */
 /* 2282 */ _lsuOverallLimitCheck = this.pellGrant;
            /*      */
 /* 2284 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2285 */ this.pellGrant -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2286 */ if (this.pellGrant < 0) {
                    this.pellGrant = 0;
                }
                /* 2287 */ _reduceRemaining = reduceAid(2);
                /* 2288 */ return true;
                /*      */            }
            /*      */
 /* 2291 */ _lsuOverallLimitCheck += this.calGrantA;
            /* 2292 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2293 */ this.calGrantA -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2294 */ if (this.calGrantA < 0) {
                    this.calGrantA = 0;
                }
                /* 2295 */ _reduceRemaining = reduceAid(3);
                /* 2296 */ return true;
                /*      */            }
            /*      */
 /* 2299 */ _lsuOverallLimitCheck += this.calGrantB;
            /* 2300 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2301 */ this.calGrantB -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2302 */ if (this.calGrantB < 0) {
                    this.calGrantB = 0;
                }
                /* 2303 */ _reduceRemaining = reduceAid(4);
                /* 2304 */ return true;
                /*      */            }
            /* 2306 */ _lsuOverallLimitCheck += this.fseogAmt;
            /* 2307 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2308 */ this.fseogAmt -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2309 */ if (this.fseogAmt < 0) {
                    this.fseogAmt = 0;
                }
                /* 2310 */ _reduceRemaining = reduceAid(5);
                /* 2311 */ return true;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 2315 */ this.pellGrant = 0;
            /* 2316 */ this.calGrantA = 0;
            /* 2317 */ this.calGrantB = 0;
            /* 2318 */ this.fseogAmt = 0;
            /*      */        }
        /*      */
 /*      */
 /* 2322 */ _lsuOverallLimitCheck += this.extAllowance;
        /* 2323 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2324 */ this.extAllowance -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2325 */ if (this.extAllowance < 0) {
                this.extAllowance = 0;
            }
            /* 2326 */ _reduceRemaining = reduceAid(6);
            /* 2327 */ return true;
            /*      */        }
        /*      */
 /*      */
 /* 2331 */ if (this.use_need_ind > 0) {
            /*      */
 /* 2333 */ _lsuOverallLimitCheck += this.nonCaGrantAmt;
            /* 2334 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2335 */ this.nonCaGrantAmt -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2336 */ if (this.nonCaGrantAmt < 0) {
                    this.nonCaGrantAmt = 0;
                }
                /* 2337 */ _reduceRemaining = reduceAid(7);
                /* 2338 */ return true;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 2342 */ this.nonCaGrantAmt = 0;
            /*      */        }
        /*      */
 /* 2345 */ _lsuOverallLimitCheck += this.outsideAmt;
        /* 2346 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2347 */ this.outsideAmt -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2348 */ if (this.outsideAmt < 0) {
                this.outsideAmt = 0;
            }
            /* 2349 */ _reduceRemaining = reduceAid(8);
            /* 2350 */ return true;
            /*      */        }
        /* 2352 */ _lsuOverallLimitCheck += this.churchBase;
        /* 2353 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2354 */ this.churchBase -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2355 */ if (this.churchBase < 0) {
                this.churchBase = 0;
            }
            /* 2356 */ _reduceRemaining = reduceAid(9);
            /* 2357 */ return true;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /* 2363 */ _lsuOverallLimitCheck += this.lsuAllowance;
        /* 2364 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2365 */ this.lsuAllowance -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2366 */ if (this.lsuAllowance < 0) {
                this.lsuAllowance = 0;
            }
            /* 2367 */ _reduceRemaining = reduceAid(10);
            /* 2368 */ return true;
            /*      */        }
        /* 2370 */ _lsuOverallLimitCheck += this.sdaAward;
        /* 2371 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /*      */
 /* 2373 */ this.sdaAward -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2374 */ if (this.sdaAward < 0) {
                this.sdaAward = 0;
            }
            /*      */
 /* 2376 */ _reduceRemaining = reduceAid(11);
            /* 2377 */ return true;
            /*      */        }

 /* 2391 */ if (this.use_need_ind > 0) {
            /*      */
 /*      */
 /* 2394 */ _lsuOverallLimitCheck += this.lsuNeedGrant;
            /* 2395 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2396 */ this.lsuNeedGrant -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2397 */ if (this.lsuNeedGrant < 0) {
                    this.lsuNeedGrant = 0;
                }
                /* 2398 */ _reduceRemaining = reduceAid(12);
                /* 2399 */ return true;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 2403 */ this.lsuNeedGrant = 0;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 2408 */ if (this.use_need_ind > 0) {
            /*      */
 /*      */
 /* 2411 */ _lsuOverallLimitCheck += this.lasuGrantAmt;
            /* 2412 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2413 */ this.lasuGrantAmt -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2414 */ if (this.lasuGrantAmt < 0) {
                    this.lasuGrantAmt = 0;
                }
                /* 2415 */ _reduceRemaining = reduceAid(13);
                /* 2416 */ return true;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 2420 */ this.lasuGrantAmt = 0;
            /*      */        }
        /*      */
 /* 2423 */ _lsuOverallLimitCheck += this.familyDiscount;
        /* 2424 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2425 */ this.familyDiscount -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2426 */ if (this.familyDiscount < 0) {
                this.familyDiscount = 0;
            }
            /* 2427 */ _reduceRemaining = reduceAid(14);
            /* 2428 */ return true;
            /*      */        }
        /* 2430 */ _lsuOverallLimitCheck += this.nationalMerit;
        /* 2431 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2432 */ this.nationalMerit -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2433 */ if (this.nationalMerit < 0) {
                this.nationalMerit = 0;
            }
            /* 2434 */ _reduceRemaining = reduceAid(15);
            /* 2435 */ return true;
            /*      */        }
        /*      */
 /* 2438 */ _lsuOverallLimitCheck += this.lsuAchievement;
        /* 2439 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2440 */ this.lsuAchievement -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2441 */ if (this.lsuAchievement < 0) {
                this.lsuAchievement = 0;
            }
            /* 2442 */ _reduceRemaining = reduceAid(16);
            /* 2443 */ return true;
            /*      */        }
        /* 2445 */ _lsuOverallLimitCheck += this.lsu4yRenewable;
        /* 2446 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2447 */ this.lsu4yRenewable -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2448 */ if (this.lsu4yRenewable < 0) {
                this.lsu4yRenewable = 0;
            }
            /* 2449 */ _reduceRemaining = reduceAid(17);
            /* 2450 */ return true;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 2455 */ _lsuOverallLimitCheck += this.churchMatch;
        /* 2456 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2457 */ this.churchMatch -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2458 */ if (this.churchMatch < 0) {
                this.churchMatch = 0;
            }
            /* 2459 */ _reduceRemaining = reduceAid(18);
            /* 2460 */ return true;
            /*      */        }
        /* 2462 */ _lsuOverallLimitCheck += this.pacificCampMatch;
        /* 2463 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2464 */ this.pacificCampMatch -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2465 */ if (this.pacificCampMatch < 0) {
                this.pacificCampMatch = 0;
            }
            /* 2466 */ _reduceRemaining = reduceAid(19);
            /* 2467 */ return true;
            /*      */        }
        /*      */
 /* 2470 */ _lsuOverallLimitCheck += this.nonPacificCampMatch;
        /* 2471 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2472 */ this.nonPacificCampMatch -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2473 */ if (this.nonPacificCampMatch < 0) {
                this.nonPacificCampMatch = 0;
            }
            /* 2474 */ _reduceRemaining = reduceAid(20);
            /* 2475 */ return true;
            /*      */        }
        /* 2477 */ _lsuOverallLimitCheck += this.litEvanMatch;
        /* 2478 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2479 */ this.litEvanMatch -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2480 */ if (this.litEvanMatch < 0) {
                this.litEvanMatch = 0;
            }
            /* 2481 */ _reduceRemaining = reduceAid(21);
            /* 2482 */ return true;
            /*      */        }
        /*      */
 /*      */
 /* 2486 */ _lsuOverallLimitCheck += this.scholarship_amt_1;
        /* 2487 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2488 */ this.scholarship_amt_1 -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2489 */ if (this.scholarship_amt_1 < 0) {
                this.scholarship_amt_1 = 0;
            }
            /* 2490 */ _reduceRemaining = reduceAid(22);
            /* 2491 */ return true;
            /*      */        }
        /* 2493 */ _lsuOverallLimitCheck += this.scholarship_amt_2;
        /* 2494 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2495 */ this.scholarship_amt_2 -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2496 */ if (this.scholarship_amt_2 < 0) {
                this.scholarship_amt_2 = 0;
            }
            /* 2497 */ _reduceRemaining = reduceAid(23);
            /* 2498 */ return true;
            /*      */        }
        /* 2500 */ _lsuOverallLimitCheck += this.scholarship_amt_7;
        /* 2501 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2502 */ this.scholarship_amt_7 -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2503 */ if (this.scholarship_amt_7 < 0) {
                this.scholarship_amt_7 = 0;
            }
            /* 2504 */ _reduceRemaining = reduceAid(24);
            /* 2505 */ return true;
            /*      */        }
        /* 2507 */ _lsuOverallLimitCheck += this.scholarship_amt_8;
        /* 2508 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2509 */ this.scholarship_amt_8 -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2510 */ if (this.scholarship_amt_8 < 0) {
                this.scholarship_amt_8 = 0;
            }
            /* 2511 */ _reduceRemaining = reduceAid(25);
            /* 2512 */ return true;
            /*      */        }
        /* 2514 */ _lsuOverallLimitCheck += this.scholarship_amt_9;
        /* 2515 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2516 */ this.scholarship_amt_9 -= _lsuOverallLimitCheck - _lsuOverallLimit;
            /* 2517 */ if (this.scholarship_amt_9 < 0) {
                this.scholarship_amt_9 = 0;
            }
            /* 2518 */ _reduceRemaining = reduceAid(26);
            /* 2519 */ return true;
            /*      */        }
        /*      */
 /*      */
 /* 2523 */ if (this.use_need_ind > 0) {
            /*      */
 /* 2525 */ _lsuOverallLimitCheck += this.subDirect;
            /*      */
 /* 2527 */ System.out.println("enforceOverallLimits() got subDirect=" + this.subDirect);
            /*      */
 /* 2529 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2530 */ this.subDirect -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2531 */ if (this.subDirect < 0) {
                    this.subDirect = 0;
                }
                /*      */
 /*      */
 /* 2534 */ System.out.println("enforceOverallLimits() max subDirect=" + this.subDirect);
                /*      */
 /* 2536 */ _reduceRemaining = reduceAid(27);
                /* 2537 */ return true;
                /*      */            }
            /* 2539 */ _lsuOverallLimitCheck += this.perkinsLoan;
            /* 2540 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2541 */ this.perkinsLoan -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2542 */ if (this.perkinsLoan < 0) {
                    this.perkinsLoan = 0;
                }
                /* 2543 */ _reduceRemaining = reduceAid(28);
                /* 2544 */ return true;
                /*      */            }
            /* 2546 */ _lsuOverallLimitCheck += this.fwsAmount;
            /* 2547 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
                /* 2548 */ this.fwsAmount -= _lsuOverallLimitCheck - _lsuOverallLimit;
                /* 2549 */ if (this.fwsAmount < 0) {
                    this.fwsAmount = 0;
                }
                /* 2550 */ _reduceRemaining = reduceAid(29);
                /* 2551 */ return true;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 2555 */ this.subDirect = 0;
            /* 2556 */ this.perkinsLoan = 0;
            /* 2557 */ this.fwsAmount = 0;
            /*      */        }
        /*      */
 /*      */
 /* 2561 */ _lsuOverallLimitCheck += this.unsubDirect;
        /*      */
 /*      */
 /*      */
 /* 2565 */ if (_lsuOverallLimitCheck > _lsuOverallLimit) {
            /* 2566 */ int _efcAmount = getEFC();
            /*      */
 /*      */
 /* 2569 */ if (!reducedAid) {
                /*      */
 /* 2571 */ this.unsubDirect -= _lsuOverallLimitCheck - _lsuOverallLimit - _efcAmount;
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */            } /* 2578 */ else if (_efcAmount > 0
                    && /* 2579 */ this.unsubDirect > _efcAmount) {
                /* 2580 */ this.unsubDirect = _efcAmount;
                /*      */            }
            /*      */
 /*      */
 /*      */
 /* 2585 */ if (this.unsubDirect > getUnSubDirect()) {
                /* 2586 */ this.unsubDirect = getUnSubDirect();
                /*      */            }
            /*      */
 /* 2589 */ if (this.unsubDirect < 0) {
                this.unsubDirect = 0;
            }
            /*      */
 /*      */        }
        /* 2592 */ return true;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final boolean reduceAid_new(String awdstr, String[] awds) {
        /*      */ int _efcAmount;
        /* 2600 */ boolean reducedAid = true;
        /*      */
 /* 2602 */ int _clearVar = Arrays.<String>asList(awds).indexOf(awdstr) + 1;
        /* 2603 */ if (_clearVar <= 0) {
            return false;
        }
        /*      */
 /* 2605 */ switch (_clearVar) {
            /*      */ case 1:
                /* 2607 */ this.pellGrant = 0;
            /*      */ case 2:
                /* 2609 */ this.calGrantA = 0;
            /*      */ case 3:
                /* 2611 */ this.calGrantB = 0;
            /*      */ case 4:
                /* 2613 */ this.fseogAmt = 0;
            /*      */ case 5:
                /* 2615 */ this.extAllowance = 0;
            /*      */ case 6:
                /* 2617 */ this.nonCaGrantAmt = 0;
            /*      */ case 7:
                /* 2619 */ this.outsideAmt = 0;
            /*      */ case 8:
                /* 2621 */ this.churchBase = 0;
            /*      */ case 9:
                /* 2623 */ this.lsuAllowance = 0;
            /*      */ case 10:
                /* 2625 */ this.sdaAward = 0;
            /*      */ case 11:
                /* 2627 */ this.lsuNeedGrant = 0;
            /*      */ case 12:
                /* 2629 */ this.lasuGrantAmt = 0;
            /*      */
 /*      */ case 13:
                /* 2632 */ this.familyDiscount = 0;
            /*      */ case 14:
                /* 2634 */ this.nationalMerit = 0;
            /*      */ case 15:
                /* 2636 */ this.lsuAchievement = 0;
            /*      */ case 16:
                /* 2638 */ this.lsu4yRenewable = 0;
            /*      */ case 17:
                /* 2640 */ this.churchMatch = 0;
            /*      */ case 18:
                /* 2642 */ this.pacificCampMatch = 0;
            /*      */ case 19:
                /* 2644 */ this.nonPacificCampMatch = 0;
            /*      */ case 20:
                /* 2646 */ this.litEvanMatch = 0;
            /*      */ case 21:
                /* 2648 */ this.scholarship_amt_1 = 0;
            /*      */ case 22:
                /* 2650 */ this.scholarship_amt_2 = 0;
            /*      */ case 23:
                /* 2652 */ this.scholarship_amt_7 = 0;
            /*      */ case 24:
                /* 2654 */ this.scholarship_amt_8 = 0;
            /*      */ case 25:
                /* 2656 */ this.scholarship_amt_9 = 0;
            /*      */ case 26:
                /* 2658 */ this.subDirect = 0;
            /*      */ case 27:
                /* 2660 */ this.perkinsLoan = 0;
            /*      */ case 28:
                /* 2662 */ this.fwsAmount = 0;
            /*      */ case 29:
                /* 2664 */ _efcAmount = getEFC();
                /* 2665 */ if (_efcAmount > 0) {
                    /* 2666 */ if (this.unsubDirect > _efcAmount) {
                        /* 2667 */ this.unsubDirect = _efcAmount;
                        break;
                        /*      */                    }
                    /* 2669 */ this.unsubDirect += _efcAmount;
                    /*      */                }
                /*      */ break;
            /*      */        }
        /* 2673 */ if (this.unsubDirect > getUnSubDirect()) {
            /* 2674 */ this.unsubDirect = getUnSubDirect();
            /*      */        }
        /* 2676 */ return true;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public final boolean reduceAid(int _clearVar) {
        /* 2682 */ boolean reducedAid = true;
        /* 2683 */ if (_clearVar <= 1) {
            /* 2684 */ this.pellGrant = 0;
            /*      */        }
        /* 2686 */ if (_clearVar <= 2) {
            /* 2687 */ this.calGrantA = 0;
            /*      */        }
        /* 2689 */ if (_clearVar <= 3) {
            /* 2690 */ this.calGrantB = 0;
            /*      */        }
        /* 2692 */ if (_clearVar <= 4) {
            /* 2693 */ this.fseogAmt = 0;
            /*      */        }
        /* 2695 */ if (_clearVar <= 5) {
            /* 2696 */ this.extAllowance = 0;
            /*      */        }
        /* 2698 */ if (_clearVar <= 6) {
            /* 2699 */ this.nonCaGrantAmt = 0;
            /*      */        }
        /* 2701 */ if (_clearVar <= 7) {
            /* 2702 */ this.outsideAmt = 0;
            /*      */        }
        /* 2704 */ if (_clearVar <= 8) {
            /* 2705 */ this.churchBase = 0;
            /*      */        }
        /* 2707 */ if (_clearVar <= 9) {
            /* 2708 */ this.lsuAllowance = 0;
            /*      */        }
        /* 2710 */ if (_clearVar <= 10) /*      */ {
            /* 2712 */ this.sdaAward = 0;
            /*      */        }
        /*      */
 /*      */
 /* 2716 */ if (_clearVar <= 11) {
            /* 2717 */ this.lsuNeedGrant = 0;
            /*      */        }
        /*      */
 /* 2720 */ if (_clearVar <= 12) {
            /* 2721 */ this.lasuGrantAmt = 0;
            /*      */        }
        /*      */
 /*      */
 /* 2725 */ if (_clearVar <= 13) {
            /* 2726 */ this.familyDiscount = 0;
            /*      */        }
        /* 2728 */ if (_clearVar <= 14) {
            /* 2729 */ this.nationalMerit = 0;
            /*      */        }
        /*      */
 /*      */
 /* 2733 */ if (_clearVar <= 15) {
            /* 2734 */ this.lsuAchievement = 0;
            /*      */        }
        /* 2736 */ if (_clearVar <= 16) {
            /* 2737 */ this.lsu4yRenewable = 0;
            /*      */        }
        /*      */
 /*      */
 /* 2741 */ if (_clearVar <= 17) {
            /* 2742 */ this.churchMatch = 0;
            /*      */        }
        /* 2744 */ if (_clearVar <= 18) {
            /* 2745 */ this.pacificCampMatch = 0;
            /*      */        }
        /* 2747 */ if (_clearVar <= 19) {
            /* 2748 */ this.nonPacificCampMatch = 0;
            /*      */        }
        /* 2750 */ if (_clearVar <= 20) {
            /* 2751 */ this.litEvanMatch = 0;
            /*      */        }
        /*      */
 /* 2754 */ if (_clearVar <= 21) /*      */ {
            /* 2756 */ this.scholarship_amt_1 = 0;
            /*      */        }
        /* 2758 */ if (_clearVar <= 22) {
            /* 2759 */ this.scholarship_amt_2 = 0;
            /*      */        }
        /* 2761 */ if (_clearVar <= 23) {
            /* 2762 */ this.scholarship_amt_7 = 0;
            /*      */        }
        /* 2764 */ if (_clearVar <= 24) {
            /* 2765 */ this.scholarship_amt_8 = 0;
            /*      */        }
        /* 2767 */ if (_clearVar <= 25) {
            /* 2768 */ this.scholarship_amt_9 = 0;
            /*      */        }
        /*      */
 /* 2771 */ if (_clearVar <= 26) /*      */ {
            /* 2773 */ this.subDirect = 0;
            /*      */        }
        /* 2775 */ if (_clearVar <= 27) {
            /* 2776 */ this.perkinsLoan = 0;
            /*      */        }
        /* 2778 */ if (_clearVar <= 28) {
            /* 2779 */ this.fwsAmount = 0;
            /*      */        }
        /* 2781 */ if (_clearVar <= 29) {
            /* 2782 */ int _efcAmount = getEFC();
            /*      */
 /*      */
 /*      */
 /* 2786 */ if (_efcAmount > 0) {
                /* 2787 */ if (this.unsubDirect > _efcAmount) {
                    /*      */
 /* 2789 */ this.unsubDirect = _efcAmount;
                    /*      */                } else {
                    /* 2791 */ this.unsubDirect += _efcAmount;
                    /*      */                }
                /*      */            }
            /*      */        }
        /*      */
 /* 2796 */ if (this.unsubDirect > getUnSubDirect()) /*      */ {
            /* 2798 */ this.unsubDirect = getUnSubDirect();
            /*      */        }
        /* 2800 */ return true;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setSAVE_STUDENT_L_INTL_STUD(String newStrVal) {
        /* 2809 */ this.SAVE_STUDENT_L_INTL_STUD = newStrVal;
        /*      */    }


 /*      */ public void setSAVE_STUDENT_M_MARRY(String SAVE_STUDENT_M_MARRY) {
        /* 2821 */ this.SAVE_STUDENT_M_MARRY = SAVE_STUDENT_M_MARRY;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_N_SDA(String SAVE_STUDENT_N_SDA) {
        /* 2825 */ this.SAVE_STUDENT_N_SDA = SAVE_STUDENT_N_SDA;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_P_GPA(String SAVE_STUDENT_P_GPA) {
        /* 2829 */ this.SAVE_STUDENT_P_GPA = SAVE_STUDENT_P_GPA;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_Q_SAT(String SAVE_STUDENT_Q_SAT) {
        /* 2833 */ this.SAVE_STUDENT_Q_SAT = SAVE_STUDENT_Q_SAT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_Q_SAT_V(String SAVE_STUDENT_Q_SAT_V) {
        /* 2837 */ this.SAVE_STUDENT_Q_SAT_V = SAVE_STUDENT_Q_SAT_V;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_R_ACT(String SAVE_STUDENT_R_ACT) {
        /* 2841 */ this.SAVE_STUDENT_R_ACT = SAVE_STUDENT_R_ACT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_S_MERIT(String SAVE_STUDENT_S_MERIT) {
        /* 2845 */ this.SAVE_STUDENT_S_MERIT = SAVE_STUDENT_S_MERIT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_U_ACADEMIC(String SAVE_STUDENT_U_ACADEMIC) {
        /* 2849 */ this.SAVE_STUDENT_U_ACADEMIC = SAVE_STUDENT_U_ACADEMIC;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_V_FAMILY(String SAVE_STUDENT_V_FAMILY) {
        /* 2853 */ this.SAVE_STUDENT_V_FAMILY = SAVE_STUDENT_V_FAMILY;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_W_DORM(String SAVE_STUDENT_W_DORM) {
        /* 2857 */ this.SAVE_STUDENT_W_DORM = SAVE_STUDENT_W_DORM;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_X_FAFSA(String SAVE_STUDENT_X_FAFSA) {
        /* 2861 */ this.SAVE_STUDENT_X_FAFSA = SAVE_STUDENT_X_FAFSA;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_Y_INDEPT(String SAVE_STUDENT_Y_INDEPT) {
        /* 2865 */ this.SAVE_STUDENT_Y_INDEPT = SAVE_STUDENT_Y_INDEPT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_Z_CALGRANT(String SAVE_STUDENT_Z_CALGRANT) {
        /* 2869 */ this.SAVE_STUDENT_Z_CALGRANT = SAVE_STUDENT_Z_CALGRANT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AA_CALGRANT_A(String SAVE_STUDENT_AA_CALGRANT_A) {
        /* 2873 */ this.SAVE_STUDENT_AA_CALGRANT_A = SAVE_STUDENT_AA_CALGRANT_A;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AB_CALGRANT_B(String SAVE_STUDENT_AB_CALGRANT_B) {
        /* 2877 */ this.SAVE_STUDENT_AB_CALGRANT_B = SAVE_STUDENT_AB_CALGRANT_B;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AC_FAMILY_SIZE(String SAVE_STUDENT_AC_FAMILY_SIZE) {
        /* 2881 */ this.SAVE_STUDENT_AC_FAMILY_SIZE = SAVE_STUDENT_AC_FAMILY_SIZE;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AD_FAMILY_INCOME(String SAVE_STUDENT_AD_FAMILY_INCOME) {
        /* 2885 */ this.SAVE_STUDENT_AD_FAMILY_INCOME = SAVE_STUDENT_AD_FAMILY_INCOME;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AE_FAMILY_ASSET(String SAVE_STUDENT_AE_FAMILY_ASSET) {
        /* 2889 */ this.SAVE_STUDENT_AE_FAMILY_ASSET = SAVE_STUDENT_AE_FAMILY_ASSET;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AF_FAMILY_CONTRIB(String SAVE_STUDENT_AF_FAMILY_CONTRIB) {
        /* 2893 */ this.SAVE_STUDENT_AF_FAMILY_CONTRIB = SAVE_STUDENT_AF_FAMILY_CONTRIB;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AG_NONLSU_ALLOWRANCE(String SAVE_STUDENT_AG_NONLSU_ALLOWRANCE) {
        /* 2897 */ this.SAVE_STUDENT_AG_NONLSU_ALLOWRANCE = SAVE_STUDENT_AG_NONLSU_ALLOWRANCE;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AH_LSU_ALLOWRANCE(String SAVE_STUDENT_AH_LSU_ALLOWRANCE) {
        /* 2901 */ this.SAVE_STUDENT_AH_LSU_ALLOWRANCE = SAVE_STUDENT_AH_LSU_ALLOWRANCE;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AI_EDU_ALLOW_PER(String SAVE_STUDENT_AI_EDU_ALLOW_PER) {
        /* 2905 */ this.SAVE_STUDENT_AI_EDU_ALLOW_PER = SAVE_STUDENT_AI_EDU_ALLOW_PER;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AJ_HOME_STATE(String SAVE_STUDENT_AJ_HOME_STATE) {
        /* 2909 */ this.SAVE_STUDENT_AJ_HOME_STATE = SAVE_STUDENT_AJ_HOME_STATE;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AK_NONCAL_GRANT(String SAVE_STUDENT_AK_NONCAL_GRANT) {
        /* 2913 */ this.SAVE_STUDENT_AK_NONCAL_GRANT = SAVE_STUDENT_AK_NONCAL_GRANT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AL_OUT_SCHOLARSHIPS(String SAVE_STUDENT_AL_OUT_SCHOLARSHIPS) {
        /* 2917 */ this.SAVE_STUDENT_AL_OUT_SCHOLARSHIPS = SAVE_STUDENT_AL_OUT_SCHOLARSHIPS;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT(String SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT) {
        /* 2921 */ this.SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT = SAVE_STUDENT_AM_OUT_SCHOLARSHIP_AMT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AQ_UNSUB_LOANS(String SAVE_STUDENT_AQ_UNSUB_LOANS) {
        /* 2925 */ this.SAVE_STUDENT_AQ_UNSUB_LOANS = SAVE_STUDENT_AQ_UNSUB_LOANS;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AR_FWS(String SAVE_STUDENT_AR_FWS) {
        /* 2929 */ this.SAVE_STUDENT_AR_FWS = SAVE_STUDENT_AR_FWS;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AU_SCHOLARSHIP1_AMT(String SAVE_STUDENT_AU_SCHOLARSHIP1_AMT) {
        /* 2933 */ this.SAVE_STUDENT_AU_SCHOLARSHIP1_AMT = SAVE_STUDENT_AU_SCHOLARSHIP1_AMT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_AX_SCHOLARSHIP2_AMT(String SAVE_STUDENT_AX_SCHOLARSHIP2_AMT) {
        /* 2937 */ this.SAVE_STUDENT_AX_SCHOLARSHIP2_AMT = SAVE_STUDENT_AX_SCHOLARSHIP2_AMT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_BA_SCHOLARSHIP3_AMT(String SAVE_STUDENT_BA_SCHOLARSHIP3_AMT) {
        /* 2941 */ this.SAVE_STUDENT_BA_SCHOLARSHIP3_AMT = SAVE_STUDENT_BA_SCHOLARSHIP3_AMT;
        /*      */    }

    /*      */
 /*      */ public void setSAVE_STUDENT_BD_SCHOLARSHIP4_AMT(String SAVE_STUDENT_BD_SCHOLARSHIP4_AMT) {
        /* 2945 */ this.SAVE_STUDENT_BD_SCHOLARSHIP4_AMT = SAVE_STUDENT_BD_SCHOLARSHIP4_AMT;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void setSdaAward(int sdaAward) {
        /* 2950 */ this.sdaAward = sdaAward;
        /*      */    }

    /*      */
 /*      */ public void setFamilyDiscount(int familyDiscount) {
        /* 2954 */ this.familyDiscount = familyDiscount;
        /*      */    }

    /*      */
 /*      */ public String getNonCaGrantDesc() {
        /* 2958 */ return this._nonCaGrantDesc;
        /*      */    }

    /*      */
 /*      */ public void setNonCaGrantDesc(String nonCaGrantDesc) {
        /* 2962 */ this._nonCaGrantDesc = nonCaGrantDesc;
        /*      */    }

    /*      */
 /*      */ public int getNonCaGrantAmt() {
        /* 2966 */ return this._nonCaGrantAmt;
        /*      */    }

    /*      */
 /*      */ public void setNonCaGrantAmt(int nonCaGrantAmt) {
        /* 2970 */ this._nonCaGrantAmt = nonCaGrantAmt;
        /*      */    }

    /*      */
 /*      */ public void setOutsideScholarship(String outsideScholarship) {
        /* 2974 */ this._outsideScholarship = outsideScholarship;
        /*      */    }

    /*      */
 /*      */ public int getOutsideScholarshipAmt() {
        /* 2978 */ return this._outsideScholarshipAmt;
        /*      */    }

    /*      */
 /*      */ public void setOutsideScholarshipAmt(int outsideScholarshipAmt) {
        /* 2982 */ this._outsideScholarshipAmt = outsideScholarshipAmt;
        /*      */    }

    /*      */
 /*      */ public void setCalGrantA(int calGrantA) {
        /* 2986 */ this.calGrantA = calGrantA;
        /*      */    }

    /*      */
 /*      */ public void setCalGrantB(int calGrantB) {
        /* 2990 */ this.calGrantB = calGrantB;
        /*      */    }

    /*      */
 /*      */ public void setLsuAllowance(int lsuAllowance) {
        /* 2994 */ this.lsuAllowance = lsuAllowance;
        /*      */    }

    /*      */
 /*      */ public void setLsuPerformance(int lsuPerformance) {
        /* 2998 */ this.lsuPerformance = lsuPerformance;
        /*      */    }

    /*      */
 /*      */ public void setNationalMerit(int nationalMerit) {
        /* 3002 */ this.nationalMerit = nationalMerit;
        /*      */    }

    /*      */
 /*      */ public void setChurchMatch(int churchMatch) {
        /* 3006 */ this.churchMatch = churchMatch;
        /*      */    }

    /*      */
 /*      */ public void setPacificCampMatch(int pacificCampMatch) {
        /* 3010 */ this.pacificCampMatch = pacificCampMatch;
        /*      */    }

    /*      */
 /*      */ public void setNonPacificCampMatch(int nonPacificCampMatch) {
        /* 3014 */ this.nonPacificCampMatch = nonPacificCampMatch;
        /*      */    }

    /*      */
 /*      */ public void setLitEvanMatch(int litEvanMatch) {
        /* 3018 */ this.litEvanMatch = litEvanMatch;
        /*      */    }

    /*      */
 /*      */ public int getPellGrant() {
        /* 3022 */ return this.pellGrant;
        /*      */    }

    /*      */
 /*      */ public void setPellGrant(int pellGrant) {
        /* 3026 */ this.pellGrant = pellGrant;
        /*      */    }

    /*      */
 /*      */ public int getFseogAmt() {
        /* 3030 */ return this.fseogAmt;
        /*      */    }

    /*      */
 /*      */ public void setFseogAmt(int fseogAmt) {
        /* 3034 */ this.fseogAmt = fseogAmt;
        /*      */    }

    /*      */
 /*      */ public int getExtAllowance() {
        /* 3038 */ return this.extAllowance;
        /*      */    }

    /*      */
 /*      */ public void setExtAllowance(int extAllowance) {
        /* 3042 */ this.extAllowance = extAllowance;
        /*      */    }

    /*      */
 /*      */ public int getNonCalGrantAmt() {
        /* 3046 */ return this.nonCaGrantAmt;
        /*      */    }

    /*      */
 /*      */ public void setNonCalGrantAmt(int nonCaGrantAmt) {
        /* 3050 */ this.nonCaGrantAmt = nonCaGrantAmt;
        /*      */    }

    /*      */
 /*      */ public int getOutsideAmt() {
        /* 3054 */ return this.outsideAmt;
        /*      */    }

    /*      */
 /*      */ public void setOutsideAmt(int outsideAmt) {
        /* 3058 */ this.outsideAmt = outsideAmt;
        /*      */    }

    /*      */
 /*      */ public void setChurchBase(int churchBase) {
        /* 3062 */ this.churchBase = churchBase;
        /*      */    }

    /*      */
 /*      */ public void setSubDirect(int subDirect) {
        /* 3066 */ this.subDirect = subDirect;
        /*      */    }

    /*      */
 /*      */ public void setPerkinsLoan(int perkinsLoan) {
        /* 3070 */ this.perkinsLoan = perkinsLoan;
        /*      */    }

    /*      */
 /*      */ public int getFwsAmount() {
        /* 3074 */ return this.fwsAmount;
        /*      */    }

    /*      */
 /*      */ public void setFwsAmount(int fwsAmount) {
        /* 3078 */ this.fwsAmount = fwsAmount;
        /*      */    }

    /*      */
 /*      */ public int getUnsubDirect() {
        /* 3082 */ return this.unsubDirect;
        /*      */    }

    /*      */ public int getUnsubDirectAmt() {
        /* 3085 */ return this.unsubDirect;
        /*      */    }

    /*      */ public int getSubDirectAmt() {
        /* 3088 */ return this.subDirect;
        /*      */    }

    /*      */
 /*      */ public void setUnsubDirect(int unsubDirect) {
        /* 3092 */ this.unsubDirect = unsubDirect;
        /*      */    }

    /*      */
 /*      */ public int getNeedAmt() {
        /* 3096 */ return this.needAmt;
        /*      */    }

    /*      */
 /*      */ public void setNeedAmt(int needAmt) {
        /* 3100 */ this.needAmt = needAmt;
        /* 3101 */ if (needAmt < 0) {
            this.needAmt = 0;
        }
        /*      */    }

    /*      */
 /*      */ public void setTuitionAndFees(int tuitionAndFees) {
        /* 3105 */ this.tuitionAndFees = tuitionAndFees;
        /*      */    }

    /*      */
 /*      */ public int getAddlExp() {
        /* 3109 */ return this.addlExp;
        /*      */    }

    /*      */
 /*      */ public void setAddlExp(int addlExp) {
        /* 3113 */ this.addlExp = addlExp;
        /*      */    }

    /*      */
 /*      */ public int getEfcInit() {
        /* 3117 */ return 99999;
        /*      */    }

    /*      */
 /*      */ public int getEfc() {
        /* 3121 */ return this.efc;
        /*      */    }

    /*      */
 /*      */ public void setEfc(int efc) {
        /* 3125 */ this.efc = efc;
        /*      */    }

    /*      */
 /*      */ public String getExcludeNote() {
        /* 3129 */ return this.excludeNote;
        /*      */    }

    /*      */
 /*      */ public void setExcludeNote(String excludeNote) {
        /* 3133 */ this.excludeNote = excludeNote;
        /*      */    }

    /*      */
 /*      */ public int getLsuLimitSubtotal() {
        /* 3137 */ return this.lsuLimitSubtotal;
        /*      */    }

    /*      */
 /*      */ public void setLsuLimitSubtotal(int lsuLimitSubtotal) {
        /* 3141 */ this.lsuLimitSubtotal = lsuLimitSubtotal;
        /*      */    }

    /*      */
 /*      */ public int getErr() {
        /* 3145 */ return this.err;
        /*      */    }

    /*      */
 /*      */ public void setErr(int err) {
        /* 3149 */ this.err = err;
        /*      */    }

 /*      */ public void setOutsideDesc(String outsideDesc) {
        /* 3162 */ this.outsideDesc = outsideDesc;
        /*      */    }

    /*      */
 /*      */ public boolean isLsuOverallSubtotal() {
        /* 3166 */ return this.lsuOverallSubtotal;
        /*      */    }

    /*      */
 /*      */ public void setLsuOverallSubtotal(boolean lsuOverallSubtotal) {
        /* 3170 */ this.lsuOverallSubtotal = lsuOverallSubtotal;
        /*      */    }

    /*      */
 /*      */ public int getMaxAid() {
        /* 3174 */ return this.maxAid;
        /*      */    }

    /*      */
 /*      */ public void setMaxAid(int maxAid) {
        /* 3178 */ this.maxAid = maxAid;
        /*      */    }

    /*      */
 /*      */ public void setRoomAndBoard(int roomAndBoard) {
        /* 3182 */ this.roomAndBoard = roomAndBoard;
        /*      */    }

    /*      */
 /*      */ public void setPacificCampBase(int pacificCampBase) {
        /* 3186 */ this.pacificCampBase = pacificCampBase;
        /*      */    }

    /*      */
 /*      */ public void setNonPacificCampBase(int nonPacificCampBase) {
        /* 3190 */ this.nonPacificCampBase = nonPacificCampBase;
        /*      */    }

    /*      */
 /*      */ public void setLitEvanBase(int litEvanBase) {
        /* 3194 */ this.litEvanBase = litEvanBase;
        /*      */    }

    /*      */
 /*      */ public int getAmtDue() {
        /* 3198 */ return this.amtDue;
        /*      */    }

    /*      */
 /*      */ public void setAmtDue(int amtDue) {
        /* 3202 */ this.amtDue = amtDue;
        /* 3203 */ if (amtDue < 0) {
            this.amtDue = 0;
        }
        /*      */    }

    /*      */
 /*      */ public int getYearInAdvanceOption() {
        /* 3207 */ return this.yearInAdvanceOption;
        /*      */    }

    /*      */
 /*      */ public void setYearInAdvanceOption(int yearInAdvanceOption) {
        /* 3211 */ this.yearInAdvanceOption = yearInAdvanceOption;
        /*      */    }

    /*      */
 /*      */ public int getQuarterInAdvanceOption() {
        /* 3215 */ return this.quarterInAdvanceOption;
        /*      */    }

    /*      */
 /*      */ public void setQuarterInAdvanceOption(int quarterInAdvanceOption) {
        /* 3219 */ this.quarterInAdvanceOption = quarterInAdvanceOption;
        /*      */    }

    /*      */
 /*      */ public int getMonthlyOption() {
        /* 3223 */ return this.monthlyOption;
        /*      */    }

    /*      */
 /*      */ public void setMonthlyOption(int monthlyOption) {
        /* 3227 */ this.monthlyOption = monthlyOption;
        /*      */    }

    /*      */
 /*      */ public int getEa_lsu_per() {
        /* 3231 */ return this.ea_lsu_per;
        /*      */    }

    /*      */
 /*      */ public void setEa_lsu_per(int ea_lsu_per) {
        /* 3235 */ this.ea_lsu_per = ea_lsu_per;
        /*      */    }

    /*      */
 /*      */ public int getEa_nonlsu_per() {
        /* 3239 */ return this.ea_nonlsu_per;
        /*      */    }

    /*      */
 /*      */ public void setEa_nonlsu_per(int ea_nonlsu_per) {
        /* 3243 */ this.ea_nonlsu_per = ea_nonlsu_per;
        /*      */    }

    /*      */
 /*      */ public int getEa_nonlsu_dorm_per() {
        /* 3247 */ return this.ea_nonlsu_dorm_per;
        /*      */    }

    /*      */
 /*      */ public void setEa_nonlsu_dorm_per(int ea_nonlsu_dorm_per) {
        /* 3251 */ this.ea_nonlsu_dorm_per = ea_nonlsu_dorm_per;
        /*      */    }

    /*      */
 /*      */ public boolean isAdjust_calgrant_amt_ind() {
        /* 3255 */ return this.adjust_calgrant_amt_ind;
        /*      */    }

    /*      */
 /*      */ public void setAdjust_calgrant_amt_ind(boolean adjust_calgrant_amt_ind) {
        /* 3259 */ this.adjust_calgrant_amt_ind = adjust_calgrant_amt_ind;
        /*      */    }

    /*      */
 /*      */ public int getScholarship_amt_1() {
        /* 3263 */ return this.scholarship_amt_1;
        /*      */    }

    /*      */
 /*      */ public void setScholarship_amt_1(int scholarship_amt_1) {
        /* 3267 */ this.scholarship_amt_1 = scholarship_amt_1;
        /*      */    }

    /*      */
 /*      */ public int getScholarship_amt_2() {
        /* 3271 */ return this.scholarship_amt_2;
        /*      */    }

    /*      */
 /*      */ public void setScholarship_amt_2(int scholarship_amt_2) {
        /* 3275 */ this.scholarship_amt_2 = scholarship_amt_2;
        /*      */    }

    /*      */
 /*      */ public int getScholarship_amt_7() {
        /* 3279 */ return this.scholarship_amt_7;
        /*      */    }

    /*      */
 /*      */ public void setScholarship_amt_7(int scholarship_amt_7) {
        /* 3283 */ this.scholarship_amt_7 = scholarship_amt_7;
        /*      */    }

    /*      */
 /*      */ public int getScholarship_amt_8() {
        /* 3287 */ return this.scholarship_amt_8;
        /*      */    }

    /*      */
 /*      */ public void setScholarship_amt_8(int scholarship_amt_8) {
        /* 3291 */ this.scholarship_amt_8 = scholarship_amt_8;
        /*      */    }

    /*      */
 /*      */ public int getScholarship_amt_9() {
        /* 3295 */ return this.scholarship_amt_9;
        /*      */    }

    /*      */
 /*      */ public void setScholarship_amt_9(int scholarship_amt_9) {
        /* 3299 */ this.scholarship_amt_9 = scholarship_amt_9;
        /*      */    }

    /*      */
 /*      */ public void setLsu4yRenewable(int lsu4yRenewable) {
        /* 3303 */ this.lsu4yRenewable = lsu4yRenewable;
        /*      */    }

    /*      */
 /*      */ public void setLsuAchievement(int lsuAchievement) {
        /* 3307 */ this.lsuAchievement = lsuAchievement;
        /*      */    }

    /*      */
 /*      */ public int getLsuAchievementInit() {
        /* 3311 */ return this.lsuAchievementInit;
        /*      */    }

    /*      */
 /*      */ public void setLsuAchievementInit(int lsuAchievementInit) {
        /* 3315 */ this.lsuAchievementInit = lsuAchievementInit;
        /*      */    }

    /*      */
 /*      */ public int getLsuNeedGrant() {
        /* 3319 */ return this.lsuNeedGrant;
        /*      */    }

    /*      */
 /*      */ public void setLsuNeedGrant(int lsuNeedGrant) {
        /* 3323 */ this.lsuNeedGrant = lsuNeedGrant;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void setStud(Student std) {
        /* 3328 */ this.std = std;
        /*      */
 /* 3330 */ newPackFunctions(std);
        /*      */    }

    /*      */
 /*      */
 /*      */ public void refreshCalc(Student newstd) {
        /* 3335 */ this.std = newstd;
        /* 3336 */ setStd(newstd);
        /*      */
 /*      */
 /*      */
 /* 3340 */ this.initdone_ind = 0;
        /*      */
 /*      */
 /* 3343 */ int LASU_COA = getOtherExpenses();
        /*      */
 /*      */
 /* 3346 */ int LASU_TUITION = getTuitionAndFees();
        /* 3347 */ int LASU_NEED = LASU_COA - getEFC();
        /* 3348 */ if (LASU_NEED < 0) {
            LASU_NEED = 0;
        }
        /*      */
 /*      */
 /*      */
 /* 3352 */ int LASU_MAXAID = 0;
        /*      */
 /* 3354 */ if ( (this.std.getStudentLIntlStud()!=null &&  this.std.getStudentLIntlStud().equalsIgnoreCase("No") )&& 
         (this.std.getStudentAkNoncalGrant() !=null && this.std.getStudentAkNoncalGrant().intValue() > 0 || (this.std.getStudentZCalgrant() !=null && this.std.getStudentZCalgrant().equalsIgnoreCase("YES") && this.std.getAdjCalgrantInd() !=null && this.std.getAdjCalgrantInd().equalsIgnoreCase("YES") && this.std.getStudentAaCalgrantA() !=null && this.std.getStudentAbCalgrantB()!=null && this.std.getStudentAaCalgrantA().intValue() + this.std.getStudentAbCalgrantB().intValue() > 0))) {
            /* 3355 */ this.use_need_ind = 1;
            /*      */
 /* 3357 */ LASU_MAXAID = LASU_NEED;
            /*      */        } else {
            /* 3359 */ this.use_need_ind = 0;
            /* 3360 */ LASU_MAXAID = LASU_COA;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 3365 */ initAndShowPellGrantAmt();
        /*      */
 /*      */
 /* 3368 */ log.info("\n<<<<<<< doc[%s] by [n/a] refreshCalc() got 1st due amount after initAndShowPellGrantAmt(). use_need=%d, and get due=%d, needAmt=%d, maxAid=%d, fws=%d", new Object[]{(this.std.getRecid() == null) ? "new" : this.std.getRecid(), Integer.valueOf(this.use_need_ind), Integer.valueOf(this.amtDue), Integer.valueOf(this.needAmt), Integer.valueOf(this.maxAid), Integer.valueOf(this.fwsAmount)});
        /*      */
 /* 3370 */ if (this.use_need_ind < 1 && this.std.getStudentLIntlStud() !=null && this.std.getStudentLIntlStud().equalsIgnoreCase("No")) {
            /*      */
 /*      */
 /* 3373 */ int due1 = this.amtDue;
            /* 3374 */ int loan1 = this.unsubDirect;
            /* 3375 */ int maxaid1 = this.maxAid;
            /*      */
 /*      */
 /* 3378 */ setStd(newstd);
            /* 3379 */ this.use_need_ind = 1;
            /*      */
 /* 3381 */ initAndShowPellGrantAmt();
            /* 3382 */ int due2 = this.amtDue;
            /* 3383 */ int loan2 = this.unsubDirect;
            /* 3384 */ int maxaid2 = this.maxAid;
            /* 3385 */ log.info("\n ++++++++ refreshCalc() got 2nd amount after initAndShowPellGrantAmt() (w/need) to compare. use_need=%d, due=%d, needAmt=%d, maxAid=%d", new Object[]{Integer.valueOf(this.use_need_ind), Integer.valueOf(due2), Integer.valueOf(this.needAmt), Integer.valueOf(this.maxAid)});
            /*      */
 /*      */
 /*      */
if (maxaid2 < maxaid1) {
             
 setStd(newstd);
              this.use_need_ind = 0;//have to check
               
 initAndShowPellGrantAmt();
                log.info("<<<<<<< refreshCalc() found the 1st aid amount is better. restored via run 3rd initAndShowPellGrantAmt().");
                          }
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */ public void init() {
        /* 3407 */ resetValues();
        /*      */    }

    /*      */
 /* 3410 */ public PackFunctions() {
        this.actor = this;
        this.std = new Student();
    }

    public PackFunctions(Student std) {
        this.actor = this;
        /*      */ setStd(std);
    }

    /*      */
 /*      */ public String showPellGrantAmt() {
        /* 3414 */ if (!this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        /* 3415 */ return this.fmt.format(this.pellGrant);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private void adjustLoanAmt() {
        /* 3424 */ this.org_loan_amt_sub = this.subDirect;
        /* 3425 */ this.org_loan_amt_unsub = this.unsubDirect;
        /* 3426 */ this.org_loan_amt_perkins = this.perkinsLoan;
        /*      */
 /* 3428 */ this.subDirect = getLoanAmtAfterOrigination(this.subDirect);
        /*      */
 /*      */
 /*      */
 /*      */
 /* 3433 */ this.unsubDirect = getLoanAmtAfterOrigination(this.unsubDirect);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String initAndShowPellGrantAmt() {
        /* 3440 */ if (this.use_need_ind < 0) {
            /* 3441 */ if ((this.std.getStudentAkNoncalGrant() !=null && this.std.getStudentAkNoncalGrant().intValue() > 0 )
                    || (this.std.getStudentZCalgrant() !=null && this.std.getAdjCalgrantInd() !=null 
                    && this.std.getStudentZCalgrant().equalsIgnoreCase("YES") && this.std.getAdjCalgrantInd().equalsIgnoreCase("YES") && this.std.getStudentAaCalgrantA() !=null && this.std.getStudentAbCalgrantB()!=null && this.std.getStudentAaCalgrantA().intValue() + this.std.getStudentAbCalgrantB().intValue() > 0)) {
                /* 3442 */ this.use_need_ind = 1;
                /* 3443 */            } else if (this.pellGrant > 0 || this.calGrantA > 0 || this.calGrantB > 0 || this.fseogAmt > 0 || this.nonCaGrantAmt > 0 || 
                        this._nonCaGrantAmt > 0 || this.lsuNeedGrant > 0 || this.lasuGrantAmt > 0) {
                /* 3444 */ this.use_need_ind = 1;
                /*      */            } else {
                /* 3446 */ this.use_need_ind = 0;
                /*      */            }
            /* 3448 */ log.info("==== initAndShowPellGrantAmt() convert use_need_ind from -1 to [%d]", new Object[]{Integer.valueOf(this.use_need_ind)});
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 3453 */ this.needAmt = getMPA();
        /*      */
 /* 3455 */ this.pellGrant = this.actor.getPell();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3462 */ this.adjust_calgrant_amt_ind = this.std.getStudentZCalgrant() !=null && this.std.getStudentZCalgrant().equalsIgnoreCase("YES") ? 
         (this.std.getAdjCalgrantInd() !=null && this.std.getAdjCalgrantInd().equalsIgnoreCase("YES")) : false;
        /*      */
 /* 3464 */ this.calGrantA = this.actor.getCalGrantA();
        /* 3465 */ this.calGrantB = this.actor.getCalGrantB();
        /* 3466 */ this.fseogAmt = this.actor.getFseog();
        /* 3467 */ this.extAllowance = this.actor.getExternalAllowance();
        /*      */
 /* 3469 */ this.err = this.actor.getNonCaGrant();
        /* 3470 */ this.nonCaGrantDesc = this._nonCaGrantDesc;
        /* 3471 */ this.nonCaGrantAmt = this._nonCaGrantAmt;
        /*      */
 /* 3473 */ this.err = this.actor.getOutsideScholarship();
        /* 3474 */ this.outsideDesc = this._outsideScholarship;
        /* 3475 */ this.outsideAmt = this._outsideScholarshipAmt;
        /*      */
 /* 3477 */ this.churchBase = this.actor.getChurchBase();
        /* 3478 */ this.sdaAward = this.actor.getSdaAward();
        /*      */
 /*      */
 /* 3481 */ this.lsuAllowance = this.actor.getLsuAllowance();
        /* 3482 */ this.familyDiscount = this.actor.getFamilyDiscount();
        /*      */
 /* 3484 */ this.lsuNeedGrant = this.actor.getLsuNeedGrantAmt();
        /*      */
 /*      */
 /*      */
 /* 3488 */ this.lasuGrantAmt = this.actor.getLasuGrantAmt();// is same as lsuNeed Grant 
        /*      */
 /* 3490 */ this.nationalMerit = this.actor.getNationalMerit();
        /*      */
 /*      */this.lsu4yRenewable = this.actor.getLsu4yRenewable();
 /*      */
 this.universityHouseGrant = this.actor.getUnivHousingGrant();
 
 
 /*      */
 /*      */
 /*      */
 /* 3497 */ if (this.nationalMerit > 0) {
            /* 3498 */ this.lsuAchievement = 0;
            /*      */        } else {
            /* 3500 */ this.lsuAchievement = this.actor.getLsuAchievement();
            /*      */        }
        /*      */
 /* 3503 */ 
        /*      */
 /*      */
 /* 3506 */ if (this.lsu4yRenewable > this.lsuAchievement +  this.nationalMerit) {
            /* 3507 */ this.lsuAchievement = 0; 
            /* 3508 */ this.nationalMerit = 0;
            /*      */        } else {
            /* 3510 */ this.lsu4yRenewable = 0;
            /*      */        }
        /*      */
 /*      */   
 /* 3514 */ this.churchMatch = this.actor.getChurchMatch();
        /* 3515 */ this.pacificCampMatch = this.actor.getPacificCampMatch();
        /* 3516 */ this.nonPacificCampMatch = this.actor.getNonPacificCampMatch();
        /* 3517 */ this.litEvanMatch = this.actor.getLitEvanMatch();
        /*      */
 /*      */
 /* 3520 */ if (this.std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
            enforceCJLimits(); //start from here after lUnch
        }
        /*      */
 /* 3522 */ this.maxAid = this.pellGrant + this.calGrantA + this.calGrantB + this.fseogAmt + this.extAllowance + this.nonCaGrantAmt + this.outsideAmt + this.churchBase + this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.subDirect + this.perkinsLoan + this.fwsAmount + this.unsubDirect;
        /*      */
 /*      */
 /* 3525 */ this.maxAid += this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3528 */ this.sum_lasu_aid = this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3531 */ this.sum_tuition_aid = this.sum_lasu_aid + this.calGrantA + this.calGrantB;
        /* 3532 */ this.amtDue = this.needAmt - this.maxAid;
        /*      */
 /*      */
 /*      */
 /* 3536 */ this.lsuLimitSubtotal = this.actor.enforceLsuLimits();
        /*      */
 /*      */
 /* 3539 */ this.maxAid = this.pellGrant + this.calGrantA + this.calGrantB + this.fseogAmt + this.extAllowance + this.nonCaGrantAmt + this.outsideAmt + this.churchBase + this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.subDirect + this.perkinsLoan + this.fwsAmount + this.unsubDirect;
        /*      */
 /*      */
 /* 3542 */ this.maxAid += this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3545 */ this.sum_lasu_aid = this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3548 */ this.sum_tuition_aid = this.sum_lasu_aid + this.calGrantA + this.calGrantB;
        /* 3549 */ this.amtDue = this.needAmt - this.maxAid;
        /*      */
 /*      */
 /*      */
 /*      */
 /* 3554 */ this.subDirect = this.actor.getSubDirect();
        /* 3555 */ this.perkinsLoan = this.actor.getPerkinsLoan();
        /* 3556 */ this.unsubDirect = this.actor.getUnSubDirect();
        /*      */
 /* 3558 */ adjustLoanAmt();
        /*      */
 /*      */
 /*      */
 /*      */
 /* 3563 */ if (this.std.getIndExcloans() !=null && this.std.getIndExcloans().equalsIgnoreCase("Yes")) {
            /* 3564 */ this.subDirect = 0;
            /* 3565 */ this.perkinsLoan = 0;
            /* 3566 */ this.unsubDirect = 0;
            /*      */        } else {
            /* 3568 */ if (this.std.getStudentApSubLoans() !=null && this.std.getStudentApSubLoans().equalsIgnoreCase("no")) {
                /* 3569 */ this.subDirect = 0;
                /*      */            }
            /* 3571 */ if (this.std.getStudentAqUnsubLoans() !=null && this.std.getStudentAqUnsubLoans().equalsIgnoreCase("no")) {
                /* 3572 */ this.unsubDirect = 0;
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 3578 */ this.fwsAmount = this.actor.getFWS();
        /*      */
 /* 3580 */ this.maxAid = this.pellGrant + this.calGrantA + this.calGrantB + this.fseogAmt + this.extAllowance + this.nonCaGrantAmt + this.outsideAmt + this.churchBase + this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable+this.universityHouseGrant + this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.subDirect + this.perkinsLoan + this.fwsAmount + this.unsubDirect;
        /*      */
 /*      */
 /* 3583 */ this.maxAid += this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3586 */ this.sum_lasu_aid = this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3589 */ this.sum_tuition_aid = this.sum_lasu_aid + this.calGrantA + this.calGrantB;
        /* 3590 */ this.amtDue = this.needAmt - this.maxAid;
        /*      */
 /*      */
 /* 3593 */ String[] awd_name = {"pellGrant", "calGrantA", "calGrantB", "fseogAmt", "extAllowance", "nonCaGrantAmt", "outsideAmt", "churchBase", "lsuAllowance", "sdaAward", "lsuNeedGrant", "lasuGrantAmt", "lsuAchievement", "lsu4yRenewable", "familyDiscount", "nationalMerit", "churchMatch", "pacificCampMatch", "nonPacificCampMatch", "litEvanMatch", "subDirect", "perkinsLoan", "fwsAmount", "unsubDirect", "scholarship_amt_1", "scholarship_amt_2", "scholarship_amt_7", "scholarship_amt_8", "scholarship_amt_9"};
        /*      */
 /*      */
 /*      */
 /*      */
 /* 3598 */ int[] awd_val = {this.pellGrant, this.calGrantA, this.calGrantB, this.fseogAmt, this.extAllowance, this.nonCaGrantAmt, this.outsideAmt, this.churchBase, this.lsuAllowance, this.sdaAward, this.lsuNeedGrant, this.lasuGrantAmt, this.lsuAchievement, this.lsu4yRenewable, this.familyDiscount, this.nationalMerit, this.churchMatch, this.pacificCampMatch, this.nonPacificCampMatch, this.litEvanMatch, this.subDirect, this.perkinsLoan, this.fwsAmount, this.unsubDirect, this.scholarship_amt_1, this.scholarship_amt_2, this.scholarship_amt_7, this.scholarship_amt_8, this.scholarship_amt_9};
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3612 */ this.lsuOverallSubtotal = this.actor.enforceOverallLimits();
        /*      */
 /*      */
 /*      */
 /* 3616 */ if (this.unsubDirect == this.org_loan_amt_unsub) /*      */ {
            /* 3618 */ this.unsubDirect = getLoanAmtAfterOrigination(this.unsubDirect);
            /*      */        }
        /*      */
 /* 3621 */ this.maxAid = this.pellGrant + this.calGrantA + this.calGrantB + this.fseogAmt + this.extAllowance + this.nonCaGrantAmt + this.outsideAmt + this.churchBase + this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.subDirect + this.perkinsLoan + this.fwsAmount + this.unsubDirect;
        /*      */
 /*      */
 /*      */
 /* 3625 */ this.maxAid += this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /* 3626 */ log.debug("initAndShowPellGrantAmt().###<after enforcing overall limit>### get maxaid=%d while fwsAmt=%d, subloan=%d, unsub=%d, perkins=%d", new Object[]{Integer.valueOf(this.maxAid), Integer.valueOf(this.fwsAmount), Integer.valueOf(this.subDirect), Integer.valueOf(this.unsubDirect), Integer.valueOf(this.perkinsLoan)});
        /*      */
 /* 3628 */ int[] awd_val2 = {this.pellGrant, this.calGrantA, this.calGrantB, this.fseogAmt, this.extAllowance, this.nonCaGrantAmt, this.outsideAmt, this.churchBase, this.lsuAllowance, this.sdaAward, this.lsuNeedGrant, this.lasuGrantAmt, this.lsuAchievement, this.lsu4yRenewable, this.familyDiscount, this.nationalMerit, this.churchMatch, this.pacificCampMatch, this.nonPacificCampMatch, this.litEvanMatch, this.subDirect, this.perkinsLoan, this.fwsAmount, this.unsubDirect, this.scholarship_amt_1, this.scholarship_amt_2, this.scholarship_amt_7, this.scholarship_amt_8, this.scholarship_amt_9};
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3642 */ this.sum_lasu_aid = this.lsuAllowance + this.sdaAward + this.lsuNeedGrant + this.lasuGrantAmt + this.lsuAchievement + this.lsu4yRenewable +this.universityHouseGrant+ this.familyDiscount + this.nationalMerit + this.churchMatch + this.pacificCampMatch + this.nonPacificCampMatch + this.litEvanMatch + this.scholarship_amt_1 + this.scholarship_amt_2 + this.scholarship_amt_7 + this.scholarship_amt_8 + this.scholarship_amt_9;
        /*      */
 /*      */
 /* 3645 */ this.sum_tuition_aid = this.sum_lasu_aid + this.calGrantA + this.calGrantB;
        /*      */
 /*      */
 /*      */
 /* 3649 */ this.amtDue = this.needAmt - this.maxAid;
        /*      */
 /*      */
 /* 3652 */ this.amtDue = (this.amtDue < 0) ? 0 : this.amtDue;
        /*      */
 /*      */
 /*      */
 /* 3656 */ if (this.SAVE_STUDENT_X_FAFSA !=null && !this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        /* 3657 */ return this.fmt.format(this.pellGrant);
        /*      */    }

    /*      */
 /*      */ public int needbased() {
        /* 3661 */ return this.use_need_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showTuitionFees() {
        /* 3666 */ int val = this.actor.getTuitionAndFees();
        /* 3667 */ this.tuitionAndFees = val;
        /* 3668 */ return this.fmt.format(val);
        /*      */    }

    /*      */
 /*      */ public String showAddlExpense() {
        /* 3672 */ int val = this.actor.getOtherExpenses() - this.tuitionAndFees;
        /* 3673 */ this.addlExp = val;
        /* 3674 */ return this.fmt.format(val);
        /*      */    }

    /*      */
 /*      */ public String showEFC() {
        /* 3678 */ int val = this.actor.getEFC();
        /* 3679 */ this.efc = val;
        /*      */
 /* 3681 */ if (val == 0) {
            /* 3682 */ this.needAmt = this.tuitionAndFees + this.addlExp;
            /*      */        } else {
            /* 3684 */ this.needAmt = this.tuitionAndFees + this.addlExp - val;
            /* 3685 */ if (this.needAmt < 0) {
                this.needAmt = 0;
            }
            /*      */
 /*      */        }
        /* 3688 */ if (this.actor.getInternationalStatus().equalsIgnoreCase("International") || !this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            /* 3689 */ //return this.fmt.format(0);
                        return "n/a"; 
            /*      */        }
        /* 3691 */ return this.fmt.format(val);
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showNeedAmt() {
        /* 3696 */ return this.fmt.format(this.needAmt);
        /*      */    }

    /*      */
 /*      */ public String showCalGrantA() {
        /* 3700 */ if (this.SAVE_STUDENT_Z_CALGRANT!=null && !this.SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        /* 3701 */ return this.fmt.format(this.calGrantA);
        /*      */    }

    /*      */ public String showCalGrantB() {
        /* 3704 */ if (this.SAVE_STUDENT_Z_CALGRANT!=null && !this.SAVE_STUDENT_Z_CALGRANT.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        /* 3705 */ return this.fmt.format(this.calGrantB);
        /*      */    }

    /*      */ public String showFseogAmt() {
        /* 3708 */ if (!this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("YES")) {
            return "n/a";
        }
        /* 3709 */ return this.fmt.format(this.fseogAmt);
        /*      */    }

    /*      */ public String showExtAllowanceAmt() {
        /* 3712 */ return this.fmt.format(this.extAllowance);
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getNonCalGrantDesc() {
        /* 3717 */ if (this.nonCaGrantDesc != null && this.nonCaGrantDesc.length() > 40) {
            /* 3718 */ return this.nonCaGrantDesc.substring(0, 37) + "...";
            /*      */        }
        /* 3720 */ return (this.nonCaGrantDesc == null) ? "n/a" : this.nonCaGrantDesc;
        /*      */    }

    /*      */
 /*      */ public String showNonCalGrantAmt() {
        /* 3724 */ return this.fmt.format(this.nonCaGrantAmt);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String getOutsideDesc() {
        /* 3730 */ if (this.outsideDesc != null && this.outsideDesc.length() > 40) {
            /* 3731 */ return this.outsideDesc.substring(0, 37) + "...";
            /*      */        }
        /* 3733 */ return (this.outsideDesc == null) ? "n/a" : this.outsideDesc;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showOutsideAmt() {
        /* 3738 */ return this.fmt.format(this.outsideAmt);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public int getChurchBaseAmt() {
        /* 3744 */ return this.churchBase;
        /*      */    }

    /*      */ public String showChurchBaseDesc() {
        /* 3747 */ return this.std.getStudentAyScholarship3Name();
        /*      */    }

    /*      */ public String showChurchBaseAmt() {
        /* 3750 */ return this.fmt.format(this.churchBase);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String showLsuAllowanceAmt() {
        /* 3757 */ return this.fmt.format(this.lsuAllowance);
        /*      */    }

    /*      */
 /*      */ public String showSdaAwardAmt() {
        /* 3761 */ return this.fmt.format(this.sdaAward);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String showLsuPerformanceAmt() {
        /* 3767 */ return this.fmt.format(this.lsuPerformance);
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getLsuAchievementAmt() {
        /* 3772 */ return this.lsuAchievement;
        /*      */    }

    /*      */ public String showLsuAchievementAmt() {
        /* 3775 */ return this.fmt.format(this.lsuAchievement);
        /*      */    }

    /*      */
 /*      */ public int getLsu4yRenewableAmt() {
        /* 3779 */ return this.lsu4yRenewable;
        /*      */    }

    /*      */ public String showLsu4yRenewableAmt() {
        /* 3782 */ return this.fmt.format(this.lsu4yRenewable);
        /*      */    }

    /*      */
    
//     public String showLsuMeritTF() {
//          boolean student_freshMan_transfer= this.std.getStd_transfer_ind()==1?true:false;
//         if (student_freshMan_transfer) {
//             return this.fmt.format(this.lsu4yRenewable);
//         } else {
//             return "0";
//         }
//
//     }
//
//     public String showLsuMeritFR() {
//          boolean student_freshMan_transfer= this.std.getStd_transfer_ind()==1?true:false;
//         if (!student_freshMan_transfer) {
//             return this.fmt.format(this.lsu4yRenewable);
//         } else {
//             return "0";
//         }
//     }
    
     public String showLsuMeritTF() {
          boolean student_merit_transfer= this.std.getStd_transfer_ind()==1?true:false;
         if (student_merit_transfer) {
             return this.fmt.format(this.lsu4yRenewable);
         } else {
             return this.fmt.format(0);

         }

     }

     public String showLsuMeritFR() {
          boolean student_merit_fr= this.std.getStd_1st_freshmen()==1?true:false;
         if (student_merit_fr) {
             return this.fmt.format(this.lsu4yRenewable);
         } else {
            return this.fmt.format(0);
         }
     }
     
         public String showLsuMeritR() {
          boolean student_returning= this.std.getReturnStdInd()==1?true:false;
         if (student_returning) {
             return this.fmt.format(this.lsu4yRenewable);
         } else {
              return this.fmt.format(0);
         }
     }
         
          public String showUnivHouseGrant() {
          boolean student_freshmen= this.std.getStd_1st_freshmen()==1?true:false;
          boolean isAdventist= this.std.getStudentNSda().equalsIgnoreCase("yes")?true:false;
          boolean isLiveInRes = this.std.getStudentWDorm().equalsIgnoreCase("yes")?true:false;
         if (student_freshmen && isAdventist && isLiveInRes) {
             return this.fmt.format(this.universityHouseGrant);
         } else {
            return this.fmt.format(0);
         }
     }
 /*      */
 /*      */
 /*      */
 /*      */ public String showLsuNeedGrantAmt() {
        /* 3789 */ return this.fmt.format(this.lsuNeedGrant);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String showFamilyDiscountAmt() {
        /* 3795 */ return this.fmt.format(this.familyDiscount);
        /*      */    }

    /*      */ public String showNationalMeritAmt() {
        /* 3798 */ return this.fmt.format(this.nationalMerit);
        /*      */    }

    /*      */
 /*      */ public int getChurchMatchAmt() {
        /* 3802 */ return this.churchMatch;
        /*      */    }

    /*      */ public String showChurchMatchDesc() {
        /* 3805 */ return cutBarStr2(this.std.getStudentAyScholarship3Name());
        /*      */    }

    /*      */ public String showChurchMatchAmt() {
        /* 3808 */ return this.fmt.format(this.churchMatch);
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getPacificCampMatchAmt() {
        /* 3813 */ return this.pacificCampMatch;
        /*      */    }

    /*      */ public String showPacificCampMatchDesc() {
        /* 3816 */ return cutBarStr2(this.std.getStudentBeScholarship5Name());
        /*      */    }

    /*      */ public String showPacificCampMatchAmt() {
        /* 3819 */ return this.fmt.format(this.pacificCampMatch);
        /*      */    }

    /*      */
 /*      */ public int getNonPacificCampMatchAmt() {
        /* 3823 */ return this.nonPacificCampMatch;
        /*      */    }

    /*      */ public String showNonPacificCampMatchDesc() {
        /* 3826 */ return cutBarStr2(this.std.getStudentBhScholarship6Name());
        /*      */    }

    /*      */ public String showNonPacificCampMatchAmt() {
        /* 3829 */ return this.fmt.format(this.nonPacificCampMatch);
        /*      */    }

    /*      */
 /*      */ public int getLitEvanMatchAmt() {
        /* 3833 */ return this.litEvanMatch;
        /*      */    }

    /*      */ public String showLitEvanMatchDesc() {
        /* 3836 */ return cutBarStr2(this.std.getStudentBbScholarship4Name());
        /*      */    }

    /*      */ public String showLitEvanMatchAmt() {
        /* 3839 */ return this.fmt.format(this.litEvanMatch);
        /*      */    }

    /*      */
 /*      */ public String showLsuLimitSubtotal() {
        /* 3843 */ return this.fmt.format(this.lsuLimitSubtotal);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String showSubdirectAmt() {
        /* 3850 */ return this.fmt.format(this.subDirect);
        /*      */    }

    /*      */
 /*      */ public String showPerkinLoanAmt() {
        /* 3854 */ return this.fmt.format(this.perkinsLoan);
        /*      */    }

    /*      */
 /*      */ public int getPerkinsLoanAmt() {
        /* 3858 */ return this.perkinsLoan;
        /*      */    }

    /*      */
 /*      */ public String showUnsubdirectAmt() {
        /* 3862 */ if (this.unsubDirect == this.org_loan_amt_unsub) {
            /* 3863 */ this.unsubDirect = getLoanAmtAfterOrigination(this.unsubDirect);
            /*      */        }
        /* 3865 */ return this.fmt.format(this.unsubDirect);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public int getFwsAmt() {
        /* 3872 */ return this.fwsAmount;
        /*      */    }

    /*      */
 /*      */ public String showFwsAmt() {
        /* 3876 */ return this.fmt.format(this.fwsAmount);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String showMaxAidAmt() {
        /* 3883 */ return this.fmt.format(this.maxAid);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String initAndShowRoomBoardAmt() {
        /* 3897 */ this.roomAndBoard = this.actor.getRoomAndBoard();
        /* 3898 */ this.pacificCampBase = this.actor.getPacificCampBase();
        /* 3899 */ this.nonPacificCampBase = this.actor.getNonPacificCampBase();
        /* 3900 */ this.litEvanBase = this.actor.getLitEvanBase();
        /* 3901 */ this.amtDue = this.tuitionAndFees + this.roomAndBoard - this.maxAid + this.fwsAmount - this.pacificCampBase - this.nonPacificCampBase - this.litEvanBase;
        /*      */
 /*      */
 /* 3904 */ if (this.amtDue < 0) {
            this.amtDue = 0;
        }
        /*      */
 /* 3906 */ return this.fmt.format(this.roomAndBoard);
        /*      */    }

    /*      */
 /*      */ public String showFaidExtDesc() {
        /* 3910 */ return (this.fwsAmount > 0 || this.std.getStudentArFws()!=null && this.std.getStudentArFws().equalsIgnoreCase("yes")) ? " (FWS excluded)" : " (FWS not Counted)";
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showFaidAmt() {
        /* 3915 */ return this.fmt.format((this.maxAid - this.fwsAmount));
        /*      */    }

    /*      */ public int getFaidExtAmt() {
        /* 3918 */ return this.maxAid - this.fwsAmount;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showPacificCampBaseDesc() {
        /* 3923 */ return "- " + cutBarStr2(this.std.getStudentBeScholarship5Name()) + " (Earnings)";
        /*      */    }

    /*      */ public String showPacificCampBaseAmt() {
        /* 3926 */ return this.fmt.format(this.pacificCampBase);
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getNonPacificCampBaseAmt() {
        /* 3931 */ return this.nonPacificCampBase;
        /*      */    }

    /*      */ public String showNonPacificCampBaseDesc() {
        /* 3934 */ return "- " + cutBarStr2(this.std.getStudentBhScholarship6Name()) + " (Earnings)";
        /*      */    }

    /*      */ public String showNonPacificCampBaseAmt() {
        /* 3937 */ return this.fmt.format(this.nonPacificCampBase);
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getLitEvanBaseAmt() {
        /* 3942 */ return this.litEvanBase;
        /*      */    }

    /*      */ public String showLitEvanBaseDesc() {
        /* 3945 */ return "- " + cutBarStr2(this.std.getStudentBbScholarship4Name()) + " (Earnings)";
        /*      */    }

    /*      */ public String showLitEvanBaseAmt() {
        /* 3948 */ return this.fmt.format(this.litEvanBase);
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showDueAmt() {
        /* 3953 */ return this.fmt.format(this.amtDue);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String initAndShowYIA() {
        /* 3960 */ if (this.amtDue > 0) {
            /* 3961 */ this.yearInAdvanceOption = this.amtDue - (new BigDecimal(this.amtDue)).multiply(PackValues.yearInAdvanceDiscount).intValue();
            /* 3962 */ this.quarterInAdvanceOption = (this.amtDue - (new BigDecimal(this.amtDue)).multiply(PackValues.quarterInAdvanceDiscount).intValue()) / 3;
            /* 3963 */ //this.monthlyOption = (this.amtDue + 90) / 9;
            /*      */   this.monthlyOption = (this.amtDue + 135) / 9;     
        } else {
            /* 3965 */ this.yearInAdvanceOption = 0;
            /* 3966 */ this.quarterInAdvanceOption = 0;
            /* 3967 */ this.monthlyOption = 0;
            /*      */        }
        /* 3969 */ return this.fmt.format(this.yearInAdvanceOption);
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showQIA() {
        /* 3974 */ return this.fmt.format(this.quarterInAdvanceOption);
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showMonthlyOption() {
        /* 3979 */ return this.fmt.format(this.monthlyOption);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public int getEaLsuPercentage() {
        /* 3986 */ return this.ea_lsu_per;
        /*      */    }

    /*      */ public int getEaNonLsuPercentage() {
        /* 3989 */ return this.ea_nonlsu_per;
        /*      */    }

    /*      */ public int getEaNonLsuPercentageByDorm(boolean in_dorm) {
        /* 3992 */ return in_dorm ? this.ea_nonlsu_dorm_per : this.ea_nonlsu_per;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void setAdjustCalGrantAmtInd(boolean ind) {
        /* 3997 */ this.adjust_calgrant_amt_ind = ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public int getScholarship1Amt() {
        /* 4003 */ return this.scholarship_amt_1;
        /*      */    }

    /*      */ public String showScholarship1Amt() {
        /* 4006 */ return this.fmt.format(this.scholarship_amt_1);
        /*      */    }

    /*      */ public String showScholarship1Desc() {
        /* 4009 */ return cutBarStr(this.std.getStudentAsScholarship1Name());
        /*      */    }

    /*      */ public String showScholarship5Desc() {
        /* 4012 */ return cutBarStr(this.std.getStudentBeScholarship5Name());
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getScholarship2Amt() {
        /* 4017 */ return this.scholarship_amt_2;
        /*      */    }

    /*      */ public String showScholarship2Amt() {
        /* 4020 */ return this.fmt.format(this.scholarship_amt_2);
        /*      */    }

    /*      */ public String showScholarship2Desc() {
        /* 4023 */ return cutBarStr(this.std.getStudentAvScholarship2Name());
        /*      */    }

    /*      */ public String showScholarship6Desc() {
        /* 4026 */ return cutBarStr(this.std.getStudentBhScholarship6Name());
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public int getScholarship7Amt() {
        /* 4032 */ return this.scholarship_amt_7;
        /*      */    }

    /*      */ public String showScholarship7Amt() {
        /* 4035 */ return this.fmt.format(this.scholarship_amt_7);
        /*      */    }

    /*      */ public String showScholarship7Desc() {
        /* 4038 */ return cutBarStr(this.std.getStudentBkScholarship7Name());
        /*      */    }

    /*      */
 /*      */ public int getScholarship8Amt() {
        /* 4042 */ return this.scholarship_amt_8;
        /*      */    }

    /*      */ public String showScholarship8Amt() {
        /* 4045 */ return this.fmt.format(this.scholarship_amt_8);
        /*      */    }

    /*      */ public String showScholarship8Desc() {
        /* 4048 */ return cutBarStr(this.std.getStudentBnScholarship8Name());
        /*      */    }

    /*      */
 /*      */ public int getScholarship9Amt() {
        /* 4052 */ return this.scholarship_amt_9;
        /*      */    }

    /*      */ public String showScholarship9Amt() {
        /* 4055 */ return this.fmt.format(this.scholarship_amt_9);
        /*      */    }

    /*      */ public String showScholarship9Desc() {
        /* 4058 */ return cutBarStr(this.std.getStudentBqScholarship9Name());
        /*      */    }

    /*      */
 /*      */ public String cutStr(String str, int max, String makeup) {
        /* 4062 */ if (str == null) {
            return "";
        }
        /* 4063 */ int i = str.length();
        /* 4064 */ if (i <= max) {
            return str;
        }
        /* 4065 */ return str.subSequence(0, max) + makeup;
        /*      */    }

    /*      */
 /*      */ public String cutBarStr(String str) {
        /* 4069 */ return cutStr(str, 40, "...");
        /*      */    }

    /*      */ public String cutBarStr2(String str) {
        /* 4072 */ return cutStr(str, 30, "...");
        /*      */    }

    /*      */
 /*      */
 /*      */ public String show_sum_lasu_aid() {
        /* 4077 */ return "La Sierra Awards (" + this.fmt.format(this.sum_lasu_aid) + ")";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public final int getLsuPerformanceOrg() {
        /* 4226 */ int _efc = 0, _lsuPerformance = 0;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4238 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes")) {
            /* 4239 */ _efc = Integer.parseInt(this.SAVE_STUDENT_AF_FAMILY_CONTRIB);
            /*      */
 /*      */        } /*      */ else {
            /*      */
 /* 4244 */ _efc = 0;
            /*      */        }
        /*      */
 /* 4247 */ if ((!TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")
                && /* 4248 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")
                && /* 4249 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")
                && /* 4250 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")
                && /* 4251 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) || this.std
                /* 4252 */.getStudentPGpa().compareTo(new BigDecimal(2)) < 0) {
            /* 4253 */ _lsuPerformance = 0;
            /*      */
 /*      */        } /* 4256 */ else if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR") || ( /* 4257 */TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")
                && /* 4258 */ getInternationalStatus().equalsIgnoreCase("Domestic"))) {
            /*      */
 /* 4260 */ if (this.std.getStudentQSat().intValue() < 950 && this.std.getStudentRAct().intValue() < 18) {
                /*      */
 /* 4262 */ if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 4617) {
                    /*      */
 /* 4264 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                        /* 4265 */ _lsuPerformance = 1760;
                        /* 4266 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                        /* 4267 */ _lsuPerformance = 5270;
                        /* 4268 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                        /* 4269 */ _lsuPerformance = 6440;
                        /*      */                    } else {
                        /* 4271 */ _lsuPerformance = 8790;
                        /*      */                    }
                    /* 4273 */                } else if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 12000) {
                    /*      */
 /* 4275 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                        /* 4276 */ _lsuPerformance = 1240;
                        /* 4277 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                        /* 4278 */ _lsuPerformance = 1240;
                        /* 4279 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                        /* 4280 */ _lsuPerformance = 3520;
                        /* 4281 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                        /* 4282 */ _lsuPerformance = 4680;
                        /*      */                    } else {
                        /* 4284 */ _lsuPerformance = 5270;
                        /*      */                    }
                    /* 4286 */                } else if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc > 12000) {
                    /*      */
 /* 4288 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                        /* 4289 */ _lsuPerformance = 870;
                        /* 4290 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                        /* 4291 */ _lsuPerformance = 870;
                        /* 4292 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                        /* 4293 */ _lsuPerformance = 2340;
                        /* 4294 */                    } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                        /* 4295 */ _lsuPerformance = 3520;
                        /*      */                    } else {
                        /* 4297 */ _lsuPerformance = 4090;
                        /*      */                    }
                    /*      */
 /*      */                } /* 4301 */ else if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4302 */ _lsuPerformance = 590;
                    /* 4303 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4304 */ _lsuPerformance = 590;
                    /* 4305 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4306 */ _lsuPerformance = 1760;
                    /* 4307 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4308 */ _lsuPerformance = 2340;
                    /*      */                } else {
                    /* 4310 */ _lsuPerformance = 2930;
                    /*      */
 /*      */                }
                /*      */
 /*      */            } /* 4315 */ else if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 4617) {
                /*      */
 /* 4317 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4318 */ _lsuPerformance = 1760;
                    /* 4319 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4320 */ _lsuPerformance = 4090;
                    /* 4321 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4322 */ _lsuPerformance = 5270;
                    /* 4323 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4324 */ _lsuPerformance = 6440;
                    /*      */                } else {
                    /* 4326 */ _lsuPerformance = 8790;
                    /*      */                }
                /* 4328 */            } else if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc <= 12000) {
                /*      */
 /* 4330 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4331 */ _lsuPerformance = 1240;
                    /* 4332 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4333 */ _lsuPerformance = 3520;
                    /* 4334 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4335 */ _lsuPerformance = 4680;
                    /* 4336 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4337 */ _lsuPerformance = 5270;
                    /*      */                } else {
                    /* 4339 */ _lsuPerformance = 7610;
                    /*      */                }
                /* 4341 */            } else if (this.SAVE_STUDENT_X_FAFSA.equalsIgnoreCase("Yes") && _efc < 100000) {
                /*      */
 /* 4343 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4344 */ _lsuPerformance = 870;
                    /* 4345 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4346 */ _lsuPerformance = 2340;
                    /* 4347 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4348 */ _lsuPerformance = 3520;
                    /* 4349 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4350 */ _lsuPerformance = 4090;
                    /*      */                } else {
                    /* 4352 */ _lsuPerformance = 6440;
                    /*      */                }
                /*      */
 /*      */            } /* 4356 */ else if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                /* 4357 */ _lsuPerformance = 590;
                /* 4358 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                /* 4359 */ _lsuPerformance = 1760;
                /* 4360 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                /* 4361 */ _lsuPerformance = 2340;
                /* 4362 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                /* 4363 */ _lsuPerformance = 2930;
                /*      */            } else {
                /* 4365 */ _lsuPerformance = 4680;
                /*      */            }
            /*      */
 /*      */        } /* 4369 */ else if (getInternationalStatus().equalsIgnoreCase("Domestic")) {
            /*      */
 /* 4371 */ if (_efc <= 4617) {
                /*      */
 /* 4373 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4374 */ _lsuPerformance = 2930;
                    /* 4375 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4376 */ _lsuPerformance = 4090;
                    /* 4377 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4378 */ _lsuPerformance = 5270;
                    /* 4379 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4380 */ _lsuPerformance = 6440;
                    /*      */                } else {
                    /* 4382 */ _lsuPerformance = 8790;
                    /*      */                }
                /* 4384 */            } else if (_efc <= 12000) {
                /*      */
 /* 4386 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4387 */ _lsuPerformance = 2340;
                    /* 4388 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4389 */ _lsuPerformance = 3560;
                    /* 4390 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4391 */ _lsuPerformance = 4680;
                    /* 4392 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4393 */ _lsuPerformance = 5270;
                    /*      */                } else {
                    /* 4395 */ _lsuPerformance = 7610;
                    /*      */                }
                /* 4397 */            } else if (_efc <= 100000) {
                /*      */
 /* 4399 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                    /* 4400 */ _lsuPerformance = 1760;
                    /* 4401 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                    /* 4402 */ _lsuPerformance = 2340;
                    /* 4403 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                    /* 4404 */ _lsuPerformance = 3520;
                    /* 4405 */                } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                    /* 4406 */ _lsuPerformance = 4090;
                    /*      */                } else {
                    /* 4408 */ _lsuPerformance = 6440;
                    /*      */                }
                /*      */
 /*      */            } /* 4412 */ else if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                /* 4413 */ _lsuPerformance = 0;
                /* 4414 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                /* 4415 */ _lsuPerformance = 1240;
                /* 4416 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                /* 4417 */ _lsuPerformance = 2340;
                /* 4418 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                /* 4419 */ _lsuPerformance = 2340;
                /*      */            } else {
                /* 4421 */ _lsuPerformance = 4680;
                /*      */
 /*      */            }
            /*      */
 /*      */        } /* 4426 */ else if (TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")) {
            /*      */
 /* 4428 */ if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
                /* 4429 */ _lsuPerformance = 4680;
                /* 4430 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
                /* 4431 */ _lsuPerformance = 4680;
                /* 4432 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
                /* 4433 */ _lsuPerformance = 4680;
                /* 4434 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
                /* 4435 */ _lsuPerformance = 4680;
                /* 4436 */            } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) >= 0) {
                /* 4437 */ _lsuPerformance = 4680;
                /*      */            }
            /*      */
 /*      */        } /* 4441 */ else if (this.std.getStudentPGpa().compareTo(new BigDecimal(2.5D)) < 0) {
            /* 4442 */ _lsuPerformance = 2340;
            /* 4443 */        } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.0D)) < 0) {
            /* 4444 */ _lsuPerformance = 3520;
            /* 4445 */        } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.5D)) < 0) {
            /* 4446 */ _lsuPerformance = 4680;
            /* 4447 */        } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) < 0) {
            /* 4448 */ _lsuPerformance = 5270;
            /* 4449 */        } else if (this.std.getStudentPGpa().compareTo(new BigDecimal(3.8D)) >= 0) {
            /* 4450 */ _lsuPerformance = 7610;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4457 */ if (!TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("FR")
                && /* 4458 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("F2")
                && /* 4459 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SO")
                && /* 4460 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("JR")
                && /* 4461 */ !TRIM(this.SAVE_STUDENT_U_ACADEMIC).equalsIgnoreCase("SR")) {
            /* 4462 */ _lsuPerformance = 0;
            /*      */        }
        /*      */
 /* 4465 */ return _lsuPerformance;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void resetGlobalVarXXX() {
        /* 4470 */ this._nonCaGrantDesc = "";
        /* 4471 */ this._nonCaGrantAmt = 0;
        /* 4472 */ this._outsideScholarship = "";
        /* 4473 */ this._outsideScholarshipAmt = 0;
        /*      */
 /* 4475 */ this.calGrantA = 0;
        /* 4476 */ this.calGrantB = 0;
        /* 4477 */ this.lsuAllowance = 0;
        /*      */
 /*      */
 /* 4480 */ this.lsuNeedGrant = 0;
        /*      */
 /* 4482 */ this.lasuGrantAmt = 0;
        /*      */
 /* 4484 */ this.lsuAchievement = 0;
        /* 4485 */ this.lsu4yRenewable = 0;
        /*      */
 /* 4487 */ this.nationalMerit = 0;
        /* 4488 */ this.churchMatch = 0;
        /* 4489 */ this.pacificCampMatch = 0;
        /* 4490 */ this.nonPacificCampMatch = 0;
        /* 4491 */ this.litEvanMatch = 0;
        /*      */
 /*      */
 /*      */
 /* 4495 */ this.pellGrant = 0;
        /* 4496 */ this.fseogAmt = 0;
        /* 4497 */ this.extAllowance = 0;
        /* 4498 */ this.nonCaGrantAmt = 0;
        /* 4499 */ this.outsideAmt = 0;
        /* 4500 */ this.churchBase = 0;
        /* 4501 */ this.subDirect = 0;
        /* 4502 */ this.perkinsLoan = 0;
        /* 4503 */ this.fwsAmount = 0;
        /* 4504 */ this.unsubDirect = 0;
        /*      */
 /*      */
 /* 4507 */ this.needAmt = 0;
        /*      */
 /*      */
 /* 4510 */ this.tuitionAndFees = 0;
        /* 4511 */ this.addlExp = 0;
        /* 4512 */ this.efc = 99999;
        /*      */
 /*      */
 /* 4515 */ this.excludeNote = "exclude loans";
        /*      */
 /*      */
 /* 4518 */ this.lsuLimitSubtotal = 0;
        /* 4519 */ this.err = 0;
        /*      */
 /* 4521 */ this.outsideDesc = "";
        /* 4522 */ this.lsuOverallSubtotal = false;
        /* 4523 */ this.maxAid = 0;
        /*      */
 /* 4525 */ this.roomAndBoard = 0;
        /* 4526 */ this.pacificCampBase = 0;
        /* 4527 */ this.nonPacificCampBase = 0;
        /* 4528 */ this.litEvanBase = 0;
        /* 4529 */ this.amtDue = 0;
        /*      */
 /* 4531 */ this.yearInAdvanceOption = 0;
        /* 4532 */ this.quarterInAdvanceOption = 0;
        /* 4533 */ this.monthlyOption = 0;
        /*      */
 /*      */
 /* 4536 */ this.scholarship_amt_1 = this.std.getStudentAuScholarship1Amt().intValue();
        /* 4537 */ this.scholarship_amt_2 = this.std.getStudentAxScholarship2Amt().intValue();
        /*      */
 /* 4539 */ this.scholarship_amt_7 = this.std.getStudentBmScholarship7Amt().intValue();
        /* 4540 */ this.scholarship_amt_8 = this.std.getStudentBpScholarship8Amt().intValue();
        /* 4541 */ this.scholarship_amt_9 = this.std.getStudentBsScholarship9Amt().intValue();
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getUse_need_ind() {
        /* 4546 */ return this.use_need_ind;
        /*      */    }

    /*      */
 /*      */ public int getSum_tuition_aid() {
        /* 4550 */ return this.sum_tuition_aid;
        /*      */    }

    /*      */
 /*      */ public int getSum_total_aid() {
        /* 4554 */ return this.sum_total_aid;
        /*      */    }

    /*      */
 /*      */ public int getSum_lasu_aid() {
        /* 4558 */ return this.sum_lasu_aid;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public int getOrg_loan_amt_sub() {
        /* 4565 */ return this.org_loan_amt_sub;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public int getOrg_loan_amt_unsub() {
        /* 4572 */ return this.org_loan_amt_unsub;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public int getOrg_loan_amt_perkins() {
        /* 4579 */ return this.org_loan_amt_perkins;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showAmt(int in_amt) {
        /* 4584 */ return this.fmt.format(in_amt);
        /*      */    }

    /*      */
 /*      */ public int getYearInAdvanceDiscountPerc() {
        /* 4588 */ return this.yearInAdvanceDiscountPerc;
        /*      */    }

    /*      */
 /*      */ public int getQuarterInAdvanceDiscountPerc() {
        /* 4592 */ return this.quarterInAdvanceDiscountPerc;
        /*      */    }
 
  public PackFunctions getActor() {
        return actor;
    }

    public void setActor(PackFunctions actor) {
        this.actor = actor;
    }
    /*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\PackFunctions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
