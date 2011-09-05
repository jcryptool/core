// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer5;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;

public class KegverContent extends Composite {

	private Group grp1 = null;
	private Text grp1_t = null;
	private Button btn1 = null;
	private Button btn2 = null;
	private Button btn3 = null;

	public KegverContent(Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			int w = 800;
			int h = 400;

			Font system = getDisplay().getSystemFont();
			FontData base = system.getFontData()[0];
			FontData normdat = new FontData(base.getName(), base.getHeight(), base.getStyle());
			Font normfont = new Font(getDisplay(), normdat);
			FormLayout thisLayout = new FormLayout();
			this.setSize(w, h);
			this.setLayout(thisLayout);

			{
				btn1 = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btn1LData = new FormData();
				btn1LData.top = new FormAttachment(this, 10);
				btn1LData.left = new FormAttachment(this, 10);
				btn1.setLayoutData(btn1LData);
				btn1.setText(Messages.KegverContent_btn1);
				btn1.setToolTipText(Messages.KegverContent_btn1_tt);
				btn1.setFont(normfont);
				btn1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						nextStep();
					}
				});
			}

			{
				btn2 = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btn2LData = new FormData();
				btn2LData.top = new FormAttachment(btn1, 10);
				btn2LData.left = new FormAttachment(this, 10);
				btn2.setLayoutData(btn2LData);
				btn2.setText(Messages.KegverContent_btn2);
				btn2.setToolTipText(Messages.KegverContent_btn2_tt);
				btn2.setFont(normfont);
				btn2.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						editCA();
					}
				});
			}

			{
				btn3 = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btn3LData = new FormData();
				btn3LData.top = new FormAttachment(btn2, 10);
				btn3LData.left = new FormAttachment(this, 10);
				btn3.setLayoutData(btn3LData);
				btn3.setText(Messages.KegverContent_btn3);
				btn3.setToolTipText(Messages.KegverContent_btn3_tt);
				btn3.setFont(normfont);
				btn3.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						editUser();
					}
				});
			}

			{
				grp1 = new Group(this, SWT.BORDER);
				FormLayout grpstepsLayout = new FormLayout();
				grp1.setLayout(grpstepsLayout);
				FormData grp1LData = new FormData();
				grp1LData.left = new FormAttachment(btn1, 10);
				grp1LData.top = new FormAttachment(this, 10);
				grp1LData.width = w - 20;
				grp1LData.height = h - 20;
				grp1.setLayoutData(grp1LData);
				grp1.setText(Messages.KegverContent_grp1);
				grp1.setFont(normfont);
				{
					// FIXME: Why is SWT.H_SCROLL not working? : As long it is not working, SWT.WRAP
					grp1_t = new Text(grp1, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.H_SCROLL |  SWT.V_SCROLL);
	                FormData grp1_tLData = new FormData();
	                grp1_t.setEditable(false);
	                grp1_t.setFont(normfont);
	                grp1_tLData.left = new FormAttachment(grp1 , 0);
	                grp1_tLData.top = new FormAttachment(grp1 , 0);
	                grp1_tLData.width =  w - 47;
	                grp1_tLData.height = h - 20;
					grp1_t.setBackground(getBackground());
					grp1_t.setText(Messages.KegverContent_grp1_t);
	                grp1_t.setLayoutData(grp1_tLData);
				}
			}

			getShell().setDefaultButton(btn1);

			this.layout();

		} catch (Exception ex) {
			LogUtil.logError(ex);
		}
	}

	private void nextStep() {
		L5.nextStep(grp1_t);
	}


	private void editCA() {
	    LogUtil.logError(new Exception("not implemented yet"));
	}


	private void editUser() {
	    LogUtil.logError(new Exception("not implemented yet"));
	}
}