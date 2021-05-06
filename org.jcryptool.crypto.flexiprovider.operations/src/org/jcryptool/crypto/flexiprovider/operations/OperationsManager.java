// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.ProviderManager2;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.EntryNode;
import org.jcryptool.crypto.flexiprovider.operations.xml.ExportRootElement;
import org.jcryptool.crypto.flexiprovider.operations.xml.OperationsViewEntryRootElement;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OperationsManager {
    private static final String OPERATIONS_FILE_NAME = "OperationsViewEntries.xml"; //$NON-NLS-1$
    private static final String FLEXIPROVIDER_FOLDER = "flexiprovider"; //$NON-NLS-1$

    private static OperationsManager instance;
    private static File operationsFile = null;

    private static Document document;
    private static OperationsViewEntryRootElement root;
    private static boolean loaded = false;

    private static Map<Long, IFlexiProviderOperation> newOperations = Collections
            .synchronizedMap(new HashMap<Long, IFlexiProviderOperation>());
    private static List<IOperationChangedListener> listeners = Collections
            .synchronizedList(new ArrayList<IOperationChangedListener>());

    private OperationsManager() {
    }

    private static void loadOperations() {
        File flexiprovider = new File(DirectoryService.getWorkspaceDir(), FLEXIPROVIDER_FOLDER);
        if (!flexiprovider.exists()) {
            flexiprovider.mkdir();
        }
        operationsFile = new File(flexiprovider, OPERATIONS_FILE_NAME);

        if (operationsFile != null && operationsFile.exists() && !loaded) {
            // ensure FlexiProvider is initialized
            ProviderManager2.getInstance();
            // load xml structure
            document = loadXML();
            if (document != null) {
                root = new OperationsViewEntryRootElement(document.getRootElement());
                Iterator<EntryNode> it = root.getEntryNodes().iterator();
                while (it.hasNext()) {
                    IFlexiProviderOperation current = it.next();
                    newOperations.put(current.getTimestamp(), current);
                }
                loaded = true;
            }
        }
    }

    private static Document loadXML() {
        SAXBuilder sax = new SAXBuilder();
        try {
            return sax.build(operationsFile);
        } catch (JDOMException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while reading the operations.xml file", e, true);
        } catch (IOException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while reading the operations.xml file", e, true);
        }
        return null;
    }

    public void saveXML() {
        FileOutputStream fos;
        try {
            root = new OperationsViewEntryRootElement();
            Iterator<IFlexiProviderOperation> it = newOperations.values().iterator();
            while (it.hasNext()) {
                root.addEntry(it.next());
            }
            document = new Document(root);
            fos = new FileOutputStream(operationsFile);
            new XMLOutputter(Format.getPrettyFormat()).output(document, fos);
        } catch (FileNotFoundException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "FileNotFoundException while attempting to write the operations.xml file", e, true);
        } catch (IOException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while attempting to write the operations.xml file", e, true);
        }
    }

    public synchronized static OperationsManager getInstance() {
        if (instance == null)
            instance = new OperationsManager();
        return instance;
    }

    public void importOperation(String fileName) {
        try {
            LogUtil.logInfo("1"); //$NON-NLS-1$
            IFileStore store = EFS.getStore(URIUtil.toURI(fileName));
            LogUtil.logInfo("2"); //$NON-NLS-1$
            InputStream is = store.openInputStream(SWT.NONE, null);
            LogUtil.logInfo("3"); //$NON-NLS-1$
            SAXBuilder sax = new SAXBuilder();
            LogUtil.logInfo("4"); //$NON-NLS-1$
            Document doc = sax.build(is);
            LogUtil.logInfo("5"); //$NON-NLS-1$
            is.close();
            LogUtil.logInfo("6"); //$NON-NLS-1$
            ExportRootElement root = new ExportRootElement(doc.getRootElement());
            LogUtil.logInfo("7"); //$NON-NLS-1$
            IFlexiProviderOperation importEntry = root.getEntryNode();
            LogUtil.logInfo("8"); //$NON-NLS-1$
            newOperations.put(importEntry.getTimestamp(), importEntry);
            LogUtil.logInfo("9"); //$NON-NLS-1$
            Iterator<IOperationChangedListener> it = listeners.iterator();
            while (it.hasNext()) {
                it.next().addOperation();
            }
            LogUtil.logInfo("10"); //$NON-NLS-1$
        } catch (CoreException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "CoreException while importing an operation", e, true);
        } catch (JDOMException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "JDOMException while importing an operation", e, true);
        } catch (IOException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while importing an operation", e, false);
        }
    }

    public void export(long timestamp, String fileName) {
        try {
            IFileStore store = EFS.getStore(URIUtil.toURI(fileName));
            OutputStream os = store.openOutputStream(SWT.NONE, null);
            Document exportee = new Document(new ExportRootElement(newOperations.get(timestamp)));
            new XMLOutputter(Format.getPrettyFormat()).output(exportee, os);
            os.close();
        } catch (CoreException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "CoreException while exporting an operation", e, false);
        } catch (IOException e) {
            LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID, "IOException while exporting an operation", e, false);
        }
    }

    public ITreeNode getTreeModel() {
        loadOperations();
        ITreeNode _invisibleRoot = new TreeNode("INVISIBLE_ROOT"); //$NON-NLS-1$
        for (IFlexiProviderOperation node : newOperations.values()) {
            if (node instanceof EntryNode) {
                _invisibleRoot.addChild((EntryNode) node);
            }
        }
        return _invisibleRoot;
    }

    public void addOperation(AlgorithmDescriptor operation) {
        LogUtil.logInfo("adding algorithm descriptor: " + operation.getClass().getName()); //$NON-NLS-1$
        long timestamp = System.currentTimeMillis();
        newOperations.put(timestamp, new EntryNode("no name", timestamp, operation)); //$NON-NLS-1$
        Iterator<IOperationChangedListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().addOperation(operation);
        }
        LogUtil.logInfo("operation 2.0 added"); //$NON-NLS-1$
    }

    public void removeOperation(long timestamp) {
        newOperations.remove(timestamp);
        Iterator<IOperationChangedListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().removeOperation();
        }
    }

    public Iterator<IOperationChangedListener> getOperationChangedListeners() {
        return listeners.iterator();
    }

    public void addOperationChangedListener(IOperationChangedListener listener) {
        listeners.add(listener);
    }

    public void removeOperationChangedListener(IOperationChangedListener listener) {
        listeners.remove(listener);
    }

}
