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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import net.sourceforge.javahexeditor.Manager;
import net.sourceforge.javahexeditor.Texts;

/**
 * HexEditor contributor. Contributes status bar and menu bar items
 *
 * @author Jordi Bergenthal
 */
public final class HexEditorActionBarContributor extends EditorActionBarContributor {

	private final class MyMenuContributionItem extends ContributionItem {
		MenuItem myMenuItem;

		MyMenuContributionItem(String id) {
			super(id);
		}

		@Override
		public void fill(Menu parent, int index) {
			boolean textSelected = activeEditor == null ? false : activeEditor.getManager().isTextSelected();
			myMenuItem = new MenuItem(parent, SWT.PUSH, index);
			if (MenuIds.SAVE_SELECTION_AS.equals(getId())) {
				myMenuItem.setText(Texts.EDITOR_SAVE_SELECTION_AS_MENU_ITEM_LABEL);
				myMenuItem.setEnabled(textSelected);
				myMenuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						activeEditor.saveToFile(true);
					}
				});
			} else if (MenuIds.TRIM.equals(getId())) {
				myMenuItem.setText(Texts.EDITOR_TRIM_MENU_ITEM_LABEL);
				myMenuItem.setEnabled(textSelected && !activeEditor.getManager().isOverwriteMode());
				myMenuItem.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						Manager manager = activeEditor.getManager();
						if (manager.isValid()) {
							activeEditor.getManager().doTrim();
						}
					}
				});
			} else if (MenuIds.SELECT_BLOCK.equals(getId())) {
				myMenuItem.setText(Texts.EDITOR_SELECT_BLOCK_MENU_ITEM_LABEL);
				// TODO This only works after the "Edit" menu was shown once
				myMenuItem.setAccelerator(SWT.CONTROL | 'E');
				myMenuItem.setEnabled(true);
				myMenuItem.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						Manager manager = activeEditor.getManager();
						if (manager.isValid()) {
							manager.doSelectBlock();
						}
					}
				});
			}
		}
	}

	private final class MyMenuListener implements IMenuListener {
		public MyMenuListener() {
		}

		@Override
		public void menuAboutToShow(IMenuManager menu) {
			boolean textSelected = activeEditor.getManager().isTextSelected();
			boolean lengthModifiable = textSelected && !activeEditor.getManager().isOverwriteMode();
			IActionBars bars = getActionBars();

			IContributionItem contributionItem = bars.getMenuManager()
					.findUsingPath(IWorkbenchActionConstants.M_FILE + '/' + MenuIds.SAVE_SELECTION_AS);
			if (contributionItem != null && ((MyMenuContributionItem) contributionItem).myMenuItem != null
					&& !((MyMenuContributionItem) contributionItem).myMenuItem.isDisposed()) {
				((MyMenuContributionItem) contributionItem).myMenuItem.setEnabled(textSelected);
			}

			contributionItem = bars.getMenuManager()
					.findUsingPath(IWorkbenchActionConstants.M_EDIT + '/' + MenuIds.TRIM);
			if (contributionItem != null && ((MyMenuContributionItem) contributionItem).myMenuItem != null
					&& !((MyMenuContributionItem) contributionItem).myMenuItem.isDisposed()) {
				((MyMenuContributionItem) contributionItem).myMenuItem.setEnabled(lengthModifiable);
			}
		}
	}

	private final class MyStatusLineContributionItem extends ContributionItem {
		MyStatusLineContributionItem(String id) {
			super(id);
		}

		@Override
		public void fill(Composite parent) {
			if (activeEditor != null) {
				activeEditor.getManager().createStatusPart(parent, true);
			}
		}
	}

	private static final class MenuIds {
		private static final String SAVE_SELECTION_AS = "saveSelectionAs";
		private static final String TRIM = "trim";
		private static final String SELECT_BLOCK = "selectBlock";
		private static final String SAVE_AS = "saveAs";
		private static final String DELETE = "delete";
		private static final String SELECT_ALL = "selectAll";
		private static final String ADDITIONS = "additions";
	}

	private static final String STATUS_LINE_ITEM_ID = "AllHexEditorStatusItemsItem";

	HexEditor activeEditor;

	/**
	 * @see EditorActionBarContributor#contributeToMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		IMenuManager menu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_FILE);
		IMenuListener myMenuListener = new MyMenuListener();
		if (menu != null) {
			menu.insertAfter(IWorkbenchCommandConstants.FILE_SAVE_AS, new MyMenuContributionItem(MenuIds.SAVE_SELECTION_AS));
//			menu.add(new MyMenuContributionItem(MenuIds.SAVE_SELECTION_AS));
			menu.addMenuListener(myMenuListener);
		}

		menu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (menu != null) {
//			menu.insertAfter(MenuIds.DELETE, new MyMenuContributionItem(MenuIds.TRIM));
			menu.add(new MyMenuContributionItem(MenuIds.TRIM));
			menu.addMenuListener(myMenuListener);
		}

		menu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (menu != null) {
//			menu.insertAfter(MenuIds.SELECT_ALL, new MyMenuContributionItem(MenuIds.SELECT_BLOCK));
			menu.add(new MyMenuContributionItem(MenuIds.SELECT_BLOCK));
			menu.addMenuListener(myMenuListener);
		}

		menu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE);
		if (menu != null) {
			Action goToAction = new Action() {
				@Override
				public void run() {
					activeEditor.getManager().doGoTo();
				}
			};
			// declared in org.eclipse.ui.workbench.text plugin.xml
			goToAction.setActionDefinitionId(ITextEditorActionDefinitionIds.LINE_GOTO);
			goToAction.setText(Texts.EDITOR_GO_TO_MENU_ITEM_LABEL);
			// TODO This only works after the "Navigate" menu was shown once
			// Eclipse standard even has the correct accelerator.
			// goToAction.setAccelerator(SWT.CTRL + 'L');
			menu.appendToGroup(MenuIds.ADDITIONS, goToAction);
		}
	}

	/**
	 * @see EditorActionBarContributor#contributeToStatusLine(org.eclipse.jface.action.IStatusLineManager)
	 */
	@Override
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		statusLineManager.add(new MyStatusLineContributionItem(STATUS_LINE_ITEM_ID));
	}

	/**
	 * @see IEditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		if (targetEditor instanceof HexEditor) {
			if (activeEditor != null) {
				Manager manager = ((HexEditor) targetEditor).getManager();
				manager.reuseStatusLinelFrom(activeEditor.getManager());
			}
			activeEditor = (HexEditor) targetEditor;
			activeEditor.getManager().setFocus();
			activeEditor.updateActionsStatus();
		}
	}
}
