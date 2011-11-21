// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.preferences;

import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.crypto.classic.alphabets.Alphabet;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;
import org.jcryptool.crypto.classic.alphabets.ui.AddAlphabetWizard;
import org.jcryptool.crypto.classic.alphabets.ui.EditAlphabetWizard;

/**
 * The PreferencePage for the Alphabets.
 *
 * @author t-kern
 *
 */
public class AlphabetsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, Listener {
    private Vector<AbstractAlphabet> tempAlphas;

    private Group managementGroup = null;
    private Table availableAlphabetsTable = null;
    private TableColumn nameColumn = null;
    private TableColumn basicColumn = null;
    private TableColumn defaultColumn = null;
    private Button addButton = null;
    private Button editButton = null;
    private Button removeButton = null;
    private Button defaultButton = null;
    private Label charsetLabel = null;
    private Text charsetText = null;
    private Group optionsGroup = null;
    private Button enableFilterCheckBox = null;

    /**
     * No-args Constructor.
     */
    public AlphabetsPreferencePage() {
        super();
        AlphabetsPlugin.getDefault().loadPreferences();
    }

    /**
     * Creates the controls, the layout and the general functionality of this
     * PreferencePage.
     *
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite pageComponent = new Composite(parent, SWT.NULL);
        GridLayout pageLayout = new GridLayout();
        pageLayout.numColumns = 1;
        GridData layoutData = new GridData(/* GridData.FILL_BOTH */);
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.grabExcessHorizontalSpace = true;
        pageComponent.setLayout(pageLayout);
        pageComponent.setLayoutData(layoutData);
        createManagementGroup(pageComponent);
        createOptionsGroup(pageComponent);

        prepare();

        fillTable();

        registerListener();

        enableFilterCheckBox.setSelection(AlphabetsPlugin.getDefault().getFilterChars());

        editButton.setEnabled(false);
        removeButton.setEnabled(false);
        defaultButton.setEnabled(false);

        return pageComponent;
    }

    /**
     * Sets up the local alphabet store.
     */
    private void prepare() {
        tempAlphas = new Vector<AbstractAlphabet>();

        int length = AlphabetsManager.getInstance().getAlphabets().length;
        for (int i = 0; i < length; i++) {
            tempAlphas.add(AlphabetsManager.getInstance().getAlphabets()[i]);
        }

    }

    /**
     * Returns the Alphabet with the given name.
     *
     * @param name The name of the Alphabet
     * @return The Alphabet
     */
    private AbstractAlphabet getAlphabet(String name) {
        for (int i = 0; i < tempAlphas.size(); i++) {
            if (name.equals(tempAlphas.get(i).getName())) {
                return tempAlphas.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the default Alphabet.
     *
     * @return The default Alphabet
     */
    private AbstractAlphabet getDefaultAlphabet() {
        for (int i = 0; i < tempAlphas.size(); i++) {
            if ((tempAlphas.get(i).isDefaultAlphabet())) {
                return tempAlphas.get(i);
            }
        }
        return null;
    }

    /**
     * This method initializes managementGroup.
     *
     */
    private void createManagementGroup(Composite parent) {
        GridData fillerData = new GridData();
        fillerData.horizontalAlignment = SWT.RIGHT;
        fillerData.heightHint = -1;
        fillerData.grabExcessVerticalSpace = true;
        fillerData.verticalAlignment = SWT.CENTER;
        GridData charsetLabelData = new GridData();
        charsetLabelData.horizontalAlignment = GridData.FILL;
        charsetLabelData.horizontalSpan = 2;
        charsetLabelData.grabExcessHorizontalSpace = true;
        charsetLabelData.verticalAlignment = GridData.END;
        GridData charsetTextData = new GridData();
        charsetTextData.horizontalAlignment = GridData.FILL;
        charsetTextData.grabExcessHorizontalSpace = true;
        charsetTextData.grabExcessVerticalSpace = false;
        charsetTextData.horizontalSpan = 2;
        charsetTextData.heightHint = 50;
        charsetTextData.verticalAlignment = GridData.FILL;
        GridData addButtonData = new GridData();
        addButtonData.horizontalAlignment = GridData.FILL;
        addButtonData.verticalAlignment = GridData.BEGINNING;
        GridData editButtonData = new GridData();
        editButtonData.horizontalAlignment = GridData.FILL;
        editButtonData.verticalAlignment = GridData.CENTER;
        GridData removeButtonData = new GridData();
        removeButtonData.horizontalAlignment = GridData.FILL;
        removeButtonData.verticalAlignment = GridData.CENTER;
        GridData defaultButtonData = new GridData();
        defaultButtonData.grabExcessHorizontalSpace = false;
        defaultButtonData.horizontalAlignment = GridData.FILL;
        defaultButtonData.verticalAlignment = GridData.CENTER;
        GridData alphabetsTableData = new GridData();
        alphabetsTableData.verticalSpan = 5;
        alphabetsTableData.horizontalAlignment = GridData.FILL;
        alphabetsTableData.verticalAlignment = GridData.FILL;
        alphabetsTableData.grabExcessHorizontalSpace = true;
        alphabetsTableData.grabExcessVerticalSpace = true;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        managementGroup = new Group(parent, SWT.NONE);
        availableAlphabetsTable = new Table(managementGroup, SWT.NONE | SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        availableAlphabetsTable.setHeaderVisible(true);
        availableAlphabetsTable.setLayoutData(alphabetsTableData);
        availableAlphabetsTable.setLinesVisible(true);

        nameColumn = new TableColumn(availableAlphabetsTable, SWT.LEFT);
        nameColumn.setText(Messages.getString("AlphabetsPreferencePage.0")); //$NON-NLS-1$
        nameColumn.setWidth(200);
        basicColumn = new TableColumn(availableAlphabetsTable, SWT.CENTER);
        basicColumn.setText(Messages.getString("AlphabetsPreferencePage.1")); //$NON-NLS-1$
        basicColumn.setWidth(50);
        defaultColumn = new TableColumn(availableAlphabetsTable, SWT.CENTER);
        defaultColumn.setText(Messages.getString("AlphabetsPreferencePage.2")); //$NON-NLS-1$
        defaultColumn.setWidth(50);

        addButton = new Button(managementGroup, SWT.NONE);
        addButton.setText(Messages.getString("AlphabetsPreferencePage.3")); //$NON-NLS-1$
        // addButton.setSize(100, 25);
        addButton.setLayoutData(addButtonData);
        editButton = new Button(managementGroup, SWT.NONE);
        editButton.setText(Messages.getString("AlphabetsPreferencePage.4")); //$NON-NLS-1$
        editButton.setLayoutData(editButtonData);
        removeButton = new Button(managementGroup, SWT.NONE);
        removeButton.setText(Messages.getString("AlphabetsPreferencePage.5")); //$NON-NLS-1$
        removeButton.setLayoutData(removeButtonData);
        defaultButton = new Button(managementGroup, SWT.NONE);
        defaultButton.setText(Messages.getString("AlphabetsPreferencePage.6")); //$NON-NLS-1$
        defaultButton.setLayoutData(defaultButtonData);
        Label filler = new Label(managementGroup, SWT.NONE);
        filler.setLayoutData(fillerData);
        charsetLabel = new Label(managementGroup, SWT.NONE);
        charsetLabel.setText(Messages.getString("AlphabetsPreferencePage.7")); //$NON-NLS-1$
        charsetLabel.setLayoutData(charsetLabelData);
        charsetText = new Text(managementGroup, SWT.BORDER | SWT.WRAP);
        charsetText.setEditable(false);
        charsetText.setLayoutData(charsetTextData);
        charsetText.setSize(0, 200);
        managementGroup.setText(Messages.getString("AlphabetsPreferencePage.8")); //$NON-NLS-1$
        managementGroup.setLayoutData(gridData);
        managementGroup.setLayout(gridLayout);
    }

    /**
     * This method initializes optionsGroup.
     *
     */
    private void createOptionsGroup(Composite parent) {
        GridData optionsGroupLayoutData = new GridData();
        optionsGroupLayoutData.grabExcessHorizontalSpace = false;
        optionsGroupLayoutData.verticalAlignment = GridData.FILL;
        optionsGroupLayoutData.horizontalAlignment = GridData.FILL;
        GridData checkBoxLayout = new GridData();
        checkBoxLayout.horizontalAlignment = GridData.FILL;
        checkBoxLayout.grabExcessHorizontalSpace = true;
        checkBoxLayout.grabExcessVerticalSpace = true;
        checkBoxLayout.verticalAlignment = GridData.CENTER;
        GridLayout optionsGroupLayout = new GridLayout();
        optionsGroupLayout.makeColumnsEqualWidth = true;
        optionsGroup = new Group(parent, SWT.NONE);
        optionsGroup.setText(Messages.getString("AlphabetsPreferencePage.9")); //$NON-NLS-1$
        optionsGroup.setLayoutData(optionsGroupLayoutData);
        optionsGroup.setLayout(optionsGroupLayout);
        enableFilterCheckBox = new Button(optionsGroup, SWT.CHECK);
        enableFilterCheckBox.setText(Messages.getString("AlphabetsPreferencePage.10")); //$NON-NLS-1$
        enableFilterCheckBox.setLayoutData(checkBoxLayout);
    }

    public void init(IWorkbench workbench) {
    }

    /**
     * Handles the various events that may occur in this PreferencePage.
     *
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
        if (event.widget.equals(this.availableAlphabetsTable)) {

            TableItem item = getSelectedTableItem(this.availableAlphabetsTable.getSelection());

            if (item != null) {
                updateCharset(item.getText(0));
                handleEditAndRemoveButton(item.getText(1));
                handleDefaultButton(item.getText(2));
            }
        } else if (event.widget.equals(addButton)) {
            addAction();
        } else if (event.widget.equals(editButton)) {
            try {
                TableItem item = this.getSelectedTableItem(this.availableAlphabetsTable.getSelection());
                editAction(item.getText(0));
            } catch (Exception e) {
                LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Unable to access the selected TableItem", e, false); //$NON-NLS-1$
            }
        } else if (event.widget.equals(removeButton)) {
            try {
                TableItem item = this.getSelectedTableItem(this.availableAlphabetsTable.getSelection());
                removeAction(item.getText(0));
            } catch (Exception e) {
                LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Unable to access the selected TableItem", e, false); //$NON-NLS-1$
            }
        } else if (event.widget.equals(defaultButton)) {
            LogUtil.logInfo("Default Button pressed"); //$NON-NLS-1$
            try {
                TableItem item = this.getSelectedTableItem(this.availableAlphabetsTable.getSelection());
                defaultAction(item.getText(0));
            } catch (Exception e) {
                LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Unable to access the selected TableItem", e, false); //$NON-NLS-1$
            }
        } else if (event.widget.equals(enableFilterCheckBox)) {
            LogUtil.logInfo("CheckBox clicked"); //$NON-NLS-1$
        }
    }

    /**
     * Will be performed after the "Add" button has been clicked.<br>
     * Opens the AddAlphabetWizard.
     */
    private void addAction() {
        AddAlphabetWizard wizard = new AddAlphabetWizard();
        WizardDialog dialog = new WizardDialog(getShell(), wizard);
        int value = dialog.open();

        if (value == Window.OK) {
            String name = wizard.getAlphabetName();
            String charset = wizard.getAlphabetCharset();
            LogUtil.logInfo("Name of the new Alphabet: " + name); //$NON-NLS-1$
            LogUtil.logInfo("Charset of the new Alphabet: " + charset); //$NON-NLS-1$

            Alphabet alphabet = new Alphabet(charset.toCharArray(), name, Alphabet.NO_DISPLAY);
            tempAlphas.add(alphabet);

            fillTable();
        }
    }

    /**
     * Will be performed after the "Edit" button has been clicked.
     *
     * @param name The name of the selected alphabet
     */
    private void editAction(String name) {
        EditAlphabetWizard wizard = new EditAlphabetWizard((Alphabet) getAlphabet(name));
        WizardDialog dialog = new WizardDialog(getShell(), wizard);
        int value = dialog.open();

        if (value == Window.OK) {
            String newName = wizard.getAlphabetName();
            String charset = wizard.getAlphabetCharset();

            AbstractAlphabet editing = getAlphabet(name);
            editing.setCharacterSet(charset.toCharArray());
            editing.setName(newName);

            fillTable();
            updateCharset(newName);
        }
    }

    /**
     * Will be performed after the "Remove" button has been clicked.
     *
     * @param name The name of the selected Alphabet
     */
    private void removeAction(String name) {
        if (MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.getString("AlphabetsPreferencePage.23"), Messages.getString("AlphabetsPreferencePage.24") + name + Messages.getString("AlphabetsPreferencePage.25"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            tempAlphas.remove(getAlphabet(name));
            fillTable();
            editButton.setEnabled(false);
            removeButton.setEnabled(false);
            defaultButton.setEnabled(false);
            updateCharset(null);
        }

    }

    /**
     * Will be performed after the "Set Default" button has been clicked.
     *
     * @param name The name of the selected Alphabet
     */
    private void defaultAction(String name) {
        if (MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.getString("AlphabetsPreferencePage.26"), Messages.getString("AlphabetsPreferencePage.27") + name + Messages.getString("AlphabetsPreferencePage.28"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            AbstractAlphabet a = getDefaultAlphabet();
            if (a != null) {
                a.setDefaultAlphabet(false);
            }
            AbstractAlphabet b = getAlphabet(name);
            b.setDefaultAlphabet(true);
            fillTable();
        }
    }

    /**
     * Handles the enabled/disabled state of the "Default" button.
     *
     * @param value The marker the state will be checked against
     */
    private void handleDefaultButton(String value) {
        if (value.equals(Messages.getString("AlphabetsPreferencePage.11"))) { //$NON-NLS-1$
            defaultButton.setEnabled(false);
        } else {
            defaultButton.setEnabled(true);
        }
    }

    /**
     * Handles the enabled/disabled state of the "Edit" and "Remove" buttons.
     *
     * @param value The marker the state will be checked against
     */
    private void handleEditAndRemoveButton(String value) {
        if (value.equals(Messages.getString("AlphabetsPreferencePage.11"))) { //$NON-NLS-1$
            editButton.setEnabled(false);
            removeButton.setEnabled(false);
        } else {
            editButton.setEnabled(true);
            removeButton.setEnabled(true);
        }
    }

    /**
     * Updates the displayed charset of the given alphabet.
     *
     * @param name The name of the alphabet
     */
    private void updateCharset(String name) {
        if (name != null) {
            AbstractAlphabet alpha = getAlphabet(name);
            charsetText.setText(new String(alpha.getCharacterSet()));
            charsetLabel.setText(Messages.getString("AlphabetsPreferencePage.31") + alpha.getCharacterSet().length + Messages.getString("AlphabetsPreferencePage.32")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            charsetText.setText(""); //$NON-NLS-1$
            charsetLabel.setText(Messages.getString("AlphabetsPreferencePage.34")); //$NON-NLS-1$
        }
    }

    /**
     * Convenience method to access the selected item.
     *
     * @param selection The table selection
     * @return The selected item, null if none
     */
    private TableItem getSelectedTableItem(TableItem[] selection) {
        if (selection.length != 1) {
            return null;
        } else {
            return selection[0];
        }
    }

    /**
     * Creates a new TableItem that will be displayed in the table.
     *
     * @param name The name of the alphabet
     * @param basic The basic parameter of the alphabet
     * @param isDefault The default parameter of the alphabet
     * @param index The index of the alphabet
     * @return A new TableItem with the given details
     */
    private TableItem newTableItem(String name, boolean basic, boolean isDefault, int index) {
        TableItem item = new TableItem(availableAlphabetsTable, index);
        item.setText(0, name);
        if (basic) {
            item.setText(1, Messages.getString("AlphabetsPreferencePage.11")); //$NON-NLS-1$
        }
        if (isDefault) {
            item.setText(2, Messages.getString("AlphabetsPreferencePage.11")); //$NON-NLS-1$
        }
        return item;
    }

    /**
     * Fills (and updates) the table displaying the various alphabets.
     */
    private void fillTable() {
        this.availableAlphabetsTable.removeAll();
        LogUtil.logInfo("filling the alphabets table"); //$NON-NLS-1$
        for (int i = 0; i < tempAlphas.size(); i++) {
            newTableItem(tempAlphas.get(i).getName(), tempAlphas.get(i).isBasic(), tempAlphas.get(i)
                    .isDefaultAlphabet(), i);
        }
    }

    /**
     * Adds this listener to the ui components.
     */
    private void registerListener() {
        addButton.addListener(SWT.Selection, this);
        editButton.addListener(SWT.Selection, this);
        removeButton.addListener(SWT.Selection, this);
        defaultButton.addListener(SWT.Selection, this);
        availableAlphabetsTable.addListener(SWT.Selection, this);
        enableFilterCheckBox.addListener(SWT.Selection, this);
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk() {
        performApply();
        return super.performOk();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    public void performDefaults() {
        // alphabets

        // revert2FactoryDefaultAlphabet
        if (!getDefaultAlphabet().getName().equals(AlphabetsManager.FACTORY_DEFAULT_ALPHABET)) {
            getDefaultAlphabet().setDefaultAlphabet(false);
            getAlphabet(AlphabetsManager.FACTORY_DEFAULT_ALPHABET).setDefaultAlphabet(true);
            fillTable();
        }

        // options
        enableFilterCheckBox.setSelection(AlphabetsPlugin.getDefault().getFilterCharsDefault());
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    public void performApply() {
        // alphabets

        // write to alphabetsmanager and save
        AlphabetsManager.getInstance().setAlphabets(toArray(tempAlphas), true);

        // options
        AlphabetsPlugin.getDefault().setFilterChars(enableFilterCheckBox.getSelection());

        // saving
        savePreferences();
        super.updateApplyButton();
    }

    /**
     * Saves the preferences.
     */
    private void savePreferences() {
        AlphabetsPlugin.getDefault().savePreferences();
    }

    /**
     * Convenience method to transform a Vector to an arraz.
     *
     * @param alphabets The vector that will be transformed
     * @return The resulting array
     */
    private AbstractAlphabet[] toArray(Vector<AbstractAlphabet> alphabets) {
        AbstractAlphabet[] a = new AbstractAlphabet[alphabets.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = alphabets.get(i);
        }
        return a;
    }

}
