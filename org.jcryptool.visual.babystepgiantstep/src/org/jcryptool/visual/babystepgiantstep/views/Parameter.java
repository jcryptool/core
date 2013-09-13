package org.jcryptool.visual.babystepgiantstep.views;

import java.math.BigInteger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * 
 * @author Miray Inel
 * 
 */
public class Parameter extends TitleAreaDialog {

	private Text group;
	private Text generator;
	private Text groupElement;

	private String groupValue;
	private String generatorValue;
	private String groupElementValue;

	private VerifyListener vl_numbers = new VerifyListener() {

		@Override
		public void verifyText(VerifyEvent e) {
			Text textField = null;
			e.doit = true;
			
			if (e.getSource() instanceof Text) {
				textField = (Text) e.getSource();
			}
			
			if ( textField == null || ((textField.getText().length() == 0 && e.text.compareTo("0") == 0 ) ||
					(textField.getSelection().x == 0 && e.keyCode == 48)) ) {
				e.doit = false;
				return;
			}

			String text = e.text;
			char[] chars = text.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				if (chars.length > 1 && i == 0 && chars[i] == '0') {
					e.doit = false;
					break;
				}
				
				
				if (!Character.isDigit(chars[i])) {
					e.doit = false;
					break;
				}
			}
		}
	};

	private ModifyListener integerModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			btnNextPrime.setEnabled(true);

			Text textField = null;

			if (e.getSource() instanceof Text) {
				textField = (Text) e.getSource();
			}

			if (!textField.getText().isEmpty()) {
				BigInteger textFieldValue = new BigInteger(textField.getText());
				if (textFieldValue.compareTo(Constants.MAX_INTEGER_BI) >= 0 || textField.getText().compareTo("1") == 0) { //$NON-NLS-1$
					textField.setBackground(Constants.RED);
				} else {
					textField.setBackground(Constants.WHITE);
					multipleCheck();
				}
			} else {
				textField.setBackground(Constants.RED);
			}

			checkBackgroundValues();
		}

	};
	private Button btnNextPrime;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public Parameter(Shell parentShell, String group, String generator, String groupElement) {
		super(parentShell);

		this.groupValue = group;
		this.generatorValue = generator;
		this.groupElementValue = groupElement;
	}

	protected void multipleCheck() {
		BigInteger textGroupElementValue = null;
		BigInteger textGroupValue = null;

		if (group.getText().isEmpty()) {
			textGroupValue = new BigInteger(groupValue);
		} else {
			textGroupValue = new BigInteger(group.getText());
		}

		if (groupElement.getText().isEmpty()) {
			textGroupElementValue = new BigInteger(groupElementValue);
		} else {
			textGroupElementValue = new BigInteger(groupElement.getText());
		}

		if (textGroupValue.isProbablePrime(10000) && textGroupElementValue.compareTo(textGroupValue) < 0) {
			group.setBackground(Constants.WHITE);
			groupElement.setBackground(Constants.WHITE);
		} else if (textGroupValue.isProbablePrime(10000) && textGroupElementValue.compareTo(textGroupValue) >= 0) {
			if (textGroupElementValue.mod(textGroupValue).compareTo(BigInteger.ZERO) == 0) {
				group.setBackground(Constants.RED);
				groupElement.setBackground(Constants.RED);
			} else {
				group.setBackground(Constants.WHITE);
				groupElement.setBackground(Constants.WHITE);
			}
		} else if (!textGroupValue.isProbablePrime(10000)
				&& (textGroupElementValue.mod(textGroupValue).compareTo(BigInteger.ZERO) == 0 || textGroupValue.mod(textGroupElementValue).compareTo(BigInteger.ZERO) == 0)) {
			group.setBackground(Constants.RED);
			groupElement.setBackground(Constants.RED);
		} else {
			group.setBackground(Constants.RED);
			groupElement.setBackground(Constants.WHITE);

		}
	}

	protected void checkBackgroundValues() {
		if (this.getButton(IDialogConstants.OK_ID) != null) {
			if (group.getBackground().toString().compareTo(Constants.WHITE.toString()) == 0 && generator.getBackground().toString().compareTo(Constants.WHITE.toString()) == 0
					&& groupElement.getBackground().toString().compareTo(Constants.WHITE.toString()) == 0 && !group.getText().isEmpty() && !groupElement.getText().isEmpty()
					&& !generator.getText().isEmpty() && group.getText().compareTo("1") != 0 && generator.getText().compareTo("1") != 0 && groupElement.getText().compareTo("1") != 0) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this.getButton(IDialogConstants.OK_ID).setEnabled(true);
			} else {
				this.getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(Messages.Parameter_0);
		setTitle(Messages.Parameter_1);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.BabystepGiantstepView_4);

		group = new Text(container, SWT.BORDER);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group.addVerifyListener(vl_numbers);
		group.addModifyListener(integerModifyListener);

		btnNextPrime = new Button(container, SWT.NONE);
		btnNextPrime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!group.getText().isEmpty()) {
					BigInteger tmp = new BigInteger(group.getText());
					if (!tmp.isProbablePrime(10000)) {
						tmp = tmp.nextProbablePrime();
					} else {
						tmp = new BigInteger(group.getText());
						tmp.add(BigInteger.ONE);
						tmp = tmp.nextProbablePrime();
					}
					group.setText(tmp.toString());
					group.setBackground(Constants.WHITE);
					btnNextPrime.setEnabled(false);

					multipleCheck();
				}
			}
		});
		btnNextPrime.setText(Messages.Parameter_3);

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText(Messages.BabystepGiantstepView_5);

		generator = new Text(container, SWT.BORDER);
		generator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		generator.addVerifyListener(vl_numbers);
		generator.addModifyListener(integerModifyListener);

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText(Messages.BabystepGiantstepView_6);

		groupElement = new Text(container, SWT.BORDER);
		groupElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		groupElement.addVerifyListener(vl_numbers);
		groupElement.addModifyListener(integerModifyListener);

		group.setText(groupValue);
		BigInteger a = new BigInteger(groupValue);
		if (a.compareTo(Constants.MAX_INTEGER_BI) > 0) {
			group.setBackground(Constants.RED);
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
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.Parameter_2, false);

		if (group.getBackground().toString().compareTo(Constants.RED.toString()) == 0 || generator.getBackground().toString().compareTo(Constants.RED.toString()) == 0
				|| groupElement.getBackground().toString().compareTo(Constants.RED.toString()) == 0 || group.getText().compareTo("1") == 0 || generator.getText().compareTo("1") == 0 //$NON-NLS-1$ //$NON-NLS-2$
				|| groupElement.getText().compareTo("1") == 0) { //$NON-NLS-1$
			this.getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	@Override
	protected void okPressed() {
		saveInput();

		super.okPressed();
	}

	private void saveInput() {
		groupValue = group.getText();
		generatorValue = generator.getText();
		groupElementValue = groupElement.getText();

	}

	public String getCyclicGroupValue() {
		return groupValue;
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
		return new Point(480, 250);
	}

}
