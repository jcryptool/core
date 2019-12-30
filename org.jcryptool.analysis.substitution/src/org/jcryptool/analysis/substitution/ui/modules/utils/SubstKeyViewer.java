//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.analysis.substitution.ui.modules.Messages;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionKey;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionKeyValidityException;

public class SubstKeyViewer extends Shell {

	protected static SubstKeyViewer activeShell;
	private Text k1PreviewText;
	private Text k2PreviewText;
	
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void show(final Map<Character, Character> mapping, final AbstractAlphabet alphabet) {
		if(activeShell!=null && !activeShell.isDisposed()) {
			activeShell.getDisplay().syncExec(new Runnable() {
				
				@Override
				public void run() {
					activeShell.close();
				}
			});
		}
		
		Display.getCurrent().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				try {
					Display display = Display.getCurrent();
					SubstKeyViewer shell = new SubstKeyViewer(display, mapping, alphabet);
					SubstKeyViewer.activeShell = shell;
					shell.open();
					shell.layout();
					shell.pack();
					while (!shell.isDisposed()) {
						if (!display.readAndDispatch()) {
							display.sleep();
						}
					}
				} catch (Exception ex) {
				    LogUtil.logError(ex);
				}
			}
		});
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public SubstKeyViewer(Display display, Map<Character, Character> mapping, AbstractAlphabet alphabet) {
		super(display, SWT.SHELL_TRIM);
		createContents();
		
		Map<Character, Character> key1Data = mapping;
		String key1String = generateKey1String(key1Data, alphabet);
		
		String key2String = Messages.SubstitutionAnalysisPanel_12;
		if(isSubstComplete(mapping, alphabet)) {
			try {
				SubstitutionKey key = new SubstitutionKey(SubstitutionKey.invertSubstitution(key1Data));
				key2String = key.toStringSubstitutionCharList(alphabet);
			} catch (SubstitutionKeyValidityException ex) {
			    LogUtil.logError(ex);
			}
		}
		k1PreviewText.setText(key1String);
		k2PreviewText.setText(key2String);
	}

	private boolean isSubstComplete(Map<Character, Character> mapping, AbstractAlphabet alphabet) {
		for(Character c: alphabet.asList()) {
			if(mapping.get(c) == null) return false;
		}
		return true;
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(Messages.SubstKeyViewer_0);
		setSize(450, 300);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		setLayout(layout);
		
		
		
		Label k1PreviewLabel = new Label(this, SWT.NONE);
		k1PreviewLabel.setText(org.jcryptool.analysis.substitution.ui.modules.Messages.SubstitutionAnalysisPanel_8);
		
		k1PreviewText = new Text(this, SWT.BORDER);
		k1PreviewText.setEditable(false);
		k1PreviewText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		k1PreviewText.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL)); //$NON-NLS-1$

		Label k2PreviewLabel = new Label(this, SWT.NONE);
		k2PreviewLabel.setText(org.jcryptool.analysis.substitution.ui.modules.Messages.SubstitutionAnalysisPanel_10);
		
		k2PreviewText = new Text(this, SWT.BORDER);
		k2PreviewText.setEditable(false);
		k2PreviewText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		k2PreviewText.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL)); //$NON-NLS-1$
	}

	private String generateKey1String(Map<Character, Character> key1Data, AbstractAlphabet alphabet) {
		StringBuilder sb = new StringBuilder();
		for(char c: alphabet.getCharacterSet()) {
			Character mapping = key1Data.get(c);
			if(mapping != null) {
				sb.append(mapping);
			} else {
				sb.append('?');
			}
		}
		return sb.toString();
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
