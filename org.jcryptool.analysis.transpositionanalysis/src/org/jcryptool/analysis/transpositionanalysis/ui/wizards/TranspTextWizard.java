//-----BEGIN DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;


/**
 * The wizard for the transformation settings
 *
 * @author SLeischnig
 *
 */
public class TranspTextWizard extends Wizard {

	private TranspTextWizardPage firstPage;
	private TranspTextModifyPage page2;

	private boolean crop;
	private int croplength;
	private int blocklength;
	private String text;

	private boolean finished = false;
	private String editorFileName;
	private int manualInputState;

	/**
	 * Creates a new instance of CaesarWizard.
	 * @param alphabets the alphabets to be displayed in the alphabet box
	 * @param defaultAlphabet the name of the default alphabet (the selected entry in the alphabet combo box) - if the alphabet is not found, the first Alphabet is used
	 */
	public TranspTextWizard() {
		setWindowTitle(Messages.TranspTextWizard_textwizard);
		firstPage = new TranspTextWizardPage();
		page2 = new TranspTextModifyPage();
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public final void addPages() {
		addPage(firstPage);
		addPage(page2);
	}

	/**
	 * @return
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#getFileName()
	 */
	public String getEditorFileName() {
		if(! finished) return firstPage.getFileName();
		else return editorFileName;
	}

	public int getManualInputState() {
		if(! finished) return firstPage.getManualInputState();
		else return manualInputState;
	}

	/**
	 * @return
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#getBlocksize()
	 */
	public int getBlocksize() {
		if(! finished) return firstPage.getBlocksize();
		else return blocklength;
	}

	/**
	 * @return
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#getCrop()
	 */
	public boolean getCrop() {
		if(! finished) return firstPage.getCrop();
		else return crop;
	}

	/**
	 * @return
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#getCropsize()
	 */
	public int getCropsize() {
		if(! finished) return firstPage.getCropsize();
		else return croplength;
	}

	/**
	 * @return
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#getText()
	 */
	public String getText() {
		TransformData transform = this.getTransformData();

		if(! finished) return Transform.transformText(firstPage.getText(), transform);
		else return Transform.transformText(text, transform);
	}

	public TransformData getTransformData() {
		return page2.getSelectedData();
	}

	public boolean getReadInDirection() {
		return firstPage.getReadInDirection();
	}

	private void saveResults() {
		this.crop = getCrop();
		this.croplength = getCropsize();
		this.blocklength = getBlocksize();
		this.text = getText();
		this.editorFileName = getEditorFileName();
		this.manualInputState = getManualInputState();
		finished = true;
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


	/**
	 * @param initBlocklength
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#setBlocklength(int)
	 */
	public void setBlocklength(int initBlocklength) {
		firstPage.setBlocklength(initBlocklength);
	}

	/**
	 * @param initCroplength
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#setCroplength(int)
	 */
	public void setCroplength(int initCroplength) {
		firstPage.setCroplength(initCroplength);
	}

	/**
	 * @param initCroptext
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#setCroptext(boolean)
	 */
	public void setCroptext(boolean initCroptext) {
		firstPage.setCroptext(initCroptext);
	}

	/**
	 * @param initFirstTabText
	 * @see org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage#setAnalysisText(java.lang.String)
	 */
	public void setAnalysisText(String initFirstTabText) {
		firstPage.setAnalysisText(initFirstTabText);

	}

	public void setManualInputState(int manualInputState) {
		firstPage.setManualInputState(manualInputState);
	}

	public void setFileName(String fileName) {
		firstPage.setFileName(fileName);
	}

	public void setReadInDirection(boolean dir) {
		firstPage.setReadInDirection(dir);
	}



}
