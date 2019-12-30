//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.alphabets.customalphabets;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.crypto.ui.alphabets.customalphabets.customhistory.CustomAlphabetHistoryManager;

public class CustomAlphabetWizard extends Wizard {
	
	public static final String USE_HISTORY_ALPHABET = "use history"; //$NON-NLS-1$
	public static final String MAKE_NEW_ALPHABET = "make custom alphabet"; //$NON-NLS-1$
	
	CreateCustomAlphabetIntroPage page1;
	CreateCustomAlphabetsWizardPage page2;
	
	private String alphaSelectMode = MAKE_NEW_ALPHABET;
	private boolean historyAlphaPermanence = false;
	private boolean customAlphaPermanence = false;
	
	private AbstractAlphabet selectedAlphabet = null;
	
	public CustomAlphabetWizard() {
		setHelpAvailable(false);
		setWindowTitle(Messages.CustomAlphabetWizard_windowtitle);
		page1 = new CreateCustomAlphabetIntroPage();
		page2 = new CreateCustomAlphabetsWizardPage();
	}
	
	@Override
	public boolean isHelpAvailable() {
		return false;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if(getAlphaSelectMode() == USE_HISTORY_ALPHABET && page == page1) 
			return null;
		
		return super.getNextPage(page);
	}
	
	@Override
	public boolean canFinish() {
		return (page1!=null && page1.getNextPage() == null) || (page2!=null&&page2.isPageComplete());
	}
	
	
	@Override
	public void addPages() {
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		if(getContainer().getCurrentPage() == page2) {
			setWizardSelectedAlphabet(page2.getAlphabetInput().getContent());
		}

		//TODO: is this the right place?
		executeFinalizeOperation();
		return true;
	}

	public void setAlphaSelectMode(String alphaSelectMode) {
		this.alphaSelectMode = alphaSelectMode;
		if(page1!=null && page2!= null && getContainer().getCurrentPage() != null) getContainer().updateButtons();
	}

	public void setHistoryAlphaPermanence(boolean b) {
		this.historyAlphaPermanence = b;
	}

	public void setCustomAlphaPermanence(boolean b) {
		this.customAlphaPermanence = b;
	}

	public void setWizardSelectedAlphabet(AbstractAlphabet abstractAlphabet) {
		this.selectedAlphabet = abstractAlphabet;
	}

	public boolean isHistoryAlphaPermanence() {
		return historyAlphaPermanence;
	}

	public boolean isCustomAlphaPermanence() {
		return customAlphaPermanence;
	}

	public AbstractAlphabet getAlphabet() {
		return selectedAlphabet;
	}

	public String getAlphaSelectMode() {
		return alphaSelectMode;
	}
	
	/**
	 * Signalizes that the overlying operation has finished successfully, 
	 * so that the create/selected alphabet will be saved (if this was chosen 
	 * in the wizard; if not, nothing happens);
	 */
	public void executeFinalizeOperation() {
		if(getAlphaSelectMode() == MAKE_NEW_ALPHABET) {
			if(isCustomAlphaPermanence()) {
				saveAlphabet(getAlphabet());
			}
			CustomAlphabetHistoryManager.customAlphabets.add(getAlphabet());
		}
		if((getAlphaSelectMode() == USE_HISTORY_ALPHABET && isHistoryAlphaPermanence())) {
			saveAlphabet(getAlphabet());
		}
	}

	private static void saveAlphabet(AbstractAlphabet alpha) {
		AlphabetsManager.getInstance().addAlphabet(alpha);
        // saving
	}
	
}
