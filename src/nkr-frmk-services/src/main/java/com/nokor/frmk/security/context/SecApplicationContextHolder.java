package com.nokor.frmk.security.context;

import org.springframework.util.Assert;

/**
 * 
 * @author prasnar
 *
 */
public final class SecApplicationContextHolder {
    private static SecApplicationContext contextHolder;


    /**
     * 
     */
    public static void clearContext() {
        contextHolder = null;
    }

    /**
     * 
     * @return
     */
    public static SecApplicationContext getContext() {
        if (contextHolder == null) {
            contextHolder = new SecApplicationContext();
        }

        return contextHolder;
    }

    /**
     * 
     * @param context
     */
    public static void setContext(SecApplicationContext context) {
        Assert.notNull(context, "Only non-null SecApplicationContext instances are permitted");
        contextHolder = context;
    }

    /**
     * 
     * @return
     */
    public static SecApplicationContext createEmptyContext() {
        return new SecApplicationContext();
    }
}

