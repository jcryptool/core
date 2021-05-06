// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.types.ActionCascade;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.preferences.PreferenceConstants;
import org.jcryptool.actions.ui.views.ActionView;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmHandler;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.dataobject.DataObjectConverter;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.integrator.IntegratorOperation;
import org.jcryptool.crypto.flexiprovider.operations.engines.PerformOperationManager;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * <b>Start handler</b> for the Actions view. Runs the created or imported action cascade.
 *
 * @author Dominik Schadow
 * @author Holger Friedrich (support for Commands)
 * @version 0.9.5
 */
public class StartHandler extends AbstractHandler {

    final Display display = Display.getCurrent();

    @Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ActionView view = (ActionView) HandlerUtil.getActivePart(event);
        if (!view.hasContent()) {
            MessageDialog.openInformation(HandlerUtil.getActiveShell(event), Messages.StartHandler_0,
                    Messages.StartHandler_1);
        } else {
            final ActionItem a = (ActionItem) ((IStructuredSelection) (view.getViewer().getSelection()))
                    .getFirstElement();
            Job job = new Job(Messages.StartHandler_5) {
                @Override
				public IStatus run(final IProgressMonitor monitor) {
                    return execute(view, a, HandlerUtil.getActiveShell(event), monitor);
                }
            };
            job.setUser(true);
            job.schedule();
        }

        return null;
    }

    public IStatus execute(final ActionView view, final ActionItem startItem, final Shell shell,
            IProgressMonitor monitor) {
        ActionCascade ac = view.getActionCascade();
        final TableViewer viewer = view.getViewer();

        LogUtil.logInfo("Running Action Cascade " + ac.getName()); //$NON-NLS-1$

        boolean start = false;
        for (final ActionItem a : ac.getAllItems()) {
            int steps = ac.getSize();

            // Start the execution at the first selected element
            if (a == startItem) {
                start = true;
                monitor.beginTask(a.getActionName(), steps);
            } else
                steps--;

            if (start) {
                if (monitor.isCanceled()) {
                    return Status.CANCEL_STATUS;
                }

                try {
                    boolean executed = false;

                    // Set focus on moved row. Just look and feel ...
                    display.asyncExec(new Runnable() {
                        @Override
						public void run() {
                            viewer.setSelection(new StructuredSelection(a), true);
                        }
                    });

                    OperationsPlugin op = OperationsPlugin.getDefault();
                    CommandInfo[] commands = op.getAlgorithmsManager().getShadowAlgorithmCommands();

                    // Try to find an CryptoAlgorithm-Plug-in (Classic or Modern)
                    for (int i = 0, length = commands.length; i < length; i++) {
                        if (commands[i].getHandler() != null && a.getPluginId().equals(commands[i].getCommandId())) {
                            ((ShadowAlgorithmHandler) commands[i].getHandler()).run(convert(a));
                            executed = true;
                        }
                    }

                    // Try to find a FlexiProvider algorithm
                    if (!executed) {
                        LogUtil.logInfo("Trying to execute FlexiProvider algorithm"); //$NON-NLS-1$
                        for (RegistryType rt : RegistryType.values()) {
                            if (rt.getName().equals(a.getPluginId())) {
                                AlgorithmDescriptor descriptor = new AlgorithmDescriptor(a.getActionName(), rt, null);

                                if (a.getParam("algorithm type").equals(RegistryType.BLOCK_CIPHER.getName())) { //$NON-NLS-1$
                                    descriptor = new BlockCipherDescriptor(descriptor.getAlgorithmName(),
                                            a.getParam("mode"), a.getParam("padding scheme"), null, null); //$NON-NLS-1$ //$NON-NLS-2$
                                } else if (a.getParam("algorithm type").equals(RegistryType.SECURE_RANDOM.getName())) { //$NON-NLS-1$
                                    // TODO Get property "alphabet" from item
                                    byte[][] alphabet = null;
                                    if (alphabet == null) {
                                        descriptor = new SecureRandomDescriptor(descriptor.getAlgorithmName(),
                                                Integer.parseInt(a.getParam("random size"))); //$NON-NLS-1$
                                    }
                                }

                                final IFlexiProviderOperation operation = new IntegratorOperation(descriptor);
                                operation.setInput(Messages.InputType);
                                operation.setOutput(Messages.StartHandler_6);
                                operation.setSignature(a.getParam("signature")); //$NON-NLS-1$

                                if ("encrypt".equals(a.getActionType())) { //$NON-NLS-1$
                                    operation.setOperation(OperationType.ENCRYPT);
                                } else if ("decrypt".equals(a.getActionType())) { //$NON-NLS-1$
                                    operation.setOperation(OperationType.DECRYPT);
                                } else if ("sign".equals(a.getActionType())) { //$NON-NLS-1$
                                    operation.setOperation(OperationType.SIGN);
                                } else if ("verify".equals(a.getActionType())) { //$NON-NLS-1$
                                    operation.setOperation(OperationType.VERIFY);
                                } else {
                                    operation.setOperation(OperationType.UNKNOWN);
                                }

                                String alias = a.getParam("key alias"); //$NON-NLS-1$
                                if (alias != null) {
                                    operation.setKeyStoreAlias(new KeyStoreAlias(alias));
                                }

                                String password = a.getParam("key password"); //$NON-NLS-1$
                                if (password != null) {
                                    operation.setPassword(password.toCharArray());
                                }

                                display.asyncExec(new Runnable() {
                                    @Override
									public void run() {
                                        PerformOperationManager.getInstance().firePerformOperation(operation);
                                    }
                                });
                                executed = true;
                            }
                        }
                    }
                    if (!executed) {
                        throw new Exception(Messages.StartHandler_2 + a.getPluginId());

                    }
                } catch (final Exception e) {
                    if (ActionsUIPlugin.getDefault().getPreferenceStore()
                            .getBoolean(PreferenceConstants.P_IGNORE_ERRORS)) {
                        LogUtil.logError(ActionsUIPlugin.PLUGIN_ID, a.getActionName() + ": " + e.getMessage(), e, false); //$NON-NLS-1$
                        if (!ActionsUIPlugin.getDefault().getPreferenceStore()
                                .getBoolean(PreferenceConstants.P_DONT_SHOW_MESSAGES)) {
                            display.syncExec(new Runnable() {
                                @Override
								public void run() {
                                    MessageDialog.openWarning(shell, Messages.StartHandler_3, e.getMessage());
                                }
                            });
                        }
                    } else {
                        display.asyncExec(new Runnable() {
                            @Override
							public void run() {
                                MessageDialog.openError(shell, Messages.StartHandler_3, e.getMessage()
                                        + Messages.StartHandler_4); 
                            }
                        });
                        return Status.CANCEL_STATUS;
                    }
                } finally {
                    monitor.worked(ac.getSize() - steps);
                }
            }
        }
        monitor.done();
        return Status.OK_STATUS;
    }

    public IDataObject convert(ActionItem actionItem) {
        IDataObject dataobject = DataObjectConverter.hashtableToProperties(actionItem.getParams(),
                actionItem.getDataObjectType());

        if (dataobject instanceof ClassicDataObject) {
            AbstractAlphabet alphabet = AlphabetsManager.getInstance().getAlphabetByName(actionItem.getAlphabet());
            ((ClassicDataObject) dataobject).setAlphabet(alphabet);
            if ("encrypt".equals(actionItem.getActionType())) { //$NON-NLS-1$
                ((ClassicDataObject) dataobject).setOpmode(AbstractAlgorithm.ENCRYPT_MODE);
            } else if ("decrypt".equals(actionItem.getActionType())) { //$NON-NLS-1$
                ((ClassicDataObject) dataobject).setOpmode(AbstractAlgorithm.DECRYPT_MODE);
            }
        }

        return dataobject;
    }
}