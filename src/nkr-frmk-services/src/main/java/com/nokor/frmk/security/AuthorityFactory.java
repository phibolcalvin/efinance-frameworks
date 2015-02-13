package com.nokor.frmk.security;

import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.service.SecurityService;


/**
 * Same as org.springframework.security.core.authority.SimpleGrantedAuthority
 * 
 * @author prasnar
 */
public class AuthorityFactory {

    private final static Log LOGGER = Log.getLog(AuthorityFactory.class);
    
//    public static final String ADMINISTRATOR_ROLE = SecProfile.DEFAULT_ADMIN_PROFILE.startsWith(SPRING_SEC_PREF_ROLE) 
//    														? SecProfile.DEFAULT_ADMIN_PROFILE : SPRING_SEC_PREF_ROLE + SecProfile.DEFAULT_ADMIN_PROFILE;
//    public static final String AUTHENTICATED_USER = SPRING_SEC_PREF_ROLE + "AuthUser";

   
    @Autowired
    private SecurityService securitySrv = null;
    
    private static AuthorityFactory instance;
    
    /**
     * 
     * @return
     */
    public static AuthorityFactory getInstance() {
    	if (instance == null) {
    		instance = new AuthorityFactory();
    	}
    	return instance;
    }
    
//    /**
//     * 
//     * @return
//     */
//	public static SecProfileGrantedAuthority AUTHORITY_AUTHENTICATED_USER() {
//		return getInstance().grantAuthority(AUTHENTICATED_USER);
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public static SecProfileGrantedAuthority AUTHORITY_SUPER_ADMIN() {
//		return getInstance().grantAuthority(ADMINISTRATOR_ROLE);
//	}

    /**
     * 
     * @param profile
     */
    public SecProfileGrantedAuthority grantAuthority(SecProfile profile) {
    	SecProfileGrantedAuthority auth = new SecProfileGrantedAuthority();
        Assert.notNull(profile, "A granted authority (SecProfile) is required");
        auth.setProfile(profile);
        
        return auth;
    }
    
	/**
	 * 
	 * @param secProCode
	 */
    private SecProfileGrantedAuthority grantAuthority(String secProCode) {
    	SecProfileGrantedAuthority auth = new SecProfileGrantedAuthority();
    	SecProfile profile = null;
		try {
			profile = securitySrv.findProfileByCode(secProCode);
		} catch (DaoException e) {
			LOGGER.error(e);
		}
		if (profile == null) {
			LOGGER.warn("The profile [" + secProCode + "] cannot be found.");
		}
        auth.setProfile(profile);
        
        return auth;
    }

}
