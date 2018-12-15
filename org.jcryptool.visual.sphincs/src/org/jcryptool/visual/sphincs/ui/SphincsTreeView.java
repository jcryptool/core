//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.sphincs.SphincsDescriptions;
import org.jcryptool.visual.sphincs.algorithm.Node;
import org.jcryptool.visual.sphincs.algorithm.Signature;
import org.jcryptool.visual.sphincs.algorithm.SimpleNode;
import org.jcryptool.visual.sphincs.algorithm.aSPHINCS256;


/**
 * Class for the Composite of Tabpage "Sphincs Description View"
 * Depicts a Graph of the Sphincs-Hypertree and a description
 * 
 * @author Philipp Guggenberger
 *
 */
public class SphincsTreeView extends Composite {
   
    private Display curDisplay;                     // current display
    private Composite sphincsDescriptionView;       // current composite, this class
     
    // GUI
    private Label descLabel;
    private ExpandBar descriptionExpander;
    private StyledText descText;
    private Composite expandComposite;
    private StyledText txtElementInfo;
    private Composite treeComposite;
    
    // tree drawing
    private GraphViewer viewer;
    private Graph graph;
    private ArrayList<GraphConnection> markedConnectionList;
    private ArrayList<GraphNode> markedAuthpathList;
    private List<?> graphNodeRetriever;
 
    // Listener 
    private boolean distinctListener = false;
    
    // Mouse-Listener
    private boolean mouseDragging;
    private Point oldMouse;
    private Point newMouse;
    private int differenceMouseX;
    private int differenceMouseY;
    private org.eclipse.draw2d.geometry.Point viewLocation;
    private ZoomManager zoomManager;
       
    // Color tree
    private Runnable currentlyHighlighted;
    private Runnable highlightedAuthpath;
    private int allColorNodeSize;
    private GraphNode[] nodes;
    private GraphNode[] horstNodes;
    private Color distinguishableColors[];
      
    // generate tree
    private ArrayList<Node> tree = new ArrayList<Node>();
    private ArrayList<Node> leaves = new ArrayList<Node>();
 
    private int singleTreeHeight;
    private int treeLayer;
    private String[] treeData;
     
    
    public SphincsTreeView(Composite parent, int style, ViewPart masterView, aSPHINCS256 sphincs, Signature signature) {
        super(parent, style);
        this.setLayout(new GridLayout(2, true));
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, SphincsConstant.DESC_HEIGHT + 1));
        curDisplay = getDisplay();
        sphincsDescriptionView = this;
        setTreeLayer(12);
        setSingleTreeHeight(5);
        treeData = sphincs.getPath(signature);
    
        // ***********************************
        // Begin of GUI elements
        // ***********************************
        
        // Sphincs-Tree Label (ganz oben)
        descLabel = new Label(this, SWT.NONE);
        descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, SphincsConstant.H_SPAN_MAIN, 1));
        descLabel.setText(SphincsDescriptions.SphincsDecription_Label_0);
        
        // Beschreibung der SPHINCS-Struktur
        descriptionExpander = new ExpandBar(this, SWT.NONE);
        descriptionExpander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        descriptionExpander.setFont(FontService.getLargeFont());
        ExpandItem collapsablePart = new ExpandItem(descriptionExpander, SWT.NONE, 0);
        
        // Ausgeklappter Teil, von  descriptionExpander
        expandComposite = new Composite(descriptionExpander, SWT.NONE);
        GridLayout expandLayout = new GridLayout(2, true);
        expandComposite.setLayout(expandLayout);
        
        // 1.Beschreibung
        descText = new StyledText(expandComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        descText.setText(SphincsDescriptions.SphincsDecription_Text_0);

        // Info s zum ausgewählten Knoten/Blatt, etc
        txtElementInfo = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        txtElementInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        txtElementInfo.setText(SphincsDescriptions.SphincsDecription_Text_1);

         // Beschreibung ein/ausblenden
        int preferredHeight = descText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        collapsablePart.setText(SphincsDescriptions.SphincsDecription_Button_1);
        collapsablePart.setControl(expandComposite);
        collapsablePart.setHeight(preferredHeight + 60);
        descriptionExpander.setBackground(curDisplay.getSystemColor(SWT.COLOR_WHITE));
            
        // Composite in der, der Baum gezeichnet wird
        treeComposite = new Composite(this, SWT.DOUBLE_BUFFERED | SWT.BORDER);
        treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        treeComposite.setLayout(new GridLayout());
        treeComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        treeComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        treeComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));
        
        // ***********************************
        // End of GUI elements
        // ***********************************  
        // ***********************************
        // Begin of the graph
        // ***********************************
  
        
        viewer = new GraphViewer(treeComposite, SWT.V_SCROLL | SWT.H_SCROLL);
        viewer.setContentProvider(new ZestNodeContentProvider()); 
        viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.orange));
        graph = viewer.getGraphControl();
        graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
        graph.setScrollBarVisibility(FigureCanvas.ALWAYS);  
        viewer.getControl().forceFocus();
        viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);     
        // generate SphincsTree
        viewer.setInput(getTree());
        viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        viewer.applyLayout();
        GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl()); 
        
        // Sets the size of the graph
        viewer.getControl().addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {
                Point currentShellSize= parent.getSize();
                
                double x=currentShellSize.x*2.1;                // x-Positionen of Graph in treeComposite 
                double y=currentShellSize.y*3.0;                // y-Positionen of Graph in treeComposite 
                           
               graph.getViewport().setSize((int) x, (int) y);
           }
        });
        
        // whole tree zoomed out
        ZoomManager startView =  new ZoomManager(graph.getRootLayer(), graph.getViewport());
        for (int i = 0; i< 2; i++) {
            startView.zoomOut();
        }
     
        graph.getViewport().setViewLocation(Integer.MAX_VALUE, 0);
        
        
        // color trees
        graphNodeRetriever = graph.getNodes();
        allColorNodeSize = graphNodeRetriever.size()-1;                
        nodes = new GraphNode[allColorNodeSize];// -1 <-- Public Key
        horstNodes = new GraphNode[2];
        colorizeMultitrees();
        
        
        // ***********************************
        // Beginning of many different Listeners
        // ***********************************
        
        markedConnectionList = new ArrayList<GraphConnection>();    // connectet Node-List
        markedAuthpathList = new ArrayList<GraphNode>();            // Authpath/way
 
        graph.addSelectionListener(new SelectionAdapter() {
            // displays the hash value of the tree
            @Override
            public void widgetSelected(SelectionEvent e) {
                distinctListener = true;
                if (e.item instanceof GraphNode) {
                    GraphNode node = (GraphNode) e.item;
                    Node n = (Node) node.getData();

                    if (n.isLeaf()) {
                        txtElementInfo.setForeground(new Color(null, new RGB(10, 70, 125)));
                        txtElementInfo.setText(SphincsDescriptions.ZestLabelProvider_5 + " " + n.getLeafNumber() + " = "+ n.getNameAsString()); // Inhalt des Knotes HORST
                                              
                        if (markedConnectionList.size() == 0) {
                            markBranch(node); 
                        } else {
                            unmarkBranch();
                            markBranch(node);
                        }
                    } else {
                        if (markedConnectionList.size() == 0) {
                            markBranch(node);
                        } else {
                            unmarkBranch();
                            markBranch(node);
                        }
                        txtElementInfo.setForeground(new Color(null, new RGB(0, 0, 0)));
                        txtElementInfo.setAlignment(SWT.LEFT);
                        txtElementInfo.setText(SphincsDescriptions.ZestLabelProvider_6 + " = " + n.getNameAsString());          // Inhalt des Knotens WOTS
                        
                    }
                }

                /* Deselects immediately to allow dragging */
                viewer.setSelection(new ISelection() {

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                });
            }
        });
        
        // ***********************************
        // Beginning of Camera Movement, Mouse Listener
        MouseListener dragQueen = new MouseListener() {

            @Override
            public void mouseUp(MouseEvent e) {
                distinctListener = false;
                mouseDragging = false;
                treeComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_CROSS));

            }

            @Override
            public void mouseDown(MouseEvent e) {
                mouseDragging = true;
                oldMouse = Display.getCurrent().getCursorLocation();
                viewLocation = graph.getViewport().getViewLocation();

                if (distinctListener == false)
                    treeComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        while (mouseDragging) {
                            if (distinctListener == false)
                                updateViewLocation();
                            try {
                                Thread.sleep(2);

                            } 
                            catch (InterruptedException e) {
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

        viewer.getGraphControl().addMouseListener(dragQueen);
        treeComposite.addMouseListener(dragQueen);

      
        // ***********************************
        // Beginning of MouseWheel zooming
        zoomManager = new ZoomManager(graph.getRootLayer(), graph.getViewport());
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
               
        // ***********************************
        // Beginning of "Fix Nodes"
        // Makes the nodes fixed (they cannot be dragged around with the mouse
        // by overriding the mouseMovedListener with empty event
        graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
            public void dispatchMouseMoved(MouseEvent e) {
            }

        });

        descriptionExpander.addExpandListener(new ExpandListener() {
            @Override
            public void itemExpanded(ExpandEvent e) {
                curDisplay.asyncExec(() -> {
                    collapsablePart.setHeight(preferredHeight + 60);
                    descriptionExpander.pack();
                    sphincsDescriptionView.layout();
                    collapsablePart.setText(SphincsDescriptions.SphincsDecription_Button_2);
                });
            }

            @Override
            public void itemCollapsed(ExpandEvent e) {
                curDisplay.asyncExec(() -> {
                    descriptionExpander.pack();
                    sphincsDescriptionView.layout();
                    collapsablePart.setText(SphincsDescriptions.SphincsDecription_Button_1);
                });
            }
        });
    }

    /**
     * Marks the whole branch beginning from the selected node
     * 
     * @param node
     *        any node of the graph
     */
    @SuppressWarnings("unchecked")
    private void markBranch(GraphNode node) {
        ArrayList<GraphItem> items = new ArrayList<GraphItem>();

        try {
            GraphConnection connection = (GraphConnection) node.getTargetConnections().get(0);

            connection.setLineColor(viewer.getGraphControl().DARK_BLUE);
            
            items.add(connection.getSource());
            items.add(connection.getDestination());

            markedConnectionList.add(connection);
            List<GraphConnection> l = connection.getSource().getTargetConnections();

            while (l.size() != 0) {
                connection = (GraphConnection) connection.getSource().getTargetConnections().get(0);
                connection.setLineColor(viewer.getGraphControl().DARK_BLUE);

                items.add(connection.getSource());
                items.add(connection.getDestination());

                markedConnectionList.add(connection);

                l = connection.getSource().getTargetConnections();
            }
        } catch (IndexOutOfBoundsException ex) {
            items.add(((GraphConnection) (node.getSourceConnections().get(0))).getSource());
        }

     
        
    }

    /**
     * Unmark a previous marked branch stored in the Lists markedConnectionList and
     * markedAuthpathList
     */
    private void unmarkBranch() {
        markedConnectionList.get(0).getDestination().setBorderWidth(0);
        
        for (GraphConnection connection : markedConnectionList) {
            connection.setLineColor(ColorConstants.lightGray);
            connection.getSource().setBorderWidth(0);
            
            // color the nodes back to light
            Node leaf = (Node) connection.getDestination().getData();
            if (leaf.isLeaf()) {
              connection.getDestination().setBorderWidth(0);
            }

        }
        for (GraphNode authNode : markedAuthpathList) {
            ((GraphConnection) authNode.getTargetConnections().get(0)).setLineColor(ColorConstants.lightGray);
            authNode.setBorderWidth(0);
        }
       
            
        if (currentlyHighlighted != null)
            getDisplay().timerExec(-1, currentlyHighlighted);
        if (highlightedAuthpath != null)
            getDisplay().timerExec(-1, highlightedAuthpath);
       
        markedConnectionList.clear();
        markedAuthpathList.clear();
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
                        graph.getViewport().setViewLocation(viewLocation.x -= differenceMouseX, viewLocation.y -= differenceMouseY);
                        oldMouse = newMouse;
                    }
                }

            }
        });
    }

    private void colorizeMultitrees() {
        int singleTreeHeight = getSingleTreeHeight();   
        int treeLayerCount = getTreeLayer();     
        
        // copy all graphnodes in nodes except the public key 
        for (int i = 0; i < allColorNodeSize+1; ++i) {
            if(i == 0 || i == 1) {
                horstNodes[i] = (GraphNode) graphNodeRetriever.get(i);
            }
            else {
                nodes[i-2] = (GraphNode) graphNodeRetriever.get(i);
            }     
        }
   
        // 7 colors
        distinguishableColors = new Color[7];
        distinguishableColors[0] = new Color(getDisplay(), 255, 255, 100);
        distinguishableColors[1] = new Color(getDisplay(), 155, 0, 155);
        distinguishableColors[2] = new Color(getDisplay(), 205, 180, 160);
        distinguishableColors[3] = new Color(getDisplay(), 0, 185, 185);
        distinguishableColors[4] = new Color(getDisplay(), 0, 185, 0);
        distinguishableColors[5] = new Color(getDisplay(), 140, 0, 0);
        distinguishableColors[6] = new Color(getDisplay(), 210, 105, 30);
        
        horstNodes[0].setBackgroundColor(distinguishableColors[6]); 
        horstNodes[1].setBackgroundColor(distinguishableColors[6]); 
     
        int colorIndex = 0;
        int sameColor = (singleTreeHeight*2); 
        
        // treeLayerCount-1 because horst its his own layer
        for(int i=0; i < treeLayerCount-1; i++) {
           for (int j = 0; j < sameColor; j++) {
               nodes[(i*sameColor)+j].setBackgroundColor(distinguishableColors[colorIndex]);                
           }
           colorIndex++;
           
           if(colorIndex > 6) {
               colorIndex =0;
           }
        }   
    }

    
    public ArrayList<Node> getTree() {
        generatSphincsTree();
 
        return tree;
    }

     // random leaf of horsttree 0-65335
    private void generateHorstLeaves(int leafIndex) {
        // SimpleNode(Content, leaf, index)
        leaves.add(new SimpleNode(treeData[0], true, leafIndex));
        leaves.add(new SimpleNode(treeData[1], true, leafIndex +1));
    }    
    
    private void generatSphincsTree() {  
        int treeIndex = 0;   
        
        generateHorstLeaves(treeIndex);
        
        int singleTreeHeight = getSingleTreeHeight();  
        int wholeTreeHeight = getTreeLayer(); // Layer 0,1,..
        
        tree = new ArrayList <Node>();
        tree.addAll(leaves);
        
        Node WotsUp, WotsRight, PublicKeyNode;
        
        for (int layerCount = 0; layerCount < wholeTreeHeight-1; layerCount++) {
            // single trees
            for (int index = 0; index < singleTreeHeight; index++, treeIndex += 2 ) {
                if(layerCount == 0 && index ==  0) { // index 0 ganz unten --> Horst aufbauen
                    // SimpleNode(content, left leaf, right leaf)
                    WotsUp = new SimpleNode(treeData[treeIndex], tree.get(treeIndex), tree.get(treeIndex + 1));
                    tree.add(WotsUp);
                    
                    // Connect, index (zb treeLayer 0 left, with 0 and right with 1 of tree)
                    tree.get(tree.size()-1).getConnectedTo().add(tree.get(treeIndex));
                    tree.get(tree.size()-1).getConnectedTo().add(tree.get(treeIndex + 1));
                    
                    // setParent-Node, make last element parent of                     
                    tree.get(treeIndex).setParent(tree.get(tree.size()-1));         
                    tree.get(treeIndex + 1).setParent(tree.get(tree.size()-1));
                    
                }
                else  {
                    // first add right "WOTS-leaf" 
                    WotsRight =  new SimpleNode(treeData[treeIndex+1], false, treeIndex +1 );
                    tree.add(WotsRight);
                        
                    // SimpleNode(content, left leaf, right leaf)
                    WotsUp = new SimpleNode(treeData[treeIndex+2], tree.get(treeIndex), tree.get(treeIndex + 1));
                    tree.add(WotsUp);
                        
                    // Connect, index (zb treeLayer 0 left, with 0 and right with 1 of tree)
                    tree.get(tree.size()-1).getConnectedTo().add(tree.get(treeIndex));
                    tree.get(tree.size()-1).getConnectedTo().add(tree.get(treeIndex + 1));
                        
                    // setParent-Node                    
                    tree.get(treeIndex).setParent(tree.get(tree.size()-1));         // zb treeIndex = 0 := leaf .setParent treeLevel := no leaf
                    tree.get(treeIndex + 1).setParent(tree.get(tree.size()-1));                  
                }    
            }  
        }
  
        PublicKeyNode = new SimpleNode(treeData[treeData.length-1], tree.get(treeIndex), null); 
        tree.add(PublicKeyNode);   
        tree.get(tree.size()-1).getConnectedTo().add(tree.get(treeIndex));          // connect element befor last element mit last one
        tree.get(treeIndex).setParent(tree.get(tree.size()-1));  
    }
    
    
    public void setTreeLayer(int treeLayer ) {
        this.treeLayer = treeLayer;
    }

    public void setSingleTreeHeight(int singleTreeHeight) {
        this.singleTreeHeight = singleTreeHeight; 
    }
    
    public int getTreeLayer() {
        return this.treeLayer;
    }

    public int getSingleTreeHeight() {
      return this.singleTreeHeight;
    }
    
}