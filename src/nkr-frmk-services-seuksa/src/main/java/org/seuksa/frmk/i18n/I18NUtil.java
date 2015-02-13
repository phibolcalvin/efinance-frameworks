package org.seuksa.frmk.i18n;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.vo.ValuePair;

/**
 * 
 * @author prasnar
 *
 */
public class I18NUtil {
    private static Log logger = Log.getInstance(I18NUtil.class);

    /**
     * 
     * @param propertyFile
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(String propertyFile, String encodingSource, String encodingTarget) {
    	init(propertyFile, Locale.getDefault(), encodingSource, encodingTarget);
    }

    /**
     * 
     * @param propertyFiles
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(List<String> propertyFiles, Locale locale, String encodingSource, String encodingTarget) {
    	HashMap<String, String> messages = null;
    	for (String propertyFile : propertyFiles) {
    		messages = buildMessagesBundle(messages, propertyFile, locale, encodingSource, encodingTarget);
    	}
    	I18N.addBundle(locale, messages);
    	
    }

    /**
     * 
     * @param propertyFile
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(String propertyFile, Locale locale, String encodingSource, String encodingTarget) {
    	HashMap<String, String> messages = null;
    	messages = buildMessagesBundle(messages, propertyFile, locale, encodingSource, encodingTarget);
    	I18N.addBundle(locale, messages);
    }
    	
    /**
     * 
     * @param propertyFile
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     */
    public static HashMap<String, String> buildMessagesBundle(HashMap<String, String> messages, String propertyFile, Locale locale, String encodingSource, String encodingTarget) {
    	ResourceBundle rb = getResourceBundle(propertyFile, locale, encodingSource, encodingTarget);
        if (rb == null) {
        	return messages;
        }
	     if (messages == null) {
	    	 messages = new HashMap<>();
	     }
        for (String key : rb.keySet()) {
        	if (!messages.containsKey(key)) {
	            ValuePair msg = new ValuePair();
	            msg.setCode(key);
	            try {
	                msg.setValue(new String(rb.getString(key).getBytes(encodingSource), encodingTarget));
	            }
	            catch (Exception e) {
	                logger.errorStackTrace(e);
	                msg.setValue(key);
	            }
	            messages.put(msg.getCode(), msg.getValue());
        	}
	     }
        return messages;
    }
    	
    /**
     * 
     * @param propertyFile
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     * @return
     */
    private static ResourceBundle getResourceBundle(String propertyFile, Locale locale, String encodingSource, String encodingTarget) {
        logger.info("Init I18n: Resource file [" + propertyFile + "]");
        if (locale == null) {
            logger.info("Locale is null, then the Locale.getDefault() [" + Locale.getDefault().toLanguageTag() + "]");
        	locale = Locale.getDefault();
        }
        Log.getInstance(I18N.class).info(". Locale [" + locale.toLanguageTag() + "]");
        Log.getInstance(I18N.class).info(". Encoding Source [" + encodingSource + "]");
        Log.getInstance(I18N.class).info(". Encoding Target [" + encodingTarget + "]");

        ResourceBundle rb = null;
        try {
            logger.info("Init I18n: Trying with [" + locale.toLanguageTag() + "]");
        	rb = ResourceBundle.getBundle(propertyFile, locale);
        } catch (MissingResourceException e1) {
        	logger.error("- 1st try: " + e1.getMessage());
            logger.info("Init I18n: Trying with default locale [" + Locale.getDefault().toLanguageTag() + "]");
            try {
            	rb = ResourceBundle.getBundle(propertyFile);
            } catch (MissingResourceException e2) {
            	logger.error("- 2nd try: " + e2.getMessage());
            	logger.error(e2.getMessage());
            }
        }
        
        return rb;
    }
}
