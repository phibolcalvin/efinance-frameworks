package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class DataRefComboBox<T extends EntityRefA> extends ComboBox {
	
	private static final long serialVersionUID = 228046564757710688L;
		
	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	 
	public DataRefComboBox(List<T> values) {
		this(null, values);
    }
	
    /**
     * @param values
     */
    public DataRefComboBox(final String caption, List<T> values) {
        super(caption);
        if (values != null  && !values.isEmpty()) {
        	for (T refEntity : values) {
        		addItem(refEntity.getId().toString());
        		setItemCaption(refEntity.getId().toString(), refEntity.getDescEn());
        		valueMap.put(refEntity.getId().toString(), refEntity);                
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
    		setValue(value.getId().toString());
    	} else {
    		setValue(null);
    	}
    }
    
    /**
     * @param code
     */
    public void setValueAsCode(String code) {
    	if (valueMap != null) {
    		for (Iterator<String> iter = valueMap.keySet().iterator(); iter.hasNext(); ) {
    			T entity = valueMap.get(iter.next());
    			if (entity.getCode() != null && entity.getCode().equals(code)) {
    				setValue(entity.getId().toString());
    				break;
    			}
    		}
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
