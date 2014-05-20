package org.jcryptool.visual.crtverification.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class ChooseCertComposite extends Composite {
	private Table table;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ChooseCertComposite(Composite parent, int style) {
        super(parent, style);
        
        Button btnTestButton = new Button(this, SWT.NONE);
  
        btnTestButton.setBounds(346, 169, 94, 28);
        btnTestButton.setText("Load");
        
        table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setBounds(10, 10, 430, 153);
        
        TableItem tableItem = new TableItem(table, SWT.NONE);
        tableItem.setText("Certificate");
        tableItem.setChecked(true);
        tableItem.setText(new String[] {});
        // Example End
        
    }
}
