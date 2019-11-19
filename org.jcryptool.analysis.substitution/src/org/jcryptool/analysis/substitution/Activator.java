//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution;

import java.util.Collections;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.analysis.substitution.calc.DynamicPredefinedStatisticsProvider;
import org.jcryptool.analysis.substitution.calc.TextStatistic;
import org.jcryptool.analysis.substitution.ui.modules.utils.PredefinedStatisticsProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.analysis.substitution"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	private static PredefinedStatisticsProvider statisticsProvider = null;
	
	public static PredefinedStatisticsProvider getPredefinedStatisticsProvider() {
		if(statisticsProvider == null) {
			try {
				statisticsProvider = new DynamicPredefinedStatisticsProvider();
			} catch (Exception e) {
				LogUtil.logError(PLUGIN_ID, e);
				return new PredefinedStatisticsProvider() {
					
					@Override
					public List<TextStatistic> getPredefinedStatistics() {
						return Collections.emptyList();
					}
				};
			}
		}
		return statisticsProvider;
		
	}
}
