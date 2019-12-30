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

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.ui.util.WidgetBubbleUIInputHandler;

public class CompositeNumberInput extends Composite {
	private static final String MESSAGE_TYPE_EXCEEDS_N = "MESSAGE_TYPE_EXCEEDS_N"; //$NON-NLS-1$
	private static final String MESSAGE_TYPE_FOUND_NOT_ALLOWED_CHARS = "MessageType_found_not_allowed_chars"; //$NON-NLS-1$
	private Text text;
	private int maxNumber;
	private TextfieldInput<List<Integer>> numberInput;
	private AbstractUIInput<Repr> reprInput;
	private WidgetBubbleUIInputHandler errorHandler;

	private static List<Character> binaryChars = new LinkedList<Character>();
	private static List<Character> hexChars = new LinkedList<Character>();
	private static List<Character> decimalChars = new LinkedList<Character>();
	static {
		decimalChars.add('0');
		decimalChars.add('1');
		decimalChars.add('2');
		decimalChars.add('3');
		decimalChars.add('4');
		decimalChars.add('5');
		decimalChars.add('6');
		decimalChars.add('7');
		decimalChars.add('8');
		decimalChars.add('9');

		hexChars.add('0');
		hexChars.add('1');
		hexChars.add('2');
		hexChars.add('3');
		hexChars.add('4');
		hexChars.add('5');
		hexChars.add('6');
		hexChars.add('7');
		hexChars.add('8');
		hexChars.add('9');
		hexChars.add('A');
		hexChars.add('B');
		hexChars.add('C');
		hexChars.add('D');
		hexChars.add('E');
		hexChars.add('F');
//		hexChars.add('a');
//		hexChars.add('b');
//		hexChars.add('c');
//		hexChars.add('d');
//		hexChars.add('e');
//		hexChars.add('f');

		binaryChars.add('0');
		binaryChars.add('1');
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeNumberInput(Composite parent, int style, int maxNumber) {
		super(parent, style);
		
		this.maxNumber = maxNumber;
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(4, false));
		
		Label lblNumberFormat = new Label(composite, SWT.NONE);
		lblNumberFormat.setText(Messages.CompositeNumberInput_2);
		
		Button btnDecimal = new Button(composite, SWT.RADIO);
		btnDecimal.setText(Messages.CompositeNumberInput_3);
		
		Button btnHex = new Button(composite, SWT.RADIO);
		btnHex.setText(Messages.CompositeNumberInput_4);
		
		Button btnBinary = new Button(composite, SWT.RADIO);
		btnBinary.setText(Messages.CompositeNumberInput_5);
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.reprInput = createReprInputObject(btnDecimal, btnHex, btnBinary);
		this.numberInput = createInputObject();
		
		SelectionAdapter reprObserver = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reprInput.synchronizeWithUserSide();
			}
		};
		
		this.reprInput.addObserver(new Observer() {
			Repr prevRepr = reprInput.getContent();
			@Override
			public void update(Observable o, Object arg) {
				transitToNewRepr(prevRepr, reprInput.getContent());
				
				prevRepr = reprInput.getContent();
			}
		});
		
		this.errorHandler = new WidgetBubbleUIInputHandler(getShell());
		this.errorHandler.addInputWidgetMapping(numberInput, text);
		this.errorHandler.addAsObserverForInput(numberInput);
		
		btnDecimal.addSelectionListener(reprObserver);
		btnHex.addSelectionListener(reprObserver);
		btnBinary.addSelectionListener(reprObserver);
	}

	protected void transitToNewRepr(Repr prevRepr, Repr content) {
		if(content != prevRepr) {
//			if(prevRepr == Repr.DECIMAL && content == Repr.HEX) {
//				
//			} else {
//				this.fieldContents.put(prevRepr, text.getText());
//				
//			}
			
			numberInput.writeContent(numberInput.getContent());
			numberInput.synchronizeWithUserSide();
			
//			numberInput.writeContent(new LinkedList<Integer>());
			errorHandler.disposeTooltips();
			
		}
	}

	private AbstractUIInput<Repr> createReprInputObject(final Button btnDecimal, final Button btnHex, final Button btnBinary) {
		AbstractUIInput<Repr> result = new AbstractUIInput<Repr>() {
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public Repr readContent() {
				if(btnDecimal.getSelection()) {
					return Repr.DECIMAL;
				} else if(btnHex.getSelection()) {
					return Repr.HEX;
				} else if(btnBinary.getSelection()) {
					return Repr.BINARY;
				} else {
					return Repr.DECIMAL;
				}
			}

			@Override
			public void writeContent(Repr content) {
				if(content == Repr.DECIMAL) {
					if(! btnDecimal.getSelection()) btnDecimal.setSelection(true);
					if(btnHex.getSelection()) btnHex.setSelection(false);
					if(btnBinary.getSelection()) btnBinary.setSelection(false);
				}
				
				if(content == Repr.BINARY) {
					if(! btnBinary.getSelection()) btnBinary.setSelection(true);
					if(btnDecimal.getSelection()) btnDecimal.setSelection(false);
					if(btnHex.getSelection()) btnHex.setSelection(false);
				}
				
				if(content == Repr.HEX) {
					if(! btnHex.getSelection()) btnHex.setSelection(true);
					if(btnDecimal.getSelection()) btnDecimal.setSelection(false);
					if(btnBinary.getSelection()) btnBinary.setSelection(false);
				}
			}

			@Override
			protected Repr getDefaultContent() {
				return Repr.DECIMAL;
			}

			@Override
			public String getName() {
				return Messages.CompositeNumberInput_6;
			}
		};
		return result;
	}

	private TextfieldInput<List<Integer>> createInputObject() {
		TextfieldInput<List<Integer>> result = new TextfieldInput<List<Integer>>() {

			@Override
			public Text getTextfield() {
				return text;
			}

			private List<Character> getAllowedCharsFor(Repr repr) {
				if(repr == Repr.DECIMAL) return decimalChars;
				if(repr == Repr.HEX) return hexChars;
				if(repr == Repr.BINARY) return binaryChars;
				return decimalChars;
			}
			
			@Override
			protected InputVerificationResult verifyUserChange() {
				String stringContent = getTextfield().getText().toUpperCase().trim();
				String woAllowedChars = stringContent.replaceAll(Pattern.quote(" "), ""); //$NON-NLS-1$ //$NON-NLS-2$
				List<Character> allowedChars = getAllowedCharsFor(reprInput.getContent());
				for(Character c: allowedChars) {
					woAllowedChars = woAllowedChars.replace(String.valueOf(c), ""); //$NON-NLS-1$
				}
				
				final String foundNotAllowedChars = woAllowedChars;
				
				if(woAllowedChars.length() > 0) {
					return new InputVerificationResult() {
						
						public Object getResultType() {return MESSAGE_TYPE_FOUND_NOT_ALLOWED_CHARS;};
						
						@Override
						public boolean isValid() {
							return false;
						}
						@Override
						public boolean isStandaloneMessage() {
							return false;
						}
						@Override
						public MessageType getMessageType() {
							return MessageType.WARNING;
						}
						
						@Override
						public String getMessage() {
							return Messages.CompositeNumberInput_10 + foundNotAllowedChars;
						}
					};
				}
				
				String stringInput = stringContent;
				String[] rawNumberStrings = stringInput.split(Pattern.quote(" ")); //$NON-NLS-1$
				
				for(String split: rawNumberStrings) {
					final String newSplit = split.trim();
					if(! (newSplit == null || newSplit.length() == 0)) {
						Integer readNumber = readNumber(newSplit, reprInput.getContent());
						if(readNumber > maxNumber) {
							return new InputVerificationResult() {
								public Object getResultType() {return MESSAGE_TYPE_EXCEEDS_N;};
								@Override
								public boolean isValid() {
									return false;
								}
								
								@Override
								public boolean isStandaloneMessage() {
									return false;
								}
								
								@Override
								public MessageType getMessageType() {
									return MessageType.WARNING;
								}
								
								@Override
								public String getMessage() {
									return MessageFormat
											.format(Messages.CompositeNumberInput_12,
													newSplit, (maxNumber+1));
								}
							};
						}
					}
				}
				
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public List<Integer> readContent() {
				List<Integer> result = new LinkedList<Integer>();
				
				String stringInput = getTextfield().getText();
				String[] rawNumberStrings = stringInput.split(Pattern.quote(" ")); //$NON-NLS-1$
				
				for(String split: rawNumberStrings) {
					String newSplit = split.trim();
					if(! (newSplit == null || newSplit.length() == 0)) {
						Integer readNumber = readNumber(newSplit, reprInput.getContent());
						result.add(readNumber);
					}
				}
					
				return result;
			}

			@Override
			protected List<Integer> getDefaultContent() {
				return new LinkedList<Integer>();
			}

			@Override
			public String getName() {
				return Messages.CompositeNumberInput_14;
			}
			
			@Override
			public void writeContent(List<Integer> content) {
				StringBuilder sb = new StringBuilder();
				boolean startN = true;
				for(Integer number: content) {
					if(!startN) {
						sb.append(" "); //$NON-NLS-1$
					}
					sb.append(reprInput.getContent().numberToString(number));
					
					startN = false;
				}
				
				setTextfieldTextExternal(sb.toString());
			}
		};
		
		return result;
	}

	protected Integer readNumber(String newSplit, Repr repr) {
		Integer radix = repr.getRadix();
		return Integer.valueOf(newSplit, radix);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public AbstractUIInput<List<Integer>> getNumberInput() {
		return numberInput;
	}
}
