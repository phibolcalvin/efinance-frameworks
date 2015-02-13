package org.seuksa.frmk.model.entity;

import java.io.Serializable;


/**
 * Entity with a composite PK of 2 FKs, EntityA
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityC2A extends AbstractEntity {
    /** */
	private static final long serialVersionUID = 8686229634321889390L;


	/**
     * 
     * @return
     */
	public abstract PK2A getPK2A();
    
    
    /**
     * @see org.seuksa.frmk.model.entity.Entity#getPrimaryKey()
     */
    public Serializable getPrimaryKey() {
        return getPK2A();
    }


}
