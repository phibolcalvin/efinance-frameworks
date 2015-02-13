package com.nokor.frmk.service;

import java.util.List;

import com.nokor.frmk.model.entity.audit.AuditEntityA;

/**
 * 
 * @author ly.youhort
 *
 */
public interface AuditService {

	void addEntryOnLoad(AuditEntityA entity, AuditModifiedProperty property);
    void addEntryOnCreate(AuditEntityA entity, AuditModifiedProperty property);
    void addEntryOnUpdate(AuditEntityA newEntity, AuditEntityA oldEntity, AuditModifiedProperty property);
    void addEntryOnDelete(AuditEntityA entity, AuditModifiedProperty property);

    List<AuditModifiedProperty> getModifiedProperties(AuditEntityA newEntity);
}
