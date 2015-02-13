package com.nokor.frmk.vaadin.ui.widget.table;



/**
 * @author ly.youhort
 *
 */
public class BooleanColumnRenderer extends PropertyColumnRenderer {
	
	private String value;
	
	public BooleanColumnRenderer(String value) {
		this.value = value;
	}
	
	@Override
	public Object getValue() {
		Boolean booleanValue = (Boolean) getPropertyValue();
		if (booleanValue != null) {
			return value;
		}
		return null;
	}
}
