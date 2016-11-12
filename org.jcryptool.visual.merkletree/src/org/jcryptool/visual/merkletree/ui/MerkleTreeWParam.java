package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

public class MerkleTreeWParam extends Composite {

	Button buttonSet4;
	Button buttonSet16;
	Label createLabel;
	Label keysum; 
	StyledText createdKey;
	StyledText descText;
	private int wParameter = 16;

	/**
	 * Create the composite. Including Descriptions and Radio Buttons
	 * generation
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeWParam(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(1, true));

		// headline
		keysum = new Label(this, SWT.NONE);
		keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));  
		keysum.setText(Descriptions.Tab0_Head5);  

		// text box with Description (Tab0_Txt2)
		createdKey = new StyledText(this, SWT.WRAP | SWT.BORDER);
		createdKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		createdKey.setText(Descriptions.Tab0_Txt2);

		// Radio Buttons 4/16
		buttonSet4 = new Button(this, SWT.RADIO);
		buttonSet4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet4.setText("4");

		buttonSet16 = new Button(this, SWT.RADIO);
		buttonSet16.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet16.setText("16");
		buttonSet16.setSelection(true);

		buttonSet4.addSelectionListener (new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 4;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		buttonSet16.addSelectionListener (new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 16;
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
