package com.nokor.frmk.security.model;

import java.util.ArrayList;
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
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "ts_sec_application")
public class SecApplication extends EntityRefA {
	/** */
	private static final long serialVersionUID = 7861116000048153225L;

	public static final String DEFAULT_APP = "MAIN";
	
	private List<SecUser> users;
	private List<SecProfile> profiles;

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_app_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_app_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_app_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "sec_app_desc_en", nullable = false)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * 
	 * @return
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_sec_application_profile",
				joinColumns = { @JoinColumn(name = "sec_app_id") }, 
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
	 * @param profiles
	 */
	public void setProfiles(List<SecProfile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * 
	 * @return
	 */
	@ManyToMany(mappedBy = "applications")
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

}
