package com.nokor.frmk.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.frmk.model.config.SettingConfig;
import com.nokor.frmk.security.model.SecApplication;

/**
 * Settings service functionality to manage settings configuration Default,
 * Application settings
 * 
 * @author prasnar
 */
public interface SettingService extends BaseEntityService {
	
    void setCurrentSecApplication(SecApplication secApplication);

	void flushCache();

	Map<String, String> getPairValues(String code);

	boolean getValueBoolean(String code);

	int getValueInt(String code);
	
	long getValueLong(String code);
	
	BigDecimal getValueBigDecimal(String code);

	Double getValueDouble(String code);

	List<Long> getValuesID(String code);

	List<Integer> getValuesInt(String code);

	List<Long> getValuesLong(String code);

	List<String> getValues(String code);

	String getValue(String code);

	SettingConfig getSettingConfig(String code);

	SettingConfig getSettingConfig(SecApplication secApp, String code);

	List<SettingConfig> list(boolean refreshCache);

	List<SettingConfig> list(SecApplication secApp, boolean refreshCache);
	
	Locale getLocale();

	<T extends Entity> List<String> getRequiredPropertiesForActive(Class<T> entityClass);


}
