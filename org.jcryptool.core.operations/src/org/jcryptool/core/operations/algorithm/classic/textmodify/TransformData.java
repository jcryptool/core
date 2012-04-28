//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.classic.textmodify;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * saving the transformation wizard settings / configuring the transformation
 * class
 *
 * @author SLeischnig
 * @version 0.6.0
 */
public class TransformData {
	private String selectedAlphabetName = ""; //$NON-NLS-1$
	private boolean doUppercase = true;
	private boolean uppercaseTransformationOn = true;
	private boolean alphabetTransformationON = false;
	private boolean umlautTransformationON = true;
	private boolean leerTransformationON = true;

	private final static String UPPERLOWERCASE_LABEL = "upper/lowercase"; //$NON-NLS-1$
	private final static String ALPHABET_LABEL = "filterByAlphabet"; //$NON-NLS-1$
	private final static String UMLAUTS_LABEL = "filterUmlauts"; //$NON-NLS-1$
	private final static String BLANKS_LABEL = "filterBlanks"; //$NON-NLS-1$
	private final static String UPPERLOWERCASE_LABEL_R = Messages.TransformData_0;
	private final static String ALPHABET_LABEL_R = Messages.TransformData_1;
	private final static String UMLAUTS_LABEL_R = Messages.TransformData_2;
	private final static String BLANKS_LABEL_R = Messages.TransformData_3;
	private final static String SEPARATOR = ", "; //$NON-NLS-1$

	/**
	 * Creates the class, setting all variables such, that a Transformation with
	 * these parameters would leave the Text unmodified.
	 */
	public TransformData() {
		setUnmodified();
	}

	/**
	 * @param pSelectedAlphabetName
	 *            the name of the filtering alphabet
	 * @param pDoUppercase
	 *            true: UPPERCASE. false: lowercase.
	 * @param pUppercaseTransformationON
	 *            whether upper/lowercase transformation will be applied.
	 * @param pLeerTransformationON
	 *            whether blanks removal transformation will be applied.
	 * @param pAlphabetTransformationON
	 *            whether filter-characters-by-alphabet transformation will be
	 *            applied.
	 * @param pUmlautTransformationON
	 *            whether umlauts-replacing transformation will be applied.
	 */
	public TransformData(final String pSelectedAlphabetName,
			final boolean pDoUppercase,
			final boolean pUppercaseTransformationON,
			final boolean pLeerTransformationON,
			final boolean pAlphabetTransformationON,
			final boolean pUmlautTransformationON) {
		setSelectedAlphabetName(pSelectedAlphabetName);
		setDoUppercase(pDoUppercase);
		setUppercaseTransformationOn(pUppercaseTransformationON);
		setAlphabetTransformationON(pAlphabetTransformationON);
		setUmlautTransformationON(pUmlautTransformationON);
		setLeerTransformationON(pLeerTransformationON);
	}

	public final void setSelectedAlphabetName(final String selectedAlphabetName) {
		this.selectedAlphabetName = selectedAlphabetName;
	}

	public final String getSelectedAlphabetName() {
		return selectedAlphabetName;
	}

	public final void setDoUppercase(final boolean doUppercase) {
		this.doUppercase = doUppercase;
	}

	public final boolean isDoUppercase() {
		return doUppercase;
	}

	public final void setUppercaseTransformationOn(
			final boolean uppercaseTransformationOn) {
		this.uppercaseTransformationOn = uppercaseTransformationOn;
	}

	public final boolean isUppercaseTransformationOn() {
		return uppercaseTransformationOn;
	}

	public final void setAlphabetTransformationON(
			final boolean alphabetTransformationON) {
		this.alphabetTransformationON = alphabetTransformationON;
	}

	public final boolean isAlphabetTransformationON() {
		return alphabetTransformationON;
	}

	public final void setUmlautTransformationON(
			final boolean umlautTransformationON) {
		this.umlautTransformationON = umlautTransformationON;
	}

	public final boolean isUmlautTransformationON() {
		return umlautTransformationON;
	}

	public final void setLeerTransformationON(final boolean leerTransformationON) {
		this.leerTransformationON = leerTransformationON;
	}

	public final boolean isLeerTransformationON() {
		return leerTransformationON;
	}

	/**
	 * Sets all parameters such, that a transformation with them would leave a
	 * text unmodified.
	 */
	public final void setUnmodified() {
		selectedAlphabetName = ""; //$NON-NLS-1$
		doUppercase = true;
		uppercaseTransformationOn = false;
		alphabetTransformationON = false;
		umlautTransformationON = false;
		leerTransformationON = false;
	}
	
	public boolean isUnmodified() {
		if(doUppercase != true) return false;
		if(uppercaseTransformationOn != false) return false;
		if(alphabetTransformationON != false) return false;
		if(umlautTransformationON != false) return false;
		if(leerTransformationON != false) return false;
		return true;
	}

	/**
	 * parses a Transformdata object from a String that was created using
	 * TransformData.toString();
	 *
	 * @return a TransformData instance
	 */
	public static TransformData fromString(String data) {
		TransformData result = new TransformData();
		try {
			String[] split = data.split(SEPARATOR);
			for (int i = 0; i < split.length; i++) {
				if (split[i].contains(UPPERLOWERCASE_LABEL)) {
					result.setUppercaseTransformationOn(true);
					String value = split[i]
							.substring(split[i].indexOf("=") + 1); //$NON-NLS-1$

					result.setDoUppercase(true);
					if (value.equals("lowercase")) { //$NON-NLS-1$
						result.setDoUppercase(false);
					}
				}

				if (split[i].contains(ALPHABET_LABEL)) {
					result.setAlphabetTransformationON(true);
					String value = split[i]
							.substring(split[i].indexOf("=") + 1); //$NON-NLS-1$

					result.setSelectedAlphabetName(value);
				}

				if (split[i].contains(BLANKS_LABEL)) {
					result.setLeerTransformationON(true);
				}

				if (split[i].contains(UMLAUTS_LABEL)) {
					result.setUmlautTransformationON(true);
				}
			}
		} catch (Exception e) {
			LogUtil.logError(OperationsPlugin.PLUGIN_ID,
					"Error when parsing TransformData from String"); //$NON-NLS-1$
		}

		return result;
	}

	public String toReadableString() {
		StringBuilder result = new StringBuilder(""); //$NON-NLS-1$
	
		if (uppercaseTransformationOn) {
			String value = Messages.TransformData_4;
			String separator = SEPARATOR;
			if (doUppercase)
				value = Messages.TransformData_5;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$
	
			result.append(separator + UPPERLOWERCASE_LABEL_R + "" + value); //$NON-NLS-1$
		}
	
		if (alphabetTransformationON) {
			String value = selectedAlphabetName;
			String separator = SEPARATOR;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$
	
			result.append(separator + ALPHABET_LABEL_R + ": " + value); //$NON-NLS-1$
		}
	
		if (leerTransformationON) {
			String value = "on"; //$NON-NLS-1$
			String separator = SEPARATOR;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$
	
			result.append(separator + BLANKS_LABEL_R); //$NON-NLS-1$
		}
	
		if (umlautTransformationON) {
			String value = "on"; //$NON-NLS-1$
			String separator = SEPARATOR;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$
	
			result.append(separator + UMLAUTS_LABEL_R); //$NON-NLS-1$
		}
	
		return result.toString();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(""); //$NON-NLS-1$

		if (uppercaseTransformationOn) {
			String value = "lowercase"; //$NON-NLS-1$
			String separator = SEPARATOR;
			if (doUppercase)
				value = "uppercase"; //$NON-NLS-1$
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$

			result.append(separator + UPPERLOWERCASE_LABEL + "=" + value); //$NON-NLS-1$
		}

		if (alphabetTransformationON) {
			String value = selectedAlphabetName;
			String separator = SEPARATOR;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$

			result.append(separator + ALPHABET_LABEL + "=" + value); //$NON-NLS-1$
		}

		if (leerTransformationON) {
			String value = "on"; //$NON-NLS-1$
			String separator = SEPARATOR;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$

			result.append(separator + BLANKS_LABEL + "=" + value); //$NON-NLS-1$
		}

		if (umlautTransformationON) {
			String value = "on"; //$NON-NLS-1$
			String separator = SEPARATOR;
			if (result.toString().equals("")) //$NON-NLS-1$
				separator = ""; //$NON-NLS-1$

			result.append(separator + UMLAUTS_LABEL + "=" + value); //$NON-NLS-1$
		}

		return result.toString();
	}
	
	@Override
	protected TransformData clone() throws CloneNotSupportedException {
		TransformData clone = new TransformData();
		clone.selectedAlphabetName = selectedAlphabetName;
		clone.doUppercase = doUppercase;
		clone.uppercaseTransformationOn = uppercaseTransformationOn;
		clone.alphabetTransformationON = alphabetTransformationON;
		clone.umlautTransformationON = umlautTransformationON;
		clone.leerTransformationON = leerTransformationON;
		return clone;
	}
	
}
