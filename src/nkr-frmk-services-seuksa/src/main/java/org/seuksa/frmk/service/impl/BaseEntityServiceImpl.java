package org.seuksa.frmk.service.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.type.Type;
import org.seuksa.frmk.dao.BaseCriteria;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.Association;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.impl.EntityDaoImpl;
import org.seuksa.frmk.dao.vo.SysTable;
import org.seuksa.frmk.model.entity.BasePK;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.IllegalRecycledBinException;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.seuksa.frmk.tools.log.Log;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author prasnar
 * @version $Revision$
 */
@Transactional
public abstract class BaseEntityServiceImpl implements BaseEntityService {

    protected static Log logger = Log.getInstance(BaseEntityServiceImpl.class);

    /**
     * 
     */
    public BaseEntityServiceImpl() {
        //logger.info("Instantiate BaseEntityServiceImpl");
    }

    public abstract BaseEntityDao getDao();

    /**
     * @see org.seuksa.frmk.service.EntityService#getSessionFactory()
     */
    public SessionFactory getSessionFactory() {
        return getDao().getSessionFactory();
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getUserName()
     */
    public String getUserName() throws DaoException {
        try {
            return getDao().getUserName();
        }
        catch (final SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getDatabaseName()
     */
    public String getDatabaseName() throws DaoException {
        try {
            return getDao().getDatabaseName();
        }
        catch (final SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getDriverName()
     */
    public String getDriverName() throws DaoException {
        try {
            return getDao().getDriverName();
        }
        catch (final SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getAllTablesName()
     */
    public List<SysTable> getAllTablesName() throws DaoException {
        return getDao().getAllTablesName();
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#executeSQLNativeQuery(java.lang.String)
     */
    public List<NativeRow> executeSQLNativeQuery(final String sql) throws NativeQueryException {
        return getDao().executeSQLNativeQuery(sql);
    }
    
    /**_______________________________________________________________________________________
     * 
     * BLOCK [IBaseHibernateService PYI]
     * _______________________________________________________________________________________
     */

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#getNamedQuery(java.lang.String)
     */
    @Override
    public Query getNamedQuery(String queryName) {
        return getDao().getNamedQuery(queryName);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#create(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> void create(T entity) throws DaoException {
    	if (entity instanceof EntityA) {
			((EntityA) entity).fillSysBlock();
		}
    	getDao().create(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#update(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> void update(T entity) throws DaoException {
    	if (entity instanceof EntityA) {
			((EntityA) entity).fillSysBlock();
		}
    	getDao().update(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#delete(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> void delete(T entity) throws DaoException {
        getDao().delete(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#delete(org.seuksa.frmk.dao.BaseCriteria)
     */
    @Override
    @Transactional(readOnly = false)
    public int delete(BaseCriteria criteria) throws DaoException {
        logger.debug("Deleting with criteria: \n" + criteria);
        final int res = getDao().delete(criteria);
        return res;
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#delete(java.lang.Class, Serializable)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> void delete(Class<T> entityClass, Serializable id) throws DaoException {
        getDao().delete(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#merge(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> T merge(T entity) throws DaoException {
    	if (entity instanceof EntityA) {
			((EntityA) entity).fillSysBlock();
		}
    	return getDao().merge(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getFirst(org.seuksa.frmk.dao.BaseCriteria)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getFirst(BaseCriteria criteria) throws DaoException {
        return getDao().getFirst(criteria);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#count(org.seuksa.frmk.dao.BaseCriteria)
     */
    @Override
    @Transactional(readOnly = true)
    public long count(BaseCriteria criteria) throws DaoException {
        return getDao().count(criteria);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#refresh(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void refresh(T entity) throws DaoException {
        getDao().refresh(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#flush()
     */
    @Override
    public void flush() throws DaoException {
        getDao().flush();
    }
    
    /**
     * @see org.seuksa.frmk.service.EntityService#close()
     */
    @Override
    public void close() throws DaoException {
        getDao().close();
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getByCode(Class<T> entityClass, String code) {
        return getDao().getByCode(entityClass, code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getByDesc(Class<T> entityClass, String desc) {
        return getDao().getByDesc(entityClass, desc);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getByField(Class<T> entityClass, String fieldName, Object value) {
        return getDao().getByField(entityClass, fieldName, value);
    }

    @Override
    public <T extends Entity> T getByIdIfNotExistError(Class<T> entityClass, Serializable id) throws DaoException {
        return getDao().getByIdIfNotExistError(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, org.seuksa.frmk.model.entity.BasePK)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getById(Class<T> entityClass, BasePK pk) throws DaoException {
        return getDao().getById(entityClass, pk);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, int)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getById(Class<T> entityClass, int id) throws DaoException {
        return getDao().getById(entityClass, Long.valueOf(id));
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getById(Class<T> entityClass, Long id) throws DaoException {
        return getDao().getById(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getById(Class<T> entityClass, String id) throws DaoException {
        return getDao().getById(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#getById(java.lang.Class, java.io.Serializable)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getById(Class<T> entityClass, Serializable id) throws DaoException {
        return getDao().getById(entityClass, id);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#saveOrUpdate(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> void saveOrUpdate(T entity) throws DaoException {
    	if (entity instanceof EntityA) {
			((EntityA) entity).fillSysBlock();
		}
    	getDao().saveOrUpdate(entity);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#saveOrUpdateBulk(java.util.List)
     */
    @Override
    @Transactional(readOnly = false)
    public <T extends Entity> List<Exception> saveOrUpdateBulk(List<T> entityLst) throws DaoException {
        return saveOrUpdateList(entityLst);
    }

    @Override
    @Transactional(readOnly = false)
    public <T> List<Exception> saveOrUpdateList(List<T> entityLst) throws DaoException {
        try {
            final List<Exception> lstExceptions = getDao().saveOrUpdateList(entityLst);

            int nbErrors = 0;
            for (final Exception ex : lstExceptions) {
                if (ex != null) {
                    nbErrors++;
                }
            }

            String stepEnd = null;
            try {
                if (nbErrors > EntityDaoImpl.NB_MAX_ERRORS) {
                    stepEnd = "Rolling back";
                    throw new DaoException("Too many errors");
                }
                else {
                    stepEnd = "Committing";
                }
            }
            catch (final Exception e) {
                final String errMsg = "Error while [" + stepEnd + "]";
                logger.errorStackTrace(e);
                throw new DaoException(errMsg, e);
            }
            finally {
                logger.debug("----end createOrUpdateBulk------ nb errors:" + nbErrors);
            }
            return lstExceptions;
        }
        catch (final Exception e) {
            logger.errorStackTrace(e);
            throw new DaoException(e);
        }

    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(org.seuksa.frmk.dao.BaseCriteria)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(BaseCriteria criteria) throws DaoException {
        return getDao().list(criteria);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass) throws DaoException {
        return getDao().list(entityClass);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, org.hibernate.criterion.Order)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, Order order) throws DaoException {
        return list(entityClass, (Criterion) null, order);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> listByIds(Class<T> entityClass, List<Long> idList) {
        return getDao().listByIds(entityClass, idList);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(org.seuksa.frmk.dao.hql.BaseRestrictions)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(BaseRestrictions<T> restrictions) throws DaoException {
        return getDao().list(restrictions);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> T getFirst(BaseRestrictions<T> restrictions) throws DaoException {
        restrictions.setMaxResults(1);
        final List<T> lst = getDao().list(restrictions);
        if (lst == null || lst.size() == 0) {
            return null;
        }
        return lst.get(0);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, org.hibernate.criterion.Criterion)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion) throws DaoException {
        return list(entityClass, (List<Association>) null, Arrays.asList(criterion), (Projection) null, null, null, (List<Order>) null);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, org.hibernate.criterion.Criterion, org.hibernate.criterion.Order)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion, Order order) throws DaoException {
        return list(entityClass, (List<Association>) null, Arrays.asList(criterion), (Projection) null, null, null, Arrays.asList(order));
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, java.util.List, java.lang.Integer, java.lang.Integer, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return list(entityClass, (List<Association>) null, criterions, (Projection) null, firstResult, maxResults, orders);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, java.util.List, java.util.List, java.lang.Integer, java.lang.Integer, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return list(entityClass, associations, criterions, (Projection) null, firstResult, maxResults, orders);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#list(java.lang.Class, java.util.List, java.util.List, org.hibernate.criterion.Projection, java.lang.Integer, java.lang.Integer, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Projection projection, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return getDao().list(entityClass, associations, criterions, projection, firstResult, maxResults, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> list(Class<T> entityClass, String associationPath, List<Association> associations, List<Criterion> criterions, List<Projection> projections, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException {
        return getDao().list(entityClass, associationPath, associations, criterions, projections, firstResult, maxResults, orders);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#count(java.lang.Class, BaseRestrictions)
     */
    @Transactional(readOnly = true)
    public <T extends Entity> long count(BaseRestrictions<T> restrictions) {
        return getDao().count(restrictions);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> long countByProperty(BaseRestrictions<T> restrictions, String property) {
        return getDao().countByProperty(restrictions, property);
    }

    /**
     * @see org.seuksa.frmk.service.BaseEntityService#getIds(org.seuksa.frmk.dao.hql.BaseRestrictions)
     */
    @Transactional(readOnly = true)
    public <T extends Entity> Long[] getIds(BaseRestrictions<T> restrictions) {
        return getDao().getIds(restrictions);
    }
    
    @Override
    @Transactional(readOnly = true)
    public <T extends Entity> List<T> listByCode(Class<T> entityClass, String code) {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.addCriterion("code", code);
        return getDao().list(restriction);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#createQuery(java.lang.String)
     */
    @Override
    public Query createQuery(String queryString) throws DaoException {
        return getDao().createQuery(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#createCriteria(java.lang.Class)
     */
    @Override
    public <T extends Entity> Criteria createCriteria(Class<T> entityClass) throws DaoException {
        return getDao().createCriteria(entityClass);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#createSqlQuery(java.lang.String)
     */
    @Override
    public SQLQuery createSqlQuery(String queryString) throws DaoException {
        return getDao().createSqlQuery(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#deleteViaHQL(java.lang.String)
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteViaHQL(String queryString) throws DaoException {
        return getDao().deleteViaHQL(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#deleteViaHQL(java.lang.String, java.lang.Object, org.hibernate.type.Type)
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteViaHQL(String queryString, Object value, Type type) throws DaoException {
        return getDao().deleteViaHQL(queryString, value, type);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#deleteViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteViaHQL(String queryString, Object[] values, Type[] types) throws DaoException {
        return getDao().deleteViaHQL(queryString, values, types);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#updateViaHQL(org.seuksa.frmk.dao.BaseCriteria)
     */
    @Override
    @Transactional(readOnly = false)
    public int updateViaHQL(BaseCriteria criteria) throws DaoException {
        return getDao().updateViaHQL(criteria);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#updateViaHQL(java.lang.String)
     */
    @Override
    @Transactional(readOnly = false)
    public int updateViaHQL(String queryString) throws DaoException {
        return getDao().updateViaHQL(queryString);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#updateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
     */
    @Override
    @Transactional(readOnly = false)
    public int updateViaHQL(String queryString, Object[] values, Type[] types) throws DaoException {
        return getDao().updateViaHQL(queryString, values, types);
    }

    /**
     * @see org.seuksa.frmk.service.EntityService#executeUpdateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
     */
    @Override
    @Transactional(readOnly = false)
    public int executeUpdateViaHQL(String queryString, Object[] values, Type[] types) throws DaoException {
        return getDao().executeUpdateViaHQL(queryString, values, types);
    }


    /**_______________________________________________________________________________________
     * 
     * BLOCK [StatusRecord]
     * _______________________________________________________________________________________
     */

    @Override
    public <T extends EntityStatusRecordAware> List<T> listByStatusRecordActive(Class<T> entityClass) {
        return listByStatusRecord(entityClass, StatusRecord.ACTIV);
    }

    @Override
    public <T extends EntityStatusRecordAware> List<T> listByStatusRecord(Class<T> entityClass, StatusRecord statusRecord) {
        final BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
        restriction.addCriterion("statusRecord", statusRecord);
        return getDao().list(restriction);
    }

    @Override
    public <T extends EntityStatusRecordAware> List<T> listByStatusRecord(Class<T> entityClass, List<StatusRecord> statusRecordList) {
        final BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
        restriction.addCriterion("statusRecord", FilterMode.IN, statusRecordList);
        return getDao().list(restriction);
    }

    @Override
    public void throwIntoRecycleBin(EntityStatusRecordAware entity) throws IllegalRecycledBinException {
        final ErrorHandler errorHandler = checkBeforeThrowIntoRecycleBin();
        if (errorHandler != null && !errorHandler.equals(ErrorHandler.ERROR_NONE)) {
            throw new IllegalRecycledBinException(errorHandler);
        }
        changeStatusRecord(entity, StatusRecord.RECYC);
    }

    /**
     * Default behavior is true: nothing blocks the action of throwing in the RecycleBin
     * @return
     */
    @Override
    public ErrorHandler checkBeforeThrowIntoRecycleBin() {
        return ErrorHandler.ERROR_NONE;
    }

    @Override
    public <T extends EntityStatusRecordAware> void throwIntoRecycleBin(Class<T> entityClass, Long id) {
        final EntityStatusRecordAware entity = getDao().getById(entityClass, id);
        changeStatusRecord(entity, StatusRecord.RECYC);

    }

    @Override
    public <T extends EntityStatusRecordAware> void restoreFromRecycleBin(Class<T> entityClass, Long id) {
        final EntityStatusRecordAware entity = getDao().getById(entityClass, id);
        processStatusRecord(entity);
        merge(entity);
    }

    @Override
    public void processStatusRecord(EntityStatusRecordAware entity) {
        if (hasCompleteInfoForActive(entity)) {
            entity.setStatusRecord(StatusRecord.ACTIV);
        }
        else {
            entity.setStatusRecord(StatusRecord.INPRO);
        }
    }

    @Override
    public boolean hasCompleteInfoForActive(EntityStatusRecordAware entity) {
        return true;
    }

    @Override
    public void restoreFromRecycleBin(EntityStatusRecordAware entity) {
        changeStatusRecord(entity, StatusRecord.INPRO);
    }

    @Override
    public <T extends EntityStatusRecordAware> void restoreFromRecycleBinToActive(Class<T> entityClass, Long id) {
        final EntityStatusRecordAware entity = getDao().getById(entityClass, id);
        changeStatusRecord(entity, StatusRecord.ACTIV);

    }

    @Override
    public void restoreFromRecycleBinToActive(EntityStatusRecordAware entity) {
        changeStatusRecord(entity, StatusRecord.ACTIV);
    }

    @Override
    public void changeStatusRecord(EntityStatusRecordAware entity, StatusRecord statusRecord) {
        entity.setStatusRecord(statusRecord);
        merge(entity);
    }

    /**_______________________________________________________________________________________
     * 
     * BLOCK [CrudAction]
     * _______________________________________________________________________________________
     */
    @Override
    public <T extends EntityA> void saveOnAction(T entity) {
        if (entity == null) {
            return;
        }

        for (final EntityA child : entity.getSubEntitiesToCascadeAction()) {
            saveOnAction(child);
        }

        if (CrudAction.CREATE.equals(entity.getCrudAction())) {
            create(entity);
            for (final List<EntityA> child : entity.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
        }
        else if (CrudAction.UPDATE.equals(entity.getCrudAction())) {
        	for (final List<EntityA> child : entity.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
            merge(entity);
        }
        else if (CrudAction.DELETE.equals(entity.getCrudAction())) {
            delete(entity);
        }
        else if (CrudAction.REFRESH.equals(entity.getCrudAction())) {
            refresh(entity);
        }
        else if (CrudAction.RECYCLE.equals(entity.getCrudAction())) {
            throwIntoRecycleBin(entity);
        }
        else if (CrudAction.RESTORE.equals(entity.getCrudAction())) {
            restoreFromRecycleBin(entity);
        }

        for (final EntityA child : entity.getSubMappedEntitiesToCascade()) {
            saveOnAction(child);
        }
        
    }

    @Override
    public <T extends EntityA> void saveOnAction(List<T> entities) {
        for (final T entity : entities) {
            saveOnAction(entity);
        }
    }
}
