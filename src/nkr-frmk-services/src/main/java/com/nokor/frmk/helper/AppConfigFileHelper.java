package com.nokor.frmk.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyStringUtils;
import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.resource.AppConfigFile;

import com.nokor.frmk.security.model.SecApplication;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class AppConfigFileHelper implements ServicesCoreHelper {
	protected static final Log logger = Log.getInstance(AppConfigFileHelper.class);

    public final static String APP_VERSION = "core.app.version";
    public final static String APP_CODE = "core.app.code";
    public final static String APP_I18N_BUNDLE_NAME = "core.app.i18n.bundle.name";
    public final static String APP_I18N_BUNDLE_NAME_LIST = "core.app.i18n.bundle.name.list";
    public final static String APP_I18N_ENCODING_SOURCE = "core.app.i18n.encoding.source";
    public final static String APP_I18N_ENCODING_TARGET = "core.app.i18n.encoding.target";

    public final static String APP_SECURITY_CHECK_TABLE_APPLICATION_USER = "core.app.security.check.table[sec_application_user]";
    public final static String APP_SECURITY_CHECK_TABLE_USER_PROFILE = "core.app.security.check.table[sec_user_profile]";
    public final static String APP_SECURITY_CHECK_TABLE_APPLICATION_PROFILE = "core.app.security.check.table[sec_application_profile]";
    public final static String APP_SECURITY_CHECK_TABLE_APPLICATION_USER_PROFILE = "core.app.security.check.table[sec_application_user_profile]";



    /**
     * Constructor
     * Can not be instantiated
     */
	protected AppConfigFileHelper() {
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static String getValue(String code) {
		return AppConfigFile.getInstance().getProperty(code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static boolean getValueBoolean(String code) {
		try {
			String value = getValue(code);
			if (StringUtils.isEmpty(value)) {
				return false;
			}
			value = value.trim();
			
            return Arrays.asList("true", "1").contains(value);
        }
        catch (Exception e) {
            String errMsg = "[getValueBoolean] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public long getValueLong(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Long.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueLong] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public int getValueInt(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueInteger] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public BigDecimal getValueBigDecimal(String code) {
		try {
            return BigDecimal.valueOf(getValueDouble(code));
        }
        catch (Exception e) {
            String errMsg = "[ValueBigDecimal] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public Double getValueDouble(String code) {
		try {
			String value = getValue(code);
            return StringUtils.isNotEmpty(value) ? Double.valueOf(value) : 0L;
        }
        catch (Exception e) {
            String errMsg = "[ValueDouble] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Long> getValuesID(String code) {
		return getValuesLong(code);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Long> getValuesLong(String code) {
		String propStr = getValue(code);
        if (StringUtils.isEmpty(propStr)) {
            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
            return new ArrayList<Long>();
        }
		return MyStringUtils.getValuesLong(propStr);
	}
	


	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Integer> getValuesInt(String code) {
		String propStr = getValue(code);
        if (StringUtils.isEmpty(propStr)) {
            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
            return new ArrayList<Integer>();
        }

        return MyStringUtils.getValuesInt(propStr);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<String> getValues(String code) {
		String propStr = getValue(code);
        if (StringUtils.isEmpty(propStr)) {
            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
            return new ArrayList<String>();
        }

        String[] values = propStr.replace(" ", "").split(MyStringUtils.LIST_SEPARATOR_COMMA);
		return Arrays.asList(values);
	}


	
	/**
	 * 
	 * @return
	 */
	public static String getApplicationCode() {
		return AppConfigFile.getInstance().getProperty(APP_CODE);
    }
	/**
	 * 
	 * @return
	 */
	public static String getI18nBundleName() {
		return AppConfigFile.getInstance().getProperty(APP_I18N_BUNDLE_NAME);
    }
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getI18nBundlesName() {
		return AppConfigFile.getInstance().getValues(APP_I18N_BUNDLE_NAME_LIST);
    }
	
	/**
	 * 
	 * @return
	 */
	public static String getI18nEncodingSource() {
		return AppConfigFile.getInstance().getProperty(APP_I18N_ENCODING_SOURCE);
    }
	/**
	 * 
	 * @return
	 */
	public static String getI18nEncodingTarget() {
		return AppConfigFile.getInstance().getProperty(APP_I18N_ENCODING_TARGET);
    }

	/**
	 * 
	 * @return
	 */
	public static SecApplication getSecApplication() {
		String appCode = getApplicationCode();
		if (StringUtils.isEmpty(appCode)) {
	    	throw new IllegalStateException("The application code [" + AppConfigFileHelper.APP_CODE + "] should be defined in [" + AppConfigFile.getConfigFile() + "]");
		}
	    SecApplication secApp = ENTITY_SRV.getByCode(SecApplication.class, appCode);
	    if (secApp == null) {
	    	throw new IllegalStateException("The current SecApplication Code set in [" + AppConfigFile.getConfigFile() + "] is invalid [" + appCode + "].");
	    }
	    return secApp;
	}
   
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUser() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_APPLICATION_USER);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableUserProfile() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_USER_PROFILE);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationProfile() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_APPLICATION_PROFILE);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUserProfile() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_APPLICATION_USER_PROFILE);
	}
}
