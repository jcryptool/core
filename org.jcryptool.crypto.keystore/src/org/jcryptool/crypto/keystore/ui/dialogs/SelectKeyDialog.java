// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.KeystoreWidget;
import org.jcryptool.crypto.keystore.ui.KeystoreWidget.Style;

import de.flexiprovider.api.keys.Key;

/**
 * @author Anatoli Barski
 * 
 */
public class SelectKeyDialog extends TitleAreaDialog implements IDoubleClickListener {

    private String algorithm = null; // null = no filtering by algorithm name
    private Style style;
    private KeystoreWidget keystoreWidget;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     * @param site
     * @param options
     * @param algorithm
     */
    public SelectKeyDialog(Shell parentShell, Style style, String algorithm) {
        super(parentShell);
        setShellStyle(SWT.RESIZE | SWT.TITLE);
        this.algorithm = algorithm;
        this.style = style;
    }

    /**
     * Creates contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.getString("SelectKeyDialog.title")); //$NON-NLS-1$
        setTitleImage(ImageService.getImage(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_key3.png"));
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new FillLayout(SWT.HORIZONTAL));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        keystoreWidget = new KeystoreWidget(container, this.style, this.algorithm);
        keystoreWidget.getViewer().addDoubleClickListener(this);

        return area;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(482, 345);
    }

    public Key getSelectedKey() {
        return keystoreWidget.getSelectedKey();
    }

    public IKeyStoreAlias getSelectedAlias() {
        return keystoreWidget.getSelectedAlias();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
     */
    public void doubleClick(DoubleClickEvent event) {
        this.okPressed();
    }

}
