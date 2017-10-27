package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.jcryptool.visual.aco.ACOPlugin;
import org.jcryptool.visual.aco.model.CommonModel;

public class AntColResultComposite extends Composite {
	private Text currentText;
	private Text bestText;
	private Label currAntNolabel;
	private Text currentTrail;
	private Text bestTrail;
	private Group resultGroup;
	private Group bestGroup;
	private StackLayout stackLayout;
	private Composite emptyTextComp;
	private Composite resultComp;

	public AntColResultComposite(CommonModel model, Composite c) {

		super(c, SWT.NONE);
		this.setLayout(new GridLayout(2, false));
		resultGroup = new Group(this, SWT.NONE);
		resultGroup.setText(Messages.Viusal_ResultGroup);
		resultGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		stackLayout = new StackLayout();
		resultGroup.setLayout(stackLayout);

		emptyTextComp = new Composite(resultGroup, SWT.NONE);
		emptyTextComp.setLayout(new GridLayout(1, false));
		emptyTextComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		resultComp = new Composite(resultGroup, SWT.NONE);
		resultComp.setLayout(new GridLayout(1, false));
		resultComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label emptyText1 = new Label(emptyTextComp, SWT.NONE);
		emptyText1.setText(Messages.Result_emptyText1);
		emptyText1.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true,
				true));
		Label emptyText2 = new Label(emptyTextComp, SWT.NONE);
		emptyText2.setText(Messages.Result_emptyText2);
		emptyText2.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true));
		setEmptyText();
	}

	public void setEmptyText(){
		stackLayout.topControl = emptyTextComp;
		resultGroup.layout();
	}
	
	public void setResultContainer(){
		stackLayout.topControl = resultComp;
		resultGroup.layout();
	}
	public void initComponent() {

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		GridData dataLabel = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1);
		currAntNolabel = new Label(resultComp, SWT.NONE);
		currAntNolabel.setLayoutData(data);
		Group currGroup = new Group(resultComp, SWT.NONE);
		currGroup.setText(Messages.Viusal_CurrAntGroup);
		currGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		currGroup.setLayout(new GridLayout(2, false));

		Label label = new Label(currGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_currEncryptionLbl); //$NON-NLS-1$ //$NON-NLS-2$

		
		currentText = new Text(currGroup, SWT.BORDER);
		currentText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		currentText.setEditable(false);
		CLabel help = new CLabel(currGroup, SWT.NONE);
		help.setImage(ACOPlugin.getImageDescriptor("platform:/plugin/org.eclipse.ui/icons/full/etool16/help_contents.png").createImage());
		
		final ToolTip tip = new ToolTip(currGroup.getShell(), SWT.BALLOON);
		tip.setMessage(Messages.Result_description);
		help.addListener(SWT.MouseDown, new Listener(){
			@Override
			public void handleEvent(Event event) {
				tip.setVisible(true);
			}});

		label = new Label(currGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_currTrailLbl); //$NON-NLS-1$ //$NON-NLS-2$

		currentTrail = new Text(currGroup, SWT.BORDER);
		currentTrail.setLayoutData(data);
		currentTrail.setEditable(false);
		
		bestGroup = new Group(resultComp, SWT.NONE);
		bestGroup.setText(Messages.Viusal_BestAntGroup);
		bestGroup.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
		bestGroup.setLayout(new GridLayout(2, false));

		label = new Label(bestGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_bestEncryptionLbl); //$NON-NLS-1$ //$NON-NLS-2$

		bestText = new Text(bestGroup, SWT.BORDER);
		bestText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bestText.setEditable(false);
		
		help = new CLabel(bestGroup, SWT.NONE);
		help.setImage(ACOPlugin.getImageDescriptor("platform:/plugin/org.eclipse.ui/icons/full/etool16/help_contents.png").createImage());
		help.addListener(SWT.MouseDown, new Listener(){
			@Override
			public void handleEvent(Event event) {
				tip.setVisible(true);
			}});

		label = new Label(bestGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_currTrailLbl); //$NON-NLS-1$ //$NON-NLS-2$

		bestTrail = new Text(bestGroup, SWT.BORDER);
		bestTrail.setLayoutData(data);
		bestTrail.setEditable(false);

		resultComp.layout();
	}

	public void setResultText(String curResult, String curTrail,
			String bestResult, String besttrail, int currAntNo) {
		if (currentText == null) {
			initComponent();
		}
		currentText.setText(curResult.toUpperCase()); //$NON-NLS-1$
		bestText.setText(bestResult.toUpperCase()); //$NON-NLS-1$
		bestTrail.setText(besttrail);
		currentTrail.setText(curTrail);
		currAntNolabel.setText(Messages.Show_decryptedByAnt1 + //$NON-NLS-1$
				" " + currAntNo); //$NON-NLS-1$ //$NON-NLS-2$
		bestGroup.setVisible(currAntNo >= 2);

		layout();
	}
}
