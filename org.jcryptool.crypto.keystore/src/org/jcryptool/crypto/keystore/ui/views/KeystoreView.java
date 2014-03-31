// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013, 2014 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
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
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreActionManager;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.KeystoreViewer;
import org.jcryptool.crypto.keystore.ui.actions.IKeyStoreActionDescriptor;
import org.jcryptool.crypto.keystore.ui.actions.ShadowKeyStoreHandler;
import org.jcryptool.crypto.keystore.ui.actions.contacts.DeleteContactHandler;
import org.jcryptool.crypto.keystore.ui.actions.contacts.NewContactHandler;
import org.jcryptool.crypto.keystore.ui.actions.del.DeleteCertificateHandler;
import org.jcryptool.crypto.keystore.ui.actions.del.DeleteKeyPairHandler;
import org.jcryptool.crypto.keystore.ui.actions.del.DeleteSecretKeyHandler;
import org.jcryptool.crypto.keystore.ui.actions.del.Messages;
import org.jcryptool.crypto.keystore.ui.actions.ex.ExportCertificateHandler;
import org.jcryptool.crypto.keystore.ui.actions.ex.ExportKeyPairHandler;
import org.jcryptool.crypto.keystore.ui.actions.ex.ExportSecretKeyHandler;
import org.jcryptool.crypto.keystore.ui.views.interfaces.ISelectedNodeListener;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IViewKeyInformation;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactDescriptorNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.NodeType;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.KeyPairNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.SecretKeyNode;

/**
 * The JCrypTool keystore view providing access to the contents of the platform keystore.
 * 
 * @author Tobias Kern, Dominik Schadow
 * @author Holger Friedrich (support for Commands)
 */
public class KeystoreView extends ViewPart implements ISelectedNodeListener, IViewKeyInformation {
    private boolean newSymmetricKeyActionContributed = false;

    private AbstractHandler exportSecretKeyHandler = new ExportSecretKeyHandler(this);
    private AbstractHandler exportKeyPairHandler = new ExportKeyPairHandler(this);
    private AbstractHandler exportCertificateHandler = new ExportCertificateHandler(this);

    private AbstractHandler newContactHandler = new NewContactHandler(this);
    private AbstractHandler deleteContactHandler = new DeleteContactHandler(this);

    private AbstractHandler deleteSecretKeyHandler = new DeleteSecretKeyHandler(this);
    private AbstractHandler deleteKeyPairHandler = new DeleteKeyPairHandler(this);
    private AbstractHandler deleteCertificateHandler = new DeleteCertificateHandler(this);

    private NodeType selectedNodeType;
    private String selectedNodeInfo;
    private IKeyStoreAlias selectedNodeAlias;
    private SecretKeyNode selectedSecretKeyNode;
    private KeyPairNode selectedKeyPairNode;
    private CertificateNode selectedPublicKeyNode;

    private KeystoreViewer viewer;
    private Label keyStoreNameLabel;

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent) {
        keyStoreNameLabel = new Label(parent, SWT.NULL);
        keyStoreNameLabel.setText(KeyStoreManager.KEYSTORE_NAME);
        viewer = new KeystoreViewer(parent);
        getSite().setSelectionProvider(viewer);

        parent.setLayout(new GridLayout());
        hookContextMenu();
        contributeToActionBars();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, KeyStorePlugin.PLUGIN_ID + ".KeystoreView"); //$NON-NLS-1$
    }

    private void hookContextMenu() {
        ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);

        Command cmdNewContact = commandService.getCommand("org.jcryptool.keystore.commands.new_contact");
        cmdNewContact.define(Messages.getString("Label.NewContact"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdNewContact.setHandler(newContactHandler);
        Command cmdDeleteContact = commandService.getCommand("org.jcryptool.keystore.commands.delete_contact");
        cmdDeleteContact.define(Messages.getString("Label.DeleteContact"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdDeleteContact.setHandler(deleteContactHandler);

        Command cmdDeleteCertificate = commandService.getCommand("org.jcryptool.keystore.commands.delete_certificate");
        cmdDeleteCertificate.define(Messages.getString("Label.DeleteCertificate"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdDeleteCertificate.setHandler(deleteCertificateHandler);
        Command cmdDeleteKeyPair = commandService.getCommand("org.jcryptool.keystore.commands.delete_key_pair");
        cmdDeleteKeyPair.define(Messages.getString("Label.DeleteKeyPair"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdDeleteKeyPair.setHandler(deleteKeyPairHandler);
        Command cmdDeleteSecretKey = commandService.getCommand("org.jcryptool.keystore.commands.delete_secret_key");
        cmdDeleteSecretKey.define(Messages.getString("Label.DeleteSecretKey"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdDeleteSecretKey.setHandler(deleteSecretKeyHandler);
        
        Command cmdExportCertificate = commandService.getCommand("org.jcryptool.keystore.commands.export_certificate");
        cmdExportCertificate.define(org.jcryptool.crypto.keystore.ui.actions.ex.Messages.getString("Label.ExportPublicKey"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdExportCertificate.setHandler(exportCertificateHandler);
        Command cmdExportKeyPair = commandService.getCommand("org.jcryptool.keystore.commands.export_key_pair");
        cmdExportKeyPair.define(org.jcryptool.crypto.keystore.ui.actions.ex.Messages.getString("Label.ExportKeyPair"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdExportKeyPair.setHandler(exportKeyPairHandler);
        Command cmdExportSecretKey = commandService.getCommand("org.jcryptool.keystore.commands.export_secret_key");
        cmdExportSecretKey.define(org.jcryptool.crypto.keystore.ui.actions.ex.Messages.getString("Label.ExportSecretKey"), 
        		null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        cmdExportSecretKey.setHandler(exportSecretKeyHandler);

        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                if (viewer.getTree().getSelection().length == 0) {
                    fillAddContactMenu(manager);
                    return;
                }

                Object selection = viewer.getTree().getSelection()[0].getData();
                if (selection instanceof ContactDescriptorNode) {
                    selectedNodeType = NodeType.CONTACT_NODE;
                    selectedNodeInfo = ((ContactDescriptorNode) selection).getName();
                    fillContactContextMenu(manager);
                } else if (selection instanceof SecretKeyNode) {
                    LogUtil.logInfo(((SecretKeyNode) selection).getAlias().getAliasString());
                    selectedNodeType = NodeType.SECRETKEY_NODE;
                    selectedNodeAlias = ((SecretKeyNode) selection).getAlias();
                    selectedSecretKeyNode = ((SecretKeyNode) selection);
                    fillSecretKeyContextMenu(manager);
                } else if (selection instanceof KeyPairNode) {
                    if (((KeyPairNode) selection).getPrivateKeyAlias() != null)
                        LogUtil.logInfo(((KeyPairNode) selection).getPrivateKeyAlias().getAliasString());
                    selectedNodeType = NodeType.KEYPAIR_NODE;
                    selectedNodeAlias = ((KeyPairNode) selection).getPrivateKeyAlias();
                    selectedKeyPairNode = ((KeyPairNode) selection);
                    fillKeyPairContextMenu(manager);
                } else if (selection instanceof CertificateNode) {
                    IKeyStoreAlias alias = ((CertificateNode) selection).getAlias();
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
    	CommandContributionItemParameter paramExportCertificate = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.export_certificate", SWT.PUSH);
    	paramExportCertificate.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png");
    	CommandContributionItem itemExportCertificate = new CommandContributionItem(paramExportCertificate);
    	
    	CommandContributionItemParameter paramDeleteCertificate = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.delete_certificate", SWT.PUSH);
    	paramDeleteCertificate.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/cancel.png");
    	CommandContributionItem itemDeleteCertificate = new CommandContributionItem(paramDeleteCertificate);

        manager.add(itemExportCertificate);
        manager.add(new Separator());
        manager.add(itemDeleteCertificate);
    }

    private void fillKeyPairPublicContextMenu(IMenuManager manager) {
    	CommandContributionItemParameter paramExportCertificate = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.export_certificate", SWT.PUSH);
    	paramExportCertificate.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png");
    	CommandContributionItem itemExportCertificate = new CommandContributionItem(paramExportCertificate);

    	manager.add(itemExportCertificate);
    }

    private void fillKeyPairContextMenu(IMenuManager manager) {
    	CommandContributionItemParameter paramExportKeyPair = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.export_key_pair", SWT.PUSH);
    	paramExportKeyPair.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png");
    	CommandContributionItem itemExportKeyPair = new CommandContributionItem(paramExportKeyPair);

    	CommandContributionItemParameter paramDeleteKeyPair = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.delete_key_pair", SWT.PUSH);
    	paramDeleteKeyPair.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/cancel.png");
    	CommandContributionItem itemDeleteKeyPair = new CommandContributionItem(paramDeleteKeyPair);

    	manager.add(itemExportKeyPair);
        manager.add(new Separator());
        manager.add(itemDeleteKeyPair);
    }

    private void fillAddContactMenu(IMenuManager manager) {
    	CommandContributionItemParameter paramNewContact = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.new_contact", SWT.PUSH);
    	paramNewContact.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/user-add.png");
    	CommandContributionItem itemNewContact = new CommandContributionItem(paramNewContact);

    	manager.add(itemNewContact);
    }

    private void fillContactContextMenu(IMenuManager manager) {
    	CommandContributionItemParameter paramNewContact = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.new_contact", SWT.PUSH);
    	paramNewContact.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/user-add.png");
    	CommandContributionItem itemNewContact = new CommandContributionItem(paramNewContact);
    	CommandContributionItemParameter paramDeleteContact = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.delete_contact", SWT.PUSH);
    	paramDeleteContact.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/user-delete-3.png");
    	CommandContributionItem itemDeleteContact = new CommandContributionItem(paramDeleteContact);
    	manager.add(itemNewContact);
    	manager.add(itemDeleteContact);
    }

    private void fillSecretKeyContextMenu(IMenuManager manager) {
    	CommandContributionItemParameter paramExportSecretKey = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.export_secret_key", SWT.PUSH);
    	paramExportSecretKey.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_export.png");
    	CommandContributionItem itemExportSecretKey = new CommandContributionItem(paramExportSecretKey);

    	CommandContributionItemParameter paramDeleteSecretKey = new CommandContributionItemParameter(getSite(), null, 
    			"org.jcryptool.keystore.commands.delete_secret_key", SWT.PUSH);
    	paramDeleteSecretKey.icon = KeyStorePlugin.getImageDescriptor("icons/16x16/cancel.png");
    	CommandContributionItem itemDeleteSecretKey = new CommandContributionItem(paramDeleteSecretKey);

    	manager.add(itemExportSecretKey);
        manager.add(new Separator());
        manager.add(itemDeleteSecretKey);
    }

    private void fillLocalToolBar(IToolBarManager manager) {
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
                String commandId = tmp.getID();
                CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
                		null, commandId, SWT.PUSH);
                param.icon = KeyStorePlugin.getImageDescriptor(tmp.getIcon());
                param.tooltip = tmp.getToolTipText();
                CommandContributionItem item = new CommandContributionItem(param);
                manager.add(item);
                break;
            }
        }
        Iterator<IKeyStoreActionDescriptor> it2 = KeyStoreActionManager.getInstance().getNewKeyPairActions();
        IKeyStoreActionDescriptor tmp2;
        while (it2.hasNext()) {
            tmp2 = it2.next();
            if (tmp2.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add a key pair action"); //$NON-NLS-1$
                String commandId = tmp2.getID();
                CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
                		null, commandId, SWT.PUSH);
                param.icon = KeyStorePlugin.getImageDescriptor(tmp2.getIcon());
                param.tooltip = tmp2.getToolTipText();
                CommandContributionItem item = new CommandContributionItem(param);
                manager.add(item);
                break;
            }
        }
        Iterator<IKeyStoreActionDescriptor> it3 = KeyStoreActionManager.getInstance().getImportActions();
        IKeyStoreActionDescriptor tmp3;
        while (it3.hasNext()) {
            tmp3 = it3.next();
            if (tmp3.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add an import action"); //$NON-NLS-1$
                String commandId = tmp3.getID();
                CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
                		null, commandId, SWT.PUSH);
                param.icon = KeyStorePlugin.getImageDescriptor(tmp3.getIcon());
                param.tooltip = tmp3.getToolTipText();
                CommandContributionItem item = new CommandContributionItem(param);
                manager.add(item);
                break;
            }
        }
    }

    /**
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
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
        ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);

        Iterator<IKeyStoreActionDescriptor> secretKeyActionIterator = KeyStoreActionManager.getInstance()
                .getNewSymmetricKeyActions();
        while (secretKeyActionIterator.hasNext()) {
        	IKeyStoreActionDescriptor descriptor = secretKeyActionIterator.next();
        	String commandId = descriptor.getID();
        	Command command = commandService.getCommand(commandId);
        	command.define(descriptor.getText(), null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        	command.setHandler(new ShadowKeyStoreHandler(descriptor));
        	
        	CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
        			null, commandId, SWT.PUSH);
        	param.icon = KeyStorePlugin.getImageDescriptor(descriptor.getIcon());
        	CommandContributionItem item = new CommandContributionItem(param);
            manager.add(item);
            newSymmetricKeyActionContributed = true;
        }
        if (newSymmetricKeyActionContributed) {
            manager.add(new Separator());
        }
        Iterator<IKeyStoreActionDescriptor> keyPairActionIterator = KeyStoreActionManager.getInstance()
                .getNewKeyPairActions();
        while (keyPairActionIterator.hasNext()) {
        	IKeyStoreActionDescriptor descriptor = keyPairActionIterator.next();
        	String commandId = descriptor.getID();
        	Command command = commandService.getCommand(commandId);
        	command.define(descriptor.getText(), null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        	command.setHandler(new ShadowKeyStoreHandler(descriptor));
        	
        	CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
        			null, commandId, SWT.PUSH);
        	param.icon = KeyStorePlugin.getImageDescriptor(descriptor.getIcon());
        	CommandContributionItem item = new CommandContributionItem(param);
            manager.add(item);
        }
        manager.add(new Separator());
        Iterator<IKeyStoreActionDescriptor> importActionIterator = KeyStoreActionManager.getInstance()
                .getImportActions();
        while (importActionIterator.hasNext()) {
        	IKeyStoreActionDescriptor descriptor = importActionIterator.next();
        	String commandId = descriptor.getID();
        	Command command = commandService.getCommand(commandId);
        	command.define(descriptor.getText(), null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
        	command.setHandler(new ShadowKeyStoreHandler(descriptor));
        	
        	CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
        			null, commandId, SWT.PUSH);
        	param.icon = KeyStorePlugin.getImageDescriptor(descriptor.getIcon());
        	CommandContributionItem item = new CommandContributionItem(param);
            manager.add(item);
        }
    }

    public IKeyStoreAlias getSelectedKeyAlias() {
        return selectedNodeAlias;
    }

    public NodeType getSelectedNodeType() {
        return selectedNodeType;
    }

    public String getSelectedNodeInfo() {
        return selectedNodeInfo;
    }

    public IKeyStoreAlias getSelectedNodeAlias() {
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
}
