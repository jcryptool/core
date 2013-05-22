package org.jcryptool.crypto.analysis.substitution.ui.modules.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

public class TextTransformationDisplayer extends Composite {

	private Composite layoutRoot;
	private Label label;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TextTransformationDisplayer(Composite parent, Composite layoutRoot, int style) {
		super(parent, style);
		this.layoutRoot = layoutRoot;

		setLayout(new GridLayout(1, false));
		
		label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

	}
	
	public void setTransformData(TransformData data) {
		setTransformData(data, true);
	}
	
	public void setTransformData(TransformData data, boolean layout) {
		label.setText(makeStringForDisplayingData(data));
		if(layout) {
			layoutRoot.layout(new Control[]{label});
		}
	}
	
	public boolean isSomethingDisplayed() {
		return label.getText().length()>0;
	}
	
	private String makeStringForDisplayingData(TransformData data) {
		return (data==null||data.isUnmodified())?"":data.toReadableString(); //$NON-NLS-1$
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
