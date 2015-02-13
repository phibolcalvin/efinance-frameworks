package com.nokor.frmk.web.struts.action;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.seuksa.frmk.tools.log.Log;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 
 * @author prasnar
 * 
 */
public class LoggingInterceptor implements Interceptor {
	/** */
	private static final long serialVersionUID = -6656803213884283724L;
    private static final Log logger = Log.getInstance(AbstractActionSupport.class);

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String className = invocation.getAction().getClass().getName();
        long startTime = System.currentTimeMillis();
		logger.info("LoggingInterceptor.intercept - Before calling action [" + className + "]");
        
		String result = "success";
		try {
			result = invocation.invoke();
		} catch (Exception ex) {
			Logger.getLogger(LoggingInterceptor.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		Object objException = ActionContext.getContext().getValueStack().findValue("exception");
		if (objException instanceof RuntimeException) {
			RuntimeException ex = (RuntimeException) objException;
			logger.errorStackTrace("LoggingInterceptor.intercept - RuntimeException", ex);
			throw ex;
		} else if (objException instanceof Exception) {
			Exception ex = (Exception) objException;
			logger.errorStackTrace("LoggingInterceptor.intercept - Exception", ex);
			throw ex;
		} else if (objException != null) {
			logger.error("LoggingInterceptor.intercept - Exception [" + objException.getClass().getName() + "]");
		}
		
		long endTime = System.currentTimeMillis();
		logger.info("After calling action: " + className + " Time taken: " + (endTime - startTime) + " ms");
		logger.info("LoggingInterceptor.intercept - rethrower");
		
		return result;
	}
}
