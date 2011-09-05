// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.ui.wizards;

import java.security.SecureRandom;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.ECDH.Messages;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.util.JavaSecureRandomWrapper;

public class SecretKeyComposite extends Composite {

	private Group groupSecret = null;
	private Spinner spnrSecret = null;
	private Button btnGenerate = null;
	private String name;
	private int max;
	private boolean large;
	private FlexiBigInt bigMax;
	private FlexiBigInt secret;
	private Text largeSecret;

	public SecretKeyComposite(Composite parent, int style, String n, int s, int m) {
		super(parent, style);
		large = false;
		name = n;
		max = m;
		initialize();
		if(s > 0)
			spnrSecret.setSelection(s);
	}

	public SecretKeyComposite(Composite parent, int style, String n, FlexiBigInt s, FlexiBigInt m) {
		super(parent, style);
		large = true;
		name = n;
		bigMax = m;
		secret = s;
		initialize();
		if(secret == null)
			secret = new FlexiBigInt(bigMax.bitCount(), new JavaSecureRandomWrapper(new SecureRandom()));
		largeSecret.setText(secret.toString(10));
	}

	private void initialize() {
		createGroupSecret();
		setSize(new Point(412, 57));
		setLayout(new GridLayout());
	}

	/**
	 * This method initializes groupSecret
	 *
	 */
	private void createGroupSecret() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalIndent = 4;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		groupSecret = new Group(this, SWT.NONE);
		groupSecret.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupSecret.setLayout(gridLayout);
		groupSecret.setText(name + Messages.getString("ECDHWizSK.groupSecret")); //$NON-NLS-1$
		Label label = new Label(groupSecret, SWT.NONE);
		label.setText(Messages.getString("SecretKeyComposite.1")); //$NON-NLS-1$
		if(large) {
			largeSecret = new Text(groupSecret, SWT.BORDER);
			largeSecret.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			largeSecret.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					try {
						FlexiBigInt fbi = new FlexiBigInt(largeSecret.getText(), 10);
						if(fbi.max(bigMax).equals(fbi))
							largeSecret.setText(secret.toString(10));
						else
							secret = fbi;
					} catch (Exception ex) {
						largeSecret.setText(secret.toString(10));
					}

				}
			});
			btnGenerate = new Button(groupSecret, SWT.NONE);
			btnGenerate.setText(Messages.getString("ECDHWizSK.btnGenerate")); //$NON-NLS-1$
			btnGenerate.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {}
				public void widgetSelected(SelectionEvent e) {
					largeSecret.setText(new FlexiBigInt(bigMax.bitCount(), new JavaSecureRandomWrapper(new SecureRandom())).toString(10));
				}
			});
		} else {
			spnrSecret = new Spinner(groupSecret, SWT.NONE);
			spnrSecret.setLayoutData(gridData1);
			spnrSecret.setMinimum(1);
			spnrSecret.setMaximum(max - 1);
			spnrSecret.setSelection((new Random()).nextInt(max - 2) + 1);
			btnGenerate = new Button(groupSecret, SWT.NONE);
			btnGenerate.setText(Messages.getString("ECDHWizSK.btnGenerate")); //$NON-NLS-1$
			btnGenerate.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {}
				public void widgetSelected(SelectionEvent e) {
					Random r = new Random();
					spnrSecret.setSelection(r.nextInt(max - 2) + 1);
				}
			});
		}
	}

	public int getSecret() {
		return spnrSecret.getSelection();
	}

	public FlexiBigInt getLargeSecret() {
		return secret;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
