package org.seuksa.frmk.tools.http;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.seuksa.frmk.tools.log.Log;

/**
 * 
 * @author prasnar
 * 
 */
public class SessionListener implements HttpSessionListener {

    /**
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent se) {
        Log.getInstance(this).info("Session created.");
        //HttpSession session = se.getSession();

    }

    /**
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        Log.getInstance(this).info("Session destroyed.");
        //HttpSession session = se.getSession();
    }

}
