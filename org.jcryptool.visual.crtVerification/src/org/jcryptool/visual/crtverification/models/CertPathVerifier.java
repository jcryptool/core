package org.jcryptool.visual.crtverification.models;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

public class CertPathVerifier {

    public static final int SHELL_MODEL = 0;

    public static final int MODIFIED_SHELL_MODEL = 1;
    public static final int CHAIN_MODEL = 2;

    /**
     * The Date when the client certificate was used so sign
     */
    private Date signatureDate;

    /**
     * The Date when the signature is verified
     */
    private Date verificationDate;

    private Certificate clientCertificate;
    private Certificate caCertificate;
    private Certificate rootCertificate;

    /**
     * Constructs a new Validator for a certificate path
     * 
     * @param rootCert the root certificate, used as TrustAnchor
     * @param caCert the intermediate CA certificate
     * @param clientCert the client certificate
     * @param signatureDate the date when the signature was created, required if the modified shell
     *            model or the chain model is used
     */
    public CertPathVerifier(Certificate rootCert, Certificate caCert, Certificate clientCert, Date verificationDate,
            Date signatureDate) {

        if (rootCert == null || caCert == null || clientCert == null) {
            throw new NullPointerException("Certificates cannot be null!");

        } else if (signatureDate == null) {
            throw new NullPointerException("signatureDate cannot be null");
        } else if (verificationDate == null) {
            throw new NullPointerException("verificationDate cannot be null");
        } else {

            this.signatureDate = signatureDate;
            this.clientCertificate = clientCert;
            this.caCertificate = caCert;
            this.rootCertificate = rootCert;
        }
    }

    /**
     * validates the certificate path using the validity model specified
     * 
     * @param model shell, modified shell or chain model
     * @return true if the path was successfully validated
     * @throws InvalidAlgorithmParameterException exception if a not existing model is selected
     */
    public boolean validate(int model) throws InvalidAlgorithmParameterException {

        boolean valid = false;

        if (model != 0 && model != 1 && model != 2) {
            throw new InvalidAlgorithmParameterException();
        }

        try {
            CertPathValidator mCertPathValidator = CertPathValidator.getInstance("PKIX", new BouncyCastleProvider());
            CertPath path = buildCertPath(clientCertificate, caCertificate, rootCertificate);
            ExtendedPKIXParameters mExtendedPKIXParameters = null;

            TrustAnchor anchor = new TrustAnchor((X509Certificate) rootCertificate, null);
            HashSet<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
            trustAnchors.add(anchor);

            mExtendedPKIXParameters = new ExtendedPKIXParameters(trustAnchors);
            mExtendedPKIXParameters.setRevocationEnabled(false);

            // select validity model and set parameters
            if (model != 2) {
                mExtendedPKIXParameters.setValidityModel(ExtendedPKIXParameters.PKIX_VALIDITY_MODEL);
                // modified shell model, verificationdate = sig date
                if (model == 1) {
                    mExtendedPKIXParameters.setDate(signatureDate);
                } else {

                    mExtendedPKIXParameters.setDate(verificationDate);
                }
            } else {
                mExtendedPKIXParameters.setValidityModel(ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL);
                mExtendedPKIXParameters.setDate(signatureDate);
            }

            mCertPathValidator.validate(path, mExtendedPKIXParameters);

            // verify a second time at signing time for shell model
            if (model == 0) {
                mExtendedPKIXParameters.setDate(signatureDate);
                mCertPathValidator.validate(path, mExtendedPKIXParameters);
            }

            // if no exception is thrown, the path is valid
            valid = true;

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertPathValidatorException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
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
     * checks if the verification and/or signature dates are within the valididity periods of the
     * certificate chain, based on the selected validity model. Expects validity periods for all
     * three certs in this order:
     * 
     * @param model the validity model to use
     * @param clientNotBefore
     * @param clientNotAfter
     * @param caNotBefore
     * @param caNotAfter
     * @param rootNotBefore
     * @param rootNotAfter
     * @param sigDate
     * @param verDate
     * @return true if the certificate chain is valid (based only on time)
     * @throws InvalidAlgorithmParameterException if a non existing model is selected
     */
    public boolean verifyChangedDate(int model, Date clientNotBefore, Date clientNotAfter, Date caNotBefore,
            Date caNotAfter, Date rootNotBefore, Date rootNotAfter, Date sigDate, Date verDate)
            throws InvalidAlgorithmParameterException {
        boolean valid = true;

        if (model != 0 && model != 1 && model != 2) {
            throw new InvalidAlgorithmParameterException();
        }

        // check verification an signature date or only signature date based on used model
        if (model == 1) {
            valid = compareDates(sigDate, clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);
        } else if (model == 0) {
            valid = compareDates(verDate, clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);
            valid = compareDates(sigDate, clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);
        } else {
            // chain model
            if (clientNotBefore.after(sigDate) || clientNotAfter.before(sigDate)) {
                valid = false;
            } else if (caNotBefore.after(clientNotBefore) || caNotAfter.before(clientNotBefore)) {
                valid = false;
            } else if (rootNotBefore.after(caNotBefore) || rootNotAfter.before(caNotBefore)) {
                valid = false;
            }
        }

        return valid;
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
