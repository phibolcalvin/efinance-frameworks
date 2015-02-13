/**
 * Created on 13/02/2012
 */
package com.nokor.frmk.interceptor.audit;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.seuksa.frmk.model.entity.audit.UpdateDateAuditable;

/**
 * UpdateDateAuditableInterceptor
 * @author kong.phirun
 *
 */
public class UpdateDateAuditableInterceptor extends EmptyInterceptor implements TypeInterceptor {

    /**
     * 
     */
    private static final long serialVersionUID = -1850484405853240317L;
    private static final Log log = LogFactory.getLog(UpdateDateAuditableInterceptor.class);

    public UpdateDateAuditableInterceptor() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public final boolean supports(final Object object) {
        return object instanceof UpdateDateAuditable;
    }

    private boolean setModificationDate(final String[] propertyNames, final Object[] currentState) {
        boolean modified = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (UpdateDateAuditable.UPDATE_DATE_PROPERTY.equals(propertyNames[i])) {
                currentState[i] = new Date();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onFlushDirty : ").append(entity);
            log.trace(message);
        }
        return setModificationDate(propertyNames, currentState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean onSave(final Object entity, final Serializable id, final Object[] currentState, final String[] propertyNames, final Type[] types) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onSave : ").append(entity);
            log.trace(message);
        }
        return setModificationDate(propertyNames, currentState);
    }

}
