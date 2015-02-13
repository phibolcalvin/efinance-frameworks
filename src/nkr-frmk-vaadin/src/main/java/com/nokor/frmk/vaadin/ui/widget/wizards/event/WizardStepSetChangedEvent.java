package com.nokor.frmk.vaadin.ui.widget.wizards.event;

import com.nokor.frmk.vaadin.ui.widget.wizards.Wizard;

@SuppressWarnings("serial")
public class WizardStepSetChangedEvent extends AbstractWizardEvent {

    public WizardStepSetChangedEvent(Wizard source) {
        super(source);
    }

}
