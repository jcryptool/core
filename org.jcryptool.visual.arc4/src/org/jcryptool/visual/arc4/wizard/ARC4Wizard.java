//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;
import org.jcryptool.visual.arc4.ui.DatavectorVisual;

/**
 * The class that manages single pages of the wizard; currently this wizards only have one page
 * 
 * @author Luca Rupp
 */
public class ARC4Wizard extends Wizard {

    // type of the wizard: there are key and plaintext wizards; values are defined
    // in ARC4Con
    private int type;

    // the description of the wizard page
    private String description;

    // the name of the single wizard page
    private String pagename;
    
    // the heading of the wizard description
    private String heading;

    // to make the connection to the parent in order to pass the data
    private DatavectorVisual parent;

    // the single wizard page that takes the input
    private ARC4WizardPage page;

    /**
     * The constructor for the ARC4Wizard
     * 
     * @param type the type of wizard to create, values are defined in ARC4Con
     * @param parent the datavector visual object to which to pass the data
     */
    public ARC4Wizard(int type, DatavectorVisual parent) {
        this.parent = parent;
        this.type = type;
        this.setNameAndDescription();
    }

    /**
     * Set the pagenamen and description for the single page depending on the type of wizard
     */
    public void setNameAndDescription() {
        if (this.type == ARC4Con.KEY) {
            this.pagename = Messages.DatavectorVisualKEYWizard;
            this.description = Messages.WizardPageDescriptionKey;
            this.heading = Messages.KeySelectionWizardHeading;
        } else if (this.type == ARC4Con.PLAIN) {
            this.pagename = Messages.DatavectorVisualPLAINWizard;
            this.description = Messages.WizardPageDescriptionPlain;
            this.heading = Messages.PlainSelectionWizardHeading;
        }
    }

    /**
     * Create the single page and add it to the wizard
     */
    @Override
	public void addPages() {
    	this.setWindowTitle(pagename);
        page = new ARC4WizardPage(heading, description);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        // pass the data to the datavector visual object
        parent.setData(page.getInput());
        return true;
    }

}