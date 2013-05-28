package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

/**
 * ???
 * @author mmacala
 *
 */
public class PubKeyListener implements SelectionListener {
Combo cmb_genKey;
	
	public PubKeyListener(Combo cmb_genKey) {
		this.cmb_genKey = cmb_genKey;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		cmb_genKey.setEnabled(true);

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
