/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author kwang
 */
@Entity
@Table(name = "COUNSELORS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Counselor.findAll", query = "SELECT c FROM Counselor c"),
    @NamedQuery(name = "Counselor.findByEmail", query = "SELECT c FROM Counselor c WHERE c.email = :email"),
    @NamedQuery(name = "Counselor.findByUsername", query = "SELECT c FROM Counselor c WHERE upper(c.username) = upper(:username)"),
    @NamedQuery(name = "Counselor.findByUsernameAndStatus", query = "SELECT c FROM Counselor c WHERE c.username = :username and c.status = :status"),
    @NamedQuery(name = "Counselor.findBySuperuser", query = "SELECT c FROM Counselor c WHERE c.superuser = :superuser"),
    @NamedQuery(name = "Counselor.findByUserid", query = "SELECT c FROM Counselor c WHERE c.userid = :userid"),
    @NamedQuery(name = "Counselor.findByCreator", query = "SELECT c FROM Counselor c WHERE c.creator = :creator"),
    @NamedQuery(name = "Counselor.findByStatus", query = "SELECT c FROM Counselor c WHERE c.status = :status"),
    @NamedQuery(name = "Counselor.findByEditor", query = "SELECT c FROM Counselor c WHERE c.editor = :editor"),
    @NamedQuery(name = "Counselor.findByDom", query = "SELECT c FROM Counselor c WHERE c.dom = :dom"),
    @NamedQuery(name = "Counselor.findByDoe", query = "SELECT c FROM Counselor c WHERE c.doe = :doe"),
    @NamedQuery(name = "Counselor.findByDoetz", query = "SELECT c FROM Counselor c WHERE c.doetz = :doetz"),
    @NamedQuery(name = "Counselor.findByDomtz", query = "SELECT c FROM Counselor c WHERE c.domtz = :domtz"),
    @NamedQuery(name = "Counselor.findByShadow", query = "SELECT c FROM Counselor c WHERE c.shadow = :shadow"),
    @NamedQuery(name = "Counselor.findByLsuid", query = "SELECT c FROM Counselor c WHERE c.lsuid = :lsuid")})
public class Counselor implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 40)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 30)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUPERUSER")
    private Integer superuser;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERID")
    private Integer userid;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATOR")
    private int creator;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;
    @Column(name = "EDITOR")
    private Integer editor;
    
    @Column(name = "DOM")
    private long dom;
    @Column(name = "DOE")
    private long doe;
    @Size(max = 20)
    @Column(name = "DOETZ")
    private String doetz;
    @Size(max = 20)
    @Column(name = "DOMTZ")
    private String domtz;
    
    
    @Column(name = "DOS")
    private long dos;
    
    @Size(max = 20)
    @Column(name = "DOSTZ")
    private String dostz;
    
    @Size(max = 256)
    @Column(name = "SHADOW")
    private String shadow;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "LSUID")
    private String lsuid;
    
    @Column(name="DEPTNAME")
    @NotNull
    @NotEmpty
    @Size(max=30)
    private String deptName;
    
    @Column(name="STD_IND")
    @Range(min=0, max=1)
    private Integer stdInd;
    
    
    public Counselor() {
    }

    public Counselor(Integer userid) {
        this.userid = userid;
    }

    public Counselor(Integer userid, Integer superuser, int creator, int status, String lsuid) {
        this.userid = userid;
        this.superuser = superuser;
        this.creator = creator;
        this.status = status;
        this.lsuid = lsuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSuperuser() {
        return superuser;
    }

    public void setSuperuser(Integer superuser) {
        this.superuser = superuser;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getEditor() {
        return editor;
    }

    public void setEditor(Integer editor) {
        this.editor = editor;
    }

    public long getDom() {
        return dom;
    }

    public void setDom(long dom) {
        this.dom = dom;
    }

    public long getDoe() {
        return doe;
    }

    public void setDoe(long doe) {
        this.doe = doe;
    }

    public String getDoetz() {
        return doetz;
    }

    public void setDoetz(String doetz) {
        this.doetz = doetz;
    }

    public String getDomtz() {
        return domtz;
    }

    public void setDomtz(String domtz) {
        this.domtz = domtz;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
    }

    public String getLsuid() {
        return lsuid;
    }

    public void setLsuid(String lsuid) {
        this.lsuid = lsuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Counselor)) {
            return false;
        }
        Counselor other = (Counselor) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Counselor[ userid=" + userid + " ]";
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public long getDos() {
        return dos;
    }

    public void setDos(long dos) {
        this.dos = dos;
    }

    public String getDostz() {
        return dostz;
    }

    public void setDostz(String dostz) {
        this.dostz = dostz;
    }

    public Integer getStdInd() {
        return stdInd;
    }

    public void setStdInd(Integer stdInd) {
        this.stdInd = stdInd;
    }
    
}


/*
 
        @Id
 //   @GeneratedValue(strategy = GenerationType.IDENTITY) //# Path: userid ########### FAILED: may not be null

    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "counselors_userid_seq") // The sequence named [counselors_userid_seq] is setup incorrectly.  Its increment does not match its pre-allocation size.
 ////   @SequenceGenerator(name = "counselors_userid_seq", sequenceName = "counselors_userid_seq")
    //The sequence named [counselors_userid_seq] is setup incorrectly.  Its increment does not match its pre-allocation size.
    @Column(name = "userid",  insertable = false, nullable=false)
 //   @Basic(optional = false)
 //   @NotNull
 //   @Column(name = "userid", nullable = false)
 //   @GeneratedValue(strategy = GenerationType.AUTO) //Call: UPDATE SEQUENCE SET SEQ_COUNT = SEQ_COUNT + ? WHERE SEQ_NAME = ?  //Query: ValueReadQuery(name="SEQUENCE" sql="SELECT SEQ_COUNT FROM SEQUENCE WHERE SEQ_NAME = ?")
        
//  @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userid;
 */