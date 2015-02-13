package com.nokor.frmk.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.seuksa.frmk.tools.log.Log;
import org.springframework.stereotype.Repository;

import com.nokor.frmk.dao.SettingConfigDao;
import com.nokor.frmk.model.config.SettingConfig;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Repository
public class SettingConfigDaoImpl extends BaseEntityDaoImpl implements SettingConfigDao {

    protected Log logger = Log.getInstance(this.getClass());

    /**
     * 
     */
    public SettingConfigDaoImpl() {

    }

	@Override
	public List<SettingConfig> list(SecApplication secApp) throws DaoException {
		List<SettingConfig> lst;
        if (secApp != null) {
            LogicalExpression expression = null;
            expression = Restrictions.or(Restrictions.eq("secAppId", secApp.getId()), Restrictions.isNull("secAppId"));
            lst = list(SettingConfig.class, expression);
        }
        else {
            final Criterion expression = Restrictions.isNull("secAppId");
            lst = list(SettingConfig.class, expression);
        }
        return lst;
	}
}
