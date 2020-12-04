// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * The ApplicationWorkbenchWindowAdvisor class configures the workbench window.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private boolean maximize = false;
	
    /**
     * Creates a new workbench window advisor for configuring a workbench window via the given workbench window
     * configurer.
     *
     * @param configurer an object for configuring the workbench window
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    /**
     * Creates a new action bar advisor to configure the action bars of the window via the given action bar configurer.
     *
     * @param configurer the action bar configurer for the window
     * @return the action bar advisor for the window
     */
    @Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    @Override
    public void preWindowOpen() {
    	
    	IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    	
    	// This Code tries to get the optimal initial size of the 
    	// JCT based on the screen resolution and the display zoom
    	
    	Rectangle clientArea = null;
    	int monitorWidth = 0;
    	int zoom = 0;
    	Point prefferredSize = null;
    	try {
    		// try-catch-block just as a precaution
    		clientArea = Display.getCurrent().getPrimaryMonitor().getClientArea();
    		monitorWidth = Display.getCurrent().getPrimaryMonitor().getBounds().width;
    		zoom = Display.getCurrent().getPrimaryMonitor().getZoom();
    	} catch (Exception e) {
    		LogUtil.logError(CorePlugin.PLUGIN_ID, e);
    	}
    	
    	if (monitorWidth < 1200) {
    		// For monitors like 1024 x 768
    		// Maximize
    		maximize = true;
    	} else if (monitorWidth < 1400) {
    		// For monitors like 1280x720 or 1366x768
    		if (zoom <= 100) {
    			prefferredSize = new Point(1100, 700);
    		} else {
    			maximize = true;
    		}
    	} else if (monitorWidth < 1800) {
    		// For monitors like 1440x900 or 1600x900
    		if (zoom <= 100) {
    			prefferredSize = new Point(1100, 700);
    		} else if (zoom <= 125) {
    			prefferredSize = new Point(1350, 750);
    		} else {
    			maximize = true;
    		}
    	} else if (monitorWidth < 2600) {
    		// For monitors like 1920x1080 or 2560x1440
    		if (zoom <= 100) {
    			prefferredSize = new Point(1200, 700);
    		} else if (zoom <= 125 ) {
    			prefferredSize = new Point(1450, 825);
    		} else if (zoom <= 150 ) {
    			prefferredSize = new Point(1700, 950);
    		} else {
    			maximize = true;
    		}
    	} else {
    		// Monitors like 4k or above
    		if (zoom <= 100) {
    			prefferredSize = new Point(1250, 800);
    		} else if (zoom <= 125 ) {
    			prefferredSize = new Point(1500, 950);
    		} else if (zoom <= 150 ) {
    			prefferredSize = new Point(1750, 1100);
    		} else {
    			maximize = true;
    		}
    	}
    	
		if (clientArea != null && prefferredSize != null) {
			if (prefferredSize.x >= clientArea.width &&
    				prefferredSize.y >= clientArea.height) {
    			maximize = true;
    		} else if (prefferredSize.x > clientArea.width) {
    			//shell.setSize(new Point(clientArea.width, prefferredSize.y));
    			prefferredSize.x = clientArea.width;
    		} else if (prefferredSize.y > clientArea.height) {
    			prefferredSize.y = clientArea.height;
    		}
		}
		
    	if (maximize) {
    		// This does not work, because the shell is not creted yet.
    		// It is created in postWindowStartup.
//    		configurer.getWindow().getShell().setMaximized(true);
    		// Do nothing. The maximize event is handled in postWindowStartup
    	} else {
    		// If the JCT does not start maximized, apply the initial size.
    		configurer.setInitialSize(prefferredSize);
    	}

        configurer.setShowCoolBar(true);
        configurer.setShowMenuBar(true);
        configurer.setShowPerspectiveBar(true);
        configurer.setShowProgressIndicator(true);
        configurer.setShowStatusLine(true);
    	configurer.addEditorAreaTransfer(FileTransfer.getInstance());
    	configurer.configureEditorAreaDropListener(new EditorAreaDropTargetListener());
    	
    	//Add to the title you enter in the jcryptool.product file the Version of the JCT (that is also
    	//entered in the jcryptool.product file) and the maven build timestamp.
    	// for definition site of these maven constants, see o.j.releng/pom.xml and /org.jcryptool.core/plugin.xml
//    	configurer.setTitle(Platform.getProduct().getName()
//    			+ " "
//    			+ Platform.getProduct().getDefiningBundle().getVersion()
//    			+ " "
//    			+ Platform.getProduct().getProperty("mavenBuildTimestamp"));
//    	System.out.println(String.format("mBISR: %s||", Platform.getProduct().getProperty("mavenBuildIsStableRelease")));
    	if (Platform.getProduct().getProperty("mavenBuildIsStableRelease").equals("true")) {
			configurer.setTitle(Platform.getProduct().getName()
    			+ " "
    			+ Platform.getProduct().getDefiningBundle().getVersion());
		} else {
			configurer.setTitle(Platform.getProduct().getName()
    			+ " "
    			+ Platform.getProduct().getDefiningBundle().getVersion()
    			+ " "
    			+ Platform.getProduct().getProperty("mavenBuildTimestamp"));
		}

        
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS, Perspective.PERSPECTIVE_ID
                + ", org.jcryptool.crypto.flexiprovider.ui.perspective.FlexiProviderPerspective"); //$NON-NLS-1$
    }


    @Override
    public void postWindowOpen() {
        TrayDialog.setDialogHelpAvailable(true);
        
        if (maximize) {
        	getWindowConfigurer().getWindow().getShell().setMaximized(true);
        }  

        super.postWindowOpen();
    }
}
