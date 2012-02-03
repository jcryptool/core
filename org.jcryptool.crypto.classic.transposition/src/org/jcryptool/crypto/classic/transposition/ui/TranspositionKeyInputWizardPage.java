package org.jcryptool.crypto.classic.transposition.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.classic.model.ui.wizard.KeyInput;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionAlgorithm;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;

public class TranspositionKeyInputWizardPage extends WizardPage {

	private Composite pageComposite;
	private Composite composite;
	private Label lblForKeysWith;
	private TranspositionKeyInputComposite keySelectionComposite;
	private Composite compTranspositionKey;
	private Text textKey;
	private Text textPreview;
	private KeyInput<TranspositionKey> keyInput;
	private AbstractAlphabet alpha;
	private TranspositionKey initialKey;
	protected String lastInputText;
	private String initialTextfieldText;

	public static class PageConfiguration {
		
		private TranspositionKey key;
		private String inputString;

		public PageConfiguration(TranspositionKey key, String inputString) {
			super();
			this.key = key;
			this.inputString = inputString;
		}


		public TranspositionKey getKey() {
			return key;
		}

		public void setKey(TranspositionKey key) {
			this.key = key;
		}

		public String getInputString() {
			return inputString;
		}
		
		
	}
	
	public TranspositionKeyInputWizardPage() {
		super("Transposition key", "Transposition key", null);
		setDescription("Please enter a transposition key.");
	}

	private void createInput() {
		keyInput = new KeyInput<TranspositionKey>() {
			@Override
			public void writeContent(TranspositionKey content) {
				setTextfieldTextExternal(content.toUnformattedChars(TranspositionKeyInputWizardPage.this.getAlphabet()));
			}
			@Override
			protected InputVerificationResult verifyUserChange() {
				List<KeyVerificator> verificators = getVerificators();
				return KeyVerificator.verify(getTextfield().getText(), TranspositionKeyInputWizardPage.this.getAlphabet(), verificators);
			}
			@Override
			public TranspositionKey readContent() {
				return new TranspositionKey(getTextfield().getText(), TranspositionKeyInputWizardPage.this.getAlphabet().getCharacterSet());
			}
			@Override
			public String getName() {
				return "transposition key";
			}
			@Override
			protected TranspositionKey getDefaultContent() {
				return (initialKey!=null)?initialKey:new TranspositionKey(new int[]{});
			}
			@Override
			protected Text getTextfield() {
				return textKey;
			}
			@SuppressWarnings("rawtypes")
			@Override
			protected void resetExternallyCaused(AbstractUIInput inputWhichCausedThis) {
				String keyNow = getTextfield().getText();
				StringBuilder stringBuilder = new StringBuilder();
				for(int i=0; i<keyNow.length(); i++) {
					if(TranspositionKeyInputWizardPage.this.getAlphabet().contains(keyNow.charAt(i))) {
						stringBuilder.append(keyNow.charAt(i));
					}
				}

				setTextfieldTextExternal(stringBuilder.toString());
				reread(inputWhichCausedThis);
			}
			@Override
			public AbstractAlphabet getAlphabet() {
				return TranspositionKeyInputWizardPage.this.getAlphabet();
			}
			@Override
			protected void saveDefaultRawUserInput() {
				super.saveDefaultRawUserInput();
				TranspositionKeyInputWizardPage.this.lastInputText = getTextfield().getText();
			}
			@Override
			protected void saveRawUserInput() {
				super.saveRawUserInput();
				TranspositionKeyInputWizardPage.this.lastInputText = getTextfield().getText();
			}
		};

		Observer keyChangeObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(arg == null) {
					textPreview.setText(keyInput.getContent().toStringOneRelative());
					if(keyInput.getContent().getLength() > 0) {
						TranspositionKeyInputWizardPage.this.setPageComplete(true);
					} else {
						TranspositionKeyInputWizardPage.this.setPageComplete(true);
					}
				}
			}
		};
		keyInput.addObserver(keyChangeObserver);
		
		if(initialTextfieldText == null) {
			keyInput.writeContent(keyInput.getContent());
		} else {
			keyInput.setTextfieldTextExternal(initialTextfieldText);
		}
		keyChangeObserver.update(keyInput, null);
		
		if(keyInput.getContent().getLength() > 0) {
			TranspositionKeyInputWizardPage.this.setPageComplete(true);
		} else {
			TranspositionKeyInputWizardPage.this.setPageComplete(true);
		}
		
		
	}
	
	protected List<KeyVerificator> getVerificators() {
		return TranspositionAlgorithm.specification.getKeyVerificators();
	}

	public AbstractAlphabet getAlphabet() {
		return TranspositionAlgorithm.specification.getDefaultPlainTextAlphabet();
	}
	
//		public static AbstractAlphabet createAlphabet(final String alphabetContent) {
//			return new AbstractAlphabet() {
//				List<Character> content = stringToList(alphabetContent);
//
//				@Override
//				public void setShortName(String shortName) {}
//				@Override
//				public void setName(String name) {}
//				@Override
//				public void setDefaultAlphabet(boolean b) {}
//				@Override
//				public void setCharacterSet(char[] characterSet) {}
//				@Override
//				public void setBasic(boolean basic) {}
//				@Override
//				public boolean isDefaultAlphabet() {return false;}
//				@Override
//				public boolean isBasic() {return false;}
//				@Override
//				public char getSubstituteCharacter() {return Character.MAX_VALUE;}
//				@Override
//				public int getDisplayMissingCharacters() {return Integer.MAX_VALUE;}
//
//				private List<Character> stringToList(String characters) {
//					List<Character> l = new LinkedList<Character>();
//					for(char c: characters.toCharArray()) l.add(c);
//					return l;
//				}
//
//
//				private String listToString(List<Character> input) {
//					StringBuffer result = new StringBuffer();
//					for(Character c: input) result.append(c);
//					return result.toString();
//				}
//
//				private char[] toCharArray(List<Character> input) {
//					char[] result = new char[input.size()];
//					for(int i=0; i<input.size(); i++) result[i] = input.get(i);
//					return result;
//				}
//
//				@Override
//				public String getShortName() {return listToString(content);} 
//				@Override
//				public String getName() {return "AtomAlphabet="+listToString(content);} //$NON-NLS-1$
//				@Override
//				public char[] getCharacterSet() {
//					return toCharArray(content);
//				}
//				@Override
//				public boolean contains(char e) {
//					return content.contains(e);
//				}
//
//				@Override
//				public String toString() {
//					return listToString(content);
//				}
//			};
//		}

	@Override
	public void createControl(Composite parent) {
		pageComposite = new Composite(parent, SWT.NONE);
		
		
		
		setControl(pageComposite);
		pageComposite.setLayout(new GridLayout(1, false));
		{
			composite = new Composite(pageComposite, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			composite.setLayout(new GridLayout(1, false));
			{
				lblForKeysWith = new Label(composite, SWT.WRAP);
				{
					GridData gd_lblForKeysWith = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
					gd_lblForKeysWith.widthHint = 200;
					lblForKeysWith.setLayoutData(gd_lblForKeysWith);
				}
				lblForKeysWith.setText("For keys with a length lesser than or equal to 10, you can simply put digits from 0 to 9 in the desired order; for more, you have to extend to alphabetical characters.");
			}
		}
		{
			compTranspositionKey = new Composite(pageComposite, SWT.NONE);
			GridLayout compTranspositionKeyLayout = new GridLayout(1, false);
			compTranspositionKeyLayout.verticalSpacing = 2;
			GridData compTranspositionKeyLData = new GridData();
			compTranspositionKeyLData.grabExcessHorizontalSpace = true;
			compTranspositionKeyLData.horizontalAlignment = GridData.FILL;
			compTranspositionKey.setLayoutData(compTranspositionKeyLData);
			compTranspositionKey.setLayout(compTranspositionKeyLayout);
			{
				textKey = new Text(compTranspositionKey, SWT.BORDER);
				GridData textKeyLData = new GridData();
				textKeyLData.grabExcessHorizontalSpace = true;
				textKeyLData.horizontalAlignment = GridData.FILL;
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
		
		createInput();
	}

	public PageConfiguration getPageConfiguration() {
		return new PageConfiguration(keyInput.getContent(), lastInputText);
	}

	public void setPageConfiguration(PageConfiguration config) {
		if(keyInput != null) {
			keyInput.writeContent(config.getKey());
			keyInput.synchronizeWithUserSide();
		} else {
			initialKey = config.getKey();
			initialTextfieldText = initialKey.toUnformattedChars(getAlphabet());
		}
		
		if(config.inputString != null) {
			if(keyInput != null) {
				if(textKey!=null) {
					keyInput.setTextfieldTextExternal(config.inputString);
				} else {
					initialTextfieldText = config.inputString;
				}
			} else {
				initialTextfieldText = config.inputString;
			}
		}
	}
}
