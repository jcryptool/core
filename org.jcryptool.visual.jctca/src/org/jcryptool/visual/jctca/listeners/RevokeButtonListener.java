/**
 * 
 */
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RR;
import org.jcryptool.visual.jctca.UserViews.dialogs.RevokeCertDialog;

/**
 * The listener on the revoke button in the "Manage Certificates" view. Shows a dialog where the user is asked to state
 * a reason for the revocation. afterwards it is forwarded to the RA.
 * 
 * @author sho
 */
public class RevokeButtonListener implements SelectionListener {

    /*
     * (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent e) {
        Button btn_revoke = (Button) e.getSource();
        KeyStoreAlias ksAlias = (KeyStoreAlias) btn_revoke.getData("selected"); //$NON-NLS-1$
        // tell the button to do nothing if no cert was selected
        if (ksAlias == null) {
            return;
        }
        Shell shell = new Shell();
        RevokeCertDialog dialog = new RevokeCertDialog(shell);
        dialog.open();
        if (dialog.getReturnCode() == Dialog.OK) {
            RR rr = new RR(ksAlias, dialog.getReason());
            CertificateCSRR.getInstance().addRR(rr);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

    }

}
