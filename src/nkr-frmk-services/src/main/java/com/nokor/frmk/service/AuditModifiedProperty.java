package com.nokor.frmk.service;

import java.io.Serializable;

/**
 * 
 * @author ly.youhort
 *
 */
public class AuditModifiedProperty implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -3729564610909298966L;
	
	private String name;
	private Object valueBefore;
	private Object valueAfter;
	private String reason;
	private String actionType;
	private String ipAddress;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the valueBefore
	 */
	public Object getValueBefore() {
		return valueBefore;
	}
	/**
	 * @param valueBefore the valueBefore to set
	 */
	public void setValueBefore(Object valueBefore) {
		this.valueBefore = valueBefore;
	}
	/**
	 * @return the valueAfter
	 */
	public Object getValueAfter() {
		return valueAfter;
	}
	/**
	 * @param valueAfter the valueAfter to set
	 */
	public void setValueAfter(Object valueAfter) {
		this.valueAfter = valueAfter;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
 
}
