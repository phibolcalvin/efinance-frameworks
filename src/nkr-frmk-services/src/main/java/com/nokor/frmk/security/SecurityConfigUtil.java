package com.nokor.frmk.security;

import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.helper.AppConfigFileHelper;
import com.nokor.frmk.helper.ServicesCoreHelper;
import com.nokor.frmk.helper.SettingConfigHelper;
import com.nokor.frmk.security.model.SecApplication;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class SecurityConfigUtil implements ServicesCoreHelper {
	private static final Log logger = Log.getInstance(SecurityConfigUtil.class);

  
    /** ---------------------------------------------------------
     *  SECURITY
     ** ---------------------------------------------------------*/
	public static final String DEFAULT_ADMIN = "admin";
	public static final Long DEFAULT_ADMIN_ID = 1L;


    /**
     * Constructor
     * Can not be instantiated
     */
	private SecurityConfigUtil() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecApplication getSecApplication() {
		return AppConfigFileHelper.getSecApplication();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUser() {
		return AppConfigFileHelper.checkSecurityTableApplicationUser();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableUserProfile() {
		return AppConfigFileHelper.checkSecurityTableUserProfile();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationProfile() {
		return AppConfigFileHelper.checkSecurityTableApplicationProfile();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUserProfile() {
		return AppConfigFileHelper.checkSecurityTableApplicationUserProfile();
	}
	 
	/**
     * 
     * @return
     */
    public static int getCredentialExpiredAfterNbDays() {
    	return SettingConfigHelper.getCredentialExpiredAfterNbDays();
    }
	
   
}
