package com.nokor.frmk.view.component;

/**
 * 
 * @author huot.pengleng
 *
 */
public class PageChangedEvent {

	private Long[] rowKeys;
	private Integer recordIndex;
	private Integer recordPerPage;
	
	public PageChangedEvent() {
	}

	/**
	 * Row key of the page belong to the event.
	 * @return {@link Long} row keys.
	 */
	public Long[] getRowKeys() {
		return rowKeys;
	}

	/**
	 * Row keys to set.
	 * @param rowKeys Row keys to set.
	 */
	public void setRowKeys(final Long[] rowKeys) {
		this.rowKeys = rowKeys;
	}
	
	/**
	 * 
	 * @return {@link Integer} recordIndex.
	 */
	public Integer getRecordIndex() {
		return recordIndex;
	}

	/**
	 * 
	 * @param recordIndex.
	 */
	public void setRecordIndex(final Integer recordIndex) {
		this.recordIndex = recordIndex;
	}
	
	/**
	 * 
	 * @return {@link Integer} recordPerPage.
	 */
	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	/**
	 * 
	 * @param recordPerPage.
	 */
	public void setRecordPerPage(final Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
}
