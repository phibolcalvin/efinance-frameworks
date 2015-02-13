package org.seuksa.frmk.model.entity.audit;

/**
 * UpdateUserAuditable
 * @author kong.phirun
 *
 */
public interface UpdateUserAuditable {

    String UPDATE_USER_PROPERTY = "updateUser";

    /**
     * getUpdateUser
     * @return
     */
    String getUpdateUser();

    /**
     * setUpdateUser
     * @param user
     */
    void setUpdateUser(final String user);

}
