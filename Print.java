/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kwang
 */
@Entity
@Table(name = "PRINTS")
@XmlRootElement
@NamedQueries({
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
    //@NamedQuery(name = "Print.findByCalGrant", query = "SELECT o FROM Print o WHERE o.calGrant = :calGrant"),
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
     //dou dod fisy
@NamedQuery(name = "Print.findByClientIdNupNdownFisy", query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime>0"),  
@NamedQuery(name = "Print.findByClientIdNupNdownFisyNonPrt", query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime=0 and o.recid=:recid"),
//@NamedQuery(name = "Print.findNonPrtByClientIdNupNdownFisy", query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime=0 and o.recid=:recid"),
    @NamedQuery(name = "Print.findBySubloan", query = "SELECT o FROM Print o WHERE o.subloan = :subloan"),
    @NamedQuery(name = "Print.findByUnsubloan", query = "SELECT o FROM Print o WHERE o.unsubloan = :unsubloan"),
    @NamedQuery(name = "Print.findByTotLoan", query = "SELECT o FROM Print o WHERE o.totLoan = :totLoan")})
public class Print implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRT_NUM") 
    @TableGenerator(name = "PRTSEQ_Gen", table = "SEQUENCE", pkColumnName="NAME",valueColumnName="ID", pkColumnValue="PRT_GEN", initialValue = 1, allocationSize = 1 )    
    @GeneratedValue(strategy = GenerationType.TABLE, generator="PRTSEQ_Gen")
    private Integer prtNum;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PRT_ID")
    private String prtId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "RECID")
    private String recid;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CLIENT_ID")
    private int clientId;
    @Column(name = "COUNSELOR_ID")
    private Integer counselorId;
    @Size(max = 100)
    @Column(name = "COUNSELOR_NAME")
    private String counselorName;
        
    
    @ManyToOne(optional = false, targetEntity=Student.class, fetch= FetchType.LAZY) //cascade=CascadeType { ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH}
    @JoinColumn(name = "RECID", referencedColumnName = "RECID", nullable = false, insertable = false, updatable = false) //name defines  Foreign Key (FK) column;  
            //referencedColumnName element of the @JoinColumn annotation is used to define the column in the referenced table for the relationship, which is (DEFAULT) the Primary Key (PK) of the referenced table. 
    private Student stud;
     //@JoinColumn(name="OWNER_ID", referencedColumnName="EMP_ID")
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRT_TIME")
    private long prtTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PRT_TZ")
    private String prtTz;
    
    @Column(name = "TUITION_FEE")
    private Integer tuitionFee;
    @Column(name = "ROOM_BOARD")
    private Integer roomBoard;
    @Column(name = "DUE")
    private Integer due;
    @Column(name = "YEAR_OPT")
    private Integer yearOpt;
    @Column(name = "QUARTER_OPT")
    private Integer quarterOpt;
    @Column(name = "MONTH_OPT")
    private Integer monthOpt;
    @Column(name = "TOT_CHARGES")
    private Integer totCharges;
    @Column(name = "TOT_AID")
    private Integer totAid;
    @Column(name = "TOT_AID_WO_WORK")
    private Integer totAidWoWork;
    @Column(name = "FWS")
    private Integer fws;
    @Column(name = "OTHER_EXPENSES")
    private Integer otherExpenses;
    @Column(name = "EFC")
    private Integer efc;
    @Column(name = "MAX_NEED")
    private Integer maxNeed;
    @Column(name = "COA")
    private Integer coa;
    @Column(name = "PELL")
    private Integer pell;
    
    @Column(name = "CAL_GRANTA")
    private Integer calGrantA;
    @Column(name = "CAL_GRANTB")
    private Integer calGrantB;
    
    @Column(name = "NONCAL_GRANT")
    private Integer noncalGrant;
    @Column(name = "PSEOG")
    private Integer pseog;
    @Column(name = "NON_LSU_EA")
    private Integer nonLsuEa;
    @Column(name = "NON_LSU_SSHIP")
    private Integer nonLsuSship;
    @Column(name = "LSU_EA")
    private Integer lsuEa;
    @Column(name = "LSU_SSHIP")
    private Integer lsuSship;
    @Column(name = "SDA")
    private Integer sda;
    @Column(name = "FAMILY_DISCT")
    private Integer familyDisct;
    @Column(name = "NATL_MERIT")
    private Integer natlMerit;
    @Column(name = "PERKINS")
    private Integer perkins;
     
    
    @Size(max = 70)
    @Column(name = "SSHIP1_NAME")
    private String sship1Name;
    @Column(name = "SSHIP1_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship1LsuAmt;    
    @Column(name = "SSHIP1_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship1ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP2_NAME")
    private String sship2Name;
    @Column(name = "SSHIP2_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship2LsuAmt;    
    @Column(name = "SSHIP2_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship2ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP3_NAME")
    private String sship3Name;
    @Column(name = "SSHIP3_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship3LsuAmt;    
    @Column(name = "SSHIP3_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship3ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP4_NAME")
    private String sship4Name;
    @Column(name = "SSHIP4_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship4LsuAmt;    
    @Column(name = "SSHIP4_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship4ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP5_NAME")
    private String sship5Name;
    @Column(name = "SSHIP5_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship5LsuAmt;    
    @Column(name = "SSHIP5_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship5ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP6_NAME")
    private String sship6Name;
    @Column(name = "SSHIP6_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship6LsuAmt;    
    @Column(name = "SSHIP6_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship6ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP7_NAME")
    private String sship7Name;
    @Column(name = "SSHIP7_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship7LsuAmt;    
    @Column(name = "SSHIP7_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship7ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP8_NAME")
    private String sship8Name;
    @Column(name = "SSHIP8_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship8LsuAmt;    
    @Column(name = "SSHIP8_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship8ExtAmt;
    
    @Size(max = 70)
    @Column(name = "SSHIP9_NAME")
    private String sship9Name;
    @Column(name = "SSHIP9_LSU_AMT")
    @Min(0)
    @NotNull
    private Integer sship9LsuAmt;    
    @Column(name = "SSHIP9_EXT_AMT")
    @Min(0)
    @NotNull
    private Integer sship9ExtAmt;
    
    @Column(name = "SUBLOAN")
    private Integer subloan;
    @Column(name = "UNSUBLOAN")
    private Integer unsubloan;
    @Column(name = "TOT_LOAN")
    private Integer totLoan;
    
    
    @Column(name = "DUP")
    private long dou;
    
    @Size(min = 1, max = 20)
    @Column(name = "DUP_TZ")
    private String douTz;
    
    
    @Column(name = "DDOWN")
    private long dod;
    
    @Size(min = 1, max = 20)
    @Column(name = "DDOWN_TZ")
    private String dodTz;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FISY")
    private Integer fisy;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FISY_PRT")
    private String fisyPrt;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSTR")
    private String versions;
    
    @Column( name = "LSU_PERF")
    private Integer lsuPerf;
    
    @Column( name = "EARNINGS")
    private Integer earnings;
    
    //added 2012/02/13, to hold three new awards
    @Column( name = "NEEDGRANT")
    private Integer needGrant;
    
    @Column( name = "ACHIEVEAWARD")
    private Integer achieveAward;
    
    @Column( name = "RENEW4Y")
    private Integer renew4y;    
    
    //2016-02 added "La Sierra Univ. Grant"
    @Column( name="LSUGRANT")
    private Integer lsugrant;
    
    public Print() {
    }

    public Print(String prtId) {
        this.prtId = prtId;
    }

    public Print(String prtId, String recid, int clientId, Integer prtNum, long prtTime, String prtTz) {
        this.prtId = prtId;
        this.recid = recid;
        this.clientId = clientId;
        this.prtNum = prtNum;
        this.prtTime = prtTime;
        this.prtTz = prtTz;
    }

    public String getPrtId() {
        return prtId;
    }

    public void setPrtId(String prtId) {
        this.prtId = prtId;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Integer getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(Integer counselorId) {
        this.counselorId = counselorId;
    }

    public String getCounselorName() {
        return counselorName;
    }

    public void setCounselorName(String counselorName) {
        this.counselorName = counselorName;
    }

    public Integer getPrtNum() {
        return prtNum;
    }

    public void setPrtNum(Integer prtNum) {
        this.prtNum = prtNum;
    }

    public long getPrtTime() {
        return prtTime;
    }

    public void setPrtTime(long prtTime) {
        this.prtTime = prtTime;
    }

    public String getPrtTz() {
        return prtTz;
    }

    public void setPrtTz(String prtTz) {
        this.prtTz = prtTz;
    }

    public Integer getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(Integer tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public Integer getRoomBoard() {
        return roomBoard;
    }

    public void setRoomBoard(Integer roomBoard) {
        this.roomBoard = roomBoard;
    }

    public Integer getDue() {
        return due;
    }

    public void setDue(Integer due) {
        this.due = due;
    }

    public Integer getYearOpt() {
        return yearOpt;
    }

    public void setYearOpt(Integer yearOpt) {
        this.yearOpt = yearOpt;
    }

    public Integer getQuarterOpt() {
        return quarterOpt;
    }

    public void setQuarterOpt(Integer quarterOpt) {
        this.quarterOpt = quarterOpt;
    }

    public Integer getMonthOpt() {
        return monthOpt;
    }

    public void setMonthOpt(Integer monthOpt) {
        this.monthOpt = monthOpt;
    }

    public Integer getTotCharges() {
        return totCharges;
    }

    public void setTotCharges(Integer totCharges) {
        this.totCharges = totCharges;
    }

    public Integer getTotAid() {
        return totAid;
    }

    public void setTotAid(Integer totAid) {
        this.totAid = totAid;
    }

    public Integer getTotAidWoWork() {
        return totAidWoWork;
    }

    public void setTotAidWoWork(Integer totAidWoWork) {
        this.totAidWoWork = totAidWoWork;
    }

    public Integer getFws() {
        return fws;
    }

    public void setFws(Integer fws) {
        this.fws = fws;
    }

    public Integer getOtherExpenses() {
        return otherExpenses;
    }

    public void setOtherExpenses(Integer otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public Integer getEfc() {
        return efc;
    }

    public void setEfc(Integer efc) {
        this.efc = efc;
    }

    public Integer getMaxNeed() {
        return maxNeed;
    }

    public void setMaxNeed(Integer maxNeed) {
        this.maxNeed = maxNeed;
    }

    public Integer getCoa() {
        return coa;
    }

    public void setCoa(Integer coa) {
        this.coa = coa;
    }

    public Integer getPell() {
        return pell;
    }

    public void setPell(Integer pell) {
        this.pell = pell;
    }
 

    public Integer getNoncalGrant() {
        return noncalGrant;
    }

    public void setNoncalGrant(Integer noncalGrant) {
        this.noncalGrant = noncalGrant;
    }

    public Integer getPseog() {
        return pseog;
    }

    public void setPseog(Integer pseog) {
        this.pseog = pseog;
    }

    public Integer getNonLsuEa() {
        return nonLsuEa;
    }

    public void setNonLsuEa(Integer nonLsuEa) {
        this.nonLsuEa = nonLsuEa;
    }

    public Integer getNonLsuSship() {
        return nonLsuSship;
    }

    public void setNonLsuSship(Integer nonLsuSship) {
        this.nonLsuSship = nonLsuSship;
    }

    public Integer getLsuEa() {
        return lsuEa;
    }

    public void setLsuEa(Integer lsuEa) {
        this.lsuEa = lsuEa;
    }

    public Integer getLsuSship() {
        return lsuSship;
    }

    public void setLsuSship(Integer lsuSship) {
        this.lsuSship = lsuSship;
    }

    public Integer getSda() {
        return sda;
    }

    public void setSda(Integer sda) {
        this.sda = sda;
    }

    public Integer getFamilyDisct() {
        return familyDisct;
    }

    public void setFamilyDisct(Integer familyDisct) {
        this.familyDisct = familyDisct;
    }

    public Integer getNatlMerit() {
        return natlMerit;
    }

    public void setNatlMerit(Integer natlMerit) {
        this.natlMerit = natlMerit;
    }

    public Integer getPerkins() {
        return perkins;
    }

    public void setPerkins(Integer perkins) {
        this.perkins = perkins;
    }
 

    public String getSship1Name() {
        return sship1Name;
    }

    public void setSship1Name(String sship1Name) {
        this.sship1Name = sship1Name;
    }


    public Integer getSubloan() {
        return subloan;
    }

    public void setSubloan(Integer subloan) {
        this.subloan = subloan;
    }

    public Integer getUnsubloan() {
        return unsubloan;
    }

    public void setUnsubloan(Integer unsubloan) {
        this.unsubloan = unsubloan;
    }

    public Integer getTotLoan() {
        return totLoan;
    }

    public void setTotLoan(Integer totLoan) {
        this.totLoan = totLoan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prtId != null ? prtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Print)) {
            return false;
        }
        Print other = (Print) object;
        if ((this.prtId == null && other.prtId != null) || (this.prtId != null && !this.prtId.equals(other.prtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Print[ prtId=" + prtId + " ]";
    }

    /**
     * @return the dou
     */
    public long getDou() {
        return dou;
    }

    /**
     * @param dou the dou to set
     */
    public void setDou(long dou) {
        this.dou = dou;
    }

    /**
     * @return the douTz
     */
    public String getDouTz() {
        return douTz;
    }

    /**
     * @param douTz the douTz to set
     */
    public void setDouTz(String douTz) {
        this.douTz = douTz;
    }

    /**
     * @return the dod
     */
    public long getDod() {
        return dod;
    }

    /**
     * @param dod the dod to set
     */
    public void setDod(long dod) {
        this.dod = dod;
    }

    /**
     * @return the dodTz
     */
    public String getDodTz() {
        return dodTz;
    }

    /**
     * @param dodTz the dodTz to set
     */
    public void setDodTz(String dodTz) {
        this.dodTz = dodTz;
    }

    /**
     * @return the fisy
     */
    public Integer getFisy() {
        return fisy;
    }

    /**
     * @param fisy the fisy to set
     */
    public void setFisy(Integer fisy) {
        this.fisy = fisy;
    }

    /**
     * @return the fisyPrt
     */
    public String getFisyPrt() {
        return fisyPrt;
    }

    /**
     * @param fisyPrt the fisyPrt to set
     */
    public void setFisyPrt(String fisyPrt) {
        this.fisyPrt = fisyPrt;
    }

    /**
     * @return the versions
     */
    public String getVersions() {
        return versions;
    }

    /**
     * @param versions the versions to set
     */
    public void setVersions(String versions) {
        this.versions = versions;
    }

    /* 
    public Student getStud() {
        return stud;
    } 
    public void setStud(Student stud) {
        this.stud = stud;
    }
    */

    public Student getStud() {
        return stud;
    }

    public void setStud(Student stud) {
        this.stud = stud;
    }

    public Integer getSship1LsuAmt() {
        return sship1LsuAmt;
    }

    public void setSship1LsuAmt(Integer sship1LsuAmt) {
        this.sship1LsuAmt = sship1LsuAmt;
    }

    public Integer getSship1ExtAmt() {
        return sship1ExtAmt;
    }

    public void setSship1ExtAmt(Integer sship1ExtAmt) {
        this.sship1ExtAmt = sship1ExtAmt;
    }

    public String getSship2Name() {
        return sship2Name;
    }

    public void setSship2Name(String sship2Name) {
        this.sship2Name = sship2Name;
    }

    public Integer getSship2LsuAmt() {
        return sship2LsuAmt;
    }

    public void setSship2LsuAmt(Integer sship2LsuAmt) {
        this.sship2LsuAmt = sship2LsuAmt;
    }

    public Integer getSship2ExtAmt() {
        return sship2ExtAmt;
    }

    public void setSship2ExtAmt(Integer sship2ExtAmt) {
        this.sship2ExtAmt = sship2ExtAmt;
    }

    public String getSship3Name() {
        return sship3Name;
    }

    public void setSship3Name(String sship3Name) {
        this.sship3Name = sship3Name;
    }

    public Integer getSship3LsuAmt() {
        return sship3LsuAmt;
    }

    public void setSship3LsuAmt(Integer sship3LsuAmt) {
        this.sship3LsuAmt = sship3LsuAmt;
    }

    public Integer getSship3ExtAmt() {
        return sship3ExtAmt;
    }

    public void setSship3ExtAmt(Integer sship3ExtAmt) {
        this.sship3ExtAmt = sship3ExtAmt;
    }

    public String getSship4Name() {
        return sship4Name;
    }

    public void setSship4Name(String sship4Name) {
        this.sship4Name = sship4Name;
    }

    public Integer getSship4LsuAmt() {
        return sship4LsuAmt;
    }

    public void setSship4LsuAmt(Integer sship4LsuAmt) {
        this.sship4LsuAmt = sship4LsuAmt;
    }

    public Integer getSship4ExtAmt() {
        return sship4ExtAmt;
    }

    public void setSship4ExtAmt(Integer sship4ExtAmt) {
        this.sship4ExtAmt = sship4ExtAmt;
    }

    public String getSship5Name() {
        return sship5Name;
    }

    public void setSship5Name(String sship5Name) {
        this.sship5Name = sship5Name;
    }

    public Integer getSship5LsuAmt() {
        return sship5LsuAmt;
    }

    public void setSship5LsuAmt(Integer sship5LsuAmt) {
        this.sship5LsuAmt = sship5LsuAmt;
    }

    public Integer getSship5ExtAmt() {
        return sship5ExtAmt;
    }

    public void setSship5ExtAmt(Integer sship5ExtAmt) {
        this.sship5ExtAmt = sship5ExtAmt;
    }

    public String getSship6Name() {
        return sship6Name;
    }

    public void setSship6Name(String sship6Name) {
        this.sship6Name = sship6Name;
    }

    public Integer getSship6LsuAmt() {
        return sship6LsuAmt;
    }

    public void setSship6LsuAmt(Integer sship6LsuAmt) {
        this.sship6LsuAmt = sship6LsuAmt;
    }

    public Integer getSship6ExtAmt() {
        return sship6ExtAmt;
    }

    public void setSship6ExtAmt(Integer sship6ExtAmt) {
        this.sship6ExtAmt = sship6ExtAmt;
    }

    public String getSship7Name() {
        return sship7Name;
    }

    public void setSship7Name(String sship7Name) {
        this.sship7Name = sship7Name;
    }

    public Integer getSship7LsuAmt() {
        return sship7LsuAmt;
    }

    public void setSship7LsuAmt(Integer sship7LsuAmt) {
        this.sship7LsuAmt = sship7LsuAmt;
    }

    public Integer getSship7ExtAmt() {
        return sship7ExtAmt;
    }

    public void setSship7ExtAmt(Integer sship7ExtAmt) {
        this.sship7ExtAmt = sship7ExtAmt;
    }

    public String getSship8Name() {
        return sship8Name;
    }

    public void setSship8Name(String sship8Name) {
        this.sship8Name = sship8Name;
    }

    public Integer getSship8LsuAmt() {
        return sship8LsuAmt;
    }

    public void setSship8LsuAmt(Integer sship8LsuAmt) {
        this.sship8LsuAmt = sship8LsuAmt;
    }

    public Integer getSship8ExtAmt() {
        return sship8ExtAmt;
    }

    public void setSship8ExtAmt(Integer sship8ExtAmt) {
        this.sship8ExtAmt = sship8ExtAmt;
    }

    public String getSship9Name() {
        return sship9Name;
    }

    public void setSship9Name(String sship9Name) {
        this.sship9Name = sship9Name;
    }

    public Integer getSship9LsuAmt() {
        return sship9LsuAmt;
    }

    public void setSship9LsuAmt(Integer sship9LsuAmt) {
        this.sship9LsuAmt = sship9LsuAmt;
    }

    public Integer getSship9ExtAmt() {
        return sship9ExtAmt;
    }

    public void setSship9ExtAmt(Integer sship9ExtAmt) {
        this.sship9ExtAmt = sship9ExtAmt;
    }

    public Integer getCalGrantA() {
        return calGrantA;
    }

    public void setCalGrantA(Integer calGrantA) {
        this.calGrantA = calGrantA;
    }

    public Integer getCalGrantB() {
        return calGrantB;
    }

    public void setCalGrantB(Integer calGrantB) {
        this.calGrantB = calGrantB;
    }

    public Integer getLsuPerf() {
        return lsuPerf;
    }

    public void setLsuPerf(Integer lsuPerf) {
        this.lsuPerf = lsuPerf;
    }

    public Integer getEarnings() {
        return earnings;
    }

    public void setEarnings(Integer earnings) {
        this.earnings = earnings;
    }

    public Integer getNeedGrant() {
        return needGrant;
    }

    public void setNeedGrant(Integer needGrant) {
        this.needGrant = needGrant;
    }

    public Integer getAchieveAward() {
        return achieveAward;
    }

    public void setAchieveAward(Integer achieveAward) {
        this.achieveAward = achieveAward;
    }

    public Integer getRenew4y() {
        return renew4y;
    }

    public void setRenew4y(Integer renew4y) {
        this.renew4y = renew4y;
    }

    public Integer getLsugrant() {
        return lsugrant;
    }

    public void setLsugrant(Integer lsugrant) {
        this.lsugrant = lsugrant;
    }
}
