/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

//import com.sun.xml.internal.fastinfoset.tools.XML_SAX_StAX_FI;

import javax.enterprise.context.Dependent;
import java.math.BigDecimal;

/**
 *
 * @author kwang
 * This class seems is replaced by PackValues, which less static part vars, and moved those vars into OBJ level for multiple user access.
 */
//@SessionScoped
@Dependent
public class PackValuesStatic {         
    
    //- Charges by Quarter --->;
    public static final int qTuition      				 = 9060;//8715;
    public static final int qRoomAndBoard 				 = 2460;//2450;
    public static final int qHealth       				 = 378;
    public static final int qSaFee        				 = 0;
    public static final int qRecFee       				 = 0;
    public static final int qTechFee      				 = 0;
    public static final int qSecurityFee  				 = 0;
    public static final int qInternationalInsFee  = 551;//430;
    public static final int qFallOrientation      = 220;//no change
    public static final int qWinSprOrientation    = 50;

    public static final int  qTotalTuitionAndFeesDomestic              = qTuition + qHealth + qSaFee + qRecFee + qTechFee + qSecurityFee;
    public static final int  qTotalTuitionAndFeesInternational         = qTuition +           qSaFee + qRecFee + qTechFee + qSecurityFee + qInternationalInsFee;
    public static final int  qTotalTuitionAndFeesDomesticFrFall        = qTotalTuitionAndFeesDomestic + qFallOrientation;
    public static final int  qTotalTuitionAndFeesDomesticFrWinSpr      = qTotalTuitionAndFeesDomestic + qWinSprOrientation;
    public static final int  qTotalTuitionAndFeesInternationalFrFall   = qTotalTuitionAndFeesInternational + qFallOrientation;
    public static final int  qTotalTuitionAndFeesInternationalFrWinSpr = qTotalTuitionAndFeesInternational + qWinSprOrientation;

    public static final int  qCoaCommunity   = 13139;
    public static final int  qCoaDorm        = 13114;
    public static final BigDecimal  qCoaCommunityFr = new BigDecimal("13212.33");
    public static final BigDecimal  qCoaDormFr      = new BigDecimal("13180.67");

    //- Charges by Year ---;
    public static final int  yTuition       = qTuition      * 3;
    public static final int  yRoomAndBoard  = qRoomAndBoard * 3;
    public static final int  yHealth        = qHealth       * 3;
    public static final int  ySaFee         = qSaFee        * 3;
    public static final int  yRecFee        = qRecFee       * 3;
    public static final int  yTechFee       = qTechFee      * 3;
    public static final int  ySecurityFee   = qSecurityFee  * 3;
    public static final int  yInternationalInsFee = qInternationalInsFee * 4;
    public static final int  yFrOrientation = 220;

    public static final int  yTotalTuitionAndFeesDomestic        = qTotalTuitionAndFeesDomestic            * 3 ;
    public static final int  yTotalTuitionAndFeesInternational   = qTotalTuitionAndFeesInternational       * 3 ;
    public static final int  yTotalTuitionAndFeesDomesticFr      = qTotalTuitionAndFeesDomestic            * 3 + qFallOrientation ;
    public static final int  yTotalTuitionAndFeesInternationalFr = qTotalTuitionAndFeesInternational       * 3 + qFallOrientation ;

    public static final int yCoaCommunity   = 40815;
    public static final int yCoaDorm        = 40527;
    public static final int yCoaCommunityFr = 41035;
    public static final int yCoaDormFr      = 40747;

    //- Pell Grant ---;
    public static final int pellBase = 5550;//5500;

    //-public static final int pellMax4400 = pellBase - round(efc-51/100)*100 + 100>---;
    public static final int pellMax0    = 5550;
    public static final int pellMax4500 = 1176;
    public static final int pellMax4600 = 1176;
    public static final int pellMax4700 = 1176;
    public static final int pellMax4800 = 800;
    public static final int pellMax4900 = 700;
    public static final int pellMax5000 = 600;
    public static final int pellMax5100 = 555;
    public static final int pellMax5200 = 555;
    public static final int pellMax5273 = 555;
    public static final int pellMaxNone = 0;
    
    
    //- Cal Grant A ---;
    public static final int aCalGrant = 9708;
    public static final int aCalGrantMinGPA = 3;
    public static final int aCalGrantAssetCeilingInd        = 29500;//28800;
    public static final int aCalGrantAssetCeilingIndSingle  = 29500;//28800;
    public static final int aCalGrantAssetCeilingIndMarried = 62000;//28800;
    public static final int aCalGrantAssetCeilingOther      = 62000;//60500;

    public static final int aCalGrantIncomeCeilingIndSingleFamily1 = 29400;//28600;
    public static final int aCalGrantIncomeCeilingIndSingleFamily2 = 72000;//70200;
    public static final int aCalGrantIncomeCeilingIndSingleFamily3 = 73700;//71900;
    public static final int aCalGrantIncomeCeilingIndSingleFamily4 = 80100;//78100;
    public static final int aCalGrantIncomeCeilingIndSingleFamily5 = 85900;//83800;
    public static final int aCalGrantIncomeCeilingIndSingleFamily6 = 92600;//90300;

    public static final int aCalGrantIncomeCeilingIndMarriedFamily1 = 0; //- Impossible ---;
    public static final int aCalGrantIncomeCeilingIndMarriedFamily2 = 33600;//32800;
    public static final int aCalGrantIncomeCeilingIndMarriedFamily3 = 73700;//71900;
    public static final int aCalGrantIncomeCeilingIndMarriedFamily4 = 80100;//78100;
    public static final int aCalGrantIncomeCeilingIndMarriedFamily5 = 85900;//83800;
    public static final int aCalGrantIncomeCeilingIndMarriedFamily6 = 92600;//90300;

    public static final int aCalGrantIncomeCeilingOtherFamily1 = 0 ;//- Impossible ---;
    public static final int aCalGrantIncomeCeilingOtherFamily2 = 72000;//70200;
    public static final int aCalGrantIncomeCeilingOtherFamily3 = 73700;//71900;
    public static final int aCalGrantIncomeCeilingOtherFamily4 = 80100;//78100;
    public static final int aCalGrantIncomeCeilingOtherFamily5 = 85900;//83800;
    public static final int aCalGrantIncomeCeilingOtherFamily6 = 92600;//90300;

    //- Cal Grant B ---;
    public static final int bCalGrantFr = 1551;
    public static final int bCalGrantFr2 = 1551;
    public static final int bCalGrantSoJrSr = 11259;
    public static final int bCalGrantMaxGPA = 3;
    public static final int bCalGrantMinGPA = 2;
    public static final int bCalGrantAssetCeilingIndSingle = 28800;
    public static final int bCalGrantAssetCeilingIndMarried = 28800;
    public static final int bCalGrantAssetCeilingOther = 60500;

    public static final int bCalGrantIncomeCeilingIndSingleFamily1 = 29400;//28600;
    public static final int bCalGrantIncomeCeilingIndSingleFamily2 = 33600;//32800;
    public static final int bCalGrantIncomeCeilingIndSingleFamily3 = 37900;//36900;
    public static final int bCalGrantIncomeCeilingIndSingleFamily4 = 42100;//41100;
    public static final int bCalGrantIncomeCeilingIndSingleFamily5 = 47100;//46000;
    public static final int bCalGrantIncomeCeilingIndSingleFamily6 = 50900;//49600;

    public static final int bCalGrantIncomeCeilingIndMarriedFamily1 = 0;  //- Impossible ---;
    public static final int bCalGrantIncomeCeilingIndMarriedFamily2 = 33600;//32800;
    public static final int bCalGrantIncomeCeilingIndMarriedFamily3 = 37900;//36900;
    public static final int bCalGrantIncomeCeilingIndMarriedFamily4 = 42100;//41100;
    public static final int bCalGrantIncomeCeilingIndMarriedFamily5 = 47100;//46000;
    public static final int bCalGrantIncomeCeilingIndMarriedFamily6 = 50900;//49600;

    public static final int bCalGrantIncomeCeilingOtherFamily1 = 0;  //- Impossible ---;
    public static final int bCalGrantIncomeCeilingOtherFamily2 = 33600;//32800;
    public static final int bCalGrantIncomeCeilingOtherFamily3 = 37900;//36900;
    public static final int bCalGrantIncomeCeilingOtherFamily4 = 42100;//41100;
    public static final int bCalGrantIncomeCeilingOtherFamily5 = 47100;//46000;
    public static final int bCalGrantIncomeCeilingOtherFamily6 = 50900;//49600;

    //- FSEOG ---;
    public static final int standardFseogEfcSub1000 = 1100;
    public static final int standardFseogEfcSub2000 = 350;
    public static final int fseogWithCalGrantEdAllowOrNatMeritEfcSub1000 = 350;

    //- External Allowance (Typically ~30% of tuition) ---;

    //- Outside Scholarship (Flat Entry) ---;

    //- Church Matching Amount (Flat Entry, but we match 1:1) ---;

    //- LSU Educational Allowance (100% of tuition) ---;

    //- SDA Membership ---;
    public static final int sdaAwardInit =  1230;
    public static int       sdaAward     =  1230; //not final, , needs to be changed/assigned value in coldfusion fucntion

    //- LSU Performance Scholarship ---;
                //- Low Freshmen ---;
    public static final int  performanceSubSat950Act18FrSub4617Sub250 = 1760;
    public static final int  performanceSubSat950Act18FrSub4617Sub300 = 1760;
    public static final int  performanceSubSat950Act18FrSub4617Sub350 = 5270;
    public static final int  performanceSubSat950Act18FrSub4617Sub380 = 6440;
    public static final int  performanceSubSat950Act18FrSub4617Sub401 = 8790;

    public static final int  performanceSubSat950Act18FrSub12000Sub250 = 1240;
    public static final int  performanceSubSat950Act18FrSub12000Sub300 = 1240;
    public static final int  performanceSubSat950Act18FrSub12000Sub350 = 3520;
    public static final int  performanceSubSat950Act18FrSub12000Sub380 = 4680;
    public static final int  performanceSubSat950Act18FrSub12000Sub401 = 5270;

    public static final int  performanceSubSat950Act18FrSub100000Sub250 = 870;
    public static final int  performanceSubSat950Act18FrSub100000Sub300 = 870;
    public static final int  performanceSubSat950Act18FrSub100000Sub350 = 2340;
    public static final int  performanceSubSat950Act18FrSub100000Sub380 = 3520;
    public static final int  performanceSubSat950Act18FrSub100000Sub401 = 4090;

    public static final int  performanceSubSat950Act18FrSubNothingSub250 = 590;
    public static final int  performanceSubSat950Act18FrSubNothingSub300 = 590;
    public static final int  performanceSubSat950Act18FrSubNothingSub350 = 1760;
    public static final int  performanceSubSat950Act18FrSubNothingSub380 = 2340;
    public static final int  performanceSubSat950Act18FrSubNothingSub401 = 2930;
                //- High Freshmen ---;
    public static final int  performanceMinSat950Act18FrSub4617Sub250 = 1760;
    public static final int  performanceMinSat950Act18FrSub4617Sub300 = 4090;
    public static final int  performanceMinSat950Act18FrSub4617Sub350 = 5270;
    public static final int  performanceMinSat950Act18FrSub4617Sub380 = 6440;
    public static final int  performanceMinSat950Act18FrSub4617Sub401 = 8790;

    public static final int  performanceMinSat950Act18FrSub12000Sub250 = 1240;
    public static final int  performanceMinSat950Act18FrSub12000Sub300 = 3520;
    public static final int  performanceMinSat950Act18FrSub12000Sub350 = 4680;
    public static final int  performanceMinSat950Act18FrSub12000Sub380 = 5270;
    public static final int  performanceMinSat950Act18FrSub12000Sub401 = 7610;

    public static final int  performanceMinSat950Act18FrSub100000Sub250 = 870;
    public static final int  performanceMinSat950Act18FrSub100000Sub300 = 2340;
    public static final int  performanceMinSat950Act18FrSub100000Sub350 = 3520;
    public static final int  performanceMinSat950Act18FrSub100000Sub380 = 4090;
    public static final int  performanceMinSat950Act18FrSub100000Sub401 = 6440;

    public static final int  performanceMinSat950Act18FrSubNothingSub250 = 590;
    public static final int  performanceMinSat950Act18FrSubNothingSub300 = 1760;
    public static final int  performanceMinSat950Act18FrSubNothingSub350 = 2340;
    public static final int  performanceMinSat950Act18FrSubNothingSub380 = 2930;
    public static final int  performanceMinSat950Act18FrSubNothingSub401 = 4680;
                //- Other Demestic ---;
    public static final int  performanceSub4617Sub250 = 2930;
    public static final int  performanceSub4617Sub300 = 4090;
    public static final int  performanceSub4617Sub350 = 5270;
    public static final int  performanceSub4617Sub380 = 6440;
    public static final int  performanceSub4617Sub401 = 8790;

    public static final int  performanceSub12000Sub250 = 2340;
    public static final int  performanceSub12000Sub300 = 3560;
    public static final int  performanceSub12000Sub350 = 4680;
    public static final int  performanceSub12000Sub380 = 5270;
    public static final int  performanceSub12000Sub401 = 7610;

    public static final int  performanceSub100000Sub250 = 1760;
    public static final int  performanceSub100000Sub300 = 2340;
    public static final int  performanceSub100000Sub350 = 3520;
    public static final int  performanceSub100000Sub380 = 4090;
    public static final int  performanceSub100000Sub401 = 6440;

    public static final int  performanceSubNothingSub250 = 0;
    public static final int  performanceSubNothingSub300 = 1240;
    public static final int  performanceSubNothingSub350 = 2340;
    public static final int  performanceSubNothingSub380 = 2340;
    public static final int  performanceSubNothingSub401 = 4680;
                //- Other International ---;
    public static final int  performanceInternationalFrSub250 = 4680;
    public static final int  performanceInternationalFrSub300 = 4680;
    public static final int  performanceInternationalFrSub350 = 4680;
    public static final int  performanceInternationalFrSub380 = 4680;
    public static final int  performanceInternationalFrSub401 = 4680;

    public static final int  performanceInternationalSub250 = 2340;
    public static final int  performanceInternationalSub300 = 3520;
    public static final int  performanceInternationalSub350 = 4680;
    public static final int  performanceInternationalSub380 = 5270;
    public static final int  performanceInternationalSub401 = 7610;

    //- Family Discount ---;
    public static  int  familyDiscount = 900; //not final, needs to be changed/assigned value in coldfusion fucntion

    //- Non-Need Scholarship (National Merit: Commended, Semi-finalist, Finalist) ---;
    public static final BigDecimal nationalMeritMC = new BigDecimal(.2);
    public static final BigDecimal nationalMeritMS = new BigDecimal(.5);
    public static final BigDecimal nationalMeritMF = new BigDecimal(1);

    //- Church Matching (LSU Portion) ---;

    //- Sub Direct Loan ---;
    public static final int  subDirectLoanMaxFr  = 3500;
    public static final int  subDirectLoanMaxFr2 = 3500;
    public static final int  subDirectLoanMaxSo  = 4500;
    public static final int  subDirectLoanMaxJr  = 5500;
    public static final int  subDirectLoanMaxSr  = 5500;

    //- UnSub Direct Loan ---;
    public static final int  unsubDirectLoanMaxDepFr  = 5500;
    public static final int  unsubDirectLoanMaxDepFr2 = 5500;
    public static final int  unsubDirectLoanMaxDepSo  = 6500;
    public static final int  unsubDirectLoanMaxDepJr  = 7500;
    public static final int  unsubDirectLoanMaxDepSr  = 7500;

    public static final int  unsubDirectLoanMaxIndepFr  = 9500;
    public static final int  unsubDirectLoanMaxIndepFr2 = 9500;
    public static final int  unsubDirectLoanMaxIndepSo  = 10500;
    public static final int  unsubDirectLoanMaxIndepJr  = 12500;
    public static final int  unsubDirectLoanMaxIndepSr  = 12500;

    //- Perkins ---;
    public static final int  perkinsSub4996  = 1500; //changed
    public static final int  perkinsSub12001 = 2500; //changed

    //- Federal Work-study ---;
    public static final int  fwsSub4996   = 2000;
    public static final int  fwsSub12001  = 2500;
    public static final int  fwsSub100000 = 3000;

    
    
    //- Year-in-Advance Discount ---;
    public static final BigDecimal yearInAdvanceDiscount = new BigDecimal(0.07);

    //- Quarter-in-Advance Discount ---;
    public static final BigDecimal quarterInAdvanceDiscount = new BigDecimal(0.02);
    
    
    
    
    ///////////////////////////////////in coldfusion function enforceLSULimits(), global coldfusion var,  needs to be reaad and changed/assigned value in coldfusion fucntion    
    public static String _nonCaGrantDesc="";
    public static int _nonCaGrantAmt=0;
    public static String _outsideScholarship="";
    public static  int _outsideScholarshipAmt=0;
    
    public static int calGrantA=0;
    public static int  calGrantB=0;
    public static int  lsuAllowance=0;
  //    public static int sdaAward=0;  //already defined
    public static int  lsuPerformance=0;
  //     public static int  familyDiscount=0; //already defined
    public static int  nationalMerit=0;
    public static int  churchMatch=0;
    public static int  pacificCampMatch=0;
    public static int  nonPacificCampMatch=0;
    public static int  litEvanMatch=0;
      
      
      
      //in coldfusion function enforceOverallLimits()
      public static int pellGrant=0;
      public static int fseogAmt=0;
      public static int extAllowance=0;
      public static int nonCaGrantAmt=0;
      public static int outsideAmt=0;
      public static int churchBase=0;
      public static int subDirect=0;
      public static int perkinsLoan=0;
      public static int fwsAmount=0;
      public static int unsubDirect=0;
      
      //in coldfusion function getCOA()
      public static int needAmt=0;
      
      //================= used in summary.cfm
      public static int tuitionAndFees=0;
      public static int addlExp=0;
      
      public static final int efcInit  =99999;
      public static int efc            =99999; //2012-01-12 Esther said EFC should default max=99999, min==0
            
      //some vars defined in estimatorStudentData.cfm
      public static String excludeNote = "exclude loans";
      
      //some vars defined in estimatorSummary.cfm, and only used in that file.
      public static int lsuLimitSubtotal =0;
      public static int err =0;
      public static String nonCaGrantDesc = "";
      public static String outsideDesc = "";
      public static boolean lsuOverallSubtotal=false;
      public static int maxAid = 0;
      
      public static int   roomAndBoard       = 0; 
      public static int   pacificCampBase     = 0;
      public static int   nonPacificCampBase  = 0;
      public static int   litEvanBase         = 0;
      public static int   amtDue 		 = 0;				
      
      public static int yearInAdvanceOption =0;
      public static int quarterInAdvanceOption = 0;
      public static int monthlyOption = 0;
      
      
      //for education allowance percentage
      public static int ea_lsu_per = 100;
      public static int ea_nonlsu_per = 35;
      public static int ea_nonlsu_dorm_per = 70;
      
      ///for estimator to set adjust_cal_grant_amt indicator
      public static boolean adjust_calgrant_amt_ind=false;
      
      
      ////////added to hold the scholarship amount for #7 ~ #9, #1 and #2
      public static int scholarship_amt_1=0;
      public static int scholarship_amt_2=0;
      
      public static int scholarship_amt_7=0;
      public static int scholarship_amt_8=0;
      public static int scholarship_amt_9=0;
      
      //2012-02-06
      public static int lsu4yRenewable=0;
      public static int lsuAchievement=0;
      public static int lsuAchievementInit=10000;
      public static int lsuNeedGrant=0;
      
   static void resetValues(){
     /* the finals are not changed
    //- Charges by Quarter --->;
    qTuition      				 = 8715;
    qRoomAndBoard 				 = 2450;
    qHealth       				 = 115;
    qSaFee        				 = 58;
    qRecFee       				 = 70;
    qTechFee      				 = 82;
    qSecurityFee  				 = 40;
    qInternationalInsFee  = 430;
    qFallOrientation      = 220;
    qWinSprOrientation    = 50;

     qTotalTuitionAndFeesDomestic              = qTuition + qHealth + qSaFee + qRecFee + qTechFee + qSecurityFee;
     qTotalTuitionAndFeesInternational         = qTuition +           qSaFee + qRecFee + qTechFee + qSecurityFee + qInternationalInsFee;
     qTotalTuitionAndFeesDomesticFrFall        = qTotalTuitionAndFeesDomestic + qFallOrientation;
     qTotalTuitionAndFeesDomesticFrWinSpr      = qTotalTuitionAndFeesDomestic + qWinSprOrientation;
     qTotalTuitionAndFeesInternationalFrFall   = qTotalTuitionAndFeesInternational + qFallOrientation;
     qTotalTuitionAndFeesInternationalFrWinSpr = qTotalTuitionAndFeesInternational + qWinSprOrientation;

     qCoaCommunity   = 13139;
     qCoaDorm        = 13114;
     qCoaCommunityFr = new BigDecimal(13212.33);
     qCoaDormFr      = new BigDecimal(13180.67);

    //- Charges by Year ---;
     yTuition       = qTuition      * 3;
     yRoomAndBoard  = qRoomAndBoard * 3  ;
     yHealth        = qHealth       * 3;
     ySaFee         = qSaFee        * 3;
     yRecFee        = qRecFee       * 3;
     yTechFee       = qTechFee      * 3;
     ySecurityFee   = qSecurityFee  * 3;
     yInternationalInsFee = qInternationalInsFee * 4;
     yFrOrientation = 220;

     yTotalTuitionAndFeesDomestic        = qTotalTuitionAndFeesDomestic            * 3 ;
     yTotalTuitionAndFeesInternational   = qTotalTuitionAndFeesInternational       * 3 ;
     yTotalTuitionAndFeesDomesticFr      = qTotalTuitionAndFeesDomestic * 3 + qFallOrientation ;
     yTotalTuitionAndFeesInternationalFr = qTotalTuitionAndFeesInternational * 3 + qFallOrientation ;

    yCoaCommunity   = 39417;
    yCoaDorm        = 39342;
    yCoaCommunityFr = 39637;
    yCoaDormFr      = 39542;

    //- Pell Grant ---;
    pellBase = 5500;

    //-pellMax4400 = pellBase - round(efc-51/100)*100 + 100>---;
    pellMax0    = 5550;
    pellMax4500 = 1176;
    pellMax4600 = 1176;
    pellMax4700 = 1176;
    pellMax4800 = 800;
    pellMax4900 = 700;
    pellMax5000 = 600;
    pellMax5100 = 555;
    pellMax5200 = 555;
    pellMax5273 = 555;
    pellMaxNone = 0;

    //- Cal Grant A ---;
    aCalGrant = 9708;
    aCalGrantMinGPA = 3;
    aCalGrantAssetCeilingInd        = 28800;
    aCalGrantAssetCeilingIndSingle  = 28800;
    aCalGrantAssetCeilingIndMarried = 28800;
    aCalGrantAssetCeilingOther      = 60500;

    aCalGrantIncomeCeilingIndSingleFamily1 = 28600;
    aCalGrantIncomeCeilingIndSingleFamily2 = 70200;
    aCalGrantIncomeCeilingIndSingleFamily3 = 71900;
    aCalGrantIncomeCeilingIndSingleFamily4 = 78100;
    aCalGrantIncomeCeilingIndSingleFamily5 = 83800;
    aCalGrantIncomeCeilingIndSingleFamily6 = 90300;

    aCalGrantIncomeCeilingIndMarriedFamily1 = 0; //- Impossible ---;
    aCalGrantIncomeCeilingIndMarriedFamily2 = 32800;
    aCalGrantIncomeCeilingIndMarriedFamily3 = 71900;
    aCalGrantIncomeCeilingIndMarriedFamily4 = 78100;
    aCalGrantIncomeCeilingIndMarriedFamily5 = 83800;
    aCalGrantIncomeCeilingIndMarriedFamily6 = 90300;

    aCalGrantIncomeCeilingOtherFamily1 = 0 ;//- Impossible ---;
    aCalGrantIncomeCeilingOtherFamily2 = 70200;
    aCalGrantIncomeCeilingOtherFamily3 = 71900;
    aCalGrantIncomeCeilingOtherFamily4 = 78100;
    aCalGrantIncomeCeilingOtherFamily5 = 83800;
    aCalGrantIncomeCeilingOtherFamily6 = 90300;

    //- Cal Grant B ---;
    bCalGrantFr = 1551;
    bCalGrantFr2 = 1551;
    bCalGrantSoJrSr = 11259;
    bCalGrantMaxGPA = 3;
    bCalGrantMinGPA = 2;
    bCalGrantAssetCeilingIndSingle = 28800;
    bCalGrantAssetCeilingIndMarried = 28800;
    bCalGrantAssetCeilingOther = 60500;

    bCalGrantIncomeCeilingIndSingleFamily1 = 28600;
    bCalGrantIncomeCeilingIndSingleFamily2 = 32800;
    bCalGrantIncomeCeilingIndSingleFamily3 = 36900;
    bCalGrantIncomeCeilingIndSingleFamily4 = 41100;
    bCalGrantIncomeCeilingIndSingleFamily5 = 46000;
    bCalGrantIncomeCeilingIndSingleFamily6 = 49600;

    bCalGrantIncomeCeilingIndMarriedFamily1 = 0;  //- Impossible ---;
    bCalGrantIncomeCeilingIndMarriedFamily2 = 32800;
    bCalGrantIncomeCeilingIndMarriedFamily3 = 36900;
    bCalGrantIncomeCeilingIndMarriedFamily4 = 41100;
    bCalGrantIncomeCeilingIndMarriedFamily5 = 46000;
    bCalGrantIncomeCeilingIndMarriedFamily6 = 49600;

    bCalGrantIncomeCeilingOtherFamily1 = 0;  //- Impossible ---;
    bCalGrantIncomeCeilingOtherFamily2 = 32800;
    bCalGrantIncomeCeilingOtherFamily3 = 36900;
    bCalGrantIncomeCeilingOtherFamily4 = 41100;
    bCalGrantIncomeCeilingOtherFamily5 = 46000;
    bCalGrantIncomeCeilingOtherFamily6 = 49600;

    //- FSEOG ---;
    standardFseogEfcSub1000 = 1100;
    standardFseogEfcSub2000 = 350;
    fseogWithCalGrantEdAllowOrNatMeritEfcSub1000 = 350;
*/
    
    
    //- External Allowance (Typically ~30% of tuition) ---;

    //- Outside Scholarship (Flat Entry) ---;

    //- Church Matching Amount (Flat Entry, but we match 1:1) ---;

    //- LSU Educational Allowance (100% of tuition) ---;

    //- SDA Membership ---;
    sdaAward =  1230; //not final, , needs to be changed/assigned value in coldfusion fucntion

   /* 
    //- LSU Performance Scholarship ---;
                //- Low Freshmen ---;
     performanceSubSat950Act18FrSub4617Sub250 = 1760;
     performanceSubSat950Act18FrSub4617Sub300 = 1760;
     performanceSubSat950Act18FrSub4617Sub350 = 5270;
     performanceSubSat950Act18FrSub4617Sub380 = 6440;
     performanceSubSat950Act18FrSub4617Sub401 = 8790;

     performanceSubSat950Act18FrSub12000Sub250 = 1240;
     performanceSubSat950Act18FrSub12000Sub300 = 1240;
     performanceSubSat950Act18FrSub12000Sub350 = 3520;
     performanceSubSat950Act18FrSub12000Sub380 = 4680;
     performanceSubSat950Act18FrSub12000Sub401 = 5270;

     performanceSubSat950Act18FrSub100000Sub250 = 870;
     performanceSubSat950Act18FrSub100000Sub300 = 870;
     performanceSubSat950Act18FrSub100000Sub350 = 2340;
     performanceSubSat950Act18FrSub100000Sub380 = 3520;
     performanceSubSat950Act18FrSub100000Sub401 = 4090;

     performanceSubSat950Act18FrSubNothingSub250 = 590;
     performanceSubSat950Act18FrSubNothingSub300 = 590;
     performanceSubSat950Act18FrSubNothingSub350 = 1760;
     performanceSubSat950Act18FrSubNothingSub380 = 2340;
     performanceSubSat950Act18FrSubNothingSub401 = 2930;
                //- High Freshmen ---;
     performanceMinSat950Act18FrSub4617Sub250 = 1760;
     performanceMinSat950Act18FrSub4617Sub300 = 4090;
     performanceMinSat950Act18FrSub4617Sub350 = 5270;
     performanceMinSat950Act18FrSub4617Sub380 = 6440;
     performanceMinSat950Act18FrSub4617Sub401 = 8790;

     performanceMinSat950Act18FrSub12000Sub250 = 1240;
     performanceMinSat950Act18FrSub12000Sub300 = 3520;
     performanceMinSat950Act18FrSub12000Sub350 = 4680;
     performanceMinSat950Act18FrSub12000Sub380 = 5270;
     performanceMinSat950Act18FrSub12000Sub401 = 7610;

     performanceMinSat950Act18FrSub100000Sub250 = 870;
     performanceMinSat950Act18FrSub100000Sub300 = 2340;
     performanceMinSat950Act18FrSub100000Sub350 = 3520;
     performanceMinSat950Act18FrSub100000Sub380 = 4090;
     performanceMinSat950Act18FrSub100000Sub401 = 6440;

     performanceMinSat950Act18FrSubNothingSub250 = 590;
     performanceMinSat950Act18FrSubNothingSub300 = 1760;
     performanceMinSat950Act18FrSubNothingSub350 = 2340;
     performanceMinSat950Act18FrSubNothingSub380 = 2930;
     performanceMinSat950Act18FrSubNothingSub401 = 4680;
                //- Other Demestic ---;
     performanceSub4617Sub250 = 2930;
     performanceSub4617Sub300 = 4090;
     performanceSub4617Sub350 = 5270;
     performanceSub4617Sub380 = 6440;
     performanceSub4617Sub401 = 8790;

     performanceSub12000Sub250 = 2340;
     performanceSub12000Sub300 = 3560;
     performanceSub12000Sub350 = 4680;
     performanceSub12000Sub380 = 5270;
     performanceSub12000Sub401 = 7610;

     performanceSub100000Sub250 = 1760;
     performanceSub100000Sub300 = 2340;
     performanceSub100000Sub350 = 3520;
     performanceSub100000Sub380 = 4090;
     performanceSub100000Sub401 = 6440;

     performanceSubNothingSub250 = 0;
     performanceSubNothingSub300 = 1240;
     performanceSubNothingSub350 = 2340;
     performanceSubNothingSub380 = 2340;
     performanceSubNothingSub401 = 4680;
                //- Other International ---;
     performanceInternationalFrSub250 = 4680;
     performanceInternationalFrSub300 = 4680;
     performanceInternationalFrSub350 = 4680;
     performanceInternationalFrSub380 = 4680;
     performanceInternationalFrSub401 = 4680;

     performanceInternationalSub250 = 2340;
     performanceInternationalSub300 = 3520;
     performanceInternationalSub350 = 4680;
     performanceInternationalSub380 = 5270;
     performanceInternationalSub401 = 7610;
*/
    //- Family Discount ---;
 familyDiscount = 900; //not final, needs to be changed/assigned value in coldfusion fucntion
/*
    //- Non-Need Scholarship (National Merit: Commended, Semi-finalist, Finalist) ---;
    nationalMeritMC = new BigDecimal(.2);
    nationalMeritMS = new BigDecimal(.5);
    nationalMeritMF = new BigDecimal(1);

    //- Church Matching (LSU Portion) ---;

    //- Sub Direct Loan ---;
     subDirectLoanMaxFr  = 3500;
     subDirectLoanMaxFr2 = 3500;
     subDirectLoanMaxSo  = 4500;
     subDirectLoanMaxJr  = 5500;
     subDirectLoanMaxSr  = 5500;

    //- UnSub Direct Loan ---;
     unsubDirectLoanMaxDepFr  = 5500;
     unsubDirectLoanMaxDepFr2 = 5500;
     unsubDirectLoanMaxDepSo  = 6500;
     unsubDirectLoanMaxDepJr  = 7500;
     unsubDirectLoanMaxDepSr  = 7500;

     unsubDirectLoanMaxIndepFr  = 9500;
     unsubDirectLoanMaxIndepFr2 = 9500;
     unsubDirectLoanMaxIndepSo  = 10500;
     unsubDirectLoanMaxIndepJr  = 12500;
     unsubDirectLoanMaxIndepSr  = 12500;

    //- Perkins ---;
     perkinsSub5274  = 1250;
     perkinsSub12000 = 2000;

    //- Federal Work-study ---;
     fwsSub2000   = 2000;
     fwsSub12000  = 2500;
     fwsSub100000 = 3000;

    
    
    //- Year-in-Advance Discount ---;
    yearInAdvanceDiscount = new BigDecimal(0.07);

    //- Quarter-in-Advance Discount ---;
    quarterInAdvanceDiscount = new BigDecimal(0.02);
   */ 
    
    
    
    ///////////////////////////////////in coldfusion function enforceLSULimits(), global coldfusion var,  needs to be reaad and changed/assigned value in coldfusion fucntion    
    _nonCaGrantDesc="";
    _nonCaGrantAmt=0;
    _outsideScholarship="";
    _outsideScholarshipAmt=0;
    
    calGrantA=0;
     calGrantB=0;
     lsuAllowance=0;
  //    sdaAward=0;  //already defined
     lsuPerformance=0;
  //      familyDiscount=0; //already defined
     nationalMerit=0;
     churchMatch=0;
     pacificCampMatch=0;
     nonPacificCampMatch=0;
     litEvanMatch=0;
      
      
      
      //in coldfusion function enforceOverallLimits()
      pellGrant=0;
      fseogAmt=0;
      extAllowance=0;
      nonCaGrantAmt=0;
      outsideAmt=0;
      churchBase=0;
      subDirect=0;
      perkinsLoan=0;
      fwsAmount=0;
      unsubDirect=0;
      
      //in coldfusion function getCOA()
       needAmt=0;
      
      //================= used in summary.cfm
      tuitionAndFees=0;
       addlExp=0;
       efc  = efcInit;//99999;
            
      //some vars defined in estimatorStudentData.cfm
      excludeNote = "exclude loans";
      
      //some vars defined in estimatorSummary.cfm, and only used in that file.
      lsuLimitSubtotal =0;
      err =0;
      nonCaGrantDesc = "";
      outsideDesc = "";
      lsuOverallSubtotal=false;
      maxAid = 0;
      
        roomAndBoard       = 0; 
        pacificCampBase     = 0;
        nonPacificCampBase  = 0;
        litEvanBase         = 0;
        amtDue 		 = 0;				
      
      yearInAdvanceOption =0;
      quarterInAdvanceOption = 0;
      monthlyOption = 0;
      
      
      //for education allowance percentage
      ea_lsu_per = 100;
      ea_nonlsu_per = 35;
      ea_nonlsu_dorm_per = 70;
      
      ///for estimator to set adjust_cal_grant_amt indicator
      adjust_calgrant_amt_ind=false;
      
      
      //2012-02-06
       lsu4yRenewable=0;
       lsuAchievement=0;
      lsuNeedGrant=0;
      
      ////////added to hold the scholarship amount for #7 ~ #9, #1 and #2
      /*
      scholarship_amt_1=0;
      scholarship_amt_2=0;
      
      scholarship_amt_7=0;
      scholarship_amt_8=0;
      scholarship_amt_9=0;      
      */
      }
      
}
