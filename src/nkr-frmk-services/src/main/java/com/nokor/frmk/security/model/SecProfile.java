package com.nokor.frmk.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "ts_sec_profile")
public class SecProfile extends EntityRefA {
	/** */
	private static final long serialVersionUID = 2599890364585224153L;

	public static final String DEFAULT_ADMIN_PROFILE = "ADMIN";
	public static final Long DEFAULT_ADMIN_PROFILE_ID = 1L;
	
	private List<SecUser> users; 
	private List<SecControlProfilePrivilege> controlProfilePrivileges;
	private List<SecApplication> applications; 

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_pro_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_pro_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_pro_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "sec_pro_desc_en", nullable = false)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * 
	 * @return
	 */
	@ManyToMany(mappedBy = "profiles")
	public List<SecUser> getUsers() {
		if (users == null) {
			users = new ArrayList<SecUser>();
		}
		return users;
	}

	/**
	 * 
	 * @param users
	 */
	public void setUsers(List<SecUser> users) {
		this.users = users;
	}

	/**
	 * @return the applications
	 */
	@ManyToMany(mappedBy = "profiles")
	public List<SecApplication> getApplications() {
		if (applications == null) {
			applications = new ArrayList<SecApplication>();
		}
		return applications;
	}

	/**
	 * @param applications the applications to set
	 */
	public void setApplications(List<SecApplication> applications) {
		this.applications = applications;
	}

	/**
	 * @return the controlProfilePrivileges
	 */
	@OneToMany(mappedBy = "secProfile", fetch = FetchType.LAZY)
	public List<SecControlProfilePrivilege> getControlProfilePrivileges() {
		return controlProfilePrivileges;
	}

	/**
	 * @param controlProfilePrivileges the controlProfilePrivileges to set
	 */
	public void setControlProfilePrivileges(List<SecControlProfilePrivilege> controlProfilePrivileges) {
		this.controlProfilePrivileges = controlProfilePrivileges;
	}

	
	
}
