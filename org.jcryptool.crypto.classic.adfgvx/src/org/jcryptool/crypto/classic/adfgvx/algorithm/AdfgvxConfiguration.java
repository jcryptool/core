package org.jcryptool.crypto.classic.adfgvx.algorithm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;

public class AdfgvxConfiguration extends ClassicAlgorithmConfiguration {

	private String substKey;
	private String transpKey;

	public AdfgvxConfiguration(boolean encryptMode, AbstractAlphabet plaintextAlpha,
			boolean filterNonalpha, TransformData preOpTransformData, String substKey, String transpKey) {
		super(encryptMode, "ADFGVX", plaintextAlpha, filterNonalpha, preOpTransformData); //$NON-NLS-1$
		this.setSubstKey(substKey);
		this.setTranspKey(transpKey);
	}

	@Override
	public Composite displayAlgorithmParameters(Composite parent, IEditorPart editor) {
		//TODO: check for GridLayout in parent
		Composite main = super.displayAlgorithmParameters(parent, editor);
		
		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.AdfgvxConfiguration_1);
			
			Control substKeyDisplay = generateSubstKeyDisplay(main, editor);
		}

		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.AdfgvxConfiguration_2);
			
			Control transpKeyDisplay = generateTranspKeyDisplay(main, editor);
		}
		
		return main;
	}

	private Text generateTranspKeyDisplay(Composite main,
			IEditorPart editor) {
		Text key = new Text(main, SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		key.setLayoutData(layoutData);
		key.setEditable(false);
		key.setText(getTranspKey());
		
		return key;
	}

	private Text generateSubstKeyDisplay(Composite main, IEditorPart editor) {
		Text key = new Text(main, SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		key.setLayoutData(layoutData);
		key.setEditable(false);
		key.setText(getSubstKey());
		
		return key;
	}

	public String getTranspKey() {
		return transpKey;
	}

	public void setTranspKey(String transpKey) {
		this.transpKey = transpKey;
	}

	public String getSubstKey() {
		return substKey;
	}

	public void setSubstKey(String substKey) {
		this.substKey = substKey;
	}

}
