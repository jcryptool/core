// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IToc;
import org.eclipse.help.ITopic;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This command loads the help page of the currently active plug-in (its view). The corresponding
 * help page is identified by the plug-in-id and the topic id. A plug-in developer only needs to
 * provide help for the plug-in, there are no other requirements. JCrypTool takes care of the rest.
 *
 * @author Dominik Schadow
 */
public class ShowHelpContents extends AbstractHandler {
    /**
     * The action has been activated. The argument of the method represents the 'real' action
     * sitting in the workbench UI.
     *
     * @see IWorkbenchWindowActionDelegate#run
     */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
			public void run() {
            	String pluginId = findContextId();
            	System.out.println(pluginId);
            	// look up whether there is a help resource reference registered explicitely for the current plugin id
            	// if so, short-circuit and just open it
            	if (pluginId != null) {
					String registeredHref = HelpHrefRegistry.getInstance().getHrefFor(findContextId());
					if (registeredHref != null) {
						PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(registeredHref);
						return;
					}
				}
            	
                IToc[] tocs = HelpSystem.getTocs();
                boolean foundTopic = false;
                String contextId = findContextId();

                if (contextId != null) {
                    ITopic topic = findTopic(contextId.split("" + IPath.SEPARATOR)[0], tocs);
                    if (topic != null) {
                        foundTopic = true;
                        System.out.println(topic.getHref());
                        PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(topic.getHref());
                    }
                }
                if (!foundTopic) {
                    PlatformUI.getWorkbench().getHelpSystem().displayHelp();
                }
            }

            private String findContextId() {
                IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
                if (page != null) {
                    IWorkbenchPart activePart = page.getActivePart();

                    if (activePart != null) {
                        return activePart.getSite().getPluginId();
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

        return null;
    }
}