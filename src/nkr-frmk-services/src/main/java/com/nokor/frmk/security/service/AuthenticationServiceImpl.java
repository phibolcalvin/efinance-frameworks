package com.nokor.frmk.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Provider;

import org.seuksa.frmk.tools.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.frmk.helper.SettingConfigHelper;
import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.service.SettingService;



/**
 * 
 * @author prasnar
 *
 */
@Service("authenticationService")
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
	private final Log logger = Log.getInstance(this);

	@Autowired
	private SecurityService securityService;
	@Autowired SettingService settingService;
	
	/** 
	 * javax.inject.Provider: make possible Spring beans against circular references
	 */
	@Autowired
	private Provider<AuthenticationManager> authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SecUser secUser = null;
		try {
			secUser = securityService.loadUserByUsername(username);
		} catch (Exception e) {
            throw new UsernameNotFoundException("The user " + username + "] can not be found.", e);
		}
        if (secUser == null) {
            throw new UsernameNotFoundException("The user " + username + "] can not be found.");
        }
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(secUser.getAuthorities());
        if (authorities.size() == 0) {
        	 throw new InsufficientAuthenticationException("NO_ROLE_AVAILABLE");
        }
        if (authorities.size() > 0 ) {
        	boolean anonymousOnly = true;
        	for (GrantedAuthority grantedAuthority : authorities) {
        		logger.debug("- " +  grantedAuthority.getAuthority());
        		anonymousOnly = grantedAuthority.getAuthority().toLowerCase().contains("anonymous");
			}
        	if (anonymousOnly) {
        		throw new InsufficientAuthenticationException("ONLY_ANONYMOUS_ROLE_IS_FOUND");
        	}
        }
        
        SecApplication secApp = SecurityConfigUtil.getSecApplication();
        SecProfile secPro = secUser.getDefaultProfile();
        
        // check security tables
        if (SecurityConfigUtil.checkSecurityTableUserProfile() 
        		&& !securityService.checkUserInUserProfile(secUser, secPro)) {
        	throw new InsufficientAuthenticationException("The user is not found in [UserProfile]");
        }
        if (SecurityConfigUtil.checkSecurityTableApplicationUser()
        		&& !securityService.checkUserInApplicationUser(secUser, secApp)) {
        	throw new InsufficientAuthenticationException("The user is not found in [ApplicationUser]");
        }
        if (SecurityConfigUtil.checkSecurityTableApplicationProfile() 
        		&& !securityService.checkUserInApplicationProfile(secUser, secApp, secPro)) {
        	throw new InsufficientAuthenticationException("The user is not found in [ApplicationProfile]");
        }
        if (SecurityConfigUtil.checkSecurityTableApplicationUserProfile()
        		&& !securityService.checkUserInApplicationUserProfile(secUser, secApp, secPro)) {
        	throw new InsufficientAuthenticationException("The user is not found in [ApplicationUserProfile]");
        }
        
        
        return secUser;
	}
	
	@Override
	public SecUser authenticate(String username, String password) {
        try {

			SecUser secUser = securityService.loadUserByUsername(username);
	        if (secUser != null 
	        		&& secUser.isLockedOut()
	        		&& !secUser.isEnabled()) {
	            throw new LockedException("USER_ACCOUNT_HAS_BEEN_LOCKED");
	        }
	        
	        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.get().authenticate(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                clearFailedLoginAttempts(secUser);
            } else {
                incrementFailedLoginAttempts(username);
            }
        
            Locale locale = null;
        	if (secUser.getLocaleType() != null) {
        		locale = secUser.getLocaleType().getLocale();
        	}
        	if (locale != null) {
        		locale = SettingConfigHelper.getLocale();
        	}
            
            
    		return secUser;
        } catch (AuthenticationException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
            // TO PUT BACK AFTER TEST
        }
       
	}

	/**
	 * 
	 * @param userAccount
	 */
	private void clearFailedLoginAttempts(SecUser secUser) {
        if (secUser.getFailedPasswordAnswerAttemptCount() > 0) {
        	secUser.clearFailedPasswordAnswerAttemptCount();
            securityService.saveOrUpdate(secUser);
        }
    }
	
	/**
	 * 
	 * @param username
	 */
	private void incrementFailedLoginAttempts(String username) {
		final SecUser secUser = securityService.loadUserByUsername(username);
        if (secUser == null) {
            return;
        }
        secUser.incrementFailedPasswordAnswerAttemptCount();

        final Integer maxAttempts = 3; 
                //ApplicationSettings.MAX_FAILED_LOGIN_ATTEMPTS.getValue(),
        if (secUser.getFailedPasswordAnswerAttemptCount() > maxAttempts) {
            secUser.setLockedOut(true);
        }
        securityService.saveOrUpdate(secUser);
	}
    

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) applicationEvent;
            logger.info("[AuthenticationSuccessEvent] User " + event.getAuthentication().getName() + " has been successfully authenticated");
        } else if (applicationEvent instanceof AuthenticationFailureBadCredentialsEvent) {
        	logger.error("[AuthenticationFailureBadCredentialsEvent] User has not been authenticated due to bad credentials");
        } else if (applicationEvent instanceof AuthorizationFailureEvent) {
            AuthorizationFailureEvent event = (AuthorizationFailureEvent) applicationEvent;
            logger.error("[AuthorizationFailureEvent] Unauthorized access to [" + event.getSource() + "] has been detected.");
        } else {
            logger.info("[" + applicationEvent.getClass().getSimpleName() + "] Unauthorized access to" + applicationEvent.getSource() + " has been detected.");
        }
	}
	
}
