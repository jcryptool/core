//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.substitution.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.InputVerificationResult.MessageType;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.substitution.SubstitutionPlugin;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionKey;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionKeyValidityException;

/**
 * The wizard page for the substitution cipher.
 *
 * @author t-kern
 *
 */
public class SubstitutionWizardPage extends AbstractClassicCryptoPage {

	private Button keyEditorBtn;
	private SubstitutionKeyEditor keyEditor;
	private AbstractUIInput<SubstitutionKey> substKeyInput;

	public SubstitutionWizardPage() {
		super(Messages.SubstitutionWizardPage_substitution, Messages.SubstitutionWizardPage_enterkey1);
	}

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                SubstitutionPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }
    
    @Override
    protected String generateCommandLineString() {
    	String encDec = operationInput.getContent()?"-E":"-D"; //$NON-NLS-1$ //$NON-NLS-2$
    	String key = "-k " + quoteCmdlineArgIfNecessary(getKey()); //$NON-NLS-1$
    	
    	String result = "substitution " + encDec + " -ed " + key; //$NON-NLS-1$ //$NON-NLS-2$

    	result += " " + generateAlphabetPartForCommandLine(); //$NON-NLS-1$
    	
    	if(!isNonAlphaFilter()) result += " --noFilter"; //$NON-NLS-1$
    	return result;
    }

    @Override
    protected boolean mayFinish() {
    	boolean mayFinish = this.getSubstKeyInput() != null && this.getSubstKeyInput().getContent() != null;
    	if(getSubstKeyInput().getContent() != null) {
    		if(!verifyKeyReallyChangesPlaintext(getSelectedAlphabet(), getSubstKeyInput().getContent())) {
    			return false;
    		}
    	}
		return mayFinish;
    }
    
    @Override
    protected void addPageObserver() {
    	super.addPageObserver();
    	this.getSubstKeyInput().addObserver(this.pageObserver);
    }
    
	@Override
	protected void createKeyInputObjects() {
		this.substKeyInput = generateSubstitutionKeyInput();
		addSubstKeyInputAsObserverToEditor();
		getAlphabetInput().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(arg==null) {
					//alphabet changed
					refreshKeyEditorWithNewAlphabet(keyGroup, 
							getKeyEditor().getAlphabet(), 
							getSelectedAlphabet(), 
							getKeyEditor().getPassword(), 
							getSubstKeyInput().getContent(), 
							getKeyEditor().getCharMapping());
				}
			}
		});
	}

	private void addSubstKeyInputAsObserverToEditor() {
		getKeyEditor().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				getSubstKeyInput().synchronizeWithUserSide();
			}
		});
		getSubstKeyInput().synchronizeWithUserSide();
	}
	
	public AbstractUIInput<SubstitutionKey> getSubstKeyInput() {
		return substKeyInput;
	}
	
	@Override
	protected void createInputVerificationHandler(Shell shell) {
		super.createInputVerificationHandler(shell);
		
		verificationDisplayHandler.addAsObserverForInput(this.getSubstKeyInput());
		verificationDisplayHandler.addInputWidgetMapping(this.getSubstKeyInput(), getKeyEditor());
	}
	
	private AbstractUIInput<SubstitutionKey> generateSubstitutionKeyInput() {
		return new AbstractUIInput<SubstitutionKey>() {

			private String keyEditorPassword;
			private Map<Character, Character> mapping;

			private SubstitutionKey generateSubstKeyFromCompleteMapping(AbstractAlphabet alphabet, Map<Character, Character> characterMapping) {
				try {
					return new SubstitutionKey(characterMapping);
				} catch (SubstitutionKeyValidityException e) {
					e.printStackTrace();
					return SubstitutionKey.createIdentitySubstitution(getSelectedAlphabet());
				}
			}
			
			@Override
			protected InputVerificationResult verifyUserChange() {
				if(getKeyEditor().isCompleteData()) {
					Map<Character, Character> data = getKeyEditor().getCharMapping();
					if(verifyKeyReallyChangesPlaintext(
							getSelectedAlphabet(), 
							generateSubstKeyFromCompleteMapping(getSelectedAlphabet(), data)
							)) {
						
						return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
					} else {
						return generateKeyDoesNotChangePlaintextVerificationResult();
					}
				} else {
					Map<Character, Character> incompleteMapping = getKeyEditor().getCharMapping();
					return generateIncompleteMappingVerificationResult(incompleteMapping);
				}
			}

			private InputVerificationResult generateIncompleteMappingVerificationResult(Map<Character, Character> incompleteMapping) {
				List<Character> charsToBeMapped = new LinkedList<Character>();
				for(Character c: incompleteMapping.keySet()) {
					if(incompleteMapping.get(c) == null) charsToBeMapped.add(c);
				}
				String msgFormatString = Messages.SubstitutionWizardPage_7;
				String charsString = charsToBeMapped.toString();
				return InputVerificationResult.generateIVR(
						true, 
						String.format(msgFormatString, charsString), 
						MessageType.INFORMATION, 
						true, 
						InputVerificationResult.RESULT_TYPE_DEFAULT, 
						InputVerificationResult.PERSISTENCE_DEFAULT);
			}

			private InputVerificationResult generateKeyDoesNotChangePlaintextVerificationResult() {
				return InputVerificationResult.generateIVR(
						true, 
						Messages.SubstitutionWizardPage_8, 
						MessageType.INFORMATION, 
						true, 
						InputVerificationResult.RESULT_TYPE_DEFAULT, 
						InputVerificationResult.PERSISTENCE_FOREVER);
			}

			@Override
			public SubstitutionKey readContent() {
				if(getKeyEditor().isCompleteData()) {
					SubstitutionKey substKey = generateSubstKeyFromCompleteMapping(getSelectedAlphabet(), getKeyEditor().getCharMapping());
					return substKey;
				} else {
					return null;
				}
			}

			
			
			@Override
			protected void resetExternallyCaused(AbstractUIInput<?> inputWhichCausedThis) {
				// this will be done externally
			}

			@Override
			protected void resetUserInput() {
				getKeyEditor().setPasswordExternal(this.keyEditorPassword, false);
				getKeyEditor().setCharMappingExternal(this.mapping);
			}
			
			@Override
			protected boolean decideNotifyObserversAboutUserSideSynchronization(boolean changed, SubstitutionKey previousContent,
					SubstitutionKey newContent, InputVerificationResult verificationResult) {
				return true;
			}

			@Override
			protected void saveRawUserInput() {
				this.keyEditorPassword = getKeyEditor().getPassword();
				this.mapping = getKeyEditor().getCharMapping();
			}

			@Override
			protected void saveDefaultRawUserInput() {
				this.keyEditorPassword = ""; //$NON-NLS-1$
				this.mapping = SubstitutionKey.createIdentitySubstitution(getSelectedAlphabet()).getSubstitutions();
			}

			@Override
			public void writeContent(SubstitutionKey content) {
				if(content != null) {
					getKeyEditor().setCharMappingExternal(content.getSubstitutions());
				} else {
					//TODO: ? (but should be covered by resetUserInput: resetting the character mapping, even if incomplete)
				}
			}

			@Override
			protected SubstitutionKey getDefaultContent() {
				return SubstitutionKey.createIdentitySubstitution(getSelectedAlphabet());
			}

			@Override
			public String getName() {
				return "substitution key"; //$NON-NLS-1$
			}
		};
	}

	@Override
	protected void createKeyGroup(Composite parent) {
		keyGroup = new Group(parent, SWT.NONE);
		
		GridLayout keyGroupGridLayout = new GridLayout();
		keyGroupGridLayout.numColumns = 4;
		
		GridData keyGroupGridData = new GridData();
		keyGroupGridData.horizontalAlignment = GridData.FILL;
		keyGroupGridData.grabExcessHorizontalSpace = true;
		keyGroupGridData.grabExcessVerticalSpace = false;
		keyGroupGridData.verticalAlignment = SWT.CENTER;
		
		keyGroup.setLayoutData(keyGroupGridData);
		keyGroup.setLayout(keyGroupGridLayout);
		keyGroup.setText("");

		initializeKeyEditor(keyGroup, getSelectedAlphabet());
	}
	
	private static boolean verifyKeyReallyChangesPlaintext(AbstractAlphabet selectedAlphabet,
			SubstitutionKey generateSubstKeyFromCompleteMapping) {
		for(char c: selectedAlphabet.getCharacterSet()) {
			if(!generateSubstKeyFromCompleteMapping.getSubstitutionFor(c).equals(Character.valueOf(c))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getKey() {
		if(getSubstKeyInput().getContent() == null) {
			throw new NullPointerException("getKey method should only be called when the wizard page is complete."); //$NON-NLS-1$
		}
		return this.getSubstKeyInput().getContent().toStringSubstitutionCharList(getSelectedAlphabet());
	}

	private void refreshKeyEditorWithNewAlphabet(Group parent, AbstractAlphabet oldAlpha, AbstractAlphabet newAlpha, String oldPassword, SubstitutionKey oldKey, Map<Character, Character> oldCharMapping) {
		if(keyEditor != null) {
			keyEditor.dispose();
		}
		
		String newPasswordString = generatePasswortStringForPostAlphaChange(oldPassword, oldKey, oldCharMapping, oldAlpha, newAlpha);
		Map<Character, Character> newCharMapping = generateCharMappingForPostAlphaChange(oldPassword, oldKey, oldCharMapping, oldAlpha, newAlpha);
		
		initializeKeyEditor(parent, newAlpha);
		verificationDisplayHandler.addInputWidgetMapping(this.getSubstKeyInput(), getKeyEditor());
		addSubstKeyInputAsObserverToEditor();
		
		keyEditor.setPasswordExternal(newPasswordString, true);
		reLayoutKeyEditor();
		getSubstKeyInput().synchronizeWithUserSide();
	}
	
	private void reLayoutKeyEditor() {
		pageComposite.layout(new Control[]{keyEditor});
	}
	
	public SubstitutionKeyEditor getKeyEditor() {
		return keyEditor;
	}

	private Map<Character, Character> generateCharMappingForPostAlphaChange(String oldPassword, SubstitutionKey oldKey,
			Map<Character, Character> oldCharMapping, AbstractAlphabet oldAlpha, AbstractAlphabet newAlpha) {
		return null;
	}

	protected String generatePasswortStringForPostAlphaChange(String oldPassword, SubstitutionKey oldKey,
			Map<Character, Character> oldCharMapping, AbstractAlphabet oldAlpha, AbstractAlphabet newAlpha) {
		// attention: substitution key may be null (when previously, the editor was not completely filled)
		return ""; //$NON-NLS-1$
	}

	private void initializeKeyEditor(Group parent, AbstractAlphabet alphabet) {
		this.keyEditor = new SubstitutionKeyEditor(keyGroup, SWT.NONE, alphabet);
		this.keyEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
    
    
    
}
