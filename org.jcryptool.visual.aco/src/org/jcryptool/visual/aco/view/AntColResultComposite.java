package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
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
	private Label emptyText1;
	private Label emptyText2;
	private Group bestGroup;

	public AntColResultComposite(CommonModel model, Composite c) {

		super(c, SWT.NONE);
		this.setLayout(new GridLayout(2, false));
		resultGroup = new Group(this, SWT.NONE);
		resultGroup.setText(Messages.Viusal_ResultGroup);
		resultGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		resultGroup.setLayout(new GridLayout(1, false));

		emptyText1 = new Label(resultGroup, SWT.NONE);
		emptyText1.setText(Messages.Result_emptyText1);
		emptyText1.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true,
				true));
		emptyText2 = new Label(resultGroup, SWT.NONE);
		emptyText2.setText(Messages.Result_emptyText2);
		emptyText2.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true));
	}

	public void initComponent() {

		emptyText1.dispose();
		emptyText2.dispose();
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		GridData dataLabel = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1);
		data.heightHint = 18;
		currAntNolabel = new Label(resultGroup, SWT.NONE);
		currAntNolabel.setLayoutData(data);
		Group currGroup = new Group(resultGroup, SWT.NONE);
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
		help.setSize(15, 15);
		Image helpImage = ACOPlugin.getImageDescriptor("platform:/plugin/org.eclipse.ui/icons/full/etool16/help_contents.gif").createImage();
		help.setImage(helpImage);
		
		final ToolTip tip = new ToolTip(currGroup.getShell(), SWT.BALLOON);
		tip.setMessage(Messages.Result_description);
		help.addListener(SWT.MouseDown, new Listener(){
			@Override
			public void handleEvent(Event event) {
				tip.setVisible(true);
			}});

		Composite filler = new Composite(currGroup, SWT.NONE);
		GridData fillerData = new GridData(SWT.TOP, SWT.LEFT, false, false);
		fillerData.heightHint = 3;
		filler.setLayoutData(fillerData);

		label = new Label(currGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_currTrailLbl); //$NON-NLS-1$ //$NON-NLS-2$

		currentTrail = new Text(currGroup, SWT.BORDER);
		currentTrail.setLayoutData(data);
		currentTrail.setEditable(false);
		
		filler = new Composite(resultGroup, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, true, true));
		
		bestGroup = new Group(resultGroup, SWT.NONE);
		bestGroup.setText(Messages.Viusal_BestAntGroup);
		bestGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		bestGroup.setLayout(new GridLayout(2, false));

		label = new Label(bestGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_bestEncryptionLbl); //$NON-NLS-1$ //$NON-NLS-2$

		bestText = new Text(bestGroup, SWT.BORDER);
		bestText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bestText.setEditable(false);
		
		help = new CLabel(bestGroup, SWT.NONE);
		help.setSize(15, 15);
		help.setImage(helpImage);
		help.addListener(SWT.MouseDown, new Listener(){
			@Override
			public void handleEvent(Event event) {
				tip.setVisible(true);
			}});
		filler = new Composite(bestGroup, SWT.NONE);
		filler.setLayoutData(fillerData);

		label = new Label(bestGroup, SWT.NONE);
		label.setLayoutData(dataLabel);
		label.setText(Messages.Result_currTrailLbl); //$NON-NLS-1$ //$NON-NLS-2$

		bestTrail = new Text(bestGroup, SWT.BORDER);
		bestTrail.setLayoutData(data);
		bestTrail.setEditable(false);

		resultGroup.layout();
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
