package org.seuksa.frmk.model.entity.audit;

import java.util.Date;

/**
 * UpdateDateAuditable
 * @author kong.phirun
 *
 */
public interface UpdateDateAuditable {

    String UPDATE_DATE_PROPERTY = "updateDate";

    /**
     * getUpdateDate
     * @return Date
     */
    Date getUpdateDate();

    /**
     * setUpdateDate
     * @param date
     */
    void setUpdateDate(final Date date);

}
