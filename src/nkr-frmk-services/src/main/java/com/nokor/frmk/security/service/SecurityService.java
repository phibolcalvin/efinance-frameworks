package com.nokor.frmk.security.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.frmk.criteria.SecUserCriteria;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;


/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface SecurityService extends BaseEntityService {
	public List<SecProfile> getProfiles() throws DaoException;
	
    public List<SecApplication> getListApplications() throws DaoException;
    
    public List<SecUser> getListUsers() throws DaoException;
    
    public List<SecUser> getListUsers(SecUserCriteria criteria);
    
    public SecUser loadUserByUsername(String username)  throws DaoException;

    public SecProfile findProfileByCode(String secProCode) throws DaoException;

    public List<SecProfile> findProfilesByUser(SecUser secUser) throws DaoException;
    
    public SecApplication getApplication(String code) throws DaoException;

	void changePassword(SecUser secUser, String newRawPwd);

	void changePassword(SecUser secUser, String oldRawPwd, String newRawPwd);

	SecUser createUser(String login, String descLogin, String password, SecProfile defaultProfile) throws SecUserCreationException;

	void create(SecUser secUser, String rawPwd) throws SecUserCreationException;

	void update(SecUser secUser);

	void changePasswordSalt(SecUser secUser);

	void addProfileToUser(SecUser secUser, SecProfile secPro);

	void addApplicationToUser(SecUser secUser, SecApplication secApp);

	void addProfileToApplication(SecApplication secApp, SecProfile secPro);

	void addApplicationUserProfile(SecApplication secApp, SecUser secUsr, SecProfile secPro);

	boolean checkUserInApplicationUser(SecUser user, SecApplication app);

	boolean checkUserInUserProfile(SecUser user, SecProfile pro);

	boolean checkUserInApplicationProfile(SecUser user, SecApplication app, SecProfile pro);

	boolean checkUserInApplicationUserProfile(SecUser user, SecApplication app, SecProfile pro);

}
