package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ShowSig extends Shell {
	private Text txtT;
	private Text txtT_2;
	private Text txtT_1;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text txtSig;
	private Text txtLnge;
	private Text txtSignedMes;
	private Text txtLngeMes;
	private Table table;
	private TableColumn tblclmnAdresse;
	private TableColumn tblclmnHex;
	private TableColumn tblclmnAscii;
	private Table table_1;
	private TableColumn tableColumn;
	private TableColumn tableColumn_1;
	private TableColumn tableColumn_2;

	/**
	 * Create the shell.
	 * @param display
	 */
	public ShowSig(Display display) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 10, 440, 536);
		
		txtT = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		txtT.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		txtT.setText(Messages.ShowSig_ownerTitle);
		txtT.setBounds(0, 0, 137, 21);
		
		txtT_2 = new Text(composite, SWT.READ_ONLY);
		txtT_2.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		txtT_2.setText(Messages.ShowSig_keyTitle);
		txtT_2.setBounds(0, 25, 137, 21);
		
		txtT_1 = new Text(composite, SWT.READ_ONLY);
		txtT_1.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		txtT_1.setText(Messages.ShowSig_methodTitle);
		txtT_1.setBounds(0, 49, 137, 21);
		
		text = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		text.setText(Messages.ShowSig_owner);
		text.setBounds(143, 0, 287, 21);
		
		text_1 = new Text(composite, SWT.READ_ONLY);
		text_1.setText(Messages.ShowSig_key);
		text_1.setBounds(143, 25, 287, 21);
		
		text_2 = new Text(composite, SWT.READ_ONLY);
		text_2.setText(Messages.ShowSig_method);
		text_2.setBounds(143, 49, 287, 21);
		
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
		
		txtSig = new Text(composite, SWT.READ_ONLY);
		txtSig.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		txtSig.setForeground(SWTResourceManager.getColor(0, 0, 0));
		txtSig.setText(Messages.ShowSig_grpSignature);
		txtSig.setBounds(0, 76, 137, 21);
		
		txtLnge = new Text(composite, SWT.READ_ONLY);
		txtLnge.setText(Messages.ShowSig_lengthSig);
		txtLnge.setBounds(0, 213, 200, 21);
		
		// TODO 
		Group grpOption = new Group(composite, SWT.NONE);
		grpOption.setText(Messages.ShowSig_grpOption);
		grpOption.setBounds(0, 240, 440, 82);
		grpOption.setLayout(null);
		
		Button btnZahl = new Button(grpOption, SWT.RADIO);
		btnZahl.setBounds(10, 49, 113, 16);
		btnZahl.setText(Messages.ShowSig_number);
		
		Button btnOkt = new Button(grpOption, SWT.RADIO);
		btnOkt.setBounds(129, 49, 90, 16);
		btnOkt.setText(Messages.ShowSig_octal);
		
		Button btnDez = new Button(grpOption, SWT.RADIO);
		btnDez.setBounds(237, 49, 90, 16);
		btnDez.setText(Messages.ShowSig_decimal);
		
		Button btnHex = new Button(grpOption, SWT.RADIO);
		btnHex.setBounds(340, 49, 90, 16);
		btnHex.setText(Messages.ShowSig_hex);
		
		Button btnHexdump = new Button(grpOption, SWT.RADIO);
		btnHexdump.setSelection(true);
		btnHexdump.setBounds(10, 27, 191, 16);
		btnHexdump.setText(Messages.ShowSig_hexDump);
		
		txtSignedMes = new Text(composite, SWT.READ_ONLY);
		txtSignedMes.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		txtSignedMes.setText(Messages.ShowSig_grpMessage);
		txtSignedMes.setBounds(0, 328, 137, 21);
		
		txtLngeMes = new Text(composite, SWT.READ_ONLY);
		txtLngeMes.setText(Messages.ShowSig_lengthMessage);
		txtLngeMes.setBounds(0, 465, 200, 21);
		
		// TODO
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 103, 440, 104);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tblclmnAdresse = new TableColumn(table, SWT.NONE);
		tblclmnAdresse.setToolTipText("");
		tblclmnAdresse.setWidth(100);
		tblclmnAdresse.setText("adresse");
		
		tblclmnHex = new TableColumn(table, SWT.NONE);
		tblclmnHex.setWidth(224);
		tblclmnHex.setText("hex");
		
		tblclmnAscii = new TableColumn(table, SWT.NONE);
		tblclmnAscii.setWidth(112);
		tblclmnAscii.setText("ascii");
		
		table_1 = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setBounds(0, 355, 440, 104);
		
		tableColumn = new TableColumn(table_1, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setToolTipText("");
		tableColumn.setText("adresse");
		
		tableColumn_1 = new TableColumn(table_1, SWT.NONE);
		tableColumn_1.setWidth(224);
		tableColumn_1.setText("hex");
		
		tableColumn_2 = new TableColumn(table_1, SWT.NONE);
		tableColumn_2.setWidth(112);
		tableColumn_2.setText("ascii");
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
