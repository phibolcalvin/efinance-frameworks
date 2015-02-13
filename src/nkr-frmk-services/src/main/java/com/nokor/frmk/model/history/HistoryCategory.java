package com.nokor.frmk.model.history;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Table 
 * @author prasnar
 */
@Entity
@Table(name = "ts_category_history")
public class HistoryCategory extends EntityRefA {
    /** */
	private static final long serialVersionUID = -7173583827581709111L;

	@OneToMany
    @OrderColumn(name="his_pro_desc")
    @JoinColumn(name="cat_his_id", nullable=false)
	private List<HistoryProperty> properties;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cat_his_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "cat_his_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "cat_his_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the properties
	 */
	public List<HistoryProperty> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<HistoryProperty> properties) {
		this.properties = properties;
	}

    
   
}
