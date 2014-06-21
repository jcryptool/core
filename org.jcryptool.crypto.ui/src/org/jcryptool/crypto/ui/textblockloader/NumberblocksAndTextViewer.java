package org.jcryptool.crypto.ui.textblockloader;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.ui.textblockloader.conversion.AlphabetCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textblockloader.conversion.NumbersToBlocksConversion;

public class NumberblocksAndTextViewer extends Composite {
	private static final String DEFAULT_TEXT_SEPARATOR = " "; //$NON-NLS-1$
	private static final String DEFAULT_NUMBER_SEPARATOR = " "; //$NON-NLS-1$
	private static final ConversionStringToBlocks defaultConversion = new ConversionStringToBlocks(new AlphabetCharsToNumbers(CharsToNumbersComposite.ASCII_ALPHABET), new NumbersToBlocksConversion(1, 1));
	
	private Text text;
	private ConversionStringToBlocks stb;
	private Repr[] viewOptions;
	private HashMap<Repr, Button> buttonMap;
	private List<Integer> content;
	private String textSeparator;
	private String numberSeparator;
	private int maxNumbersToDisplay = 100;


	private boolean aContains(Repr[] a, Repr o) {
		for(Repr r: a) if(r.equals(o)) return true;
		return false;
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NumberblocksAndTextViewer(Composite parent, int style, Repr[] viewOptions) {
		super(parent, style);
		GridLayout thisLayout = new GridLayout(1, false);
		thisLayout.marginHeight = 5;
		thisLayout.marginWidth = 5;
		thisLayout.verticalSpacing = 0;
		setLayout(thisLayout);
		
		this.viewOptions = viewOptions;
				
		this.setTextBlockSeparator(DEFAULT_TEXT_SEPARATOR);
		this.setNumberBlocksSeparator(DEFAULT_NUMBER_SEPARATOR);
		
		this.buttonMap = new HashMap<Repr, Button>(); 
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout compositeLayout = new GridLayout(6, false);
		compositeLayout.marginWidth = 0;
		compositeLayout.marginHeight = 0;
		composite.setLayout(compositeLayout);
		
		Label spacerLabel = new Label(composite, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label lblShowAs = new Label(composite, SWT.NONE);
		lblShowAs.setText(Messages.NumberblocksAndTextViewer_2);
		
		SelectionAdapter btnListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshDisplayedText();
			}
		};
		
		Button btnDecimal = new Button(composite, SWT.RADIO);
		btnDecimal.setText(Messages.NumberblocksAndTextViewer_3);
		btnDecimal.setLayoutData(new GridData());
		btnDecimal.addSelectionListener(btnListener);
		buttonMap.put(Repr.DECIMAL, btnDecimal);
		
		Button btnHex = new Button(composite, SWT.RADIO);
		btnHex.setLayoutData(new GridData());
		btnHex.setText(Messages.NumberblocksAndTextViewer_4);
		btnHex.addSelectionListener(btnListener);
		buttonMap.put(Repr.HEX, btnHex);
		
		Button btnBinary = new Button(composite, SWT.RADIO);
		btnBinary.setLayoutData(new GridData());
		btnBinary.setText(Messages.NumberblocksAndTextViewer_5);
		btnBinary.addSelectionListener(btnListener);
		buttonMap.put(Repr.BINARY, btnBinary);
		
		Button btnText = new Button(composite, SWT.RADIO);
		btnText.setLayoutData(new GridData());
		btnText.setText(Messages.NumberblocksAndTextViewer_6);
		btnText.addSelectionListener(btnListener);
		buttonMap.put(Repr.STRING, btnText);
		
		for(Repr r: Repr.values()) {
			if(!aContains(viewOptions, r) || viewOptions.length < 2) {
				showBtn(buttonMap.get(r), false);
			}
		}
		
		this.buttonMap.get(viewOptions[0]).setSelection(true);
		
		text = new Text(this, SWT.MULTI | SWT.WRAP | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text.setEditable(false);
	}
	
	private void refreshDisplayedText() {
//		if(stb.getCtn() == null || stb.getNtb() == null) {
//			LogUtil.logWarning("NumberblocksViewer: did not refresh because conversion parameters not set.");
//			return;
//		}
		
		Repr selectedRepr = getCurrentRepresentation();
		String displayedText = calcFieldContent(content, selectedRepr, internalGetSTBC());
		this.text.setText(displayedText);
	}
	
	private ConversionStringToBlocks internalGetSTBC() {
		if(this.stb != null) return this.stb;
		return defaultConversion;
	}

	public void setTextBlockSeparator(String textSeparator) {
		this.textSeparator = textSeparator;
	}
	
	public void setNumberBlocksSeparator(String numberSeparator) {
		this.numberSeparator = numberSeparator;
	}
	
	public void setContent(List<Integer> content, ConversionStringToBlocks stb) {
		this.setStb(stb, false);
		List<Integer> shortenedContent = content.subList(0, Math.min(this.maxNumbersToDisplay, content.size()));
		this.content = shortenedContent;
		refreshDisplayedText();
	}
	public void setContent(String contentP, ConversionStringToBlocks stb) {
		String content = contentP.substring(0, Math.min(this.maxNumbersToDisplay, contentP.length()));
		List<Integer> blockContent = stb.convert(content);
		this.setContent(blockContent, stb);
	}
	
	public void setStb(ConversionStringToBlocks stb, boolean refresh) {
		this.stb = stb;
		if(refresh) refreshDisplayedText();
	}
	
	private Repr getCurrentRepresentation() {
		Repr currentR = Repr.DECIMAL;
		for(Repr r: viewOptions) {
			Button btn = buttonMap.get(r);
			if(btn.getSelection()) {
				currentR = r; 
			}
		}
		
		return currentR;
	}

	private String calcFieldContent(List<Integer> contentP, Repr representation, ConversionStringToBlocks stb) {
		List<Integer> content = contentP.subList(0, Math.min(this.maxNumbersToDisplay, contentP.size()));
		if(representation == Repr.STRING) {
			String displayedString = stb.revert(content);
			return displayedString;
		} else {
			List<Integer> displayedNumbers = content;
			String sep = representation.isNumeric() ? this.numberSeparator : this.textSeparator;
			StringBuilder sb = new StringBuilder();
			int counter = 0;
			for(Integer charNumber: displayedNumbers) {
				if(counter != 0) {
					sb.append(sep);
				}
				sb.append(representation.numberToString(charNumber));
				
				if(counter < 1) counter++;
			}
			
			return sb.toString();
		}
	}
	
	public void setMaxNumbersToDisplay(int maxNumbersToDisplay) {
		this.maxNumbersToDisplay = maxNumbersToDisplay;
	}
	
	private void showBtn(Button button, boolean show) {
		GridData lData = (GridData) button.getLayoutData();
		lData.exclude = !show;
		button.setVisible(show);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public List<Integer> getContent() {
		return content;
	}

}
