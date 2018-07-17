package org.jcryptool.visual.huffmanCoding.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.huffmanCoding.HuffmanCodingPlugin;
import org.jcryptool.visual.huffmanCoding.algorithm.Node;

/**
 * @author Miray Inel
 * @author <i>revised by</i>
 * @author Michael Altenhuber
 * 
 */
public class HuffmanCodingViewTree extends Composite implements IZoomableWorkbenchPart {

	private GraphViewer viewer;
	private StyledText styledTextTree;
	private ArrayList<GraphConnection> markedConnectionList;

	private IActionBars actionBar;
	private int layoutCounter = 1;

	public HuffmanCodingViewTree(Composite parent, int style, ArrayList<Node> nodes, IActionBars actionBar) {
		super(parent, style);
		this.actionBar = actionBar;

		this.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				viewer.applyLayout();
			}
		});
		this.setLayout(new GridLayout(1, false));

		styledTextTree = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);

		GridData gd_styledTextTree = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_styledTextTree.heightHint = 40;
		styledTextTree.setLayoutData(gd_styledTextTree);
		styledTextTree.setText(Messages.ZestLabelProvider_4);

		viewer = new GraphViewer(this, SWT.NONE);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider());
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		markedConnectionList = new ArrayList<GraphConnection>();

		Control control = viewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		viewer.setInput(nodes);
		LayoutAlgorithm layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();
		fillToolBar();

		Graph graph = viewer.getGraphControl();
		graph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof GraphNode) {
					GraphNode node = (GraphNode) e.item;
					Node n = (Node) node.getData();

					if (n.isLeaf()) {
						styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
						styledTextTree.setFont(FontService.getHugeFont());
						styledTextTree.setText(
								Messages.ZestLabelProvider_5 + " '" + n.getNameAsString() + "': " + n.getCode()); //$NON-NLS-1$ //$NON-NLS-2$

						if (markedConnectionList.size() == 0) {
							markBranch(node);
						} else {
							unmarkBranch(markedConnectionList);
							markedConnectionList.clear();
							markBranch(node);
						}
					} else {
						if (markedConnectionList.size() != 0) {
							unmarkBranch(markedConnectionList);
							markedConnectionList.clear();
						}
						styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
						styledTextTree.setAlignment(SWT.LEFT);
						styledTextTree.setFont(FontService.getNormalFont());
						styledTextTree.setText(Messages.ZestLabelProvider_4);
					}

				}
			}
		});

	}

	/**
	 * Marks the whole branch beginning from the leaf node
	 * 
	 * @param leaf
	 *            - the leaf node of the branch
	 */
	@SuppressWarnings("unchecked")
	private void markBranch(GraphNode leaf) {
		ArrayList<GraphItem> items = new ArrayList<GraphItem>();

		GraphConnection connection = (GraphConnection) leaf.getTargetConnections().get(0);

		connection.setLineColor(viewer.getGraphControl().DARK_BLUE);
		connection.getSource().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
		connection.getDestination().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);

		items.add(connection.getSource());
		items.add(connection.getDestination());

		markedConnectionList.add(connection);
		List<GraphConnection> l = connection.getSource().getTargetConnections();

		while (l.size() != 0) {
			connection = (GraphConnection) connection.getSource().getTargetConnections().get(0);
			connection.setLineColor(viewer.getGraphControl().DARK_BLUE);
			connection.getSource().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
			connection.getDestination().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);

			items.add(connection.getSource());
			items.add(connection.getDestination());

			markedConnectionList.add(connection);

			l = connection.getSource().getTargetConnections();
		}

		viewer.getGraphControl().setSelection(items.toArray(new GraphItem[items.size()]));
	}

	/**
	 * Unmark a previous marked branch
	 * 
	 * @param markedConnectionList
	 *            - Contains marked elements
	 */
	private void unmarkBranch(List<GraphConnection> markedConnectionList) {
		for (GraphConnection connection : markedConnectionList) {
			connection.setLineColor(ColorConstants.lightGray);
			connection.getSource().setBackgroundColor(viewer.getGraphControl().LIGHT_BLUE);
			Node leaf = (Node) connection.getDestination().getData();
			if (leaf.isLeaf()) {
				connection.getDestination().setBackgroundColor(ColorConstants.lightGreen);
			} else {
				connection.getDestination().setBackgroundColor(viewer.getGraphControl().LIGHT_BLUE);
			}
		}
	}

	public void highlightNode(String nodeValue) {
		// int selection = table.getSelectionIndex();

		// TableItem tmpItem = table.getItem(selection);
		// String code;
		// if (modus.compareTo("COMPRESS") == 0) {
		// code = tmpItem.getText(2);
		// } else {
		// code = tmpItem.getText(1);
		// }
		// code = tmpItem.getText(1);
		GraphNode graphNode = null;

		@SuppressWarnings("unchecked")
		List<GraphNode> graphNodeList = viewer.getGraphControl().getNodes();
		for (GraphNode gn : graphNodeList) {
			Node n = (Node) gn.getData();
			if (n.isLeaf() && n.getCode().compareTo(nodeValue) == 0) {
				graphNode = gn;
				styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
				styledTextTree.setFont(FontService.getHugeFont());
				styledTextTree.setText(Messages.ZestLabelProvider_5 + " '" + n.getNameAsString() + "': " //$NON-NLS-1$ //$NON-NLS-2$
						+ n.getCode());
				break;
			}
		}

		if (!markedConnectionList.isEmpty()) {
			unmarkBranch(markedConnectionList);
		}
		markBranch(graphNode);

	}

	/**
	 * Change the layout of the huffman tree
	 * 
	 * @see org.jcryptool.visual.huffmanCoding/src/org/jcryptool/visual/huffmanCoding/handlers/ChangeLayout.java
	 */
	public void setLayoutManager() {

		if (!viewer.getControl().isDisposed()) {

			switch (layoutCounter) {
			case 1:
				viewer.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
						true);
				viewer.applyLayout();

				layoutCounter++;
				break;
			case 2:
				viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				viewer.applyLayout();

				layoutCounter++;
				break;
			case 3:
				viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				viewer.applyLayout();

				layoutCounter = 1;
				break;
			}
		}
	}

	/**
	 * Initialize the graph tool bar including layout and zooming
	 */
	private void fillToolBar() {
		if (actionBar == null)
			return;

		int layoutID = 0;
		String itemID = "";

		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		toolbarZoomContributionViewItem.setId("org.jcryptool.visual.huffmanCoding.command.Zoom");

		IContributionItem[] items = actionBar.getToolBarManager().getItems();

		if (items[0] != null)
			itemID = items[0].getId();

		//Create a separator pipe to visible divide the graph operations
		Separator barSeparator = new Separator();
		barSeparator.setId("org.jcryptool.visual.huffmanCoding.command.Separator");

		//Get the layout icon
		for (int i = 0; i < items.length; ++i) {
			if (items != null && items[i].getId().equals("org.jcryptool.visual.huffmanCoding.command.ChangeLayout"))
				layoutID = i;
		}

		actionBar.getToolBarManager().remove(items[layoutID].getId());

		//Shuffle the zoom and layout icon to the left of the separator
		try {
			actionBar.getToolBarManager().insertBefore(itemID, barSeparator);
			actionBar.getToolBarManager().insertBefore(barSeparator.getId(), toolbarZoomContributionViewItem);
			actionBar.getToolBarManager().insertBefore(barSeparator.getId(), items[layoutID]);
		} catch (IllegalArgumentException e) {
			LogUtil.logError(HuffmanCodingPlugin.PLUGIN_ID,
					"Could not find ToolBar item in list which should be there - Check Plugin Extensions for command ChangeLayout");
		}

		actionBar.updateActionBars();
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

}
