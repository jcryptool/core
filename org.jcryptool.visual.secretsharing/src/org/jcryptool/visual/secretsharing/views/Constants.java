// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.views;

import java.math.BigInteger;

import org.eclipse.swt.graphics.Color;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public interface Constants {
	public enum Mode {
		GRAPHICAL, NUMERICAL
	}
	/*
	 * COLORS
	 */
	public Color BLACK = new Color(null, 0, 0, 0);
	public Color WHITE = new Color(null, 255, 255, 255);
	public Color RED = new Color(null, 255, 0, 0);
	public Color GREEN = new Color(null, 0, 255, 0);
	public Color BLUE = new Color(null, 0, 0, 255);
	public Color LIGHTGREY = new Color(null, 240, 240, 240);
	public Color LIGHTBLUE = new Color(null, 0, 255, 255);
	public Color PURPLE = new Color(null, 255, 0, 255);
	public Color DARKPURPLE = new Color(null, 148, 3, 148);
	public Color MAGENTA = new Color(null, 255, 0, 255);

	/*
	 * UNICODE CONSTANTS
	 */
	public final String sZero = ("\u2070"); //$NON-NLS-1$
	public final String sOne = ("\u00B9"); //$NON-NLS-1$
	public final String sTwo = ("\u00B2"); //$NON-NLS-1$
	public final String sThree = ("\u00B3"); //$NON-NLS-1$
	public final String sFour = ("\u2074"); //$NON-NLS-1$
	public final String sFive = ("\u2075"); //$NON-NLS-1$
	public final String sSix = ("\u2076"); //$NON-NLS-1$
	public final String sSeven = ("\u2077"); //$NON-NLS-1$
	public final String sEight = ("\u2078"); //$NON-NLS-1$
	public final String sNine = ("\u2079"); //$NON-NLS-1$

	public final String uCongruence = ("\u2261"); //$NON-NLS-1$
	public final String uDot = "\u2022"; //$NON-NLS-1$
	public final String uE = ("\u220A"); //$NON-NLS-1$
	public final String uI = ("\u1D62"); //$NON-NLS-1$
	public final String uMinus = "\u208B"; //$NON-NLS-1$
	public final String uMinusUp = "\u207B"; //$NON-NLS-1$
	public final String uNotEqual = ("\u2260"); //$NON-NLS-1$
	public final String uProduct = "\u03A0"; //$NON-NLS-1$
	public final String uR = ("\u1D63"); //$NON-NLS-1$
	public final String uST = ("\u2264"); //$NON-NLS-1$
	public final String uSum = ("\u03A3"); //$NON-NLS-1$
	public final String uT = ("\u2071"); //$NON-NLS-1$
	public final String uElem = ("\u2208"); //$NON-NLS-1$

	public final String uZero = ("\u2080"); //$NON-NLS-1$
	public final String uOne = ("\u2081"); //$NON-NLS-1$
	public final String uTwo = ("\u2082"); //$NON-NLS-1$
	public final String uThree = ("\u2083"); //$NON-NLS-1$
	public final String uFour = ("\u2084"); //$NON-NLS-1$
	public final String uFive = ("\u2085"); //$NON-NLS-1$
	public final String uSix = ("\u2086"); //$NON-NLS-1$
	public final String uSeven = ("\u2087"); //$NON-NLS-1$
	public final String uEight = ("\u2088"); //$NON-NLS-1$
	public final String uNine = ("\u2089"); //$NON-NLS-1$

	/*
	 * MESSAGE CONSTANTS
	 */
	public final String MESSAGE_TITLE = ("Shamir's Secret Sharing"); //$NON-NLS-1$

	public final String MESSAGE_FORMULAR = ("\u03A3 si \u2022 \u03A0 ( x - xr ) \u2022 ( xi - xr )\u207B\u00B9 mod p"); //$NON-NLS-1$
	public final String MESSAGE_FORMULAR_RANGE = ("0  \u2264 i,r \u2264 t, i\u2260r"); //$NON-NLS-1$
	public final String MESSAGE_DESCRIPTION = (Messages.SSSConstants_Title_Info);
	public final String MESSAGE_INFO = Messages.SSSConstants_Reconstruct_Info;
	public final String MESSAGE_RECONSTRUCTION_FALSE = Messages.SSSConstants_Polynom_Not_Equal;
	public final String MESSAGE_RECONSTRUCTION_TRUE = Messages.SSSConstants_Polynom_Equal;
	public final String MESSAGE_GRAPH = ("Graph"); //$NON-NLS-1$
	public final String MESSAGE_RECONSTRUCT = Messages.SSSConstants_Reconstrct;
	public final String MESSAGE_SELECT_ALL = Messages.SSSConstants_Select_All_Button;
	public final String MESSAGE_DESELECT_ALL = Messages.SSSConstants_Deselect_All_Button;
	public final String MESSAGE_ZOOM = Messages.SSSConstants_Zoom;
	public final String MESSAGE_SETTINGS = Messages.SSSConstants_Settings;
	public final String MESSAGE_MODUS = Messages.SSSConstants_Select_Modus;
	public final String MESSAGE_GRAPHICAL = Messages.SSSConstants_Graphical_Mode;
	public final String MESSAGE_NUMERICAL = Messages.SSSConstants_Numerical_Mode;
	public final String MESSAGE_PARAMETER = Messages.SSSConstants_Select_Parameter;
	public final String MESSAGE_CONCERNED_PERSONS = Messages.SSSConstants_Concerned_Persons;
	public final String MESSAGE_RECONSTRUCT_PERSONS = Messages.SSSConstants_reconstruct_Person;
	public final String MESSAGE_MODUL = Messages.SSSConstants_Modul_info;
	public final String MESSAGE_SECRET = Messages.SSSConstants_Secret_Info;
	public final String MESSAGE_COEFFICIENT = Messages.SSSConstants_Coefficient;
	public final String MESSAGE_SELECT = Messages.SSSConstants_Select;
	public final String MESSAGE_POLYNOM = Messages.SSSConstants_Polynom_Info;
	public final String MESSAGE_P = ("P(x):"); //$NON-NLS-1$
	public final String MESSAGE_COMPUTE_SHARES = Messages.SSSConstants_Compute_Share_Button;
	public final String MESSAGE_SHARES = "Shares"; //$NON-NLS-1$
	public final String MESSAGE_RECONSTRUT = Messages.SSSConstants_Reconstruction_Group;
	public final String MESSAGE_SHARE = "Share "; //$NON-NLS-1$
	public final String MESSAGE_LEFT_PARENTHESIS = "("; //$NON-NLS-1$
	public final String MESSAGE_RIGHT_PARENTHESIS = ")"; //$NON-NLS-1$
	public final String MESSAGE_EQUAL = "="; //$NON-NLS-1$
	public final String MESSAGE_SEPERATOR = "|"; //$NON-NLS-1$
	public final String MESSAGE_W = "w"; //$NON-NLS-1$
	public final String MESSAGE_INFO_GROUP = Messages.SSSConstants_Info_Group;
	public final String MESSAGE_P_ = "P'(x)"; //$NON-NLS-1$
	public final String MESSAGE_LAGRAGE = Messages.SSSConstants_Title_Info_Formula;

	// public final String MESSAGE_FORMULAR = (uSum + " " + "s" + uI + " " + " " + uDot + " " + uProduct + " ( x - x" +
	// uR
	// + " ) " + uDot + " ( x" + uI + " - " + "x" + uR + " )" + uMinusUp + sOne + " mod p");
	// public final String MESSAGE_FORMULAR_RANGE = ("0 " + " " + uST + " i,r " + uST + " t, i" + uNotEqual + "r");
	// public final String MESSAGE_DESCRIPTION =
	// ("Shamir's Secret Sharing is a secret sharing algorithm for distributing a secret. The secret is divided into parts, giving each participant its own unique"
	// +
	// "\npart where some of the parts or all of them are needed in order to reconstruct the secret. The secret is reconstructed with the lagrage polynomial.\n");
	// public final String MESSAGE_INFO = "In order to reconstruct the polynomial any t points "
	// + "will be enough. We use the Lagrage interpolation formula to reconstruct the polynomial:";
	// public final String MESSAGE_MODUL = "Modul p (p " + uElem + " prime, p > n):";
	// public final String MESSAGE_POLYNOM = "Polynom: s + a" + uOne + "x + a" + uTwo + "x²" + " + ... + a" + uR +
	// "xʳ \t   (r := t-1)";

	/*
	 * MESSAGE COEFFICIENTS DIALOG
	 */
	public final String MESSAGE_COEFFICIENTS_DIALOG = (Messages.SSSConstants_Dialog_Info);
	public final String MESSAGE_COEFFICIENTS_GROUP_NAME = (Messages.SSSConstants_Dialog_Coefficient_Group);
	public final String MESSAGE_COEFFICIENTS_TITLE = (Messages.SSSConstants_Dialog_Select_Coefficient_Button);
	public final String MESSAGE_COEFFICIENTS_GENERATE = (Messages.SSSConstants_Dialog_Coefficient_Generate_Button);

	/*
	 * MESSAGE PRIME DIALOG
	 */
	public final String MESSAGE_PRIME_MODUL_LABEL = (Messages.SSSConstants_Dialog_Modul_Info);
	public final String MESSAGE_PRIME_DIALOG = (Messages.SSSConstants_Dialog_Message);
	public final String MESSAGE_PRIME_HEAD = (Messages.SSSConstants_Dialog_Prime_Title);
	public final String MESSAGE_PRIME_TITLE = (Messages.SSSConstants_Dialog_Prime);
	public final String MESSAGE_NEXT_PRIME = (Messages.SSSConstants_Dialog_Next_Prime);
	public final String MESSAGE_VERIFY_INPUT = (Messages.SSSConstants_Dialog_Verify_Input);

	/*
	 * MESSAGE SECRET DIALOG
	 */
	public final String MESSAGE_SECRET_LABEL = (Messages.SSSConstants_Dialog_Secret_Info);
	public final String MESSAGE_SECRET_DIALOG = (Messages.SSSConstants_Dialog_Secret_Message);
	public final String MESSAGE_SECRET_HEADER = (Messages.SSSConstants_Dialog_Secret_Header);
	public final String MESSAGE_SECRET_TITLE = (Messages.SSSConstants_Dialog_Secret);

	/*
	 * MESSAGE RECONSTRUCTION
	 */
	public final String MESSAGE_RECONSTRUCTION = ("P'(x) = \u03A3 w\u1D62 ="); //$NON-NLS-1$

	public final BigInteger MINUS_ONE = new BigInteger("-1"); //$NON-NLS-1$
}
