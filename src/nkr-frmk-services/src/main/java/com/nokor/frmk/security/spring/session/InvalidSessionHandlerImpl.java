package com.nokor.frmk.security.spring.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nokor.frmk.security.spring.MarkerResponseHandler;


/**
 * @author ly.youhort
 */
public class InvalidSessionHandlerImpl extends MarkerResponseHandler implements InvalidSessionHandler {
	
	private static final Log LOG = LogFactory.getLog(InvalidSessionHandlerImpl.class);

    public InvalidSessionHandlerImpl(){
        setMarkerSnippet(LOGIN_REQUIRED_MARKER);
    }

    @Override
    public void sessionInvalidated(HttpServletRequest request, HttpServletResponse response) 
    	throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Responded with LOGIN_REQUIRED_MARKER");
        }
        //handle(request, response);
        throw new InvalidSessionException();
    }
}
