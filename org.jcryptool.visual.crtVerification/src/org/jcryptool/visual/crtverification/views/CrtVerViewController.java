package org.jcryptool.visual.crtverification.views;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

public class CrtVerViewController {
	private Calendar calendar = Calendar.getInstance();
	private Calendar calendar1 = Calendar.getInstance();
	private String dateformat = "/MMM/yy";
	private SimpleDateFormat dt1 = new SimpleDateFormat(dateformat);
	private Date now = calendar.getTime();
	public static X509Certificate RootCA;
	public static X509Certificate CA;
	private static X509Certificate TN;
	
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
	
}
