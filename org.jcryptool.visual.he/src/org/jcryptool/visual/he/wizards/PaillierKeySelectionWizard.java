// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigInteger;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.PaillierData;
import org.jcryptool.visual.he.ui.FileCrypto;

import de.flexiprovider.api.Cipher;

/**
 * Wizard to choose whether to generate a new key or to enter an existing one for Paillier
 * homomorphic visualization
 *
 * @author Coen Ramaekers
 *
 */
public class PaillierKeySelectionWizard extends Wizard {
    private final PaillierData data;
    private final Display display;

    /** wizard title, displayed in the titlebar. */
    private static final String TITLE = Messages.KeySelectionWizard_Title;

    public PaillierKeySelectionWizard(PaillierData data, Display display) {
        this.data = data;
        this.display = display;
        this.setWindowTitle(TITLE);
    }

    @Override
    public final void addPages() {
        addPage(new PaillierChooseKeySelGenPage());
        addPage(new PaillierNewKeyPage(data, display));
        // addPage(new PaillierSaveKeyPage(data));
        addPage(new PaillierLoadKeyPage(display.getActiveShell()));
    }

    @Override
    public final boolean canFinish() {
        return (getPage(PaillierNewKeyPage.getPagename()).isPageComplete())
                || (getPage(PaillierLoadKeyPage.getPagename()).isPageComplete());
    }

    @Override
    public boolean performFinish() {
        if (((PaillierNewKeyPage) getPage(PaillierNewKeyPage.getPagename())).getSave()) {
            FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
            dialog.setFilterPath(DirectoryService.getUserHomeDir());
            dialog.setFilterExtensions(new String[] {"*.papub"});
            dialog.setOverwrite(true);

            String filename = dialog.open();

            try {
                /** first the encrypted private key */
                FileOutputStream out = new FileOutputStream(filename);
                out.write(("Owner%" + data.getContactName() + "%").getBytes());
                out.write(("l%" + data.getPrivKey()[0].toString() + "%").getBytes());
                out.write(("mu%" + data.getPrivKey()[1].toString() + "%").getBytes());
                out.write("END".getBytes());
                new FileCrypto(filename, filename.replace(".papub", ".papr"), data.getPassword(),
                        Cipher.ENCRYPT_MODE);
                /** then the public key */
                out = new FileOutputStream(filename);
                out.write(("Owner%" + data.getContactName() + "%").getBytes());
                out.write(("Type%Paillier Key Pair%").getBytes());
                out.write(("n%" + data.getPubKey()[0].toString() + "%").getBytes());
                out.write(("g%" + data.getPubKey()[1].toString() + "%").getBytes());
            } catch (Exception ex) {
                LogUtil.logError(ex);
            }
        }
        if (getPage(PaillierLoadKeyPage.getPagename()).isPageComplete()) {
            String filename =
                    ((PaillierLoadKeyPage) getPage(PaillierLoadKeyPage.getPagename())).getFileName();
            String passwd =
                    ((PaillierLoadKeyPage) getPage(PaillierLoadKeyPage.getPagename())).getPassword();
            try {
                /** first the public key */
                StringBuffer fileData = new StringBuffer(1000);
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                char[] buf = new char[1024];
                int numRead = 0;
                while ((numRead = reader.read(buf)) != -1) {
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                    buf = new char[1024];
                }
                reader.close();
                String[] contents = fileData.toString().split("%");
                BigInteger n = BigInteger.ONE;
                BigInteger g = BigInteger.ONE;
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i].equals("n"))
                        n = new BigInteger(contents[i + 1]);
                    if (contents[i].equals("g"))
                        g = new BigInteger(contents[i + 1]);
                }

                if (n.compareTo(BigInteger.ONE) != 0) {
                    data.setPubKey(n, g);
                } else {
                    ((PaillierLoadKeyPage) getPage(PaillierLoadKeyPage.getPagename())).setErrorMessage(Messages.Wrong_Password);
                    return false;
                }

                new FileCrypto(filename.replace(".papub", ".papr"), filename.replace(".papub", ".tmp"), passwd, Cipher.DECRYPT_MODE);
                fileData = new StringBuffer(1000);
                reader = new BufferedReader(new FileReader(filename.replace(".papub", ".tmp")));
                buf = new char[1024];
                numRead = 0;
                while ((numRead = reader.read(buf)) != -1) {
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                    buf = new char[1024];
                }
                reader.close();
                contents = fileData.toString().split("%");
                BigInteger l = BigInteger.ONE;
                BigInteger mu = BigInteger.ONE;
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i].equals("l"))
                        l = new BigInteger(contents[i + 1]);
                    if (contents[i].equals("mu"))
                        mu = new BigInteger(contents[i + 1]);
                }

                if (l.compareTo(BigInteger.ONE) != 0) {
                    data.setPrivKey(l, mu);
                    new File(filename.replace(".papub", ".tmp")).delete();
                } else {
                    ((PaillierLoadKeyPage) getPage(PaillierLoadKeyPage.getPagename())).setErrorMessage(Messages.Wrong_Password);
                    new File(filename.replace(".papub", ".tmp")).delete();
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.logError(ex);
            }

        }
        return true;
    }
}
