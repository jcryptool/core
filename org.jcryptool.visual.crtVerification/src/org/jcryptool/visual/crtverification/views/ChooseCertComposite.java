package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.Page;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

public class ChooseCertComposite extends Composite {
	private Table table;
    private ChooseCertPage page;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ChooseCertComposite(Composite parent, int style, ChooseCertPage p) {
        super(parent, style);
        page = p;
        
        Button btnLoad = new Button(this, SWT.NONE);
  
        btnLoad.setBounds(346, 169, 94, 28);
        btnLoad.setText("Load");
        btnLoad.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    page.setPageComplete(true);
                } catch (Exception ex) {
                    LogUtil.logError(Activator.PLUGIN_ID, ex);
                }
            }
        });
        
        table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setBounds(10, 10, 430, 153);
        
        TableItem tableItem = new TableItem(table, SWT.NONE);
        tableItem.setText("Certificate");
        tableItem.setChecked(false);
        tableItem.setText(new String[] {});
        
    }
}
