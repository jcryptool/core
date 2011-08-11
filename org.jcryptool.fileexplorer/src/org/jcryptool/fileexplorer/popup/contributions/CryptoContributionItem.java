//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.fileexplorer.popup.contributions;

import java.util.Collections;
import java.util.HashMap;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.handlers.IHandlerService;
import org.jcryptool.core.ApplicationActionBarAdvisor;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.fileexplorer.FileExplorerPlugin;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Cryptographic contribution item. Generates the <b>Encryption/Decryption</b> context menu
 * in the <b>File Explorer</b> view context menu. This menu is dynamically filled with all
 * installed cryptographic algorithms that do extend the <code>AlgorithmsManager</code>.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CryptoContributionItem extends ContributionItem {
    private Menu encMenu;
    private final FileExplorerView view;
    private IHandler handler;

    public CryptoContributionItem(FileExplorerView view, IHandler handler) {
        this.view = view;
        this.handler = handler;
    }

    public void fill(Menu menu, int index) {
        encMenu = new Menu(menu);
        MenuItem item;
        Menu typeMenu = null;
        HashMap<String, Menu> typeMap = new HashMap<String, Menu>();
        IAction[] algorithmActions = OperationsPlugin.getDefault().getAlgorithmsManager().getShadowAlgorithmActions();

        for (int i = 0; i < algorithmActions.length; i++) {
            String type = OperationsPlugin.getDefault().getAlgorithmsManager().getAlgorithmType(algorithmActions[i]);

            // create menu if necessary
            if (typeMap.get(type) == null) {
                typeMap.put(type, new Menu(encMenu));

                // create corresponding menu item to occupy the menu
                item = new MenuItem(encMenu, SWT.CASCADE);
                item.setText(ApplicationActionBarAdvisor.getTypeTranslation(type));
                item.setMenu(typeMap.get(type));
            }

            // get the menu
            typeMenu = typeMap.get(type);

            final IAction cryptoAction = algorithmActions[i];

            // create an item for the algorithm
            item = new MenuItem(typeMenu, SWT.CASCADE);
            item.setText(algorithmActions[i].getText());
            item.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    run(cryptoAction);
                }
            });

            // update the menu
            typeMap.put(type, typeMenu);
        }

        item = new MenuItem(menu, SWT.CASCADE, index);
        item.setText(Messages.CryptoContributionItem_0);
        item.setMenu(encMenu);
    }

    public void run(IAction cryptoAction) {
        final IHandlerService handlerService = (IHandlerService) view.getSite().getService(IHandlerService.class);
        IEvaluationContext evaluationContext = handlerService.createContextSnapshot(true);
        ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, evaluationContext);

        try {
            handler.execute(event);
            cryptoAction.run();
        } catch (ExecutionException ex) {
            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
        }
    }
}
