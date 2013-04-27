package org.jcryptool.crypto.classic.doppelkasten.algorithm;

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

public class DoppelkastenConfiguration extends ClassicAlgorithmConfiguration {

	private String key1;
	private String key2;

	public DoppelkastenConfiguration(boolean encryptMode, AbstractAlphabet plaintextAlpha,
			boolean filterNonalpha, TransformData preOpTransformData, String key1, String key2) {
		super(encryptMode, Messages.DoppelkastenConfiguration_0, plaintextAlpha, filterNonalpha, preOpTransformData);
		this.setKey1(key1);
		this.setKey2(key2);
	}

	@Override
	public Composite displayAlgorithmParameters(Composite parent, IEditorPart editor) {
		//TODO: check for GridLayout in parent
		Composite main = super.displayAlgorithmParameters(parent, editor);
		
		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.DoppelkastenConfiguration_1);
			
			Control substKeyDisplay = generateKey1Display(main, editor);
		}

		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.DoppelkastenConfiguration_2);
			
			Control transpKeyDisplay = generateKey2Display(main, editor);
		}
		
		return main;
	}

	private Text generateKey1Display(Composite main,
			IEditorPart editor) {
		Text key = new Text(main, SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		key.setLayoutData(layoutData);
		key.setEditable(false);
		key.setText(getKey1());
		
		return key;
	}

	private Text generateKey2Display(Composite main, IEditorPart editor) {
		Text key = new Text(main, SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		key.setLayoutData(layoutData);
		key.setEditable(false);
		key.setText(getKey2());
		
		return key;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

}
