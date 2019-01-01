//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Composite for the Tabpage "Signature"
 * Main window for the subclasses org.jcryptool.visual.merkletree.ui.InteractiveSignatureComposite.java
 * and org.jcryptool.visual.merkletree.ui.PlainSignatureComposite.java.
 * Basically provides display and interaction tasks, for example selecting which kind of signature class the user wants to use.
 * 
 * @author Kevin Muehlboeck
 * @author Christoph Sonnberger
 *
 */
public class MerkleTreeSignatureComposite extends Composite {

	private ViewPart masterView;
	private SUIT mode;
	private Shell shell;
	private Label descLabel;
	private Composite selectionComposite;
	private Composite topBar;
	private Composite signatureComposite;
	private StackLayout stackLayout;
	private StyledText tabDescriptionLabel;
	private Button interactiveButton;
	private Button plainButton;
	private Button interactiveTopButton;
	private Button plainTopButton;

	private Label indexLabel;
	private Label spacerTop;
	private Label spacerBottom;
	private Label spacerBottom2;

	private String signatures[];
	private String messages[];
	int index = 0;
	boolean interactiveStatus = false;

	private InteractiveSignatureComposite interactive;
	private PlainSignatureComposite plain;

	private ISimpleMerkle merkle;

	/**
	 * Create a header including headline, index counter and buttons for the StackLayout
	 * In the StackLayout, the Signature classes will be displayed
	 * 
	 * @param parent
	 * @param style
	 *            SWT Composite style bits
	 */
	public MerkleTreeSignatureComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode,
			ViewPart masterView) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.merkle = merkle;
		this.mode = mode;
		this.masterView = masterView;
		shell = this.getShell();

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		topBar = new Composite(this, SWT.NONE);
		topBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 8, 1));
		topBar.setLayout(new GridLayout(8, true));

		indexLabel = new Label(topBar, SWT.CENTER);
		indexLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		indexLabel.setText(Descriptions.MerkleTreeSign_11 + " 0 " + " / " + (merkle.getLeafCounter() - 1));
		indexLabel.setVisible(false);

		interactiveTopButton = new Button(topBar, SWT.PUSH);
		interactiveTopButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		interactiveTopButton.setText(Descriptions.InteractiveSignature_Button_0);
		interactiveTopButton.setVisible(false);

		plainTopButton = new Button(topBar, SWT.PUSH);
		plainTopButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		plainTopButton.setText(Descriptions.InteractiveSignature_Button_5);
		plainTopButton.setVisible(false);

		descLabel = new Label(topBar, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1));

		// Strings depending on mode
		switch (mode) {
		case XMSS:
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
			break;
		case XMSS_MT:
			descLabel.setText(Descriptions.XMSS_MT.Tab1_Head0);
			break;
		case MSS:
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
		default:
			break;
		}

		signatures = new String[merkle.getLeafCounter()];
		messages = new String[signatures.length];

		spacerTop = new Label(this, SWT.NONE);
		spacerTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		selectionComposite = new Composite(this, SWT.BORDER);
		selectionComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 8, 1));
		selectionComposite.setLayout(new GridLayout(4, true));
		selectionComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		tabDescriptionLabel = new StyledText(selectionComposite, SWT.NONE);
		tabDescriptionLabel.setText(Descriptions.InteractiveSignature_8);
		tabDescriptionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 4, 1));
		tabDescriptionLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabDescriptionLabel.setCaret(null);
		tabDescriptionLabel.setEditable(false);

		interactiveButton = new Button(selectionComposite, SWT.PUSH);
		interactiveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		interactiveButton.setText(Descriptions.InteractiveSignature_Button_0);
		interactiveButton.setToolTipText(Descriptions.InteractiveSignature_9);

		plainButton = new Button(selectionComposite, SWT.PUSH);
		plainButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		plainButton.setText(Descriptions.InteractiveSignature_Button_5);
		plainButton.setToolTipText(Descriptions.InteractiveSignature_10);

		spacerBottom = new Label(this, SWT.NONE);
		spacerBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		spacerBottom2 = new Label(this, SWT.NONE);
		spacerBottom2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		// ***********************************
		// End of GUI elements
		// ***********************************

		// Listeners to select between interactive/plain in the beginning
		interactiveButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createInteractiveComposite(false);
			}
		});
		// Listeners to select between interactive/plain in the beginning
		plainButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createPlainComposite(false);

			}

		});

		// Listeners to switch between interactive/plain afterwards
		interactiveTopButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createInteractiveComposite(true);
			}
		});

		// Listeners to switch between interactive/plain afterwards
		plainTopButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// This logic checks if the interactive process is "interrupted"
				// If so a messagebox asks if you want to leave the process
				// This is pseudo, as the interactive signature generation is an atomic process, which gets
				// laid out by the GUI to look as it would work step by step
				if (interactiveStatus) {
					MessageBox abortInteractive = new MessageBox(shell, SWT.ICON_WORKING | SWT.YES | SWT.NO);
					abortInteractive.setText(Descriptions.InteractiveSignature_abort_1);
					abortInteractive.setMessage(Descriptions.InteractiveSignature_abort_2);
					int boxValue = abortInteractive.open();
					switch (boxValue) {
					case SWT.YES:
						createPlainComposite(true);
						interactive.withdrawSignature();
						break;
					default:
						break;
					}
				} else {
					createPlainComposite(true);
				}
			}
		});

	}

	/**
	 * If no instance of InteractiveSignatureComposite has been created yet, it gets created
	 * elsewise it is put on top of the StackLayout
	 * 
	 * @param toggle
	 */
	private void createInteractiveComposite(boolean toggle) {

		if (!toggle) {
			disposeSelection();
		}
		if (signatureComposite == null) {
			signatureComposite = new Composite(this, SWT.NONE);
			signatureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			stackLayout = new StackLayout();
			signatureComposite.setLayout(stackLayout);
		}

		interactive = new InteractiveSignatureComposite(signatureComposite, SWT.NO_REDRAW_RESIZE, merkle, mode,
				masterView, this);
		interactive.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		interactive.pack();
		interactive.interactiveSignatureGeneration();
		stackLayout.topControl = interactive;
		interactiveTopButton.setEnabled(false);
		plainTopButton.setEnabled(true);
		this.layout();
		signatureComposite.layout();
	}

	/**
	 * If no instance of PlainSignatureComposite has been created yet, it gets created
	 * elsewise it is put on top of the StackLayout
	 * 
	 * @param toggle
	 */
	private void createPlainComposite(boolean toggle) {
		if (!toggle) {
			disposeSelection();
		}
		if (signatureComposite == null) {
			signatureComposite = new Composite(this, SWT.NONE);
			signatureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			stackLayout = new StackLayout();
			signatureComposite.setLayout(stackLayout);
		}
		if (plain == null) {
			plain = new PlainSignatureComposite(signatureComposite, SWT.NONE, merkle, this, mode);
			plain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			plain.pack();
		}
		stackLayout.topControl = plain;
		plainTopButton.setEnabled(false);
		interactiveTopButton.setEnabled(true);
		plain.clearSignatureText();
		this.layout();
		signatureComposite.layout();
	}

	/**
	 * disposes the old main selection GUI
	 */
	private void disposeSelection() {
		selectionComposite.dispose();
		spacerTop.dispose();
		spacerBottom.dispose();
		spacerBottom2.dispose();

		indexLabel.setVisible(true);
		interactiveTopButton.setVisible(true);
		plainTopButton.setVisible(true);

	}

	/**
	 * Synchronizes Signature with the other Tabpages
	 * 
	 * @return Signature
	 */
	public String[] getSignatures() {
		return signatures;
	}

	/**
	 * Return the used Message necessary for tab sync -> verification tab
	 * 
	 * @return usedText
	 */
	public String[] getMessages() {
		return messages;
	}

	public void addSignatureAndMessage(String signature, String message) {
		signatures[index] = signature;
		messages[index] = message;
		updateIndexLabel(merkle.getKeyIndex());
		++index;
	}

	public void updateIndexLabel(int pseudoIndex) {
		if (pseudoIndex < 0) {
			indexLabel.setText(Descriptions.MerkleTreeSign_10);
		} else if (pseudoIndex > merkle.getLeafCounter() - 1) {
			indexLabel.setText(Descriptions.MerkleTreeSign_10);
		} else {
			indexLabel.setText(
					Descriptions.MerkleTreeSign_11 + " " + pseudoIndex + " / " + (merkle.getLeafCounter() - 1));
		}
	}

	/**
	 * @author christoph sonnberger returns the Length of the Siganture as
	 *         String used for styledTextSignSize in GUI
	 * @param signature
	 * @return length of the Signature
	 */
	public String getSignatureLength(String signature) {
		int length = signature.length();
		// divide by 2 to get the length in bytes
		length = length / 2;
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append(length);
		String sigLength = sb.toString();
		return sigLength;
	}

	/**
	 * @author christoph sonnberger returns the Index of a given signature as
	 *         String the Index is the first Letter of the signature
	 * @param signature
	 * @return index
	 */
	public String getKeyIndex(String signature) {
		int iend = signature.indexOf("|");
		String subString = signature.substring(0, iend);
		return subString;
	}

	/**
	 * Synchronizes the MerkleTree Object with the other Tabpages
	 * 
	 * @return ISimpleMerkle Object
	 */
	public ISimpleMerkle getMerkleFromForm() {
		return this.merkle;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of non-composite SWT components
		// this is a composite subclass
	}

	/**
	 * Displays a messagebox that no more one-time keys are available
	 */
	protected void keysExceededMessage() {
		MessageBox box = new MessageBox(shell, SWT.COLOR_INFO_BACKGROUND | SWT.OK);
		box.setMessage(Descriptions.MerkleTreeSign_9);
		int boxOK = box.open();

		switch (boxOK) {
		case SWT.OK:
		default:
			((MerkleTreeView) masterView).setTab(0);
			((MerkleTreeView) masterView).deleteTree();
			merkle = null;
			break;
		}
	}

	/**
	 * Declares in the parent MerkleTreeSignatureComposite if an interactive
	 * signature generation is ongoing or not
	 * 
	 * @param status
	 */
	protected void setInteractiveStatus(boolean status) {
		interactiveStatus = status;
	}

}
