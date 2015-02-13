/**
 * Created on 22/05/2012
 */
package org.seuksa.frmk.type;

/**
 * 
 * @author kong.phirun
 *
 */
public class FloatCurrency {

    private Float value;
    private CurrencyType currencyType;

    public Float getValue() {
        return value;
    }

    public void setValue(final Float value) {
        this.value = value;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(final CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

}
