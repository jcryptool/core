// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import static java.math.BigInteger.ONE;
import java.math.BigInteger;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.ElGamalPlugin;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for creating a new RSA public-private-keypair.
 *
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class NewKeypairPage extends WizardPage {

    /**
     * a {@link VerifyListener} instance that makes sure only digits are entered.
     */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

    /** shared data-object to push around. */
    private final ElGamalData data;

    /** Selection whether the user wants to save the new Keypair. */
    private Button saveKeypairButton;

    /** Drop-Down for selecting the p values. */
    private Combo dfield;

    /** field to enter q */
    private Text qfield;

    /** field for selecting a g */
    private Combo gfield;

    /** field for entering the private b */
    private Text bfield;

    /** field for entering the public B */
    private Text btext;
    
    /** text for the p changed note */
    private CLabel pchanged;
    
    private ModifyListener listenerCalculateB = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {

			 if (!bfield.getText().isEmpty()) {
                 BigInteger b = new BigInteger(bfield.getText());
                 if (dfield.getText().isEmpty()) {
                 	return;
                 }
                 if (b.compareTo(Constants.TWO) < 0
                         || b.compareTo(new BigInteger(dfield.getText()).subtract(Constants.TWO)) > 0) {
                     setErrorMessage(Messages.NewKeypairPage_error_invalid_a);
                     setPageComplete(false);
                 } else {
                 	// Check if a value in g is selected
                 	if (gfield.getText().isEmpty()) {
                 		setPageComplete(false);
                 	} else {
	                        btext.setText(new BigInteger(gfield.getText()).modPow(b, new BigInteger(dfield.getText())).toString());
	                        setErrorMessage(null);
	                        setPageComplete(true);
                 	}
                 }
             } else {
                 setPageComplete(false);
             }
		}
	};

    /**
     * Constructor, setting description completeness-status and data-object.
     *
     * @param data the data object to store the entered values
     */
    public NewKeypairPage(final ElGamalData data) {
        super("New Keypair Page", Messages.NewKeypairPage_choose_params, null);
        this.setDescription(Messages.NewKeypairPage_choose_params_text);
        this.data = data;
        setPageComplete(false);
    }

    /**
     * set up the UI stuff.
     *
     * @param parent the parent composite
     */
    @Override
	public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        // set layout
        //Layout with 5 rows
        GridLayout gl = new GridLayout(5, false);
        gl.marginWidth = 50;
        composite.setLayout(gl);
        // begin stuff
        // modulus
        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        label.setText(Messages.NewKeypairPage_choose_p_text);
        
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        label.setText(Messages.NewKeypairPage_choose_q_text);
        
        new Label(composite, SWT.NONE).setText("d = "); //$NON-NLS-1$
        
        dfield = new Combo(composite, SWT.SINGLE);
        dfield.addVerifyListener(VL);
        dfield.addSelectionListener(new SelectionAdapter() {
        	
            @Override
			public void widgetSelected(final SelectionEvent e) {
                qfield.setText(""); //$NON-NLS-1$
            }
        });
        
		dfield.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if (dfield.getText().isEmpty()) {
					return;
				}
				BigInteger modulus = new BigInteger(dfield.getText());
				String error = getErrorMessage();
				if (!Lib.isPrime(modulus)) {
					if (error == null) {
						setErrorMessage(Messages.NewKeypairPage_error_p_not_prime);
					} else {
						setErrorMessage(error + "\n" //$NON-NLS-1$
								+ Messages.NewKeypairPage_error_p_not_prime);
					}
				} else {
					setErrorMessage(null);
					pchanged.setVisible(false);
				}
				if (modulus.compareTo(Constants.TWOFIVESIX) <= 0) {
					setErrorMessage(Messages.NewKeypairPage_error_p_lt_256);
				}
				bfield.setText(""); //$NON-NLS-1$
				btext.setText("");
				gfield.setItems(Lib.calcG(modulus).toArray(new String[0]));
			}
		});
        filld();
        
        Label qLabel = new Label(composite, SWT.NONE);
        qLabel.setText("q = ");
        GridData gd_qLable = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gd_qLable.horizontalIndent = 20;
        qLabel.setLayoutData(gd_qLable);
        
        qfield = new Text(composite, SWT.BORDER);
        qfield.addVerifyListener(VL);
        qfield.addModifyListener(new ModifyListener() {
        	
            @Override
			public void modifyText(ModifyEvent e) {
                if (qfield.getText().equals("")) { //$NON-NLS-1$
                    return;
                }
                BigInteger q = new BigInteger(qfield.getText());
                if (Lib.isPrime(q)) {
                    setErrorMessage(null);
                    dfield.setText(q.multiply(Constants.TWO).add(ONE).toString());
                    pchanged.setVisible(true);
                } else {
                    setErrorMessage(Messages.NewKeypairPage_error_q_not_prime);
                    pchanged.setVisible(false);
                }
            }
        });
        
        pchanged = new CLabel(composite, SWT.NONE);
        pchanged.setVisible(false);
        pchanged.setText(Messages.NewKeypairPage_pChanged);
        pchanged.setImage(ElGamalPlugin.getImageDescriptor("platform:/plugin/org.eclipse.jface/org/eclipse/jface/dialogs/images/message_info.png").createImage());
        
        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        
        // primitive root
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewKeypairPage_select_g);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        label.setText(Messages.NewKeypairPage_real_g_values);
        
        new Label(composite, SWT.NONE).setText("g = "); //$NON-NLS-1$
        
        gfield = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        gfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        gfield.addModifyListener(listenerCalculateB);
        
        
        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        // a
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewKeypairPage_select_a);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewKeypairPage_A_explanation);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        
        new Label(composite, SWT.NONE).setText("b = "); //$NON-NLS-1$
        
        bfield = new Text(composite, SWT.SINGLE | SWT.BORDER);
        bfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        bfield.addVerifyListener(VL);
        bfield.addModifyListener(listenerCalculateB);
        

        Label bLabel = new Label(composite, SWT.NONE); 
        bLabel.setText("B = ");
        GridData gd_bLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gd_bLabel.horizontalIndent = 20;
        bLabel.setLayoutData(gd_bLabel);
        
        
        btext = new Text(composite, SWT.READ_ONLY | SWT.BORDER);
        btext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
       
        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        
        // Save?
        saveKeypairButton = new Button(composite, SWT.CHECK);
        saveKeypairButton.setText(Messages.NewKeypairPage_save_keypair);
        saveKeypairButton.setToolTipText(Messages.NewKeypairPage_save_keypair_popup);
        saveKeypairButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        saveKeypairButton.setSelection(data.isStandalone());
        saveKeypairButton.setEnabled(!data.isStandalone());
        saveKeypairButton.addSelectionListener(new SelectionAdapter() {
        	
            @Override
			public void widgetSelected(SelectionEvent e) {
                getContainer().updateButtons();
            }
        });

        // fill in old data
        if (data.getA() != null) {
            dfield.setText(data.getModulus().toString());
            gfield.setText(data.getGenerator().toString());
            bfield.setText(data.getA().toString());
        }
        
		//Select the save field and disable it for the user when it is standalone (only key creation)
		if (data.isStandalone()) {
			saveKeypairButton.setSelection(true);
			saveKeypairButton.setEnabled(false);
		}

        // finish
        setControl(composite);
    }

    private void filld() {
        for (final Integer i : Lib.POSSBLE_PS) {
            dfield.add(i.toString());
        }
    }
    
    @Override
    public boolean canFlipToNextPage() {
		if (isPageComplete() && wantSave()) {
			return true;
		} else {
			return false;
		}
    }

    @Override
    public final IWizardPage getNextPage() {
        if (saveKeypairButton.getSelection()) {
        	return getWizard().getPage("Save Keypair Page");
        } else {
            return null;
        }
    }
    
	/**
	 * @return the modulus d as BigInteger
	 * If something went wrong null is returned.
	 */
	public BigInteger getModulus() {
		try {
			return new BigInteger(dfield.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}
	
	/**
	 * @return the generator g
	 * If something went wrong null is returned.
	 */
	public BigInteger getGenerator() {
		try {
			return new BigInteger(gfield.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}
	
	/**
	 * @return the public Exponent B
	 * If something went wrong null is returned.
	 */
	public BigInteger getExponentB() {
		try {
			return new BigInteger(btext.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}
	
	/**
	 * @return the private b
	 * If something went wrong null is returned.
	 */
	public BigInteger getPrivateb() {
		try {
			return new BigInteger(bfield.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}

    /**
     * getter for the selection-status of the save-button.
     *
     * @return the selection-status
     */
    public final boolean wantSave() {
        return saveKeypairButton.getSelection();
    }
}
