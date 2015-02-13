package com.nokor.frmk.vaadin.ui.panel;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nokor.frmk.helper.ServicesCoreHelper;
import com.nokor.frmk.helper.SettingConfigHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract form panel
 * @author kong.phirun
 */
public abstract class AbstractFormMainTabPanel extends AbstractControlPanel implements ServicesCoreHelper {

	/**	 */
	private static final long serialVersionUID = 5785719662652431138L;

	protected final Log logger = LogFactory.getLog(AbstractFormMainTabPanel.class);
	
	private VerticalLayout messagePanel;
	private Long entityId;
	
	
	/**
	 * Initialization
	 */
	public void init() {
		setMargin(true);
		setSpacing(true);
				
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		addComponent(messagePanel);		
		addComponent(createForm());
	}
	
	/**
	 * @return
	 */
	public NavigationPanel addNavigationPanel() {
		return addNavigationPanel(0);
	}
	
	/**
	 * @return
	 */
	public NavigationPanel addNavigationPanel(int index) {
		NavigationPanel navigationPanel = new NavigationPanel(); 
		addComponent(navigationPanel, index);
		return navigationPanel;
	}	
	
	/**
	 * Display title info message
	 */
	public void displayTitleInfo(String entityName, String entityTitle, String lastUpdatedUser, String dateInfo) {
		String leftTitle = I18N.message(entityName) + " : <b>" + entityTitle + "</b>";
		Label messageLabel = new Label(leftTitle, ContentMode.HTML);
		messageLabel.addStyleName("info");
		
		String strLastModification = I18N.message("last_modification_info", lastUpdatedUser, dateInfo);
		String rightTitle = "<i>" + strLastModification + "</i>";
		Label dateInfoLabel = new Label(rightTitle, ContentMode.HTML);
		dateInfoLabel.addStyleName("info_date");
		messagePanel.removeAllComponents();
		messagePanel.addComponent(new HorizontalLayout(messageLabel, dateInfoLabel));
		messagePanel.setVisible(true);
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		messagePanel.setVisible(false);
		errors.clear();
	}
		
	protected abstract Component createForm();
	
	protected abstract void disableTabs();
	
	protected abstract void enableTabs();

		
	/**
	 * 
	 * @param templateName
	 * @return
	 */
	protected CustomLayout initCustomLayout(String templateName) {
		if (StringUtils.isEmpty(SettingConfigHelper.getVaadinCustomLayoutsFolder())) {
			throw new IllegalArgumentException("The Vaadin custom layouts folder is not configured yet.");
		}
		String fullPath = SettingConfigHelper.getVaadinCustomLayoutsFolder() + templateName;
		logger.debug("Customer layout: [" + fullPath + "]");
		
		InputStream layoutFile = getClass().getResourceAsStream(fullPath);
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (Exception e) {
			String errMsg = "Could not locate template " + templateName;
			logger.error(errMsg);
			Notification.show(errMsg, e.getMessage(), Type.ERROR_MESSAGE);
		}
		return customLayout;
	}
	
	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
