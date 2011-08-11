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
package org.jcryptool.analysis.transpositionanalysis.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.textmodify.wizard.ModifyWizardPage;


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
public class TranspTextModifyPage extends ModifyWizardPage {
	private Label label1;
	public TranspTextModifyPage() {
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
			label1.setText(Messages.TranspTextModifyPage_textmodifydescription);
		}
	}
}
