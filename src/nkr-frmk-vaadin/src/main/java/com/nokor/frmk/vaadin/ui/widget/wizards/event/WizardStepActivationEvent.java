package com.nokor.frmk.vaadin.ui.widget.wizards.event;

import com.nokor.frmk.vaadin.ui.widget.wizards.Wizard;
import com.nokor.frmk.vaadin.ui.widget.wizards.WizardStep;

@SuppressWarnings("serial")
public class WizardStepActivationEvent extends AbstractWizardEvent {

    private final WizardStep activatedStep;

    /**
     * @param source
     * @param activatedStep
     */
    public WizardStepActivationEvent(Wizard source, WizardStep activatedStep) {
        super(source);
        this.activatedStep = activatedStep;
    }

    /**
     * Returns the {@link VWizardStep} that was the activated.
     * 
     * @return the activated {@link VWizardStep}.
     */
    public WizardStep getActivatedStep() {
        return activatedStep;
    }

}