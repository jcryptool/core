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
package org.jcryptool.crypto.ui.textblockloader;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

public class TextAsNumbersLoaderWizard extends Wizard {

	protected TANLOriginChooserPage pageInputMethod;
	protected TANLTextPage pageTextToNumbersPage;
	protected TANLBlockConversionPage pageBlockParams;
	protected TANLNumberLoaderPage pageNumbersOnly;
	protected int maxNumber;
	private boolean numbersOnlyInput;
	public static final String METHOD_TEXT_BASED = "METHOD_TEXT_BASED"; //$NON-NLS-1$
	public static final String METHOD_NUMERIC = "METHOD_NUMERIC"; //$NON-NLS-1$

	public TextAsNumbersLoaderWizard(int maxNumber, boolean numbersOnlyInput) {
		this.maxNumber = maxNumber;
		this.numbersOnlyInput = numbersOnlyInput;
		setWindowTitle(Messages.TextAsNumbersLoaderWizard_wtitle_overall);
		
		pageInputMethod = new TANLOriginChooserPage();
		pageTextToNumbersPage = new TANLTextPage(this.maxNumber);
		pageBlockParams = new TANLBlockConversionPage(this.maxNumber);
		pageNumbersOnly = new TANLNumberLoaderPage(this.maxNumber);
	}

	@Override
	public void addPages() {
		addPage(getPageInputMethod());
		addPage(getPageNumbersOnly());
		addPage(getPageTextToNumbersPage());
		addPage(getPageBlockParams());
	}

	@Override
	public boolean performFinish() {
		//TODO: 
		return true;
	}

	@Override
	public IWizardPage getStartingPage() {
		if(this.numbersOnlyInput) {
			return getPageNumbersOnly();
		} else {
			return getPageInputMethod();
		}
	}
	
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		if(this.numbersOnlyInput) {
			if(page == getPageNumbersOnly()) return null;
			return super.getPreviousPage(page);
		} else {
			return super.getPreviousPage(page);
		}
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if(page == getPageInputMethod()) {
			if(getDataInputMethod().equals(TextAsNumbersLoaderWizard.METHOD_NUMERIC)) {
				return getPageNumbersOnly();
			} else {
				return getPageTextToNumbersPage();
			}
		} else if(page == getPageTextToNumbersPage()) {
			return getPageBlockParams();
		} else if(page == getPageBlockParams()) {
			return null;
		} else if(page == getPageNumbersOnly()) {
			return null;
		}
		return null;
	}
	
	@Override
	public boolean canFinish() {
		boolean numbersOnly = getDataInputMethod().equals(METHOD_NUMERIC);
		boolean byAlpha = getDataInputMethod().equals(METHOD_TEXT_BASED);
		
		boolean numbersOnlyComplete = isByNumbersOnlyDataSufficient();
		boolean byAlphaComplete = isByAlphaDataSufficient();
		
		boolean canFinish = (numbersOnly && numbersOnlyComplete) || (byAlpha && byAlphaComplete);
		
		return canFinish;
	}
	
	private boolean isByAlphaDataSufficient() {
		return isNumbersDataComplete(getDataBlocksByText());
	}

	private boolean isByNumbersOnlyDataSufficient() {
		return isNumbersDataComplete(getDataBlocksByNumericInput());
	}
	
	private boolean isNumbersDataComplete(List<Integer> data) {
		return data.size() > 0;
	}

	public TANLOriginChooserPage getPageInputMethod() {
		return pageInputMethod;
	}

	public TANLTextPage getPageTextToNumbersPage() {
		return pageTextToNumbersPage;
	}
	
	public TANLBlockConversionPage getPageBlockParams() {
		return pageBlockParams;
	}
	
	public TANLNumberLoaderPage getPageNumbersOnly() {
		return pageNumbersOnly;
	}
	
	public ConversionCharsToNumbers getCTN() {
		return getPageTextToNumbersPage().getCharsToNumbersConversionInput().getContent();
	}
	
	public ConversionStringToBlocks getSTBConversion() {
		ConversionCharsToNumbers ctn = getCTN();
		ConversionNumbersToBlocks ntb = getPageBlockParams().getBlockMethodInput().getContent();
		ConversionStringToBlocks conversion = new ConversionStringToBlocks(ctn, ntb);
		return conversion;
	}
	
	public String getDataInputMethod() {
		if(this.numbersOnlyInput) return METHOD_NUMERIC;
		
		return getPageInputMethod().getMethod();
	}
	
	public TextInputWithSource getText() {
		return getPageTextToNumbersPage().getTextInput().getContent();
	}

	public List<Integer> getDataBlocksByText() {
		String stringData = getText().getText();
		List<Integer> blocks = getSTBConversion().convert(stringData);
		return blocks;
	}
	public List<Integer> getDataBlocksByNumericInput() {
		return getPageNumbersOnly().getNumbers();
	}
	public List<Integer> getDataBlocks() {
		if(getDataInputMethod().equals(METHOD_NUMERIC)) {
			return getDataBlocksByNumericInput();
		} else {
			return getDataBlocksByText();
		}
	}
	
}
