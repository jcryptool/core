package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.GHData;
import org.jcryptool.visual.he.wizards.pages.GHSettingsPage;

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
