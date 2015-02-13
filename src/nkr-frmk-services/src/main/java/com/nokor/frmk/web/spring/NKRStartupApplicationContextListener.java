package com.nokor.frmk.web.spring;

import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.resource.AppConfigFile;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nokor.frmk.helper.AppConfigFileHelper;
import com.nokor.frmk.helper.SettingConfigConstants;
import com.nokor.frmk.helper.SettingConfigHelper;
import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 * 
 */
public class NKRStartupApplicationContextListener extends ContextLoaderListener implements ServletContextListener, SettingConfigConstants {
    protected Log logger = Log.getInstance(NKRStartupApplicationContextListener.class);

    /**
     * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        try {
            logger.info("FRMK Context - Initializing...");
            
            SpringUtils.initAppContext(event.getServletContext());
            
            final String logFile = event.getServletContext().getInitParameter(LOG4J_INIT_CFG_FILE);
            logger.info("Web.xml [" + LOG4J_INIT_CFG_FILE + "]: " + (logFile  != null ? logFile : "<N/A>"));
            if (!StringUtils.isEmpty(logFile)) {
                Log.initFile(logFile);
            }

            String appCode = null;

            String appCfgFile = event.getServletContext().getInitParameter(APP_CFG_FILE);
            logger.info("Web.xml [" + APP_CFG_FILE + "]: " + (appCfgFile  != null ? appCfgFile : "<N/A>"));
            if (StringUtils.isEmpty(appCfgFile)) {
	            logger.info("The default config file is [" + AppConfigFile.DEFAULT_APP_CONFIGURATION_FILE + "]");
	            appCfgFile = AppConfigFile.DEFAULT_APP_CONFIGURATION_FILE;
            }
            
        	AppConfigFile.initFile(appCfgFile);
        	appCode = AppConfigFileHelper.getApplicationCode();
            logger.info("Config file [" + appCfgFile + "] [" + AppConfigFileHelper.APP_CODE + "]: " + (appCode  != null ? appCode : "<N/A>"));
        
//            if (StringUtils.isEmpty(appCode)) {
//	            appCode = event.getServletContext().getInitParameter(AppConfigFileHelper.APP_CODE);
//	            logger.info("Web.xml [" + AppConfigFileHelper.APP_CODE + "]: " + (appCode  != null ? appCode : "<N/A>"));
//            }
            
            if (StringUtils.isEmpty(appCode)) {
            	throw new IllegalStateException("The application code [" + AppConfigFileHelper.APP_CODE + "] must be not defined in [" + AppConfigFile.getConfigFile() + "]" );
            }
            
            SecApplicationContext secAppContext = SecApplicationContextHolder.getContext();
            secAppContext.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()));

	    	SecApplication secApp =  SecurityConfigUtil.getSecApplication();
            logger.info("SecApplication: [" + (secApp  != null ? secApp : "<null>"));
            secAppContext.setSecApplication(secApp);
	            
	    	Locale locale = SettingConfigHelper.getLocale();
            secAppContext.setLocale(locale); // read from init file/db
            
        }
        catch (final Exception e) {
            logger.errorStackTrace("****FRMK Context NOT initialized.*****", e);
            throw new IllegalStateException(e);
        }
        logger.info("FRMK Context successfully initialized.");

    }
    /**
     * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        logger.info("FRMK Context destroyed.");
    }
}
