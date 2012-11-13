/*==========================================================================
 * 
 * Utils.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.6 $
 * $Date: 2012/11/06 16:45:21 $
 * $Name:  $
 * 
 * Created on 29-Dec-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.core;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.Platform;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class Utils {
	/**
	 * Static array of all one-byte hex numbers (00...FF)
	 */
	private static final String[] hexValues = new String[256];
	
	static {
		//
		// Fill in the array of hex strings
		//
		for (int i = 0; i < 256; i++) {
			String s = Integer.toHexString(i).toUpperCase();
			hexValues[i] = (s.length() < 2) ? ("0" + s) : s; //$NON-NLS-1$
		} // for
	} // static

	/**
	 * Converts single byte to string
	 * @param b byte to convert
	 * @return string 2-digit hex string
	 */
	public static String byte2string(byte b) {
		return hexValues[((int) b) & 0xff];
	}

	/**
	 * Converts hex string to single byte
	 * @param s hex string to convert
	 * @return byte
	 */
	public static byte string2byte(String s) {
		if (s == null || s.length() == 0)
			return 0;
		try {
			byte b1 = Byte.parseByte("" + s.charAt(0), 16); //$NON-NLS-1$
			if (s.length() < 2)
				return b1;
			byte b2 = Byte.parseByte("" + s.charAt(1), 16); //$NON-NLS-1$
			return (byte) (((b1 & 0x0f) << 4) | b2);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Converts hex string to byte array
	 * @param s hex string to convert
	 * @return byte array
	 */
	public static byte[] string2bytes(String s) {
		if (s == null)
			return new byte[0];
		byte[] result = new byte[s.length() / 2];
		for (int i = 0, j = 0, k = 0, n = s.length(); i < result.length && j < n; i++, j += 2) {
			k = j + 2 < n ? j + 2 : n;
			result[i] = string2byte(s.substring(j, k));
		}
		return result;
	}

	/**
	 * Converts integer to string
	 * @param n Number to convert to String
	 * @return Zero-padded number converted to String
	 */
	public static String int2string(int n) {
		String nString = Integer.toHexString(n).toUpperCase();
		return "00000000".substring(nString.length()) + nString; //$NON-NLS-1$
	}

	/**
	 * Converts the specified string of hex characters to a character string.
	 * @param s the hex characters
	 * @return the string
	 */
	public static String hexToChars(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i += 2) {
			sb.append((char) Utils.string2byte(s.substring(i)));
		}
		return sb.toString();
	}
	
	/**
	 * Converts the specified string of characters to a hex character string.
	 * @param s the characters
	 * @return the hex string
	 */
	public static String charsToHex(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			sb.append(Utils.byte2string((byte) c));
		}
		return sb.toString();
	}
	
	/**
	 * Returns true if the given string consists of at most 2 characters which represent a hex number.
	 * @param str the string
	 * @return true if valid
	 */
	public static boolean isValidHexNumber(String str) {
		return str != null && str.length() <= 2
				&& (str.length() > 0 ? Character.digit(str.charAt(0), 16) != -1 : true)
				&& (str.length() > 1 ? Character.digit(str.charAt(1), 16) != -1 : true);
	}

	public static boolean isValidHexString(String str, boolean allowEmptyString) {
		try {
			if (str != null && str.length() > 0 || allowEmptyString) {
				if (allowEmptyString && str.length() == 0)
					return true;
				Integer.parseInt(str, 16);
				return true;
			}
		} catch (NumberFormatException e) {
		}
		return false;
		
	}

	/**
	 * Returns the hex string converted to lowercase.
	 * 
	 * @param hex the hex string
	 * @return the hex string lowercase
	 */
	public static String toLowerCase(String hex) {
		return charsToHex(hexToChars(hex).toLowerCase());
	}

	/**
	 * Returns the EHEP plugin version
	 * @return String EHEP version (e.g. "0.0.5" or "1.0.0") or null in case of troubles
	 */
	public static String getEhepVersion() {
		//
		// Create a descriptive object for each BundleGroup
		//
	    IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();
        if (providers != null) {
        	try {
	            for (int i = 0; i < providers.length; ++i) {
	                IBundleGroup[] bundleGroups = providers[i].getBundleGroups();
	                for (int j = 0; j < bundleGroups.length; ++j) {
	                	if (bundleGroups[j].getIdentifier().equals(EHEP.PLUGIN_ID)) {
	                		//
	                		// We found the EHEP descriptor
	                		//
	                		return bundleGroups[j].getVersion();
	                	} // if
	                } // for
	            } // for
        	} // try
        	catch (Exception e) {
        		return null;
        	}
        } // if
		
		return null;
	}

	/**
	 * Converts the string to "zero-padded" string 
	 * @param str string to pad
	 * @param len desired string length including zeros
	 * @return zero-padded string
	 */
	public static String zeroPadding(String str, int len) {
		StringBuffer buffer = new StringBuffer(str);
		for (int i = 0; i < len-str.length(); i++) {
			buffer.insert(0, '0');
		}
		return buffer.toString();		
	}

//	/**
//	 * Registers the EHEP plugin with all file extensions registered in Eclipse 
//	 */
//	public static void registerPluginForAllExtensions() {
//		StringBuffer debugBuffer = new StringBuffer();
//		IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
//		IEditorDescriptor hexEditorDescriptor = registry.findEditor(EHEP.PLUGIN_ID + ".editors.HexEditor"); //$NON-NLS-1$
//		if (hexEditorDescriptor == null)
//			return;
//		boolean isEhepAssigned = false;
//		String ehepId = hexEditorDescriptor.getId();
//		IFileEditorMapping[] array = registry.getFileEditorMappings();
//
//		if (hexEditorDescriptor != null) {
//			debugBuffer.append("---[ registerPluginForAllExtensions() ]---\n"); //$NON-NLS-1$
//		}
//
//		for (int i = 0; i < array.length; i++) {
//			IFileEditorMapping mapping = (IFileEditorMapping) array[i];
//			IEditorDescriptor[] ed = mapping.getEditors();
//			isEhepAssigned = false;
//
//			debugBuffer.append(mapping.getExtension() + ", " + mapping.getLabel() + ", " + mapping.getName() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//
//			for (int j = 0; j < ed.length; j++) {
//				if (ed[j].getId().equals(ehepId))
//					isEhepAssigned = true;
//				debugBuffer.append("    {" + ed[j].getId() + ", " + ed[j].getLabel() + "}\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			} // for
//			debugBuffer.append("\n"); //$NON-NLS-1$
//	
//			if (!isEhepAssigned) {
//				//
//				// Assign EHEP for this file mapping
//				//
//				((FileEditorMapping) mapping).addEditor((EditorDescriptor) hexEditorDescriptor);
//				debugBuffer.append("    EHEP registered for " + mapping.getExtension() + ", " + mapping.getLabel() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			} // if
//		} // for
//
//		//
//		// Update & save editor mappings
//		//
//		((EditorRegistry) registry).saveAssociations();
//		
//		//
//		// Put some messages to the log file
//		//
//		EhepPlugin.log(debugBuffer.toString());
//	}
//
//	/**
//	 * Removes the editor from all file extensions registered in Eclipse 
//	 */
//	public static void removePluginFromAllExtensions() {
//		IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
//		IEditorDescriptor hexEditorDescriptor = registry.findEditor(EHEP.PLUGIN_ID + ".editors.HexEditor"); //$NON-NLS-1$
//		if (hexEditorDescriptor == null)
//			return;
//		boolean isEhepAssigned = false;
//		String ehepId = hexEditorDescriptor.getId();
//		IFileEditorMapping[] array = registry.getFileEditorMappings();
//
//		for (int i = 0; i < array.length; i++) {
//			IFileEditorMapping mapping = (IFileEditorMapping) array[i];
//			IEditorDescriptor[] ed = mapping.getEditors();
//			isEhepAssigned = false;
//
//			for (int j = 0; j < ed.length; j++) {
//				if (ed[j].getId().equals(ehepId)) isEhepAssigned = true;
//			} // for
//	
//			if (isEhepAssigned) {
//				//
//				// Assign EHEP for this file mapping
//				//
//				((FileEditorMapping) mapping).removeEditor((EditorDescriptor) hexEditorDescriptor);
//			} // if
//		} // for
//
//		//
//		// Update & save editor mappings
//		//
//		((EditorRegistry) registry).saveAssociations();
//	}

}
