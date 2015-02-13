package com.nokor.frmk.vaadin.ui.widget.wizards.event;

import com.nokor.frmk.vaadin.ui.widget.wizards.Wizard;

@SuppressWarnings("serial")
public class WizardCancelledEvent extends AbstractWizardEvent {

    public WizardCancelledEvent(Wizard source) {
        super(source);
    }

}
