/*==========================================================================
 * 
 * GotoDialog.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.5 $
 * $Date: 2012/11/06 16:45:23 $
 * $Name:  $
 * 
 * Created on 2-Feb-2004
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.ehep.core.EHEP;

/**
 * @author Marcel Palko
 * @author randallco@users.sourceforge.net
 */
public class GotoDialog extends Dialog {
	private Button btnDecAddress;
	private Button btnHexAddress;
	private Text txtAddress;
	private Button btnAbsoluteMode;
	private Button btnRelativeDownMode;
	private Button btnRelativeUpMode;
	private int rowIndex;
	private int columnIndex;
	private HexTablePointer newPosition;
		
	public GotoDialog(Shell parent, int rowIndex, int columnIndex) {
		super(parent);
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.GotoDialog_0);
	}
	
	public HexTablePointer getNewPosition() {
		return newPosition;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = (Composite) super.createDialogArea(parent);

		//
		// Panel with user input components
		//
		Composite inputPanel = new Composite(control, SWT.NONE);
		GridLayout inputGrid = new GridLayout();
		inputGrid.numColumns = 2;
		inputGrid.marginHeight = 0;
		inputGrid.marginWidth = 0;
		inputGrid.horizontalSpacing = 5;
		inputPanel.setLayout(inputGrid);
		inputPanel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));

		//
		// Panel with "Go to" address
		//
		Composite addressPanel = new Composite(inputPanel, SWT.NONE);
		GridLayout addressGrid = new GridLayout();
		addressGrid.numColumns = 1;
		addressPanel.setLayout(addressGrid);
		addressPanel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		createAddressPanel(addressPanel);
		
		//
		// Panel with "Go to" mode
		//
		Composite modePanel = new Composite(inputPanel, SWT.NONE);
		GridLayout modeGrid = new GridLayout();
		modeGrid.numColumns = 1;
		modePanel.setLayout(modeGrid);
		modePanel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		createModePanel(modePanel);

		return control;
	}

	private void createAddressPanel(Composite parent) {
		Group addressGroup = new Group(parent, SWT.NONE);
		addressGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		addressGroup.setText(Messages.GotoDialog_1);
		GridLayout addressLayout = new GridLayout();
		addressLayout.numColumns = 1;
		addressGroup.setLayout(addressLayout);

		btnDecAddress = new Button(addressGroup, SWT.RADIO);
		btnDecAddress.setText(Messages.GotoDialog_2);
		btnDecAddress.setSelection(true);

		btnHexAddress = new Button(addressGroup, SWT.RADIO);
		btnHexAddress.setText(Messages.GotoDialog_3);
		btnHexAddress.setSelection(false);

		txtAddress = new Text(addressGroup, SWT.SINGLE | SWT.BORDER);
		txtAddress.setTextLimit(9);
		txtAddress.setText("000000000"); // To make this widget wide enough (see Bug #1277533) //$NON-NLS-1$
		txtAddress.setFocus();
		
		txtAddress.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				String addr = txtAddress.getText();
				
				if (addr != null && addr.compareTo("000000000") == 0) { //$NON-NLS-1$
					txtAddress.setText(""); //$NON-NLS-1$
					txtAddress.setTextLimit(8);
				}
			}
			
			public void focusLost(FocusEvent e) {}
		});
	}

	private void createModePanel(Composite parent) {
		Group modeGroup = new Group(parent, SWT.NONE);
		modeGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		modeGroup.setText(Messages.GotoDialog_4);
		GridLayout modeLayout = new GridLayout();
		modeLayout.numColumns = 1;
		modeGroup.setLayout(modeLayout);

		btnAbsoluteMode = new Button(modeGroup, SWT.RADIO);
		btnAbsoluteMode.setText(Messages.GotoDialog_8);
		btnAbsoluteMode.setSelection(true);

		btnRelativeDownMode = new Button(modeGroup, SWT.RADIO);
		btnRelativeDownMode.setText(Messages.GotoDialog_9);
		btnRelativeDownMode.setSelection(false);

		btnRelativeUpMode = new Button(modeGroup, SWT.RADIO);
		btnRelativeUpMode.setText(Messages.GotoDialog_10);
		btnRelativeUpMode.setSelection(false);
	}
	
	@Override
	protected void okPressed() {
		//
		// Check the user input
		//
		int iAddress = 0;
		String sAddress = txtAddress.getText();
		boolean addressIsDec = btnDecAddress.getSelection();
		newPosition = null;
		
		if (sAddress.length() == 0) {
			MessageDialog.openError(getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.GotoDialog_11);
			txtAddress.setFocus();
			return;
		} // if
		
		try {
			iAddress = Integer.parseInt(sAddress, (addressIsDec) ? 10 : 16);
		}
		catch (NumberFormatException nfeSize) {
			MessageDialog.openError(getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.GotoDialog_12 + ((addressIsDec) ? Messages.GotoDialog_13 : Messages.GotoDialog_14) + Messages.GotoDialog_15);
			txtAddress.setFocus();
			txtAddress.selectAll();
			return;
		}
				
		if (iAddress < 0) {
			MessageDialog.openError(getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.GotoDialog_16);
			txtAddress.setFocus();
			txtAddress.selectAll();
			return;
		} // if

		//
		// Calculate new position (offset)
		//
		if (btnAbsoluteMode.getSelection()) {
			//
			// 'Absolute mode' selected
			//
			newPosition = new HexTablePointer(0, 0).move(iAddress);
		} // if
		else {
			//
			// 'Relative mode' selected
			//
			if (btnRelativeDownMode.getSelection()) {
				newPosition = new HexTablePointer(rowIndex, columnIndex).move(iAddress-1);
			}
			else {
				newPosition = new HexTablePointer(rowIndex, columnIndex).move(-iAddress-1);
			}
		} // else
		super.okPressed();
	}
}
