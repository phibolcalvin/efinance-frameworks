package org.seuksa.frmk.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.seuksa.frmk.dao.BaseCriteria;
import org.seuksa.frmk.dao.EntityJpaDao;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.log.Log;

/**
 * @author prasnar
 * @version $Revision$
 */
public class EntityJpaDaoImpl implements EntityJpaDao<Entity> {
    protected Log logger = Log.getInstance(this.getClass());
    public static int NB_MAX_ERRORS = 1;

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;

    /**
     * 
     */
    public EntityJpaDaoImpl() {
		logger.info("Instantiate EntityJpaDao.");
    }



	public Connection getConnection() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}


    /**
     * @see org.seuksa.frmk.dao.EntityDao#getDatabaseMetaData()
     */
    public DatabaseMetaData getDatabaseMetaData() throws DaoException {
        try {
            return getConnection().getMetaData();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.dao.EntityDao#getUserName()
     */
    public String getUserName() throws DaoException {
        try {
            return getDatabaseMetaData().getUserName();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#getDatabaseName()
     */
    public String getDatabaseName() throws DaoException {
        try {
            return getDatabaseMetaData().getDatabaseProductName();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#getDriverName()
     */
    public String getDriverName() throws DaoException {
        try {
            return getDatabaseMetaData().getDriverName();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#create(org.seuksa.frmk.model.entity.Entity.IEntity)
     */
    public void create(Entity entity) throws DaoException {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#update(org.seuksa.frmk.model.entity.Entity.IEntity)
     */
    public void update(Entity entity) throws DaoException {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#createOrUpdate(org.seuksa.frmk.model.entity.Entity.IEntity)
     */
    public void createOrUpdate(Entity entity) throws DaoException {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#createOrUpdateBulk(java.util.List)
     */
    public List<Exception> createOrUpdateBulk(List<? extends Entity> entityLst) throws DaoException {
        logger.debug("----start createOrUpdateBulk------ nb entities:" + entityLst.size());
        int i = 0;
        Entity entity = null;
        int nbErrors = 0;
        List<Exception> lstExceptions = new LinkedList<Exception>();

        for (; i < entityLst.size() && nbErrors < NB_MAX_ERRORS; i++) {
            try {
                entity = entityLst.get(i);
                createOrUpdate(entity);
                lstExceptions.add(null);
            } catch (Exception e) {
                nbErrors++;
                logger.error("---- Error [" + nbErrors + "] ------");

                String errMsg = "Error on entity [" + i + "]";
                logger.error(errMsg);
                logger.errorStackTrace(e);
                lstExceptions.add(new Exception(errMsg, e));
            }
        }

        return lstExceptions;
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#delete(java.lang.Class,
     *      java.io.Serializable)
     */
    public void delete(Class<? extends Entity> entityClass, Serializable id) throws DaoException {
        Entity entity = loadIfNotExistError(entityClass, id);
        delete(entity);
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#delete(org.seuksa.frmk.model.entity.Entity.IEntity)
     */
    public void delete(Entity entity) throws DaoException {
        try {
            entityManager.remove(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#flush()
     */
    public void flush() throws DaoException {
        try {
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#getByIdIfNotExistError(java.lang.Class,
     *      java.io.Serializable)
     */
    public Entity loadIfNotExistError(Class<? extends Entity> entityClass, Serializable id) throws DaoException {
        try {
            Entity entity = (Entity) entityManager.find(entityClass, id);
            if (entity == null) {
                throw new DaoException("loadIfNotExistError: " + entityClass.getName() + " [" + id + "] does not exist.");
            }
            return entity;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#loadAll(java.lang.Class)
     */
    public List<Entity> loadAll(Class<? extends Entity> entityClass) {
        String hql = "from " + entityClass.getSimpleName();
        return entityManager.createQuery(hql).getResultList();
        // return entityManager.createCriteria(entityClass).list();
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#load(java.lang.Class,
     *      java.io.Serializable)
     */
    public Entity load(Class<? extends Entity> entityClass, Serializable id) {
        Entity entity = (Entity) entityManager.find(entityClass, id);
        return entity;
    }

    /**
     * 
     */
    public Entity loadNoCache(Class<? extends Entity> entityClass, Serializable id) throws DaoException {
        // TODO
        return null;
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#delete(org.seuksa.frmk.BaseRestrictions.BaseCriteria)
     */
    public int delete(BaseCriteria criteria) throws DaoException {
        return deleteViaHQL(criteria.queryDelete());
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#deleteViaHQL(java.lang.String)
     */
    public int deleteViaHQL(String queryString) throws DaoException {
        try {
            Object[] values = new Object[] {};
            return deleteViaHQL(queryString, values);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    
    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#deleteViaHQL(java.lang.String,
     *      org.seuksa.frmk.model.entity.Entity.IEntity[], org.hibernate.type.Type[])
     */
    public int deleteViaHQL(String queryString, Object[] values) throws DaoException {
        return executeUpdateViaHQL(queryString, values);
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#updateViaHQL(org.seuksa.frmk.BaseRestrictions.BaseCriteria)
     */
    public int updateViaHQL(BaseCriteria criteria) throws DaoException {
        return updateViaHQL(criteria.queryUpdate());
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#updateViaHQL(java.lang.String)
     */
    public int updateViaHQL(String queryString) throws DaoException {
        return executeUpdateViaHQL(queryString, null);
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#executeUpdateViaHQL(java.lang.String,
     *      java.lang.Object[], org.hibernate.type.Type[])
     */
    public int executeUpdateViaHQL(String queryString, Object[] values) throws DaoException {
        try {
            Query q = entityManager.createQuery(queryString);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    q.setParameter(i, values[i]);
                }
            }
            int result = q.executeUpdate();
            return result;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#count(org.seuksa.frmk.BaseRestrictions.BaseCriteria)
     */
    public long count(BaseCriteria criteria) throws DaoException {
        Query query = entityManager.createQuery(criteria.queryCount());
        long count = (Long) (query.getSingleResult());
        return count;
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#list(org.seuksa.frmk.BaseRestrictions.BaseCriteria)
     */
    public List<? extends Entity> list(BaseCriteria criteria) throws DaoException {
        try {
            Query query = entityManager.createQuery(criteria.queryList());
            if (criteria.getIndexFirstRow() < 0) {
                criteria.setIndexFirstRow(0);
            }
            query = query.setFirstResult(criteria.getIndexFirstRow());
            if (criteria.getMaxResult() > 0) {
                query = query.setMaxResults(criteria.getMaxResult());
            }
            return (List<? extends Entity>) query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#getFirst(org.seuksa.frmk.BaseRestrictions.BaseCriteria)
     */
    public Entity getFirst(BaseCriteria criteria) throws DaoException {
        try {
            Query query = entityManager.createQuery(criteria.queryList());
            // query = query.setMaxResults(1);
            return (Entity) query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#list(java.lang.Class)
     */
    public List<? extends Entity> list(Class<? extends Entity> entityClass) throws DaoException {

        try {
            String hql = "from " + entityClass.getSimpleName();
            return entityManager.createQuery(hql).getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @see org.seuksa.frmk.EntityDao.IEntityDao#refresh(org.seuksa.frmk.model.entity.Entity.IEntity)
     */
    public void refresh(Entity entity) throws DaoException {
        try {
            entityManager.refresh(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }


}
