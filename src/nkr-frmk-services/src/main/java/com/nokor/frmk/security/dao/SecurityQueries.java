package com.nokor.frmk.security.dao;


import java.util.List;

import com.nokor.frmk.annotation.QueryResource;
import com.nokor.frmk.dao.Queries;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 */
public interface SecurityQueries extends Queries {
	@QueryResource(named = "find.user.active.by.username.active")
    public SecUser loadUserByUsername(String username);

    @QueryResource(named = "find.profile.by.procode")
	public SecProfile findProfileByCode(String secProCode);

    @QueryResource(named = "find.profiles.by.userid")
    public List<SecProfile> findProfilesByUser(Long secUserId);


}
