package com.nokor.frmk.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;

import com.nokor.frmk.service.MainEntityService;
import com.nokor.frmk.service.SettingService;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class MainEntityServiceImpl extends BaseEntityServiceImpl implements MainEntityService {

    @Autowired
    private SettingService settingService;

    /**
     * 
     */
    public MainEntityServiceImpl() {
        super();
    }

    /**
     * Prevent to call directly the method
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#saveOrUpdate(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void saveOrUpdate(T entity) throws DaoException {
        // throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.saveOrUpdate(entity);
    }
    
    @Override
    public void saveOrUpdateProcess(MainEntity mainEntity) throws DaoException {
    	super.saveOrUpdate(mainEntity);
    }

    /**
     * Prevent to call directly the method
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#create(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void create(T entity) throws DaoException {
        // throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.create(entity);
    }

    /**
     * Prevent to call directly the method
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#update(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void update(T entity) throws DaoException {
        // throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.update(entity);
    }

    /**
     * Prevent to call directly the method
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#delete(org.seuksa.frmk.model.entity.Entity)
     */
    @Override
    public <T extends Entity> void delete(T entity) throws DaoException {
        // throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.delete(entity);
    }

    @Override
    public <T extends EntityA> void saveOnAction(T entity) {
        //    	throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.saveOnAction(entity);
    }

    @Override
    public <T extends EntityA> void saveOnAction(List<T> entities) {
        //throw new IllegalStateException("This method can not be used directly. the **Process() method should be called instead.");
        super.saveOnAction(entities);
    }

    @Override
    public <T extends MainEntity> List<String> getRequiredInfosForActive(T entity) {
        logger.debug("Check RequiredInfosForActive");
        final List<String> missingProperties = new ArrayList<String>();
        try {
            final List<String> requiredProperties = settingService.getRequiredPropertiesForActive(entity.getClass());

            for (final String property : requiredProperties) {
                boolean missing = false;

                final Field field = this.getClass().getDeclaredField(property);

                final Object value = field.get(this);
                logger.debug(" - [" + property + "] [" + (value != null ? value.toString() : "<null>") + "]");
                if (Number.class.isAssignableFrom(value.getClass())) {
                    missing = ((Number) value).doubleValue() < 0;
                }
                else if (value instanceof String) {
                    missing = StringUtils.isEmpty((String) value);
                }
                else {
                    missing = value == null;
                }

                if (missing) {
                    missingProperties.add(property);
                }
            }

        }
        catch (final Exception e) {
            logger.error(e);
        }
        return missingProperties;

    }

}