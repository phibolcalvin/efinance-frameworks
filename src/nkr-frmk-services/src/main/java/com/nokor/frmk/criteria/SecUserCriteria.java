package com.nokor.frmk.criteria;

import org.seuksa.frmk.dao.BaseCriteria;

/**
 * @author prasnar
 * @version $revision$
 */
public class SecUserCriteria extends BaseCriteria {
    
	private final static String TABLE_NAME = "SecUser";
	private final static String SORTING_FIELD = "login";

	private Long id;
    private String login;

    /**
     * 
     */
    public SecUserCriteria() {
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
        if (login != null) {
            criteria += " and t.login = '" + login + "'";
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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}


}
