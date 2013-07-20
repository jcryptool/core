package org.jcryptool.visual.babystepgiantstep.views;

import java.math.BigInteger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Miray Inel
 *
 */
public class Parameter extends TitleAreaDialog {
	private Text cyclicGroup;
	private Text generator;
	private Text groupElement;
	
	private String cyclicGroupValue;
	private String generatorValue;
	private String groupElementValue;
	
	private VerifyListener vl_numbers = new VerifyListener() {

		@Override
		public void verifyText(VerifyEvent e) {
			e.doit = true;

			String text = e.text;
			char[] chars = text.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				if (!Character.isDigit(chars[i])) {
					e.doit = false;
					break;
				}
			}
			//TODO prüfen ob gesamter wert größer als int ist

		}
	};
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public Parameter(Shell parentShell, String group, String generator, String groupElement) {
		super(parentShell);
		
		this.cyclicGroupValue = group;
		this.generatorValue = generator;
		this.groupElementValue = groupElement;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(Constants.paraMsg);
		setTitle("Parameter");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Constants.msgEnterACyclicGroup);
		
		cyclicGroup = new Text(container, SWT.BORDER);
		cyclicGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cyclicGroup.addVerifyListener(vl_numbers);
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText(Constants.msgEnterAGenerator);
		
		generator = new Text(container, SWT.BORDER);
		generator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		generator.addVerifyListener(vl_numbers);
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText(Constants.msgEnterEGroupelement);
		
		groupElement = new Text(container, SWT.BORDER);
		groupElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		groupElement.addVerifyListener(vl_numbers);
		
		cyclicGroup.setText(cyclicGroupValue);
		BigInteger a = new BigInteger(cyclicGroupValue);
		if (a.compareTo(Constants.MAX_INTEGER_BI) > 0) {
			cyclicGroup.setBackground(Constants.RED);
		}
		
		generator.setText(generatorValue);
		BigInteger b = new BigInteger(generatorValue);
		if (b.compareTo(Constants.MAX_INTEGER_BI) > 0) {
			generator.setBackground(Constants.RED);
		}
		
		groupElement.setText(groupElementValue);
		BigInteger c = new BigInteger(groupElementValue);
		if (c.compareTo(Constants.MAX_INTEGER_BI) > 0) {
			groupElement.setBackground(Constants.RED);
		}

		return area;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		
		if (cyclicGroup.getBackground().getRed() == 255 || generator.getBackground().getRed() == 255 || groupElement.getBackground().getRed() == 255) {
			this.getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}
	
	@Override
	protected void okPressed() {
		saveInput();
		
		super.okPressed();
	}


	private void saveInput() {
		cyclicGroupValue = cyclicGroup.getText();
		generatorValue = generator.getText();
		groupElementValue = groupElement.getText();
		
	}

	public String getCyclicGroupValue() {
		return cyclicGroupValue;
	}

	public String getGeneratorValue() {
		return generatorValue;
	}

	public String getGroupElementValue() {
		return groupElementValue;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 250);
	}

}
