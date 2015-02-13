package com.nokor.frmk.vaadin.ui.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 *
 */
public abstract class AbstractControlPanel extends VerticalLayout {

	private static final long serialVersionUID = 5503932281636575767L;
	
	protected final Log logger = LogFactory.getLog(AbstractControlPanel.class);
	protected List<String> errors = new ArrayList<String>();


    /**
     * Sets component error to field group layout
     *
     * @param localizedMessage - localized message
     */
    public void setComponentError(String localizedMessage) {
        setComponentError(new UserError(localizedMessage));
    }
    
    /**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMandatorySelectField(AbstractSelect field, String messageKey) {
		boolean isValid = true;
		if (field.getValue() == null) {
			errors.add(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMandatoryField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isEmpty(field.getValue())) {
			errors.add(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMandatoryDateField(DateField field, String messageKey) {
		boolean isValid = true;
		if (field.getValue() == null) {
			errors.add(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	protected boolean checkRangeDateField(DateField start, DateField end) {
		boolean isValid = true;
		if (start.getValue() != null && end.getValue() != null &&
				end.getValue().before(start.getValue())) {
			errors.add(I18N.message("field.range.date.incorrect"));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkDoubleField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Double.parseDouble(field.getValue());
			} catch (NumberFormatException e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkIntegerField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Integer.parseInt(field.getValue());
			} catch (NumberFormatException e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Double getDouble(AbstractTextField field, double defaultValue) {
		try {
			return Double.parseDouble(field.getValue());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Double getDouble(AbstractTextField field) {
		try {
			return Double.parseDouble(field.getValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Integer getInteger(AbstractTextField field, int defaultValue) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Integer getInteger(AbstractTextField field) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Long getLong(AbstractTextField field, long defaultValue) {
		try {
			return Long.parseLong(field.getValue());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Long getLong(AbstractTextField field) {
		try {
			return Long.parseLong(field.getValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(String value) {
		return value == null ? "" : value;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(Integer value) {
		return value == null ? "" : String.valueOf(value);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(Double value) {
		return value == null ? "" : String.valueOf(value);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(Long value) {
		return value == null ? "" : String.valueOf(value);
	}
	
	/**
	 * Reset
	 */
	protected void reset() {
		errors = new ArrayList<String>();
	}
}
