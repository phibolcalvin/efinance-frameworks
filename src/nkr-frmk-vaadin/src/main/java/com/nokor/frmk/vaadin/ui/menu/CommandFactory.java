/**
 * 
 */
package com.nokor.frmk.vaadin.ui.menu;

import java.io.Serializable;

import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.ui.menu.model.MenuItemEntity;
import com.vaadin.server.Page;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * @author prasnar
 *
 */
public class CommandFactory implements Serializable {
	/** */
	private static final long serialVersionUID = 6121470677357403124L;

	private static final Log logger = Log.getInstance(MenuHelper.class);

	
	/**
	 * 
	 */
	private CommandFactory() {
	}

	
	
	@SuppressWarnings("serial")
	public static Command create(final MenuItemEntity menuItem) {
		return new Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				String viewName = menuItem.getAction();
				Page.getCurrent().setUriFragment("!" + viewName);
			}
		};
	}
	
	
}
