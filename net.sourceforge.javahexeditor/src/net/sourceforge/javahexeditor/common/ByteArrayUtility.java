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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.javahexeditor.Texts;

/**
 * Utility to handle byte array conversions.
 *
 * @author Peter Dell
 *
 */
public final class ByteArrayUtility {

	/**
	 * Creation is private.
	 */
	private ByteArrayUtility() {

	}

	/**
	 * Converts a hex String to byte[]. Ignores white spaces. Will convert full
	 * bytes only, odd number of hex characters per will have a leading '0' added.
	 *
	 * @param hexString an hex string (ie. "0fdA1").
	 * @return the byte[] value of the hex string
	 */
	public static byte[] parseString(String value) throws NumberFormatException {
		List<Byte> bytes = new ArrayList<Byte>();
		StringTokenizer st = new StringTokenizer(value);
		while (st.hasMoreTokens()) {
			String hexString = st.nextToken();
			if ((hexString.length() & 1) == 1) {
				hexString = '0' + hexString;
			}
			for (int i = 0; i < hexString.length(); i = i + 2) {
				int high = Character.digit(hexString.charAt(i), 16);
				int low = Character.digit(hexString.charAt(i + 1), 16);
				if (high < 0 || low < 0) {
					throw new NumberFormatException(
							TextUtility.format(Texts.BYTE_ARRAY_UTILITY_INVALID_HEX_STRING, value));
				}
				bytes.add(Byte.valueOf((byte) ((high << 4) | low)));

			}
		}
		byte[] result = new byte[bytes.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = bytes.get(i).byteValue();
		}

		return result;
	}
}
