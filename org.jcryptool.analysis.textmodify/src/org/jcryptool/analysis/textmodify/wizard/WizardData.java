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
package org.jcryptool.analysis.textmodify.wizard;

/**
 * @author SLeischnig
 * saving the transformation wizard settings / configuring the transformation class
 */
public class WizardData {
	private String selectedAlphabetName = ""; //$NON-NLS-1$
	private boolean doUppercase = true;
	private boolean uppercaseTransformationOn = true;
	private boolean alphabetTransformationON = false;
	private boolean umlautTransformationON = true;
	private boolean leerTransformationON = true;

	/**
	 * Creates the class, setting all variables such, that a Transformation with these parameters would leave the Text unmodified.
	 */
	public WizardData()
	{setUnmodified();}

	/**
	 * @param pSelectedAlphabetName the name of the filtering alphabet
	 * @param pDoUppercase true: UPPERCASE. false: lowercase.
	 * @param pUppercaseTransformationON whether upper/lowercase transformation will be applied.
	 * @param pLeerTransformationON whether blanks removal transformation will be applied.
	 * @param pAlphabetTransformationON whether filter-characters-by-alphabet transformation will be applied.
	 * @param pUmlautTransformationON whether umlauts-replacing transformation will be applied.
	 */
	public WizardData(final String pSelectedAlphabetName, final boolean pDoUppercase, final boolean pUppercaseTransformationON, final boolean pLeerTransformationON, final boolean pAlphabetTransformationON, final boolean pUmlautTransformationON)
	{
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

	public final void setUppercaseTransformationOn(final boolean uppercaseTransformationOn) {
		this.uppercaseTransformationOn = uppercaseTransformationOn;
	}

	public final boolean isUppercaseTransformationOn() {
		return uppercaseTransformationOn;
	}

	public final void setAlphabetTransformationON(final boolean alphabetTransformationON) {
		this.alphabetTransformationON = alphabetTransformationON;
	}

	public final boolean isAlphabetTransformationON() {
		return alphabetTransformationON;
	}

	public final void setUmlautTransformationON(final boolean umlautTransformationON) {
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
	 * Sets all parameters such, that a transformation with them would leave a text unmodified.
	 */
	public final void setUnmodified()
	{
		selectedAlphabetName = ""; //$NON-NLS-1$
		doUppercase = true;
		uppercaseTransformationOn = false;
		alphabetTransformationON = false;
		umlautTransformationON = false;
		leerTransformationON = false;
	}

}
