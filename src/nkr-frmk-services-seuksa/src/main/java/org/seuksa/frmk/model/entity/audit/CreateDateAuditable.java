package org.seuksa.frmk.model.entity.audit;

import java.util.Date;

/**
 * CreateDateAuditable
 * @author kong.phirun
 *
 */
public interface CreateDateAuditable {

    String CREATE_DATE_PROPERTY = "createDate";

    /**
     * getCreateDate
     * @return Date
     */
    Date getCreateDate();

    /**
     * setCreateDate
     * @param date
     */
    void setCreateDate(final Date date);

}
