//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.fileexplorer.popup.contributions;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

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
 * @version 0.9.5
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
        Comparator<String> menuStringsComparator = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        };

        SortedMap<String, Menu> typeMap = new TreeMap<String, Menu>(menuStringsComparator);
        SortedMap<String, IAction> actionMap = new TreeMap<String, IAction>(menuStringsComparator);
        IAction[] algorithmActions = OperationsPlugin.getDefault().getAlgorithmsManager().getShadowAlgorithmActions();

        for (final IAction action : algorithmActions) {
            String entry = ApplicationActionBarAdvisor.getTypeTranslation(OperationsPlugin.getDefault().getAlgorithmsManager().getAlgorithmType(action));

            if (!typeMap.containsKey(entry)) {
                typeMap.put(entry, new Menu(encMenu));
            }

            actionMap.put(action.getText(), action);
        }

        for (String subMenuKey : typeMap.keySet()) {
            item = new MenuItem(encMenu, SWT.CASCADE);
            item.setText(subMenuKey);
            item.setMenu(typeMap.get(subMenuKey));
        }

        for (String algorithmItems : actionMap.keySet()) {
            final IAction action = actionMap.get(algorithmItems);
            String entry = ApplicationActionBarAdvisor.getTypeTranslation(OperationsPlugin.getDefault().getAlgorithmsManager().getAlgorithmType(action));

            // get the menu
            Menu typeMenu = typeMap.get(entry);

            // create an item for the algorithm
            item = new MenuItem(typeMenu, SWT.CASCADE);
            item.setText(action.getText());
            item.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    run(action);
                }
            });

            // update the menu
            typeMap.put(entry, typeMenu);
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
