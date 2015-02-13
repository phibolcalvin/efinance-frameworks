package com.nokor.frmk.vaadin.ui.widget.wizards.event;

import com.nokor.frmk.vaadin.ui.widget.wizards.Wizard;

@SuppressWarnings("serial")
public class WizardCompletedEvent extends AbstractWizardEvent {

    public WizardCompletedEvent(Wizard source) {
        super(source);
    }

}
