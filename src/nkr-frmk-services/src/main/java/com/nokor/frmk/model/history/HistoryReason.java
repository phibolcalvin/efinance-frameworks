package com.nokor.frmk.model.history;

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
@Table(name = "ts_reason_history")
public class HistoryReason extends EntityRefA {
    /** */
	private static final long serialVersionUID = -3054939991917876460L;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rea_his_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "rea_his_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "rea_his_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    
   
}
