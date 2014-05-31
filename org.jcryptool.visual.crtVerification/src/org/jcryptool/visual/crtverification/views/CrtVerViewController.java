package org.jcryptool.visual.crtverification.views;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.visual.crtverification.models.ShellModelVerifier;

public class CrtVerViewController {
	private Calendar calendar = Calendar.getInstance();
	private Calendar calendar1 = Calendar.getInstance();
	private SimpleDateFormat dt1 = new SimpleDateFormat();
	private Date now = calendar.getTime();
	public static X509Certificate RootCA;
	public static X509Certificate CA;
	private static X509Certificate TN;
	static boolean flag = false;
	
	public CrtVerViewController(){
		super();
	}
	
	// GETTER AND SETTER
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
	public String now(){
		return dt1.format(now);
	}
	
	
	/**
	 * Method to get months from a calendar dependent on the selection of a scale and it's default selection.
	 * 
	 * @param selection The actual selection getting with .getSelection() 
	 * @param default_selection The default Selection of Scale Receiver, it will be subtracted from the value
	 * @return The modified date represented as a String
	 */
	public String scaleUpdate(int selection, int default_selection){
		String format = "/MMM/yy";
		dt1.applyPattern(format);
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, selection - default_selection);
		return String.valueOf(dt1.format(calendar.getTime()));
	}
	
	/**
	 * Method to get month from a calendar dependent on the selection of a scale and it's default selection.
	 * 
	 * @param selection The actual selection getting with .getSelection() 
	 * @param default_selection The default Selection of Scale Receiver, it will be subtracted from the value
	 * @param format The date format to represent
	 * @return The modified date represented as a String
	 */
	public String scaleUpdate(int selection, int default_selection, String format){
		dt1.applyPattern(format);
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, selection - default_selection);
			return String.valueOf(dt1.format(calendar.getTime()));
	}
	
	/**
	 * Method to check a Text Field if value is between 1 and 31. 
	 * @param input The text field
	 * @return True if Textfield has a valid value
	 */
	public boolean inputcheck(Text input){
		int value = Integer.parseInt(input.getText());
		if(value > 0 && value <= 31){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int dateOffset(Date date){
		int offset = 0;
		int month = 0, year=0;
		calendar.setTime(now);
		calendar1.setTime(date);
		month = calendar1.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
		year = calendar1.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
		offset = month + 12*year;
		calendar.add(Calendar.MONTH, offset);
		return offset;
	}
	
	public void setScales(int CertType) {
		Calendar cal = Calendar.getInstance();
		CrtVerViewController controller = new CrtVerViewController();
		switch (CertType){
			case 1:
				// Scale From User Certificate
				CrtVerViewComposite.ScaleFromTN.setSelection(180 + controller.dateOffset(controller.getTN().getNotBefore()));
				CrtVerViewComposite.fromCert.setText(controller.scaleUpdate(CrtVerViewComposite.ScaleFromTN.getSelection(), 180));
				CrtVerViewComposite.ScaleFromTN.setToolTipText(controller.scaleUpdate(CrtVerViewComposite.ScaleFromTN.getSelection(), 180, "MMM/yy"));
				
				// Set Textfield for the Day | From TN
				cal.setTime(controller.getTN().getNotBefore());
				CrtVerViewComposite.TextCertFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				
				// Scale Thru User Certificate
				CrtVerViewComposite.ScaleThruTN.setSelection(180 + controller.dateOffset(controller.getTN().getNotAfter()));
				CrtVerViewComposite.thruCert.setText(controller.scaleUpdate(CrtVerViewComposite.ScaleThruTN.getSelection(), 180));
				CrtVerViewComposite.ScaleThruTN.setToolTipText(controller.scaleUpdate(CrtVerViewComposite.ScaleThruTN.getSelection(), 180, "MMM/yy"));
				
				// Set Textfield for the Day | Thru TN
				cal.setTime(controller.getTN().getNotAfter());
				CrtVerViewComposite.TextCertThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				break;
			case 2:
				// Scale From CA
				CrtVerViewComposite.ScaleFromCA.setSelection(180 + controller.dateOffset(controller.getCA().getNotBefore()));
				CrtVerViewComposite.fromCa.setText(controller.scaleUpdate(CrtVerViewComposite.ScaleFromCA.getSelection(), 180));
				CrtVerViewComposite.ScaleFromCA.setToolTipText(controller.scaleUpdate(CrtVerViewComposite.ScaleFromCA.getSelection(), 180, "MMM/yy"));
				
				// Set Textfield for the Day | From CA
				cal.setTime(controller.getCA().getNotBefore());
				CrtVerViewComposite.TextCaFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				
				// Scale Thru CA
				CrtVerViewComposite.ScaleThruCA.setSelection(180 + controller.dateOffset(controller.getCA().getNotAfter()));
				CrtVerViewComposite.thruCa.setText(controller.scaleUpdate(CrtVerViewComposite.ScaleThruCA.getSelection(), 180));
				CrtVerViewComposite.ScaleThruCA.setToolTipText(controller.scaleUpdate(CrtVerViewComposite.ScaleThruCA.getSelection(), 180, "MMM/yy"));
				
				// Set Textfield for the Day | Thru CA
				cal.setTime(controller.getCA().getNotAfter());
				CrtVerViewComposite.TextCaThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				break;
			case 3:
				// Scale From Root CA
				CrtVerViewComposite.ScaleFromRoot.setSelection(180 + controller.dateOffset(controller.getRootCA().getNotBefore()));
				CrtVerViewComposite.fromRootCa.setText(controller.scaleUpdate(CrtVerViewComposite.ScaleFromRoot.getSelection(), 180));
				CrtVerViewComposite.ScaleFromRoot.setToolTipText(controller.scaleUpdate(CrtVerViewComposite.ScaleFromRoot.getSelection(), 180, "MMM/yy"));
				
				// Set Textfield for the Day | From Root CA
				cal.setTime(controller.getRootCA().getNotBefore());
				CrtVerViewComposite.TextRootCaFromDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				
				// Scale Thru Root CA
				CrtVerViewComposite.ScaleThruRoot.setSelection(180 + controller.dateOffset(controller.getRootCA().getNotAfter()));
				CrtVerViewComposite.thruRootCa.setText(controller.scaleUpdate(CrtVerViewComposite.ScaleThruRoot.getSelection(), 180));
				CrtVerViewComposite.ScaleThruRoot.setToolTipText(controller.scaleUpdate(CrtVerViewComposite.ScaleThruRoot.getSelection(), 180, "MMM/yy"));
				
				// Set Textfield for the Day | Thru Root CA
				cal.setTime(controller.getRootCA().getNotAfter());
				CrtVerViewComposite.TextRootCaThruDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				break;
		}
	}
	
	public void validate(int mode){
		// Mode 1: Schalenmodell
		// Mode 2: Modifiziertes Schalenmodell
		// Mode 3: Kettenmodell
		if(mode == 1){
			if(getRootCA()!=null && getCA()!=null && getTN()!=null){
				ShellModelVerifier smv = new ShellModelVerifier(getRootCA(), getCA(), getTN(), false, now);
				if(flag && smv.verify()){
					CrtVerViewComposite.validity.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
					CrtVerViewComposite.validity.setText("VALID CERTIFICATE CHAIN");
				}
				else{
					CrtVerViewComposite.validity.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					CrtVerViewComposite.validity.setText("NOT VALID");
				}
			}
		}
		else if(mode == 2){
			if(getRootCA()!=null && getCA()!=null && getTN()!=null && flag){
				ShellModelVerifier smv = new ShellModelVerifier(getRootCA(), getCA(), getTN(), false, now);
				if(smv.verify()){
					CrtVerViewComposite.validity.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
					CrtVerViewComposite.validity.setText("VALID CERTIFICATE CHAIN");
				}
				else{
					CrtVerViewComposite.validity.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					CrtVerViewComposite.validity.setText("NOT VALID");
				}
			}
		}
	}
}
