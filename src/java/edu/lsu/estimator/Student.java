/*      */ package edu.lsu.estimator;

/*      */
 /*      */ import edu.lsu.estimator.Print;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import javax.persistence.Basic;
/*      */ import javax.persistence.Column;
/*      */ import javax.persistence.Entity;
/*      */ import javax.persistence.GeneratedValue;
/*      */ import javax.persistence.GenerationType;
/*      */ import javax.persistence.Id;
/*      */ import javax.persistence.JoinColumn;
/*      */ import javax.persistence.NamedQueries;
/*      */ import javax.persistence.NamedQuery;
/*      */ import javax.persistence.OneToMany;
/*      */ import javax.persistence.Table;
/*      */ import javax.persistence.TableGenerator;
/*      */ import javax.persistence.Temporal;
/*      */ import javax.persistence.TemporalType;
/*      */ import javax.validation.constraints.DecimalMax;
/*      */ import javax.validation.constraints.DecimalMin;
/*      */ import javax.validation.constraints.Max;
/*      */ import javax.validation.constraints.Min;
/*      */ import javax.validation.constraints.NotNull;
/*      */ import javax.validation.constraints.Size;
/*      */ import javax.xml.bind.annotation.XmlRootElement;
/*      */ import org.hibernate.validator.constraints.Email;
/*      */ import org.hibernate.validator.constraints.NotBlank;
/*      */ import org.hibernate.validator.constraints.NotEmpty;
/*      */ import org.hibernate.validator.constraints.Range;

/*      */ @Entity
/*      */ @Table(name = "STUDENT")
/*      */ @XmlRootElement
/*      */ @NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findPOPWID", query = "SELECT s FROM Student s where s.pickupInd=1 and s.studentALsuid= :lsuid and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob ORDER BY s.ddoe desc "),
    @NamedQuery(name = "Student.findPOPWOID", query = "SELECT s FROM Student s where s.pickupInd=1 and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob ORDER BY s.ddoe desc "),
    @NamedQuery(name = "Student.findPOPWIDwrecid", query = "SELECT s FROM Student s where s.pickupInd=1 and s.studentALsuid= :lsuid and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob and s.recid <> :recid ORDER BY s.ddoe desc "),
    @NamedQuery(name = "Student.findPOPWOIDwrecid", query = "SELECT s FROM Student s where s.pickupInd=1 and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob and s.recid <> :recid ORDER BY s.ddoe desc "),
    @NamedQuery(name = "Student.findByStudentFisy", query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy"),
    @NamedQuery(name = "Student.findActiveStudentByFisyNupNdownDiff", query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.pickupInd=1 and ((s.dup=0 and s.ddown=0 and s.clientId = :clientId) or (s.estmNumb<0))   "),
    @NamedQuery(name = "Student.findInactiveStudentByFisyNupNdownDiff", query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.pickupInd=0 and s.dup=0 and s.ddown=0  and s.clientId = :clientId and s.prtTimes>0 "),
    @NamedQuery(name = "Student.findUploadedNonPrtStudentByFisy", query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.dup>0 and s.ddown=0  and s.clientId = :clientId  and s.prtTimes=0 "),
    @NamedQuery(name = "Student.findByActiveLsuid", query = "SELECT s FROM Student s WHERE s.studentFisy = :fisy and s.recid <> :recid and s.studentALsuid = :lsuid and s.pickupInd=1"),
    @NamedQuery(name = "Student.findByStudentALsuid", query = "SELECT s FROM Student s WHERE s.studentALsuid = :studentALsuid"),
    @NamedQuery(name = "Student.findByStudentBLastname", query = "SELECT s FROM Student s WHERE s.studentBLastname = :studentBLastname"),
    @NamedQuery(name = "Student.findByStudentCFirstname", query = "SELECT s FROM Student s WHERE s.studentCFirstname = :studentCFirstname"),
    @NamedQuery(name = "Student.findByStudentDDob", query = "SELECT s FROM Student s WHERE s.studentDDob = :studentDDob"),
    @NamedQuery(name = "Student.findByStudentEEmail", query = "SELECT s FROM Student s WHERE s.studentEEmail = :studentEEmail"),
    @NamedQuery(name = "Student.findByStudentFPhone", query = "SELECT s FROM Student s WHERE s.studentFPhone = :studentFPhone"),
    @NamedQuery(name = "Student.findByStudentGStreet", query = "SELECT s FROM Student s WHERE s.studentGStreet = :studentGStreet"),
    @NamedQuery(name = "Student.findByStudentHCity", query = "SELECT s FROM Student s WHERE s.studentHCity = :studentHCity"),
    @NamedQuery(name = "Student.findByStudentIState", query = "SELECT s FROM Student s WHERE s.studentIState = :studentIState"),
    @NamedQuery(name = "Student.findByStudentJZip", query = "SELECT s FROM Student s WHERE s.studentJZip = :studentJZip"),
    @NamedQuery(name = "Student.findByStudentKCountry", query = "SELECT s FROM Student s WHERE s.studentKCountry = :studentKCountry"),
    @NamedQuery(name = "Student.findByStudentLIntlStud", query = "SELECT s FROM Student s WHERE s.studentLIntlStud = :studentLIntlStud"),
    @NamedQuery(name = "Student.findByStudentMMarry", query = "SELECT s FROM Student s WHERE s.studentMMarry = :studentMMarry"),
    @NamedQuery(name = "Student.findByStudentNSda", query = "SELECT s FROM Student s WHERE s.studentNSda = :studentNSda"),
    @NamedQuery(name = "Student.findByStudentOLastSchool", query = "SELECT s FROM Student s WHERE s.studentOLastSchool = :studentOLastSchool"),
    @NamedQuery(name = "Student.findByStudentPGpa", query = "SELECT s FROM Student s WHERE s.studentPGpa = :studentPGpa"),
    @NamedQuery(name = "Student.findByStudentQSat", query = "SELECT s FROM Student s WHERE s.studentQSat = :studentQSat"),
    @NamedQuery(name = "Student.findByStudentRAct", query = "SELECT s FROM Student s WHERE s.studentRAct = :studentRAct"),
    @NamedQuery(name = "Student.findByStudentSMerit", query = "SELECT s FROM Student s WHERE s.studentSMerit = :studentSMerit"),
    @NamedQuery(name = "Student.findByStudentTMajor", query = "SELECT s FROM Student s WHERE s.studentTMajor = :studentTMajor"),
    @NamedQuery(name = "Student.findByStudentUAcademic", query = "SELECT s FROM Student s WHERE s.studentUAcademic = :studentUAcademic"),
    @NamedQuery(name = "Student.findByStudentVFamily", query = "SELECT s FROM Student s WHERE s.studentVFamily = :studentVFamily"),
    @NamedQuery(name = "Student.findByStudentWDorm", query = "SELECT s FROM Student s WHERE s.studentWDorm = :studentWDorm"),
    @NamedQuery(name = "Student.findByStudentXFafsa", query = "SELECT s FROM Student s WHERE s.studentXFafsa = :studentXFafsa"),
    @NamedQuery(name = "Student.findByStudentYIndept", query = "SELECT s FROM Student s WHERE s.studentYIndept = :studentYIndept"),
    @NamedQuery(name = "Student.findByStudentZCalgrant", query = "SELECT s FROM Student s WHERE s.studentZCalgrant = :studentZCalgrant"),
    @NamedQuery(name = "Student.findByStudentAaCalgrantA", query = "SELECT s FROM Student s WHERE s.studentAaCalgrantA = :studentAaCalgrantA"),
    @NamedQuery(name = "Student.findByStudentAbCalgrantB", query = "SELECT s FROM Student s WHERE s.studentAbCalgrantB = :studentAbCalgrantB"),
    @NamedQuery(name = "Student.findByStudentAcFamilySize", query = "SELECT s FROM Student s WHERE s.studentAcFamilySize = :studentAcFamilySize"),
    @NamedQuery(name = "Student.findByStudentAfFamilyContrib", query = "SELECT s FROM Student s WHERE s.studentAfFamilyContrib = :studentAfFamilyContrib"),
    @NamedQuery(name = "Student.findByStudentAgNonlsuAllowrance", query = "SELECT s FROM Student s WHERE s.studentAgNonlsuAllowrance = :studentAgNonlsuAllowrance"),
    @NamedQuery(name = "Student.findByStudentAhLsuAllowrance", query = "SELECT s FROM Student s WHERE s.studentAhLsuAllowrance = :studentAhLsuAllowrance"),
    @NamedQuery(name = "Student.findByStudentAiEduAllowPer", query = "SELECT s FROM Student s WHERE s.studentAiEduAllowPer = :studentAiEduAllowPer"),
    @NamedQuery(name = "Student.findByStudentAjHomeState", query = "SELECT s FROM Student s WHERE s.studentAjHomeState = :studentAjHomeState"),
    @NamedQuery(name = "Student.findByStudentAkNoncalGrant", query = "SELECT s FROM Student s WHERE s.studentAkNoncalGrant = :studentAkNoncalGrant"),
    @NamedQuery(name = "Student.findByStudentAlOutScholarships", query = "SELECT s FROM Student s WHERE s.studentAlOutScholarships = :studentAlOutScholarships"),
    @NamedQuery(name = "Student.findByStudentAmOutScholarshipAmt", query = "SELECT s FROM Student s WHERE s.studentAmOutScholarshipAmt = :studentAmOutScholarshipAmt"),
    @NamedQuery(name = "Student.findByStudentAnPubNotes", query = "SELECT s FROM Student s WHERE s.studentAnPubNotes = :studentAnPubNotes"),
    @NamedQuery(name = "Student.findByStudentAoPriNotes", query = "SELECT s FROM Student s WHERE s.studentAoPriNotes = :studentAoPriNotes"),
    @NamedQuery(name = "Student.findByStudentApSubLoans", query = "SELECT s FROM Student s WHERE s.studentApSubLoans = :studentApSubLoans"),
    @NamedQuery(name = "Student.findByStudentAqUnsubLoans", query = "SELECT s FROM Student s WHERE s.studentAqUnsubLoans = :studentAqUnsubLoans"),
    @NamedQuery(name = "Student.findByStudentArFws", query = "SELECT s FROM Student s WHERE s.studentArFws = :studentArFws"),
    @NamedQuery(name = "Student.findByStudentAsScholarship1Name", query = "SELECT s FROM Student s WHERE s.studentAsScholarship1Name = :studentAsScholarship1Name"),
    @NamedQuery(name = "Student.findByStudentAtScholarship1Note", query = "SELECT s FROM Student s WHERE s.studentAtScholarship1Note = :studentAtScholarship1Note"),
    @NamedQuery(name = "Student.findByStudentAuScholarship1Amt", query = "SELECT s FROM Student s WHERE s.studentAuScholarship1Amt = :studentAuScholarship1Amt"),
    @NamedQuery(name = "Student.findByStudentAvScholarship2Name", query = "SELECT s FROM Student s WHERE s.studentAvScholarship2Name = :studentAvScholarship2Name"),
    @NamedQuery(name = "Student.findByStudentAwScholarship2Note", query = "SELECT s FROM Student s WHERE s.studentAwScholarship2Note = :studentAwScholarship2Note"),
    @NamedQuery(name = "Student.findByStudentAxScholarship2Amt", query = "SELECT s FROM Student s WHERE s.studentAxScholarship2Amt = :studentAxScholarship2Amt"),
    @NamedQuery(name = "Student.findByStudentAyScholarship3Name", query = "SELECT s FROM Student s WHERE s.studentAyScholarship3Name = :studentAyScholarship3Name"),
    @NamedQuery(name = "Student.findByStudentAzScholarship3Note", query = "SELECT s FROM Student s WHERE s.studentAzScholarship3Note = :studentAzScholarship3Note"),
    @NamedQuery(name = "Student.findByStudentBaScholarship3Amt", query = "SELECT s FROM Student s WHERE s.studentBaScholarship3Amt = :studentBaScholarship3Amt"),
    @NamedQuery(name = "Student.findByStudentBbScholarship4Name", query = "SELECT s FROM Student s WHERE s.studentBbScholarship4Name = :studentBbScholarship4Name"),
    @NamedQuery(name = "Student.findByStudentBcScholarship4Note", query = "SELECT s FROM Student s WHERE s.studentBcScholarship4Note = :studentBcScholarship4Note"),
    @NamedQuery(name = "Student.findByStudentBdScholarship4Amt", query = "SELECT s FROM Student s WHERE s.studentBdScholarship4Amt = :studentBdScholarship4Amt"),
    @NamedQuery(name = "Student.findByStudentBeScholarship5Name", query = "SELECT s FROM Student s WHERE s.studentBeScholarship5Name = :studentBeScholarship5Name"),
    @NamedQuery(name = "Student.findByStudentBfScholarship5Note", query = "SELECT s FROM Student s WHERE s.studentBfScholarship5Note = :studentBfScholarship5Note"),
    @NamedQuery(name = "Student.findByStudentBgScholarship5Amt", query = "SELECT s FROM Student s WHERE s.studentBgScholarship5Amt = :studentBgScholarship5Amt"),
    @NamedQuery(name = "Student.findByStudentBhScholarship6Name", query = "SELECT s FROM Student s WHERE s.studentBhScholarship6Name = :studentBhScholarship6Name"),
    @NamedQuery(name = "Student.findByStudentBiScholarship6Note", query = "SELECT s FROM Student s WHERE s.studentBiScholarship6Note = :studentBiScholarship6Note"),
    @NamedQuery(name = "Student.findByStudentBjScholarship6Amt", query = "SELECT s FROM Student s WHERE s.studentBjScholarship6Amt = :studentBjScholarship6Amt"),
    @NamedQuery(name = "Student.findByStudentBkScholarship7Name", query = "SELECT s FROM Student s WHERE s.studentBkScholarship7Name = :studentBkScholarship7Name"),
    @NamedQuery(name = "Student.findByStudentBlScholarship7Note", query = "SELECT s FROM Student s WHERE s.studentBlScholarship7Note = :studentBlScholarship7Note"),
    @NamedQuery(name = "Student.findByStudentBmScholarship7Amt", query = "SELECT s FROM Student s WHERE s.studentBmScholarship7Amt = :studentBmScholarship7Amt"),
    @NamedQuery(name = "Student.findByStudentBnScholarship8Name", query = "SELECT s FROM Student s WHERE s.studentBnScholarship8Name = :studentBnScholarship8Name"),
    @NamedQuery(name = "Student.findByStudentBoScholarship8Note", query = "SELECT s FROM Student s WHERE s.studentBoScholarship8Note = :studentBoScholarship8Note"),
    @NamedQuery(name = "Student.findByStudentBpScholarship8Amt", query = "SELECT s FROM Student s WHERE s.studentBpScholarship8Amt = :studentBpScholarship8Amt"),
    @NamedQuery(name = "Student.findByStudentBqScholarship9Name", query = "SELECT s FROM Student s WHERE s.studentBqScholarship9Name = :studentBqScholarship9Name"),
    @NamedQuery(name = "Student.findByStudentBrScholarship9Note", query = "SELECT s FROM Student s WHERE s.studentBrScholarship9Note = :studentBrScholarship9Note"),
    @NamedQuery(name = "Student.findByStudentBsScholarship9Amt", query = "SELECT s FROM Student s WHERE s.studentBsScholarship9Amt = :studentBsScholarship9Amt"),
    @NamedQuery(name = "Student.findByStudentBtSupercounselor", query = "SELECT s FROM Student s WHERE s.studentBtSupercounselor = :studentBtSupercounselor"),
    @NamedQuery(name = "Student.findByStudentBwProgress", query = "SELECT s FROM Student s WHERE s.studentBwProgress = :studentBwProgress"),
    @NamedQuery(name = "Student.findByStudentBzUploaded", query = "SELECT s FROM Student s WHERE s.studentBzUploaded = :studentBzUploaded"),
    @NamedQuery(name = "Student.findByStudentCbBanner", query = "SELECT s FROM Student s WHERE s.studentCbBanner = :studentCbBanner"),
    @NamedQuery(name = "Student.findByStudentPassword", query = "SELECT s FROM Student s WHERE s.studentPassword = :studentPassword"),
    @NamedQuery(name = "Student.findByStudentTermStart", query = "SELECT s FROM Student s WHERE s.studentTermStart = :studentTermStart"),
    @NamedQuery(name = "Student.findByStudentTermEnd", query = "SELECT s FROM Student s WHERE s.studentTermEnd = :studentTermEnd"),
    @NamedQuery(name = "Student.findByStudentStudType", query = "SELECT s FROM Student s WHERE s.studentStudType = :studentStudType"),
    @NamedQuery(name = "Student.findByStudentQSatV", query = "SELECT s FROM Student s WHERE s.studentQSatV = :studentQSatV"),
    @NamedQuery(name = "Student.findByPickupInd", query = "SELECT s FROM Student s WHERE s.pickupInd = :pickupInd"),
    @NamedQuery(name = "Student.findByEstmNumb", query = "SELECT s FROM Student s WHERE s.estmNumb = :estmNumb"),
    @NamedQuery(name = "Student.findByStudentNumb", query = "SELECT s FROM Student s WHERE s.studentNumb = :studentNumb"),
    @NamedQuery(name = "Student.findByIndEfc", query = "SELECT s FROM Student s WHERE s.indEfc = :indEfc"),
    @NamedQuery(name = "Student.findByIndExcloans", query = "SELECT s FROM Student s WHERE s.indExcloans = :indExcloans"),
    @NamedQuery(name = "Student.findByIndEalsu", query = "SELECT s FROM Student s WHERE s.indEalsu = :indEalsu"),
    @NamedQuery(name = "Student.findByIndEanonlsu", query = "SELECT s FROM Student s WHERE s.indEanonlsu = :indEanonlsu"),
    @NamedQuery(name = "Student.findByStudentAdFamilyIncome", query = "SELECT s FROM Student s WHERE s.studentAdFamilyIncome = :studentAdFamilyIncome"),
    @NamedQuery(name = "Student.findByStudentAeFamilyAsset", query = "SELECT s FROM Student s WHERE s.studentAeFamilyAsset = :studentAeFamilyAsset"),
    @NamedQuery(name = "Student.findByStudentByDom", query = "SELECT s FROM Student s WHERE s.studentByDom = :studentByDom"),
    @NamedQuery(name = "Student.findByStudentBvDoe", query = "SELECT s FROM Student s WHERE s.studentBvDoe = :studentBvDoe"),
    @NamedQuery(name = "Student.findByAdjCalgrantInd", query = "SELECT s FROM Student s WHERE s.adjCalgrantInd = :adjCalgrantInd"),
    @NamedQuery(name = "Student.findBySex", query = "SELECT s FROM Student s WHERE s.sex = :sex"),
    @NamedQuery(name = "Student.findByHomeAddrApt", query = "SELECT s FROM Student s WHERE s.homeAddrApt = :homeAddrApt"),
    @NamedQuery(name = "Student.findByHomecostudies", query = "SELECT s FROM Student s WHERE s.homecostudies = :homecostudies"),
    @NamedQuery(name = "Student.findByTzdoe", query = "SELECT s FROM Student s WHERE s.tzdoe = :tzdoe"),
    @NamedQuery(name = "Student.findByTzdom", query = "SELECT s FROM Student s WHERE s.tzdom = :tzdom"),
    @NamedQuery(name = "Student.findByTzup", query = "SELECT s FROM Student s WHERE s.tzup = :tzup"),
    @NamedQuery(name = "Student.findByTzdown", query = "SELECT s FROM Student s WHERE s.tzdown = :tzdown"),
    @NamedQuery(name = "Student.findByRecid", query = "SELECT s FROM Student s WHERE s.recid = :recid"),
    @NamedQuery(name = "Student.findByClientId", query = "SELECT s FROM Student s WHERE s.clientId = :clientId"),
    @NamedQuery(name = "Student.findByCounselorId", query = "SELECT s FROM Student s WHERE s.counselorId = :counselorId"),
    @NamedQuery(name = "Student.findByModRoot", query = "SELECT s FROM Student s WHERE s.modRoot = :modRoot"),
    @NamedQuery(name = "Student.findByModPre", query = "SELECT s FROM Student s WHERE s.modPre = :modPre"),
    @NamedQuery(name = "Student.findByStudentBxModCounselor", query = "SELECT s FROM Student s WHERE s.studentBxModCounselor = :studentBxModCounselor"),
    @NamedQuery(name = "Student.findByStudentBuOrigCounselor", query = "SELECT s FROM Student s WHERE s.studentBuOrigCounselor = :studentBuOrigCounselor"),
    @NamedQuery(name = "Student.findByCounselorOrig", query = "SELECT s FROM Student s WHERE s.counselorOrig = :counselorOrig"),
    @NamedQuery(name = "Student.findByCounselorMod", query = "SELECT s FROM Student s WHERE s.counselorMod = :counselorMod"),
    @NamedQuery(name = "Student.findByLostTz", query = "SELECT s FROM Student s WHERE s.lostTz = :lostTz"),
    @NamedQuery(name = "Student.findByStudentUserName", query = "SELECT s FROM Student s WHERE s.studentUserName = :studentUserName"),
    @NamedQuery(name = "Student.findActiveByLsuidOrUsernamewofisy", query = "SELECT s FROM Student s WHERE s.pickupInd = 1 and (s.studentALsuid = :lsuid or s.studentUserName = :username) "),
    @NamedQuery(name = "Student.findActiveByLsuidOrUsername", query = "SELECT s FROM Student s WHERE s.pickupInd = 1 and (s.studentALsuid = :lsuid or s.studentUserName = :username)  and s.studentFisy = :studentFisy"),
    @NamedQuery(name = "Student.findByDup", query = "SELECT s FROM Student s WHERE s.dup = :dup"),
    @NamedQuery(name = "Student.findByDdown", query = "SELECT s FROM Student s WHERE s.ddown = :ddown"),
    @NamedQuery(name = "Student.findByDdoe", query = "SELECT s FROM Student s WHERE s.ddoe = :ddoe"),
    @NamedQuery(name = "Student.findByDdom", query = "SELECT s FROM Student s WHERE s.ddom = :ddom"),
    @NamedQuery(name = "Student.findByLostTime", query = "SELECT s FROM Student s WHERE s.lostTime = :lostTime"),
    @NamedQuery(name = "Student.findByLostToMaster", query = "SELECT s FROM Student s WHERE s.lostToMaster = :lostToMaster"),
    @NamedQuery(name = "Student.findByLostToLocal", query = "SELECT s FROM Student s WHERE s.lostToLocal = :lostToLocal"),
    @NamedQuery(name = "Student.findStudNumb", query = "Select s.studentNumb from Student s "),
    @NamedQuery(name = "Student.findByPrtTimes", query = "SELECT s FROM Student s WHERE s.prtTimes >0")})

/*      */ public class Student
        /*      */ implements Serializable /*      */ {

    /*      */ private static final long serialVersionUID = 1L;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "STUDENT_FISY")
    /*      */ private int studentFisy;
    /*      */    @Basic(optional = true)
    /*      */    @Size(max = 7)
    /*      */    @Column(name = "STUDENT_A_LSUID")
    /*      */ private String studentALsuid;
    /*      */    @Size(min = 1, max = 50)
    /*      */    @Column(name = "STUDENT_B_LASTNAME")
    /*      */    @NotNull
    /*      */    @NotEmpty
    /*      */ private String studentBLastname;
    /*      */    @Size(min = 1, max = 50)
    /*      */    @Column(name = "STUDENT_C_FIRSTNAME")
    /*      */    @NotNull
    /*      */    @NotEmpty
    /*      */ private String studentCFirstname;
    /*      */    @Column(name = "STUDENT_D_DOB")
    /*      */    @NotBlank
    /*      */    @Size(min = 10, max = 10)
    /*      */ private String studentDDob;
    /*      */    @Id
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 100)
    /*      */    @Column(name = "RECID")
    /*      */ private String recid;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "STUDENT_NUMB")
    /*      */    @TableGenerator(name = "ID_Gen", table = "SEQUENCE", pkColumnName = "NAME", valueColumnName = "ID", pkColumnValue = "STUD_GEN", initialValue = 2, allocationSize = 1)
    /*      */    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_Gen")
    /*      */ private Integer studentNumb;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_E_EMAIL")
    /*      */    @Email
    /*      */    @NotBlank
    /*      */ private String studentEEmail;
    /*      */    @Size(max = 30)
    /*      */    @NotNull
    /*      */    @Column(name = "STUDENT_F_PHONE")
    /*      */ private String studentFPhone;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_G_STREET")
    /*      */ private String studentGStreet;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_H_CITY")
    /*      */ private String studentHCity;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_I_STATE")
    /*      */    @NotBlank
    /*      */ private String studentIState;
    /*      */    @Size(max = 30)
    /*      */    @NotBlank
    /*      */    @Column(name = "STUDENT_J_ZIP")
    /*      */ private String studentJZip;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_K_COUNTRY")
    /*      */ private String studentKCountry;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_L_INTL_STUD")
    /*      */ private String studentLIntlStud;
    /*      */    @Size(max = 10)
    /*      */    @Column(name = "STUDENT_M_MARRY")
    /*      */ private String studentMMarry;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_N_SDA")
    /*      */ private String studentNSda;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_O_LAST_SCHOOL")
    /*      */ private String studentOLastSchool;
    /*      */    @Column(name = "STUDENT_P_GPA")
    /*      */    @Range(min = 0L, max = 7L)
    /*      */    @DecimalMin("0.00")
    /*      */    @DecimalMax("7.00")
    /*      */    @NotNull
    /*      */ private BigDecimal studentPGpa;
    /*      */    @Column(name = "STUDENT_Q_SAT")
    /*      */    @Range(min = 0L, max = 2400L)
    /*      */    @NotNull
    /*      */ private Integer studentQSat;
    /*      */    @Column(name = "STUDENT_Q_SAT_V")
    /*      */    @Range(min = 0L, max = 800L)
    /*      */    @NotNull
    /*      */ private Integer studentQSatV;
    /*      */    @Column(name = "STUDENT_R_ACT")
    /*      */    @Range(min = 0L, max = 36L)
    /*      */    @NotNull
    /*      */ private Integer studentRAct;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_S_MERIT")
    /*      */ private String studentSMerit;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_T_MAJOR")
    /*      */ private String studentTMajor;
    /*      */    @Size(max = 10)
    /*      */    @Column(name = "STUDENT_U_ACADEMIC")
    /*      */ private String studentUAcademic;
    /*      */    @Size(max = 250)
    /*      */    @Column(name = "STUDENT_V_FAMILY")
    /*      */ private String studentVFamily;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Column(name = "HOMECOSTUDIES")
    /*      */ private Integer homecostudies;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_W_DORM")
    /*      */ private String studentWDorm;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_X_FAFSA")
    /*      */ private String studentXFafsa;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_Y_INDEPT")
    /*      */ private String studentYIndept;
                  @Size(max = 3)
                  @Column(name = "STUDENT_F_PELLGRANT")
                 private String studentFPellGrant;
    /*      */

 @Size(max = 3)
    /*      */    @Column(name = "STUDENT_Z_CALGRANT")
    /*      */ private String studentZCalgrant;
    /*      */    @Column(name = "STUDENT_AA_CALGRANT_A")
    /*      */    @Range(min = 0L)
    /*      */    @NotNull
    /*      */ private Integer studentAaCalgrantA;
    /*      */    @Column(name = "STUDENT_AB_CALGRANT_B")
    /*      */    @Range(min = 0L)
    /*      */    @NotNull
    /*      */ private Integer studentAbCalgrantB;
    /*      */    @Column(name = "STUDENT_AC_FAMILY_SIZE")
    /*      */    @Range(min = 1L, max = 999L)
    /*      */    @NotNull
    /*      */ private Integer studentAcFamilySize;
    /*      */    @Column(name = "STUDENT_AF_FAMILY_CONTRIB")
    /*      */    @Range(min = -1500L, max = 999999L)
    /*      */    @NotNull
    /*      */ private Integer studentAfFamilyContrib;
    /*      */    @Column(name = "STUDENT_AG_NONLSU_ALLOWRANCE")
    /*      */ private BigDecimal studentAgNonlsuAllowrance;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_AH_LSU_ALLOWRANCE")
    /*      */ private String studentAhLsuAllowrance;
    /*      */    @Column(name = "STUDENT_AI_EDU_ALLOW_PER")
    /*      */    @DecimalMax("1.00")
    /*      */ private BigDecimal studentAiEduAllowPer;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_AJ_HOME_STATE")
    /*      */ private String studentAjHomeState;
    /*      */    @Column(name = "STUDENT_AK_NONCAL_GRANT")
    /*      */    @Range(min = 0L)
    /*      */    @NotNull
    /*      */ private Integer studentAkNoncalGrant;
    /*      */    @Size(max = 50)
    /*      */    @Column(name = "STUDENT_AL_OUT_SCHOLARSHIPS")
    /*      */ private String studentAlOutScholarships;
    /*      */    @Column(name = "STUDENT_AM_OUT_SCHOLARSHIP_AMT")
    /*      */    @Range(min = 0L)
    /*      */    @NotNull
    /*      */ private Integer studentAmOutScholarshipAmt;
    /*      */    @Size(max = 512)
    /*      */    @Column(name = "STUDENT_AN_PUB_NOTES")
    /*      */ private String studentAnPubNotes;
    /*      */    @Size(max = 512)
    /*      */    @Column(name = "STUDENT_AO_PRI_NOTES")
    /*      */ private String studentAoPriNotes;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_AP_SUB_LOANS")
    /*      */ private String studentApSubLoans;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_AQ_UNSUB_LOANS")
    /*      */ private String studentAqUnsubLoans;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_AR_FWS")
    /*      */ private String studentArFws;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_AS_SCHOLARSHIP1_NAME")
    /*      */ private String studentAsScholarship1Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_AT_SCHOLARSHIP1_NOTE")
    /*      */ private String studentAtScholarship1Note;
    /*      */    @Column(name = "STUDENT_AU_SCHOLARSHIP1_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentAuScholarship1Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_AV_SCHOLARSHIP2_NAME")
    /*      */ private String studentAvScholarship2Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_AW_SCHOLARSHIP2_NOTE")
    /*      */ private String studentAwScholarship2Note;
    /*      */    @Column(name = "STUDENT_AX_SCHOLARSHIP2_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentAxScholarship2Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_AY_SCHOLARSHIP3_NAME")
    /*      */ private String studentAyScholarship3Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_AZ_SCHOLARSHIP3_NOTE")
    /*      */ private String studentAzScholarship3Note;
    /*      */    @Column(name = "STUDENT_BA_SCHOLARSHIP3_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBaScholarship3Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_BB_SCHOLARSHIP4_NAME")
    /*      */ private String studentBbScholarship4Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_BC_SCHOLARSHIP4_NOTE")
    /*      */ private String studentBcScholarship4Note;
    /*      */    @Column(name = "STUDENT_BD_SCHOLARSHIP4_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBdScholarship4Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_BE_SCHOLARSHIP5_NAME")
    /*      */ private String studentBeScholarship5Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_BF_SCHOLARSHIP5_NOTE")
    /*      */ private String studentBfScholarship5Note;
    /*      */    @Column(name = "STUDENT_BG_SCHOLARSHIP5_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBgScholarship5Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_BH_SCHOLARSHIP6_NAME")
    /*      */ private String studentBhScholarship6Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_BI_SCHOLARSHIP6_NOTE")
    /*      */ private String studentBiScholarship6Note;
    /*      */    @Column(name = "STUDENT_BJ_SCHOLARSHIP6_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBjScholarship6Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_BK_SCHOLARSHIP7_NAME")
    /*      */ private String studentBkScholarship7Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_BL_SCHOLARSHIP7_NOTE")
    /*      */ private String studentBlScholarship7Note;
    /*      */    @Column(name = "STUDENT_BM_SCHOLARSHIP7_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBmScholarship7Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_BN_SCHOLARSHIP8_NAME")
    /*      */ private String studentBnScholarship8Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_BO_SCHOLARSHIP8_NOTE")
    /*      */ private String studentBoScholarship8Note;
    /*      */    @Column(name = "STUDENT_BP_SCHOLARSHIP8_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBpScholarship8Amt;
    /*      */    @Size(max = 70)
    /*      */    @Column(name = "STUDENT_BQ_SCHOLARSHIP9_NAME")
    /*      */ private String studentBqScholarship9Name;
    /*      */    @Size(max = 256)
    /*      */    @Column(name = "STUDENT_BR_SCHOLARSHIP9_NOTE")
    /*      */ private String studentBrScholarship9Note;
    /*      */    @Column(name = "STUDENT_BS_SCHOLARSHIP9_AMT")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentBsScholarship9Amt;
    /*      */    @Size(max = 10)
    /*      */    @Column(name = "STUDENT_BT_SUPERCOUNSELOR")
    /*      */ private String studentBtSupercounselor;
    /*      */    @Column(name = "STUDENT_BW_PROGRESS")
    /*      */ private Integer studentBwProgress;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_BZ_UPLOADED")
    /*      */ private String studentBzUploaded;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "STUDENT_CB_BANNER")
    /*      */ private String studentCbBanner;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 8)
    /*      */    @Column(name = "STUDENT_PASSWORD")
    /*      */ private String studentPassword;
    /*      */    @Size(max = 6)
    /*      */    @Column(name = "STUDENT_TERM_START")
    /*      */ private String studentTermStart;
    /*      */    @Size(max = 6)
    /*      */    @Column(name = "STUDENT_TERM_END")
    /*      */ private String studentTermEnd;
    /*      */    @Size(max = 30)
    /*      */    @Column(name = "STUDENT_STUD_TYPE")
    /*      */ private String studentStudType;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "PICKUP_IND")
    /*      */ private int pickupInd;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "ESTM_NUMB")
    /*      */ private int estmNumb;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 5)
    /*      */    @Column(name = "IND_EFC")
    /*      */ private String indEfc;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 5)
    /*      */    @Column(name = "IND_EXCLOANS")
    /*      */ private String indExcloans;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 5)
    /*      */    @Column(name = "IND_EALSU")
    /*      */ private String indEalsu;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Size(min = 1, max = 5)
    /*      */    @Column(name = "IND_EANONLSU")
    /*      */ private String indEanonlsu;
    /*      */    @Column(name = "STUDENT_AD_FAMILY_INCOME")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentAdFamilyIncome;
    /*      */    @Column(name = "STUDENT_AE_FAMILY_ASSET")
    /*      */    @Min(0L)
    /*      */    @NotNull
    /*      */ private Integer studentAeFamilyAsset;
    /*      */    @Column(name = "STUDENT_BY_DOM")
    /*      */    @Temporal(TemporalType.TIMESTAMP)
    /*      */ private Date studentByDom;
    /*      */    @Column(name = "STUDENT_BV_DOE")
    /*      */    @Temporal(TemporalType.TIMESTAMP)
    /*      */ private Date studentBvDoe;
    /*      */    @Size(max = 3)
    /*      */    @Column(name = "ADJ_CALGRANT_IND")
    /*      */ private String adjCalgrantInd;
    /*      */    @Size(max = 1, min = 1)
    /*      */    @NotNull
    /*      */    @NotEmpty
    /*      */    @Column(name = "SEX")
    /*      */ private String sex;
    /*      */    @Size(max = 7)
    /*      */    @Column(name = "HOME_ADDR_APT")
    /*      */ private String homeAddrApt;
    /*      */    @Column(name = "SCHOLASHIP1ID")
    /*      */ private Integer fund1id;
    /*      */    @Column(name = "SCHOLASHIP2ID")
    /*      */ private Integer fund2id;
    /*      */    @Column(name = "SCHOLASHIP3ID")
    /*      */ private Integer fund3id;
    /*      */    @Column(name = "SCHOLASHIP4ID")
    /*      */ private Integer fund4id;
    /*      */    @Column(name = "SCHOLASHIP5ID")
    /*      */ private Integer fund5id;
    /*      */    @Column(name = "SCHOLASHIP6ID")
    /*      */ private Integer fund6id;
    /*      */    @Column(name = "SCHOLASHIP7ID")
    /*      */ private Integer fund7id;
    /*      */    @Column(name = "SCHOLASHIP8ID")
    /*      */ private Integer fund8id;
    /*      */    @Column(name = "SCHOLASHIP9ID")
    /*      */ private Integer fund9id;
    /*      */    @Size(max = 12)
    /*      */    @Column(name = "TZDOE")
    /*      */ private String tzdoe;
    /*      */    @Size(max = 12)
    /*      */    @Column(name = "TZDOM")
    /*      */ private String tzdom;
    /*      */    @Size(max = 12)
    /*      */    @Column(name = "TZUP")
    /*      */ private String tzup;
    /*      */    @Size(max = 12)
    /*      */    @Column(name = "TZDOWN")
    /*      */ private String tzdown;
    /*      */    @Column(name = "CLIENT_ID")
    /*      */ private Integer clientId;
    /*      */    @Column(name = "COUNSELOR_ID")
    /*      */ private Integer counselorId;
    /*      */    @Size(max = 100)
    /*      */    @Column(name = "MOD_ROOT")
    /*      */ private String modRoot;
    /*      */    @Size(max = 100)
    /*      */    @Column(name = "MOD_PRE")
    /*      */ private String modPre;
    /*      */    @Size(max = 30)
    /*      */    @Column(name = "STUDENT_BX_MOD_COUNSELOR")
    /*      */ private String studentBxModCounselor;
    /*      */    @Size(max = 30)
    /*      */    @Column(name = "STUDENT_BU_ORIG_COUNSELOR")
    /*      */ private String studentBuOrigCounselor;
    /*      */    @Column(name = "COUNSELOR_ORIG")
    /*      */ private Integer counselorOrig;
    /*      */    @Column(name = "COUNSELOR_MOD")
    /*      */ private Integer counselorMod;
    /*      */    @Size(max = 20)
    /*      */    @Column(name = "LOST_TZ")
    /*      */ private String lostTz;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "DID")
    /*      */ private long did;
    /*      */    @Size(max = 12)
    /*      */    @Column(name = "TZDID")
    /*      */ private String tzdid;
    /*      */    @Size(max = 32)
    /*      */    @Column(name = "DIDSTR")
    /*      */ private String didstr;
    /*      */    @Size(max = 120)
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "STUDENT_USER_NAME")
    /*      */ private String studentUserName;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "DUP")
    /*      */ private long dup;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "DDOWN")
    /*      */ private long ddown;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "DDOE")
    /*      */ private long ddoe;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "DDOM")
    /*      */ private long ddom;
    /*      */    @Basic(optional = false)
    /*      */    @NotNull
    /*      */    @Column(name = "LOST_TIME")
    /*      */ private long lostTime;
    /*      */    @Size(max = 100)
    /*      */    @Column(name = "LOST_TO_MASTER")
    /*      */ private String lostToMaster;
    /*      */    @Size(max = 100)
    /*      */    @Column(name = "LOST_TO_LOCAL")
    /*      */ private String lostToLocal;
    /*      */    @Column(name = "PRT_TIMES")
    /*      */ private Integer prtTimes;
    /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(1L)
    /*      */    @Column(name = "RETURNING_")
    /*      */ private Integer returnStdInd;
    /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(1L)
    /*      */    @Column(name = "NC")
    /*      */ private Integer ncStdInd;
    /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(4L)
    /*      */    @Column(name = "TERMS")
    /*  695 */ private Integer terms = Integer.valueOf(0);
    /*      */
 /*      */
 /*      */
 /*      */
 /*      */    @Size(max = 6)
    /*      */    @Column(name = "PUSER_ID")
    /*      */ private String puser_id;
    /*      */
 /*      */
 /*      */
 /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(100L)
    /*      */    @Column(name = "EA_LSU_PERC")
    /*  710 */ private Integer ea_lsu_perc = Integer.valueOf(0);
    /*      */
 /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(100L)
    /*      */    @Column(name = "EA_NONLSU_PERC")
    /*  716 */ private Integer ea_nonlsu_perc = Integer.valueOf(0);
    /*      */
 /*      */    @Size(max = 6)
    /*      */    @Column(name = "TERM_CODE1")
    /*      */ private String term_code1;
    /*      */
 /*      */    @Size(max = 6)
    /*      */    @Column(name = "TERM_CODE2")
    /*      */ private String term_code2;
    /*      */
 /*      */    @Size(max = 6)
    /*      */    @Column(name = "TERM_CODE3")
    /*      */ private String term_code3;
    /*      */
 /*      */    @Size(max = 6)
    /*      */    @Column(name = "TERM_CODE4")
    /*      */ private String term_code4;
    /*      */
 /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(100L)
    /*      */    @Column(name = "TERM_LOAD1")
    /*  738 */ private Integer term_load1 = Integer.valueOf(0);
    /*      */
 /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(100L)
    /*      */    @Column(name = "TERM_LOAD2")
    /*  744 */ private Integer term_load2 = Integer.valueOf(0);
    /*      */
 /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(100L)
    /*      */    @Column(name = "TERM_LOAD3")
    /*  750 */ private Integer term_load3 = Integer.valueOf(0);
    /*      */
 /*      */    @NotNull
    /*      */    @Min(0L)
    /*      */    @Max(100L)
    /*      */    @Column(name = "TERM_LOAD4")
    /*  756 */ private Integer term_load4 = Integer.valueOf(0);
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("99.9")
    /*      */    @Column(name = "TERM_UNIT1")
    /*  758 */ private BigDecimal term_unit1 = new BigDecimal("0");
    /*      */
 /*      */    @NotNull
    /*      */    @DecimalMin("0.0")
    /*      */    @DecimalMax("99.9")
    /*      */    @Column(name = "TERM_UNIT2")
    /*  764 */ private BigDecimal term_unit2 = new BigDecimal("0");
    /*      */
 /*      */
 /*      */    @NotNull
    /*      */    @DecimalMin("0.0")
    /*      */    @DecimalMax("99.9")
    /*      */    @Column(name = "TERM_UNIT3")
    /*  771 */ private BigDecimal term_unit3 = new BigDecimal("0");
    /*      */
 /*      */
 /*      */    @NotNull
    /*      */    @DecimalMin("0.0")
    /*      */    @DecimalMax("99.9")
    /*      */    @Column(name = "TERM_UNIT4")
    /*  778 */ private BigDecimal term_unit4 = new BigDecimal("0");
    /*      */
 /*      */
 /*      */    @Size(max = 50)
    /*      */    @Column(name = "TERM_PROG1")
    /*      */ private String term_prog1;
    /*      */
 /*      */
 /*      */    @Size(max = 50)
    /*      */    @Column(name = "TERM_PROG2")
    /*      */ private String term_prog2;
    /*      */
 /*      */
 /*      */    @Size(max = 50)
    /*      */    @Column(name = "TERM_PROG3")
    /*      */ private String term_prog3;
    /*      */
 /*      */
 /*      */    @Size(max = 50)
    /*      */    @Column(name = "TERM_PROG4")
    /*      */ private String term_prog4;
    /*      */
 /*      */
 /*      */    @Min(-1L)
    /*      */    @Max(1L)
    /*      */    @Column(name = "STD_TRANSFER_IND")
    /*  804 */ private Integer std_transfer_ind = Integer.valueOf(-1);
    /*      */
 /*      */
 /*      */    @Min(-1L)
    /*      */    @Max(1L)
    /*      */    @Column(name = "STD_1ST_FRESHMEN")
    /*  810 */ private Integer std_1st_freshmen = Integer.valueOf(-1);
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
 /*      */    @OneToMany
    /*      */    @JoinColumn(name = "RECID")
    /*      */ private List<Print> pdfs;

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
 /*      */ public Student() {

        /*  852 */ setInitVals();
        /*      */    }

    /*      */
 /*      */ public Student(String recid) {
        /*  856 */ this.recid = recid;

        /*  857 */ setInitVals();
        /*      */    }

    /*      */
 /*      */ public Student(String recid, short studentFisy, String studentPassword, int pickupInd, int estmNumb, int studentNumb, String indEfc, String indExcloans, String indEalsu, String indEanonlsu, int homecostudies, long dup, long ddown, long ddoe, long ddom, long lostTime) {
        /*  861 */
        this.recid = recid;
        /*  862 */ this.studentFisy = studentFisy;
        /*  863 */ this.studentPassword = studentPassword;
        /*  864 */ this.pickupInd = pickupInd;
        /*  865 */ this.estmNumb = estmNumb;
        /*  866 */ this.studentNumb = Integer.valueOf(studentNumb);
        /*  867 */ this.indEfc = indEfc;
        /*  868 */ this.indExcloans = indExcloans;
        /*  869 */ this.indEalsu = indEalsu;
        /*  870 */ this.indEanonlsu = indEanonlsu;
        /*  871 */ this.homecostudies = Integer.valueOf(homecostudies);
        /*  872 */ this.dup = dup;
        /*  873 */ this.ddown = ddown;
        /*  874 */ this.ddoe = ddoe;
        /*  875 */ this.ddom = ddom;
        /*  876 */ this.lostTime = lostTime;
        /*  877 */ setInitVals();
        /*      */    }

    /*      */
 /*      */
 /*      */ public void setInitVals() {
        /*  882 */ this.studentLIntlStud = "no";
        /*  883 */ this.studentMMarry = "no";
        /*  884 */ this.studentNSda = "no";
        /*  885 */ this.studentUAcademic = "FR";
        /*  886 */ this.studentWDorm = "no";
        /*  887 */ this.studentXFafsa = "no";
        /*  888 */ this.studentYIndept = "no";
        /*  889 */ this.studentZCalgrant = "no";
                   this.studentFPellGrant="no";
        /*  890 */ this.studentAcFamilySize = Integer.valueOf(2);
        /*      */     //this.recid = "tmpid";
//System.out.println("Rec id " + this.recid);
/*  892 */ this.studentSMerit = "";
        /*      */
 /*      */
 /*  895 */ this.studentAhLsuAllowrance = "no";
        /*  896 */ this.studentAiEduAllowPer = new BigDecimal(0);
        /*      */
 /*  898 */ this.studentAgNonlsuAllowrance = new BigDecimal(0);
        /*  899 */ this.indEalsu = "no";
        /*  900 */ this.indEanonlsu = "no";
        /*      */
 /*  902 */ this.indEfc = "no";
        /*  903 */ this.indExcloans = "no";
        /*  904 */ this.adjCalgrantInd = "no";
        /*      */
 /*  906 */ this.studentAaCalgrantA = Integer.valueOf(0);
        /*  907 */ this.studentAbCalgrantB = Integer.valueOf(0);
        /*      */
 /*  909 */ this.studentApSubLoans = "yes";
        /*  910 */ this.studentAqUnsubLoans = "yes";
        /*  911 */ this.studentArFws = "yes";
        /*      */
 /*  913 */ this.studentQSatV = Integer.valueOf(0);
        /*  914 */ this.studentPGpa = new BigDecimal(0.0D);
        /*  915 */ this.studentQSat = Integer.valueOf(0);
        /*  916 */ this.studentRAct = Integer.valueOf(0);
        /*      */
 /*  918 */ this.studentVFamily = "";
        /*  919 */ this.studentAcFamilySize = Integer.valueOf(1);
        /*  920 */ this.studentAfFamilyContrib = Integer.valueOf(999999);
        /*  921 */ this.studentAdFamilyIncome = Integer.valueOf(0);
        /*  922 */ this.studentAeFamilyAsset = Integer.valueOf(0);
        /*      */
 /*  924 */ this.studentApSubLoans = "yes";
        /*  925 */ this.studentAqUnsubLoans = "yes";
        /*  926 */ this.studentArFws = "yes";
        /*  927 */ this.studentAkNoncalGrant = Integer.valueOf(0);
        /*  928 */ this.studentAmOutScholarshipAmt = Integer.valueOf(0);
        /*      */
 /*  930 */ this.studentAuScholarship1Amt = Integer.valueOf(0);
        this.studentAxScholarship2Amt = Integer.valueOf(0);
        this.studentBaScholarship3Amt = Integer.valueOf(0);
        /*  931 */ this.studentBdScholarship4Amt = Integer.valueOf(0);
        this.studentBgScholarship5Amt = Integer.valueOf(0);
        this.studentBjScholarship6Amt = Integer.valueOf(0);
        /*  932 */ this.studentBmScholarship7Amt = Integer.valueOf(0);
        this.studentBpScholarship8Amt = Integer.valueOf(0);
        this.studentBsScholarship9Amt = Integer.valueOf(0);
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  941 */ this.studentBkScholarship7Name = "";
        /*  942 */ this.studentBnScholarship8Name = "";
        /*  943 */ this.studentBqScholarship9Name = "";
        /*      */
 /*  945 */ this.studentAtScholarship1Note = " ";
        /*  946 */ this.studentAwScholarship2Note = " ";
        /*  947 */ this.studentAzScholarship3Note = " ";
        /*  948 */ this.studentBcScholarship4Note = " ";
        /*  949 */ this.studentBfScholarship5Note = " ";
        /*  950 */ this.studentBiScholarship6Note = " ";
        /*  951 */ this.studentBlScholarship7Note = " ";
        /*  952 */ this.studentBoScholarship8Note = " ";
        /*  953 */ this.studentBrScholarship9Note = " ";
        /*      */
 /*  955 */ this.fund1id = Integer.valueOf(0);
        /*  956 */ this.fund2id = Integer.valueOf(0);
        /*  957 */ this.fund3id = Integer.valueOf(0);
        /*  958 */ this.fund4id = Integer.valueOf(0);
        /*  959 */ this.fund5id = Integer.valueOf(0);
        /*  960 */ this.fund6id = Integer.valueOf(0);
        /*  961 */ this.fund7id = Integer.valueOf(0);
        /*  962 */ this.fund8id = Integer.valueOf(0);
        /*  963 */ this.fund9id = Integer.valueOf(0);
        /*      */
 /*  965 */ this.sex = "N";
        /*  966 */ this.homecostudies = Integer.valueOf(0);
        /*  967 */ this.homeAddrApt = "";
        /*  968 */ this.studentDDob = "";
        /*      */
 /*  970 */ this.returnStdInd = Integer.valueOf(0);
        /*  971 */ this.ncStdInd = Integer.valueOf(0);
        /*      */
 /*      */
 /*  974 */ this.studentFisy = 2024;//2023
        /*  975 */ setStudentUserName("|||");
        /*  976 */ setStudentPassword("password");
        /*  977 */ setStudentBwProgress(Integer.valueOf(5));
        /*  978 */ setStudentStudType("FYUG");
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getStudentFisy() {
        /*  983 */ return this.studentFisy;
        /*      */    }

    /*      */
 /*      */ public void setStudentFisy(int studentFisy) {
        /*  987 */ this.studentFisy = studentFisy;
        /*      */    }

    /*      */
 /*      */ public String getStudentALsuid() {
        /*  991 */ return this.studentALsuid;
        /*      */    }

    /*      */
 /*      */ public void setStudentALsuid(String studentALsuid) {
        /*  995 */ this.studentALsuid = studentALsuid;
        /*      */    }

    /*      */
 /*      */ public String getStudentBLastname() {
        /*  999 */ return this.studentBLastname;
        /*      */    }

    /*      */
 /*      */ public void setStudentBLastname(String studentBLastname) {
        /* 1003 */ this.studentBLastname = studentBLastname;
        /*      */    }

    /*      */
 /*      */ public String getStudentCFirstname() {
        /* 1007 */ return this.studentCFirstname;
        /*      */    }

    /*      */
 /*      */ public void setStudentCFirstname(String studentCFirstname) {
        /* 1011 */ this.studentCFirstname = studentCFirstname;
        /*      */    }

    /*      */
 /*      */ public String getStudentDDob() {
        /* 1015 */ return this.studentDDob;
        /*      */    }

    /*      */
 /*      */ public void setStudentDDob(String studentDDob) {
        /* 1019 */ this.studentDDob = studentDDob;
        /*      */    }

    /*      */
 /*      */ public String getStudentEEmail() {
        /* 1023 */ return this.studentEEmail;
        /*      */    }

    /*      */
 /*      */ public void setStudentEEmail(String studentEEmail) {
        /* 1027 */ this.studentEEmail = studentEEmail;
        /*      */    }

    /*      */
 /*      */ public String getStudentFPhone() {
        /* 1031 */ return this.studentFPhone;
        /*      */    }

    /*      */
 /*      */ public void setStudentFPhone(String studentFPhone) {
        /* 1035 */ this.studentFPhone = studentFPhone;
        /*      */    }

    /*      */
 /*      */ public String getStudentGStreet() {
        /* 1039 */ return this.studentGStreet;
        /*      */    }

    /*      */
 /*      */ public void setStudentGStreet(String studentGStreet) {
        /* 1043 */ this.studentGStreet = studentGStreet;
        /*      */    }

    /*      */
 /*      */ public String getStudentHCity() {
        /* 1047 */ return this.studentHCity;
        /*      */    }

    /*      */
 /*      */ public void setStudentHCity(String studentHCity) {
        /* 1051 */ this.studentHCity = studentHCity;
        /*      */    }

    /*      */
 /*      */ public String getStudentIState() {
        /* 1055 */ return this.studentIState;
        /*      */    }

    /*      */
 /*      */ public void setStudentIState(String studentIState) {
        /* 1059 */ this.studentIState = studentIState;
        /*      */    }

    /*      */
 /*      */ public String getStudentJZip() {
        /* 1063 */ return this.studentJZip;
        /*      */    }

    /*      */
 /*      */ public void setStudentJZip(String studentJZip) {
        /* 1067 */ this.studentJZip = studentJZip;
        /*      */    }

    /*      */
 /*      */ public String getStudentKCountry() {
        /* 1071 */ return this.studentKCountry;
        /*      */    }

    /*      */
 /*      */ public void setStudentKCountry(String studentKCountry) {
        /* 1075 */ this.studentKCountry = studentKCountry;
        /*      */    }

    /*      */
 /*      */ public String getStudentLIntlStud() {
        /* 1079 */ return this.studentLIntlStud;
        /*      */    }

    /*      */
 /*      */ public void setStudentLIntlStud(String studentLIntlStud) {
        /* 1083 */ this.studentLIntlStud = studentLIntlStud;
        /*      */    }

    /*      */
 /*      */ public String getStudentMMarry() {
        /* 1087 */ return this.studentMMarry;
        /*      */    }

    /*      */
 /*      */ public void setStudentMMarry(String studentMMarry) {
        /* 1091 */ this.studentMMarry = studentMMarry;
        /*      */    }

    /*      */
 /*      */ public String getStudentNSda() {
        /* 1095 */ return this.studentNSda;
        /*      */    }

    /*      */
 /*      */ public void setStudentNSda(String studentNSda) {
        /* 1099 */ this.studentNSda = studentNSda;
        /*      */    }

    /*      */
 /*      */ public String getStudentOLastSchool() {
        /* 1103 */ return this.studentOLastSchool;
        /*      */    }

    /*      */
 /*      */ public void setStudentOLastSchool(String studentOLastSchool) {
        /* 1107 */ this.studentOLastSchool = studentOLastSchool;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getStudentPGpa() {
        /* 1111 */ return this.studentPGpa;
        /*      */    }

    /*      */
 /*      */ public void setStudentPGpa(BigDecimal studentPGpa) {
        /* 1115 */ this.studentPGpa = studentPGpa;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentQSat() {
        /* 1119 */ return this.studentQSat;
        /*      */    }

    /*      */
 /*      */ public void setStudentQSat(Integer studentQSat) {
        /* 1123 */ this.studentQSat = studentQSat;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentRAct() {
        /* 1127 */ return this.studentRAct;
        /*      */    }

    /*      */
 /*      */ public void setStudentRAct(Integer studentRAct) {
        /* 1131 */ this.studentRAct = studentRAct;
        /*      */    }

    /*      */
 /*      */ public String getStudentSMerit() {
        /* 1135 */ return this.studentSMerit;
        /*      */    }

    /*      */
 /*      */ public void setStudentSMerit(String studentSMerit) {
        /* 1139 */ this.studentSMerit = studentSMerit;
        /*      */    }

    /*      */
 /*      */ public String getStudentTMajor() {
        /* 1143 */ return this.studentTMajor;
        /*      */    }

    /*      */
 /*      */ public void setStudentTMajor(String studentTMajor) {
        /* 1147 */ this.studentTMajor = studentTMajor;
        /*      */    }

    /*      */
 /*      */ public String getStudentUAcademic() {
        /* 1151 */ return this.studentUAcademic;
        /*      */    }

    /*      */
 /*      */ public void setStudentUAcademic(String studentUAcademic) {
        /* 1155 */ this.studentUAcademic = studentUAcademic;
        /*      */    }

    /*      */
 /*      */ public String getStudentVFamily() {
        /* 1159 */ return this.studentVFamily;
        /*      */    }

    /*      */
 /*      */ public void setStudentVFamily(String studentVFamily) {
        /* 1163 */ this.studentVFamily = studentVFamily;
        /*      */    }

    /*      */
 /*      */ public String getStudentWDorm() {
        /* 1167 */ return this.studentWDorm;
        /*      */    }

    /*      */
 /*      */ public void setStudentWDorm(String studentWDorm) {
        /* 1171 */ this.studentWDorm = studentWDorm;
        /*      */    }

    /*      */
 /*      */ public String getStudentXFafsa() {
        /* 1175 */ return this.studentXFafsa;
        /*      */    }

    /*      */
 /*      */ public void setStudentXFafsa(String studentXFafsa) {
        /* 1179 */ this.studentXFafsa = studentXFafsa;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getStudentYIndept() {
        /* 1184 */ return this.studentYIndept;
        /*      */    }

    /*      */
 /*      */ public void setStudentYIndept(String studentYIndept) {
        /* 1188 */ this.studentYIndept = studentYIndept;
        /*      */    }

    /*      */
 /*      */ public String getStudentZCalgrant() {
        /* 1192 */ return this.studentZCalgrant;
        /*      */    }

    /*      */
 /*      */ public void setStudentZCalgrant(String studentZCalgrant) {
        /* 1196 */ this.studentZCalgrant = studentZCalgrant;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAaCalgrantA() {
        /* 1200 */ return this.studentAaCalgrantA;
        /*      */    }

    /*      */
 /*      */ public void setStudentAaCalgrantA(Integer studentAaCalgrantA) {
        /* 1204 */ this.studentAaCalgrantA = studentAaCalgrantA;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAbCalgrantB() {
        /* 1208 */ return this.studentAbCalgrantB;
        /*      */    }

    /*      */
 /*      */ public void setStudentAbCalgrantB(Integer studentAbCalgrantB) {
        /* 1212 */ this.studentAbCalgrantB = studentAbCalgrantB;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAcFamilySize() {
        /* 1216 */ return this.studentAcFamilySize;
        /*      */    }

    /*      */
 /*      */ public void setStudentAcFamilySize(Integer studentAcFamilySize) {
        /* 1220 */ this.studentAcFamilySize = studentAcFamilySize;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAfFamilyContrib() {
        /* 1224 */ return this.studentAfFamilyContrib;
        /*      */    }

    /*      */
 /*      */ public void setStudentAfFamilyContrib(Integer studentAfFamilyContrib) {
        /* 1228 */ this.studentAfFamilyContrib = studentAfFamilyContrib;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getStudentAgNonlsuAllowrance() {
        /* 1232 */ return this.studentAgNonlsuAllowrance;
        /*      */    }

    /*      */
 /*      */ public void setStudentAgNonlsuAllowrance(BigDecimal studentAgNonlsuAllowrance) {
        /* 1236 */ this.studentAgNonlsuAllowrance = studentAgNonlsuAllowrance;
        /*      */    }

    /*      */
 /*      */ public String getStudentAhLsuAllowrance() {
        /* 1240 */ return this.studentAhLsuAllowrance;
        /*      */    }

    /*      */
 /*      */ public void setStudentAhLsuAllowrance(String studentAhLsuAllowrance) {
        /* 1244 */ this.studentAhLsuAllowrance = studentAhLsuAllowrance;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getStudentAiEduAllowPer() {
        /* 1248 */ return this.studentAiEduAllowPer;
        /*      */    }

    /*      */
 /*      */ public void setStudentAiEduAllowPer(BigDecimal studentAiEduAllowPer) {
        /* 1252 */ this.studentAiEduAllowPer = studentAiEduAllowPer;
        /*      */    }

    /*      */
 /*      */ public String getStudentAjHomeState() {
        /* 1256 */ return this.studentAjHomeState;
        /*      */    }

    /*      */
 /*      */ public void setStudentAjHomeState(String studentAjHomeState) {
        /* 1260 */ this.studentAjHomeState = studentAjHomeState;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAkNoncalGrant() {
        /* 1264 */ return this.studentAkNoncalGrant;
        /*      */    }

    /*      */
 /*      */ public void setStudentAkNoncalGrant(Integer studentAkNoncalGrant) {
        /* 1268 */ this.studentAkNoncalGrant = studentAkNoncalGrant;
        /*      */    }

    /*      */
 /*      */ public String getStudentAlOutScholarships() {
        /* 1272 */ return this.studentAlOutScholarships;
        /*      */    }

    /*      */
 /*      */ public void setStudentAlOutScholarships(String studentAlOutScholarships) {
        /* 1276 */ this.studentAlOutScholarships = studentAlOutScholarships;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAmOutScholarshipAmt() {
        /* 1280 */ return this.studentAmOutScholarshipAmt;
        /*      */    }

    /*      */
 /*      */ public void setStudentAmOutScholarshipAmt(Integer studentAmOutScholarshipAmt) {
        /* 1284 */ this.studentAmOutScholarshipAmt = studentAmOutScholarshipAmt;
        /*      */    }

    /*      */
 /*      */ public String getStudentAnPubNotes() {
        /* 1288 */ return this.studentAnPubNotes;
        /*      */    }

    /*      */
 /*      */ public void setStudentAnPubNotes(String studentAnPubNotes) {
        /* 1292 */ this.studentAnPubNotes = studentAnPubNotes;
        /*      */    }

    /*      */
 /*      */ public String getStudentAoPriNotes() {
        /* 1296 */ return this.studentAoPriNotes;
        /*      */    }

    /*      */
 /*      */ public void setStudentAoPriNotes(String studentAoPriNotes) {
        /* 1300 */ this.studentAoPriNotes = studentAoPriNotes;
        /*      */    }

    /*      */
 /*      */ public String getStudentApSubLoans() {
        /* 1304 */ return this.studentApSubLoans;
        /*      */    }

    /*      */
 /*      */ public void setStudentApSubLoans(String studentApSubLoans) {
        /* 1308 */ this.studentApSubLoans = studentApSubLoans;
        /*      */    }

    /*      */
 /*      */ public String getStudentAqUnsubLoans() {
        /* 1312 */ return this.studentAqUnsubLoans;
        /*      */    }

    /*      */
 /*      */ public void setStudentAqUnsubLoans(String studentAqUnsubLoans) {
        /* 1316 */ this.studentAqUnsubLoans = studentAqUnsubLoans;
        /*      */    }

    /*      */
 /*      */ public String getStudentArFws() {
        /* 1320 */ return this.studentArFws;
        /*      */    }

    /*      */
 /*      */ public void setStudentArFws(String studentArFws) {
        /* 1324 */ this.studentArFws = studentArFws;
        /*      */    }

    /*      */
 /*      */ public String getStudentAsScholarship1Name() {
        /* 1328 */ return this.studentAsScholarship1Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentAsScholarship1Name(String studentAsScholarship1Name) {
        /* 1332 */ this.studentAsScholarship1Name = studentAsScholarship1Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentAtScholarship1Note() {
        /* 1336 */ return this.studentAtScholarship1Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentAtScholarship1Note(String studentAtScholarship1Note) {
        /* 1340 */ this.studentAtScholarship1Note = studentAtScholarship1Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAuScholarship1Amt() {
        /* 1344 */ return this.studentAuScholarship1Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentAuScholarship1Amt(Integer studentAuScholarship1Amt) {
        /* 1348 */ this.studentAuScholarship1Amt = studentAuScholarship1Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentAvScholarship2Name() {
        /* 1352 */ return this.studentAvScholarship2Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentAvScholarship2Name(String studentAvScholarship2Name) {
        /* 1356 */ this.studentAvScholarship2Name = studentAvScholarship2Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentAwScholarship2Note() {
        /* 1360 */ return this.studentAwScholarship2Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentAwScholarship2Note(String studentAwScholarship2Note) {
        /* 1364 */ this.studentAwScholarship2Note = studentAwScholarship2Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAxScholarship2Amt() {
        /* 1368 */ return this.studentAxScholarship2Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentAxScholarship2Amt(Integer studentAxScholarship2Amt) {
        /* 1372 */ this.studentAxScholarship2Amt = studentAxScholarship2Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentAyScholarship3Name() {
        /* 1376 */ return this.studentAyScholarship3Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentAyScholarship3Name(String studentAyScholarship3Name) {
        /* 1380 */ this.studentAyScholarship3Name = studentAyScholarship3Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentAzScholarship3Note() {
        /* 1384 */ return this.studentAzScholarship3Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentAzScholarship3Note(String studentAzScholarship3Note) {
        /* 1388 */ this.studentAzScholarship3Note = studentAzScholarship3Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBaScholarship3Amt() {
        /* 1392 */ return this.studentBaScholarship3Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBaScholarship3Amt(Integer studentBaScholarship3Amt) {
        /* 1396 */ this.studentBaScholarship3Amt = studentBaScholarship3Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBbScholarship4Name() {
        /* 1400 */ return this.studentBbScholarship4Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentBbScholarship4Name(String studentBbScholarship4Name) {
        /* 1404 */ this.studentBbScholarship4Name = studentBbScholarship4Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentBcScholarship4Note() {
        /* 1408 */ return this.studentBcScholarship4Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentBcScholarship4Note(String studentBcScholarship4Note) {
        /* 1412 */ this.studentBcScholarship4Note = studentBcScholarship4Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBdScholarship4Amt() {
        /* 1416 */ return this.studentBdScholarship4Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBdScholarship4Amt(Integer studentBdScholarship4Amt) {
        /* 1420 */ this.studentBdScholarship4Amt = studentBdScholarship4Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBeScholarship5Name() {
        /* 1424 */ return this.studentBeScholarship5Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentBeScholarship5Name(String studentBeScholarship5Name) {
        /* 1428 */ this.studentBeScholarship5Name = studentBeScholarship5Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentBfScholarship5Note() {
        /* 1432 */ return this.studentBfScholarship5Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentBfScholarship5Note(String studentBfScholarship5Note) {
        /* 1436 */ this.studentBfScholarship5Note = studentBfScholarship5Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBgScholarship5Amt() {
        /* 1440 */ return this.studentBgScholarship5Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBgScholarship5Amt(Integer studentBgScholarship5Amt) {
        /* 1444 */ this.studentBgScholarship5Amt = studentBgScholarship5Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBhScholarship6Name() {
        /* 1448 */ return this.studentBhScholarship6Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentBhScholarship6Name(String studentBhScholarship6Name) {
        /* 1452 */ this.studentBhScholarship6Name = studentBhScholarship6Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentBiScholarship6Note() {
        /* 1456 */ return this.studentBiScholarship6Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentBiScholarship6Note(String studentBiScholarship6Note) {
        /* 1460 */ this.studentBiScholarship6Note = studentBiScholarship6Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBjScholarship6Amt() {
        /* 1464 */ return this.studentBjScholarship6Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBjScholarship6Amt(Integer studentBjScholarship6Amt) {
        /* 1468 */ this.studentBjScholarship6Amt = studentBjScholarship6Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBkScholarship7Name() {
        /* 1472 */ return this.studentBkScholarship7Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentBkScholarship7Name(String studentBkScholarship7Name) {
        /* 1476 */ this.studentBkScholarship7Name = studentBkScholarship7Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentBlScholarship7Note() {
        /* 1480 */ return this.studentBlScholarship7Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentBlScholarship7Note(String studentBlScholarship7Note) {
        /* 1484 */ this.studentBlScholarship7Note = studentBlScholarship7Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBmScholarship7Amt() {
        /* 1488 */ return this.studentBmScholarship7Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBmScholarship7Amt(Integer studentBmScholarship7Amt) {
        /* 1492 */ this.studentBmScholarship7Amt = studentBmScholarship7Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBnScholarship8Name() {
        /* 1496 */ return this.studentBnScholarship8Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentBnScholarship8Name(String studentBnScholarship8Name) {
        /* 1500 */ this.studentBnScholarship8Name = studentBnScholarship8Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentBoScholarship8Note() {
        /* 1504 */ return this.studentBoScholarship8Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentBoScholarship8Note(String studentBoScholarship8Note) {
        /* 1508 */ this.studentBoScholarship8Note = studentBoScholarship8Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBpScholarship8Amt() {
        /* 1512 */ return this.studentBpScholarship8Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBpScholarship8Amt(Integer studentBpScholarship8Amt) {
        /* 1516 */ this.studentBpScholarship8Amt = studentBpScholarship8Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBqScholarship9Name() {
        /* 1520 */ return this.studentBqScholarship9Name;
        /*      */    }

    /*      */
 /*      */ public void setStudentBqScholarship9Name(String studentBqScholarship9Name) {
        /* 1524 */ this.studentBqScholarship9Name = studentBqScholarship9Name;
        /*      */    }

    /*      */
 /*      */ public String getStudentBrScholarship9Note() {
        /* 1528 */ return this.studentBrScholarship9Note;
        /*      */    }

    /*      */
 /*      */ public void setStudentBrScholarship9Note(String studentBrScholarship9Note) {
        /* 1532 */ this.studentBrScholarship9Note = studentBrScholarship9Note;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBsScholarship9Amt() {
        /* 1536 */ return this.studentBsScholarship9Amt;
        /*      */    }

    /*      */
 /*      */ public void setStudentBsScholarship9Amt(Integer studentBsScholarship9Amt) {
        /* 1540 */ this.studentBsScholarship9Amt = studentBsScholarship9Amt;
        /*      */    }

    /*      */
 /*      */ public String getStudentBtSupercounselor() {
        /* 1544 */ return this.studentBtSupercounselor;
        /*      */    }

    /*      */
 /*      */ public void setStudentBtSupercounselor(String studentBtSupercounselor) {
        /* 1548 */ this.studentBtSupercounselor = studentBtSupercounselor;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentBwProgress() {
        /* 1552 */ return this.studentBwProgress;
        /*      */    }

    /*      */
 /*      */ public void setStudentBwProgress(Integer studentBwProgress) {
        /* 1556 */ this.studentBwProgress = studentBwProgress;
        /*      */    }

    /*      */
 /*      */ public String getStudentBzUploaded() {
        /* 1560 */ return this.studentBzUploaded;
        /*      */    }

    /*      */
 /*      */ public void setStudentBzUploaded(String studentBzUploaded) {
        /* 1564 */ this.studentBzUploaded = studentBzUploaded;
        /*      */    }

    /*      */
 /*      */ public String getStudentCbBanner() {
        /* 1568 */ return this.studentCbBanner;
        /*      */    }

    /*      */
 /*      */ public void setStudentCbBanner(String studentCbBanner) {
        /* 1572 */ this.studentCbBanner = studentCbBanner;
        /*      */    }

    /*      */
 /*      */ public String getStudentPassword() {
        /* 1576 */ return this.studentPassword;
        /*      */    }

    /*      */
 /*      */ public void setStudentPassword(String studentPassword) {
        /* 1580 */ this.studentPassword = studentPassword;
        /*      */    }

    /*      */
 /*      */ public String getStudentTermStart() {
        /* 1584 */ return this.studentTermStart;
        /*      */    }

    /*      */
 /*      */ public void setStudentTermStart(String studentTermStart) {
        /* 1588 */ this.studentTermStart = studentTermStart;
        /*      */    }

    /*      */
 /*      */ public String getStudentTermEnd() {
        /* 1592 */ return this.studentTermEnd;
        /*      */    }

    /*      */
 /*      */ public void setStudentTermEnd(String studentTermEnd) {
        /* 1596 */ this.studentTermEnd = studentTermEnd;
        /*      */    }

    /*      */
 /*      */ public String getStudentStudType() {
        /* 1600 */ return this.studentStudType;
        /*      */    }

    /*      */
 /*      */ public void setStudentStudType(String studentStudType) {
        /* 1604 */ this.studentStudType = studentStudType;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentQSatV() {
        /* 1608 */ return this.studentQSatV;
        /*      */    }

    /*      */
 /*      */ public void setStudentQSatV(Integer studentQSatV) {
        /* 1612 */ this.studentQSatV = studentQSatV;
        /*      */    }

    /*      */
 /*      */ public int getPickupInd() {
        /* 1616 */ return this.pickupInd;
        /*      */    }

    /*      */
 /*      */ public void setPickupInd(int pickupInd) {
        /* 1620 */ this.pickupInd = pickupInd;
        /*      */    }

    /*      */
 /*      */ public int getEstmNumb() {
        /* 1624 */ return this.estmNumb;
        /*      */    }

    /*      */
 /*      */ public void setEstmNumb(int estmNumb) {
        /* 1628 */ this.estmNumb = estmNumb;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentNumb() {
        /* 1632 */ return this.studentNumb;
        /*      */    }

    /*      */
 /*      */ public void setStudentNumb(Integer studentNumb) {
        /* 1636 */ this.studentNumb = studentNumb;
        /*      */    }

    /*      */
 /*      */ public String getIndEfc() {
        /* 1640 */ return this.indEfc;
        /*      */    }

    /*      */
 /*      */ public void setIndEfc(String indEfc) {
        /* 1644 */ this.indEfc = indEfc;
        /*      */    }

    /*      */
 /*      */ public String getIndExcloans() {
        /* 1648 */ return this.indExcloans;
        /*      */    }

    /*      */
 /*      */ public void setIndExcloans(String indExcloans) {
        /* 1652 */ this.indExcloans = indExcloans;
        /*      */    }

    /*      */
 /*      */ public String getIndEalsu() {
        /* 1656 */ return this.indEalsu;
        /*      */    }

    /*      */
 /*      */ public void setIndEalsu(String indEalsu) {
        /* 1660 */ this.indEalsu = indEalsu;
        /*      */    }

    /*      */
 /*      */ public String getIndEanonlsu() {
        /* 1664 */ return this.indEanonlsu;
        /*      */    }

    /*      */
 /*      */ public void setIndEanonlsu(String indEanonlsu) {
        /* 1668 */ this.indEanonlsu = indEanonlsu;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAdFamilyIncome() {
        /* 1672 */ return this.studentAdFamilyIncome;
        /*      */    }

    /*      */
 /*      */ public void setStudentAdFamilyIncome(Integer studentAdFamilyIncome) {
        /* 1676 */ this.studentAdFamilyIncome = studentAdFamilyIncome;
        /*      */    }

    /*      */
 /*      */ public Integer getStudentAeFamilyAsset() {
        /* 1680 */ return this.studentAeFamilyAsset;
        /*      */    }

    /*      */
 /*      */ public void setStudentAeFamilyAsset(Integer studentAeFamilyAsset) {
        /* 1684 */ this.studentAeFamilyAsset = studentAeFamilyAsset;
        /*      */    }

    /*      */
 /*      */ public Date getStudentByDom() {
        /* 1688 */ return this.studentByDom;
        /*      */    }

    /*      */
 /*      */ public void setStudentByDom(Date studentByDom) {
        /* 1692 */ this.studentByDom = studentByDom;
        /*      */    }

    /*      */
 /*      */ public Date getStudentBvDoe() {
        /* 1696 */ return this.studentBvDoe;
        /*      */    }

    /*      */
 /*      */ public void setStudentBvDoe(Date studentBvDoe) {
        /* 1700 */ this.studentBvDoe = studentBvDoe;
        /*      */    }

    /*      */
 /*      */ public String getAdjCalgrantInd() {
        /* 1704 */ return this.adjCalgrantInd;
        /*      */    }

    /*      */
 /*      */ public void setAdjCalgrantInd(String adjCalgrantInd) {
        /* 1708 */ this.adjCalgrantInd = adjCalgrantInd;
        /*      */    }

    /*      */
 /*      */ public String getSex() {
        /* 1712 */ return this.sex;
        /*      */    }

    /*      */
 /*      */ public void setSex(String sex) {
        /* 1716 */ this.sex = sex;
        /*      */    }

    /*      */
 /*      */ public String getHomeAddrApt() {
        /* 1720 */ return this.homeAddrApt;
        /*      */    }

    /*      */
 /*      */ public void setHomeAddrApt(String homeAddrApt) {
        /* 1724 */ this.homeAddrApt = homeAddrApt;
        /*      */    }

    /*      */
 /*      */ public Integer getHomecostudies() {
        /* 1728 */ return this.homecostudies;
        /*      */    }

    /*      */
 /*      */ public void setHomecostudies(Integer homecostudies) {
        /* 1732 */ this.homecostudies = homecostudies;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getTzdoe() {
        /* 1737 */ return this.tzdoe;
        /*      */    }

    /*      */
 /*      */ public void setTzdoe(String tzdoe) {
        /* 1741 */ this.tzdoe = tzdoe;
        /*      */    }

    /*      */
 /*      */ public String getTzdom() {
        /* 1745 */ return this.tzdom;
        /*      */    }

    /*      */
 /*      */ public void setTzdom(String tzdom) {
        /* 1749 */ this.tzdom = tzdom;
        /*      */    }

    /*      */
 /*      */ public String getTzup() {
        /* 1753 */ return this.tzup;
        /*      */    }

    /*      */
 /*      */ public void setTzup(String tzup) {
        /* 1757 */ this.tzup = tzup;
        /*      */    }

    /*      */
 /*      */ public String getTzdown() {
        /* 1761 */ return this.tzdown;
        /*      */    }

    /*      */
 /*      */ public void setTzdown(String tzdown) {
        /* 1765 */ this.tzdown = tzdown;
        /*      */    }

    /*      */
 /*      */ public String getRecid() {
        /* 1769 */ return this.recid;
        /*      */    }

    /*      */
 /*      */ public void setRecid(String recid) {
        /* 1773 */ this.recid = recid;
        /*      */    }

    /*      */
 /*      */ public Integer getClientId() {
        /* 1777 */ return this.clientId;
        /*      */    }

    /*      */
 /*      */ public void setClientId(Integer clientId) {
        /* 1781 */ this.clientId = clientId;
        /*      */    }

    /*      */
 /*      */ public Integer getCounselorId() {
        /* 1785 */ return this.counselorId;
        /*      */    }

    /*      */
 /*      */ public void setCounselorId(Integer counselorId) {
        /* 1789 */ this.counselorId = counselorId;
        /*      */    }

    /*      */
 /*      */ public String getModRoot() {
        /* 1793 */ return this.modRoot;
        /*      */    }

    /*      */
 /*      */ public void setModRoot(String modRoot) {
        /* 1797 */ this.modRoot = modRoot;
        /*      */    }

    /*      */
 /*      */ public String getModPre() {
        /* 1801 */ return this.modPre;
        /*      */    }

    /*      */
 /*      */ public void setModPre(String modPre) {
        /* 1805 */ this.modPre = modPre;
        /*      */    }

    /*      */
 /*      */ public String getStudentBxModCounselor() {
        /* 1809 */ return this.studentBxModCounselor;
        /*      */    }

    /*      */
 /*      */ public void setStudentBxModCounselor(String studentBxModCounselor) {
        /* 1813 */ this.studentBxModCounselor = studentBxModCounselor;
        /*      */    }

    /*      */
 /*      */ public String getStudentBuOrigCounselor() {
        /* 1817 */ return this.studentBuOrigCounselor;
        /*      */    }

    /*      */
 /*      */ public void setStudentBuOrigCounselor(String studentBuOrigCounselor) {
        /* 1821 */ this.studentBuOrigCounselor = studentBuOrigCounselor;
        /*      */    }

    /*      */
 /*      */ public Integer getCounselorOrig() {
        /* 1825 */ return this.counselorOrig;
        /*      */    }

    /*      */
 /*      */ public void setCounselorOrig(Integer counselorOrig) {
        /* 1829 */ this.counselorOrig = counselorOrig;
        /*      */    }

    /*      */
 /*      */ public Integer getCounselorMod() {
        /* 1833 */ return this.counselorMod;
        /*      */    }

    /*      */
 /*      */ public void setCounselorMod(Integer counselorMod) {
        /* 1837 */ this.counselorMod = counselorMod;
        /*      */    }

    /*      */
 /*      */ public String getLostTz() {
        /* 1841 */ return this.lostTz;
        /*      */    }

    /*      */
 /*      */ public void setLostTz(String lostTz) {
        /* 1845 */ this.lostTz = lostTz;
        /*      */    }

    /*      */
 /*      */ public String getStudentUserName() {
        /* 1849 */ return this.studentUserName;
        /*      */    }

    /*      */
 /*      */ public void setStudentUserName(String studentUserName) {
        /* 1853 */ this.studentUserName = studentUserName;
        /*      */    }

    /*      */
 /*      */ public long getDup() {
        /* 1857 */ return this.dup;
        /*      */    }

    /*      */
 /*      */ public void setDup(long dup) {
        /* 1861 */ this.dup = dup;
        /*      */    }

    /*      */
 /*      */ public long getDdown() {
        /* 1865 */ return this.ddown;
        /*      */    }

    /*      */
 /*      */ public void setDdown(long ddown) {
        /* 1869 */ this.ddown = ddown;
        /*      */    }

    /*      */
 /*      */ public long getDdoe() {
        /* 1873 */ return this.ddoe;
        /*      */    }

    /*      */
 /*      */ public void setDdoe(long ddoe) {
        /* 1877 */ this.ddoe = ddoe;
        /*      */    }

    /*      */
 /*      */ public long getDdom() {
        /* 1881 */ return this.ddom;
        /*      */    }

    /*      */
 /*      */ public void setDdom(long ddom) {
        /* 1885 */ this.ddom = ddom;
        /*      */    }

    /*      */
 /*      */ public long getLostTime() {
        /* 1889 */ return this.lostTime;
        /*      */    }

    /*      */
 /*      */ public void setLostTime(long lostTime) {
        /* 1893 */ this.lostTime = lostTime;
        /*      */    }

    /*      */
 /*      */ public String getLostToMaster() {
        /* 1897 */ return this.lostToMaster;
        /*      */    }

    /*      */
 /*      */ public void setLostToMaster(String lostToMaster) {
        /* 1901 */ this.lostToMaster = lostToMaster;
        /*      */    }

    /*      */
 /*      */ public String getLostToLocal() {
        /* 1905 */ return this.lostToLocal;
        /*      */    }

    /*      */
 /*      */ public void setLostToLocal(String lostToLocal) {
        /* 1909 */ this.lostToLocal = lostToLocal;
        /*      */    }

    /*      */
 /*      */
 /*      */ public int hashCode() {
        /* 1914 */ int hash = 0;
        /* 1915 */ hash += (this.recid != null) ? this.recid.hashCode() : 0;
        /* 1916 */ return hash;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public boolean equals(Object object) {
        /* 1922 */ if (!(object instanceof edu.lsu.estimator.Student)) {
            /* 1923 */ return false;
            /*      */        }
        /* 1925 */ edu.lsu.estimator.Student other = (edu.lsu.estimator.Student) object;
        /* 1926 */ if ((this.recid == null && other.recid != null) || (this.recid != null && !this.recid.equals(other.recid))) {
            /* 1927 */ return false;
            /*      */        }
        /* 1929 */ return true;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String toString() {
        /* 1934 */ return "edu.lsu.estimator.Student[ recid=" + this.recid + " ]";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund2id() {
        /* 1941 */ return this.fund2id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund2id(Integer fund2id) {
        /* 1948 */ this.fund2id = fund2id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund3id() {
        /* 1955 */ return this.fund3id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund3id(Integer fund3id) {
        /* 1962 */ this.fund3id = fund3id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund4id() {
        /* 1969 */ return this.fund4id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund4id(Integer fund4id) {
        /* 1976 */ this.fund4id = fund4id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund5id() {
        /* 1983 */ return this.fund5id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund5id(Integer fund5id) {
        /* 1990 */ this.fund5id = fund5id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund6id() {
        /* 1997 */ return this.fund6id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund6id(Integer fund6id) {
        /* 2004 */ this.fund6id = fund6id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund7id() {
        /* 2012 */ return this.fund7id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund7id(Integer fund7id) {
        /* 2020 */ this.fund7id = fund7id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund8id() {
        /* 2027 */ return this.fund8id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund8id(Integer fund8id) {
        /* 2034 */ this.fund8id = fund8id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getFund9id() {
        /* 2041 */ return this.fund9id;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setFund9id(Integer fund9id) {
        /* 2048 */ this.fund9id = fund9id;
        /*      */    }

    /*      */
 /*      */
 /*      */ public Integer getFund1id() {
        /* 2053 */ return this.fund1id;
        /*      */    }

    /*      */
 /*      */ public void setFund1id(Integer fund1id) {
        /* 2057 */ this.fund1id = fund1id;
        /*      */    }

    /*      */
 /*      */ public List<Print> getPdfs() {
        /* 2061 */ return this.pdfs;
        /*      */    }

    /*      */
 /*      */ public void setPdfs(List<Print> pdfs) {
        /* 2065 */ this.pdfs = pdfs;
        /*      */    }

    /*      */
 /*      */ public Integer getPrtTimes() {
        /* 2069 */ return this.prtTimes;
        /*      */    }

    /*      */
 /*      */ public void setPrtTimes(Integer prtTimes) {
        /* 2073 */ this.prtTimes = prtTimes;
        /*      */    }

    /*      */
 /*      */ public long getDid() {
        /* 2077 */ return this.did;
        /*      */    }

    /*      */
 /*      */ public void setDid(long did) {
        /* 2081 */ this.did = did;
        /*      */    }

    /*      */
 /*      */ public String getTzdid() {
        /* 2085 */ return this.tzdid;
        /*      */    }

    /*      */
 /*      */ public void setTzdid(String tzdid) {
        /* 2089 */ this.tzdid = tzdid;
        /*      */    }

    /*      */
 /*      */ public String getDidstr() {
        /* 2093 */ return this.didstr;
        /*      */    }

    /*      */
 /*      */ public void setDidstr(String didstr) {
        /* 2097 */ this.didstr = didstr;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getReturnStdInd() {
        /* 2104 */ return this.returnStdInd;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setReturnStdInd(Integer returnStdInd) {
        /* 2111 */ this.returnStdInd = returnStdInd;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getNcStdInd() {
        /* 2118 */ return this.ncStdInd;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setNcStdInd(Integer ncStdInd) {
        /* 2125 */ this.ncStdInd = ncStdInd;
        /*      */    }

    /*      */
 /*      */ public Integer getTerms() {
        /* 2129 */ return this.terms;
        /*      */    }

    /*      */
 /*      */ public void setTerms(Integer terms) {
        /* 2133 */ this.terms = terms;
        /*      */    }

    /*      */
 /*      */ public String getPuser_id() {
        /* 2137 */ return this.puser_id;
        /*      */    }

    /*      */
 /*      */ public void setPuser_id(String puser_id) {
        /* 2141 */ this.puser_id = puser_id;
        /*      */    }

    /*      */
 /*      */ public Integer getEa_lsu_perc() {
        /* 2145 */ return this.ea_lsu_perc;
        /*      */    }

    /*      */
 /*      */ public void setEa_lsu_perc(Integer ea_lsu_perc) {
        /* 2149 */ this.ea_lsu_perc = ea_lsu_perc;
        /*      */    }

    /*      */
 /*      */ public Integer getEa_nonlsu_perc() {
        /* 2153 */ return this.ea_nonlsu_perc;
        /*      */    }

    /*      */
 /*      */ public void setEa_nonlsu_perc(Integer ea_nonlsu_perc) {
        /* 2157 */ this.ea_nonlsu_perc = ea_nonlsu_perc;
        /*      */    }

    /*      */
 /*      */ public String getTerm_code1() {
        /* 2161 */ return this.term_code1;
        /*      */    }

    /*      */
 /*      */ public void setTerm_code1(String term_code1) {
        /* 2165 */ this.term_code1 = term_code1;
        /*      */    }

    /*      */
 /*      */ public String getTerm_code2() {
        /* 2169 */ return this.term_code2;
        /*      */    }

    /*      */
 /*      */ public void setTerm_code2(String term_code2) {
        /* 2173 */ this.term_code2 = term_code2;
        /*      */    }

    /*      */
 /*      */ public String getTerm_code3() {
        /* 2177 */ return this.term_code3;
        /*      */    }

    /*      */
 /*      */ public void setTerm_code3(String term_code3) {
        /* 2181 */ this.term_code3 = term_code3;
        /*      */    }

    /*      */
 /*      */ public String getTerm_code4() {
        /* 2185 */ return this.term_code4;
        /*      */    }

    /*      */
 /*      */ public void setTerm_code4(String term_code4) {
        /* 2189 */ this.term_code4 = term_code4;
        /*      */    }

    /*      */
 /*      */ public Integer getTerm_load1() {
        /* 2193 */ return this.term_load1;
        /*      */    }

    /*      */
 /*      */ public void setTerm_load1(Integer term_load1) {
        /* 2197 */ this.term_load1 = term_load1;
        /*      */    }

    /*      */
 /*      */ public Integer getTerm_load2() {
        /* 2201 */ return this.term_load2;
        /*      */    }

    /*      */
 /*      */ public void setTerm_load2(Integer term_load2) {
        /* 2205 */ this.term_load2 = term_load2;
        /*      */    }

    /*      */
 /*      */ public Integer getTerm_load3() {
        /* 2209 */ return this.term_load3;
        /*      */    }

    /*      */
 /*      */ public void setTerm_load3(Integer term_load3) {
        /* 2213 */ this.term_load3 = term_load3;
        /*      */    }

    /*      */
 /*      */ public Integer getTerm_load4() {
        /* 2217 */ return this.term_load4;
        /*      */    }

    /*      */
 /*      */ public void setTerm_load4(Integer term_load4) {
        /* 2221 */ this.term_load4 = term_load4;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getTerm_unit1() {
        /* 2225 */ return this.term_unit1;
        /*      */    }

    /*      */
 /*      */ public void setTerm_unit1(BigDecimal term_unit1) {
        /* 2229 */ this.term_unit1 = term_unit1;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getTerm_unit2() {
        /* 2233 */ return this.term_unit2;
        /*      */    }

    /*      */
 /*      */ public void setTerm_unit2(BigDecimal term_unit2) {
        /* 2237 */ this.term_unit2 = term_unit2;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getTerm_unit3() {
        /* 2241 */ return this.term_unit3;
        /*      */    }

    /*      */
 /*      */ public void setTerm_unit3(BigDecimal term_unit3) {
        /* 2245 */ this.term_unit3 = term_unit3;
        /*      */    }

    /*      */
 /*      */ public BigDecimal getTerm_unit4() {
        /* 2249 */ return this.term_unit4;
        /*      */    }

    /*      */
 /*      */ public void setTerm_unit4(BigDecimal term_unit4) {
        /* 2253 */ this.term_unit4 = term_unit4;
        /*      */    }

    /*      */
 /*      */ public String getTerm_prog1() {
        /* 2257 */ return this.term_prog1;
        /*      */    }

    /*      */
 /*      */ public void setTerm_prog1(String term_prog1) {
        /* 2261 */ this.term_prog1 = term_prog1;
        /*      */    }

    /*      */
 /*      */ public String getTerm_prog2() {
        /* 2265 */ return this.term_prog2;
        /*      */    }

    /*      */
 /*      */ public void setTerm_prog2(String term_prog2) {
        /* 2269 */ this.term_prog2 = term_prog2;
        /*      */    }

    /*      */
 /*      */ public String getTerm_prog3() {
        /* 2273 */ return this.term_prog3;
        /*      */    }

    /*      */
 /*      */ public void setTerm_prog3(String term_prog3) {
        /* 2277 */ this.term_prog3 = term_prog3;
        /*      */    }

    /*      */
 /*      */ public String getTerm_prog4() {
        /* 2281 */ return this.term_prog4;
        /*      */    }

    /*      */
 /*      */ public void setTerm_prog4(String term_prog4) {
        /* 2285 */ this.term_prog4 = term_prog4;
        /*      */    }

    /*      */
 /*      */ public Integer getStd_transfer_ind() {
        /* 2289 */ return this.std_transfer_ind;
        /*      */    }

    /*      */
 /*      */ public void setStd_transfer_ind(Integer std_transfer_ind) {
        /* 2293 */ this.std_transfer_ind = std_transfer_ind;
        /*      */    }

    /*      */
 /*      */ public Integer getStd_1st_freshmen() {
        /* 2297 */ return this.std_1st_freshmen;
        /*      */    }

    /*      */
 /*      */ public void setStd_1st_freshmen(Integer std_1st_freshmen) {
        /* 2301 */ this.std_1st_freshmen = std_1st_freshmen;
        /*      */    }
 
    public String getStudentFPellGrant() {
        return studentFPellGrant;
    }

    public void setStudentFPellGrant(String studentFPellGrant) {
        this.studentFPellGrant = studentFPellGrant;
    }
    /*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Student.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
