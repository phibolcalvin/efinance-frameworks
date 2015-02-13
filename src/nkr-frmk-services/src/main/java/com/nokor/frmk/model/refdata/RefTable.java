package com.nokor.frmk.model.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Table Reference model
 * @author kong.phirun
 */
@Entity
@Table(name = "ts_table_ref")
public class RefTable extends EntityRefA {

    /**   */
    private static final long serialVersionUID = -5106203923247044844L;

    private Long secAppId;
    private String canonicalClassName;
    private Boolean readOnly;
    private Boolean useSortIndex;
    private Boolean generateCode;
    

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tab_ref_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "tab_ref_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "tab_ref_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    

    /**
	 * @return the secAppId
	 */
    @Column(name = "sec_app_id", nullable = true)
	public Long getSecAppId() {
		return secAppId;
	}

	/**
	 * @param secAppId the appId to set
	 */
	public void setSecAppId(Long secAppId) {
		this.secAppId = secAppId;
	}

	/**
     * @return canonicalClassName
     */
    @Column(name = "tab_ref_entity_name", nullable = false)
    public String getCanonicalClassName() {
        return canonicalClassName;
    }

    /**
     * 
     * @param canonicalClassName
     */
    public void setCanonicalClassName(final String canonicalClassName) {
        this.canonicalClassName = canonicalClassName;
    }


    /**
     * @return readOnly
     */
    @Column(name = "tab_ref_readonly")
    public Boolean getReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly
     */
    public void setReadOnly(final Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return useSortIndex
     */
    @Column(name = "tab_ref_use_sort_index")
    public Boolean getUseSortIndex() {
        return useSortIndex;
    }

    /**
     * @param useSortIndex
     */
    public void setUseSortIndex(final Boolean useSortIndex) {
        this.useSortIndex = useSortIndex;
    }

	/**
	 * @return the generateCode
	 */
    @Column(name = "tab_ref_generate_code")
	public Boolean getGenerateCode() {
		return generateCode;
	}

	/**
	 * @param generateCode the generateCode to set
	 */
	public void setGenerateCode(Boolean generateCode) {
		this.generateCode = generateCode;
	}

   
}
