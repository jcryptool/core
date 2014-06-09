package org.jcryptool.visual.crtverification.models;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

public class ShellModelVerifier {

    /**
     * If true, the Modified Shell model is used to verify the CertifcatePath
     */
    private boolean modifiedShellModel = false;

    /**
     * The Date when the client certificate was used so sign
     */
    private Date signatureDate;

    private Certificate clientCertificate;
    private Certificate caCertificate;
    private Certificate rootCertificate;

    /**
     * Constructs a new Verifier for the shell model or the modified shell model
     * 
     * @param rootCert the root certificate, used as trust anchor of the certificate path
     * @param caCert the ca certificate
     * @param clientCert the client certificate
     * @param modifiedModelEnabled true if the modified shell model should be used
     * @param signatureDate the date when the signature was created, required if the modified shell
     *            model is used
     */
    public ShellModelVerifier(Certificate rootCert, Certificate caCert, Certificate clientCert,
            boolean modifiedModelEnabled, Date signatureDate) {
        if (rootCert == null || caCert == null || clientCert == null) {
            throw new NullPointerException("Certificates cannot be null!");
        } else if (signatureDate == null) {
            if (modifiedShellModel) {
                throw new NullPointerException("signatureDate cannot be null");
            } else {
                signatureDate = new Date();
            }
        } else {
            this.modifiedShellModel = modifiedModelEnabled;
            this.signatureDate = signatureDate;
            this.clientCertificate = clientCert;
            this.caCertificate = caCert;
            this.rootCertificate = rootCert;
        }
    }

    /**
     * Verifies the CertPath
     * 
     * @return true if the path is valid
     */
    public boolean verify() {
        boolean valid = false;
        try {
            // using PKIX from BC provider
            CertPathValidator mCertPathValidator = CertPathValidator.getInstance("PKIX", new BouncyCastleProvider());

            // set the root certificate as TrustAnchor
            TrustAnchor rootAnchor = new TrustAnchor((X509Certificate) rootCertificate, null);
            HashSet<TrustAnchor> trustAnchorsCaRoot = new HashSet<TrustAnchor>();
            trustAnchorsCaRoot.add(rootAnchor);

            PKIXParameters pkixParameters = new PKIXParameters(trustAnchorsCaRoot);
            pkixParameters.setRevocationEnabled(false);
            PKIXCertPathValidatorResult result = null;

            // if modified model is selected, set the signatureDate as verification date
            if (modifiedShellModel) {
                pkixParameters.setDate(signatureDate);
            }

            // build CertPath
            CertPath validationPath = buildCertPath(clientCertificate, caCertificate, rootCertificate);

            // validate cert path
            try {
                result = (PKIXCertPathValidatorResult) mCertPathValidator.validate(validationPath, pkixParameters);
                valid = true;
                System.out.println("path validated");
            } catch (CertPathValidatorException e) {
                System.out.println("Error validating path" + result);
                LogUtil.logError(Activator.PLUGIN_ID, e);
            }

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }

        return valid;

    }

    /**
     * checks if the verificationDate (or the signatureDate when using the modified model) is within
     * the valididity periods of the certificate chain Expects valididy periods for all three certs
     * in this order:
     * 
     * @param clientNotBefore
     * @param clientNotAfter
     * @param caNotBefore
     * @param caNotAfter
     * @param rootNotBefore
     * @param rootNotAfter
     * @param sigDate
     * @param verDate
     * @return true if the certificate chain is valid (based only on time)
     */
    public boolean verifyChangedDate(Date clientNotBefore, Date clientNotAfter, Date caNotBefore, Date caNotAfter,
            Date rootNotBefore, Date rootNotAfter, Date sigDate, Date verDate) {
        boolean valid = true;

        // check verification an signature date or only signature date based on used model
        if (modifiedShellModel) {
            valid = compareDates(sigDate, clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);
        } else {
            valid = compareDates(verDate, clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);
            valid = compareDates(sigDate, clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);
        }

        return valid;
    }

    /**
     * sets the certificates for the validator
     * 
     * @param rootCert root certificate
     * @param caCert CA certificate
     * @param clientCert client certificate
     */
    public void setCertificiates(Certificate rootCert, Certificate caCert, Certificate clientCert) {
        if (rootCert == null || caCert == null || clientCert == null) {
            throw new NullPointerException("Certificates cannot be null!");
        } else {
            this.clientCertificate = clientCert;
            this.caCertificate = caCert;
            this.rootCertificate = rootCert;
        }
    }

    /**
     * sets the model which should be used
     * 
     * @param modifiedModelEnabled true if the modified shell model should be used, false if shell
     *            model
     */
    public void setModifiedShellModell(boolean modifiedModelEnabled) {
        this.modifiedShellModel = modifiedModelEnabled;
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

    /**
     * @return true if all dates are within validity periodes
     */
    private boolean compareDates(Date compareDate, Date clientNotBefore, Date clientNotAfter, Date caNotBefore,
            Date caNotAfter, Date rootNotBefore, Date rootNotAfter) {
        boolean valid = true;

        if (clientNotBefore.after(compareDate) || clientNotAfter.before(compareDate)) {
            valid = false;
        } else if (caNotBefore.after(compareDate) || caNotAfter.before(compareDate)) {
            valid = false;
        } else if (rootNotBefore.after(compareDate) || rootNotAfter.before(compareDate)) {
            valid = false;
        }
        return valid;
    }
}
