package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.visual.sigVerification.algorithm.Hash;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;
import org.jcryptool.visual.sigVerification.ui.view.SigVerView;

public class SignaturResult extends Shell {
    private Table tableSig;
    private Table tableHash;
    Input input;
    Hash hashInst;
    SigVerification sigVerification;
    Composite parent;
    SigVerView sigVerView;

    private int signatureLengh;
    private int hashBitSize;

    /**
     * Create the shell.
     * 
     * @param display
     * @param view 
     * @param parent 
     */
    public SignaturResult(Display display, final Input input, Hash hashInst,
            SigVerification sigVerification, final SigVerView sigVerView) {
//        super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE | SWT.APPLICATION_MODAL);
    	super(display, SWT.SHELL_TRIM);
        this.input = input;
        this.hashInst = hashInst;
        this.sigVerification = sigVerification;
        this.signatureLengh = input.signatureSize;
        if (hashInst.hashHex != null) {
            this.hashBitSize = hashInst.hashHex.length() * 8;
        } else {
        	this.hashBitSize = -1;
        }

        this.sigVerView = sigVerView;

        setLayout(new GridLayout());
        
        Clipboard clipboard = new Clipboard(display);

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(2, false));

        Label key = new Label(composite, SWT.READ_ONLY);
        key.setText(Messages.SignaturResult_keyTitle);
        key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Label keyType = new Label(composite, SWT.READ_ONLY);
        if (input.publicKeyAlias == null) {
            if (input.signaturemethod != null && input.signaturemethod.contains("ECDSA")) {
                keyType.setText("ANSI X9.62 prime256v1 (256 bits)");
            } else {
                keyType.setText("-");
            }
        } else {
            keyType.setText(input.publicKeyAlias.getClassName());
        }
        keyType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
          
        Label signatureMethod = new Label(composite, SWT.READ_ONLY);
        signatureMethod.setText(Messages.SignaturResult_methodTitle);
        signatureMethod.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        Label signatureInfo = new Label(composite, SWT.READ_ONLY);
        signatureInfo.setText(input.signaturemethod);
        signatureInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        Label signature = new Label(composite, SWT.READ_ONLY);
        signature.setText(Messages.SignaturResult_grpSignature);
        signature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

        Composite sigComp = new Composite(composite, SWT.NONE);
        GridData gd_sigComp = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        sigComp.setLayoutData(gd_sigComp);
        GridLayout gl_sigComp = new GridLayout();
        gl_sigComp.marginHeight = 0;
        gl_sigComp.marginWidth = 0;
        sigComp.setLayout(gl_sigComp);
        
        Label txtSigNum = new Label(sigComp, SWT.BORDER | SWT.WRAP );
        GridData gd_txtSigNum = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_txtSigNum.widthHint = 490;
        gd_txtSigNum.heightHint = 151;
        gd_txtSigNum.exclude = true;
        txtSigNum.setVisible(false);
        txtSigNum.setLayoutData(gd_txtSigNum);
        txtSigNum.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        
        //Context menu for coyping text field clipboard
        Menu signatureMenu = new Menu(txtSigNum);
        txtSigNum.setMenu(signatureMenu);
        MenuItem copy = new MenuItem(signatureMenu, SWT.NONE);
        copy.setText(Messages.Copy);
        copy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clipboard.setContents(new Object[] {txtSigNum.getText()}, new Transfer[] {TextTransfer.getInstance()});
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}      	
        });


        // create table to show the generated signature
        Composite table1Composite = new Composite(sigComp, SWT.NONE);
        GridData gd_table1Composite = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_table1Composite.widthHint = 490;
        gd_table1Composite.heightHint = 151;
        table1Composite.setLayoutData(gd_table1Composite);
        TableColumnLayout table1Layout = new TableColumnLayout();
        table1Composite.setLayout(table1Layout);
        
        tableSig = new Table(table1Composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableSig.setLinesVisible(true);
        tableSig.setHeaderVisible(true);

        TableColumn tblclmnAddress = new TableColumn(tableSig, SWT.NONE);
        table1Layout.setColumnData(tblclmnAddress, new ColumnWeightData(15, 90, true));
        tblclmnAddress.setToolTipText("");
        tblclmnAddress.setText(Messages.SignaturResult_tblAdr);

        TableColumn tblclmnHex = new TableColumn(tableSig, SWT.NONE);
        table1Layout.setColumnData(tblclmnHex, new ColumnWeightData(60, 250, true));
        tblclmnHex.setText(Messages.SignaturResult_tblHex);

        TableColumn tblclmnAscii = new TableColumn(tableSig, SWT.NONE);
        table1Layout.setColumnData(tblclmnAscii, new ColumnWeightData(25, 150, true));
        tblclmnAscii.setText(Messages.SignaturResult_tblAscii);

        int stepSize = 14;

        if (input.signatureHex != null) {
            int len1 = input.signatureHex.length();
            String asciistr1 = convertHexToString(input.signatureHex);
            int lenAscii1 = asciistr1.length();

            for (int i1 = 0; i1 < (Math.ceil((double) len1 / (stepSize * 2))); i1++) {
                TableItem item = new TableItem(tableSig, SWT.NONE);

                // column 2 - hex
                int start1 = i1 * (stepSize * 2);
                int end1 = i1 * (stepSize * 2) + (stepSize * 2);
                end1 = end1 >= len1 ? len1 : end1;

                int startascii1 = start1 / 2;
                int endascii1 = (end1 / 2) >= lenAscii1 ? lenAscii1 : (end1 / 2);

                if ((start1 < end1) && (startascii1 < endascii1)) {
                    // column 1 - address
                    item.setText(0, getAddress(i1, stepSize));

                    StringBuffer bufferS1 = new StringBuffer();
                    for (int m1 = 0; m1 < (end1 - start1) / 2; m1++) {
                        bufferS1.append(input.signatureHex.charAt((2 * m1) + start1));
                        bufferS1.append(input.signatureHex.charAt((2 * m1 + 1) + start1));
                        bufferS1.append(" ");
                    }
                    item.setText(1, bufferS1.toString());

                    // column 3 - ascii
                    StringBuffer bufferS2 = new StringBuffer();
                    bufferS2.append(asciistr1, startascii1, endascii1);
                    item.setText(2, bufferS2.toString());
                } else {
                    i1 = (len1 / (stepSize * 2)) + 5;
                }
            }
        }
        
        
        //Context menu for coyping table content to clipboard
        Menu tableMenu = new Menu(tableSig);
        tableSig.setMenu(tableMenu);
        
        MenuItem copyLine = new MenuItem(tableMenu, SWT.NONE);
        copyLine.setText(Messages.CopyLine);
        copyLine.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableSig.getSelectionCount() > 0) {
					TableItem selection = tableSig.getSelection()[0];
					String address = "Address: " + selection.getText(0) + "\n";
					String hex = "Hex: " + selection.getText(1) + "\n";
					String ascii = "Ascii: " + selection.getText(2);
					String result = address + hex + ascii;
					clipboard.setContents(new Object[] {result}, new Transfer[] {TextTransfer.getInstance()});
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}      	
        });
        
        MenuItem copyAll = new MenuItem(tableMenu, SWT.NONE);
        copyAll.setText(Messages.CopyAll);
        copyAll.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] tableContents = tableSig.getItems();
				String result = "";
				for (TableItem item : tableContents) {
					String address = "Address: " + item.getText(0) + "\n";
					String hex = "Hex: " + item.getText(1) + "\n";
					String ascii = "Ascii: " + item.getText(2);
					String lineResult = address + hex + ascii + "\n\n";
					result+= lineResult;
				}
				clipboard.setContents(new Object[] {result}, new Transfer[] {TextTransfer.getInstance()});
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}    	
        });

        Label signatureLength = new Label(composite, SWT.READ_ONLY);
        signatureLength.setText(Messages.SignaturResult_lengthSig + signatureLengh + " bit");
        signatureLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        
        Group options = new Group(composite, SWT.NONE);
        options.setText(Messages.SignaturResult_grpOption);
        options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        options.setLayout(new GridLayout(2, true));
        
        Button btnOkt = new Button(options, SWT.RADIO);
        btnOkt.setText(Messages.SignaturResult_octal);
        btnOkt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Button btnDec = new Button(options, SWT.RADIO);
        btnDec.setText(Messages.SignaturResult_decimal);
        btnDec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Button btnHex = new Button(options, SWT.RADIO);
        btnHex.setText(Messages.SignaturResult_hex);
        btnHex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Button btnHexdump = new Button(options, SWT.RADIO);
        btnHexdump.setText(Messages.SignaturResult_hexDump);
        btnHexdump.setSelection(true);
        btnHexdump.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        Label hash = new Label(composite, SWT.READ_ONLY);
        hash.setText(Messages.SignaturResult_grpMessage);
        GridData gd_hash = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
        gd_hash.verticalIndent = 20;
        hash.setLayoutData(gd_hash);
        
        // create table to show hash
        Composite table2Composite = new Composite(composite, SWT.NONE);
        GridData gd_table2Composite = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_table2Composite.widthHint = 490;
        gd_table2Composite.heightHint = 151;
        table2Composite.setLayoutData(gd_table2Composite);
        TableColumnLayout table2Layout = new TableColumnLayout();
        table2Composite.setLayout(table2Layout);
        
        tableHash = new Table(table2Composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableHash.setLinesVisible(true);
        tableHash.setHeaderVisible(true);

        TableColumn tblclmnAddress_1 = new TableColumn(tableHash, SWT.NONE);
        table2Layout.setColumnData(tblclmnAddress_1, new ColumnWeightData(15, 90, true));
        tblclmnAddress_1.setToolTipText("");
        tblclmnAddress_1.setText(Messages.SignaturResult_tblAdr);

        TableColumn tblclmnHex_1 = new TableColumn(tableHash, SWT.NONE);
        table2Layout.setColumnData(tblclmnHex_1, new ColumnWeightData(60, 250, true));
        tblclmnHex_1.setText(Messages.SignaturResult_tblHex);

        TableColumn tblclmnAscii_1 = new TableColumn(tableHash, SWT.NONE);
        table2Layout.setColumnData(tblclmnAscii_1, new ColumnWeightData(25, 150, true));
        tblclmnAscii_1.setText(Messages.SignaturResult_tblAscii);

        if (hashInst.hashHex != null) {
            int len2 = hashInst.getHashHex().length();
            String asciistr2 = convertHexToString(hashInst.getHashHex());
            int lenAscii2 = asciistr2.length();

            // shows only 6 rows - optimize performance
            for (int i2 = 0; i2 < 6; i2++) {
                // Create one item for each row
                TableItem item = new TableItem(tableHash, SWT.NONE);

                int start2 = i2 * (stepSize * 2);
                int end2 = i2 * (stepSize * 2) + (stepSize * 2);
                end2 = end2 >= len2 ? len2 : end2;

                int startascii2 = start2 / 2;
                int endascii2 = (end2 / 2) >= lenAscii2 ? lenAscii2 : (end2 / 2);

                if ((start2 < end2) && (startascii2 < endascii2)) {
                    // column 1 - address
                    item.setText(0, getAddress(i2, stepSize));// Column, string

                    // column 2 - hex
                    StringBuffer bufferD1 = new StringBuffer();
                    for (int n1 = 0; n1 < (end2 - start2) / 2; n1++) {
                        bufferD1.append(hashInst.getHashHex().charAt((2 * n1) + start2));
                        bufferD1.append(hashInst.getHashHex().charAt((2 * n1 + 1) + start2));
                        bufferD1.append(" ");
                    }
                    item.setText(1, bufferD1.toString());

                    // column 3 - ascii
                    StringBuffer bufferD2 = new StringBuffer();
                    bufferD2.append(asciistr2, startascii2, endascii2);
                    item.setText(2, bufferD2.toString());
                } else {
                    i2 = 10;
                }
            }
     
            //Context menu for coyping table to clipboard
            Menu table2Menu = new Menu(tableHash);
            tableHash.setMenu(table2Menu);
            
            MenuItem copyLineTable2 = new MenuItem(table2Menu, SWT.NONE);
            copyLineTable2.setText(Messages.CopyLine);
            copyLineTable2.addSelectionListener(new SelectionListener() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				if (tableHash.getSelectionCount() > 0) {
    					TableItem selection = tableHash.getSelection()[0];
    					String address = "Address: " + selection.getText(0) + "\n";
    					String hex = "Hex: " + selection.getText(1) + "\n";
    					String ascii = "Ascii: " + selection.getText(2);
    					String result = address + hex + ascii;
    					clipboard.setContents(new Object[] {result}, new Transfer[] {TextTransfer.getInstance()});
    				}
    			}
    			@Override
    			public void widgetDefaultSelected(SelectionEvent e) {}      	
            });
            
            MenuItem copyAllTable2 = new MenuItem(table2Menu, SWT.NONE);
            copyAllTable2.setText(Messages.CopyAll);
            copyAllTable2.addSelectionListener(new SelectionListener() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				TableItem[] tableContents = tableHash.getItems();
    				String result = "";
    				for (TableItem item : tableContents) {
    					String address = "Address: " + item.getText(0) + "\n";
    					String hex = "Hex: " + item.getText(1) + "\n";
    					String ascii = "Ascii: " + item.getText(2);
    					String lineResult = address + hex + ascii + "\n\n";
    					result+= lineResult;
    				}
    				clipboard.setContents(new Object[] {result}, new Transfer[] {TextTransfer.getInstance()});
    			}
    			@Override
    			public void widgetDefaultSelected(SelectionEvent e) {}    	
            });
        }


        if (hashBitSize != -1) {
            Label hashLength = new Label(composite, SWT.READ_ONLY);
            hashLength.setText(Messages.SignaturResult_lengthMessage + hashBitSize + " bit");
            hashLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        }

//        Label infoText = new Label(composite, SWT.WRAP | SWT.CENTER | SWT.V_SCROLL);
//        infoText.setAlignment(SWT.LEFT);
//        GridData gd_infoText = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
//        gd_infoText.verticalIndent = 20;
//        infoText.setLayoutData(gd_infoText);
//        infoText.setText(Messages.SignaturResult_editorDescripton);
//        infoText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        
        //Button Composite
        Composite btnComp = new Composite(composite, SWT.NONE);
        btnComp.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 2, 1));
        btnComp.setLayout(new RowLayout());

//        Button btnVerificationModels = new Button(btnComp, SWT.NONE);
//        btnVerificationModels.setText(Messages.SignaturResult_btnVerificationModels);
//        btnVerificationModels.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//                // ToDo
//                try {
//                    sigVerView.createTabItem();
//                    sigVerView.changeTab();
//                } catch (Exception ex) {
//                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
//                }
//
//            }
//        });

        // close window
        Button btnClose = new Button(btnComp, SWT.NONE);
        btnClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SignaturResult.this.close();
            }
        });
        btnClose.setText(Messages.SignaturResult_btnClose);

        
        //Add selection listeners
        btnOkt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setText(input.getSignatureOct());
                tableSig.setVisible(false);
                gd_table1Composite.exclude = true;
                txtSigNum.setVisible(true);
                gd_txtSigNum.exclude = false;
                sigComp.layout();
            }
        });

        btnDec.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setText(hexToDecimal(input.getSignatureHex()));
                tableSig.setVisible(false);
                gd_table1Composite.exclude = true;
                txtSigNum.setVisible(true);
                gd_txtSigNum.exclude = false;
                sigComp.layout();
            }
        });

        btnHex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setText(input.getSignatureHex());
                
                tableSig.setVisible(false);
                gd_table1Composite.exclude = true;
                txtSigNum.setVisible(true);
                gd_txtSigNum.exclude = false;
                sigComp.layout();
            }
        });

        btnHexdump.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setVisible(false);
                tableSig.setVisible(true);
                gd_txtSigNum.exclude = true;
                gd_table1Composite.exclude = false;
                sigComp.layout();
            }
        });   
        
        createContents();
    }

    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText(Messages.SignaturResult_title);
        setSize(computeSize(660, SWT.DEFAULT));
        setMinimumSize(computeSize(660, SWT.DEFAULT));

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * Returns a string to get the address in the hex-dump-table.
     * 
     * @param i Row of table
     * @param stepSize Difference between digits in the row.
     * @return a string containing the address in the table
     */
    protected String getAddress(int i, int stepSize) {
        return String.format("%05X", (i * stepSize) & 0xFFFFF);
    }

    /**
     * Returns the ascii representation of an hexadecimal string.
     * 
     * @param hex
     * @return a string containing the ascii representation
     */
    public String convertHexToString(String hex) {
    	if (hex == null) return "";
    	
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2) {

            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);
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
    	if (hex == null) return "";
    	
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2) {
            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            sb.append(decimal);
        }
        return sb.toString();
    }
}