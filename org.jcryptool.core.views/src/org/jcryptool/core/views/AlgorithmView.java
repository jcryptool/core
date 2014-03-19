// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.operations.CommandOrAction;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmAction;
import org.jcryptool.core.views.content.PaletteView;
import org.jcryptool.core.views.content.TreeView;

/**
 * Displays all available algorithms in a tree or palette view.
 *
 * @author t-kern
 * @version 0.9.1
 */
public class AlgorithmView extends ViewPart implements IOperationsConstants {
    public static final String ID = "org.jcryptool.core.views.AlgorithmView"; //$NON-NLS-1$
//    public static final String EDITOR_ID_HEX = "net.sourceforge.javahexeditor.editors.BinaryEditor"; //$NON-NLS-1$
    public static final String MENU_TEXT_ALGORITHM = Messages.AlgorithmView_menu_text_algorithms;
    public static final String MENU_TEXT_ANALYSIS = Messages.AlgorithmView_menu_text_analysis;
    public static final String MENU_TEXT_VISUALS = Messages.AlgorithmView_menu_text_visuals;
    public static final String MENU_TEXT_GAMES = Messages.AlgorithmView_menu_text_games;

    private static final int VIEW_TREE = 0x00;
    private static final int VIEW_PALETTE = 0x01;

    private static TreeView treeView;
    private static PaletteView paletteView;

    private static StackLayout layout;
    private Composite listing;

    private boolean initialSearchState = true;

    private static final Color COLOR_FILTER_USER = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK); // black
    private static final Color COLOR_FILTER_INITIAL = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY); // grey

    private static ArrayList<ShadowAlgorithmAction> algorithmTypes = new ArrayList<ShadowAlgorithmAction>();

    /**
     * The constructor.
     */
    public AlgorithmView() {
        PlatformUI.getPreferenceStore().setDefault("showView", VIEW_PALETTE); //$NON-NLS-1$
        loadAlgorithms();
    }

    /**
     * This is a callback that will allow us to create the org.jcryptool.core.views content and
     * initialize it.
     */
    public void createPartControl(final Composite parent) {
        GridLayout outerLayout = new GridLayout(2, false);
        outerLayout.horizontalSpacing = 0;
        outerLayout.verticalSpacing = 0;
        outerLayout.marginHeight = 0;
        outerLayout.marginWidth = 0;
        parent.setLayout(outerLayout);
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createSearchArea(parent);
        createViewArea(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, ViewsPlugin.PLUGIN_ID + ".algorithmsView"); //$NON-NLS-1$
    }

    /**
     * creates the area containing the views
     *
     * @param parent the parent composite
     */
    private void createViewArea(Composite parent) {
        layout = new StackLayout();
        listing = new Composite(parent, SWT.NONE);
        listing.setLayout(layout);
        listing.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        // create both views
        paletteView = new PaletteView(listing);
        treeView = new TreeView(listing);

        showTab(MENU_TEXT_ALGORITHM);

        // create the action to switch the views
        Action action = new Action() {
            public void run() {
                switch (PlatformUI.getPreferenceStore().getInt("showView")) { //$NON-NLS-1$
                    case VIEW_PALETTE:
                        layout.topControl = treeView.getControl();
                        PlatformUI.getPreferenceStore().setValue("showView", VIEW_TREE); //$NON-NLS-1$
                        setToolTipText(Messages.AlgorithmView_showPalette);
                        setText(Messages.AlgorithmView_showPalette);
                        break;
                    default:
                        layout.topControl = paletteView.getControl();
                        PlatformUI.getPreferenceStore().setValue("showView", VIEW_PALETTE); //$NON-NLS-1$
                        setToolTipText(Messages.AlgorithmView_showTree);
                        setText(Messages.AlgorithmView_showTree);
                }
                listing.layout();
            }
        };
        action.setImageDescriptor(ViewsPlugin.getImageDescriptor("icons/switch.gif")); //$NON-NLS-1$

        // set the correct view to be shown and assign the corresponding action
        switch (PlatformUI.getPreferenceStore().getInt("showView")) { //$NON-NLS-1$
            case VIEW_PALETTE:
                layout.topControl = paletteView.getControl();
                action.setToolTipText(Messages.AlgorithmView_showTree);
                action.setText(Messages.AlgorithmView_showTree);
                break;
            default:
                layout.topControl = treeView.getControl();
                action.setToolTipText(Messages.AlgorithmView_showPalette);
                action.setText(Messages.AlgorithmView_showPalette);
        }

        IMenuManager manager = getViewSite().getActionBars().getMenuManager();
        manager.add(action);
    }

    /**
     * Creates the search field. On Mac the text does contain a clear (reset) icon which resets
     * the view filter. Since Windows does not support this an additional clear (reset) icon
     * is shown next to the text.
     *
     * @param parent The parent composite
     */
    private void createSearchArea(Composite parent) {
        // search field
        final Text search = new Text(parent, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);

        if ((search.getStyle() & SWT.CANCEL) == 0) {
            // reset search button for systems that do not support the cancel icon
            Canvas canvas = new Canvas(parent, SWT.NONE);
            GridData gridData = new GridData();
            gridData.heightHint = 18;
            gridData.widthHint = 18;
            canvas.setLayoutData(gridData);
            canvas.addPaintListener(new PaintListener(){
                public void paintControl(PaintEvent e) {
                    e.gc.drawImage(ViewsPlugin.getImageDescriptor("icons/clear.gif").createImage(), 1, 1); //$NON-NLS-1$
                }
            });

            canvas.addMouseListener(new MouseAdapter() {
                public void mouseUp(MouseEvent e) {
                    search.setText(Messages.AlgorithmView_search_message);
                    search.setForeground(COLOR_FILTER_INITIAL);
                    initialSearchState = true;
                    treeView.setFilter(""); //$NON-NLS-1$
                    paletteView.setFilter(""); //$NON-NLS-1$
                }
            });
        }

        search.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        search.setText(Messages.AlgorithmView_search_message);
        search.setForeground(COLOR_FILTER_INITIAL);
        search.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                if (initialSearchState) {
                    search.setText(""); //$NON-NLS-1$
                    search.setForeground(COLOR_FILTER_USER);
                    initialSearchState = false;
                }
            }
        });
        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                treeView.setFilter(search.getText());
                paletteView.setFilter(search.getText());
            }

        });
        search.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                if (e.detail == SWT.CANCEL) {
                    search.setText(Messages.AlgorithmView_search_message);
                    search.setForeground(COLOR_FILTER_INITIAL);
                    initialSearchState = true;
                    treeView.setFilter(""); //$NON-NLS-1$
                    paletteView.setFilter(""); //$NON-NLS-1$
                }
            }
        });
    }

    /**
     * used to trigger actions when an algorithm is drag&dropped to an editor
     *
     * @param algorithmName the name of the algorithm whose action is to be done
     */
    public static void doAction(String algorithmName) {
        Iterator<ShadowAlgorithmAction> it9 = algorithmTypes.iterator();
        ShadowAlgorithmAction action = null;
        while (it9.hasNext()) {
            action = it9.next();
            if (algorithmName.equals(action.getText())) {
                action.run();
            }
        }
    }

    /**
     * Passing the focus request to the org.jcryptool.core.views.content's control.
     */
    public void setFocus() {
        switch (PlatformUI.getPreferenceStore().getInt("showView")) { //$NON-NLS-1$
            case VIEW_PALETTE:
                paletteView.getControl().setFocus();
                break;
            default:
                treeView.getControl().setFocus();
        }
    }

    /**
     * Displays messages in a MessageDialog
     *
     * @param message the message text
     */
    public static void showMessage(String message) {
        MessageDialog.openInformation(null, Messages.AlgorithmView_0, message);
    }

    /**
     * sets the viewers to display the given tab
     *
     * @param name the name of the tab to display
     */
    public static void showTab(String name) {
        if (treeView != null)
            treeView.showTab(name);
        if (paletteView != null)
            paletteView.showTab(name);
    }

    public static void showContextHelp(final String extensionPointId, final String name) {
        IConfigurationElement[] elements = Platform.getExtensionRegistry().getExtensionPoint(
                extensionPointId).getConfigurationElements();
        for (IConfigurationElement element : elements) {
            if (element.getAttribute("name").equals(name)) { //$NON-NLS-1$
                final String contextHelpId = element.getAttribute("contextHelpId"); //$NON-NLS-1$
                if (contextHelpId != null && !"".equals(contextHelpId)) { //$NON-NLS-1$
                    PlatformUI.getWorkbench().getHelpSystem().displayHelp(contextHelpId);
                } else {
                    PlatformUI.getWorkbench().getHelpSystem().displayHelp(ViewsPlugin.PLUGIN_ID + ".algorithmsView"); //$NON-NLS-1$
                }
                return;
            }
        }
    }

    private void loadAlgorithms() {
        CommandOrAction[] actions = OperationsPlugin.getDefault().getAlgorithmsManager()
                .getShadowAlgorithmActions();
        for (int i = 0, length = actions.length; i < length; i++) {
        	IAction action = actions[i].getAction();
            if (!algorithmTypes.contains(((ShadowAlgorithmAction) action))) {
                algorithmTypes.add(((ShadowAlgorithmAction) action));
            }
        }
    }
}