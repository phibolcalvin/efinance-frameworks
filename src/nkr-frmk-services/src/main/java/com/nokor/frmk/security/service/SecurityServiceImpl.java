package com.nokor.frmk.security.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.frmk.criteria.SecApplicationCriteria;
import com.nokor.frmk.criteria.SecUserCriteria;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.dao.SecurityDao;
import com.nokor.frmk.security.dao.SecurityQueries;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecApplicationUserProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.spring.SecSaltGenerator;
import com.nokor.frmk.security.spring.encoding.PasswordEncoderUtils;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("securityService")
@Transactional
public class SecurityServiceImpl extends BaseEntityServiceImpl implements SecurityService {
	
	@Autowired
    private SecurityQueries queries;
	@Autowired
    private SecurityDao dao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SaltSource saltSource;
	
    /**
     * 
     */
    public SecurityServiceImpl() {
    }
    
   

    /**
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
	
	
    /**
	 * @return the passwordEncoder
	 */
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}



	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}



	/**
     * @
     * 
     */
	public List<SecApplication> getListApplications(SecApplicationCriteria criteria) {
		return dao.list(criteria);
	}

    /**
     * @
     * 
     */
	@Override
	public List<SecUser> getListUsers(SecUserCriteria criteria) {
		return dao.list(criteria);
	}
	

	

	/**
	 * @
	 * @see com.nokor.frmk.security.service.SecurityService#getApplication(java.lang.String)
	 */
	@Override
	public SecApplication getApplication(String code) {
		if (StringUtils.isEmpty(code)) {
    		return null;
    	}
        SecApplicationCriteria criteria = new SecApplicationCriteria();
        criteria.setCode(code);
        List<SecApplication> lst = getListApplications(criteria);
        return lst != null && lst.size() > 0 ? lst.get(0) : null;
	}


	@Override
	public List<SecProfile> getProfiles() {
		return null;
	}

	@Override
	public List<SecApplication> getListApplications() {
		return null;
	}

	@Override
	public List<SecUser> getListUsers() {
		return null;
	}

	@Override
	public SecUser loadUserByUsername(String username) {
        return queries.loadUserByUsername(username);
	}

    @Override
    public List<SecProfile> findProfilesByUser(SecUser secUser) {
        return queries.findProfilesByUser(secUser.getId());
    }
    
    @Override
	public boolean checkUserInApplicationUser(SecUser user, SecApplication app) {
    	if (user.getApplications() == null || user.getApplications().size() == 0) {
    		return false;
    	}
        for (SecApplication currApp : user.getApplications()) {
			if (currApp.getId().equals(app.getId())) {
				return true;
			}
		}
        return false;
	}

    @Override
	public boolean checkUserInUserProfile(SecUser user, SecProfile pro) {
    	if (user.getProfiles() == null || user.getProfiles().size() == 0) {
    		return false;
    	}
        for (SecProfile currPro : user.getProfiles()) {
			if (currPro.getId().equals(pro.getId())) {
				return true;
			}
		}
        return false;
	}
    
    @Override
	public boolean checkUserInApplicationProfile(SecUser user, SecApplication app, SecProfile pro) {
        return checkUserInApplicationUser(user, app) && checkUserInUserProfile(user, pro);
	}
    
    @Override
    public boolean checkUserInApplicationUserProfile(SecUser user, SecApplication app, SecProfile pro) {
    	BaseRestrictions<SecApplicationUserProfile> restrictions = new BaseRestrictions<SecApplicationUserProfile>(SecApplicationUserProfile.class);
    	restrictions.addAssociation("secApplication", "app", JoinType.INNER_JOIN);
    	restrictions.addAssociation("secUser", "user", JoinType.INNER_JOIN);
    	restrictions.addAssociation("secProfile", "pro", JoinType.INNER_JOIN);
    	restrictions.addCriterion("app.id", app.getId());
    	restrictions.addCriterion("user.id", user.getId());
    	restrictions.addCriterion("pro.id", pro.getId());
    	
        List<SecApplicationUserProfile> lst = list(restrictions);
        
        return lst != null && lst.size() > 0;
    }
    
    @Override
    public SecProfile findProfileByCode(String secProCode) {
        return queries.findProfileByCode(secProCode);
    }
    
	@Override
	public SecUser createUser(String login, String descLogin, String password, SecProfile defaultProfile) throws SecUserCreationException {
		SecUser secUser = null;
		try {
			boolean exist = true;
			int i = 1;
			String currUsername = login;
			do {
				if (i > 1) {
					int indDuplicate = currUsername.indexOf(SecUser.LOGIN_SEP_DUPLICATE);
					if (indDuplicate > 5) {
						currUsername = currUsername.substring(0, indDuplicate);
					}
					currUsername += SecUser.LOGIN_SEP_DUPLICATE + i;
				}
				secUser = loadUserByUsername(currUsername);
				exist = secUser != null && secUser.getId() != null && secUser.getId() > 0;
				i++;
			} while (exist);
			
			String createdBy = null;
			if (SecApplicationContext.isAuthenticated()) {
				createdBy = SecApplicationContext.getSecUser().getLogin();
			} else {
				createdBy = "admin";
			}
			
			secUser = SecurityEntityFactory.createSecUserInstance(createdBy);
			secUser.setLogin(currUsername);
			secUser.setDesc(descLogin);
			secUser.setComment(password);
			secUser.setDefaultProfile(defaultProfile);
			
			create(secUser, password);
			
		} catch (Exception e) {
    		String errMsg ="Error during the creation of the user [" + secUser.getLogin() + "]";
    		logger.error(errMsg);
    		throw new SecUserCreationException(errMsg, e);
		} finally {
			if(secUser == null) {
				logger.error("The creation of [" + login + "] has failed.");
			} else {
				logger.debug("The creation of [" + login + "] is successful.");
			}
		}
		
		
		return secUser;
	}


	
    @Override
    public void create(SecUser secUser, String rawPwd) throws SecUserCreationException {
    	if (secUser == null) {
    		throw new SecUserCreationException("SecUser is null");
    	}
    	if (secUser.getId() != null && secUser.getId() > 0) {
        	throw new SecUserCreationException("The secUser should have an empty id.");
        }
    	if (StringUtils.isEmpty(rawPwd)) {
    		throw new SecUserCreationException("An empty password is not allowed");
    	}
    	if (secUser.getDefaultProfile() == null) {
        	throw new SecUserCreationException("The secUser must have a default profile assigned.");
        }
    	    	
    	try {
	        String encodedPwd = passwordEncoder.encodePassword(rawPwd, saltSource.getSalt(secUser));
	        secUser.setPwd(encodedPwd);
	        secUser.setStatusRecord(StatusRecord.ACTIV);
	        getDao().create(secUser);
	        getDao().saveOrUpdateList(secUser.getProfiles());
	
			logger.info("The creation of the user [" 
						+ secUser.getId() + "] ]"
						+ secUser.getLogin() + "] [" 
						+ secUser.getDesc() +  "] is successful.");
			logger.info("Default profile [" + secUser.getDefaultProfile().getDesc() + "]");
			logger.info("Digested password [" + secUser.getPassword() + "]");
			logger.info("Salt password [" + secUser.getPasswordSalt() + "]");
    	} catch (Exception e) {
    		String errMsg ="Error during the creation of the user [" + secUser.getLogin() + "]";
    		logger.error(errMsg);
    		throw new SecUserCreationException(errMsg, e);
    	}
    }
    
    @Override
    public void update(SecUser secUser) {
    	if (secUser == null || secUser.getId() <= 0) {
        	throw new IllegalArgumentException("The secUser should not have an empty id.");
        }
        getDao().update(secUser);
    }

    @Override
    @Transactional(readOnly=false)
    public void changePasswordSalt(SecUser secUser) {
    	secUser.setPasswordSalt(SecSaltGenerator.generateSaltFromSecUserId(secUser));
        getDao().merge(secUser);
//        getDao().updateViaHQL("update SecUser a set a.passwordSalt ='" + secUser.getPasswordSalt() + "';");
//        getDao().refresh(secUser);
    }
    	
    @Override
    @Transactional(readOnly=false)
    public void changePassword(SecUser secUser, String oldRawPwd, String newRawPwd) {
    	// should decode ?? from the digested pwd
        String encodedOldPwd = passwordEncoder.encodePassword(oldRawPwd, saltSource.getSalt(secUser));
        if (!PasswordEncoderUtils.equals(secUser.getPassword(), encodedOldPwd)) {
        	throw new BadCredentialsException("The old password does not correspond to the existing one.");
        }
    	changePassword(secUser, newRawPwd);
    }

    @Override
    public void changePassword(SecUser secUser, String newRawPwd) {
        String encodedPwd = passwordEncoder.encodePassword(newRawPwd, saltSource.getSalt(secUser));
        secUser.setPwd(encodedPwd);
        secUser.setLastPasswordChangeDate(new Date());
        getDao().update(secUser);
    }



	@Override
	public void addProfileToUser(SecUser secUser, SecProfile secPro) {
		// add in both bi-directionnal mapping
		secUser.getProfiles().add(secPro);
		secPro.getUsers().add(secUser);
		
		if (secUser.getDefaultProfile() == null) {
			secUser.setDefaultProfile(secPro);
		}
		update(secUser);
		
	}

	@Override
	public void addApplicationToUser(SecUser secUser, SecApplication secApp) {
		// add in both bi-directionnal mapping
		secUser.getApplications().add(secApp);
		secApp.getUsers().add(secUser);
	}
	
	@Override
	public void addProfileToApplication(SecApplication secApp, SecProfile secPro) {
		// add in both bi-directionnal mapping
		secApp.getProfiles().add(secPro);
		secPro.getApplications().add(secApp);
	}
	
	@Override
	public void addApplicationUserProfile(SecApplication secApp, SecUser secUsr, SecProfile secPro) {
		SecApplicationUserProfile appUsrPro = SecurityEntityFactory.createInstance(SecApplicationUserProfile.class);
		appUsrPro.setSecApplication(secApp);
		appUsrPro.setSecUser(secUsr);
		appUsrPro.setSecProfile(secPro);
		create(appUsrPro);
	}
	
}