package com.nokor.frmk.testing;

import java.util.List;

import org.seuksa.frmk.model.EntityFactory;
import org.springframework.util.Assert;

import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.SecurityService;

/**
 * 
 * @author prasnar
 *
 */
public class BaseSecurityTestCase extends BaseTestCase {

	/**
	 * 
	 */
	public BaseSecurityTestCase() {
		super();
	}
	
	/**
	 * 
	 */
	public void buildDefaultSecurity() {
		buildSecurityWithApp(SecApplication.DEFAULT_APP);
	}
	
	/**
	 * Build the default records for secAppCode = appCode
	 * . ts_sec_application
	 * . tu_sec_user (ADMIN)
	 * . ts_sec_profile (ADMIN)
	 * . tu_sec_application_user
	 * . tu_sec_application_profile
	 * . tu_sec_user_profile
	 */
	public void buildSecurityWithApp(String appCode) {
		SecurityService secSrv = getBean(SecurityService.class);
		
		SecApplication defaultApp = secSrv.getApplication(appCode);
		if (defaultApp == null) {
			defaultApp = EntityFactory.createInstance(SecApplication.class);
			defaultApp.setCode(appCode);
			defaultApp.setDesc(appCode);
			defaultApp.setDescEn(defaultApp.getDesc());
			secSrv.saveOrUpdate(defaultApp);
		} 
		Assert.notNull(defaultApp, "Could not create the application [" + appCode + "]");
		logger.info("Application [" + defaultApp.getCode() + "] created successfully.");
		
		
		List<SecProfile> secProLst = secSrv.list(SecProfile.class);
		SecProfile defaultPro = null;
		if (secProLst == null || secProLst.size() == 0) {
			defaultPro = EntityFactory.createInstance(SecProfile.class);
			defaultPro.setCode(SecProfile.DEFAULT_ADMIN_PROFILE);
			defaultPro.setDesc(SecProfile.DEFAULT_ADMIN_PROFILE);
			defaultPro.setDescEn(defaultPro.getDesc());
			secSrv.saveOrUpdate(defaultPro);
		} else {
			defaultPro = secProLst.get(0);
		}
		Assert.notNull(defaultPro, "Could not create a default profile.");
		logger.info("Default profile [" + defaultPro.getCode() + "].");
		
		//List<SecUser> secUsrLst = secSrv.list(SecUser.class);
		SecUser defaultUsr = secSrv.loadUserByUsername(SecurityConfigUtil.DEFAULT_ADMIN);
		if (defaultUsr == null) {
			defaultUsr = SecurityEntityFactory.createSecUserInstance("adminCreator");
			defaultUsr.setLogin(SecurityConfigUtil.DEFAULT_ADMIN);
			defaultUsr.setDesc(SecurityConfigUtil.DEFAULT_ADMIN);
			defaultUsr.setDefaultProfile(defaultPro);
			String rawPassword = defaultUsr.getLogin(); 
			secSrv.create(defaultUsr, rawPassword);
			
			defaultUsr.setPasswordSaltSelfProtection();
			secSrv.changePassword(defaultUsr, rawPassword);
		}
		Assert.notNull(defaultUsr, "Could not create a default user.");
		logger.info("Default user [" + defaultUsr.getLogin() + "].");
	
		if (!defaultUsr.getProfiles().contains(defaultUsr)) {
			//defaultUsr.getProfiles().add(defaultPro);
			secSrv.addProfileToUser(defaultUsr, defaultPro);
		} else {
			defaultPro = defaultUsr.getDefaultProfile();
			Assert.isTrue(!SecProfile.DEFAULT_ADMIN_PROFILE.equals(defaultPro), "The user has already profile assigned, but is not [" + SecProfile.DEFAULT_ADMIN_PROFILE + "]" );
		}
		Assert.notNull(defaultUsr.getProfiles().size(), "Could not add a default profile to the user [" + defaultUsr.getLogin() + "]");

		
		if (!defaultUsr.getApplications().contains(defaultApp)) {
//			defaultUsr.getApplications().add(defaultApp);
			secSrv.addApplicationToUser(defaultUsr, defaultApp);
		}
		
		if (!defaultApp.getProfiles().contains(defaultPro)) {
			secSrv.addProfileToApplication(defaultApp, defaultPro);
		}
		
		if (!secSrv.checkUserInApplicationUserProfile(defaultUsr, defaultApp, defaultPro)) {
			secSrv.addApplicationUserProfile(defaultApp, defaultUsr, defaultPro);
		}
		
		logger.info("The default security configuration has been built successfully:");
		logger.info(". Default application [" + defaultApp.getCode() + "].");
		logger.info(". Default profile [" + defaultPro.getCode() + "].");
		logger.info(". Default user [" + defaultUsr.getLogin() + "] having the default SecProfile.");
	}
	
	
	

}
