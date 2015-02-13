package com.nokor.frmk.helper;

import java.util.Locale;

import org.seuksa.frmk.tools.log.Log;



/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class SettingConfigHelper implements ServicesCoreHelper {
	protected static final Log logger = Log.getInstance(SettingConfigHelper.class);
    
    public final static String APP_CODE_BO = "app.code.bo";
    public final static String APP_CODE_FO = "app.code.fo";

    public final static String APP_MAIN_COM_ID = "app.main.com.id";

    public final static String APP_LOCALE = "app.locale";


    public final static int APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE = 20;
    public final static String APP_FO_LIST_NB_RECORDS_PER_PAGE = "app.fo.list.nb.records.per.page";
    public final static String APP_BO_LIST_NB_RECORDS_PER_PAGE = "app.bo.list.nb.records.per.page";

    public final static String APP_TMP_FOLDER = "app.tmp.folder";

    public final static String APP_REPORT_TEMPLATE_FOLDER = "app.report.template.folder";
    public final static String APP_REPORT_OUTPUT_FOLDER = "app.report.output.folder";

    public final static String APP_MAIL_SMTP = "app.mail.smtp";
    public final static String APP_MAIL_USER = "app.mail.user";
    public final static String APP_MAIL_PASSWORD = "app.mail.password";
    public final static String APP_MAIL_IS_ENABLED = "app.mail.is.enabled";

    public final static String APP_VAADIN_CUSTOM_LAYOUTS_FOLDER = "app.vaadin.custom.layout.folder";

    
    /** ---------------------------------------------------------
     *  SECURITY
     ** ---------------------------------------------------------*/
    public final static String APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS = "app.security.credential.expired.after.nb.days";
    public final static int APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS_DEFAULT_VALUE = 0;
    

    /**
     * Constructor
     * Can not be instantiated
     */
	protected SettingConfigHelper() {
	}

	/**
     * 
     * @return
     */
    public static int getCredentialExpiredAfterNbDays() {
    	try {
    		return SETTING_SRV.getValueInt(APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS);
    	} catch (Exception e) {
    		logger.error("Error while getting [APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS]");
    		logger.error("Return default value [" + APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS_DEFAULT_VALUE + "]");
    		return APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS_DEFAULT_VALUE;
    	}
    }
    
  
   
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListFO() {
    	try {
    		return SETTING_SRV.getValueInt(APP_FO_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListFO]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListBO() {
    	try {
    		return SETTING_SRV.getValueInt(APP_BO_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListFO]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    
    /**
     * 
     * @return
     */
    public static boolean getMailIsEnabled() {
		return SETTING_SRV.getValueBoolean(APP_MAIL_IS_ENABLED);
    }
    
    /**
     * 
     * @return
     */
    public static String getMailSMTP() {
        return SETTING_SRV.getValue(APP_MAIL_SMTP);
    }
    
    /**
     * 
     * @return
     */
    public static String getMailUser() {
        return SETTING_SRV.getValue(APP_MAIL_USER);
    }
    
    /**
     * 
     * @return
     */
    public static String getMailPassword() {
        return SETTING_SRV.getValue(APP_MAIL_PASSWORD);
    }
    
    /**
     * 
     * @return
     */
    public static String getApplicationCodeBO() {
    	return SETTING_SRV.getValue(APP_CODE_BO);
    }
    
    /**
     * 
     * @return
     */
    public static String getApplicationCodeFO() {
    	return SETTING_SRV.getValue(APP_CODE_FO);
    }


    /**
     * 
     * @return
     */
	public static Locale getLocale() {
		Locale locale = null;
		try {
	    	String localeLanguageTag = SETTING_SRV.getValue(APP_LOCALE);
	    	locale = Locale.forLanguageTag(localeLanguageTag);
		} catch(Exception e) {
			logger.error("Error in getting Locale from the application settings configuration.");
			logger.error("The system default Locale is taken instead.");
			locale = Locale.getDefault();
		}
		return locale;
		
	}

  
   /**
    * 
    * @return
    */
   public static Long getAppMainCompanyId() {
  		return SETTING_SRV.getValueLong(APP_MAIN_COM_ID);
   }
   

   /**
    * 
    * @return
    */
   public static String getTemporaryFolder() {
  		return SETTING_SRV.getValue(APP_TMP_FOLDER);
   }

	/**
	 * 
	 * @return
	 */
	public static String getVaadinCustomLayoutsFolder() {
		return SETTING_SRV.getValue(APP_VAADIN_CUSTOM_LAYOUTS_FOLDER);
	}
   
   /**
    * 
    * @return
    */
	public static String getReportOutputFolder() {
		return SETTING_SRV.getValue(APP_REPORT_OUTPUT_FOLDER);
	}
   
   /**
    * 
    * @return
    */
	public static String getReportTemplateFolder() {
		return SETTING_SRV.getValue(APP_REPORT_TEMPLATE_FOLDER);
	}

}
