package com.nokor.frmk.ui.menu.service;

import java.util.List;

import com.nokor.frmk.ui.menu.MenuItemParameters;
import com.nokor.frmk.ui.menu.MenuParameters;
import com.nokor.frmk.ui.menu.model.MenuEntity;
import com.nokor.frmk.ui.menu.model.MenuItemEntity;

/**
 * Menu service functionality to manage menu and menu items.
 * @author prasnar
 */
public interface MenuService {
    /**
     * Finds menu items list by key.
     *
     * @param menuKey - menu key constant
     * @return list of menu items
     */
    public List<MenuItemEntity> findMenuItemList(String menuKey);


    /**
     * Finds menu item by item id.
     *
     * @param itemId - menu item id
     * @return menu item
     */
    public MenuItemEntity findMenuItem(Long itemId);

    /**
     * Updates given menu item with data from parameters.
     *
     * @param itemId             - menu item id
     * @param menuItemParameters menu item parameters
     */
    public void saveMenuItem(Long itemId, MenuItemParameters menuItemParameters);

    /**
     * Creates menu item within given menu as sibling to given menu item and data from menu item parameters
     *
     * @param menuId             - menu id
     * @param parentItemId       - parent menu item id
     * @param menuItemParameters menu item parameters
     * @return menu item object
     */
    public MenuItemEntity addMenuItem(Long menuId, Long parentItemId, MenuItemParameters menuItemParameters);

    /**
     * Deletes menu item by given id
     *
     * @param menuItemId - menu item id
     */
    public void deleteMenuItem(Long menuItemId);

    /**
     * Finds menu by given key
     *
     * @param menuKey - menu key
     * @return menu object
     */
    public MenuEntity findMenu(String menuKey);

    /**
     * Finds list of menus
     *
     * @return list of menus
     */
    public List<MenuEntity> findAllMenus();

    /**
     * Creates menu from given parameters.
     *
     * @param parameters - menu parameters
     * @return menu object
     */
    public MenuEntity createMenu(MenuParameters parameters);

    /**
     * Updates menu from given menu parameters
     *
     * @param parameters - menu parameters
     */
    public void updateMenu(MenuParameters parameters);

    /**
     * Deletes menu for given key.
     *
     * @param key - menu
     * @throws EntityDeleteException
     */
    public void deleteMenu(String key);

    /**
     * Finds menu item by event.
     *
     * @param eventName - event name
     * @return - menu item
     */
    public MenuItemEntity findMenuItemByEvent(String eventName);
}
