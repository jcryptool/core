package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;

class PrivateKeyTextComposite extends Composite {

	StyledText privateKeySign;

	PrivateKeyTextComposite(Composite parent, int style, ISimpleMerkle merkle) {
		super(parent, style);
		this.setLayout(new GridLayout(1, true));

		privateKeySign = new StyledText(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		privateKeySign.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, MerkleConst.H_SPAN_MAIN, 1));
	}

	public void setPrivateKeyText(String privateKey) {

		privateKeySign.setText(privateKey);
	}

	public void setColor(int start, int length, Color color) {
		privateKeySign.setStyleRange(new StyleRange(start, length, color, null));
	}

}
