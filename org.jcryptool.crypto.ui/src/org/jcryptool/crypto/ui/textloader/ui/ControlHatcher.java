//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textloader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public abstract class ControlHatcher {
	
	public static class LabelHatcher extends ControlHatcher {
		private String text;
		private int style;
		private int widthHint;

		public LabelHatcher(String text, int style, int widthHint) {
			this.text = text;
			this.style = style;
			this.widthHint = widthHint;
			
		}
		
		public LabelHatcher(String text) {
			this(text, SWT.WRAP, 400);
		}

		@Override
		public Control hatch(Composite parent) {
			Label lbl = new Label(parent, style);
			lbl.setText(text);
			if(style == SWT.WRAP) {
				GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
				layoutData.widthHint = this.widthHint;
				lbl.setLayoutData(layoutData);
			}
			
			return lbl;
		}
		
		
	}
	
	public abstract Control hatch(Composite parent);
	
}
