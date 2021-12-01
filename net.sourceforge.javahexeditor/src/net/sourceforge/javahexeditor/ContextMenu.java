// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package net.sourceforge.javahexeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

/**
 * Context Menu 
 * @author Thorben Groos 
 *
 */
public class ContextMenu {
	
	/**
	 * Adds the context menu to the given styled text
	 * @param st The styledText where the context menu should be added.
	 * @param manager JavaHexEditor Manager to get acces to methods like doCopy(), etc.
	 */
	public static void createMenuForText(StyledText st, Manager manager) {
		
		Menu menu = new Menu(st);
		st.setMenu(menu);
		
		MenuItem undoItem = new MenuItem(menu, SWT.None);
		undoItem.setText(Texts.ContextMenu_undo);
		undoItem.setImage(ImageService.createIconFromURL("platform:/plugin/org.eclipse.ui/icons/full/etool16/undo_edit.png").createImage()); //$NON-NLS-1$
		undoItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				manager.doUndo();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// This method did not get called.
			}
		});
		
		MenuItem redoItem = new MenuItem(menu, SWT.None);
		redoItem.setText(Texts.ContextMenu_redo);
		redoItem.setImage(ImageService.createIconFromURL("platform:/plugin/org.eclipse.ui/icons/full/etool16/redo_edit.png").createImage()); //$NON-NLS-1$
		redoItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				manager.doRedo();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// This method did not get called.
			}
		});
		
		// Separator between redo and copy
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem cutItem = new MenuItem(menu, SWT.None);
		cutItem.setText(Texts.ContextMenu_cut);
		cutItem.setImage(ImageService.createIconFromURL("platform:/plugin/org.eclipse.ui/icons/full/etool16/cut_edit.png").createImage()); //$NON-NLS-1$
		cutItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				manager.doCut();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// This method did not get called.
			}
		});
		
		MenuItem copyItem = new MenuItem(menu, SWT.None);
		copyItem.setText(Texts.ContextMenu_copy);
		copyItem.setImage(ImageService.createIconFromURL("platform:/plugin/org.eclipse.ui/icons/full/etool16/copy_edit.png").createImage()); //$NON-NLS-1$
		copyItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				manager.doCopy();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// This method did not get called.
			}
		});
		
		MenuItem pasteItem = new MenuItem(menu, SWT.None);
		pasteItem.setText(Texts.ContextMenu_paste);
		pasteItem.setImage(ImageService.createIconFromURL("platform:/plugin/org.eclipse.ui/icons/full/etool16/paste_edit.png").createImage()); //$NON-NLS-1$
		pasteItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				manager.doPaste();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// This method did not get called.
			}
		});
		
		// Separator
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem switchItem = new MenuItem(menu, SWT.None);
		switchItem.setText(Texts.ContextMenu_openin);
		switchItem.setImage(ImageService.getImageDescriptor(JCTTextEditorPlugin.PLUGIN_ID, "icons/text_edit.png").createImage()); //$NON-NLS-1$
		switchItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				OpenInTexteditor.changeEditor();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// This method did not get called.
			}
		});
		
		
		menu.addMenuListener(new MenuListener() {
			
			@Override
			public void menuShown(MenuEvent e) {
				
				// Enable / Disable the undo menu entry.
				if (manager.canUndo()) {
					undoItem.setEnabled(true);
				} else {
					undoItem.setEnabled(false);
				}
				
				// Enable / Disable the redo menu entry
				if (manager.canRedo()) {
					redoItem.setEnabled(true);
				} else {
					redoItem.setEnabled(false);
				}
				
				// Enable / Disable the cut menu entry
				if (manager.isTextSelected() && !manager.isOverwriteMode()) {
					cutItem.setEnabled(true);
				} else {
					cutItem.setEnabled(false);
				}
				
				// Enable / Disable the copy menu entry
				if (manager.isTextSelected()) {
					copyItem.setEnabled(true);
				} else {
					copyItem.setEnabled(false);
				}
				
				// Enable / Disable the paste Item
				if (manager.canPaste()) {
					pasteItem.setEnabled(true);
				} else {
					pasteItem.setEnabled(false);
				}
			}
			
			@Override
			public void menuHidden(MenuEvent e) {
				// No action required when the menu is closed.
			}
		});

	}


}
