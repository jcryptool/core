// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.properties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.calendar.CalendarService;
import org.jcryptool.core.util.units.UnitsService;
import org.jcryptool.fileexplorer.FileExplorerPlugin;

public class ResourcePropertyPage extends PropertyPage {

    private boolean prevReadOnly;
    private boolean prevHidden;

    private boolean newReadOnly;

    private File file;
    private String contentType;

    private boolean initValid = false;

    /**
     * Constructor for ResourcePropertyPage.
     */
    public ResourcePropertyPage() {
        super();
    }

    private boolean init() {
        IFileStore fileStore = (IFileStore) getElement();
        if (fileStore == null)
            return false;

        try {
            file = fileStore.toLocalFile(0, new NullProgressMonitor());
        } catch (CoreException ex) {
            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
            return false;
        }

        URL url = null;
        try {
            URI uri = file.toURI();
            if (uri != null)
                url = uri.toURL();
        } catch (MalformedURLException e) {
            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, e);
        }

        if (url != null) {
            try {
                URLConnection urlConnection = url.openConnection();
                if (urlConnection != null) {
                    urlConnection.connect();
                    contentType = urlConnection.getContentType();
                }
            } catch (IOException e) {
                LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, e);
            }
        }

        prevReadOnly = !file.canWrite();
        prevHidden = file.isHidden();

        newReadOnly = prevReadOnly;

        return true;
    }

    private void addFirstSection(Composite parent) {
        Composite composite = createDefaultComposite(parent);

        // Label for path field
        Label pathLabel = new Label(composite, SWT.NONE);
        pathLabel.setText(Messages.FileExplorerProperties_path);

        // Path text field
        Text pathValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        pathValueText.setText(file.getAbsolutePath());

        // Label for type field
        Label typeLabel = new Label(composite, SWT.NONE);
        typeLabel.setText(Messages.FileExplorerProperties_type);

        // Type text field
        Text typeValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        typeValueText.setText(contentType == null ? "" : contentType);

        // Label for size field
        Label sizeLabel = new Label(composite, SWT.NONE);
        sizeLabel.setText(Messages.FileExplorerProperties_size);

        // Type text field
        Text sizeValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        sizeValueText.setText(UnitsService.humanReadableByteCount(file.length(), true));

        // Label for last modified field
        Label lastModifiedLabel = new Label(composite, SWT.NONE);
        lastModifiedLabel.setText(Messages.FileExplorerProperties_lastModified);

        // Type text field
        Text lastModifiedValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        lastModifiedValueText.setText(CalendarService.getDateAndTime(file.lastModified()));
    }

    private void addSecondSection(Composite parent) {
        // Label for attributes
        Label attributesLabel = new Label(parent, SWT.NONE);
        attributesLabel.setText(Messages.FileExplorerProperties_attributes);

        Composite composite = createDefaultComposite(parent);

        // read only checkbox
        final Button readOnlyCheck = new Button(composite, SWT.CHECK);
        readOnlyCheck.setSelection(prevReadOnly);
        readOnlyCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                newReadOnly = readOnlyCheck.getSelection();
            }
        });

        // Label for readonly checkbox
        Label readonlyLabel = new Label(composite, SWT.NONE);
        readonlyLabel.setText(Messages.FileExplorerProperties_readonly);

        // hidden checkbox
        final Button hiddenCheck = new Button(composite, SWT.CHECK);
        hiddenCheck.setEnabled(false);
        hiddenCheck.setSelection(prevHidden);

        // Label for hidden checkbox
        Label hiddensLabel = new Label(composite, SWT.NONE);
        hiddensLabel.setText(Messages.FileExplorerProperties_hidden);
    }

    private void addSeparator(Composite parent) {
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        separator.setLayoutData(gridData);
    }

    /**
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(Composite parent) {
        initValid = init();

        if (!initValid) {
            LogUtil.logError(Messages.FileExplorerProperties_initFailed);
            return null;
        }

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL);
        data.grabExcessHorizontalSpace = true;
        composite.setLayoutData(data);

        addFirstSection(composite);
        addSeparator(composite);
        addSecondSection(composite);
        return composite;
    }

    private Composite createDefaultComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        composite.setLayoutData(data);

        return composite;
    }

    protected void performDefaults() {
        super.performDefaults();
        // Populate the owner text field with the default value
    }

    public boolean performOk() {
        if (!initValid)
            return false;

        if (prevReadOnly != newReadOnly) {
            if (!file.setWritable(!newReadOnly, false))
                LogUtil.logError("failed to set read only property for " + file.getAbsolutePath());
        }

        return true;
    }
}
