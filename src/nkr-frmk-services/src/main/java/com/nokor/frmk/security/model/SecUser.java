package com.nokor.frmk.security.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nokor.frmk.model.entity.audit.AuditEntityA;
import com.nokor.frmk.model.t9n.LocaleType;
import com.nokor.frmk.security.AuthorityFactory;
import com.nokor.frmk.security.SecProfileGrantedAuthority;
import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.spring.SaltedUser;
import com.nokor.frmk.security.spring.SecSaltGenerator;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "tu_sec_user")
public class SecUser extends AuditEntityA implements UserDetails, SaltedUser {
	/** */
	private static final long serialVersionUID = -4994748026472811207L;
	public final static String LOGIN_SEP_DUPLICATE = "-";
	
	private String login;
    private String desc;
    private String pwd;
    private Integer passwordFormat;
    private String passwordSalt;
    private String passwordQuestion;
    private String passwordAnswer;
    private Date expirationDate;
    private String email;
    private String comment;
    private Boolean isApproved;
    private Boolean isLockedOut;
    private Date lastLoginDate;
    private Date lastLockedOutDate;
    private Date lastPasswordChangeDate;
    private Integer failedPasswordAttemptCount = 0;
    private Integer failedPasswordAnswerAttemptCount = 0;
    private Date lastLoginIp;
    private SecProfile defaultProfile; 
    private LocaleType localeType;
	
	private List<SecProfile> profiles;
	private List<SecApplication> applications;

    private transient Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    private SecUser auditSecUser;

    /**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_usr_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
	 * @return the login
	 */
	@Column(name = "sec_usr_login", unique = true, nullable = false)
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * @return the desc
	 */
	@Column(name = "sec_usr_desc", nullable = false)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the password
	 */
	@Column(name = "sec_usr_pwd", nullable = false)
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param password the password to set
	 */
	public void setPwd(String password) {
		this.pwd = password;
	}
	
	/**
	 * @return the expirationDate
	 */
	@Column(name = "sec_usr_expiration_date", nullable = true)
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the passwordFormat
	 */
	@Column(name = "sec_usr_format", nullable = true)
	public Integer getPasswordFormat() {
		return passwordFormat;
	}

	/**
	 * @param passwordFormat the passwordFormat to set
	 */
	public void setPasswordFormat(Integer passwordFormat) {
		this.passwordFormat = passwordFormat;
	}

	/**
	 * @return the passwordSalt
	 */
	@Column(name = "sec_usr_salt", nullable = true)
	public String getPasswordSalt() {
		return passwordSalt;
	}

	/**
	 * @param passwordSalt the passwordSalt to set
	 */
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	
	/**
	 * 
	 */
	public void setPasswordSaltSelfProtection() {
		this.passwordSalt = SecSaltGenerator.generateSaltFromSecUserId(this);
	}

	/**
	 * @return the passwordQuestion
	 */
	@Column(name = "sec_usr_question", nullable = true)
	public String getPasswordQuestion() {
		return passwordQuestion;
	}

	/**
	 * @param passwordQuestion the passwordQuestion to set
	 */
	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

	/**
	 * @return the passwordAnswer
	 */
	@Column(name = "sec_usr_answer", nullable = true)
	public String getPasswordAnswer() {
		return passwordAnswer;
	}

	/**
	 * @param passwordAnswer the passwordAnswer to set
	 */
	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}

	/**
	 * @return the email
	 */
	@Column(name = "sec_usr_email", nullable = true)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the localeType
	 */
    @ManyToOne
    @JoinColumn(name="typ_loc_id", nullable = true)
	public LocaleType getLocaleType() {
		return localeType;
	}

	/**
	 * @param localeType the localeType to set
	 */
	public void setLocaleType(LocaleType localeType) {
		this.localeType = localeType;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "sec_usr_comment", nullable = true)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the isApproved
	 */
	@Column(name = "sec_usr_is_approved", nullable = true)
	public Boolean isApproved() {
		return isApproved != null ? isApproved : Boolean.TRUE;
	}

	/**
	 * @param isApproved the isApproved to set
	 */
	public void setApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * @return the isLockedOut
	 */
	@Column(name = "sec_usr_is_locked_out", nullable = true)
	public Boolean isLockedOut() {
		return isLockedOut != null ? isLockedOut : Boolean.FALSE;
	}

	/**
	 * @param isLockedOut the isLockedOut to set
	 */
	public void setLockedOut(Boolean isLockedOut) {
		this.isLockedOut = isLockedOut;
	}

	/**
	 * @return the lastLoginDate
	 */
	@Column(name = "sec_usr_last_login_date", nullable = true)
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * @return the lastLockedOutDate
	 */
	@Column(name = "sec_usr_last_locked_out_date", nullable = true)
	public Date getLastLockedOutDate() {
		return lastLockedOutDate;
	}

	/**
	 * @param lastLockedOutDate the lastLockedOutDate to set
	 */
	public void setLastLockedOutDate(Date lastLockedOutDate) {
		this.lastLockedOutDate = lastLockedOutDate;
	}

	/**
	 * @return the lastPasswordChangeDate
	 */
	@Column(name = "sec_usr_last_pwd_change_date", nullable = true)
	public Date getLastPasswordChangeDate() {
		return lastPasswordChangeDate != null ? lastPasswordChangeDate : getCreateDate();
	}

	/**
	 * @param lastPasswordChangeDate the lastPasswordChangeDate to set
	 */
	public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
		this.lastPasswordChangeDate = lastPasswordChangeDate;
	}

	/**
	 * @return the failedPasswordAttemptCount
	 */
	@Column(name = "sec_usr_failed_pwd_count", nullable = true)
	public Integer getFailedPasswordAttemptCount() {
		if (failedPasswordAttemptCount == null) {
			failedPasswordAttemptCount = 0;
		}
		return failedPasswordAttemptCount;
	}

	/**
	 * @param failedPasswordAttemptCount the failedPasswordAttemptCount to set
	 */
	public void setFailedPasswordAttemptCount(Integer failedPasswordAttemptCount) {
		if (failedPasswordAttemptCount == null) {
			failedPasswordAttemptCount = 0;
		}
		this.failedPasswordAttemptCount = failedPasswordAttemptCount;
	}

	/**
	 * @return the failedPasswordAnswerAttemptCount
	 */
	@Column(name = "sec_usr_failed_pwd_answer_count", nullable = true)
	public Integer getFailedPasswordAnswerAttemptCount() {
		if (failedPasswordAnswerAttemptCount == null) {
			failedPasswordAnswerAttemptCount = 0;
		}
		return failedPasswordAnswerAttemptCount;
	}

	/**
	 * @param failedPasswordAnswerAttemptCount the failedPasswordAnswerAttemptCount to set
	 */
	public void setFailedPasswordAnswerAttemptCount(Integer failedPasswordAnswerAttemptCount) {
		if (failedPasswordAnswerAttemptCount == null) {
			failedPasswordAnswerAttemptCount = 0;
		}
		this.failedPasswordAnswerAttemptCount = failedPasswordAnswerAttemptCount;
	}
	
	/**
	 * 
	 */
	@Transient
	public void incrementFailedPasswordAnswerAttemptCount() {
		failedPasswordAnswerAttemptCount++;
	}
	
	/**
	 * 
	 */
	@Transient
	public void clearFailedPasswordAnswerAttemptCount() {
		failedPasswordAnswerAttemptCount = 0;
	}

	/**
	 * @return the lastLoginIp
	 */
    @Column(name = "sec_usr_last_login_ip")
	public Date getLastLoginIp() {
		return lastLoginIp;
	}

	/**
	 * @param lastLoginIp the lastLoginIp to set
	 */
	public void setLastLoginIp(Date lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	
	/**
	 * @return the profiles
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="tu_sec_user_profile",
				joinColumns = { @JoinColumn(name = "sec_usr_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_pro_id") })
    @OnDelete(action=OnDeleteAction.NO_ACTION)
	public List<SecProfile> getProfiles() {
		if (profiles == null) {
			profiles = new ArrayList<SecProfile>();
		}
		return profiles;
	}
	/**
	 * 
	 * @return
	 */
	@Transient
	public void addProfile(SecProfile secPro) {
		getProfiles().add(secPro);
//		setDefaultProfile();
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<SecProfile> profiles) {
		this.profiles = profiles;
	}
	
	

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_sec_application_user",
				joinColumns = { @JoinColumn(name = "sec_usr_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_app_id") })
    @OnDelete(action=OnDeleteAction.NO_ACTION)
	public List<SecApplication> getApplications() {
		if (applications == null) {
			applications = new ArrayList<SecApplication>();
		}
		return applications;
	}

	/**
	 * 
	 * @param applications
	 */
	public void setApplications(List<SecApplication> applications) {
		this.applications = applications;
	}
	
	@Transient
	public boolean hasProfile(SecProfile secPro) {
		return secPro != null && hasProfile(profiles, secPro.getId());
	}
	
	@Transient
	public static boolean hasProfile(List<SecProfile> profiles, Long secProId) {
		if (profiles == null || secProId == null) {
			return false;
		}
		for (SecProfile pro : profiles) {
			if (pro != null && pro.getId() != null 
					&& pro.getId().equals(secProId)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 
	 * @return
	 */
	@Transient
	public boolean isInAdminProfile() {
		return hasProfile(profiles, SecProfile.DEFAULT_ADMIN_PROFILE_ID);
	}
	
	/**_______________________________________________________________________________________
     * 
     * START BLOCK [SPRING SECURITY]
     * _______________________________________________________________________________________
     */
	
	/**
	 * @return the defaultProfile
	 */
    @ManyToOne
    @JoinColumn(name="default_sec_pro_id", nullable = true)
	public SecProfile getDefaultProfile() {
    	setDefaultProfile();
		return defaultProfile;
	}

	/**
	 * @param defaultProfile the defaultProfile to set
	 */
	public void setDefaultProfile(SecProfile defaultProfile) {
		this.defaultProfile = defaultProfile;
//		if (getProfiles().size() == 0) {
//			addProfile(defaultProfile);
//		}
	}
	
	/**
	 * 
	 */
	private void setDefaultProfile() {
		if (defaultProfile == null && getProfiles().size() > 0) {
    		defaultProfile = getProfiles().get(0);
    	}
	}

	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 * A granted Authority is checled for example at the Login
	 * org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter()/invoke()/beforeInvocation()
	 *     org.springframework.security.access.vote..AffirmativeBased.decide()
	 *        org.springframework.security.access.vote.RoleVoter.vote() / rolePrefix = "ROLE_"
	 */
	@Override
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
		if (profiles != null) {
			for (SecProfile profile : profiles) {
				SecProfileGrantedAuthority auth = AuthorityFactory.getInstance().grantAuthority(profile);
				authorities.add(auth);
			}
		}
		//authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
        return authorities;
    }

	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
    @Override
    @Transient
    public String getPassword() {
        return pwd;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    @Transient
    public String getUsername() {
        return login;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return getExpirationDate() == null 
        		|| getExpirationDate().before(DateUtils.today());
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return !isLockedOut();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
    	boolean credentialsNonExpired = true;
        int expiredInDays = 0;
        try {
        	expiredInDays = SecurityConfigUtil.getCredentialExpiredAfterNbDays();
        } catch (Exception e) {
        	// Nothing to do here
        }

        if (getLastPasswordChangeDate() != null && expiredInDays > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getLastPasswordChangeDate());
            calendar.add(Calendar.DAY_OF_YEAR, expiredInDays);
            credentialsNonExpired = calendar.getTime().after(new Date());
        }
        return credentialsNonExpired;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    @Transient
    public boolean isEnabled() {
        return StatusRecord.ACTIV.equals(getStatusRecord());
    }

    /**_______________________________________________________________________________________
     * 
     * END BLOCK [SPRING SECURITY]
     * _______________________________________________________________________________________
     */

    /**_______________________________________________________________________________________
     * 
     * START BLOCK [AUDIT]
     * _______________________________________________________________________________________
     */
    
    /**
     * @return the auditSecUser
     */
    @Transient
    public SecUser getAuditSecUser() {
        return auditSecUser;
    }

    /**
     * @param auditSecUser the auditSecUser to set
     */
    public void setAuditSecUser(final SecUser auditSecUser) {
        this.auditSecUser = auditSecUser;
    }

    /**
     * @see com.nokor.frmk.model.entity.audit.audit.AuditEntityA#getEntityOrigin()
     */
    @Override
    @Transient
    public AuditEntityA getEntityOrigin() {
        return this.auditSecUser;
    }

    /**
     * @see com.nokor.frmk.model.entity.audit.audit.AuditEntityA#setEntityOrigin(com.nokor.frmk.model.entity.audit.audit.AuditEntityA)
     */
    @Override
    public void setEntityOrigin(final AuditEntityA entityOrigin) {
        this.auditSecUser = (SecUser) entityOrigin;
    }
    
    /**_______________________________________________________________________________________
     * 
     * END BLOCK [AUDIT]
     * _______________________________________________________________________________________
     */

	    
    @Override
    public String toString() {
    	return login;
    }
	
}
