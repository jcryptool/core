package org.jcryptool.visual.jctca.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.jctca.listeners.TabItemListener;

public class SecondUserTab {

	public SecondUserTab(TabFolder parent, Group exp, int style) {

		TabItem t = new TabItem(parent, SWT.NONE);
		t.setText("2. Benutzer");
		Group generalGroup = new Group(parent, SWT.NONE);
		generalGroup.setLayoutData(new GridData(SWT.TOP, SWT.TOP, true,
				true, 1, 1));
		t.setControl(generalGroup);
		//ShowSig sSig= new ShowSig(generalGroup, exp);
		generalGroup.setLayout(new GridLayout(1, false));
		
		
	
	}
}