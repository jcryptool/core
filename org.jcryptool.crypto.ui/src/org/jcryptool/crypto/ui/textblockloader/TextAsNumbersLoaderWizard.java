package org.jcryptool.crypto.ui.textblockloader;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

public class TextAsNumbersLoaderWizard extends Wizard {

	private TANLOriginChooserPage pageInputMethod;
	private TANLTextPage pageTextToNumbersPage;
	private TANLBlockConversionPage pageBlockParams;
	private int maxNumber;

	public TextAsNumbersLoaderWizard(int maxNumber) {
		this.maxNumber = maxNumber;
		setWindowTitle("New Wizard");
		
		pageInputMethod = new TANLOriginChooserPage();
		pageTextToNumbersPage = new TANLTextPage(this.maxNumber);
		pageBlockParams = new TANLBlockConversionPage(this.maxNumber);
	}

	@Override
	public void addPages() {
		addPage(getPageInputMethod());
		addPage(getPageTextToNumbersPage());
		addPage(getPageBlockParams());
	}

	@Override
	public boolean performFinish() {
		//TODO: 
		return true;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
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
	
	public ConversionCharsToNumbers getCTN() {
		return getPageTextToNumbersPage().getCharsToNumbersConversionInput().getContent();
	}
	
	public ConversionStringToBlocks getSTBConversion() {
		ConversionCharsToNumbers ctn = getCTN();
		ConversionNumbersToBlocks ntb = getPageBlockParams().getBlockMethodInput().getContent();
		ConversionStringToBlocks conversion = new ConversionStringToBlocks(ctn, ntb);
		return conversion;
	}
	
	public boolean isDataByTextMethod() {
		return getPageInputMethod().getMethod() == TANLOriginChooserPage.METHOD_TEXT_BASED;
	}
	
	public TextInputWithSource getText() {
		return getPageTextToNumbersPage().getTextInput().getContent();
	}

	public List<Integer> getDataBlocks() {
		String stringData = getText().getText();
		List<Integer> blocks = getSTBConversion().convert(stringData);
		return blocks;
	}
	
	
}
