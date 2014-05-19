package org.jcryptool.visual.crtverification.models;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

/**
 * Class to verify a CertPath using the Shell Model or the Modifies Shell Model
 * 
 * @author Clemens Elst
 * 
 */
public class ShellModelVerifier {

    private TrustAnchor rootAnchor;
    private TrustAnchor caAnchor;

    private Date signatureDate;

    private CertPath path;

    /**
     * If true, the Modified Shell model is used to verify the CertifcatePath
     */
    private boolean modifiedShellModel = false;

    public ShellModelVerifier(CertPath path, boolean modifiedShellModel, Date signaturDate) {
        if (path == null) {
            throw new NullPointerException("CertPath cannot be null!");
        } else if (signaturDate == null) {
            throw new NullPointerException("signature Date cannot be null");
        } else {
            this.modifiedShellModel = modifiedShellModel;
            this.signatureDate = signaturDate;
            this.path = path;

        }
    }

    public void verify() {
        try {
            CertPathValidator mCertPathValidator = CertPathValidator.getInstance("PKIX", new BouncyCastleProvider());
            rootAnchor = new TrustAnchor((X509Certificate) path.getCertificates().get(0), null);
            caAnchor = new TrustAnchor((X509Certificate) path.getCertificates().get(1), null);

            //one hash set containing root and one ca trust anchor
            HashSet<TrustAnchor> trustAnchorsCaRoot = new HashSet<TrustAnchor>();
            trustAnchorsCaRoot.add(rootAnchor);
            HashSet<TrustAnchor> trustAnchorsTnCa = new HashSet<TrustAnchor>();
            trustAnchorsTnCa.add(caAnchor);

            //validate root first, then ca
            PKIXParameters pkixParameters = new PKIXParameters(trustAnchorsCaRoot);
            pkixParameters.setRevocationEnabled(false);
            
            //modifiy pkix to modified shell model
            if(modifiedShellModel == true){
                pkixParameters.setDate(signatureDate);
            }
            
            try {
                PKIXCertPathValidatorResult mCertPathValidatorResult = (PKIXCertPathValidatorResult) mCertPathValidator.validate(path, pkixParameters);
            } catch (CertPathValidatorException e) {
               System.out.println("error root anchor");
            }
            
            pkixParameters.setTrustAnchors(trustAnchorsTnCa);
            try {
                PKIXCertPathValidatorResult mCertPathValidatorResult = (PKIXCertPathValidatorResult) mCertPathValidator.validate(path, pkixParameters);
            } catch (CertPathValidatorException e) {
               System.out.println("error ca anchor");
            }
            

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
    }

}
