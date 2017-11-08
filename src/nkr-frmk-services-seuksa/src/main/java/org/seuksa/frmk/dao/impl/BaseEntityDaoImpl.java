package org.seuksa.frmk.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import org.seuksa.frmk.dao.BaseCriteria;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.Association;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.tools.NativeColumnUtils;
import org.seuksa.frmk.dao.vo.SysColumn;
import org.seuksa.frmk.dao.vo.SysTable;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.meta.FieldType;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.seuksa.frmk.tools.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * @author prasnar
 * @version $Revision$
 */
@Repository
public abstract class BaseEntityDaoImpl implements BaseEntityDao {
    protected Log logger = Log.getInstance(this.getClass());

    protected Properties envProperties = Environment.getProperties();
	public static int NB_MAX_ERRORS = 1;

	@Autowired
	private SessionFactory sessionFactory;

	/**
     * 
     */
	public BaseEntityDaoImpl() {
		//logger.info("Instantiate EntityDao.");
	}

	/**
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getSessionFactory()
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getCurrentSession()
	 */
	public Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return getConnectionProvider().getConnection();
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getConnectionProvider()
	 */
	@Override
	public ConnectionProvider getConnectionProvider() {
		return ((SessionFactoryImpl) getSessionFactory())
				.getConnectionProvider();
		// return envProperties.get(Environment.CONNECTION_PROVIDER);
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getDatabaseMetaData()
	 */
	@Override
	public DatabaseMetaData getDatabaseMetaData() throws SQLException {
		return getConnection().getMetaData();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getUserName()
	 */
	@Override
	public String getUserName() throws SQLException {
		return getDatabaseMetaData().getUserName();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getDatabaseName()
	 */
	@Override
	public String getDatabaseName() throws SQLException {
		return getDatabaseMetaData().getDatabaseProductName();
	}

	/**
	 * @throws SQLException 
	 * @see org.seuksa.frmk.dao.EntityDao#getDriverName()
	 */
	@Override
	public String getDriverName() throws SQLException {
		return getDatabaseMetaData().getDriverName();
	}
	
	
	@Override
    public Query getNamedQuery(String queryName) {
    	return getCurrentSession().getNamedQuery(queryName);
    }


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#create(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void create(T entity) {
		getCurrentSession().save(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#update(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void update(T entity) {
		getCurrentSession().update(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#saveOrUpdate(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void saveOrUpdate(T entity) {
		getCurrentSession().saveOrUpdate(entity);
	}

	@Override
	public <T extends Entity> List<Exception> saveOrUpdateBulk(List<T> entityLst) {
		return saveOrUpdateList(entityLst);
	}
		
	@Override
	public <T> List<Exception> saveOrUpdateList(List<T> entityLst) {
		Log.getInstance(this).debug("----start saveOrUpdateBulk------ nb entities:" + entityLst.size());
		int i = 0;
		Entity entity = null;
		int nbErrors = 0;
		List<Exception> lstExceptions = new LinkedList<Exception>();

		for (; i < entityLst.size() && nbErrors < NB_MAX_ERRORS; i++) {
			try {
				entity = (Entity) entityLst.get(i);
				saveOrUpdate(entity);
				lstExceptions.add(null);
			} catch (Exception e) {
				nbErrors++;
				Log.getInstance(this).error("---- Error [" + nbErrors + "] ------");

				String errMsg = "Error on entity [" + i + "]";
                logger.error(errMsg);
                logger.errorStackTrace(e);
				lstExceptions.add(new Exception(errMsg, e));
			}
		}

		return lstExceptions;
	}

	@Override
	public <T> List<Exception> deleteList(List<T> entityLst) {
		Log.getInstance(this).debug("----start deleteList------ nb entities:" + entityLst.size());
		int i = 0;
		Entity entity = null;
		int nbErrors = 0;
		List<Exception> lstExceptions = new LinkedList<Exception>();

		for (; i < entityLst.size() && nbErrors < NB_MAX_ERRORS; i++) {
			try {
				entity = (Entity) entityLst.get(i);
				delete(entity);
				lstExceptions.add(null);
			} catch (Exception e) {
				nbErrors++;
				Log.getInstance(this).error("---- Error [" + nbErrors + "] ------");

				String errMsg = "Error on entity [" + i + "]";
                logger.error(errMsg);
                logger.errorStackTrace(e);
				lstExceptions.add(new Exception(errMsg, e));
			}
		}
		i = 0;
		for (; i < entityLst.size(); i++) {
			entityLst.remove(i);
			i = 0;
		}

		return lstExceptions;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#delete(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T extends Entity> void delete(Class<T> entityClass, Serializable id)  {
		T entity = getById(entityClass, id);
		delete(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#delete(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#flush()
	 */
	@Override
	public void flush() {
		getCurrentSession().flush();
	}
	
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#close()
	 */
	@Override
	public void close() {
		getCurrentSession().close();
	}
	
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#close()
	 */
	@Override
	public void clear() {
		getCurrentSession().clear();
	}

	/**
	 * @throws DaoException 
	 * @see org.seuksa.frmk.dao.EntityDao#getByIdIfNotExistError(java.lang.Class, java.lang.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getByIdIfNotExistError(Class<T> entityClass, Serializable id) throws DaoException {
		// load: if object not found throw exception
		// get: if object not found return null
		T entity = (T) getCurrentSession().load(entityClass, id);
		if (entity == null) {
			throw new DaoException("loadIfNotExistError: "
					+ entityClass.getName() + " [" + id
					+ "] does not exist.");
		}
		return entity;
	}

	@Override
    public <T extends Entity> T getByCode(Class<T> entityClass, String code) {
		return getByField(entityClass, "code", code);
    }
	
	@Override
    public <T extends Entity> T getByDesc(Class<T> entityClass, String desc) {
		return getByField(entityClass, "desc", desc);
    }
	
	@Override
    public <T extends Entity> T getByField(Class<T> entityClass, String fieldName, Object value) {
		Criteria criteria = createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName, value));
		T entity = (T) criteria.uniqueResult();
		return entity;
    }
    
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getById(java.lang.Class, java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getById(Class<T> entityClass, Serializable id) {
		// load: if object not found throw exception
		// get: if object not found return null
		return (T) getCurrentSession().get(entityClass, id);
	}

	/**
	 * @throws DaoException 
	 * @see org.seuksa.frmk.dao.EntityDao#delete(org.seuksa.frmk.dao.BaseCriteria)
	 */
	@Override
	public int delete(BaseCriteria criteria) {
		return deleteViaHQL(criteria.queryDelete());
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#deleteViaHQL(java.lang.String)
	 */
	@Override
	public int deleteViaHQL(String queryString) {
		Object[] values = new Object[] {};
		Type[] types = new Type[] {};
		return deleteViaHQL(queryString, values, types);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#deleteViaHQL(java.lang.String, java.lang.Object, org.hibernate.type.Type)
	 */
	@Override
	public int deleteViaHQL(String queryString, Object value, Type type) {
		Object[] values = new Object[] { value };
		Type[] types = new Type[] { type };
		return deleteViaHQL(queryString, values, types);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#deleteViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public int deleteViaHQL(String queryString, Object[] values, Type[] types) {
		return executeUpdateViaHQL(queryString, values, types);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#updateViaHQL(org.seuksa.frmk.dao.BaseCriteria)
	 */
	@Override
	public int updateViaHQL(BaseCriteria criteria) {
		return updateViaHQL(criteria.queryUpdate());
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#updateViaHQL(java.lang.String)
	 */
	@Override
	public int updateViaHQL(String queryString) {
		return updateViaHQL(queryString, null, null);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#updateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public int updateViaHQL(String queryString, Object[] values, Type[] types)
			{
		return executeUpdateViaHQL(queryString, values, types);
	}


	/**
	 * @see org.seuksa.frmk.dao.EntityDao#executeUpdateViaHQL(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
	 */
	@Override
	public int executeUpdateViaHQL(String queryString, Object[] values, Type[] types) {
		Query q = getCurrentSession().createQuery(queryString);
		if (values != null && types != null) {
			q.setParameters(values, types);
		}
		int result = q.executeUpdate();
		return result;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#count(org.seuksa.frmk.dao.BaseCriteria)
	 */
	@Override
	public long count(BaseCriteria criteria) {
		Query query = getCurrentSession().createQuery(criteria.queryCount());
		long count = (Long) (query.list().get(0));
		return count;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(org.seuksa.frmk.dao.BaseCriteria)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> list(BaseCriteria criteria) {
		Query query = getCurrentSession().createQuery(criteria.queryList());
		if (criteria.getIndexFirstRow() < 0) {
			criteria.setIndexFirstRow(0);
		}
		query = query.setFirstResult(criteria.getIndexFirstRow());
		if (criteria.getMaxResult() > 0) {
			query = query.setMaxResults(criteria.getMaxResult());
		}
		List<T> lst = query.list();
		return lst;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#getFirst(org.seuksa.frmk.dao.BaseCriteria)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getFirst(BaseCriteria criteria) {
		Query query = getCurrentSession().createQuery(criteria.queryList());
		query = query.setMaxResults(1);
		List<T> lst = query.list();
		return lst != null && lst.size() > 0 ? lst.get(0) : null;
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass) {
		return createCriteria(entityClass).list();
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(java.lang.Class, org.hibernate.criterion.Order)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, Order order) {
		return createCriteria(entityClass).addOrder(order).list();
	}

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(org.seuksa.frmk.dao.hql.BaseRestrictions)
	 */
	@Override
	public <T extends Entity> List<T> list(BaseRestrictions<T> restrictions) {
		restrictions.internalPreBuildSpecificCriteria();
		restrictions.internalPreBuildCommunCriteria();
		List<T> lst = list(
				restrictions.getEntityClass(), 
				restrictions.getAssociations(), 
				restrictions.getCriterions(), 
				restrictions.getFirstResult(), 
				restrictions.getMaxResults(), 
				restrictions.getOrders());	
		return lst;
	}
	
	
	@Override
	public <T extends Entity> long count(final BaseRestrictions<T> restrictions) {
		return countByProperty(restrictions, "id");
	}
	
	@Override
	public <T extends Entity> long countByProperty(BaseRestrictions<T> restrictions, String property) {
		restrictions.internalPreBuildSpecificCriteria();
		restrictions.internalPreBuildCommunCriteria();
		List result = list(
				restrictions.getEntityClass(), 
				restrictions.getAssociations(), 
				restrictions.getCriterions(), 
				Projections.countDistinct(property),
				null, 
				null, 
				null);
		if (!result.isEmpty()) {
            return (Long) result.get(0);
        }
		return 0;
	}
	
	@Override
	public <T extends Entity> Long[] getIds(BaseRestrictions<T> restrictions) {
		restrictions.internalPreBuildSpecificCriteria();
		restrictions.internalPreBuildCommunCriteria();
		List<T> result = list(
				restrictions.getEntityClass(), 
				restrictions.getAssociations(), 
				restrictions.getCriterions(), 
				Projections.distinct(Projections.property("id")),
				restrictions.getFirstResult(),
	            restrictions.getMaxResults(), 
				restrictions.getOrders());
		
		return result.toArray(new Long[0]);
	}

	
	@Override
	public <T extends Entity> List<T> listByIds(Class<T> entityClass, List<Long> idList) {
		BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
		restriction.addCriterion(Restrictions.in("id", idList));
    	return list(restriction);
	}


	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(java.lang.Class, org.hibernate.criterion.Criterion)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, Criterion criterion) {
		return list(entityClass, Arrays.asList(criterion), null);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(java.lang.Class, java.util.List, org.hibernate.criterion.Order)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, List<Criterion> criterions, Order order) {
		return list(entityClass, (List<Association>) null, criterions, null, null, null, Arrays.asList(order));
	}

	
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#list(java.lang.Class, org.hibernate.criterion.Criterion[], org.hibernate.criterion.Projection, java.lang.Integer, java.lang.Integer, org.hibernate.criterion.Order[])
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Integer firstResult, Integer maxResults, List<Order> orders){
		return list(entityClass, associations, criterions, null, firstResult, maxResults, orders);
	}
	

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#list(java.lang.Class, java.util.List, java.util.List, org.hibernate.criterion.Projection, java.lang.Integer, java.lang.Integer, java.util.List)
	 */
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, List<Association> associations, List<Criterion> criterions, Projection projection, Integer firstResult, Integer maxResults, List<Order> orders)  {
		return list(entityClass, null, associations, criterions, Arrays.asList(projection), firstResult, maxResults, orders);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> list(Class<T> entityClass, String associationPath, List<Association> associations, List<Criterion> criterions, List<Projection> projections, Integer firstResult, Integer maxResults, List<Order> orders)  {
		
		Criteria criteria = createCriteria(entityClass);
		
		if (associations != null) {
			for (Association association : associations) {
				if (association != null) {
					criteria.createAlias(
							association.getAssociationPath(), 
							association.getAlias(), 
							association.getJoinType());
				}
			}
		}
		
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria = criteria.add(criterion);
				}
			}
		}
		
		if (projections != null) {
			ProjectionList projList = Projections.projectionList();
			for (Projection proj : projections) {
				if (proj != null) {
			        projList.add(proj);
				}
			}
			if (projList != null && projList.getLength() > 0) {
				criteria.setProjection(projList);
			}
		}

		
		if (orders != null) {
			for (Order order : orders) {
				if (order != null) {
					criteria = criteria.addOrder(order);
				}
			}
		}

		
		if (firstResult != null) {
			criteria.setFirstResult(firstResult);
		}

		if (maxResults != null) {
			criteria.setMaxResults(maxResults);
		}

		
		return criteria.list();
		
	}

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#refresh(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	public <T extends Entity> void refresh(T entity) {
		getCurrentSession().refresh(entity);
	}

	/**
	 * @see org.seuksa.frmk.dao.BaseEntityDao#createCriteria(java.lang.Class)
	 */
	@Override
	public <T extends Entity> Criteria createCriteria(Class<T> entityClass) {
		return getCurrentSession().createCriteria(entityClass);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#createQuery(java.lang.String)
	 */
	@Override
	public Query createQuery(String queryString) {
		return getCurrentSession().createQuery(queryString);
	}

	/**
	 * @see org.seuksa.frmk.dao.EntityDao#createSqlQuery(java.lang.String)
	 */
	@Override
	public SQLQuery createSqlQuery(String queryString) {
		return getCurrentSession().createSQLQuery(queryString);
	}

	/**
	 * _______________________________________________________________________________________
	 * 
	 * BLOCK [CommonDao]
	 * _______________________________________________________________________________________
	 */
	/**
	 * @see org.seuksa.frmk.dao.EntityDao#merge(org.seuksa.frmk.model.entity.Entity)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T merge(T entity) {
		return (T) getCurrentSession().merge(entity);
	}


	/**
	 * Retrieve the name of all tables that are managed by the SessionFactory
	 * @see org.seuksa.frmk.dao.EntityDao#getAllTablesName()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<SysTable> getAllTablesName() {
		List<SysTable> sysTableList = new ArrayList<SysTable>();
		Map classMetadata = getSessionFactory().getAllClassMetadata();
		for (Iterator iter = classMetadata.keySet().iterator(); iter.hasNext();) {
			String className = (String) iter.next();
			AbstractEntityPersister aep = (AbstractEntityPersister) classMetadata
					.get(className);
			SysTable sysTable = new SysTable();
			sysTable.setName(aep.getTableName());
			String[] propertyNames = aep.getPropertyNames();
			for (int i = 0; i < propertyNames.length; i++) {
				String columnName = aep
						.getPropertyColumnNames(propertyNames[i])[0];
				if (columnName != null) {
					sysTable.addColumn(new SysColumn(columnName));
				}
			}
			sysTableList.add(sysTable);
		}
		return sysTableList;
	}
	
	/**
	 * @see org.seuksa.frmk.mvc.dao.EntityDao#executeSQLNativeQuery(java.lang.String)
	 */
	@Override
	public List<NativeRow> executeSQLNativeQuery(final String sql) throws NativeQueryException {
		String sqlLowercase = sql.toLowerCase();
		if (sqlLowercase.indexOf("update ") != -1
				|| sqlLowercase.indexOf("delete ") != -1
				|| sqlLowercase.indexOf("insert ") != -1) {
			throw new NativeQueryException(
					"Operation INSERT or UPDATE or DELETE is not allowed");
		}
		final List<NativeRow> rows = new ArrayList<NativeRow>();
		try {
			getCurrentSession().doWork(new Work() {

				public void execute(Connection conn) throws SQLException {
					Statement stmt = null;
					ResultSet rs = null;
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							NativeRow row = new NativeRow();
							for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
								NativeColumn column = new NativeColumn();
								column.setName(rs.getMetaData().getColumnName(i));
								FieldType type = NativeColumnUtils.getFieldType(rs.getMetaData().getColumnType(i));
								switch (type) {
								case LONG:
									column.setValue(rs.getLong(column.getName()));
									break;
								case INTEGER:
									column.setValue(rs.getInt(column.getName()));
									break;
								case DATE:
									column.setValue(rs.getDate(column.getName()));
									break;
								case TIME:
									column.setValue(rs.getTime(column.getName()));
									break;
								case DATETIME:
									column.setValue(rs.getTimestamp(column.getName()));
									break;
								case DOUBLE:
									column.setValue(rs.getDouble(column.getName()));
									break;
								case FLOAT:
									column.setValue(rs.getFloat(column.getName()));
									break;
								case BOOLEAN:
									column.setValue(rs.getBoolean(column.getName()));
									break;
								default:
									column.setValue(rs.getString(column.getName()));
									break;
								}
								column.setType(type);
								row.addColumn(column);
							}
							rows.add(row);
						}
					} finally {
						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}
					}
				}
			});
		} catch (Exception e) {
			Log.getInstance(this).error("-> Error while executing native sql query : " + sql, e);
			throw new NativeQueryException(e);
		}

		return rows;
	}

}
