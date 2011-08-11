/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.preferences;

import java.util.ArrayList;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.utils.IAlgorithms;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;

/**
 * <p>This class represents the Quick Signature and Quick Verification preference page of the XML
 * Security Tools.</p>
 * 
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PreferencePageSignature extends PreferencePage implements IWorkbenchPreferencePage {
    /** Signature type radio buttons. */
    private RadioGroupFieldEditor signatureType = null;
    /** Keystore file. */
    private FileFieldEditor keystoreFile = null;
    /** Radio button sign document. */
    private Button signDocument;
    /** Radio button sign selection. */
    private Button signSelection;
    /** Radio button sign XPath expression. */
    private Button signXPath;
    /** Text XPath expression. */
    private Text signXPathText;
    /** Combo canonicalization algorithm. */
    private Combo algoCanonCombo;
    /** Combo transformation algorithm. */
    private Combo algoTransformCombo;
    /** Combo message digest algorithm. */
    private Combo algoMDCombo;
    /** Combo signature algorithm. */
    private Combo algoSignCombo;
    /** Text key name. */
    private Text keyNameText;
    /** Text signature id. */
    private Text signIdText;
    /** Sign XPath label. */
    private Label signXPathLabel;

    /** All checkboxes on the page. */
    private ArrayList<Button> checkBoxes;
    /** All radio buttons on the page. */
    private ArrayList<Button> radioButtons;
    /** All textfields on the page. */
    private ArrayList<Text> textControls;
    /** All comboboxes on the page. */
    private ArrayList<Combo> comboBoxes;
    /** Selection listener. */
    private SelectionListener selectionListener;
    /** Modify listerner. */
    private ModifyListener modifyListener;

    /**
     * Constructor.
     */
    public PreferencePageSignature() {
        super();

        checkBoxes = new ArrayList<Button>();
        comboBoxes = new ArrayList<Combo>();
        radioButtons = new ArrayList<Button>();
        textControls = new ArrayList<Text>();

        addListener();
    }

    /**
     * Adds all listeners for the current preference page.
     */
    private void addListener() {
        selectionListener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                controlChanged(e.widget);
            }
        };

        modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                controlModified(e.widget);
            }
        };
    }

    /**
     * Initializes the preference page.
     * 
     * @param iWorkbench The current workbench
     */
    public void init(IWorkbench iWorkbench) {
        setPreferenceStore(XSTUIPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.signaturePrefsDesc);
    }

    /**
     * Indicates whether the page is currently valid.
     * 
     * @return Possible to close the preference page or not
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Called when the user wishes to flip to another page.
     * 
     * @return Possible to flip to another page or not
     */
    public boolean okToLeave() {
        return true;
    }

    /**
     * Fills this preference page with content.
     * 
     * @param parent Parent composite
     * @return The Control
     */
    protected Control createContents(Composite parent) {
        initializeDialogUnits(parent);

        Composite top = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = PreferenceConstants.MARGIN;
        layout.marginWidth = PreferenceConstants.MARGIN;
        layout.numColumns = PreferenceConstants.COLUMNS;
        top.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group signatureResourceGroup = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.SMALL_GROUP;
        signatureResourceGroup.setLayout(layout);
        signatureResourceGroup.setLayoutData(gd);
        signatureResourceGroup.setText(Messages.resource);

        int indent = 0;

        signDocument = addRadioButton(signatureResourceGroup, Messages.document,
                PreferenceConstants.SIGN_RESOURCE, "document", indent);
        signDocument.addSelectionListener(selectionListener);

        signSelection = addRadioButton(signatureResourceGroup, Messages.selection,
                PreferenceConstants.SIGN_RESOURCE, "selection", indent);
        signSelection.addSelectionListener(selectionListener);

        signXPath = addRadioButton(signatureResourceGroup, Messages.xpath,
                PreferenceConstants.SIGN_RESOURCE, "xpath", indent);
        signXPath.addSelectionListener(selectionListener);

        indent = convertWidthInCharsToPixels(4);

        signXPathLabel = new Label(signatureResourceGroup, SWT.NONE);
        signXPathLabel.setText(Messages.xpathExpression);
        signXPathLabel.setEnabled(signXPath.getSelection());
        signXPathText = addTextControl(signatureResourceGroup, signXPathLabel,
                PreferenceConstants.SIGN_XPATH, indent);
        signXPathText.addModifyListener(modifyListener);
        signXPathText.setEnabled(signXPath.getSelection());

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        signatureType = new RadioGroupFieldEditor(PreferenceConstants.SIGN_TYPE, Messages.type,
                PreferenceConstants.SMALL_GROUP, IPreferenceValues.SIGNATURE_TYPES, top, true);
        signatureType.setPage(this);
        signatureType.setPreferenceStore(this.getPreferenceStore());
        signatureType.load();
        signatureType.fillIntoGrid(top, PreferenceConstants.SMALL_GROUP);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group signatureAlgorithmsGroup = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.SMALL_GROUP;
        signatureAlgorithmsGroup.setLayout(layout);
        signatureAlgorithmsGroup.setLayoutData(gd);
        signatureAlgorithmsGroup.setText(Messages.algorithms);

        indent = 0;

        Label algoCanonLabel = new Label(signatureAlgorithmsGroup, SWT.NONE);
        algoCanonLabel.setText(Messages.signaturePrefsAlgoCanon);
        algoCanonCombo = addComboControl(signatureAlgorithmsGroup, algoCanonLabel,
                PreferenceConstants.SIGN_CANON, indent, IAlgorithms.CANONICALIZATION_ALOGRITHMS);
        algoCanonCombo.addModifyListener(modifyListener);

        Label algoTransformLabel = new Label(signatureAlgorithmsGroup, SWT.NONE);
        algoTransformLabel.setText(Messages.signaturePrefsAlgoTransform);
        algoTransformCombo = addComboControl(signatureAlgorithmsGroup, algoTransformLabel,
                PreferenceConstants.SIGN_TRANS, indent, IAlgorithms.TRANSFORMATION_ALOGRITHMS);
        algoTransformCombo.addModifyListener(modifyListener);

        Label algoMDLabel = new Label(signatureAlgorithmsGroup, SWT.NONE);
        algoMDLabel.setText(Messages.signaturePrefsAlgoMD);
        algoMDCombo = addComboControl(signatureAlgorithmsGroup, algoMDLabel,
                PreferenceConstants.SIGN_MDA, indent, IAlgorithms.MD_ALOGRITHMS);
        algoMDCombo.addModifyListener(modifyListener);

        Label algoSignLabel = new Label(signatureAlgorithmsGroup, SWT.NONE);
        algoSignLabel.setText(Messages.signaturePrefsAlgoSign);
        algoSignCombo = addComboControl(signatureAlgorithmsGroup, algoSignLabel,
                PreferenceConstants.SIGN_SA, indent, IAlgorithms.SIGNATURE_ALOGRITHMS);
        algoSignCombo.addModifyListener(modifyListener);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group keystoreGroup = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.LARGE_GROUP;
        keystoreGroup.setLayout(layout);
        keystoreGroup.setLayoutData(gd);
        keystoreGroup.setText(Messages.keystoreAndKey);

        indent = 0;

        keystoreFile = new FileFieldEditor(PreferenceConstants.SIGN_KEYSTORE_FILE,
                Messages.keystore, keystoreGroup);
        keystoreFile.setFileExtensions(IGlobals.KEY_STORE_EXTENSION);
        keystoreFile.setChangeButtonText(Messages.open);
        keystoreFile.setPage(this);
        keystoreFile.setPreferenceStore(this.getPreferenceStore());
        keystoreFile.load();
        keystoreFile.fillIntoGrid(keystoreGroup, PreferenceConstants.LARGE_GROUP);

        Label keyNameLabel = new Label(keystoreGroup, SWT.NONE);
        keyNameLabel.setText(Messages.key);
        keyNameText = addTextControl(keystoreGroup, keyNameLabel,
                PreferenceConstants.SIGN_KEY_NAME, indent);
        keyNameText.addModifyListener(modifyListener);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group signatureIdGroup = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.SMALL_GROUP;
        signatureIdGroup.setLayout(layout);
        signatureIdGroup.setLayoutData(gd);
        signatureIdGroup.setText(Messages.id);

        indent = 0;

        Label signIdLabel = new Label(signatureIdGroup, SWT.NONE);
        signIdLabel.setText(Messages.id);
        signIdText = addTextControl(signatureIdGroup, signIdLabel, PreferenceConstants.SIGN_ID,
                indent);
        signIdText.addModifyListener(modifyListener);

        return top;
    }

    /**
     * Adds context sensitive help to this preference page.
     * 
     * @param parent The parent composite
     */
    public void createControl(Composite parent) {
        super.createControl(parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                IContextHelpIds.PREFERENCES_SIGNATURE);
    }

    /**
     * Adds a radio button to this preference page.
     * 
     * @param parent The parent composite
     * @param label The label for the radio button
     * @param key The key of the radio button
     * @param value The value of the radio button
     * @param indent The indent
     * @return The complete radio button
     */
    private Button addRadioButton(Composite parent, String label, String key, String value,
            int indent) {
        IPreferenceStore store = getPreferenceStore();
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan = 2;
        gd.horizontalIndent = indent;

        Button button = new Button(parent, SWT.RADIO);
        button.setText(label);
        button.setData(new String[] {key, value});
        button.setLayoutData(gd);
        button.setSelection(value.equals(store.getString(key)));

        radioButtons.add(button);
        return button;
    }

    /**
     * Adds a textfield to this preference page.
     * 
     * @param parent The parent composite
     * @param labelControl The label
     * @param key The key of the textfield
     * @param indent The indent
     * @return The complete textfield
     */
    private Text addTextControl(Composite parent, Label labelControl, String key, int indent) {
        IPreferenceStore store = getPreferenceStore();
        GridData gd = new GridData();
        gd.horizontalIndent = indent;

        labelControl.setLayoutData(gd);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = convertWidthInCharsToPixels(PreferenceConstants.CHARS_TO_PIXEL);

        Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        text.setData(key);
        text.setLayoutData(gd);
        text.setText(store.getString(key));

        textControls.add(text);
        return text;
    }

    /**
     * Adds a combo box to this preference page.
     * 
     * @param parent The parent composite
     * @param labelControl The label
     * @param key The key of the combo box
     * @param indent The indent
     * @param items The items to add
     * @return The complete combo box
     */
    private Combo addComboControl(Composite parent, Label labelControl, String key, int indent,
            String[] items) {
        IPreferenceStore store = getPreferenceStore();
        GridData gd = new GridData();
        gd.horizontalIndent = indent;

        labelControl.setLayoutData(gd);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = convertWidthInCharsToPixels(PreferenceConstants.CHARS_TO_PIXEL);

        Combo combo = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
        combo.setItems(items);
        combo.setData(key);
        combo.setLayoutData(gd);
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItem(i).equals(store.getString(key))) {
                combo.select(i);
            }
        }

        comboBoxes.add(combo);
        return combo;
    }

    /**
     * Loads the default values for the page.
     */
    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();

        for (Button b : checkBoxes) {
            String key = (String) b.getData();
            b.setSelection(store.getDefaultBoolean(key));
        }
        for (Button b : radioButtons) {
            String[] info = (String[]) b.getData();
            b.setSelection(info[1].equals(store.getDefaultString(info[0])));
        }
        for (Text t : textControls) {
            String key = (String) t.getData();
            t.setText(store.getDefaultString(key));
        }
        for (Combo c : comboBoxes) {
            String key = (String) c.getData();
            for (int i = 0; i < c.getItemCount(); i++) {
                if (c.getItem(i).equals(store.getDefaultString(key))) {
                    c.select(i);
                }
            }
        }

        signatureType.loadDefault();
        keystoreFile.loadDefault();

        super.performDefaults();

        validateXPath();
    }

    /**
     * Validates the XPath expression and enables (or disables) the XPath expression text.
     */
    private void validateXPath() {
        boolean useXPathExpression = signXPath.getSelection();

        signXPathText.setEnabled(useXPathExpression);
        signXPathLabel.setEnabled(useXPathExpression);
    }

    /**
     * Called after click on apply button.
     */
    protected void performApply() {
        performOk();
    }

    /**
     * Called after click on OK button.
     * 
     * @return Result of store process
     */
    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();

        for (Button b : checkBoxes) {
            String key = (String) b.getData();
            store.setValue(key, b.getSelection());
        }
        for (Button b : radioButtons) {
            if (b.getSelection()) {
                String[] info = (String[]) b.getData();
                store.setValue(info[0], info[1]);
            }
        }
        for (Text t : textControls) {
            String key = (String) t.getData();
            store.setValue(key, t.getText());
        }
        for (Combo c : comboBoxes) {
            String key = (String) c.getData();
            store.setValue(key, c.getItem(c.getSelectionIndex()));
        }

        signatureType.store();
        keystoreFile.store();

        return super.performOk();
    }

    /**
     * Updates widgets after controls have changed.
     * 
     * @param widget The widget that has changed
     */
    private void controlChanged(Widget widget) {
        if (widget == signDocument || widget == signSelection) {
            signXPathLabel.setEnabled(false);
            signXPathText.setEnabled(false);
        } else if (widget == signXPath) {
            signXPathLabel.setEnabled(true);
            signXPathText.setEnabled(true);
        }
    }

    /**
     * Called after a control has been modified.
     * 
     * @param widget The modified widget
     */
    private void controlModified(Widget widget) {
    }
}
