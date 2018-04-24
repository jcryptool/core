//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.wizards;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;
import org.jcryptool.visual.sig.algorithm.Input;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.eclipse.core.runtime.Path;

public class ShowSig extends Shell {
    private Table table;
    private Table tableSignedMessage;

    private int signatureLengh = Input.signature.length * 8;
    private int dataLength = Input.data.length * 8;
	private Button btnOpenInHexEditor;

    /**
     * Create the shell.
     * 
     * @param display
     */
    public ShowSig(Display display, String signatureInformation) {
    	super(display, SWT.SHELL_TRIM);
        setLayout(new GridLayout());
        
        Clipboard clipboard = new Clipboard(display);

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(2, false));

        Label owner = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
        owner.setText(Messages.ShowSig_ownerTitle);
        owner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        String userName = "-";
        if (Input.key != null) {
            userName = Input.key.getContactName();
        } else if (Input.privateKey != null) {
            userName = Input.privateKey.getContactName();
        }
        Label username = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
        username.setText(userName);
        username.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Label key = new Label(composite, SWT.READ_ONLY);
        key.setText(Messages.ShowSig_keyTitle);
        key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Label keyType = new Label(composite, SWT.READ_ONLY);
        if (Input.privateKey == null && Input.key == null) {
            if (signatureInformation.contains("ECDSA")) {
                keyType.setText("ANSI X9.62 prime256v1 (256 bit)");
            } else {
                keyType.setText("-");
            }
        } else {
            if (Input.key != null) {
                keyType.setText(Input.key.getClassName());
            } else {
                keyType.setText(Input.privateKey.getClassName());
            }
        }
        keyType.setBounds(182, 24, 302, 21);
        keyType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        Label signatureMethod = new Label(composite, SWT.READ_ONLY);
        signatureMethod.setText(Messages.ShowSig_methodTitle);
        signatureMethod.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Label signatureInfo = new Label(composite, SWT.READ_ONLY);
        signatureInfo.setText(signatureInformation);
        signatureInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Label signature = new Label(composite, SWT.READ_ONLY);
        signature.setText(Messages.ShowSig_grpSignature);
        signature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        
        Composite sigComp = new Composite(composite, SWT.NONE);
        GridData gd_sigComp = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        sigComp.setLayoutData(gd_sigComp);
        GridLayout gl_sigComp = new GridLayout();
        gl_sigComp.marginHeight = 0;
        gl_sigComp.marginWidth = 0;
        sigComp.setLayout(gl_sigComp);
        
        // text field to show signature as hex, octal or decimal
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
        copy.setText(Messages.ShowSig_ContextCopy);
        copy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() > 0) {
					clipboard.setContents(new Object[] {txtSigNum.getText()}, new Transfer[] {TextTransfer.getInstance()});
				}
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
        
        table = new Table(table1Composite, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        TableColumn tblclmnAddress = new TableColumn(table, SWT.NONE);
        tblclmnAddress.setText(Messages.ShowSig_tblAdr);
        table1Layout.setColumnData(tblclmnAddress, new ColumnWeightData(15, 90, true));

        TableColumn tblclmnHex = new TableColumn(table, SWT.NONE);
        tblclmnHex.setText(Messages.ShowSig_tblHex);
        table1Layout.setColumnData(tblclmnHex, new ColumnWeightData(60, 250, true));

        TableColumn tblclmnAscii = new TableColumn(table, SWT.NONE);
        tblclmnAscii.setText(Messages.ShowSig_tblAscii);
        table1Layout.setColumnData(tblclmnAscii, new ColumnWeightData(25, 150, true));

        int stepSize = 14;

        int len1 = Input.signatureHex.length();
        String asciistr1 = convertHexToString(Input.signatureHex);
        int lenAscii1 = asciistr1.length();

        for (int i1 = 0; i1 < (Math.ceil((double) len1 / (stepSize * 2))); i1++) {
            TableItem item = new TableItem(table, SWT.NONE);

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
                    bufferS1.append(Input.signatureHex.charAt((2 * m1) + start1));
                    bufferS1.append(Input.signatureHex.charAt((2 * m1 + 1) + start1));
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
        
        //Context menu for coyping table content to clipboard
        Menu tableMenu = new Menu(table);
        table.setMenu(tableMenu);
        MenuItem copyLine = new MenuItem(tableMenu, SWT.NONE);
        copyLine.setText(Messages.ShowSig_ContextCopySelection);
        copyLine.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() > 0) {
					TableItem selection = table.getSelection()[0];
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
        copyAll.setText(Messages.ShowSig_ContexstCopyAll);
        copyAll.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] tableContents = table.getItems();
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
        signatureLength.setText(Messages.ShowSig_lengthSig + signatureLengh + " bit");
        signatureLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

        Group options = new Group(composite, SWT.NONE);
        options.setText(Messages.ShowSig_grpOption);
        options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        options.setLayout(new GridLayout(2, true));
        
        // display options
        Button btnOkt = new Button(options, SWT.RADIO);
        btnOkt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setVisible(false);
                gd_table1Composite.exclude = true;
                txtSigNum.setVisible(true);
                gd_txtSigNum.exclude = false;
                txtSigNum.setText(Input.signatureOct);
                sigComp.layout();
                
            }
        });
        btnOkt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        btnOkt.setText(Messages.ShowSig_octal);

        Button btnDez = new Button(options, SWT.RADIO);
        btnDez.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setVisible(false);
                gd_table1Composite.exclude = true;
                txtSigNum.setVisible(true);
                gd_txtSigNum.exclude = false;
                txtSigNum.setText(hexToDecimal(Input.signatureHex));
                sigComp.layout();
            }
        });
        btnDez.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        btnDez.setText(Messages.ShowSig_decimal);

        Button btnHex = new Button(options, SWT.RADIO);
        btnHex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setVisible(false);
                gd_table1Composite.exclude = true;
                txtSigNum.setVisible(true);
                gd_txtSigNum.exclude = false;
                txtSigNum.setText(Input.signatureHex);
                sigComp.layout();
            }
        });
        btnHex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        btnHex.setText(Messages.ShowSig_hex);

        Button btnHexdump = new Button(options, SWT.RADIO);
        btnHexdump.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setVisible(false);
                gd_txtSigNum.exclude = true;
                table.setVisible(true);
                gd_table1Composite.exclude = false;
                sigComp.layout();
            }
        });
        btnHexdump.setSelection(true);
        btnHexdump.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        btnHexdump.setText(Messages.ShowSig_hexDump);

        Label signedMessage = new Label(composite, SWT.READ_ONLY);
        if (Input.data.length > Input.dataHex.length()) {
        	signedMessage.setText(Messages.ShowSig_grpMessage + Messages.ShowSig_grpMessage_first10kB);
        } else {
        	signedMessage.setText(Messages.ShowSig_grpMessage);
        }
        GridData gd_signedMessage = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
        gd_signedMessage.verticalIndent = 20;
        signedMessage.setLayoutData(gd_signedMessage);

        // create table to show signed message
        Composite table2Composite = new Composite(composite, SWT.NONE);
        GridData gd_table2Composite = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_table2Composite.widthHint = 490;
        gd_table2Composite.heightHint = 151;
        table2Composite.setLayoutData(gd_table2Composite);
        TableColumnLayout table2Layout = new TableColumnLayout();
        table2Composite.setLayout(table2Layout);
        
        tableSignedMessage = new Table(table2Composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableSignedMessage.setLinesVisible(true);
        tableSignedMessage.setHeaderVisible(true);

        TableColumn tblclmnAddress_1 = new TableColumn(tableSignedMessage, SWT.NONE);
        tblclmnAddress_1.setText(Messages.ShowSig_tblAdr);
        table2Layout.setColumnData(tblclmnAddress_1, new ColumnWeightData(15, 90, true));

        TableColumn tblclmnHex_1 = new TableColumn(tableSignedMessage, SWT.NONE);
        tblclmnHex_1.setText(Messages.ShowSig_tblHex);
        table2Layout.setColumnData(tblclmnHex_1, new ColumnWeightData(60, 250, true));

        TableColumn tblclmnAscii_1 = new TableColumn(tableSignedMessage, SWT.NONE);
        tblclmnAscii_1.setText(Messages.ShowSig_tblAscii);
        table2Layout.setColumnData(tblclmnAscii_1, new ColumnWeightData(25, 150, true));

        int len2 = Input.dataHex.length();
        String asciistr2 = convertHexToString(Input.dataHex);
        int lenAscii2 = asciistr2.length();

        for (int i2= 0; i2 < len2; i2++) {
            // Create one item for each row
            TableItem item = new TableItem(tableSignedMessage, SWT.NONE);

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
                    bufferD1.append(Input.dataHex.charAt((2 * n1) + start2));
                    bufferD1.append(Input.dataHex.charAt((2 * n1 + 1) + start2));
                    bufferD1.append(" ");
                }
                item.setText(1, bufferD1.toString());

                // column 3 - ascii
                StringBuffer bufferD2 = new StringBuffer();
                bufferD2.append(asciistr2, startascii2, endascii2);
                item.setText(2, bufferD2.toString());
            } else {
            	break;
            }
        }
        
        //Context menu for coyping table to clipboard
        Menu table2Menu = new Menu(tableSignedMessage);
        tableSignedMessage.setMenu(table2Menu);
        
        MenuItem copyLineTable2 = new MenuItem(table2Menu, SWT.NONE);
        copyLineTable2.setText(Messages.ShowSig_ContextCopySelection);
        copyLineTable2.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableSignedMessage.getSelectionCount() > 0) {
					TableItem selection = tableSignedMessage.getSelection()[0];
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
        copyAllTable2.setText(Messages.ShowSig_ContexstCopyAll);
        copyAllTable2.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] tableContents = tableSignedMessage.getItems();
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

        Label signedMessageLength = new Label(composite, SWT.READ_ONLY);
        signedMessageLength.setText(Messages.ShowSig_lengthMessage + dataLength + " bit");
        signedMessageLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        
        Label lblTextopeneditor = new Label(composite, SWT.WRAP | SWT.CENTER | SWT.V_SCROLL);
        lblTextopeneditor.setAlignment(SWT.LEFT);
        GridData gd_lblTextopeneditor = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        lblTextopeneditor.setLayoutData(gd_lblTextopeneditor);
        if (Input.s != 1) {
        	lblTextopeneditor.setText(Messages.ShowSig_editorDescripton + "\n\n" + Messages.ShowSig_randomizedMethods);
        } else {
        	lblTextopeneditor.setText(Messages.ShowSig_editorDescripton);
        }
        lblTextopeneditor.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        
        //Button Composite
        Composite btnComp = new Composite(composite, SWT.NONE);
        btnComp.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 2, 1));
        btnComp.setLayout(new RowLayout());

        //save button
        Button btnSave = new Button(btnComp, SWT.NONE);
        btnSave.setText(Messages.ShowSig_btnSave);
        btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Open File save dialog
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFileName("signature_and_message.bin"); //NON-NLS-1$
                String savePath = dialog.open();
                // Write the file
                if (savePath != null) {
                    try {
                        OutputStream os = null;
                        try {
                            ByteBuffer b = ByteBuffer.allocate(4);
                            b.putInt(signatureLengh);
                            os = new BufferedOutputStream(new FileOutputStream(savePath));
                            os.write(b.array());
                            os.write(Input.signature);
                            os.write(Input.data);
                            
                            Input.savePath = savePath;
                            btnOpenInHexEditor.setEnabled(true);

                            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                                    | SWT.OK);
                            messageBox.setText(Messages.ShowSig_MessageBoxTitle);
                            messageBox.setMessage(Messages.ShowSig_MessageBoxText + " " + savePath);
                            messageBox.open();
                        } finally {
                            if (os != null) {
                                os.close();
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                    } catch (IOException ex) {
                        LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
                    }
                }
            }
        });
        
        btnOpenInHexEditor = new Button(btnComp, SWT.NONE);
        btnOpenInHexEditor.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		if (Input.savePath != null) {
        			try {
        			PathEditorInput pathEditorInput = new PathEditorInput(new Path(Input.savePath));
        			if (pathEditorInput.exists()) {
            			EditorsManager.getInstance().openNewHexEditor(pathEditorInput);
        			} else {
                        btnOpenInHexEditor.setEnabled(false);
        			}
					} catch (PartInitException e1) {
						 LogUtil.logError(SigPlugin.PLUGIN_ID, e1);
					}
        		} else {
        			btnOpenInHexEditor.setEnabled(false);
        		}
        	}
        });
        btnOpenInHexEditor.setText(Messages.ShowSig_btnOpen);
        btnOpenInHexEditor.setEnabled(Input.savePath != null);

        // close window button
        Button btnNewButton = new Button(btnComp, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ShowSig.this.close();
            }
        });
        btnNewButton.setText(Messages.ShowSig_btnClose);

        createContents();
    }

    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText(Messages.ShowSig_title);
        setSize(computeSize(600, SWT.DEFAULT));
        setMinimumSize(computeSize(600, SWT.DEFAULT));

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