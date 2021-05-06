// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/

/**
 * Displays a key pair, and offers two methods to control whether this
 * should be labeled the "public" or the "private" part.
 *
 * @author Simon L
 */
public class NewKeyPairComposite extends NewKeyComposite {

	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/
	protected void checkSubclass() {
	}

	/**
	 * Creates a NewKeyPairComposite, with the key pair that is represented by it's public part
	 *
	 * @param parent the parent control
	 * @param publicKeyAlias the public key alias of the key pair
	 */
	public NewKeyPairComposite(org.eclipse.swt.widgets.Composite parent, KeyStoreAlias publicKeyAlias) {
		super(parent, publicKeyAlias);
	}

	public void setPublicMode() {
		setInfoLabelText(Messages.getString("NewKeyComposite.pkeypart")); //$NON-NLS-1$
	}

	public void setPrivateMode() {
		setInfoLabelText(Messages.getString("NewKeyComposite.privkeypart")); //$NON-NLS-1$
	}

	@Override
	protected String getKeyLabel() {
		return Messages.getString("NewKeyComposite.keypair"); //$NON-NLS-1$
	}

	@Override
	protected void removeKeyFromKeystore() {
		KeyStoreManager.getInstance().deleteEntry(KeyStoreManager.getInstance().getPrivateForPublic(this.getPublicKeyAlias()));
		getRemoveObserver().notifyObservers(null);
	}

	@Override
	protected ImageDescriptor getKeyImageDescriptor() {
		return ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_key2.png");
	}

}
