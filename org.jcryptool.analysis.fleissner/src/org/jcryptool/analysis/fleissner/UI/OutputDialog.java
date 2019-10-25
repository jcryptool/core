/**
 * 
 */
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
import org.jcryptool.analysis.fleissner.UI.LoadFiles;

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
    
    public void create(String string) {
        super.create();
        setTitle(string);
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
    protected void createButtonsForButtonBar(Composite parent) {
     super.createButtonsForButtonBar(parent);
     getButton(IDialogConstants.OK_ID).setText("Speichern");
    }

    private void createOutput(Composite container) {
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);

        output = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        output.setLayoutData(gridData);
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
  
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(save/*+"./FleissnerOutput.txt"*/)); 
            bw.write(outputString);
            bw.newLine();
            bw.close();
         } catch (IOException e) {
            e.printStackTrace();
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
