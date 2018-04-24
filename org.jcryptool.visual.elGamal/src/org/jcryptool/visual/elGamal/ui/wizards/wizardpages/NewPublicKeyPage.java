// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import java.math.BigInteger;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for entering the parameters of a new Public Key.
 *
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class NewPublicKeyPage extends WizardPage {
    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "New Public Key Page"; //$NON-NLS-1$

    /**
     * getter for the pagename constant for easy access.
     *
     * @return the pagename
     */
    public static String getPagename() {
        return PAGENAME;
    }

    /** verify listener for checking inputs. */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

    private final ModifyListener ml = new ModifyListener() {
        public void modifyText(final ModifyEvent e) {
            setPageComplete();
        }
    };

    /** data-object storing all relevant information about the algorithm. */
    private final ElGamalData data;

    /** selection whether this key should be saved. */
    private Button saveButton;

    /** field for entering the value for p */
    private Text ptext;

    /** field for entering the value for g */
    private Text gtext;

    /** field for entering the value for a */
    private Text atext;

    /** basic composite of this page */
    private Composite composite;

    /**
     * Constructor for a new wizardpage getting the data object.
     *
     * @param data the data object
     */
    public NewPublicKeyPage(final ElGamalData data) {
        super(PAGENAME, Messages.NewPublicKeyPage_select_params, null);
        this.data = data;
        this.setDescription(Messages.NewPublicKeyPage_select_params_text);
        setPageComplete(false);
    }

    /**
     * Set up the UI stuff.
     *
     * @param parent the parent composite
     */
    public final void createControl(final Composite parent) {
        // do stuff like layout et al
        composite = new Composite(parent, SWT.NONE);
        GridLayout gl_composite = new GridLayout(3, false);
        gl_composite.marginWidth = 50;
        composite.setLayout(gl_composite);

        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        label.setText(Messages.NewPublicKeyPage_select_p);
        new Label(composite, SWT.NONE).setText("p = "); //$NON-NLS-1$
        ptext = new Text(composite, SWT.BORDER);
        ptext.addVerifyListener(VL);
        ptext.addModifyListener(ml);
        Button suggestPrime = new Button(composite, SWT.PUSH);
        suggestPrime.setText(Messages.NewPublicKeyPage_4);
        suggestPrime.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ptext.getText().equals("")) { //$NON-NLS-1$
					//Get a random Prime
					int rndm = (int) (Math.random() * 19500 + 257);
					ptext.setText(Integer.toString(Lib.PRIMES.lower(rndm)));
				} else {
					//Get a prime near the entered value
					ptext.setText(Integer.toString(Lib.PRIMES.lower(Integer.parseInt(ptext.getText()))));
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

        Label separator1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_separator1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_separator1.verticalIndent = 10;
        separator1.setLayoutData(gd_separator1);
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewPublicKeyPage_select_g);
        GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_label.verticalIndent = 10;
        label.setLayoutData(gd_label);
        new Label(composite, SWT.NONE).setText("g = "); //$NON-NLS-1$
        gtext = new Text(composite, SWT.BORDER);
        gtext.addVerifyListener(VL);
        gtext.addModifyListener(ml);
        
        Button suggestGenerator = new Button(composite, SWT.PUSH);
        suggestGenerator.setText(Messages.NewPublicKeyPage_7);
        suggestGenerator.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (gtext.getText().equals("")) { //$NON-NLS-1$
					// get a random generator between 200 and 15000
					int rnd = (int) (Math.random()*15000+200);
					gtext.setText(getRandomGenerator(Integer.toString(rnd), ptext.getText()).toString());
				} else {
					// Get a Generator near the entered value
					gtext.setText(getRandomGenerator(gtext.getText(), ptext.getText()).toString());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

        Label separator2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_separator2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_separator2.verticalIndent = 20;
        separator2.setLayoutData(gd_separator2);
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        label.setText(Messages.NewPublicKeyPage_select_A);
        GridData gd_label2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_label2.verticalIndent = 10;
        label.setLayoutData(gd_label2);
        new Label(composite, SWT.NONE).setText("B = "); //$NON-NLS-1$
        atext = new Text(composite, SWT.BORDER);
        atext.addVerifyListener(VL);
        atext.addModifyListener(ml);
        Button suggestA = new Button(composite, SWT.PUSH);
        suggestA.setText(Messages.NewPublicKeyPage_10);
        suggestA.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!ptext.getText().equals("")) { //$NON-NLS-1$
					// if p is set, set A to a value between 1 and p - 1
					int rnd = (int) (Math.random()*Integer.parseInt(ptext.getText()));
					atext.setText(Integer.toString(rnd));
				} else {
					// if p isn't set, set A to a number between 1 and 1000 
					int rnd = (int) (Math.random()*10000 + 1);
					atext.setText(Integer.toString(rnd));
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
        
        //Spacer
        new Label(composite, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

        // Separator
        Label separator3 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_separator3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_separator3.verticalIndent = 10;
        separator3.setLayoutData(gd_separator3);

        // should this key be saved?
        saveButton = new Button(composite, SWT.CHECK);
        saveButton.setText(Messages.NewPublicKeyPage_save_pubkey);
        saveButton.setToolTipText(Messages.NewPublicKeyPage_save_pubkey_popup);
        saveButton.setSelection(data.isStandalone());
        saveButton.setEnabled(!data.isStandalone());
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        saveButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                getContainer().updateButtons();
            }
        });

        // fill in old data
        if (data.getPublicA() != null) {
            ptext.setText(data.getModulus().toString());
            gtext.setText(data.getGenerator().toString());
            atext.setText(data.getPublicA().toString());
        }

        // finishing touch
        setControl(composite);
    }

    /**
     * 
     * @param generator a value near a generator for the prime
     * @param prime a prime number
     * @return a generator to the given prime
     */
	protected BigInteger getRandomGenerator(String generator, String prime) {
		BigInteger p = new BigInteger(prime);
		BigInteger g = new BigInteger(generator);
		while (!Lib.checkGenerator(g, p)) {
			g = g.add(BigInteger.ONE);
		}
		return g;
	}

	/**
     * checks whether this page is completed and sets the status accordingly
     */
    private void setPageComplete() {
        setErrorMessage(null);
        if (!ptext.getText().equals("")) { //$NON-NLS-1$
            final BigInteger p = new BigInteger(ptext.getText());
            if (p.compareTo(Constants.TWOFIVESIX) < 0) {
                setErrorMessage(Messages.NewPublicKeyPage_error_p_lt_256);
            }
            if (!Lib.isPrime(p)) {
                setErrorMessage(Messages.NewPublicKeyPage_error_p_not_prime);
            }

            if (!gtext.getText().equals("")) { //$NON-NLS-1$
                final BigInteger g = new BigInteger(gtext.getText());
                if (!Lib.checkGenerator(g, p)) {
                    setErrorMessage(Messages.NewPublicKeyPage_error_g_not_generator);
                }
            } else {
                setErrorMessage(Messages.NewPublicKeyPage_0);
            }

            if (!atext.getText().equals("")) { //$NON-NLS-1$
                final BigInteger a = new BigInteger(atext.getText());
                if (a.compareTo(p) > 0) {
                    setErrorMessage(Messages.NewPublicKeyPage_error_A_gt_p);
                }
            } else {
                setErrorMessage(Messages.NewPublicKeyPage_1);
            }
        } else {
            setErrorMessage(Messages.NewPublicKeyPage_2);
        }

        setPageComplete(getErrorMessage() == null);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardPage#setPageComplete(boolean)
     */
    @Override
    public void setPageComplete(boolean complete) {
        if (complete) {
            if (!gtext.getText().isEmpty()) {
                data.setGenerator(new BigInteger(gtext.getText()));
            }
            if (!ptext.getText().isEmpty()) {
                data.setModulus(new BigInteger(ptext.getText()));
            }
            if (!atext.getText().isEmpty()) {
                data.setPublicA(new BigInteger(atext.getText()));
            }
        }
        super.setPageComplete(complete);
    }

    @Override
    public void setErrorMessage(final String newMessage) {
        if (newMessage == null || getErrorMessage() == null) {
            super.setErrorMessage(newMessage);
        } else {
            super.setErrorMessage(getErrorMessage() + "\n" + newMessage); //$NON-NLS-1$
        }
    }

    @Override
    public final IWizardPage getNextPage() {
        if (wantSave()) {
            return super.getNextPage();
        } else {
            return null;
        }
    }

    /**
     * getter for the status of the save button to be accessed externally.
     *
     * @return whether the user wants to save the key
     */
    public final boolean wantSave() {
        return saveButton.getSelection();
    }
}
