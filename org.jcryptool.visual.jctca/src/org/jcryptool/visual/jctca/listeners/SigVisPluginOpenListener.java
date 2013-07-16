package org.jcryptool.visual.jctca.listeners;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.notifiers.SignatureNotifier;
import org.jcryptool.visual.sig.algorithm.Input;
import org.jcryptool.visual.sig.listener.SignatureListener;

/**
 * Listener on the sign part in the user view. also accesses the SigVis plugin for signing
 * 
 * @author mmacala
 * 
 */
public class SigVisPluginOpenListener implements SelectionListener {
    Button btn_check;
    Label lbl_file;
    Text txt_sign;
    Combo cmb_keys;

    public SigVisPluginOpenListener(Button btn_check, Label lbl_file, Text txt_sign, Combo cmb_keys) {
        this.btn_check = btn_check;
        this.lbl_file = lbl_file;
        this.txt_sign = txt_sign;
        this.cmb_keys = cmb_keys;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        String selected = cmb_keys.getText();
        String key_hash = selected.split("Hash:")[1]; //$NON-NLS-1$
        key_hash = key_hash.split(" ")[0]; //$NON-NLS-1$
        KeyStoreAlias pubAlias = (KeyStoreAlias) cmb_keys.getData(cmb_keys.getText());
        KeyStoreAlias privAlias = KeyStoreManager.getInstance().getPrivateForPublic(pubAlias);
        org.jcryptool.visual.sig.algorithm.Input.publicKey = pubAlias;
        org.jcryptool.visual.sig.algorithm.Input.privateKey = privAlias;
        org.jcryptool.visual.sig.algorithm.Input.data = (lbl_file.getText() != "" ? lbl_file //$NON-NLS-1$
                .getText() : txt_sign.getText()).getBytes();
        org.jcryptool.visual.sig.algorithm.Input.path = lbl_file.getText();
        ArrayList<SignatureListener> lsts = org.jcryptool.visual.sig.listener.SignatureListenerAdder.getListeners();
        if (lsts == null || lsts.isEmpty()) {
            org.jcryptool.visual.sig.listener.SignatureListenerAdder.addSignatureListener(new SignatureNotifier());
        }
        if (btn_check.getSelection() == true) {
            IViewReference ref = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .findViewReference("org.jcryptool.visual.sig.view");
            if (ref != null) {
                Util.showMessageBox("Signaturerzeugung schließen",
                        "Sie müssen zuerst die Signaturerzeugung schließen bevor sie fortfahren können.",
                        SWT.ICON_INFORMATION);
            } else {
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView("org.jcryptool.visual.sig.view"); //$NON-NLS-1$
                } catch (PartInitException e1) {
                    LogUtil.logError(e1);
                }
            }
        } else {
            try {
                org.jcryptool.visual.sig.algorithm.Input.chosenHash = "SHA256"; //$NON-NLS-1$
                org.jcryptool.visual.sig.algorithm.SigGeneration.signInput("SHA256withRSA", Input.data); //$NON-NLS-1$
            } catch (Exception e1) {
                LogUtil.logError(e1);
            }
            Util.showMessageBox(Messages.SigVisPluginOpenListener_msgbox_title_success,
                    Messages.SigVisPluginOpenListener_msgbox_text_signed_msg_was_sent, SWT.ICON_INFORMATION);
            this.lbl_file.setText(""); //$NON-NLS-1$
            this.txt_sign.setText(Messages.SigVisPluginOpenListener_enter_text_to_sign);
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
}