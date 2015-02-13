package com.nokor.frmk.security.service;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.nokor.frmk.security.model.SecUser;

/**
 * Responsible for user authentication with Spring Security and application.
 *
 * @author prasnar
 */
public interface AuthenticationService extends UserDetailsService, ApplicationListener {
    
    public SecUser authenticate(String username, String password);

}
