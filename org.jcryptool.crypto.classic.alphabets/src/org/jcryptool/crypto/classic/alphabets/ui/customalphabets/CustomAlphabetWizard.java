package org.jcryptool.crypto.classic.alphabets.ui.customalphabets;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.internal.ole.win32.ISpecifyPropertyPages;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;
import org.jcryptool.crypto.classic.alphabets.ui.AddAlphabetWizardPage2;
import org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory.CustomAlphabetHistoryManager;

public class CustomAlphabetWizard extends Wizard {
	
	public static final String USE_HISTORY_ALPHABET = "use history";
	public static final String MAKE_NEW_ALPHABET = "make custom alphabet";
	
	CreateCustomAlphabetIntroPage page1;
	AddAlphabetWizardPage2 page2;
	
	private String alphaSelectMode = MAKE_NEW_ALPHABET;
	private boolean historyAlphaPermanence = false;
	private boolean customAlphaPermanence = false;
	
	private AbstractAlphabet selectedAlphabet = null;
	
	public CustomAlphabetWizard() {
		setWindowTitle("Create custom alphabets");
		page1 = new CreateCustomAlphabetIntroPage();
		page2 = new AddAlphabetWizardPage2();
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
        AlphabetsPlugin.getDefault().savePreferences();
	}
	
}
