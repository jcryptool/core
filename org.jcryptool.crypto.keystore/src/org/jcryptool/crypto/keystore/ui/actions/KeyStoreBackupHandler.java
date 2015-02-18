package org.jcryptool.crypto.keystore.ui.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

public class KeyStoreBackupHandler extends AbstractHandler {

	public KeyStoreBackupHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
			String pathToFile = dlg.open();
			if(pathToFile != null)
				KeyStoreManager.getInstance().backupKeystore(pathToFile);
		} catch(Exception ex) {
			LogUtil.logError(KeyStorePlugin.PLUGIN_ID, ex);
		}
		return null;
	}

}
