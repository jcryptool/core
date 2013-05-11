/**
 * 
 */
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.jctca.UserViews.dialogs.RevokeCertDialog;

/**
 * @author sho
 *
 */
public class RevokeButtonListener implements SelectionListener {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
		Button btn_revoke = (Button) e.getSource();
		KeyStoreAlias ksAlias = (KeyStoreAlias) btn_revoke.getData("selected");
		//tell the button to do nothing if no cert was selected
		if (ksAlias == null){
			return;
		}
		Shell shell = new Shell();
		RevokeCertDialog dialog = new RevokeCertDialog(shell);
		dialog.open();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
