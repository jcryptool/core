package org.jcryptool.crypto.keystore.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

/**
 * A Handler to open the keystore from the icon in the toolbar. Opens the same view as 
 * Window -> Show View -> Other ... ->Cryptography -> Keystore.
 * @author Thorben Groos
 *
 */
public class OpenKeystoreHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.showView("org.jcryptool.crypto.keystore.KeystoreView");
		} catch (PartInitException e) {
			LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
		}
		
		
		return null;
	}

}
