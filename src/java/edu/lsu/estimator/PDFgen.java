package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.Login;
import edu.lsu.estimator.PackFunctions;
import edu.lsu.estimator.PackValues;
import edu.lsu.estimator.Print;
import edu.lsu.estimator.Student;
import edu.lsu.estimator.Constants;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;

@SessionScoped
public class PDFgen
        implements Serializable {

    private static final long serialVersionUID = 1L;
    /*  44 */    private final String PDFROOT_UNIX = "/var/estimator-files/";
    /*  45 */    private final String PDFROOT_MS = "c:\\";
    /*  46 */    private String TMPLATE = "estimator2012_t.pdf";
    /*  47 */    private String fisy_aidy = "2012-2013";
    /*  48 */    private int fisy = 2012;

    /*  51 */    private String pdfname = null;
    /*  52 */    private final String[] modes = new String[]{"-", ".", "_", ","};

    /*  55 */    private static final Logger log = LoggerFactory.getLogger();

    public PDFgen() {
        /*  58 */ syncFISY();
    }

    public void syncFISY() {
    }

    public void resyncFISY(AppReference ref) {
        /*  78 */ this.TMPLATE = "estimator" + ref.getFiscal_year() + "_t.pdf";
        /*  79 */ this.fisy = ref.getFiscal_year();
        /*  80 */ this.fisy_aidy = ref.getFaid_year();
    }

    public String getPdfRoot2(String separator) {
        String osName = System.getProperty("os.name");
        
        /*  84 */ if (separator.equals("/") && (!osName.contains("Windows"))) {
            /*  85 */ return "/var/estimator-files/" + this.fisy + separator;
        }
       

        if (Constants.pdfGenPathDev.equalsIgnoreCase("windows") || osName.contains("Windows")) {
            /*  87 */ return "c:\\" + this.fisy + separator;
        }
        return "";
    }

    public String getEstimaterLogoPath()
    {
        String osName = System.getProperty("os.name");
        String unix="/opt/glassfish3/glassfish/domains/domain1/applications/estimator/web/resources/img/estimator_banner.png";
      
      if(!osName.contains("Windows")) {
            /*  85 */ return unix;
        }
       if(osName.contains("Windows"))
       {
          return "D:\\Projects\\latest_builds\\sept13\\web\\resources\\img\\estimator_banner.png";
       }
    return "";
    
    
    }
    
    public String getPdfRoot() {
        /*  92 */ String seperator = System.getProperty("file.separator");
        /*  93 */ return getPdfRoot2(seperator);
    }

    String genPDF(Student std, Login login, AppReference ref, PackFunctions calc, Print pdf, int mode, Integer... p) {
        /* 141 */ resyncFISY(ref);

        /* 143 */ boolean pu = (p.length > 0);

        /* 145 */ String msg = "";
        /* 146 */ File tmpt = new File(getPdfRoot() + this.TMPLATE);

        /* 147 */ if (!tmpt.exists() || !tmpt.isFile() || !tmpt.canRead()) {
            /* 148 */ msg = "Failed: invalid PDF template";
            /* 149 */ log.info(" not found template=%s, path=%s, name=%s ", new Object[]{tmpt.getAbsolutePath(), tmpt.getPath(), tmpt.getName()});
        } /* 151 */ else if (std == null) {
            /* 152 */ msg = "Failed: no student data";
            /* 153 */        } else if (std.getRecid() == null || std.getRecid().isEmpty() || std.getStudentBLastname() == null || std.getStudentBLastname().isEmpty()) {
            /* 154 */ msg = "Failed: student data is not valid";
        } else {
            /* 156 */ int fisy = std.getStudentFisy();
            /* 157 */ String recid = std.getRecid();

            /* 159 */ String timeShowFmtStr = "yyyy-MM-dd h:mm:ss a z";
            /* 160 */ SimpleDateFormat sdf = new SimpleDateFormat(timeShowFmtStr);

            /* 164 */ String ln = std.getStudentBLastname().trim().replaceAll(" ", "_");
            /* 165 */ String fn = std.getStudentCFirstname().trim().replaceAll(" ", "_");

            /* 167 */ this.pdfname = fisy + this.modes[mode] + ln + "." + fn + this.modes[mode] + recid + ".pdf";

            /* 169 */ String str = "", stmp = "";
            int x = 0, amt = 0;
            try {
                /* 171 */ PdfReader reader = new PdfReader(getPdfRoot() + this.TMPLATE);
                /* 172 */ PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(getPdfRoot() + this.pdfname));
                /* 173 */ AcroFields form = stamp.getAcroFields();

                /* 175 */ setMetaData(reader, stamp, std, sdf, ref.getClientverions());

                /* 177 */ if (std.getDdoe() > 0L && (new DateTime(std.getDdoe())).getYear() == ref.getFiscal_year()) {

                    /* 179 */ str = ref.fmtFullTimeWithZone(std.getDdoe(), std.getTzdoe());
                    /* 180 */ if (pu) {
                        /* 181 */ form.setField("doe", "entered on " + str + (std.getPuser_id().equals(std.getStudentALsuid()) ? " by self" : "") + " via MyCampus Portal");
                    } else {
                        /* 183 */ boolean portal = (std.getPuser_id() != null);
                        /* 184 */ boolean org_portal = (std.getCounselorOrig().intValue() == 0 || (std.getModRoot() != null && std.getModRoot().startsWith("0.")));
                        /* 185 */ form.setField("doe", "entered on " + str + (portal ? ((std.getPuser_id().equals(std.getStudentALsuid()) ? " by self" : "") + " via MyCampus Portal") : ((org_portal ? " via " : " by ") + std.getStudentBuOrigCounselor())));
                    }
                }

                /* 189 */ if (std.getDdom() > 0L) {
                    /* 190 */ str = ref.fmtFullTimeWithZone(std.getDdom(), std.getTzdom());
                    /* 191 */ if (pu) {
                        /* 192 */ form.setField("dom", "updated on " + str + " by MyCampus Portal");
                    } else {
                        /* 194 */ form.setField("dom", "updated on " + str + " by " + std.getStudentBxModCounselor());
                    }
                }

                /* 199 */ form.setField("year", ref.getFaid_year());

                /* 201 */ str = ref.fmtFullNowTimeWithDefaultZone();
                /* 202 */ if (pu) {
                    /* 203 */ form.setField("pdf_time", str + (std.getPuser_id().equals(std.getStudentALsuid()) ? " by self" : "") + " via MyCampus Portal");
                } else {
                    /* 205 */ form.setField("pdf_time", str + " by " + login.getCurrentUser().getUsername());
                }
                /* 207 */ form.setField("verions", ref.getClientverions());
                /* 208 */ pdf.setVersions(ref.getClientverions());
                /* 209 */ form.setField("recid", std.getRecid());
                /* 210 */ pdf.setRecid(std.getRecid());

                /* 216 */ str = std.getStudentCFirstname() + " " + std.getStudentBLastname();
                /* 217 */ stmp = std.getStudentALsuid();

                /* 219 */ if (stmp == null || stmp.isEmpty()) {
                    /* 220 */ x = 0;
                } else {
                    /* 222 */ x = 1;
                }
                /* 224 */ form.setField("name_id", str + ((x > 0) ? (" (" + std.getStudentALsuid() + ")") : ""));
                /* 225 */ form.setField("street", std.getStudentGStreet());
                /* 226 */ form.setField("city_state_zip", std.getStudentHCity() + (ref.isEmp(std.getStudentHCity()) ? "" : ", ") + std.getStudentIState() + " " + std.getStudentJZip());
                /* 227 */ form.setField("email", std.getStudentEEmail());

                /* 229 */ form.setField("gpa", std.getStudentPGpa().toString());
                /* 230 */ form.setField("sat", std.getStudentQSat().toString());
                /* 231 */ form.setField("act", std.getStudentRAct().toString());
                /* 232 */ form.setField("highschool", std.getStudentOLastSchool());
                /* 233 */ form.setField("major", std.getStudentTMajor());
                /* 234 */ form.setField("phonenumber", std.getStudentFPhone());

                /* 236 */ str = std.getStudentAnPubNotes();
                /* 237 */ if (!ref.isEmp(str)) {
                    form.setField("pub_notes", str);
                } /* 238 */ else {
                    form.setField("pub_notes", "none");
                }

                /* 240 */ calc.init();
                /* 241 */ calc.initAndShowPellGrantAmt();

                /* 243 */ str = calc.showTuitionFees();
                /* 244 */ pdf.setTuitionFee(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 245 */ form.setField("tuition_fee", str);

                /* 248 */ str = calc.showAddlExpense();
                /* 249 */ pdf.setOtherExpenses(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 250 */ form.setField("expense", str);

                /* 252 */ str = calc.showEFC();
                /* 253 */ pdf.setEfc(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 254 */ form.setField("efc", str);

                /* 256 */ str = calc.showNeedAmt();
                /* 257 */ pdf.setMaxNeed(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 258 */ form.setField("need", str);

                /* 260 */ int line = 1, subtot = 0;

                /* 263 */ calc.refreshCalc(std);
                /* 264 */ str = calc.showPellGrantAmt();
                /* 265 */ amt = ref.convertCurrencyStrToInt(str);
                /* 266 */ pdf.setPell(Integer.valueOf(amt));
                /* 267 */ if (amt > 0) {
                    setLineContent(form, line++, "Federal PELL Grant", str, null);
                }
                /* 268 */ subtot += pdf.getPell().intValue();

                /* 270 */ str = calc.showCalGrantA();
                /* 271 */ amt = ref.convertCurrencyStrToInt(str);
                /* 272 */ pdf.setCalGrantA(Integer.valueOf(amt));
                /* 273 */ if (amt > 0) {
                    setLineContent(form, line++, "Cal Grant A", str, null);
                }
                /* 274 */ subtot += pdf.getCalGrantA().intValue();

                /* 276 */ str = calc.showCalGrantB();
                /* 277 */ amt = ref.convertCurrencyStrToInt(str);
                /* 278 */ pdf.setCalGrantB(Integer.valueOf(amt));
                /* 279 */ if (amt > 0) {
                    setLineContent(form, line++, "Cal Grant B", str, null);
                }
                /* 280 */ subtot += pdf.getCalGrantB().intValue();

                /* 282 */ str = calc.showFseogAmt();
                /* 283 */ amt = ref.convertCurrencyStrToInt(str);
                /* 284 */ pdf.setPseog(Integer.valueOf(amt));
                /* 285 */ if (amt > 0) {
                    setLineContent(form, line++, "FSEOG", str, null);
                }
                /* 286 */ subtot += pdf.getPseog().intValue();

                /* 288 */ str = calc.showExtAllowanceAmt();
                /* 289 */ amt = ref.convertCurrencyStrToInt(str);
                /* 290 */ pdf.setNonLsuEa(Integer.valueOf(amt));
                /* 291 */ if (amt > 0) {
                    setLineContent(form, line++, "External Educational Allowance", str, null);
                }
                /* 292 */ subtot += pdf.getNonLsuEa().intValue();

                /* 294 */ pdf.setNoncalGrant(Integer.valueOf(calc.getNonCalGrantAmt()));
                /* 295 */ subtot += pdf.getNoncalGrant().intValue();
                /* 296 */ if (calc.getNonCalGrantAmt() > 0) {
                    /* 297 */ str = calc.showNonCalGrantAmt();
                    /* 298 */ setLineContent(form, line++, calc.getNonCalGrantDesc(), str, null);
                }

                /* 301 */ pdf.setNonLsuSship(Integer.valueOf(calc.getOutsideAmt()));
                /* 302 */ subtot += pdf.getNonLsuSship().intValue();
                /* 303 */ if (calc.getOutsideAmt() > 0) {
                    /* 304 */ str = calc.showOutsideAmt();
                    /* 305 */ setLineContent(form, line++, calc.getOutsideDesc(), str, null);
                }

                /* 308 */ pdf.setSship3ExtAmt(Integer.valueOf(calc.getChurchBaseAmt()));
                /* 309 */ subtot += pdf.getSship3ExtAmt().intValue();
                /* 310 */ if (calc.getChurchBaseAmt() > 0) {
                    /* 311 */ str = calc.showChurchBaseAmt();
                    /* 312 */ setLineContent(form, line++, calc.showChurchBaseDesc(), str, null);
                }

                /* 315 */ if (subtot > 0) {
                    /* 316 */ setLineContent(form, line++, "External Grants Subtotal", null, ref.fmtMoney(Integer.valueOf(subtot)));
                    /* 317 */ setLineContent(form, line++, " ", null, null);
                }
                /* 319 */ subtot = 0;

                /* 322 */ str = calc.showLsuAllowanceAmt();
                /* 323 */ amt = ref.convertCurrencyStrToInt(str);
                /* 324 */ pdf.setLsuEa(Integer.valueOf(amt));
                /* 325 */ if (amt > 0) {
                    setLineContent(form, line++, "La Sierra Educational Allowance", str, null);
                }
                /* 326 */ subtot += pdf.getLsuEa().intValue();

                /* 328 */ str = calc.showSdaAwardAmt();
                /* 329 */ amt = ref.convertCurrencyStrToInt(str);
                /* 330 */ pdf.setSda(Integer.valueOf(amt));
                /* 331 */ if (amt > 0) {
                    setLineContent(form, line++, "SDA Award", str, null);
                }
                /* 332 */ subtot += pdf.getSda().intValue();

                /* 339 */ str = calc.showLsuNeedGrantAmt();
                /* 340 */ amt = ref.convertCurrencyStrToInt(str);
                /* 341 */ pdf.setNeedGrant(Integer.valueOf(amt));
                /* 342 */ if (amt > 0) {
                    setLineContent(form, line++, "La Sierra Need Grant", str, null);
                }
                /* 343 */ subtot += pdf.getNeedGrant().intValue();

                /* 345 */ str = calc.showFamilyDiscountAmt();
                /* 346 */ amt = ref.convertCurrencyStrToInt(str);
                /* 347 */ pdf.setFamilyDisct(Integer.valueOf(amt));
                /* 348 */ if (amt > 0) {
                    setLineContent(form, line++, "Family Discount", str, null);
                }
                /* 349 */ subtot += pdf.getFamilyDisct().intValue();

                /* 351 */ str = calc.showNationalMeritAmt();
                /* 352 */ amt = ref.convertCurrencyStrToInt(str);
                /* 353 */ pdf.setNatlMerit(Integer.valueOf(amt));
                /* 354 */ if (amt > 0) {
                    setLineContent(form, line++, "National Merit Scholarship", str, null);
                }
                /* 355 */ subtot += pdf.getNatlMerit().intValue();

                /* 358 */ str = calc.showLsuAchievementAmt();
                /* 359 */ amt = ref.convertCurrencyStrToInt(str);
                /* 360 */ pdf.setAchieveAward(Integer.valueOf(amt));
                /* 361 */ if (amt > 0) {
                    setLineContent(form, line++, "La Sierra Achievement Award", str, null);
                }
                /* 362 */ subtot += pdf.getAchieveAward().intValue();

                /* 364 */ str = calc.showLsu4yRenewableAmt();
                /* 365 */ amt = ref.convertCurrencyStrToInt(str);
                /* 366 */ pdf.setRenew4y(Integer.valueOf(amt));
                /* 367 */ if (amt > 0) {
                   // setLineContent(form, line++, "La Sierra 4-year Renewable Scholarship", str, null);
                     setLineContent(form, line++, "La Sierra Merit Scholarship", str, null);
                }
                /* 368 */ subtot += pdf.getRenew4y().intValue();

                /* 370 */ str = calc.showLasuGrantAmt();
                /* 371 */ amt = ref.convertCurrencyStrToInt(str);
                /* 372 */ pdf.setLsugrant(Integer.valueOf(amt));
                /* 373 */ if (amt > 0) {
                    setLineContent(form, line++, "La Sierra Univ. Grant", str, null);
                }
                /* 374 */ subtot += pdf.getLsugrant().intValue();

                /* 378 */ int min = 0;

                /* 380 */ min = calc.getScholarship1Amt();
                /* 381 */ if (min < 0) {
                    min = 0;
                }
                /* 382 */ pdf.setSship1LsuAmt(Integer.valueOf(min));
                /* 383 */ subtot += min;
                /* 384 */ if (min > 0) {
                    /* 385 */ setLineContent(form, line++, calc.showScholarship1Desc(), calc.showScholarship1Amt(), null);
                }

                /* 388 */ min = calc.getScholarship2Amt();
                /* 389 */ if (min < 0) {
                    min = 0;
                }
                /* 390 */ pdf.setSship2LsuAmt(Integer.valueOf(min));
                /* 391 */ subtot += min;
                /* 392 */ if (min > 0) {
                    /* 393 */ setLineContent(form, line++, calc.showScholarship2Desc(), calc.showScholarship2Amt(), null);
                }

                /* 396 */ min = calc.getChurchMatchAmt();
                /* 397 */ if (min < 0) {
                    min = 0;
                }
                /* 398 */ pdf.setSship3LsuAmt(Integer.valueOf(min));
                /* 399 */ subtot += min;
                /* 400 */ if (min > 0) {
                    /* 401 */ setLineContent(form, line++, calc.showChurchMatchDesc(), calc.showChurchMatchAmt(), null);
                }

                /* 404 */ min = calc.getLitEvanMatchAmt();
                /* 405 */ if (min < 0) {
                    min = 0;
                }
                /* 406 */ pdf.setSship4LsuAmt(Integer.valueOf(min));
                /* 407 */ subtot += min;
                /* 408 */ if (min > 0) {
                    /* 409 */ setLineContent(form, line++, calc.showLitEvanMatchDesc(), calc.showLitEvanMatchAmt(), null);
                }

                /* 412 */ min = calc.getPacificCampMatchAmt();
                /* 413 */ if (min < 0) {
                    min = 0;
                }
                /* 414 */ pdf.setSship5LsuAmt(Integer.valueOf(min));
                /* 415 */ subtot += min;
                /* 416 */ if (min > 0) {
                    /* 417 */ setLineContent(form, line++, calc.showPacificCampMatchDesc(), calc.showPacificCampMatchAmt(), null);
                }

                /* 420 */ min = calc.getNonPacificCampMatchAmt();
                /* 421 */ if (min < 0) {
                    min = 0;
                }
                /* 422 */ pdf.setSship6LsuAmt(Integer.valueOf(min));
                /* 423 */ subtot += min;
                /* 424 */ if (min > 0) {
                    /* 425 */ setLineContent(form, line++, calc.showNonPacificCampMatchDesc(), calc.showNonPacificCampMatchAmt(), null);
                }

                /* 428 */ min = calc.getScholarship7Amt();
                /* 429 */ if (min < 0) {
                    min = 0;
                }
                /* 430 */ pdf.setSship7LsuAmt(Integer.valueOf(min));
                /* 431 */ subtot += min;
                /* 432 */ if (min > 0) {
                    /* 433 */ setLineContent(form, line++, calc.showScholarship7Desc(), calc.showScholarship7Amt(), null);
                }

                /* 436 */ min = calc.getScholarship8Amt();
                /* 437 */ if (min < 0) {
                    min = 0;
                }
                /* 438 */ pdf.setSship8LsuAmt(Integer.valueOf(min));
                /* 439 */ subtot += min;
                /* 440 */ if (min > 0) {
                    /* 441 */ setLineContent(form, line++, calc.showScholarship8Desc(), calc.showScholarship8Amt(), null);
                }

                /* 444 */ min = calc.getScholarship9Amt();
                /* 445 */ if (min < 0) {
                    min = 0;
                }
                /* 446 */ pdf.setSship9LsuAmt(Integer.valueOf(min));
                /* 447 */ subtot += min;
                /* 448 */ if (min > 0) {
                    /* 449 */ setLineContent(form, line++, calc.showScholarship9Desc(), calc.showScholarship9Amt(), null);
                }

                /* 452 */ if (subtot > 0) {
                    /* 453 */ setLineContent(form, line++, "La Sierra Awards Subtotal", null, ref.fmtMoney(Integer.valueOf(subtot)));
                    /* 454 */ setLineContent(form, line++, " ", null, null);
                }
                /* 456 */ subtot = 0;

                /* 458 */ if (std.getStudentXFafsa().equalsIgnoreCase("Yes")) {
                    /* 459 */ str = std.getIndExcloans();
                    /* 460 */ if (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("yes")) {
                        /* 461 */ setLineContent(form, line++, "Option 'exclude loans' is checked", "n/a", null);
                        /* 462 */ setLineContent(form, line++, "Loans Subtotal", null, "n/a");
                    } else {
                        /* 464 */ str = std.getStudentApSubLoans();
                        /* 465 */ if (str != null && !str.isEmpty() && !str.trim().equalsIgnoreCase("no")) {

                            /* 468 */ str = calc.showSubdirectAmt();
                            /* 469 */ amt = ref.convertCurrencyStrToInt(str);
                            /* 470 */ pdf.setSubloan(Integer.valueOf(amt));
                            /* 471 */ if (amt > 0) {
                                setLineContent(form, line++, "Subsidized Direct Loan (borrow " + calc.showAmt(calc.getOrg_loan_amt_sub()) + ")", str, null);
                            }
                            /* 472 */ subtot += pdf.getSubloan().intValue();
                        }

                        /* 475 */ str = calc.showPerkinLoanAmt();
                        /* 476 */ amt = ref.convertCurrencyStrToInt(str);
                        /* 477 */ pdf.setPerkins(Integer.valueOf(amt));
                        /* 478 */ if (amt > 0) {
                            setLineContent(form, line++, "Loan to Scholarship (borrow " + calc.showAmt(calc.getOrg_loan_amt_perkins()) + ")", str, null);
                        }
                        /* 479 */ subtot += pdf.getPerkins().intValue();

                        /* 481 */ str = std.getStudentAqUnsubLoans();
                        /* 482 */ if (str != null && !str.isEmpty() && !str.trim().equalsIgnoreCase("no")) {

                            /* 485 */ str = calc.showUnsubdirectAmt();
                            /* 486 */ amt = ref.convertCurrencyStrToInt(str);
                            /* 487 */ pdf.setUnsubloan(Integer.valueOf(amt));
                            /* 488 */ if (amt > 0) {
                                setLineContent(form, line++, "Unsubsidized Direct Loan (borrow " + calc.showAmt(calc.getOrg_loan_amt_unsub()) + ")", str, null);
                            }
                            /* 489 */ subtot += pdf.getUnsubloan().intValue();
                        }

                        /* 492 */ if (subtot > 0) {
                            setLineContent(form, line++, "Loans Subtotal", null, ref.fmtMoney(Integer.valueOf(subtot)));
                        }
                    }
                    /* 494 */ setLineContent(form, line++, " ", null, null);
                    /* 495 */ subtot = 0;
                }

                /* 499 */ str = std.getStudentArFws();
                /* 500 */ if (str != null && !str.isEmpty() && !str.trim().equalsIgnoreCase("no")) {
                    /* 502 */ if (calc.getFwsAmt() > 0) {

                        /* 505 */ pdf.setFws(Integer.valueOf(calc.getFwsAmt()));

                        /* 507 */ setLineContent(form, line++, "Federal Work-Study", null, calc.showFwsAmt());
                        /* 508 */ setLineContent(form, line++, " ", null, null);
                    }
                }
                /* 511 */ str = calc.showMaxAidAmt();
                /* 512 */ pdf.setTotAid(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 513 */ str = calc.showFaidAmt();
                /* 514 */ pdf.setTotAidWoWork(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 515 */ setLineContent(form, line++, "Total Financial Aid", null, calc.showMaxAidAmt());

                /* 651 */ str = calc.showTuitionFees();

                /* 653 */ form.setField("lsu_tuition_fees", str);

                /* 655 */ str = calc.initAndShowRoomBoardAmt();
                /* 656 */ pdf.setRoomBoard(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 657 */ form.setField("roomboard", "+" + str);

                /* 659 */ str = ref.fmtMoney(Integer.valueOf(pdf.getTuitionFee().intValue() + pdf.getRoomBoard().intValue()));
                /* 660 */ form.setField("charges", str);

                /* 662 */ str = calc.showFaidAmt();
                /* 663 */ pdf.setTotAidWoWork(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 664 */ form.setField("totaid", "-" + str);

                /* 666 */ subtot = calc.getPacificCampBase() + calc.getNonPacificCampBaseAmt() + calc.getLitEvanBaseAmt();
                /* 667 */ pdf.setEarnings(Integer.valueOf(subtot));
                /* 668 */ pdf.setSship4ExtAmt(Integer.valueOf(calc.getLitEvanBaseAmt()));
                /* 669 */ pdf.setSship5ExtAmt(Integer.valueOf(calc.getPacificCampBase()));
                /* 670 */ pdf.setSship6ExtAmt(Integer.valueOf(calc.getNonPacificCampBaseAmt()));
                /* 671 */ str = ref.fmtMoney(Integer.valueOf(-1 * subtot));
                /* 672 */ form.setField("earnings", str);

                /* 674 */ str = calc.showDueAmt();
                /* 675 */ pdf.setDue(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 676 */ form.setField("due", str);

                /* 678 */ str = calc.initAndShowYIA();
                /* 679 */ pdf.setYearOpt(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 680 */ form.setField("yearly", str);

                /* 682 */ str = calc.showQIA();
                /* 683 */ if (!std.getStudentUAcademic().equalsIgnoreCase("CJ")) {
                    /* 684 */ pdf.setQuarterOpt(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                    /* 685 */ form.setField("quarterly", str);
                } else {
                    /* 687 */ form.setField("quarterly", "n/a");
                }

                /* 690 */ str = calc.showMonthlyOption();
                /* 691 */ pdf.setMonthOpt(Integer.valueOf(ref.convertCurrencyStrToInt(str)));
                /* 692 */ form.setField("monthly", str);

                /* 696 */ if (ref.getStrYIA().isEmpty()) {
                    /* 697 */ synchronized (this) {
                        /* 698 */ if (ref.getStrYIA().isEmpty()) {
                            /* 699 */ StringBuilder sbs = new StringBuilder(100);
                            /* 700 */ ref.setStrYIA(sbs.append("If you pay by ").append("9/15/").append(ref.getFiscal_year()).append(" and receive the ").append(PackValues.yearInAdvanceDiscount.movePointRight(2).intValueExact()).append("% discount").toString());
                            /* 701 */ sbs.setLength(0);
                            /* 702 */ sbs.append("If you pay by ").append(9).append("/").append(15).append("/").append(ref.getFiscal_year());
                            /* 703 */ int m = 12;
                            /* 704 */ sbs.append(", ").append((m > 12) ? (m - 12) : m).append("/").append(15).append("/").append((m > 12) ? (ref.getFiscal_year() + 1) : ref.getFiscal_year());
                            /* 705 */ m += 3;
                            /* 706 */ sbs.append(", and ").append((m > 12) ? (m - 12) : m).append("/").append(15).append("/").append((m > 12) ? (ref.getFiscal_year() + 1) : ref.getFiscal_year());
                            /* 707 */ sbs.append(", and receive the ").append(PackValues.quarterInAdvanceDiscount.movePointRight(2).intValueExact()).append("% discount");
                            /* 708 */ ref.setStrQIA(sbs.toString());
                            /* 709 */ sbs.setLength(0);
                            /* 710 */ sbs.append("If you pay on the first day of each month starting ").append("9/15/").append(ref.getFiscal_year()).append(" and include the $").append(90).append(" fee");
                            /* 711 */ ref.setStrMPN(sbs.toString());
                        }
                    }
                }

                /* 716 */ form.setField("sfs_yia", ref.getStrYIA());
                /* 717 */ form.setField("sfs_qia", ref.getStrQIA());
                /* 718 */ form.setField("sfs_mpln", ref.getStrMPN());

                /* 720 */ if (!pu) {
                    /* 721 */ stamp.setFormFlattening(true);
                }
                /* 723 */ stamp.close();
                /* 724 */ reader.close();

                /* 726 */ msg = this.pdfname;
                /* 727 */            } catch (Exception e) {
                /* 728 */ e.printStackTrace();
                /* 729 */ msg = "Failed. Reason: " + e.getMessage();
            }
        }
        /* 732 */ return msg;
    }

    private void setMetaData(PdfReader reader, PdfStamper stamper, Student std, SimpleDateFormat sdf, String ver) {
        /* 765 */ HashMap<String, String> info = reader.getInfo();
        /* 766 */ info.put("Subject", "Estimate");
        /* 767 */ info.put("Author", "La Sierra University Estimator Program Version " + ver);
        /* 768 */ info.put("Keywords", std.getStudentBLastname() + ", " + std.getStudentCFirstname());
        /* 769 */ info.put("Title", "La Sierra University Estimate for " + std.getStudentCFirstname() + " " + std.getStudentBLastname());
        /* 770 */ info.put("Creator", std.getStudentBuOrigCounselor() + " at " + sdf.format(new Date()));
        /* 771 */ stamper.setMoreInfo(info);
    }

    private void setLineContent(AcroFields form, int linenumb, String name, String val, String subtotal) {
        /* 776 */ String fieldname = "aidname" + linenumb;
        /* 777 */ String fieldval = "aidamt" + linenumb;
        /* 778 */ String fieldtot = "aidtot" + linenumb;
        /* 779 */ int[] t = {0, 1, 2, 3};

        try {
            /* 785 */ if (subtotal != null && !subtotal.isEmpty()) {
                /* 786 */ form.setFieldProperty(fieldtot, "fflags", 0, null);
                /* 787 */ form.setFieldProperty(fieldtot, "bordercolor", Color.BLUE, null);
                /* 788 */ form.setField(fieldtot, subtotal);

                /* 790 */ form.setFieldProperty(fieldname, "fflags", 0, null);
                /* 791 */ form.setFieldProperty(fieldname, "bordercolor", Color.BLUE, null);
                /* 792 */ form.setFieldProperty(fieldval, "fflags", 0, null);
                /* 793 */ form.setFieldProperty(fieldval, "bordercolor", Color.BLUE, null);
            }
            /* 795 */ if (name != null) {
                form.setField(fieldname, name);
            }
            /* 796 */ if (val != null && !val.isEmpty()) {
                form.setField(fieldval, val);
            }

        } /* 800 */ catch (Exception e) {
            /* 801 */ e.printStackTrace();
        }
    }

    String downloadPDF(String filename) {
        /* 813 */ String path = getPdfRoot() + filename;

        /* 815 */ String pdfmsg = "";
        /* 816 */ File file = new File(path);
        /* 817 */ long filesize = file.length();
        /* 818 */ byte[] data = new byte[(int) filesize];
        /* 819 */ ByteBuffer buffer = ByteBuffer.allocate(32000);
        /* 820 */ int p = 0, l = 0;

        /* 822 */ FacesContext fc = FacesContext.getCurrentInstance();
        /* 823 */ FileInputStream fis = null;
        /* 824 */ FileChannel fic = null;
        try {
            /* 826 */ fis = new FileInputStream(path);
            /* 827 */ fic = fis.getChannel();
            /* 828 */ while ((l = fic.read(buffer)) > 0) {
                /* 829 */ buffer.flip();

                /* 831 */ buffer.get(data, p, l);
                /* 832 */ p += l;
                /* 833 */ buffer.clear();
            }
            /* 835 */        } catch (Exception e) {
            /* 836 */ pdfmsg = "Failed to read PDF. Reason: " + e.getMessage();
            /* 837 */ fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, pdfmsg, pdfmsg));
            /* 838 */ return pdfmsg;
        } finally {
            try {
                /* 841 */ if (fic != null) {
                    fic.close();
                }
                /* 842 */ if (fis != null) {
                    fis.close();
                }
                /* 843 */            } catch (Exception exception) {
            }
        }

        /* 847 */ HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        /* 848 */ HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();

        /* 850 */ response.setContentType("application/pdf");
        /* 851 */ response.setContentLength((int) filesize);

        /* 857 */ response.setHeader("Content-disposition", "attachment;filename=" + filename);
        /* 858 */ response.setHeader("Refresh", "3; url = estimate-new.jsf");

        try {
            /* 861 */ ServletOutputStream output = response.getOutputStream();
            /* 862 */ output.write(data);
            /* 863 */ output.flush();
        } /* 865 */ catch (IOException e) {
            /* 866 */ pdfmsg = "Failed to feedback PDF content. Reason: " + e.getMessage();

            /* 868 */ fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, pdfmsg, pdfmsg));
            /* 869 */ return pdfmsg;
        } finally {
        }

        /* 872 */ fc.responseComplete();
        /* 873 */ return pdfmsg;
    }
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\PDFgen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
