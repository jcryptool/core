package org.jcryptool.visual.crtverification.views;

import java.security.cert.X509Certificate;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class ChooseCertPage extends WizardPage {
    private ChooseCertComposite compositeFile;
    private X509Certificate certificate;

    public ChooseCertPage(String pageName) {
        super(pageName);

        setTitle(pageName);
        setDescription("Bitte wählen Sie ein zu ladendes Zertifikat aus dem Java Keystore aus.");
    }
    
    public void setCertificate(X509Certificate cert){
    	this.certificate = cert;
    }
    
    public X509Certificate getCertificate(){
    	if(certificate !=null){
    		return certificate;
    	}
    	return null;
    }
    
    // TestComposite wird erzeugt
    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new ChooseCertComposite(parent, NONE, this);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public ChooseCertComposite getCompositeFile() {
        return compositeFile;
    }

}
