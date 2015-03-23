package org.seuksa.frmk.tools.amount;

import java.io.Serializable;

import org.seuksa.frmk.tools.DoubleUtils;

/**
 * @author ly.youhort
 *
 */
public class Amount implements Serializable {
	private static final long serialVersionUID = 3612496365754262305L;
	
	private Double tiAmountUsd;
	private Double vatAmountUsd;
	private Double teAmountUsd;
	private int nbDecimal = 2;
	
	public Amount() {
		
	}
	
	/**
	 * @param teAmountUsd
	 * @param vatAmountUsd
	 * @param tiAmountUsd
	 */
	public Amount(Double teAmountUsd, Double vatAmountUsd, Double tiAmountUsd) {
		this(teAmountUsd, vatAmountUsd, tiAmountUsd, 2);
	}
	
	/**
	 * @param teAmountUsd
	 * @param vatAmountUsd
	 * @param tiAmountUsd
	 */
	public Amount(Double teAmountUsd, Double vatAmountUsd, Double tiAmountUsd, int nbDecimal) {
		this.teAmountUsd = teAmountUsd;
		this.vatAmountUsd = vatAmountUsd;
		this.tiAmountUsd = tiAmountUsd;
		this.nbDecimal = nbDecimal;
	}
	
	/**
	 * @return the tiAmountUsd
	 */
	public Double getTiAmountUsd() {
		return tiAmountUsd;
	}
	
	/**
	 * @param tiAmountUsd the tiAmountUsd to set
	 */
	public void setTiAmountUsd(Double tiAmountUsd) {
		this.tiAmountUsd = tiAmountUsd;
	}
	
	/**
	 * @return the vatAmountUsd
	 */
	public Double getVatAmountUsd() {
		return vatAmountUsd;
	}
	
	/**
	 * @param vatAmountUsd the vatAmountUsd to set
	 */
	public void setVatAmountUsd(Double vatAmountUsd) {
		this.vatAmountUsd = vatAmountUsd;
	}
	
	/**
	 * @return the teAmountUsd
	 */
	public Double getTeAmountUsd() {
		return teAmountUsd;
	}
	
	/**
	 * @param teAmountUsd the teAmountUsd to set
	 */
	public void setTeAmountUsd(Double teAmountUsd) {
		this.teAmountUsd = teAmountUsd;
	}
	
	/**
	 * @param tiAmountUsd
	 */
	public void plusTiAmountUsd(Double tiAmountUsd) {
		this.tiAmountUsd = DoubleUtils.getDouble(this.tiAmountUsd) + DoubleUtils.getDouble(tiAmountUsd);
	}
	
	/**
	 * @param teAmountUsd
	 */
	public void plusTeAmountUsd(Double teAmountUsd) {
		this.teAmountUsd = DoubleUtils.getDouble(this.teAmountUsd) + DoubleUtils.getDouble(teAmountUsd);
	}
	
	/**
	 * @param vatAmountUsd
	 */
	public void plusVatAmountUsd(Double vatAmountUsd) {
		this.vatAmountUsd = DoubleUtils.getDouble(this.vatAmountUsd) + DoubleUtils.getDouble(vatAmountUsd);
	}
	
	/**
	 * @return the nbDecimal
	 */
	public int getNbDecimal() {
		return nbDecimal;
	}

	/**
	 * @param nbDecimal the nbDecimal to set
	 */
	public void setNbDecimal(int nbDecimal) {
		this.nbDecimal = nbDecimal;
	}
	
	/**
	 * @param amount
	 */
	public void plus(Amount amount) {
		plusTeAmountUsd(amount.getTeAmountUsd());
		plusVatAmountUsd(amount.getVatAmountUsd());
		plusTiAmountUsd(amount.getTiAmountUsd());
	}
}
