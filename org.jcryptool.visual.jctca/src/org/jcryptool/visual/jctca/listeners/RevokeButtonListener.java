/**
 * 
 */
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

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

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Event triggered");
		Button btn_revoke = (Button) e.getSource();
		KeyStoreAlias ksAlias = (KeyStoreAlias) btn_revoke.getData("selected");
		System.out.println(ksAlias.toString());
	}

}
