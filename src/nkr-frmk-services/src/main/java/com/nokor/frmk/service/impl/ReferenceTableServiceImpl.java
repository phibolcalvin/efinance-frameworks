package com.nokor.frmk.service.impl;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.frmk.dao.ReferenceTableDao;
import com.nokor.frmk.model.refdata.RefTable;
import com.nokor.frmk.security.SecurityConfigUtil;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.service.ReferenceTableService;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("referenceTableService")
@Transactional
public class ReferenceTableServiceImpl extends BaseEntityServiceImpl implements ReferenceTableService {

    private static String SEP = ";";
    private static Hashtable<Long, List<RefTable>> refTablesCache = new Hashtable<Long, List<RefTable>>();
    private static Hashtable<String, List<EntityRefA>> refValuesCache = new Hashtable<String, List<EntityRefA>>();

    private SecApplication currentSecApplication = null;

    @Autowired
    private ReferenceTableDao refTableDao;

    /**
     * 
     */
    public ReferenceTableServiceImpl() {
    }
    
    @PostConstruct
    public void postConstruct() {
    	
    }

    @Override
    public SecApplication getCurrentSecApplication() {
    	if (currentSecApplication == null) {
    		currentSecApplication = SecApplicationContextHolder.getContext().getSecApplication();
    	}
    	if (currentSecApplication == null) {
    		currentSecApplication = SecurityConfigUtil.getSecApplication();
    	}
    	if (currentSecApplication == null) {
    		throw new IllegalStateException("The current SecApplication must be set.");
    	}
        return currentSecApplication;
    }

    @Override
    public void setCurrentSecApplication(SecApplication currentSecApplication) {
        this.currentSecApplication = currentSecApplication;
    }

    /**
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
     */
    @Override
    public BaseEntityDao getDao() {
        return refTableDao;
    }

    /**
     * @return the tableRefDao
     */
    protected ReferenceTableDao getTableRefDao() {
        return refTableDao;
    }

    @Override
    public EntityRefA createInstance(String tablename) {
        return null;
    }

    @Override
    public List<RefTable> getTables() {
        return getTablesByAppId(getCurrentSecApplication().getId());
    }

    @Override
    public List<RefTable> getTablesByAppId(Long secAppId) {
        return getTablesByAppId(secAppId, false);
    }

    @Override
    public List<RefTable> getTablesByAppId(Long secAppId, boolean refreshCache) {
        if (secAppId == null || secAppId <= 0) {
            secAppId = getCurrentSecApplication().getId();
            logger.warn("The CurrentSecApplication is taken.");
        }
        if (refreshCache) {
            flushCache();
        }
        List<RefTable> tableRefList = refTablesCache.get(secAppId);
        if (tableRefList == null) {
            tableRefList = getTableRefDao().getTables(secAppId);
            if (tableRefList != null) {
                refTablesCache.put(secAppId, tableRefList);
            }
        }

        return tableRefList;
    }

    @Override
    public RefTable getTable(Long secAppId, String tablename) {
        List<RefTable> tableRefList = getTablesByAppId(secAppId);
        if (tableRefList != null) {
            for (RefTable table : tableRefList) {
                if (StringUtils.isEmpty(table.getCode()) || table.getCode().equals(tablename)) {
                    return table;
                }
            }
        }

        return null;
    }

    @Override
    public <T extends EntityRefA> RefTable getTable(Class<T> entityClass) {
        return getTable(getCurrentSecApplication().getId(), entityClass);
    }

    @Override
    public <T extends EntityRefA> RefTable getTable(Long secAppId, Class<T> entityClass) {
        List<RefTable> tableRefList = getTablesByAppId(secAppId);
        if (tableRefList != null) {
            for (RefTable table : tableRefList) {
                if (table.getCanonicalClassName().equals(entityClass.getCanonicalName())) {
                    return table;
                }
            }
        }

        return null;
    }

    @Override
    public RefTable getTable(String tablename) {
        return getTable(getCurrentSecApplication().getId(), tablename);
    }

    @Override
    public <T extends EntityRefA> T getValueById(Class<T> entityClass, Long id) {
        List<T> lstEntities = getValues(entityClass);
        if (lstEntities != null) {
            for (T entity : lstEntities) {
                if (entity.getId().equals(id)) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends EntityRefA> T getValueByCode(Class<T> entityClass, String code) {
        List<T> lstEntities = getValues(entityClass);
        if (lstEntities != null) {
            for (T entity : lstEntities) {
                if (entity.getCode().equals(code)) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends EntityRefA> List<T> getValuesActive(Class<T> entityClass) {
        return getValues(entityClass, StatusRecord.ACTIV);
    }
    
    @Override
    public <T extends EntityRefA> List<T> getValuesActive(Class<T> entityClass, Order order) {
        return getValues(entityClass, order, StatusRecord.ACTIV);
    }

    @Override
    public <T extends EntityRefA> List<T> getValuesRecycled(Class<T> entityClass) {
        return getValues(entityClass, StatusRecord.RECYC);
    }
    
    @Override
    public <T extends EntityRefA> List<T> getValuesRecycled(Class<T> entityClass, Order order) {
        return getValues(entityClass, order, StatusRecord.RECYC);
    }
    
    @Override
	public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, StatusRecord statusRecord) {
		return getValues(entityClass, (Order) null, statusRecord);
	}
    
    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, Order order, StatusRecord statusRecord) {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.setStatusRecord(statusRecord);
    	restriction.setOrder(order);
        return getValues(restriction, false);
    }
    
    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass)  {
        return getValues(entityClass, (Order) null);
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, Order order) {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.setOrder(order);
        return getValues(restriction, false);
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass, boolean refreshCache) {
        return getValues(new BaseRestrictions(entityClass), refreshCache);
    }

	@Override
    public <T extends EntityRefA> List<T> getValues(BaseRestrictions<T> restriction, boolean refreshCache) {
        if (restriction == null) {
            throw new IllegalArgumentException("restriction can not be null.");
        }
        if (restriction.getOrder() == null) {
        	restriction.setOrder(getOrder(restriction.getEntityClass()));
        }

        String key = restriction.getEntityClass().getCanonicalName() + SEP + restriction.getOrder().toString();
        List<T> tableRefValueList = (List<T>) refValuesCache.get(key);
        if (tableRefValueList == null || tableRefValueList.size() == 0 || refreshCache) {
            try {
                tableRefValueList = getTableRefDao().getValues(restriction);
            }
            catch (DaoException e) {
                logger.errorStackTrace(e);
            }
            if (tableRefValueList != null) {
                refValuesCache.put(key, (List<EntityRefA>) tableRefValueList);
            }
        }
        return tableRefValueList;
    }

    //	@Override
    //	public List<EntityRefA> getValuesByTable(String tablename) {
    //		return getValuesByTable(tablename, false);
    //	}
    //
    //	@Override
    //	public List<EntityRefA> getValuesByTable(String tablename, boolean refreshCache) {
    //		Class<? extends EntityRefA> entityClass = null;
    //		String classname = getTable(tablename).getCanonicalClassName();
    //		try {
    //			entityClass = ClassUtils.getClass(classname);
    //		} catch (ClassNotFoundException e) {
    //            logger.error("The class [" + tablename + "] is not initialized as a reference table.");
    //			logger.errorStackTrace(e);
    //		}
    //		
    //        if (entityClass == null) {
    //            logger.error("The table [" + tablename + "] is not initialized as a reference table.");
    //            return null;
    //        }
    //        return getValues(entityClass, refreshCache);
    //	}

    /**
     * 
     * @param entityClassname
     * @return
     * @
     */
    private <T extends EntityRefA> Order getOrder(Class<T> entityClass) {
        RefTable entityRef = getTable(entityClass);
        if (entityRef != null && Boolean.TRUE.equals(entityRef.getUseSortIndex())) {
            return Order.asc(EntityRefA.SORTKEY_FIELD);
        }

        return Order.asc(EntityRefA.DESC_FIELD);
    }

    /**
     * 
     * @param secAppId
     * @param entityClassname
     * @return
     * @
     */
    //	private <T extends EntityRefA> RefTable getTableByAppId(Long secAppId, Class<T> entityClass) {
    //        List<RefTable> tableRefList = getTablesByAppId(secAppId);
    //		if (tableRefList != null) {
    //	        for (RefTable table : tableRefList) {
    //	            if (table.getCanonicalClassName().equals(entityClass.getCanonicalName())) {
    //	                return table;
    //	            }
    //	        }
    //		}
    //
    //        return null;
    //    }

    @Override
    public <T extends EntityRefA> String getDescValue(Class<T> entityClass, Long entityId) {
        List<T> lstValues = getValues(entityClass);
        if (lstValues != null) {
            for (EntityRefA entity : lstValues) {
                if (entity.getId().equals(entityId)) {
                    return entity.getDesc();
                }
            }
        }

        return null;
    }

    /**
     * 
     * @param secAppId
     * @param canonicalClassName
     * @return
     * @
     */
    private RefTable getTableByClassname(Long secAppId, String canonicalClassName) {
        List<RefTable> tableRefList = getTablesByAppId(secAppId);
        if (tableRefList != null) {
            for (RefTable table : tableRefList) {
                if (table.getCanonicalClassName().equals(canonicalClassName)) {
                    return table;
                }
            }
        }

        return null;
    }

    @Override
    public <T extends EntityRefA> String generateNextSequenceCode(Class<T> entityClass) {
        return generateNextSequenceCode(entityClass.getSimpleName());
    }

    @Override
    public String generateNextSequenceCode(String entityName) {
        RefTable entityRef = getTableByClassname(getCurrentSecApplication().getId(), entityName);
        if (Boolean.TRUE.equals(entityRef.getGenerateCode())) {
            return getTableRefDao().generateNextSequenceCode(entityName);
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(EntityRefA entityRef) {
        super.saveOrUpdate(entityRef);
        flushCache();
        return true;
    }

    @Override
    public boolean delete(EntityRefA entityRef) {
        super.delete(entityRef);
        flushCache();
        return true;
    }

    @Override
    public boolean saveOrUpdate(RefTable tableRef) {
        super.saveOrUpdate(tableRef);
        flushCache();
        return true;
    }

    @Override
    public boolean delete(RefTable tableRef) {
        super.delete(tableRef);
        flushCache();
        return true;
    }

    @Override
    public <T extends EntityRefA> boolean recycled(Class<T> entityClass, Long id) {
        EntityRefA entityRefA = getDao().getById(entityClass, id);
        entityRefA.setStatusRecord(StatusRecord.RECYC);
        super.saveOrUpdate(entityRefA);
        flushCache();
        return true;
    }

    @Override
    public void flushCache() {
        refTablesCache.clear();
        refValuesCache.clear();

    }

	


}