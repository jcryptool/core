package org.jcryptool.visual.jctca;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateViews.Messages;

public class Util {
	
	
	public static void createRootNodes(Tree tree){
		TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
		tree_item_csr.setText(Messages.ShowReq_CertReqs);
		
		TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
		tree_item_crl.setText(Messages.ShowReq_RevReqs);

		tree.getItems()[0].setExpanded(true);
		tree.getItems()[1].setExpanded(true);
	}
	/**	
	 * Find all RSA and DSA public keys in a given keystore ksm and return them
	 * in an array of well formatted strings
	 * 
	 * @param ksm
	 *            - KeyStoreManager from where to get the keys
	 * @return ArrayList of strings well formatted for use with the JCT-CA
	 *         visual
	 */
	public static ArrayList<String> getAllRSAAndDSAPublicKeys(
			KeyStoreManager ksm) {
		ArrayList<String> RSAAndDSAPublicKeys = new ArrayList<String>();
		for (KeyStoreAlias ksAlias : ksm.getAllPublicKeys()) {
			if (ksAlias.getOperation().contains("RSA")) {
				String KeyListEntry = ksAlias.getContactName() + " ("
						+ ksAlias.getKeyLength() + "bit RSA)";
				RSAAndDSAPublicKeys.add(KeyListEntry);
			} else if (ksAlias.getOperation().contains("DSA")) {
				String KeyListEntry = ksAlias.getContactName() + " ("
						+ ksAlias.getKeyLength() + "bit DSA)";
				RSAAndDSAPublicKeys.add(KeyListEntry);
			}
		}
		return RSAAndDSAPublicKeys;
	}

	public static void showMessageBox(String title, String text, int type) {
		MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(),
				type);
		box.setText(title);
		box.setMessage(text);
		box.open();
	}

}
