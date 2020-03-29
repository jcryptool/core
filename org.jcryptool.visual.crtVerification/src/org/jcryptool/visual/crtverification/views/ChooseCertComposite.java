// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.views;

import java.util.ArrayList;
import java.util.Enumeration;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;

public class ChooseCertComposite extends Composite {
    private ChooseCertPage page;
    CrtVerViewController controller;
    Button btnLoad;
    private ArrayList<IKeyStoreAlias> publicKeys = new ArrayList<IKeyStoreAlias>();

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public ChooseCertComposite(Composite parent, int style, final ChooseCertPage p, final CrtVerViewController controller) {
        super(parent, style);
        this.page = p;
        this.controller = controller;
        setLayout(new FormLayout());

        ListViewer listViewer = new ListViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        final List list = listViewer.getList();
        FormData fd_list = new FormData();
        fd_list.top = new FormAttachment(0, 5);
        fd_list.right = new FormAttachment(100, -10);
        fd_list.left = new FormAttachment(0, 5);
        list.setLayoutData(fd_list);


        // Wenn ein Listen-Element ausgewählt wird, wird der Load-Button aktiv
        list.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	page.contact = publicKeys.get(list.getSelectionIndex());
            	page.setPageComplete(true);
            	
            }
        });

        Enumeration<String> aliases = controller.getKsc().getAllAliases();
        
        while (aliases.hasMoreElements()) {
            IKeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());
            KeyType type = alias.getKeyStoreEntryType();
            if (type == KeyType.KEYPAIR_PUBLIC_KEY || type == KeyType.PUBLICKEY) {
                list.add(alias.getContactName() + " - " + alias.getOperation());
                publicKeys.add(alias);
            }
        }
    }
}
