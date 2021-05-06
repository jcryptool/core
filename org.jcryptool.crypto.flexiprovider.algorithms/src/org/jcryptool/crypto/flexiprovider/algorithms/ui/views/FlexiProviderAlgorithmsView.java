// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.algorithms.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.AlgorithmsManager;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes.AlgorithmNode;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes.CategoryNode;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.nodes.FolderNode;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.providers.FlexiProviderAlgorithmsViewContentProvider;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.providers.FlexiProviderAlgorithmsViewLabelProvider;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class FlexiProviderAlgorithmsView extends ViewPart {
    public static final String ID = "org.jcryptool.crypto.flexiprovider.algorithms.ui.views.FlexiProviderAlgorithmsView"; //$NON-NLS-1$

    private AbstractHandler doubleClickHandler;
    private TreeViewer viewer;

    @Override
	public void createPartControl(Composite parent) {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.setContentProvider(new FlexiProviderAlgorithmsViewContentProvider(this));
        viewer.setLabelProvider(new FlexiProviderAlgorithmsViewLabelProvider());
        viewer.setInput(getViewSite());
        viewer.getTree().setLayoutData(gridData);
        parent.setLayout(new GridLayout());
        hookActions("/xml/help_algorithms.xml");
        contributeToActionBars();

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent, FlexiProviderAlgorithmsPlugin.PLUGIN_ID + ".AlgorithmView"); //$NON-NLS-1$
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
    }

    @Override
	public void setFocus() {
        viewer.getControl().setFocus();
    }

    /**
     * Adds an listener, which will provide the context help for a selected algorithm. If for a child is no context help
     * available, it will use the parents context help
     */
    @SuppressWarnings("unchecked")
    private void hookActions(String xmlfile) {
        doubleClickHandler = new AbstractHandler() {
            @Override
			public Object execute(ExecutionEvent event) {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();

                if (obj instanceof CategoryNode || obj instanceof FolderNode) {
                    if (viewer.getTree().getSelection()[0].getExpanded()) {
                        viewer.collapseToLevel(obj, 1);
                    } else {
                        viewer.expandToLevel(obj, 1);
                    }
                } else if (obj instanceof AlgorithmNode) {
                    AlgorithmsManager.getInstance().algorithmCalled(((AlgorithmNode) obj).getAlgorithm());
                }
                return(null);
            }
        };

        URL xmlFile = FlexiProviderAlgorithmsPlugin.getDefault().getBundle().getEntry(xmlfile);
        SAXBuilder builder = new SAXBuilder();

        // Build a lookup table for the names defined in xml/help_algorithmis.xml
        // to the according context id (in $nl$/contexts_algorithms.xml)
        final Hashtable<String, String> contextIdLookup = new Hashtable<String, String>();
        try {
            Document doc = builder.build(xmlFile);
            Element root = doc.getRootElement();

            List<Element> helpEntries = root.getChildren("helpentry");
            for (Element helpEntry : helpEntries) {
                String mainname = helpEntry.getAttributeValue("mainname");
                String contextid = helpEntry.getAttributeValue("contextid");
                contextIdLookup.put(mainname, contextid);

                Element aliasesRoot = helpEntry.getChild("aliases");
                if (aliasesRoot != null) {
                    List<Element> aliases = aliasesRoot.getChildren("alias");
                    for (Element alias : aliases) {
                        contextIdLookup.put(alias.getValue(), contextid);
                    }
                }
            }

            viewer.getControl().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseDoubleClick(final MouseEvent e) {
                    if (e.button == 1) { // only left button double clicks
                    	try {
                    		doubleClickHandler.execute(null); // run assigned action
                    	} catch(ExecutionException ex) {
                    		LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, ex);
                    	}
                    }
                }

                @Override
                public void mouseDown(final MouseEvent event) {
                    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                    Object obj = selection.getFirstElement();

                    if (obj instanceof ITreeNode) {
                        ITreeNode node = (ITreeNode) obj;

                        // Display the best matching context help for a selected algorithm
                        // display help of parent entry if no context help is available
                        do {
                            String name = node.toString();
                            if (contextIdLookup.containsKey(name)) {
                                String contextId = FlexiProviderAlgorithmsPlugin.PLUGIN_ID + "."
                                        + contextIdLookup.get(name);
                                PlatformUI.getWorkbench().getHelpSystem().displayHelp(contextId);
                                break;
                            }
                            node = node.getParent();
                        } while (node != null);

                        viewer.getControl().setFocus();
                        viewer.setSelection(selection);
                    }
                }
            });
        } catch (JDOMException e) {
            LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, e);
        }
    }
}
