package org.jcryptool.visual.rsa.ui.wizards.wizardpages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.textmodify.wizard.ModifyWizardPage;
import org.jcryptool.visual.rsa.Messages;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class RsaTextModifyPage extends ModifyWizardPage {
	private Label label1;

	public RsaTextModifyPage() {
		super();
	}

	@Override
	protected void addControlsBefore(Composite parent) {

		{
			label1 = new Label(parent, SWT.NONE);
			GridData label1LData = new GridData();

			label1LData.verticalIndent = 10;
			label1LData.verticalAlignment = SWT.CENTER;

			label1.setLayoutData(label1LData);
			label1.setText(Messages.RsaTextModifyPage_textmodifydescription);
		}
	}

//	@Override
//	public ModifySelectionComposite(Composite parent, int style, TransformData defaultData) {
//    super(parent, style);
//
//    GridLayout layout = new GridLayout();
//    this.setLayout(layout);
//
//    this.alphabets = getAlphabetList();
//    this.defaultAlphabet = getStandardAlphabetName();
//
//    try {
//        createUppercaseGroup(this);
//        createUmlautGroup(this);
//        createLeerGroup(this);
//        createAlphabetGroup(this);
//    } catch (Exception e) {
//        LogUtil.logError(TextmodifyPlugin.PLUGIN_ID, e);
//    }
//
//    setTransformData(defaultData);
//
//	}

}
