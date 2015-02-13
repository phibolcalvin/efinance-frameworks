package org.seuksa.frmk.model.entity;

import java.util.List;

import org.seuksa.frmk.model.sysref.StatusRecord;


/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface StatusRecordListAware {
	List<StatusRecord> getStatusRecordList();
	void setStatusRecord(List<StatusRecord> statusRecordList);
}
