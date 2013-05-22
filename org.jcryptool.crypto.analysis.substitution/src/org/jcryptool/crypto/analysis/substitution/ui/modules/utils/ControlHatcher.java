package org.jcryptool.crypto.analysis.substitution.ui.modules.utils;

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
