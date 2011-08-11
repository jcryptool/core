//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.tools;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import org.jcryptool.crypto.classic.alphabets.Alphabet;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * Saves and loads the alphabets.<br>
 * Extends the <code>ObjectPersistence</code> class in the XStream Plug-in.
 *
 * @author t-kern
 *
 */
public class AlphabetPersistence  {

	/**
	 * Saves the alphabets vector to disk.
	 *
	 * @param alphabets	Vector containing the alphabet
	 * @param writer	The OutputStreamWriter to write to
	 */
	public static void saveAlphabetsToXML(Vector<Alphabet> alphabets, OutputStreamWriter writer) {
		XStream xstream = new XStream(new DomDriver());

		xstream.alias("alphabets", Vector.class);
		xstream.alias("alphabet", Alphabet.class);
		xstream.toXML(alphabets, writer);
	}

	/**
	 * Loads the alphabets vector from disk.
	 *
	 * @param reader	The InputStreamReader containing the alphabet
	 * @return			The Alphabet vector
	 */
	@SuppressWarnings("unchecked")
	public Vector<Alphabet> loadAlphabetsFromXML(InputStreamReader reader) {

		XStream xstream = new XStream(new DomDriver());

		xstream.alias("alphabets", Vector.class);
		xstream.alias("alphabet", Alphabet.class);

		xstream.setClassLoader(AlphabetsPlugin.getDefault().getClass().getClassLoader());

		Vector<Alphabet> vector = (Vector <Alphabet>)xstream.fromXML(reader);
		return vector;
	}
}
