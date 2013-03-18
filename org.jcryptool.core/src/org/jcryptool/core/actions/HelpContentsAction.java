package org.jcryptool.core.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IToc;
import org.eclipse.help.ITopic;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/*
 * @see IWorkbenchWindowActionDelegate
 */
public class HelpContentsAction implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public HelpContentsAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				IToc[] tocs = HelpSystem.getTocs();
				boolean foundTopic = false;
				String contextId = null;
				
				contextId = findContextId();
				
				if(contextId != null)
				{
					ITopic topic = findTopic(contextId.split("" + IPath.SEPARATOR)[0], tocs);
					if(topic != null)
					{
						foundTopic = true;
						PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(topic.getHref());
					}
				}
				if(!foundTopic)
					PlatformUI.getWorkbench().getHelpSystem().displayHelp();
			}

			private String findContextId() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if(window != null)
				{
					IWorkbenchPage page = window.getActivePage();
					IWorkbenchPartReference ref;
					if(page != null)
					{
						ref = page.getActivePartReference();
						if(ref != null)
							return ref.getId();
					}
				}
				return null;
			}

			private ITopic findTopic(String id, IToc[] tocs) {
				for (IToc toc : tocs) {
					ITopic topic = findTopic(id, toc.getTopics());
					if(topic != null)
						return topic;
				}
				return null;
			}

			private ITopic findTopic(String id, ITopic[] topics) {
				for (ITopic topic : topics) {
					if(isTopic(id, topic))
						return topic;
					ITopic sub = findTopic(id, topic.getSubtopics());
					if(sub != null)
						return sub;
				}
				return null;
			}

			private boolean isTopic(String id, ITopic topic) {
				String idStart = topic.getHref().split("" + IPath.SEPARATOR)[1].toLowerCase();
				return id.toLowerCase().startsWith(idStart);
			}
		});

	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}