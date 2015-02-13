package com.nokor.frmk.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.log.Log;
import org.springframework.stereotype.Repository;

import com.nokor.frmk.dao.ReferenceTableDao;
import com.nokor.frmk.model.refdata.RefTable;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Repository
public class ReferenceTableDaoImpl extends BaseEntityDaoImpl implements ReferenceTableDao {

    protected Log logger = Log.getInstance(this.getClass());

    /**
     * 
     */
    public ReferenceTableDaoImpl() {

    }

    @Override
    public List<RefTable> getTables(final Long secAppId, final Order order) throws DaoException {
        List<RefTable> lst;
        if (secAppId != null & secAppId > 0) {
            LogicalExpression expression = null;
            expression = Restrictions.or(Restrictions.eq("secAppId", secAppId), Restrictions.isNull("secAppId"));
            lst = list(RefTable.class, expression);
        }
        else {
            final Criterion expression = Restrictions.isNull("secAppId");
            lst = list(RefTable.class, expression);
        }
        return lst;
    }

    @Override
    public List<RefTable> getTables(final Long appId) throws DaoException {
        return getTables(appId, Order.asc(EntityRefA.CODE_FIELD));
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(BaseRestrictions<T> restricton) throws DaoException {
        return list(restricton);
    }

    @Override
    public <T extends EntityRefA> List<T> getValues(Class<T> entityClass) throws DaoException {
    	BaseRestrictions<T> restriction = new BaseRestrictions<T>(entityClass);
    	restriction.setOrder(Order.asc(EntityRefA.CODE_FIELD));
        return getValues(restriction);
    }

    @Override
    public String generateNextSequenceCode(final String entitySimpleName) {
        final String hql = "select max(e.code) from " + entitySimpleName + " e";
        final String maxCode = (String) createQuery(hql).list().get(0);

        try {
            if (maxCode.equals("")) {
                // For first code
                return "0000000001";
            }
            else {
                return StringUtils.leftPad(String.valueOf(Integer.valueOf(maxCode) + 1), 10, '0');
            }
        }
        catch (final Exception e) {
            logger.errorStackTrace(e);
            return null;
        }
    }

}
