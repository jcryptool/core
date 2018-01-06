/**
 * 
 */
package org.jcryptool.visual.elGamal.ui.wizards;

import org.jcryptool.crypto.ui.textblockloader.TextAsNumbersLoaderWizard;
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;

/**
 * @author Thorben Groos
 *
 */
public class NewTextEntryWizard extends TextAsNumbersLoaderWizard {

	public NewTextEntryWizard(ElGamalData data) {
		
		//FIXME Die maximale Blocklänge (Konstruktor super 1. Argument) wird nicht von A, sondern von der Größe
		//der Gruppe p begrenzt. Maximale Blocklänge ist p-1 (ensprechend umgerecht auf die andere Basis (256 bei ASCII))
		super(data.getModulus().intValue(), ((data.getAction() == Action.DecryptAction) || (data.getAction() == Action.VerifyAction)));

	}

}
