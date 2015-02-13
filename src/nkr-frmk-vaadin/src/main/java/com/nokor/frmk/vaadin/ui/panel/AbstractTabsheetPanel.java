package com.nokor.frmk.vaadin.ui.panel;

import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract panel for entity
 * 
 * @author ly.youhort
 *
 */
public abstract class AbstractTabsheetPanel extends VerticalLayout {
	private static final long serialVersionUID = -5095697526484655321L;

	protected Log logger = Log.getInstance(getClass());
	
	private TabSheet tabSheet;
	
	/**
	 * Build the Tabsheet panel
	 */
	@SuppressWarnings("serial")
	protected void init() {
		tabSheet = new TabSheet();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
				
		verticalLayout.addComponent(tabSheet);
		verticalLayout.setExpandRatio(tabSheet, 1.0f);
		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1f);
		
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component selectedTab = event.getTabSheet().getSelectedTab();
				if (selectedTab != tabSheet.getTablePanel()) {
					Item selectedItem = ((SelectedItem) tabSheet.getTablePanel()).getSelectedItem();
					if (selectedItem == null && !tabSheet.isAdd() && !tabSheet.isForceSelected()) {
						tabSheet.setSelectedTab(0);
						MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"),
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.setWidth("300px");
						mb.show();
						return;
					}
				} else {
					tabSheet.removeFormsPanel();
					initSelectedTab(selectedTab);
				}
			}
		});
	}
	
	/**
	 * Get tabSheet
	 * @return
	 */
	public TabSheet getTabSheet() {
		return tabSheet;
	}
	
	
	public abstract void onAddEventClick();
	public abstract void onEditEventClick();
	public abstract void initSelectedTab(Component selectedTab);

}


