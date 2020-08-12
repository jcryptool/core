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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Central factory to track creation of RandomAccessFile instance.
 *
 * @author Peter Dell
 */
public final class RandomAccessFileFactory {

	private static final List<RandomAccessFile> instances = new ArrayList<RandomAccessFile>(3);
	private static final boolean debug = false;

	public static RandomAccessFile createRandomAccessFile(final File file, final String mode)
			throws FileNotFoundException {
		RandomAccessFile raf = new RandomAccessFile(file, mode) {
			@Override
			public void close() throws IOException {
				super.close();
				synchronized (instances) {
					instances.remove(this);
				}
				logContext("Closed random access file for '" + file.getAbsolutePath());

			}
		};

		synchronized (instances) {
			instances.add(raf);
		}
		logContext("Created random access file for '" + file.getAbsolutePath() + "' in mode '" + mode + "'");
		return raf;
	}

	public static void log(String message) {
		if (debug) {
			System.out.println("RandomAccessFileFactory: " + message);
			System.out.flush();
		}
	}

	private static void logContext(String message) {
		if (debug) {
			log(message);
			log(instances.size() + " random access files open");
			Thread.dumpStack();
			System.err.flush();
		}
	}
}
