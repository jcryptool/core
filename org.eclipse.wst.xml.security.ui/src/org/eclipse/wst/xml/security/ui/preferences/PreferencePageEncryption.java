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
 * <p>This class represents the Encryption and Decryption preference page of the
 * XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PreferencePageEncryption extends PreferencePage implements IWorkbenchPreferencePage {
    /** Encryption type radio buttons. */
    private RadioGroupFieldEditor encryptionType = null;
    /** Keystore. */
    private FileFieldEditor keystore = null;
    /** Radio button encrypt document. */
    private Button bDocument;
    /** Radio button encrypt selection. */
    private Button bSelection;
    /** Radio button encrypt XPath expression. */
    private Button bXpath;
    /** Label XPath expression. */
    private Label lXpath;
    /** Text XPath expression. */
    private Text tXpath;
    /** Text key name. */
    private Text tKeyName;
    /** Combo encryption algorithm. */
    private Combo cEncryptionAlgorithm;
    /** Combo key wrap algorithm. */
    private Combo cKeyWrapAlgorithm;
    /** Text encryption id. */
    private Text tId;
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
    public PreferencePageEncryption() {
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
        setDescription(Messages.encryptionPrefsDesc);
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

        Group gResource = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.SMALL_GROUP;
        gResource.setLayout(layout);
        gResource.setLayoutData(gd);
        gResource.setText(Messages.resource);

        int indent = 0;

        bDocument = addRadioButton(gResource, Messages.document,
                PreferenceConstants.ENCRYPT_RESOURCE, "document", indent);
        bDocument.addSelectionListener(selectionListener);

        bSelection = addRadioButton(gResource, Messages.selection,
                PreferenceConstants.ENCRYPT_RESOURCE, "selection", indent);
        bSelection.addSelectionListener(selectionListener);

        bXpath = addRadioButton(gResource, Messages.xpath,
                PreferenceConstants.ENCRYPT_RESOURCE, "xpath", indent);
        bXpath.addSelectionListener(selectionListener);

        indent = convertWidthInCharsToPixels(4);

        lXpath = new Label(gResource, SWT.NONE);
        lXpath.setText(Messages.xpathExpression);
        lXpath.setEnabled(bXpath.getSelection());
        tXpath = addTextControl(gResource, lXpath, PreferenceConstants.ENCRYPT_XPATH, indent);
        tXpath.addModifyListener(modifyListener);
        tXpath.setEnabled(bXpath.getSelection());

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        encryptionType = new RadioGroupFieldEditor(PreferenceConstants.ENCRYPT_TYPE,
                Messages.type, PreferenceConstants.SMALL_GROUP,
                IPreferenceValues.ENCRYPTION_TYPES, top, true);
        encryptionType.setPage(this);
        encryptionType.setPreferenceStore(this.getPreferenceStore());
        encryptionType.load();
        encryptionType.fillIntoGrid(top, PreferenceConstants.SMALL_GROUP);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group gAlgorithms = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.SMALL_GROUP;
        gAlgorithms.setLayout(layout);
        gAlgorithms.setLayoutData(gd);
        gAlgorithms.setText(Messages.algorithms);

        indent = 0;

        Label lEncryptionAlgorithm = new Label(gAlgorithms, SWT.NONE);
        lEncryptionAlgorithm.setText(Messages.encryptionPrefsEncryptionAlgorithm);
        cEncryptionAlgorithm = addComboControl(gAlgorithms, lEncryptionAlgorithm,
                PreferenceConstants.ENCRYPT_ENCRYPTION, indent, IAlgorithms.ENCRYPTION_ALGORITHMS);
        cEncryptionAlgorithm.addModifyListener(modifyListener);

        Label lKeyWrapAlgorithm = new Label(gAlgorithms, SWT.NONE);
        lKeyWrapAlgorithm.setText(Messages.encryptionPrefsAlgoKeyWrap);
        cKeyWrapAlgorithm = addComboControl(gAlgorithms, lKeyWrapAlgorithm,
                PreferenceConstants.ENCRYPT_KEY_WRAP, indent, IAlgorithms.KEY_WRAP_ALGORITHMS);
        cKeyWrapAlgorithm.addModifyListener(modifyListener);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group gKeystore = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.LARGE_GROUP;
        gKeystore.setLayout(layout);
        gKeystore.setLayoutData(gd);
        gKeystore.setText(Messages.keystoreAndKey);

        indent = 0;

        keystore = new FileFieldEditor(PreferenceConstants.ENCRYPT_KEY_STORE,
                Messages.keystore, gKeystore);
        keystore.setFileExtensions(IGlobals.KEY_STORE_EXTENSION);
        keystore.setChangeButtonText(Messages.open);
        keystore.setPage(this);
        keystore.setPreferenceStore(this.getPreferenceStore());
        keystore.load();
        keystore.fillIntoGrid(gKeystore, PreferenceConstants.LARGE_GROUP);

        Label lKeyName = new Label(gKeystore, SWT.NONE);
        lKeyName.setText(Messages.key);
        tKeyName = addTextControl(gKeystore, lKeyName, PreferenceConstants.ENCRYPT_KEY_NAME, indent);
        tKeyName.addModifyListener(modifyListener);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = PreferenceConstants.SMALL_GROUP;

        Group gId = new Group(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = PreferenceConstants.SMALL_GROUP;
        gId.setLayout(layout);
        gId.setLayoutData(gd);
        gId.setText(Messages.id);

        indent = 0;

        Label lId = new Label(gId, SWT.NONE);
        lId.setText(Messages.id);
        tId = addTextControl(gId, lId, PreferenceConstants.ENCRYPT_ID, indent);
        tId.addModifyListener(modifyListener);

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
                IContextHelpIds.PREFERENCES_ENCRYPTION);
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
        IPreferenceStore store = XSTUIPlugin.getDefault().getPreferenceStore();
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

        encryptionType.loadDefault();
        keystore.loadDefault();

        super.performDefaults();

        validateXPath();
    }

    /**
     * Validates the XPath expression and enables (or disables) the XPath expression text.
     */
    private void validateXPath() {
        boolean useXPathExpression = bXpath.getSelection();

        tXpath.setEnabled(useXPathExpression);
        lXpath.setEnabled(useXPathExpression);
    }

    /**
     * Called after click on apply button.
     */
    protected void performApply() {
        performOk();
    }

    /**
     * Stores the current settings of the page.
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

        encryptionType.store();
        keystore.store();

        return super.performOk();
    }

    /**
     * Updates widgets after controls have changed.
     *
     * @param widget The widget that has changed
     */
    private void controlChanged(Widget widget) {
        if (widget == bDocument || widget == bSelection) {
            lXpath.setEnabled(false);
            tXpath.setEnabled(false);
        } else if (widget == bXpath) {
            lXpath.setEnabled(true);
            tXpath.setEnabled(true);
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
