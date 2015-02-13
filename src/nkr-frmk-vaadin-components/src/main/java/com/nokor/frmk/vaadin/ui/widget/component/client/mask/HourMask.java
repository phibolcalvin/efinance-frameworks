package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class HourMask extends AbstractConcateMask {
	
	public boolean isValid(char character, String concate) {
		if (!Character.isDigit(character)) {
			return false;
		}
		
		try {
			int i = Integer.parseInt(concate);
			if (i > 23) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}	
		return true;
	}
}
