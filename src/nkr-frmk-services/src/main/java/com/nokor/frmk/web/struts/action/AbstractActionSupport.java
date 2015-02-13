package com.nokor.frmk.web.struts.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.exception.ErrorCode;
import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.service.ReferenceTableService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author sok.pongsametrey
 *
 */
public abstract class AbstractActionSupport extends ActionSupport implements ServletRequestAware, RequestAware, SessionAware, ServletResponseAware {

    private static final long serialVersionUID = 2279430216282145422L;

    protected static final Log logger = Log.getInstance(AbstractActionSupport.class);

    public static final Long DEFAULT_SEC_APPLICATION_ID = Long.valueOf(1);
    public static final String CURRENT_APPLICATION = "currentApplication";
    public static final String ACTION_RESULT = "actionResult";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> sessionMap = new HashMap<String, Object>();
    private Map<String, Object> requestMap = new HashMap<String, Object>();
    private ActionResult actionResult;
    private String urlRedirect;
    
    private EntityService entitySrv;
    private ReferenceTableService refTableSrv;
    private SecurityService secSrv;

    private String urlReferer;
    
    private boolean initServices = false;

    @Override
    public String execute() throws Exception {
        logger.enter("execute - start");

        try {
            logger.info("Service called by : IP [" + request.getRemoteAddr() + "]");
            logger.info("Service called by : Host [" + request.getRemoteHost() + "]");
            logger.info("Service called by : Port [" + request.getRemotePort() + "]");
            logger.info("Service called by : User [" + request.getRemoteUser() + "]");
            this.urlReferer = request.getHeader("referer");

            invokeMethod();

        } catch (AuthenticationCredentialsNotFoundException e) {
            String errMsg = I18N.message("error.not.authorized.page", getRequest().getRequestURI());
            logger.error(errMsg);
            setResultNotAuthorized();
            getActionResult().getErrorHandler().setCode(ErrorCode.ERR_SYS);
            getActionResult().getErrorHandler().setTechnicalMessage(errMsg);
        } catch (Exception e) {
            // Any exceptions thrown by invoking the server
            // method should be sent back to the client. 
            // Make sure we are working with a 'pure' output stream
            // that does not contain any other data	
            String techErrMsg = "An expected error has occurred.";
            logger.error(techErrMsg, e);
            String errMsg = "The action has failed. A technical error has occured.";
            getActionResult().getErrorHandler().setCode(ErrorCode.ERR_SYS);
            setActionResultSysError(errMsg);
            getActionResult().getErrorHandler().setTechnicalMessage(techErrMsg + ActionResult.BREAKLINE + logger.getStackTrace(e));
            
        } finally {
            logger.exit("execute - result: " + getActionResult().getResult());
            getRequest().setAttribute(ErrorHandler.ERROR_HANDLER_KEY, getActionResult().getErrorHandler());
            getRequest().getSession().setAttribute(ACTION_RESULT, getActionResult());
        }

        return getActionResult().getResult();
    }

    /**
     * 
     */
    private void initServices() {
        entitySrv = getService(EntityService.class);
        refTableSrv = getService(ReferenceTableService.class);
        secSrv = getService(SecurityService.class);

        initServices = true;
    }

    /**
     * @return the actionResult
     */
    public ActionResult getActionResult() {
        if (actionResult == null) {
            actionResult = new ActionResult();
        }
        return actionResult;
    }
    
    /**
     * 
     * @param urlRedirect
     */
    public void setActionResultRedirect(String urlRedirect) {
    	getActionResult().setResultRedirect(urlRedirect);
    	this.urlRedirect = urlRedirect;
    	
    }
    
    /**
     * 
     */
    public void setResultNotAuthorized() {
    	getActionResult().setResultNotAuthorized();
    }
    
    /**
     * 
     */
    public void setActionResultSysError() {
    	getActionResult().setResultSysError();
    }
    
    public void setActionResultSysError(String sysMsg) {
    	getActionResult().setErrorSysMessage(sysMsg);
    }

    public void setActionResultError(String errMsg) {
    	getActionResult().setErrorMessage(errMsg);
    }

    /**
     * 
     * @return
     */
    protected static ApplicationContext getAppContext() {
        return SpringUtils.getAppContext();
    }

    /**
     * 
     * @param serviceClass
     * @return
     */
    protected static <T extends BaseEntityService> T getService(final Class<T> serviceClass) {
        return getAppContext().getBean(serviceClass);
    }

    /**
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(final Map<String, Object> value) {
        sessionMap = value;
    }

    /**
     * 
     * @return
     */
    public Object getSessionAttribute(final String key) {
        return sessionMap.get(key);
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void setSessionAttribute(final String key, final Object value) {
        this.sessionMap.put(key, value);
    }

    /**
     * @see org.apache.struts2.interceptor.RequestAware#setRequest(java.util.Map)
     */
    @Override
    public void setRequest(final Map<String, Object> map) {
        this.requestMap = map;
    }

    /**
     * 
     * @param key
     * @return
     */
    public Object getRequestAttribute(final String key) {
        return this.requestMap.get(key);
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void setRequestAttribute(final String key, final Object value) {
        this.requestMap.put(key, value);
    }

    /**
     * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
     */
    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }

    /**
     * 
     * @return
     */
    public String getUrlReferer() {
        return urlReferer;
    }

    /**
     * 
     * @param urlReferer
     */
    public void setUrlReferer(final String urlReferer) {
        this.urlReferer = urlReferer;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public abstract void invokeMethod() throws Exception;

    /**
     * @return the entitySrv
     */
    public EntityService getEntitySrv() {
    	if (!initServices) {
    		initServices();
    	}
        return entitySrv;
    }

    /**
     * @param entitySrv the entitySrv to set
     */
    public void setEntitySrv(final EntityService entitySrv) {
        this.entitySrv = entitySrv;
    }

   
    /**
     * 
     * @return
     */
	public SecUser getSecUser() {
		return isAuthenticated() ? SecApplicationContext.getSecUser() : null;
	}
	
	public boolean isInAdminProfile() {
		return isAuthenticated() && getSecUser().isInAdminProfile();
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecUser;
				//SecUserContextHolder.getContext().isAuthenticated();
	}

	/**
     * @return the refTableSrv
     */
    public ReferenceTableService getRefTableSrv() {
    	if (!initServices) {
    		initServices();
    	}
        return refTableSrv;
    }

    /**
     * @param refTableSrv the refTableSrv to set
     */
    public void setRefTableSrv(final ReferenceTableService refTableSrv) {
        this.refTableSrv = refTableSrv;
    }

    /**
     * @return the secSrv
     */
    public SecurityService getSecSrv() {
    	if (!initServices) {
    		initServices();
    	}
        return secSrv;
    }

    /**
     * @param secSrv the secSrv to set
     */
    public void setSecSrv(final SecurityService secSrv) {
        this.secSrv = secSrv;
    }

	/**
	 * @return the urlRedirect
	 */
	public String getUrlRedirect() {
		return urlRedirect;
	}

	/**
	 * @param urlRedirect the urlRedirect to set
	 */
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
}
