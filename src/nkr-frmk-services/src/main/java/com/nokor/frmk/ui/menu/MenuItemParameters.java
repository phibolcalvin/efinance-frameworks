package com.nokor.frmk.ui.menu;

/**
 * Menu item parameters.
 */
public interface MenuItemParameters {
    /**
     * Menu item name
     *
     * @return Menu item name
     */
    public String getName();

    /**
     * Sets menu item name
     *
     * @param name menu item name
     */
    public void setName(String name);

    /**
     * Gets event name
     *
     * @return event name
     */
    public String getEvent();

    /**
     * Sets event menu
     *
     * @param event - sets event
     */
    public void setEvent(String event);

    /**
     * Order by
     *
     * @return integer value indicating order of current menu item
     */
    public Integer getOrderBy();

    /**
     * Sets order by
     *
     * @param orderBy - integer value indicating order of current menu item
     */
    public void setOrderBy(Integer orderBy);


  
}
