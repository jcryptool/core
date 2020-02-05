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
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
    
    private ArrayList<String> analysisOutput = new ArrayList<String>();
    
    private Text output;
    private String outputString;
    private LoadFiles lf;
    private Composite texts;
    private Text[] textnames;
//    private Label[] labels;

    /**
     * @param parentShell
     */
    public OutputDialog(Shell parentShell, ArrayList<String> futter) {
        super(parentShell);
        this.analysisOutput = futter;
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
        container.setLayout(new GridLayout(1,true));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        setHelpAvailable(false);      

        createOutput(container);
        container.layout();

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
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.V_SCROLL);
        Composite parent = new Composite(scrolledComposite, SWT.NONE);

        GridLayout gridLayoutParent = new GridLayout(1, false);
        parent.setLayout(gridLayoutParent);
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        texts = new Composite(parent, SWT.NONE);
        texts.setLayout(new GridLayout(1, false));
        data.heightHint = parent.getClientArea().height;
        texts.setLayoutData(data);

        textnames = new Text[analysisOutput.size()];
//        labels = new Label[analysisOutput.size()];

        int size =0, i=0;
        for (String output : analysisOutput) {
            
//            labels[i] = new Label(texts, SWT.NONE);
//            labels[i].setText(output);
//            labels[i].requestLayout();

            textnames[i] = new Text(texts, SWT.WRAP | SWT.MULTI);
            textnames[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
            textnames[i].setFont(FontService.getNormalMonospacedFont());
            textnames[i].setText(output);
            int height = textnames[i].getLineCount()*textnames[i].getLineHeight();
            size+= height;
            textnames[i].requestLayout();
            texts.requestLayout();

            i++;
        }
        texts.layout();

        scrolledComposite.setContent(parent);
        scrolledComposite.setMinSize(parent.computeSize(SWT.DEFAULT, size));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.layout(true);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    // save content of the Text into *.txt file
    private void saveInput() {
        
        lf = new LoadFiles();
        outputString = new String("");
        for (String output : analysisOutput) {
            outputString += output;
        }
//        outputString = output.getText();
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
    

    /**
     * @return the texts
     */
    public Composite getTexts() {
        return texts;
    }

    /**
     * @param texts the texts to set
     */
    public void setTexts() {
        
//        this.analysisOutput = analysisOutput;
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        output = new Text(this.texts, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//        output.setLayoutData(gridData);
//        output.setFont(FontService.getNormalMonospacedFont());
        int i=0;
//        Text[] textnames = new Text[analysisOutput.size()];
      for (String output : analysisOutput) {
//    String group+String.valueOf(i);
//    textnames[i] = new Text(texts, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//    textnames[i].setLayoutData(gridData);
//    textnames[i].setFont(FontService.getNormalMonospacedFont());
//          if (i<10) {
              textnames[i].setText(output);
              i++;
//          }
}
        
//        for (String output : analysisOutput) {
////            String group+String.valueOf(i);
//            Group group = new Group(texts, SWT.NONE);
//            group.setLayout(new GridLayout());
//            group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
//            Text text = new Text(group, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//            text.setLayoutData(gridData);
//            text.setFont(FontService.getNormalMonospacedFont());
//            text.setText(output);
//
//        }
    }
}
