package com.nokor.frmk.model.scheduler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Time Unit
 * @author kong.phirun
 */
@Entity
@Table(name = "ts_scheduler_time_unit")
public class SchedulerTimeUnit extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 1425103647344691583L;
	
	public static long DAILY = 1;
    public static long WEEKLY = 2;
    public static long MONTHLY = 3;
    public static long IN_MINUTES = 4;
    public static long IN_HOURS = 5;

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sch_tim_uni_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sch_tim_uni_code", nullable = true, length = 10)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sch_tim_uni_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
    
}
