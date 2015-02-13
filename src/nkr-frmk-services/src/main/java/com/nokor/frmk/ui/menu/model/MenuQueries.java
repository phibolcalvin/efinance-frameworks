package com.nokor.frmk.ui.menu.model;


import java.util.List;

import com.nokor.frmk.annotation.QueryResource;

/**
 * Menu queries
 */
public interface MenuQueries {
    /**
     * Finds menu items by menu id
     *
     * @param menuId - menu id
     * @return list of menu items
     */
    @QueryResource(named = "find.all.menu.items.by.menu")
    public List<MenuItemEntity> findMenuItems(String menuId);

    /**
     * Finds all menus
     *
     * @return list of menus
     */
    @QueryResource(named = "find.all.menus")
    public List<MenuEntity> findAllMenus();

    /**
     * Finds menu by key.
     *
     * @param key - menu key
     * @return found menu
     */
    @QueryResource(named = "find.menu.by.key")
    public MenuEntity findMenu(String key);

    /**
     * Finds menu item by given event.
     *
     * @param eventName - event name
     * @return found menu item
     */
    @QueryResource(named = "find.menu.item.by.event")
    public MenuItemEntity findMenuItemByEvent(String eventName);
}
