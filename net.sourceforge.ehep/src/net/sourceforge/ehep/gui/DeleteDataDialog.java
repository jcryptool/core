/*==========================================================================
 * 
 * DeleteDataDialog.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.3 $
 * $Date: 2012/11/06 16:45:23 $
 * $Name:  $
 * 
 * Created on 31-Jan-2004
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.ehep.core.EHEP;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class DeleteDataDialog extends Dialog {
	private int result = 0;
	private Text dataSizeText;
	private Combo sizeCombo;
	private Button chbToTheEnd;
	public DeleteDataDialog(Shell parent) {
		super(parent);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.DeleteDataDialog_0);
	}
	
	public int getResult() {
		return result;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = (Composite) super.createDialogArea(parent);
		//
		// Panel with data size entry
		//
		Composite sizePanel = new Composite(control, SWT.NONE);
		GridLayout sizeGrid = new GridLayout();
		sizeGrid.numColumns = 3;
		sizePanel.setLayout(sizeGrid);
		sizePanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Label label = new Label(sizePanel, SWT.NULL);
		label.setText(Messages.DeleteDataDialog_1);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dataSizeText = new Text(sizePanel, SWT.SINGLE | SWT.BORDER);
		dataSizeText.setTextLimit(8);
		dataSizeText.setText("1"); //$NON-NLS-1$
		dataSizeText.selectAll();
		dataSizeText.setFocus();
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 50;
		dataSizeText.setLayoutData(data);
		sizeCombo = new Combo(sizePanel, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		sizeCombo.add("B"); //$NON-NLS-1$
		sizeCombo.add("kB"); //$NON-NLS-1$
		sizeCombo.add("MB"); //$NON-NLS-1$
		sizeCombo.select(0);

		//
		// Options
		//
		Composite optionPanel = new Composite(control, SWT.NONE);
		GridLayout optionGrid = new GridLayout();
		optionGrid.numColumns = 3;
		optionPanel.setLayout(optionGrid);
		optionPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		chbToTheEnd = new Button(optionPanel, SWT.CHECK);
		chbToTheEnd.setText(Messages.DeleteDataDialog_6);
		chbToTheEnd.setSelection(false);
		chbToTheEnd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//
				// Enable/disable other GUI controls according to the checkbox
				//
				dataSizeText.setEnabled(!chbToTheEnd.getSelection());
				sizeCombo.setEnabled(!chbToTheEnd.getSelection());
			} // widgetSelected()
		} // SelectionAdapter()
		);

		//
		// Panel with dialog buttons
		//
		Composite buttonPanel = new Composite(control, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		buttonPanel.setLayout(grid);
		buttonPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		return control;
	}
	
	@Override
	protected void okPressed() {
		String sDataSize = dataSizeText.getText().trim();
		int iDataSize = 0;
		
		if (chbToTheEnd.getSelection()) {
			//
			// User wants to delete the rest of the file
			//
			result = -1;
		} else  {
		
			try {
				iDataSize = Integer.parseInt(sDataSize,10);
			}
			catch (NumberFormatException nfeSize) {
				MessageDialog.openError(getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.DeleteDataDialog_7);
				return;
			}
			
			if (iDataSize < 0) {
				MessageDialog.openError(getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.DeleteDataDialog_8);
				return;
			} // if
	
			result = (int) (iDataSize * Math.pow(1024,sizeCombo.getSelectionIndex()));
		}

		super.okPressed();
	}
}
