package org.seuksa.frmk.dao;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author prasnar
 * @version $Revision$
 */
public abstract class BaseCriteria {
    protected static Logger logger = Logger.getLogger(BaseCriteria.class.getName());
    private int indexFirstRow = 0; 
    private int maxResult = 0;
    protected String sortCriteria = null;

    private final static String TABLE_TOKEN = "@T";
    private final static String TABLE_ALIAS_TOKEN = "@TA";
    private final static String SORTING_FIELD_TOKEN = "@FS";

    protected final static String DEFAULT_TABLE_ALIAS = "t";
    private final static String SQL_COUNT = "select count(id) from " + TABLE_TOKEN + " " + TABLE_ALIAS_TOKEN;
    protected final static String SQL_SELECT = "from " + TABLE_TOKEN + " " + TABLE_ALIAS_TOKEN;
    private final static String SQL_DELETE = "delete from " + TABLE_TOKEN + " " + TABLE_ALIAS_TOKEN;
    private final static String DEFAULT_SORT = TABLE_ALIAS_TOKEN + "." + SORTING_FIELD_TOKEN + " asc";

    protected Long id;
    protected List<Long> ids;

    
    
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
     * @return the maxResult
     */
    public int getMaxResult() {
        return maxResult;
    }

    /**
     * @param maxResult the maxResult to set
     */
    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }
    
    /**
     * 
     * @return
     */
    protected abstract String getCommonCriteria();
    protected abstract String getTableName();
    protected abstract String getSortingFieldName();
    
    /**
     * 
     * @return
     */
    protected String getTableAliasName() {
    	return DEFAULT_TABLE_ALIAS;
    }
    
    /**
     * 
     * @return
     */
    protected String getSqlSelect() {
    	return SQL_SELECT.replaceFirst(TABLE_TOKEN, getTableName()).replaceFirst(TABLE_ALIAS_TOKEN, getTableAliasName());
    }
    
    /**
     * 
     * @return
     */
    protected String getSqlDelete() {
    	return SQL_DELETE.replaceFirst(TABLE_TOKEN, getTableName()).replaceFirst(TABLE_ALIAS_TOKEN, getTableAliasName());
    }
    
    /**
     * 
     * @return
     */
    protected String getSqlCount() {
    	return SQL_COUNT.replaceFirst(TABLE_TOKEN, getTableName()).replaceFirst(TABLE_ALIAS_TOKEN, getTableAliasName());
    }
    
    /**
     * 
     * @return
     */
    protected String getSqlUpdate() {
    	throw new NotImplementedException("Not yet done.");
    }
    
    protected String getDefaultSort() {
		return DEFAULT_SORT.replaceFirst(SORTING_FIELD_TOKEN, getSortingFieldName()).replaceFirst(TABLE_ALIAS_TOKEN, getTableAliasName());
	}
    
    /**
     * 
     * @return
     */
    public String queryList() {
    	String hql = getSqlSelect();

        String commonCrit = getCommonCriteria();
        if (ids != null) {
        	commonCrit += getIdsCriteria();
        }
        if (StringUtils.isNotEmpty(commonCrit)) {
            hql += " where " + commonCrit;
        }
        if (StringUtils.isEmpty(sortCriteria)) {
            sortCriteria = getDefaultSort();
        }
        if (StringUtils.isEmpty(sortCriteria)) {
        	hql += " order by " + sortCriteria;
        }

        logger.debug(hql);

        return hql;
    }



    /**
     * 
     */
    public String queryCount() {
        String hql = getSqlCount();

        String commonCrit = getCommonCriteria();
        if (StringUtils.isNotEmpty(commonCrit)){
            hql += " where " + commonCrit;
        }
        logger.debug(hql);

        return hql;
    }

    /**
     * 
     * @return
     */
    public String queryDelete() {
        String hql = getSqlDelete();

        String commonCrit = getCommonCriteria();
        if (StringUtils.isNotEmpty(commonCrit)) {
            hql += " where " + commonCrit;
        }

        logger.debug(hql);

        return hql;
    }
    
    /**
     * 
     * @return
     */
    public String queryUpdate() {
    	String hql = getSqlUpdate();

        String commonCrit = getCommonCriteria();
        if (StringUtils.isNotEmpty(commonCrit)) {
            hql += " where " + commonCrit;
        }

        logger.debug(hql);

        return hql;
    }
    
    /**
     * @return the indexFirstRow
     */
    public int getIndexFirstRow() {
        return indexFirstRow;
    }

    /**
     * @param indexFirstRow the indexFirstRow to set
     */
    public void setIndexFirstRow(int indexFirstRow) {
        this.indexFirstRow = indexFirstRow;
    }
    
    /**
     * 
     * @return
     */
    public String getIdsCriteria() {
    	String criteria = "";
    	if (ids != null) {
        	String idsStr = "";
        	for (Long id : ids) {
        		idsStr += id + ",";
        	}
        	if (idsStr != null) {
        		 if (idsStr.indexOf(",") > -1) {
        			 idsStr = idsStr.substring(0, idsStr.length()-1);
        		 }
        		 criteria += " and a.id in " + "(" + idsStr + ")";
        	}
        }
    	return criteria;
    }
}
