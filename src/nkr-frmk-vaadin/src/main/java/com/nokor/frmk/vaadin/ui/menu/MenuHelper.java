package com.nokor.frmk.vaadin.ui.menu;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.ui.menu.model.MenuItemEntity;
import com.nokor.frmk.vaadin.util.VaadinServicesCoreHelper;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;

/**
 * @author prasnar
 *
 */
public class MenuHelper implements VaadinServicesCoreHelper {
	private static final Log logger = Log.getInstance(MenuHelper.class);

	public static final String MAIN_MENU_CODE = "MAIN";
	public static final String MENU_SEPARATOR = "menu.separator";
	

	/**
	 * 
	 */
	private MenuHelper() {
	}

	
	/**
	 * 
	 * @return
	 */
    public static MenuBar buildMenuBar() {
    	return buildMenuBar(MAIN_MENU_CODE);
    }
	
    /**
     * 
     * @param mainMenuCode
     * @return
     */
    public static MenuBar buildMenuBar(String mainMenuCode) {
    	MenuBar menubar = new MenuBar();
    	List<MenuItemEntity> menuItems = MENU_SRV.findMenuItemList(mainMenuCode);

        for (MenuItemEntity menuItem : menuItems) {
            final String name = menuItem.getCode();
            MenuBar.MenuItem uiMenuItem = menubar.addItem(I18N.message(name), null);
            if (StringUtils.isNotEmpty(menuItem.getIconPath())) {
            	uiMenuItem.setIcon(new ThemeResource(menuItem.getIconPath()));
//            	uiMenuItem.setStyleName(menuItem.getIconPath());
            }

            addSubMenus(uiMenuItem, menuItem.getChildren());
        }
        return menubar;
    }

    /**
     * 
     * @param uiMenuItem
     * @param children
     */
    private static void addSubMenus(MenuBar.MenuItem uiMenuItem, List<MenuItemEntity> children) {
        for (MenuItemEntity child : children) {
            final String childName = child.getCode();
            if (childName.equals(MENU_SEPARATOR)) {
                uiMenuItem.addSeparator();
            } else {
                MenuBar.Command command = null;
                if (child.getChildren().isEmpty()) {
                    command = CommandFactory.create(child);
                }
                MenuBar.MenuItem menuItem1 = uiMenuItem.addItem(childName, command);
                if (StringUtils.isNotEmpty(child.getIconPath())) {
                	menuItem1.setIcon(new ThemeResource(child.getIconPath()));
                }
                addSubMenus(menuItem1, child.getChildren());
            }
        }
    }

}
