package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.sig.algorithm.Input;

public class ShowSig extends Shell {
	private Label txtT;
	private Label txtT_2;
	private Label txtT_1;
	private Label text;
	private Label text_1;
	private Label text_2;
	private Label txtSig;
	private Label txtLnge;
	private Label txtSignedMes;
	private Label txtLngeMes;
	private Table table;
	private TableColumn tblclmnAdresse;
	private TableColumn tblclmnHex;
	private TableColumn tblclmnAscii;
	private Table table_1;
	private TableColumn tblclmnAddress;
	private TableColumn tableColumn_1;
	private TableColumn tableColumn_2;
	private Label lblNewLabel;
	private Label lblSigNum;

	/**
	 * Create the shell.
	 * @param display
	 */
	public ShowSig(Display display, String sig) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 10, 440, 536);
		
		txtT = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
		txtT.setText(Messages.ShowSig_ownerTitle);
		txtT.setBounds(0, 0, 137, 21);
		
		txtT_2 = new Label(composite, SWT.READ_ONLY);
		txtT_2.setText(Messages.ShowSig_keyTitle);
		txtT_2.setBounds(0, 25, 137, 21);
		
		txtT_1 = new Label(composite, SWT.READ_ONLY);
		txtT_1.setText(Messages.ShowSig_methodTitle);
		txtT_1.setBounds(0, 49, 137, 21);
		
		/**** place holder ****/
		
		// get owner of the key
		String userName = org.jcryptool.visual.sig.algorithm.Input.privateKey.getContactName();
		text = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
		text.setText(userName);
		text.setBounds(143, 0, 287, 21);
		
		
		text_1 = new Label(composite, SWT.READ_ONLY);
		text_1.setText(org.jcryptool.visual.sig.algorithm.Input.privateKey.getClassName());
		text_1.setBounds(143, 25, 287, 21);
		
		text_2 = new Label(composite, SWT.READ_ONLY);
		text_2.setText(sig);
		text_2.setBounds(143, 49, 287, 21);
		
		txtSig = new Label(composite, SWT.READ_ONLY);
		txtSig.setText(Messages.ShowSig_grpSignature);
		txtSig.setBounds(0, 76, 137, 21);
		
		txtLnge = new Label(composite, SWT.READ_ONLY);
		txtLnge.setText(Messages.ShowSig_lengthSig);
		txtLnge.setBounds(0, 213, 200, 21);
		
		// TODO 
		Group grpOption = new Group(composite, SWT.NONE);
		grpOption.setText(Messages.ShowSig_grpOption);
		grpOption.setBounds(0, 245, 440, 93);
		grpOption.setLayout(null);
			
		lblNewLabel = new Label(grpOption, SWT.NONE);
		lblNewLabel.setBounds(10, 44, 162, 15);
		lblNewLabel.setText(Messages.ShowSig_dispOpt);
		
		txtSignedMes = new Label(composite, SWT.READ_ONLY);
		txtSignedMes.setText(Messages.ShowSig_grpMessage);
		txtSignedMes.setBounds(0, 344, 137, 21);
		
		txtLngeMes = new Label(composite, SWT.READ_ONLY);
		txtLngeMes.setText(Messages.ShowSig_lengthMessage);
		txtLngeMes.setBounds(0, 481, 200, 21);
		
		

		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 103, 440, 104);
		
		tblclmnAdresse = new TableColumn(table, SWT.NONE);
		tblclmnAdresse.setWidth(100);
		tblclmnAdresse.setToolTipText("");
		tblclmnAdresse.setText("address");
		
		tblclmnHex = new TableColumn(table, SWT.NONE);
		tblclmnHex.setWidth(224);
		tblclmnHex.setText("hex");
		
		tblclmnAscii = new TableColumn(table, SWT.NONE);
		tblclmnAscii.setWidth(112);
		tblclmnAscii.setText("ascii");
		
		lblSigNum = new Label(composite, SWT.NONE);
		lblSigNum.setBounds(0, 103, 440, 104);
		
		table_1 = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setBounds(0, 371, 440, 104);
		
		tblclmnAddress = new TableColumn(table_1, SWT.NONE);
		tblclmnAddress.setWidth(100);
		tblclmnAddress.setToolTipText("");
		tblclmnAddress.setText("address");
		
		tableColumn_1 = new TableColumn(table_1, SWT.NONE);
		tableColumn_1.setWidth(224);
		tableColumn_1.setText("hex");
		
		tableColumn_2 = new TableColumn(table_1, SWT.NONE);
		tableColumn_2.setWidth(112);
		tableColumn_2.setText("ascii");
		
		// Radiobuttons
		
		// OKT
		Button btnOkt = new Button(grpOption, SWT.RADIO);
		btnOkt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setVisible(false);
				lblSigNum.setVisible(true);
				lblSigNum.setText(org.jcryptool.visual.sig.algorithm.Input.signatureOct);
			}
		});
		btnOkt.setBounds(178, 43, 80, 16);
		btnOkt.setText(Messages.ShowSig_octal);
		
		//DEZ
		Button btnDez = new Button(grpOption, SWT.RADIO);
		btnDez.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setVisible(false);
				lblSigNum.setVisible(true);
				lblSigNum.setText(org.jcryptool.visual.sig.algorithm.Input.signatureDec);
			}
		});
		btnDez.setBounds(264, 43, 80, 16);
		btnDez.setText(Messages.ShowSig_decimal);
		
		// HEX
		Button btnHex = new Button(grpOption, SWT.RADIO);
		btnHex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setVisible(false);
				lblSigNum.setVisible(true);
				lblSigNum.setText(org.jcryptool.visual.sig.algorithm.Input.signatureHex);
			}
		});
		btnHex.setBounds(350, 43, 80, 16);
		btnHex.setText(Messages.ShowSig_hex);

		
		// DUMP
		Button btnHexdump = new Button(grpOption, SWT.RADIO);
		btnHexdump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblSigNum.setVisible(false);
				table.setVisible(true);
			}
		});
		btnHexdump.setSelection(true);
		btnHexdump.setBounds(10, 21, 191, 16);
		btnHexdump.setText(Messages.ShowSig_hexDump);
		
		// Button Close and Save
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ShowSig.this.close();
			}
		});
		btnNewButton.setBounds(340, 511, 100, 25);
		btnNewButton.setText(Messages.ShowSig_btnClose);
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO
			}
		});
		btnNewButton_1.setBounds(233, 511, 100, 25);
		btnNewButton_1.setText(Messages.ShowSig_btnSave);
		btnNewButton_1.setEnabled(false);
		
		createContents();
		
		
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(Messages.ShowSig_title);
		setSize(466, 584);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
