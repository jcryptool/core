package org.jcryptool.visual.jctca.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.jctca.CertificateViews.ShowReq;

/**
 * creates the tabitem and the view for the CA
 * @author mmacala
 *
 */
public class CertificationTab {
	/**
	 * Generating Certification Tab 
	 * @param parent TabFolder to which the new TabItem is added
	 * @param exp Group in which the explanation is shown
	 * @param style well, yeah, don't know
	 **/
	public CertificationTab(TabFolder parent, Composite exp, int style) {

		TabItem t = new TabItem(parent, SWT.NONE);
		t.setText(Messages.CertificationTab_tabitem_name);
		Group generalGroup = new Group(parent, SWT.NONE);
		generalGroup.setLayoutData(new GridData(SWT.TOP, SWT.TOP, true, true,
				1, 1));
		t.setControl(generalGroup);
		@SuppressWarnings("unused")
		ShowReq sReq = new ShowReq(generalGroup, exp);
		generalGroup.setLayout(new GridLayout(1, false));
		
	}
}
