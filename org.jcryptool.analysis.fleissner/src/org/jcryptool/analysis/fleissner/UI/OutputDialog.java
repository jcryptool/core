// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.fleissner.UI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.analysis.fleissner.UI.LoadFiles;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;

/**
 * @author Dinah
 *
 */
public class OutputDialog extends TitleAreaDialog {
    
    private Text output;
    private String outputString;
    private LoadFiles lf;

    /**
     * @param parentShell
     */
    public OutputDialog(Shell parentShell) {
        super(parentShell);
    }
    
    public void create(String title, String message) {
        super.create();
        setTitle(title);
        setMessage(message);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        setHelpAvailable(false);

        createOutput(container);
        return area;
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.OutputDialog_detailOutput);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
     super.createButtonsForButtonBar(parent);
     getButton(IDialogConstants.OK_ID).setText(Messages.OutputDialog_save);
    }

    private void createOutput(Composite container) {
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);

        output = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        output.setLayoutData(gridData);
        output.setFont(FontService.getNormalMonospacedFont());
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    // save content of the Text into *.txt file
    private void saveInput() {
        
        lf = new LoadFiles();
        outputString = output.getText();
        String save = lf.openFileDialog(SWT.SAVE);
  
        // Check if the user selected a file in the dialog. If not save will be null.
        // This check avoids exceptions.
        if (save != null) {
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(save)); 
                bw.write(outputString);
                bw.newLine();
                bw.close();
             } catch (IOException e) {
                LogUtil.logError(Activator.PLUGIN_ID, e);
             }
        }
    }

    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }

    /**
     * @return the output
     */
    public Text getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(Text output) {
        this.output = output;
    }
}
