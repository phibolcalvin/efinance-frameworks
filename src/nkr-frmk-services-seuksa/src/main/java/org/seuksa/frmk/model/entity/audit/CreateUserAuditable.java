package org.seuksa.frmk.model.entity.audit;

/**
 * CreateUserAuditable
 * @author kong.phirun
 *
 */
public interface CreateUserAuditable {

    String CREATE_USER_PROPERTY = "createUser";

    /**
     * getCreateUser
     * @return String
     */
    String getCreateUser();

    /**
     * setCreateUser
     * @param user
     */
    void setCreateUser(final String user);

}
