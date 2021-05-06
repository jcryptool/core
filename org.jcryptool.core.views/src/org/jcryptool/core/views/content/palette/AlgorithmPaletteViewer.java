//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.views.content.palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.jcryptool.core.ApplicationActionBarAdvisor;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmHandler;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.core.views.ISearchable;
import org.jcryptool.core.views.ViewsPlugin;
import org.jcryptool.core.views.content.TreeView;

/**
 * A PaletteViewer for the algorithm extension point.
 *
 * @author mwalthart
 * @author Holger Friedrich (support for Commands)
 * @version 0.9.6
 */
public class AlgorithmPaletteViewer extends PaletteViewer implements ISearchable {
	private AbstractHandler doubleClickHandler;
	private AlgorithmPaletteViewer viewer = this;
	private PaletteRoot invisibleRoot;
	private ArrayList<CommandInfo> algorithmList = new ArrayList<CommandInfo>();
	private String search;
	private String extensionPointId = "org.jcryptool.core.operations.algorithms_cmd"; //$NON-NLS-1$

	/**
	 * creates a palette viewer
	 *
	 * @param parent
	 *            the parent composite
	 */
	public AlgorithmPaletteViewer(Composite parent) {
		super();
		loadAlgorithms();
		createTree(new String[] { "" }); //$NON-NLS-1$
		createControl(parent);

		// close all except classic and symmetric
		for (Object drawer : invisibleRoot.getChildren()) {
			if (!((PaletteDrawer) drawer).getLabel().equals(
					Messages.AlgorithmPaletteViewer_1)
					&& !((PaletteDrawer) drawer).getLabel().equals(
							Messages.AlgorithmPaletteViewer_2))
				((PaletteDrawer) drawer)
						.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		}

		setPaletteRoot(invisibleRoot);
		setupDragAndDrop();
		makeAndAssignActions();
	}

	/**
	 * enables drag'n'drop functionality
	 */
	private void setupDragAndDrop() {
		viewer.addDragSourceListener(new TransferDragSourceListener() {
			public Transfer getTransfer() {
				return TextTransfer.getInstance();
			}

			public void dragFinished(DragSourceEvent event) {
			}

			public void dragSetData(DragSourceEvent event) {
				Object element = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
				Object model = ((PaletteEditPart) element).getModel();
				event.data = ((PaletteEntry) model).getLabel();
			}

			public void dragStart(DragSourceEvent event) {
				Object obj = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

				 // only allow drag&drop for algorithm, not for categories
				if (!(obj instanceof PaletteEditPart))
					event.doit = false;

				// random number generators have also no drag&drop
				final String label = ((PaletteEntry) ((PaletteEditPart) obj).getModel()).getParent().getLabel();
				if (label != null && label.equals(org.jcryptool.core.Messages.applicationActionBarAdvisor_Menu_Algorithms_PRNG)) //$NON-NLS-1$
					event.doit = false;
			}
		});
	}

	/**
	 * loads the algorithms from the extension point
	 */
	private void loadAlgorithms() {
		for (CommandInfo command : OperationsPlugin.getDefault().getAlgorithmsManager().getShadowAlgorithmCommands()) {
			if (!algorithmList.contains(command)) {
				algorithmList.add(command);
			}
		}
	}

	/**
	 * creates a tree representation of the algorithm structure
	 *
	 * @param needles
	 *            a search string to filter the algorithms
	 */
	private void createTree(String[] needles) {
		invisibleRoot = new PaletteRoot();
		TreeMap<String, PaletteDrawer> types = new TreeMap<String, PaletteDrawer>();
		TreeMap<String, SelectionToolEntry> sortList = new TreeMap<String, SelectionToolEntry>();

		Iterator<CommandInfo> it = algorithmList.iterator();
		CommandInfo info = null;

		while (it.hasNext()) {
			info = it.next();

			String text = "";
			String type = "";
			String toolTipText = "";
			boolean isFlexiProviderAlgorithm = false;
			
			ShadowAlgorithmHandler handler = (ShadowAlgorithmHandler)info.getHandler();
			text = handler.getText();
			type = handler.getType();
			toolTipText = handler.getToolTipText();
			isFlexiProviderAlgorithm = handler.isFlexiProviderAlgorithm();
			
			// filter
			boolean show = true;
			for (String needle : needles) {
				if (!text.toLowerCase()
						.matches(".*" + needle.toLowerCase() + ".*")) //$NON-NLS-1$ //$NON-NLS-2$
					show = false;
			}

			if (show) {
				// Create Category
				if (types.get(type) == null) {
					// translate
					type = ApplicationActionBarAdvisor.getTypeTranslation(type);

					PaletteDrawer paletteDrawer = new PaletteDrawer(type);
					paletteDrawer.setSmallIcon(ImageService.getImageDescriptor(ViewsPlugin.PLUGIN_ID, TreeView.ICON_FOLDER));
					paletteDrawer.setLargeIcon(ImageService.getImageDescriptor(ViewsPlugin.PLUGIN_ID, TreeView.ICON_FOLDER));
					types.put(type, paletteDrawer);
				}

				// Add element
				SelectionToolEntry paletteEntry = new SelectionToolEntry(
						text, toolTipText);
				if (isFlexiProviderAlgorithm) { // FlexiProvider item
					paletteEntry.setSmallIcon(ImageService.IMAGEDESCRIPTOR_PERSPECTIVE_ALGORITHM);
					paletteEntry.setLargeIcon(ImageService.IMAGEDESCRIPTOR_PERSPECTIVE_ALGORITHM);
				} else { // JCrypTool item
					paletteEntry.setSmallIcon(ImageService.IMAGEDESCRIPTOR_PERSPECTIVE_STANDARD);
					paletteEntry.setLargeIcon(ImageService.IMAGEDESCRIPTOR_PERSPECTIVE_STANDARD);
				}
				paletteEntry
						.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
				paletteEntry.setType(type);

				sortList.put(paletteEntry.getLabel(), paletteEntry); // temporary save in list
			}
		}
		ArrayList<PaletteDrawer> parents = new ArrayList<PaletteDrawer>(
				types.values());

		for (SelectionToolEntry paletteEntry : sortList.values()) {
			// read from sorted list
			types.get(paletteEntry.getType()).add(paletteEntry); // put sorted into palette
		}

		// attach tree to the root element
		Iterator<PaletteDrawer> parentIterator2 = parents.iterator();
		while (parentIterator2.hasNext()) {
			invisibleRoot.add(parentIterator2.next());
		}
	}

	/**
	 * Constructs the actions according to the algorithm extension point and
	 * assigns the actions to the doubleclick listener of the viewer
	 */
	private void makeAndAssignActions() {
		doubleClickHandler = new AbstractHandler() {
			public Object execute(ExecutionEvent event) {
				Object selection = ((IStructuredSelection) viewer
						.getSelection()).getFirstElement();

				if (selection instanceof PaletteEditPart) {
					PaletteEditPart paletteEditPart = (PaletteEditPart) selection;

					Object model = paletteEditPart.getModel();

					IEditorReference[] editorReferences = PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().getEditorReferences();
					if (editorReferences.length == 0
							&& (!((PaletteEntry) model)
									.getParent()
									.getLabel()
									.equals(org.jcryptool.core.Messages.applicationActionBarAdvisor_Menu_Algorithms_PRNG))) {
						AlgorithmView
								.showMessage(Messages.AlgorithmPaletteViewer_0);
					} else {
		                final ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);

		                Iterator<CommandInfo> it9 = algorithmList
								.iterator();
						CommandInfo commandInfo = null;
						while (it9.hasNext()) {
							commandInfo = it9.next();
							ShadowAlgorithmHandler handler = (ShadowAlgorithmHandler)commandInfo.getHandler();
							String commandId = commandInfo.getCommandId();
							if (commandId != null && model.toString().equals(
									"Palette Entry (" + handler.getText() + ")")) { //$NON-NLS-1$ //$NON-NLS-2$
								Command command = commandService.getCommand(commandId);
								try
								{
									return command.executeWithChecks(event);
								} catch(Exception ex) {
									LogUtil.logError(ViewsPlugin.PLUGIN_ID, ex);
									return(null);
								}
							}
						}
					}
				}
				return(null);
			}
		};

		viewer.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				if (e.button == 1) { // only left button double clicks
                    final IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
                    IEvaluationContext evaluationContext = handlerService.createContextSnapshot(true);
                    ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, evaluationContext);

                    try {
						doubleClickHandler.execute(event); // run assigned action
					} catch(ExecutionException ex) {
						LogUtil.logError(ViewsPlugin.PLUGIN_ID, ex);
					}
				}
			}

			@Override
			public void mouseDown(final MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				Object obj = selection.getFirstElement();

				if (obj instanceof PaletteEditPart) {
					AlgorithmView.showContextHelp(extensionPointId,
							((PaletteEntry) ((PaletteEditPart) obj).getModel())
									.getLabel());
					viewer.getControl().setFocus();
					viewer.setSelection(selection);
				}
			}
		});
	}

	/**
	 * returns the current search string of the viewer
	 *
	 * @see ISearchable
	 */
	public String getCurrentSearch() {
		if (search == null)
			return ""; //$NON-NLS-1$
		return search;
	}

	/**
	 * sets the search string for the viewer
	 */
	public void search(String needle) {
		this.search = needle;

		createTree(needle.split(" ")); //$NON-NLS-1$
		setPaletteRoot(invisibleRoot);
	}
}
