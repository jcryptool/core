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
package org.jcryptool.crypto.classic.caesar.ui;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.caesar.CaesarPlugin;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.model.ui.wizard.KeyInput;

/**
 * The wizard page for the caesar wizard.
 *
 * @author t-kern
 *
 */
public class CaesarWizardPage extends AbstractClassicCryptoPage {


	private CaesarKeyInput keyInputComp;
	private boolean dontlistenCombo;
	private Button btnShift0;
	private Button btnShift1;
	protected int aShift;
	/**
	 * Creates a new instance of CaesarWizardPage.
	 */
	public CaesarWizardPage() {
		super(Messages.Label_Caesar, Messages.Label_WizardPageMessage);
	}

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                CaesarPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }
    
    @Override
    protected String generateCommandLineString() {
    	String encDec = operationInput.getContent()?"-E":"-D"; //$NON-NLS-1$ //$NON-NLS-2$
    	String key = "-k " + quoteCmdlineArgIfNecessary(getKey()); //$NON-NLS-1$
    	
    	String result = "caesar " + encDec + " -ed " + key; //$NON-NLS-1$ //$NON-NLS-2$

    	result += " " + generateAlphabetPartForCommandLine(); //$NON-NLS-1$
    	
    	if(!isNonAlphaFilter()) result += " --noFilter"; //$NON-NLS-1$
    	return result;
    }
    
    @Override
    protected void createCustomAlphaGroupObjects(Composite innerGroup) {
    	Composite cmpAlphaExpl = new Composite(innerGroup, SWT.NONE);
    	GridLayout layout = new GridLayout(3, false);
    	layout.marginWidth = 0;
    	layout.marginHeight = 0;
		cmpAlphaExpl.setLayout(layout);
		cmpAlphaExpl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
    	
		Label lblAlphaExpl = new Label(cmpAlphaExpl, SWT.NONE);
		lblAlphaExpl.setText("Interpretation des ersten Alphabetzeichens: ");
		
		btnShift0 = new Button(cmpAlphaExpl, SWT.RADIO);
		btnShift0.setText("Verschiebewert = 0");
		
    	btnShift1 = new Button(cmpAlphaExpl, SWT.RADIO);
    	btnShift1.setText("Verschiebewert = 1");
    }
    
    @Override
    protected void createKeyGroup(Composite parent) {
    	keyGroup = new Group(parent, SWT.NONE);

        GridLayout keyGroupGridLayout = new GridLayout();
        keyGroupGridLayout.numColumns = 2;

        GridData keyGroupGridData = new GridData();
        keyGroupGridData.horizontalAlignment = GridData.FILL;
        keyGroupGridData.grabExcessHorizontalSpace = true;
        keyGroupGridData.grabExcessVerticalSpace = false;
        keyGroupGridData.verticalAlignment = SWT.TOP;

        keyGroup.setLayoutData(keyGroupGridData);
        keyGroup.setLayout(keyGroupGridLayout);
        keyGroup.setText(org.jcryptool.crypto.classic.model.ui.wizard.Messages.WizardPage_key);

        keyInputComp = new CaesarKeyInput(keyGroup, SWT.NONE);
        keyInputComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        
    }
    @Override
    protected void createKeyInputObjects() {
    	keyInput = new KeyInput<String>() {
            
			@Override
            public Text getTextfield() {
                return keyInputComp.passwordText;
            }

            @Override
            public String readContent() {
                return getTextfield().getText();
            }

            @Override
            protected String getDefaultContent() {
                return String.valueOf(getSelectedAlphabet().getCharacterSet()[Math.min(getSelectedAlphabet().getCharacterSet().length-1, 2)]); //$NON-NLS-1$
            }

            @Override
            public String getName() {
                return "Caesar key";
            }

            @Override
            protected void resetExternallyCaused(AbstractUIInput<?> inputWhichCausedThis) {
                // reacting to a change of alphabets
                String keyNow = getTextfield().getText();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < keyNow.length(); i++) {
                    if (getAlphabetInput().getContent().contains(keyNow.charAt(i))) {
                        stringBuilder.append(keyNow.charAt(i));
                    }
                }

                setTextfieldTextExternal(stringBuilder.toString());
                reread(inputWhichCausedThis);
                
                
            }

            @Override
            protected void saveDefaultRawUserInput() {
            	super.saveDefaultRawUserInput();
            	CaesarWizardPage.this.aShift = 0;
            }
            
            @Override
            protected void saveRawUserInput() {
            	super.saveRawUserInput();
            	CaesarWizardPage.this.aShift = btnShift0.getSelection()?0:1;
            }
            
            @Override
            protected void resetUserInput() {
            	// TODO Auto-generated method stub
            	super.resetUserInput();
            	btnShift0.setSelection(aShift==0);
            	btnShift1.setSelection(aShift!=0);
            }
            
            @Override
            public AbstractAlphabet getAlphabet() {
                return getAlphabetInput().getContent();
            }

            @Override
            protected InputVerificationResult verifyUserChange() {
                return KeyVerificator.verify(getTextfield().getText(), getAlphabet(), getKeyVerificators());
            }
            
            @Override
            public void writeContent(String content) {
            	super.writeContent(content);
            	AbstractAlphabet alpha = getSelectedAlphabet();
            	if(content == null || content.length() < 1) {
            		keyInputComp.shiftCombo.select(-1);
            	} else {
            		char keyChar = content.charAt(0);
					int shift = keyCharToShift(alpha, keyChar, aShift);
            		if(!keyInputComp.shiftCombo.getText().equals(String.valueOf(shift))) {
            			dontlistenCombo = true;
            			String valueOf = String.valueOf(shift);
						keyInputComp.shiftCombo.setText(valueOf);
            			dontlistenCombo = false;
            		}
            	}
            	
            }

        };

        final TextfieldInput<String> inp = keyInput;
        
        SelectionAdapter shiftChangeListener = new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		keyInput.synchronizeWithUserSide();
        		updateShiftcombo();
        		keyInput.writeContent(keyInput.getContent());
        	}
		};
		btnShift0.addSelectionListener(shiftChangeListener);
		btnShift1.addSelectionListener(shiftChangeListener);
        
        keyInputComp.shiftCombo.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		if(dontlistenCombo) return;
        		String selection = keyInputComp.shiftCombo.getText();
            	Integer shift = keyInputComp.shiftCombo.getSelectionIndex()+aShift;
            	Character shiftChar = getSelectedAlphabet().getCharacterSet()[shift-aShift];
            	inp.setTextfieldTextExternal(String.valueOf(shiftChar));
            	inp.synchronizeWithUserSide();
        	}
		});
        
        keyInputComp.shiftCombo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(dontlistenCombo) return;
				String selection = keyInputComp.shiftCombo.getText();
            	try{
            		Integer shift = Integer.parseInt(selection);
            		if(shift > 0+getaShift() || shift < keyInputComp.shiftCombo.getItemCount()) {
            			Character shiftChar = getSelectedAlphabet().getCharacterSet()[shift-aShift];
                    	inp.setTextfieldTextExternal(String.valueOf(shiftChar));
                    	inp.synchronizeWithUserSide();
            		} else {
            			throw new RuntimeException("index out of range in Caesar. this should be caught."); //$NON-NLS-1$
            		}
                    	
            	} catch(Exception exc) {
            		inp.writeContent(inp.getContent());
            	}
            	
			}
		});
        
        
        // changes in the currentAlphabet input must be forwarded to the key input for revalidation
        keyInput.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				keyInput.writeContent(keyInput.getContent());
			}
		});
        
        getAlphabetInput().addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				updateShiftcombo();
				keyInput.update(o, arg);
				keyInput.writeContent(keyInput.getContent());
			}
		});
        updateShiftcombo();
    }
    
    private int keyCharToShift(AbstractAlphabet alpha, char keyChar, int aShift) {
		return alpha.asList().indexOf(Character.valueOf(keyChar))+aShift;
	}
    
    private void updateShiftcombo() {
		AbstractAlphabet alpha = getSelectedAlphabet();
		keyInputComp.shiftCombo.setItems(new String[]{});
		List<Character> asList = alpha.asList();
		for (int i = 0; i < asList.size(); i++) {
			String comboItem = String.valueOf(i+getaShift());
			keyInputComp.shiftCombo.add(comboItem);
		}
	}
    
    @Override
    protected void createInputVerificationHandler(Shell shell) {
    	super.createInputVerificationHandler(shell);
    	verificationDisplayHandler.addInputWidgetMapping(keyInput, keyInput.getTextfield());
    }
    
    @Override
    protected void createAlphabetInputObjects() {
    	// TODO Auto-generated method stub
    	super.createAlphabetInputObjects();
    	getAlphabetInput().addObserver(new Observer() {
			
			@Override
			public void update(Observable arg0, Object arg1) {
				updateAlphaGroupText();
			}
		});
    	updateAlphaGroupText();
    }

	protected void updateAlphaGroupText() {
		alphabetGroup.setText(org.jcryptool.crypto.classic.model.ui.wizard.Messages.WizardPage_alpha + 
				String.format(" (current length: %s )", getAlphabetInput().getContent().getCharacterSet().length));
	}
	
	public int getaShift() {
		return aShift;
	}
	
}
