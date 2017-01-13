package org.jcryptool.visual.merkletree.ui;

// import java.security.SecureRandom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
// import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
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
	Label tabDescriptionLabel;
	Text descrText;
	Button interactiveButton;
	Button plainButton;

	Label spacerTop;
	Label spacerBottom;
	Label spacerLeft;
	Label spacerRight;

	String signature[] = null;

	StyledText styledTextKeyNumber;
	ISimpleMerkle merkle;

	public MerkleTreeSignatureComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode, ViewPart masterView) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.merkle = merkle;
		this.mode = mode;
		this.masterView = masterView;
		instance = this;
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
		if (merkle instanceof XMSSTree) {
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
		} else if (merkle instanceof SimpleMerkleTree) {
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
		}

		spacerTop = new Label(this, SWT.NONE);
		spacerTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		selectionComposite = new Composite(this, SWT.BORDER);
		selectionComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 8, 1));
		selectionComposite.setLayout(new GridLayout(4, true));
		selectionComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		tabDescriptionLabel = new Label(selectionComposite, SWT.NONE);
		tabDescriptionLabel.setText("In diesem Tab können Signaturen erstellt werden. Wählen sie eine der zwei optionen, sie unterscheiden sich lediglich in ihrer Darstellung.");
		tabDescriptionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 4, 1));
		tabDescriptionLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		descrText = new Text(selectionComposite, SWT.WRAP | SWT.CENTER);
		descrText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 4, 1));
		// descrText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		interactiveButton = new Button(selectionComposite, SWT.PUSH);
		interactiveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		interactiveButton.setText("Interaktive Signaturerstellung");

		plainButton = new Button(selectionComposite, SWT.PUSH);
		plainButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		plainButton.setText("Einfache Signaturerstellung");

		spacerBottom = new Label(this, SWT.NONE);
		spacerBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		interactiveButton.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExit(MouseEvent e) {
				descrText.setText("");
			}

			@Override
			public void mouseEnter(MouseEvent e) {
				descrText.setText("Die Signaturerstellung wird Schritt für Schritt visuell durchgeführt und erklärt. ");

			}
		});

		plainButton.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub

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

		interactiveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createInteractiveComposite();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		plainButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createPlainComposite();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void createInteractiveComposite() {
		selectionComposite.dispose();
		spacerTop.dispose();
		spacerBottom.dispose();
		InteractiveSignatureComposite interactive = new InteractiveSignatureComposite(this, SWT.NONE, merkle, mode, masterView);
		interactive.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		this.layout();
	}

	private void createPlainComposite() {
		selectionComposite.dispose();
		spacerTop.dispose();
		spacerBottom.dispose();
		PlainSignatureComposite plain = new PlainSignatureComposite(this, SWT.NONE, merkle);
		plain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		this.layout();
	}

	/**
	 * Synchronizes Signature with the other Tabpages
	 * 
	 * @return Signature
	 */
	public String[] getSignatures() {
		return signature;
	}

	/**
	 * Return the used Message necessary for tab sync -> verification tab
	 * 
	 * @return usedText
	 */
	// public String getMessage() {
	// return textSign.getText();
	// }

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

}
