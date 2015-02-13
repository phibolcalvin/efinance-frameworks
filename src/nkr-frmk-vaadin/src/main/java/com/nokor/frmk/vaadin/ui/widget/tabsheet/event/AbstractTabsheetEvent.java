package com.nokor.frmk.vaadin.ui.widget.tabsheet.event;

import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class AbstractTabsheetEvent extends Component.Event {

    /**
     * @param source
     */
	protected AbstractTabsheetEvent(TabSheet source) {
        super(source);
    }

    /**
     * Returns the {@link TabSheet} component that was the source of this event.
     * 
     * @return the source {@link TabSheet} of this event.
     */
    public TabSheet getWizard() {
        return (TabSheet) getSource();
    }
}
