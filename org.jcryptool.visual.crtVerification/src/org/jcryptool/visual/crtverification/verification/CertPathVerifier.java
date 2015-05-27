// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2015 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.verification;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters.Builder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

public class CertPathVerifier {

    /**
     * identifier for the shell model
     */
    public static final int SHELL_MODEL = 0;

    /**
     * identifier for the modified shell model
     */
    public static final int MODIFIED_SHELL_MODEL = 1;

    /**
     * identifier for the chain model
     */
    public static final int CHAIN_MODEL = 2;

    /**
     * The Date when the client certificate was used so sign
     */
    private Date signatureDate;

    /**
     * The Date when the signature is verified
     */
    private Date verificationDate;

    /**
     * The client certificate used to sign
     */
    private Certificate clientCertificate;

    /**
     * The intermediate CAs certificate
     */
    private Certificate caCertificate;

    /**
     * The root CAs certificate, used as trust anchor
     */
    private Certificate rootCertificate;

    /**
     * A list containing all error messages of the path validation
     */
    private ArrayList<String> errors;

    /**
     * Constructs a new validator for a certification Path
     * 
     * @param verificationDate the date when the signature is verified
     * @param signatureDate the date when the signature was created
     */
    public CertPathVerifier(Date verificationDate, Date signatureDate) {
        if (verificationDate == null || signatureDate == null) {
            throw new NullPointerException("Dates cannot be null");
        } else {
            this.verificationDate = verificationDate;
            this.signatureDate = signatureDate;
        }
    }

    /**
     * Constructs a new validator for a certificate path
     * 
     * 
     * @param rootCert the root certificate, used as TrustAnchor
     * @param caCert the intermediate CA certificate
     * @param clientCert the client certificate
     * @param verificationDate the date when the signature is verified
     * @param signatureDate the date when the signature was created, required if the modified shell
     *            model or the chain model is used
     */
    public CertPathVerifier(Certificate rootCert, Certificate caCert, Certificate clientCert, Date verificationDate,
            Date signatureDate) {
        this(verificationDate, signatureDate);

        if (rootCert == null || caCert == null || clientCert == null) {
            throw new NullPointerException("Certificates cannot be null!");
        } else {
            this.clientCertificate = clientCert;
            this.caCertificate = caCert;
            this.rootCertificate = rootCert;
        }
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
     * @param signatureDate the date when the signature was created
     */
    public void setSignatureDate(Date signatureDate) {
        if (signatureDate != null) {
            this.signatureDate = signatureDate;
        } else {
            throw new NullPointerException("signatureDate cannot be null");
        }
    }

    /**
     * @param verificationDate the date when the signature is verified
     */
    public void setVerificationDate(Date verificationDate) {
        if (signatureDate != null) {
            this.verificationDate = verificationDate;
        } else {
            throw new NullPointerException("verificationeDate cannot be null");
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
        } else if (clientCertificate == null || caCertificate == null || rootCertificate == null) {
            throw new NullPointerException("certificates cannot be null");
        }

        try {
            CertPathValidator certPathValidator = CertPathValidator.getInstance("PKIX", new BouncyCastleProvider());
            CertPath certPath = buildCertPath(clientCertificate, caCertificate, rootCertificate);

            // set rootcert as trust anchor
            TrustAnchor trustAnchor = new TrustAnchor((X509Certificate) rootCertificate, null);
            HashSet<TrustAnchor> trustAnchors = new HashSet<>();
            trustAnchors.add(trustAnchor);
            PKIXParameters pkixParameters = new PKIXParameters(trustAnchors);
            Builder builder = new PKIXExtendedParameters.Builder(pkixParameters);
            builder.setRevocationEnabled(false);

            // select validity model and set parameters
            if (model != 2) {
                builder.setValidityModel(PKIXExtendedParameters.PKIX_VALIDITY_MODEL);
                // modified shell model, verificationdate = sig date
                if (model == 1) {
                    pkixParameters.setDate(signatureDate);
                } else {
                    pkixParameters.setDate(verificationDate);
                }
            } else {
                builder.setValidityModel(PKIXExtendedParameters.CHAIN_VALIDITY_MODEL);
                pkixParameters.setDate(signatureDate);
            }

            certPathValidator.validate(certPath, builder.build());

            // if shell model, verify a second time at signing time
            if (model == 0) {
                pkixParameters.setDate(signatureDate);
                certPathValidator.validate(certPath, builder.build());
            }

            // if no exception is thrown, the path is valid
            valid = true;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        } catch (CertPathValidatorException e) {
            LogUtil.logInfo(e.getMessage());
        }

        return valid;
    }

    /**
     * checks if the verification and/or signature dates are within the validity periods of the
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
     * @param signatureDate
     * @param verificationDate
     * @return an ArrayList containing all error messages as Strings, if no errors occurred, the
     *         list is empty
     * @throws InvalidAlgorithmParameterException if a non existing model is selected
     */
    public ArrayList<String> verifyChangedDate(int model, Date clientNotBefore, Date clientNotAfter, Date caNotBefore,
            Date caNotAfter, Date rootNotBefore, Date rootNotAfter, Date signatureDate, Date verificationDate)
            throws InvalidAlgorithmParameterException {
        errors = new ArrayList<>();

        if (model != 0 && model != 1 && model != 2) {
            throw new InvalidAlgorithmParameterException();
        }

        if (model == 0 || model == 1) {
            // check if ver and sig date are within the validity period of the client cert
            verifyShellModelPeriodes(clientNotBefore, clientNotAfter, caNotBefore, caNotAfter, rootNotBefore,
                    rootNotAfter);

            if (!isDateWithinPeriod(signatureDate, clientNotBefore, clientNotAfter)) {
                errors.add(Messages.CrtVericiation_sigDateNotWithinClient);
            }

            // check sig and ver date if shell model
            if (model == 0) {
                // check if sig date is before ver date
                if (signatureDate.after(verificationDate)) {
                    errors.add(Messages.CrtVerifciation_sigDateAfterVerDate);

                }

                if (!isDateWithinPeriod(verificationDate, clientNotBefore, clientNotAfter)) {
                    errors.add(Messages.CrtVericiation_verDateNotWithinClient);
                }

            }

        } else {
            // chain model
            if (clientNotBefore.after(signatureDate)) {
                errors.add(Messages.CrtVericiation_clientNotBeforeAfterSigDate);

            } else if (clientNotAfter.before(signatureDate)) {
                errors.add(Messages.CrtVericiation_clientNotAfterBeforeSigDate);

            } else if (caNotBefore.after(clientNotBefore)) {
                errors.add(Messages.CrtVericiation_caNotBeforeAfterClientNotBefore);

            } else if (caNotAfter.before(clientNotBefore)) {
                errors.add(Messages.CrtVericiation_caNotAfterBeforeClientNotAfter);

            } else if (rootNotBefore.after(caNotBefore)) {
                errors.add(Messages.CrtVericiation_rootNotBeforeAfterCaNotBefore);

            } else if (rootNotAfter.before(caNotBefore)) {
                errors.add(Messages.CrtVericiation_rootNotAfterBeforeCANotAfter);
            }
        }

        return errors;
    }

    /**
     * build a CertPath from root, ca and client certificate for validation
     * 
     * @param client's the client certificate
     * @param ca the intermediate ca's certificate
     * @param root the root ca's certificate
     * @return the constructed CertPath
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
     * check if client is within the ca, and ca within the root validity period, occurred errors are
     * added to the errors list
     * <p>
     * Requires the dates in the following order:
     * 
     * @param clientNotBefore
     * @param clientNotAfter
     * @param caNotBefore
     * @param caNotAfter
     * @param rootNotBefore
     * @param rootNotAfter
     */
    private void verifyShellModelPeriodes(Date clientNotBefore, Date clientNotAfter, Date caNotBefore, Date caNotAfter,
            Date rootNotBefore, Date rootNotAfter) {

        if (clientNotBefore.before(caNotBefore)) {
            errors.add(Messages.CrtVericiation_caNotBeforeAfterClientNotBefore);
        }

        if (clientNotAfter.after(caNotAfter)) {
            errors.add(Messages.CrtVericiation_caNotAfterBeforeClientNotAfter);
        }

        if (caNotBefore.before(rootNotBefore)) {
            errors.add(Messages.CrtVericiation_rootNotBeforeAfterCaNotBefore);
        }

        if (caNotAfter.after(rootNotAfter)) {
            errors.add(Messages.CrtVericiation_rootNotAfterBeforeCANotAfter);
        }
    }

    /**
     * checks if a date is within two other dates
     * 
     * @param compareDate the date to compare
     * @param validityBegin the begin of the valid period
     * @param validityEnd the end of the valid period
     * @return true if the date is within the valid period
     */
    private boolean isDateWithinPeriod(Date compareDate, Date validityBegin, Date validityEnd) {
        if (validityBegin.before(compareDate) && validityEnd.after(compareDate)) {
            return true;
        }

        return false;
    }

}
