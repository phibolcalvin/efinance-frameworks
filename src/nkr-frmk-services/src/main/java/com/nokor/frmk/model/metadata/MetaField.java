package com.nokor.frmk.model.metadata;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Table 
 * @author prasnar
 */
@Entity
@Table(name = "ts_meta_field")
public class MetaField extends EntityRefA {
	/** */
	private static final long serialVersionUID = -2952340420756926690L;

	private MetaTable table;
	private MetaType type;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "met_fie_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "met_fie_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "met_fie_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the table
	 */
    @OneToOne
    @JoinColumn(name = "met_tab_id")
	public MetaTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(MetaTable table) {
		this.table = table;
	}

	/**
	 * @return the type
	 */
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="met_typ_id")
	public MetaType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MetaType type) {
		this.type = type;
	}

    
   
}
