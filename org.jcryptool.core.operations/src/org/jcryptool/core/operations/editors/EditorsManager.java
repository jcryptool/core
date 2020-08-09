// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.editors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * The EditorsManager class is a singleton. It provides a global point of access to the registered editor services. The
 * class has only instance. The registered editor services are collected in a hash map. The keys of the hash map are the
 * IDs of the editors and the appropriate values are the editor services.
 * 
 * @see org.jcryptool.core.operations.editors.AbstractEditorService
 * @author amro
 * @version 0.9.3
 */
public class EditorsManager {
    /** The singleton object. */
    private static EditorsManager instance = null;
    /**
     * Editor services, editorID presents the key and editorService the appropriate value.
     */
    private HashMap<String, AbstractEditorService> editorServices = null;

    private Set<IEditorReference> lastEditorRefs;
    private IEditorReference lastActiveEditorRef;

    private static class ChangeableObservable extends Observable {
        @Override
		public void setChanged() {
            super.setChanged();
        }
    }

    private ChangeableObservable editorAvailabilityObservervable;
    private ChangeableObservable activeEditorObservervable;

    /**
     * The constructor exists only to defeat instantiation.
     */
    private EditorsManager() {
        lastEditorRefs = new HashSet<IEditorReference>();
        lastActiveEditorRef = null;
        editorAvailabilityObservervable = new ChangeableObservable();
        activeEditorObservervable = new ChangeableObservable();

        addListenerToWindow();
    }

    /**
     * Getter for the only EditorManager object
     * 
     * @return the EditorManager object
     */
    public synchronized static EditorsManager getInstance() {
        if (instance == null) {
            instance = new EditorsManager();
        }

        return instance;
    }

    protected void checkNeedToNotify() {
        List<IEditorReference> nowEditors = getEditorReferences();
        IEditorReference nowActiveEditorRef = getActiveEditorReference();

        // check active editor changes
        if (lastActiveEditorRef != null) {
            if (!lastActiveEditorRef.equals(nowActiveEditorRef)) {
                lastActiveEditorRef = nowActiveEditorRef;
                notifyActiveEditorObservers();
            }
        } else if (lastActiveEditorRef != nowActiveEditorRef) {
            lastActiveEditorRef = nowActiveEditorRef;
            notifyActiveEditorObservers();
        }

        // check overall editor changes
        if (lastEditorRefs.size() != nowEditors.size()) {
            lastEditorRefs.clear();
            lastEditorRefs.addAll(nowEditors);
            notifyEditorAvailabilityObservers();
        } else {
            for (IEditorReference ref : nowEditors) {
                if (!lastEditorRefs.contains(ref)) {
                    lastEditorRefs.clear();
                    lastEditorRefs.addAll(nowEditors);
                    notifyEditorAvailabilityObservers();
                    break;
                }
            }
        }

    }

    private void notifyActiveEditorObservers() {
        activeEditorObservervable.setChanged();
        activeEditorObservervable.notifyObservers();
    }

    private void notifyEditorAvailabilityObservers() {
        editorAvailabilityObservervable.setChanged();
        editorAvailabilityObservervable.notifyObservers();
    }

    private IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
    }

    public void openNewHexEditor(IEditorInput input) throws PartInitException {
        getActivePage().openEditor(input, IOperationsConstants.ID_HEX_EDITOR);
    }

    public void openNewTextEditor(IEditorInput input) throws PartInitException {
        getActivePage().openEditor(input, IOperationsConstants.ID_TEXT_EDITOR);
    }

    /**
     * @return whether an text editor is currently opened
     */
    public boolean isEditorOpen() {
        return getEditorReferences().size() > 0;
    }

    /**
     * Returns an input stream of the active editor content, or <code>null</code> if no editor is active.
     * 
     * @return An input stream of the active editor content, or <code>null</code> if no editor is active
     */
    public InputStream getActiveEditorContentInputStream() {
        IEditorPart editorPart = getActivePage().getActiveEditor();
        if (editorPart != null) {
            return getContentInputStream(editorPart);
        } else {
            return null;
        }
    }

    /**
     * @return a list of editor references, which may be empty, but not null.
     */
    public List<IEditorReference> getEditorReferences() {
        if (getActivePage() == null)
            return new LinkedList<IEditorReference>();
        if (getActivePage() != null) {
            IEditorReference[] refs = getActivePage().getEditorReferences();
            if (refs == null)
                return new LinkedList<IEditorReference>();
            return Arrays.asList(refs);
        } else {
            return null;
        }
    }

    /**
     * @return the reference for the active editor, or null, if there is none.
     */
    public IEditorReference getActiveEditorReference() {
        if (getActivePage() == null)
            return null;
        IEditorPart editorPart = getActivePage().getActiveEditor();

        for (final IEditorReference r : getEditorReferences()) {
            IEditorPart refEditor = r.getEditor(false);
            if (refEditor != null && refEditor.equals(editorPart))
                return r;
        }

        return null;
    }

    /**
     * @return the title of the active Editor, or null, if there is none.
     */
    public String getActiveEditorTitle() {
        IEditorReference ref = getActiveEditorReference();
        return ref == null ? null : ref.getPart(false).getTitle();
    }

    public InputStream getContentInputStream(IEditorPart editorPart) {
        retrieveEditorServices();
        return editorServices.get(editorPart.getEditorSite().getId()).getContentOfEditorAsInputStream(editorPart);
    }

    /**
     * This method performs the retrieval of the (byte[]) content of the parameterized editor.
     * 
     * @deprecated Do not use with regard to modern cryptography. Use streams instead.
     * @param editorPart the editor the content is to be retrieved
     * @return the content as a byte[]
     */
    @Deprecated
	public byte[] getContentAsBytes(IEditorPart editorPart) {
        if (editorServices == null) {
            editorServices = new HashMap<String, AbstractEditorService>();
            retrieveEditorServices();
        }

        return editorServices.get(editorPart.getEditorSite().getId()).getContentOfEditorAsBytes(editorPart);
    }

    /**
     * This method performs the retrieval of the (String) content of the parameterized editor.
     * 
     * @deprecated Do not use with regard to modern cryptography. Use streams instead.
     * @param editorPart the editor the content is to be retrieved
     * @return the content as a String
     */
    @Deprecated
	public String getContentAsString(IEditorPart editorPart) {
        if (editorServices == null) {
            editorServices = new HashMap<String, AbstractEditorService>();
            retrieveEditorServices();
        }

        return editorServices.get(editorPart.getEditorSite().getId()).getContentOfEditorAsString(editorPart);
    }

    /**
     * This method performs the retrieval of the editor services,
     * 
     * which are registered in extension registry.
     */
    private void retrieveEditorServices() {
        if (editorServices == null) {
            editorServices = new HashMap<String, AbstractEditorService>();
            retrieveEditorServices();
        }
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_EDITOR_SERVICES);
        IExtension[] extensions = extensionPoint.getExtensions();

        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] configElements = extensions[i].getConfigurationElements();

            for (int j = 0; j < configElements.length; j++) {
                IConfigurationElement element = configElements[j];

                if (element.getName().equals(IOperationsConstants.TAG_SERVICE)) {
                    try {
                        AbstractEditorService service = (AbstractEditorService) element
                                .createExecutableExtension(IOperationsConstants.ATT_CLASS);
                        editorServices.put(service.getEditorID(), service);
                    } catch (CoreException e) {
                        LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                                "Exception while loading the EditorServices", e, false); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    /**
     * Compiles list with titles of all open editors and returns it.
     * 
     * @return a list of editor titles.
     * @throws EditorNotFoundException if no active workbench page could be found.
     */
    public String[] listEditorTitle() throws EditorNotFoundException {
        IWorkbenchPage[] pages = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages();

        if (0 == pages.length) {
            throw new EditorNotFoundException("Could not find active workbench page."); //$NON-NLS-1$
        }

        IEditorReference[] refs = pages[0].getEditorReferences();
        ArrayList<String> list = new ArrayList<String>();

        for (IEditorReference ref : refs) {
            list.add(ref.getTitle());
        }

        return list.toArray(new String[0]);
    }

    /**
     * Looks up editor with this title and reads out its content.
     * 
     * @param title the title of the editor to look up.
     * @return the streamed content of the editor; or <code>null</code> if no editor with this title is available.
     * @throws EditorNotFoundException if no active workbench page could be found.
     */
    public InputStream getEditorContent(final String title) throws EditorNotFoundException {
        IWorkbenchPage[] pages = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages();

        if (0 == pages.length) {
            throw new EditorNotFoundException("Could not find active workbench page."); //$NON-NLS-1$
        }

        IEditorReference[] refs = pages[0].getEditorReferences();
        IEditorPart ip = null;

        for (IEditorReference er : refs) {
            if (er.getTitle().equals(title)) {
                ip = er.getEditor(true);
                break;
            }
        }

        if (null != ip) {
            return getContentInputStream(ip);
        }

        return null;
    }

    /**
     * @return an observable object which notifies the Observer every time the "active editor" predicate has been given
     *         from one editor to another.
     */
    public Observable getActiveEditorChangedObservable() {
        return activeEditorObservervable;
    }

    /**
     * @return an observable object which notifies the Observer every time there are changes in the availability of
     *         editors; precise: every time the set of accessible editors changes.
     */
    public Observable getEditorAvailabilityObservable() {
        return editorAvailabilityObservervable;
    }

    private void addPartListener(IWorkbenchPage page) {
        page.addPartListener(new IPartListener2() {
            @Override
			public void partVisible(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partOpened(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partHidden(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partClosed(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partActivated(IWorkbenchPartReference partRef) {
                checkNeedToNotify();
            }

            @Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
            }
        });
    }

    private void addListenerToPage(IWorkbenchWindow window) {
        if (window.getPages().length > 0) {
            IWorkbenchPage page = window.getActivePage();
            if (page == null)
                page = window.getPages()[0];
            addPartListener(page);
            return;
        }
        window.addPageListener(new IPageListener() {
            @Override
			public void pageOpened(IWorkbenchPage page) {
                addPartListener(page);
            }

            @Override
			public void pageActivated(IWorkbenchPage page) {
            }

            @Override
			public void pageClosed(IWorkbenchPage page) {
            }
        });
    }

    private void addListenerToWindow() {
        // TODO: As it is now, it works for the JCrypTool. There is of course
        // the possibility of more
        // workbench windows and on them, multiple pages, and this
        // implementation should be adapted to it.
        // Idea: in the getter of the observable object, register the listener,
        // not here in this central constructor-called procedure.
        if (PlatformUI.getWorkbench().getWorkbenchWindowCount() > 0) {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (window == null)
                window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
            addListenerToPage(window);
            return;
        }
        PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
            @Override
			public void windowOpened(IWorkbenchWindow window) {
                addListenerToPage(window);
            }

            @Override
			public void windowDeactivated(IWorkbenchWindow window) {
            }

            @Override
			public void windowClosed(IWorkbenchWindow window) {
            }

            @Override
			public void windowActivated(IWorkbenchWindow window) {
            }
        });
    }
    
    public AbstractEditorService getServiceFor(IEditorPart part) {
    	retrieveEditorServices();
        return editorServices.get(part.getEditorSite().getId());
    }
}
