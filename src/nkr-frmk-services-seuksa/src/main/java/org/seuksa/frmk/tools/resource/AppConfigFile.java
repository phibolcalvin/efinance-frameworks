package org.seuksa.frmk.tools.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyStringUtils;
import org.seuksa.frmk.tools.log.Log;

/**
 * 
 * @author prasnar
 *
 */
public class AppConfigFile extends Properties {
	/** */
	private static final long serialVersionUID = 4817939764467381363L;
    private Log logger = Log.getInstance(this.getClass());

    public static final String DEFAULT_APP_CONFIGURATION_FILE = "app-config.properties";

    private static String configFile = DEFAULT_APP_CONFIGURATION_FILE;

    
    /** Singleton */
    private static AppConfigFile instance;

    /**
     * Constructor
     * Can not be instantiated
     */
	private AppConfigFile() {
	}

	/**
     * 
     * @param configFile
     */
    public static void initFile(String configFile) {
    	AppConfigFile.configFile = configFile;
    	getInstance();
     }
    
	
	/**
	 * 
	 * @param cfgFile
	 * @return
	 */
	public static AppConfigFile getInstance() {
		if (instance == null) {
			synchronized (AppConfigFile.class) {
				if (instance == null) {
					instance = new AppConfigFile();
					instance.loadConfigFile();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public String getValue(String code) {
		return getProperty(code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean getValueBoolean(String code) {
		try {
			String value = getValue(code);
			if (StringUtils.isEmpty(value)) {
				return false;
			}
			value = value.trim();
			
            return Arrays.asList("true", "1").contains(value);
        } catch (Exception e) {
            String errMsg = "[getValueBoolean] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
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
            String errMsg = "[ValueLong] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
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
            String errMsg = "[ValueInteger] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<String> getValues(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) { 
	            logger.warn("[" + code + "] is not defined in [" + configFile + "] file.");
	            return new ArrayList<String>();
	        }
	
	        String[] values = propStr.replace(" ", "").split(MyStringUtils.LIST_SEPARATOR_COMMA);
			return Arrays.asList(values);
		} catch (Exception e) {
            String errMsg = "[getValues] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Integer> getValuesInt(String code) {
		try {
			String propStr = getValue(code);
		    if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in [" + configFile + "] file.");
		        return new ArrayList<Integer>();
		    }
		
		    return MyStringUtils.getValuesInt(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesInt] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Long> getValuesLong(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in [" + configFile + "] file.");
	            return new ArrayList<Long>();
	        }
			return MyStringUtils.getValuesLong(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesLong] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * @return the configFile
	 */
	public static String getConfigFile() {
		return configFile;
	}

	/**
	 * @param cfgFile the configFile to set
	 */
	public  static void setConfigFile(String cfgFile) {
		configFile = cfgFile;
	}

	/**
     * Load the configuration properties file
     * @return Properties
     */
    private void loadConfigFile() {
    	logger.info("Loading the configuration file [" + configFile + "]");
    	
    	logger.debug("Searching in the application classpath.." + configFile);
        InputStream in = ClassLoader.getSystemResourceAsStream(configFile);
        if (in == null) {
        	logger.debug("Searching in the system classpath.." + configFile);
            in = ClassLoader.getSystemResourceAsStream("/" + configFile);
        }
        if (in == null) {
        	logger.debug("Searching in the system classpath.." + configFile);
            in = ClassLoader.getSystemClassLoader().getResourceAsStream("/" + configFile);
        }
        if (in == null) {
        	logger.debug("Searching in the system classpath.." + configFile);
            in = AppConfigFile.class.getClassLoader().getResourceAsStream("/" + configFile); 
        }
        
        if (in == null) {
            String errMsg = "Can't find " + configFile 
            				+ " in the classpath, check your Configuration";
            logger.error(errMsg);
            throw new IllegalStateException("Can't find [" + configFile + "]");
        }
        try {
        	instance.load(in);
		} catch (IOException e) {
			 String errMsg = "Error while loading [" + configFile + "]";
			logger.error(errMsg);
			throw new IllegalStateException("Can't load [" + configFile + "]");
		}
        try {
			in.close();
		} catch (IOException e) {
		    logger.warn(e.getMessage());
            // do nothing
		    
		}
		Enumeration eKeys = instance.keys();
		while(eKeys.hasMoreElements()) {
			String key = (String) eKeys.nextElement();
			String value = (String) instance.get(key);

			logger.debug("Configuration: [" +  key + "] = [" + value + "]");
		}
    }
    
 }
