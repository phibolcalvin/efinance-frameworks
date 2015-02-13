package com.nokor.frmk.security.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "ts_sec_page")
public class SecPage extends EntityRefA {
    /** */
	private static final long serialVersionUID = 3651181820551617050L;

	private List<SecControl> controls;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_pag_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_pag_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_pag_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @return the controls
	 */
	@OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
	public List<SecControl> getControls() {
		return controls;
	}

	/**
	 * @param controls the controls to set
	 */
	public void setControls(List<SecControl> controls) {
		this.controls = controls;
	}

	
}
