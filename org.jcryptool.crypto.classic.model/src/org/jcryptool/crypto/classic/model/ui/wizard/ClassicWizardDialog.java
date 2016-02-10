package org.jcryptool.crypto.classic.model.ui.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class ClassicWizardDialog extends WizardDialog {

	public ClassicWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Point getInitialSize() {
		// TODO Auto-generated method stub
		Point size = super.getInitialSize();
		
		// here, the width multiplicator from default (minimum) size for all classic wizards that implement this dialog can be configured
		return new Point((int) Math.round(size.x*1.1D), size.y);
	}

}
