package org.jcryptool.crypto.ui.alphabets.customalphabets.customhistory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.ui.alphabets.composite.AtomAlphabet;

public class CustomAlphabetItem extends Composite {
	private Label lblName;
	private Label lblContent;
	private Label lblName_1;
	private Label lblContent_1;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CustomAlphabetItem(Composite parent, AbstractAlphabet alpha) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(2, false));
		MouseAdapter clickListener = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				CustomAlphabetItem.this.notifyListeners(SWT.MouseDown, null);
			}
		};
		{
			lblName_1 = new Label(this, SWT.NONE);
			lblName_1.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
			lblName_1.setText(Messages.CustomAlphabetItem_name_label1);
			lblName_1.addMouseListener(clickListener);
		}
		{
			lblName = new Label(this, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lblName.setText(alpha.getName());
			lblName.addMouseListener(clickListener);
		}
		{
			lblContent_1 = new Label(this, SWT.NONE);
			lblContent_1.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
			lblContent_1.setText(Messages.CustomAlphabetItem_content_label1);
			lblContent_1.addMouseListener(clickListener);
		}
		{
			lblContent = new Label(this, SWT.WRAP);
			GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			layoutData.widthHint = 150;
			lblContent.setLayoutData(layoutData);
			lblContent.setText(AbstractAlphabet.alphabetContentAsString(alpha.getCharacterSet()));
			lblContent.addMouseListener(clickListener);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
