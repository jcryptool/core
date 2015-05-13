// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2014 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmHandler;
import org.jcryptool.core.views.content.PaletteView;
import org.jcryptool.core.views.content.TreeView;

/**
 * Displays all available algorithms in a tree or palette view.
 *
 * @author t-kern
 * @author Holger Friedrich (support for Commands)
 * @version 0.9.3
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

    private static ArrayList<ShadowAlgorithmHandler> algorithmTypes = new ArrayList<ShadowAlgorithmHandler>();

    final static String showPaletteCommandId = ViewsPlugin.PLUGIN_ID + ".commands.showPalette";
    final static String showTreeCommandId = ViewsPlugin.PLUGIN_ID + ".commands.showTree";
    
    private ICommandService commandService;
    private Category autogeneratedCategory;
    private IServiceLocator serviceLocator;

    AbstractHandler showPaletteHandler;
    AbstractHandler showTreeHandler;

    CommandContributionItem showPaletteContributionItem;
    CommandContributionItem showTreeContributionItem;
    
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

    private void defineCommand(final String commandId, final String name, AbstractHandler handler) {
    	Command command = commandService.getCommand(commandId);
    	command.define(name,  null, autogeneratedCategory);
    	command.setHandler(handler);
    }

    private CommandContributionItem createContributionItem(final String commandId,
        	final ImageDescriptor icon, final String tooltip)
        {
        	CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator,
        		null, commandId, SWT.PUSH);
        	if(icon != null)
        		param.icon = icon;
        	if(tooltip != null && !tooltip.equals(""))
        		param.tooltip = tooltip;
        	CommandContributionItem item = new CommandContributionItem(param);
        	return(item);
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

        // create the command to switch the views
        // This may be a little tricky to translate to Commands.
        // The Action seems to change its own text, tool tip and icon when it is invoked...
        // Perhaps we should create two CommandContributionItems and switch between them
        // as the view style is switched from palette to tree and vice versa.
        showPaletteHandler = new AbstractHandler() {
        	public Object execute(ExecutionEvent event) {
                layout.topControl = paletteView.getControl();
                PlatformUI.getPreferenceStore().setValue("showView", VIEW_PALETTE); //$NON-NLS-1$
                listing.layout();
                IMenuManager manager = getViewSite().getActionBars().getMenuManager();
                manager.removeAll();
                manager.add(showTreeContributionItem);
                return(null);
        	}
        };
        showTreeHandler = new AbstractHandler() {
        	public Object execute(ExecutionEvent event) {
                layout.topControl = treeView.getControl();
                PlatformUI.getPreferenceStore().setValue("showView", VIEW_TREE); //$NON-NLS-1$
                listing.layout();
                IMenuManager manager = getViewSite().getActionBars().getMenuManager();
                manager.removeAll();
                manager.add(showPaletteContributionItem);
        		return(null);
        	}
        };
        
        serviceLocator = PlatformUI.getWorkbench();
        commandService = (ICommandService)serviceLocator.getService(ICommandService.class);
        autogeneratedCategory = commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID);

        defineCommand(showPaletteCommandId, Messages.AlgorithmView_showPalette, showPaletteHandler);
        defineCommand(showTreeCommandId, Messages.AlgorithmView_showTree, showTreeHandler);
        
        showPaletteContributionItem = createContributionItem(showPaletteCommandId,
        		ViewsPlugin.getImageDescriptor("icons/switch.gif"), Messages.AlgorithmView_showPalette);
        showTreeContributionItem = createContributionItem(showTreeCommandId,
        		ViewsPlugin.getImageDescriptor("icons/switch.gif"), Messages.AlgorithmView_showTree);
        
        // set the correct view to be shown and assign the corresponding action
        IMenuManager manager = getViewSite().getActionBars().getMenuManager();
        switch (PlatformUI.getPreferenceStore().getInt("showView")) { //$NON-NLS-1$
            case VIEW_PALETTE:
                layout.topControl = paletteView.getControl();
                manager.add(showTreeContributionItem);
                break;
            default:
                layout.topControl = treeView.getControl();
                manager.add(showPaletteContributionItem);
        }
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
        Iterator<ShadowAlgorithmHandler> it9 = algorithmTypes.iterator();
        ShadowAlgorithmHandler handler = null;
        while (it9.hasNext()) {
            handler = it9.next();
            // TODO the text property doesn't belong in the Handler but in the CommandInfo
            if (algorithmName.equals(handler.getText())) {
            	// TODO maybe Command.executeWithChecks() should be called here, which also requires an ExecutionEvent
                handler.execute(null);
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
        CommandInfo[] commands = OperationsPlugin.getDefault().getAlgorithmsManager()
                .getShadowAlgorithmCommands();
        for (int i = 0, length = commands.length; i < length; i++) {
        	AbstractHandler handler = commands[i].getHandler();
            if (!algorithmTypes.contains(((ShadowAlgorithmHandler) handler))) {
                algorithmTypes.add(((ShadowAlgorithmHandler) handler));
            }
        }
        // This didn't quite support the case when we have a Handler rather than an Action in the CommandOrAction.
        // I think I now added Handler support.
        // TODO Is this loadAlgorithms() method used anywhere?
    }
}