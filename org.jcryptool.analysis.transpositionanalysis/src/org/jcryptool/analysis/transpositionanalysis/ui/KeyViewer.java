//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
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
* Copyright (c) 2010 JCrypTool Team and Contributors
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
     * @param pText displayed text
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

            GridLayout dialogShellLayout = new GridLayout();
            dialogShellLayout.makeColumnsEqualWidth = true;
            dialogShell.setLayout(dialogShellLayout);
            dialogShell.setText(title);
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(427, 285);
            {
            	composite1 = new Composite(dialogShell, SWT.NONE |SWT.BORDER);
            	GridLayout composite1Layout = new GridLayout();
            	composite1Layout.makeColumnsEqualWidth = true;
            	GridData composite1LData = new GridData();
            	composite1LData.grabExcessHorizontalSpace = true;
            	composite1LData.horizontalAlignment = GridData.FILL;
            	composite1LData.verticalAlignment = GridData.BEGINNING;
            	composite1.setLayoutData(composite1LData);
            	composite1.setLayout(composite1Layout);
            	{
            		label1 = new Label(composite1, SWT.NONE);
            		GridData label1LData = new GridData();
            		label1.setLayoutData(label1LData);
            		label1.setText(Messages.KeyViewer_prettyversion);
            	}
            	{
            		text1 = new Text(composite1, SWT.NONE|SWT.BORDER);
            		GridData text1LData = new GridData();
            		text1LData.horizontalIndent = 10;
            		text1LData.grabExcessHorizontalSpace = true;
            		text1LData.horizontalAlignment = GridData.FILL;
            		text1.setLayoutData(text1LData);
            		text1.setEditable(false);
            		text1.setText(key.toStringOneRelative());
            	}
            }
            {
            	composite2 = new Composite(dialogShell, SWT.NONE |SWT.BORDER);
            	GridLayout composite2Layout = new GridLayout();
            	composite2Layout.makeColumnsEqualWidth = true;
            	GridData composite2LData = new GridData();
            	composite2LData.grabExcessHorizontalSpace = true;
            	composite2LData.horizontalAlignment = GridData.FILL;
            	composite2LData.verticalAlignment = GridData.BEGINNING;
            	composite2.setLayoutData(composite2LData);
            	composite2.setLayout(composite2Layout);
            	{
            		label2 = new Label(composite2, SWT.NONE | SWT.WRAP);
            		GridData label2LData = new GridData();
            		label2.setLayoutData(label2LData);
            		label2.setText(Messages.KeyViewer_cryptoversion);
            	}
            	{
            		text2 = new Text(composite2, SWT.NONE|SWT.BORDER);
            		GridData text2LData = new GridData();
            		text2LData.horizontalIndent = 10;
            		text2LData.grabExcessHorizontalSpace = true;
            		text2LData.horizontalAlignment = GridData.FILL;
            		text2.setLayoutData(text2LData);
            		text2.setEditable(false);
            		text2.setText(key.toUnformattedChars(alpha));
            	}
            }
            {
            	composite3 = new Composite(dialogShell, SWT.NONE |SWT.BORDER);
            	GridLayout composite3Layout = new GridLayout();
            	composite3Layout.makeColumnsEqualWidth = true;
            	GridData composite3LData = new GridData();
            	composite3LData.grabExcessHorizontalSpace = true;
            	composite3LData.horizontalAlignment = GridData.FILL;
            	composite3LData.verticalAlignment = GridData.FILL;
            	composite3LData.grabExcessVerticalSpace = true;
            	composite3.setLayoutData(composite3LData);
            	composite3.setLayout(composite3Layout);
            	{
            		label3 = new Label(composite3, SWT.NONE| SWT.WRAP);
            		GridData label3LData = new GridData();
            		label3LData.widthHint = 380;
            		label3LData.grabExcessVerticalSpace = true;
            		label3LData.verticalAlignment = SWT.FILL;
            		label3.setLayoutData(label3LData);
            		label3.setText(Messages.KeyViewer_invertdescription);
            	}
            	{
            		button1 = new Button(composite3, SWT.PUSH | SWT.CENTER);
            		GridData button1LData = new GridData();
            		button1LData.horizontalIndent = 10;
            		button1LData.grabExcessHorizontalSpace = true;
            		button1LData.horizontalAlignment = GridData.FILL;
            		button1.setLayoutData(button1LData);
            		button1.addSelectionListener(new SelectionAdapter() {
            			public void widgetSelected(SelectionEvent evt) {
            				switchInverted();
            			}
            		});
            		button1.setText(Messages.KeyViewer_invertone);
            	}
            }
            dialogShell.setLocation(getParent().toDisplay((int) Math.round(Math.random() * 50),
                    (int) Math.round(Math.random() * 4) + 200));
            dialogShell.open();
            visible = true;

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
    	if(inverted) {
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