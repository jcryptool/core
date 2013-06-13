package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

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
	private Text txtSigNum;
	private int sigLen = org.jcryptool.visual.sig.algorithm.Input.signature.length;
	private String sigStrLen = Integer.toString(sigLen);
	
	

	private int mesLen = org.jcryptool.visual.sig.algorithm.Input.data.length;
	private String mesStrLen = Integer.toString(mesLen);
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public ShowSig(Display display, String sig) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 10, 454, 602);
		
		txtT = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
		txtT.setText(Messages.ShowSig_ownerTitle);
		txtT.setBounds(0, 0, 137, 21);
		
		txtT_2 = new Label(composite, SWT.READ_ONLY);
		txtT_2.setText(Messages.ShowSig_keyTitle);
		txtT_2.setBounds(0, 25, 137, 21);
		
		txtT_1 = new Label(composite, SWT.READ_ONLY);
		txtT_1.setText(Messages.ShowSig_methodTitle);
		txtT_1.setBounds(0, 49, 137, 21);
		
		// get owner of the key
		String userName;
		if ((org.jcryptool.visual.sig.algorithm.Input.privateKey == null) && (org.jcryptool.visual.sig.algorithm.Input.key == null)) {
			userName = "-";
		} else {
			if (org.jcryptool.visual.sig.algorithm.Input.key != null) {
				userName = org.jcryptool.visual.sig.algorithm.Input.key.getContactName();
			} else {
				userName = org.jcryptool.visual.sig.algorithm.Input.privateKey.getContactName();
			}	
		}
		text = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
		text.setText(userName);
		text.setBounds(143, 0, 287, 21);
		
		
		text_1 = new Label(composite, SWT.READ_ONLY);
		if ((org.jcryptool.visual.sig.algorithm.Input.privateKey == null) && (org.jcryptool.visual.sig.algorithm.Input.key == null)) {
			if (sig.contains("ECDSA")) {
				text_1.setText("ANSI X9.62 prime256v1 (256 bits)");
			} else {
				text_1.setText("-");
			}
		} else {
			if (org.jcryptool.visual.sig.algorithm.Input.key != null) {
				text_1.setText(org.jcryptool.visual.sig.algorithm.Input.key.getClassName());
			} else {
				text_1.setText(org.jcryptool.visual.sig.algorithm.Input.privateKey.getClassName());
			}
		}
		text_1.setBounds(143, 25, 287, 21);
		
		text_2 = new Label(composite, SWT.READ_ONLY);
		text_2.setText(sig);
		text_2.setBounds(143, 49, 287, 21);
		
		txtSig = new Label(composite, SWT.READ_ONLY);
		txtSig.setText(Messages.ShowSig_grpSignature);
		txtSig.setBounds(0, 82, 137, 21);
		
		txtLnge = new Label(composite, SWT.READ_ONLY);
		txtLnge.setText(Messages.ShowSig_lengthSig + sigStrLen + " Bits");
		txtLnge.setBounds(0, 245, 430, 21);
		
		Group grpOption = new Group(composite, SWT.NONE);
		grpOption.setText(Messages.ShowSig_grpOption);
		grpOption.setBounds(0, 277, 440, 93);
		grpOption.setLayout(null);
			
		lblNewLabel = new Label(grpOption, SWT.NONE);
		lblNewLabel.setBounds(10, 44, 162, 15);
		lblNewLabel.setText(Messages.ShowSig_dispOpt);
		
		txtSignedMes = new Label(composite, SWT.READ_ONLY);
		txtSignedMes.setText(Messages.ShowSig_grpMessage);
		txtSignedMes.setBounds(0, 386, 137, 21);
		
		txtLngeMes = new Label(composite, SWT.READ_ONLY);
		txtLngeMes.setText(Messages.ShowSig_lengthMessage + mesStrLen + " Bits");
		txtLngeMes.setBounds(0, 550, 430, 21);
		
/***************************Table Signature******************************************/		
		// TABLE SIGNATURE

		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 103, 454, 136);
		
		// Spalte Adresse 
		tblclmnAdresse = new TableColumn(table, SWT.NONE);
		tblclmnAdresse.setResizable(false);
		tblclmnAdresse.setWidth(60);
		tblclmnAdresse.setToolTipText("");
		tblclmnAdresse.setText(Messages.ShowSig_tblAdr);
		
		tblclmnHex = new TableColumn(table, SWT.NONE);
		tblclmnHex.setResizable(false);
		tblclmnHex.setWidth(250);
		tblclmnHex.setText(Messages.ShowSig_tblHex);
		
		tblclmnAscii = new TableColumn(table, SWT.NONE);
		tblclmnAscii.setResizable(false);
		tblclmnAscii.setWidth(138);
		tblclmnAscii.setText(Messages.ShowSig_tblAscii);

		// Spalten füllen
		int stepSize = 14;
		int len = org.jcryptool.visual.sig.algorithm.Input.signatureHex.length();

	    for (int i1 = 0; i1 < (len/(stepSize*2)) ; i1++) {
	        TableItem item = new TableItem(table, SWT.NONE);
	        
	        //Spalte 1
	        item.setText(0, getAdress(i1, stepSize));
	        
	        // Spalte 2
	        /*int start = i1 * stepSize;
	        int end = i1 * stepSize + stepSize;
	      
	        end = end >= len ? len : end;*/
	        
	        int start = i1 * (stepSize*2);
	        int end = i1 * (stepSize*2) + (stepSize*2);
	        end = end >= len ? len : end;
	    
	        StringBuffer buffer = new StringBuffer();
	        for (int m = 0; m < stepSize ; m++){
	         buffer.append(org.jcryptool.visual.sig.algorithm.Input.signatureHex.charAt((2*m)+start));
	         buffer.append(org.jcryptool.visual.sig.algorithm.Input.signatureHex.charAt((2*m+1)+start));
	         buffer.append(" ");
	        }
	        item.setText(1, buffer.toString());

	        // Spalte 3 org.jcryptool.visual.sig.algorithm.Input.signatureHex
	        item.setText(2, convertHexToString(org.jcryptool.visual.sig.algorithm.Input.signatureHex));

	      }
/*******************************Textbox Radiobutton**************************************/			
		txtSigNum = new Text(composite, SWT.BORDER | SWT.WRAP);
		txtSigNum.setEditable(false);
		txtSigNum.setBounds(0, 103, 440, 104);

/******************************Table Message***************************************/	
		table_1 = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setBounds(0, 408, 454, 136);
		
		tblclmnAddress = new TableColumn(table_1, SWT.NONE);
		tblclmnAddress.setResizable(false);
		tblclmnAddress.setWidth(60);
		tblclmnAddress.setToolTipText("");
		tblclmnAddress.setText(Messages.ShowSig_tblAdr);
		
		tableColumn_1 = new TableColumn(table_1, SWT.NONE);
		tableColumn_1.setResizable(false);
		tableColumn_1.setWidth(250);
		tableColumn_1.setText(Messages.ShowSig_tblHex);
		
		tableColumn_2 = new TableColumn(table_1, SWT.NONE);
		tableColumn_2.setResizable(false);
		tableColumn_2.setWidth(138);
		tableColumn_2.setText(Messages.ShowSig_tblAscii);
		
	    for (int i2 = 0; i2 < sigLen; i2++) {
	        TableItem item = new TableItem(table_1, SWT.NONE);
	        item.setText(0, getAdress(i2, 14));
	        item.setText(1, "unfinished");
	        item.setText(2, "unfinished");

	      }
	    
/*******************************Buttons**************************************/			
		// OKT
		Button btnOkt = new Button(grpOption, SWT.RADIO);
		btnOkt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setVisible(false);
				txtSigNum.setVisible(true);
				txtSigNum.setText(org.jcryptool.visual.sig.algorithm.Input.signatureOct);
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
				txtSigNum.setVisible(true);
				txtSigNum.setText(hexToDecimal(org.jcryptool.visual.sig.algorithm.Input.signatureHex));
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
				txtSigNum.setVisible(true);
				txtSigNum.setText(org.jcryptool.visual.sig.algorithm.Input.signatureHex);
			}
		});
		btnHex.setBounds(350, 43, 80, 16);
		btnHex.setText(Messages.ShowSig_hex);

		
		// DUMP
		Button btnHexdump = new Button(grpOption, SWT.RADIO);
		btnHexdump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtSigNum.setVisible(false);
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
		btnNewButton.setBounds(354, 577, 100, 25);
		btnNewButton.setText(Messages.ShowSig_btnClose);
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO
			}
		});
		btnNewButton_1.setBounds(247, 577, 100, 25);
		btnNewButton_1.setText(Messages.ShowSig_btnSave);
		btnNewButton_1.setEnabled(false);

		createContents();	
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(Messages.ShowSig_title);
		setSize(480, 650);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Returns a string to get the adress in the hex-dump-table.
	 * 
	 * @param i Row of table
	 * @param stepSize Differnce between digits in the row.
	 * @return
	 */
	protected String getAdress(int i, int stepSize){
		   return String.format("%05X", (i*stepSize) & 0xFFFFF);
		}
	/**
	 * Retruns the ascii representation of an hexadecimal string.
	 * 
	 * @param hex
	 * @return
	 */
	public String convertHexToString(String hex){
		  StringBuilder sb = new StringBuilder();
	 
		  for( int i=0; i<hex.length()-1; i+=2 ){
	 
		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      //convert the decimal to character
		      sb.append((char)decimal);
		  }
		  return sb.toString();
	  }
	
	/**
	 * Returns the decimal representation of an hexadecimal string.
	 * 
	 * @param hex
	 * @return
	 */
	public String hexToDecimal(String hex) {
		 StringBuilder sb = new StringBuilder();
		
		 for( int i=0; i<hex.length()-1; i+=2 ){
		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      sb.append(decimal);
		  }
		  return sb.toString();
	}
}
