package com.nokor.frmk.vaadin.ui.panel;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nokor.frmk.helper.ServicesCoreHelper;
import com.nokor.frmk.helper.SettingConfigHelper;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Button.ClickEvent;
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
public abstract class AbstractFormTabPanel extends AbstractControlPanel implements SaveClickListener, ServicesCoreHelper {

	/**	 */
	private static final long serialVersionUID = -809238081053948031L;

	protected final Log logger = LogFactory.getLog(AbstractFormTabPanel.class);
	
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
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		messagePanel.setVisible(false);
		errors.clear();
		if (validate()) {
			saveEntity();
			displaySuccess();
			if (getParent() instanceof TabSheet) {
				((TabSheet) getParent()).setNeedRefresh(true);
			}
		} else {
			displayErrors();
		}
	}

	/**
	 * Display errors messages
	 */
	public void displayErrors() {
		messagePanel.removeAllComponents();
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}
	
	/**
	 * Display success message
	 */
	public void displaySuccess() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		messagePanel.removeAllComponents();
		messagePanel.addComponent(new HorizontalLayout(messageLabel));
		messagePanel.setVisible(true);
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		entityId = null;
		messagePanel.setVisible(false);
		errors.clear();
	}
		
	/**
	 * @return
	 */
	protected boolean validate() {
		return true;
	}

	public abstract void assignValuesToControls(Long entityId);
	protected abstract void saveEntity();
	protected abstract Component createForm();

		
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
