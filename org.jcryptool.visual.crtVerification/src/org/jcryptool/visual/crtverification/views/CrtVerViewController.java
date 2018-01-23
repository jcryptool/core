// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.views;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;
import org.jcryptool.visual.crtverification.verification.CertPathVerifier;
import org.jcryptool.visual.crtverification.verification.KeystoreConnector;

public class CrtVerViewController {
    private String dateformat1 = "/MMM/yy";
    private String dateformat2 = "MMM/yy";
    private String dateformat3 = "yyyy";
    private KeystoreConnector ksc = new KeystoreConnector();
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendar1 = Calendar.getInstance();
    private SimpleDateFormat dt1 = new SimpleDateFormat();
    private Date now = calendar.getTime();

    private Date thruRootCa;
    private Date fromRootCa;
    private Date thruCa;
    private Date fromCa;
    private Date thruCert;
    private Date fromCert;
    private Date signatureDate;
    private Date verificationDate;

    private X509Certificate rootCA = null;
    private X509Certificate ca = null;
    private X509Certificate tn = null;
    private boolean flag = false;
    private CrtVerViewComposite composite;

    public CrtVerViewController(CrtVerViewComposite composite) {
        super();
        this.composite = composite;
    }

    public KeystoreConnector getKsc() {
        return ksc;
    }

    public void setKsc(KeystoreConnector ksc) {
        this.ksc = ksc;
    }

    public String getDateformat1() {
        return dateformat1;
    }

    public void setDateformat1(String dateformat1) {
        this.dateformat1 = dateformat1;
    }

    public String getDateformat2() {
        return dateformat2;
    }

    public void setDateformat2(String dateformat2) {
        this.dateformat2 = dateformat2;
    }

    public String getDateformat3() {
        return dateformat3;
    }

    public void setDateformat3(String dateformat3) {
        this.dateformat3 = dateformat3;
    }

    public X509Certificate getRootCA() {
        return rootCA;
    }

    public void setRootCA(X509Certificate rootCA) {
        this.rootCA = rootCA;
    }

    public X509Certificate getCA() {
        return ca;
    }

    public void setCA(X509Certificate ca) {
        this.ca = ca;
    }

    public X509Certificate getTN() {
        return tn;
    }

    public void setTN(X509Certificate tn) {
        this.tn = tn;
    }

    public String getThruRootCa() {
        return parseDateToString(thruRootCa);
    }

    public String getFromRootCa() {
        return parseDateToString(fromRootCa);
    }

    public String getThruCA() {
        return parseDateToString(thruCa);
    }

    public String getFromCA() {
        return parseDateToString(fromCa);
    }

    public String getThruClient() {
        return parseDateToString(thruCert);
    }

    public String getFromClient() {
        return parseDateToString(fromCert);
    }

    public String getVerDate() {
        return parseDateToString(verificationDate);
    }

    public String getSigDate() {
        return parseDateToString(signatureDate);
    }

    /**
     * Method to get the actual date in "MM/yy" Format
     * 
     * @return Returns the actual date as MM/yy format
     */
    public String now(String format) {
        Calendar cal = Calendar.getInstance();
        Date time_now = cal.getTime();
        cal.setTime(time_now);
        dt1.applyPattern(format);
        return dt1.format(time_now);
    }

    /**
     * Method to get month from a calendar dependent on the selection of a scale and it's default
     * selection.
     * 
     * @param selection The actual selection getting with .getSelection()
     * @param default_selection The default Selection of Scale Receiver, it will be subtracted from
     *            the value
     * @param format The date format to represent
     * @return The modified date represented as a String
     */
    public String scaleUpdate(int selection, int default_selection, String format) {
        dt1.applyPattern(format);
        calendar.setTime(now);
        if (default_selection == 360) {
            if (((selection - default_selection) % 2) == 0) {
                calendar.add(Calendar.MONTH, (selection - default_selection) / 2);
            } else {
                calendar.add(Calendar.MONTH, ((selection + 1) - default_selection) / 2);
            }
        } else {
            calendar.add(Calendar.MONTH, selection - default_selection);
        }
        return String.valueOf(dt1.format(calendar.getTime()));
    }

    /**
     * Method to check a Text Field if value is between 1 and 31.
     * 
     * @param input The text field
     * @return True if Textfield has a valid value
     */
    public void inputcheck(Text input) {
        int value = Integer.parseInt(input.getText());
        if (!(value > 0 && value <= 31)) {
            input.setText("1");
            composite.txtLogWindow.append(input.getToolTipText() + " " + Messages.crtVerification_status_invalidDate
                    + "  \r\n");
        } else {
            setLogText(input.getToolTipText() + " " + Messages.CrtVerViewComposite_dateSet + " \"" + input.getText()
                    + "\"");

        }

    }

    public int dateOffset(Date date) {
        int offset = 0;
        int month = 0, year = 0;
        calendar.setTime(now);
        calendar1.setTime(date);
        month = calendar1.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
        year = calendar1.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        offset = month + 12 * year;
        calendar.add(Calendar.MONTH, offset);
        return offset;
    }

    public void setScales(int CertType) {
        Calendar cal = Calendar.getInstance();
        switch (CertType) {
        case 1:
            // Scale From User Certificate
            composite.scaleCertBegin.setSelection(180 + dateOffset(getTN().getNotBefore()));
            composite.fromCert.setText(scaleUpdate(composite.scaleCertBegin.getSelection(), 180, dateformat1));
            composite.scaleCertBegin.setToolTipText(scaleUpdate(composite.scaleCertBegin.getSelection(), 180,
                    dateformat2));

            // Set Textfield for the Day | From TN
            cal.setTime(getTN().getNotBefore());
            composite.textCertFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

            // Scale Thru User Certificate
            composite.scaleCertEnd.setSelection(180 + dateOffset(getTN().getNotAfter()));
            composite.thruCert.setText(scaleUpdate(composite.scaleCertEnd.getSelection(), 180, dateformat1));
            composite.scaleCertEnd.setToolTipText(scaleUpdate(composite.scaleCertEnd.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | Thru TN
            cal.setTime(getTN().getNotAfter());
            composite.textCertThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            break;
        case 2:
            // Scale From CA
            composite.scaleCaBegin.setSelection(180 + dateOffset(getCA().getNotBefore()));
            composite.fromCa.setText(scaleUpdate(composite.scaleCaBegin.getSelection(), 180, dateformat1));
            composite.scaleCaBegin.setToolTipText(scaleUpdate(composite.scaleCaBegin.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | From CA
            cal.setTime(getCA().getNotBefore());
            composite.textCaFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

            // Scale Thru CA
            composite.scaleCaEnd.setSelection(180 + dateOffset(getCA().getNotAfter()));
            composite.thruCa.setText(scaleUpdate(composite.scaleCaEnd.getSelection(), 180, dateformat1));
            composite.scaleCaEnd.setToolTipText(scaleUpdate(composite.scaleCaEnd.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | Thru CA
            cal.setTime(getCA().getNotAfter());
            composite.textCaThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            break;
        case 3:
            // Scale From Root CA
            composite.scaleRootCaBegin.setSelection(180 + dateOffset(getRootCA().getNotBefore()));
            composite.fromRootCa.setText(scaleUpdate(composite.scaleRootCaBegin.getSelection(), 180, dateformat1));
            composite.scaleRootCaBegin.setToolTipText(scaleUpdate(composite.scaleRootCaBegin.getSelection(), 180,
                    dateformat2));

            // Set Textfield for the Day | From Root CA
            cal.setTime(getRootCA().getNotBefore());
            composite.textRootCaFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

            // Scale Thru Root CA
            composite.scaleRootCaEnd.setSelection(180 + dateOffset(getRootCA().getNotAfter()));
            composite.thruRootCa.setText(scaleUpdate(composite.scaleRootCaEnd.getSelection(), 180, dateformat1));
            composite.scaleRootCaEnd.setToolTipText(scaleUpdate(composite.scaleRootCaEnd.getSelection(), 180,
                    dateformat2));

            // Set Textfield for the Day | Thru Root CA
            cal.setTime(getRootCA().getNotAfter());
            composite.textRootCaThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            break;
        }
    }

    /**
     * Validates the three Models with the help of Class ShellModelVerifier and CertPathVerifier.
     * 
     * @param mode 1=Shell Model, 2=Modified Shell Model, 3=Chain Model
     */
    public void validate(int mode) {
        // Mode 0: Shell Model
        // Mode 1: Modified Shell Model
        // Mode 2: Chain Model
        parseDatesFromComposite();

        try {
            CertPathVerifier cpv = null;
            boolean valid = false;

            if (flag) {
                cpv = new CertPathVerifier(getRootCA(), getCA(), getTN(), verificationDate, signatureDate);

                if (cpv.validate(mode)) {
                    valid = true;
                    switch (mode) {
                    case 0:
                        setLogText(Messages.crtVerification_status_chainSucShell);
                        break;
                    case 1:
                        setLogText(Messages.crtVerification_status_chainSucModShell);
                        break;
                    case 2:
                        setLogText(Messages.crtVerification_status_chainSucChain);
                        break;
                    }
                } else {
                    switch (mode) {
                    case 0:
                        setLogText(Messages.crtVerification_status_chainFailShell);
                        break;
                    case 1:
                        setLogText(Messages.crtVerification_status_chainFailModShell);
                        break;
                    case 2:
                        setLogText(Messages.crtVerification_status_chainFailChain);
                        break;
                    }
                }
            } else {
                cpv = new CertPathVerifier(verificationDate, signatureDate);
                ArrayList<String> errors = cpv.verifyChangedDate(mode, fromCert, thruCert, fromCa, thruCa, fromRootCa,
                        thruRootCa, signatureDate, verificationDate);

                if (errors.isEmpty()) {
                    valid = true;
                    switch (mode) {
                    case 0:
                        setLogText(Messages.crtVerification_status_dateSucShell);
                        break;
                    case 1:
                        setLogText(Messages.crtVerification_status_dateSucModShell);
                        break;
                    case 2:
                        setLogText(Messages.crtVerification_status_dateSucChain);
                        break;
                    }

                } else {
                    switch (mode) {
                    case 0:
                        setLogText(Messages.crtVerification_status_dateFailShell);
                        break;
                    case 1:
                        setLogText(Messages.crtVerification_status_dateFailModShell);
                        break;
                    case 2:
                        setLogText(Messages.crtVerification_status_dateFailChain);
                        break;
                    }

                    for (String string : errors) {
                        setLogText(string);
                    }
                }
            }

            if (valid) {
                composite.setValidtiySymbol(1);
            } else {
                composite.setValidtiySymbol(2);
            }

        } catch (NullPointerException | InvalidAlgorithmParameterException e) {
            if (rootCA == null) {
                setLogText(Messages.crtVerification_status_missingRootCert);
            }
            if (ca == null) {
                setLogText(Messages.crtVerification_status_missingCACert);
            }
            if (tn == null) {
                setLogText(Messages.crtVerification_status_missingClientCert);
            }
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
    }

    /**
     * Updating local variables with the values of the Textfield and Labels from Composite Class.
     */
    public void parseDatesFromComposite() {
        thruRootCa = parseDate(composite.textRootCaThruDay.getText(), composite.thruRootCa.getText());
        fromRootCa = parseDate(composite.textRootCaFromDay.getText(), composite.fromRootCa.getText());
        thruCa = parseDate(composite.textCaThruDay.getText(), composite.thruCa.getText());
        fromCa = parseDate(composite.textCaFromDay.getText(), composite.fromCa.getText());
        thruCert = parseDate(composite.textCertThruDay.getText(), composite.thruCert.getText());
        fromCert = parseDate(composite.textCertFromDay.getText(), composite.fromCert.getText());
        verificationDate = parseDate(composite.textVerificationDateDay.getText(), composite.verificationDate.getText());
        signatureDate = parseDate(composite.textSignatureDateDay.getText(), composite.signatureDate.getText());
//        if (!composite.btnReset.isEnabled())
//            composite.btnReset.setEnabled(true);
    }

    /**
     * Parses a date in format /{Month_3Letters}/{Year_last_two_numbers_}{Day}
     * 
     * @param day the day of the date
     * @param monthYear the month and year of the date
     * @return a date or null if parsing fails
     */
    public Date parseDate(String day, String monthYear) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");

        String month = monthYear.split("/")[1];
        String year = monthYear.split("/")[2];

        int yearInt = Integer.parseInt(year);

        if (yearInt < 84) {
            if (String.valueOf(yearInt).length() == 1) {
                year = "200" + yearInt;
            } else {
                year = "20" + yearInt;
            }
        } else {
            if (String.valueOf(yearInt).length() == 1) {
                year = "190" + yearInt;
            } else {
                year = "19" + yearInt;
            }
        }

        try {
            date = df.parse(day + month + year);
        } catch (ParseException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }

        return date;
    }

    public String parseDateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date);
    }

    public void reset() {

        composite.scaleRootCaBegin.setSelection(70);
        composite.scaleRootCaEnd.setSelection(330);
        composite.scaleCaBegin.setSelection(100);
        composite.scaleCaEnd.setSelection(300);
        composite.scaleCertBegin.setSelection(130);
        composite.scaleCertEnd.setSelection(230);
        composite.scaleVerificationDate.setSelection(380);
        composite.scaleSignatureDate.setSelection(340);

        updateElements(composite.fromRootCa, composite.scaleRootCaBegin, 180);
        updateElements(composite.thruRootCa, composite.scaleRootCaEnd, 180);
        updateElements(composite.fromCa, composite.scaleCaBegin, 180);
        updateElements(composite.thruCa, composite.scaleCaEnd, 180);
        updateElements(composite.fromCert, composite.scaleCertBegin, 180);
        updateElements(composite.thruCert, composite.scaleCertEnd, 180);
        updateElements(composite.verificationDate, composite.scaleVerificationDate, 360);
        updateElements(composite.signatureDate, composite.scaleSignatureDate, 360);

        composite.textRootCaFromDay.setText("1");
        composite.textRootCaThruDay.setText("1");
        composite.textCaFromDay.setText("1");
        composite.textCaThruDay.setText("1");
        composite.textCertFromDay.setText("1");
        composite.textCertThruDay.setText("1");
        composite.textVerificationDateDay.setText("1");
        composite.textSignatureDateDay.setText("1");
        composite.labelValiditySymbol.setVisible(false);
        composite.btnLoadRootCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        composite.btnLoadCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        composite.btnLoadUserCert.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));

        if (((composite.scaleSignatureDate.getSelection() - 360) % 2) == 0) {
            composite.arrowSigDiff = (composite.scaleSignatureDate.getSelection() - 360) / 2;
        } else {
            composite.arrowSigDiff = ((composite.scaleSignatureDate.getSelection() + 1) - 360) / 2;
        }

        if (((composite.scaleVerificationDate.getSelection() - 360) % 2) == 0) {
            composite.arrowVerDiff = (composite.scaleVerificationDate.getSelection() - 360) / 2;
        } else {
            composite.arrowVerDiff = ((composite.scaleVerificationDate.getSelection() + 1) - 360) / 2;
        }

        composite.canvas1.redraw();
        composite.canvas2.redraw();

        flushCertificates();

        composite.txtLogWindow.setText("");
        composite.validationCounter = 0;
        composite.btnShellModel.setSelection(true);
        composite.btnShellModelModified.setSelection(false);
        composite.btnChainModel.setSelection(false);
        composite.scaleVerificationDate.setEnabled(true);
//        composite.btnReset.setEnabled(false);

    }

    /**
     * Flushing Certificates
     */
    public void flushCertificates() {
        rootCA = null;
        ca = null;
        tn = null;
        setLogText(Messages.crtVerification_status_flushCerts);
    }

    public void loadCertificate(ChooseCertPage p, X509Certificate cert, String contact_name) {
    	composite.labelValiditySymbol.setVisible(false);
        switch (p.getCertType()) {
        case 1: // [1] UserCert
            setTN(cert);
            setScales(1);
            flag = true;
            composite.btnLoadUserCert.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
            setLogText("\"" + contact_name + "\" " + Messages.crtVerification_status_UserCertLoaded);
            break;
        case 2: // [2] Cert
            setCA(cert);
            setScales(2);
            flag = true;
            composite.btnLoadCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
            setLogText("\"" + contact_name + "\" " + Messages.crtVerification_status_CaLoaded);
            break;
        case 3: // [3] RootCert
            setRootCA(cert);
            setScales(3);
            flag = true;
            composite.btnLoadRootCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
            setLogText("\"" + contact_name + "\" " + Messages.crtVerification_status_RootCaLoaded);
            break;
        }
        p.setPageComplete(true);
//        if (!composite.btnReset.isEnabled())
//            composite.btnReset.setEnabled(true);
    }

    public void updateElements(Label l, Scale s, int default_selection) {
        l.setText(scaleUpdate(s.getSelection(), default_selection, getDateformat1()));
        s.setToolTipText(scaleUpdate(s.getSelection(), default_selection, getDateformat2()));
        // if(default_selection==180){
        if (rootCA != null || ca != null || tn != null) {
            flushCertificates();
        }
        // }
        flag = false;
    }

    /**
     * Setting the Log Text
     * 
     * @param s The String to append to Log Field
     */
    public void setLogText(String s) {
        // composite.txtLogWindow.append(now("dd.MM.yy HH:mm:ss") + ": " + s +
        // " \r\n");
        composite.txtLogWindow.append(s + "\n");
    }

    /**
     * Prints the NotBefore, NotAfter of all three certificates and Verification and Signation Time
     * to the log
     */
    public void logValidityDates() {

        parseDatesFromComposite();

        setLogText(Messages.CrtVerViewComposite_RootCa + ": " + Messages.CrtVerViewComposite_validFrom + " "
                + getFromRootCa() + ", " + Messages.CrtVerViewComposite_validThru + " " + getThruRootCa());
        setLogText(Messages.CrtVerViewComposite_Ca + ": " + Messages.CrtVerViewComposite_validFrom + " " + getFromCA()
                + ", " + Messages.CrtVerViewComposite_validThru + " " + getThruCA());
        setLogText(Messages.CrtVerViewComposite_UserCertificate + ": " + Messages.CrtVerViewComposite_validFrom + " "
                + getFromClient() + ", " + Messages.CrtVerViewComposite_validThru + " " + getThruClient());

        setLogText(Messages.CrtVerViewComposite_signatureDate + ": " + getSigDate());
        setLogText(Messages.CrtVerViewComposite_verificationDate + ": " + getVerDate());

    }

}
