package com.nokor.frmk.model.scheduler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name = "ts_scheduler_task")
public class SchedulerTask extends EntityRefA {
	
	/**	 */
	private static final long serialVersionUID = 5685799514292933250L;
	
	private String className;
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sch_tas_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sch_tas_code", nullable = true, length = 10)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sch_tas_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }

    /**
	 * @return the className
	 */
    @Column(name = "sch_tas_classname", nullable = false)
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
}
