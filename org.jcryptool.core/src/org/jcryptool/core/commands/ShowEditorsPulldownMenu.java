// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.commands;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.osgi.framework.Bundle;

/**
 * a class to show all registered editors in a drop down list at the coolbar the action should open a new editors page.
 *
 * @author mwalthart
 * @version 0.5.0
 */
public class ShowEditorsPulldownMenu extends AbstractHandler {

    private Menu showEditorsPulldownMenu;

    /**
     * Creates a new menu as a singleton object.
     */
    public Menu getMenu(Control parent) {
        if (showEditorsPulldownMenu == null) {
            showEditorsPulldownMenu = createEditorsMenu(parent, showEditorsPulldownMenu);
        }

        MenuItem[] items = showEditorsPulldownMenu.getItems();
        for (MenuItem item : items) {
            item.setEnabled(true);
        }
        return showEditorsPulldownMenu;
    }

    /**
     * Creates the menu.
     *
     * @param parent
     * @param menu
     * @return
     */
    private static Menu createEditorsMenu(Control parent, Menu menu) {
        if (menu == null) {
            menu = new Menu(parent);

            IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(
                    "org.jcryptool.core.operations.editorServices"); //$NON-NLS-1$

            Comparator<IConfigurationElement> comp = new Comparator<IConfigurationElement>() {
                public int compare(IConfigurationElement o1, IConfigurationElement o2) {
                    String l1 = o1.getAttribute("label"); //$NON-NLS-1$
                    String l2 = o2.getAttribute("label"); //$NON-NLS-1$
                    String c1 = o1.getAttribute("category"); //$NON-NLS-1$
                    String c2 = o2.getAttribute("category"); //$NON-NLS-1$

                    int cat = c1.compareTo(c2);
                    int label = l1.compareTo(l2);
                    if (cat != 0) {
                        return cat;
                    } else {
                        if (o1.getAttribute("id").contains("org.jcryptool.editor.text") //$NON-NLS-1$ //$NON-NLS-2$
                                && !o2.getAttribute("id").contains("org.jcryptool.editor.text")) {
                            return -1;
                        }
                        if (!o1.getAttribute("id").contains("org.jcryptool.editor.text") //$NON-NLS-1$ //$NON-NLS-2$
                                && o2.getAttribute("id").contains("org.jcryptool.editor.text")) {
                            return 1;
                        }

                        if (label != 0) {
                            return label;
                        } else {
                            return o2.hashCode() - o1.hashCode();
                        }
                    }
                }
            };

            Set<IConfigurationElement> entries = new TreeSet<IConfigurationElement>(comp);

            for (IExtension extension : point.getExtensions()) {
                for (IConfigurationElement element : extension.getConfigurationElements()) {
                    entries.add(element);
                }
            }

            String currentCat = entries.size() > 0 ? entries.iterator().next().getAttribute("category") : null; //$NON-NLS-1$
            for (IConfigurationElement element : entries) {
                if (!currentCat.equals(element.getAttribute("category"))) { //$NON-NLS-1$
                    new MenuItem(menu, SWT.SEPARATOR);
                    currentCat = element.getAttribute("category"); //$NON-NLS-1$
                }

                final MenuItem menuItem = new MenuItem(menu, SWT.PUSH);

                // Set the Labels to the entries
                menuItem.setText(element.getAttribute("label")); //$NON-NLS-1$

                // set the actions to the entries
                try {
                    Object o = element.createExecutableExtension("pushAction"); //$NON-NLS-1$
                    menuItem.setData(o);
                } catch (CoreException e) {
                    LogUtil.logError(CorePlugin.PLUGIN_ID, e);
                }

                String iconPath = element.getAttribute("icon"); // subpath of the icon within the plugin //$NON-NLS-1$
                if ((iconPath != null) && (!iconPath.equals(""))) { //$NON-NLS-1$
                    try {
                        Object o = element.getAttribute("id"); // id of the plugin for path resolution //$NON-NLS-1$
                        if (o != null) {
                            Bundle bundle = Platform.getBundle(o.toString());
                            URL fileUrl = FileLocator.find(bundle, new Path("/"), null); //$NON-NLS-1$
                            fileUrl = FileLocator.toFileURL(fileUrl);
                            iconPath = fileUrl.getFile() + iconPath;

                            menuItem.setImage(new Image(null, iconPath));
                        }
                    } catch (IOException ex) {
                        LogUtil.logError(CorePlugin.PLUGIN_ID, ex);
                    }
                }

                // Handle selection
                menuItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        // execute the actions
                        Object o = ((MenuItem) e.getSource()).getData();
                        IActionDelegate iad = (IActionDelegate) o;
                        iad.run(null);
                    }
                });
            }

        }

        return menu;
    }

    /**
     * Removes the menu.
     */
    public void dispose() {
        if (showEditorsPulldownMenu != null) {
            showEditorsPulldownMenu.dispose();
        }
    }

    /**
     * Unused.
     */
    public void init(IWorkbenchWindow window) {
    }

    /**
     * Rhe implementation of the on click action.
     */
    public Object execute(ExecutionEvent event) {
        // connects to the extension point
        IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint("org.jcryptool.core.editorButton"); //$NON-NLS-1$
        IExtension extension = point.getExtensions()[0];
        IConfigurationElement element = extension.getConfigurationElements()[0];
        try {
            // runs the defined action
            IActionDelegate iad = (IActionDelegate) element.createExecutableExtension("OnClickClass"); //$NON-NLS-1$
            iad.run(null);
        } catch (CoreException ex) {
            LogUtil.logError(CorePlugin.PLUGIN_ID, ex);
        }
        return null;
    }

    /**
     * Unused.
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }
}