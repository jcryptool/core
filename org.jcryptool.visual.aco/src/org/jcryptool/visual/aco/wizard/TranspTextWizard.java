//-----BEGIN DISCLAIMER-----
package org.jcryptool.visual.aco.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.visual.aco.wizard.TranspTextWizardPage.PageConfiguration;

/**
 * The wizard for the transformation settings
 * 
 * @author SLeischnig
 * 
 */
public class TranspTextWizard extends Wizard {

	private TranspTextWizardPage firstPage;
	private TranspTextModifyPage page2;

	private boolean finished = false;
	private PageConfiguration textPageConfig;
	/**
	 * Creates a new instance of CaesarWizard.
	 * 
	 * @param alphabets
	 *            the alphabets to be displayed in the alphabet box
	 * @param defaultAlphabet
	 *            the name of the default alphabet (the selected entry in the
	 *            alphabet combo box) - if the alphabet is not found, the first
	 *            Alphabet is used
	 */
	public TranspTextWizard(int blockLength) {
		setWindowTitle(Messages.TranspTextWizard_textwizard);
		firstPage = new TranspTextWizardPage(blockLength);
		page2 = new TranspTextModifyPage();
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public final void addPages() {
		addPage(firstPage);
	}

	public TranspTextWizardPage.PageConfiguration getTextPageConfig() {
		if (!finished) return firstPage.getPageConfiguration();
		else return textPageConfig;
	}


	private void saveResults() {
		this.textPageConfig = getTextPageConfig();
		finished = true;
	}

	public void setTextPageConfig(TranspTextWizardPage.PageConfiguration config) {
		firstPage.setPageConfiguration(config);
	}

	public void setTransformData(TransformData transformData) {
		page2.setSelectedData(transformData);
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public final boolean performFinish() {
		saveResults();
		return true;
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public final boolean performCancel() {
		saveResults();
		return true;
	}

}
