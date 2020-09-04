// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crt.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crt.algorithm.ChineseRemainderTheorem;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class FileExporter {

	private static final String CR = "\n";
	private static final String SEPARATOR = ";";

	private ChineseRemainderTheorem crt;
	private String file;

	private BigInteger[] valueA;
	private BigInteger[] valueModuli;
	private BigInteger[] valueBigM;
	private BigInteger[] valueInverse;

	/**
	 * constructor
	 * @param crt the chinese remainder object which contains the algorithm
	 * @param file the output-file
	 */
	public FileExporter(ChineseRemainderTheorem crt, String file) {
		this.crt = crt;
		this.file = file;

		valueA = crt.getA();
		valueModuli = crt.getModuli();
		valueBigM = crt.getBigM();
		valueInverse = crt.getInverse();
	}

	/**
	 * creates the CSV output-file
	 */
	public void exportToCSV() {

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

			valueA = crt.getA();
			valueModuli = crt.getModuli();
			valueBigM = crt.getBigM();
			valueInverse = crt.getInverse();

			osw.write("Chinese Remainder Theorem");
			osw.write(SEPARATOR);
			osw.write(CR);
			osw.write("Equations:");
			osw.write(SEPARATOR);
			osw.write(CR);

			for (int i = 0; i < valueA.length; i++) {
				osw.write("x");
				osw.write(SEPARATOR);
				osw.write("=");
				osw.write(SEPARATOR);
				osw.write(valueA[i].toString());
				osw.write(SEPARATOR);
				osw.write("mod");
				osw.write(SEPARATOR);
				osw.write(valueModuli[i].toString());
				osw.write(SEPARATOR);
				osw.write(CR);
			}

			osw.write(CR);
			osw.write(CR);
			osw.write("Inverse:");
			osw.write(CR);
			osw.write("m");
			osw.write(SEPARATOR);
			osw.write("=");
			osw.write(SEPARATOR);
			osw.write(crt.getModulus().toString());
			osw.write(SEPARATOR);
			osw.write(CR);
			osw.write(CR);

			for (int i = 0; i < valueInverse.length; i++) {
				osw.write("M_" + i);
				osw.write(SEPARATOR);
				osw.write("=");
				osw.write(SEPARATOR);
				osw.write(valueBigM[i].toString());
				osw.write(SEPARATOR);
				osw.write("y_" + i);
				osw.write(SEPARATOR);
				osw.write(valueInverse[i].toString());
				osw.write(SEPARATOR);
				osw.write(CR);
			}

			osw.write(CR);
			osw.write("One solution of the simultaneous congruences is:");
			osw.write(SEPARATOR);
			osw.write(crt.getFinalResult().toString());
			osw.write(SEPARATOR);

			osw.write(CR);
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (IOException e) {
		    LogUtil.logError(e);
		}

	}

	/**
	 * creates the latex output-file
	 */
	public void exportToLatex() {
		valueA = crt.getA();
		valueModuli = crt.getModuli();
		valueBigM = crt.getBigM();
		valueInverse = crt.getInverse();

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

			osw = writeLatexHeader(osw);

			osw.write("\\textbf{Chinese Remainder Theorem}");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("Equations:");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			for (int i = 0; i < valueA.length; i++) {
				osw.write("$x = " + valueA[i] + " ~mod ~" + valueModuli[i] + "$");
				osw.write(CR);
				osw.write("\\newline");
				osw.write(CR);
			}
			osw.write("\\newline");
			osw.write(CR);
			osw.write("Compute");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("$m = \\prod \\limits_{i=i}^{n} m_i, \\qquad M_i = m / m_i, \\qquad 0 \\le i < n.$");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("$m = " + crt.getModulus() + "$");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			for (int i = 0; i < valueBigM.length; i++) {
				osw.write("$m_" + i + " = " + valueBigM[i] + "$");
				osw.write(CR);
				osw.write("\\newline");
				osw.write(CR);
			}
			osw.write(CR);
			osw.write(CR);
			osw.write("To get the inverse you can use the extended euclidean.");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write("$y_iM_i \\equiv 1 ~mod ~m_i, ~0 \\le i < n.$");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			for (int i = 0; i < valueInverse.length; i++) {
				osw.write("$y_" + i + " = " + valueInverse[i] + "$");
				osw.write(CR);
				osw.write("\\newline");
				osw.write(CR);

			}
			osw.write(CR);
			osw.write("To get one solution of the simultaneous congruences");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("$x = \\sum \\limits_{i=1}^{n} a_iy_iM_i ~mod ~m.$");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("\\newline");
			osw.write(CR);
			osw.write("One solution is: $" + crt.getFinalResult() + "$");
			osw.write(CR);

			osw.write(CR);
			osw.write("\\end{document}");

			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (IOException e) {
		    LogUtil.logError(e);
		}
	}

	/**
	 * writes the header for the latex file
	 * @param osw the OutputStreamWriter
	 * @return the OutputStreamWriter which contains the latex header
	 */
	private OutputStreamWriter writeLatexHeader(OutputStreamWriter osw) {
		try {

			osw.write("\\documentclass{article}");
			osw.write(CR);
			osw.write(CR);
			osw.write("\\begin{document}");
			osw.write(CR);
			osw.write(CR);

		} catch (Exception e) {
		}
		return osw;
	}

}
