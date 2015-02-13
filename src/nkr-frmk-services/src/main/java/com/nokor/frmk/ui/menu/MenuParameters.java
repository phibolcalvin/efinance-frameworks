package com.nokor.frmk.ui.menu;

/**
 * Menu parameters.
 *
 * @author prasnar
 */
public interface MenuParameters {

	public String getKey();
    public void setKey(String key);
	
    /**
     * Gets menu name
     *
     * @return menu name
     */
    public String getName();

    /**
     * Sets menu name
     *
     * @param name - menu name
     */
    public void setName(String name);

    /**
     * Gets menu type id
     *
     * @return - menu type id
     */
    public Long getMenuTypeId();

    /**
     * Sets menu type id
     *
     * @param menuTypeId - menu type id
     */
    public void setMenuTypeId(Long menuTypeId);
}
