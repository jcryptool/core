// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;

/**
 * @author Anatoli Barski
 * 
 */
public class ShowCertificateDialog extends CommonPropertyDialog {

    private CertificateNode certificateNode;

    public ShowCertificateDialog(Shell parentShell, CertificateNode certificateNode) {
        super(parentShell, certificateNode);
        this.certificateNode = certificateNode;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
    	
    	// Only print the first 100 Chars to avoid the 
    	// dialog from taking too much horizontal space
    	String alias = certificateNode.getAlias().toString();
    	if (alias.length() > 100) {
    		alias = alias.substring(0, 100) + "...";
    	}
    	
        setTitle(Messages.getString("certificate.dialog.title") + "\n" + alias); //$NON-NLS-1$
        setTitleImage(ImageService.getImage(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_identity.png"));
        Composite container = (Composite) super.createDialogArea(parent);

        // Set the title of the shell
        getShell().setText(Messages.getString("CommonPropertyDialog.certificateTitle"));
        
        return container;
    }

}
