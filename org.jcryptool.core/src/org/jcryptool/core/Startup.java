package org.jcryptool.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Here you can do things, before the JCT starts.</br>
 * At the moment it only registers a PerspectiveChangedListener
 * that takes care of opening and closing the algorithm-instruction-view.</br>
 * This is necessary, because the introduction is displayed in the middle
 * of the JCT, which is the "editor-area". Eclipse does not want to
 * open views in the editor area. So I have to work around this.</br></br>
 * 
 * Feel free to add code to this class.
 * 
 * @author Thorben Groos
 *
 */
public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		// Da auf den GUI Thread zugegriffen wird mus der
		// Code mit getDisplay().asyncExec() ausgeführt 
		// werden.
		 workbench.getDisplay().asyncExec(new Runnable() {
		   public void run() {
		     IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		     if (window != null) {
		       // do something
		    	 window.addPerspectiveListener(new IPerspectiveListener() {
					
					@Override
					public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
						
					}
					
					@Override
					public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {

						// Lädt alle geöffneten Views und prüft, ob eine Introduction view dabei ist.
						Stream<IViewReference> myStream = Arrays.stream(page.getViewReferences());
						List<IViewReference> introsInEditorArea = myStream.filter(e -> e.getId().equals("org.jcryptool.core.introduction.views.AlgorithmInstruction"))
								.collect(Collectors.toList());
						
						// This counts how many instances of the Introduction view are already opened.
						// This will be mostly 0 (no Introduction view is openend) or 1 (one Introduction
						// view is openend.
						long countOpenIntros = introsInEditorArea.size();
						
						// The user switches to the algorithm perspective.
						if (perspective.getId().equals("org.jcryptool.crypto.flexiprovider.ui.perspective.FlexiProviderPerspective")) {

							// No instance opened already -> go on.
							if (countOpenIntros == 0) {
								// Checks if the user has unchecked the "do not show again" checkbox.
								IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, CorePlugin.PLUGIN_ID);
								boolean show = prefs.getBoolean("DONT_SHOW_ALGORITHM_INTRODUCTION");
								if (!show) {
									try {
										// Open the Introduction view.
										window.getActivePage().showView("org.jcryptool.core.introduction.views.AlgorithmInstruction");
									} catch (PartInitException e1) {
										LogUtil.logError(CorePlugin.PLUGIN_ID, e1);
									}
								}
							}
						}
						
						// The user switches to the default position.
						if (perspective.getId().equals("org.jcryptool.core.perspective")) {
							// This closes all introductions that are opened in the 
							// editor area. This is necessary, because Eclipse does not
							// close views in the editor area when changing from one 
							// perspective to another.
							for (IViewReference ref : introsInEditorArea) {
								page.hideView(ref);
							}
						}
					}
				});
		    	 
		     }
		   }
		 });
		 
	}
	
}
