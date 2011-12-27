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
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.FHEParams;
import org.jcryptool.visual.he.algo.GHData;
import org.jcryptool.visual.he.algo.GHKeyPair;
import org.jcryptool.visual.he.ui.FileCrypto;
import org.jcryptool.visual.he.wizards.pages.GHChooseKeySelGenPage;
import org.jcryptool.visual.he.wizards.pages.GHLoadKeyPage;
import org.jcryptool.visual.he.wizards.pages.GHSettingsPage;

import de.flexiprovider.api.Cipher;

/**
 * Wizard to change settings for Gentry & Halevi fully homomorphic visualization
 *
 * @author Coen Ramaekers
 *
 */
public class GHSettingsWizard extends Wizard {
    private final GHData data;

    /** wizard title, displayed in the titlebar. */
    private static final String TITLE = Messages.SettingsWizard_Title;

    public GHSettingsWizard(GHData data) {
        this.data = data;
        this.setWindowTitle(TITLE);
    }

    @Override
    public final void addPages() {
        addPage(new GHSettingsPage(data));
    }

    @Override
    public final boolean canFinish() {
        return getPage(GHSettingsPage.getPagename()).isPageComplete();
    }

    @Override
    public boolean performFinish() {
        return true;
    }
}
