package com.nokor.frmk.ui.menu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name = "ts_type_menu")
public class TypeMenu extends EntityRefA {
	/** */
	private static final long serialVersionUID = 7297313286206459830L;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typ_mnu_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "typ_mnu_code", nullable = false, length = 50)
	@Override
	public String getCode() {
		return code;
	}


	@Column(name = "typ_mnu_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
    
}
