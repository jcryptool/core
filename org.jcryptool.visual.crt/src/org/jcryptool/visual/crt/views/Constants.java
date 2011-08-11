/**
 *
 */
package org.jcryptool.visual.crt.views;


/**
 * @author Oryal Inel
 */
public interface Constants {

	/*
	 * Subscript unicode values
	 */
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
	public final String uCongruence = ("\u2261"); //$NON-NLS-1$
	public final String uR = ("\u1D63"); //$NON-NLS-1$
	public final String uI = ("\u1D62"); //$NON-NLS-1$
	public final String uST = ("\u2264"); //$NON-NLS-1$
	public final String uProduct = "\u03A0"; //$NON-NLS-1$
	public final String uSum = "\u03A3"; //$NON-NLS-1$
	public final String uDot = "\u2022"; //$NON-NLS-1$

	/*
	 * Message constants
	 */

	public final String MESSAGE_STEP1 = Messages.CRTConstants_Step1;
	public final String MESSAGE_STEP2 = Messages.CRTConstants_Step2;
	public final String MESSAGE_STEP3 = Messages.CRTConstants_Step3;
	public final String MESSAGE_STEP4 = Messages.CRTConstants_Step4;

//	public final String MESSAGE_SPACE = " ";
//	public final String MESSAGE_NEW_LINE = "\n";
//	public final String MESSAGE_COMMA = ",";
//	public final String MESSAGE_DOTS = ", ... ,";
//	public final String MESSAGE_POINT = ".";
//	public final String MESSAGE_STEP1 = "Add equations of the form" + MESSAGE_SPACE + "x" + MESSAGE_SPACE + uCongruence
//			+ MESSAGE_SPACE + "a" + uZero + MESSAGE_SPACE + "mod " + "m" + uZero + MESSAGE_SPACE + MESSAGE_COMMA
//			+ MESSAGE_NEW_LINE + "x" + MESSAGE_SPACE + uCongruence + MESSAGE_SPACE + "a" + uOne + MESSAGE_SPACE + "mod"
//			+ MESSAGE_SPACE + "m" + uOne + MESSAGE_SPACE + MESSAGE_DOTS + MESSAGE_SPACE + "x" + MESSAGE_SPACE
//			+ uCongruence + MESSAGE_SPACE + "a" + uR + MESSAGE_SPACE + "mod " + "m" + uR + MESSAGE_POINT
//			+ MESSAGE_SPACE + "The values" + MESSAGE_NEW_LINE + "m" + uZero + MESSAGE_SPACE + MESSAGE_DOTS
//			+ MESSAGE_SPACE + "m" + uR + MESSAGE_SPACE + "and" + MESSAGE_SPACE + "a" + uZero + MESSAGE_SPACE
//			+ MESSAGE_DOTS + MESSAGE_SPACE + "a" + uR + MESSAGE_SPACE + "have to be natural numbers."
//			+ MESSAGE_NEW_LINE + "Also" + MESSAGE_SPACE + "m" + uZero + MESSAGE_SPACE + MESSAGE_DOTS + MESSAGE_SPACE
//			+ "m" + uR + MESSAGE_SPACE + "have to be" + MESSAGE_SPACE + "paired coprime.";
//	public final String MESSAGE_STEP2 = "Compute m =" + MESSAGE_SPACE + uProduct + MESSAGE_SPACE + "( m" + uZero
//			+ MESSAGE_SPACE + MESSAGE_DOTS + MESSAGE_SPACE + "m" + uR + " ) " + "and" + MESSAGE_SPACE + "M" + uI
//			+ MESSAGE_SPACE + "= m / m" + uI + MESSAGE_NEW_LINE + "for 0" + MESSAGE_SPACE + uST + MESSAGE_SPACE + "i"
//			+ MESSAGE_SPACE + uST + MESSAGE_SPACE + "r";
//	public final String MESSAGE_STEP3 = "Use the extended euclidean to find the" + MESSAGE_NEW_LINE + "inverse y" + uI
//	+ MESSAGE_SPACE + "of M" + uI + " : " + "( y" + uI + MESSAGE_SPACE + uDot + " M" + uI + " ) " + uCongruence
//	+ " 1 mod m" + uI + MESSAGE_SPACE + MESSAGE_COMMA + MESSAGE_SPACE + "for 0 " + uST + " i " + uST
//	+ MESSAGE_SPACE + "r" + MESSAGE_COMMA + MESSAGE_NEW_LINE + "because of" + MESSAGE_SPACE + "m" + uI
//	+ MESSAGE_SPACE + "paired coprime. Note that the" + MESSAGE_NEW_LINE + "inverse" + MESSAGE_SPACE + "y" + uI
//	+ MESSAGE_SPACE + "can be negative.";
//	public final String MESSAGE_STEP4 = "Now the solution can be calculated in the following" + MESSAGE_NEW_LINE
//			+ "manner: x = " + uSum + " ( a" + uI + MESSAGE_SPACE + uDot + " y" + uI + MESSAGE_SPACE + uDot + " M" + uI
//			+ " ) mod m , " + "for 0 " + uST + " i " + uST + " r." + MESSAGE_SPACE
//			+ "Note" + MESSAGE_NEW_LINE + "that the algorithm is independent of the choice" + MESSAGE_NEW_LINE + "of the a" + uI + "'s.";
	public final String MESSAGE_RESULT = Messages.CRTConstants_StepResult;
	public final String MESSAGE_MORE_SOLUTION = Messages.CRTConstants_MoreSolution;
	public final String MESSAGE_GROUP_NAME = Messages.CRTConstants_ActionFlow;
	public final String MESSAGE_INVERSE_GROUP = Messages.CRTConstants_Inverse;
	public final String MESSAGE_RESULT_GROUP = Messages.CRTConstants_Result;
	public final String MESSAGE_VERIFY_GROUP = Messages.CRTConstants_Veify;
	public final String MESSAGE_NEXT = Messages.CRTConstants_Next;
	public final String MESSAGE_PREVIOUS = Messages.CRTConstants_Previous;
	public final String MESSAGE_GROUP_EQUATION = (Messages.CRTConstants_EquationGroup);

	public final String MESSAGE_STEP_1_GROUP = Messages.CRTConstants_Step1Title;
	public final String MESSAGE_STEP_2_GROUP = Messages.CRTConstants_Step2Title;
	public final String MESSAGE_STEP_3_GROUP = Messages.CRTConstants_Step3Title;
	public final String MESSAGE_STEP_4_GROUP = Messages.CRTConstants_Step4Title;

	/*
	 * Dialog messages
	 */
	public final String MESSAGE_DIALOG_TITLE = (Messages.CRTConstants_DialogHeader);
	public final String MESSAGE_DIALOG_INFO = (Messages.CRTConstants_DialogInfo);
	public final String MESSAGE_DIALOG_SUGGESTION = (Messages.CRTConstants_DialogButtonSuggestion);
	public final String MESSAGE_DIALOG_VERIFY = (Messages.CRTConstants_DialogButtonVerify);
	public final String MESSAGE_DIALOG_VERIFY_INPUT = (Messages.CRTConstants_DialogTitle);

	/*
	 * Export
	 */
	public final String MESSAGE_EXPORT_PDF = (Messages.CRTConstants_ExportPDF);
	public final String MESSAGE_EXPORT_LATEX = (Messages.CRTConstants_ExportLATEX);
	public final String MESSAGE_EXPORT_CSV = (Messages.CRTConstants_ExportCSV);

}
