package com.nokor.frmk.vaadin.ui.paged.builder;

import java.util.ArrayList;
import java.util.List;

import com.nokor.frmk.vaadin.ui.paged.ComponentsManager;
import com.nokor.frmk.vaadin.ui.paged.PagingComponent;
import com.nokor.frmk.vaadin.ui.paged.button.ButtonPageNavigator;
import com.nokor.frmk.vaadin.ui.paged.customizer.element.ElementsCustomizer;
import com.nokor.frmk.vaadin.ui.paged.utilities.Utils;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * Build the buttons and separators with the help of {@link ElementsCustomizer} object given in parameter to the constructor.
 */
public class ElementsBuilder {

	protected List<ButtonPageNavigator> listButtons;// button corresponding to pages
	protected Button buttonPrevious, buttonNext, buttonFirst, ButtonLast;
	protected Component firstSeparator, secondSeparator;
	
	public ElementsBuilder(ComponentsManager manager, ElementsCustomizer elementsCustomizer) {
		listButtons = new ArrayList<ButtonPageNavigator>();
		
		if (manager.getNumberTotalOfPages() == 1) {
			firstSeparator = elementsCustomizer.createFirstSeparator();
			secondSeparator = elementsCustomizer.createLastSeparator();
			listButtons.add(elementsCustomizer.createButtonPage());
		} else if (manager.getNumberTotalOfPages() > 1) {
			PagingComponent<?> pagingComponent = manager.getPagingComponent();
			
			firstSeparator = elementsCustomizer.createFirstSeparator();
			secondSeparator = elementsCustomizer.createLastSeparator();
			
			buttonFirst = elementsCustomizer.createButtonFirst();
			Utils.addListener(buttonFirst, pagingComponent);
			
			ButtonLast = elementsCustomizer.createButtonLast();
			Utils.addListener(ButtonLast, pagingComponent);
			
			buttonNext = elementsCustomizer.createButtonNext();
			Utils.addListener(buttonNext, pagingComponent);
			
			buttonPrevious = elementsCustomizer.createButtonPrevious();
			Utils.addListener(buttonPrevious, pagingComponent);
			
			for (int i = 0; i < manager.getNumberOfButtonsPage() && i < manager.getNumberTotalOfPages(); i++) {
				ButtonPageNavigator button = elementsCustomizer.createButtonPage();
				button.setManager(manager);
				button.addClickListener(pagingComponent);
				listButtons.add(button);
			}
		}
	}
	
	public List<ButtonPageNavigator> getListButtons() {
		return listButtons;
	}

	public Button getButtonPrevious() {
		return buttonPrevious;
	}

	public Button getButtonNext() {
		return buttonNext;
	}

	public Button getButtonFirst() {
		return buttonFirst;
	}

	public Button getButtonLast() {
		return ButtonLast;
	}

	public Component getFirstSeparator() {
		return firstSeparator;
	}

	public Component getLastSeparator() {
		return secondSeparator;
	}
	
}
