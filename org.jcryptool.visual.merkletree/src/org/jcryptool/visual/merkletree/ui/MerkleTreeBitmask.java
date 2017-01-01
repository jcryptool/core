package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;

/**
 * Class for the Composite with the Bitmask generation in Tabpage 1 Only used in
 * XMSS and XMSS^MT
 * 
 * @author Maximilian Lindpointner
 */
public class MerkleTreeBitmask extends Composite {

	Button buttonCreateSeed;
	Label createLabel;
	StyledText descText;
	Text textSeed;
	Label bitLable;
	Label randomgenerator;

	public MerkleTreeBitmask(Composite parent, int style, ViewPart masterView) {
		super(parent, style);

		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		createLabel = new Label(this, SWT.NONE);
		createLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
		Group group = new Group(this, SWT.NONE);
		group.setText(Descriptions.Tab0_Head5);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		group.setLayout(new GridLayout(1, true));
		group.setText(Descriptions.Tab0_Head3);

		descText = new StyledText(group, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		descText.setText(Descriptions.Tab0_Txt3);
		descText.setEditable(false);
		descText.setCaret(null);

		/*
		 * Bitmask Seed Label
		 */
		randomgenerator = new Label(group, SWT.NONE);
		randomgenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		randomgenerator.setText(Descriptions.Tab0_Head4);

		/*
		 * Textbox for seed
		 */
		textSeed = new Text(group, SWT.BORDER | SWT.CENTER);
		textSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		/*
		 * Button generate new Seed
		 */
		buttonCreateSeed = new Button(group, SWT.NONE);
		buttonCreateSeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		buttonCreateSeed.setText(Descriptions.Tab0_Button3);

		// createLabel.setText(Descriptions.Tab0_Head3);

	}

}
