package com.nokor.frmk.tools;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class MessagingConfig {
	
	private static final Log log = LogFactory.getLog(SmsSender.class);
	
	/** Config name */
    private static final String CONFIG_NAME = "/messaging-config.xml";
    
    private static MessagingConfig config = null;
    
    private Configuration configuration = null;
    
    private MessagingConfig() {
    	try {
			configuration = new XMLConfiguration(MessagingConfig.class.getResource(CONFIG_NAME));			
		} catch (ConfigurationException e) {
			log.error("Error while loading configuation file [messaging-config.xml]", e);
			throw new IllegalArgumentException("Error while loading configuation file [messaging-config.xml]", e);
		}
    }
    
    /**
     * @return
     */
    public static MessagingConfig getInstance() {
    	if (config == null) {
    		config = new MessagingConfig();
    	}
    	return config;
    }
    
    /**
     * Return the current configuration 
     * @return
     */
    public Configuration getConfiguration() {
    	return configuration;
    }
}
