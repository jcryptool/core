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
package org.jcryptool.visual.viterbi.algorithm;

/**
 *
 * This interface is used for interaction with the GUI. It allows the viterbi
 * thread, to send status information while still working.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */

public interface ViterbiObserver {

	/**
	 * The viterbi algorithm provides the path with the best probability for
	 * each iteration.
	 *
	 * This path can be used to display the progress. As this is not the final
	 * solution, it should not be used for further calculations.
	 */

	public void update(Path path);

	/**
	 * When the viterbi thread is finished, it will call this method.
	 */

	public void viterbiFinished();
}
