package com.nokor.frmk.vaadin.ui.panel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract search panel
 * @author ly.youhort
 */
public abstract class AbstractSearchPanel<T extends Entity> extends Panel {

	private static final long serialVersionUID = 1758781365223334094L;

	final Log log = LogFactory.getLog(AbstractFormPanel.class);
	
	protected Button btnSearch;
	protected Button btnReset;	
		
	/**
	 * Initialization
	 */
	@SuppressWarnings("serial")
	public AbstractSearchPanel (final String caption, final AbstractTablePanel<T> tablePanel) {
		setCaption(caption);		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setStyleName("panel-search");
		containLayout.setMargin(true);
			
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setStyleName("panel-search-center");
		btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				tablePanel.searchButtonClick(event);
			}
		});
		
		
		btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		containLayout.addComponent(createForm());
		containLayout.addComponent(ComponentFactory.getVerticalLayout(10));
		containLayout.addComponent(buttonsLayout);
		setContent(containLayout);
	}
		
	protected abstract void reset();
	protected abstract Component createForm();
	
	public abstract BaseRestrictions<T> getRestrictions();
}
