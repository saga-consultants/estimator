/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PackValues
/*     */ {
/*     */   public static final int qTuition = 11970;//11076;
/*     */   public static final int qRoomAndBoard = 4236;//2930;
/*     */   public static final int qHealth = 0;
/*     */   public static final int qSaFee = 0;
/*     */   public static final int qRecFee = 0;
/*     */   public static final int qTechFee = 0;
/*     */   public static final int qSecurityFee = 0;
/*     */   public static final int qComprehensiveFee = 330;
/*     */   public static final int qInternationalInsFee = 725;//TBD
/*     */   public static final int qFallOrientation = 220;//TBD
/*     */   public static final int qWinSprOrientation = 50;//TBD
/*     */   public static final int qTotalTuitionAndFeesDomestic = 11970;//11406
/*     */   public static final int qTotalTuitionAndFeesInternational = 12131;//TBD
/*     */   public static final int qTotalTuitionAndFeesDomesticFrFall = 11626;//TBD
/*     */   public static final int qTotalTuitionAndFeesDomesticFrWinSpr = 11456;//TBD
/*     */   public static final int qTotalTuitionAndFeesInternationalFrFall = 12351;//TBD
/*     */   public static final int qTotalTuitionAndFeesInternationalFrWinSpr = 12181;//TBD
/*     */   public static final int yTuition = 35910;//33228
/*     */   public static final int yRoomAndBoard = 9240;//8790;
/*     */   public static final int yHealth = 0;
/*     */   public static final int ySaFee = 0;
/*     */   public static final int yRecFee = 0;
/*     */   public static final int yTechFee = 0;
/*     */   public static final int ySecurityFee = 0;
/*     */   public static final int yInternationalInsFee = 2900;//TBD
/*     */   public static final int yFrOrientation = 220;
/*     */   public static final int yTotalTuitionAndFeesDomestic =38862; //37710-2024;//35910;//34218;
/*     */   public static final int yTotalTuitionAndFeesInternational = 42862;//41310-2024;//37118;//TBD
/*     */   public static final int yTotalTuitionAndFeesDomesticFr = 39082;//2024-37930;//36130 ;//34438
/*     */   public static final int yTotalTuitionAndFeesInternationalFr = 43082;//2024-41530;//37338;//TBD
/*     */   public static final int yExtraCostCommunity = 16565;
/*     */   public static final int yExtraCostDorm = 17258;
/*     */   public static final int qCoaCommunity =  20821;//17158; 
/*     */   public static final int qCoaDorm =17845;//16927
/*     */   public static final int qCoaCommunityFr =  20821;//17158;
/*     */   public static final int qCoaDormFr =  17845;//16927;
/*  86 */   public static final int yCoaCommunity = 59946;  // 2024- Math.max(58252, 58254);//Math.max(61852, 61854);//prev : Math.max(62463, 62465);// Math.max(50781, 50783);
/*  87 */   public static final int yCoaDorm = 57853;// Math.max(53535, 53537); //;//Math.max(53535, 53537);//old Math.max(51474, 51476)
/*     */   
/*  89 */   public static final int yCoaCommunityFr = yCoaCommunity + 220;
/*  90 */   public static final int yCoaDormFr = yCoaDorm + 220; // old values yCoaDorm + 220 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final int yCoaCommunityIntl = yCoaCommunity + 4000;//2024- 3600;//TBD 2900
/*  95 */   public static final int yCoaDormIntl = yCoaDorm + 4000;//2024-3600;//TBD 2900
/*     */ 
/*     */   
/*  98 */   public static final int yCoaCommunityFrIntl = yCoaCommunityFr + 3600;//TBD 2900
/*  99 */   public static final int yCoaDormFrIntl = yCoaDormFr + 3600;//TBD 2900
/*     */   
/*     */   public static final int pellBase = 7395;//6345;
/*     */   
/*     */   public static final int pellCOAbase = 6656;//6195;
/*     */   
/*     */   public static final int pellCOAtop = 7395;//999999;
/*     */   
/*     */   public static final int aCalGrant = 9358 ;//9084;

            public static final int pellSAILimit=6656;
            
            public static final int pellMax=7395;
           
            public static final int pellMin=740;//767
/*     */   
/*     */   public static final int aCalGrantMinGPA = 3;
/*     */   
/*     */   public static final int aCalGrantAssetCeilingInd = 46200;
/*     */   
/*     */   public static final int aCalGrantAssetCeilingIndSingle = 46000;
/*     */   
/*     */   public static final int aCalGrantAssetCeilingIndMarried = 46000;
/*     */   
/*     */   public static final int aCalGrantAssetCeilingOther = 97200;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndSingleFamily1 = 46000;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndSingleFamily2 = 112900;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndSingleFamily3 = 115600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndSingleFamily4 = 125600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndSingleFamily5 = 134600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndSingleFamily6 = 145200;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndMarriedFamily1 = 0;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndMarriedFamily2 = 46000;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndMarriedFamily3 = 115600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndMarriedFamily4 = 125600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndMarriedFamily5 = 134600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingIndMarriedFamily6 = 145200;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingOtherFamily1 = 0;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingOtherFamily2 = 112900;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingOtherFamily3 = 115600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingOtherFamily4 = 125600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingOtherFamily5 = 134600;
/*     */   
/*     */   public static final int aCalGrantIncomeCeilingOtherFamily6 = 145200;
/*     */   
/*     */   public static final int bCalGrantFr = 1648;
/*     */   
/*     */   public static final int bCalGrantFr2 = 1648;
/*     */   
/*     */   //public static final int bCalGrantSoJrSr = 10868;

            public static final int bCalGrantSoJrSr = 11006;
/*     */   
/*     */   public static final int bCalGrantMaxGPA = 3;
/*     */   
/*     */   public static final int bCalGrantMinGPA = 2;
/*     */   
/*     */   public static final int bCalGrantAssetCeilingIndSingle = 46200;
/*     */   
/*     */   public static final int bCalGrantAssetCeilingIndMarried = 46200;
/*     */   
/*     */   public static final int bCalGrantAssetCeilingOther = 97200;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndSingleFamily1 = 46000;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndSingleFamily2 = 52700;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndSingleFamily3 = 59400;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndSingleFamily4 = 66000;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndSingleFamily5 = 73900;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndSingleFamily6 = 79700;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndMarriedFamily1 = 52700;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndMarriedFamily2 = 52700;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndMarriedFamily3 = 59400;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndMarriedFamily4 = 66000;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndMarriedFamily5 = 73900;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingIndMarriedFamily6 = 79700;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingOtherFamily1 = 0;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingOtherFamily2 = 52700;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingOtherFamily3 = 59400;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingOtherFamily4 = 66000;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingOtherFamily5 = 73900;
/*     */   
/*     */   public static final int bCalGrantIncomeCeilingOtherFamily6 = 79700;// stop till pell grant and then proceed
/*     */   
/*     */   public static final int standardFseogEfcSub1000 = 1350;
/*     */   
/*     */   public static final int standardFseogEfcSub2000 = 600;
/*     */   
/*     */   public static final int fseogWithCalGrantEdAllowOrNatMeritEfcSub1000 = 350;
/*     */   
/*     */   public static final int sdaAwardInit = 2250;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub4617Sub250 = 1760;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub4617Sub300 = 1760;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub4617Sub350 = 5270;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub4617Sub380 = 6440;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub4617Sub401 = 8790;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub12000Sub250 = 1240;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub12000Sub300 = 1240;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub12000Sub350 = 3520;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub12000Sub380 = 4680;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub12000Sub401 = 5270;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub100000Sub250 = 870;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub100000Sub300 = 870;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub100000Sub350 = 2340;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub100000Sub380 = 3520;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSub100000Sub401 = 4090;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSubNothingSub250 = 590;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSubNothingSub300 = 590;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSubNothingSub350 = 1760;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSubNothingSub380 = 2340;
/*     */   
/*     */   public static final int performanceSubSat950Act18FrSubNothingSub401 = 2930;
/*     */   public static final int performanceMinSat950Act18FrSub4617Sub250 = 1760;
/*     */   public static final int performanceMinSat950Act18FrSub4617Sub300 = 4090;
/*     */   public static final int performanceMinSat950Act18FrSub4617Sub350 = 5270;
/*     */   public static final int performanceMinSat950Act18FrSub4617Sub380 = 6440;
/*     */   public static final int performanceMinSat950Act18FrSub4617Sub401 = 8790;
/*     */   public static final int performanceMinSat950Act18FrSub12000Sub250 = 1240;
/*     */   public static final int performanceMinSat950Act18FrSub12000Sub300 = 3520;
/*     */   public static final int performanceMinSat950Act18FrSub12000Sub350 = 4680;
/*     */   public static final int performanceMinSat950Act18FrSub12000Sub380 = 5270;
/*     */   public static final int performanceMinSat950Act18FrSub12000Sub401 = 7610;
/*     */   public static final int performanceMinSat950Act18FrSub100000Sub250 = 870;
/*     */   public static final int performanceMinSat950Act18FrSub100000Sub300 = 2340;
/*     */   public static final int performanceMinSat950Act18FrSub100000Sub350 = 3520;
/*     */   public static final int performanceMinSat950Act18FrSub100000Sub380 = 4090;
/*     */   public static final int performanceMinSat950Act18FrSub100000Sub401 = 6440;
/*     */   public static final int performanceMinSat950Act18FrSubNothingSub250 = 590;
/*     */   public static final int performanceMinSat950Act18FrSubNothingSub300 = 1760;
/*     */   public static final int performanceMinSat950Act18FrSubNothingSub350 = 2340;
/*     */   public static final int performanceMinSat950Act18FrSubNothingSub380 = 2930;
/*     */   public static final int performanceMinSat950Act18FrSubNothingSub401 = 4680;
/*     */   public static final int performanceSub4617Sub250 = 2930;
/*     */   public static final int performanceSub4617Sub300 = 4090;
/*     */   public static final int performanceSub4617Sub350 = 5270;
/*     */   public static final int performanceSub4617Sub380 = 6440;
/*     */   public static final int performanceSub4617Sub401 = 8790;
/*     */   public static final int performanceSub12000Sub250 = 2340;
/*     */   public static final int performanceSub12000Sub300 = 3560;
/*     */   public static final int performanceSub12000Sub350 = 4680;
/*     */   public static final int performanceSub12000Sub380 = 5270;
/*     */   public static final int performanceSub12000Sub401 = 7610;
/*     */   public static final int performanceSub100000Sub250 = 1760;
/*     */   public static final int performanceSub100000Sub300 = 2340;
/*     */   public static final int performanceSub100000Sub350 = 3520;
/*     */   public static final int performanceSub100000Sub380 = 4090;
/*     */   public static final int performanceSub100000Sub401 = 6440;
/*     */   public static final int performanceSubNothingSub250 = 0;
/*     */   public static final int performanceSubNothingSub300 = 1240;
/*     */   public static final int performanceSubNothingSub350 = 2340;
/*     */   public static final int performanceSubNothingSub380 = 2340;
/*     */   public static final int performanceSubNothingSub401 = 4680;
/*     */   public static final int performanceInternationalFrSub250 = 4680;
/*     */   public static final int performanceInternationalFrSub300 = 4680;
/*     */   public static final int performanceInternationalFrSub350 = 4680;
/*     */   public static final int performanceInternationalFrSub380 = 4680;
/*     */   public static final int performanceInternationalFrSub401 = 4680;
/*     */   public static final int performanceInternationalSub250 = 2340;
/*     */   public static final int performanceInternationalSub300 = 3520;
/*     */   public static final int performanceInternationalSub350 = 4680;
/*     */   public static final int performanceInternationalSub380 = 5270;
/*     */   public static final int performanceInternationalSub401 = 7610;
/* 304 */   public static final BigDecimal nationalMeritMC = new BigDecimal("0.33");
/* 305 */   public static final BigDecimal nationalMeritMS = new BigDecimal("0.5");
/* 306 */   public static final BigDecimal nationalMeritMF = new BigDecimal(1);
/*     */   
           //Fr means freshMan Gpa is Gpa 2De74 means 2.74 //this.showEFC() sai -1500 to 6550
            public static final int Lsu4yRenewableFrGpa2_2De74_sai_1=8000;//3400;//3300//3100;
           public static final int Lsu4yRenewableFrGpa2De75_3De24_sai_1=10000;//6600;//6400// 6100;
           public static final int Lsu4yRenewableFrGpa3De25_3De49_sai_1=11000;//7700 ;//7500//7100;
           public static final int Lsu4yRenewableFrGpa3De50_3De74_sai_1=13000;//8900;//8600//8200;
            public static final int Lsu4yRenewableFrGpa3De75_4_sai_1=15000;//11000;//10700//10200;
            
                //FrTF means freshMan Gpa is Gpa 2De74 means 2.74
            public static final int Lsu4yRenewableFrTfGpa2_2De74_sai_1=8000;//3400;//3300//3100;
           public static final int Lsu4yRenewableFrTfGpa2De75_3De24_sai_1=8000;//4400;//4300//4100;
           public static final int Lsu4yRenewableFrTfGpa3De25_3De49_sai_1=10000;//5600;//5400//5100;
           public static final int Lsu4yRenewableFrTfGpa3De50_3De74_sai_1=11000;//6600;//6400//6100;
            public static final int Lsu4yRenewableFrTfGpa3De75_4_sai_1=13000;//7700;//7500//7100;
            
            
                 //FrTF means returning Gpa is Gpa 2De74 means 2.74
            public static final int Lsu4yRenewableRGpa2_2De74_sai_1=6000;
           public static final int Lsu4yRenewableRGpa2De75_3De24_sai_1=9000;
           public static final int Lsu4yRenewableRGpa3De25_3De49_sai_1=9000;
           public static final int Lsu4yRenewableRGpa3De50_3De74_sai_1=10000;
            public static final int Lsu4yRenewableRGpa3De75_4_sai_1=12000;
            
            //sai 6551+
           
                 public static final int Lsu4yRenewableFrGpa2_2De74_sai_2=6000;//3400;//3300//3100;
           public static final int Lsu4yRenewableFrGpa2De75_3De24_sai_2=8000;//6600;//6400// 6100;
           public static final int Lsu4yRenewableFrGpa3De25_3De49_sai_2=10000;//7700 ;//7500//7100;
           public static final int Lsu4yRenewableFrGpa3De50_3De74_sai_2=12000;//8900;//8600//8200;
            public static final int Lsu4yRenewableFrGpa3De75_4_sai_2=14000;//11000;//10700//10200;
            
                //FrTF means freshMan Gpa is Gpa 2De74 means 2.74
            public static final int Lsu4yRenewableFrTfGpa2_2De74_sai_2=6000;//3400;//3300//3100;
           public static final int Lsu4yRenewableFrTfGpa2De75_3De24_sai_2=6000;//4400;//4300//4100;
           public static final int Lsu4yRenewableFrTfGpa3De25_3De49_sai_2=8000;//5600;//5400//5100;
           public static final int Lsu4yRenewableFrTfGpa3De50_3De74_sai_2=10000;//6600;//6400//6100;
            public static final int Lsu4yRenewableFrTfGpa3De75_4_sai_2=12000;//7700;//7500//7100;
            
            
                 //FrTF means returning Gpa is Gpa 2De74 means 2.74
            public static final int Lsu4yRenewableRGpa2_2De74_sai_2=4000;
           public static final int Lsu4yRenewableRGpa2De75_3De24_sai_2=7000;
           public static final int Lsu4yRenewableRGpa3De25_3De49_sai_2=9000;
           public static final int Lsu4yRenewableRGpa3De50_3De74_sai_2=10000;
            public static final int Lsu4yRenewableRGpa3De75_4_sai_2=12000;
            
            //sai 0
                  public static final int Lsu4yRenewableFrGpa2_2De74_sai_3=5000;//3400;//3300//3100;
           public static final int Lsu4yRenewableFrGpa2De75_3De24_sai_3=7000;//6600;//6400// 6100;
           public static final int Lsu4yRenewableFrGpa3De25_3De49_sai_3=8000;//7700 ;//7500//7100;
           public static final int Lsu4yRenewableFrGpa3De50_3De74_sai_3=10000;//8900;//8600//8200;
            public static final int Lsu4yRenewableFrGpa3De75_4_sai_3=12000;//11000;//10700//10200;
            
                //FrTF means freshMan Gpa is Gpa 2De74 means 2.74
            public static final int Lsu4yRenewableFrTfGpa2_2De74_sai_3=5000;//3400;//3300//3100;
           public static final int Lsu4yRenewableFrTfGpa2De75_3De24_sai_3=5000;//4400;//4300//4100;
           public static final int Lsu4yRenewableFrTfGpa3De25_3De49_sai_3=7000;//5600;//5400//5100;
           public static final int Lsu4yRenewableFrTfGpa3De50_3De74_sai_3=8000;//6600;//6400//6100;
            public static final int Lsu4yRenewableFrTfGpa3De75_4_sai_3=10000;//7700;//7500//7100;
            
            
                 //FrTF means returning Gpa is Gpa 2De74 means 2.74 
            public static final int Lsu4yRenewableRGpa2_2De74_sai_3=2000;
           public static final int Lsu4yRenewableRGpa2De75_3De24_sai_3=5000;
           public static final int Lsu4yRenewableRGpa3De25_3De49_sai_3=6000;
           public static final int Lsu4yRenewableRGpa3De50_3De74_sai_3=8000;
            public static final int Lsu4yRenewableRGpa3De75_4_sai_3=10000;
            
/*     */   public static final int subDirectLoanMaxFr = 3500;
/*     */   
/*     */   public static final int subDirectLoanMaxFr2 = 3500;
/*     */   
/*     */   public static final int subDirectLoanMaxSo = 4500;
/*     */   
/*     */   public static final int subDirectLoanMaxJr = 5500;
/*     */   
/*     */   public static final int subDirectLoanMaxSr = 5500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxDepFr = 5500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxDepFr2 = 5500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxDepSo = 6500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxDepJr = 7500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxDepSr = 7500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxIndepFr = 9500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxIndepFr2 = 9500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxIndepSo = 10500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxIndepJr = 12500;
/*     */   
/*     */   public static final int unsubDirectLoanMaxIndepSr = 12500;
/*     */   
/*     */   public static final int perkinsSubEfc1 = 5157;
/*     */   
/*     */   public static final int perkinsSubVal1 = 2500;
/*     */   
/*     */   public static final int perkinsSubEfc2 = 15000;
/*     */   
/*     */   public static final int perkinsSubVal2 = 4000;
/*     */   
/*     */   public static final int fwsSub4996 = 2000;
/*     */   
/*     */   public static final int fwsSub12001 = 2500;
/*     */   public static final int fwsSub100000 = 3000;

           public static final int roomAndBoard = 9900;
           public static final int lsuNeedGrant=1000;
           
           public static final int lsuAchievementInit=11000;//10700;//10200;
           
           public static final int _subDirectFr_F2=3500;
           
           public static final int _subDirectSO=4500;
           
           public static final int _subDirectSr=5500;
           
           public static final int _unsubDirectFr_F2_independent=9500;
           
           public static final int _unsubDirectSO_independent=10500;
           
           public static final int _unsubDirectSr_independent=12500;
           
           public static final int _unsubDirectFr_F2_dependent=5500;
           
           public static final int _unsubDirectSO_dependent=6500;
           
           public static final int _unsubDirectSr_dependent=7500;
           
           
/* 350 */  // public static final BigDecimal yearInAdvanceDiscount = new BigDecimal("0.07");
/*     */  public static final BigDecimal yearInAdvanceDiscount = new BigDecimal("0.05");
/*     */   
/* 353 */   public static final BigDecimal quarterInAdvanceDiscount = new BigDecimal("0.02");
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int efcInit = 99999;
/*     */ 
/*     */   
/*     */   public static final int sdaInit = 2250;
/*     */ 
/*     */   
/*     */   public static final int familyDisctInit = 900;

            public static final int universityHouseGrant=5190;
/*     */ 
/*     */   
/*     */   public static final int monthlyOptionFees = 90;
/*     */ 
/*     */   
/*     */   public static final int monthlyOptionMonthes = 9;
/*     */ 
/*     */   
/*     */   public static final String monthlyOptionDateStr = "9/15/";
/*     */ 
/*     */   
/*     */   public static final String yearInAdvanceDateStr = "9/15/";
/*     */ 
/*     */   
/*     */   public static final int quarterInAdvanceStartMonth = 9;
/*     */ 
/*     */   
/*     */   public static final int quarterInAdvanceStartDay = 15;
/*     */ 
/*     */   
/*     */   public static final int quarterInAdvanceIntervalMonthes = 3;
/*     */ 
/*     */   
/* 387 */   public static final HashMap<String, Integer> validAidsCJ = new HashMap<String, Integer>();
/*     */   
/*     */   static void resetValues() {}
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\PackValues.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */