package org.seuksa.frmk.service;

import java.io.Serializable;
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
import org.seuksa.frmk.dao.hql.Association;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.vo.SysTable;
import org.seuksa.frmk.model.entity.BasePK;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.IllegalRecycledBinException;
import org.seuksa.frmk.tools.exception.NativeQueryException;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface BaseEntityService  {
	
	public SessionFactory getSessionFactory();

    public String getUserName() throws DaoException;

    public String getDatabaseName() throws DaoException;

    public String getDriverName() throws DaoException;

    public List<SysTable> getAllTablesName() throws DaoException;

	List<NativeRow> executeSQLNativeQuery(final String sql) throws NativeQueryException;

    /**_______________________________________________________________________________________
     * 
     * BLOCK [IBaseHibernateService PYI]
     * _______________________________________________________________________________________
     */
    public Query getNamedQuery(String queryName);

    public <T extends Entity> void saveOrUpdate(T entity) throws DaoException;
    
    public <T> List<Exception> saveOrUpdateList(List<T> entityLst) throws DaoException;
    
    public <T extends Entity> List<Exception> saveOrUpdateBulk(List<T> entityLst) throws DaoException;

    public <T extends Entity> void create(T entity) throws DaoException;

    public <T extends Entity> void update(T entity) throws DaoException;

    public <T extends Entity> void delete(T entity) throws DaoException;

    public <T extends Entity> void delete(Class<T> entityClass, Serializable id) throws DaoException;

    public int delete(BaseCriteria criteria) throws DaoException;

    public <T extends Entity> T merge(T entity) throws DaoException;

    public void flush() throws DaoException;
    
    public void close() throws DaoException;
    
    public <T extends Entity> T getFirst(BaseCriteria criteria) throws DaoException;

    public long count(BaseCriteria criteria) throws DaoException;

    public <T extends Entity> void refresh(T entity) throws DaoException;

    public <T extends Entity> T getByCode(Class<T> entityClass, String code);

    public <T extends Entity> T getByDesc(Class<T> entityClass, String desc);

    public <T extends Entity> T getByField(Class<T> entityClass, String fieldName, Object value);
    
    public <T extends Entity> T getById(Class<T> entityClass, BasePK pk) throws DaoException;

    public <T extends Entity> T getById(Class<T> entityClass, int id) throws DaoException;

    public <T extends Entity> T getById(Class<T> entityClass, Long id) throws DaoException;

    public <T extends Entity> T getById(Class<T> entityClass, String id) throws DaoException;

    public <T extends Entity> T getById(Class<T> entityClass, Serializable id) throws DaoException;

    public <T extends Entity> T getByIdIfNotExistError(Class<T> entityClass, Serializable id) throws DaoException;

    public <T extends Entity> long count(BaseRestrictions<T> baseRestrictions);
    
    public <T extends Entity> long countByProperty(BaseRestrictions<T> restrictions, String property);
    
    public <T extends Entity> Long[] getIds(BaseRestrictions<T> restrictions);
    
    public <T extends Entity> List<T> list(BaseCriteria criteria) throws DaoException;

    public <T extends Entity> List<T> list(Class<T> entityClass) throws DaoException;

    public <T extends Entity> List<T> list(Class<T> entityClass, Order order) throws DaoException;
    

    public <T extends Entity> List<T> listByIds(Class<T> entityClass, List<Long> idList) ;
    
    public <T extends Entity> T getFirst(BaseRestrictions<T> restrictions) throws DaoException;
    
    public <T extends Entity> List<T> list(BaseRestrictions<T> restrictions) throws DaoException;

    public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion) throws DaoException;

    public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion, Order order) throws DaoException;
    
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException;

    public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException;
    
    public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Projection projection, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException;
    
    public <T extends Entity> List<T> list(Class<T> entityClass, String associationPath, List<Association> associations, List<Criterion> criterions, List<Projection> projections, Integer firstResult, Integer maxResults, List<Order> orders) throws DaoException;
    
    public <T extends Entity> List<T> listByCode(Class<T> entityClass, String code);
    
    public Query createQuery(String queryString) throws DaoException;
    
    public <T extends Entity> Criteria createCriteria(Class<T> entityClass) throws DaoException;
    
    public SQLQuery createSqlQuery(String queryString) throws DaoException;
    
    public int deleteViaHQL(String queryString) throws DaoException;

    public int deleteViaHQL(String queryString, Object value, Type type) throws DaoException;

    public int deleteViaHQL(String queryString, Object[] values, Type[] types) throws DaoException;
 
    public int updateViaHQL(BaseCriteria criteria) throws DaoException;

    public int updateViaHQL(String queryString) throws DaoException;

    public int updateViaHQL(String queryString, Object[] values, Type[] types) throws DaoException;

    public int executeUpdateViaHQL(String queryString, Object[] values, Type[] types) throws DaoException;


    /**_______________________________________________________________________________________
     * 
     * BLOCK [StatusRecord]
     * _______________________________________________________________________________________
     */

	<T extends EntityStatusRecordAware> void throwIntoRecycleBin(Class<T> entityClass, Long id);

	void throwIntoRecycleBin(EntityStatusRecordAware entity) throws IllegalRecycledBinException;
    
	ErrorHandler checkBeforeThrowIntoRecycleBin();
	
	<T extends EntityStatusRecordAware> void restoreFromRecycleBin(Class<T> entityClass, Long id);

	void restoreFromRecycleBin(EntityStatusRecordAware entity);
    
	<T extends EntityStatusRecordAware> void restoreFromRecycleBinToActive(Class<T> entityClass, Long id);

	void restoreFromRecycleBinToActive(EntityStatusRecordAware entity);

	void changeStatusRecord(EntityStatusRecordAware entity, StatusRecord statusRecord);

    void processStatusRecord(EntityStatusRecordAware entity);

    boolean hasCompleteInfoForActive(EntityStatusRecordAware entity);

	<T extends EntityStatusRecordAware> List<T> listByStatusRecordActive(Class<T> entityClass);

	<T extends EntityStatusRecordAware> List<T> listByStatusRecord(Class<T> entityClass, StatusRecord statusRecord);

	<T extends EntityStatusRecordAware> List<T> listByStatusRecord(Class<T> entityClass, List<StatusRecord> statusRecordList);

    /**_______________________________________________________________________________________
     * 
     * BLOCK [CrudAction]
     * _______________________________________________________________________________________
     */
	<T extends EntityA> void saveOnAction(List<T> entities);

	<T extends EntityA> void saveOnAction(T entity);
}
