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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;




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
 * Chooser for the read/write direction of a transposition table.
 *
 * @author Simon L
 */
public class ReadDirectionChooser extends org.eclipse.swt.widgets.Composite {
	private AbstractUIInput<Boolean> input;
	private Button btnRowwise;
	private Button btnColumnar;
	private String inputName;
	private Listener radioGroup;
	private boolean flat;

	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/
	protected void checkSubclass() {
	}


	public ReadDirectionChooser(org.eclipse.swt.widgets.Composite parent) {
		this(parent, false);
	}

	public ReadDirectionChooser(org.eclipse.swt.widgets.Composite parent, boolean flat) {
		super(parent, SWT.NONE);
		this.flat = flat;
		initGUI();
	}

	private void initGUI() {
		radioGroup = new Listener() {
			public void handleEvent(Event event) {
				Button b = (Button) event.widget;
				if(b.getSelection() == false) {
					b.setSelection(true);
				} else {
					if(event.widget == btnColumnar) {
						btnRowwise.setSelection(! btnColumnar.getSelection());
					} else {
						btnColumnar.setSelection(! btnRowwise.getSelection());
					}
					input.synchronizeWithUserSide();
				}
			}
		};

		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.marginWidth = 0;
			thisLayout.marginHeight = 0;
			thisLayout.numColumns = 2;
			thisLayout.horizontalSpacing = 0;
			thisLayout.makeColumnsEqualWidth = true;
			this.setLayout(thisLayout);
			{
				btnColumnar = new Button(this, SWT.TOGGLE | SWT.FLAT | SWT.CENTER);
				GridData btnColumnarLData = new GridData();
				btnColumnarLData.grabExcessHorizontalSpace = true;
				btnColumnarLData.horizontalAlignment = GridData.FILL;
				btnColumnarLData.verticalAlignment = GridData.FILL;
				if(flat) btnColumnarLData.heightHint = 18;
				btnColumnar.setLayoutData(btnColumnarLData);
				btnColumnar.setText(Messages.ReadDirectionChooser_0);
				btnColumnar.addListener(SWT.Selection, radioGroup);
			}
			{
				btnRowwise = new Button(this, SWT.TOGGLE | SWT.FLAT | SWT.CENTER);
				GridData btnRowwiseLData = new GridData();
				btnRowwiseLData.grabExcessHorizontalSpace = true;
				btnRowwiseLData.horizontalAlignment = GridData.FILL;
				btnRowwiseLData.verticalAlignment = GridData.FILL;
				if(flat) btnRowwiseLData.heightHint = 18;
				btnRowwise.setLayoutData(btnRowwiseLData);
				btnRowwise.setText(Messages.ReadDirectionChooser_1);
				btnRowwise.addListener(SWT.Selection, radioGroup);
			}
			this.layout();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		initInput();
	}

	private void initInput() {
		input = new ButtonInput() {
			@Override
			public Button getButton() {
				return btnColumnar;
			}
			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}
			@Override
			protected Boolean getDefaultContent() {
				return true;
			}
			@Override
			public String getName() {
				return getInputName() == null ? "read direction":getInputName(); //$NON-NLS-1$
			}
			@Override
			public void writeContent(Boolean content) {
				super.writeContent(content);
				btnRowwise.setSelection(!content);
			}
		};
	}

	/**
	 * sets the direction selection
	 *
	 * @param direction true: columnwise; false: rowwise.
	 */
	public void setDirection(boolean direction) {
		input.writeContent(direction);
		input.synchronizeWithUserSide();
	}

	protected String getInputName() {
		return inputName;
	}

	/**
	 * set the name of this input
	 *
	 * @param inputName
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	/**
	 * @return the input (true: columnwise; false: rowwise.)
	 */
	public AbstractUIInput<Boolean> getInput() {
		return input;
	}

}
