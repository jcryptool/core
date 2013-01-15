// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreActionManager;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.crypto.keystore.ui.KeystoreViewer;
import org.jcryptool.crypto.keystore.ui.actions.ChooseKeyStoreAction;
import org.jcryptool.crypto.keystore.ui.actions.IKeyStoreActionDescriptor;
import org.jcryptool.crypto.keystore.ui.actions.ShadowKeyStoreAction;
import org.jcryptool.crypto.keystore.ui.actions.contacts.DeleteContactAction;
import org.jcryptool.crypto.keystore.ui.actions.contacts.NewContactAction;
import org.jcryptool.crypto.keystore.ui.actions.del.DeleteCertificateAction;
import org.jcryptool.crypto.keystore.ui.actions.del.DeleteKeyPairAction;
import org.jcryptool.crypto.keystore.ui.actions.del.DeleteSecretKeyAction;
import org.jcryptool.crypto.keystore.ui.actions.ex.ExportCertificateAction;
import org.jcryptool.crypto.keystore.ui.actions.ex.ExportKeyPairAction;
import org.jcryptool.crypto.keystore.ui.actions.ex.ExportSecretKeyAction;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IChangeKeyStoreListener;
import org.jcryptool.crypto.keystore.ui.views.interfaces.ISelectedNodeListener;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IViewKeyInformation;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactDescriptorNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.NodeType;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.KeyPairNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.SecretKeyNode;

/**
 *
 *
 */
public class KeystoreView extends ViewPart implements ISelectedNodeListener, IViewKeyInformation,
        IChangeKeyStoreListener {
    public static final String ID = "org.jcryptool.crypto.keystore.KeystoreView"; //$NON-NLS-1$

    private boolean newSymmetricKeyActionContributed = false;

    private Action chooseKeyStoreAction = new ChooseKeyStoreAction(this);

    private Action exportSecretKeyAction = new ExportSecretKeyAction(this);
    private Action exportKeyPairAction = new ExportKeyPairAction(this);
    private Action exportPublicKeyAction = new ExportCertificateAction(this);
    
    private Action newContactAction = new NewContactAction(this);
    private Action deleteContactAction = new DeleteContactAction(this);
    
    private Action deleteSecretKeyAction = new DeleteSecretKeyAction(this);
    private Action deleteKeyPairAction = new DeleteKeyPairAction(this);
    private Action deleteCertificateAction = new DeleteCertificateAction(this);

    private NodeType selectedNodeType;
    private String selectedNodeInfo;
    private KeyStoreAlias selectedNodeAlias;
    private SecretKeyNode selectedSecretKeyNode;
    private KeyPairNode selectedKeyPairNode;
    private CertificateNode selectedPublicKeyNode;

    private KeystoreViewer viewer;
    private Label keyStoreNameLabel;

    /**
     * The constructor.
     */
    public KeystoreView() {
        LogUtil.logInfo("Starting Keystore View"); //$NON-NLS-1$

        if(!KeyStorePlugin.isInitialized()) KeyStorePlugin.initialize();
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent) {
        
        keyStoreNameLabel = new Label(parent, SWT.NULL);
        keyStoreNameLabel.setText(KeyStorePlugin.getCurrentKeyStore());
        viewer = new KeystoreViewer(parent);
        getSite().setSelectionProvider(viewer);
        
        parent.setLayout(new GridLayout());
        hookContextMenu();
        contributeToActionBars();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, KeyStorePlugin.PLUGIN_ID + ".KeystoreView"); //$NON-NLS-1$
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                if(viewer.getTree().getSelection().length == 0) return;
                
                Object selection = viewer.getTree().getSelection()[0].getData();
                if (selection instanceof ContactDescriptorNode) {
                    selectedNodeType = NodeType.CONTACT_NODE;
                    selectedNodeInfo = ((ContactDescriptorNode) selection).getName();
                    fillContactContextMenu(manager);
                } else if (selection instanceof SecretKeyNode) {
                    LogUtil.logInfo("right-click on a secretkey"); //$NON-NLS-1$
                    LogUtil.logInfo(((SecretKeyNode) selection).getAlias().getAliasString());
                    selectedNodeType = NodeType.SECRETKEY_NODE;
                    selectedNodeAlias = ((SecretKeyNode) selection).getAlias();
                    selectedSecretKeyNode = ((SecretKeyNode) selection);
                    fillSecretKeyContextMenu(manager);
                } else if (selection instanceof KeyPairNode) {
                    LogUtil.logInfo("right-click on a keypair"); //$NON-NLS-1$
                    if(((KeyPairNode) selection).getPrivateKeyAlias() != null) LogUtil.logInfo(((KeyPairNode) selection).getPrivateKeyAlias().getAliasString());
                    selectedNodeType = NodeType.KEYPAIR_NODE;
                    selectedNodeAlias = ((KeyPairNode) selection).getPrivateKeyAlias();
//                    if(selectedNodeAlias == null) {
//                    	System.err.println("key pair contained only one key");
//                    	selectedNodeAlias = ((KeyPairNode) selection).getPublicKeyAlias();
//                    }
                    selectedKeyPairNode = ((KeyPairNode) selection);
                    fillKeyPairContextMenu(manager);
                } else if (selection instanceof CertificateNode) {
                    LogUtil.logInfo("right click on a public key"); //$NON-NLS-1$
                    KeyStoreAlias alias = ((CertificateNode) selection).getAlias();
                    if (alias.getKeyStoreEntryType().equals(KeyType.PUBLICKEY)) {
                        selectedNodeType = NodeType.PUBLICKEY_NODE;
                        selectedNodeAlias = ((CertificateNode) selection).getAlias();
                        selectedPublicKeyNode = ((CertificateNode) selection);
                        fillCertificateContextMenu(manager);
                    } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
                        selectedNodeType = NodeType.PUBLICKEY_NODE;
                        selectedNodeAlias = ((CertificateNode) selection).getAlias();
                        selectedPublicKeyNode = ((CertificateNode) selection);
                        fillKeyPairPublicContextMenu(manager);
                    }
                }
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu("org.jcryptool.crypto.keystore.popup", menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillCertificateContextMenu(IMenuManager manager) {
        manager.add(exportPublicKeyAction);
        manager.add(new Separator());
        manager.add(deleteCertificateAction);
    }

    private void fillKeyPairPublicContextMenu(IMenuManager manager) {
        manager.add(exportPublicKeyAction);
    }

    private void fillKeyPairContextMenu(IMenuManager manager) {
        manager.add(exportKeyPairAction);
        manager.add(new Separator());
        manager.add(deleteKeyPairAction);
    }

    private void fillContactContextMenu(IMenuManager manager) {
    	manager.add(newContactAction);
        manager.add(deleteContactAction);
    }

    private void fillSecretKeyContextMenu(IMenuManager manager) {
        manager.add(exportSecretKeyAction);
        manager.add(new Separator());
        manager.add(deleteSecretKeyAction);
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(chooseKeyStoreAction);
        manager.add(new Separator());
        addFlexibleActions(manager);
        manager.add(new Separator());
    }

    private void addFlexibleActions(IToolBarManager manager) {
        Iterator<IKeyStoreActionDescriptor> it = KeyStoreActionManager.getInstance().getNewSymmetricKeyActions();
        IKeyStoreActionDescriptor tmp;
        while (it.hasNext()) {
            tmp = it.next();
            if (tmp.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add a secret key action"); //$NON-NLS-1$
                manager.add(new ShadowKeyStoreAction(tmp));
                break;
            }
        }
        Iterator<IKeyStoreActionDescriptor> it2 = KeyStoreActionManager.getInstance().getNewKeyPairActions();
        IKeyStoreActionDescriptor tmp2;
        while (it2.hasNext()) {
            tmp2 = it2.next();
            if (tmp2.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add a key pair action"); //$NON-NLS-1$
                manager.add(new ShadowKeyStoreAction(tmp2));
                break;
            }
        }
        Iterator<IKeyStoreActionDescriptor> it3 = KeyStoreActionManager.getInstance().getImportActions();
        IKeyStoreActionDescriptor tmp3;
        while (it3.hasNext()) {
            tmp3 = it3.next();
            if (tmp3.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add an import action"); //$NON-NLS-1$
                manager.add(new ShadowKeyStoreAction(tmp3));
                break;
            }
        }
    }

    /**
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        KeyStoreManager.getInstance().storeKeyStore();

        super.dispose();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    private void fillLocalPullDown(IMenuManager manager) {
        Iterator<IKeyStoreActionDescriptor> secretKeyActionIterator = KeyStoreActionManager.getInstance()
                .getNewSymmetricKeyActions();
        while (secretKeyActionIterator.hasNext()) {
            manager.add(new ShadowKeyStoreAction(secretKeyActionIterator.next()));
            newSymmetricKeyActionContributed = true;
        }
        if (newSymmetricKeyActionContributed) {
            manager.add(new Separator());
        }
        Iterator<IKeyStoreActionDescriptor> keyPairActionIterator = KeyStoreActionManager.getInstance()
                .getNewKeyPairActions();
        while (keyPairActionIterator.hasNext()) {
            manager.add(new ShadowKeyStoreAction(keyPairActionIterator.next()));
        }
        manager.add(new Separator());
        Iterator<IKeyStoreActionDescriptor> importActionIterator = KeyStoreActionManager.getInstance()
                .getImportActions();
        while (importActionIterator.hasNext()) {
            manager.add(new ShadowKeyStoreAction(importActionIterator.next()));
        }
    }

    public KeyStoreAlias getSelectedKeyAlias() {
        return selectedNodeAlias;
    }

    public NodeType getSelectedNodeType() {
        return selectedNodeType;
    }

    public String getSelectedNodeInfo() {
        return selectedNodeInfo;
    }

    public KeyStoreAlias getSelectedNodeAlias() {
        return selectedNodeAlias;
    }

    public SecretKeyNode getSelectedSecretKeyNode() {
        return selectedSecretKeyNode;
    }

    public KeyPairNode getSelectedKeyPairNode() {
        return selectedKeyPairNode;
    }

    public CertificateNode getSelectedPublicKeyNode() {
        return selectedPublicKeyNode;
    }

    public void changeKeyStoreTo(String newKeyStoreInformation) {
        LogUtil.logInfo("changing keystore..."); //$NON-NLS-1$
        String name = newKeyStoreInformation.substring(0, newKeyStoreInformation.indexOf(",")); //$NON-NLS-1$
        LogUtil.logInfo("nks name: " + name); //$NON-NLS-1$
        String uri = newKeyStoreInformation.substring(
                newKeyStoreInformation.indexOf(",") + 1, newKeyStoreInformation.length()); //$NON-NLS-1$
        LogUtil.logInfo("nks uri(s): " + uri); //$NON-NLS-1$
        URI real = null;
        try {
            if (uri.endsWith(";")) {
                uri = uri.substring(0, uri.length() - 1);
            }

            real = new URI(uri);

            // close old keystore!
            KeyStoreManager.getInstance().storeKeyStore();

            // change name
            keyStoreNameLabel.setText(name);

            // open new keystore
            KeyStoreManager.getInstance().loadKeyStore(real);

            // reload the model
            viewer.reload();

            // make new keystore the current one and store the preferences
            KeyStorePlugin.setCurrentKeyStore(name);
            KeyStorePlugin.savePreferences();
        } catch (URISyntaxException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "URISyntaxException: ", e, false); //$NON-NLS-1$
        } catch (NoKeyStoreFileException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                    "NoKeyStoreFileException while opening the keystore: " + real.toString(), e, false); //$NON-NLS-1$
        }
    }

}
