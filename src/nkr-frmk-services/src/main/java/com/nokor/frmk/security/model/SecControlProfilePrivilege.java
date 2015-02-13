package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tu_sec_control_profile_privilege")
public class SecControlProfilePrivilege extends EntityA {
	/** */
	private static final long serialVersionUID = 5135486814057630320L;

	private SecControl secControl;
	private SecProfile secProfile;
	private SecPrivilege secPrivilege;
	
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_ppp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    
    /**
	 * @return the secProfile
	 */
    @ManyToOne
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
	 * @return the secControl
	 */
    @ManyToOne
    @JoinColumn(name="sec_ctl_id", nullable = false)
	public SecControl getSecControl() {
		return secControl;
	}

	/**
	 * @param secControl the secControl to set
	 */
	public void setSecControl(SecControl secControl) {
		this.secControl = secControl;
	}

	/**
	 * @return the secPrivilege
	 */
    @ManyToOne
    @JoinColumn(name="sec_pri_id", nullable = false)
	public SecPrivilege getSecPrivilege() {
		return secPrivilege;
	}

	/**
	 * @param secPrivilege the secPrivilege to set
	 */
	public void setSecPrivilege(SecPrivilege secPrivilege) {
		this.secPrivilege = secPrivilege;
	}

	
}
