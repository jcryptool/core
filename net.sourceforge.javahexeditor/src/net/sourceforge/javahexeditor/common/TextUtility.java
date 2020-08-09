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
package net.sourceforge.javahexeditor.common;

import net.sourceforge.javahexeditor.Texts;

/**
 * Text formatting utility.
 *
 * @author Peter Dell
 */
public final class TextUtility {

	/**
	 * Parameter variable tokens.
	 */
	private static final String[] PARAMETERS = { "{0}", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}" };

	/**
	 * Creation is private.
	 */
	private TextUtility() {

	}

	/**
	 * Formats a text with parameters "{0}" to "{9}".
	 *
	 * @param text
	 *            The text with the parameters "{0}" to "{9}", may be empty, not
	 *            <code>null</code>.
	 * @param parameters
	 *            The parameters, may be empty or <code>null</code>.
	 *
	 * @return The formatted text, may be empty, not <code>null</code>.
	 */
	public static String format(String text, String... parameters) {
		if (text == null) {
			throw new IllegalArgumentException("Parameter 'text' must not be null.");
		}
		if (parameters == null) {
			parameters = new String[0];
		}
		for (int i = 0; i < parameters.length; i++) {
			String parameter = parameters[i];
			if (parameter == null) {
				parameter = Texts.EMPTY;
			}
			text = text.replace(PARAMETERS[i], parameter);
		}
		return text;
	}

}
