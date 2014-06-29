package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ChooseCertPage extends WizardPage {
   	private ChooseCertComposite compositeFile;
   	private CrtVerViewController controller;
    int certType; // [1] UserCert; [2] Cert; [3] RootCert
    
    public int getCertType() {
		return certType;
	}


	public void setCertType(int certType) {
		this.certType = certType;
	}
    
    public ChooseCertPage(String pageName, int type, CrtVerViewController controller) {
        super(pageName);
        this.controller = controller;
        certType = type;
        setTitle(pageName);
        //TODO Message
        setDescription(Messages.ChooseCertPage_description);
    }
    
    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new ChooseCertComposite(parent, SWT.NONE, this, controller);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public ChooseCertComposite getCompositeFile() {
        return compositeFile;
    }

}
