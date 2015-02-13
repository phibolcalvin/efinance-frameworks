package com.nokor.frmk.service;

import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.sysref.StatusRecord;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.frmk.model.refdata.RefTable;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface ReferenceTableService extends BaseEntityService {

    SecApplication getCurrentSecApplication();

    void setCurrentSecApplication(SecApplication secApplication);

    EntityRefA createInstance(String tablename);

    List<RefTable> getTables() ;

    List<RefTable> getTablesByAppId(Long secAppId) ;

    List<RefTable> getTablesByAppId(Long secAppId, boolean refreshCache) ;

    RefTable getTable(Long secAppId, String tablename) ;

    RefTable getTable(String tablename) ;

    <T extends EntityRefA> RefTable getTable(Class<T> entityClass) ;

    <T extends EntityRefA> RefTable getTable(Long secAppId, Class<T> entityClass) ;

    //<T extends EntityRefA> T getValuesByTable(String tablename) ;

    //<T extends EntityRefA> T getValuesByTable(String tablename, boolean refreshCache) ;

    <T extends EntityRefA> T getValueById(Class<T> entityClass, Long id) ;

    <T extends EntityRefA> T getValueByCode(Class<T> entityClass, String code) ;
    
    <T extends EntityRefA> List<T> getValuesActive(Class<T> entityClass);
    
    <T extends EntityRefA> List<T> getValuesActive(Class<T> entityClass, Order order);
    
    <T extends EntityRefA> List<T> getValuesRecycled(Class<T> entityClass);
    
    <T extends EntityRefA> List<T> getValuesRecycled(Class<T> entityClass, Order order);
    
    <T extends EntityRefA> List<T> getValues(Class<T> entityClass, StatusRecord statusRecord);

    <T extends EntityRefA> List<T> getValues(Class<T> entityClass, Order order, StatusRecord statusRecord);

    <T extends EntityRefA> List<T> getValues(Class<T> entityClass) ;

    <T extends EntityRefA> List<T> getValues(Class<T> entityClass, Order order) ;

    <T extends EntityRefA> List<T> getValues(Class<T> entityClass, boolean refreshCache) ;

    <T extends EntityRefA> List<T> getValues(BaseRestrictions<T> restriction, boolean refreshCache) ;

    <T extends EntityRefA> String getDescValue(Class<T> entityClass, Long entityId) ;

    <T extends EntityRefA> String generateNextSequenceCode(Class<T> entityClass) ;

    String generateNextSequenceCode(String entityName) ;

    void flushCache();

    boolean saveOrUpdate(EntityRefA entityRef) ;

    boolean delete(EntityRefA entityRef) ;

    boolean delete(RefTable tableRef) ;

    boolean saveOrUpdate(RefTable tableRef) ;

    <T extends EntityRefA> boolean recycled(Class<T> entityClass, Long id) ;

}
