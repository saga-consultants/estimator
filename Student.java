/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author kwang
 */
@Entity
@Table(name = "STUDENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
@NamedQuery(name = "Student.findPOPWID", query="SELECT s FROM Student s where s.pickupInd=1 and s.studentALsuid= :lsuid and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob ORDER BY s.ddoe desc "),
@NamedQuery(name = "Student.findPOPWOID", query="SELECT s FROM Student s where s.pickupInd=1 and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob ORDER BY s.ddoe desc "),
@NamedQuery(name = "Student.findPOPWIDwrecid", query="SELECT s FROM Student s where s.pickupInd=1 and s.studentALsuid= :lsuid and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob and s.recid <> :recid ORDER BY s.ddoe desc "),
@NamedQuery(name = "Student.findPOPWOIDwrecid", query="SELECT s FROM Student s where s.pickupInd=1 and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and s.studentDDob like :dob and s.recid <> :recid ORDER BY s.ddoe desc "),

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
  /*
    @NamedQuery(name = "Student.findByScholaship1id", query = "SELECT s FROM Student s WHERE s.scholaship1id = :scholaship1id"),
    @NamedQuery(name = "Student.findByScholaship2id", query = "SELECT s FROM Student s WHERE s.scholaship2id = :scholaship2id"),
    @NamedQuery(name = "Student.findByScholaship3id", query = "SELECT s FROM Student s WHERE s.scholaship3id = :scholaship3id"),
    @NamedQuery(name = "Student.findByScholaship4id", query = "SELECT s FROM Student s WHERE s.scholaship4id = :scholaship4id"),
    @NamedQuery(name = "Student.findByScholaship5id", query = "SELECT s FROM Student s WHERE s.scholaship5id = :scholaship5id"),
    @NamedQuery(name = "Student.findByScholaship6id", query = "SELECT s FROM Student s WHERE s.scholaship6id = :scholaship6id"),
    @NamedQuery(name = "Student.findByScholaship7id", query = "SELECT s FROM Student s WHERE s.scholaship7id = :scholaship7id"),
    @NamedQuery(name = "Student.findByScholaship8id", query = "SELECT s FROM Student s WHERE s.scholaship8id = :scholaship8id"),
    @NamedQuery(name = "Student.findByScholaship9id", query = "SELECT s FROM Student s WHERE s.scholaship9id = :scholaship9id"),
    
    */
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
    @NamedQuery(name = "Student.findByPrtTimes", query = "SELECT s FROM Student s WHERE s.prtTimes >0")
})
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STUDENT_FISY")
    private int studentFisy;
    
    @Basic(optional = true)
    @Size(max = 6)
    @Column(name = "STUDENT_A_LSUID")
    private String studentALsuid;
    
    @Size(min=1, max = 50)
    @Column(name = "STUDENT_B_LASTNAME")
    @NotNull
    @NotEmpty
    private String studentBLastname;
    
    @Size(min=1, max = 50)
    @Column(name = "STUDENT_C_FIRSTNAME")
    @NotNull
    @NotEmpty
    private String studentCFirstname;
    
    @Column(name = "STUDENT_D_DOB")
    @NotBlank
    @Size(min=10, max=10)
    //@Temporal(TemporalType.DATE)
    private String studentDDob; //Date 
    
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "RECID")    
    private String recid;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "STUDENT_NUMB")
    @TableGenerator(name = "ID_Gen", table = "SEQUENCE", pkColumnName="NAME",valueColumnName="ID", pkColumnValue="STUD_GEN", initialValue = 2, allocationSize = 1 ) 
    @GeneratedValue(strategy = GenerationType.TABLE, generator="ID_Gen")    
    private Integer studentNumb;
    
    
    @Size(max = 50)
    @Column(name = "STUDENT_E_EMAIL")
    @Email
    @NotBlank
    private String studentEEmail;
    
    @Size(max = 30)
    @NotNull
    @Column(name = "STUDENT_F_PHONE")
    private String studentFPhone;
    
    @Size(max = 50)
    @Column(name = "STUDENT_G_STREET")
    private String studentGStreet;
    @Size(max = 50)
    @Column(name = "STUDENT_H_CITY")
    private String studentHCity;
    
    @Size(max = 50)
    @Column(name = "STUDENT_I_STATE")
    @NotBlank
    private String studentIState;
    
    //2013-01-25 MOD: intl stud has long zip
    @Size(max = 30)
    @NotBlank
    @Column(name = "STUDENT_J_ZIP")
    private String studentJZip;
    
    @Size(max = 50)
    @Column(name = "STUDENT_K_COUNTRY")
    private String studentKCountry;
    @Size(max = 3)
    @Column(name = "STUDENT_L_INTL_STUD")
    private String studentLIntlStud;
    @Size(max = 10)
    @Column(name = "STUDENT_M_MARRY")
    private String studentMMarry;
    @Size(max = 3)
    @Column(name = "STUDENT_N_SDA")
    private String studentNSda;
    @Size(max = 50)
    @Column(name = "STUDENT_O_LAST_SCHOOL")
    private String studentOLastSchool;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "STUDENT_P_GPA")
    //@Range(min=0, max=4)
    @Range(min=(long)0.00, max=(long)7.00)
    @DecimalMin("0.00")
    @DecimalMax("7.00")
    @NotNull
    private BigDecimal studentPGpa;
    
    @Column(name = "STUDENT_Q_SAT")
    @Range(min=0, max=2400)
    @NotNull
    private Integer studentQSat;    
    
    @Column(name = "STUDENT_Q_SAT_V")
    @Range(min=0, max=800)
    @NotNull
    private Integer studentQSatV;
    
    @Column(name = "STUDENT_R_ACT")  
    @Range(min=0, max=36)
    @NotNull
    private Integer studentRAct;
    
    @Size(max = 50)
    @Column(name = "STUDENT_S_MERIT")
    private String studentSMerit;
    @Size(max = 50)
    @Column(name = "STUDENT_T_MAJOR")
    private String studentTMajor;
    @Size(max = 10)
    @Column(name = "STUDENT_U_ACADEMIC")
    private String studentUAcademic;
    
    @Size(max = 250)
    @Column(name = "STUDENT_V_FAMILY")
    private String studentVFamily;
    
    @Basic(optional = false)
    @NotNull
    @Min(0)
    @Column(name = "HOMECOSTUDIES")
    private Integer homecostudies;
    
    @Size(max = 3)
    @Column(name = "STUDENT_W_DORM")
    private String studentWDorm;
    @Size(max = 3)
    @Column(name = "STUDENT_X_FAFSA")
    private String studentXFafsa;
    @Size(max = 3)
    @Column(name = "STUDENT_Y_INDEPT")
    private String studentYIndept;
    @Size(max = 3)
    @Column(name = "STUDENT_Z_CALGRANT")
    private String studentZCalgrant;
    
    @Column(name = "STUDENT_AA_CALGRANT_A")
    @Range(min=0)
    @NotNull
    private Integer studentAaCalgrantA;
    
    @Column(name = "STUDENT_AB_CALGRANT_B")
    @Range(min=0)
    @NotNull
    private Integer studentAbCalgrantB;
    
    @Column(name = "STUDENT_AC_FAMILY_SIZE")
    @Range(min=1, max=999)
    @NotNull
    private Integer studentAcFamilySize;
    
    @Column(name = "STUDENT_AF_FAMILY_CONTRIB")
    @Range(min=0, max=99999)
    @NotNull
    private Integer studentAfFamilyContrib; //Ehster asked default shall be max=99999
    
    @Column(name = "STUDENT_AG_NONLSU_ALLOWRANCE")
    private BigDecimal studentAgNonlsuAllowrance;
    
    @Size(max = 3)
    @Column(name = "STUDENT_AH_LSU_ALLOWRANCE")
    private String studentAhLsuAllowrance;
    
    @Column(name = "STUDENT_AI_EDU_ALLOW_PER")
    @DecimalMax("1.00") 
    private BigDecimal studentAiEduAllowPer;
    
    @Size(max = 50)
    @Column(name = "STUDENT_AJ_HOME_STATE")
    private String studentAjHomeState;
    
    @Column(name = "STUDENT_AK_NONCAL_GRANT")
    @Range(min=0)
    @NotNull
    private Integer studentAkNoncalGrant;
    
    @Size(max = 50)
    @Column(name = "STUDENT_AL_OUT_SCHOLARSHIPS")
    private String studentAlOutScholarships;
    
    @Column(name = "STUDENT_AM_OUT_SCHOLARSHIP_AMT")
    @Range(min=0)
    @NotNull
    private Integer studentAmOutScholarshipAmt;
    
    @Size(max = 512)
    @Column(name = "STUDENT_AN_PUB_NOTES")
    private String studentAnPubNotes;
    @Size(max = 512)
    @Column(name = "STUDENT_AO_PRI_NOTES")
    private String studentAoPriNotes;
    @Size(max = 3)
    @Column(name = "STUDENT_AP_SUB_LOANS")
    private String studentApSubLoans;
    @Size(max = 3)
    @Column(name = "STUDENT_AQ_UNSUB_LOANS")
    private String studentAqUnsubLoans;
    @Size(max = 3)
    @Column(name = "STUDENT_AR_FWS")
    private String studentArFws;
    @Size(max = 70)
    @Column(name = "STUDENT_AS_SCHOLARSHIP1_NAME")
    private String studentAsScholarship1Name;
    @Size(max = 256)
    @Column(name = "STUDENT_AT_SCHOLARSHIP1_NOTE")
    private String studentAtScholarship1Note;
    
    @Column(name = "STUDENT_AU_SCHOLARSHIP1_AMT")
    //@Range(min=0)
     @Min(0)
    @NotNull
    private Integer studentAuScholarship1Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_AV_SCHOLARSHIP2_NAME")
    private String studentAvScholarship2Name;
    @Size(max = 256)
    @Column(name = "STUDENT_AW_SCHOLARSHIP2_NOTE")
    private String studentAwScholarship2Note;
    
    @Column(name = "STUDENT_AX_SCHOLARSHIP2_AMT")
     @Min(0)
    @NotNull
    private Integer studentAxScholarship2Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_AY_SCHOLARSHIP3_NAME")
    private String studentAyScholarship3Name;
    @Size(max = 256)
    @Column(name = "STUDENT_AZ_SCHOLARSHIP3_NOTE")
    private String studentAzScholarship3Note;
    
    @Column(name = "STUDENT_BA_SCHOLARSHIP3_AMT")
     @Min(0)
    @NotNull
    private Integer studentBaScholarship3Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_BB_SCHOLARSHIP4_NAME")
    private String studentBbScholarship4Name;
    @Size(max = 256)
    @Column(name = "STUDENT_BC_SCHOLARSHIP4_NOTE")
    private String studentBcScholarship4Note;
    
    @Column(name = "STUDENT_BD_SCHOLARSHIP4_AMT")
     @Min(0)
    @NotNull
    private Integer studentBdScholarship4Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_BE_SCHOLARSHIP5_NAME")
    private String studentBeScholarship5Name;
    @Size(max = 256)
    @Column(name = "STUDENT_BF_SCHOLARSHIP5_NOTE")
    private String studentBfScholarship5Note;
    
    @Column(name = "STUDENT_BG_SCHOLARSHIP5_AMT")
     @Min(0)
    @NotNull
    private Integer studentBgScholarship5Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_BH_SCHOLARSHIP6_NAME")
    private String studentBhScholarship6Name;
    @Size(max = 256)
    @Column(name = "STUDENT_BI_SCHOLARSHIP6_NOTE")
    private String studentBiScholarship6Note;
    
    @Column(name = "STUDENT_BJ_SCHOLARSHIP6_AMT")
     @Min(0)
    @NotNull
    private Integer studentBjScholarship6Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_BK_SCHOLARSHIP7_NAME")
    private String studentBkScholarship7Name;
    @Size(max = 256)
    @Column(name = "STUDENT_BL_SCHOLARSHIP7_NOTE")
    private String studentBlScholarship7Note;
    
    @Column(name = "STUDENT_BM_SCHOLARSHIP7_AMT")
     @Min(0)
    @NotNull
    private Integer studentBmScholarship7Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_BN_SCHOLARSHIP8_NAME")
    private String studentBnScholarship8Name;
    @Size(max = 256)
    @Column(name = "STUDENT_BO_SCHOLARSHIP8_NOTE")
    private String studentBoScholarship8Note;
    
    @Column(name = "STUDENT_BP_SCHOLARSHIP8_AMT")
     @Min(0)
    @NotNull
    private Integer studentBpScholarship8Amt;
    @Size(max = 70)
    @Column(name = "STUDENT_BQ_SCHOLARSHIP9_NAME")
    private String studentBqScholarship9Name;
    @Size(max = 256)
    @Column(name = "STUDENT_BR_SCHOLARSHIP9_NOTE")
    private String studentBrScholarship9Note;    
    
    @Column(name = "STUDENT_BS_SCHOLARSHIP9_AMT")
    @Min(0)
    @NotNull
    private Integer studentBsScholarship9Amt;
    
    @Size(max = 10)
    @Column(name = "STUDENT_BT_SUPERCOUNSELOR")
    private String studentBtSupercounselor;
    @Column(name = "STUDENT_BW_PROGRESS")
    private Integer studentBwProgress;
    @Size(max = 3)
    @Column(name = "STUDENT_BZ_UPLOADED")
    private String studentBzUploaded;
    @Size(max = 3)
    @Column(name = "STUDENT_CB_BANNER")
    private String studentCbBanner;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "STUDENT_PASSWORD")
    private String studentPassword;
    @Size(max = 6)
    @Column(name = "STUDENT_TERM_START")
    private String studentTermStart;
    @Size(max = 6)
    @Column(name = "STUDENT_TERM_END")
    private String studentTermEnd;
    @Size(max = 30)
    @Column(name = "STUDENT_STUD_TYPE")
    private String studentStudType;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "PICKUP_IND")
    private int pickupInd;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTM_NUMB")
    private int estmNumb;    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "IND_EFC")
    private String indEfc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "IND_EXCLOANS")
    private String indExcloans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "IND_EALSU")
    private String indEalsu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "IND_EANONLSU")
    private String indEanonlsu;
    
    @Column(name = "STUDENT_AD_FAMILY_INCOME")
    //@Range(min=0)
    @Min(0)
    @NotNull
    private Integer studentAdFamilyIncome;
    
    @Column(name = "STUDENT_AE_FAMILY_ASSET")
    @Min(0)
    @NotNull
    private Integer studentAeFamilyAsset;
    
    @Column(name = "STUDENT_BY_DOM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date studentByDom;
    @Column(name = "STUDENT_BV_DOE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date studentBvDoe;
    @Size(max = 3)
    @Column(name = "ADJ_CALGRANT_IND")
    private String adjCalgrantInd;
    
    @Size(max = 1, min=1)
    @NotNull
    @NotEmpty
    @Column(name = "SEX")
    private String sex;
    
    @Size(max = 7)
    @Column(name = "HOME_ADDR_APT")
    private String homeAddrApt;
    
    @Column(name = "SCHOLASHIP1ID")
    private Integer fund1id;
    @Column(name = "SCHOLASHIP2ID")
    private Integer fund2id;
    @Column(name = "SCHOLASHIP3ID")
    private Integer fund3id;
    @Column(name = "SCHOLASHIP4ID")
    private Integer fund4id;
    @Column(name = "SCHOLASHIP5ID")
    private Integer fund5id;
    @Column(name = "SCHOLASHIP6ID")
    private Integer fund6id;
    @Column(name = "SCHOLASHIP7ID")
    private Integer fund7id;
    @Column(name = "SCHOLASHIP8ID")
    private Integer fund8id;
    @Column(name = "SCHOLASHIP9ID")
    private Integer fund9id;
    @Size(max = 12)
    @Column(name = "TZDOE")
    private String tzdoe;
    @Size(max = 12)
    @Column(name = "TZDOM")
    private String tzdom;
    @Size(max = 12)
    @Column(name = "TZUP")
    private String tzup;
    @Size(max = 12)
    @Column(name = "TZDOWN")
    private String tzdown;
    
    
    @Column(name = "CLIENT_ID")
    private Integer clientId;
    @Column(name = "COUNSELOR_ID")
    private Integer counselorId;
    @Size(max = 100)
    @Column(name = "MOD_ROOT")
    private String modRoot;
    @Size(max = 100)
    @Column(name = "MOD_PRE")
    private String modPre;
    @Size(max = 30)
    @Column(name = "STUDENT_BX_MOD_COUNSELOR")
    private String studentBxModCounselor;
    @Size(max = 30)
    @Column(name = "STUDENT_BU_ORIG_COUNSELOR")
    private String studentBuOrigCounselor;
    @Column(name = "COUNSELOR_ORIG")
    private Integer counselorOrig;
    @Column(name = "COUNSELOR_MOD")
    private Integer counselorMod;
    @Size(max = 20)
    @Column(name = "LOST_TZ")
    private String lostTz;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DID")
    private long did;
    
    @Size(max = 12)
    @Column(name = "TZDID")
    private String tzdid;
    
    @Size(max = 32)
    @Column(name = "DIDSTR")
    private String didstr;
    
    
    @Size(max = 120)
    @Basic(optional = false)
    @NotNull
    @Column(name = "STUDENT_USER_NAME")
    private String studentUserName;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DUP")
    private long dup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DDOWN")
    private long ddown;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DDOE")
    private long ddoe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DDOM")
    private long ddom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOST_TIME")
    private long lostTime;
    @Size(max = 100)
    @Column(name = "LOST_TO_MASTER")
    private String lostToMaster;
    @Size(max = 100)
    @Column(name = "LOST_TO_LOCAL")
    private String lostToLocal;

    @Column(name="PRT_TIMES")
    private Integer prtTimes;
      
    @NotNull
    @Min(0)
    @Max(1)
    @Column( name="RETURNING_")
    private Integer returnStdInd;
    
    @NotNull
    @Min(0)
    @Max(1)
    @Column( name="NC")
    private Integer ncStdInd;
    
    //----------------------------------------------------------------
    @NotNull
    @Min(0)
    @Max(4)
    @Column( name="TERMS")
    private Integer terms=0; //only init the obj, not the DB row insert/update default
    //@Column(columnDefinition="double precision default '96'")  is DB specific
    //@Column(columnDefinition="tinyint(1) default 1")
    //@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //@Column(name="Price", columnDefinition="Decimal(10,2) default '100.00'")
    //@Column(name = “REQUEST_DATE”, nullable = false, columnDefinition = “date default sysdate″)
    
    @Size(max = 6)
    @Column(name = "PUSER_ID")
    private String puser_id ;
    
    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "EA_LSU_PERC")
    private Integer ea_lsu_perc = 0;// -- the percentage * 100
    
    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "EA_NONLSU_PERC")
    private Integer ea_nonlsu_perc=0;
    
    @Size(max = 6)
    @Column(name = "TERM_CODE1")
    private String term_code1;
    
    @Size(max = 6)
    @Column(name = "TERM_CODE2")
    private String term_code2 ;
    
    @Size(max = 6)
    @Column(name = "TERM_CODE3")
    private String term_code3;
    
    @Size(max = 6)
    @Column(name = "TERM_CODE4")
    private String term_code4;
    
    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "TERM_LOAD1")
    private Integer term_load1= 0;//  -- load percentage * 100
    
    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "TERM_LOAD2")
    private Integer term_load2 = 0;
    
    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "TERM_LOAD3")
    private Integer term_load3 = 0;
    
    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "TERM_LOAD4")
    private Integer term_load4 = 0;
    
    @NotNull
    @DecimalMin("0.0") 
    @DecimalMax("99.9") 
    @Column(name = "TERM_UNIT1")
    private BigDecimal term_unit1 = new BigDecimal("0"); // numeric(3,1) NOT NULL DEFAULT 0,
    
    @NotNull
    @DecimalMin("0.0") 
    @DecimalMax("99.9") 
    @Column(name = "TERM_UNIT2")
    private BigDecimal term_unit2 = new BigDecimal("0"); //numeric(3,1) NOT NULL DEFAULT 0,
            
            
    @NotNull
    @DecimalMin("0.0") 
    @DecimalMax("99.9") 
    @Column(name = "TERM_UNIT3")
    private BigDecimal term_unit3 = new BigDecimal("0");// numeric(3,1) NOT NULL DEFAULT 0,
            
            
    @NotNull
    @DecimalMin("0.0") 
    @DecimalMax("99.9") 
    @Column(name = "TERM_UNIT4")
    private BigDecimal term_unit4 = new BigDecimal("0");// numeric(3,1) NOT NULL DEFAULT 0,
    
    @Size(max = 50)
    @Column(name = "TERM_PROG1")
    private String term_prog1;
    
    @Size(max = 50)
    @Column(name = "TERM_PROG2")
    private String term_prog2;
    
    @Size(max = 50)
    @Column(name = "TERM_PROG3")
    private String term_prog3;
    
    @Size(max = 50)
    @Column(name = "TERM_PROG4")
    private String term_prog4;
    
    //@NotNull
    @Min(-1)
    @Max(1)
    @Column( name="STD_TRANSFER_IND")
    private Integer std_transfer_ind = -1;
    
    //@NotNull
    @Min(-1)
    @Max(1)
    @Column( name="STD_1ST_FRESHMEN")
    private Integer std_1st_freshmen = -1;
    
    
       
    
    //@OneToOne(optional=false)
    //@JoinColumn(name = "RECID")  @JoinColumn( nullable = false, insertable = false, updatable = false) 
   // private List<Print> prints; 
   // @OneToMany(fetch=FetchType.LAZY,  mappedBy="recid", cascade = CascadeType.ALL) //  cascade = CascadeType.ALL, targetEntity=Print.class, orphanRemoval=false   )   
    @OneToMany
    @JoinColumn(name = "RECID") //the FK column of target objs table, thus save join table
    private List<Print> pdfs;
    
   /*********************mappedBy = "RECID"*****************
Caused by: Exception [EclipseLink-7154] (Eclipse Persistence Services - 2.3.1.v20111018-r10243): org.eclipse.persistence.exceptions.ValidationException
Exception Description: The attribute [prints] in entity class [class edu.lsu.estimator.Student] has a mappedBy value of [RECID] which does not exist in its owning entity class [class edu.lsu.estimator.OutPDF]. 
* If the owning entity class is a @MappedSuperclass, this is invalid, and your attribute should reference the correct subclass.    
    
    ************mappedBy = "recid"*****************
 Internal Exception: Exception [EclipseLink-7244] (Eclipse Persistence Services - 2.3.1.v20111018-r10243): org.eclipse.persistence.exceptions.ValidationException
Exception Description: An incompatible mapping has been encountered between [class edu.lsu.estimator.Student] and [class edu.lsu.estimator.Print]. 
* This usually occurs when the cardinality of a mapping does not correspond with the cardinality of its backpointer.. 
    
  org.eclipse.persistence.exceptions.ValidationException
Exception Description: An incompatible mapping has been encountered between [class edu.lsu.estimator.Student] and [class edu.lsu.estimator.Print]. 
* This usually occurs when the cardinality of a mapping does not correspond with the cardinality of its backpointer.
    * 
    * 
    ************ get rid off 'mappedBy = " "*****************
    Exception [EclipseLink-48] (Eclipse Persistence Services - 2.3.1.v20111018-r10243): org.eclipse.persistence.exceptions.DescriptorException
Exception Description: Multiple writable mappings exist for the field [PRINTS.RECID].  Only one may be defined as writable, all others must be specified read-only.
Mapping: org.eclipse.persistence.mappings.ManyToOneMapping[stud]
Descriptor: RelationalDescriptor(edu.lsu.estimator.Print --> [DatabaseTable(PRINTS)])
    
    ************************ targetEntity=Print.class -------------
    *eclipselink asked to query join table "STUDENT_PRINTS"
    
    
    */       
    
    
    public Student() {
        setInitVals();
    }

    public Student(String recid) {
        this.recid = recid;
        setInitVals();
    }

    public Student(String recid, short studentFisy, String studentPassword, int pickupInd, int estmNumb, int studentNumb, String indEfc, String indExcloans, String indEalsu, String indEanonlsu, int homecostudies, long dup, long ddown, long ddoe, long ddom, long lostTime) {
        this.recid = recid;
        this.studentFisy = studentFisy;
        this.studentPassword = studentPassword;
        this.pickupInd = pickupInd;
        this.estmNumb = estmNumb;
        this.studentNumb = studentNumb;
        this.indEfc = indEfc;
        this.indExcloans = indExcloans;
        this.indEalsu = indEalsu;
        this.indEanonlsu = indEanonlsu;
        this.homecostudies = homecostudies;
        this.dup = dup;
        this.ddown = ddown;
        this.ddoe = ddoe;
        this.ddom = ddom;
        this.lostTime = lostTime;
        setInitVals();
    }
    
    public void setInitVals(){
 //       System.out.println("******************************** stud setInitVals() invoked. empty institution scholarships*******"); //too many times, eqch query
        this.studentLIntlStud="no";        
        this.studentMMarry="no";//Single";
        this.studentNSda = "no";
        this.studentUAcademic="FR"; //Freshmen
        this.studentWDorm="no";
        this.studentXFafsa="no";        
        this.studentYIndept="no";
        this.studentZCalgrant="no";        
        this.studentAcFamilySize=2; //this.studentVFamily="" is for LSU studying; //number in family default is 2, and indept's can be 1
        
        this.studentSMerit="";
        
        //this.educationalAllowance is missing?
        this.studentAhLsuAllowrance="no";
        this.studentAiEduAllowPer = new BigDecimal(0); //will div by 1000;  //percentage  
        
        this.studentAgNonlsuAllowrance=new BigDecimal(0);        
        this.indEalsu = "no"; //ind_ealsu
        this.indEanonlsu = "no"; //ind_eanonlsu
        
        this.indEfc = "no"; //ind_efc
        this.indExcloans = "no"; //ind_noloans
        this.adjCalgrantInd = "no"; //ind_acalgrant
        
        this.studentAaCalgrantA = 0;//new BigDecimal(0);
        this.studentAbCalgrantB = 0;//new BigDecimal(0);
        
        this.studentApSubLoans="yes";
        this.studentAqUnsubLoans="yes";
        this.studentArFws="yes";
        
        this.studentQSatV = 0;
        this.studentPGpa = new BigDecimal(0.00);
        this.studentQSat=0;
        this.studentRAct=0;
        
        this.studentVFamily = ""; //family member  studying in LSU
        this.studentAcFamilySize=1;
        this.studentAfFamilyContrib = 99999;//new BigDecimal(0.00); //EFC ??? //2012-01-12 Esther saked to set default as max 99999, instead of min==0
        this.studentAdFamilyIncome=0;
        this.studentAeFamilyAsset=0;
        
        this.studentApSubLoans="yes";
        this.studentAqUnsubLoans="yes";
        this.studentArFws = "yes";
        this.studentAkNoncalGrant= 0;//new BigDecimal(0);
        this.studentAmOutScholarshipAmt=0;
        
        studentAuScholarship1Amt=0;studentAxScholarship2Amt=0;studentBaScholarship3Amt=0;
        studentBdScholarship4Amt=0; studentBgScholarship5Amt=0; studentBjScholarship6Amt=0;
        studentBmScholarship7Amt=0; studentBpScholarship8Amt=0; studentBsScholarship9Amt=0;
        
        /*
        this.studentAsScholarship1Name="Church Matching Scholarship (100% match, $1000 max)";
        this.studentAvScholarship2Name="Pacific Union Camp Earning (100% match, $2000 max)";
        this.studentAyScholarship3Name="Non-Pacific Union Camp Earning (50% match, $1500 max)";
        this.studentBbScholarship4Name="Literature Evangelist Earnings (100% match, $3000 max)";
        this.studentBeScholarship5Name="";
        this.studentBhScholarship6Name=""; */
        this.studentBkScholarship7Name=""; 
        this.studentBnScholarship8Name="";
        this.studentBqScholarship9Name="";
        
        this.studentAtScholarship1Note=" ";
        this.studentAwScholarship2Note=" ";
        this.studentAzScholarship3Note=" ";
        this.studentBcScholarship4Note=" ";
        this.studentBfScholarship5Note=" ";
        this.studentBiScholarship6Note=" ";
        this.studentBlScholarship7Note=" ";
        this.studentBoScholarship8Note=" ";
        this.studentBrScholarship9Note=" ";
        
        this.fund1id=0;
        this.fund2id=0;
        this.fund3id=0;
        this.fund4id=0;
        this.fund5id=0;
        this.fund6id=0;
        this.fund7id=0;
        this.fund8id=0;
        this.fund9id=0;
        
        sex = "N"; //std_sex
        homecostudies = 0; //costudents
        homeAddrApt = ""; //home_addr_apt
        studentDDob = "";
        
        returnStdInd=0;
        ncStdInd=0;
        
      //  this.studentNumb=9999;
        this.studentFisy=2012;
        this.setStudentUserName("|||");
        this.setStudentPassword("password");
        this.setStudentBwProgress(5); //BigDecimal.ZERO //getStudentBwProgress
        this.setStudentStudType("FYUG");  //studentStudType
    }
    
    
    public int getStudentFisy() {
        return studentFisy;
    }

    public void setStudentFisy(int studentFisy) {
        this.studentFisy = studentFisy;
    }

    public String getStudentALsuid() {
        return studentALsuid;
    }

    public void setStudentALsuid(String studentALsuid) {
        this.studentALsuid = studentALsuid;
    }

    public String getStudentBLastname() {
        return studentBLastname;
    }

    public void setStudentBLastname(String studentBLastname) {
        this.studentBLastname = studentBLastname;
    }

    public String getStudentCFirstname() {
        return studentCFirstname;
    }

    public void setStudentCFirstname(String studentCFirstname) {
        this.studentCFirstname = studentCFirstname;
    }

    public String getStudentDDob() {
        return studentDDob;
    }

    public void setStudentDDob(String studentDDob) {
        this.studentDDob = studentDDob;
    }

    public String getStudentEEmail() {
        return studentEEmail;
    }

    public void setStudentEEmail(String studentEEmail) {
        this.studentEEmail = studentEEmail;
    }

    public String getStudentFPhone() {
        return studentFPhone;
    }

    public void setStudentFPhone(String studentFPhone) {
        this.studentFPhone = studentFPhone;
    }

    public String getStudentGStreet() {
        return studentGStreet;
    }

    public void setStudentGStreet(String studentGStreet) {
        this.studentGStreet = studentGStreet;
    }

    public String getStudentHCity() {
        return studentHCity;
    }

    public void setStudentHCity(String studentHCity) {
        this.studentHCity = studentHCity;
    }

    public String getStudentIState() {
        return studentIState;
    }

    public void setStudentIState(String studentIState) {
        this.studentIState = studentIState;
    }

    public String getStudentJZip() {
        return studentJZip;
    }

    public void setStudentJZip(String studentJZip) {
        this.studentJZip = studentJZip;
    }

    public String getStudentKCountry() {
        return studentKCountry;
    }

    public void setStudentKCountry(String studentKCountry) {
        this.studentKCountry = studentKCountry;
    }

    public String getStudentLIntlStud() {
        return studentLIntlStud;
    }

    public void setStudentLIntlStud(String studentLIntlStud) {
        this.studentLIntlStud = studentLIntlStud;
    }

    public String getStudentMMarry() {
        return studentMMarry;
    }

    public void setStudentMMarry(String studentMMarry) {
        this.studentMMarry = studentMMarry;
    }

    public String getStudentNSda() {
        return studentNSda;
    }

    public void setStudentNSda(String studentNSda) {
        this.studentNSda = studentNSda;
    }

    public String getStudentOLastSchool() {
        return studentOLastSchool;
    }

    public void setStudentOLastSchool(String studentOLastSchool) {
        this.studentOLastSchool = studentOLastSchool;
    }

    public BigDecimal getStudentPGpa() {
        return studentPGpa;
    }

    public void setStudentPGpa(BigDecimal studentPGpa) {
        this.studentPGpa = studentPGpa;
    }

    public Integer getStudentQSat() {
        return studentQSat;
    }

    public void setStudentQSat(Integer studentQSat) {
        this.studentQSat = studentQSat;
    }

    public Integer getStudentRAct() {
        return studentRAct;
    }

    public void setStudentRAct(Integer studentRAct) {
        this.studentRAct = studentRAct;
    }

    public String getStudentSMerit() {
        return studentSMerit;
    }

    public void setStudentSMerit(String studentSMerit) {
        this.studentSMerit = studentSMerit;
    }

    public String getStudentTMajor() {
        return studentTMajor;
    }

    public void setStudentTMajor(String studentTMajor) {
        this.studentTMajor = studentTMajor;
    }

    public String getStudentUAcademic() {
        return studentUAcademic;
    }

    public void setStudentUAcademic(String studentUAcademic) {
        this.studentUAcademic = studentUAcademic;
    }

    public String getStudentVFamily() {
        return studentVFamily;
    }

    public void setStudentVFamily(String studentVFamily) {
        this.studentVFamily = studentVFamily;
    }

    public String getStudentWDorm() {
        return studentWDorm;
    }

    public void setStudentWDorm(String studentWDorm) {
        this.studentWDorm = studentWDorm;
    }

    public String getStudentXFafsa() {
        return studentXFafsa;
    }

    public void setStudentXFafsa(String studentXFafsa) {
        this.studentXFafsa = studentXFafsa;
        //log.info();
    }

    public String getStudentYIndept() {
        return studentYIndept;
    }

    public void setStudentYIndept(String studentYIndept) {
        this.studentYIndept = studentYIndept;
    }

    public String getStudentZCalgrant() {
        return studentZCalgrant;
    }

    public void setStudentZCalgrant(String studentZCalgrant) {
        this.studentZCalgrant = studentZCalgrant;
    }

    public Integer getStudentAaCalgrantA() {
        return studentAaCalgrantA;
    }

    public void setStudentAaCalgrantA(Integer studentAaCalgrantA) {
        this.studentAaCalgrantA = studentAaCalgrantA;
    }

    public Integer getStudentAbCalgrantB() {
        return studentAbCalgrantB;
    }

    public void setStudentAbCalgrantB(Integer studentAbCalgrantB) {
        this.studentAbCalgrantB = studentAbCalgrantB;
    }

    public Integer getStudentAcFamilySize() {
        return studentAcFamilySize;
    }

    public void setStudentAcFamilySize(Integer studentAcFamilySize) {
        this.studentAcFamilySize = studentAcFamilySize;
    }

    public Integer getStudentAfFamilyContrib() {
        return studentAfFamilyContrib;
    }

    public void setStudentAfFamilyContrib(Integer studentAfFamilyContrib) {
        this.studentAfFamilyContrib = studentAfFamilyContrib;
    }

    public BigDecimal getStudentAgNonlsuAllowrance() {
        return studentAgNonlsuAllowrance;
    }

    public void setStudentAgNonlsuAllowrance(BigDecimal studentAgNonlsuAllowrance) {
        this.studentAgNonlsuAllowrance = studentAgNonlsuAllowrance;
    }

    public String getStudentAhLsuAllowrance() {
        return studentAhLsuAllowrance;
    }

    public void setStudentAhLsuAllowrance(String studentAhLsuAllowrance) {
        this.studentAhLsuAllowrance = studentAhLsuAllowrance;
    }

    public BigDecimal getStudentAiEduAllowPer() {
        return studentAiEduAllowPer;
    }

    public void setStudentAiEduAllowPer(BigDecimal studentAiEduAllowPer) {
        this.studentAiEduAllowPer = studentAiEduAllowPer;
    }

    public String getStudentAjHomeState() {
        return studentAjHomeState;
    }

    public void setStudentAjHomeState(String studentAjHomeState) {
        this.studentAjHomeState = studentAjHomeState;
    }

    public Integer getStudentAkNoncalGrant() {
        return studentAkNoncalGrant;
    }

    public void setStudentAkNoncalGrant(Integer studentAkNoncalGrant) {
        this.studentAkNoncalGrant = studentAkNoncalGrant;
    }

    public String getStudentAlOutScholarships() {
        return studentAlOutScholarships;
    }

    public void setStudentAlOutScholarships(String studentAlOutScholarships) {
        this.studentAlOutScholarships = studentAlOutScholarships;
    }

    public Integer getStudentAmOutScholarshipAmt() {
        return studentAmOutScholarshipAmt;
    }

    public void setStudentAmOutScholarshipAmt(Integer studentAmOutScholarshipAmt) {
        this.studentAmOutScholarshipAmt = studentAmOutScholarshipAmt;
    }

    public String getStudentAnPubNotes() {
        return studentAnPubNotes;
    }

    public void setStudentAnPubNotes(String studentAnPubNotes) {
        this.studentAnPubNotes = studentAnPubNotes;
    }

    public String getStudentAoPriNotes() {
        return studentAoPriNotes;
    }

    public void setStudentAoPriNotes(String studentAoPriNotes) {
        this.studentAoPriNotes = studentAoPriNotes;
    }

    public String getStudentApSubLoans() {
        return studentApSubLoans;
    }

    public void setStudentApSubLoans(String studentApSubLoans) {
        this.studentApSubLoans = studentApSubLoans;
    }

    public String getStudentAqUnsubLoans() {
        return studentAqUnsubLoans;
    }

    public void setStudentAqUnsubLoans(String studentAqUnsubLoans) {
        this.studentAqUnsubLoans = studentAqUnsubLoans;
    }

    public String getStudentArFws() {
        return studentArFws;
    }

    public void setStudentArFws(String studentArFws) {
        this.studentArFws = studentArFws;
    }

    public String getStudentAsScholarship1Name() {
        return studentAsScholarship1Name;
    }

    public void setStudentAsScholarship1Name(String studentAsScholarship1Name) {
        this.studentAsScholarship1Name = studentAsScholarship1Name;
    }

    public String getStudentAtScholarship1Note() {
        return studentAtScholarship1Note;
    }

    public void setStudentAtScholarship1Note(String studentAtScholarship1Note) {
        this.studentAtScholarship1Note = studentAtScholarship1Note;
    }

    public Integer getStudentAuScholarship1Amt() {
        return studentAuScholarship1Amt;
    }

    public void setStudentAuScholarship1Amt(Integer studentAuScholarship1Amt) {
        this.studentAuScholarship1Amt = studentAuScholarship1Amt;
    }

    public String getStudentAvScholarship2Name() {
        return studentAvScholarship2Name;
    }

    public void setStudentAvScholarship2Name(String studentAvScholarship2Name) {
        this.studentAvScholarship2Name = studentAvScholarship2Name;
    }

    public String getStudentAwScholarship2Note() {
        return studentAwScholarship2Note;
    }

    public void setStudentAwScholarship2Note(String studentAwScholarship2Note) {
        this.studentAwScholarship2Note = studentAwScholarship2Note;
    }

    public Integer getStudentAxScholarship2Amt() {
        return studentAxScholarship2Amt;
    }

    public void setStudentAxScholarship2Amt(Integer studentAxScholarship2Amt) {
        this.studentAxScholarship2Amt = studentAxScholarship2Amt;
    }

    public String getStudentAyScholarship3Name() {
        return studentAyScholarship3Name;
    }

    public void setStudentAyScholarship3Name(String studentAyScholarship3Name) {
        this.studentAyScholarship3Name = studentAyScholarship3Name;
    }

    public String getStudentAzScholarship3Note() {
        return studentAzScholarship3Note;
    }

    public void setStudentAzScholarship3Note(String studentAzScholarship3Note) {
        this.studentAzScholarship3Note = studentAzScholarship3Note;
    }

    public Integer getStudentBaScholarship3Amt() {
        return studentBaScholarship3Amt;
    }

    public void setStudentBaScholarship3Amt(Integer studentBaScholarship3Amt) {
        this.studentBaScholarship3Amt = studentBaScholarship3Amt;
    }

    public String getStudentBbScholarship4Name() {
        return studentBbScholarship4Name;
    }

    public void setStudentBbScholarship4Name(String studentBbScholarship4Name) {
        this.studentBbScholarship4Name = studentBbScholarship4Name;
    }

    public String getStudentBcScholarship4Note() {
        return studentBcScholarship4Note;
    }

    public void setStudentBcScholarship4Note(String studentBcScholarship4Note) {
        this.studentBcScholarship4Note = studentBcScholarship4Note;
    }

    public Integer getStudentBdScholarship4Amt() {
        return studentBdScholarship4Amt;
    }

    public void setStudentBdScholarship4Amt(Integer studentBdScholarship4Amt) {
        this.studentBdScholarship4Amt = studentBdScholarship4Amt;
    }

    public String getStudentBeScholarship5Name() {
        return studentBeScholarship5Name;
    }

    public void setStudentBeScholarship5Name(String studentBeScholarship5Name) {
        this.studentBeScholarship5Name = studentBeScholarship5Name;
    }

    public String getStudentBfScholarship5Note() {
        return studentBfScholarship5Note;
    }

    public void setStudentBfScholarship5Note(String studentBfScholarship5Note) {
        this.studentBfScholarship5Note = studentBfScholarship5Note;
    }

    public Integer getStudentBgScholarship5Amt() {
        return studentBgScholarship5Amt;
    }

    public void setStudentBgScholarship5Amt(Integer studentBgScholarship5Amt) {
        this.studentBgScholarship5Amt = studentBgScholarship5Amt;
    }

    public String getStudentBhScholarship6Name() {
        return studentBhScholarship6Name;
    }

    public void setStudentBhScholarship6Name(String studentBhScholarship6Name) {
        this.studentBhScholarship6Name = studentBhScholarship6Name;
    }

    public String getStudentBiScholarship6Note() {
        return studentBiScholarship6Note;
    }

    public void setStudentBiScholarship6Note(String studentBiScholarship6Note) {
        this.studentBiScholarship6Note = studentBiScholarship6Note;
    }

    public Integer getStudentBjScholarship6Amt() {
        return studentBjScholarship6Amt;
    }

    public void setStudentBjScholarship6Amt(Integer studentBjScholarship6Amt) {
        this.studentBjScholarship6Amt = studentBjScholarship6Amt;
    }

    public String getStudentBkScholarship7Name() {
        return studentBkScholarship7Name;
    }

    public void setStudentBkScholarship7Name(String studentBkScholarship7Name) {
        this.studentBkScholarship7Name = studentBkScholarship7Name;
    }

    public String getStudentBlScholarship7Note() {
        return studentBlScholarship7Note;
    }

    public void setStudentBlScholarship7Note(String studentBlScholarship7Note) {
        this.studentBlScholarship7Note = studentBlScholarship7Note;
    }

    public Integer getStudentBmScholarship7Amt() {
        return studentBmScholarship7Amt;
    }

    public void setStudentBmScholarship7Amt(Integer studentBmScholarship7Amt) {
        this.studentBmScholarship7Amt = studentBmScholarship7Amt;
    }

    public String getStudentBnScholarship8Name() {
        return studentBnScholarship8Name;
    }

    public void setStudentBnScholarship8Name(String studentBnScholarship8Name) {
        this.studentBnScholarship8Name = studentBnScholarship8Name;
    }

    public String getStudentBoScholarship8Note() {
        return studentBoScholarship8Note;
    }

    public void setStudentBoScholarship8Note(String studentBoScholarship8Note) {
        this.studentBoScholarship8Note = studentBoScholarship8Note;
    }

    public Integer getStudentBpScholarship8Amt() {
        return studentBpScholarship8Amt;
    }

    public void setStudentBpScholarship8Amt(Integer studentBpScholarship8Amt) {
        this.studentBpScholarship8Amt = studentBpScholarship8Amt;
    }

    public String getStudentBqScholarship9Name() {
        return studentBqScholarship9Name;
    }

    public void setStudentBqScholarship9Name(String studentBqScholarship9Name) {
        this.studentBqScholarship9Name = studentBqScholarship9Name;
    }

    public String getStudentBrScholarship9Note() {
        return studentBrScholarship9Note;
    }

    public void setStudentBrScholarship9Note(String studentBrScholarship9Note) {
        this.studentBrScholarship9Note = studentBrScholarship9Note;
    }

    public Integer getStudentBsScholarship9Amt() {
        return studentBsScholarship9Amt;
    }

    public void setStudentBsScholarship9Amt(Integer studentBsScholarship9Amt) {
        this.studentBsScholarship9Amt = studentBsScholarship9Amt;
    }

    public String getStudentBtSupercounselor() {
        return studentBtSupercounselor;
    }

    public void setStudentBtSupercounselor(String studentBtSupercounselor) {
        this.studentBtSupercounselor = studentBtSupercounselor;
    }

    public Integer getStudentBwProgress() {
        return studentBwProgress;
    }

    public void setStudentBwProgress(Integer studentBwProgress) {
        this.studentBwProgress = studentBwProgress;
    }

    public String getStudentBzUploaded() {
        return studentBzUploaded;
    }

    public void setStudentBzUploaded(String studentBzUploaded) {
        this.studentBzUploaded = studentBzUploaded;
    }

    public String getStudentCbBanner() {
        return studentCbBanner;
    }

    public void setStudentCbBanner(String studentCbBanner) {
        this.studentCbBanner = studentCbBanner;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentTermStart() {
        return studentTermStart;
    }

    public void setStudentTermStart(String studentTermStart) {
        this.studentTermStart = studentTermStart;
    }

    public String getStudentTermEnd() {
        return studentTermEnd;
    }

    public void setStudentTermEnd(String studentTermEnd) {
        this.studentTermEnd = studentTermEnd;
    }

    public String getStudentStudType() {
        return studentStudType;
    }

    public void setStudentStudType(String studentStudType) {
        this.studentStudType = studentStudType;
    }

    public Integer getStudentQSatV() {
        return studentQSatV;
    }

    public void setStudentQSatV(Integer studentQSatV) {
        this.studentQSatV = studentQSatV;
    }

    public int getPickupInd() {
        return pickupInd;
    }

    public void setPickupInd(int pickupInd) {
        this.pickupInd = pickupInd;
    }

    public int getEstmNumb() {
        return estmNumb;
    }

    public void setEstmNumb(int estmNumb) {
        this.estmNumb = estmNumb;
    }

    public Integer getStudentNumb() {
        return studentNumb;
    }

    public void setStudentNumb(Integer studentNumb) {
        this.studentNumb = studentNumb;
    }

    public String getIndEfc() {
        return indEfc;
    }

    public void setIndEfc(String indEfc) {
        this.indEfc = indEfc;
    }

    public String getIndExcloans() {
        return indExcloans;
    }

    public void setIndExcloans(String indExcloans) {
        this.indExcloans = indExcloans;
    }

    public String getIndEalsu() {
        return indEalsu;
    }

    public void setIndEalsu(String indEalsu) {
        this.indEalsu = indEalsu;
    }

    public String getIndEanonlsu() {
        return indEanonlsu;
    }

    public void setIndEanonlsu(String indEanonlsu) {
        this.indEanonlsu = indEanonlsu;
    }

    public Integer getStudentAdFamilyIncome() {
        return studentAdFamilyIncome;
    }

    public void setStudentAdFamilyIncome(Integer studentAdFamilyIncome) {
        this.studentAdFamilyIncome = studentAdFamilyIncome;
    }

    public Integer getStudentAeFamilyAsset() {
        return studentAeFamilyAsset;
    }

    public void setStudentAeFamilyAsset(Integer studentAeFamilyAsset) {
        this.studentAeFamilyAsset = studentAeFamilyAsset;
    }

    public Date getStudentByDom() {
        return studentByDom;
    }

    public void setStudentByDom(Date studentByDom) {
        this.studentByDom = studentByDom;
    }

    public Date getStudentBvDoe() {
        return studentBvDoe;
    }

    public void setStudentBvDoe(Date studentBvDoe) {
        this.studentBvDoe = studentBvDoe;
    }

    public String getAdjCalgrantInd() {
        return adjCalgrantInd;
    }

    public void setAdjCalgrantInd(String adjCalgrantInd) {
        this.adjCalgrantInd = adjCalgrantInd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHomeAddrApt() {
        return homeAddrApt;
    }

    public void setHomeAddrApt(String homeAddrApt) {
        this.homeAddrApt = homeAddrApt;
    }

    public Integer getHomecostudies() {
        return homecostudies;
    }

    public void setHomecostudies(Integer homecostudies) {
        this.homecostudies = homecostudies;
    }


    public String getTzdoe() {
        return tzdoe;
    }

    public void setTzdoe(String tzdoe) {
        this.tzdoe = tzdoe;
    }

    public String getTzdom() {
        return tzdom;
    }

    public void setTzdom(String tzdom) {
        this.tzdom = tzdom;
    }

    public String getTzup() {
        return tzup;
    }

    public void setTzup(String tzup) {
        this.tzup = tzup;
    }

    public String getTzdown() {
        return tzdown;
    }

    public void setTzdown(String tzdown) {
        this.tzdown = tzdown;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(Integer counselorId) {
        this.counselorId = counselorId;
    }

    public String getModRoot() {
        return modRoot;
    }

    public void setModRoot(String modRoot) {
        this.modRoot = modRoot;
    }

    public String getModPre() {
        return modPre;
    }

    public void setModPre(String modPre) {
        this.modPre = modPre;
    }

    public String getStudentBxModCounselor() {
        return studentBxModCounselor;
    }

    public void setStudentBxModCounselor(String studentBxModCounselor) {
        this.studentBxModCounselor = studentBxModCounselor;
    }

    public String getStudentBuOrigCounselor() {
        return studentBuOrigCounselor;
    }

    public void setStudentBuOrigCounselor(String studentBuOrigCounselor) {
        this.studentBuOrigCounselor = studentBuOrigCounselor;
    }

    public Integer getCounselorOrig() {
        return counselorOrig;
    }

    public void setCounselorOrig(Integer counselorOrig) {
        this.counselorOrig = counselorOrig;
    }

    public Integer getCounselorMod() {
        return counselorMod;
    }

    public void setCounselorMod(Integer counselorMod) {
        this.counselorMod = counselorMod;
    }

    public String getLostTz() {
        return lostTz;
    }

    public void setLostTz(String lostTz) {
        this.lostTz = lostTz;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public long getDup() {
        return dup;
    }

    public void setDup(long dup) {
        this.dup = dup;
    }

    public long getDdown() {
        return ddown;
    }

    public void setDdown(long ddown) {
        this.ddown = ddown;
    }

    public long getDdoe() {
        return ddoe;
    }

    public void setDdoe(long ddoe) {
        this.ddoe = ddoe;
    }

    public long getDdom() {
        return ddom;
    }

    public void setDdom(long ddom) {
        this.ddom = ddom;
    }

    public long getLostTime() {
        return lostTime;
    }

    public void setLostTime(long lostTime) {
        this.lostTime = lostTime;
    }

    public String getLostToMaster() {
        return lostToMaster;
    }

    public void setLostToMaster(String lostToMaster) {
        this.lostToMaster = lostToMaster;
    }

    public String getLostToLocal() {
        return lostToLocal;
    }

    public void setLostToLocal(String lostToLocal) {
        this.lostToLocal = lostToLocal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recid != null ? recid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.recid == null && other.recid != null) || (this.recid != null && !this.recid.equals(other.recid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Student[ recid=" + recid + " ]";
    }

    /**
     * @return the fund2id
     */
    public Integer getFund2id() {
        return fund2id;
    }

    /**
     * @param fund2id the fund2id to set
     */
    public void setFund2id(Integer fund2id) {
        this.fund2id = fund2id;
    }

    /**
     * @return the fund3id
     */
    public Integer getFund3id() {
        return fund3id;
    }

    /**
     * @param fund3id the fund3id to set
     */
    public void setFund3id(Integer fund3id) {
        this.fund3id = fund3id;
    }

    /**
     * @return the fund4id
     */
    public Integer getFund4id() {
        return fund4id;
    }

    /**
     * @param fund4id the fund4id to set
     */
    public void setFund4id(Integer fund4id) {
        this.fund4id = fund4id;
    }

    /**
     * @return the fund5id
     */
    public Integer getFund5id() {
        return fund5id;
    }

    /**
     * @param fund5id the fund5id to set
     */
    public void setFund5id(Integer fund5id) {
        this.fund5id = fund5id;
    }

    /**
     * @return the fund6id
     */
    public Integer getFund6id() {
        return fund6id;
    }

    /**
     * @param fund6id the fund6id to set
     */
    public void setFund6id(Integer fund6id) {
        this.fund6id = fund6id;
    }

    /**
     * @return the fund7id
     */
    public Integer getFund7id() {
//        System.out.println("ggggggggggggggggggggggggggg stud getFund7id( ) as "+fund7id+" while stud recid= "+recid);
        return fund7id;
    }

    /**
     * @param fund7id the fund7id to set
     */
    public void setFund7id(Integer fund7id) {
 //       System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSS stud setFund7id( ) as "+fund7id+" while stud recid= "+recid);
        this.fund7id = fund7id;
    }
    
    /**
     * @return the fund8id
     */
    public Integer getFund8id() {
        return fund8id;
    }

    /**
     * @param fund8id the fund8id to set
     */
    public void setFund8id(Integer fund8id) {
        this.fund8id = fund8id;
    }
     
    /**
     * @return the fund9id
     */
    public Integer getFund9id() {
        return fund9id;
    }

    /**
     * @param fund9id the fund9id to set
     */
    public void setFund9id(Integer fund9id) {
        this.fund9id = fund9id;
    }
    
    
    public Integer getFund1id() {
        return fund1id;
    }

    public void setFund1id(Integer fund1id) {
        this.fund1id = fund1id;
    }

    public List<Print> getPdfs() {
        return pdfs;
    }

    public void setPdfs(List<Print> pdfs) {
        this.pdfs = pdfs;
    }

    public Integer getPrtTimes() {
        return prtTimes;
    }

    public void setPrtTimes(Integer prtTimes) {
        this.prtTimes = prtTimes;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public String getTzdid() {
        return tzdid;
    }

    public void setTzdid(String tzdid) {
        this.tzdid = tzdid;
    }

    public String getDidstr() {
        return didstr;
    }

    public void setDidstr(String didstr) {
        this.didstr = didstr;
    }

    /**
     * @return the returnStdInd
     */
    public Integer getReturnStdInd() {
        return returnStdInd;
    }

    /**
     * @param returnStdInd the returnStdInd to set
     */
    public void setReturnStdInd(Integer returnStdInd) {
        this.returnStdInd = returnStdInd;
    }

    /**
     * @return the ncStdInd
     */
    public Integer getNcStdInd() {
        return ncStdInd;
    }

    /**
     * @param ncStdInd the ncStdInd to set
     */
    public void setNcStdInd(Integer ncStdInd) {
        this.ncStdInd = ncStdInd;
    }

    public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

    public String getPuser_id() {
        return puser_id;
    }

    public void setPuser_id(String puser_id) {
        this.puser_id = puser_id;
    }

    public Integer getEa_lsu_perc() {
        return ea_lsu_perc;
    }

    public void setEa_lsu_perc(Integer ea_lsu_perc) {
        this.ea_lsu_perc = ea_lsu_perc;
    }

    public Integer getEa_nonlsu_perc() {
        return ea_nonlsu_perc;
    }

    public void setEa_nonlsu_perc(Integer ea_nonlsu_perc) {
        this.ea_nonlsu_perc = ea_nonlsu_perc;
    }

    public String getTerm_code1() {
        return term_code1;
    }

    public void setTerm_code1(String term_code1) {
        this.term_code1 = term_code1;
    }

    public String getTerm_code2() {
        return term_code2;
    }

    public void setTerm_code2(String term_code2) {
        this.term_code2 = term_code2;
    }

    public String getTerm_code3() {
        return term_code3;
    }

    public void setTerm_code3(String term_code3) {
        this.term_code3 = term_code3;
    }

    public String getTerm_code4() {
        return term_code4;
    }

    public void setTerm_code4(String term_code4) {
        this.term_code4 = term_code4;
    }

    public Integer getTerm_load1() {
        return term_load1;
    }

    public void setTerm_load1(Integer term_load1) {
        this.term_load1 = term_load1;
    }

    public Integer getTerm_load2() {
        return term_load2;
    }

    public void setTerm_load2(Integer term_load2) {
        this.term_load2 = term_load2;
    }

    public Integer getTerm_load3() {
        return term_load3;
    }

    public void setTerm_load3(Integer term_load3) {
        this.term_load3 = term_load3;
    }

    public Integer getTerm_load4() {
        return term_load4;
    }

    public void setTerm_load4(Integer term_load4) {
        this.term_load4 = term_load4;
    }

    public BigDecimal getTerm_unit1() {
        return term_unit1;
    }

    public void setTerm_unit1(BigDecimal term_unit1) {
        this.term_unit1 = term_unit1;
    }

    public BigDecimal getTerm_unit2() {
        return term_unit2;
    }

    public void setTerm_unit2(BigDecimal term_unit2) {
        this.term_unit2 = term_unit2;
    }

    public BigDecimal getTerm_unit3() {
        return term_unit3;
    }

    public void setTerm_unit3(BigDecimal term_unit3) {
        this.term_unit3 = term_unit3;
    }

    public BigDecimal getTerm_unit4() {
        return term_unit4;
    }

    public void setTerm_unit4(BigDecimal term_unit4) {
        this.term_unit4 = term_unit4;
    }

    public String getTerm_prog1() {
        return term_prog1;
    }

    public void setTerm_prog1(String term_prog1) {
        this.term_prog1 = term_prog1;
    }

    public String getTerm_prog2() {
        return term_prog2;
    }

    public void setTerm_prog2(String term_prog2) {
        this.term_prog2 = term_prog2;
    }

    public String getTerm_prog3() {
        return term_prog3;
    }

    public void setTerm_prog3(String term_prog3) {
        this.term_prog3 = term_prog3;
    }

    public String getTerm_prog4() {
        return term_prog4;
    }

    public void setTerm_prog4(String term_prog4) {
        this.term_prog4 = term_prog4;
    }

    public Integer getStd_transfer_ind() {
        return std_transfer_ind;
    }

    public void setStd_transfer_ind(Integer std_transfer_ind) {
        this.std_transfer_ind = std_transfer_ind;
    }

    public Integer getStd_1st_freshmen() {
        return std_1st_freshmen;
    }

    public void setStd_1st_freshmen(Integer std_1st_freshmen) {
        this.std_1st_freshmen = std_1st_freshmen;
    }
    
}

/*

    @Basic(optional = false)
    @NotNull
    @Column(name = "STUDENT_NUMB")      
//    @TableGenerator(name = "Seq_Gen", table = "SEQUENCE", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "Addr_Gen", initialValue = 10000, allocationSize = 100)    
    @TableGenerator(name = "ID_Gen", table = "SEQUENCE", pkColumnName="NAME",valueColumnName="ID", pkColumnValue="STUD_GEN", initialValue = 2, allocationSize = 1 ) 
    ////for glassfish default, they will be SEQUENCE, SEQ_NAME, SEQ_COUNT
    // initialValue is the initial value assigned to the primary key sequence. The default value is 0. The sequence is incremented by a value of 1. 
    //The allocationSize is the cache size into which the persistence engine reads from the sequence table. The default value is 50.
    @GeneratedValue(strategy = GenerationType.TABLE, generator="ID_Gen")    
    
    private Integer studentNumb; 
 */

/**** this works with derby sequence, pre created by hand **********
    @SequenceGenerator(name="Stud_Gen", sequenceName="STUD_GEN",  initialValue = 12,   allocationSize = 1)//initialValue = 1, 
    //the sequenceName element to specify the name of the database sequence object to use.
    //If the sequence object already exists in the database, then you must specify the allocationSize to match the INCREMENT value of the database sequence object
    @GeneratedValue(strategy= GenerationType.AUTO, generator="Stud_Gen")     */



/*
 
    /*     

  @PrePersist
  public void prePersist() {
    System.out.println("@PrePersist");
  }

  @PostPersist
  public void postPersist() {
    System.out.println("@PostPersist");
  }

  @PostLoad
  public void postLoad() {
    System.out.println("@PostLoad");
  }

  @PreUpdate
  public void preUpdate() {
    System.out.println("@PreUpdate");
  }

  @PostUpdate
  public void postUpdate() {
    System.out.println("@PostUpdate");
  }

  @PreRemove
  public void preRemove() {
    System.out.println("@PreRemove");
  }

  @PostRemove
  public void postRemove() {
    System.out.println("@PostRemove");
  }
      
 */