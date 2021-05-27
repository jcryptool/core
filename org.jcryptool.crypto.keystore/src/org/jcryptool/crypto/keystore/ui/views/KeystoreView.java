// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
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
import org.eclipse.ui.services.IServiceLocator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreActionManager;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.KeystoreViewer;
import org.jcryptool.crypto.keystore.ui.actions.IKeyStoreActionDescriptor;
import org.jcryptool.crypto.keystore.ui.actions.KeyStoreBackupHandler;
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
    private boolean newSymmetricKeyCommandContributed = false;

    private final String exportSecretKeyCommandId = "org.jcryptool.keystore.commands.exportSecretKey"; //$NON-NLS-1$
    private final String exportKeyPairCommandId = "org.jcryptool.keystore.commands.exportKeyPair"; //$NON-NLS-1$
    private final String exportCertificateCommandId = "org.jcryptool.keystore.commands.exportCertificate"; //$NON-NLS-1$
    
    private AbstractHandler exportSecretKeyHandler = new ExportSecretKeyHandler(this);
    private AbstractHandler exportKeyPairHandler = new ExportKeyPairHandler(this);
    private AbstractHandler exportCertificateHandler = new ExportCertificateHandler(this);

    private final String newContactCommandId = "org.jcryptool.keystore.commands.newContact"; //$NON-NLS-1$
    private final String deleteContactCommandId = "org.jcryptool.keystore.commands.deleteContact"; //$NON-NLS-1$
    
    private AbstractHandler newContactHandler = new NewContactHandler(this);
    private AbstractHandler deleteContactHandler = new DeleteContactHandler(this);

    private final String deleteSecretKeyCommandId = "org.jcryptool.keystore.commands.deleteSecretKey"; //$NON-NLS-1$
    private final String deleteKeyPairCommandId = "org.jcryptool.keystore.commands.deleteKeyPair"; //$NON-NLS-1$
    private final String deleteCertificateCommandId = "org.jcryptool.keystore.comands.deleteCertificate"; //$NON-NLS-1$
    
    private AbstractHandler deleteSecretKeyHandler = new DeleteSecretKeyHandler(this);
    private AbstractHandler deleteKeyPairHandler = new DeleteKeyPairHandler(this);
    private AbstractHandler deleteCertificateHandler = new DeleteCertificateHandler(this);

    private final String commandIdBackup = "org.jcryptool.keystore.commands.backup"; //$NON-NLS-1$
    
    private NodeType selectedNodeType;
    private String selectedNodeInfo;
    private IKeyStoreAlias selectedNodeAlias;
    private SecretKeyNode selectedSecretKeyNode;
    private KeyPairNode selectedKeyPairNode;
    private CertificateNode selectedPublicKeyNode;

    private KeystoreViewer viewer;
    private Label keyStoreNameLabel;

    private ICommandService commandService;
    private Category autogeneratedCategory;
    private IServiceLocator serviceLocator;

	private Label keyStorePersistenceHint;

    public KeystoreViewer getViewer() {
    	return viewer;
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent) {
        keyStoreNameLabel = new Label(parent, SWT.NULL);
        keyStoreNameLabel.setText(KeyStoreManager.KEYSTORE_NAME);
        keyStorePersistenceHint = new Label(parent, SWT.WRAP);
        keyStorePersistenceHint.setText(Messages.getString("KeystoreView.PersistenceHint")); //$NON-NLS-1$
        GridData kSPHLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        kSPHLayoutData.widthHint = 400;
		keyStorePersistenceHint.setLayoutData(kSPHLayoutData);
        viewer = new KeystoreViewer(parent);
        getSite().setSelectionProvider(viewer);

        parent.setLayout(new GridLayout());
        defineAllCommands();
        serviceLocator = getSite();
        hookContextMenu();
        contributeToActionBars();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, KeyStorePlugin.PLUGIN_ID + ".KeystoreView"); //$NON-NLS-1$
    }

    
    private void defineCommand(final String commandId, final String name, AbstractHandler handler) {
    	Command command = commandService.getCommand(commandId);
    	command.define(name,  null, autogeneratedCategory);
    	command.setHandler(handler);
    }
    
    private void defineAllCommands() {
        commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
        autogeneratedCategory = commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID);

        defineCommand(newContactCommandId, Messages.getString("Label.NewContact"), newContactHandler); //$NON-NLS-1$
        defineCommand(deleteContactCommandId, Messages.getString("Label.DeleteContact"), deleteContactHandler); //$NON-NLS-1$

        defineCommand(deleteCertificateCommandId, Messages.getString("Label.DeleteCertificate"), deleteCertificateHandler); //$NON-NLS-1$
        defineCommand(deleteKeyPairCommandId, Messages.getString("Label.DeleteKeyPair"), deleteKeyPairHandler); //$NON-NLS-1$
        defineCommand(deleteSecretKeyCommandId, Messages.getString("Label.DeleteSecretKey"), deleteSecretKeyHandler); //$NON-NLS-1$

        defineCommand(exportCertificateCommandId,
        	org.jcryptool.crypto.keystore.ui.actions.ex.Messages.getString("Label.ExportPublicKey"),  //$NON-NLS-1$
        	exportCertificateHandler);
        defineCommand(exportKeyPairCommandId,
        	org.jcryptool.crypto.keystore.ui.actions.ex.Messages.getString("Label.ExportKeyPair"),  //$NON-NLS-1$
        	exportKeyPairHandler);
        defineCommand(exportSecretKeyCommandId,
        		org.jcryptool.crypto.keystore.ui.actions.ex.Messages.getString("Label.ExportSecretKey"),  //$NON-NLS-1$
        		exportSecretKeyHandler);
    }
    
    private void hookContextMenu() {
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
        getSite().registerContextMenu("org.jcryptool.crypto.keystore.popup", menuMgr, viewer); //$NON-NLS-1$
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void addContributionItem(IContributionManager manager, final String commandId,
        	final ImageDescriptor icon, final String tooltip)
        {
        	CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator,
        		null, commandId, SWT.PUSH);
        	if(icon != null)
        		param.icon = icon;
        	if(tooltip != null && !tooltip.equals("")) //$NON-NLS-1$
        		param.tooltip = tooltip;
        	CommandContributionItem item = new CommandContributionItem(param);
        	manager.add(item);
        }
        
    private void fillCertificateContextMenu(IMenuManager manager) {
    	addContributionItem(manager, exportCertificateCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/kgpg_export.png"), null); //$NON-NLS-1$
        manager.add(new Separator());
        addContributionItem(manager, deleteCertificateCommandId,
        		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/cancel.png"), null);	 //$NON-NLS-1$
    }

    private void fillKeyPairPublicContextMenu(IMenuManager manager) {
    	addContributionItem(manager, exportCertificateCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/kgpg_export.png"), null); //$NON-NLS-1$
    }

    private void fillKeyPairContextMenu(IMenuManager manager) {
    	addContributionItem(manager, exportKeyPairCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/kgpg_export.png"), null); //$NON-NLS-1$
        manager.add(new Separator());
        addContributionItem(manager, deleteKeyPairCommandId,
        		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/cancel.png"), null);	 //$NON-NLS-1$
    }

    private void fillAddContactMenu(IMenuManager manager) {
    	addContributionItem(manager, newContactCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/user-add.png"), null); //$NON-NLS-1$
    }

    private void fillContactContextMenu(IMenuManager manager) {
    	addContributionItem(manager, newContactCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/user-add.png"), null); //$NON-NLS-1$
    	addContributionItem(manager, deleteContactCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/user-delete-3.png"), null); //$NON-NLS-1$
    }

    private void fillSecretKeyContextMenu(IMenuManager manager) {
    	addContributionItem(manager, exportSecretKeyCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/kgpg_export.png"), null); //$NON-NLS-1$
    	manager.add(new Separator());
    	addContributionItem(manager, deleteSecretKeyCommandId,
    			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/16x16/cancel.png"), null); //$NON-NLS-1$
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
                LogUtil.logInfo("should add a secret key command"); //$NON-NLS-1$
                addContributionItem(manager, tmp.getID(),
                		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, tmp.getIcon()), tmp.getToolTipText());
                break;
            }
        }
        Iterator<IKeyStoreActionDescriptor> it2 = KeyStoreActionManager.getInstance().getNewKeyPairActions();
        IKeyStoreActionDescriptor tmp2;
        while (it2.hasNext()) {
            tmp2 = it2.next();
            if (tmp2.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add a key pair command"); //$NON-NLS-1$
                addContributionItem(manager, tmp2.getID(),
                		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, tmp2.getIcon()), tmp2.getToolTipText());
                break;
            }
        }
        Iterator<IKeyStoreActionDescriptor> it3 = KeyStoreActionManager.getInstance().getImportActions();
        IKeyStoreActionDescriptor tmp3;
        while (it3.hasNext()) {
            tmp3 = it3.next();
            if (tmp3.getExtensionUID().equals("org.jcryptool.crypto.flexiprovider.keystore")) { //$NON-NLS-1$
                LogUtil.logInfo("should add an import command"); //$NON-NLS-1$
                addContributionItem(manager, tmp3.getID(),
                		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, tmp3.getIcon()), tmp3.getToolTipText());
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
        Iterator<IKeyStoreActionDescriptor> secretKeyActionIterator = KeyStoreActionManager.getInstance()
                .getNewSymmetricKeyActions();
        while (secretKeyActionIterator.hasNext()) {
        	IKeyStoreActionDescriptor descriptor = secretKeyActionIterator.next();
        	String commandId = descriptor.getID();
        	defineCommand(commandId, descriptor.getText(), new ShadowKeyStoreHandler(descriptor));
        	addContributionItem(manager, commandId, 
        			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, descriptor.getIcon()), null);
            newSymmetricKeyCommandContributed = true;
        }
        if (newSymmetricKeyCommandContributed) {
            manager.add(new Separator());
        }
        Iterator<IKeyStoreActionDescriptor> keyPairActionIterator = KeyStoreActionManager.getInstance()
                .getNewKeyPairActions();
        while (keyPairActionIterator.hasNext()) {
        	IKeyStoreActionDescriptor descriptor = keyPairActionIterator.next();
        	String commandId = descriptor.getID();
        	defineCommand(commandId, descriptor.getText(), new ShadowKeyStoreHandler(descriptor));
        	addContributionItem(manager, commandId,
        			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, descriptor.getIcon()), null);
        }
        manager.add(new Separator());
        Iterator<IKeyStoreActionDescriptor> importActionIterator = KeyStoreActionManager.getInstance()
                .getImportActions();
        while (importActionIterator.hasNext()) {
        	IKeyStoreActionDescriptor descriptor = importActionIterator.next();
        	String commandId = descriptor.getID();
        	defineCommand(commandId, descriptor.getText(), new ShadowKeyStoreHandler(descriptor));
        	addContributionItem(manager, commandId,
        			ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, descriptor.getIcon()), null);
        }
        manager.add(new Separator());
        defineCommand(commandIdBackup, Messages.getString("Menu.BackupRestoreKeystore"), new KeyStoreBackupHandler(this)); //$NON-NLS-1$
        addContributionItem(manager, commandIdBackup, null, null);
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
