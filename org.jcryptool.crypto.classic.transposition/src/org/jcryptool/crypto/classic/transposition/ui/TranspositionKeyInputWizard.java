//-----BEGIN DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.ui;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionKeyInputWizardPage.PageConfiguration;

/**
 * The wizard for the transformation settings
 * 
 * @author SLeischnig
 * 
 */
public class TranspositionKeyInputWizard extends Wizard {

	private TranspositionKeyInputWizardPage firstPage;

	private boolean finished = false;
	private PageConfiguration firstPageConfig;

	public TranspositionKeyInputWizard() {
		setWindowTitle("Transposition key");
		firstPage = new TranspositionKeyInputWizardPage();
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public final void addPages() {
		addPage(firstPage);
	}

	public PageConfiguration getPageConfig() {
		if (!finished) return firstPage.getPageConfiguration();
		else return firstPageConfig;
	}


	private void saveResults() {
		this.firstPageConfig = getPageConfig();
		finished = true;
	}

	public void setPageConfig(PageConfiguration config) {
		firstPage.setPageConfiguration(config);
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
