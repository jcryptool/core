/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * The official javahexeditor site is sourceforge.net/projects/javahexeditor
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor.plugin.editors;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.javahexeditor.BinaryContent;
import net.sourceforge.javahexeditor.HexTexts;
import net.sourceforge.javahexeditor.Manager;
import net.sourceforge.javahexeditor.plugin.BinaryEditorPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.WorkbenchPart;

@SuppressWarnings("unchecked")
public class BinaryEditor extends EditorPart implements ISelectionProvider {

    public static final String ID = "net.sourceforge.javahexeditor.editors.BinaryEditor"; //$NON-NLS-1$

    class MyAction extends Action {
        String myId = null;

        MyAction(String id) {
            myId = id;
        }

        public void run() {
            if (myId.equals(ActionFactory.UNDO.getId()))
                getManager().doUndo();
            else if (myId.equals(ActionFactory.REDO.getId()))
                getManager().doRedo();
            else if (myId.equals(ActionFactory.CUT.getId()))
                getManager().doCut();
            else if (myId.equals(ActionFactory.COPY.getId()))
                getManager().doCopy();
            else if (myId.equals(ActionFactory.PASTE.getId()))
                getManager().doPaste();
            else if (myId.equals(ActionFactory.DELETE.getId()))
                getManager().doDelete();
            else if (myId.equals(ActionFactory.SELECT_ALL.getId()))
                getManager().doSelectAll();
            else if (myId.equals(ActionFactory.FIND.getId()))
                getManager().doFind();
        }
    }

    static final String textSavingFilePleaseWait = "Saving file, please wait";

    private Manager manager = null;
    IPropertyChangeListener preferencesChangeListener = null;
    @SuppressWarnings("rawtypes")
	Set selectionListeners = null; // of ISelectionChangedListener

    public BinaryEditor() {
        super();
    }

    @SuppressWarnings("rawtypes")
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null) {
            selectionListeners = new HashSet();
        }
        selectionListeners.add(listener);
    }

    public void createPartControl(Composite parent) {
        getManager().setTextFont(BinaryEditorPlugin.getFontData());
        getManager().setFindReplaceLists(BinaryEditorPlugin.getFindReplaceFindList(),
                BinaryEditorPlugin.getFindReplaceReplaceList());
        manager.createEditorPart(parent);
        FillLayout fillLayout = new FillLayout();
        parent.setLayout(fillLayout);

        IEditorInput unresolved = getEditorInput();
        String charset = null;
        File systemFile = null;
        if (unresolved instanceof IPathEditorInput) {
            IPathEditorInput file = (IPathEditorInput) unresolved;
            systemFile = file.getPath().toFile();
        }
        // open file
        try {
            manager.openFile(systemFile, charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setPartName(systemFile.getName());

        // Register any global actions with the site's IActionBars.
        IActionBars bars = getEditorSite().getActionBars();
        String id = ActionFactory.UNDO.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.REDO.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.CUT.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.COPY.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.PASTE.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.DELETE.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.SELECT_ALL.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));
        id = ActionFactory.FIND.getId();
        bars.setGlobalActionHandler(id, new MyAction(id));

        manager.addListener(new Listener() {
            public void handleEvent(Event event) {
                firePropertyChange(PROP_DIRTY);
                updateActionsStatus();
            }
        });

        bars.updateActionBars();

        preferencesChangeListener = new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (PreferencesPage.preferenceFontData.equals(event.getProperty()))
                    manager.setTextFont((FontData) event.getNewValue());
            }
        };
        IPreferenceStore store = BinaryEditorPlugin.getDefault().getPreferenceStore();
        store.addPropertyChangeListener(preferencesChangeListener);

        manager.addLongSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("rawtypes")
			public void widgetSelected(SelectionEvent e) {
                if (selectionListeners == null)
                    return;

                long[] longSelection = HexTexts.getLongSelection(e);
                SelectionChangedEvent event = new SelectionChangedEvent(BinaryEditor.this,
                        new StructuredSelection(new Object[] {new Long(longSelection[0]),
                                new Long(longSelection[1])}));
                for (Iterator iterator = selectionListeners.iterator(); iterator.hasNext();) {
                    ((ISelectionChangedListener) iterator.next()).selectionChanged(event);
                }
            }
        });
        getSite().getPage().addSelectionListener(
                new ISelectionListener() {
                    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
                        if ("net.sourceforge.javahexeditor".equals(part.getSite().getId()))
                            return;
                    }
                });

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
                "net.sourceforge.javahexeditor.hexEditor"); //$NON-NLS-1$
    }

    /**
     * Removes preferences-changed listener
     *
     * @see WorkbenchPart#dispose()
     */
    public void dispose() {
        IPreferenceStore store = BinaryEditorPlugin.getDefault().getPreferenceStore();
        store.removePropertyChangeListener(preferencesChangeListener);
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor) {
        monitor.beginTask(textSavingFilePleaseWait, IProgressMonitor.UNKNOWN);
        boolean successful = getManager().saveFile();
        monitor.done();
        if (!successful) {
            manager.showErrorBox(getEditorSite().getShell());
        }
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs() {
        saveToFile(false);
    }

    @SuppressWarnings("rawtypes")
	public Object getAdapter(Class required) {
        Object result = null;
        if (BinaryContent.class.isAssignableFrom(required)) {
            result = getManager().getContent();
        } else {
            result = super.getAdapter(required);
        }
        return result;
    }

    /**
     * Getter for the manager instance.
     *
     * @return the manager
     */
    public Manager getManager() {
        if (manager == null)
            manager = new Manager();

        return manager;
    }

    public ISelection getSelection() {
        long[] longSelection = getManager().getSelection();
        return new StructuredSelection(new Object[] {new Long(longSelection[0]),
                new Long(longSelection[1])});
    }

    @SuppressWarnings("rawtypes")
	boolean implementsInterface(IEditorInput input, String interfaceName) {
        Class[] classes = input.getClass().getInterfaces();
        for (int i = 0; i < classes.length; ++i) {
            if (interfaceName.equals(classes[i].getName()))
                return true;
        }

        return false;
    }

    public void init(IEditorSite site, final IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        site.getActionBarContributor().setActiveEditor(this);
        site.setSelectionProvider(this);
    }

    public boolean isDirty() {
        return getManager().isDirty();
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        if (selectionListeners != null) {
            selectionListeners.remove(listener);
        }
    }

    void saveToFile(final boolean selection) {
        final File file = getManager().showSaveAsDialog(getEditorSite().getShell(), selection);
        if (file == null)
            return;

        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) {
                monitor.beginTask(textSavingFilePleaseWait, IProgressMonitor.UNKNOWN);
                boolean successful = false;
                if (selection)
                    successful = manager.doSaveSelectionAs(file);
                else
                    successful = manager.saveAsFile(file);
                monitor.done();
                if (successful && !selection) {
                    setPartName(file.getName());
                    firePropertyChange(PROP_DIRTY);
                }
                if (!successful)
                    manager.showErrorBox(getEditorSite().getShell());
            }
        };
        ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(getEditorSite().getShell());
        try {
            monitorDialog.run(false, false, runnable);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFocus() {
        // useless. It is called before ActionBarContributor.setActiveEditor() so focusing is done there
    }

    public void setSelection(ISelection selection) {
        if (selection.isEmpty())
            return;
        StructuredSelection aSelection = (StructuredSelection) selection;
        long start = ((Long) aSelection.getFirstElement()).longValue();
        long end = start;
        if (aSelection.size() > 1) {
            end = ((Long) aSelection.toArray()[1]).longValue();
        }
        getManager().setSelection(start, end);
    }

    /**
     * Updates the status of actions: enables/disables them depending on whether there is text
     * selected and whether inserting or overwriting. Undo/redo actions enabled/disabled as well.
     */
    public void updateActionsStatus() {
        boolean textSelected = getManager().isTextSelected();
        boolean lengthModifiable = textSelected && !manager.isOverwriteMode();
        IActionBars bars = getEditorSite().getActionBars();
        IAction action = bars.getGlobalActionHandler(ActionFactory.UNDO.getId());
        if (action != null)
            action.setEnabled(manager.canUndo());

        action = bars.getGlobalActionHandler(ActionFactory.REDO.getId());
        if (action != null)
            action.setEnabled(manager.canRedo());

        action = bars.getGlobalActionHandler(ActionFactory.CUT.getId());
        if (action != null)
            action.setEnabled(lengthModifiable);

        action = bars.getGlobalActionHandler(ActionFactory.COPY.getId());
        if (action != null)
            action.setEnabled(textSelected);

        action = bars.getGlobalActionHandler(ActionFactory.DELETE.getId());
        if (action != null)
            action.setEnabled(lengthModifiable);

        bars.updateActionBars();
    }
}
