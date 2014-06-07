package org.jcryptool.visual.crtverification.views;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;
import org.jcryptool.visual.crtverification.keystore.KeystoreConnector;
import org.jcryptool.visual.crtverification.models.CertPathVerifier;

public class CrtVerViewController {
    private String dateformat1 = "/MMM/yy";
    private String dateformat2 = "MMM/yy";
    private String dateformat3 = "yyyy";
    KeystoreConnector ksc = new KeystoreConnector();
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

    // Elements of Composite
    private Scale ScaleFromTN = CrtVerViewComposite.ScaleCertBegin;
    private Scale ScaleThruTN = CrtVerViewComposite.ScaleCertEnd;
    private Scale ScaleFromCA = CrtVerViewComposite.ScaleCaBegin;
    private Scale ScaleThruCA = CrtVerViewComposite.ScaleCaEnd;
    private Scale ScaleFromRoot = CrtVerViewComposite.ScaleRootCaBegin;
    private Scale ScaleThruRoot = CrtVerViewComposite.ScaleRootCaEnd;

    private static X509Certificate RootCA = null;
    private static X509Certificate CA = null;
    private static X509Certificate TN = null;
    static boolean flag = false;

    public CrtVerViewController() {
        super();
    }

    // GETTER AND SETTER
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
        return RootCA;
    }

    public void setRootCA(X509Certificate rootCA) {
        RootCA = rootCA;
    }

    public X509Certificate getCA() {
        return CA;
    }

    public void setCA(X509Certificate cA) {
        CA = cA;
    }

    public X509Certificate getTN() {
        return TN;
    }

    public void setTN(X509Certificate tN) {
        TN = tN;
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
        if(default_selection == 360){
        		if(((selection - default_selection) % 2) == 0){
        			calendar.add(Calendar.MONTH, (selection - default_selection)/2);
        		}
        		else{
        			calendar.add(Calendar.MONTH, ((selection+1)-default_selection)/2);
        		}
        }
        else{
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
            CrtVerViewComposite.txtTheCertificatechainAlias.append(input.getToolTipText()+" had an invalid Value (>31 or <0) it has been resetted to default value: 1 \r\n");
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
            ScaleFromTN.setSelection(180 + dateOffset(getTN().getNotBefore()));
            CrtVerViewComposite.fromCert.setText(scaleUpdate(ScaleFromTN.getSelection(), 180, dateformat1));
            ScaleFromTN.setToolTipText(scaleUpdate(ScaleFromTN.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | From TN
            cal.setTime(getTN().getNotBefore());
            CrtVerViewComposite.TextCertFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

            // Scale Thru User Certificate
            ScaleThruTN.setSelection(180 + dateOffset(getTN().getNotAfter()));
            CrtVerViewComposite.thruCert.setText(scaleUpdate(ScaleThruTN.getSelection(), 180, dateformat1));
            ScaleThruTN.setToolTipText(scaleUpdate(ScaleThruTN.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | Thru TN
            cal.setTime(getTN().getNotAfter());
            CrtVerViewComposite.TextCertThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            break;
        case 2:
            // Scale From CA
            ScaleFromCA.setSelection(180 + dateOffset(getCA().getNotBefore()));
            CrtVerViewComposite.fromCa.setText(scaleUpdate(ScaleFromCA.getSelection(), 180, dateformat1));
            ScaleFromCA.setToolTipText(scaleUpdate(ScaleFromCA.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | From CA
            cal.setTime(getCA().getNotBefore());
            CrtVerViewComposite.TextCaFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

            // Scale Thru CA
            ScaleThruCA.setSelection(180 + dateOffset(getCA().getNotAfter()));
            CrtVerViewComposite.thruCa.setText(scaleUpdate(ScaleThruCA.getSelection(), 180, dateformat1));
            ScaleThruCA.setToolTipText(scaleUpdate(ScaleThruCA.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | Thru CA
            cal.setTime(getCA().getNotAfter());
            CrtVerViewComposite.TextCaThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            break;
        case 3:
            // Scale From Root CA
            ScaleFromRoot.setSelection(180 + dateOffset(getRootCA().getNotBefore()));
            CrtVerViewComposite.fromRootCa.setText(scaleUpdate(ScaleFromRoot.getSelection(), 180, dateformat1));
            ScaleFromRoot.setToolTipText(scaleUpdate(ScaleFromRoot.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | From Root CA
            cal.setTime(getRootCA().getNotBefore());
            CrtVerViewComposite.TextRootCaFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

            // Scale Thru Root CA
            ScaleThruRoot.setSelection(180 + dateOffset(getRootCA().getNotAfter()));
            CrtVerViewComposite.thruRootCa.setText(scaleUpdate(ScaleThruRoot.getSelection(), 180, dateformat1));
            ScaleThruRoot.setToolTipText(scaleUpdate(ScaleThruRoot.getSelection(), 180, dateformat2));

            // Set Textfield for the Day | Thru Root CA
            cal.setTime(getRootCA().getNotAfter());
            CrtVerViewComposite.TextRootCaThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
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
                    switch(mode){
                    	case 0:
                    		setLogText("Certificate Chain SUCCESSFULLY validated with SHELL MODEL!");
                    		break;
                    	case 1: 
                    		setLogText("Certificate Chain SUCCESSFULLY validated with MODIFIED SHELL MODEL!");
                    		break;
                    	case 2:
                    		setLogText("Certificate Chain SUCCESSFULLY validated with CHAIN MODEL!");
                    		break;
                    }
                }
                else{
                	switch(mode){
                	case 0:
                		setLogText("Certificate Chain FAILED to validate with SHELL MODEL!");
                		break;
                	case 1: 
                		setLogText("Certificate Chain FAILED to validate with MODIFIED SHELL MODEL!");
                		break;
                	case 2:
                		setLogText("Certificate Chain FAILED to validate with CHAIN MODEL!");
                		break;
                }
                }
            } else {
                cpv = new CertPathVerifier(verificationDate, signatureDate);
                if (cpv.verifyChangedDate(mode, fromCert, thruCert, fromCa, thruCa, fromRootCa,
                        thruRootCa, signatureDate, verificationDate)) {
                    valid = true;
                    switch(mode){
                    	case 0:
                    		setLogText("Dates based on selection SUCCESSFULLY validated with SHELL MODEL!");
                    		break;
                    	case 1:
                    		setLogText("Dates based on selection SUCCESSFULLY validated with MODIFIED SHELL MODEL!");
                    		break;
                    	case 2:
                    		setLogText("Dates based on selection SUCCESSFULLY validated with CHAIN MODEL!");
                    		break;
                    }
                }
                else{
                	switch(mode){
                	case 0:
                		setLogText("Dates based on selection FAILED to validate with SHELL MODEL!");
                		break;
                	case 1:
                		setLogText("Dates based on selection FAILED to validate with MODIFIED SHELL MODEL!");
                		break;
                	case 2:
                		setLogText("Dates based on selection FAILED to validate with CHAIN MODEL!");
                		break;
                }
                }
            }

            if (valid) {
                CrtVerViewComposite.setValidtiySymbol(1);
            } else {
                CrtVerViewComposite.setValidtiySymbol(2);
            }

        } catch (NullPointerException | InvalidAlgorithmParameterException e) {
        	if(RootCA==null){
        		setLogText("Root CA certificate missing for certificate chain validation");
        	}
        	if(CA==null){
        		setLogText("CA certificate missing for certificate chain validation");
        	}
        	if(TN==null){
        		setLogText("TN certificate missing for certificate chain validation");
        	}
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
    }

    /**
     * Updating local variables with the values of the Textfield and Labels from Composite Class.
     */
    public void parseDatesFromComposite() {
        thruRootCa = parseDate(CrtVerViewComposite.TextRootCaThruDay.getText(),
                CrtVerViewComposite.thruRootCa.getText());
        fromRootCa = parseDate(CrtVerViewComposite.TextRootCaFromDay.getText(),
                CrtVerViewComposite.fromRootCa.getText());
        thruCa = parseDate(CrtVerViewComposite.TextCaThruDay.getText(), CrtVerViewComposite.thruCa.getText());
        fromCa = parseDate(CrtVerViewComposite.TextCaFromDay.getText(), CrtVerViewComposite.fromCa.getText());
        thruCert = parseDate(CrtVerViewComposite.TextCertThruDay.getText(), CrtVerViewComposite.thruCert.getText());
        fromCert = parseDate(CrtVerViewComposite.TextCertFromDay.getText(), CrtVerViewComposite.fromCert.getText());
        verificationDate = parseDate(CrtVerViewComposite.TextVerificationDateDay.getText(),
                CrtVerViewComposite.verificationDate.getText());
        signatureDate = parseDate(CrtVerViewComposite.TextSignatureDateDay.getText(),
                CrtVerViewComposite.signatureDate.getText());
    }

    /**
     * Parses a date in format /<Month_3Letters>/<Year_last_two_numbers_><Day>
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
            year = "20" + yearInt;
        } else {
            year = "19" + yearInt;
        }

        try {
            date = df.parse(day + month + year);
        } catch (ParseException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }

        return date;
    }

    public void reset() {
        CrtVerViewComposite.ScaleRootCaBegin.setSelection(180);
        CrtVerViewComposite.ScaleRootCaEnd.setSelection(180);
        CrtVerViewComposite.ScaleCaBegin.setSelection(180);
        CrtVerViewComposite.ScaleCaEnd.setSelection(180);
        CrtVerViewComposite.ScaleCertBegin.setSelection(180);
        CrtVerViewComposite.ScaleCertEnd.setSelection(180);
        CrtVerViewComposite.ScaleVerificationDate.setSelection(360);
        CrtVerViewComposite.ScaleSignatureDate.setSelection(360);

        updateElements(CrtVerViewComposite.fromRootCa, CrtVerViewComposite.ScaleRootCaBegin, 180);
        updateElements(CrtVerViewComposite.thruRootCa, CrtVerViewComposite.ScaleRootCaEnd, 180);
        updateElements(CrtVerViewComposite.fromCa, CrtVerViewComposite.ScaleCaBegin, 180);
        updateElements(CrtVerViewComposite.thruCa, CrtVerViewComposite.ScaleCaEnd, 180);
        updateElements(CrtVerViewComposite.fromCert, CrtVerViewComposite.ScaleCertBegin, 180);
        updateElements(CrtVerViewComposite.thruCert, CrtVerViewComposite.ScaleCertEnd, 180);
        updateElements(CrtVerViewComposite.verificationDate, CrtVerViewComposite.ScaleVerificationDate, 360);
        updateElements(CrtVerViewComposite.signatureDate, CrtVerViewComposite.ScaleSignatureDate, 360);

        CrtVerViewComposite.TextRootCaFromDay.setText("1");
        CrtVerViewComposite.TextRootCaThruDay.setText("1");
        CrtVerViewComposite.TextCaFromDay.setText("1");
        CrtVerViewComposite.TextCaThruDay.setText("1");
        CrtVerViewComposite.TextCertFromDay.setText("1");
        CrtVerViewComposite.TextCertThruDay.setText("1");
        CrtVerViewComposite.TextVerificationDateDay.setText("1");
        CrtVerViewComposite.TextSignatureDateDay.setText("1");
        // CrtVerViewComposite.validity.setBackground(null);
        // CrtVerViewComposite.validity.setText("");
        CrtVerViewComposite.validitySymbol.hide();
        CrtVerViewComposite.btnLoadRootCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        CrtVerViewComposite.btnLoadCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        CrtVerViewComposite.btnLoadUserCert.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        CrtVerViewComposite.arrowSigDiff=0;
        CrtVerViewComposite.arrowVerDiff=0;
        CrtVerViewComposite.canvas1.redraw();
        CrtVerViewComposite.canvas2.redraw();
        
        flushCertificates();
        
        CrtVerViewComposite.txtTheCertificatechainAlias.setText("Logging: \r\n");
    }
    /**
     * Flushing Certificates
     */
    public void flushCertificates(){
    	RootCA = null;
        CA = null;
        TN = null;
        setLogText("Certificates flushed");
    }

    public void loadCertificate(ChooseCertPage p, List list) {
        switch (p.getCertType()) {
        case 1: // [1] UserCert
            setTN(ksc.getAllCertificates().get(list.getSelectionIndex()));
            setScales(1);
            flag = true;
            CrtVerViewComposite.btnLoadUserCert.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
            setLogText("User Certificate successfully loaded");
            break;
        case 2: // [2] Cert
            setCA(ksc.getAllCertificates().get(list.getSelectionIndex()));
            setScales(2);
            flag = true;
            CrtVerViewComposite.btnLoadCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
            setLogText("CA Certificate successfully loaded");
            break;
        case 3: // [3] RootCert
            setRootCA(ksc.getAllCertificates().get(list.getSelectionIndex()));
            setScales(3);
            flag = true;
            CrtVerViewComposite.btnLoadRootCa.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
            setLogText("Root Ca Certificate successfully loaded");
            break;
        }
        p.setPageComplete(true);
    }

    public void updateElements(Label l, Scale s, int default_selection) {
        l.setText(scaleUpdate(s.getSelection(), default_selection, getDateformat1()));
        s.setToolTipText(scaleUpdate(s.getSelection(), default_selection, getDateformat2()));
        //if(default_selection==180){
        	if(RootCA != null || CA != null || TN != null){
        		flushCertificates();
        	}
        //}
        flag = false;
    }
  
    /**
     * Setting the Log Text
     * 
     * @param s The String to append to Log Field
     */
    public void setLogText(String s){
    		CrtVerViewComposite.txtTheCertificatechainAlias.append(now("dd.MM.yy HH:mm:ss") + ": " + s + " \r\n");
    }
}
