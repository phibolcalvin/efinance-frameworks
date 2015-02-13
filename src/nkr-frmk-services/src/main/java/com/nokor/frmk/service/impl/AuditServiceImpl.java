package com.nokor.frmk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seuksa.frmk.tools.BeanUtils;

import com.nokor.frmk.model.entity.audit.AuditEntityA;
import com.nokor.frmk.service.AuditModifiedProperty;
import com.nokor.frmk.service.AuditService;

/**
 * @author ly.youhort
 */
public class AuditServiceImpl implements AuditService {

    private static final Log log = LogFactory.getLog(AuditServiceImpl.class);

    public void addEntryOnLoad(final AuditEntityA entity, final AuditModifiedProperty property) {
        final String msg = "'[" + entity.getClass().getName() + "][" + entity.getId() + "] - property [" + property.getName() + "]:" + (property.getValueAfter() != null ? property.getValueAfter() : "<null>") + "].";
        log.debug("[AddEntryOnLoad] " + msg);
    }

    public void addEntryOnCreate(final AuditEntityA entity, final AuditModifiedProperty property) {
        final String msg = "'[" + entity.getClass().getName() + "][" + entity.getId() + "] - property [" + property.getName() + "]:" + (property.getValueAfter() != null ? property.getValueAfter() : "<null>") + "].";
        log.debug("[AddEntryOnCreate] " + msg);
    }

    public void addEntryOnUpdate(final AuditEntityA newEntity, final AuditEntityA oldEntity, final AuditModifiedProperty property) {
        final String msg = "'[" + newEntity.getClass().getName() + "][" + newEntity.getId() + "] - property [" + property.getName() + "] from [" + (property.getValueBefore() != null ? property.getValueBefore() : "<null>") + "] to [" + (property.getValueAfter() != null ? property.getValueAfter() : "<null>") + "].";
        log.debug("[AddEntryOnUpdate] " + msg);
    }

    public void addEntryOnDelete(final AuditEntityA entity, final AuditModifiedProperty property) {
        final String msg = "'[" + entity.getClass().getName() + "][" + entity.getId() + "] - property [" + property + "]:" + (property.getValueAfter() != null ? property.getValueAfter() : "<null>") + "].";
        log.debug("[AddEntryOnDelete] " + msg);
    }

    public List<AuditModifiedProperty> getModifiedProperties(final AuditEntityA newEntity) {
        final List<String> auditFields = newEntity.getAuditableProperties();

        if (auditFields == null || auditFields.size() == 0) {
            log.warn("GetModifiedProperties - has no auditable property.");
            return null;
        }

        final List<AuditModifiedProperty> lstModified = new ArrayList<AuditModifiedProperty>();
        AuditModifiedProperty modifiedProp = null;

        final AuditEntityA oldEntity = newEntity.getEntityOrigin();
        for (final String property : auditFields) {
            Object newValue = null;
            Object oldValue = null;
            try {
                newValue = BeanUtils.getProperty(newEntity, property);
                oldValue = BeanUtils.getProperty(oldEntity, property);
            }
            catch (final Exception e) {
                log.error("[GetModifiedProperties]", e);
            }
            /*if (oldEntity is ISecuredEntity
                && ((ISecuredEntity)oldEntity).GetEncryptedProperties().Contains(property))
            {
                oldValue = SecurityUtils.UnencodePassword((String) oldValue);
            }*/
            final boolean hasChanged = (oldValue == null && newValue != null) || (oldValue != null && newValue == null) || (oldValue != null && !oldValue.equals(newValue));

            if (hasChanged) {
                modifiedProp = new AuditModifiedProperty();
                modifiedProp.setName(property);
                modifiedProp.setValueBefore(oldValue);
                modifiedProp.setValueAfter(newValue);
                lstModified.add(modifiedProp);
            }
        }

        return lstModified;
    }
}
