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
import org.jcryptool.visual.he.algo.FHEParams;
import org.jcryptool.visual.he.algo.GHKeyPair;
import org.jcryptool.visual.he.ui.FileCrypto;

import de.flexiprovider.api.Cipher;

/**
 * Wizard to choose whether to generate a new key or to enter an existing one for Gentry & Halevi
 * fully homomorphic visualization
 *
 * @author Coen Ramaekers
 *
 */
public class GHKeySelectionWizard extends Wizard {
    private final GHKeyPair keyPair;
    private final FHEParams fheParams;
    private final Display display;

    /** wizard title, displayed in the titlebar. */
    private static final String TITLE = Messages.KeySelectionWizard_Title;

    public GHKeySelectionWizard(GHKeyPair keyPair, FHEParams fheParams, Display display) {
        this.keyPair = keyPair;
        this.fheParams = fheParams;
        this.display = display;
        this.setWindowTitle(TITLE);
    }

    @Override
    public final void addPages() {
        addPage(new GHChooseKeySelGenPage());
        addPage(new GHNewKeyPage(keyPair, fheParams, display));
        // addPage(new GHSaveKeyPage(keyPair));
        addPage(new GHLoadKeyPage(display.getActiveShell()));
    }

    @Override
    public final boolean canFinish() {
        return (getPage(GHNewKeyPage.getPagename()).isPageComplete())
                || (getPage(GHLoadKeyPage.getPagename()).isPageComplete());
    }

    @Override
    public boolean performFinish() {
        if (((GHNewKeyPage) getPage(GHNewKeyPage.getPagename())).getSave()) {
            FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
            dialog.setFilterPath(DirectoryService.getUserHomeDir());
            dialog.setFilterExtensions(new String[] {"*.ghpub"});
            dialog.setOverwrite(true);

            String filename = dialog.open();

            try {
                /** first the encrypted file contents are entered */
                FileOutputStream out = new FileOutputStream(filename);
                out.write(("Owner%" + keyPair.getContactName() + "%").getBytes());
                out.write(("Dimension%" + fheParams.logn + "%").getBytes());
                out.write(("Det%" + keyPair.det.toString() + "%").getBytes());
                out.write(("Root%" + keyPair.root.toString() + "%").getBytes());
                out.write(("W%" + keyPair.w.toString() + "%").getBytes());
                out.write("END".getBytes());
                new FileCrypto(filename, filename.replace(".ghpub", ".ghpr"), keyPair.getPassword(),
                        Cipher.ENCRYPT_MODE);

                /** now the public file contents are entered */
                out = new FileOutputStream(filename);
                out.write(("Owner%" + keyPair.getContactName() + "%").getBytes());
                out.write(("Type%Gentry-Halevi Key Pair%").getBytes());
                out.write(("Dimension%" + fheParams.logn + "%").getBytes());
                out.write(("Det%" + keyPair.det.toString() + "%").getBytes());
                out.write(("Root%" + keyPair.root.toString() + "%").getBytes());
                out.write("C%".getBytes());
                for (int i = 0; i < keyPair.ctxts.length; i++)
                    out.write((Integer.toString(i) + "%" + keyPair.ctxts[i].toString() + "%").getBytes());
                out.write("Blocks%".getBytes());
                for (int i = 0; i < keyPair.pkBlocksX.length; i++)
                    out.write((Integer.toString(i) + "%" + keyPair.pkBlocksX[i].toString() + "%").getBytes());
                out.write("END".getBytes());
            } catch (Exception ex) {
                LogUtil.logError(ex);
            }
        }
        if (getPage(GHLoadKeyPage.getPagename()).isPageComplete()) {
            String filename = ((GHLoadKeyPage) getPage(GHLoadKeyPage.getPagename())).getFileName();
            String passwd = ((GHLoadKeyPage) getPage(GHLoadKeyPage.getPagename())).getPassword();
            try {
                /** extract the public key */
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
                int logDim = 0;
                BigInteger det = BigInteger.ONE;
                BigInteger root = BigInteger.ONE;
                BigInteger[] ctxts = new BigInteger[] {BigInteger.ONE};
                int ctxtsStart = 0, ctxtsEnd = 0, pkBlocksStart = 0, pkBlocksEnd = 0;
                BigInteger[] pkBlocksX = new BigInteger[] {BigInteger.ONE};
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i].equals("Dimension"))
                        logDim = Integer.parseInt(contents[i + 1]);
                    if (contents[i].equals("Det"))
                        det = new BigInteger(contents[i + 1]);
                    if (contents[i].equals("Root"))
                        root = new BigInteger(contents[i + 1]);
                    if (contents[i].equals("C"))
                        ctxtsStart = i + 1;
                    if (contents[i].equals("Blocks")) {
                        ctxtsEnd = i - 1;
                        pkBlocksStart = i + 1;
                    }
                    if (contents[i].equals("END"))
                        pkBlocksEnd = i - 1;
                }
                if (ctxtsEnd != 0) {
                    if ((ctxtsEnd - ctxtsStart) / 2 != 0) {
                        ctxts = new BigInteger[(ctxtsEnd + 1 - ctxtsStart) / 2];
                        int idx = 0;
                        for (int i = ctxtsStart + 1; i <= ctxtsEnd; i += 2) {
                            ctxts[idx] = new BigInteger(contents[i]);
                            idx++;
                        }
                    }
                    if ((pkBlocksEnd - pkBlocksStart) / 2 != 0) {
                        pkBlocksX = new BigInteger[(pkBlocksEnd + 1 - pkBlocksStart) / 2];
                        int idx = 0;
                        for (int i = pkBlocksStart + 1; i <= pkBlocksEnd; i += 2) {
                            pkBlocksX[idx] = new BigInteger(contents[i]);
                            idx++;
                        }
                    }
                    fheParams.setPrms(72, 384, logDim);
                } else {
                    return false;
                }

                /** extract the private key */

                new FileCrypto(filename.replace(".ghpub", ".ghpr"), filename.replace(".ghpub", ".tmp"), passwd, Cipher.DECRYPT_MODE);
                fileData = new StringBuffer(1000);
                reader = new BufferedReader(new FileReader(filename.replace(".ghpub", ".tmp")));
                buf = new char[1024];
                numRead = 0;
                while ((numRead = reader.read(buf)) != -1) {
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                    buf = new char[1024];
                }
                reader.close();
                contents = fileData.toString().split("%");
                BigInteger w = BigInteger.ONE;
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i].equals("W"))
                        w = new BigInteger(contents[i + 1]);
                }
                new File(filename.replace(".ghpub", ".tmp")).delete();
                if (w.compareTo(BigInteger.ONE) != 0) {
                    keyPair.setKeyPair(det, root, w, ctxts, pkBlocksX);
                } else {
                    ((GHLoadKeyPage) getPage(GHLoadKeyPage.getPagename())).setErrorMessage(Messages.Wrong_Password);
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.logError(ex);
            }
        }
        return true;
    }
}
