// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizards;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.visual.extendedrsa.ExtendedTabFolder;
import org.jcryptool.visual.extendedrsa.Identity;
import org.jcryptool.visual.extendedrsa.IdentityManager;
import org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages.ManageVisibleIdentitiesPage;

/**
 * This is the wizard to create a new Identity with the button in the visual
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class ManageVisibleIdentitesWizard extends Wizard {
    private ManageVisibleIdentitiesPage visiblePage;
    private ExtendedTabFolder tabfolder;
    private Text txtExplain;

    public ManageVisibleIdentitesWizard(ExtendedTabFolder folder, Text txtExplain) {
        this.tabfolder = folder;
        this.txtExplain = txtExplain;
    }

    @Override
    public final void addPages() {
        visiblePage = new ManageVisibleIdentitiesPage(tabfolder);
        addPage(visiblePage);
    }

    @Override
    public boolean performFinish() {
        if (visiblePage.isPageComplete()) {
            Vector<String> allreadyAdded = new Vector<String>();
            for (String s : IdentityManager.getInstance().getContacts()) {
                if (visiblePage.getAllreadyShownList().contains(s) && !visiblePage.getDisplayList().contains(s)) {
                    // id 'deselected' -> remove from visual
                    for (TabItem ti : tabfolder.getItems()) {
                        Identity current = (Identity) ti;
                        if (current.getIdentityName().equals(s)) {
                            current.dispose();
                        }
                    }
                }
                if (!visiblePage.getAllreadyShownList().contains(s) && visiblePage.getDisplayList().contains(s)) {
                    // create selected identity in the visual
                    Iterator<Contact> it = ContactManager.getInstance().getContacts();

                    Contact contactNode;
                    while (it.hasNext()) {
                        contactNode = it.next();
                        if (contactNode.getName().equals(s) && !allreadyAdded.contains(s)) {
                            new Identity(tabfolder, SWT.NONE, contactNode, txtExplain);
                            allreadyAdded.add(contactNode.getName());
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
