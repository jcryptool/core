// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.editor.text.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.jcryptool.core.commands.FileOpener;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

/**
 * A simple text editor.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class JCTTextEditor extends AbstractTextEditor implements IPropertyListener {
	/** ID of JCT text editor. */
	public static final String ID = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$
	private boolean isDirty = false;
	private boolean isHot = false;

    /**
	 * constructor which sets the key bindings' scopes and process an internal
	 * init.
	 */
	public JCTTextEditor() {
		super();
		configureInsertMode(SMART_INSERT, false);
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" }); //$NON-NLS-1$
		setDocumentProvider(new SimpleDocumentProvider(this));
		addPropertyListener(this);
		IPreferenceStore store = new PreferenceStore();
		setPreferenceStore(store);
	}
	
	/**
	 * override to grab the parent object for drag&drop
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		DropTarget target = new DropTarget(parent, DND.DROP_MOVE);
		target.setTransfer(new Transfer[] { TextTransfer.getInstance(), FileTransfer.getInstance() });

		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
					String[] filenames = (String [])event.data;
					for(String filename:  filenames) {
						FileOpener.open(filename);
					}
				} else {
					AlgorithmView.doAction((String) event.data); // call the algorithm action
				}
			}
		});

		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(parent, "org.jcryptool.editor.text.textEditor"); //$NON-NLS-1$
	}

	/**
	 * Retrieves the current content of the editor
	 *
	 * @return the current content of the editor as an IDocument-object
	 */
	public IDocument getDocument() {
		return getDocumentProvider().getDocument(getEditorInput());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void doSaveAs() {
		performSaveAs(getProgressMonitor());
	}

	private String queryFilePath() {
		FileDialog dialog = new FileDialog(getSite().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION });
		dialog.setFilterNames(new String[] { IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME });
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setOverwrite(true);
		return dialog.open();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		final String name = getEditorInput().getName();

		if (Pattern.matches(Messages.JCTTextEditor_4, name) || Pattern.matches(IConstants.OUTPUT_REGEXP, name)) {
			doSaveAs();
		} else {
			super.doSave(monitor);
		}
	}

	@Override
	protected void performSaveAs(IProgressMonitor monitor) {
		String queriedFilePath = queryFilePath();
		IPath path;
		if (queriedFilePath != null) {
			path = new Path(queriedFilePath);
		} else
			return;

		final IEditorInput newInput = new PathEditorInput(new Path(
				path.toOSString()));
		final String name = getEditorInput().getName();

		if (Pattern.matches(Messages.JCTTextEditor_4, name) || Pattern.matches(IConstants.OUTPUT_REGEXP, name)) {
			// we need the isDirty flag true to follow the default property changed order
			isDirty = true;
		}

		final boolean overwrite = checkOverwrite(queriedFilePath);

		if (!overwrite) {
			return;
		}

		final boolean success = saveFile(path.toFile(), monitor, true);

		if (success) {
			isHot = false;

			setInput(newInput);
		}

		if (monitor != null) {
			monitor.setCanceled(!success);
		}
	}

	private boolean checkOverwrite(final String file) {
		boolean overwrite = true;

		if (new File(file).exists()) {
			overwrite = MessageDialog.openConfirm(getSite().getShell(),
					Messages.JCTTextEditor_5, Messages.JCTTextEditor_6);
		}

		return overwrite;
	}

	/**
	 * Saves the current content of the Editor to a file.
	 *
	 * @param ioFile
	 *            The target file
	 * @param monitor
	 *            The attached ProgressMonitor
	 * @param isSaveAs
	 *            true, if this method should save to a new file; false, if an
	 *            existing file is being overwritten
	 */
	private boolean saveFile(File ioFile, IProgressMonitor monitor,
			boolean isSaveAs) {
		String absolutePath = ioFile.getAbsolutePath();

		if (!isSaveAs) {
			if (!ioFile.exists()) {
				MessageDialog.openInformation(getSite().getShell(),
						Messages.JCTTextEditor_9,
						NLS.bind(Messages.JCTTextEditor_10, absolutePath));
				return false;
			}
			if (!ioFile.isFile()) {
				MessageDialog.openInformation(getSite().getShell(),
						Messages.JCTTextEditor_9,
						NLS.bind(Messages.JCTTextEditor_13, absolutePath));
				return false;
			}
			if (!ioFile.canWrite()) {
				MessageDialog.openInformation(getSite().getShell(),
						Messages.JCTTextEditor_9,
						NLS.bind(Messages.JCTTextEditor_16, absolutePath));
				return false;
			}
		}

		File file = new File(absolutePath);

		if (isSaveAs) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LogUtil.logError(
						JCTTextEditorPlugin.PLUGIN_ID,
						NLS.bind(Messages.JCTTextEditor_21,
								file.getAbsolutePath()), e, true);
				return false;
			}
		}

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			fos.write(getDocument().get().getBytes());
			fos.close();
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (FileNotFoundException e) {
			LogUtil.logError(JCTTextEditorPlugin.PLUGIN_ID,
					"Exception while initializing an output stream", e, false); //$NON-NLS-1$
			return false;
		} catch (IOException e) {
			LogUtil.logError(JCTTextEditorPlugin.PLUGIN_ID,
					"Exception while writing to an output stream", e, false); //$NON-NLS-1$
			return false;
		}

		return true;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * In order to have a working dirty behavior, we have to override the
	 * propertyChanged listener method and set the dirty flag accordingly.
	 * However, since PROP_DIRTY is thrown one time before the editor is
	 * actually open to the user, we have to use an additional flag to activate
	 * the listener behavior.
	 *
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object,
	 *      int)
	 */
	@Override
	public void propertyChanged(Object source, int propId) {
		if (getEditorInput() != null) {
			final String name = getEditorInput().getName();

			// sets the editor to dirty for generated output files
			if (Pattern.matches(IConstants.OUTPUT_REGEXP, name)) {
				isHot = true;
			}
		}

		if (propId == IEditorPart.PROP_DIRTY) {
			if (!isHot) {
				isHot = true;
			} else {
				if (getEditorSite() != null && getEditorInput() != null) {
					boolean value = isDirty;
					this.isDirty = !value;
				}
			}
		}
	}
}
