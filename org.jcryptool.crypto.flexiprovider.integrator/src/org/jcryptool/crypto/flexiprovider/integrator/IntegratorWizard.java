//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaPaddingScheme;
import org.jcryptool.crypto.flexiprovider.keystore.KeyStoreHelper;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * The wizard for the FlexiProvider ciphers.
 *
 * @author mwalthart
 */
public class IntegratorWizard extends Wizard {
    private IntegratorWizardPage page;
	private String title;
	private String page_title;
	private String header_description;
	private boolean showOperationGroup;
	private boolean showPaddingGroup;
	private String showKeyGroup;
	private boolean showKeySourceGroup;
	private int[] validKeyLengths;
	private boolean showSignatureGroup;
	private boolean showRandomGroup;
	private int showMessageDigestGroup;
	private int algorithmType;
	

    /**
     * Creates a new instance of DummyWizard.
     * @param algorithmType 
     */
    public IntegratorWizard(String page_title,
    		String title,
    		String header_description,
    		boolean showOperationGroup,
    		boolean showPaddingGroup,
    		String showKeyGroup,
    		boolean showKeySourceGroup,
    		int[] validKeyLengths,
    		boolean showSignatureGroup,
    		boolean showRandomGroup,
    		int showMessageDigestGroup, int algorithmType) {
        setWindowTitle(page_title);
        this.title = title;
        this.page_title = page_title;
        this.header_description = header_description;
        this.showOperationGroup = showOperationGroup;
        this.showPaddingGroup = showPaddingGroup;
        this.showKeyGroup = showKeyGroup;
        this.showKeySourceGroup = showKeySourceGroup;
        this.validKeyLengths = validKeyLengths;
        this.showSignatureGroup = showSignatureGroup;
        this.showRandomGroup = showRandomGroup;
        this.showMessageDigestGroup = showMessageDigestGroup;
        this.algorithmType = algorithmType;
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() {
        page = new IntegratorWizardPage(page_title,
        		title,
        		header_description,
        		showOperationGroup,
        		showPaddingGroup,
        		showKeyGroup,
        		showKeySourceGroup,
        		validKeyLengths,
        		showSignatureGroup,
        		showRandomGroup,
        		showMessageDigestGroup,
        		algorithmType);
        addPage(page);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performCancel()
     */
    @Override
    public boolean performCancel() {
        Job[] jobs = Job.getJobManager().find(KeyStoreHelper.KEYSTOREHELPER_FAMILY);
        if(jobs.length == 1)
        {
            Job job = jobs[0];
            job.getThread().interrupt();
            job.cancel();
        }
        return super.performCancel();
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        return true;
    }

    /**
     * Returns <code>true</code>, if the selected operation is <i>Encrypt</i>;
     * <code>false</code> if it is <i>Decrypt</i>.
     *
     * @return <code>true</code>, if the selected operation is <i>Encrypt</i>;
     *         <code>false</code> if it is <i>Decrypt</i>
     */
    public boolean encrypt() {
        return page.encrypt();
    }

    /**
     * returns the selected padding scheme
     * @return the selected padding scheme
     */
	public IMetaPaddingScheme getPadding() {
		return page.padding();
	}

	/**
	 * returns the selected mode
	 * @return the selected mode
	 */
	public IMetaMode getMode() {
		return page.mode();
	}

	/**
	 * returns the selected keystore alias
	 * @return the selected keystore alias
	 */
	public KeyStoreAlias getKey() {
		return page.key();
	}

	/**
	 * returns the selected signature file
	 * @return the selected signature file
	 */
	public String signature() {
		return page.signature();
	}

	/**
	 * returns the entered random size
	 * @return the entered random size
	 */
	public int getRandomSize() {
		return page.getRandomSize();
	}

	/**
	 * returns true if a filter applies
	 * @return true if a filter applies
	 */
	public boolean doFilter(){
		return page.doFilter();
	}

	/**
	 * returns the characters to be filtered/allowed
	 * @return the characters to be filtered/allowed
	 */
	public byte[][] getFilter(){
		return page.getFilter();
	}

	public String getExpectedChecksum(){
		return page.getExpectedChecksum();
	}

	/**
	 * Returns whether a key entered by the user should be used
	 */
	public boolean useCustomKey() {
		return page.useCustomKey();
	}
	
	/**
	 * Returns whether a key entered by the user should be used
	 */
	public byte[] getCustomKey() {
		return page.getCustomKey();
	}
}
