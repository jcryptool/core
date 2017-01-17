package org.jcryptool.visual.merkletree.ui;

// import java.security.SecureRandom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
// import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Composite for the Tabpage "Signatur"
 * 
 * @author Kevin Muehlboeck
 * @author Christoph Sonnberger
 *
 */
public class MerkleTreeSignatureComposite extends Composite {

	/**
	 * Create the composite. Includes Message definition, Signature generation
	 * and Signature content
	 * 
	 * @param parent
	 * @param style
	 */
	MerkleTreeSignatureComposite instance;
	ViewPart masterView;
	SUIT mode;
	Label descLabel;
	Composite selectionComposite;
	Composite topBar;
	Composite signatureComposite;
	StackLayout stackLayout;
	Label tabDescriptionLabel;
	Text descrText;
	Button interactiveButton;
	Button plainButton;
	Button interactiveTopButton;
	Button plainTopButton;

	Label topBarSpacer;
	Label indexLabel;

	Label spacerTop;
	Label spacerBottom;
	Label spacerBottom2;

	String signatures[];
	String messages[];
	int index = 0;
	boolean interactiveStatus = false;

	InteractiveSignatureComposite interactive;
	PlainSignatureComposite plain;

	StyledText styledTextKeyNumber;
	ISimpleMerkle merkle;

	public MerkleTreeSignatureComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode, ViewPart masterView) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.merkle = merkle;
		this.mode = mode;
		this.masterView = masterView;
		instance = this;

		topBar = new Composite(this, SWT.NONE);
		topBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 8, 1));
		topBar.setLayout(new GridLayout(8, true));

		topBarSpacer = new Label(topBar, SWT.NONE);
		topBarSpacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		indexLabel = new Label(topBar, SWT.NONE);
		indexLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		indexLabel.setText("Aktueller Index: " + index + "/" + (merkle.getLeafCounter() - 1));
		indexLabel.setVisible(false);

		interactiveTopButton = new Button(topBar, SWT.PUSH);
		interactiveTopButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		interactiveTopButton.setText(Descriptions.InteractiveSignature_Button_0);
		interactiveTopButton.setVisible(false);

		plainTopButton = new Button(topBar, SWT.PUSH);
		plainTopButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		plainTopButton.setText(Descriptions.InteractiveSignature_Button_5);
		plainTopButton.setVisible(false);

		descLabel = new Label(topBar, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1));
		if (merkle instanceof XMSSTree) {
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
		} else if (merkle instanceof SimpleMerkleTree) {
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
		}

		signatures = new String[merkle.getLeafCounter()];
		messages = new String[signatures.length];

		spacerTop = new Label(this, SWT.NONE);
		spacerTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		selectionComposite = new Composite(this, SWT.BORDER);
		selectionComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 8, 1));
		selectionComposite.setLayout(new GridLayout(4, true));
		selectionComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		tabDescriptionLabel = new Label(selectionComposite, SWT.NONE);
		tabDescriptionLabel.setText(Descriptions.InteractiveSignature_8);
		tabDescriptionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 4, 1));
		tabDescriptionLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		descrText = new Text(selectionComposite, SWT.WRAP | SWT.CENTER);
		descrText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));
		// descrText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		interactiveButton = new Button(selectionComposite, SWT.PUSH);
		interactiveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		interactiveButton.setText(Descriptions.InteractiveSignature_Button_0);

		plainButton = new Button(selectionComposite, SWT.PUSH);
		plainButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		plainButton.setText(Descriptions.InteractiveSignature_Button_5);

		spacerBottom = new Label(this, SWT.NONE);
		spacerBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		spacerBottom2 = new Label(this, SWT.NONE);
		spacerBottom2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		interactiveButton.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// nothing here

			}

			@Override
			public void mouseExit(MouseEvent e) {
				descrText.setText("");
			}

			@Override
			public void mouseEnter(MouseEvent e) {
				descrText.setText(Descriptions.InteractiveSignature_9);

			}
		});

		plainButton.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// nothing here

			}

			@Override
			public void mouseExit(MouseEvent e) {
				descrText.setText("");
			}

			@Override
			public void mouseEnter(MouseEvent e) {
				descrText.setText("Geben Sie einen Text ein und erhalten sofort eine Signatur");

			}
		});

		interactiveButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createInteractiveComposite(false);
			}
		});

		plainButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createPlainComposite(false);

			}

		});

		interactiveTopButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createInteractiveComposite(true);
			}
		});

		plainTopButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (interactiveStatus) {
					MessageBox abortInteractive = new MessageBox(new Shell(), SWT.ICON_WORKING | SWT.YES | SWT.NO);
					abortInteractive.setText("Warnung");
					abortInteractive.setMessage("Wollen sie die interaktive Signaturerstellung abbrechen?");
					int boxValue = abortInteractive.open();
					switch (boxValue) {
					case SWT.YES:
						createPlainComposite(true);
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

	private void createInteractiveComposite(boolean toggle) {

		if (!toggle) {
			disposeSelection();
		} else {
			// plain.dispose();
		}
		if (signatureComposite == null) {
			signatureComposite = new Composite(this, SWT.NONE);
			signatureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			stackLayout = new StackLayout();
			signatureComposite.setLayout(stackLayout);
		}
		// if (interactive == null) {
		interactive = new InteractiveSignatureComposite(signatureComposite, SWT.NO_REDRAW_RESIZE, merkle, mode, masterView, this);
		interactive.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		interactive.pack();
		interactive.interactiveSignatureGeneration();
		// }
		stackLayout.topControl = interactive;
		interactiveTopButton.setEnabled(false);
		plainTopButton.setEnabled(true);
		this.layout();
		signatureComposite.layout();
	}

	private void createPlainComposite(boolean toggle) {
		if (!toggle) {
			disposeSelection();
		} else {
			// interactive.dispose();
		}
		if (signatureComposite == null) {
			signatureComposite = new Composite(this, SWT.NONE);
			signatureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			stackLayout = new StackLayout();
			signatureComposite.setLayout(stackLayout);
		}
		if (plain == null) {
			plain = new PlainSignatureComposite(signatureComposite, SWT.NONE, merkle, this);
			plain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			plain.pack();
		}
		stackLayout.topControl = plain;
		plainTopButton.setEnabled(false);
		interactiveTopButton.setEnabled(true);
		this.layout();
		signatureComposite.layout();
	}

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
		indexLabel.setText("Aktueller Index: " + index + "/" + (merkle.getLeafCounter() - 1));
		++index;
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
		// Disable the check that prevents subclassing of SWT components
	}

	protected void keysExceededMessage() {
		MessageBox box = new MessageBox(new Shell(), SWT.COLOR_INFO_BACKGROUND | SWT.OK);
		box.setMessage(Descriptions.MerkleTreeSign_8);
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

	protected Rectangle getSignatureCompositeBounds() {
		return selectionComposite.getBounds();
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
