package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

public class MerkleTreeWParam extends Composite {

	Button buttonSet4;
	Button buttonSet16;
	Label createLabel;
	Label titleLabel;
	StyledText createdKey;
	StyledText descText;
	private int wParameter = 16;

	/**
	 * Create the composite. Including Descriptions and Radio Buttons generation
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeWParam(Composite parent, int style, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		Group group = new Group(this, SWT.NONE);
		group.setText(Descriptions.Tab0_Head5);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		group.setLayout(new GridLayout(1, true));

		// headline
		titleLabel = new Label(this, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		// titleLabel.setText(Descriptions.Tab0_Head5);

		// text box with Description
		createdKey = new StyledText(group, SWT.WRAP | SWT.BORDER | SWT.READ_ONLY);
		createdKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		createdKey.setText(Descriptions.Tab0_Txt2);

		// Radio Buttons 4/16
		buttonSet4 = new Button(group, SWT.RADIO);
		buttonSet4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet4.setText("4");

		buttonSet16 = new Button(group, SWT.RADIO);
		buttonSet16.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet16.setText("16");
		buttonSet16.setSelection(true);

		buttonSet4.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 4;
				((MerkleTreeView) masterView).updateElement();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		buttonSet16.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 16;
				((MerkleTreeView) masterView).updateElement();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

	}

	public int getWinternitzParameter() {
		return wParameter;
	}

}
