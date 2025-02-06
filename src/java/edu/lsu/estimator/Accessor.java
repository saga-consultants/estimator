/*     */ package edu.lsu.estimator;

/*     */
 /*     */ import edu.lsu.estimator.AppReference;
/*     */ import edu.lsu.estimator.Counselor;
/*     */ import edu.lsu.estimator.InfoState;
/*     */ import edu.lsu.estimator.Logs;
/*     */ import edu.lsu.estimator.Print;
/*     */ import edu.lsu.estimator.Student;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import javax.ejb.ApplicationException;
/*     */ import javax.ejb.Stateless;
/*     */ import javax.ejb.TransactionAttribute;
/*     */ import javax.ejb.TransactionAttributeType;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.inject.Inject;
/*     */ import javax.inject.Named;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.persistence.PersistenceContext;
/*     */ import javax.transaction.UserTransaction;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ConstraintViolationException;

/*     */
 /*     */
 /*     */
 /*     */
 /*     */ @Stateless
/*     */ @Named("accessor")
/*     */ @ApplicationException(rollback = true)
/*     */ public class Accessor /*     */ {

    /*     */ @PersistenceContext
    /*     */ private EntityManager em;
    /*     */ @Resource
    /*     */ private UserTransaction utx;
    /*     */ @Inject
    /*     */ AppReference ref;

    /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String saveStudInfo(Student stud, Student studMod, List<Student> matches, String tz,
            Integer... p) {
        /* 42 */ String msg = "";
        /*     */
 /* 44 */ boolean pu = (p.length > 0);
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ try {
            /* 52 */ FacesContext context = FacesContext.getCurrentInstance();
            /*     */
 /*     */ try {
                /* 55 */ this.em.persist(stud);
                /*     */
 /* 57 */ for (Student one : matches) {
                    /* 58 */ one.setPickupInd(0);
                    /* 59 */ one.setLostTime(System.currentTimeMillis());
                    /* 60 */ one.setLostTz(tz);
                    /* 61 */ one.setLostToLocal(null);
                    /* 62 */ this.em.merge(one);
                    /*     */ }
                /*     */
 /*     */
 /*     */
 /*     */
 /* 68 */ if (!pu) {
                    /* 69 */ Counselor me = (Counselor) context.getApplication().evaluateExpressionGet(context,
                            "#{login.currentUser}", Object.class);
                    /* 70 */ Logs log = new Logs(new Date(), "ESTIMATE", "NEWSTUD", me.getUsername(), "SAVED");
                    /* 71 */ log.setResult("ok");
                    /* 72 */ this.em.persist(log);
                    /*     */ }
                /*     */
 /*     */ } /* 76 */ catch (ConstraintViolationException ve) {
                /*     */
 /* 78 */ msg = msg + "Invalid Data: [";
                /* 79 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                    /* 80 */ System.out.println("###saveStudInfo### Path: " + cv.getPropertyPath().toString()
                            + " ########### FAILED: " + cv.getMessage());
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
 /* 109 */ msg = msg + cv.getRootBeanClass().getSimpleName() + " " + cv.getPropertyPath().toString()
                            + "(val=" + cv.getInvalidValue() + "): " + cv.getMessage() + ";  ";
                    /*     */ }
                /*     */
 /*     */
 /*     */
 /* 114 */ msg = msg + "]";
                /* 115 */ return msg;
                /*     */ } /* 117 */ catch (Exception e) {
                /* 118 */ e.printStackTrace();
                /* 119 */ return msg = "Exception: " + e.getMessage();
                /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ }
            /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ } /* 132 */ catch (ConstraintViolationException ve) {
            /*     */
 /* 134 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 135 */ System.out.println(
                        "###saveStudInfo Path: " + cv.getPropertyPath().toString() + " ### FAILED: " + cv.getMessage());
                /*     */ }
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
 /* 148 */ msg = "Violations2: " + ve.toString();
            /*     */ } /* 150 */ catch (Exception e) {
            /* 151 */ System.out.println("########## ");
            /* 152 */ msg = "Exceptions2: " + e.getMessage();
            /* 153 */ e.printStackTrace();
            /*     */ }
        /* 155 */ return msg;
        /*     */ }

    /*     */
 /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String markOldDupStud(Student stud, String tz, Integer... p) {
        /* 161 */ String msg = "";
        /* 162 */ boolean pu = (p.length > 0);
        /*     */
 /* 164 */ this.em = this.ref.wtfClosedEntityManagerFactory(this.em, null);
        /*     */ try {
            /* 166 */ this.em.createNativeQuery(
                    "update student set pickup_ind=0, lost_time=?, lost_tz=?, lost_to_local=? where  recid<>? and student_user_name=? and  pickup_ind=1 ")
                    /* 167 */.setParameter(1, Long.valueOf(System.currentTimeMillis()))/* 168 */.setParameter(2, tz)
                    /* 169 */.setParameter(3, stud.getRecid())/* 170 */.setParameter(4, stud.getRecid())
                    /* 171 */.setParameter(5, stud.getStudentUserName())
                    /*     */
                    /* 173 */.executeUpdate();
            /* 174 */ if (!pu) {
                /* 175 */ FacesContext context = FacesContext.getCurrentInstance();
                /* 176 */ InfoState info = (InfoState) context.getApplication().evaluateExpressionGet(context,
                        "#{info}", Object.class);
                /* 177 */ info.sumUp();
                /*     */ }
            /* 179 */ } catch (Exception e) {
            /* 180 */ e.printStackTrace();
            /* 181 */ msg = e.toString();
            /*     */ }
        /* 183 */ return msg;
        /*     */ }

    /*     */
 /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String updateStudInfo(Student stud) {
        /* 189 */ String msg = "";

        /* 190 */ FacesContext context = FacesContext.getCurrentInstance();
        /*     */ try {

            /* 192 */ this.em.persist(stud);

            /* 193 */ this.em.flush();
            /* 194 */ Counselor me = (Counselor) context.getApplication().evaluateExpressionGet(context,
                    "#{login.currentUser}", Object.class);
            /* 195 */ Logs log = new Logs(new Date(), "ESTIMATE", "UPDATESTUD", me.getUsername(), "SAVED");
            /* 196 */ log.setResult("ok");
            /* 197 */ this.em.persist(log);

            /*     */ } /* 199 */ catch (ConstraintViolationException ve) {
            /*     */
 /* 201 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 202 */ System.out.println("###updateStudInfo### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }

            /* 213 */ msg = ve.toString();
            /*     */ }
        /* 215 */ return msg;
        /*     */ }

    /*     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String updateStudentPickUpInd(String oldRecId) {
        /* 248 */ String msg = "";

        /*     */ try {
            this.em.createNativeQuery(
                    "update student set pickup_ind=0 where  recid=?  ")
                    .setParameter(1, oldRecId)/* 170 */
                    .executeUpdate();
            this.em.flush();
            /*     */ } /* 266 */ catch (ConstraintViolationException ve) {
            /* 267 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 268 */ System.out.println("###updateStudPrtTimes### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /* 270 */ msg = ve.toString();
            /* 271 */ } catch (Exception e) {
            /* 272 */ e.printStackTrace();
            /* 273 */ msg = e.getMessage();
            /*     */ }
        /* 275 */ return msg;
        /*     */ }

    /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public void saveLog(Logs log) {
        /* 220 */ this.em = this.ref.wtfClosedEntityManagerFactory(this.em, null);
        /*     */ try {
            /* 222 */ this.em.persist(log);
            /* 223 */ } catch (Exception e) {
            /* 224 */ e.printStackTrace();
            /*     */ }
        /*     */ }

    /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String savePrt(Print pdf) {
        /* 230 */ String msg = "";
        /*     */ try {
            /* 232 */ this.em.persist(pdf);
            /*     */ } /* 234 */ catch (ConstraintViolationException ve) {
            /* 235 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 236 */ System.out.println("###savePrt### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /* 238 */ msg = ve.toString();
            /* 239 */ } catch (Exception e) {
            /* 240 */ e.printStackTrace();
            /* 241 */ msg = e.getMessage();
            /*     */ }
        /* 243 */ return msg;
        /*     */ }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String updatePrint(Print pdf, Print pdf_) {
        /* 248 */ String msg = "";
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ try {
            /* 256 */ this.em.createNativeQuery("update  prints set prt_id=? where prt_id=?")
                    /* 257 */.setParameter(1, pdf_.getPrtId())
                    .setParameter(2, pdf.getPrtId())
                    /* 258 */.executeUpdate();
            /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ } /* 266 */ catch (ConstraintViolationException ve) {
            /* 267 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 268 */ System.out.println("###updateStudPrtTimes### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /* 270 */ msg = ve.toString();
            /* 271 */ } catch (Exception e) {
            /* 272 */ e.printStackTrace();
            /* 273 */ msg = e.getMessage();
            /*     */ }
        /* 275 */ return msg;
        /*     */ }

    /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String updateStudPrtTimes(Student stud) {
        /* 248 */ String msg = "";
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ try {
            /* 256 */ this.em.createNativeQuery("upsert  student set prt_times=prt_times+1 where recid=?")
                    /* 257 */.setParameter(1, stud.getRecid())/* 258 */.executeUpdate();
            /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ } /* 266 */ catch (ConstraintViolationException ve) {
            /* 267 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 268 */ System.out.println("###updateStudPrtTimes### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /* 270 */ msg = ve.toString();
            /* 271 */ } catch (Exception e) {
            /* 272 */ e.printStackTrace();
            /* 273 */ msg = e.getMessage();
            /*     */ }
        /* 275 */ return msg;
        /*     */ }

    /*     */
 /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String updateCounselorPwd(Counselor one) {
        /* 281 */ String msg = "";
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ try {
            /* 291 */ Counselor who = (Counselor) this.em.find(Counselor.class, one.getUserid());
            /* 292 */ who.setShadow(one.getShadow());
            /* 293 */ this.em.merge(who);
            /*     */ } /* 295 */ catch (ConstraintViolationException ve) {
            /* 296 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 297 */ System.out.println("###updateCounselorPwd### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /* 299 */ msg = ve.toString();
            /* 300 */ } catch (Exception e) {
            /* 301 */ e.printStackTrace();
            /* 302 */ msg = e.getMessage();
            /*     */ }
        /* 304 */ return msg;
        /*     */ }

    /*     */
 /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public String saveMasterStudInfo(Student stud, List<Student> matches, String tz) {
        /* 310 */ String msg = "";
        /* 311 */ FacesContext context = FacesContext.getCurrentInstance();
        /*     */
 /*     */ try {
            /* 314 */ this.em.persist(stud);
            /*     */
 /* 316 */ for (Student one : matches) {
                /* 317 */ one.setPickupInd(0);
                /* 318 */ one.setLostTime(one.getDup());
                /* 319 */ one.setLostTz(one.getTzup());
                /* 320 */ one.setLostToMaster(one.getRecid());
                /* 321 */ this.em.merge(one);
                /*     */ }
            /*     */
 /*     */
 /*     */
 /*     */
 /* 327 */ Counselor me = (Counselor) context.getApplication().evaluateExpressionGet(context,
                    "#{login.currentUser}", Object.class);
            /* 328 */ Logs log = new Logs(new Date(), "ESTIMATE", "MASTERSTUD", me.getUsername(), "SAVED");
            /* 329 */ log.setResult("ok");
            /* 330 */ this.em.persist(log);
            /* 331 */ this.em.flush();
            /* 332 */ } catch (ConstraintViolationException ve) {
            /*     */
 /* 334 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 335 */ System.out.println("###saveStudInfo### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 346 */ return msg = "Violation: " + ve.toString();
            /* 347 */ } catch (Exception e) {
            /* 348 */ e.printStackTrace();
            /* 349 */ return msg = "Exception: " + e.getMessage();
            /*     */ }
        /* 351 */ return msg;
        /*     */ }

    /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public void triggerTmpidPrt() {


        //" update prints set prt_id= RTRIM(char(counselor_id))||'.'||RTRIM(char(client_id))||'.'||RTRIM(char(PRT_NUM)) where prt_id='tmpid'" 
        /* 356 */ this.em.createNativeQuery(
                " update prints set prt_id= ((counselor_id))||'.'||((client_id))||'.'||((PRT_NUM)) where prt_id='tmpid'")
                .executeUpdate();
        /*     */ }

    /*     */
 /*     */
 /*     */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*     */ public void saveSuppliedLasuId(Student std) {
        /*     */ try {
            /* 363 */ this.em.merge(std);
            /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 369 */ List<Student> dups = this.em.createNamedQuery("Student.findByActiveLsuid", Student.class)
                    .setParameter("lsuid", std.getStudentALsuid())
                    .setParameter("fisy", Integer.valueOf(this.ref.getFiscal_year()))
                    .setParameter("recid", std.getRecid()).getResultList();
            /* 370 */ for (Student one : dups) {
                /* 371 */ one.setPickupInd(0);
                /* 372 */ one.setLostToLocal(std.getRecid());
                /* 373 */ one.setLostTime(System.currentTimeMillis());
                /* 374 */ one.setLostTz(this.ref.getTzSN());
                /* 375 */ this.em.merge(one);
                /*     */ }
            /*     */
 /* 378 */ FacesContext context = FacesContext.getCurrentInstance();
            /* 379 */ Counselor me = (Counselor) context.getApplication().evaluateExpressionGet(context,
                    "#{login.currentUser}", Object.class);
            /* 380 */ Logs modlog = new Logs(new Date(), "ADDID", "MODSTUD", me.getUsername(), "TRIED");
            /* 381 */ modlog.setLocation(" recid=" + std.getRecid() + " lasuid=" + std.getStudentALsuid());
            /* 382 */ modlog.setResult("OK");
            /* 383 */ this.em.persist(modlog);
            /*     */
 /* 385 */ this.em.flush();
            /* 386 */ } catch (ConstraintViolationException ve) {
            /* 387 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                /* 388 */ System.out.println("###updateCounselorPwd### Path: " + cv.getPropertyPath().toString()
                        + " ########### FAILED: " + cv.getMessage());
                /*     */ }
            /*     */ } /* 391 */ catch (Exception e) {
            /* 392 */ e.printStackTrace();
            /*     */ }
        /*     */ }
    /*     */ }

/*
 * Location:
 * D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\
 * estimator\Accessor.class Java compiler version: 7 (51.0) JD-Core Version:
 * 1.1.3
 */
