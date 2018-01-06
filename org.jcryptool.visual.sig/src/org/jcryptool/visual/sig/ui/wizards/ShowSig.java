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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;
import org.jcryptool.visual.sig.algorithm.Input;

public class ShowSig extends Shell {
    private Table table;
    private Table tableSignedMessage;

    private int signatureLengh = Input.signature.length * 8;
    private int dataLength = Input.data.length * 8;

    /**
     * Create the shell.
     * 
     * @param display
     */
    public ShowSig(Display display, String signatureInformation) {
        super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE | SWT.APPLICATION_MODAL);
        setLayout(new GridLayout());

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
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
                keyType.setText("ANSI X9.62 prime256v1 (256 bits)");
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
        GridData gd_sigComp = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        sigComp.setLayoutData(gd_sigComp);
        GridLayout gl_sigComp = new GridLayout();
        gl_sigComp.marginHeight = 0;
        gl_sigComp.marginWidth = 0;
        sigComp.setLayout(gl_sigComp);
        
        // text field to show signature as hex, octal or decimal
        Label txtSigNum = new Label(sigComp, SWT.BORDER | SWT.WRAP );
        GridData gd_txtSigNum = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gd_txtSigNum.widthHint = 490;
        gd_txtSigNum.heightHint = 151;
        gd_txtSigNum.exclude = true;
        txtSigNum.setVisible(false);
        txtSigNum.setLayoutData(gd_txtSigNum);
        txtSigNum.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        
        // create table to show the generated signature
        table = new Table(sigComp, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gd_table.widthHint = 490;
        gd_table.heightHint = 151;
        table.setLayoutData(gd_table);

        TableColumn tblclmnAddress = new TableColumn(table, SWT.NONE);
        tblclmnAddress.setResizable(true);
        tblclmnAddress.setWidth(90);
        tblclmnAddress.setToolTipText("");
        tblclmnAddress.setText(Messages.ShowSig_tblAdr);

        TableColumn tblclmnHex = new TableColumn(table, SWT.NONE);
        tblclmnHex.setResizable(true);
        tblclmnHex.setWidth(250);
        tblclmnHex.setText(Messages.ShowSig_tblHex);

        TableColumn tblclmnAscii = new TableColumn(table, SWT.NONE);
        tblclmnAscii.setResizable(true);
        tblclmnAscii.setWidth(150);
        tblclmnAscii.setText(Messages.ShowSig_tblAscii);

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

        Label signatureLength = new Label(composite, SWT.READ_ONLY);
        signatureLength.setText(Messages.ShowSig_lengthSig + signatureLengh + " Bits");
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
                gd_table.exclude = true;
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
                gd_table.exclude = true;
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
                gd_table.exclude = true;
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
                gd_table.exclude = false;
                sigComp.layout();
            }
        });
        btnHexdump.setSelection(true);
        btnHexdump.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        btnHexdump.setText(Messages.ShowSig_hexDump);

        Label signedMessage = new Label(composite, SWT.READ_ONLY);
        signedMessage.setText(Messages.ShowSig_grpMessage);
        signedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

        // create table to show signed message
        tableSignedMessage = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableSignedMessage.setLinesVisible(true);
        tableSignedMessage.setHeaderVisible(true);
        GridData gd_tableSignedMessage = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gd_tableSignedMessage.widthHint = 490;
        gd_tableSignedMessage.heightHint = 150;
        tableSignedMessage.setLayoutData(gd_tableSignedMessage);

        TableColumn tblclmnAddress_1 = new TableColumn(tableSignedMessage, SWT.NONE);
        tblclmnAddress_1.setResizable(true);
        tblclmnAddress_1.setWidth(90);
        tblclmnAddress_1.setToolTipText("");
        tblclmnAddress_1.setText(Messages.ShowSig_tblAdr);

        TableColumn tblclmnHex_1 = new TableColumn(tableSignedMessage, SWT.NONE);
        tblclmnHex_1.setResizable(true);
        tblclmnHex_1.setWidth(250);
        tblclmnHex_1.setText(Messages.ShowSig_tblHex);

        TableColumn tblclmnAscii_1 = new TableColumn(tableSignedMessage, SWT.NONE);
        tblclmnAscii_1.setResizable(true);
        tblclmnAscii_1.setWidth(150);
        tblclmnAscii_1.setText(Messages.ShowSig_tblAscii);

        int len2 = Input.dataHex.length();
        String asciistr2 = convertHexToString(Input.dataHex);
        int lenAscii2 = asciistr2.length();

        // shows only 6 rows - optimize performance
        for (int i2 = 0; i2 < 6; i2++) {
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
                i2 = 10;
            }
        }

        Label signedMessageLength = new Label(composite, SWT.READ_ONLY);
        signedMessageLength.setText(Messages.ShowSig_lengthMessage + dataLength + " Bits");
        signedMessageLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        
        Label lblTextopeneditor = new Label(composite, SWT.WRAP | SWT.CENTER | SWT.V_SCROLL);
        lblTextopeneditor.setAlignment(SWT.LEFT);
        GridData gd_lblTextopeneditor = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        lblTextopeneditor.setLayoutData(gd_lblTextopeneditor);
        lblTextopeneditor.setText(Messages.ShowSig_editorDescripton);
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
                dialog.setFileName("signature_and_message");
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

                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
                            | SWT.OK);
                    messageBox.setText(Messages.ShowSig_MessageBoxTitle);
                    messageBox.setMessage(Messages.ShowSig_MessageBoxText);
                    messageBox.open();
                }
            }
        });

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