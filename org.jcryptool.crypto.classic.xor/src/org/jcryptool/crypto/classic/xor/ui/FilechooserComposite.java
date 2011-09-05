// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.xor.ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;


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
public class FilechooserComposite extends org.eclipse.swt.widgets.Composite {
	private Text textFile;
	private Button btnBrowse;
	private TextfieldInput<String> fileInput;

	/**
	* Auto-generated main method to display this
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/

	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/
	protected void checkSubclass() {
	}

	/**
	* Auto-generated method to display this
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/

	public FilechooserComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
		fileInput = new TextfieldInput<String>() {
			@Override
			protected Text getTextfield() {
				return textFile;
			}
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}
			@Override
			public String readContent() {
				return getTextfield().getText();
			}
			@Override
			protected String getDefaultContent() {
				return ""; //$NON-NLS-1$
			}
			@Override
			public String getName() {
				return Messages.FilechooserComposite_inputname_file;
			}
		};


	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.numColumns = 2;
			thisLayout.marginHeight = 0;
			thisLayout.marginWidth = 0;
			this.setLayout(thisLayout);
			{
				GridData textFileLData = new GridData();
				textFileLData.grabExcessHorizontalSpace = true;
				textFileLData.horizontalAlignment = GridData.FILL;
				textFileLData.widthHint = 150;
				textFileLData.minimumWidth = 150;
				textFile = new Text(this, SWT.BORDER);
				textFile.setLayoutData(textFileLData);
			}
			{
				btnBrowse = new Button(this, SWT.PUSH | SWT.CENTER);
				GridData btnBrowseLData = new GridData();
				btnBrowse.setLayoutData(btnBrowseLData);
				btnBrowse.setText(Messages.FilechooserComposite_browse);
				btnBrowse.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				        dialog.setFilterPath(DirectoryService.getUserHomeDir());
				        dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, "*.ver", "*.dat", "*.xor"});

				        String filename = dialog.open();
				        if(filename != null) {
				        	fileInput.writeContent(filename);
				        	fileInput.reread("file dialog"); //$NON-NLS-1$
				        }
					}
				});
			}
			this.layout();
			pack();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
	}

	public TextfieldInput<String> getFileInput() {
		return fileInput;
	}

	public void updateForEnabledState(boolean enabled) {
		Control[] allCtrls = new Control[]{
			textFile,
			btnBrowse
		};
		for(Control c: allCtrls) {
			c.setEnabled(enabled);
		}
	}

	public boolean hasValidFile() {
		if(fileInput != null) {
			File f = new File(fileInput.getContent());
			return f.exists();
		}
		return false;
	}

}
