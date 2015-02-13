package com.nokor.frmk.model.entity.audit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.audit.CreateDateAuditable;
import org.seuksa.frmk.model.entity.audit.CreateUserAuditable;
import org.seuksa.frmk.model.entity.audit.UpdateDateAuditable;
import org.seuksa.frmk.model.entity.audit.UpdateUserAuditable;

import com.nokor.frmk.service.AuditModifiedProperty;

/**
 * @author prasnar
 * @version $Revision$
 */
@MappedSuperclass
public abstract class AuditEntityA extends EntityA implements CreateDateAuditable, CreateUserAuditable, UpdateDateAuditable, UpdateUserAuditable {

    private static final long serialVersionUID = -2339669280553770898L;
    
    private boolean auditAtCreation = false;
    private boolean auditAtUpdate = true;
    private boolean auditAtRemove = true;
    private boolean auditAtLoad = false;

    private AuditableEvent event;
    private List<AuditModifiedProperty> modifiedProperties;

    /**
     * @return the auditAtCreation
     */
    @Transient
    public boolean isAuditAtCreation() {
        return auditAtCreation;
    }

    /**
     * @param auditAtCreation the auditAtCreation to set
     */
    public void setAuditAtCreation(final boolean auditAtCreation) {
        this.auditAtCreation = auditAtCreation;
    }

    /**
     * @return the auditAtUpdate
     */
    @Transient
    public boolean isAuditAtUpdate() {
        return auditAtUpdate;
    }

    /**
     * @param auditAtUpdate the auditAtUpdate to set
     */
    public void setAuditAtUpdate(final boolean auditAtUpdate) {
        this.auditAtUpdate = auditAtUpdate;
    }

    /**
     * @return the auditAtRemove
     */
    @Transient
    public boolean isAuditAtRemove() {
        return auditAtRemove;
    }

    /**
     * @param auditAtRemove the auditAtRemove to set
     */
    public void setAuditAtRemove(final boolean auditAtRemove) {
        this.auditAtRemove = auditAtRemove;
    }

    /**
     * @return the auditAtLoad
     */
    @Transient
    public boolean isAuditAtLoad() {
        return auditAtLoad;
    }

    /**
     * @param auditAtLoad the auditAtLoad to set
     */
    public void setAuditAtLoad(final boolean auditAtLoad) {
        this.auditAtLoad = auditAtLoad;
    }

    /**
     * @return the modifiedProperties
     */
    @Transient
    public List<AuditModifiedProperty> getModifiedProperties() {
        return modifiedProperties;
    }

    /**
     * @param modifiedProperties the modifiedProperties to set
     */
    public void setModifiedProperties(final List<AuditModifiedProperty> modifiedProperties) {
        this.modifiedProperties = modifiedProperties;
    }

    @Transient
    public List<String> getAuditableProperties() {
        final List<String> auditableProperties = new ArrayList<String>();
        final InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("audit-properties.properties");
        final Properties properties = new Properties();
        try {
            properties.load(inStream);
            final String values = properties.getProperty(this.getClass().getName());
            if (values != null && !values.equals("")) {
                final String[] fields = values.split(",");
                for (final String field : fields) {
                    auditableProperties.add(field.trim());
                }
            }
        }
        catch (final IOException e) {
        }
        return auditableProperties;
    }

    @Transient
    public String getPrimaryField() {
        return "id";
    }

    /**
     * @return the entityOrigin
     */
    @Transient
    public AuditEntityA getEntityOrigin() {
        return null;
    }

    public void setEntityOrigin(final AuditEntityA entityOrigin) {
        // do nothing
    }

    /**
     * @return the event
     */
    @Transient
    public AuditableEvent getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(final AuditableEvent event) {
        this.event = event;
    }

    public enum AuditableEvent {
        CREATE,
        LOAD,
        UPDATE,
        REMOVE
    }
}
