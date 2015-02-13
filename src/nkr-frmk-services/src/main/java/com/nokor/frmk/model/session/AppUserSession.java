package com.nokor.frmk.model.session;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;




/**
 * 
 * @author prasnar
 */
@Entity
@Table(name = "td_app_user_session")
public class AppUserSession extends EntityA {
	/** */
	private static final long serialVersionUID = -19051667607291885L;

	private String key;
	private Long usrId;
	private Long appId;
	private Date startDate;
	private Date endDate;
	private String IPaddress;
    

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usr_ses_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }


	/**
	 * @return the key
	 */
    @Column(name = "usr_ses_key", nullable = false)
	public String getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}


	/**
	 * @return the usrId
	 */
    @Column(name = "sec_usr_id", nullable = false)
	public Long getUsrId() {
		return usrId;
	}


	/**
	 * @param usrId the usrId to set
	 */
	public void setUsrId(Long usrId) {
		this.usrId = usrId;
	}


	/**
	 * @return the appId
	 */
    @Column(name = "sec_app_id", nullable = false)
	public Long getAppId() {
		return appId;
	}


	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}


	/**
	 * @return the startDate
	 */
    @Column(name = "usr_ses_start_date", nullable = false)
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
    @Column(name = "usr_ses_end_date", nullable = false)
	public Date getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the iPaddress
	 */
    @Column(name = "usr_ses_addr_ip", nullable = false)
	public String getIPaddress() {
		return IPaddress;
	}


	/**
	 * @param iPaddress the iPaddress to set
	 */
	public void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
	}
    
    


   
}
