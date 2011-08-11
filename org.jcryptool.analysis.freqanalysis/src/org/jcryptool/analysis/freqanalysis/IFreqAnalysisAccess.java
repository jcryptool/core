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
package org.jcryptool.analysis.freqanalysis;

/**
 * @author SLeischnig
 *
 */
public interface IFreqAnalysisAccess {

	/** The remote control procedure - control this plugin by implementing this Interface on the frequency analysis view.
	 * @param simpleView whether the simple or the extended view is shown (true - simple, false - extended)
	 * @param keyLength sets the key length
	 * @param keyPos sets the offset
	 * @param overlayIndex sets the selection index of the reference overlay text combo box. simpleView=false only!
	 * @param resetShift resets the dragging of the graph
	 * @param executeCalc whether a calculation has now to be executed
	 * @param whichTab selects, which tab has to be shown. simpleView=false only!
	 * @param activateOverlay activates the overlay. simpleView=false only!
	 */
	public void execute(boolean simpleView, int keyLength, int keyPos, int overlayIndex, boolean resetShift, boolean executeCalc, boolean firstTab, boolean activateOverlay);

}
