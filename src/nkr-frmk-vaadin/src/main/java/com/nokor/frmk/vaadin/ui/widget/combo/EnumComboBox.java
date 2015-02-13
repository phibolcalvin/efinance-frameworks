package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.LinkedHashMap;
import java.util.List;

import org.seuksa.frmk.model.sysref.ISysRefData;

import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 *
 */
public class EnumComboBox<T extends ISysRefData> extends ComboBox {

	private static final long serialVersionUID = 8795061348473343268L;

	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	
	/**
	 * @param values
	 */
	public EnumComboBox(final List<T> values) {
		this(null, values);
	}
	
	/**
	 * @param caption Caption
	 * @param values Combo values
	 */
	public EnumComboBox(final String caption, final List<T> values) {
		super(caption);
		if (values != null  && !values.isEmpty()) {
        	for (T sysRefData : values) {
        		addItem(sysRefData.getCode());
        		setItemCaption(sysRefData.getCode(), sysRefData.getDesc());
        		valueMap.put(sysRefData.getCode(), sysRefData);                
        	}
        }
	}
	
	/**
     * @return
     */
    public T getSelectedEntity() {
        return valueMap.get(getValue());
    }
    
    /**
     * @return
     */
    public void setSelectedEntity(T value) {
    	if (value != null) {
    		setValue(value.getCode());
    	} else {
    		setValue(null);
    	}
    }
       
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, T> getValueMap() {
        return valueMap;
    }
    
    /**
     * @param key
     * @return
     */
    public boolean containsKey(final String key) {
    	return key == null ? false : valueMap.containsKey(key);
    }
    
    /**
     */
    public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }
}
