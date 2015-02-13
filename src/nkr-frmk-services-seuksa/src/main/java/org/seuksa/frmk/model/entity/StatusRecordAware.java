package org.seuksa.frmk.model.entity;

import org.seuksa.frmk.model.sysref.StatusRecord;


/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface StatusRecordAware {
	StatusRecord getStatusRecord();
	void setStatusRecord(StatusRecord statusRecord);
}
