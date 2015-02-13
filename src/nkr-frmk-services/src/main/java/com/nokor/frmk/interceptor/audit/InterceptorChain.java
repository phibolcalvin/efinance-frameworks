/**
 * Created on 13/02/2012
 */
package com.nokor.frmk.interceptor.audit;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.type.Type;

/**
 * InterceptorChain
 * @author kong.phirun
 *
 */
public class InterceptorChain extends EmptyInterceptor {

    /**
     * 
     */
    private static final long serialVersionUID = 3911046483628479451L;

    private List<? extends TypeInterceptor> interceptors;

    private static final Log log = LogFactory.getLog(InterceptorChain.class);

    public InterceptorChain() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDelete(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onDelete : ").append(entity).append(" - Begin");
            log.trace(message);
        }
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                interceptor.onDelete(entity, id, state, propertyNames, types);
            }
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onDelete : ").append(entity).append(" - End");
            log.trace(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onFlushDirty : ").append(entity).append(" - Begin");
            log.trace(message);
        }
        boolean modified = false;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                modified |= interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
            }
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onFlushDirty : ").append(entity).append(" - End");
            log.trace(message);
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onLoad(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onLoad : ").append(entity).append(" - Begin");
            log.trace(message);
        }
        boolean modified = false;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                modified |= interceptor.onLoad(entity, id, state, propertyNames, types);
            }
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onLoad : ").append(entity).append(" - End");
            log.trace(message);
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean onSave(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onSave : ").append(entity).append(" - Begin");
            log.trace(message);
        }
        boolean modified = false;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                modified |= interceptor.onSave(entity, id, state, propertyNames, types);
            }
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onSave : ").append(entity).append(" - End");
            log.trace(message);
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] findDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("findDirty : ").append(entity).append(" - Begin");
            log.trace(message);
        }
        int[] indices = null;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(entity)) {
                indices = interceptor.findDirty(entity, id, currentState, previousState, propertyNames, types);
                if (indices != null) {
                    break;
                }
            }
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("findDirty : ").append(entity).append(" - End");
            log.trace(message);
        }
        return indices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getEntity(final String entityName, final Serializable id) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("getEntity : ").append(entityName).append(" - Begin");
            log.trace(message);
        }
        final Object result = super.getEntity(entityName, id);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("getEntity : ").append(entityName).append(" - End");
            log.trace(message);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntityName(final Object object) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("getEntityName : ").append(object).append(" - Begin");
            log.trace(message);
        }
        final String result = super.getEntityName(object);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("getEntityName : ").append(object).append(" - End");
            log.trace(message);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object instantiate(final String entityName, final EntityMode entityMode, final Serializable id) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("instantiate : ").append(entityName).append(" - Begin");
            log.trace(message);
        }
        final Object result = super.instantiate(entityName, entityMode, id);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("instantiate : ").append(entityName).append(" - End");
            log.trace(message);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient(final Object entity) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("isTransient : ").append(entity).append(" - Begin");
            log.trace(message);
        }
        final Boolean result = super.isTransient(entity);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("isTransient : ").append(entity).append(" - End");
            log.trace(message);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionRecreate(final Object collection, final Serializable key) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onCollectionRecreate : ").append(collection).append(" - Begin");
            log.trace(message);
        }
        super.onCollectionRecreate(collection, key);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onCollectionRecreate : ").append(collection).append(" - End");
            log.trace(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionRemove(final Object collection, final Serializable key) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onCollectionRemove : ").append(collection).append(" - Begin");
            log.trace(message);
        }
        super.onCollectionRemove(collection, key);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onCollectionRemove : ").append(collection).append(" - End");
            log.trace(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionUpdate(final Object collection, final Serializable key) throws CallbackException {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onCollectionUpdate : ").append(collection).append(" - Begin");
            log.trace(message);
        }
        final PersistentCollection persistentCollection = (PersistentCollection) collection;
        for (final TypeInterceptor interceptor : interceptors) {
            if (interceptor.supports(persistentCollection.getOwner())) {
                interceptor.onCollectionUpdate(collection, key);
            }
        }
        super.onCollectionUpdate(collection, key);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onCollectionUpdate : ").append(collection).append(" - End");
            log.trace(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String onPrepareStatement(final String sql) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onPrepareStatement : ").append(sql).append(" - Begin");
            log.trace(message);
        }
        final String result = super.onPrepareStatement(sql);
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("onPrepareStatement : ").append(sql).append(" - End");
            log.trace(message);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postFlush(final Iterator entities) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("postFlush : ").append(entities).append(" - Begin");
            log.trace(message);
        }
        for (final TypeInterceptor interceptor : interceptors) {
        	interceptor.postFlush(entities);
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("postFlush : ").append(entities).append(" - End");
            log.trace(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preFlush(final Iterator entities) {
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("preFlush : ").append(entities).append(" - Begin");
            log.trace(message);
        }
        for (final TypeInterceptor interceptor : interceptors) {
        	interceptor.postFlush(entities);
        }
        if (log.isTraceEnabled()) {
            final StringBuilder message = new StringBuilder();
            message.append("preFlush : ").append(entities).append(" - End");
            log.trace(message);
        }
    }

    public final void setInterceptors(final List<? extends TypeInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

}
