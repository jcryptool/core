// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.grille.algorithm;

import java.util.Arrays;


public class Schablone {

	char [][]schablone;

	public Schablone(int size){
		schablone = new char[size][size];
		for(int i = 0; i < size; i++) // init to '0'
		{
		    Arrays.fill(schablone[i], '0');
		}

	}
	/**
	 * gibt Wert der Schablone an der jeweiligen Position an
	 * @param row Zeilennummer (von 0 bis schablonenlänge -1)
	 * @param column Spaltennummer (von 0 bis Schablonenlänge -1)
	 * @return Wert an der jeweiligen Position
	 */
	public char get(int row, int column){
		return schablone[row][column];
	}
	public void set(int row, int column, char value){
		schablone[row][column]= value;
	}
	@Override
	public String toString() {
	    StringBuilder s = new StringBuilder();
		for(int r = 0; r < schablone.length; r++){
			for(int c = 0; c < schablone[r].length; c++){
				s.append(get(r,c));
			}
			s.append("\n");
		}
		return s.toString();
	}
	/**
	 * gibt die Größe der Schablone zurück
	 * @return die Größe der Schablone
	 */
	public int getSize(){
		return schablone.length;
	}
}
