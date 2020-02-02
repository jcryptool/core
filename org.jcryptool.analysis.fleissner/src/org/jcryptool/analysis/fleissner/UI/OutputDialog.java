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

import javax.swing.JTextArea;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.analysis.fleissner.UI.LoadFiles;
import org.jcryptool.analysis.fleissner.UI.FleissnerWindow;
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
    private Label[] labels;
    private Group[] labelGroups;

    /**
     * @param parentShell
     */
    public OutputDialog(Shell parentShell, ArrayList<String> futter) {
        super(parentShell);
        this.analysisOutput = futter;
    }
    
    public void create(String title, String message/*, ArrayList<String> input*/) {
        super.create();
        setTitle(title);
        setMessage(message);
//        this.analysisOutput = new ArrayList<String>(input.size());
//        for (String inputString : input) {
//            System.out.println("Input Nr. "+analysisOutput.indexOf(inputString)+": "+inputString);
//            analysisOutput.add(inputString);
//        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        ScrolledComposite sc = new ScrolledComposite(area, SWT.V_SCROLL);
        Composite container = new Composite(sc, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      GridLayout layout = new GridLayout();
      container.setLayout(layout);
      setHelpAvailable(false);
        
        
//        Composite container = new Composite(area, /*SWT.NONE*/SWT.V_SCROLL);
//        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        GridLayout layout = new GridLayout();
//        container.setLayout(layout);
//        setHelpAvailable(false);

        createOutput(container);
        container.layout();
        
      sc.setContent(container);
      sc.setMinSize(container.computeSize( SWT.DEFAULT, SWT.DEFAULT));
      sc.setExpandHorizontal(true);
      sc.setExpandVertical(true);
      sc.layout();
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
        
//        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        texts = new Composite(container, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        
        
        
//        output = new Text(this.texts, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//        output.setLayoutData(gridData);
//        output.setFont(FontService.getNormalMonospacedFont());
//        int i=0;
//        for (String output : analysisOutput) {
//            System.out.println("Output Nr. "+analysisOutput.indexOf(output)+": "+output);
//        }
//        for (String inputString : org.jcryptool.analysis.fleissner.UI.FleissnerWindow.) {
//            System.out.println("Input Nr. "+analysisOutput.indexOf(inputString)+": "+inputString);
//            analysisOutput.add(inputString);
//        }
        
//        ScrolledComposite scrolledComposite = new ScrolledComposite(container, /*SWT.H_SCROLL |*/ SWT.V_SCROLL);
//        Composite parent = new Composite(scrolledComposite, SWT.NONE);
//
//        GridLayout gridLayoutParent = new GridLayout(1, false);
//        parent.setLayout(gridLayoutParent);
        
//        fw = new FleissnerWindow(parent, SWT.NONE);
//        GridLayout layout = new GridLayout(1,true);
//        layout.marginWidth = 0;
//        layout.marginHeight = 0;
//        fw.setLayout(layout);
//        fw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));


        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        
//        data.heightHint = container.getSize().y;
        texts = new Composite(/*scrolledComposite,*/container, /*SWT.RESIZE SWT.MULTI | SWT.WRAP | SWT.NONE SWT.WRAP|SWT.MULTI |*/ SWT.V_SCROLL);
        texts.setLayout(new GridLayout(1, false));
        texts.setLayoutData(data);
        int i=0;
//        for (int i=0; i<textnames.length; i++) {
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = texts.getClientArea().width;
//        gridData.verticalSpan = gridData.minimumHeight;
//        gridData.widthHint = texts.getSize().x;
        
//        Group undertexts = new Group(texts, SWT.NONE);
//        undertexts.setLayout(new GridLayout(1, false));
//        undertexts.setLayoutData(gridData);
        
        JTextArea[] area = new JTextArea[analysisOutput.size()];
        
        textnames = new Text[analysisOutput.size()];
        labels = new Label[analysisOutput.size()];
        labelGroups = new Group[analysisOutput.size()];
//        composites = new Composite[analysisOutput.size()];
        for (String futter : analysisOutput) {
            
//            labelGroups[i] = new Group(container, SWT.NONE);
            labels[i] = new Label(/*labelGroups[i]*/container, /*SWT.BORDER | SWT.MULTI |*/ SWT.WRAP/*SWT.NONE*/);
            labels[i].setText(futter);
//          textnames[i].setLayoutData(gridData);
//          textnames[i].setFont(FontService.getNormalMonospacedFont());
//          textnames[i].setText(futter);
//          textnames[i].setSize(SWT.DEFAULT, textnames[i].getLineCount()*textnames[i].getLineHeight());
//          textnames[i].requestLayout();
//            texts.add
//            area[i] = new JTextArea(futter);
//            area[i].
//            labels[i].setLayoutData(gridData);
//            composites[i].setFont(FontService.getNormalMonospacedFont());
            labels[i].setBackground(org.jcryptool.core.util.colors.ColorService.WHITE);
            labels[i].setForeground(org.jcryptool.core.util.colors.ColorService.BLACK);
            
            labels[i].isAutoScalable();
            labels[i].requestLayout();
//            texts.layout();
          i++;
        }
//        texts.layout();
        
//        scrolledComposite.setContent(texts);
//        scrolledComposite.setMinSize(texts.computeSize( 300, 300));
//        scrolledComposite.setExpandHorizontal(true);
//        scrolledComposite.setExpandVertical(true);
//        scrolledComposite.layout();
        
        


//        for (String output : analysisOutput) {
////            String group+String.valueOf(i);
//            textnames[i] = new Text(group, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//            textnames[i].setLayoutData(gridData);
//            textnames[i].setFont(FontService.getNormalMonospacedFont());
//            textnames[i].setText(output);
//            i++;
//        }

//        output = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
//        output.setLayoutData(gridData);
//        output.setFont(FontService.getNormalMonospacedFont());
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
