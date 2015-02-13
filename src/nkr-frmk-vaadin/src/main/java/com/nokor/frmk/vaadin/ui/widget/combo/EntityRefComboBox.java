package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.service.EntityService;

import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class EntityRefComboBox<T extends EntityRefA> extends ComboBox {
	
	private static final long serialVersionUID = 228046564757710688L;
	
	private EntityService entityService; 
	/** Base criteria */
	private BaseRestrictions<T> restrictions;
	
	private boolean onlyActive;
	
	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	 
	public EntityRefComboBox() {
		this(null);
    }
	
    /**
     * @param values
     */
    public EntityRefComboBox(final String caption) {
        this(caption, false);
    }
    
    /**
     * @param values
     */
    public EntityRefComboBox(final String caption, boolean onlyActive) {
        super(caption);
        this.onlyActive = onlyActive;
        entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
    }
    
    
    /**
	 * @return the restrictions
	 */
	public BaseRestrictions<T> getRestrictions() {
		return restrictions;
	}

	/**
	 * @param restrictions the restrictions to set
	 */
	public void setRestrictions(BaseRestrictions<T> restrictions) {
		this.restrictions = restrictions;
		if (onlyActive) {
			this.restrictions.addCriterion("statusRecord", FilterMode.EQUALS, StatusRecord.ACTIV);
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
     * Renderer list value
     */
    public void renderer() {
    	valueMap.clear();
    	removeAllItems();
    	if (restrictions.getOrders() == null || restrictions.getOrders().isEmpty()) {
    		restrictions.addOrder(Order.asc("descEn"));
    	}
		List<T> entities = entityService.list(restrictions);
    	for (T refEntity : entities) {
    		addItem(refEntity.getId().toString());
    		setItemCaption(refEntity.getId().toString(), refEntity.getDescEn());
    		valueMap.put(refEntity.getId().toString(), refEntity);                
    	}
    	setValue(null);
    }
    
    /**
     */
    public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }
}
