package org.seuksa.frmk.tools.amount;

import java.text.DecimalFormat;

/**
 * @author ly.youhort
 */
public class AmountUtils {
	
	/**
	 * @param value
	 * @return
	 */
	public static String formatUSD(Double value) {
		if (value != null) {
			DecimalFormat stringValue = new DecimalFormat("$###,###.00");
			return stringValue.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 * @return
	 */
	public static String format(Double value) {
		if (value != null) {
			DecimalFormat stringValue = new DecimalFormat("##0.00");
			return stringValue.format(value);
		}
		return "";
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static Amount convertToAmount(Double value) {
		Amount amount = new Amount();
		amount.setTiAmountUsd(value);
		amount.setVatAmountUsd(0d);
		amount.setTeAmountUsd(value);
		return amount;
	}
}

