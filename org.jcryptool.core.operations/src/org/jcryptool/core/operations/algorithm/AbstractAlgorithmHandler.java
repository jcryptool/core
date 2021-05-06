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
package org.jcryptool.core.operations.algorithm;

import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.DataObjectConverter;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.operations.dataobject.classic.IClassicDataObject;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorsManager;

/**
 * The standard abstract implementation of an algorithm command handler.
 * 
 * @author amro
 * @author Dominik Schadow
 * @author Holger Friedrich (supprt for Commands, adapted from AbstractAlgorithmAction)
 * @version 0.9.5
 */
public abstract class AbstractAlgorithmHandler extends AbstractHandler {
    /** Type of an algorithm (classic, asymmetric etc). */
    private String algorithmType;
    /** The data object. */
    private IDataObject dataObject;
    /** The (active) editor part. */
    private IEditorPart editorPart;
    /** The editors manager. */
    private EditorsManager manager;
    /** The input of the (active) editor. */
    private IEditorInput output;
    /** The active workbench window. */
    private IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    
    private String id;
    public String getId() {
    	return(id);
    }
    public void setId(String id) {
    	this.id = id;
    }

    /**
     * Constructor. Disables every action when it is instantiated.
     */
    public AbstractAlgorithmHandler() {
        super();
        setEnabled(false);
    }

    /**
     * Getter for this class' object
     * 
     * @return the handler object
     */
    public IHandler getHandler() {
        return this;
    }

    /**
     * Sets the data object.
     * 
     * @param dataObject
     */
    public void setDataObject(IDataObject dataObject) {
        this.dataObject = dataObject;
    }

    /**
     * Returns the data object.
     * 
     * @return The data object
     */
    public IDataObject getDataObject() {
        return dataObject;
    }

    /**
     * Performs the user interface action. Subclasses must provide mechanisms to retrieve eventually input for
     * initializing the algorithm. It is recommended to include all cryptographic actions into a user Job (background
     * task) to perform the possibly long running operation as stoppable background task instead of blocking the UI the
     * whole time.
     */
    public abstract Object execute(ExecutionEvent event);

    /**
     * Executes the algorithm described by the data object and without calling the wizard. This method is used by the
     * <b>Actions View</b> to replay cryptographic operations without user interaction.
     * 
     * @param dataobject
     */
    public abstract void run(IDataObject dataobject);

    /**
     * This method completes the algorithm action In this connection the polymorphism is taken advantage of by using an
     * abstract algorithm object which is passed as the parameter.
     * 
     * Subclasses should use this method to finalize the to be implemented <i>run()</i> method.
     * 
     * @param algorithm the abstract algorithm
     */
    protected void finalizeRun(AbstractAlgorithm algorithm) {
        Observer dummyObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            }
        };
        finalizeRun(algorithm, dummyObserver);
    }

    /**
     * This method completes the algorithm action In this connection the polymorphism is taken advantage of by using an
     * abstract algorithm object which is passed as the parameter. The observer-parameter will be notified when an
     * editor opens as result of the algorithm finalization.
     * 
     * Subclasses should use this method to finalize the to be implemented <i>run()</i> method.
     * 
     * @param algorithm the abstract algorithm
     * @param o an observer which will be notified when an editor is opened. The "observable" argument will always be
     *            null; the "arg" argument will be either null (if opening the editor failed) or the IEditorPart
     *            reference to the opened editor.
     * 
     * @throws NullPointerException if the provided observer object is null
     */
    protected void finalizeRun(AbstractAlgorithm algorithm, Observer o) {
        dataObject = algorithm.execute();

        if (editorPart == null) {
            editorPart = getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        }
        final String editorId = editorPart.getSite().getId();

        if (algorithm instanceof AbstractClassicAlgorithm) {
            if (((IClassicDataObject) dataObject).getOutputIS() != null) {
                output = AbstractEditorService.createOutputFile(((IClassicDataObject) dataObject).getOutputIS());
                // TODO: fix special character issues with IS input as well
            } else {
                output = AbstractEditorService.createOutputFile(((IClassicDataObject) dataObject).getOutput());
            }

            addActionItem(algorithm);

            performOpenEditor(output, editorId, o);
        } else if (algorithm instanceof AbstractModernAlgorithm) {
            if (((IModernDataObject) dataObject).getOutputIS() != null) {
                output = AbstractEditorService.createOutputFile(((IModernDataObject) dataObject).getOutputIS());
            } else {
                output = AbstractEditorService.createOutputFile(((IModernDataObject) dataObject).getOutput());
            }

            addActionItem(algorithm);

            // modern algorithms are based on bytes, not characters; using the
            // text editor would be inappropriate
            performOpenEditor(output, IOperationsConstants.ID_HEX_EDITOR, o);
        } else {
            LogUtil.logWarning("Action recording for algorithms of type " + //$NON-NLS-1$
                    algorithm.getClass() + " is not implemented"); //$NON-NLS-1$
        }
    }

    /**
     * Adds the cryptographic action information as a new <code>ActionItem</code> to the <b>Actions view</b>. Adding the
     * new ActionItem must be executed in an async way since a Job may be used to execute the cryptographic operation.
     * 
     * @param algorithm The executed AbstractAlgorithm implementation
     */
    private void addActionItem(AbstractAlgorithm algorithm) {
        ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);

        if ((Boolean) service.getCommand("org.jcryptool.actions.recordCommand"). //$NON-NLS-1$
                getState("org.jcryptool.actions.recordCommand.toggleState").getValue()) { //$NON-NLS-1$
            final ActionItem item = new ActionItem(output.getName(), algorithm.getAlgorithmName());
            item.setPluginId(this.getId());
            if (algorithm instanceof AbstractClassicAlgorithm) {
                ClassicDataObject dataobject = (ClassicDataObject) dataObject;
                if (dataobject.getOpmode() == 0) {
                    item.setActionType("encrypt"); //$NON-NLS-1$
                } else if (dataobject.getOpmode() == 1) {
                    item.setActionType("decrypt"); //$NON-NLS-1$
                }
                item.setAlphabet(dataobject.getAlphabet().getName());
                dataobject.setFilterNonAlphaChars(((AbstractClassicAlgorithm) algorithm).isFilter());
                item.setParams(DataObjectConverter.propertiesToHashtable(dataobject));
                item.setDataObjectType(dataobject.getClass().getName());

            } else if (algorithm instanceof AbstractModernAlgorithm) {
                ModernDataObject dataobject = (ModernDataObject) dataObject;
                item.setParams(DataObjectConverter.propertiesToHashtable(dataobject));
                item.setDataObjectType(dataobject.getClass().getName());
            }
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    ActionCascadeService.getInstance().addItem(item);
                }
            });
        }
    }

    /**
     * Tries to open the given IEditorInput in the Editor associated with the given ID. This method must be executed in
     * an async way since a Job may be used to execute the cryptographic operation.
     * 
     * @param input The IEditorInput that shall be displayed
     * @param editorId The ID of the Editor that is supposed to open
     */
    protected void performOpenEditor(final IEditorInput input, final String editorId) {
        Observer dummyObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            }
        };
        performOpenEditor(input, editorId, dummyObserver);
    }

    /**
     * Tries to open the given IEditorInput in the Editor associated with the given ID. This method must be executed in
     * an async way since a Job may be used to execute the cryptographic operation.
     * 
     * @param input The IEditorInput that shall be displayed
     * @param editorId The ID of the Editor that is supposed to open
     * @param o an observer which will be notified when the editor is opened. The "observable" argument will always be
     *            null; the "arg" argument will be either null (if opening the editor failed) or the IEditorPart
     *            reference to the opened editor.
     * 
     * @throws NullPointerException if the provided observer object is null
     */
    protected void performOpenEditor(final IEditorInput input, final String editorId, final Observer o) {
        if (o == null) {
            throw new NullPointerException("provided observer for performOpenEditor method was null.");
        }
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                try {
                    IEditorPart editorRef = getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
                    o.update(null, editorRef);
                } catch (PartInitException ex) {
                    try {
                        IEditorPart editorRef = getActiveWorkbenchWindow().getActivePage().openEditor(input,
                                IOperationsConstants.ID_HEX_EDITOR);
                        o.update(null, editorRef);
                    } catch (PartInitException e) {
                        o.update(null, null);
                        LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);

                        MessageDialog.openError(getActiveWorkbenchWindow().getShell(),
                                Messages.AbstractAlgorithmAction_1,
                                NLS.bind(Messages.AbstractAlgorithmAction_2, new Object[] { editorId }));
                    } catch (Exception e) {
                        o.update(null, null);
                        LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);

                        MessageDialog.openError(getActiveWorkbenchWindow().getShell(),
                                Messages.AbstractAlgorithmAction_1,
                                NLS.bind(Messages.AbstractAlgorithmAction_2, new Object[] { editorId }));
                    }
                }
            }
        });
    }

    /**
     * Getter for the algorithm type
     * 
     * @return the algorithm as a string
     */
    public String getAlgorithmType() {
        return algorithmType;
    }

    /**
     * Setter for the algorithm type
     * 
     * @param algorithmType the type of an algorithm
     */
    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    /**
     * Getter for the active workbench window
     * 
     * @return the active workbench window
     */
    protected IWorkbenchWindow getActiveWorkbenchWindow() {
        if (window == null) {
            window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        return window;
    }

    /**
     * Getter for the (String) content of the active editor. Specifically the EditorsManager (a singleton object) is
     * used to ask the current editor for its content.
     * 
     * @see org.jcryptool.core.operations.editors.EditorsManager
     * 
     * @return the content of the active editor as a string
     */
    @SuppressWarnings("deprecation")
    protected String getActiveEditorContentAsString() {
        editorPart = getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        manager = EditorsManager.getInstance();
        return manager.getContentAsString(editorPart);
    }

    /**
     * Getter for the (byte[]) content of the active editor. Specifically the EditorsManager (a singleton object) is
     * used to ask the current editor for its content.
     * 
     * @deprecated Use org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction.getActiveEditorInputStream()
     *             instead
     * 
     * @see org.jcryptool.core.operations.editors.EditorsManager
     * 
     * @return the content of the active editor as a byte[]
     */
    protected byte[] getActiveEditorContentAsBytes() {
        editorPart = getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        manager = EditorsManager.getInstance();
        return manager.getContentAsBytes(editorPart);
    }

    /**
     * Getter for the (InputStream) content of the active editor. Specifically the EditorsManager (a singleton object)
     * is used to ask the current editor for its content.
     * 
     * @see org.jcryptool.core.operations.editors.EditorsManager
     * 
     * @return the content of the active editor as InputStream
     */
    protected InputStream getActiveEditorInputStream() {
        editorPart = getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        manager = EditorsManager.getInstance();
        return manager.getContentInputStream(editorPart);
    }

}
