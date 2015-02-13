package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "ts_sec_control")
public class SecControl extends EntityRefA {
    /** */
	private static final long serialVersionUID = -3566768898505180272L;


	private SecPage page;
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_ctl_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_ctl_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_ctl_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_pag_id")
	public SecPage getPage() {
		return page;
	}

	public void setPage(SecPage page) {
		this.page = page;
	}


}
