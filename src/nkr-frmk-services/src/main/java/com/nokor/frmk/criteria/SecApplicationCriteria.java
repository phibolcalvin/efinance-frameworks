package com.nokor.frmk.criteria;

import org.seuksa.frmk.dao.BaseCriteria;

/**
 * @author prasnar
 * @version $revision$
 */
public class SecApplicationCriteria extends BaseCriteria {
    
	private final static String TABLE_NAME = "SecApplication";
	private final static String SORTING_FIELD = "code";

    private Long id;
    private String code;

    /**
     * 
     */
    public SecApplicationCriteria() {
    }
    
    /**
     * @see org.seuksa.frmk.dao.BaseCriteria#getTableName()
     */
    @Override
    protected String getTableName() {
    	return TABLE_NAME;
    }

    /**
     * @see org.seuksa.frmk.dao.BaseCriteria#getSortingFieldName()
     */
    protected String getSortingFieldName() {
    	return SORTING_FIELD;
    }
    
    /**
     * @see org.seuksa.frmk.dao.BaseCriteria#getCommonCriteria()
     */
    @Override
    protected String getCommonCriteria() {
        String criteria = "";

        if (id != null && id > 0) {
            criteria += " and t.id = " + id;
        }
        if (code != null) {
            criteria += " and t.code = '" + code + "'";
        }
        

        return criteria != "" ? " (" + criteria.substring(5) + ") " : "";
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

    

}
