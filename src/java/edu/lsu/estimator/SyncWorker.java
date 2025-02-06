/*      */ package edu.lsu.estimator;

/*      */ import com.caucho.hessian.client.HessianProxyFactory;
/*      */ import com.kingombo.slf5j.Logger;
/*      */ import com.kingombo.slf5j.LoggerFactory;
/*      */ import edu.lsu.estimator.Accessor;
/*      */ import edu.lsu.estimator.AppReference;
/*      */ import edu.lsu.estimator.Config;
/*      */ import edu.lsu.estimator.Counselor;
/*      */ import edu.lsu.estimator.Fund;
/*      */ import edu.lsu.estimator.Login;
/*      */ import edu.lsu.estimator.Logs;
/*      */ import edu.lsu.estimator.Print;
/*      */ import edu.lsu.estimator.Student;
/*      */ import edu.lsu.estimator.Version;
/*      */ import edu.lsu.estimator.tdo.SPIEstimatorMaster;
/*      */ import edu.lsu.estimator.tdo.TDOCounselor;
/*      */ import edu.lsu.estimator.tdo.TDOEstimatorReq;
/*      */ import edu.lsu.estimator.tdo.TDOFund;
/*      */ import edu.lsu.estimator.tdo.TDOMasterRes;
/*      */ import edu.lsu.estimator.tdo.TDOPrint;
/*      */ import edu.lsu.estimator.tdo.TDOStudent;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.KeyGenerator;
/*      */ import javax.crypto.SecretKey;
/*      */ import javax.ejb.ApplicationException;
/*      */ import javax.ejb.Singleton;
/*      */ import javax.ejb.Stateful;
/*      */ import javax.ejb.TransactionAttribute;
/*      */ import javax.ejb.TransactionAttributeType;
/*      */ import javax.ejb.TransactionManagement;
/*      */ import javax.ejb.TransactionManagementType;
/*      */ import javax.enterprise.context.ApplicationScoped;
/*      */ import javax.faces.context.FacesContext;
/*      */ import javax.inject.Inject;
/*      */ import javax.inject.Named;
/*      */ import javax.persistence.EntityManager;
/*      */ import javax.persistence.PersistenceContext;
/*      */ import javax.validation.ConstraintViolation;
/*      */ import javax.validation.ConstraintViolationException;

/*      */
 /*      */ @Stateful
/*      */ @Named("sync")
/*      */ @Singleton
/*      */ @ApplicationScoped
/*      */ @ApplicationException(rollback = true)
/*      */ @TransactionManagement(TransactionManagementType.CONTAINER)
/*      */ public class SyncWorker implements Serializable {

    /*   56 */ private static Logger log = LoggerFactory.getLogger();
    private static final long serialVersionUID = 1L;
    /*   57 */    private static SecretKey aesKey = null;
    @Inject
    /*      */ AppReference ref;
    @Inject
    /*      */ Accessor accessor;
    @Inject 
        InfoState info_;
    /*      */    @Inject
    /*      */ Login login;
    /*      */    @PersistenceContext(unitName = "estimator")
    /*      */ private EntityManager em;
    /*   64 */    private String init_step_msg = "";
    /*   65 */    private int init_on_ind = 0;
    /*      */
 /*   67 */    private int masterUp = 0;
    /*   68 */    private SPIEstimatorMaster hook = null;
    /*      */
 /*   70 */    TDOEstimatorReq req = null;
    /*   71 */    TDOMasterRes<TDOCounselor> res = null;
    /*   72 */    TDOMasterRes<TDOFund> res_fund = null;
    /*      */
 /*   74 */    private String showSyncMsg = "";
    /*      */
 /*      */
 /*   77 */    private String showBaseMsg = "";
    /*   78 */    private String showReadyMsg = "";
    /*      */
 /*   80 */    private Integer init_lock = Integer.valueOf(0);
    /*   81 */    private Integer sync_lock = Integer.valueOf(0);

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void initSyncWorker(Config seed) {
        /*   93 */ FacesContext context = FacesContext.getCurrentInstance();
        /*   94 */ ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        /*   95 */ String message = bundle.getString("MasterIsConnected");
        /*      */
 /*   97 */ this.showBaseMsg = message;
        /*   98 */ log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& initSyncWorker() got seed getPingInterval [%d]", new Object[]{Short.valueOf((seed == null) ? -1 : seed.getPingInterval())});
        /*      */
 /*      */
 /*      */
 /*  102 */ this.showReadyMsg = this.showBaseMsg;
        /*      */
 /*  104 */ this.showSyncMsg = "will auto sync every " + seed.getPingInterval() + " minutes";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String sync() {
        /*  111 */ if (this.sync_lock.intValue() > 0) {
            /*  112 */ log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& sync() returns since counselor %d is doing the task", new Object[]{this.sync_lock});
            /*  113 */ return "";
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*  118 */ synchronized (this.sync_lock) {
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  125 */ if (this.sync_lock.intValue() == 0) {
                /*      */
 /*  127 */ Integer counselorid = new Integer(999);
                /*  128 */ this.sync_lock = counselorid;
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  135 */ Logs synclog = new Logs(new Date(), "SYNC", "UPLOAD", "SYS", "TRIED");
                /*      */
 /*      */ try {
                    /*  138 */ dosync(counselorid.intValue());
                    /*      */
 /*      */
 /*  141 */ synclog.setResult("ok");
                    /*  142 */                } catch (Exception e) {
                    /*  143 */ e.printStackTrace();
                    /*  144 */ this.showSyncMsg = "Failed to sync by Exception";
                    /*  145 */ synclog.setResult("fail");
                    /*  146 */ log.info("Failed to sync: excpetion while syncing:", e);
                    /*      */                } finally {
                    /*  148 */ this.sync_lock = Integer.valueOf(0);
                    /*  149 */ this.accessor.saveLog(synclog);
                    /*      */                }
                /*      */            } else {
                /*  152 */ log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& gained the lock, but sync() returns since SYS is doing the task");
                /*      */            }
            /*      */        }
        /*  155 */ return "";
        /*      */    }

    /*      */
 /*      */
 /*      */ private int foundMatch(String[] all, String str) {
        /*  160 */ for (int i = 0; i < all.length; i++) {
            /*  161 */ if (all[i].equalsIgnoreCase(str)) {
                /*  162 */ return i;
                /*      */            }
            /*      */        }
        /*  165 */ return 0;
        /*      */    }

    /*      */
 /*      */ private List<Student> queryActiveStudsByIdOrUsername(Student stud) {
        /*  169 */ boolean nobaseid = this.ref.isEmp(stud.getStudentALsuid());
        /*      */
 /*      */
 /*      */
 /*      */
 /*  174 */ List<Student> results = this.em.createNamedQuery("Student.findActiveByLsuidOrUsername").setParameter("lsuid", nobaseid ? "1234567" : stud.getStudentALsuid()).setParameter("username", stud.getStudentUserName()).setParameter("studentFisy", Short.valueOf(this.ref.getSeed().getClientFscy())).getResultList();
        /*  175 */ List<Student> matches = new ArrayList<>();
        /*  176 */ for (Student one : results) {
            /*  177 */ if (!this.ref.isEmp(one.getStudentALsuid()) && !nobaseid) {
                /*  178 */ if (one.getStudentALsuid().equals(stud.getStudentALsuid())) /*  179 */ {
                    matches.add(one);
                }
                continue;
                /*      */            }
            /*  181 */ if (one.getStudentUserName().equalsIgnoreCase(stud.getStudentUserName())) {
                /*  182 */ matches.add(one);
                /*      */            }
            /*      */        }
        /*  185 */ return matches;
        /*      */    }

    /*      */
 /*      */ private synchronized void dosync(int counselorid) {
        /*  189 */ log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& dosync() is invoked , lock=%d (0 to quit)", new Object[]{this.sync_lock});
        /*  190 */ if (this.sync_lock.intValue() == 0) /*      */ {
            return;
        }
        /*  192 */ if (this.hook == null) {
            hookMaster();
        }
        /*  193 */ if (this.hook == null) {
            /*  194 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() has to return since can not get Hessian hook");
            /*  195 */ this.showSyncMsg = "failed to get Hessian Hook";
            /*      */
 /*      */ return;
            /*      */        }
        /*  199 */ this.showSyncMsg = "";
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ try {
            /*  207 */ //long last_std_dod = ((BigDecimal) this.em.createNativeQuery("select coalesce(max(dup), 0) from student   where student_fisy=?").setParameter(1, Short.valueOf(this.ref.getSeed().getClientFscy())).getSingleResult()).longValue();
            /*      */
 /*      */long last_std_dod = ((Integer) this.em.createNativeQuery("select coalesce(max(dup), 0) from student   where student_fisy=?").setParameter(1, Short.valueOf(this.ref.getSeed().getClientFscy())).getSingleResult()).longValue();
            /*      */
 /*      */
 /*  211 */ long last_prt_dod = ((BigDecimal) this.em.createNativeQuery("select coalesce(max(dup), 0) from prints  where fisy=?").setParameter(1, Short.valueOf(this.ref.getSeed().getClientFscy())).getSingleResult()).longValue();
            /*  212 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() got  last_std_dod=%d, last_prt_dod=%d", new Object[]{Long.valueOf(last_std_dod), Long.valueOf(last_prt_dod)});
            /*      */
 /*      */
 /*  215 */ String[] master_names = {"", "impl", "costs", "awards", "funds", "counselors", "std", "prt"};
            /*      */
 /*  217 */ prepReq(counselorid);
            /*  218 */ if (this.req == null) {
                /*  219 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() has to return since can not gen req obj");
                /*  220 */ this.showSyncMsg = "failed to get Request Object";
                /*      */ return;
                /*      */            }
            /*  223 */ this.req.last_prt_dod = last_prt_dod;
            /*  224 */ this.req.last_stud_dod = last_std_dod;
            /*      */
 /*  226 */ log.info(">>>>>>>>>>>>>>>>>>>>>>>>> dosync() comm with master check() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            /*  227 */ TDOMasterRes<Object> res_chk = this.hook.check(this.req, new Boolean[]{Boolean.valueOf(true)});
            /*  228 */ log.info("<<<<<<<<<<<<<<<<<<<<<<<  dosync() get reply code %d for check(), msg=%s <<<<<<<<<<<<<", new Object[]{Integer.valueOf((res_chk == null) ? -99999 : res_chk.code), res_chk.msgs});
            /*      */
 /*  230 */ int m_impl = 0, m_costs = 0, m_awards = 0, m_funds = 0, m_counselors = 0, m_std = 0, m_prt = 0;
            /*  231 */ if (res_chk.msgs != null) {
                /*  232 */ int i = 0;
                /*  233 */ for (String msg : res_chk.msgs) {
                    /*  234 */ log.info("  dosync() get reply master msg: %s", new Object[]{msg});
                    /*  235 */ i = foundMatch(master_names, msg);
                    /*  236 */ this.showSyncMsg = msg;
                    /*  237 */ switch (i) {
                        case 1:
                            /*  238 */ m_impl++;
                        /*  239 */ case 2:
                            m_costs++;
                        /*  240 */ case 3:
                            m_awards++;
                        /*  241 */ case 4:
                            m_funds++;
                        /*  242 */ case 5:
                            m_counselors++;
                        /*  243 */ case 6:
                            m_std++;
                        /*  244 */ case 7:
                            m_prt++;
                    }
                    /*      */
 /*      */
 /*      */
 /*      */
 /*      */                }
                /*      */            }
            /*  251 */ if (res_chk.code > 2000) {
                /*      */
 /*  253 */ log.info("########################## updated FISY [%s] ##############################", new Object[]{Integer.valueOf(res_chk.code)});
                /*  254 */ this.ref.getSeed().setClientFscy((short) res_chk.code);
                /*  255 */ this.em.merge(this.ref.getSeed());
                /*      */
 /*  257 */ log.info("########################## seed fisy=%d", new Object[]{Short.valueOf(this.ref.getSeed().getClientFscy())});
                /*  258 */ this.ref.refreshSeedVersion();
                /*  259 */ this.showSyncMsg = "updated FISY. scheduled next run.";
                /*      */
 /*  261 */ this.ref.reloadSeed();
                /*  262 */ log.info("########################## after reloadSeed(), seed fisy=%d", new Object[]{Short.valueOf(this.ref.getSeed().getClientFscy())});
                /*  263 */ log.info("########################## after reloadSeed(), ref fisy=%d", new Object[]{Integer.valueOf(this.ref.getFiscal_year())});
                /*  264 */ log.info("########################## after reloadSeed(), ref fasy=%s", new Object[]{this.ref.getFaid_year()});
                /*      */
 /*  266 */ this.ref.getSeed().setClientFscy((short) res_chk.code);
                /*      */
 /*      */
 /*      */ return;
                /*      */            }
            /*      */
 /*  272 */ if (res_chk.code != 0) {
                /*  273 */ if (this.showSyncMsg == null || this.showSyncMsg.isEmpty()) {
                    this.showSyncMsg = "failed to check master changes";
                }
                /*      */
 /*      */
 /*      */
 /*      */ return;
                /*      */            }
            /*      */
 /*  280 */ List<TDOStudent> cup_students = new ArrayList<>();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  289 */ List<Student> students = this.em.createNamedQuery("Student.findActiveStudentByFisyNupNdownDiff", Student.class).setParameter("studentFisy", Integer.valueOf(this.ref.getFiscal_year())).setParameter("clientId", Integer.valueOf(this.ref.getClientid())).getResultList();
            /*      */
 /*      */
 /*  292 */ if (m_std > 0 || students.size() > 0) {
                /*  293 */ for (Student one : students) {
                    /*      */
 /*      */
 /*  296 */ Integer prts = (Integer) this.em.createNativeQuery("select count(*) from prints where recid=?").setParameter(1, one.getRecid()).getSingleResult();
                    /*  297 */ if (prts.intValue() > 0) {
                        /*  298 */ TDOStudent tdo = genTDOStudFromClientStud(one);
                        /*      */
 /*  300 */ cup_students.add(tdo);
                        continue;
                        /*      */                    }
                    /*  302 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() filters non-printed record: recid=%s", new Object[]{one.getRecid()});
                    /*      */                }
                /*      */            }
            /*      */
 /*  306 */ log.info(">>>>>> estimator got %d new local student to upload or sync to master", new Object[]{Integer.valueOf(cup_students.size())});
            /*      */
 /*      */
 /*  309 */ prepReq(counselorid);
            /*  310 */ this.req.last_stud_dod = last_std_dod;
            /*  311 */ TDOMasterRes<TDOStudent> res_std = this.hook.syncNewPickedStuds(this.req, cup_students);
            /*  312 */ if (res_std.code != 0) {
                /*  313 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() get reply code %d for syncNewStuds", new Object[]{Integer.valueOf(res_std.code)});
                /*  314 */ if (res_std.msgs != null) /*  315 */ {
                    for (String msg : res_std.msgs) {
                        log.info("  master msg: %s", new Object[]{msg});
                    }
                }
                /*      */
 /*  317 */ this.showSyncMsg = "failed to sync new students";
                /*      */ return;
                /*      */            }
            /*  320 */ Map<String, String> losts = new HashMap<>();
            /*  321 */ if (res_std.objs != null && res_std.objs.size() > 0) {
                /*  322 */ Student std = null;
                /*      */
 /*  324 */ int i = res_std.objs.size();
                /*  325 */ for (int x = 0; x < i; x++) {
                    /*  326 */ TDOStudent tdo = res_std.objs.get(x);
                    /*  327 */ if (tdo.estmNumb < 0) {
                        tdo.estmNumb *= -1;
                    }
                    /*      */
 /*  329 */ Student one = genClientStudFromTdo(tdo);
                    /*  330 */ if (tdo.clientId.intValue() == this.req.clientid) {
                        /*      */
 /*      */
 /*      */
 /*      */
 /*  335 */ one.setDup(res_std.masterclock);
                        /*  336 */ one.setTzup(res_std.mastertz);
                        /*  337 */ this.em.merge(one);
                        /*  338 */ losts.put(one.getRecid(), "lost");
                        /*  339 */ log.info("master returns diff set: #%d of %d: update the looser record from master [recid=%s]", new Object[]{Integer.valueOf(x + 1), Integer.valueOf(i), tdo.recid});
                        /*      */                    } else {
                        /*  341 */ List<Student> matches = queryActiveStudsByIdOrUsername(one);
                        /*      */
 /*  343 */ log.info("master returns diff set: #%d of %d: insert the new record from master [recid=%s], and updates %d matched ones.", new Object[]{Integer.valueOf(x + 1), Integer.valueOf(i), tdo.recid, Integer.valueOf((matches == null) ? 0 : matches.size())});
                        /*  344 */ if (!this.accessor.saveMasterStudInfo(one, matches, this.showSyncMsg).isEmpty()) {
                            /*  345 */ this.showSyncMsg = "failed to save student data from master";
                            /*  346 */ log.info("master returns diff set: #%d of %d: new record from master [recid=%s] FAILED.", new Object[]{Integer.valueOf(x + 1), Integer.valueOf(i), tdo.recid});
                            /*      */
 /*      */
 /*      */
 /*      */ return;
                            /*      */                        }
                        /*      */                    }
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*  358 */ for (Student one : students) {
                /*  359 */ if (!losts.containsKey(one.getRecid())) {
                    /*  360 */ one.setDup(res_std.masterclock);
                    /*  361 */ one.setTzup(res_std.mastertz);
                    /*  362 */ if (one.getEstmNumb() < 0) {
                        one.setEstmNumb(one.getEstmNumb() * -1);
                    }
                    /*  363 */ this.em.merge(one);
                    /*  364 */ log.info("estimator updating dup of uploaded and winner record [recid=%s]", new Object[]{one.getRecid()});
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  372 */ List<TDOStudent> c_students = new ArrayList<>();
            /*      */
 /*      */
 /*      */
 /*      */
 /*  377 */ students = this.em.createNamedQuery("Student.findInactiveStudentByFisyNupNdownDiff", Student.class).setParameter("studentFisy", Integer.valueOf(this.ref.getFiscal_year())).setParameter("clientId", Integer.valueOf(this.ref.getClientid())).getResultList();
            /*      */
 /*      */
 /*  380 */ if (students.size() > 0) {
                /*  381 */ for (Student one : students) {
                    /*  382 */ TDOStudent tdo = genTDOStudFromClientStud(one);
                    /*  383 */ c_students.add(tdo);
                    /*      */                }
                /*  385 */ prepReq(counselorid);
                /*  386 */ TDOMasterRes<Object> res_up = this.hook.uploadNewPrtsUnpickedStuds(this.req, c_students);
                /*  387 */ log.info(">>>>>> dosync() get reply code %d for uploadPrtStuds", new Object[]{Integer.valueOf(res_up.code)});
                /*      */
 /*  389 */ if (res_up.code != 0) {
                    /*  390 */ if (res_up.msgs != null) /*  391 */ {
                        for (String msg : res_up.msgs) {
                            log.info("  master msg: %s", new Object[]{msg});
                        }
                    }
                    /*      */
 /*  393 */ this.showSyncMsg = "failed to sync printed students";
                    /*      */ return;
                    /*      */                }
                /*  396 */ for (Student one : students) {
                    /*  397 */ if (one.getEstmNumb() < 0) {
                        one.setEstmNumb(one.getEstmNumb() * -1);
                    }
                    /*  398 */ one.setDup(res_up.masterclock);
                    /*  399 */ one.setTzup(res_up.mastertz);
                    /*  400 */ this.em.merge(one);
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  408 */ List<TDOPrint> c_prints = new ArrayList<>();
            /*      */
 /*      */
 /*      */
 /*  412 */ List<Print> prints = this.em.createNamedQuery("Print.findByClientIdNupNdownFisy", Print.class).setParameter("clientId", Integer.valueOf(this.ref.getClientid())).setParameter("fisy", Integer.valueOf(this.ref.getFiscal_year())).getResultList();
            /*      */
 /*  414 */ if (m_prt > 0 || prints.size() > 0) {
                /*  415 */ for (Print one : prints) {
                    /*  416 */ TDOPrint tdo = genTDOPrintFromClientPrt(one);
                    /*  417 */ c_prints.add(tdo);
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*  423 */ log.info(">>>>>> dosync() collected newprts, size=%d.  will also check those non-prts for new students, whose size=%d", new Object[]{Integer.valueOf(prints.size()), Integer.valueOf(cup_students.size())});
            /*  424 */ for (TDOStudent onenewtdo : cup_students) {
                /*      */
 /*      */
 /*      */
 /*      */
 /*  429 */ prints = this.em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class).setParameter("clientId", Integer.valueOf(this.ref.getClientid())).setParameter("fisy", Integer.valueOf(this.ref.getFiscal_year())).setParameter("recid", onenewtdo.recid).getResultList();
                /*  430 */ for (Print one : prints) {
                    /*  431 */ TDOPrint tdo = genTDOPrintFromClientPrt(one);
                    /*  432 */ c_prints.add(tdo);
                    /*      */                }
                /*  434 */ log.info(">>>>>> dosync() collected extra prts for new/pick std %s, size=%d", new Object[]{onenewtdo.recid, Integer.valueOf(prints.size())});
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  463 */ List<String> recs = this.em.createNativeQuery("SELECT s.recid FROM Student s WHERE s.student_Fisy = ? and s.dup>0 and s.ddown=0  and s.client_Id = ?  and s.prt_Times=0 and  exists( select '1' from Prints o  where o.dup=0 and o.ddown=0 and o.prt_Time=0 and o.recid= s.recid)").setParameter(1, Integer.valueOf(this.ref.getFiscal_year())).setParameter(2, Integer.valueOf(this.ref.getClientid())).getResultList();
            /*  464 */ if (recs != null && recs.size() > 0) {
                /*  465 */ for (String rec : recs) {
                    /*      */
 /*      */
 /*      */
 /*      */
 /*  470 */ List<Print> nprts = this.em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class).setParameter("clientId", Integer.valueOf(this.ref.getClientid())).setParameter("fisy", Integer.valueOf(this.ref.getFiscal_year())).setParameter("recid", rec).getResultList();
                    /*  471 */ for (Print prt : nprts) {
                        /*  472 */ TDOPrint tdo = genTDOPrintFromClientPrt(prt);
                        /*  473 */ c_prints.add(tdo);
                        /*      */                    }
                    /*  475 */ log.info(">>>>>> dosync() collected non-prts for new/pick std %s, size=%d", new Object[]{rec, Integer.valueOf(nprts.size())});
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  484 */ List<String> fakeprtimes = this.em.createNativeQuery("select a.recid from student a where a.prt_times>0 and a.dup>0 and not exists ( select '1' from prints b where b.recid = a.recid and b.prt_time>0)").getResultList();
            /*  485 */ if (fakeprtimes != null && fakeprtimes.size() > 0) {
                /*  486 */ for (String onerecid : fakeprtimes) {
                    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  492 */ List<Print> nprts = this.em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class).setParameter("clientId", Integer.valueOf(this.ref.getClientid())).setParameter("fisy", Integer.valueOf(this.ref.getFiscal_year())).setParameter("recid", onerecid).getResultList();
                    /*  493 */ for (Print prt : nprts) {
                        /*  494 */ TDOPrint tdo = genTDOPrintFromClientPrt(prt);
                        /*  495 */ c_prints.add(tdo);
                        /*      */                    }
                    /*  497 */ log.info(">>>>>> dosync() collected fake-prts for new/pick std %s, size=%d", new Object[]{onerecid, Integer.valueOf(nprts.size())});
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*  503 */ log.info(">>>>>> dosync() collected total prts size=%d", new Object[]{Integer.valueOf(c_prints.size())});
            /*      */
 /*  505 */ prepReq(counselorid);
            /*  506 */ this.req.last_prt_dod = last_prt_dod;
            /*  507 */ TDOMasterRes<TDOPrint> res_prt = this.hook.syncNewPrts(this.req, c_prints);
            /*  508 */ log.info(">>>>>> dosync() get reply code %d for syncNewPrts", new Object[]{Integer.valueOf(res_prt.code)});
            /*  509 */ if (res_prt.code != 0) {
                /*  510 */ if (res_prt.msgs != null) /*  511 */ {
                    for (String msg : res_prt.msgs) {
                        log.info("  master msg : %s", new Object[]{msg});
                    }
                }
                /*      */
 /*  513 */ this.showSyncMsg = "failed to sync prints";
                /*      */
 /*      */ return;
                /*      */            }
            /*  517 */ int diffs = 0;
            /*  518 */ if (res_prt.objs != null && (diffs = res_prt.objs.size()) > 0) {
                /*      */
 /*  520 */ if (res_prt.obj2s != null && res_prt.obj2s.size() > 0) {
                    /*  521 */ Student std = null;
                    /*  522 */ for (TDOStudent tdo : res_prt.obj2s) {
                        /*  523 */ Student one = genClientStudFromTdo(tdo);
                        /*  524 */ if (one.getEstmNumb() < 0) {
                            one.setEstmNumb(one.getEstmNumb() * -1);
                        }
                        /*      */
 /*  526 */ if (tdo.clientId.intValue() == this.req.clientid) {
                            /*  527 */ std = (Student) this.em.find(Student.class, tdo.recid);
                            /*  528 */ if (std == null) {
                                /*  529 */ log.info(">> dosync() will save new std of same client since can not find that obj with pk=%s", new Object[]{tdo.recid});
                                /*  530 */ this.em.persist(one);
                                continue;
                                /*      */                            }
                            /*  532 */ log.info(">> dosync() will merge std of same client since find obj with pk=%s", new Object[]{tdo.recid});
                            /*  533 */ this.em.merge(one);
                            /*      */ continue;
                            /*      */                        }
                        /*  536 */ log.info(">> dosync() will save new std of diff client %s  with pk=%s", new Object[]{tdo.clientId, tdo.recid});
                        /*  537 */ this.em.persist(one);
                        /*      */                    }
                    /*      */                }
                /*      */
 /*      */
 /*      */
 /*  543 */ Print prt = null;
                /*      */
 /*  545 */ int x = 0;
                while (true) {
                    if (x < diffs) {
                        /*  546 */ TDOPrint tdo = res_prt.objs.get(x);
                        /*      */
 /*  548 */ if (tdo.clientId != this.req.clientid) {
                            /*  549 */ Print one = genClientPrintFromTdo(tdo);
                            /*  550 */ one.setDou(res_prt.masterclock);
                            /*  551 */ one.setDouTz(res_prt.mastertz);
                            /*      */
 /*  553 */ if (one.getRecid() == null) {
                                /*  554 */ log.info(">> dosync() found new PRT obj with null recid.");
                                /*  555 */ if (tdo.recid != null) {
                                    /*  556 */ log.info(">> dosync() reset new PRT obj recid from TDO");
                                    /*  557 */ one.setRecid(tdo.recid);
                                    /*      */                                } else {
                                    /*  559 */ log.info(">> dosync() can not set new PRT obj recid from TDO either. skipping....");
                                    /*      */
 /*      */ x++;
                                    /*      */                                }
                                /*      */                            }
                            /*      */
 /*  565 */ log.info(">> dosync() will save new PRT of diff client %s  with pk=%s of recid=%s", new Object[]{Integer.valueOf(tdo.clientId), tdo.prtId, tdo.recid});
                            /*  566 */ this.em.persist(one);
                            /*      */                        }
                        /*      */                    } else {
                        /*      */ break;
                        /*      */                    }
                    /*      */
 /*      */
 /*      */
 /*      */ x++;
                }
                /*      */
 /*      */            }
            /*      */
 /*      */
 /*  579 */ if (c_prints != null && c_prints.size() > 0) {
                /*  580 */ for (TDOPrint tdo : c_prints) {
                    /*  581 */ Print one = (Print) this.em.find(Print.class, tdo.prtId);
                    /*  582 */ one.setDou(res_prt.masterclock);
                    /*  583 */ one.setDouTz(res_prt.mastertz);
                    /*  584 */ if (one.getRecid() == null) {
                        /*  585 */ log.info(">> dosync() found PRT obj with null recid.");
                        /*  586 */ if (tdo.recid != null) {
                            /*  587 */ log.info(">> dosync() reset PRT obj recid from TDO");
                            /*  588 */ one.setRecid(tdo.recid);
                            /*      */                        } else {
                            /*  590 */ log.info(">> dosync() can not set PRT obj recid from TDO either. skipping....");
                            /*      */ continue;
                            /*      */                        }
                        /*      */                    }
                    /*  594 */ log.info(">> dosync() will merge  PRT (old recid=%s)   with pk=%s of recid=%s", new Object[]{one.getRecid(), tdo.prtId, tdo.recid});
                    /*  595 */ this.em.merge(one);
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*  600 */ log.info(">>>>>> ready to sync new rules. m_funds=%d, m_counselors=%d", new Object[]{Integer.valueOf(m_funds), Integer.valueOf(m_counselors)});
            /*  601 */ if (m_funds > 0) {
                /*      */
 /*      */
 /*      */
 /*  605 */ long last_fund_dos = Long.valueOf(this.em.createNativeQuery("select coalesce(max(dos), 0) from funds").getSingleResult().toString()).longValue();
                /*  606 */ prepReq(counselorid);
                /*  607 */ TDOMasterRes<TDOFund> res_fund = this.hook.getNewFunds(this.req, last_fund_dos);
                /*  608 */ if (res_fund.code != 0) {
                    /*  609 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() get reply code %d for syncNewFunds", new Object[]{Integer.valueOf(res_fund.code)});
                    /*  610 */ if (res_fund.msgs != null) /*  611 */ {
                        for (String msg : res_fund.msgs) {
                            log.info("  master msg : %s", new Object[]{msg});
                        }
                    }
                    /*      */
 /*  613 */ this.showSyncMsg = "failed to sync new funds";
                    /*      */ return;
                    /*      */                }
                /*  616 */ if (res_fund.objs != null && res_fund.objs.size() > 0) {
                    /*  617 */ for (TDOFund tdo : res_fund.objs) {
                        /*  618 */ tdo.updator = Integer.valueOf(0);
                        /*  619 */ Fund one = genClientFundFromTDO(tdo);
                        /*  620 */ Fund old = (Fund) this.em.find(Fund.class, one.getFundId());
                        /*  621 */ if (old != null) {
                            /*  622 */ this.em.merge(one);
                            /*  623 */ log.info("===== ====== updated fund of ID=%s from master, max=%d, auto=%d, new status=%s", new Object[]{one.getFundId(), one.getMax_ind(), one.getAuto_ind(), one.getStatus()});
                            continue;
                            /*      */                        }
                        /*  625 */ this.em.persist(one);
                        /*  626 */ log.info("===== inserted fund of ID=%s from master", new Object[]{one.getFundId()});
                        /*      */                    }
                    /*      */
 /*  629 */ log.info("=====get master funds version [%d]", new Object[]{res_fund.objsversion});
                    /*      */                }
                /*      */
 /*      */
 /*  633 */ if (res_fund.objs != null && res_fund.objs.size() > 0 && res_fund.objsversion.intValue() > 0) {
                    /*      */
 /*  635 */ Version fundver = new Version();
                    /*  636 */ fundver.setDos(System.currentTimeMillis());
                    /*  637 */ fundver.setDostz(this.ref.getTzSN());
                    /*  638 */ fundver.setModule("funds");
                    /*  639 */ fundver.setVersion(res_fund.objsversion.intValue());
                    /*  640 */ fundver.setEffInd(1);
                    /*  641 */ fundver.setSrcTime(0L);
                    /*  642 */ fundver.setSrcTz("");
                    /*  643 */ fundver.setSrcWho(0);
                    /*      */
 /*  645 */ saveUserVer(fundver);
                    /*  646 */ log.info("+++++++++++ saved fund module version");
                    /*      */
 /*  648 */ String over = this.ref.getSeed().getClientVersion();
                    /*  649 */ String[] overs = over.split("\\.");
                    /*  650 */ String[] rule_names = {"impl", "costs", "awards", "funds", "counselors"};
                    /*  651 */ String old = overs[3];
                    /*  652 */ if (!old.equals(String.valueOf(res_fund.objsversion))) {
                        /*  653 */ StringBuilder sbVer = new StringBuilder(16);
                        /*  654 */ int p = 0;
                        /*  655 */ for (p = 0; p < 3; p++) {
                            /*  656 */ sbVer.append(overs[p]).append(".");
                            /*      */                        }
                        /*  658 */ sbVer.append(res_fund.objsversion);
                        /*  659 */ for (int q = p + 1; q < overs.length; q++) {
                            /*  660 */ sbVer.append(".").append(overs[q]);
                            /*      */                        }
                        /*      */
 /*  663 */ log.info("+++++++++++ got client funds version [%s], master funds version [%s], and shall save [%s]", new Object[]{overs[3], res_fund.objsversion, sbVer.toString()});
                        /*  664 */ this.ref.getSeed().setClientVersion(sbVer.toString());
                        /*  665 */ this.em.merge(this.ref.getSeed());
                        /*  666 */ log.info("+++++++++++ config updated...");
                        /*  667 */ this.ref.refreshSeedVersion();
                        /*  668 */ log.info("+++++++++++ config refreshed..., ver=%s", new Object[]{this.ref.getSeed().getClientVersion()});
                        /*      */                    }
                    /*      */
 /*  671 */ this.ref.reloadFunds();
                    /*  672 */ log.info("+++++++++++ fund reloaded ...");
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*  677 */ if (m_counselors > 0) {
                /*      */
 /*  679 */ long last_user_dos = Long.valueOf(this.em.createNativeQuery("select coalesce(max(dos), 0) from counselors").getSingleResult().toString()).longValue();
                /*  680 */ prepReq(counselorid);
                /*  681 */ TDOMasterRes<TDOCounselor> res_user = this.hook.getNewCounselors(this.req, last_user_dos);
                /*  682 */ if (res_user.code != 0) {
                    /*  683 */ log.info("XXXXXXXXXXXXXXXXXXXX dosync() get reply code %d for syncNewCounselors", new Object[]{Integer.valueOf(res_user.code)});
                    /*  684 */ if (res_user.msgs != null) /*  685 */ {
                        for (String msg : res_user.msgs) {
                            log.info("  master msg : %s", new Object[]{msg});
                        }
                    }
                    /*      */
 /*  687 */ this.showSyncMsg = "failed to sync users";
                    /*      */ return;
                    /*      */                }
                /*  690 */ if (res_user.objs != null && res_user.objs.size() > 0) {
                    /*  691 */ for (TDOCounselor tdo : res_user.objs) {
                        /*  692 */ Counselor one = genClientCounselorFromTDO(tdo);
                        /*  693 */ Counselor old = (Counselor) this.em.find(Counselor.class, one.getUserid());
                        /*  694 */ if (old != null) {
                            this.em.merge(one);
                            continue;
                        }
                        /*  695 */ this.em.persist(one);
                        /*      */                    }
                    /*      */                }
                /*  698 */ if (res_user.objs != null && res_user.objs.size() > 0 && res_user.objsversion.intValue() > 0) {
                    /*  699 */ Version userver = new Version();
                    /*  700 */ userver.setDos(System.currentTimeMillis());
                    /*  701 */ userver.setDostz(this.ref.getTzSN());
                    /*  702 */ userver.setModule("counselors");
                    /*  703 */ userver.setVersion(res_user.objsversion.intValue());
                    /*  704 */ userver.setEffInd(1);
                    /*  705 */ userver.setSrcTime(0L);
                    /*  706 */ userver.setSrcTz("");
                    /*  707 */ userver.setSrcWho(0);
                    /*  708 */ saveUserVer(userver);
                    /*      */
 /*  710 */ String over = this.ref.getSeed().getClientVersion();
                    /*      */
 /*  712 */ int p = over.lastIndexOf(".") + 1;
                    /*  713 */ String old = over.substring(p);
                    /*      */
 /*      */
 /*  716 */ if (!old.equals(String.valueOf(res_user.objsversion))) {
                        /*  717 */ StringBuilder sbVer = new StringBuilder(16);
                        /*  718 */ sbVer.append(over.substring(0, p));
                        /*  719 */ sbVer.append(res_user.objsversion);
                        /*      */
 /*  721 */ log.info("+++++++++++ master user version [%s], and shall save [%s]", new Object[]{res_user.objsversion, sbVer.toString()});
                        /*  722 */ this.ref.getSeed().setClientVersion(sbVer.toString());
                        /*      */
 /*  724 */ this.em.merge(this.ref.getSeed());
                        /*  725 */ log.info("+++++++++++ updated config record!!!");
                        /*  726 */ this.ref.refreshSeedVersion();
                        /*      */                    }
                    /*      */
 /*  729 */ this.ref.reloadUsers();
                    /*  730 */ log.info("+++++++++++ reloaded users");
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*  736 */ this.showSyncMsg = "sync is done. scheduled next run.";
            
        info_.sumUp();// org.jboss.weld.context.ContextNotActiveException: WELD-001303 No active contexts for scope type javax.enterprise.context.SessionScoped
                 
        //info.newStudsOfUser
        //info.sumUp();
 
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */        } /*  742 */ catch (Exception e) {
            /*  743 */ e.printStackTrace();
            /*  744 */ this.showSyncMsg = "Failed to sync by Exceptions";
            /*      */ return;
            /*      */        } finally {
            /*  747 */ log.info("+++++++++++ sync return msg=[%s]", new Object[]{this.showSyncMsg});
            /*      */        }
        /*      */    }

    /*      */
 /*      */ private int pollmaster() {
        /*  752 */ return 1;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String init() {
        /*  761 */ if (this.init_lock.intValue() > 0) {
            return null;
        }
        /*  762 */ synchronized (this.init_lock) {
            /*  763 */ if (this.init_lock.intValue() == 0) {
                /*  764 */ this.init_lock = Integer.valueOf(1);
                /*  765 */ doinit();
                /*  766 */ this.init_lock = Integer.valueOf(0);
                /*      */            }
            /*      */        }
        /*  769 */ return null;
        /*      */    }

    /*      */ public String doinit() {
        /*  772 */ String url = null;
        /*  773 */ StringBuilder sb = new StringBuilder(512);
        /*  774 */ int ind = 0;
        /*      */
 /*      */ try {
            /*  777 */ this.init_on_ind = 1;
            /*  778 */ sb.append("\n<br/>starting initialization ...");
            /*  779 */ log.info("starting initialization ...");
            /*      */
 /*      */
 /*      */
 /*  783 */ sb.append("done\n<br/>").append("checking Estimator status ...");
            /*  784 */ log.info("checking Estimator status ...");
            /*  785 */ if (this.ref.getClientid() > 0) {
                return url;
            }
            /*      */
 /*      */
 /*  788 */ sb.append("done\n<br/>").append("checking master status ...");
            /*  789 */ log.info("checking master status ...");
            /*  790 */ this.masterUp = this.ref.reachHostBySocket(this.ref.getSeed().getMastername(), Integer.parseInt(this.ref.getSeed().getMasterport())) ? 1 : 0;
            /*  791 */ if (this.masterUp == 0) {
                return url;
            }
            /*      */
 /*      */
 /*  794 */ sb.append("done\n<br/>").append("shaking hands with master ...");
            /*  795 */ log.info("shaking hands with master ...");
            /*  796 */ hookMaster();
            /*  797 */ if (this.hook == null) {
                return url;
            }
            /*      */
 /*      */
 /*  800 */ sb.append("done\n<br/>").append("sending initial request to master and waiting for response ...");
            /*  801 */ log.info("sending initial request to master and waiting for response ...");
            /*  802 */ initReqRes();
            /*  803 */ if (this.res == null) {
                return url;
            }
            /*      */
 /*      */
 /*  806 */ sb.append("done\n<br/>").append("checking response data ...");
            /*  807 */ log.info("checking response data ...");
            /*  808 */ if (this.res.code != 0 || this.res.clientInitNumb == 0) {
                /*  809 */ ind = -1;
                /*  810 */ sb.append("failed");
                /*  811 */ if (this.res.msgs != null) {
                    /*  812 */ for (String msg : this.res.msgs) {
                        /*  813 */ sb.append("\n<br/>&nbsp;&nbsp;master: ").append(msg);
                        /*  814 */ log.info("master msg: %s", new Object[]{msg});
                        /*      */                    }
                    /*      */                }
                /*      */
 /*  818 */ return url;
                /*      */            }
            /*      */
 /*      */
 /*  822 */ sb.append("done\n<br/>").append("saving client identity ...");
            /*  823 */ log.info("saving client identity ...");
            /*  824 */ Config seed = this.ref.getSeed();
            /*      */
 /*  826 */ if (this.res.remote_clock_ind != 0) {
                /*  827 */ this.ref.setSys_clk_msg("Estimator clock has " + Math.abs(this.res.remote_clock_ind) + " seonds offset from Master." + ((this.res.remote_clock_ind > 0) ? " You have to fix the issue." : ""));
                /*  828 */ this.ref.setSys_clk_ind(this.res.remote_clock_ind);
                /*      */            }
            /*  830 */ if (this.res.remote_stop_sys_ind > 0) {
                /*  831 */ this.ref.setSys_blk_msg("Master has disabled this Estimator.");
                /*  832 */ this.ref.setSys_blk_ind(this.res.remote_stop_sys_ind);
                /*      */            }
            /*  834 */ if (this.res.remote_stop_user_ind > 0) {
                /*  835 */ this.ref.setSys_usr_msg("Master has denied you. You can not sign in or operate.");
                /*  836 */ this.ref.setSys_usr_ind(this.res.remote_stop_user_ind);
                /*      */            }
            /*      */
 /*  839 */ if (this.ref.getSys_blk_ind() > 0 || this.ref.getSys_clk_ind() > 0) {
                /*  840 */ log.info("master set blk or clk ind. quit");
                /*  841 */ return url;
                /*      */            }
            /*      */
 /*  844 */ String over = seed.getClientVersion();
            /*  845 */ String mver = this.res.masterver;
            /*  846 */ log.info("+++++++++++ got client init version [%s], master latest version [%s], and shall save [%s]", new Object[]{over, mver, over.substring(0, over.indexOf(".")) + mver.substring(mver.indexOf("."))});
            /*  847 */ seed.setClientVersion(over.substring(0, over.indexOf(".")) + mver.substring(mver.indexOf(".")));
            /*  848 */ Version userver = new Version();
            /*  849 */ userver.setDos(System.currentTimeMillis());
            /*  850 */ userver.setDostz(this.ref.getTzSN());
            /*  851 */ userver.setModule("counselors");
            /*  852 */ userver.setVersion(this.res.objsversion.intValue());
            /*  853 */ userver.setEffInd(1);
            /*  854 */ userver.setSrcTime(this.res.objs_masterdoe);
            /*  855 */ userver.setSrcTz(this.res.objs_masterdoetz);
            /*  856 */ userver.setSrcWho(this.res.objs_masterupdator.intValue());
            /*      */
 /*      */
 /*      */
 /*      */
 /*  861 */ sb.append("done\n<br/>").append("checking and saving  users/counselors data ...");
            /*  862 */ log.info("checking and saving  users/counselors data ...");
            /*  863 */ if (this.res.objs == null || this.res.objs.size() == 0) {
                /*  864 */ ind = -1;
                /*  865 */ sb.append("failed");
                /*  866 */ return url;
                /*      */            }
            /*  868 */ List<TDOCounselor> users = this.res.objs;
            /*  869 */ saveInitUsers(users);
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  877 */ sb.append("done\n<br/>").append("sending second request to master and waiting for response ...");
            /*  878 */ log.info("sending second request to master and waiting for response ...");
            /*  879 */ secdReqRes();
            /*  880 */ if (this.res_fund == null) {
                return url;
            }
            /*  881 */ if (this.res_fund.code != 0) {
                /*  882 */ ind = -1;
                /*  883 */ sb.append("failed");
                /*  884 */ if (this.res_fund.msgs != null) {
                    /*  885 */ for (String msg : this.res_fund.msgs) {
                        /*  886 */ sb.append("\n<br/>&nbsp;&nbsp;  master: ").append(msg);
                        /*  887 */ log.info(" master msg: %s", new Object[]{msg});
                        /*      */                    }
                    /*      */                }
                /*  890 */ return url;
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*  895 */ sb.append("done\n<br/>").append("checking and saving funds data ...");
            /*  896 */ log.info("checking and saving funds data ...");
            /*  897 */ List<TDOFund> funds = this.res_fund.objs;
            /*  898 */ saveInitFunds(funds);
            /*  899 */ sb.append("done\n<br/>");
            /*  900 */ Version fundver = new Version();
            /*  901 */ fundver.setDos(System.currentTimeMillis());
            /*  902 */ fundver.setDostz(this.ref.getTzSN());
            /*  903 */ fundver.setModule("funds");
            /*  904 */ fundver.setVersion(this.res_fund.objsversion.intValue());
            /*  905 */ fundver.setEffInd(1);
            /*  906 */ fundver.setSrcTime(this.res_fund.objs_masterdoe);
            /*  907 */ fundver.setSrcTz(this.res_fund.objs_masterdoetz);
            /*  908 */ fundver.setSrcWho(this.res_fund.objs_masterupdator.intValue());
            /*      */
 /*      */
 /*  911 */ saveUserVer(userver);
            /*  912 */ saveUserVer(fundver);
            /*  913 */ seed.setClientid(Integer.valueOf(this.res.clientInitNumb));
            /*  914 */ this.ref.setClientid(this.res.clientInitNumb);
            /*  915 */ seed.setMasterecho(javaAESdecryptor(aesKey, this.res.aesData));
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  922 */ sb.append("downloading all active students from master ......");
            /*  923 */ log.info("downloading all active students from master ......");
            /*  924 */ this.req.clientclock = System.currentTimeMillis();
            /*  925 */ this.req.clienttz = this.ref.getTzSN();
            /*  926 */ this.req.last_stud_dod = 0L;
            /*      */
 /*      */
 /*  929 */ this.req.fisy = this.ref.getSeed().getClientFscy();
            /*  930 */ log.info("doinit() ---- ----- set req fisy from seed=%d", new Object[]{Integer.valueOf(this.req.fisy)});
            /*      */
 /*  932 */ TDOMasterRes<TDOStudent> res_std = this.hook.syncNewPickedStuds(this.req, null);
            /*      */
 /*  934 */ if (res_std.code != 0) {
                /*  935 */ ind = -1;
                /*  936 */ sb.append("failed");
                /*  937 */ if (res_std.msgs != null) {
                    /*  938 */ for (String msg : res_std.msgs) {
                        /*  939 */ sb.append("\n<br/>&nbsp;&nbsp;  master: ").append(msg);
                        /*  940 */ log.info("master msg: %s", new Object[]{msg});
                        /*      */                    }
                    /*      */                }
                /*  943 */ return url;
                /*      */            }
            /*  945 */ sb.append("done\n<br/>");
            /*  946 */ if (res_std.objs != null && res_std.objs.size() > 0) {
                /*  947 */ Student std = null;
                /*  948 */ for (TDOStudent tdo : res_std.objs) {
                    /*  949 */ Student one = genClientStudFromTdo(tdo);
                    /*  950 */ if (tdo.clientId.intValue() == this.req.clientid) {
                        /*  951 */ std = (Student) this.em.find(Student.class, one.getRecid());
                        /*  952 */ if (std == null) {
                            this.em.persist(one);
                            continue;
                        }
                        /*  953 */ this.em.merge(one);
                        continue;
                        /*      */                    }
                    /*  955 */ this.em.persist(one);
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*  962 */ this.req.clientclock = System.currentTimeMillis();
            /*  963 */ this.req.clienttz = this.ref.getTzSN();
            /*  964 */ this.req.last_prt_dod = 0L;
            /*  965 */ TDOMasterRes<TDOPrint> res_prt = this.hook.syncNewPrts(this.req, null);
            /*  966 */ sb.append("downloading all print records and corresponding students from master ......");
            /*  967 */ log.info("downloading all print records and corresponding students from master ......");
            /*  968 */ if (res_prt.code != 0) {
                /*  969 */ ind = -1;
                /*  970 */ sb.append("failed");
                /*  971 */ if (res_prt.msgs != null) {
                    /*  972 */ for (String msg : res_prt.msgs) {
                        /*  973 */ sb.append("\n<br/>&nbsp;&nbsp;  master: ").append(msg);
                        /*  974 */ log.info("master msg: %s", new Object[]{msg});
                        /*      */                    }
                    /*      */                }
                /*  977 */ return url;
                /*      */            }
            /*  979 */ if (res_prt.objs != null && res_prt.objs.size() > 0) {
                /*  980 */ Print prt = null;
                /*  981 */ for (TDOPrint tdo : res_prt.objs) {
                    /*  982 */ Print one = genClientPrintFromTdo(tdo);
                    /*  983 */ this.em.persist(one);
                    /*      */                }
                /*  985 */ if (res_prt.obj2s != null && res_prt.obj2s.size() > 0) {
                    /*  986 */ Student std = null;
                    /*  987 */ for (TDOStudent tdo : res_prt.obj2s) {
                        /*  988 */ Student one = genClientStudFromTdo(tdo);
                        /*  989 */ if (tdo.clientId.intValue() == this.req.clientid) {
                            /*  990 */ std = (Student) this.em.find(Student.class, one.getRecid());
                            /*  991 */ if (std == null) {
                                this.em.persist(one);
                                continue;
                            }
                            /*  992 */ this.em.merge(one);
                            continue;
                            /*      */                        }
                        /*  994 */ this.em.persist(one);
                        /*      */                    }
                    /*      */                }
                /*      */            }
            /*      */
 /*      */
 /* 1000 */ sb.append("done\n<br/>");
            /* 1001 */ log.info("done fetching student and prints data from master for init");
            /*      */
 /*      */
 /* 1004 */ saveInitCfg(seed);
            /* 1005 */ this.ref.reloadSeed();
            /* 1006 */ this.ref.getFunds();
            /* 1007 */ ind = 1;
            /* 1008 */ log.info("done init. saved clientid and seed record");
            /*      */
 /*      */        } /* 1011 */ catch (Exception ex) {
            /* 1012 */ log.info("", ex);
            /* 1013 */ this.init_step_msg = "Error: " + ex.getMessage();
            /*      */        } finally {
            /* 1015 */ this.init_on_ind = 0;
            /* 1016 */ if (ind == 0) {
                sb.append("failed\n<br/>");
            }
            /* 1017 */ if (ind == 1) {
                /* 1018 */ sb.append("\n<br/><br/>Initialization is performed.");
                /* 1019 */ log.info("Initialization is performed.");
                /*      */            } else {
                /* 1021 */ sb.append("\n<br/><br/>Initialization is not performed.");
                /* 1022 */ log.info("Initialization is not performed.");
                /*      */            }
            /* 1024 */ this.init_step_msg = sb.toString();
            /*      */        }
        /* 1026 */ return url;
        /*      */    }

    /*      */
 /*      */ private void hookMaster() {
        /* 1030 */ this.masterUp = this.ref.getMaster_up_ind();
        /* 1031 */ log.error("~~~~~~~~~~~~~~~~~~~ hookMaster() got master_up_ind=%d. ", new Object[]{Integer.valueOf(this.masterUp)});
        /* 1032 */ if (this.masterUp <= 0) {
            /* 1033 */ this.hook = null;
            /*      */
 /*      */ return;
            /*      */        }
        /* 1037 */ String url = this.ref.getSeed().getMasterurl();
        /* 1038 */ HessianProxyFactory factory = new HessianProxyFactory();
        /*      */ try {
            /* 1040 */ this.hook = (SPIEstimatorMaster) factory.create(SPIEstimatorMaster.class, url);
            /*      */
 /*      */
 /*      */        } /* 1044 */ catch (Exception e) {
            /* 1045 */ this.hook = null;
            /* 1046 */ this.ref.setMaster_up_ind(0);
            /*      */
 /* 1048 */ e.printStackTrace();
            /*      */        }
        /* 1050 */ if (aesKey == null) {
            /*      */ try {
                /* 1052 */ KeyGenerator keygen = KeyGenerator.getInstance("AES");
                /* 1053 */ aesKey = keygen.generateKey();
                /* 1054 */            } catch (Exception e) {
                /* 1055 */ aesKey = null;
                /* 1056 */ e.printStackTrace();
                /*      */            }
            /*      */        }
        /* 1059 */ log.info("hookMaster() hooked master at url=[%s]", new Object[]{url});
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ private void prepReq(int counselorid) {
        /* 1065 */ if (this.req == null) {
            /* 1066 */ this.req = new TDOEstimatorReq();
            /* 1067 */ HashMap<String, String> map = new HashMap<>();
            /* 1068 */ this.req.fisy = this.ref.getSeed().getClientFscy();
            /* 1069 */ log.info(" prepreq()---- ----- set req fisy from seed=%d", new Object[]{Integer.valueOf(this.req.fisy)});
            /*      */
 /* 1071 */ this.req.clientid = this.ref.getClientid();
            /*      */
 /* 1073 */ if (aesKey == null) {
                /*      */ try {
                    /* 1075 */ KeyGenerator keygen = KeyGenerator.getInstance("AES");
                    /* 1076 */ aesKey = keygen.generateKey();
                    /* 1077 */                } catch (Exception e) {
                    /* 1078 */ aesKey = null;
                    /* 1079 */ e.printStackTrace();
                    /* 1080 */ this.req = null;
                    /*      */ return;
                    /*      */                }
                /*      */            }
            /* 1084 */ this.req.aesData = javaAESencryptor(this.ref.getSeed().getMasterecho());
            /* 1085 */ this.req.c_key = aesKey;
            /*      */
 /* 1087 */ this.req.clientversions = this.ref.getFullclientverions();
            /*      */
 /* 1089 */ this.req.counselorid = counselorid;
            /*      */
 /* 1091 */ this.req.clienthostname = this.ref.getOSinfo();
            /* 1092 */ this.req.clientaddress = this.ref.getAddress();
            /* 1093 */ this.req.clientjava = this.ref.getJavaInfo();
            /*      */
 /*      */
 /* 1096 */ this.req.last_stud_dod = 0L;
            /* 1097 */ this.req.last_prt_dod = 0L;
            /*      */
 /* 1099 */ this.req.clienttz = this.ref.getTzSN();
            /* 1100 */ this.req.clientclock = System.currentTimeMillis();
            /*      */        } else {
            /* 1102 */ this.req.clientversions = this.ref.getFullclientverions();
            /* 1103 */ this.req.counselorid = counselorid;
            /* 1104 */ this.req.last_stud_dod = 0L;
            /* 1105 */ this.req.last_prt_dod = 0L;
            /*      */
 /* 1107 */ this.req.clienttz = this.ref.getTzSN();
            /* 1108 */ this.req.clientclock = System.currentTimeMillis();
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ private void initReqRes() {
        /* 1115 */ this.res = null;
        /* 1116 */ if (this.hook == null) {
            /* 1117 */ log.error(" ***** testReqRes() can not get master handler through Hessian factory. quit. ********* ");
            /*      */ return;
            /*      */        }
        /* 1120 */ if (aesKey == null) {
            /* 1121 */ log.error(" ***** testReqRes() can not get AES key for secure communication. quit. ********* ");
            /*      */
 /*      */ return;
            /*      */        }
        /* 1125 */ this.req = new TDOEstimatorReq();
        /* 1126 */ HashMap<String, String> map = new HashMap<>();
        /* 1127 */ this.req.fisy = this.ref.getSeed().getClientFscy();
        /* 1128 */ log.info("initreqres()---- ----- set req fisy from seed=%d", new Object[]{Integer.valueOf(this.req.fisy)});
        /*      */
 /* 1130 */ this.req.clientid = 0;
        /* 1131 */ this.req.c_key = aesKey;
        /* 1132 */ this.req.clientclock = System.currentTimeMillis();
        /* 1133 */ this.req.clienttz = this.ref.getTzSN();
        /* 1134 */ this.req.clientversions = null;
        /* 1135 */ this.req.counselorid = 0;
        /* 1136 */ this.req.aesData = null;
        /* 1137 */ this.req.last_stud_dod = 0L;
        /* 1138 */ this.req.last_prt_dod = 0L;
        /*      */
 /* 1140 */ this.req.clienthostname = this.ref.getOSinfo();
        /* 1141 */ this.req.clientaddress = this.ref.getAddress();
        /* 1142 */ this.req.clientjava = this.ref.getJavaInfo();
        /*      */
 /*      */
 /* 1145 */ this.res = this.hook.init(this.req);
        /*      */
 /* 1147 */ if (this.res == null) {
            /* 1148 */ log.info(" --- got empty master init res.");
            /*      */        } else {
            /* 1150 */ log.info(" --- got master res. code=%d, new clientid=%d", new Object[]{Integer.valueOf(this.res.code), Integer.valueOf(this.res.clientInitNumb)});
            /* 1151 */ if (this.res.code == 0) {
                /* 1152 */ log.info(" --- got master counselors. numb=%d, first one's name=%s", new Object[]{Integer.valueOf(this.res.objs.size()), ((TDOCounselor) this.res.objs.get(0)).username});
                /* 1153 */ log.info(" --- got master assigned client key=%s", new Object[]{javaAESdecryptor(aesKey, this.res.aesData)});
                /*      */            }
            /* 1155 */ if (this.res.msgs != null) /* 1156 */ {
                for (String msg : this.res.msgs) {
                    /* 1157 */ log.info(" --- got master res msg = %s", new Object[]{msg});
                    /*      */                }
            }
            /*      */        }
        /*      */    }

    /*      */
 /*      */ private void secdReqRes() {
        /* 1163 */ this.req.clientid = this.res.clientInitNumb;
        /* 1164 */ this.req.aesData = this.res.aesData;
        /* 1165 */ this.req.clientclock = System.currentTimeMillis();
        /* 1166 */ this.req.clientversions = this.ref.getFullclientverions();
        /* 1167 */ this.req.c_key = aesKey;
        /* 1168 */ this.req.aesData = this.res.aesData;
        /*      */
 /* 1170 */ this.res_fund = this.hook.getInitFunds(this.req);
        /* 1171 */ if (this.res_fund == null) {
            /* 1172 */ log.info(" --- got empty master funds res.");
            /* 1173 */        } else if (this.res_fund.code != 0) {
            /* 1174 */ log.info(" --- got master res. code=%d", new Object[]{Integer.valueOf(this.res_fund.code)});
            /* 1175 */ if (this.res_fund.msgs != null) {
                /* 1176 */ for (String msg : this.res_fund.msgs) {
                    /* 1177 */ log.info(" --- got master funds res msg = %s", new Object[]{msg});
                    /*      */                }
                /*      */            }
            /*      */        }
        /*      */    }

    /*      */
 /*      */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*      */ private void saveInitCfg(Config seed) {
        /* 1185 */ this.em.createNativeQuery("update config set clientid=?, masterecho=?, client_version=? where clientid=0")
                /* 1186 */.setParameter(1, seed.getClientid())
                /* 1187 */.setParameter(2, seed.getMasterecho())
                /* 1188 */.setParameter(3, seed.getClientVersion())
                /* 1189 */.executeUpdate();
        /*      */    }

    /*      */
 /*      */
 /*      */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*      */ private void saveInitUsers(List<TDOCounselor> users) {
        /* 1195 */ this.em.createNativeQuery("delete from counselors").executeUpdate();
        /* 1196 */ for (TDOCounselor one : users) {
            /* 1197 */ Counselor ndo = new Counselor();
            /*      */
 /* 1199 */ ndo.setUserid(one.userid);
            /* 1200 */ ndo.setUsername(one.username);
            /* 1201 */ ndo.setSuperuser(Integer.valueOf(one.superuser.booleanValue() ? 1 : 0));
            /* 1202 */ ndo.setStatus(one.status);
            /* 1203 */ ndo.setDeptName(one.dept);
            /* 1204 */ ndo.setLsuid(one.lsuid);
            /* 1205 */ ndo.setCreator((int) one.creator);
            /* 1206 */ ndo.setDoe(one.doe);
            /* 1207 */ ndo.setDoetz(one.doetz);
            /* 1208 */ ndo.setEditor(Integer.valueOf((int) one.editor));
            /* 1209 */ ndo.setDom(one.dom);
            /* 1210 */ ndo.setDomtz(one.domtz);
            /* 1211 */ ndo.setEmail(one.email);
            /*      */
 /* 1213 */ ndo.setDos(one.dos);
            /* 1214 */ ndo.setDostz(one.dostz);
            /*      */
 /*      */ try {
                /* 1217 */ this.em.persist(ndo);
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */            } /* 1224 */ catch (ConstraintViolationException ve) {
                /*      */
 /* 1226 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                    /* 1227 */ System.out.println("########## Path: " + cv.getPropertyPath().toString() + " ########### FAILED: " + cv.getMessage());
                    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */                }
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */            } /* 1239 */ catch (Exception e) {
                /* 1240 */ e.printStackTrace();
                /*      */            }
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*      */ private void saveInitFunds(List<TDOFund> funds) {
        /* 1249 */ this.em.createNativeQuery("delete from funds").executeUpdate();
        /* 1250 */ for (TDOFund one : funds) {
            /* 1251 */ Fund ndo = new Fund();
            /* 1252 */ ndo.setFundId(one.fundId);
            /* 1253 */ ndo.setFundCode(one.fundCode);
            /* 1254 */ ndo.setFundDesc(one.fundDesc);
            /*      */
 /* 1256 */ ndo.setHasMatching(one.hasMatching);
            /* 1257 */ ndo.setMatchPerc(one.matchPerc);
            /* 1258 */ ndo.setMatchTop(one.matchTop);
            /* 1259 */ ndo.setReqNoteInd(one.reqNoteInd);
            /*      */
 /* 1261 */ ndo.setStatus(one.status);
            /* 1262 */ ndo.setEarningsMatch(one.earningsMatch);
            /* 1263 */ ndo.setPriority(one.priority);
            /* 1264 */ ndo.setCreator(one.creator);
            /* 1265 */ ndo.setDoe(one.doe);
            /* 1266 */ ndo.setDoetz(one.doetz);
            /*      */
 /* 1268 */ ndo.setUpdator(Integer.valueOf((one.updator == null) ? 0 : one.updator.intValue()));
            /* 1269 */ ndo.setDom(one.dom);
            /* 1270 */ ndo.setDomtz(one.domtz);
            /*      */
 /* 1272 */ ndo.setSyncor(Integer.valueOf(0));
            /* 1273 */ ndo.setDos(one.dos);
            /* 1274 */ ndo.setDostz(one.dostz);
            /*      */
 /* 1276 */ ndo.setAuto_ind(one.auto_ind);
            /* 1277 */ ndo.setMax_ind(one.max_ind);
            /*      */
 /* 1279 */ ndo.setInstCapExcept(one.instCapExcept);
            /*      */ try {
                /* 1281 */ this.em.persist(ndo);
                /* 1282 */            } catch (ConstraintViolationException ve) {
                /*      */
 /* 1284 */ for (ConstraintViolation cv : ve.getConstraintViolations()) {
                    /* 1285 */ System.out.println("########## Path: " + cv.getPropertyPath().toString() + " ########### FAILED: " + cv.getMessage());
                    /*      */                }
                /* 1287 */            } catch (Exception e) {
                /* 1288 */ e.printStackTrace();
                /*      */            }
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */ @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*      */ private void saveUserVer(Version ver) {
        /* 1296 */ this.em.createNativeQuery("update versions set eff_ind=0 where eff_ind=1 and module=?").setParameter(1, ver.getModule()).executeUpdate();
        /* 1297 */ this.em.persist(ver);
        /* 1298 */ log.info("------ saveUserVer()  updated versions eff_ind to 0 for module=%s", new Object[]{ver.getModule()});
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void timedTask() {
    }

    /*      */
 /*      */
 /*      */
 /*      */ public String handsync() {
        /* 1308 */ return "sync?faces-redirect=true";
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getInit_step_msg() {
        /* 1313 */ return this.init_step_msg;
        /*      */    }

    /*      */
 /*      */ public void setInit_step_msg(String init_step_msg) {
        /* 1317 */ this.init_step_msg = init_step_msg;
        /*      */    }

    /*      */
 /*      */ public int getInit_on_ind() {
        /* 1321 */ return this.init_on_ind;
        /*      */    }

    /*      */
 /*      */ public void setInit_on_ind(int init_on_ind) {
        /* 1325 */ this.init_on_ind = init_on_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String javaAESdecryptor(SecretKey key, byte[] cipherStr) {
        /* 1331 */ if (cipherStr == null || cipherStr.length == 0) {
            return "";
        }
        /*      */
 /*      */
 /*      */ try {
            /* 1335 */ KeyGenerator keygen = KeyGenerator.getInstance("AES");
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1341 */ Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1356 */ aesCipher.init(2, key);
            /*      */
 /* 1358 */ byte[] cleartext1 = aesCipher.doFinal(cipherStr);
            /*      */
 /*      */
 /*      */
 /* 1362 */ System.out.println("client javaAESdecryptor() deciphered text=" + new String(cleartext1));
            /* 1363 */ return new String(cleartext1);
            /* 1364 */        } catch (Exception e) {
            /* 1365 */ e.printStackTrace();
            /*      */
 /* 1367 */ return "client AES decryptor  not working";
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public byte[] javaAESencryptor(String clearStr) {
        /* 1372 */ if (clearStr == null || clearStr.isEmpty()) {
            return "".getBytes();
        }
        /*      */
 /*      */
 /*      */ try {
            /* 1376 */ KeyGenerator keygen = KeyGenerator.getInstance("AES");
            /*      */
 /* 1378 */ if (aesKey == null) {
                /* 1379 */ aesKey = keygen.generateKey();
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /* 1385 */ Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            /*      */
 /*      */
 /*      */
 /* 1389 */ aesCipher.init(1, aesKey);
            /*      */
 /*      */
 /* 1392 */ byte[] cleartext = clearStr.getBytes();
            /*      */
 /* 1394 */ byte[] ciphertext = aesCipher.doFinal(cleartext);
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1405 */ return ciphertext;
            /* 1406 */        } catch (Exception e) {
            /* 1407 */ e.printStackTrace();
            /*      */
 /* 1409 */ return "client AES encryptor not working".getBytes();
            /*      */        }
        /*      */    }

    /*      */ public String getShowSyncMsg() {
        /* 1413 */ return this.showSyncMsg;
        /*      */    }

    /*      */
 /*      */ public void setShowSyncMsg(String showSyncMsg) {
        /* 1417 */ this.showSyncMsg = showSyncMsg;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ private Fund genClientFundFromTDO(TDOFund tdo) {
        /* 1423 */ Fund one = new Fund();
        /* 1424 */ one.setCreator(tdo.creator);
        /* 1425 */ one.setDoe(tdo.doe);
        /* 1426 */ one.setDoetz(tdo.doetz);
        /* 1427 */ one.setUpdator(tdo.updator);
        /* 1428 */ one.setDom(tdo.dom);
        /* 1429 */ one.setDomtz(tdo.domtz);
        /*      */
 /* 1431 */ one.setFundId(tdo.fundId);
        /* 1432 */ one.setFundCode(tdo.fundCode);
        /* 1433 */ one.setFundDesc(tdo.fundDesc);
        /* 1434 */ one.setEarningsMatch(tdo.earningsMatch);
        /* 1435 */ one.setHasMatching(tdo.hasMatching);
        /* 1436 */ one.setMatchPerc(tdo.matchPerc);
        /* 1437 */ one.setMatchTop(tdo.matchTop);
        /* 1438 */ one.setInstCapExcept(tdo.instCapExcept);
        /* 1439 */ one.setPriority(tdo.priority);
        /* 1440 */ one.setReqNoteInd(tdo.reqNoteInd);
        /* 1441 */ one.setStatus(tdo.status);
        /*      */
 /* 1443 */ one.setSyncor(tdo.syncor);
        /* 1444 */ one.setDos(tdo.dos);
        /* 1445 */ one.setDostz(tdo.dostz);
        /*      */
 /*      */
 /*      */
 /*      */
 /* 1450 */ one.setAuto_ind(tdo.auto_ind);
        /* 1451 */ one.setMax_ind(tdo.max_ind);
        /*      */
 /* 1453 */ return one;
        /*      */    }

    /*      */ private Counselor genClientCounselorFromTDO(TDOCounselor tdo) {
        /* 1456 */ Counselor one = new Counselor();
        /* 1457 */ one.setUserid(tdo.userid);
        /* 1458 */ one.setUsername(tdo.username);
        /* 1459 */ one.setStatus(tdo.status);
        /* 1460 */ one.setSuperuser(Integer.valueOf(tdo.superuser.booleanValue() ? 1 : 0));
        /* 1461 */ one.setCreator((int) tdo.creator);
        /* 1462 */ one.setDoe(tdo.doe);
        /* 1463 */ one.setDoetz(tdo.doetz);
        /* 1464 */ one.setEditor(Integer.valueOf((int) tdo.editor));
        /* 1465 */ one.setDom(tdo.dom);
        /* 1466 */ one.setDomtz(tdo.domtz);
        /* 1467 */ one.setLsuid(tdo.lsuid);
        /* 1468 */ one.setDeptName(tdo.dept);
        /* 1469 */ one.setEmail(tdo.email);
        /*      */
 /* 1471 */ one.setDos(tdo.dos);
        /* 1472 */ one.setDostz(tdo.dostz);
        /* 1473 */ one.setStdInd(tdo.stdInd);
        /* 1474 */ return one;
        /*      */    }

    /*      */
 /*      */
 /*      */ private TDOPrint genTDOPrintFromClientPrt(Print one) {
        /* 1479 */ TDOPrint tdo = new TDOPrint();
        /*      */
 /* 1481 */ tdo.dou = one.getDou();
        /* 1482 */ tdo.douTz = one.getDouTz();
        /*      */
 /* 1484 */ tdo.dod = one.getDod();
        /* 1485 */ tdo.dodTz = one.getDodTz();
        /*      */
 /* 1487 */ tdo.calGrantA = one.getCalGrantA();
        /* 1488 */ tdo.calGrantB = one.getCalGrantB();
        /* 1489 */ tdo.clientId = one.getClientId();
        /* 1490 */ tdo.coa = one.getCoa();
        /* 1491 */ tdo.counselorId = one.getCounselorId();
        /* 1492 */ tdo.counselorName = one.getCounselorName();
        /*      */
 /* 1494 */ tdo.due = one.getDue();
        /* 1495 */ tdo.earnings = one.getEarnings();
        /* 1496 */ tdo.efc = one.getEfc();
        /* 1497 */ tdo.familyDisct = one.getFamilyDisct();
        /* 1498 */ tdo.fisy = one.getFisy();
        /* 1499 */ tdo.fisyPrt = one.getFisyPrt();
        /* 1500 */ tdo.fws = one.getFws();
        /* 1501 */ tdo.lsuEa = one.getLsuEa();
        /* 1502 */ tdo.lsuPerf = one.getLsuPerf();
        /* 1503 */ tdo.lsuSship = one.getLsuSship();
        /* 1504 */ tdo.maxNeed = one.getMaxNeed();
        /* 1505 */ tdo.monthOpt = one.getMonthOpt();
        /* 1506 */ tdo.natlMerit = one.getNatlMerit();
        /* 1507 */ tdo.nonLsuEa = one.getNonLsuEa();
        /* 1508 */ tdo.nonLsuSship = one.getNonLsuSship();
        /* 1509 */ tdo.noncalGrant = one.getNoncalGrant();
        /* 1510 */ tdo.otherExpenses = one.getOtherExpenses();
        /* 1511 */ tdo.pell = one.getPell();
        /* 1512 */ tdo.perkins = one.getPerkins();
        /* 1513 */ tdo.prtId = one.getPrtId();
        /* 1514 */ tdo.prtNum = one.getPrtNum();
        /* 1515 */ tdo.prtTime = one.getPrtTime();
        /* 1516 */ tdo.prtTz = one.getPrtTz();
        /* 1517 */ tdo.pseog = one.getPseog();
        /* 1518 */ tdo.quarterOpt = one.getQuarterOpt();
        /* 1519 */ tdo.recid = one.getRecid();
        /* 1520 */ tdo.roomBoard = one.getRoomBoard();
        /* 1521 */ tdo.sda = one.getSda();
        /* 1522 */ tdo.sship1ExtAmt = one.getSship1ExtAmt();
        /* 1523 */ tdo.sship1LsuAmt = one.getSship1LsuAmt();
        /* 1524 */ tdo.sship1Name = one.getSship1Name();
        /* 1525 */ tdo.sship2ExtAmt = one.getSship2ExtAmt();
        /* 1526 */ tdo.sship2LsuAmt = one.getSship2LsuAmt();
        /* 1527 */ tdo.sship2Name = one.getSship2Name();
        /* 1528 */ tdo.sship3ExtAmt = one.getSship3ExtAmt();
        /* 1529 */ tdo.sship3LsuAmt = one.getSship3LsuAmt();
        /* 1530 */ tdo.sship3Name = one.getSship3Name();
        /* 1531 */ tdo.sship4ExtAmt = one.getSship4ExtAmt();
        /* 1532 */ tdo.sship4LsuAmt = one.getSship4LsuAmt();
        /* 1533 */ tdo.sship4Name = one.getSship4Name();
        /* 1534 */ tdo.sship5ExtAmt = one.getSship5ExtAmt();
        /* 1535 */ tdo.sship5LsuAmt = one.getSship5LsuAmt();
        /* 1536 */ tdo.sship5Name = one.getSship5Name();
        /* 1537 */ tdo.sship6ExtAmt = one.getSship6ExtAmt();
        /* 1538 */ tdo.sship6LsuAmt = one.getSship6LsuAmt();
        /* 1539 */ tdo.sship6Name = one.getSship6Name();
        /* 1540 */ tdo.sship7ExtAmt = one.getSship7ExtAmt();
        /* 1541 */ tdo.sship7LsuAmt = one.getSship7LsuAmt();
        /* 1542 */ tdo.sship7Name = one.getSship7Name();
        /* 1543 */ tdo.sship8ExtAmt = one.getSship8ExtAmt();
        /* 1544 */ tdo.sship8LsuAmt = one.getSship8LsuAmt();
        /* 1545 */ tdo.sship8Name = one.getSship8Name();
        /* 1546 */ tdo.sship9ExtAmt = one.getSship9ExtAmt();
        /* 1547 */ tdo.sship9LsuAmt = one.getSship9LsuAmt();
        /* 1548 */ tdo.sship9Name = one.getSship9Name();
        /*      */
 /* 1550 */ tdo.needGrant = one.getNeedGrant();
        /* 1551 */ tdo.achieveAward = one.getAchieveAward();
        /* 1552 */ tdo.renew4y = one.getRenew4y();
        /*      */
 /* 1554 */ tdo.lsugrant = one.getLsugrant();
        /*      */
 /* 1556 */ tdo.subloan = one.getSubloan();
        /* 1557 */ tdo.totAid = one.getTotAid();
        /* 1558 */ tdo.totAidWoWork = one.getTotAidWoWork();
        /* 1559 */ tdo.totCharges = one.getTotCharges();
        /* 1560 */ tdo.totLoan = one.getTotLoan();
        /* 1561 */ tdo.tuitionFee = one.getTuitionFee();
        /* 1562 */ tdo.unsubloan = one.getUnsubloan();
        /* 1563 */ tdo.versions = one.getVersions();
        /* 1564 */ tdo.yearOpt = one.getYearOpt();
        /* 1565 */ return tdo;
        /*      */    }

    /*      */
 /*      */ private Print genClientPrintFromTdo(TDOPrint tdo) {
        /* 1569 */ Print one = new Print();
        /* 1570 */ one.setDod(tdo.dod);
        /* 1571 */ one.setDodTz(tdo.dodTz);
        /*      */
 /* 1573 */ one.setDou(tdo.dou);
        /* 1574 */ one.setDouTz(tdo.douTz);
        /* 1575 */ one.setCalGrantA(tdo.calGrantA);
        /* 1576 */ one.setCalGrantB(tdo.calGrantB);
        /* 1577 */ one.setClientId(tdo.clientId);
        /* 1578 */ one.setCoa(tdo.coa);
        /* 1579 */ one.setCounselorId(tdo.counselorId);
        /* 1580 */ one.setCounselorName(tdo.counselorName);
        /*      */
 /* 1582 */ one.setDue(tdo.due);
        /* 1583 */ one.setEarnings(tdo.earnings);
        /* 1584 */ one.setEfc(tdo.efc);
        /* 1585 */ one.setFamilyDisct(tdo.familyDisct);
        /* 1586 */ one.setFisy(tdo.fisy);
        /* 1587 */ one.setFisyPrt(tdo.fisyPrt);
        /* 1588 */ one.setFws(tdo.fws);
        /* 1589 */ one.setLsuEa(tdo.lsuEa);
        /* 1590 */ one.setLsuPerf(tdo.lsuPerf);
        /* 1591 */ one.setLsuSship(tdo.lsuSship);
        /* 1592 */ one.setMaxNeed(tdo.maxNeed);
        /* 1593 */ one.setMonthOpt(tdo.monthOpt);
        /* 1594 */ one.setNatlMerit(tdo.natlMerit);
        /* 1595 */ one.setNonLsuEa(tdo.nonLsuEa);
        /* 1596 */ one.setNonLsuSship(tdo.nonLsuSship);
        /* 1597 */ one.setNoncalGrant(tdo.noncalGrant);
        /* 1598 */ one.setOtherExpenses(tdo.otherExpenses);
        /* 1599 */ one.setPell(tdo.pell);
        /* 1600 */ one.setPerkins(tdo.perkins);
        /* 1601 */ one.setPrtId(tdo.prtId);
        /* 1602 */ one.setPrtNum(tdo.prtNum);
        /* 1603 */ one.setPrtTime(tdo.prtTime);
        /* 1604 */ one.setPrtTz(tdo.prtTz);
        /* 1605 */ one.setPseog(tdo.pseog);
        /* 1606 */ one.setQuarterOpt(tdo.quarterOpt);
        /* 1607 */ one.setRecid(tdo.recid);
        /* 1608 */ one.setRoomBoard(tdo.roomBoard);
        /* 1609 */ one.setSda(tdo.sda);
        /* 1610 */ one.setSship1ExtAmt(tdo.sship1ExtAmt);
        /* 1611 */ one.setSship1LsuAmt(tdo.sship1LsuAmt);
        /* 1612 */ one.setSship1Name(tdo.sship1Name);
        /* 1613 */ one.setSship2ExtAmt(tdo.sship2ExtAmt);
        /* 1614 */ one.setSship2LsuAmt(tdo.sship2LsuAmt);
        /* 1615 */ one.setSship2Name(tdo.sship2Name);
        /* 1616 */ one.setSship3ExtAmt(tdo.sship3ExtAmt);
        /* 1617 */ one.setSship3LsuAmt(tdo.sship3LsuAmt);
        /* 1618 */ one.setSship3Name(tdo.sship3Name);
        /* 1619 */ one.setSship4ExtAmt(tdo.sship4ExtAmt);
        /* 1620 */ one.setSship4LsuAmt(tdo.sship4LsuAmt);
        /* 1621 */ one.setSship4Name(tdo.sship4Name);
        /* 1622 */ one.setSship5ExtAmt(tdo.sship5ExtAmt);
        /* 1623 */ one.setSship5LsuAmt(tdo.sship5LsuAmt);
        /* 1624 */ one.setSship5Name(tdo.sship5Name);
        /* 1625 */ one.setSship6ExtAmt(tdo.sship6ExtAmt);
        /* 1626 */ one.setSship6LsuAmt(tdo.sship6LsuAmt);
        /* 1627 */ one.setSship6Name(tdo.sship6Name);
        /* 1628 */ one.setSship7ExtAmt(tdo.sship7ExtAmt);
        /* 1629 */ one.setSship7LsuAmt(tdo.sship7LsuAmt);
        /* 1630 */ one.setSship7Name(tdo.sship7Name);
        /* 1631 */ one.setSship8ExtAmt(tdo.sship8ExtAmt);
        /* 1632 */ one.setSship8LsuAmt(tdo.sship8LsuAmt);
        /* 1633 */ one.setSship8Name(tdo.sship8Name);
        /* 1634 */ one.setSship9ExtAmt(tdo.sship9ExtAmt);
        /* 1635 */ one.setSship9LsuAmt(tdo.sship9LsuAmt);
        /* 1636 */ one.setSship9Name(tdo.sship9Name);
        /*      */
 /*      */
 /* 1639 */ one.setNeedGrant(tdo.needGrant);
        /* 1640 */ one.setAchieveAward(tdo.achieveAward);
        /* 1641 */ one.setRenew4y(tdo.renew4y);
        /*      */
 /* 1643 */ one.setLsugrant(tdo.lsugrant);
        /*      */
 /* 1645 */ one.setSubloan(tdo.subloan);
        /* 1646 */ one.setTotAid(tdo.totAid);
        /* 1647 */ one.setTotAidWoWork(tdo.totAidWoWork);
        /* 1648 */ one.setTotCharges(tdo.totCharges);
        /* 1649 */ one.setTotLoan(tdo.totLoan);
        /* 1650 */ one.setTuitionFee(tdo.tuitionFee);
        /* 1651 */ one.setUnsubloan(tdo.unsubloan);
        /* 1652 */ one.setVersions(tdo.versions);
        /* 1653 */ one.setYearOpt(tdo.yearOpt);
        /*      */
 /*      */
 /* 1656 */ return one;
        /*      */    }

    /*      */
 /*      */
 /*      */ private TDOStudent genTDOStudFromClientStud(Student one) {
        /* 1661 */ TDOStudent tdo = new TDOStudent();
        /* 1662 */ tdo.dup = one.getDup();
        /* 1663 */ tdo.tzup = one.getTzup();
        /*      */
 /* 1665 */ tdo.ddown = one.getDdown();
        /* 1666 */ tdo.tzdown = one.getTzdown();
        /*      */
 /* 1668 */ tdo.recid = one.getRecid();
        /* 1669 */ tdo.clientId = one.getClientId();
        /* 1670 */ tdo.counselorId = one.getCounselorId();
        /* 1671 */ tdo.studentNumb = one.getStudentNumb();
        /* 1672 */ tdo.studentUserName = one.getStudentUserName();
        /* 1673 */ tdo.studentALsuid = one.getStudentALsuid();
        /* 1674 */ tdo.studentBLastname = one.getStudentBLastname();
        /* 1675 */ tdo.studentCFirstname = one.getStudentCFirstname();
        /* 1676 */ tdo.studentDDob = one.getStudentDDob();
        /* 1677 */ tdo.pickupInd = one.getPickupInd();
        /*      */
 /* 1679 */ tdo.sex = one.getSex();
        /* 1680 */ tdo.counselorMod = one.getCounselorMod();
        /* 1681 */ tdo.counselorOrig = one.getCounselorOrig();
        /* 1682 */ tdo.estmNumb = one.getEstmNumb();
        /* 1683 */ tdo.fund1id = one.getFund1id();
        /* 1684 */ tdo.fund2id = one.getFund2id();
        /* 1685 */ tdo.fund3id = one.getFund3id();
        /* 1686 */ tdo.fund4id = one.getFund4id();
        /* 1687 */ tdo.fund5id = one.getFund5id();
        /* 1688 */ tdo.fund6id = one.getFund6id();
        /* 1689 */ tdo.fund7id = one.getFund7id();
        /* 1690 */ tdo.fund8id = one.getFund8id();
        /* 1691 */ tdo.fund9id = one.getFund9id();
        /* 1692 */ tdo.homeAddrApt = one.getHomeAddrApt();
        /* 1693 */ tdo.homecostudies = one.getHomecostudies();
        /* 1694 */ tdo.indEalsu = one.getIndEalsu();
        /* 1695 */ tdo.indEanonlsu = one.getIndEanonlsu();
        /* 1696 */ tdo.indEfc = one.getIndEfc();
        /* 1697 */ tdo.indExcloans = one.getIndExcloans();
        /*      */
 /* 1699 */ tdo.adjCalgrantInd = one.getAdjCalgrantInd();
        /*      */
 /* 1701 */ tdo.studentAaCalgrantA = one.getStudentAaCalgrantA();
        /* 1702 */ tdo.studentAbCalgrantB = one.getStudentAbCalgrantB();
        /* 1703 */ tdo.studentAcFamilySize = one.getStudentAcFamilySize();
        /* 1704 */ tdo.studentAdFamilyIncome = one.getStudentAdFamilyIncome();
        /* 1705 */ tdo.studentAeFamilyAsset = one.getStudentAeFamilyAsset();
        /* 1706 */ tdo.studentAfFamilyContrib = one.getStudentAfFamilyContrib();
        /* 1707 */ tdo.studentAgNonlsuAllowrance = one.getStudentAgNonlsuAllowrance();
        /* 1708 */ tdo.studentAiEduAllowPer = one.getStudentAiEduAllowPer();
        /* 1709 */ tdo.studentAjHomeState = one.getStudentAjHomeState();
        /* 1710 */ tdo.studentAkNoncalGrant = one.getStudentAkNoncalGrant();
        /* 1711 */ tdo.studentAlOutScholarships = one.getStudentAlOutScholarships();
        /* 1712 */ tdo.studentAmOutScholarshipAmt = one.getStudentAmOutScholarshipAmt();
        /* 1713 */ tdo.studentAnPubNotes = one.getStudentAnPubNotes();
        /* 1714 */ tdo.studentAoPriNotes = one.getStudentAoPriNotes();
        /* 1715 */ tdo.studentApSubLoans = one.getStudentApSubLoans();
        /* 1716 */ tdo.studentAqUnsubLoans = one.getStudentAqUnsubLoans();
        /* 1717 */ tdo.studentArFws = one.getStudentArFws();
        /* 1718 */ tdo.studentAsScholarship1Name = one.getStudentAsScholarship1Name();
        /* 1719 */ tdo.studentAtScholarship1Note = one.getStudentAtScholarship1Note();
        /* 1720 */ tdo.studentAuScholarship1Amt = one.getStudentAuScholarship1Amt();
        /* 1721 */ tdo.studentAvScholarship2Name = one.getStudentAvScholarship2Name();
        /* 1722 */ tdo.studentAwScholarship2Note = one.getStudentAwScholarship2Note();
        /* 1723 */ tdo.studentAxScholarship2Amt = one.getStudentAxScholarship2Amt();
        /* 1724 */ tdo.studentAyScholarship3Name = one.getStudentAyScholarship3Name();
        /* 1725 */ tdo.studentAzScholarship3Note = one.getStudentAzScholarship3Note();
        /* 1726 */ tdo.studentBaScholarship3Amt = one.getStudentBaScholarship3Amt();
        /* 1727 */ tdo.studentBbScholarship4Name = one.getStudentBbScholarship4Name();
        /* 1728 */ tdo.studentBcScholarship4Note = one.getStudentBcScholarship4Note();
        /* 1729 */ tdo.studentBdScholarship4Amt = one.getStudentBdScholarship4Amt();
        /* 1730 */ tdo.studentBeScholarship5Name = one.getStudentBeScholarship5Name();
        /* 1731 */ tdo.studentBfScholarship5Note = one.getStudentBfScholarship5Note();
        /* 1732 */ tdo.studentBgScholarship5Amt = one.getStudentBgScholarship5Amt();
        /* 1733 */ tdo.studentBhScholarship6Name = one.getStudentBhScholarship6Name();
        /* 1734 */ tdo.studentBiScholarship6Note = one.getStudentBiScholarship6Note();
        /* 1735 */ tdo.studentBjScholarship6Amt = one.getStudentBjScholarship6Amt();
        /* 1736 */ tdo.studentBkScholarship7Name = one.getStudentBkScholarship7Name();
        /* 1737 */ tdo.studentBlScholarship7Note = one.getStudentBlScholarship7Note();
        /* 1738 */ tdo.studentBmScholarship7Amt = one.getStudentBmScholarship7Amt();
        /* 1739 */ tdo.studentBnScholarship8Name = one.getStudentBnScholarship8Name();
        /* 1740 */ tdo.studentBoScholarship8Note = one.getStudentBoScholarship8Note();
        /* 1741 */ tdo.studentBpScholarship8Amt = one.getStudentBpScholarship8Amt();
        /* 1742 */ tdo.studentBqScholarship9Name = one.getStudentBqScholarship9Name();
        /* 1743 */ tdo.studentBrScholarship9Note = one.getStudentBrScholarship9Note();
        /* 1744 */ tdo.studentBsScholarship9Amt = one.getStudentBsScholarship9Amt();
        /*      */
 /* 1746 */ tdo.studentBtSupercounselor = one.getStudentBtSupercounselor();
        /*      */
 /* 1748 */ tdo.studentBwProgress = one.getStudentBwProgress();
        /* 1749 */ tdo.studentCbBanner = one.getStudentCbBanner();
        /*      */
 /* 1751 */ tdo.studentEEmail = one.getStudentEEmail();
        /* 1752 */ tdo.studentFPhone = one.getStudentFPhone();
        /* 1753 */ tdo.studentFisy = one.getStudentFisy();
        /* 1754 */ tdo.studentGStreet = one.getStudentGStreet();
        /* 1755 */ tdo.studentHCity = one.getStudentHCity();
        /* 1756 */ tdo.studentIState = one.getStudentIState();
        /* 1757 */ tdo.studentJZip = one.getStudentJZip();
        /* 1758 */ tdo.studentKCountry = one.getStudentKCountry();
        /* 1759 */ tdo.studentLIntlStud = one.getStudentLIntlStud();
        /* 1760 */ tdo.studentMMarry = one.getStudentMMarry();
        /* 1761 */ tdo.studentNSda = one.getStudentNSda();
        /*      */
 /* 1763 */ tdo.studentOLastSchool = one.getStudentOLastSchool();
        /* 1764 */ tdo.studentPGpa = one.getStudentPGpa();
        /* 1765 */ tdo.studentPassword = one.getStudentPassword();
        /* 1766 */ tdo.studentQSat = one.getStudentQSat();
        /* 1767 */ tdo.studentQSatV = one.getStudentQSatV();
        /* 1768 */ tdo.studentRAct = one.getStudentRAct();
        /* 1769 */ tdo.studentSMerit = one.getStudentSMerit();
        /* 1770 */ tdo.studentStudType = one.getStudentStudType();
        /* 1771 */ tdo.studentTMajor = one.getStudentTMajor();
        /* 1772 */ tdo.studentTermEnd = one.getStudentTermEnd();
        /* 1773 */ tdo.studentTermStart = one.getStudentTermStart();
        /* 1774 */ tdo.studentUAcademic = one.getStudentUAcademic();
        /* 1775 */ tdo.studentVFamily = one.getStudentVFamily();
        /* 1776 */ tdo.studentWDorm = one.getStudentWDorm();
        /* 1777 */ tdo.studentXFafsa = one.getStudentXFafsa();
        /* 1778 */ tdo.studentYIndept = one.getStudentYIndept();
        /* 1779 */ tdo.studentZCalgrant = one.getStudentZCalgrant();
        /*      */
 /* 1781 */ tdo.lostTime = one.getLostTime();
        /* 1782 */ tdo.lostToLocal = one.getLostToLocal();
        /* 1783 */ tdo.lostTz = one.getLostTz();
        /* 1784 */ tdo.studentBuOrigCounselor = String.valueOf(one.getCounselorOrig());
        /* 1785 */ tdo.studentBvDoe = new Date(one.getDdoe());
        /* 1786 */ tdo.studentBxModCounselor = String.valueOf(one.getCounselorMod());
        /* 1787 */ tdo.studentByDom = new Date(one.getDdom());
        /* 1788 */ tdo.studentBzUploaded = one.getStudentBzUploaded();
        /*      */
 /* 1790 */ tdo.modPre = one.getModPre();
        /* 1791 */ tdo.modRoot = one.getModRoot();
        /*      */
 /* 1793 */ tdo.ddoe = one.getDdoe();
        /* 1794 */ tdo.ddom = one.getDdom();
        /*      */
 /* 1796 */ tdo.tzdoe = one.getTzdoe();
        /* 1797 */ tdo.tzdom = one.getTzdom();
        /*      */
 /* 1799 */ tdo.prtTimes = one.getPrtTimes();
        /*      */
 /* 1801 */ tdo.did = one.getDid();
        /* 1802 */ tdo.didstr = one.getDidstr();
        /* 1803 */ tdo.tzdid = one.getTzdid();
        /*      */
 /* 1805 */ tdo.returnStdInd = one.getReturnStdInd();
        /* 1806 */ tdo.ncStdInd = one.getNcStdInd();
        /*      */
 /* 1808 */ tdo.terms = one.getTerms();
        /* 1809 */ tdo.term_code1 = one.getTerm_code1();
        /* 1810 */ tdo.term_code2 = one.getTerm_code2();
        /* 1811 */ tdo.term_code3 = one.getTerm_code3();
        /* 1812 */ tdo.term_code4 = one.getTerm_code4();
        /* 1813 */ tdo.term_load1 = one.getTerm_load1();
        /* 1814 */ tdo.term_load2 = one.getTerm_load2();
        /* 1815 */ tdo.term_load3 = one.getTerm_load3();
        /* 1816 */ tdo.term_load4 = one.getTerm_load4();
        /* 1817 */ tdo.term_prog1 = one.getTerm_prog1();
        /* 1818 */ tdo.term_prog2 = one.getTerm_prog2();
        /* 1819 */ tdo.term_prog3 = one.getTerm_prog3();
        /* 1820 */ tdo.term_prog4 = one.getTerm_prog4();
        /* 1821 */ tdo.term_unit1 = one.getTerm_unit1();
        /* 1822 */ tdo.term_unit2 = one.getTerm_unit2();
        /* 1823 */ tdo.term_unit3 = one.getTerm_unit3();
        /* 1824 */ tdo.term_unit4 = one.getTerm_unit4();
        /* 1825 */ tdo.puser_id = one.getPuser_id();
        /* 1826 */ tdo.ea_lsu_perc = one.getEa_lsu_perc();
        /* 1827 */ tdo.ea_nonlsu_perc = one.getEa_nonlsu_perc();
        /* 1828 */ tdo.std_1st_freshmen = one.getStd_1st_freshmen();
        /* 1829 */ tdo.std_transfer_ind = one.getStd_transfer_ind();
        /*      */
 /*      */
 /* 1832 */ return tdo;
        /*      */    }

    /*      */ private Student genClientStudFromTdo(TDOStudent tdo) {
        /* 1835 */ Student one = new Student();
        /* 1836 */ one.setDdown(tdo.ddown);
        /* 1837 */ one.setTzdown(tdo.tzdown);
        /*      */
 /* 1839 */ one.setDup(tdo.dup);
        /* 1840 */ one.setTzup(tdo.tzup);
        /*      */
 /* 1842 */ one.setRecid(tdo.recid);
        /* 1843 */ one.setClientId(tdo.clientId);
        /* 1844 */ one.setCounselorId(tdo.counselorId);
        /*      */
 /* 1846 */ one.setPickupInd(tdo.pickupInd);
        /*      */
 /* 1848 */ one.setSex(tdo.sex);
        /* 1849 */ if (tdo.counselorMod != null) {
            one.setCounselorMod(tdo.counselorMod);
        }
        /* 1850 */ one.setCounselorOrig(tdo.counselorOrig);
        /* 1851 */ one.setEstmNumb(tdo.estmNumb);
        /*      */
 /* 1853 */ one.setHomeAddrApt(tdo.homeAddrApt);
        /* 1854 */ one.setHomecostudies(tdo.homecostudies);
        /* 1855 */ one.setIndEalsu(tdo.indEalsu);
        /* 1856 */ one.setIndEanonlsu(tdo.indEanonlsu);
        /* 1857 */ one.setIndEfc(tdo.indEfc);
        /* 1858 */ one.setIndExcloans(tdo.indExcloans);
        /*      */
 /* 1860 */ one.setStudentNumb(tdo.studentNumb);
        /* 1861 */ one.setStudentUserName(tdo.studentUserName);
        /* 1862 */ one.setStudentALsuid(tdo.studentALsuid);
        /* 1863 */ one.setStudentBLastname(tdo.studentBLastname);
        /* 1864 */ one.setStudentCFirstname(tdo.studentCFirstname);
        /* 1865 */ one.setStudentDDob(tdo.studentDDob);
        /* 1866 */ one.setFund1id(tdo.fund1id);
        /* 1867 */ one.setFund2id(tdo.fund2id);
        /* 1868 */ one.setFund3id(tdo.fund3id);
        /* 1869 */ one.setFund4id(tdo.fund4id);
        /* 1870 */ one.setFund5id(tdo.fund5id);
        /* 1871 */ one.setFund6id(tdo.fund6id);
        /* 1872 */ one.setFund7id(tdo.fund7id);
        /* 1873 */ one.setFund8id(tdo.fund8id);
        /* 1874 */ one.setFund9id(tdo.fund9id);
        /*      */
 /* 1876 */ one.setAdjCalgrantInd(tdo.adjCalgrantInd);
        /*      */
 /* 1878 */ one.setStudentAaCalgrantA(tdo.studentAaCalgrantA);
        /* 1879 */ one.setStudentAbCalgrantB(tdo.studentAbCalgrantB);
        /* 1880 */ one.setStudentAcFamilySize(tdo.studentAcFamilySize);
        /* 1881 */ one.setStudentAdFamilyIncome(tdo.studentAdFamilyIncome);
        /* 1882 */ one.setStudentAeFamilyAsset(tdo.studentAeFamilyAsset);
        /* 1883 */ one.setStudentAfFamilyContrib(tdo.studentAfFamilyContrib);
        /* 1884 */ one.setStudentAgNonlsuAllowrance(tdo.studentAgNonlsuAllowrance);
        /* 1885 */ one.setStudentAiEduAllowPer(tdo.studentAiEduAllowPer);
        /* 1886 */ one.setStudentAjHomeState(tdo.studentAjHomeState);
        /* 1887 */ one.setStudentAkNoncalGrant(tdo.studentAkNoncalGrant);
        /* 1888 */ one.setStudentAlOutScholarships(tdo.studentAlOutScholarships);
        /* 1889 */ one.setStudentAmOutScholarshipAmt(tdo.studentAmOutScholarshipAmt);
        /* 1890 */ one.setStudentAnPubNotes(tdo.studentAnPubNotes);
        /* 1891 */ one.setStudentAoPriNotes(tdo.studentAoPriNotes);
        /* 1892 */ one.setStudentApSubLoans(tdo.studentApSubLoans);
        /* 1893 */ one.setStudentAqUnsubLoans(tdo.studentAqUnsubLoans);
        /* 1894 */ one.setStudentArFws(tdo.studentArFws);
        /* 1895 */ one.setStudentAsScholarship1Name(tdo.studentAsScholarship1Name);
        /* 1896 */ one.setStudentAtScholarship1Note(tdo.studentAtScholarship1Note);
        /* 1897 */ one.setStudentAuScholarship1Amt(tdo.studentAuScholarship1Amt);
        /* 1898 */ one.setStudentAvScholarship2Name(tdo.studentAvScholarship2Name);
        /* 1899 */ one.setStudentAwScholarship2Note(tdo.studentAwScholarship2Note);
        /* 1900 */ one.setStudentAxScholarship2Amt(tdo.studentAxScholarship2Amt);
        /* 1901 */ one.setStudentAyScholarship3Name(tdo.studentAyScholarship3Name);
        /* 1902 */ one.setStudentAzScholarship3Note(tdo.studentAzScholarship3Note);
        /* 1903 */ one.setStudentBaScholarship3Amt(tdo.studentBaScholarship3Amt);
        /* 1904 */ one.setStudentBbScholarship4Name(tdo.studentBbScholarship4Name);
        /* 1905 */ one.setStudentBcScholarship4Note(tdo.studentBcScholarship4Note);
        /* 1906 */ one.setStudentBdScholarship4Amt(tdo.studentBdScholarship4Amt);
        /* 1907 */ one.setStudentBeScholarship5Name(tdo.studentBeScholarship5Name);
        /* 1908 */ one.setStudentBfScholarship5Note(tdo.studentBfScholarship5Note);
        /* 1909 */ one.setStudentBgScholarship5Amt(tdo.studentBgScholarship5Amt);
        /* 1910 */ one.setStudentBhScholarship6Name(tdo.studentBhScholarship6Name);
        /* 1911 */ one.setStudentBiScholarship6Note(tdo.studentBiScholarship6Note);
        /* 1912 */ one.setStudentBjScholarship6Amt(tdo.studentBjScholarship6Amt);
        /* 1913 */ one.setStudentBkScholarship7Name(tdo.studentBkScholarship7Name);
        /* 1914 */ one.setStudentBlScholarship7Note(tdo.studentBlScholarship7Note);
        /* 1915 */ one.setStudentBmScholarship7Amt(tdo.studentBmScholarship7Amt);
        /* 1916 */ one.setStudentBnScholarship8Name(tdo.studentBnScholarship8Name);
        /* 1917 */ one.setStudentBoScholarship8Note(tdo.studentBoScholarship8Note);
        /* 1918 */ one.setStudentBpScholarship8Amt(tdo.studentBpScholarship8Amt);
        /* 1919 */ one.setStudentBqScholarship9Name(tdo.studentBqScholarship9Name);
        /* 1920 */ one.setStudentBrScholarship9Note(tdo.studentBrScholarship9Note);
        /* 1921 */ one.setStudentBsScholarship9Amt(tdo.studentBsScholarship9Amt);
        /*      */
 /* 1923 */ one.setStudentBtSupercounselor(tdo.studentBtSupercounselor);
        /*      */
 /* 1925 */ one.setStudentBwProgress(tdo.studentBwProgress);
        /* 1926 */ one.setStudentCbBanner(tdo.studentCbBanner);
        /*      */
 /* 1928 */ one.setStudentEEmail(tdo.studentEEmail);
        /* 1929 */ one.setStudentFPhone(tdo.studentFPhone);
        /*      */
 /* 1931 */ one.setStudentGStreet(tdo.studentGStreet);
        /* 1932 */ one.setStudentHCity(tdo.studentHCity);
        /* 1933 */ one.setStudentIState(tdo.studentIState);
        /* 1934 */ one.setStudentJZip(tdo.studentJZip);
        /* 1935 */ one.setStudentKCountry(tdo.studentKCountry);
        /* 1936 */ one.setStudentLIntlStud(tdo.studentLIntlStud);
        /* 1937 */ one.setStudentMMarry(tdo.studentMMarry);
        /* 1938 */ one.setStudentNSda(tdo.studentNSda);
        /*      */
 /* 1940 */ one.setStudentOLastSchool(tdo.studentOLastSchool);
        /* 1941 */ one.setStudentPGpa(tdo.studentPGpa);
        /* 1942 */ one.setStudentPassword(tdo.studentPassword);
        /* 1943 */ one.setStudentQSat(tdo.studentQSat);
        /* 1944 */ one.setStudentQSatV(tdo.studentQSatV);
        /* 1945 */ one.setStudentRAct(tdo.studentRAct);
        /* 1946 */ one.setStudentSMerit(tdo.studentSMerit);
        /* 1947 */ one.setStudentStudType(tdo.studentStudType);
        /* 1948 */ one.setStudentTMajor(tdo.studentTMajor);
        /* 1949 */ one.setStudentTermEnd(tdo.studentTermEnd);
        /* 1950 */ one.setStudentTermStart(tdo.studentTermStart);
        /* 1951 */ one.setStudentUAcademic(tdo.studentUAcademic);
        /* 1952 */ one.setStudentVFamily(tdo.studentVFamily);
        /* 1953 */ one.setStudentWDorm(tdo.studentWDorm);
        /* 1954 */ one.setStudentXFafsa(tdo.studentXFafsa);
        /* 1955 */ one.setStudentYIndept(tdo.studentYIndept);
        /* 1956 */ one.setStudentZCalgrant(tdo.studentZCalgrant);
        /*      */
 /*      */
 /* 1959 */ one.setLostTime(tdo.lostTime);
        /* 1960 */ one.setLostToMaster(tdo.lostToMaster);
        /* 1961 */ one.setLostTz(tdo.lostTz);
        /*      */
 /* 1963 */ one.setStudentBzUploaded(tdo.studentBzUploaded);
        /* 1964 */ one.setModPre(tdo.modPre);
        /* 1965 */ one.setModRoot(tdo.modRoot);
        /*      */
 /* 1967 */ one.setDdoe(tdo.ddoe);
        /* 1968 */ one.setTzdoe(tdo.tzdoe);
        /* 1969 */ one.setDdom(tdo.ddom);
        /* 1970 */ one.setTzdom(tdo.tzdom);
        /*      */
 /* 1972 */ one.setPrtTimes(tdo.prtTimes);
        /*      */
 /* 1974 */ one.setCounselorOrig(tdo.counselorOrig);
        /* 1975 */ if (tdo.counselorMod != null) {
            one.setCounselorMod(tdo.counselorMod);
        }
        /*      */
 /* 1977 */ one.setDid(tdo.did);
        /* 1978 */ one.setDidstr(tdo.didstr);
        /* 1979 */ one.setTzdid(tdo.tzdid);
        /*      */
 /* 1981 */ one.setReturnStdInd(tdo.returnStdInd);
        /* 1982 */ one.setNcStdInd(tdo.ncStdInd);
        /*      */
 /* 1984 */ one.setTerms(tdo.terms);
        /* 1985 */ one.setTerm_code1(tdo.term_code1);
        /* 1986 */ one.setTerm_code2(tdo.term_code2);
        /* 1987 */ one.setTerm_code3(tdo.term_code3);
        /* 1988 */ one.setTerm_code4(tdo.term_code4);
        /* 1989 */ one.setTerm_load1(tdo.term_load1);
        /* 1990 */ one.setTerm_load2(tdo.term_load2);
        /* 1991 */ one.setTerm_load3(tdo.term_load3);
        /* 1992 */ one.setTerm_load4(tdo.term_load4);
        /* 1993 */ one.setTerm_prog1(tdo.term_prog1);
        /* 1994 */ one.setTerm_prog2(tdo.term_prog2);
        /* 1995 */ one.setTerm_prog3(tdo.term_prog3);
        /* 1996 */ one.setTerm_prog4(tdo.term_prog4);
        /* 1997 */ one.setTerm_unit1(tdo.term_unit1);
        /* 1998 */ one.setTerm_unit2(tdo.term_unit2);
        /* 1999 */ one.setTerm_unit3(tdo.term_unit3);
        /* 2000 */ one.setTerm_unit4(tdo.term_unit4);
        /* 2001 */ one.setPuser_id(tdo.puser_id);
        /* 2002 */ one.setEa_lsu_perc(tdo.ea_lsu_perc);
        /* 2003 */ one.setEa_nonlsu_perc(tdo.ea_nonlsu_perc);
        /* 2004 */ one.setStd_1st_freshmen(tdo.std_1st_freshmen);
        /* 2005 */ one.setStd_transfer_ind(tdo.std_transfer_ind);
        /*      */
 /* 2007 */ return one;
        /*      */    }

    /*      */
 /*      */ public String getShowBaseMsg() {
        /* 2011 */ if (this.ref.getMaster_up_ind() > 0) {
            /* 2012 */ return this.showBaseMsg + " | " + this.showSyncMsg;
            /*      */        }
        /* 2014 */ return this.ref.getJSFMsgByKey("MasterIsNotAvailable");
        /*      */    }
    /*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\SyncWorker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
