package org.seuksa.frmk.model.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.model.sysref.StatusRecord;

/**
 * This class is table reference which used an autoincrement ID as Id (primary key)
 * 
 * @author prasnar
 * @version $Revision$
 */
@MappedSuperclass
public abstract class EntityRefA extends EntityA {

    /** */
    private static final long serialVersionUID = -8839423010654857579L;

    public static final String CODE_FIELD = "code";
    public static final String DESC_FIELD = "desc";
    public static final String SORTKEY_FIELD = "sortIndex";

    protected String code;
    protected String desc;
    protected String descEn;
    private Integer sortIndex;

    
    
    /**
     * 
     * @param clazz
     * @param code
     * @return
     * @throws Exception
     */
    public static EntityRefA newInstance(final Class<? extends EntityRefA> clazz) {
		try {
			EntityRefA entity = clazz.newInstance();
	        entity.setStatusRecord(StatusRecord.ACTIV);
	        entity.fillSysBlock("admin");
	        return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
    }

    /**
     * @return the code
     */
    @Transient
    public String getCode() {
    	return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     */
    @Transient
    public String getDesc() {
    	return desc;
    }

    
    @Transient
    public String getDescEn() {
    	return descEn;
    }
    
    /**
     * @param desc the desc to set
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    /**
     * @param desc the descEn to set
     */
    public void setDescEn(final String descEn) {
        this.descEn = descEn;
    }

    /**
     * @return SortIndex
     */
    @Column(name = "sort_index")
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param SortIndex
     */
    public void setSortIndex(final Integer SortIndex) {
        this.sortIndex = SortIndex;
    }

}
