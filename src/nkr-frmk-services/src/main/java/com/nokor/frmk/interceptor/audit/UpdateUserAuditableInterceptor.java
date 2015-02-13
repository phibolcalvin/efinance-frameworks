/**
 * Created on 13/02/2012
 */
package com.nokor.frmk.interceptor.audit;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.seuksa.frmk.model.entity.audit.UpdateUserAuditable;

import com.nokor.frmk.security.context.SecApplicationContext;

/**
 * UpdateUserAuditableInterceptor
 * @author kong.phirun
 *
 */
public class UpdateUserAuditableInterceptor extends EmptyInterceptor implements TypeInterceptor {

    /**
     * 
     */
    private static final long serialVersionUID = 98937386905286908L;
    private static final Log log = LogFactory.getLog(UpdateUserAuditableInterceptor.class);

    public UpdateUserAuditableInterceptor() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public final boolean supports(final Object object) {
        return object instanceof UpdateUserAuditable;
    }

    private boolean setUpdateUser(final String[] propertyNames, final Object[] currentState) {
        boolean modified = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (UpdateUserAuditable.UPDATE_USER_PROPERTY.equals(propertyNames[i])) {
            	if (SecApplicationContext.isAuthenticated()) {
                    currentState[i] = SecApplicationContext.getSecUser().getLogin();
                } else {
                    // TODO Remove
                    currentState[i] = "migration";
                }
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
        return setUpdateUser(propertyNames, currentState);
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
        return setUpdateUser(propertyNames, currentState);
    }

}
