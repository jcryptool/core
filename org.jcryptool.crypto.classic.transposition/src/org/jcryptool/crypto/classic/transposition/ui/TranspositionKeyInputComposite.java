// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.ui;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.model.ui.wizard.KeyInput;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionAlgorithm;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;
import org.jcryptool.crypto.ui.util.WidgetBubbleUIInputHandler;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class TranspositionKeyInputComposite extends Composite {


	private Button checkEnabled;
	private Label labelInOrder;
	private Composite compReadIn;
	private Button checkInOrderRowwise;
	private Button checkOutOrderColumnwise;
	private Composite compReadOut;
	private Label labelOutOrder;
	private Button checkOutOrderRowwise;
	private Text textPreview;
	private Text textKey;
	private Composite compTranspositionKey;
	private Label labelTranspositionKey;
	private Button checkInOrderColumnwise;
	private Composite compInputCtrls;

	/**
	 * true - columnwise<br />
	 * false - rowwise
	 */
	private ButtonInput readInInput;
	/**
	 * true - columnwise<br />
	 * false - rowwise
	 */
	private ButtonInput readOutInput;
	private TextfieldInput<TranspositionKey> keyInput;
	/**
	 * Whether this encryption is active or not.
	 */
	private ButtonInput isActiveInput;
	private AbstractUIInput<AbstractAlphabet> alphabetInput;
	private String keyInputName;
	private WidgetBubbleUIInputHandler verificationDisplayHandler;
	private Label labelTitle;
	private Composite compTitle;
	private List<KeyVerificator> verificators = new LinkedList<KeyVerificator>();


	public TranspositionKeyInputComposite(Composite parent, boolean hasChoice) {
		super(parent, SWT.NONE);
		initGUI(hasChoice);
		createInputs();
		createInputHandler();
		if(!hasChoice) hideObject(checkEnabled, true);
	}

	/**
	 * Important to set an currentAlphabet input, because the key should be verified
	 * over it.
	 *
	 * @param alpha
	 */
	public void setAlphabetInput(AbstractUIInput<AbstractAlphabet> alpha) {
		this.alphabetInput = alpha;
		if(keyInput != null) {
			this.alphabetInput.addObserver(keyInput);
		}
	}

	//TODO: !provisory replace with Atomalphabet later
	public static AbstractAlphabet createAlphabet(final String alphabetContent) {
		return new AbstractAlphabet() {
			List<Character> content = stringToList(alphabetContent);

			@Override
			public void setShortName(String shortName) {}
			@Override
			public void setName(String name) {}
			@Override
			public void setDefaultAlphabet(boolean b) {}
			@Override
			public void setCharacterSet(char[] characterSet) {}
			@Override
			public void setBasic(boolean basic) {}
			@Override
			public boolean isDefaultAlphabet() {return false;}
			@Override
			public boolean isBasic() {return false;}
			@Override
			public char getSubstituteCharacter() {return Character.MAX_VALUE;}
			@Override
			public int getDisplayMissingCharacters() {return Integer.MAX_VALUE;}

			private List<Character> stringToList(String characters) {
				List<Character> l = new LinkedList<Character>();
				for(char c: characters.toCharArray()) l.add(c);
				return l;
			}


			private String listToString(List<Character> input) {
				StringBuffer result = new StringBuffer();
				for(Character c: input) result.append(c);
				return result.toString();
			}

			private char[] toCharArray(List<Character> input) {
				char[] result = new char[input.size()];
				for(int i=0; i<input.size(); i++) result[i] = input.get(i);
				return result;
			}

			@Override
			public String getShortName() {return listToString(content);} 
			@Override
			public String getName() {return "AtomAlphabet="+listToString(content);} //$NON-NLS-1$
			@Override
			public char[] getCharacterSet() {
				return toCharArray(content);
			}
			@Override
			public boolean contains(char e) {
				return content.contains(e);
			}

			@Override
			public String toString() {
				return listToString(content);
			}
		};
	}

	private AbstractAlphabet internalGetCurrentAlphabet() {
		if(alphabetInput != null) {
			return alphabetInput.getContent();
		} else {
			return TranspositionAlgorithm.specification.getDefaultPlainTextAlphabet();
//			return createAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöüß1234567890!§$%&/()=?*+#,.-;:_");
		}
	}

	protected String internalGetKeyInputName() {
		return keyInputName!=null?keyInputName:"KEY INPUT NAME NOT SET"; //$NON-NLS-1$
	}

	/**
     * Excludes a control from Layout calculation
     *
     * @param that
     * @param hideit
     */
    private void hideObject(final Control that, final boolean hideit) {
        GridData GData = (GridData) that.getLayoutData();
        GData.exclude = hideit;
        that.setVisible(!hideit);
        Control[] myArray = {that};
        layout(myArray);
    }

	private void createInputs() {
		readInInput = new ButtonInput() {
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}
			@Override
			public void writeContent(Boolean content) {
				super.writeContent(content);
				checkInOrderRowwise.setSelection(! content);
			}
			@Override
			public String getName() {
				return Messages.TranspositionKeyInputComposite_inputname_read_in_order;
			}
			@Override
			protected Boolean getDefaultContent() {
				return false;
			}
			@Override
			public Button getButton() {
				return checkInOrderColumnwise;
			}
		};
		readOutInput = new ButtonInput() {
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}
			@Override
			public void writeContent(Boolean content) {
				super.writeContent(content);
				checkOutOrderRowwise.setSelection(! content);
			}
			@Override
			public String getName() {
				return Messages.TranspositionKeyInputComposite_inputname_read_out_order;
			}
			@Override
			protected Boolean getDefaultContent() {
				return true;
			}
			@Override
			public Button getButton() {
				return checkOutOrderColumnwise;
			}
		};
		isActiveInput = new ButtonInput() {
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}
			@Override
			public String getName() {
				return getButton().getText();
			}
			@Override
			protected Boolean getDefaultContent() {
				return true;
			}
			@Override
			public Button getButton() {
				return checkEnabled;
			}
		};
		isActiveInput.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
//				hideObject(compInputCtrls, ! isActiveInput.getContent());
				updateForEnabledState(isActiveInput.getContent());
			}
		});

		keyInput = new KeyInput<TranspositionKey>() {
			@Override
			public void writeContent(TranspositionKey content) {
				setTextfieldTextExternal(content.toUnformattedChars(internalGetCurrentAlphabet()));
			}
			@Override
			protected InputVerificationResult verifyUserChange() {
				List<KeyVerificator> verificators = getVerificators();
				return KeyVerificator.verify(getTextfield().getText(), internalGetCurrentAlphabet(), verificators);
			}
			@Override
			public TranspositionKey readContent() {
				return new TranspositionKey(getTextfield().getText(), internalGetCurrentAlphabet().getCharacterSet());
			}
			@Override
			public String getName() {
				return internalGetKeyInputName();
			}
			@Override
			protected TranspositionKey getDefaultContent() {
				return new TranspositionKey(new int[]{});
			}
			@Override
			public Text getTextfield() {
				return textKey;
			}
			@SuppressWarnings("rawtypes")
			@Override
			protected void resetExternallyCaused(AbstractUIInput inputWhichCausedThis) {
				String keyNow = getTextfield().getText();
				StringBuilder stringBuilder = new StringBuilder();
				for(int i=0; i<keyNow.length(); i++) {
					if(internalGetCurrentAlphabet().contains(keyNow.charAt(i))) {
						stringBuilder.append(keyNow.charAt(i));
					}
				}

				setTextfieldTextExternal(stringBuilder.toString());
				reread(inputWhichCausedThis);
			}
			@Override
			public AbstractAlphabet getAlphabet() {
				return alphabetInput.getContent();
			}
		};

		keyInput.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(arg == null) {
					textPreview.setText(keyInput.getContent().toStringOneRelative());
				}
			}
		});
	}

	protected List<KeyVerificator> getVerificators() {
		return verificators;
	}

	/**
	 * Sets the verificators that will be used within key string verification
	 *
	 * @param verificators the list of KeyInputVerificators
	 */
	public void setVerificators(List<KeyVerificator> verificators) {
		this.verificators = verificators;
	}

	private void createInputHandler() {
		verificationDisplayHandler = new WidgetBubbleUIInputHandler(getShell());

		verificationDisplayHandler.addAsObserverForInput(keyInput);
		verificationDisplayHandler.addAsObserverForInput(isActiveInput);
		verificationDisplayHandler.addAsObserverForInput(readInInput);
		verificationDisplayHandler.addAsObserverForInput(readOutInput);

		//static mappings (dynamic, like at operation, are handled above in the overridden method)
		verificationDisplayHandler.addInputWidgetMapping(keyInput, textKey);
		verificationDisplayHandler.addInputWidgetMapping(isActiveInput, checkEnabled);
		verificationDisplayHandler.addInputWidgetMapping(readInInput, compReadIn);
		verificationDisplayHandler.addInputWidgetMapping(readOutInput, compReadOut);
	}

	public void setObserverToAllInputs(Observer o) {
		keyInput.addObserver(o);
		readInInput.addObserver(o);
		readOutInput.addObserver(o);
		isActiveInput.addObserver(o);
	}

	/**
	 * Sets this transposition active or not (like when clicking on the checkbox)
	 *
	 * @param active
	 */
	public void setActive(boolean active) {
		isActiveInput.writeContent(active);
		isActiveInput.reread("setActive from program"); //$NON-NLS-1$
	}

	/**
	 * Sets the input name of the key input.
	 *
	 * @param keyInputName the name
	 * @see UIInput#getName()
	 */
	public void setKeyInputName(String keyInputName) {
		this.keyInputName = keyInputName;
	}

	/**
	 * Sets the "title" of this composite, which is represented by
	 * the text next to the checkbox.
	 *
	 * @param title the title
	 */
	public void setTitle(String title) {
		labelTitle.setText(title);
	}

	public ButtonInput getReadInInput() {
		return readInInput;
	}

	public ButtonInput getReadOutInput() {
		return readOutInput;
	}

	public TextfieldInput<TranspositionKey> getKeyInput() {
		return keyInput;
	}

	public ButtonInput getIsActiveInput() {
		return isActiveInput;
	}

	public void updateForEnabledState(boolean enabled) {
		updateForEnabledState(enabled, false);
	}
	
	public void updateForEnabledState(boolean enabled, boolean preferNoneditableIfPossible) {
		Control[] allCtrls = new Control[]{
//			checkEnabled,
			labelInOrder,
//			compReadIn,
			checkInOrderRowwise,
			checkOutOrderColumnwise,
//			compReadOut,
			labelOutOrder,
			checkOutOrderRowwise,
//			textPreview,
			textKey,
//			compTranspositionKey,
			labelTranspositionKey,
			checkInOrderColumnwise,
//			compInputCtrls,
//			labelTitle,
//			compTitle
		};

		for(Control c: allCtrls) {
			if(c instanceof Text && c != textPreview && preferNoneditableIfPossible) {
				((Text) c).setEditable(enabled);
			} else {
				c.setEnabled(enabled);
			}
		}

	}
	
	public void makeEditable(boolean editable) {
		Control[] allCtrls = new Control[]{
//			checkEnabled,
//			labelInOrder,
//			compReadIn,
			checkInOrderRowwise,
			checkOutOrderColumnwise,
//			compReadOut,
//			labelOutOrder,
			checkOutOrderRowwise,
//			textPreview,
			textKey,
//			compTranspositionKey,
//			labelTranspositionKey,
			checkInOrderColumnwise,
//			compInputCtrls,
//			labelTitle,
//			compTitle
		};

		for(Control c: allCtrls) {
			if(c instanceof Text && c != textPreview) {
				((Text) c).setEditable(editable);
			} else {
				c.setEnabled(editable);
			}
		}

	}

	private void initGUI(boolean hasChoice) {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			this.setLayout(thisLayout);
			{
				compTitle = new Composite(this, SWT.NONE);
				GridLayout compTitleLayout = new GridLayout();
				compTitleLayout.marginHeight = 0;
				compTitleLayout.marginWidth = 0;
				compTitleLayout.numColumns = 2;
				GridData compTitleLData = new GridData();
				compTitleLData.grabExcessHorizontalSpace = true;
				compTitleLData.horizontalAlignment = GridData.FILL;
				compTitle.setLayoutData(compTitleLData);
				compTitle.setLayout(compTitleLayout);
				{
					GridData checkEnabledLData = new GridData();
					checkEnabled = new Button(compTitle, SWT.CHECK | SWT.LEFT);
					checkEnabled.setLayoutData(checkEnabledLData);
					checkEnabled.setText(""); //$NON-NLS-1$
					checkEnabled.setEnabled(hasChoice);
				}
				{
					labelTitle = new Label(compTitle, SWT.NONE);
					GridData labelTitleLData = new GridData();
					labelTitleLData.grabExcessHorizontalSpace = true;
					labelTitleLData.horizontalAlignment = GridData.FILL;
					labelTitle.setLayoutData(labelTitleLData);
					Font segoe = new Font(labelTitle.getDisplay(), new FontData("Segoe UI", 9, 1));  //$NON-NLS-1$
					labelTitle.setFont(segoe);
					labelTitle.setText(""); //$NON-NLS-1$
				}
			}
			{
				compInputCtrls = new Composite(this, SWT.NONE);
				GridLayout inputCtrlsLayout = new GridLayout();
				inputCtrlsLayout.makeColumnsEqualWidth = true;
				inputCtrlsLayout.marginLeft = 10;
				GridData inputCtrlsLData = new GridData();
				inputCtrlsLData.grabExcessHorizontalSpace = true;
				inputCtrlsLData.horizontalAlignment = GridData.FILL;
				inputCtrlsLData.verticalAlignment = GridData.FILL;
				inputCtrlsLData.grabExcessVerticalSpace = true;
				compInputCtrls.setLayoutData(inputCtrlsLData);
				compInputCtrls.setLayout(inputCtrlsLayout);
				{
					labelInOrder = new Label(compInputCtrls, SWT.NONE);
					GridData labelFirstOrderLData = new GridData();
					labelFirstOrderLData.grabExcessHorizontalSpace = true;
					labelFirstOrderLData.horizontalAlignment = GridData.FILL;
					labelInOrder.setLayoutData(labelFirstOrderLData);
					labelInOrder.setText(Messages.TranspositionKeyInputComposite_transpositionsteps_first_readin);
				}
				{
					compReadIn = new Composite(compInputCtrls, SWT.NONE);
					GridLayout compReadInLayout = new GridLayout();
					compReadInLayout.numColumns = 2;
					compReadInLayout.makeColumnsEqualWidth = true;
					compReadInLayout.marginTop = -5;
					GridData compReadInLData = new GridData();
					compReadInLData.grabExcessHorizontalSpace = true;
					compReadInLData.horizontalAlignment = GridData.FILL;
					compReadInLData.horizontalIndent = 5;
					compReadIn.setLayoutData(compReadInLData);
					compReadIn.setLayout(compReadInLayout);
					{
						checkInOrderColumnwise = new Button(compReadIn, SWT.RADIO | SWT.LEFT);
						GridData btnInOrderColumnwiseLData = new GridData();
						checkInOrderColumnwise.setLayoutData(btnInOrderColumnwiseLData);
						checkInOrderColumnwise.setText(Messages.TranspositionKeyInputComposite_columnwise);
					}
					{
						checkInOrderRowwise = new Button(compReadIn, SWT.RADIO | SWT.LEFT);
						GridData btnInOrderRowwiseLData = new GridData();
						checkInOrderRowwise.setLayoutData(btnInOrderRowwiseLData);
						checkInOrderRowwise.setText(Messages.TranspositionKeyInputComposite_rowwise);
					}
				}
				{
					labelTranspositionKey = new Label(compInputCtrls, SWT.NONE);
					GridData labelTranspositionKeyLData = new GridData();
					labelTranspositionKeyLData.verticalIndent = 4;
					labelTranspositionKey.setLayoutData(labelTranspositionKeyLData);
					labelTranspositionKey.setText(Messages.TranspositionKeyInputComposite_transpositionsteps_second_transposition);
				}
				{
					compTranspositionKey = new Composite(compInputCtrls, SWT.NONE);
					GridLayout compTranspositionKeyLayout = new GridLayout();
					compTranspositionKeyLayout.makeColumnsEqualWidth = true;
					compTranspositionKeyLayout.marginTop = -5;
					compTranspositionKeyLayout.verticalSpacing = 2;
					GridData compTranspositionKeyLData = new GridData();
					compTranspositionKeyLData.grabExcessHorizontalSpace = true;
					compTranspositionKeyLData.horizontalAlignment = GridData.FILL;
					compTranspositionKeyLData.horizontalIndent = 5;
					compTranspositionKey.setLayoutData(compTranspositionKeyLData);
					compTranspositionKey.setLayout(compTranspositionKeyLayout);
					{
						GridData textKeyLData = new GridData();
						textKeyLData.grabExcessHorizontalSpace = true;
						textKeyLData.horizontalAlignment = GridData.FILL;
						textKey = new Text(compTranspositionKey, SWT.BORDER);
						textKey.setLayoutData(textKeyLData);
					}
					{
						textPreview = new Text(compTranspositionKey, SWT.BORDER);
						GridData textPreviewLData = new GridData();
						textPreviewLData.grabExcessHorizontalSpace = true;
						textPreviewLData.horizontalAlignment = GridData.FILL;
						textPreview.setLayoutData(textPreviewLData);
						textPreview.setText(Messages.TranspositionKeyInputComposite_previewkey);
						textPreview.setEnabled(false);
					}
				}
				{
					labelOutOrder = new Label(compInputCtrls, SWT.NONE);
					GridData labelOutOrderLData = new GridData();
					labelOutOrderLData.verticalIndent = 4;
					labelOutOrder.setLayoutData(labelOutOrderLData);
					labelOutOrder.setText(Messages.TranspositionKeyInputComposite_transpositionsteps_third_readout);
				}
				{
					compReadOut = new Composite(compInputCtrls, SWT.NONE);
					GridLayout composite1Layout = new GridLayout();
					composite1Layout.numColumns = 2;
					composite1Layout.makeColumnsEqualWidth = true;
					composite1Layout.marginTop = -5;
					compReadOut.setLayout(composite1Layout);
					GridData composite1LData = new GridData();
					composite1LData.widthHint = 285;
					composite1LData.heightHint = 21;
					composite1LData.horizontalIndent = 5;
					compReadOut.setLayoutData(composite1LData);
					{
						checkOutOrderColumnwise = new Button(compReadOut, SWT.RADIO | SWT.LEFT);
						checkOutOrderColumnwise.setText(Messages.TranspositionKeyInputComposite_columnwise);
						GridData button1LData = new GridData();
						checkOutOrderColumnwise.setLayoutData(button1LData);
					}
					{
						checkOutOrderRowwise = new Button(compReadOut, SWT.RADIO | SWT.LEFT);
						checkOutOrderRowwise.setText(Messages.TranspositionKeyInputComposite_rowwise);
						GridData button2LData = new GridData();
						checkOutOrderRowwise.setLayoutData(button2LData);
					}
				}
			}
			this.layout();
			pack();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
	}

	public WidgetBubbleUIInputHandler getVerificationDisplayHandler() {
		return verificationDisplayHandler;
	}
	
	public String getTextfieldString() {
		return textKey.getText();
	}

}
