package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
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
        super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE | SWT.APPLICATION_MODAL);
        this.input = input;
        this.hashInst = hashInst;
        this.sigVerification = sigVerification;
        this.signatureLengh = input.signatureSize;
        this.hashBitSize = hashInst.hashHex.length() * 8;
        this.sigVerView = sigVerView;
        input.setSignatureHex();
        input.setSignatureOct();

        Composite composite = new Composite(this, SWT.NONE);
        composite.setBounds(10, 10, 485, 623);

        Label key = new Label(composite, SWT.READ_ONLY);
        key.setText(Messages.SignaturResult_keyTitle);
        key.setBounds(0, 0, 237, 21);

        Label signatureMethod = new Label(composite, SWT.READ_ONLY);
        signatureMethod.setText(Messages.SignaturResult_methodTitle);
        signatureMethod.setBounds(0, 27, 176, 21);

        Label keyType = new Label(composite, SWT.READ_ONLY);
        if (sigVerification.alias == null) {
            if (input.signaturemethod.contains("ECDSA")) {
                keyType.setText("ANSI X9.62 prime256v1 (256 bits)");
            } else {
                keyType.setText("-");
            }
        } else {
            keyType.setText(sigVerification.alias.getClassName());
        }
        keyType.setBounds(244, 0, 302, 21);

        Label signatureInfo = new Label(composite, SWT.READ_ONLY);
        signatureInfo.setText(input.signaturemethod);
        signatureInfo.setBounds(244, 27, 302, 21);

        Label signature = new Label(composite, SWT.READ_ONLY);
        signature.setText(Messages.SignaturResult_grpSignature);
        signature.setBounds(0, 116, 137, 21);

        Label signatureLength = new Label(composite, SWT.READ_ONLY);
        signatureLength.setText(Messages.SignaturResult_lengthSig + signatureLengh + " Bits");
        signatureLength.setBounds(0, 292, 430, 21);

        Label hash = new Label(composite, SWT.READ_ONLY);
        hash.setText(Messages.SignaturResult_grpMessage);
        hash.setBounds(0, 319, 137, 21);

        Label hashLength = new Label(composite, SWT.READ_ONLY);
        hashLength.setText(Messages.SignaturResult_lengthMessage + hashBitSize + " Bits");
        hashLength.setBounds(0, 494, 430, 21);

        // create table to show the generated signature
        tableSig = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableSig.setLinesVisible(true);
        tableSig.setHeaderVisible(true);
        tableSig.setBounds(0, 135, 484, 151);

        TableColumn tblclmnAddress = new TableColumn(tableSig, SWT.NONE);
        tblclmnAddress.setResizable(false);
        tblclmnAddress.setWidth(60);
        tblclmnAddress.setToolTipText("");
        tblclmnAddress.setText(Messages.SignaturResult_tblAdr);

        TableColumn tblclmnHex = new TableColumn(tableSig, SWT.NONE);
        tblclmnHex.setResizable(false);
        tblclmnHex.setWidth(250);
        tblclmnHex.setText(Messages.SignaturResult_tblHex);

        TableColumn tblclmnAscii = new TableColumn(tableSig, SWT.NONE);
        tblclmnAscii.setResizable(false);
        tblclmnAscii.setWidth(150);
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
        // create table to show hash
        tableHash = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableHash.setLinesVisible(true);
        tableHash.setHeaderVisible(true);
        tableHash.setBounds(0, 340, 484, 150);

        TableColumn tblclmnAddress_1 = new TableColumn(tableHash, SWT.NONE);
        tblclmnAddress_1.setResizable(false);
        tblclmnAddress_1.setWidth(60);
        tblclmnAddress_1.setToolTipText("");
        tblclmnAddress_1.setText(Messages.SignaturResult_tblAdr);

        TableColumn tblclmnHex_1 = new TableColumn(tableHash, SWT.NONE);
        tblclmnHex_1.setResizable(false);
        tblclmnHex_1.setWidth(250);
        tblclmnHex_1.setText(Messages.SignaturResult_tblHex);

        TableColumn tblclmnAscii_1 = new TableColumn(tableHash, SWT.NONE);
        tblclmnAscii_1.setResizable(false);
        tblclmnAscii_1.setWidth(150);
        tblclmnAscii_1.setText(Messages.SignaturResult_tblAscii);

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

        // close window
        Button btnNewButton = new Button(composite, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SignaturResult.this.close();
            }
        });
        btnNewButton.setBounds(389, 590, 95, 28);
        btnNewButton.setText(Messages.SignaturResult_btnClose);

        Text txtTextopeneditor = new Text(composite, SWT.WRAP);
        txtTextopeneditor.setEditable(false);
        txtTextopeneditor.setBounds(2, 540, 475, 41);
        txtTextopeneditor.setText(Messages.SignaturResult_editorDescripton);
        txtTextopeneditor.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

        Group group = new Group(composite, SWT.NONE);
        group.setLayout(null);
        group.setText("Optionen für Signaturen anzeigen");
        group.setBounds(0, 54, 484, 59);

        // text field to show signature as hex, octal or decimal
        final Label txtSigNum = new Label(composite, SWT.BORDER | SWT.WRAP);
        txtSigNum.setBounds(0, 135, 484, 151);
        txtSigNum.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

        Button btnOkt = new Button(group, SWT.RADIO);
        btnOkt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tableSig.setVisible(false);
                txtSigNum.setVisible(true);
                txtSigNum.setText(input.getSignatureOct());
            }
        });
        btnOkt.setText("Octal");
        btnOkt.setBounds(186, 16, 70, 16);

        Button btnDec = new Button(group, SWT.RADIO);
        btnDec.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tableSig.setVisible(false);
                txtSigNum.setVisible(true);
                txtSigNum.setText(hexToDecimal(input.getSignatureHex()));
            }
        });
        btnDec.setText("Decimal");
        btnDec.setBounds(262, 16, 80, 16);

        Button btnHex = new Button(group, SWT.RADIO);
        btnHex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tableSig.setVisible(false);
                txtSigNum.setVisible(true);
                txtSigNum.setText(input.getSignatureHex());
            }
        });
        btnHex.setText("Hex");
        btnHex.setBounds(348, 16, 70, 16);

        Button btnHexdump = new Button(group, SWT.RADIO);
        btnHexdump.setText("Hex dump (hex and ascii)");
        btnHexdump.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtSigNum.setVisible(false);
                tableSig.setVisible(true);
            }
        });
        btnHexdump.setSelection(true);
        btnHexdump.setBounds(10, 16, 170, 16);

        Button btnVerificationModels = new Button(composite, SWT.NONE);
        btnVerificationModels.setBounds(0, 593, 137, 25);
        btnVerificationModels.setText(Messages.SignaturResult_btnVerificationModels);
        btnVerificationModels.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // ToDo
                try {
                    sigVerView.createTabItem();
                    sigVerView.changeTab();
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }

            }
        });
        createContents();
    }

    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText(Messages.SignaturResult_title);
        setSize(512, 666);

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