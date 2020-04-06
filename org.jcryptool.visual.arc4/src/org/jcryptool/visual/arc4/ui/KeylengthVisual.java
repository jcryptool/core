package org.jcryptool.visual.arc4.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.jcryptool.visual.arc4.ARC4Con;

public class KeylengthVisual extends Composite {

	public KeylengthVisual(ARC4Composite parent, int style) {
		super(parent, style);

        GridLayout tlayout = new GridLayout(1, true);
        tlayout.marginHeight = 0;
        tlayout.marginWidth = 0;
        setLayout(tlayout);
        
        Group keylengthGroup = new Group(this, SWT.NONE);
        keylengthGroup.setLayout(new GridLayout(2, true));
        keylengthGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        keylengthGroup.setText("Keylength");
        keylengthGroup.setToolTipText("Here you can specifiy the length of the key");
        
        Label keyLengthlabel = new Label(keylengthGroup, SWT.CENTER);
        keyLengthlabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        keyLengthlabel.setText("Keylength:");
        
        Spinner keylengthSpinner = new Spinner(keylengthGroup, SWT.NONE);
        keylengthSpinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        keylengthSpinner.setSelection(ARC4Con.DEFAULT_DATAVECTOR_VISUAL_LENGTH);
        keylengthSpinner.setMinimum(0);
        keylengthSpinner.setMaximum(16);
        
        keylengthSpinner.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				parent.setKeyLength(keylengthSpinner.getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
	}

}
