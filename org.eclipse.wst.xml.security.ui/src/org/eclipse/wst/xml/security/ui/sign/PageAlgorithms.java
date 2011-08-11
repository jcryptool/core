/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.sign;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.sign.DigitalSignatureProperty;
import org.eclipse.wst.xml.security.core.sign.Signature;
import org.eclipse.wst.xml.security.core.utils.IAlgorithms;
import org.eclipse.wst.xml.security.core.utils.IGlobals;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;

/**
 * <p>
 * Last page of the wizard to create a XML signature. Lets the user select the <i>message digest algorithm</i>, the
 * <i>signature algorithm</i> and the <i>canonicalization</i> and <i>transformation algorithm</i>. Optional <i>Signature
 * Properties</i> can be provided. The <i>Signature ID</i> is optional but recommended.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageAlgorithms extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "SignPageAlgorithms"; //$NON-NLS-1$
    /** Message digest algorithms combo. */
    private Combo cMDA = null;
    /** Signature algorithms combo. */
    private Combo cSign = null;
    /** Canonicalization algorithms combo. */
    private Combo cCanon = null;
    /** Transformation algorithms combo. */
    private Combo cTransform = null;
    /** Signature ID text. */
    private Text tId = null;
    /** Start Encryption Wizard afterwards checkbox. */
    private Button bEncWiz = null;
    /** All signature IDs in the current XML document. */
    private String[] ids;
    /** The signature properties table. */
    private Table properties = null;
    /** The table viewer of the properties table. */
    private TableViewer tableViewer = null;
    /** Property of the id column. */
    private static final String ID_PROPERTY = "id";
    /** Property of the target column. */
    private static final String TARGET_PROPERTY = "target";
    /** Property of the content column. */
    private static final String CONTENT_PROPERTY = "content";
    /** The button to add a new property row in the table. */
    private Button bAddProperty = null;
    /** The button to remove a selected property row in the table. */
    private Button bRemoveProperty = null;
    /** Stored setting for the Encryption Wizard call after signing. */
    private static final String SETTING_CALL_ENCRYPTION_WIZARD = "sign_enc";
    /** Model for the XML Signature Wizard. */
    private Signature signature = null;

    /**
     * Constructor for PageAlgorithms.
     *
     * @param signature The signature wizard model
     */
    public PageAlgorithms(final Signature signature) {
        super(PAGE_NAME);
        setTitle(Messages.signatureTitle);
        setDescription(Messages.algorithmsDescription);

        this.signature = signature;

        determineIds();
    }

    /**
     * Creates the wizard page with the layout settings.
     *
     * @param parent Parent composite
     */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        FormLayout formLayout = new FormLayout();
        container.setLayout(formLayout);

        createPageContent(container);
        addListeners();
        setControl(container);
        loadSettings();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IContextHelpIds.WIZARD_SIGNATURE_ALGORITHMS);
    }

    /**
     * Fills this wizard page with content. Five groups (<i>Canonicalization and Transformation Algorithm</i>,
     * <i>Message Digest and Signature Algorithm</i>, <i>Signature Properties</i>, <i>Signature ID</i> and <i>Encryption
     * Wizard</i>) and all their widgets are inserted.
     *
     * @param parent Parent composite
     */
    private void createPageContent(final Composite parent) {
        FormLayout layout = new FormLayout();
        layout.marginTop = IGlobals.MARGIN / 2;
        layout.marginBottom = IGlobals.MARGIN / 2;
        layout.marginLeft = IGlobals.MARGIN / 2;
        layout.marginRight = IGlobals.MARGIN / 2;
        parent.setLayout(layout);

        // Five groups
        Group gTransform = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gTransform.setLayout(layout);
        gTransform.setText(Messages.canonicalizationTransformation);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gTransform.setLayoutData(data);

        Group gSign = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gSign.setLayout(layout);
        gSign.setText(Messages.messageDigestSignature);
        data = new FormData();
        data.top = new FormAttachment(gTransform, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gSign.setLayoutData(data);

        Group gProperties = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gProperties.setLayout(layout);
        gProperties.setText(Messages.properties);
        data = new FormData();
        data.top = new FormAttachment(gSign, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gProperties.setLayoutData(data);

        Group gID = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gID.setLayout(layout);
        gID.setText(Messages.signatureId);
        data = new FormData();
        data.top = new FormAttachment(gProperties, IGlobals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gID.setLayoutData(data);

        Group gEncWiz = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gEncWiz.setLayout(layout);
        gEncWiz.setText(Messages.encryptionWizard);
        data = new FormData();
        data.top = new FormAttachment(gID, IGlobals.MARGIN * 2, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(IGlobals.GROUP_NUMERATOR);
        gEncWiz.setLayoutData(data);

        // Elements for group "Canonicalization and Transformation Algorithm"
        cCanon = new Combo(gTransform, SWT.READ_ONLY);
        data = new FormData();
        data.left = new FormAttachment(gTransform);
        data.top = new FormAttachment(gTransform);
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        cCanon.setLayoutData(data);

        cTransform = new Combo(gTransform, SWT.READ_ONLY);
        data = new FormData();
        data.top = new FormAttachment(gTransform);
        data.left = new FormAttachment(cCanon, IGlobals.COMBO_MARGIN);
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        cTransform.setLayoutData(data);

        // Elements for group "Message Digest and Signature Algorithm"
        cMDA = new Combo(gSign, SWT.READ_ONLY);
        data = new FormData();
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        data.top = new FormAttachment(gSign);
        data.left = new FormAttachment(gSign);
        cMDA.setLayoutData(data);

        cSign = new Combo(gSign, SWT.READ_ONLY);
        data = new FormData();
        data.width = IGlobals.COMBO_LARGE_WIDTH;
        data.top = new FormAttachment(gSign);
        data.left = new FormAttachment(cMDA, IGlobals.COMBO_MARGIN);
        cSign.setLayoutData(data);

        // Elements for group "Signature Properties"
        properties = new Table(gProperties, SWT.FULL_SELECTION);
        properties.setHeaderVisible(true);
        properties.setLinesVisible(true);
        addProperties();
        data = new FormData();
        data.top = new FormAttachment(gProperties);
        data.left = new FormAttachment(gProperties);
        data.width = 350;
        data.height = 50;
        properties.setLayoutData(data);

        bAddProperty = new Button(gProperties, SWT.PUSH);
        bAddProperty.setText("+");
        data = new FormData();
        data.top = new FormAttachment(gProperties, 0, SWT.CENTER);
        data.left = new FormAttachment(properties, IGlobals.MARGIN);
        data.width = 50;
        bAddProperty.setLayoutData(data);

        bRemoveProperty = new Button(gProperties, SWT.PUSH);
        bRemoveProperty.setText("-");
        data = new FormData();
        data.top = new FormAttachment(gProperties, 25, SWT.CENTER);
        data.left = new FormAttachment(properties, IGlobals.MARGIN);
        data.width = 50;
        bRemoveProperty.setLayoutData(data);

        // Elements for group "Signature ID"
        Label lID = new Label(gID, SWT.SHADOW_IN);
        lID.setText(Messages.signatureId);
        data = new FormData();
        data.top = new FormAttachment(gID);
        data.left = new FormAttachment(gID);
        data.width = 80;
        lID.setLayoutData(data);

        tId = new Text(gID, SWT.SINGLE);
        tId.setTextLimit(IGlobals.ID_LIMIT);
        data = new FormData();
        data.top = new FormAttachment(gID);
        data.left = new FormAttachment(lID);
        data.width = IGlobals.MEDIUM_TEXT_WIDTH;
        tId.setLayoutData(data);

        // Elements for group "Encryption Wizard"
        bEncWiz = new Button(gEncWiz, SWT.CHECK);
        bEncWiz.setText(Messages.startEncryptionWizard);
        data = new FormData();
        data.top = new FormAttachment(gEncWiz);
        data.left = new FormAttachment(gEncWiz);
        bEncWiz.setLayoutData(data);
    }

    /**
     * Sets the header of the properties table and adds the context menu to add and remove signature properties rows.
     */
    private void addProperties() {
        tableViewer = new TableViewer(properties);

        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(50, 50, true));
        layout.addColumnData(new ColumnWeightData(125, 100, true));
        layout.addColumnData(new ColumnWeightData(175, 150, true));
        properties.setLayout(layout);

        TableColumn colId = new TableColumn(properties, SWT.LEFT);
        colId.setText(Messages.signaturePropertyId);
        colId.setToolTipText(Messages.signaturePropertyIdToolTip);

        TableColumn colTarget = new TableColumn(properties, SWT.LEFT);
        colTarget.setText(Messages.signaturePropertyTarget);
        colTarget.setToolTipText(Messages.signaturePropertyTargetToolTip);

        TableColumn colContent = new TableColumn(properties, SWT.LEFT);
        colContent.setText(Messages.signaturePropertyContent);
        colContent.setToolTipText(Messages.signaturePropertyContentToolTip);

        attachLabelProvider(tableViewer);
        attachCellEditors(tableViewer, properties);

        MenuManager popupMenu = new MenuManager();
        IAction newRowAction = new NewRowAction();
        IAction removeRowAction = new RemoveRowAction();
        popupMenu.add(newRowAction);
        popupMenu.add(removeRowAction);
        Menu menu = popupMenu.createContextMenu(properties);
        properties.setMenu(menu);
    }

    /**
     * Adds the LabelProvider to the TableViewer.
     *
     * @param viewer The TableViewer
     */
    private void attachLabelProvider(final TableViewer viewer) {
        viewer.setLabelProvider(new ITableLabelProvider() {

            public Image getColumnImage(final Object element, final int columnIndex) {
                return null;
            }

            public String getColumnText(final Object element, final int columnIndex) {
                if (element instanceof java.lang.String) {
                    return (String) element;
                }

                switch (columnIndex) {
                    case 0:
                        return ((EditableTableItem) element).getId();
                    case 1:
                        return ((EditableTableItem) element).getTarget();
                    case 2:
                        return ((EditableTableItem) element).getContent();
                    default:
                        return "Invalid column: " + columnIndex;
                }
            }

            public void addListener(final ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(final Object element, final String property) {
                return false;
            }

            public void removeListener(final ILabelProviderListener listener) {
            }
        });
    }

    /**
     * Adds the cell editors to the TableViewer.
     *
     * @param viewer The TableViewer
     * @param parent The parent composite
     */
    private void attachCellEditors(final TableViewer viewer, final Composite parent) {
        viewer.setCellModifier(new ICellModifier() {

            public boolean canModify(final Object element, final String property) {
                return true;
            }

            public Object getValue(final Object element, final String property) {
                if (ID_PROPERTY.equals(property)) {
                    return ((EditableTableItem) element).getId();
                } else if (TARGET_PROPERTY.equals(property)) {
                    return ((EditableTableItem) element).getTarget();
                } else if (CONTENT_PROPERTY.equals(property)) {
                    return ((EditableTableItem) element).getContent();
                } else {
                    return null;
                }
            }

            public void modify(final Object element, final String property, final Object value) {
                TableItem tableItem = (TableItem) element;
                EditableTableItem data = (EditableTableItem) tableItem.getData();
                if (ID_PROPERTY.equals(property)) {
                    data.setId(value.toString());
                } else if (TARGET_PROPERTY.equals(property)) {
                    data.setTarget(value.toString());
                } else if (CONTENT_PROPERTY.equals(property)) {
                    data.setContent(value.toString());
                }

                viewer.refresh(data);
            }
        });

        viewer.setCellEditors(new CellEditor[] {new TextCellEditor(parent), new TextCellEditor(parent),
                new TextCellEditor(parent)});

        viewer.setColumnProperties(new String[] {ID_PROPERTY, TARGET_PROPERTY, CONTENT_PROPERTY});
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        cMDA.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        cSign.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        cCanon.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        cTransform.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tId.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                if (e.keyCode == 8 || e.keyCode == 127) {
                    e.doit = true;
                } else if (!Utils.validateId(e.text)) {
                    e.doit = false;
                }
            }
        });
        tId.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        bAddProperty.addListener(SWT.Selection, this);
        bRemoveProperty.addListener(SWT.Selection, this);
    }

    /**
     * Determines all available signature ids in this XML document.
     */
    private void determineIds() {
        ids = signature.getIds();
    }

    /**
     * Determines the (error) message for the missing field.
     */
    private void dialogChanged() {
        if (cCanon.getText().equals("")) { //$NON-NLS-1$
            updateStatus(Messages.selectCanonicalization, DialogPage.INFORMATION);
            return;
        } else if (cTransform.getText().equals("")) { //$NON-NLS-1$
            updateStatus(Messages.selectTransformation, DialogPage.INFORMATION);
            return;
        } else if (cMDA.getText().equals("")) { //$NON-NLS-1$
            updateStatus(Messages.selectMessageDigest, DialogPage.INFORMATION);
            return;
        } else if (cSign.getText().equals("")) { //$NON-NLS-1$
            updateStatus(Messages.selectSignature, DialogPage.INFORMATION);
            return;
        }
        if (!tId.getText().equals("")) {
            if (ids != null && ids.length > 0) {
                boolean uniqueId = Utils.ensureIdIsUnique(tId.getText(), ids);

                if (!uniqueId) {
                    updateStatus(Messages.ambiguousSignatureId, DialogPage.ERROR);
                    return;
                }
            }
        }

        updateStatus(null, DialogPage.NONE);
    }

    /**
     * Shows a message to the user to complete the fields on this page.
     *
     * @param message The message for the user
     * @param status The status type of the message
     */
    private void updateStatus(final String message, final int status) {
        setMessage(message, status);
        if (message == null && getErrorMessage() == null) {
            setPageComplete(true);
            saveDataToModel();
        } else {
            setPageComplete(false);
        }
    }

    /**
     * Called when finishing the wizard.
     *
     * @return Finishing status
     */
    public boolean performFinish() {
        return saveDataToModel();
    }

    /**
     * Handles the events from this wizard page.
     *
     * @param e The triggered event
     */
    public void handleEvent(final Event e) {
        if (e.widget == bAddProperty) { // Button Add Property
            new NewRowAction().run();
        } else if (e.widget == bRemoveProperty) { // Button Remove Property
            new RemoveRowAction().run();
        }
    }

    /**
     * Sets the completed field on the wizard class when all the data is entered and the wizard can be completed.
     *
     * @return Page completion state
     */
    public boolean isPageComplete() {
        saveDataToModel();
        if (getMessage() == null && getErrorMessage() == null) {
            return true;
        }
        return false;
    }

    /**
     * Saves the selections on this wizard page to the model. Called on exit of the page.
     *
     * @return Saving state
     */
    private boolean saveDataToModel() {
        signature.setMessageDigestAlgorithm(cMDA.getText());
        signature.setSignatureAlgorithm(cSign.getText());
        signature.setCanonicalizationAlgorithm(cCanon.getText());
        signature.setTransformationAlgorithm(cTransform.getText());

        if (properties.getItemCount() > 0) {
            ArrayList<DigitalSignatureProperty> signatureProperties = new ArrayList<DigitalSignatureProperty>();
            for (int i = 0; i < properties.getItemCount(); i++) {
                TableItem tableItem = properties.getItem(i);
                EditableTableItem data = (EditableTableItem) tableItem.getData();
                if (!"".equals(data.getId()) && !"".equals(data.getTarget())) {
                    signatureProperties.add(new DigitalSignatureProperty(data.getId(), data.getTarget(), data
                            .getContent()));
                }
            }
            signature.setSignatureProperties(signatureProperties);
        }

        signature.setSignatureId(tId.getText());
        signature.setLaunchEncryptionWizard(bEncWiz.getSelection());

        return true;
    }

    /**
     * Called on enter of the page to fill the combo boxes based on the selections on the first wizard page (like the
     * Basic Security Profile and detached file selections). Preselects a default value in every combo box.
     */
    public void onEnterPage() {
        File detachedFile = signature.getDetachedFile();
        String type = signature.getSignatureType();
        String certificateType = signature.getKeyAlgorithm();
        String fileType = null;
        if (detachedFile != null) { // Detached file
            String fileName = detachedFile.getName();
            fileType = fileName.substring(fileName.lastIndexOf(".") + 1); //$NON-NLS-1$
        }
        if (signature.getBsp()) { // BSP selected
            cCanon.setItems(IAlgorithms.CANONICALIZATION_ALOGRITHMS_BSP);
            cCanon.setText(IAlgorithms.CANONICALIZATION_ALOGRITHMS_BSP[0]);
            if (fileType == null || fileType.equalsIgnoreCase("xml")) { //$NON-NLS-1$
                cTransform.setItems(IAlgorithms.TRANSFORMATION_ALOGRITHMS_BSP);
                cTransform.select(0);
            } else { // arbitrary data, no transformation
                cTransform.setItems(IAlgorithms.NONE_ALGORITHM);
                cTransform.select(0);
            }
            cMDA.setItems(IAlgorithms.MD_ALOGRITHMS_BSP);
            cMDA.select(0);
            cSign.setItems(IAlgorithms.SIGNATURE_ALOGRITHMS_BSP);
            cSign.select(0);
        } else { // BSP not selected
            cCanon.setItems(IAlgorithms.CANONICALIZATION_ALOGRITHMS);
            cCanon.select(0);
            // detached signature without XML file
            if (type.equalsIgnoreCase("detached") && !fileType.equalsIgnoreCase("xml")) {
                cTransform.setItems(IAlgorithms.NONE_ALGORITHM);
                cTransform.select(0);
            } else { // enveloped or enveloping or detached signature with XML file
                cTransform.setItems(IAlgorithms.TRANSFORMATION_ALOGRITHMS);
                cTransform.select(4);
            }
            cMDA.setItems(IAlgorithms.MD_ALOGRITHMS);
            cMDA.setText(IAlgorithms.MD_ALOGRITHMS[2]);
            // available signature algorithms depend on selected certificate
            if (certificateType.equals("SHA1withDSA")) {
                cSign.setItems(IAlgorithms.SIGNATURE_ALOGRITHMS_DSA);
                cSign.select(0);
            } else if (certificateType.equals("SHA1withECDSA")) {
                cSign.setItems(IAlgorithms.SIGNATURE_ALOGRITHMS_EC);
                cSign.select(0);
            } else if (certificateType.equals("SHA1withRSA")) {
                cSign.setItems(IAlgorithms.SIGNATURE_ALOGRITHMS_RSA);
                cSign.select(3);
            } else {
                cSign.setItems(IAlgorithms.SIGNATURE_ALOGRITHMS);
                cSign.select(0);
            }
        }
        setMessage(null);
    }

    /**
     * Loads the stored settings for this wizard page.
     */
    private void loadSettings() {
        bEncWiz.setSelection(getDialogSettings().get(SETTING_CALL_ENCRYPTION_WIZARD) != null ? getDialogSettings()
                .getBoolean(SETTING_CALL_ENCRYPTION_WIZARD) : false);
    }

    /**
     * Stores some settings of this wizard page in the current workspace.
     */
    protected void storeSettings() {
        IDialogSettings settings = getDialogSettings();
        settings.put(SETTING_CALL_ENCRYPTION_WIZARD, bEncWiz.getSelection());
    }

    /**
     * Action to add a new property row.
     *
     * @author Dominik Schadow (info@xml-sicherheit.de), www.xml-sicherheit.de
     * @version 2.0.0, 28.01.2007
     */
    private class NewRowAction extends Action {
        /**
         * Constructor.
         */
        public NewRowAction() {
            super(Messages.buttonAddProperty);
        }

        /**
         * Adds a new property row.
         */
        public void run() {
            tableViewer.add(new EditableTableItem("", "", ""));
        }
    }

    /**
     * Action to remove a selected property row.
     *
     * @author Dominik Schadow (info@xml-sicherheit.de), www.xml-sicherheit.de
     * @version 2.0.0, 28.01.2007
     */
    private class RemoveRowAction extends Action {
        /**
         * Constructor.
         */
        public RemoveRowAction() {
            super(Messages.buttonRemoveProperty);
        }

        /**
         * Removes the selected property row.
         */
        public void run() {
            if (properties.getSelectionIndex() > -1) {
                properties.remove(properties.getSelectionIndex());
            }
        }
    }
}
