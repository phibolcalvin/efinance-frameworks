package com.nokor.frmk.model.scheduler;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.MainEntity;

@Entity
@Table(name = "tu_scheduler")
public class Scheduler extends MainEntity {
	
	/**	 */
	private static final long serialVersionUID = 6445235616169146794L;
	
	private String desc;
	private SchedulerTask task;
	private SchedulerTimeUnit timeUnit;
	private Integer day;
	private Integer hours;
	private Integer minutes;
	private String expression;
	private String comment;
	
	 /**
     * 
     * @return
     */
    public static Scheduler createInstance() {
        Scheduler tas = EntityFactory.createInstance(Scheduler.class);
        return tas;
    }
    
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sch_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
	 * @return the desc
	 */
    @Column(name = "sch_desc", nullable = false)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the task
	 */
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "sch_tas_id")
	public SchedulerTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(SchedulerTask task) {
		this.task = task;
	}	

	/**
	 * @return the timeUnit
	 */
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "sch_tim_uni_id")
	public SchedulerTimeUnit getTimeUnit() {
		return timeUnit;
	}

	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(SchedulerTimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	/**
	 * @return the day
	 */
	@Column(name = "sch_day")
	public Integer getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * @return the hours
	 */
	@Column(name = "sch_hours")
	public Integer getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(Integer hours) {
		this.hours = hours;
	}

	/**
	 * @return the minutes
	 */
	@Column(name = "sch_minutes")
	public Integer getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the expression
	 */
	@Column(name = "sch_expression", length = 50)
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	/**
	 * @return the comment
	 */
	@Column(name = "sch_comment", length = 255)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
