// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.introduction.views;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.core.commands.HelpHrefRegistry;
import org.osgi.framework.BundleContext;
/**
 * This class only provides the plugin ID to other classes.
 * 
 * @author Thorben Groos
 *
 */
public class IntroductionPlugin extends AbstractUIPlugin {
	
    /** The plug-in ID. */
    public static final String PLUGIN_ID = "org.jcryptool.core.introduction"; //$NON-NLS-1$
    
    
    @Override
    public void start(BundleContext context) throws Exception {
    	super.start(context);
    	
    	/**
    	 * This assigns a "wrong" online-help to this plugin.
    	 * This was added, to force the JCT open the online-help of the
    	 * Algorithm perspective when clicking on the help icon in the
    	 * JCT toolbar.
    	 */
    	String linkToAlgorithmHelp = "/org.jcryptool.core.help/$nl$/help/users/general/perspective_algorithm.html";
    	HelpHrefRegistry.getInstance().registerHrefFor(PLUGIN_ID, linkToAlgorithmHelp);
    }
}
