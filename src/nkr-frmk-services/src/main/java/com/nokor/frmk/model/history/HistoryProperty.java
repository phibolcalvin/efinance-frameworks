package com.nokor.frmk.model.history;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Table 
 * @author prasnar
 */
@Entity
@Table(name = "ts_histo_property")
public class HistoryProperty extends EntityRefA {
	/** */
	private static final long serialVersionUID = 1742232234504687329L;
	
	private String className;
	
	@ManyToOne
    @JoinColumn(name="his_cat_id", insertable=false, updatable=false, nullable=false)
	private HistoryCategory category;
	
	private HistoryProperty parent;
	private List<HistoryProperty> children;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "his_pro_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "his_pro_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "his_pro_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    /**
	 * @return the className
	 */
    @Column(name = "his_pro_classname", nullable = false)
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	
	
	/**
	 * @return the category
	 */
	public HistoryCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(HistoryCategory category) {
		this.category = category;
	}

	/**
	 * @return the parent
	 */
	@ManyToOne
    @JoinColumn(name="parent_his_pro_id", nullable=true)
	public HistoryProperty getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(HistoryProperty parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="his_pro_id")
	@OrderColumn(name="his_pro_desc")
    @OnDelete(action=OnDeleteAction.NO_ACTION)
	public List<HistoryProperty> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<HistoryProperty> children) {
		this.children = children;
	}

	
   
}
