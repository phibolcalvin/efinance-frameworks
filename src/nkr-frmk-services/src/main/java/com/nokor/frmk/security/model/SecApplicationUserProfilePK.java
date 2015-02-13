package com.nokor.frmk.security.model;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public class SecApplicationUserProfilePK implements Serializable {
	/** */
	private static final long serialVersionUID = 8299834025200128248L;

	@ManyToOne
    @JoinColumn(name = "sec_app_id", referencedColumnName = "sec_app_id")
	private SecApplication secApplication;
	@ManyToOne
    @JoinColumn(name = "sec_usr_id", referencedColumnName = "sec_usr_id")
	private SecUser secUser;
	@ManyToOne
    @JoinColumn(name = "sec_pro_id", referencedColumnName = "sec_pro_id")
	private SecProfile secProfile;
	
	
	/**
	 * @return the secApplication
	 */
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
	public SecUser getSecUser() {
		return secUser;
	}
	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	/**
	 * @return the secProfile
	 */
	public SecProfile getSecProfile() {
		return secProfile;
	}
	/**
	 * @param secProfile the secProfile to set
	 */
	public void setSecProfile(SecProfile secProfile) {
		this.secProfile = secProfile;
	}
	
	
	
	
}
