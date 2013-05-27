package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;

public class KeyComposite extends Composite implements SelectionListener {

	public KeyComposite(Composite parent, int style) {
		super(parent, style);
		
		Label lblSelectKey = new Label(this, SWT.NONE);
		lblSelectKey.setBounds(10, 14, 59, 14);
		lblSelectKey.setText("New Label");
		
		Combo cmbSelectKey = new Combo(this, SWT.READ_ONLY);
		cmbSelectKey.setBounds(75, 10, 337, 22);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
