package org.seuksa.frmk.model;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Factory class to create Entity instances.
 *
 * @author prasnar
 */
public class EntityFactory {
	
	private static final Log LOGGER = Log.getInstance(EntityFactory.class);

	
	/**
	 * 
	 * @param entityClass
	 * @return
	 */
	public static <T extends Entity> T createInstance(Class<T> entityClass) {
		return createInstance(entityClass, null);
	}
    /**
     * 
     * @param entityClass
     * @param username
     * @return
     */
    public static <T extends Entity> T createInstance(Class<T> entityClass, String username) {
        try {
            T entity = entityClass.newInstance();
            if (entity instanceof EntityA) {
            	String createdBy; 
            	if (!StringUtils.isEmpty(username)) {
            		createdBy = username;
            	} else if (SpringUtils.isAuthenticated()) {
            		createdBy = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            	} else {
            		createdBy = EntityA.NOT_AUTHENTICATED_USER;
            	}
            	((EntityA) entity).fillSysBlock(createdBy);
            	((EntityA) entity).setStatusRecord(StatusRecord.ACTIV);
            	((EntityA) entity).setCrudAction(CrudAction.CREATE);
            } else if (entity instanceof EntityRefA) {
            	((EntityRefA) entity).setStatusRecord(StatusRecord.ACTIV);
            }

            return entity;
        } catch (Exception e) {
            LOGGER.error("Cannot instantiate class " + entityClass, e);
            throw new RuntimeException("Cannot instantiate class " + entityClass, e);
        }
    }

}
