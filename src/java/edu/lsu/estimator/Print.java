/*      */ package edu.lsu.estimator;

/*      */
 /*      */ import edu.lsu.estimator.Student;
/*      */ import java.io.Serializable;
/*      */ import javax.persistence.Basic;
/*      */ import javax.persistence.Column;
/*      */ import javax.persistence.Entity;
/*      */ import javax.persistence.FetchType;
/*      */ import javax.persistence.GeneratedValue;
/*      */ import javax.persistence.GenerationType;
/*      */ import javax.persistence.Id;
/*      */ import javax.persistence.JoinColumn;
/*      */ import javax.persistence.ManyToOne;
/*      */ import javax.persistence.NamedQueries;
/*      */ import javax.persistence.NamedQuery;
/*      */ import javax.persistence.Table;
/*      */ import javax.persistence.TableGenerator;
/*      */ import javax.validation.constraints.Min;
/*      */ import javax.validation.constraints.NotNull;
/*      */ import javax.validation.constraints.Size;
/*      */ import javax.xml.bind.annotation.XmlRootElement;

/*      */ @Entity
/*      */ @Table(name = "PRINTS")
/*      */ @XmlRootElement
/*      */ @NamedQueries({
    @NamedQuery(name = "Print.findAll", query = "SELECT o FROM Print o"),
    @NamedQuery(name = "Print.findByPrtId", query = "SELECT o FROM Print o WHERE o.prtId = :prtId"),
    @NamedQuery(name = "Print.findByRecid", query = "SELECT o FROM Print o WHERE o.recid = :recid"),
    @NamedQuery(name = "Print.findByClientId", query = "SELECT o FROM Print o WHERE o.clientId = :clientId"),
    @NamedQuery(name = "Print.findByCounselorId", query = "SELECT o FROM Print o WHERE o.counselorId = :counselorId"),
    @NamedQuery(name = "Print.findByCounselorName", query = "SELECT o FROM Print o WHERE o.counselorName = :counselorName"),
    @NamedQuery(name = "Print.findByPrtNum", query = "SELECT o FROM Print o WHERE o.prtNum = :prtNum"),
    @NamedQuery(name = "Print.findByPrtTime", query = "SELECT o FROM Print o WHERE o.prtTime = :prtTime"),
    @NamedQuery(name = "Print.findByPrtTz", query = "SELECT o FROM Print o WHERE o.prtTz = :prtTz"),
    @NamedQuery(name = "Print.findByTuitionFee", query = "SELECT o FROM Print o WHERE o.tuitionFee = :tuitionFee"),
    @NamedQuery(name = "Print.findByRoomBoard", query = "SELECT o FROM Print o WHERE o.roomBoard = :roomBoard"),
    @NamedQuery(name = "Print.findByDue", query = "SELECT o FROM Print o WHERE o.due = :due"),
    @NamedQuery(name = "Print.findByYearOpt", query = "SELECT o FROM Print o WHERE o.yearOpt = :yearOpt"),
    @NamedQuery(name = "Print.findByQuarterOpt", query = "SELECT o FROM Print o WHERE o.quarterOpt = :quarterOpt"),
    @NamedQuery(name = "Print.findByMonthOpt", query = "SELECT o FROM Print o WHERE o.monthOpt = :monthOpt"),
    @NamedQuery(name = "Print.findByTotCharges", query = "SELECT o FROM Print o WHERE o.totCharges = :totCharges"),
    @NamedQuery(name = "Print.findByTotAid", query = "SELECT o FROM Print o WHERE o.totAid = :totAid"),
    @NamedQuery(name = "Print.findByTotAidWoWork", query = "SELECT o FROM Print o WHERE o.totAidWoWork = :totAidWoWork"),
    @NamedQuery(name = "Print.findByFws", query = "SELECT o FROM Print o WHERE o.fws = :fws"),
    @NamedQuery(name = "Print.findByOtherExpenses", query = "SELECT o FROM Print o WHERE o.otherExpenses = :otherExpenses"),
    @NamedQuery(name = "Print.findByEfc", query = "SELECT o FROM Print o WHERE o.efc = :efc"),
    @NamedQuery(name = "Print.findByMaxNeed", query = "SELECT o FROM Print o WHERE o.maxNeed = :maxNeed"),
    @NamedQuery(name = "Print.findByCoa", query = "SELECT o FROM Print o WHERE o.coa = :coa"),
    @NamedQuery(name = "Print.findByPell", query = "SELECT o FROM Print o WHERE o.pell = :pell"),
    @NamedQuery(name = "Print.findByNoncalGrant", query = "SELECT o FROM Print o WHERE o.noncalGrant = :noncalGrant"),
    @NamedQuery(name = "Print.findByPseog", query = "SELECT o FROM Print o WHERE o.pseog = :pseog"),
    @NamedQuery(name = "Print.findByNonLsuEa", query = "SELECT o FROM Print o WHERE o.nonLsuEa = :nonLsuEa"),
    @NamedQuery(name = "Print.findByNonLsuSship", query = "SELECT o FROM Print o WHERE o.nonLsuSship = :nonLsuSship"),
    @NamedQuery(name = "Print.findByLsuEa", query = "SELECT o FROM Print o WHERE o.lsuEa = :lsuEa"),
    @NamedQuery(name = "Print.findByLsuSship", query = "SELECT o FROM Print o WHERE o.lsuSship = :lsuSship"),
    @NamedQuery(name = "Print.findBySda", query = "SELECT o FROM Print o WHERE o.sda = :sda"),
    @NamedQuery(name = "Print.findByFamilyDisct", query = "SELECT o FROM Print o WHERE o.familyDisct = :familyDisct"),
    @NamedQuery(name = "Print.findByNatlMerit", query = "SELECT o FROM Print o WHERE o.natlMerit = :natlMerit"),
    @NamedQuery(name = "Print.findByPerkins", query = "SELECT o FROM Print o WHERE o.perkins = :perkins"),
    @NamedQuery(name = "Print.findByClientIdNupNdownFisy", query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime>0"),
    @NamedQuery(name = "Print.findByClientIdNupNdownFisyNonPrt", query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime=0 and o.recid=:recid"),
    @NamedQuery(name = "Print.findBySubloan", query = "SELECT o FROM Print o WHERE o.subloan = :subloan"),
    @NamedQuery(name = "Print.findByUnsubloan", query = "SELECT o FROM Print o WHERE o.unsubloan = :unsubloan"),
    @NamedQuery(name = "Print.findByTotLoan", query = "SELECT o FROM Print o WHERE o.totLoan = :totLoan")})
/*      */ public class Print
        /*      */ implements Serializable /*      */ {

    /*      */ private static final long serialVersionUID = 1L;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "PRT_NUM")
    /*      */    @TableGenerator(name = "PRTSEQ_Gen", table = "SEQUENCE", pkColumnName = "NAME", valueColumnName = "ID", pkColumnValue = "PRT_GEN", initialValue = 1, allocationSize = 1)
    /*      */    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRTSEQ_Gen")
    /*      */ private Integer prtNum;
    /*      */    @Id
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 50)
    /*      */    @Column(name = "PRT_ID")
    /*      */ private String prtId;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 100)
    /*      */    @Column(name = "RECID")
    /*      */ private String recid;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "CLIENT_ID")
    /*      */ private int clientId;
    /*      */    @Column(name = "COUNSELOR_ID")
    /*      */ private Integer counselorId;
    /*      */    @Size(max = 100)
    /*      */    @Column(name = "COUNSELOR_NAME")
    /*      */ private String counselorName;
    /*      */    @ManyToOne(optional = false, targetEntity = Student.class, fetch = FetchType.LAZY)
    /*      */    @JoinColumn(name = "RECID", referencedColumnName = "RECID", nullable = true, insertable = false, updatable = false)
    /*      */ private Student stud;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "PRT_TIME")
    /*      */ private long prtTime;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 20)
    /*      */    @Column(name = "PRT_TZ")
    /*      */ private String prtTz;
    /*      */    @Column(name = "TUITION_FEE")
    /*      */ private Integer tuitionFee;
    /*      */    @Column(name = "ROOM_BOARD")
    /*      */ private Integer roomBoard;
    /*      */    @Column(name = "DUE")
    /*      */ private Integer due;
    /*      */    @Column(name = "YEAR_OPT")
    /*      */ private Integer yearOpt;
    /*      */    @Column(name = "QUARTER_OPT")
    /*      */ private Integer quarterOpt;
    /*      */    @Column(name = "MONTH_OPT")
    /*      */ private Integer monthOpt;
    /*      */    @Column(name = "TOT_CHARGES")
    /*      */ private Integer totCharges;
    /*      */    @Column(name = "TOT_AID")
    /*      */ private Integer totAid;
    /*      */    @Column(name = "TOT_AID_WO_WORK")
    /*      */ private Integer totAidWoWork;
    /*      */    @Column(name = "FWS")
    /*      */ private Integer fws;
    /*      */    @Column(name = "OTHER_EXPENSES")
    /*      */ private Integer otherExpenses;
    /*      */    @Column(name = "EFC")
    /*      */ private Integer efc;
    /*      */    @Column(name = "MAX_NEED")
    /*      */ private Integer maxNeed;
    /*      */    @Column(name = "COA")
    /*      */ private Integer coa;
    /*      */    @Column(name = "PELL")
    /*      */ private Integer pell;
    /*      */    @Column(name = "CAL_GRANTA")
    /*      */ private Integer calGrantA;
    /*      */    @Column(name = "CAL_GRANTB")
    /*      */ private Integer calGrantB;
    /*      */    @Column(name = "NONCAL_GRANT")
    /*      */ private Integer noncalGrant;
    /*      */    @Column(name = "PSEOG")
    /*      */ private Integer pseog;
    /*      */    @Column(name = "NON_LSU_EA")
    /*      */ private Integer nonLsuEa;
    /*      */    @Column(name = "NON_LSU_SSHIP")
    /*      */ private Integer nonLsuSship;
    /*      */    @Column(name = "LSU_EA")
    /*      */ private Integer lsuEa;
    /*      */    @Column(name = "LSU_SSHIP")
    /*      */ private Integer lsuSship;
    /*      */    @Column(name = "SDA")
    /*      */ private Integer sda;
    /*      */    @Column(name = "FAMILY_DISCT")
    /*      */ private Integer familyDisct;
    /*      */    @Column(name = "NATL_MERIT")
    /*      */ private Integer natlMerit;
    /*      */    @Column(name = "PERKINS")
    /*      */ private Integer perkins;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP1_NAME")
    /*      */ private String sship1Name;
    /*      */    @Column(name = "SSHIP1_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship1LsuAmt;
    /*      */    @Column(name = "SSHIP1_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship1ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP2_NAME")
    /*      */ private String sship2Name;
    /*      */    @Column(name = "SSHIP2_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship2LsuAmt;
    /*      */    @Column(name = "SSHIP2_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship2ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP3_NAME")
    /*      */ private String sship3Name;
    /*      */    @Column(name = "SSHIP3_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship3LsuAmt;
    /*      */    @Column(name = "SSHIP3_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship3ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP4_NAME")
    /*      */ private String sship4Name;
    /*      */    @Column(name = "SSHIP4_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship4LsuAmt;
    /*      */    @Column(name = "SSHIP4_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship4ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP5_NAME")
    /*      */ private String sship5Name;
    /*      */    @Column(name = "SSHIP5_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship5LsuAmt;
    /*      */    @Column(name = "SSHIP5_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship5ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP6_NAME")
    /*      */ private String sship6Name;
    /*      */    @Column(name = "SSHIP6_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship6LsuAmt;
    /*      */    @Column(name = "SSHIP6_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship6ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP7_NAME")
    /*      */ private String sship7Name;
    /*      */    @Column(name = "SSHIP7_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship7LsuAmt;
    /*      */    @Column(name = "SSHIP7_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship7ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP8_NAME")
    /*      */ private String sship8Name;
    /*      */    @Column(name = "SSHIP8_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship8LsuAmt;
    /*      */    @Column(name = "SSHIP8_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship8ExtAmt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "SSHIP9_NAME")
    /*      */ private String sship9Name;
    /*      */    @Column(name = "SSHIP9_LSU_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship9LsuAmt;
    /*      */    @Column(name = "SSHIP9_EXT_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer sship9ExtAmt;
    /*      */    @Column(name = "SUBLOAN")
    /*      */ private Integer subloan;
    /*      */    @Column(name = "UNSUBLOAN")
    /*      */ private Integer unsubloan;
    /*      */    @Column(name = "TOT_LOAN")
    /*      */ private Integer totLoan;
    /*      */    @Column(name = "DUP")
    /*      */ private long dou;
    /*      */    @Size(min = 1, max = 20)
    /*      */    @Column(name = "DUP_TZ")
    /*      */ private String douTz;
    /*      */    @Column(name = "DDOWN")
    /*      */ private long dod;
    /*      */    @Size(min = 1, max = 20)
    /*      */    @Column(name = "DDOWN_TZ")
    /*      */ private String dodTz;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "FISY")
    /*      */ private Integer fisy;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "FISY_PRT")
    /*      */ private String fisyPrt;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "VERSTR")
    /*      */ private String versions;
    /*      */    @Column(name = "LSU_PERF")
    /*      */ private Integer lsuPerf;
    /*      */    @Column(name = "EARNINGS")
    /*      */ private Integer earnings;
    /*      */    @Column(name = "NEEDGRANT")
    /*      */ private Integer needGrant;
    /*      */    @Column(name = "ACHIEVEAWARD")
    /*      */ private Integer achieveAward;
    /*      */    @Column(name = "RENEW4Y")
    /*      */ private Integer renew4y;
    /*      */    @Column(name = "LSUGRANT")
    /*      */ private Integer lsugrant;

    /*      */
 /*      */ public Print() {
    }

    /*      */
 /*      */ public Print(String prtId) {
        /*  342 */ this.prtId = prtId;
        /*      */    }

    /*      */
 /*      */ public Print(String prtId, String recid, int clientId, Integer prtNum, long prtTime, String prtTz) {
        /*  346 */ this.prtId = prtId;
        /*  347 */ this.recid = recid;
        /*  348 */ this.clientId = clientId;
        /*  349 */ this.prtNum = prtNum;
        /*  350 */ this.prtTime = prtTime;
        /*  351 */ this.prtTz = prtTz;
        /*      */    }

    /*      */
 /*      */ public String getPrtId() {
        /*  355 */ return this.prtId;
        /*      */    }

    /*      */
 /*      */ public void setPrtId(String prtId) {
        /*  359 */ this.prtId = prtId;
        /*      */    }

    /*      */
 /*      */ public String getRecid() {
        /*  363 */ return this.recid;
        /*      */    }

    /*      */
 /*      */ public void setRecid(String recid) {
        /*  367 */ this.recid = recid;
        /*      */    }

    /*      */
 /*      */ public int getClientId() {
        /*  371 */ return this.clientId;
        /*      */    }

    /*      */
 /*      */ public void setClientId(int clientId) {
        /*  375 */ this.clientId = clientId;
        /*      */    }

    /*      */
 /*      */ public Integer getCounselorId() {
        /*  379 */ return this.counselorId;
        /*      */    }

    /*      */
 /*      */ public void setCounselorId(Integer counselorId) {
        /*  383 */ this.counselorId = counselorId;
        /*      */    }

    /*      */
 /*      */ public String getCounselorName() {
        /*  387 */ return this.counselorName;
        /*      */    }

    /*      */
 /*      */ public void setCounselorName(String counselorName) {
        /*  391 */ this.counselorName = counselorName;
        /*      */    }

    /*      */
 /*      */ public Integer getPrtNum() {
        /*  395 */ return this.prtNum;
        /*      */    }

    /*      */
 /*      */ public void setPrtNum(Integer prtNum) {
        /*  399 */ this.prtNum = prtNum;
        /*      */    }

    /*      */
 /*      */ public long getPrtTime() {
        /*  403 */ return this.prtTime;
        /*      */    }

    /*      */
 /*      */ public void setPrtTime(long prtTime) {
        /*  407 */ this.prtTime = prtTime;
        /*      */    }

    /*      */
 /*      */ public String getPrtTz() {
        /*  411 */ return this.prtTz;
        /*      */    }

    /*      */
 /*      */ public void setPrtTz(String prtTz) {
        /*  415 */ this.prtTz = prtTz;
        /*      */    }

    /*      */
 /*      */ public Integer getTuitionFee() {
        /*  419 */ return this.tuitionFee;
        /*      */    }

    /*      */
 /*      */ public void setTuitionFee(Integer tuitionFee) {
        /*  423 */ this.tuitionFee = tuitionFee;
        /*      */    }

    /*      */
 /*      */ public Integer getRoomBoard() {
        /*  427 */ return this.roomBoard;
        /*      */    }

    /*      */
 /*      */ public void setRoomBoard(Integer roomBoard) {
        /*  431 */ this.roomBoard = roomBoard;
        /*      */    }

    /*      */
 /*      */ public Integer getDue() {
        /*  435 */ return this.due;
        /*      */    }

    /*      */
 /*      */ public void setDue(Integer due) {
        /*  439 */ this.due = due;
        /*      */    }

    /*      */
 /*      */ public Integer getYearOpt() {
        /*  443 */ return this.yearOpt;
        /*      */    }

    /*      */
 /*      */ public void setYearOpt(Integer yearOpt) {
        /*  447 */ this.yearOpt = yearOpt;
        /*      */    }

    /*      */
 /*      */ public Integer getQuarterOpt() {
        /*  451 */ return this.quarterOpt;
        /*      */    }

    /*      */
 /*      */ public void setQuarterOpt(Integer quarterOpt) {
        /*  455 */ this.quarterOpt = quarterOpt;
        /*      */    }

    /*      */
 /*      */ public Integer getMonthOpt() {
        /*  459 */ return this.monthOpt;
        /*      */    }

    /*      */
 /*      */ public void setMonthOpt(Integer monthOpt) {
        /*  463 */ this.monthOpt = monthOpt;
        /*      */    }

    /*      */
 /*      */ public Integer getTotCharges() {
        /*  467 */ return this.totCharges;
        /*      */    }

    /*      */
 /*      */ public void setTotCharges(Integer totCharges) {
        /*  471 */ this.totCharges = totCharges;
        /*      */    }

    /*      */
 /*      */ public Integer getTotAid() {
        /*  475 */ return this.totAid;
        /*      */    }

    /*      */
 /*      */ public void setTotAid(Integer totAid) {
        /*  479 */ this.totAid = totAid;
        /*      */    }

    /*      */
 /*      */ public Integer getTotAidWoWork() {
        /*  483 */ return this.totAidWoWork;
        /*      */    }

    /*      */
 /*      */ public void setTotAidWoWork(Integer totAidWoWork) {
        /*  487 */ this.totAidWoWork = totAidWoWork;
        /*      */    }

    /*      */
 /*      */ public Integer getFws() {
        /*  491 */ return this.fws;
        /*      */    }

    /*      */
 /*      */ public void setFws(Integer fws) {
        /*  495 */ this.fws = fws;
        /*      */    }

    /*      */
 /*      */ public Integer getOtherExpenses() {
        /*  499 */ return this.otherExpenses;
        /*      */    }

    /*      */
 /*      */ public void setOtherExpenses(Integer otherExpenses) {
        /*  503 */ this.otherExpenses = otherExpenses;
        /*      */    }

    /*      */
 /*      */ public Integer getEfc() {
        /*  507 */ return this.efc;
        /*      */    }

    /*      */
 /*      */ public void setEfc(Integer efc) {
        /*  511 */ this.efc = efc;
        /*      */    }

    /*      */
 /*      */ public Integer getMaxNeed() {
        /*  515 */ return this.maxNeed;
        /*      */    }

    /*      */
 /*      */ public void setMaxNeed(Integer maxNeed) {
        /*  519 */ this.maxNeed = maxNeed;
        /*      */    }

    /*      */
 /*      */ public Integer getCoa() {
        /*  523 */ return this.coa;
        /*      */    }

    /*      */
 /*      */ public void setCoa(Integer coa) {
        /*  527 */ this.coa = coa;
        /*      */    }

    /*      */
 /*      */ public Integer getPell() {
        /*  531 */ return this.pell;
        /*      */    }

    /*      */
 /*      */ public void setPell(Integer pell) {
        /*  535 */ this.pell = pell;
        /*      */    }

    /*      */
 /*      */
 /*      */ public Integer getNoncalGrant() {
        /*  540 */ return this.noncalGrant;
        /*      */    }

    /*      */
 /*      */ public void setNoncalGrant(Integer noncalGrant) {
        /*  544 */ this.noncalGrant = noncalGrant;
        /*      */    }

    /*      */
 /*      */ public Integer getPseog() {
        /*  548 */ return this.pseog;
        /*      */    }

    /*      */
 /*      */ public void setPseog(Integer pseog) {
        /*  552 */ this.pseog = pseog;
        /*      */    }

    /*      */
 /*      */ public Integer getNonLsuEa() {
        /*  556 */ return this.nonLsuEa;
        /*      */    }

    /*      */
 /*      */ public void setNonLsuEa(Integer nonLsuEa) {
        /*  560 */ this.nonLsuEa = nonLsuEa;
        /*      */    }

    /*      */
 /*      */ public Integer getNonLsuSship() {
        /*  564 */ return this.nonLsuSship;
        /*      */    }

    /*      */
 /*      */ public void setNonLsuSship(Integer nonLsuSship) {
        /*  568 */ this.nonLsuSship = nonLsuSship;
        /*      */    }

    /*      */
 /*      */ public Integer getLsuEa() {
        /*  572 */ return this.lsuEa;
        /*      */    }

    /*      */
 /*      */ public void setLsuEa(Integer lsuEa) {
        /*  576 */ this.lsuEa = lsuEa;
        /*      */    }

    /*      */
 /*      */ public Integer getLsuSship() {
        /*  580 */ return this.lsuSship;
        /*      */    }

    /*      */
 /*      */ public void setLsuSship(Integer lsuSship) {
        /*  584 */ this.lsuSship = lsuSship;
        /*      */    }

    /*      */
 /*      */ public Integer getSda() {
        /*  588 */ return this.sda;
        /*      */    }

    /*      */
 /*      */ public void setSda(Integer sda) {
        /*  592 */ this.sda = sda;
        /*      */    }

    /*      */
 /*      */ public Integer getFamilyDisct() {
        /*  596 */ return this.familyDisct;
        /*      */    }

    /*      */
 /*      */ public void setFamilyDisct(Integer familyDisct) {
        /*  600 */ this.familyDisct = familyDisct;
        /*      */    }

    /*      */
 /*      */ public Integer getNatlMerit() {
        /*  604 */ return this.natlMerit;
        /*      */    }

    /*      */
 /*      */ public void setNatlMerit(Integer natlMerit) {
        /*  608 */ this.natlMerit = natlMerit;
        /*      */    }

    /*      */
 /*      */ public Integer getPerkins() {
        /*  612 */ return this.perkins;
        /*      */    }

    /*      */
 /*      */ public void setPerkins(Integer perkins) {
        /*  616 */ this.perkins = perkins;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getSship1Name() {
        /*  621 */ return this.sship1Name;
        /*      */    }

    /*      */
 /*      */ public void setSship1Name(String sship1Name) {
        /*  625 */ this.sship1Name = sship1Name;
        /*      */    }

    /*      */
 /*      */
 /*      */ public Integer getSubloan() {
        /*  630 */ return this.subloan;
        /*      */    }

    /*      */
 /*      */ public void setSubloan(Integer subloan) {
        /*  634 */ this.subloan = subloan;
        /*      */    }

    /*      */
 /*      */ public Integer getUnsubloan() {
        /*  638 */ return this.unsubloan;
        /*      */    }

    /*      */
 /*      */ public void setUnsubloan(Integer unsubloan) {
        /*  642 */ this.unsubloan = unsubloan;
        /*      */    }

    /*      */
 /*      */ public Integer getTotLoan() {
        /*  646 */ return this.totLoan;
        /*      */    }

    /*      */
 /*      */ public void setTotLoan(Integer totLoan) {
        /*  650 */ this.totLoan = totLoan;
        /*      */    }

    /*      */
 /*      */
 /*      */ public int hashCode() {
        /*  655 */ int hash = 0;
        /*  656 */ hash += (this.prtId != null) ? this.prtId.hashCode() : 0;
        /*  657 */ return hash;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public boolean equals(Object object) {
        /*  663 */ if (!(object instanceof edu.lsu.estimator.Print)) {
            /*  664 */ return false;
            /*      */        }
        /*  666 */ edu.lsu.estimator.Print other = (edu.lsu.estimator.Print) object;
        /*  667 */ if ((this.prtId == null && other.prtId != null) || (this.prtId != null && !this.prtId.equals(other.prtId))) {
            /*  668 */ return false;
            /*      */        }
        /*  670 */ return true;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String toString() {
        /*  675 */ return "edu.lsu.estimator.Print[ prtId=" + this.prtId + " ]";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public long getDou() {
        /*  682 */ return this.dou;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setDou(long dou) {
        /*  689 */ this.dou = dou;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getDouTz() {
        /*  696 */ return this.douTz;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setDouTz(String douTz) {
        /*  703 */ this.douTz = douTz;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public long getDod() {
        /*  710 */ return this.dod;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setDod(long dod) {
        /*  717 */ this.dod = dod;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getDodTz() {
        /*  724 */ return this.dodTz;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setDodTz(String dodTz) {
        /*  731 */ this.dodTz = dodTz;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFisy() {
        /*  738 */ return this.fisy;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFisy(Integer fisy) {
        /*  745 */ this.fisy = fisy;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getFisyPrt() {
        /*  752 */ return this.fisyPrt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFisyPrt(String fisyPrt) {
        /*  759 */ this.fisyPrt = fisyPrt;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getVersions() {
        /*  766 */ return this.versions;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setVersions(String versions) {
        /*  773 */ this.versions = versions;
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
 /*      */ public Student getStud() {
        /*  786 */ return this.stud;
        /*      */    }

    /*      */
 /*      */ public void setStud(Student stud) {
        /*  790 */ this.stud = stud;
        /*      */    }

    /*      */
 /*      */ public Integer getSship1LsuAmt() {
        /*  794 */ return this.sship1LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship1LsuAmt(Integer sship1LsuAmt) {
        /*  798 */ this.sship1LsuAmt = sship1LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship1ExtAmt() {
        /*  802 */ return this.sship1ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship1ExtAmt(Integer sship1ExtAmt) {
        /*  806 */ this.sship1ExtAmt = sship1ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship2Name() {
        /*  810 */ return this.sship2Name;
        /*      */    }

    /*      */
 /*      */ public void setSship2Name(String sship2Name) {
        /*  814 */ this.sship2Name = sship2Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship2LsuAmt() {
        /*  818 */ return this.sship2LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship2LsuAmt(Integer sship2LsuAmt) {
        /*  822 */ this.sship2LsuAmt = sship2LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship2ExtAmt() {
        /*  826 */ return this.sship2ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship2ExtAmt(Integer sship2ExtAmt) {
        /*  830 */ this.sship2ExtAmt = sship2ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship3Name() {
        /*  834 */ return this.sship3Name;
        /*      */    }

    /*      */
 /*      */ public void setSship3Name(String sship3Name) {
        /*  838 */ this.sship3Name = sship3Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship3LsuAmt() {
        /*  842 */ return this.sship3LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship3LsuAmt(Integer sship3LsuAmt) {
        /*  846 */ this.sship3LsuAmt = sship3LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship3ExtAmt() {
        /*  850 */ return this.sship3ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship3ExtAmt(Integer sship3ExtAmt) {
        /*  854 */ this.sship3ExtAmt = sship3ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship4Name() {
        /*  858 */ return this.sship4Name;
        /*      */    }

    /*      */
 /*      */ public void setSship4Name(String sship4Name) {
        /*  862 */ this.sship4Name = sship4Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship4LsuAmt() {
        /*  866 */ return this.sship4LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship4LsuAmt(Integer sship4LsuAmt) {
        /*  870 */ this.sship4LsuAmt = sship4LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship4ExtAmt() {
        /*  874 */ return this.sship4ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship4ExtAmt(Integer sship4ExtAmt) {
        /*  878 */ this.sship4ExtAmt = sship4ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship5Name() {
        /*  882 */ return this.sship5Name;
        /*      */    }

    /*      */
 /*      */ public void setSship5Name(String sship5Name) {
        /*  886 */ this.sship5Name = sship5Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship5LsuAmt() {
        /*  890 */ return this.sship5LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship5LsuAmt(Integer sship5LsuAmt) {
        /*  894 */ this.sship5LsuAmt = sship5LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship5ExtAmt() {
        /*  898 */ return this.sship5ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship5ExtAmt(Integer sship5ExtAmt) {
        /*  902 */ this.sship5ExtAmt = sship5ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship6Name() {
        /*  906 */ return this.sship6Name;
        /*      */    }

    /*      */
 /*      */ public void setSship6Name(String sship6Name) {
        /*  910 */ this.sship6Name = sship6Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship6LsuAmt() {
        /*  914 */ return this.sship6LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship6LsuAmt(Integer sship6LsuAmt) {
        /*  918 */ this.sship6LsuAmt = sship6LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship6ExtAmt() {
        /*  922 */ return this.sship6ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship6ExtAmt(Integer sship6ExtAmt) {
        /*  926 */ this.sship6ExtAmt = sship6ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship7Name() {
        /*  930 */ return this.sship7Name;
        /*      */    }

    /*      */
 /*      */ public void setSship7Name(String sship7Name) {
        /*  934 */ this.sship7Name = sship7Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship7LsuAmt() {
        /*  938 */ return this.sship7LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship7LsuAmt(Integer sship7LsuAmt) {
        /*  942 */ this.sship7LsuAmt = sship7LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship7ExtAmt() {
        /*  946 */ return this.sship7ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship7ExtAmt(Integer sship7ExtAmt) {
        /*  950 */ this.sship7ExtAmt = sship7ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship8Name() {
        /*  954 */ return this.sship8Name;
        /*      */    }

    /*      */
 /*      */ public void setSship8Name(String sship8Name) {
        /*  958 */ this.sship8Name = sship8Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship8LsuAmt() {
        /*  962 */ return this.sship8LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship8LsuAmt(Integer sship8LsuAmt) {
        /*  966 */ this.sship8LsuAmt = sship8LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship8ExtAmt() {
        /*  970 */ return this.sship8ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship8ExtAmt(Integer sship8ExtAmt) {
        /*  974 */ this.sship8ExtAmt = sship8ExtAmt;
        /*      */    }

    /*      */
 /*      */ public String getSship9Name() {
        /*  978 */ return this.sship9Name;
        /*      */    }

    /*      */
 /*      */ public void setSship9Name(String sship9Name) {
        /*  982 */ this.sship9Name = sship9Name;
        /*      */    }

    /*      */
 /*      */ public Integer getSship9LsuAmt() {
        /*  986 */ return this.sship9LsuAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship9LsuAmt(Integer sship9LsuAmt) {
        /*  990 */ this.sship9LsuAmt = sship9LsuAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getSship9ExtAmt() {
        /*  994 */ return this.sship9ExtAmt;
        /*      */    }

    /*      */
 /*      */ public void setSship9ExtAmt(Integer sship9ExtAmt) {
        /*  998 */ this.sship9ExtAmt = sship9ExtAmt;
        /*      */    }

    /*      */
 /*      */ public Integer getCalGrantA() {
        /* 1002 */ return this.calGrantA;
        /*      */    }

    /*      */
 /*      */ public void setCalGrantA(Integer calGrantA) {
        /* 1006 */ this.calGrantA = calGrantA;
        /*      */    }

    /*      */
 /*      */ public Integer getCalGrantB() {
        /* 1010 */ return this.calGrantB;
        /*      */    }

    /*      */
 /*      */ public void setCalGrantB(Integer calGrantB) {
        /* 1014 */ this.calGrantB = calGrantB;
        /*      */    }

    /*      */
 /*      */ public Integer getLsuPerf() {
        /* 1018 */ return this.lsuPerf;
        /*      */    }

    /*      */
 /*      */ public void setLsuPerf(Integer lsuPerf) {
        /* 1022 */ this.lsuPerf = lsuPerf;
        /*      */    }

    /*      */
 /*      */ public Integer getEarnings() {
        /* 1026 */ return this.earnings;
        /*      */    }

    /*      */
 /*      */ public void setEarnings(Integer earnings) {
        /* 1030 */ this.earnings = earnings;
        /*      */    }

    /*      */
 /*      */ public Integer getNeedGrant() {
        /* 1034 */ return this.needGrant;
        /*      */    }

    /*      */
 /*      */ public void setNeedGrant(Integer needGrant) {
        /* 1038 */ this.needGrant = needGrant;
        /*      */    }

    /*      */
 /*      */ public Integer getAchieveAward() {
        /* 1042 */ return this.achieveAward;
        /*      */    }

    /*      */
 /*      */ public void setAchieveAward(Integer achieveAward) {
        /* 1046 */ this.achieveAward = achieveAward;
        /*      */    }

    /*      */
 /*      */ public Integer getRenew4y() {
        /* 1050 */ return this.renew4y;
        /*      */    }

    /*      */
 /*      */ public void setRenew4y(Integer renew4y) {
        /* 1054 */ this.renew4y = renew4y;
        /*      */    }

    /*      */
 /*      */ public Integer getLsugrant() {
        /* 1058 */ return this.lsugrant;
        /*      */    }

    /*      */
 /*      */ public void setLsugrant(Integer lsugrant) {
        /* 1062 */ this.lsugrant = lsugrant;
        /*      */    }
    /*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Print.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
