/**
 * Copyright (C) 2013 - 2014 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of a WUDSN software distribution.
 *
 * The!Cart Studio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * The!Cart Studio distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the WUDSN software distribution. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.javahexeditor.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Utility class to access resources in the class path.
 *
 * @author Peter Dell
 */
public final class ResourceUtility {

	/**
	 * Loads a resource as string.
	 *
	 * @param path
	 *            The resource path, not empty, not <code>null</code>.
	 * @return The resource content or <code>null</code> if the resource was not
	 *         found.
	 */
	public static String loadResourceAsString(String path) {
		if (path == null) {
			throw new IllegalArgumentException("Parameter 'path' must not be null.");
		}
		if (path.isEmpty()) {
			throw new IllegalArgumentException("Parameter 'path' must not be empty.");
		}
		final InputStream inputStream = getInputStream(path);
		if (inputStream == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(inputStream);
			char[] buffer = new char[8192];
			int actualLength;
			while ((actualLength = reader.read(buffer, 0, buffer.length)) != -1) {
				builder.append(buffer, 0, actualLength);
			}
			reader.close();

		} catch (IOException ex) {
			LogUtil.logError("Cannot load resource " + path , ex);
		} finally {

			try {
				inputStream.close();
			} catch (IOException ignore) {
			}
		}
		return builder.toString();
	}

	/**
	 * Self implemented logic to bypass the bug described in
	 * <a href="http://bugs.sun.com/view_bug.do?bug_id=4523159">JDK-4523159 :
	 * getResourceAsStream on jars in path with "!"</a>. Note that this is not the
	 * full logic. The rest was removed to reduced dependencies.
	 *
	 * @param path
	 *            The path of the resource to load, not <code>null</code>.
	 * @return The input stream or <code>null</code> if the source was not found.
	 */
	private static InputStream getInputStream(String path) {
		if (path == null) {
			throw new IllegalArgumentException("Parameter 'path' must not be null.");
		}
		// If there is no loader, the program was launched using the Java
		// boot class path and the system class loader must be used.
		ClassLoader loader = ResourceUtility.class.getClassLoader();
		URL url = (loader == null) ? ClassLoader.getSystemResource(path) : loader.getResource(path);
		InputStream result = null;
		try {
			if (url != null) {
				result = url.openStream();
			}
		} catch (IOException ex) {
			LogUtil.logError("Cannot get input stream for path " + path, ex);
		}
		return result;
	}
}
