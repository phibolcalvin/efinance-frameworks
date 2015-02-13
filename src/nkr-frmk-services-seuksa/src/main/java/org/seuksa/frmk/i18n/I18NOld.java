package org.seuksa.frmk.i18n;

import java.util.HashMap;

import org.seuksa.frmk.tools.log.Log;
import org.seuksa.frmk.tools.vo.ValuePair;

/**
 * @author ly.youhort
 * 
 */
public final class I18NOld {

    public static final String US_LOCALE = "en-US";
    public static String ERROR_FIELD_MANDATORY = "error.field.mandatory";

    private static String locale = US_LOCALE;
    private static HashMap<String, String> bundle = new HashMap<String, String>();

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
        if (bundle.containsKey(code)) {
            return bundle.get(code);
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
        catch (final Throwable t) {
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
     * 
     * @return
     */
    public static String getLocale() {
        return locale;
    }

    /**
     * 
     * @param locale
     */
    public static void setLocale(final String locale) {
        I18NOld.locale = locale;
    }

    /**
     * 
     * @param messages
     */
    public static void initBundle(final ValuePair[] messages) {
        Log.getInstance(I18N.class).info("Init I18n bundle - Nb messages : " + messages.length);
        bundle.clear();
        for (final ValuePair val : messages) {
            bundle.put(val.getCode(), val.getValue());
        }
    }


}
