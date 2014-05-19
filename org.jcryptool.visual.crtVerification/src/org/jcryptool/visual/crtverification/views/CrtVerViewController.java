package org.jcryptool.visual.crtverification.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.Text;

public class CrtVerViewController {
	private Calendar calendar = Calendar.getInstance();
	private SimpleDateFormat dt1 = new SimpleDateFormat("MM/yy");
	private Date now = calendar.getTime();
	
	public CrtVerViewController(){
		super();
	}
	
	/**
	 * Method to get the actual date in "MM/yy" Format
	 * 
	 * @return Returns the actual date as MM/yy format
	 */
	public String now(){
		return String.valueOf(dt1.format(now));
	}
	
	
	/**
	 * Method to add or remove months from a calendar dependent on the selection of a scale and it's default selection.
	 * 
	 * @param selection The actual selection getting with .getSelection() 
	 * @param default_selection The default Selection of Scale Receiver, it will be subtracted from the value
	 * @return The modified date represented as a String
	 */
	public String scaleUpdate(int selection, int default_selection){
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
	
}
