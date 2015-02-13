package org.seuksa.frmk.model.sysref;

import org.seuksa.frmk.i18n.I18N;

/**
 * Auto-generated StatusRecord class
 * @author prasnar
 *
 */
public enum StatusRecord implements ISysRefData {

    ACTIV("active"),
    NEW("new"),
    INPRO("inprogress"),
    INACT("inactive"),
    RECYC("recycled"),
    ARCHI("archived");

    private final String code;

    /**
     * 
     */
    private StatusRecord(final String code) {
        this.code = code;
    }

    /**
     * return code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * return desc
     */
    @Override
    public String getDesc() {
        return I18N.value(code);
    }

}
