package org.jcryptool.crypto.classic.model.algorithm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class ClassicAlgorithmConfigurationWithKey extends
		ClassicAlgorithmConfiguration {

	private String key;

	public ClassicAlgorithmConfigurationWithKey(boolean encryptMode,
			String algorithmName, AbstractAlphabet plaintextAlpha,
			boolean filterNonalpha, TransformData preOpTransformData, String key) {
		super(encryptMode, algorithmName, plaintextAlpha, filterNonalpha,
				preOpTransformData);
		this.setKey(key);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Composite displayAlgorithmParameters(Composite parent,
			IEditorPart editor) {
		Composite main = super.displayAlgorithmParameters(parent, editor);

		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false, 1, 1));
			descr.setText("Key:");

		}

		return main;
	}
	
	private Text generateKeyDisplay(Composite main,
			IEditorPart editor) {
		return generateKeyDisplay(main, editor, getKey());
	}
	
	public static Text generateKeyDisplay(Composite main, IEditorPart editor, String key) {
		Text keyText = new Text(main, SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		keyText.setLayoutData(layoutData);
		keyText.setEditable(false);
		keyText.setText(key);
		
		return keyText;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
