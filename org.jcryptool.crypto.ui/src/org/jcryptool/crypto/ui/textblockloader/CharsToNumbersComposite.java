package org.jcryptool.crypto.ui.textblockloader;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite;
import org.jcryptool.crypto.ui.alphabets.composite.AtomAlphabet;
import org.jcryptool.crypto.ui.textblockloader.conversion.AlphabetCharsToNumbers;

public class CharsToNumbersComposite extends Composite {

	public static final AbstractAlphabet ASCII_ALPHABET = mkASCIIAlphabet();
	private AlphabetSelectorComposite alphaSelector;
	private AbstractUIInput<AlphabetCharsToNumbers> uiInput;
	private Button btnConvertIntoIndices;
	private Button btnConvertIntoASCII;
	private int maxNumber;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param maxNumber 
	 */
	public CharsToNumbersComposite(Composite parent, int style, int maxNumber) {
		super(parent, style);
		this.maxNumber = maxNumber;
		setLayout(new GridLayout(1, false));

		boolean canUseASCII = maxNumber >= 255;
		AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
		List<AbstractAlphabet> alphabetsToShow = calcAlphabetsToShow(maxNumber, alphas);
		boolean canShowAllAlphas = alphas.length == alphabetsToShow.size();
		
		btnConvertIntoASCII = new Button(this, SWT.RADIO);
		btnConvertIntoASCII.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnConvertIntoASCII.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnConvertIntoASCII.setText("Convert characters into numbers according to ASCII");
		btnConvertIntoASCII.setEnabled(canUseASCII);
		
		
		btnConvertIntoIndices = new Button(this, SWT.RADIO);
		btnConvertIntoIndices.setText("Convert characters into the their indices in a selected alphabet: ");
		GridData btnConvertIntoIndicesLayoutData = new GridData();
		btnConvertIntoIndicesLayoutData.verticalIndent = 5;
		btnConvertIntoIndices.setLayoutData(btnConvertIntoIndicesLayoutData);
		
		Composite alphaSubComp = new Composite(this, SWT.NONE);
		alphaSubComp.setLayout(new GridLayout());
		GridData alphaSubCompLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		alphaSubCompLayoutData.horizontalIndent = 20;
		alphaSubComp.setLayoutData(alphaSubCompLayoutData);
		
		alphaSelector = new AlphabetSelectorComposite(alphaSubComp, alphabetsToShow, null, AlphabetSelectorComposite.Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON);
		alphaSelector.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		if(!(canUseASCII && (alphabetsToShow.size() == alphas.length))) {
			Composite warningComposite = new Composite(this, SWT.NONE);
			GridData warningCompositeLayoutData = new GridData();
			warningCompositeLayoutData.verticalIndent = 0;
			GridLayout warningCompositeLayout = new GridLayout(2, false);
			warningComposite.setLayoutData(warningCompositeLayoutData);
			warningComposite.setLayout(warningCompositeLayout);
			warningCompositeLayout.marginWidth = 0;
			warningCompositeLayout.marginHeight = 0;
			
			Label lblWarning1 = new Label(warningComposite, SWT.NONE);
			GridData lblWarning1LayoutData = new GridData();
			lblWarning1.setLayoutData(lblWarning1LayoutData);
			lblWarning1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK));
			
			Label lblNotAllAlphas = new Label(warningComposite, SWT.NONE);
			GridData lblNotAllAlphasLayoutData = new GridData();
			lblNotAllAlphas.setLayoutData(lblNotAllAlphasLayoutData);
			lblNotAllAlphas.setText("Some alphabets are not available because the RSA modulus N (" + (maxNumber+1) + ") is too small.");
		}
		
		this.uiInput = makeUIInput();
		registerInputListenersFor(this.uiInput);
		
		this.uiInput.addObserver(new Observer() {
			
			@Override
			public void update(Observable arg0, Object arg1) {
				alphaSelector.setEnabled(!uiInput.getContent().isIs256ASCII());
			}
		});
		
		makeFirstConversionSelection(canShowAllAlphas, canUseASCII, alphabetsToShow);
		this.uiInput.forceNotifyObservers();
		
	}
	
	private void makeFirstConversionSelection(boolean canShowAllAlphas,
			boolean canUseASCII, List<AbstractAlphabet> alphabetsToShow) {
		if(canUseASCII) {
			//do nothing
		} else {
			AbstractAlphabet biggestUnderLimit = null;
			for(AbstractAlphabet a: alphabetsToShow) {
				if(biggestUnderLimit == null || biggestUnderLimit.getCharacterSet().length < a.getCharacterSet().length) {
					biggestUnderLimit = a;
				}
			}
			if(biggestUnderLimit == null) {
				AtomAlphabet alpha = new AtomAlphabet("ABCDE");
				AlphabetCharsToNumbers conv = new AlphabetCharsToNumbers(alpha);
				this.getConversionInput().writeContent(conv);
				this.getConversionInput().synchronizeWithUserSide();
			} else {
				AlphabetCharsToNumbers conv = new AlphabetCharsToNumbers(biggestUnderLimit);
				this.getConversionInput().writeContent(conv);
				this.getConversionInput().synchronizeWithUserSide();
			}
		}
	}

	private List<AbstractAlphabet> calcAlphabetsToShow(int maxNumber2, AbstractAlphabet[] alphasToConsider) {
		List<AbstractAlphabet> result = new LinkedList<AbstractAlphabet>();
		
		for(AbstractAlphabet alpha: alphasToConsider) {
			if(maxNumber2 >= (alpha.getCharacterSet().length-1)) {
				result.add(alpha);
			}
		}
		return result;
	}

	private void registerInputListenersFor(
			final AbstractUIInput<AlphabetCharsToNumbers> input) {
		this.alphaSelector.getAlphabetInput().addObserver(input);
		this.btnConvertIntoIndices.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				input.synchronizeWithUserSide();
			}
		});
	}

	// ASCII characters
	// alphabet indices

	private static AbstractAlphabet mkASCIIAlphabet() {
		AbstractAlphabet alpha;
		int asciiRange = 256;
		char[] asciiCharacters = new char[asciiRange];
		for(int i=0; i<asciiRange; i++) {
			asciiCharacters[i] = (char) i;
		}
		alpha = new AtomAlphabet(asciiCharacters);
		alpha.setName("ASCII 256 characters");
		return alpha;
	}

	private AbstractUIInput<AlphabetCharsToNumbers> makeUIInput() {
		AbstractUIInput<AlphabetCharsToNumbers> result = new AbstractUIInput<AlphabetCharsToNumbers>() {

			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public AlphabetCharsToNumbers readContent() {
				AbstractAlphabet alpha;
				boolean isASCII = btnConvertIntoASCII.getSelection();
				if(isASCII) {
					alpha = ASCII_ALPHABET;
				} else {
					alpha = alphaSelector.getAlphabetInput().getContent();
				}
				
				AlphabetCharsToNumbers result = new AlphabetCharsToNumbers(alpha, isASCII);
				return result;
			}

			@Override
			public void writeContent(AlphabetCharsToNumbers content) {
				btnConvertIntoASCII.setSelection(content.isIs256ASCII());
				btnConvertIntoIndices.setSelection(!content.isIs256ASCII());
				
				if(! content.isIs256ASCII()) {
					alphaSelector.getAlphabetInput().writeContent(content.getAlpha());
					alphaSelector.getAlphabetInput().synchronizeWithUserSide();
				}
			}

			@Override
			protected AlphabetCharsToNumbers getDefaultContent() {
				return new AlphabetCharsToNumbers(ASCII_ALPHABET, true);
			}

			@Override
			public String getName() {
				return "character conversion alphabet";
			}
		};
		
		return result;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public AbstractUIInput<AlphabetCharsToNumbers> getConversionInput() {
		return this.uiInput;
	}
}
