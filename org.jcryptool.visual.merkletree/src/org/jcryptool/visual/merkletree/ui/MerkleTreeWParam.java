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
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

public class MerkleTreeWParam extends Composite {

	Label createLabel;
	StyledText createdKey;
	StyledText descText;
	private int wParameter = 16;

	/**
	 * Create the composite. Including KeyPair content and button for keyPair
	 * generation
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeWParam(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(1, true));

		// headline
		createLabel = new Label(this, SWT.NONE);
		createLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		// text box
		Label keysum = new Label(this, SWT.NONE);
		keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		keysum.setText("WOTS-Parameter");

		// text box with generated key info
		createdKey = new StyledText(this, SWT.WRAP | SWT.BORDER);
		createdKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		createdKey.setText("Der Winternitz Parameter fickt bitches und Beschreibung");

		// Radio Buttons 4/16
		Button param4Button = new Button(this, SWT.RADIO);
		param4Button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		param4Button.setText("4");

		Button param16Button = new Button(this, SWT.RADIO);
		param16Button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		param16Button.setText("16");
		param16Button.setSelection(true);

		param4Button.addSelectionListener (new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 4;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		param16Button.addSelectionListener (new SelectionListener() {
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
