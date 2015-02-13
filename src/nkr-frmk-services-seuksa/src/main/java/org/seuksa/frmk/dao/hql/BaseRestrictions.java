package org.seuksa.frmk.dao.hql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.criteria.FieldFilter;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.criteria.StringFilterMode;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.StatusRecordAware;
import org.seuksa.frmk.model.entity.StatusRecordListAware;
import org.seuksa.frmk.model.sysref.StatusRecord;

/**
 * @author prasnar
 * @version $Revision$
 */
public class BaseRestrictions <T extends Entity> implements ColumnInfosAware, StatusRecordAware, StatusRecordListAware {
    protected static Logger logger = Logger.getLogger(BaseRestrictions.class.getName());
    
    private List<ColumnInfo> columnInfos;
    private Class<T> entityClass;
    private List<Association> associations;
    private List<Criterion> criterions;
    private Integer firstResult;
    private Integer maxResults;
    private List<Order> orders;
    private Boolean orderBySortIndex;
    
	private List<StatusRecord> statusRecordList;

    private boolean hasBuildSpecificCriteria = false;
    private boolean hasBuildCommunCriteria = false;

    private Map<String, Object> mapCriterias = new HashMap<String, Object>();

    /**
     * 
     * @param entityClass
     */
    public BaseRestrictions(Class<T> entityClass) {
    	this.entityClass = entityClass;
    	associations = new ArrayList<Association>();
    	criterions = new ArrayList<Criterion>();
    	orders = new ArrayList<Order>();
    	columnInfos = new ArrayList<ColumnInfo>();
    	
    	statusRecordList = new ArrayList<StatusRecord>();
    }
    
   
    /**
     * To override following the needs
     */
    public final void internalPreBuildSpecificCriteria() {
    	if (!hasBuildSpecificCriteria) {
    		preBuildAssociation();
	    	preBuildSpecificCriteria();
	    	hasBuildSpecificCriteria = true;
    	}
    }
    
    /**
     * Common calculations of mapCriterias
     */
    public final void internalPreBuildCommunCriteria() {
    	if (hasBuildCommunCriteria) {
    		return;
    	}
    	
    	preBuildCommunCriteria();
    	
    	Iterator<String> keys = mapCriterias.keySet().iterator();
    	while (keys.hasNext()) {
			String columnName = keys.next();
			Object[] params = (Object[]) mapCriterias.get(columnName);
			FilterMode comparator = (FilterMode) params[0];
			Object[] values = (Object[]) params[1];
			addCriterion(new FieldFilter(columnName, comparator, values));
		}
    	hasBuildCommunCriteria = true;
    }
    
    
    /**
     * 
     */
    public void preBuildAssociation() {
    	
    }
    
    /**
     * To override following the needs
     */
    public void preBuildSpecificCriteria() {
    	
    }
    
    /**
     * To override following the needs
     */
    public void preBuildCommunCriteria() {
    	if (statusRecordList == null || statusRecordList.size() == 0) {
        	statusRecordList = Arrays.asList(StatusRecord.ACTIV);
        }
    	
    	if (statusRecordList.contains(StatusRecord.ACTIV)) {
    		addCriterion(Restrictions.or(Restrictions.in("statusRecord", statusRecordList), Restrictions.isNull("statusRecord")));
    	} else {
        	addCriterion(Restrictions.in("statusRecord", statusRecordList));
        }
    	
    	if (Boolean.TRUE.equals(orderBySortIndex)) {
    		setOrder(Order.asc("sortIndex"));
    	}
    }
    
    /**
     * 
     * @param entityClass
     */
    public Association addAssociation(Class<? extends Entity> entityClass) {
    	Association assoc = new Association(entityClass);
    	if (associations.contains(assoc)) {
    		throw new IllegalArgumentException("The alias [" + assoc.getAlias() + "] is already used.");
    	}
    	associations.add(assoc);
    	return(assoc);
    }
    
    /**
     * 
     * @param entityClass
     * @param alias
     * @param joinType
     */
    public Association addAssociation(Class<? extends Entity> entityClass, String alias, JoinType joinType) {
    	Association assoc = new Association(entityClass, alias, joinType);
    	if (associations.contains(assoc)) {
    		throw new IllegalArgumentException("The alias [" + alias + "] is already used.");
    	}
    	associations.add(assoc);
    	return(assoc);
    }
    
    
    /**
     * 
     * @param associationPath
     * @param alias
     * @param joinType
     */
    public Association addAssociation(String associationPath, String alias, JoinType joinType) {
    	Association assoc = new Association(associationPath, alias, joinType);
    	if (associations.contains(assoc)) {
    		logger.warn("The alias [" + alias + "] has been already added.");
    	} else {
    		associations.add(assoc);
    	}
    	return(assoc);
    }
    
    /**
     * 
     * @param entityClasses
     */
    public Association addAssociation(Class<? extends Entity>...entityClasses) {
    	return addAssociation(entityClasses[entityClasses.length-1].getSimpleName().toLowerCase(), JoinType.INNER_JOIN, entityClasses);
    }


    /**
     * 
     * @param entityClasses
     * @param alias
     */
    public Association addAssociation(String alias, Class<? extends Entity>...entityClasses) {
    	return addAssociation(alias, JoinType.INNER_JOIN, entityClasses);
    }
    	
    /**
     * 
     * @param entityClasses
     * @param alias
     * @param joinType
     */
    public Association addAssociation(String alias, JoinType joinType, Class<? extends Entity>...entityClasses) {
    	String associationPath = entityClasses[0].getSimpleName().toLowerCase();
    	for (int i = 1; i < entityClasses.length; i++) {
			associationPath += "." + entityClasses[i].getSimpleName().toLowerCase();
		}
    	Association assoc = new Association(associationPath, alias, joinType);
    	if (associations.contains(assoc)) {
    		throw new IllegalArgumentException("The alias [" + alias + "] is already used.");
    	}
    	associations.add(assoc);
    	return assoc;
    }
    
    /**
     * 
     * @param criterion
     */
    public void addCriterion(Criterion criterion) {
    	criterions.add(criterion);
    }
    

    /**
     * 
     * @param order
     */
    public void addOrder(Order order) {
    	orders.add(order);
    }

	/**
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}
	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	/**
	 * @return the associations
	 */
	public List<Association> getAssociations() {
		return associations;
	}
	/**
	 * @param associations the associations to set
	 */
	public void setAssociations(List<Association> associations) {
		this.associations = associations;
	}
	/**
	 * @return the criterions
	 */
	public List<Criterion> getCriterions() {
		return criterions;
	}
	/**
	 * @param criterions the criterions to set
	 */
	public void setCriterions(List<Criterion> criterions) {
		this.criterions = criterions;
	}
	/**
	 * @return the firstResult
	 */
	public Integer getFirstResult() {
		return firstResult;
	}
	/**
	 * @param firstResult the firstResult to set
	 */
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	/**
	 * @return the maxResults
	 */
	public Integer getMaxResults() {
		return maxResults;
	}
	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 
	 * @param order
	 */
	public void setOrder(Order order) {
		orders = new ArrayList<Order>();
		orders.add(order);
	}
	
	/**
	 * 
	 * @return
	 */
	public Order getOrder() {
		if (orders == null || orders.size() == 0) {
			return null;
		}
		return orders.get(0);
	}
	
	/**
	 * 
	 * @param columname
	 * @param comparator
	 * @param value
	 */
	public void addCriterion(String columname, FilterMode comparator, Object...values) {
    	mapCriterias.put(columname, new Object[] { comparator, values });
    }
	
	/**
	 * 
	 * @param columname
	 * @param value
	 */
	public void addCriterion(String columname, Object...values) {
		FilterMode comparator = new FilterMode();
    	mapCriterias.put(columname, new Object[] { comparator.getDefaultFilterMode(), values });
    }

    /**
     * 
     * @param criterion
     */
    private void addCriterion(FieldFilter filter) {
    	Criterion criterion = null;
    	if (filter.getFilterMode().equals(FilterMode.BETWEEN)) {
			criterion = Restrictions.between(filter.getFieldName(), filter.getFieldValue(), filter.getField2Value());
	    	criterions.add(criterion);
		} else if (filter.getFilterMode().equals(FilterMode.IN)) {
			if (filter.getFieldValues()[0] instanceof Collection) {
				criterion = Restrictions.in(filter.getFieldName(), (Collection) filter.getFieldValues()[0]);
			} else if (filter.getFieldValues()[0] instanceof Object[]) {
				criterion = Restrictions.in(filter.getFieldName(), (Object[]) filter.getFieldValues()[0]);
			} else {
			criterion = Restrictions.in(filter.getFieldName(), filter.getFieldValues());
			}
	    	criterions.add(criterion);
		} else {
		    Criterion[] criteria = new Criterion[filter.getFieldValues().length];
			for (int i = 0; i < filter.getFieldValues().length; i++) {
		    	criterion = null;
		    	Object value = filter.getFieldValues()[i];
		    	
				if (filter.getFilterMode().equals(FilterMode.EQUALS)) {
					criterion = Restrictions.eq(filter.getFieldName(), value);
				} else if (filter.getFilterMode().equals(FilterMode.GREATER_THAN)) {
					criterion = Restrictions.gt(filter.getFieldName(), value);
				} else if (filter.getFilterMode().equals(FilterMode.GREATER_OR_EQUALS)) {
					criterion = Restrictions.ge(filter.getFieldName(), value);
				} else if (filter.getFilterMode().equals(FilterMode.LESS_THAN)) {
					criterion = Restrictions.lt(filter.getFieldName(), value);
				} else if (filter.getFilterMode().equals(FilterMode.LESS_OR_EQUALS)) {
					criterion = Restrictions.le(filter.getFieldName(), value);
				} else if (filter.getFilterMode().equals(FilterMode.BLANK)) {
					criterion = Restrictions.eq(filter.getFieldName(), "");
				} else if (filter.getFilterMode().equals(FilterMode.NULL)) {
					criterion = Restrictions.isNull(filter.getFieldName());
				} else if (filter.getFilterMode().equals(FilterMode.EMPTY)) {
					criterion = Restrictions.isEmpty(filter.getFieldName());
				} else if (filter.getFilterMode().equals(FilterMode.NOT_EMPTY)) {
					criterion = Restrictions.isNotEmpty(filter.getFieldName());
				} else if (filter.getFilterMode().equals(StringFilterMode.CONTAINS)) {
					criterion = Restrictions.ilike(filter.getFieldName(), (String) value, MatchMode.ANYWHERE);
				} else if (filter.getFilterMode().equals(StringFilterMode.START_WITH)) {
					criterion = Restrictions.ilike(filter.getFieldName(), (String) value, MatchMode.START);
				} else if (filter.getFilterMode().equals(StringFilterMode.END_WITH)) {
					criterion = Restrictions.ilike(filter.getFieldName(), (String) value, MatchMode.END);
				} else if (filter.getFilterMode().equals(StringFilterMode.DOES_NOT_CONTAIN)) {
					criterion = Restrictions.not(Restrictions.ilike(filter.getFieldName(), (String) value, MatchMode.ANYWHERE));
				} 
		    	criteria[i] = criterion;
			}
			
			criterions.add(Restrictions.or(criteria));
		}
    	
    }
    
	/**
	 * @see org.seuksa.frmk.dao.hql.ColumnInfosAware#getColumnInfos()
	 */
	@Override
	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	
	/**
	 * 
	 * @param colInfo
	 */
	public void addColumnInfo(ColumnInfo colInfo) {
		columnInfos.add(colInfo);
	}


	/**
	 * @param orderBySortIndex the orderBySortIndex to set
	 */
	public void setOrderBySortIndex() {
		this.orderBySortIndex = true;
	}


	@Override
	public List<StatusRecord> getStatusRecordList() {
		if (statusRecordList == null) {
			statusRecordList = new ArrayList<StatusRecord>();
		}
		return statusRecordList;
	}


	@Override
	public void setStatusRecord(List<StatusRecord> statusRecordList) {
		this.statusRecordList = statusRecordList;
	}


	@Override
	public StatusRecord getStatusRecord() {
		return getStatusRecordList() != null && getStatusRecordList().size() > 0 ? getStatusRecordList().get(0) :  null;
	}


	@Override
	public void setStatusRecord(StatusRecord statusRecord) {
		getStatusRecordList().add(statusRecord);
	}
	
	public void setStatusRecordArchived() {
		getStatusRecordList().add(StatusRecord.ARCHI);
	}
	
	public void setStatusRecordActive() {
		getStatusRecordList().add(StatusRecord.ACTIV);
	}
	
	public void setStatusRecordInActive() {
		getStatusRecordList().add(StatusRecord.INACT);
	}
	public void setStatusRecordInProcess() {
		getStatusRecordList().add(StatusRecord.INPRO);
	}
	public void setStatusRecordRecycled() {
		getStatusRecordList().add(StatusRecord.RECYC);
	}
	
}
