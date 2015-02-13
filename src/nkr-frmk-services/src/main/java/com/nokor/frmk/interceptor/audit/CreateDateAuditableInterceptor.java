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
import org.seuksa.frmk.model.entity.audit.CreateDateAuditable;

/**
 * CreateDateAuditableInterceptor
 * @author kong.phirun
 *
 */
public class CreateDateAuditableInterceptor extends EmptyInterceptor implements TypeInterceptor {

    /**
     * 
     */
    private static final long serialVersionUID = 3927847163416605067L;
    private static final Log log = LogFactory.getLog(CreateDateAuditableInterceptor.class);

    public CreateDateAuditableInterceptor() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public final boolean supports(final Object object) {
        return object instanceof CreateDateAuditable;
    }

    private boolean setCreationDate(final String[] propertyNames, final Object[] currentState) {
        boolean modified = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (CreateDateAuditable.CREATE_DATE_PROPERTY.equals(propertyNames[i])) {
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
    public final boolean onSave(final Object entity, final Serializable id, final Object[] currentState, final String[] propertyNames, final Type[] types) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onSave : ").append(entity);
            log.trace(message);
        }
        return setCreationDate(propertyNames, currentState);
    }

}
