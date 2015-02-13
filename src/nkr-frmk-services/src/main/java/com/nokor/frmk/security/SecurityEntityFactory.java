package com.nokor.frmk.security;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.spring.SecSaltGenerator;


/**
 * Factory class to create Entity instances.
 *
 * @author prasnar
 */
public class SecurityEntityFactory {
	
	private static final Log LOGGER = Log.getInstance(SecurityEntityFactory.class);

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
	 * @param createdBy
	 * @return
	 */
	public static <T extends Entity> T createInstance(Class<T> entityClass, String createdBy) {
		return EntityFactory.createInstance(entityClass, createdBy);
	}
	

	/**
	 * 
	 * @return
	 */
	public static SecUser createSecUserInstance() {
		return createSecUserInstance(null);
	}
	
	/**
	 * 
	 * @param entityClass
	 * @param createdBy
	 * @return
	 */
	public static SecUser createSecUserInstance(String createdBy) {
		SecUser secUser = EntityFactory.createInstance(SecUser.class, createdBy);
		secUser.setPasswordSalt(SecSaltGenerator.generateSecureRandom());
		return secUser;
	}
   

}
