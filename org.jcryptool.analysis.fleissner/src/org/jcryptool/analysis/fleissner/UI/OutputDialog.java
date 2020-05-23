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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BinaryOperator;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.eclipse.swt.layout.FillLayout;

/**
 * @author Dinah
 *
 */
public class OutputDialog extends TitleAreaDialog {
    
    private ArrayList<TextPresentation> analysisOutput = new ArrayList<TextPresentation>();
    
    private Text output;
    private String outputString;
    private LoadFiles lf;
    private Composite texts;
    private Text[] textnames;
//    private Label[] labels;

	private Composite area;

	private Composite container;

    /**
     * @param parentShell
     * @wbp.parser.constructor
     */
    public OutputDialog(Shell parentShell) {
        super(parentShell);
    }

    public OutputDialog(Shell shell, ArrayList<String> outputInput) {
		this(shell);
		for (String input : outputInput) {
			this.addText(new TextPresentation() {{
				text=input;
			}});
		}
	}
    
	public void addText(TextPresentation presentation) {
    	this.analysisOutput.add(presentation);
    }
    
    public void create(String title, String message) {
        super.create();
        setTitle(title);
        setMessage(message);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        area = (Composite) super.createDialogArea(parent);
        container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        TextFoldViewer textFoldViewer = new TextFoldViewer(scrolledComposite, SWT.NONE, this.analysisOutput);
        GridData foldviewLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textFoldViewer.setLayoutData(foldviewLayoutData);
        textFoldViewer.redrawListeners.add(new Runnable() {
			@Override
			public void run() {
				parent.layout(true, true);
				parent.requestLayout();
				Point computeSize = textFoldViewer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				scrolledComposite.setMinSize(computeSize);
			}
		});
        
        scrolledComposite.setContent(textFoldViewer);
        Point computeSize = textFoldViewer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        computeSize.y = computeSize.y + 40;
		scrolledComposite.setMinSize(computeSize);
		scrolledComposite.setMinWidth(SWT.DEFAULT);
		scrolledComposite.getHorizontalBar().setEnabled(false);
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
    
    

    @Override
    protected boolean isResizable() {
        return true;
    }

    // save content of the Text into *.txt file
    private void saveInput() {
        
        lf = new LoadFiles();
        outputString = new String("");
        for (TextPresentation output : analysisOutput) {
            outputString += output.text;
            String lastLine = output.text.substring(output.text.lastIndexOf("\n"));
            String visualDivide = "\n";
            for (int i=0;i<lastLine.length()*2;i++)
                visualDivide+="-";
            outputString+=visualDivide+"\n";
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
      for (TextPresentation output : analysisOutput) {

//    String group+String.valueOf(i);
//    textnames[i] = new Text(texts, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//    textnames[i].setLayoutData(gridData);
//    textnames[i].setFont(FontService.getNormalMonospacedFont());
//          if (i<10) {
              textnames[i].setText(output.text.trim());
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
