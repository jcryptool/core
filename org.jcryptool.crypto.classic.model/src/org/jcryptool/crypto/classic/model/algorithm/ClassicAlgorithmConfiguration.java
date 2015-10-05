package org.jcryptool.crypto.classic.model.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.ClassicCryptoModelPlugin;

/**
 * @author simlei
 *
 */
public abstract class ClassicAlgorithmConfiguration {

	protected String algorithmName;
	protected TransformData preOpTransformData;
	protected boolean filterNonalpha;
	protected AbstractAlphabet plaintextAlpha;
	protected boolean encryptMode;

	private static Map<IEditorPart, ClassicAlgorithmConfiguration> editorsToAlgorithmConfigMap = new HashMap<IEditorPart, ClassicAlgorithmConfiguration>();

	public static void storeAlgorithmConfigForEditor(
			IEditorPart part, ClassicAlgorithmConfiguration config) {
		editorsToAlgorithmConfigMap.put(part, config);
	}

	public static ClassicAlgorithmConfiguration getAlgorithmConfigForEditor(IEditorPart part) {
		return editorsToAlgorithmConfigMap.get(part);
	}

	public static Observer createEditorOpenHandler(
			final AbstractClassicAlgorithm algorithm, final ClassicAlgorithmConfiguration algorithmConfig) {
		return new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(arg != null) {
					IEditorPart part = (IEditorPart) arg;
					ClassicAlgorithmConfiguration.storeAlgorithmConfigForEditor(part, algorithmConfig);
				} else {
					LogUtil.logInfo(ClassicCryptoModelPlugin.PLUGIN_ID, Messages.ClassicAlgorithmConfiguration_1 + algorithm.getAlgorithmName());
				}
			}
		};
	}

	public ClassicAlgorithmConfiguration(boolean encryptMode, String algorithmName, AbstractAlphabet plaintextAlpha, boolean filterNonalpha, TransformData preOpTransformData) {
		this.setEncryptMode(encryptMode);
		this.algorithmName = algorithmName;
		this.plaintextAlpha = plaintextAlpha;
		this.filterNonalpha = filterNonalpha;
		this.preOpTransformData = preOpTransformData;
	}

	/**
	 * Displays the algorithm configuration by creating ui elements and returning them after.<br />
	 * Enclosing layout must be a GridLayout.
	 *
	 * @param parent the parent composite where to display the algorithm configuration
	 * @return the controls created
	 */
	public Composite displayAlgorithmParameters(Composite parent, IEditorPart editor) {
		Composite main = makeStandardDisplays(parent, editor);
		return main;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public TransformData getPreOpTransformData() {
		return preOpTransformData;
	}

	public void setPreOpTransformData(TransformData preOpTransformData) {
		this.preOpTransformData = preOpTransformData;
	}

	public boolean isFilterNonalpha() {
		return filterNonalpha;
	}

	public void setFilterNonalpha(boolean filterNonalpha) {
		this.filterNonalpha = filterNonalpha;
	}

	public AbstractAlphabet getPlaintextAlpha() {
		return plaintextAlpha;
	}

	public void setPlaintextAlpha(AbstractAlphabet plaintextAlpha) {
		this.plaintextAlpha = plaintextAlpha;
	}

	public boolean isEncryptMode() {
		return encryptMode;
	}

	public void setEncryptMode(boolean encryptMode) {
		this.encryptMode = encryptMode;
	}

	protected Composite makeStandardDisplays(Composite parent, IEditorPart editor) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout(2, false);
		mainLayout.verticalSpacing = 10;
		mainLayout.horizontalSpacing = 15;
		main.setLayout(mainLayout);
		GridData mainLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		main.setLayoutData(mainLayoutData);

		{
			Label titleLbl = generateTitleLabel(main, editor);
		}

		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.ClassicAlgorithmConfiguration_2);

			Composite alphaDisplay = generateAlphabetDisplay(main, editor);
		}

		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.ClassicAlgorithmConfiguration_3);

		}



//		Composite filterNonalphaDisplay = generateFilterNonalphaDisplay(main, editor);
//		Composite preOpTransformDisplay = generatePreOpTransformDisplay(main, editor);

		return main;
	}

	private Button generateFilterNonalphaDisplay(Composite main, IEditorPart editor) {
		Button b = new Button(main, SWT.CHECK);
		b.setEnabled(false);
		b.setSelection(filterNonalpha);

		return b;
	}

	private Composite generateAlphabetDisplay(Composite main, IEditorPart editor) {
		Composite alphaDisplay = new Composite(main, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		alphaDisplay.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		alphaDisplay.setLayoutData(layoutData);

		Label alphaNameTitle = new Label(alphaDisplay, SWT.NONE);
		GridData layoutData0 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		alphaNameTitle.setLayoutData(layoutData0);
		alphaNameTitle.setText(Messages.ClassicAlgorithmConfiguration_4);

		Label alphaContentTitle = new Label(alphaDisplay, SWT.NONE);
		GridData layoutData2 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		alphaContentTitle.setLayoutData(layoutData2);
		alphaContentTitle.setText(Messages.ClassicAlgorithmConfiguration_5);

		Text alphabetName = new Text(alphaDisplay, SWT.BORDER);
		GridData layoutData3 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		alphabetName.setLayoutData(layoutData3);
		alphabetName.setEditable(false);
		alphabetName.setText(getPlaintextAlpha().getName());

		Text alphabetContent = new Text(alphaDisplay, SWT.BORDER);
		GridData layoutData4 = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData4.widthHint = 250;
		alphabetContent.setLayoutData(layoutData4);
		alphabetContent.setEditable(false);
		alphabetContent.setText(AbstractAlphabet.alphabetContentAsString(getPlaintextAlpha().getCharacterSet()));

		return alphaDisplay;
	}

	private Label generateTitleLabel(Composite main, IEditorPart editor) {
		Label lbl = new Label(main, SWT.WRAP);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.widthHint = 250;
		layoutData.horizontalSpan = 2;
		lbl.setLayoutData(layoutData);
		String encDecString = isEncryptMode()?Messages.ClassicAlgorithmConfiguration_6:Messages.ClassicAlgorithmConfiguration_7;
		lbl.setText(String.format(Messages.ClassicAlgorithmConfiguration_8, editor.getTitle(), encDecString, getAlgorithmName()));

		return lbl;
	}

}
