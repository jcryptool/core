package org.jcryptool.core.introduction;

import java.util.Optional;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.views.AlgorithmView;

public class IntroPageStartup implements IStartup {

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub

		IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
		IWorkbenchWindow wbwin = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		wbwin.getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (getPreferenceString("WELCOMEPAGE_AT_STARTUP").orElse("yes").equals("yes")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					introManager.showIntro(wbwin, false);
					introManager.getIntro().getIntroSite().getPage().addPartListener(new IPartListener2() {
						public void partHidden(org.eclipse.ui.IWorkbenchPartReference partRef) {
							if (partRef.getId().equals("org.eclipse.ui.internal.introview")) { //$NON-NLS-1$
								setPreferenceString("WELCOMEPAGE_AT_STARTUP", "no"); //$NON-NLS-1$ //$NON-NLS-2$
							}
						};
					});
				}
			}
		});
		
		wbwin.getActivePage().addPartListener(new IPartListener2() {
			@Override
			public void partActivated(IWorkbenchPartReference partRef) {

				String id = "org.jcryptool.core.views.AlgorithmView"; //$NON-NLS-1$
				
				if (partRef.getId().equals(id)) {
					int times_shown = getPreferenceString("HAS_SEEN_CRYPTOEXPLORER_HINT").map(s -> Integer.parseInt(s)).orElse(0);
					if (times_shown < 3) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setPreferenceString("HAS_SEEN_CRYPTOEXPLORER_HINT", ""+(times_shown+1)); //$NON-NLS-1$ //$NON-NLS-2$
						AlgorithmView view = AlgorithmView.instance;
						if (view != null) {
							view.showBubble(Messages.IntroPageStartup_0);
						}
					}
				}
			}
		});
	}
	
	private Optional<String> getPreferenceString(String id) {
		IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, CorePlugin.PLUGIN_ID);
		String string = prefs.getString(id);
		return string.length() == 0 ? Optional.empty() : Optional.of(string);
	}
	
	private void setPreferenceString(String id, String value) {
		IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, CorePlugin.PLUGIN_ID);
		prefs.setValue(id, value); //$NON-NLS-1$
	}


}
