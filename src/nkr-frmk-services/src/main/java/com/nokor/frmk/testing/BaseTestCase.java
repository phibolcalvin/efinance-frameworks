package com.nokor.frmk.testing;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.resource.AppConfigFile;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.nokor.frmk.helper.AppConfigFileHelper;
import com.nokor.frmk.helper.SettingConfigConstants;
import com.nokor.frmk.helper.SettingConfigHelper;
import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.service.ReferenceTableService;
import com.nokor.frmk.service.SettingService;
import com.nokor.frmk.web.spring.NKRStartupApplicationContextListener;

/**
 * @author prasnar
 * @version $Revision$
 */
public class BaseTestCase extends TestCase implements SettingConfigConstants {
    protected static Log logger = Log.getInstance(BaseTestCase.class);
    
	private static final String DEFAULT_APP_CONFIGURATION_FILE = "app-config.properties";
    private static final String DEFAULT_I18N_FILE = "MessagesI18n";
    private static final String DEFAULT_MAIN_APP_CONTEXT_FILE = "application-main-context.xml";

    private String appCfgFile = DEFAULT_APP_CONFIGURATION_FILE;
    private String i18nFile = DEFAULT_I18N_FILE;
    private String mainAppContextFile = DEFAULT_MAIN_APP_CONTEXT_FILE;
    
    protected EntityService entitySrv; 
    protected SettingService settingSrv; 
    protected SecurityService securitySrv; 
    protected ReferenceTableService refTableSrv;

    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        try {
            SpringUtils.initAppContext(mainAppContextFile);
            
        	if (isInitSecApplicationContext()) {
        		initSecApplicationContext();
        	}

    		openSession();
        		
	    	entitySrv = getBean("entityService");
	    	securitySrv = getBean("securityService");
	    	settingSrv = getBean("settingService");
	    	refTableSrv = getBean("referenceTableService");
        
        } catch (Exception e) {
            String errMsg = "*******Fatal error occured initializing application. Exiting.!!!*******";
            System.err.println(errMsg);
            logger.error(errMsg);
            logger.errorStackTrace(e);
            SpringUtils.shutdownHsql();
            System.exit(-1);
        }
    }
    
    /**
     * 
     * @return
     */
    protected boolean isInitSecApplicationContext() {
    	return true;
    }
  
    /**
     * @see NKRStartupApplicationContextListener
     */
    public void initSecApplicationContext() {
        try {
            
            String appCode = null;

            if (StringUtils.isEmpty(appCfgFile)) {
	            logger.info("The default config file is [" + AppConfigFile.DEFAULT_APP_CONFIGURATION_FILE + "]");
	            appCfgFile = AppConfigFile.DEFAULT_APP_CONFIGURATION_FILE;
            }
            
        	AppConfigFile.initFile(appCfgFile);
        	appCode = AppConfigFileHelper.getApplicationCode();
            logger.info("Config file [" + appCfgFile + "] [" + AppConfigFileHelper.APP_CODE + "]: " + (appCode  != null ? appCode : "<N/A>"));
        

            if (StringUtils.isEmpty(appCode)) {
            	throw new IllegalStateException("The application code [" + AppConfigFileHelper.APP_CODE + "] must be not defined in [" + AppConfigFile.getConfigFile() + "]" );
            }

            SecApplicationContext secAppContext = SecApplicationContextHolder.getContext();
            secAppContext.setApplicationContext(SpringUtils.getAppContext());
            
	    	SecApplication secApp =  SecurityConfigUtil.getSecApplication();
            logger.info("SecApplication: [" + (secApp  != null ? secApp : "<null>"));
            secAppContext.setSecApplication(secApp);
	            
	    	Locale locale = SettingConfigHelper.getLocale();
            secAppContext.setLocale(locale); // read from init file/db
            
            logger.info("Context initialized.");

        }
        catch (final Exception e) {
            logger.errorStackTrace("****Context NOT initialized.*****", e);
            throw new IllegalStateException(e);
        }
    }
 
    
    /**
     * 
     * @throws Exception
     */
    protected void openSession() throws Exception {
    	logger.info("OPEN SESSION - Simulate OpenSessionInViewFilter");
        SessionFactory sessionFactory = SpringUtils.getSessionFactory();
        Session session = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    
    /**
     * 
     */
    protected void closeSession() {
    	logger.info("CLOSE SESSION - Simulate OpenSessionInViewFilter");
        SessionFactory sessionFactory = SpringUtils.getSessionFactory();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.closeSession(sessionHolder.getSession());
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }

    /**
     * 
     */
    public BaseTestCase() {
    }

    /**
     * @return the appContext
     */
    public static ApplicationContext getAppContext() {
        return SpringUtils.getAppContext();
    }
    
    /**
     * 
     * @param serviceClass
     * @return
     */
    protected static <T extends BaseEntityService> T getService(Class<T> serviceClass) {
    	return SpringUtils.getService(serviceClass);
    }

    /**
     * 
     * @param beanClass
     * @return
     */
    protected static <T> T getBean(Class<T> beanClass) {
    	return SpringUtils.getBean(beanClass);
    }
    
    /**
     * 
     * @param beanName
     * @return
     */
    protected static <T> T getBean(String beanName) {
    	return SpringUtils.getBean(beanName);
    }

	/**
	 * @return the appConfigFile
	 */
	public String getAppConfigFile() {
		return appCfgFile;
	}

	/**
	 * @param appConfigFile the appConfigFile to set
	 */
	public void setAppConfigFile(String appConfigFile) {
		this.appCfgFile = appConfigFile;
	}

	/**
	 * @return the i18nFile
	 */
	public String getI18nFile() {
		return i18nFile;
	}

	/**
	 * @param i18nFile the i18nFile to set
	 */
	public void setI18nFile(String i18nFile) {
		this.i18nFile = i18nFile;
	}

	/**
	 * @return the mainAppContextFile
	 */
	public String getMainAppContextFile() {
		return mainAppContextFile;
	}

	/**
	 * @param mainAppContextFile the mainAppContextFile to set
	 */
	public void setMainAppContextFile(String mainAppContextFile) {
		this.mainAppContextFile = mainAppContextFile;
	}

   
  

}
