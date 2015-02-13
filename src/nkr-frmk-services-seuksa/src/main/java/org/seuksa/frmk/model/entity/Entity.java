package org.seuksa.frmk.model.entity;

import java.io.Serializable;

/**
 * Abstract entity
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface Entity extends Serializable, Cloneable {
	Serializable getPrimaryKey();
}
