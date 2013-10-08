package org.jcryptool.crypto.ui.textblockloader;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.crypto.ui.textblockloader.NumberblocksAndTextViewer.Repr;
import org.jcryptool.crypto.ui.textblockloader.conversion.AlphabetCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textblockloader.conversion.NumbersToBlocksConversion;
import org.jcryptool.crypto.ui.textloader.ui.wizard.loadtext.LoadTextWizardPage;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

public class TANLTextPage extends LoadTextWizardPage {

	private CharsToNumbersComposite charsToNumbersComposite;
	private NumberblocksAndTextViewer txtNumberPreview;
	private int maxNumber;

	public TANLTextPage(int maxNumber) {
		super();
		this.maxNumber = maxNumber;
		this.setTitle("Text selection");
		this.setMessage("Set the text to be converted into data blocks.");
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createControl(parent);
		super.getTextInput().writeContent(new TextInputWithSource(""));
		super.getTextInput().synchronizeWithUserSide();
		
		boolean canUseASCII = maxNumber >= 255;
		
		Group compConversionParams1 = new Group(container, SWT.NONE);
		compConversionParams1.setLayout(new GridLayout());
		compConversionParams1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compConversionParams1.setText("Transformation of text into numbers");
		
		this.charsToNumbersComposite = new CharsToNumbersComposite(compConversionParams1, SWT.NONE, maxNumber);
		this.charsToNumbersComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Group compNumberPreview = new Group(container, SWT.NONE);
		compNumberPreview.setLayout(new GridLayout());
		compNumberPreview.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compNumberPreview.setText("Characters as numbers preview");
		
		Repr[] viewOptions = new NumberblocksAndTextViewer.Repr[]{
					NumberblocksAndTextViewer.Repr.DECIMAL, 
					NumberblocksAndTextViewer.Repr.HEX, 
					NumberblocksAndTextViewer.Repr.BINARY, 
					NumberblocksAndTextViewer.Repr.STRING 
				};
		txtNumberPreview = new NumberblocksAndTextViewer(compNumberPreview, SWT.NONE, 
				viewOptions);
		GridData txtNumberPreviewLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		txtNumberPreviewLayoutData.heightHint = 150;
		txtNumberPreview.setLayoutData(txtNumberPreviewLayoutData);
		
		
		Observer previewRefreshObserver = new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				ConversionCharsToNumbers ctn = charsToNumbersComposite.getConversionInput().getContent();
				ConversionNumbersToBlocks ntb = new NumbersToBlocksConversion(1, NumbersToBlocksConversion.getDefaultBaseForNumberToBlockConversion(ctn));
				ConversionStringToBlocks conversion = new ConversionStringToBlocks(ctn, ntb);
				TextInputWithSource stringContent = TANLTextPage.super.getPageConfiguration();
				
				txtNumberPreview.setContent(stringContent.getText(), conversion);
			}
		};
		
		super.getTextInput().addObserver(previewRefreshObserver);
		this.charsToNumbersComposite.getConversionInput().addObserver(previewRefreshObserver);
		previewRefreshObserver.update(null, null);
	}
	
	protected void refreshMaxPotNumberNextPage() {
		getDefaultWiz().getPageBlockParams().setMaxPotentialNumber(charsToNumbersComposite.getConversionInput().getContent().getMaxNumberValue());
	}

	public AbstractUIInput<AlphabetCharsToNumbers> getCharsToNumbersConversionInput() {
		return charsToNumbersComposite.getConversionInput();
	}
	
	private TextAsNumbersLoaderWizard getDefaultWiz() {
		return (TextAsNumbersLoaderWizard) getWizard();
	}

}
