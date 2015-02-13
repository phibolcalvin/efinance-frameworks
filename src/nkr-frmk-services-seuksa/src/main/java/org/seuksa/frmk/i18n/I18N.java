package org.seuksa.frmk.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.vo.ValuePair;

/**
 * 
 * @author prasnar
 * 
 */
public class I18N {
    private static Log logger = Log.getInstance(I18N.class);

    public static String ERROR_FIELD_MANDATORY = "error.field.mandatory";

    private static I18N instance;
	private static Map<Locale, Map<String, String>> resources;
    private Locale currentLocale;
    	
    /**
     * 
     */
    private I18N() {
    }
    

    /**
     * 
     * @return
     */
	public static void createInstance(Locale locale) {
		initSingleton();
		instance.currentLocale = locale;
		instance.resources = new HashMap<>();
	}
    
    /**
     * 
     * @return
     */
	private static void initSingleton() {
		if (instance == null) {
			synchronized (I18N.class) {
				if (instance == null) {
					instance = new I18N();
				}
			}
		}
	}
    
	/**
     * 
     * @param messages
     */
    public static void addBundle(Locale locale, Map<String, String> messages) {
    	logger.info("Init I18n bundle - Nb messages : " + messages.size());
    	
    	if (instance == null) {
    		createInstance(locale);
    	}
        instance.resources.put(locale, messages);
    }

  
	/**
	 * @return the currentLocale
	 */
	public static Locale getCurrentLocale() {
		return instance.currentLocale;
	}



	/**
	 * @param currentLocale the currentLocale to set
	 */
	public static void setCurrentLocale(Locale currentLocale) {
		instance.currentLocale = currentLocale;
	}
	
	public static Locale getLocale() {
		return getCurrentLocale();
	}



	/**
	 * @param currentLocale the currentLocale to set
	 */
	public static void setLocale(Locale currentLocale) {
		setCurrentLocale(currentLocale);
	}

	   /**
     * 
     * @param messages
     */
    public static void initBundle(final ValuePair[] messages) {
    	initBundle(getLocale(), messages);
    }
    
    /**
     * 
     * @param messages
     */
    public static void initBundle(Locale locale, final ValuePair[] messages) {
        //Log.getInstance(I18N.class).info("Init I18n bundle - Nb messages : " + messages.length);
        HashMap<String, String> bundle = new HashMap<String, String>();
        bundle.clear();
        for (final ValuePair val : messages) {
            bundle.put(val.getCode(), val.getValue());
        }
        addBundle(locale, bundle);
    }

	/**
	 * 
	 * @return
	 */
	protected Map<String, String> getCurrentLocaleMessages() {
		return resources.get(getCurrentLocale());
	}

    
    /**
     * @return
     */
    public static char groupingSepator() {
        final String gs = message("grouping_separator");
        return gs.charAt(gs.length() - 1);
    }

    /**
     * 
     * @return
     */
    public static char decimalSepator() {
        final String gs = message("decimal_separator");
        return gs.charAt(gs.length() - 1);
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public static String message(final String code) {
        if (instance.getCurrentLocaleMessages().containsKey(code)) {
            return instance.getCurrentLocaleMessages().get(code);
        }
        else {
            return code;
        }
    }

    /**
     * 
     * @param field
     * @return
     */
    public static String messageMandatoryField(final String field) {
        return message(ERROR_FIELD_MANDATORY, field);
    }

    /**
     * 
     * @param key
     * @param vals
     * @return
     */
    public static String message(final String code, final String... vals) {
        String message = message(code);
        try {
            for (int i = 0; i < vals.length; i++) {
                message = message.replaceAll("\\{" + i + "\\}", vals[i]);
            }
        }
        catch (final Throwable e) {
        	logger.errorStackTrace(e);
        }
        return message;
    }

    /**
     * @param code
     * @return
     */
    public static String value(final String code) {
        return message(code);
    }
    /**
     * @param code
     * @return
     */
    public static String val(final String code) {
        return message(code);
    }
    /**
     * @param code
     * @return
     */
    public static String msg(final String code) {
        return message(code);
    }

}
