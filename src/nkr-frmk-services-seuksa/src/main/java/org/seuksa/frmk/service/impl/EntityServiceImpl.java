package org.seuksa.frmk.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author prasnar
 * @version $Revision$
 */

@Service("entityService")
@Transactional
public class EntityServiceImpl extends BaseEntityServiceImpl implements EntityService {

    @Autowired
    private EntityDao dao;

    /**
	 * 
	 */
    public EntityServiceImpl() {
    	super();
    }
    
    /**
     * @see org.seuksa.frmk.service.BaseEntityService#getDao()
     */
    @Override
    public BaseEntityDao getDao() {
        return dao;
    }

    

}
