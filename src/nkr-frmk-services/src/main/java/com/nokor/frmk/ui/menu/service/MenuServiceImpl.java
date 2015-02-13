package com.nokor.frmk.ui.menu.service;


import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.model.EntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.frmk.ui.menu.MenuItemParameters;
import com.nokor.frmk.ui.menu.MenuParameters;
import com.nokor.frmk.ui.menu.model.MenuEntity;
import com.nokor.frmk.ui.menu.model.MenuItemEntity;
import com.nokor.frmk.ui.menu.model.MenuQueries;
import com.nokor.frmk.ui.menu.model.TypeMenu;

/**
 * 
 * @author prasnar
 *
 */
@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private EntityDao dao;
    @Autowired
    private MenuQueries menuQueries;
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItemEntity> findMenuItemList(String menuKey) {
//    	Query query = createQuery("select mi from MenuItemEntity mi where mi.menu.code = ? and mi.parent is null");
//    	query.setParameter(0, menuKey);
//    	List<MenuItemEntity>  lst = query.list();
//    	return lst;
        return menuQueries.findMenuItems(menuKey);
    }

    /**
     * 
     * @param menuItems
     * @param allowedEvents
     * @return
     */
    private boolean filterItems(List<MenuItemEntity> menuItems, List<String> allowedEvents) {
        boolean contains = false;
        for (Iterator<MenuItemEntity> iterator = menuItems.iterator(); iterator.hasNext(); ) {
            boolean conto = false;
            MenuItemEntity menuItem = iterator.next();
            if (menuItem.getParent() != null) {
	            final List<MenuItemEntity> children = menuItem.getChildren();
	            final boolean notEmpty = CollectionUtils.isNotEmpty(children);
	            if (notEmpty) {
	                conto = filterItems(children, allowedEvents);
	            }
	            final String event = menuItem.getAction();
	            if (event != null) {
	                if (allowedEvents.contains(event)) {
	                    conto = true;
	                }
	            }
	            if (!conto) {
	                iterator.remove();
	            }
	            contains |= conto;
            }
        }
        return contains;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuEntity findMenu(String menuKey) {
        return menuQueries.findMenu(menuKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuEntity> findAllMenus() {
        return menuQueries.findAllMenus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItemEntity findMenuItem(Long itemId) {
        return dao.getById(MenuItemEntity.class, itemId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMenuItem(Long itemId, MenuItemParameters menuItemParameters) {
        MenuItemEntity dest = dao.getById(MenuItemEntity.class, itemId);
        dest.setCode(menuItemParameters.getName());
        dest.setAction(menuItemParameters.getEvent());

        dao.merge(dest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItemEntity addMenuItem(Long menuId,Long parentItemId, MenuItemParameters menuItemParameters) {
        final MenuEntity menu = dao.getById(MenuEntity.class, menuId);
        final MenuItemEntity menuItem = new MenuItemEntity();
        MenuItemEntity parentMenuItem = null;
        if (parentItemId != null) {
            parentMenuItem = dao.getById(MenuItemEntity.class, parentItemId);
        }
        menuItem.setMenu(menu);
        menuItem.setSortIndex(1);
        menuItem.setParent(parentMenuItem);
        menuItem.setCode(menuItemParameters.getName());
        menuItem.setAction(menuItemParameters.getEvent());

        dao.update(menuItem);
        return menuItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMenuItem(Long menuItemId) {
    	MenuItemEntity menuItem  = dao.getById(MenuItemEntity.class, menuItemId);
    	if (menuItem != null) {
    		dao.delete(menuItem);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuEntity createMenu(MenuParameters parameters) {
        final MenuEntity menu = EntityFactory.createInstance(MenuEntity.class);
        menu.setCode(parameters.getKey());
        menu.setDesc(parameters.getName());
        TypeMenu menuType = dao.getById(TypeMenu.class, parameters.getMenuTypeId());
        menu.setMenuType(menuType);
        dao.create(menu);
        return menu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMenu(MenuParameters parameters) {
        final MenuEntity menu = findMenu(parameters.getKey());
        menu.setCode(parameters.getKey());
        menu.setCode(parameters.getName());
        TypeMenu menuType = dao.getById(TypeMenu.class, parameters.getMenuTypeId());
        menu.setMenuType(menuType);
        dao.update(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMenu(String key) {
        final MenuEntity menu = findMenu(key);
        final List<MenuItemEntity> menuItems = menuQueries.findMenuItems(key);
        if (CollectionUtils.isNotEmpty(menuItems)) {
//            throw new T9nException(Translations.ENTITY_CANNOT_BE_DELETED,
//                    menu.getCode(),
//                    messageSource.getMessage(Translations.MENU_ITEM));
        }
        dao.delete(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItemEntity findMenuItemByEvent(String eventName) {
        return menuQueries.findMenuItemByEvent(eventName);
    }

	
}
