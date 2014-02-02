// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IToc;
import org.eclipse.help.ITopic;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * This action loads the help page of the currently active plug-in (its view). The corresponding
 * help page is identified by the plug-in-id and the topic id. A plug-in developer only needs to
 * provide help for the plug-in, there are no other requirements. JCrypTool takes care of the rest.
 * 
 * @author Dominik Schadow
 */
public class HelpContentsAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    /**
     * The action has been activated. The argument of the method represents the 'real' action
     * sitting in the workbench UI.
     * 
     * @see IWorkbenchWindowActionDelegate#run
     */
    public void run(IAction action) {
        BusyIndicator.showWhile(null, new Runnable() {
            public void run() {
                IToc[] tocs = HelpSystem.getTocs();
                boolean foundTopic = false;
                String contextId = findContextId();

                if (contextId != null) {
                    ITopic topic = findTopic(contextId.split("" + IPath.SEPARATOR)[0], tocs);
                    if (topic != null) {
                        foundTopic = true;
                        PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(topic.getHref());
                    }
                }
                if (!foundTopic) {
                    PlatformUI.getWorkbench().getHelpSystem().displayHelp();
                }
            }

            private String findContextId() {
                if (window != null) {
                    IWorkbenchPage page = window.getActivePage();
                    if (page != null) {
                        IWorkbenchPart activePart = page.getActivePart();

                        if (activePart != null) {
                            return activePart.getSite().getPluginId();
                        }
                    }
                }

                return null;
            }

            private ITopic findTopic(String id, IToc[] tocs) {
                for (IToc toc : tocs) {
                    ITopic topic = findTopic(id, toc.getTopics());
                    if (topic != null) {
                        return topic;
                    }
                }
                return null;
            }

            private ITopic findTopic(String id, ITopic[] topics) {
                for (ITopic topic : topics) {
                    if (isTopic(id, topic)) {
                        return topic;
                    }
                    ITopic sub = findTopic(id, topic.getSubtopics());
                    if (sub != null) {
                        return sub;
                    }
                }
                return null;
            }

            private boolean isTopic(String id, ITopic topic) {
                String href = topic.getHref();

                if (href == null || href.isEmpty()) {
                    return false;
                }

                return id.equalsIgnoreCase(href.split("" + IPath.SEPARATOR)[1]);
            }
        });
    }

    /**
     * Selection in the workbench has been changed. We can change the state of the 'real' action
     * here if we want, but this can only happen after the delegate has been created.
     * 
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * We can use this method to dispose of any system resources we previously allocated.
     * 
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    public void dispose() {
    }

    /**
     * We will cache window object in order to be able to provide parent shell for the message
     * dialog.
     * 
     * @see IWorkbenchWindowActionDelegate#init
     */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}