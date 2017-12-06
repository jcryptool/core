// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
	Color BLACK = new Color(null, 0, 0, 0);
	Color WHITE = new Color(null, 255, 255, 255);
	Color RED = new Color(null, 255, 0, 0);
	Color GREEN = new Color(null, 0, 255, 0);
	Color BLUE = new Color(null, 0, 0, 255);
	Color LIGHTGREY = new Color(null, 240, 240, 240);
	Color LIGHTBLUE = new Color(null, 0, 255, 255);
	Color PURPLE = new Color(null, 255, 0, 255);
	Color DARKPURPLE = new Color(null, 148, 3, 148);
	Color MAGENTA = new Color(null, 255, 0, 255);

	/*
	 * UNICODE CONSTANTS
	 */
	String sZero = ("\u2070"); //$NON-NLS-1$
	String sOne = ("\u00B9"); //$NON-NLS-1$
	String sTwo = ("\u00B2"); //$NON-NLS-1$
	String sThree = ("\u00B3"); //$NON-NLS-1$
	String sFour = ("\u2074"); //$NON-NLS-1$
	String sFive = ("\u2075"); //$NON-NLS-1$
	String sSix = ("\u2076"); //$NON-NLS-1$
	String sSeven = ("\u2077"); //$NON-NLS-1$
	String sEight = ("\u2078"); //$NON-NLS-1$
	String sNine = ("\u2079"); //$NON-NLS-1$

	String uCongruence = ("\u2261"); //$NON-NLS-1$
	String uDot = "\u2022"; //$NON-NLS-1$
	String uE = ("\u220A"); //$NON-NLS-1$
	String uI = ("\u1D62"); //$NON-NLS-1$
	String uMinus = "\u208B"; //$NON-NLS-1$
	String uMinusUp = "\u207B"; //$NON-NLS-1$
	String uNotEqual = ("\u2260"); //$NON-NLS-1$
	String uProduct = "\u03A0"; //$NON-NLS-1$
	String uR = ("\u1D63"); //$NON-NLS-1$
	String uST = ("\u2264"); //$NON-NLS-1$
	String uSum = ("\u03A3"); //$NON-NLS-1$
	String uT = ("\u2071"); //$NON-NLS-1$
	String uElem = ("\u2208"); //$NON-NLS-1$

	String uZero = ("\u2080"); //$NON-NLS-1$
	String uOne = ("\u2081"); //$NON-NLS-1$
	String uTwo = ("\u2082"); //$NON-NLS-1$
	String uThree = ("\u2083"); //$NON-NLS-1$
	String uFour = ("\u2084"); //$NON-NLS-1$
	String uFive = ("\u2085"); //$NON-NLS-1$
	String uSix = ("\u2086"); //$NON-NLS-1$
	String uSeven = ("\u2087"); //$NON-NLS-1$
	String uEight = ("\u2088"); //$NON-NLS-1$
	String uNine = ("\u2089"); //$NON-NLS-1$

	/*
	 * MESSAGE CONSTANTS
	 */
	String MESSAGE_TITLE = ("Shamir's Secret Sharing"); //$NON-NLS-1$

	String MESSAGE_FORMULAR = ("\u03A3 si \u2022 \u03A0 ( x - xr ) \u2022 ( xi - xr )\u207B\u00B9 mod p"); //$NON-NLS-1$
	String MESSAGE_FORMULAR_RANGE = ("0  \u2264 i,r \u2264 t, i\u2260r"); //$NON-NLS-1$
	String MESSAGE_DESCRIPTION = (Messages.SSSConstants_Title_Info);
	String MESSAGE_INFO = Messages.SSSConstants_Reconstruct_Info;
	String MESSAGE_RECONSTRUCTION_FALSE = Messages.SSSConstants_Polynom_Not_Equal;
	String MESSAGE_RECONSTRUCTION_TRUE = Messages.SSSConstants_Polynom_Equal;
	String MESSAGE_GRAPH = ("Graph"); //$NON-NLS-1$
	String MESSAGE_RECONSTRUCT = Messages.SSSConstants_Reconstrct;
	String MESSAGE_SELECT_ALL = Messages.SSSConstants_Select_All_Button;
	String MESSAGE_DESELECT_ALL = Messages.SSSConstants_Deselect_All_Button;
	String MESSAGE_ZOOM = Messages.SSSConstants_Zoom;
	String MESSAGE_SETTINGS = Messages.SSSConstants_Settings;
	String MESSAGE_MODUS = Messages.SSSConstants_Select_Modus;
	String MESSAGE_GRAPHICAL = Messages.SSSConstants_Graphical_Mode;
	String MESSAGE_NUMERICAL = Messages.SSSConstants_Numerical_Mode;
	String MESSAGE_PARAMETER = Messages.SSSConstants_Select_Parameter;
	String MESSAGE_CONCERNED_PERSONS = Messages.SSSConstants_Concerned_Persons;
	String MESSAGE_RECONSTRUCT_PERSONS = Messages.SSSConstants_reconstruct_Person;
	String MESSAGE_MODUL = Messages.SSSConstants_Modul_info;
	String MESSAGE_SECRET = Messages.SSSConstants_Secret_Info;
	String MESSAGE_COEFFICIENT = Messages.SSSConstants_Coefficient;
	String MESSAGE_SELECT = Messages.SSSConstants_Select;
	String MESSAGE_POLYNOM = Messages.SSSConstants_Polynom_Info;
	String MESSAGE_P = ("P(x):"); //$NON-NLS-1$
	String MESSAGE_COMPUTE_SHARES = Messages.SSSConstants_Compute_Share_Button;
	String MESSAGE_SHARES = "Shares"; //$NON-NLS-1$
	String MESSAGE_RECONSTRUT = Messages.SSSConstants_Reconstruction_Group;
	String MESSAGE_SHARE = "Share "; //$NON-NLS-1$
	String MESSAGE_LEFT_PARENTHESIS = "("; //$NON-NLS-1$
	String MESSAGE_RIGHT_PARENTHESIS = ")"; //$NON-NLS-1$
	String MESSAGE_EQUAL = "="; //$NON-NLS-1$
	String MESSAGE_SEPERATOR = "|"; //$NON-NLS-1$
	String MESSAGE_W = "w"; //$NON-NLS-1$
	String MESSAGE_INFO_GROUP = Messages.SSSConstants_Info_Group;
	String MESSAGE_P_ = "P'(x)"; //$NON-NLS-1$
	String MESSAGE_LAGRANGE = Messages.SSSConstants_Title_Info_Formula;
	String MESSAGE_RESET = Messages.SSSConstants_Reset;

	/*
	 * MESSAGE COEFFICIENTS DIALOG
	 */
	String MESSAGE_COEFFICIENTS_DIALOG = (Messages.SSSConstants_Dialog_Info);
	String MESSAGE_COEFFICIENTS_GROUP_NAME = (Messages.SSSConstants_Dialog_Coefficient_Group);
	String MESSAGE_COEFFICIENTS_TITLE = (Messages.SSSConstants_Dialog_Select_Coefficient_Button);
	String MESSAGE_COEFFICIENTS_GENERATE = (Messages.SSSConstants_Dialog_Coefficient_Generate_Button);

	/*
	 * MESSAGE PRIME DIALOG
	 */
	String MESSAGE_PRIME_MODUL_LABEL = (Messages.SSSConstants_Dialog_Modul_Info);
	String MESSAGE_PRIME_DIALOG = (Messages.SSSConstants_Dialog_Message);
	String MESSAGE_PRIME_HEAD = (Messages.SSSConstants_Dialog_Prime_Title);
	String MESSAGE_PRIME_TITLE = (Messages.SSSConstants_Dialog_Prime);
	String MESSAGE_NEXT_PRIME = (Messages.SSSConstants_Dialog_Next_Prime);
	String MESSAGE_VERIFY_INPUT = (Messages.SSSConstants_Dialog_Verify_Input);

	/*
	 * MESSAGE SECRET DIALOG
	 */
	String MESSAGE_SECRET_LABEL = (Messages.SSSConstants_Dialog_Secret_Info);
	String MESSAGE_SECRET_DIALOG = (Messages.SSSConstants_Dialog_Secret_Message);
	String MESSAGE_SECRET_HEADER = (Messages.SSSConstants_Dialog_Secret_Header);
	String MESSAGE_SECRET_TITLE = (Messages.SSSConstants_Dialog_Secret);

	/*
	 * MESSAGE RECONSTRUCTION
	 */
	String MESSAGE_RECONSTRUCTION = ("P'(x) = \u03A3 w\u1D62 ="); //$NON-NLS-1$

	BigInteger MINUS_ONE = new BigInteger("-1"); //$NON-NLS-1$
}
