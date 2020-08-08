/*==========================================================================
 * 
 * Encoder.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.4 $
 * $Date: 2012/11/06 16:45:21 $
 * $Name:  $
 * 
 * Created on 15-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.core;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.widgets.Composite;

import net.sourceforge.ehep.EhepPlugin;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class Encoder {
	private Vector<String> encodingTypes = new Vector<String>();
	
	/**
	 * Loads the encoding types from plugin property file
	 * @param parent
	 */
	public Encoder(Composite parent) {
		int encodingDefault = 0;
		
		String encodingList = EhepPlugin.getResourceString("encoding.list"); //$NON-NLS-1$
		
		if (encodingList == null) {
			//
			// Missing 'encoding.list' parameter - set default
			//
			encodingList = "ASCII"; //$NON-NLS-1$
		}
		
		try {
			//
			// Get index of the default encoding from EHEP property file
			//
			encodingDefault = Integer.parseInt(EhepPlugin.getResourceString("encoding.default")); //$NON-NLS-1$
		}
		catch (NumberFormatException e) {
			//
			// Missing or incorrect 'encoding.default' parameter - set default
			//
			encodingDefault = 0;
		}
		
		String encodingName;
		StringTokenizer st = new StringTokenizer(encodingList, ","); //$NON-NLS-1$
		
		while (st.hasMoreTokens()) {
			encodingName = st.nextToken();
			if (isEncodingSupported(encodingName)) {
				//
				// Encoding is supported, add it to the vector
				//
				encodingTypes.add(encodingName);
			} // if
		} // while
		
		//
		// Move default encoding at the beginning of the vector
		//
		if (encodingDefault > 0) {
			String s = encodingTypes.get(encodingDefault);
			encodingTypes.removeElementAt(encodingDefault);
			encodingTypes.insertElementAt(s, 0);
		} // if
	}

	/**
	 * Returns vector with encoding types (canonical names)
	 * @return vector with encoding types (canonical names)
	 */
	public Vector<String> getEncodingTypes() {
		return encodingTypes;
	}

	/**
	 * Checks whether the particular encoding is really supported by installed JRE or not<br/>
	 * Bugfix #847459 (see http://sourceforge.net/projects/ehep)
	 * @param encodingName canonical encoding name
	 * @return true if encoding is supported otherwise false
	 */
	private boolean isEncodingSupported(String encodingName) {
		//
		// Testing data sample
		//
		byte[] b = {-128, -127, -126, -125, 0, 1, 2, 3, 124, 125, 126, 127};
		String s;
		
		try {
			//
			// Try to create a string from byte array (data sample) with required encoding
			//
			s = new String(b, encodingName);
			s.getClass();
		}
		catch (UnsupportedEncodingException e) {
			//
			// Oops, JRE doesn't support this encoding :o(
			//
			return false;
		}
		//
		// Sure, this encoding is supported :o)
		//
		return true;
	}
}
