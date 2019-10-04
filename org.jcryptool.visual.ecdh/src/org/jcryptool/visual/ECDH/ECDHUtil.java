package org.jcryptool.visual.ECDH;

import org.jcryptool.visual.ECDH.ui.wizards.PublicParametersComposite;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.ellipticcurves.Point;

public class ECDHUtil {
	
	/**
	 * Little helper to transform integers to bitstrings
	 * 
	 * @param i a normal positive integer
	 * @return input parameter i as bitstring
	 */
	public static String intToBitString(int i) {
		String s = ""; //$NON-NLS-1$
		int j = i;
		while (j > 1) {
			s = (j % 2) + s;
			j /= 2;
		}
		s = (j % 2) + s;
		return s;
	}
	
	/**
	 * 
	 * Convert int to bitstring and make the bitstring a certain length Not quite
	 * sure why this is needed, I guess it makes leading 0
	 * 
	 * @param i
	 * @param length
	 * @return
	 */
	public static String intToBitString(int i, int length) {
		String s = ""; //$NON-NLS-1$
		int j = i;
		for (int k = 0; k < length; k++) {
			s = (j % 2) + s;
			j /= 2;
		}
		return s;
	}
	
	/**
	 * Insert a space at every 8th character in a given String
	 * 
	 * @param input a String to be spaced
	 * @return the String with inserted spaces
	 */
	public static String spaceString(String input) {
		return input.replaceAll("(.{8})", "$1 "); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Recursively exponentiate Point on elliptic curve
	 * 
	 * @param p a elliptic curve point
	 * @param m the exponent
	 * @return the power of base p times exponent m
	 */
	public static Point exponentiateLargePoint(Point p, FlexiBigInt m) {
		if (m.doubleValue() == 0)
			return null;
		if (m.doubleValue() == 1)
			return p;
		if (m.mod(new FlexiBigInt("2")).doubleValue() == 0) //$NON-NLS-1$
			return exponentiateLargePoint(p, m.divide(new FlexiBigInt("2"))).multiplyBy2(); //$NON-NLS-1$
		else
			return p.add(exponentiateLargePoint(p, m.subtract(new FlexiBigInt("1")))); //$NON-NLS-1$
	}
	
	
	/**
	 * Convert a large elliptic curve to a nice string, as some of the algorithm methods
	 * format them awfully.
	 * 
	 * @param largeCurveString The raw string by the toString() method
	 * @param type Wether if it's a FP or FM curve
	 * @return a nicely formatted String
	 */
	public static String formatLargeCurve(String largeCurveString, int type) {
		String str = largeCurveString;
		String lines[] = str.split("\\r?\\n");
		String lineA, lineB, lineOrder;
		
		if (type == PublicParametersComposite.TYPE_FP) {
			lineA = lines[1].substring(4, lines[1].length() - 1);
			lineB = lines[2].substring(4, lines[2].length() - 1);
			lineOrder = lines[3].trim().substring(14, lines[3].length() - 1);
			
			lineA = ECDHUtil.spaceString(lineA.toUpperCase());
			lineB = ECDHUtil.spaceString(lineB.toUpperCase());
			lineOrder = ECDHUtil.spaceString(lineOrder.toUpperCase());
		}
		else if (type == PublicParametersComposite.TYPE_FM) {			
			lineA = lines[1].substring(4, lines[1].length() - 1);
			lineB = lines[2].substring(4, lines[2].length() - 1);
			lineOrder = lines[3].trim().substring(14, lines[3].length() - 1);
			
			lineA = lineA.toUpperCase();
			lineB = lineB.toUpperCase();
			lineOrder = lineA.toUpperCase();
		}
		else
			return "";
		
		str = lines[0] + "\na = " + lineA + "\nb = " + lineB + "\nfield order = " + lineOrder;  
		
		str = str.replace("<sup>", "^"); //$NON-NLS-1$ //$NON-NLS-2$
		str = str.replace("</sup>", "");  //$NON-NLS-1$ //$NON-NLS-2$
		return str;
	}
	
	/**
	 * Convert a large elliptic curve generator to a nice string, as some of the algorithm methods
	 * format them awfully.
	 * @param x The raw string by the toString() method
	 * @param y The raw string by the toString() method
	 * @param type Wether if it's a FP or FM curve
	 * @return A nicely formatted String
	 */
	public static String formatLargeGenerator(String x, String y, int type) {
		String str;
		x = x.trim().toUpperCase();
		y = y.trim().toUpperCase();
		
		if (type == PublicParametersComposite.TYPE_FP) {
			x = ECDHUtil.spaceString(x);
			y = ECDHUtil.spaceString(y);
		}
		
		str = "(" + x + ", " + y + ")";  
		return str;
	}

}
