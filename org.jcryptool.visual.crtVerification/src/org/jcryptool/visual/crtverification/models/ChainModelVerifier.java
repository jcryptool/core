package org.jcryptool.visual.crtverification.models;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.CertPathReviewerException;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.PKIXCertPathReviewer;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

public class ChainModelVerifier {

    public void foo(Certificate certroot, Certificate certca, Certificate certtn) {

        // RFC3280CertPathUtilities mRfc3280CertPathUtilities = new RFC3280CertPathUtilities();
        try {
            CertPathValidator mCertPathValidator = CertPathValidator.getInstance("PKIX", new BouncyCastleProvider());

            TrustAnchor mAnchor = new TrustAnchor((X509Certificate) certroot, null);
            HashSet<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
            trustAnchors.add(mAnchor);

            CertPath path = buildCertPath(certtn, certca, certroot);
            CertPathValidatorResult result = null;
 
ExtendedPKIXParameters mExtendedPKIXParameters = null;

            try {
                mExtendedPKIXParameters = new ExtendedPKIXParameters(trustAnchors);

                mExtendedPKIXParameters.setValidityModel(ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL);
                mExtendedPKIXParameters.setRevocationEnabled(false);
                
                result = (PKIXCertPathValidatorResult) mCertPathValidator.validate(path, mExtendedPKIXParameters);
                System.out.println(result.toString());

                //System.out.println(mExtendedPKIXParameters.getValidityModel());
            } catch (InvalidAlgorithmParameterException | CertPathValidatorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            //****************************************************************
            
            PKIXCertPathReviewer mPkixCertPathReviewer = new PKIXCertPathReviewer(path, mExtendedPKIXParameters);
            System.out.println(mPkixCertPathReviewer.isValidCertPath());
            
        } catch (NoSuchAlgorithmException | CertPathReviewerException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    /**
     * build a CertPath from root, ca and client certificate for validation
     */
    private CertPath buildCertPath(Certificate client, Certificate ca, Certificate root) {
        CertPath path = null;

        try {
            CertificateFactory mCertificateFactory = CertificateFactory.getInstance("X.509");
            path = mCertificateFactory.generateCertPath(Arrays.asList(client, ca, root));
        } catch (CertificateException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }

        return path;
    }

}
