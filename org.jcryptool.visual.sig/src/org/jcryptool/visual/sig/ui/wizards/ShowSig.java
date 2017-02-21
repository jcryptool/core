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
    private Table table_1;

    private int signatureLengh = Input.signature.length * 8;
    private int dataLength = Input.data.length * 8;

    /**
     * Create the shell.
     * 
     * @param display
     */
    public ShowSig(Display display, String signatureInformation) {
        super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE | SWT.APPLICATION_MODAL);

        Composite composite = new Composite(this, SWT.NONE);
        composite.setBounds(10, 10, 485, 661);

        Label owner = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
        owner.setText(Messages.ShowSig_ownerTitle);
        owner.setBounds(0, 0, 176, 21);

        Label key = new Label(composite, SWT.READ_ONLY);
        key.setText(Messages.ShowSig_keyTitle);
        key.setBounds(0, 24, 176, 21);

        Label signatureMethod = new Label(composite, SWT.READ_ONLY);
        signatureMethod.setText(Messages.ShowSig_methodTitle);
        signatureMethod.setBounds(0, 48, 176, 21);

        String userName = "-";

        if (Input.key != null) {
            userName = Input.key.getContactName();
        } else if (Input.privateKey != null) {
            userName = Input.privateKey.getContactName();
        }

        Label username = new Label(composite, SWT.READ_ONLY | SWT.WRAP);
        username.setText(userName);
        username.setBounds(182, 0, 302, 21);

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

        Label signatureInfo = new Label(composite, SWT.READ_ONLY);
        signatureInfo.setText(signatureInformation);
        signatureInfo.setBounds(182, 48, 302, 21);

        Label signature = new Label(composite, SWT.READ_ONLY);
        signature.setText(Messages.ShowSig_grpSignature);
        signature.setBounds(0, 77, 137, 21);

        Label signatureLength = new Label(composite, SWT.READ_ONLY);
        signatureLength.setText(Messages.ShowSig_lengthSig + signatureLengh + " Bits");
        signatureLength.setBounds(0, 253, 430, 21);

        Group options = new Group(composite, SWT.NONE);
        options.setText(Messages.ShowSig_grpOption);
        options.setBounds(0, 280, 484, 73);
        options.setLayout(null);

        Label signedMessage = new Label(composite, SWT.READ_ONLY);
        signedMessage.setText(Messages.ShowSig_grpMessage);
        signedMessage.setBounds(0, 373, 137, 21);

        Label signedMessageLength = new Label(composite, SWT.READ_ONLY);
        signedMessageLength.setText(Messages.ShowSig_lengthMessage + dataLength + " Bits");
        signedMessageLength.setBounds(0, 548, 430, 21);

        // create table to show the generated signature
        table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setBounds(0, 98, 484, 151);

        TableColumn tblclmnAddress = new TableColumn(table, SWT.NONE);
        tblclmnAddress.setResizable(false);
        tblclmnAddress.setWidth(60);
        tblclmnAddress.setToolTipText("");
        tblclmnAddress.setText(Messages.ShowSig_tblAdr);

        TableColumn tblclmnHex = new TableColumn(table, SWT.NONE);
        tblclmnHex.setResizable(false);
        tblclmnHex.setWidth(250);
        tblclmnHex.setText(Messages.ShowSig_tblHex);

        TableColumn tblclmnAscii = new TableColumn(table, SWT.NONE);
        tblclmnAscii.setResizable(false);
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

        // create table to show signed message
        table_1 = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        table_1.setLinesVisible(true);
        table_1.setHeaderVisible(true);
        table_1.setBounds(0, 394, 484, 150);

        TableColumn tblclmnAddress_1 = new TableColumn(table_1, SWT.NONE);
        tblclmnAddress_1.setResizable(false);
        tblclmnAddress_1.setWidth(60);
        tblclmnAddress_1.setToolTipText("");
        tblclmnAddress_1.setText(Messages.ShowSig_tblAdr);

        TableColumn tblclmnHex_1 = new TableColumn(table_1, SWT.NONE);
        tblclmnHex_1.setResizable(false);
        tblclmnHex_1.setWidth(250);
        tblclmnHex_1.setText(Messages.ShowSig_tblHex);

        TableColumn tblclmnAscii_1 = new TableColumn(table_1, SWT.NONE);
        tblclmnAscii_1.setResizable(false);
        tblclmnAscii_1.setWidth(150);
        tblclmnAscii_1.setText(Messages.ShowSig_tblAscii);

        int len2 = Input.dataHex.length();
        String asciistr2 = convertHexToString(Input.dataHex);
        int lenAscii2 = asciistr2.length();

        // shows only 6 rows - optimize performance
        for (int i2 = 0; i2 < 6; i2++) {
            // Create one item for each row
            TableItem item = new TableItem(table_1, SWT.NONE);

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

        // text field to show signature as hex, octal or decimal
        final Label txtSigNum = new Label(composite, SWT.BORDER | SWT.WRAP);
        txtSigNum.setBounds(0, 98, 484, 151);
        txtSigNum.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

        // display options
        Button btnOkt = new Button(options, SWT.RADIO);
        btnOkt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setVisible(false);
                txtSigNum.setVisible(true);
                txtSigNum.setText(Input.signatureOct);
            }
        });
        btnOkt.setBounds(186, 30, 70, 16);
        btnOkt.setText(Messages.ShowSig_octal);

        Button btnDez = new Button(options, SWT.RADIO);
        btnDez.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setVisible(false);
                txtSigNum.setVisible(true);
                txtSigNum.setText(hexToDecimal(Input.signatureHex));
            }
        });
        btnDez.setBounds(262, 30, 80, 16);
        btnDez.setText(Messages.ShowSig_decimal);

        Button btnHex = new Button(options, SWT.RADIO);
        btnHex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setVisible(false);
                txtSigNum.setVisible(true);
                txtSigNum.setText(Input.signatureHex);
            }
        });
        btnHex.setBounds(348, 30, 70, 16);
        btnHex.setText(Messages.ShowSig_hex);

        Button btnHexdump = new Button(options, SWT.RADIO);
        btnHexdump.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setVisible(false);
                table.setVisible(true);
            }
        });
        btnHexdump.setSelection(true);
        btnHexdump.setBounds(10, 30, 170, 16);
        btnHexdump.setText(Messages.ShowSig_hexDump);

        // close window
        Button btnNewButton = new Button(composite, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ShowSig.this.close();
            }
        });
        btnNewButton.setBounds(389, 633, 95, 28);
        btnNewButton.setText(Messages.ShowSig_btnClose);

        Label lblTextopeneditor = new Label(composite, SWT.WRAP | SWT.CENTER);
        lblTextopeneditor.setAlignment(SWT.LEFT);
        lblTextopeneditor.setBounds(2, 584, 475, 32);
        lblTextopeneditor.setText(Messages.ShowSig_editorDescripton);
        lblTextopeneditor.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

        Button btnSave = new Button(composite, SWT.NONE);
        btnSave.setBounds(289, 633, 95, 28);
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

        createContents();
    }

    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText(Messages.ShowSig_title);
        setSize(512, 710);

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