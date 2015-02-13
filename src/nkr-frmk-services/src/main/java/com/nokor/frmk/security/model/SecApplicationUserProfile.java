package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "tu_sec_application_user_profile")
public class SecApplicationUserProfile extends EntityA {
	/** */
	private static final long serialVersionUID = -4749387019266383918L;
	
	private SecApplication secApplication;
	private SecUser secUser;
	private SecProfile secProfile;
	
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_aup_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    
    /**
	 * @return the secProfile
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sec_pro_id", nullable = false)
	public SecProfile getSecProfile() {
		return secProfile;
	}

	/**
	 * @param secProfile the secProfile to set
	 */
	public void setSecProfile(SecProfile secProfile) {
		this.secProfile = secProfile;
	}

	/**
	 * @return the secApplication
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sec_app_id", nullable = false)
	public SecApplication getSecApplication() {
		return secApplication;
	}

	/**
	 * @param secApplication the secApplication to set
	 */
	public void setSecApplication(SecApplication secApplication) {
		this.secApplication = secApplication;
	}

	/**
	 * @return the secUser
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sec_usr_id", nullable = false)
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	
}
