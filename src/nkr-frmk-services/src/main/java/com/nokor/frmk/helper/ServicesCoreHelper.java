package com.nokor.frmk.helper;

import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.frmk.security.service.AuthenticationService;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.service.ReferenceTableService;
import com.nokor.frmk.service.SettingService;
import com.nokor.frmk.ui.menu.service.MenuService;

/**
 * 
 * @author prasnar
 *
 */
public interface ServicesCoreHelper {
    EntityService ENTITY_SRV = SpringUtils.getBean(EntityService.class);
    ReferenceTableService REFTABLE_SRV = SpringUtils.getBean(ReferenceTableService.class);
    SettingService SETTING_SRV = SpringUtils.getBean(SettingService.class);
    SecurityService SECURITY_SRV = SpringUtils.getBean(SecurityService.class);
    AuthenticationService AUTHENTICAT_SRV = SpringUtils.getBean(AuthenticationService.class);

    public static MenuService MENU_SRV = SpringUtils.getBean(MenuService.class);

}
