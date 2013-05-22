package org.jcryptool.crypto.analysis.substitution.ui.wizard.loadtext;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

public class LoadTextWizard extends Wizard {

	private LoadTextWizardPage firstPage;
	private TranspTextModifyPage page2;
	private boolean finished = false;
	private TextInputWithSource textPageConfig;
	private TransformData transformData;

	public LoadTextWizard() {
		setWindowTitle("Load text");
		firstPage = new LoadTextWizardPage();
		page2 = new TranspTextModifyPage();
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public final void addPages() {
		addPage(firstPage);
		addPage(page2);
	}

	public TextInputWithSource getTextPageConfig() {
		if (!finished) return firstPage.getPageConfiguration();
		else return textPageConfig;
	}

	public TransformData getTransformData() {
		if (!finished) return page2.getSelectedData();
		else return transformData;
	}

	private void saveResults() {
		this.transformData = getTransformData();
		this.textPageConfig = getTextPageConfig();
	}

	public void setTextPageConfig(TextInputWithSource config) {
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
		finished = true;
		return true;
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public final boolean performCancel() {
		saveResults();
		finished = true;
		return true;
	}
}
