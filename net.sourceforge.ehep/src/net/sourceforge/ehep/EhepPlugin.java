/*==========================================================================
 * 
 * EhepPlugin.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.8 $
 * $Date: 2012/11/06 16:45:23 $
 *
 * Created on 10-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net) 
 *==========================================================================*/
package net.sourceforge.ehep;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import net.sourceforge.ehep.core.EHEP;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class EhepPlugin extends AbstractUIPlugin implements IPropertyChangeListener {
	//
	//The shared instance.
	//
	private static EhepPlugin plugin;

	//
	//Resource bundle.
	//
	private ResourceBundle resourceBundle;
	
	private Map<RGB, Color> sharedColors = new HashMap<RGB, Color>();
	
	private Map<FontData, Font> fonts = new HashMap<FontData, Font>();
	
	private boolean debugMode;
	
	/**
	 * The constructor.
	 */
	public EhepPlugin() {
		super();
		plugin = this;

		try {
			resourceBundle = ResourceBundle.getBundle(EHEP.PLUGIN_ID + ".EhepPlugin"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if (EHEP.PROPERTY_DEBUG_MODE.equals(event.getProperty()))
			debugMode = (Boolean) event.getNewValue();
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static EhepPlugin getDefault() {
		return plugin;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		disposeColors();
		disposeFonts();
		getDefault().getPreferenceStore().removePropertyChangeListener(this);
		super.stop(context);
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = EhepPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	public static boolean isDebugMode() {
		return getDefault().debugMode;
	}

	/**
	 * Writes a status to plugin log
	 * @param status
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/**
	 * Writes a message to plugin log
	 * @param severity
	 * @param message
	 */
	public static void log(int severity, String message) {
		if (!isDebugMode()) return;
		Status status = new Status(severity, EHEP.PLUGIN_ID, IStatus.OK, message, null);
		log(status);
	}

	/**
	 * Writes a message to plugin log
	 * @param message
	 */
	public static void log(String message) {
		if (!isDebugMode()) return;
		log(IStatus.INFO, message);
	}

	/**
	 * Writes an exception to plugin log
	 * @param e
	 */
	public static void log(Throwable e) {
		if (!isDebugMode()) return;
		log(new Status(IStatus.ERROR, EHEP.PLUGIN_ID, IStatus.ERROR, "EhepPlugin.internalErrorOccurred", e)); //$NON-NLS-1$
	}

	/**
	 * Returns the color for the specified rgb on the device. The colors
	 * created by this call are cached and disposed at bundle stop time. 
	 * @param rgb the rgb value, must not be null
	 * @return the color
	 */
	public static Color getColor(RGB rgb) {
		Assert.isNotNull(rgb);
		
		Device device = Display.getCurrent();
		if (device == null)
			device = Display.getDefault();
		Color color = getDefault().sharedColors.get(rgb);
		if (color == null)
			getDefault().sharedColors.put(rgb, color = new Color(device, rgb));
		return color;
	}
	
	public static Font getFont(FontData data) {
		Assert.isNotNull(data);
		
		Device device = Display.getCurrent();
		if (device == null)
			device = Display.getDefault();
		Font font = getDefault().fonts.get(data);
		if (font == null)
			getDefault().fonts.put(data, font = new Font(device, data));
		return font;
	}

	private void disposeColors() {
		for (Iterator<Color> it = sharedColors.values().iterator(); it.hasNext();) {
			it.next().dispose();
		} // for
	}

	private void disposeFonts() {
		for (Iterator<Font> it = fonts.values().iterator(); it.hasNext();) {
			it.next().dispose();
		}
	}
}
