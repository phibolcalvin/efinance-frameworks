package com.nokor.frmk.model.metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Table 
 * @author prasnar
 */
@Entity
@Table(name = "ts_meta_type")
public class MetaType extends EntityRefA {
    /** */
	private static final long serialVersionUID = -4487645761800787579L;


	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "met_typ_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "met_typ_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "met_typ_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    
   
}
