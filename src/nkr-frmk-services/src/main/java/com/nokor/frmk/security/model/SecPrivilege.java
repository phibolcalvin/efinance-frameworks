package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "ts_sec_privilege")
public class SecPrivilege extends EntityRefA {
    /** */
	private static final long serialVersionUID = -895225666964109617L;

	public static final Long READ_ID = 1L;
	public static final Long WRITE_ID = 2L;
	public static final Long EXECUTE_ID = 3L;
	public static final Long VISIBLE_ID = 4L;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_pri_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_pri_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_pri_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }


}
