package org.jcryptool.visual.babystepgiantstep.views;

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * @author Miray Inel
 * 
 */
public interface Constants {
	public static final BigInteger MAX_INTEGER_BI = new BigInteger(String.valueOf(Integer.MAX_VALUE));
	
	/*
	 * COLORS
	 */
	public static final Color BLACK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	public static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	public static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	public static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	public static final Color BLUE = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
	public static final Color MAGENTA = Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);

	public static final Color LIGHTGREY = new Color(null, 240, 240, 240);
	public static final Color LIGHTBLUE = new Color(null, 0, 255, 255);
	public static final Color PURPLE = new Color(null, 255, 0, 255);
	public static final Color DARKPURPLE = new Color(null, 148, 3, 148);

	/*
	 * UNICODE CONSTANTS
	 */
	public static final String uGamma = ("\u03B3");
	public static final String uAlpha = ("\u03B1");
	public static final String uDelta = ("\u03B4");

	public static final String uCongruence = ("\u2261");
	public static final String uDot = "\u2022";
	/*
	 * messages
	 */
	public static final String msgIntro =
			"In dieser Visualisierung des Shanks Babystep-Giantstep-Algorithmus geht es um das Problem, diskrete Logarithmen zu berechnen. Wir wollen den diskreten Logaritmus von " + uAlpha + " zur Basis " + uGamma + " in G berechnen.\r\n" +
			"Sei G eine endliche zyklische Gruppe der Ordnung n und sei " + uGamma + " ein Erzeuger dieser Gruppe mit neutralem Element 1 in G. Ferner sei " + uAlpha + " ein Gruppenelement.\r\n" +
			"Gesucht ist die kleinste nicht negative ganze Zahl x, für die " + uAlpha + " = " + uGamma + " ^ x gilt.";
					
		
	public static final String msgButton1 = 
			"In diesem Schritt berechnen wir die Gruppenordnung und die obere Schranke für die Gruppenordnung, da viele Algorithmen zur Lösung des DL-Problems auch mit einer oberen Schranke \r\n" + 
			"für die Gruppenordnung funktionieren. Das multiplikative Inverse des Erzeugers in mod G brauchen wir für die Berechnung der Babysteps in der nächsten Schritt.\r\n" +
			"Wir machen den Ansatz x = q " + uDot + " m + r, 0 <= r < m, dabei r ist der Rest und q ist Quotient der Division von x durch m.";
	
	
	public static final String msgButton2True = 
			"Der Babystep-Giantstep-Algorithmus berechnet q und r. Dafür setzen wir x ein. "+ uAlpha + " = " + uGamma + "^ x = " + uGamma + "^(q " + uDot + " m + r) => " + uAlpha + " " + uDot + " " + uGamma + "^-r = (" + uGamma + "^m)^q.\r\n" +
			"Wir berechnen zuerst die Menge der Babysteps B={(" + uAlpha + " " + uDot + " " + uGamma + "^-r, r): 0 <= r < m}. Falls wir ein Paar (1,r) finden, so können wir x = r setzen, und haben damit das DL-Problem gelöst.";
			
	
	public static final String msgButton2False = 
			"Der Babystep-Giantstep-Algorithmus berechnet q und r. Dafür setzen wir x ein. " + uAlpha + " = " + uGamma + " ^ x = " + uGamma + "^(q " + uDot + " m + r) => " + uAlpha + " " + uDot + " " + uGamma + " ^-r = ( " + uGamma + " ^m)^q.\r\n" +
			"Wir berechnen zuerst die Menge der Babysteps B={(" + uAlpha + " " + uDot + " " + uGamma + "^-r, r): 0 <= r < m}.";
	
	
	public static final String msgButton3 = 
			"In diesem Schritt berechnen wir die Giantsteps, in dem wir " + uDelta + " = " + uGamma + " ^m setzen und prüfen, ob für q = 1, 2, 3,... das Gruppenelement " + uDelta + "^q als erste Komponente eines Elementes\r\n" +
			"von B vorkommt, ob also ein Paar (" + uDelta + "^q, r) zu B gehört. Sobald dies der Fall ist, gilt " + uAlpha + " " + uDot + " " + uGamma + "^-r = " + uDelta + "^q = " + uGamma + "^(q " + uDot + " m).";
	
	
	public static final String msgButton4True = 
			"Somit haben wir den diskreten Logarithmus x = q " + uDot + " m + r gefunden.";
	
	public static final String msgButton4OnlyBs = 
			"Somit haben wir den diskreten Logatihmus x = r gefunden.";
	
	public static final String msgButton4False = 
			"Für die eingegebenen Parametern konnte keine Lösung des diskreten Logarithmus gefunden werden.";
	
	
	public static final String msgStep1 = "Step 1 - Enter the parameters";
	public static final String msgStep2 = "Step 2 - Calculate the group oder and the ceiling(sqrt(Ord(G))):";
	public static final String msgStep3 = "Step 3 - Calculate the babysteps";
	public static final String msgStep4 = "Step 4 - Calculate the giantsteps";
	
	public static final String msgEnterACyclicGroup = "Enter a cyclic group (G):";
	public static final String msgEnterAGenerator = "Enter a generator (" + uGamma +"):";
	public static final String msgEnterEGroupelement = "Enter a group element (" + uAlpha +"):";
	public static final String msgCalculateTheGrouporder = "Calculate the group order:";
	public static final String msgCalculateTheCeiling = "Calculate the ceiling of sqrt(Ord(G)):";
	public static final String msgCalculateTheInverse = "Calculate the Inverse:";
	
	public static final String msgResultNoGiantsteps = "no need to compute Giantsteps";
	public static final String msgResultNoSolution = "There is no solution for the given parameters";
	

	/*
	 * buttons
	 */
	public static final String btnStep2 = "Continue to Step 2";
	public static final String btnStep3 = "Continue to Step 3";
	public static final String btnStep4 = "Continue to Step 4";
	public static final String btnClear = "Clear the tables";
	public static final String btnResult = "Compute Result";
	
	/*
	 * Parameter class
	 */
	public static final String paraMsg = "Bitte nur Werte im Integer Bereich eingeben. Des Weiteren darf das Gruppenelement kein Vielfaches der zyklischen Gruppe sein.";
	

}
