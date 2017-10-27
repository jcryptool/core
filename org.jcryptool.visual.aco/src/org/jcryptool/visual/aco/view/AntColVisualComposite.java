package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolTip;
import org.jcryptool.visual.aco.ACOPlugin;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.CommonModel;

public class AntColVisualComposite extends Composite {

	private StackLayout stackLayout;
	private AntColGraphComposite graphComp;
	private Button selectGraph;
	private Button selectMatrix;
	private AntColPherMatrixComposite matrixComp;
	private Composite visualComp;
	private CommonModel model;
	private AntColEventController controller;

	public AntColVisualComposite(final CommonModel model, Composite c) {
		super(c, SWT.NONE);
		this.model = model;
		this.setLayout(new GridLayout(1, false));
		Group visualGroup = new Group(this, SWT.NONE);

		visualGroup.setText(Messages.Visual_GraphMatrixGroupTitle);
		visualGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		visualGroup.setLayout(new GridLayout(1, false));

		selectGraph = new Button(visualGroup, SWT.RADIO);
		selectGraph
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		selectGraph.setText(Messages.Visual_graphVisualRadio); //$NON-NLS-1$
		selectGraph.setSelection(true);
		selectGraph.setEnabled(false);
		selectGraph.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onSelectShowGraph();
			}
		});

		selectMatrix = new Button(visualGroup, SWT.RADIO);
		selectMatrix
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		selectMatrix.setText(Messages.Visual_matrixVisualRadio); //$NON-NLS-1$
		selectMatrix.setEnabled(false);
		selectMatrix.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onSelectShowMatrix();
			}
		});
		stackLayout = new StackLayout();

		visualComp = new Composite(visualGroup, SWT.NONE);
		visualComp.setLayout(stackLayout);
		visualComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		graphComp = new AntColGraphComposite(model, visualComp);
		graphComp.setLayout(new GridLayout(1, false));

		stackLayout.topControl = graphComp;

		matrixComp = new AntColPherMatrixComposite(model, visualComp);
		matrixComp.setLayout(new GridLayout(1, false));

		CLabel help = new CLabel(visualGroup, SWT.NONE);
		help.setImage(ACOPlugin.getImageDescriptor("platform:/plugin/org.eclipse.ui/icons/full/etool16/help_contents.png").createImage());
		final ToolTip tip = new ToolTip(visualGroup.getShell(), SWT.BALLOON);
		tip.setMessage(Messages.Result_description);
		help.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(selectGraph.getSelection()){
					tip.setMessage(Messages.graph_probHintLbl);
				} else {
					tip.setMessage(Messages.PherMatrix_description);
				}
				tip.setVisible(true);
			}
		});

		help.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
	}

	public void animationStep() {
		graphComp.animationStep();
	}

	public void activateRadioButtons() {
		selectMatrix.setEnabled(model.isVisualizable());
		selectGraph.setEnabled(model.isVisualizable());
	}

	public AntColGraphComposite getGraphComp() {
		return graphComp;
	}

	public AntColPherMatrixComposite getMatrixComp() {
		return matrixComp;
	}

	public void setEnabledRadioGroup(boolean enabled) {
		selectGraph.setEnabled(enabled);
		selectMatrix.setEnabled(enabled);
	}

	public void setSelectionShowGraphRadio(boolean enabled) {
		selectGraph.setSelection(enabled);
		selectMatrix.setSelection(!enabled);
	}

	public void setTopContolStackLayout(Composite comp) {
		stackLayout.topControl = comp;
		visualComp.layout();
	}

	public Composite getCurrentDisplyedComp() {
		return (Composite) stackLayout.topControl;
	}

	public void addController(AntColEventController reg) {
		this.controller = reg;
		graphComp.addController(reg);
		matrixComp.addController(reg);
	}
}
