// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * 
 * @author Sebastian Ranftl
 *
 */
public class SphincsPlusTreeView extends Composite {

    private Graph graph;

    private GraphViewer graphViewer;

    private Composite zestComposite;
    private Composite expandComposite;
    private Composite sphincsPlusTreeView;

    private Text descLabel;

    private boolean distinctListener = false;
    private boolean mouseDragging;

    private Point oldMouse;
    private Point newMouse;
    private org.eclipse.draw2d.geometry.Point viewLocation;

    private int differenceMouseX;
    private int differenceMouseY;

    private Display curDisplay;

    private ExpandBar descriptionExpander;

    private StyledText descText;
    private StyledText styledTextTree;

    /**
     * 
     * @param parent
     * @param style
     * @param sphincsPlusView
     */
    public SphincsPlusTreeView(Composite parent, int style, SphincsPlusView sphincsPlusView) {
        super(parent, style);
        this.setLayout(new GridLayout(2, true));
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));
        curDisplay = getDisplay();
        sphincsPlusTreeView = this;

        descLabel = new Text(this, SWT.READ_ONLY | SWT.CURSOR_ARROW);
        descLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
        descLabel.setText(Messages.SphincsPlusTreeView_descLabel);

        descriptionExpander = new ExpandBar(this, SWT.NONE);
        descriptionExpander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        ExpandItem collapsablePart = new ExpandItem(descriptionExpander, SWT.NONE, 0);

        expandComposite = new Composite(descriptionExpander, SWT.NONE);
        GridLayout expandLayout = new GridLayout(2, true);
        expandComposite.setLayout(expandLayout);

        descText = new StyledText(expandComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        descText.setText(Messages.SphincsPlusTreeView_descText_general);

        styledTextTree = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        int preferredHeight = descText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        collapsablePart.setText(Messages.SphincsPlusTreeView_collapsedPart_expanded);
        collapsablePart.setExpanded(true);
        collapsablePart.setControl(expandComposite);
        collapsablePart.setHeight(preferredHeight + 60);
        descriptionExpander.setBackground(curDisplay.getSystemColor(SWT.COLOR_WHITE));

        zestComposite = new Composite(this, SWT.DOUBLE_BUFFERED | SWT.BORDER);
        zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        zestComposite.setLayout(new GridLayout());
        zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        zestComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));

        // create GraphViewer and Graph
        graphViewer = new GraphViewer(zestComposite, SWT.V_SCROLL | SWT.H_SCROLL);
        graph = graphViewer.getGraphControl();
        graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
        // graph.setScrollBarVisibility(FigureCanvas.NEVER);
        graphViewer.getControl().forceFocus();
        graphViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

        // Adding graph nodes and connect them
        GraphNode node_1 = new GraphNode(graph, SWT.NONE, "PK.root (Layer d)");
        GraphContainer container_1 = new GraphContainer(graph, SWT.NONE, "XMSS (Layer d-1)");
        GraphContainer container_2 = new GraphContainer(graph, SWT.NONE, "XMSS (Layer d-2 = layer 0)");
        GraphContainer container_3 = new GraphContainer(graph, SWT.NONE, "XMSS (Layer d-2 = layer 0)");
        GraphContainer container_4 = new GraphContainer(graph, SWT.NONE, "XMSS (Layer d-2 = layer 0)");
        GraphContainer container_5 = new GraphContainer(graph, SWT.NONE, "XMSS (Layer d-2 = layer 0)");
        GraphNode node_2 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_3 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_4 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_5 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_6 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_7 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_8 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_9 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_10 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_11 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_12 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_13 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_14 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_15 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_16 = new GraphNode(graph, SWT.NONE, "FORS-Tree");
        GraphNode node_17 = new GraphNode(graph, SWT.NONE, "FORS-Tree");

        // Setting sizes and colors
        node_1.setSize(150, 40);
        node_1.setBackgroundColor(ColorConstants.orange);
        container_1.setBackgroundColor(ColorConstants.orange);
        container_2.setBackgroundColor(ColorConstants.orange);
        container_3.setBackgroundColor(ColorConstants.orange);
        container_4.setBackgroundColor(ColorConstants.orange);
        container_5.setBackgroundColor(ColorConstants.orange);
        node_2.setBackgroundColor(ColorConstants.lightGreen);
        node_3.setBackgroundColor(ColorConstants.lightGreen);
        node_4.setBackgroundColor(ColorConstants.lightGreen);
        node_5.setBackgroundColor(ColorConstants.lightGreen);
        node_6.setBackgroundColor(ColorConstants.lightGreen);
        node_7.setBackgroundColor(ColorConstants.lightGreen);
        node_8.setBackgroundColor(ColorConstants.lightGreen);
        node_9.setBackgroundColor(ColorConstants.lightGreen);
        node_10.setBackgroundColor(ColorConstants.lightGreen);
        node_11.setBackgroundColor(ColorConstants.lightGreen);
        node_12.setBackgroundColor(ColorConstants.lightGreen);
        node_13.setBackgroundColor(ColorConstants.lightGreen);
        node_14.setBackgroundColor(ColorConstants.lightGreen);
        node_15.setBackgroundColor(ColorConstants.lightGreen);
        node_16.setBackgroundColor(ColorConstants.lightGreen);
        node_17.setBackgroundColor(ColorConstants.lightGreen);

        // Creating SubTrees
        createXmssSubTree(container_1);
        createXmssSubTree(container_2);
        createXmssSubTree(container_3);
        createXmssSubTree(container_4);
        createXmssSubTree(container_5);

        // Making all connections
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node_1, container_1);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_1, container_2);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_1, container_3);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_1, container_4);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_1, container_5);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_2, node_2);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_2, node_3);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_2, node_4);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_2, node_5);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_3, node_6);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_3, node_7);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_3, node_8);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_3, node_9);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_4, node_10);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_4, node_11);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_4, node_12);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_4, node_13);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_5, node_14);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_5, node_15);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_5, node_16);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, container_5, node_17);

        // Reload TreeLayout
        // graphViewer.applyLayout();

        /*
         * This graphViewer consists of 2 components: the control and the graph (Figure) We want to
         * give the control a size by the layout and the graph a custom, bigger value. For the
         * control (graphViewer.getControl) I simply grab all available space
         */
        GridDataFactory.fillDefaults().grab(true, true).applyTo(graphViewer.getControl());

        // For the graph we have to create a PaintListener.
        graphViewer.getControl().addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {
                Point currentShellSize;
                currentShellSize = parent.getSize();
                double x, y;
                x = currentShellSize.x;
                y = currentShellSize.y * 1.4;
                graph.getViewport().setSize((int) x, (int) y);
            }

        });
        // The Graph now fills the shell/parent composite,
        // but the actual graph size can be set as we want in the paint

        graph.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                if (e.item == node_1) {
                    styledTextTree.setText(
                            "SPHINCS+ public key = HT public key is the public key (root node) of the single XMSS tree on the top layer");
//                    descText.setText(Messages.SphincsPlusTreeView_descText_root_node_d);
                } else if ((e.item == container_1) || (e.item == container_2) || (e.item == container_3)
                        || (e.item == container_4) || (e.item == container_5)) {
                    styledTextTree.setText("XMSS public key = root of the tree");
//                    descText.setText(Messages.SphincsPlusTreeView_descText_root_node_XMSS);
                } else {
                    styledTextTree.setText("");
//                    descText.setText(Messages.SphincsPlusTreeView_descText_general);
                }
            }
        });

        graph.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    if ((e.item == node_2) || (e.item == node_3) || (e.item == node_4) || (e.item == node_5)
                            || (e.item == node_6) || (e.item == node_7) || (e.item == node_8) || (e.item == node_9)
                            || (e.item == node_10) || (e.item == node_11) || (e.item == node_12) || (e.item == node_13)
                            || (e.item == node_14) || (e.item == node_15) || (e.item == node_16)
                            || (e.item == node_17)) {

                        sphincsPlusView.getSphincsPlusForsView();
                        sphincsPlusView.setFocusOnSphincsPlusForsView(true);
                    }

                } catch (Exception ex) {
                    LogUtil.logError(ex);
                }
            }
        });

        // Zooming with the class org.eclipse.gef.editparts.ZoomManager
        // As arguments we need a ScalableFigure which we receive by graph.getRootLayer and the
        // Viewport.
        ZoomManager zoomManager = new ZoomManager(graph.getRootLayer(), graph.getViewport());

        // we bind the zoom mechanic to a simple mouse wheel listener
        graph.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseScrolled(MouseEvent e) {
                if (e.count < 0) {
                    zoomManager.zoomOut();
                } else {
                    zoomManager.zoomIn();
                }
            }
        });

        // Camera Movement
        MouseListener dragQueen = new MouseListener() {

            @Override
            public void mouseUp(MouseEvent e) {
                distinctListener = false;
                mouseDragging = false;
                zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_CROSS));
            }

            @Override
            public void mouseDown(MouseEvent e) {
                mouseDragging = true;
                oldMouse = Display.getCurrent().getCursorLocation();
                viewLocation = graph.getViewport().getViewLocation();

                if (distinctListener == false)
                    zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        while (mouseDragging) {
                            if (distinctListener == false)
                                updateViewLocation();
                            try {
                                Thread.sleep(2);

                            } catch (InterruptedException e) {
                            }
                        }
                    }
                };
                new Thread(runnable).start();
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                // do nothing
            }
        };

        graphViewer.getGraphControl().addMouseListener(dragQueen);
        zestComposite.addMouseListener(dragQueen);

        // We give the focus to our graphViewer, so it receives the MouseWheel Events
        graphViewer.getControl().forceFocus();

        // Makes the nodes fixed (they cannot be dragged around with the mouse
        // by overriding the mouseMovedListener with empty event
        graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
            @Override
			public void dispatchMouseMoved(MouseEvent e) {
            }

        });

        /**
         * Opening and closing the description expander Expand to the height of the text
         */
        descriptionExpander.addExpandListener(new ExpandListener() {
            @Override
            public void itemExpanded(ExpandEvent e) {
                curDisplay.asyncExec(() -> {
                    collapsablePart.setHeight(preferredHeight + 60);
                    descriptionExpander.pack();
                    sphincsPlusTreeView.layout();
                    collapsablePart.setText(Messages.SphincsPlusTreeView_collapsedPart_expanded);
                });
            }

            @Override
            public void itemCollapsed(ExpandEvent e) {
                curDisplay.asyncExec(() -> {
                    descriptionExpander.pack();
                    sphincsPlusTreeView.layout();
                    collapsablePart.setText(Messages.SphincsPlusTreeView_collapsedPart_collapsed);
                });
            }
        });
    }

    /**
     * Sets the current view location based on mouse movement
     */
    private void updateViewLocation() {
        curDisplay.asyncExec(new Runnable() {

            @Override
            public void run() {
                newMouse = getDisplay().getCursorLocation();
                differenceMouseX = newMouse.x - oldMouse.x;
                differenceMouseY = newMouse.y - oldMouse.y;

                if (differenceMouseX != 0 || differenceMouseY != 0) {
                    if (mouseDragging) {
                        graph.getViewport().setViewLocation(viewLocation.x -= differenceMouseX,
                                viewLocation.y -= differenceMouseY);
                        oldMouse = newMouse;
                    }
                }
            }
        });
    }

    /**
     * Creates all the subtrees inside the XMSS Nodes
     * 
     * @param container The GraphContainer in which the tree should appear
     */
    private void createXmssSubTree(GraphContainer container) {
        container.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), false);
        GraphNode node_1 = new GraphNode(container, SWT.NONE, "Node");
        GraphNode node_2 = new GraphNode(container, SWT.NONE, "Node");
        GraphNode node_3 = new GraphNode(container, SWT.NONE, "WOTS+");
        GraphNode node_4 = new GraphNode(container, SWT.NONE, "WOTS+");
        GraphNode node_5 = new GraphNode(container, SWT.NONE, "WOTS+");
        GraphNode node_6 = new GraphNode(container, SWT.NONE, "WOTS+");
        GraphNode root = new GraphNode(container, SWT.NONE, "ROOT");

        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, root, node_1);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, root, node_2);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node_1, node_3);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node_1, node_4);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node_2, node_5);
        new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node_2, node_6);

        root.setBackgroundColor(ColorConstants.orange);
        container.open(true);
        container.applyLayout();
    }

}
