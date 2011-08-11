/*
 * javahexeditor, a java hex editor Copyright (C) 2006, 2009 Jordi Bergenthal,
 * pestatije(-at_)users.sourceforge.net The official javahexeditor site is
 * sourceforge.net/projects/javahexeditor This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor.plugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class BinaryEditorPlugin extends AbstractUIPlugin {

    // Find/Replace dialog combo box lists
    @SuppressWarnings("rawtypes")
	private static List findReplaceFindList = null;
    @SuppressWarnings("rawtypes")
	private static List findReplaceReplaceList = null;
    // The shared instance.
    private static BinaryEditorPlugin plugin;
    // Preferences font identifiers
    public static final String preferenceFontName = "font.name";
    public static final String preferenceFontSize = "font.size";
    public static final String preferenceFontStyle = "font.style";

    private IPreferenceStore preferenceStore = null;

    /**
     * Returns the shared instance.
     */
    public static BinaryEditorPlugin getDefault() {
        return plugin;
    }

    /**
     * Get the list of previous finds
     *
     * @return list of tuples {find String, Boolean:is hex(true) or text(false)}
     */
    @SuppressWarnings("rawtypes")
	public static List getFindReplaceFindList() {
        if (findReplaceFindList == null) {
            findReplaceFindList = new ArrayList();
        }

        return findReplaceFindList;
    }

    /**
     * Get the list of previous replaces
     *
     * @return list of tuples {find String, Boolean:is hex(true) or text(false)}
     */
    @SuppressWarnings("rawtypes")
	public static List getFindReplaceReplaceList() {
        if (findReplaceReplaceList == null) {
            findReplaceReplaceList = new ArrayList();
        }

        return findReplaceReplaceList;
    }

    /**
     * Get font data information common to all plugin editors. Data comes from preferences store.
     *
     * @return font data to be used by plugin editors. Returns null for default font data.
     */
    public static FontData getFontData() {
        IPreferenceStore store = getDefault().getPreferenceStore();
        String name = store.getString(preferenceFontName);
        int style = store.getInt(preferenceFontStyle);
        int size = store.getInt(preferenceFontSize);
        FontData fontData = null;
        if (name != null && !"".equals(name) && size > 0)
            fontData = new FontData(name, size, style);

        return fontData;
    }

    /**
     * @see AbstractUIPlugin#getPreferenceStore()
     */
    public IPreferenceStore getPreferenceStore() {
        if (preferenceStore == null) {
            preferenceStore = super.getPreferenceStore();
            preferenceStore.setDefault(preferenceFontName, "Courier New");
            preferenceStore.setDefault(preferenceFontSize, 10);
        }

        return preferenceStore;
    }

    /**
     * The constructor.
     */
    public BinaryEditorPlugin() {
        super();
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }
}
