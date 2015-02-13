package com.nokor.frmk.dao;

import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.frmk.model.refdata.RefTable;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface ReferenceTableDao extends BaseEntityDao {

    List<RefTable> getTables(Long appId, Order order) throws DaoException;

    List<RefTable> getTables(Long appId) throws DaoException;

    <T extends EntityRefA> List<T> getValues(BaseRestrictions<T> restricton) throws DaoException;

    <T extends EntityRefA> List<T> getValues(Class<T> entityClass) throws DaoException;

    String generateNextSequenceCode(String entitySimpleNames) throws DaoException;

}
