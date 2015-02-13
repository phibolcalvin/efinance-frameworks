package com.nokor.finance.services.shared;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.sysref.ISysRefData;

/**
 * Payment position
 * @author ly.youhort
 *
 */
public enum PaymentPosition implements ISysRefData {
	
	BEGIN_PERIOD("begin.period"),
	END_PERIOD("end.period");
    
    private final String code;

    private PaymentPosition(final String code) {
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
