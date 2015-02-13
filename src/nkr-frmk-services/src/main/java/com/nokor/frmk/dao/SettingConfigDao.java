package com.nokor.frmk.dao;

import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.frmk.model.config.SettingConfig;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface SettingConfigDao extends BaseEntityDao {

    List<SettingConfig> list(SecApplication secApp) throws DaoException;

  
}
