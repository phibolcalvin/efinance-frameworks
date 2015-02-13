package com.nokor.frmk.model.history;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.seuksa.frmk.model.entity.EntityA;



/**
 * Table 
 * @author prasnar
 */
@Entity
@Table(name = "td_history")
public class History extends EntityA {
    /** */
	private static final long serialVersionUID = 7141829633776417195L;

	private String identifier;
	private Long property;
	private Long reason;
	private String valueBefore;
	private String valueAfter;
	private String comment;

	private History parent;
	private List<History> children;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "his_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the identifier
	 */
    @Column(name = "his_identifier", nullable = false)
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the property
	 */
    @Column(name = "his_pro_id", nullable = false)
	public Long getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Long property) {
		this.property = property;
	}

	/**
	 * @return the historyReason
	 */
    @Column(name = "rea_his_id", nullable = false)
	public Long getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(Long reason) {
		this.reason = reason;
	}

	/**
	 * @return the valueBefore
	 */
    @Column(name = "his_value_before", nullable = false)
	public String getValueBefore() {
		return valueBefore;
	}

	/**
	 * @param valueBefore the valueBefore to set
	 */
	public void setValueBefore(String valueBefore) {
		this.valueBefore = valueBefore;
	}

	/**
	 * @return the valueAfter
	 */
    @Column(name = "his_value_after", nullable = false)
	public String getValueAfter() {
		return valueAfter;
	}

	/**
	 * @param valueAfter the valueAfter to set
	 */
	public void setValueAfter(String valueAfter) {
		this.valueAfter = valueAfter;
	}

	/**
	 * @return the comment
	 */
    @Column(name = "his_comment", nullable = false)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne
    @JoinColumn(name="parent_his_id", nullable = true)
	public History getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(History parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="his_id")
	@OrderColumn(name="his_identifier")
    @OnDelete(action=OnDeleteAction.NO_ACTION)
	public List<History> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<History> children) {
		this.children = children;
	}
    
    

    
   
}
