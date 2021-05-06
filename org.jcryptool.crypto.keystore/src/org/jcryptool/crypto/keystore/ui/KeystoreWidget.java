// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactDescriptorNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.CertificateContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.KeyPairContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.containers.SecretKeyContainerNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.AbstractKeyNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.KeyPairNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.PrivateKeyNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.SecretKeyNode;

import de.flexiprovider.api.keys.Key;

/**
 * @author Anatoli Barski
 * 
 */
public class KeystoreWidget extends Composite implements ISelectionChangedListener {

    public static class Style {
        public static final int SHOW_SECRETKEYNODES = 1 << 10;
        public static final int SHOW_KEYPAIRNODES = 1 << 9;
        public static final int SHOW_PRIVATEKEYNODES = SHOW_KEYPAIRNODES | (1 << 8);
        public static final int SHOW_PUBLICKEYNODES = SHOW_KEYPAIRNODES | (1 << 7);
        public static final int SHOW_CERTIFICATES = 1 << 6;
        public static final int SHOW_COLLAPSED = 1 << 5;
        public static final int SHOW_ALL = SHOW_KEYPAIRNODES | SHOW_SECRETKEYNODES | SHOW_PRIVATEKEYNODES
                | SHOW_PUBLICKEYNODES | SHOW_CERTIFICATES;

        private int style = SHOW_ALL;

        public Style(int options) {
            init(options);
        }

        public void init(int options) {
            style = options;
        }

        public void set(int option) {
            style = style | option;
        }

        public boolean isSet(int option) {
            return (option & style) == option;
        }

    }

    private final KeystoreViewer viewer;
    private Object selection;

    /**
     * @return the viewer
     */
    public KeystoreViewer getViewer() {
        return viewer;
    }

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param algorithm
     */
    public KeystoreWidget(Composite parent, final Style style, final String algorithm) {
        super(parent, SWT.NONE);
        setLayout(new GridLayout(1, false));

        viewer = new KeystoreViewer(this);
        if (!style.isSet(Style.SHOW_COLLAPSED)) {
            viewer.expandToLevel(3);
        }

        viewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof ContactDescriptorNode)
                    return true;
                if (style.isSet(Style.SHOW_ALL))
                    return true;
                if (style.isSet(Style.SHOW_KEYPAIRNODES) && element instanceof KeyPairContainerNode)
                    return true;
                if (style.isSet(Style.SHOW_KEYPAIRNODES) && element instanceof KeyPairNode)
                    return true;
                if (style.isSet(Style.SHOW_SECRETKEYNODES) && element instanceof SecretKeyContainerNode)
                    return true;
                if (style.isSet(Style.SHOW_SECRETKEYNODES) && element instanceof SecretKeyNode)
                    return true;
                if (style.isSet(Style.SHOW_PRIVATEKEYNODES) && element instanceof PrivateKeyNode)
                    return true;
                if (style.isSet(Style.SHOW_PUBLICKEYNODES) && element instanceof CertificateNode)
                    return true;
                if (style.isSet(Style.SHOW_CERTIFICATES) && element instanceof CertificateContainerNode)
                    return true;
                if (style.isSet(Style.SHOW_CERTIFICATES) && element instanceof CertificateNode)
                    return true;
                return false;
            }
        });

        viewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (algorithm == null)
                    return true;
                if (element instanceof SecretKeyNode) {
                    if (!((SecretKeyNode) element).getAlias().getOperation().startsWith(algorithm))
                        return false;
                }
                if (element instanceof KeyPairNode) {
                    if (!((KeyPairNode) element).getPrivateKeyAlias().getOperation().startsWith(algorithm))
                        return false;
                    if (!((KeyPairNode) element).getPublicKeyAlias().getOperation().startsWith(algorithm))
                        return false;
                }
                if (element instanceof PrivateKeyNode) {
                    if (!((PrivateKeyNode) element).getAlias().getOperation().startsWith(algorithm))
                        return false;
                }
                if (element instanceof CertificateNode) {
                    if (!((CertificateNode) element).getAlias().getOperation().startsWith(algorithm))
                        return false;
                }
                return true;
            }
        });

        viewer.addSelectionChangedListener(this);

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * @return
     */
    public Key getSelectedKey() {
        if (selection == null)
            return null;

        try {
            if (selection instanceof AbstractKeyNode) {
                return KeyStoreManager.getInstance().getKey(((AbstractKeyNode) selection).getAlias(),
                        KeyStoreManager.KEY_PASSWORD);
            }
        } catch (UnrecoverableEntryException ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "The entered password was not correct.", ex, true);
        } catch (NoSuchAlgorithmException ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "The requested algorithm is not supported.", ex, true);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent
     * )
     */
    public void selectionChanged(SelectionChangedEvent event) {
        ISelection sel = event.getSelection();
        if (sel instanceof IStructuredSelection) {
            selection = ((IStructuredSelection) sel).getFirstElement();
        }
    }

    /**
     * @return
     */
    public IKeyStoreAlias getSelectedAlias() {
        if (selection == null)
            return null;

        if (selection instanceof AbstractKeyNode) {
            return ((AbstractKeyNode) selection).getAlias();
        }

        return null;
    }

}
