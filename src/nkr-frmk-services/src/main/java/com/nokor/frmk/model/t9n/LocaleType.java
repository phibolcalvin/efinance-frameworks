package com.nokor.frmk.model.t9n;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.LocaleUtils;
import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Table LocaleType
 * @author prasnar
 */
@Entity
@Table(name = "ts_type_locale")
public class LocaleType extends EntityRefA {
	/** */
	private static final long serialVersionUID = 1930980500438568600L;


	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "typ_loc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "typ_loc_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "typ_loc_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    /**
     * 
     * @return
     */
    @Transient
    public Locale getLocale() {
        return LocaleUtils.toLocale(getCode());
    }    
   
}
