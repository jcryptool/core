//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui;

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.transpositionanalysis.TranspositionAnalysisPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/**
 * @author SLeischnig views a simple text in a little shell.
 */
public class KeyViewer extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private String title;
	private Label label1;
	private Text text2;
	private Text text1;
	private Composite composite3;
	private Composite composite2;
	private Composite composite1;
	private Label label3;
	private Button button1;
	private Label label2;
	private boolean visible = false;
	private TranspositionKey key;

	private boolean inverted = false;

	private String btnNormalMsg = Messages.KeyViewer_invertone;
	private String btnInvertedMsg = Messages.KeyViewer_invertback;

	char[] alpha;

	/**
	 * creates the text viewer, setting title and displayed text.
	 * 
	 * @param parent the parent
	 * @param pTitle title of the shell
	 * @param pText  displayed text
	 */
	public KeyViewer(final Shell parent, final String pTitle, final TranspositionKey key) {
		super(parent, SWT.NONE);
		this.title = pTitle;
		this.key = key;

		alpha = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnoprstuvwxyz".toCharArray(); //$NON-NLS-1$
	}

	/**
	 * sets the displayed text
	 */
	public final void setDisplayedText(final String pText) {
	}

	/**
	 * opens the TextViewer.
	 */
	public final void open() {
		try {
			Shell parent = getParent();

			dialogShell = new Shell(parent, SWT.DIALOG_TRIM);
			dialogShell.setLayout(new GridLayout());
			dialogShell.setText(title);

			composite1 = new Composite(dialogShell, SWT.BORDER);
			composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			composite1.setLayout(new GridLayout());

			label1 = new Label(composite1, SWT.NONE);
			label1.setText(Messages.KeyViewer_prettyversion);

			text1 = new Text(composite1, SWT.READ_ONLY | SWT.BORDER);
			GridData text1LData = new GridData(SWT.FILL, SWT.FILL, true, false);
			text1LData.horizontalIndent = 10;
			text1.setLayoutData(text1LData);
			text1.setText(key.toStringOneRelative());

			composite2 = new Composite(dialogShell, SWT.BORDER);
			composite2.setLayout(new GridLayout());
			composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			label2 = new Label(composite2, SWT.NONE);
			label2.setText(Messages.KeyViewer_cryptoversion);

			text2 = new Text(composite2, SWT.READ_ONLY | SWT.BORDER);
			GridData text2LData = new GridData(SWT.FILL, SWT.FILL, true, false);
			text2LData.horizontalIndent = 10;
			text2.setLayoutData(text2LData);
			text2.setText(key.toUnformattedChars(alpha));

			composite3 = new Composite(dialogShell, SWT.BORDER);
			composite3.setLayout(new GridLayout());
			composite3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			label3 = new Label(composite3, SWT.WRAP);
			GridData label3LData = new GridData(SWT.FILL, SWT.FILL, true, false);
			label3LData.widthHint = 400;
			label3.setLayoutData(label3LData);
			label3.setText(Messages.KeyViewer_invertdescription);

			button1 = new Button(composite3, SWT.PUSH | SWT.CENTER);
			GridData button1LData = new GridData(SWT.FILL, SWT.FILL, true, false);
			button1LData.verticalIndent = 20;
			button1.setLayoutData(button1LData);
			button1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					switchInverted();
				}
			});
			button1.setText(Messages.KeyViewer_invertone);

			// Enforce the shell to go to its minimum size.
			dialogShell.pack();

			dialogShell.setLocation(getParent().toDisplay((int) Math.round(Math.random() * 50),
					(int) Math.round(Math.random() * 4) + 200));
			dialogShell.open();

			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			visible = false;
		} catch (Exception e) {
			LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
		}
	}

	private void switchInverted() {
		key = key.getReverseKey();
		if (inverted) {
			button1.setText(btnNormalMsg);
			inverted = false;
		} else {
			button1.setText(btnInvertedMsg);
			inverted = true;
		}
		text1.setText(key.toStringOneRelative());
		text2.setText(key.toUnformattedChars(alpha));
	}

	public boolean isVisible() {
		return visible;
	}

}