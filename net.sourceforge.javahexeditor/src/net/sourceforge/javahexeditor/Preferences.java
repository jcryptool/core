/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * The official javahexeditor site is sourceforge.net/projects/javahexeditor
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

/**
 * Preferences data container.
 * 
 * @author Peter Dell
 */
public final class Preferences {
	// Property names
	public static final String FONT_NAME = "font.name";
	public static final String FONT_SIZE = "font.size";
	public static final String FONT_STYLE = "font.style";
	public static final String FONT_DATA = "font.data";

	private static final FontData DEFAULT_FONT_DATA = new FontData("Courier New", 10, SWT.NORMAL);

	public static FontData getDefaultFontData() {
		return DEFAULT_FONT_DATA;
	}
}
