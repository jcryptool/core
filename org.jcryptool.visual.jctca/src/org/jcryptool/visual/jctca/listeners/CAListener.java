package org.jcryptool.visual.jctca.listeners;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CRLEntry;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RR;

/**
 * Listener that handles events fired in the CA View
 * 
 * @author mmacala
 * 
 */
public class CAListener implements SelectionListener {

    Tree requests;
    Button accept;
    Button reject;
    private Label lbl_value_city;
    private Label lbl_value_country;
    private Label lbl_value_firstname;
    private Label lbl_value_lastname;
    private Label lbl_value_mail;
    private Label lbl_value_street;
    private Label lbl_value_ZIP;

    /**
     * Constructor for the Listener
     * 
     * @param requests - the ree in which the requests are shown
     * @param keys - the list in which the root certificates are listed
     * @param accept - the accept button
     * @param reject - the reject button
     * @param lbl_value_ZIP
     * @param lbl_value_street
     * @param lbl_value_mail
     * @param lbl_value_lastname
     * @param lbl_value_firstname
     * @param lbl_value_country
     * @param lbl_value_city
     */
    public CAListener(Tree requests, Button accept, Button reject, Label lbl_value_city, Label lbl_value_country,
            Label lbl_value_firstname, Label lbl_value_lastname, Label lbl_value_mail, Label lbl_value_street,
            Label lbl_value_ZIP) {
        this.requests = requests;
        this.accept = accept;
        this.reject = reject;
        this.lbl_value_city = lbl_value_city;
        this.lbl_value_country = lbl_value_country;
        this.lbl_value_firstname = lbl_value_firstname;
        this.lbl_value_lastname = lbl_value_lastname;
        this.lbl_value_mail = lbl_value_mail;
        this.lbl_value_street = lbl_value_street;
        this.lbl_value_ZIP = lbl_value_ZIP;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {

    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        Object src = arg0.getSource();

        if (src.equals(requests)) {
            // when something was selected in the list or the tree, check if the buttons should be enabled
            enableButtons(src);
            loadData();
        } else if ((src.equals(accept) || src.equals(reject)) && (requests.getSelection()[0].getData() instanceof CSR)) {
            // csr is selected and either accept or reject has been clicked
            handleCSR(src);
        } else if ((src.equals(accept) || src.equals(reject)) && (requests.getSelection()[0].getData() instanceof RR)) {
            // RR is selected and either accept or reject has been clicked
            handleRR(src);
        }
    }

    /**
     * Sets all the fields on the right half. Gets them from the selected treeitem.
     */
    private void loadData() {
        setLabels("", "", "", "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        if (requests.getSelectionCount() == 1) {
            TreeItem sel = requests.getSelection()[0];
            if (sel.getData() instanceof CSR) {
                CSR csr = (CSR) sel.getData();
                setLabels(csr.getTown(), csr.getCountry(), csr.getFirst(), csr.getLast(), csr.getMail(),
                        csr.getStreet(), csr.getZip());
            } else if (sel.getData() instanceof RR) {
                RR rr = (RR) sel.getData();
                X509Certificate x509 = null;
                try {
                    x509 = (X509Certificate) KeyStoreManager.getInstance().getCertificate(rr.getAlias());
                } catch (UnrecoverableEntryException e) {
                    LogUtil.logError(e);
                } catch (NoSuchAlgorithmException e) {
                    LogUtil.logError(e);
                }
                String[] fields = x509.getSubjectX500Principal().toString().split(", "); //$NON-NLS-1$
                String town = null;
                String country = null;
                String first = null;
                String last = null;
                String mail = null;
                String street = null;
                String zip = null;
                for (String field : fields) {
                    if (field.startsWith("EMAILADDRESS=")) { //$NON-NLS-1$
                        mail = field.split("=").length > 1 ? field.split("=")[1] : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    } else if (field.startsWith("C=")) { //$NON-NLS-1$
                        country = field.split("=").length > 1 ? field.split("=")[1] : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    } else if (field.startsWith("L=")) { //$NON-NLS-1$
                        String[] zip_town = field.split("="); //$NON-NLS-1$
                        if (zip_town.length > 1) {
                            zip_town = zip_town[1].split(" "); //$NON-NLS-1$
                            town = zip_town.length > 1 ? zip_town[1] : zip_town[0];
                            zip = zip_town.length > 1 ? zip_town[0] : ""; //$NON-NLS-1$
                        }
                    } else if (field.startsWith("ST=")) { //$NON-NLS-1$
                        street = field.split("=").length > 1 ? field.split("=")[1] : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    } else if (field.startsWith("CN=")) { //$NON-NLS-1$
                        first = field.split("=").length > 1 ? field.split("=")[1] : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        last = first.split(" ")[first.split(" ").length - 1]; //$NON-NLS-1$ //$NON-NLS-2$
                        first = field.substring(3, field.lastIndexOf(" ")); //$NON-NLS-1$
                    }
                }
                this.setLabels(town, country, first, last, mail, street, zip);
            }
        }
    }

    /**
     * sets the labels on the right half
     * 
     * @param town
     * @param country
     * @param first
     * @param last
     * @param mail
     * @param street
     * @param zip
     */
    private void setLabels(String town, String country, String first, String last, String mail, String street,
            String zip) {
        lbl_value_city.setText(town);
        lbl_value_country.setText(country);
        lbl_value_firstname.setText(first);
        lbl_value_lastname.setText(last);
        lbl_value_mail.setText(mail);
        lbl_value_street.setText(street);
        lbl_value_ZIP.setText(zip);
    }

    /**
     * checks if the buttons need to be enabled. this happens only, if a treeitem has been selected that has a parent
     * (so either a RR or CSR)
     * 
     * @param src - src of the event
     */
    private void enableButtons(Object src) {
        TreeItem[] sel = requests.getSelection();
        if (sel != null && sel.length > 0 && sel[0].getParentItem() != null) {
            accept.setEnabled(true);
            reject.setEnabled(true);
        } else {
            accept.setEnabled(false);
            reject.setEnabled(false);
        }
    }

    /**
     * Handels RR. Gets called when a RR has been selected and one of the buttons is clicked.
     * 
     * @param src - the button that is firing this event
     */
    private void handleRR(Object src) {
        if (src.equals(accept)) {
            TreeItem sel = requests.getSelection()[0];
            RR rr = (RR) sel.getData();
            KeyStoreAlias pubAlias = rr.getAlias();
            KeyStoreManager mng = KeyStoreManager.getInstance();
            Certificate c = null;
            try {
                c = mng.getCertificate(pubAlias);
            } catch (UnrecoverableEntryException e) {
                LogUtil.logError(e);
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(e);
            }
            if (c instanceof X509Certificate) {
                X509Certificate cert = (X509Certificate) c;
                BigInteger sn = cert.getSerialNumber();
                Date revokeTime = new Date(System.currentTimeMillis());
                CRLEntry crle = new CRLEntry(sn, revokeTime);
                CertificateCSRR.getInstance().addCRLEntry(crle);
                // mng.addCertificate(cert, new KeyStoreAlias("JCT-CA Certificate Revocation List - DO NOT DELETE",
                // KeyType.PUBLICKEY, "RSA", 1024, cert.getPublicKey().hashCode()+"",cert.getClass().toString()));
                KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(CertificateCSRR.getInstance().getCAKey(0));
                mng.addKeyPair(
                        kp.getPrivate(),
                        cert,
                        "1234".toCharArray(),
                        new KeyStoreAlias(
                                "JCT-PKI Certificate Revocation List - DO NOT DELETE", KeyType.KEYPAIR_PRIVATE_KEY, "RSA", 1024, cert.getPublicKey().hashCode() + "", cert.getClass().toString()), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        new KeyStoreAlias(
                                "JCT-PKI Certificate Revocation List - DO NOT DELETE", KeyType.KEYPAIR_PUBLIC_KEY, revokeTime.getTime() + "", 1024, kp.getPrivate().hashCode() + "", kp.getPrivate().getClass().toString())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                this.removeEntry(sel);
                Util.showMessageBox(Messages.CAListener_msgbox_title_cert_revoked,
                        Messages.CAListener_msgbox_text_cert_revoked, SWT.ICON_INFORMATION);

            }
        } else if (src.equals(reject)) {
            TreeItem sel = requests.getSelection()[0];
            this.removeEntry(sel);
            Util.showMessageBox(Messages.CAListener_msgbox_title_cert_not_revoked,
                    Messages.CAListener_msgbox_text_cert_not_revoked, SWT.ICON_INFORMATION);
        }
    }

    /**
     * Handels CSRs. gets called when either the accept or reject button has been clicked.
     * @param src - the button, that has been firing the event
     */
    private void handleCSR(Object src) {
        if (src.equals(accept)) {
            TreeItem sel = requests.getSelection()[0];
            CSR csr = (CSR) sel.getData();
            KeyStoreManager mng = KeyStoreManager.getInstance();
            CertificateCSRR csrr = CertificateCSRR.getInstance();
            Date startDate = new Date(System.currentTimeMillis());// time from which certificate is valid
            Date expiryDate = new Date(System.currentTimeMillis() + (10 * 5 * 60 * 60 * 1000));
            BigInteger serialNumber = new BigInteger(System.currentTimeMillis() + "");// serial number for certificate //$NON-NLS-1$

            AsymmetricCipherKeyPair keypair = csrr.getCAKey(0);
            KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(keypair);
            X509Certificate caCert = csrr.getCACert(0);
            X509Certificate cert = Util.certificateForKeyPair(csr, serialNumber, caCert, expiryDate, startDate,
                    kp.getPrivate());
            try {
                PrivateKey priv = mng.getPrivateKey(csr.getPrivAlias(), "1234".toCharArray());
                this.removeEntry(sel);
                mng.addKeyPair(priv, cert, "1234".toCharArray(), csr.getPrivAlias(), csr.getPubAlias());
                Util.showMessageBox(Messages.CAListener_msgbox_title_cert_created,
                        Messages.CAListener_msgbox_text_cert_created, SWT.ICON_INFORMATION);

            } catch (Exception e) {
                LogUtil.logError(e);
            }
        } else if (src.equals(reject)) {
            TreeItem sel = requests.getSelection()[0];
            this.removeEntry(sel);
            Util.showMessageBox(Messages.CAListener_msgbox_title_cert_not_created,
                    Messages.CAListener_msgbox_text_cert_not_created, SWT.ICON_INFORMATION);

        }

    }

    /**
     * removes an entry from the tree and from the list it is contained in
     * 
     * @param sel - the selected tree item that should be removed
     */
    public void removeEntry(TreeItem sel) {
        if (sel.getData() instanceof CSR) {
            CSR csr = (CSR) sel.getData();
            CertificateCSRR.getInstance().removeCSR(csr);
            KeyStoreManager mng = KeyStoreManager.getInstance();
            if (csr.getPubAlias() != null) {
                mng.deleteEntry(csr.getPubAlias());
            }
        } else if (sel.getData() instanceof RR) {
            RR rr = (RR) sel.getData();
            CertificateCSRR.getInstance().removeRR(rr);
        }

        sel.dispose();
        accept.setEnabled(false);
        reject.setEnabled(false);
    }

}
