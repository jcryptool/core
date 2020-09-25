// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.modern.sha3.SHA3Plugin;

/**
 * Implements the sha3 wizardpage
 *
 * @author Michael Starzer
 *
 */
public class SHA3WizardPage extends WizardPage implements Listener {
    private Composite algorithmGroup;
    private Composite bitlengthGroup;
    private Label algorithmListLabel;
    private Label BitlengthListLabel;
    private Combo AlgorithmCombo;
    private Combo BitlengthCombo;

    private Group SaltGroup;
    private Button Salt;
    private Text SaltValue;
    private String SaltEcho = "";
    /**
     * Option group for choosing create or verify hash
     */
    private Group OptionGroup;
    private Button CreateHash;
    private Button VerifyHash;
    private Text HashValue;
    /**
     * The inserted hash of the user
     */
    private String Hash;

    /**
     * Group for the output mode
     */
    private Group HashOutputGroup;
    private Button HashStream;
    private Button HashFormatted;
    private Button HashHexEditor;
    /**
     * The chosen mode (hexeditor, texteditor, hashstream)
     */
    private int OutputMode;

    private int SHA3Type = 0;
    private int Bitlength = 0;
    private boolean Mode = false;

    public SHA3WizardPage() {
        super(".", "SHA3", null);
        setTitle(Messages.WizardTitle);
        setMessage(Messages.WizardMessage0);
    }

    /**
     * Control-group for the wizard
     */
    public void createControl(Composite parent) {
        Composite pageComposite = new Composite(parent, SWT.NULL);
        createAlgorithmGroup(pageComposite);
        createBitlengthGroup(pageComposite);
        AlgorithmCombo.select(0);
        BitlengthCombo.removeAll();
        BitlengthCombo.add("224");
        BitlengthCombo.add("256");
        BitlengthCombo.add("384");
        BitlengthCombo.add("512");
        BitlengthCombo.select(0);
        createOptionGroup(pageComposite);
        createHashOutputGroup(pageComposite);
        CreateHash.setSelection(true);
        HashValue.setEnabled(false);
        HashHexEditor.setSelection(true);
        pageComposite.setLayout(new GridLayout());
        createSaltGroup(pageComposite);
        setControl(pageComposite);
        setPageComplete(mayFinish());

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), SHA3Plugin.PLUGIN_ID + ".wizardhelp");
    }

    /**
     * Creates a new group for a verify and a hash button
     *
     * @param parent
     */
    private void createOptionGroup(Composite parent) {
        GridLayout OptionGroupGridLayout = new GridLayout();
        OptionGroupGridLayout.numColumns = 3;

        GridData OptionGroupGridData = new GridData();
        OptionGroupGridData.horizontalAlignment = GridData.FILL;
        OptionGroupGridData.grabExcessHorizontalSpace = true;
        OptionGroupGridData.grabExcessVerticalSpace = false;
        OptionGroupGridData.verticalAlignment = GridData.FILL;

        OptionGroup = new Group(parent, SWT.NONE);
        OptionGroup.setLayoutData(OptionGroupGridData);
        OptionGroup.setLayout(OptionGroupGridLayout);
        OptionGroup.setText(Messages.WizardMessage2);

        GridData CreateHashButton = new GridData();
        CreateHashButton.horizontalAlignment = GridData.FILL;
        CreateHashButton.grabExcessHorizontalSpace = false;
        CreateHashButton.grabExcessVerticalSpace = false;
        CreateHashButton.verticalAlignment = GridData.CENTER;

        CreateHash = new Button(OptionGroup, SWT.RADIO);
        CreateHash.setText(Messages.WizardMessage3);
        CreateHash.setLayoutData(CreateHashButton);
        CreateHash.addListener(SWT.Selection, this);

        GridData VerifyHashButton = new GridData();
        VerifyHashButton.horizontalAlignment = GridData.FILL;
        VerifyHashButton.grabExcessHorizontalSpace = false;
        VerifyHashButton.grabExcessVerticalSpace = false;
        VerifyHashButton.verticalAlignment = GridData.CENTER;

        VerifyHash = new Button(OptionGroup, SWT.RADIO);
        VerifyHash.setText(Messages.WizardMessage4);
        VerifyHash.setLayoutData(VerifyHashButton);
        VerifyHash.addListener(SWT.Selection, this);

        GridData SaltGridData = new GridData();
        SaltGridData.horizontalAlignment = GridData.FILL;
        SaltGridData.grabExcessHorizontalSpace = false;
        SaltGridData.grabExcessVerticalSpace = false;
        SaltGridData.verticalAlignment = GridData.CENTER;

        Salt = new Button(OptionGroup, SWT.CHECK);
        Salt.setText("Salt");
        Salt.setLayoutData(SaltGridData);
        Salt.addListener(SWT.Selection, this);

        GridData HashLabelGridData = new GridData();
        HashLabelGridData.horizontalSpan = 2;
        HashLabelGridData.horizontalAlignment = GridData.BEGINNING;
        HashLabelGridData.grabExcessHorizontalSpace = false;
        HashLabelGridData.grabExcessVerticalSpace = false;
        HashLabelGridData.verticalAlignment = GridData.CENTER;

        GridData HashValueGridData = new GridData();
        HashValueGridData.horizontalSpan = 2;
        HashValueGridData.verticalAlignment = GridData.CENTER;
        HashValueGridData.grabExcessHorizontalSpace = false;
        HashValueGridData.horizontalAlignment = GridData.FILL;

        HashValue = new Text(OptionGroup, SWT.BORDER | SWT.MULTI);
        HashValue.setLayoutData(HashValueGridData);
        String temp = "";
        HashValue.setText(temp);
        HashValue.addListener(SWT.Modify, this);
        HashValue.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                setErrorMessage(null);
                if (e.character != SWT.BS && e.character != SWT.DEL) {
                    e.text = e.text.toUpperCase();
                    /* verify the input for illegal expressions */
                    Pattern hex = Pattern.compile("[A-Fa-f0-9]+");
                    Pattern length = Pattern.compile("[A-Fa-f0-9]{1," + (getBitLength() / 4) + "}");
                    Matcher m = hex.matcher(e.text);
                    Matcher n = length.matcher(e.text);
                    if (!m.matches()) {
                        setErrorMessage(Messages.WizardError1);
                        e.doit = false;
                    } else if ((HashValue.getText().length() + 1) > getBitLength() / 4 || !n.matches()) {
                        setErrorMessage(Messages.WizardError2);
                        e.doit = false;
                    }
                }
            }

        });
        HashValue.setVisible(false);
    }

    public void createSaltGroup(Composite parent) {
        GridLayout SaltGroupGridLayout = new GridLayout();
        SaltGroupGridLayout.numColumns = 3;

        GridData SaltGroupGridData = new GridData();
        SaltGroupGridData.horizontalAlignment = GridData.FILL;
        SaltGroupGridData.grabExcessHorizontalSpace = true;
        SaltGroupGridData.grabExcessVerticalSpace = false;
        SaltGroupGridData.verticalAlignment = GridData.FILL;

        SaltGroup = new Group(parent, SWT.NONE);
        SaltGroup.setLayoutData(SaltGroupGridData);
        SaltGroup.setLayout(SaltGroupGridLayout);
        SaltGroup.setText(Messages.WizardMessage13);

        SaltValue = new Text(SaltGroup, SWT.BORDER | SWT.MULTI);
        SaltValue.setLayoutData(SaltGroupGridData);
        String temp2 = "";
        SaltValue.setText(temp2);
        SaltValue.addListener(SWT.Modify, this);
        SaltValue.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent eSalt) {
                setErrorMessage(null);
                if (eSalt.character != SWT.BS && eSalt.character != SWT.DEL) {
                    eSalt.text = eSalt.text.toUpperCase();
                    /* verify the input for illegal expressions */
                    Pattern hex = Pattern.compile("[A-Fa-f0-9]+");
                    Pattern length = Pattern.compile("[A-Fa-f0-9]{1,32}");
                    Matcher m = hex.matcher(eSalt.text);
                    Matcher n = length.matcher(eSalt.text);
                    if (!m.matches()) {
                        setErrorMessage(Messages.WizardError1);
                        eSalt.doit = false;
                    } else if ((SaltValue.getText().length() + 1) > 32 || !n.matches()) {
                        setErrorMessage(Messages.WizardError3);
                        eSalt.doit = false;
                    } else
                        setErrorMessage(Messages.WizardError4);
                }
            }

        });
        SaltGroup.setVisible(false);
    }

    /**
     * Creates a new group for three buttons HexEditor, TextEditor, HashStream button
     *
     * @param parent
     */
    private void createHashOutputGroup(Composite parent) {
        GridLayout HashOutputGroupGridLayout = new GridLayout();
        HashOutputGroupGridLayout.numColumns = 2;

        GridData HashOutputGroupGridData = new GridData();
        HashOutputGroupGridData.horizontalAlignment = GridData.FILL;
        HashOutputGroupGridData.grabExcessHorizontalSpace = true;
        HashOutputGroupGridData.grabExcessVerticalSpace = false;
        HashOutputGroupGridData.verticalAlignment = GridData.FILL;

        HashOutputGroup = new Group(parent, SWT.NONE);
        HashOutputGroup.setLayoutData(HashOutputGroupGridData);
        HashOutputGroup.setLayout(HashOutputGroupGridLayout);
        HashOutputGroup.setText(Messages.WizardMessage5);

        GridData HashHexEditorButton = new GridData();
        HashHexEditorButton.horizontalAlignment = GridData.FILL;
        HashHexEditorButton.grabExcessHorizontalSpace = false;
        HashHexEditorButton.grabExcessVerticalSpace = false;
        HashHexEditorButton.verticalAlignment = GridData.CENTER;

        HashHexEditor = new Button(HashOutputGroup, SWT.RADIO);
        HashHexEditor.setText(Messages.WizardMessage6);
        HashHexEditor.setLayoutData(HashHexEditorButton);
        HashHexEditor.addListener(SWT.Selection, this);

        GridData HashFormattedButton = new GridData();
        HashFormattedButton.horizontalAlignment = GridData.FILL;
        HashFormattedButton.grabExcessHorizontalSpace = false;
        HashFormattedButton.grabExcessVerticalSpace = false;
        HashFormattedButton.verticalAlignment = GridData.CENTER;

        HashFormatted = new Button(HashOutputGroup, SWT.RADIO);
        HashFormatted.setText(Messages.WizardMessage7);
        HashFormatted.setLayoutData(HashFormattedButton);
        HashFormatted.addListener(SWT.Selection, this);

//        GridData HashStreamButton = new GridData();
//        HashStreamButton.horizontalAlignment = GridData.FILL;
//        HashStreamButton.grabExcessHorizontalSpace = true;
//        HashStreamButton.grabExcessVerticalSpace = true;
//        HashStreamButton.verticalAlignment = GridData.CENTER;
//
//        HashStream = new Button(HashOutputGroup, SWT.RADIO);
//        HashStream.setText(Messages.WizardMessage8);
//        HashStream.setLayoutData(HashStreamButton);
//        HashStream.addListener(SWT.Selection, this);
    }

    /**
     * Creates a drop-down list for all algorithms For adding a new algorithm just do another
     * AlgorithmCombo.add("<algorithm-name>"); at the end of the method
     *
     * @param parent
     */
    protected void createAlgorithmGroup(Composite parent) {
        algorithmGroup = new Group(parent, SWT.NONE);
        GridLayout AlgorithmGroupGridLayout = new GridLayout();
        AlgorithmGroupGridLayout.numColumns = 1;

        GridData AlgorithmGroupGridData = new GridData();
        AlgorithmGroupGridData.horizontalAlignment = GridData.FILL;
        AlgorithmGroupGridData.grabExcessHorizontalSpace = false;
        AlgorithmGroupGridData.grabExcessVerticalSpace = false;
        AlgorithmGroupGridData.verticalAlignment = SWT.TOP;

        algorithmGroup.setLayoutData(AlgorithmGroupGridData);
        algorithmGroup.setLayout(AlgorithmGroupGridLayout);

        algorithmListLabel = new Label(algorithmGroup, SWT.NONE);
        GridData AlgorithmLabelGridData = new GridData();
        AlgorithmLabelGridData.horizontalAlignment = GridData.FILL;
        AlgorithmLabelGridData.grabExcessHorizontalSpace = false;
        AlgorithmLabelGridData.grabExcessVerticalSpace = false;
        AlgorithmLabelGridData.verticalAlignment = GridData.CENTER;

        algorithmListLabel.setText(Messages.WizardMessage9);
        algorithmListLabel.setLayoutData(AlgorithmLabelGridData);

        AlgorithmCombo = new Combo(algorithmGroup, SWT.BORDER | SWT.READ_ONLY);
        GridData AlgorithmComboGridData = new GridData();
        AlgorithmComboGridData.horizontalAlignment = GridData.FILL;
        AlgorithmComboGridData.grabExcessHorizontalSpace = false;
        AlgorithmComboGridData.grabExcessVerticalSpace = false;
        AlgorithmComboGridData.verticalAlignment = GridData.CENTER;

        AlgorithmCombo.setLayoutData(AlgorithmComboGridData);
        AlgorithmCombo.addListener(SWT.Selection, this);

        AlgorithmCombo.add("ECHO");
        AlgorithmCombo.add("JH");
        AlgorithmCombo.add("Skein");
    }

    /**
     * Creates a drop-down list for the bitlength
     *
     * @param parent
     */
    protected void createBitlengthGroup(Composite parent) {
        bitlengthGroup = new Group(parent, SWT.NONE);
        GridLayout BitlengthGroupGridLayout = new GridLayout();
        BitlengthGroupGridLayout.numColumns = 1;

        GridData BitlengthGroupGridData = new GridData();
        BitlengthGroupGridData.horizontalAlignment = GridData.FILL;
        BitlengthGroupGridData.grabExcessHorizontalSpace = false;
        BitlengthGroupGridData.grabExcessVerticalSpace = false;
        BitlengthGroupGridData.verticalAlignment = SWT.TOP;

        bitlengthGroup.setLayoutData(BitlengthGroupGridData);
        bitlengthGroup.setLayout(BitlengthGroupGridLayout);

        BitlengthListLabel = new Label(bitlengthGroup, SWT.NONE);
        GridData BitlengthLabelGridData = new GridData();
        BitlengthLabelGridData.horizontalAlignment = GridData.FILL;
        BitlengthLabelGridData.grabExcessHorizontalSpace = false;
        BitlengthLabelGridData.grabExcessVerticalSpace = false;
        BitlengthLabelGridData.verticalAlignment = GridData.CENTER;

        BitlengthListLabel.setText(Messages.WizardMessage10);
        BitlengthListLabel.setLayoutData(BitlengthLabelGridData);

        BitlengthCombo = new Combo(bitlengthGroup, SWT.BORDER | SWT.READ_ONLY);
        GridData BitlengthComboGridData = new GridData();
        BitlengthComboGridData.horizontalAlignment = GridData.GRAB_HORIZONTAL;
        BitlengthComboGridData.grabExcessHorizontalSpace = false;
        BitlengthComboGridData.grabExcessVerticalSpace = true;
        BitlengthComboGridData.verticalAlignment = GridData.CENTER;

        BitlengthCombo.setLayoutData(BitlengthComboGridData);
        BitlengthCombo.addListener(SWT.Selection, this);
    }

    /**
     * This method is responsible for all events that may happen in the wizard
     */
    public void handleEvent(Event event) {
        if (event.widget == VerifyHash) {
            HashStream.setEnabled(false);
            HashFormatted.setEnabled(false);
            HashHexEditor.setEnabled(false);
            Mode = true;
            HashValue.setEnabled(true);
            HashValue.setVisible(true);
        } else if (event.widget == CreateHash) {
            HashHexEditor.setEnabled(true);
            HashStream.setEnabled(true);
            HashFormatted.setEnabled(true);
            Mode = false;
            HashValue.setEnabled(false);
            HashValue.setVisible(false);
        }
        if (event.widget == HashHexEditor)
            OutputMode = 0;
        if (event.widget == HashFormatted)
            OutputMode = 1;
        if (event.widget == HashStream)
            OutputMode = 2;
        if (event.widget == HashValue) {
            Hash = HashValue.getText();
        }
        if (event.widget == SaltValue) {
            SaltEcho = SaltValue.getText();
        }
        if (event.widget == AlgorithmCombo) {
            if (AlgorithmCombo.getSelectionIndex() == 0) {
                SHA3Type = 0;
                BitlengthCombo.removeAll();
                BitlengthCombo.add("224");
                BitlengthCombo.add("256");
                BitlengthCombo.add("384");
                BitlengthCombo.add("512");
                Salt.setVisible(true);
            } else if (AlgorithmCombo.getSelectionIndex() == 1) {
                SHA3Type = 1;
                BitlengthCombo.removeAll();
                BitlengthCombo.add("224");
                BitlengthCombo.add("256");
                BitlengthCombo.add("384");
                BitlengthCombo.add("512");
                SaltGroup.setVisible(false);
                Salt.setVisible(false);
                Salt.setSelection(false);
            } else if (AlgorithmCombo.getSelectionIndex() == 2) {
                SHA3Type = 2;
                BitlengthCombo.removeAll();
                BitlengthCombo.add("256");
                BitlengthCombo.add("512");
                BitlengthCombo.add("1024");
                SaltGroup.setVisible(false);
                Salt.setVisible(false);
                Salt.setSelection(false);
            }
            BitlengthCombo.select(0);
        }
        if (event.widget == Salt) {
            if (Salt.getSelection()) {
                SaltGroup.setVisible(true);
            } else
                SaltGroup.setVisible(false);
        }
        if (event.widget == BitlengthCombo) {
            if (BitlengthCombo.getSelectionIndex() == 0) {
                Bitlength = 0;
            } else if (BitlengthCombo.getSelectionIndex() == 1) {
                Bitlength = 1;
            } else if (BitlengthCombo.getSelectionIndex() == 2) {
                Bitlength = 2;
            } else if (BitlengthCombo.getSelectionIndex() == 3) {
                Bitlength = 3;
            }
        }
        setPageComplete(mayFinish());
    }

    /**
     * Returns the entered hash value
     *
     * @return
     */
    public String getHashValue() {
        return Hash;
    }

    /**
     * Returns the name of the chosen algorithm
     *
     * @return
     */
    public String getSha3Type() {
        String sha3 = "";
        switch (SHA3Type) {
            case 0:
                sha3 = "ECHO";
                break;
            case 1:
                sha3 = "JH";
                break;
            case 2:
                sha3 = "Skein";
                break;

        }
        return sha3;
    }

    public String getSalt() {
        return SaltEcho;
    }

    /**
     * This method returns the chosen bitlength
     *
     * @return
     */
    public int getBitLength() {
        if (getSha3Type().compareTo("Skein") == 0) {
            switch (Bitlength) {
                case 0:
                    Bitlength = 256;
                    break;
                case 1:
                    Bitlength = 512;
                    break;
                case 2:
                    Bitlength = 1024;
                    break;
            }
        } else {
            switch (Bitlength) {
                case 0:
                    Bitlength = 224;
                    break;
                case 1:
                    Bitlength = 256;
                    break;
                case 2:
                    Bitlength = 384;
                    break;
                case 3:
                    Bitlength = 512;
                    break;
            }
        }
        return Bitlength;
    }

    /**
     * @return false = createHash, true = verifyHash
     */
    public String getMode() {
        if (Mode == false)
            return "CreateHash";
        else
            return "VerifyHash";
    }

    /**
     * Returns the chosen output mode
     *
     * @return 0...HexEditor, 1...HashFormatted, 2...HashStream
     */
    public int getOutputMode() {
        return OutputMode;
    }

    private boolean mayFinish() {
        /*
         * if (SHA3Type == -1) return false; if (Bitlength == -1) return false; if (CreateHash.getSelection()==false &&
         * VerifyHash.getSelection()==false) return false;
         */
        return true;
    }
}