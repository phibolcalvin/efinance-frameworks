package com.nokor.frmk.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.frmk.dao.SettingConfigDao;
import com.nokor.frmk.helper.SettingConfigConstants;
import com.nokor.frmk.model.config.SettingConfig;
import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.service.SettingService;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("settingService")
@Transactional
public class SettingServiceImpl extends BaseEntityServiceImpl implements SettingService {

	@Autowired
    private SettingConfigDao dao;
	
	private static HashMap<String, List<SettingConfig>> mapAppSettingsCache = new HashMap<String, List<SettingConfig>>();
    private SecApplication currentSecApplication = null;

    /**
     * 
     */
    public SettingServiceImpl() {
    }
    
    @PostConstruct
    public void postConstruct() {
    }
    
    @Override
	public BaseEntityDao getDao() {
		return dao;
	}

	/**
	 * @return the currentSecApplication
	 */
	public SecApplication getCurrentSecApplication() {
		if (currentSecApplication == null) {
    		currentSecApplication = SecApplicationContextHolder.getContext().getSecApplication();
    	}
		if (currentSecApplication == null) {
    		currentSecApplication = SecurityConfigUtil.getSecApplication();
    	}
    	if (currentSecApplication == null) {
    		throw new IllegalStateException("The current SecApplication must be set.");
    	}
        return currentSecApplication;
	}

	/**
	 * @param currentSecApplication the currentSecApplication to set
	 */
	@Override
	public void setCurrentSecApplication(SecApplication currentSecApplication) {
		this.currentSecApplication = currentSecApplication;
	}

	@Override
	public void flushCache() {
		mapAppSettingsCache.clear();
	}

	@Override
	public Map<String, String> getPairValues(String code) {
		try {
			Map<String, String> pairs = new HashMap<String, String>();
	        String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in the Application Settings Configuration.");
	            return pairs;
	        }
	        String[] properties = propStr.split(";");
	
	        for (String prop : properties) {
	        	String[] pair = prop.split(":");
	            pairs.put(pair[0], pair[1]);
	        }
	        return pairs;
		} catch (Exception e) {
            String errMsg = "[getPairValues] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public boolean getValueBoolean(String code) {
		try {
			String value = getValue(code);
			if (StringUtils.isEmpty(value)) {
				return false;
			}
			value = value.trim();
			
            return Arrays.asList("true", "1").contains(value);
        } catch (Exception e) {
            String errMsg = "[getValueBoolean] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public long getValueLong(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Long.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueLong] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public int getValueInt(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueInteger] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public BigDecimal getValueBigDecimal(String code) {
		try {
            return BigDecimal.valueOf(getValueDouble(code));
        }
        catch (Exception e) {
            String errMsg = "[ValueBigDecimal] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public Double getValueDouble(String code) {
		try {
			String value = getValue(code);
            return StringUtils.isNotEmpty(value) ? Double.valueOf(value) : 0L;
        } catch (Exception e) {
            String errMsg = "[ValueDouble] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public List<Long> getValuesID(String code) {
		return getValuesLong(code);
	}

	@Override
	public List<Long> getValuesLong(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
	            return new ArrayList<Long>();
	        }
			return MyStringUtils.getValuesLong(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesLong] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	


	@Override
	public List<Integer> getValuesInt(String code) {
		try {
			String propStr = getValue(code);
		    if (StringUtils.isEmpty(propStr)) {
		        logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
		        return new ArrayList<Integer>();
		    }
		
		    return MyStringUtils.getValuesInt(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesInt] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public List<String> getValues(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
	            return new ArrayList<String>();
	        }
	
	        String[] values = propStr.replace(" ", "").split(MyStringUtils.LIST_SEPARATOR_COMMA);
			return Arrays.asList(values);
		} catch (Exception e) {
            String errMsg = "[getValues] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public String getValue(String code) {
		try {
			SettingConfig appCfg = getSettingConfig(null, code);
	        if (appCfg == null) {
	            logger.warn("[SettingService.Value[" + code + "] returns NULL - Check its code/value in the [ts_setting_config] table" );
	            return null;
	        }
	        return appCfg.getValue();
		} catch (Exception e) {
            String errMsg = "[getValue] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public SettingConfig getSettingConfig(String code) {
		return getSettingConfig(null, code);
	}

	@Override
	public SettingConfig getSettingConfig(SecApplication secApp, String code) {
		List<SettingConfig> cfgDataList = list(secApp, false);
        for (SettingConfig cfgData : cfgDataList) {
           if (cfgData.getCode().equals(code)) {
                return cfgData;
            }
        }

        logger.warn("SettingService - Get(" + (secApp != null ? secApp.getCode() : "<no_app>") + ", " + code + ") = null");
        return null;
	}

	@Override
	public List<SettingConfig> list(boolean refreshCache) {
		return list(getCurrentSecApplication(), refreshCache);
	}

	@Override
	public List<SettingConfig> list(SecApplication secApp, boolean refreshCache) {
		if (secApp == null) {
			secApp = getCurrentSecApplication();
		}
		
		if (refreshCache) {
            flushCache();
        }
        List<SettingConfig> appConfigList = mapAppSettingsCache.get(secApp.getCode());
        if (appConfigList == null) {
            appConfigList = dao.list(secApp);
            if (mapAppSettingsCache.containsKey(secApp.getCode())) {
            	mapAppSettingsCache.remove(secApp.getCode());
            }
            mapAppSettingsCache.put(secApp.getCode(), appConfigList);
        }

        return appConfigList;
	}

	
	@Override
	public Locale getLocale() {
		String localeCode = getValue(SettingConfigConstants.APP_LOCALE);
		Locale locale = null;
		try {
			locale = LocaleUtils.toLocale(localeCode);
		} catch(Exception e) {
			logger.error("Error in getting Locale from the application settings configuration.");
			logger.error("The system default Locale is taken instead.");
			locale = Locale.getDefault();
		}
		return locale;
		
	}
	@Override
	public <T extends Entity> List<String> getRequiredPropertiesForActive(Class<T> entityClass) {
		return getValues(entityClass.getSimpleName() + "." + SettingConfigConstants.SUFFIX_REQUIRED_PROPERTIES_FOR_ACTIVE);
	}


}