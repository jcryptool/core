// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
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

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class FileExporter {

	private static final String CR = "\n";
	private static final String SEPARATOR = ";";

	private ChineseRemainderTheorem crt;
	private Document document;
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
		document = new Document();
		this.file = file;

		valueA = crt.getA();
		valueModuli = crt.getModuli();
		valueBigM = crt.getBigM();
		valueInverse = crt.getInverse();
	}

	/**
	 * creates the PDF output-file using iText library
	 */
	public void exportToPDF() {
		Font fontSupscript = new Font(Font.HELVETICA, 6, Font.NORMAL);
		Font fontSymbol = new Font(Font.SYMBOL, 12, Font.NORMAL);

		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			document.add(new Paragraph("Chinese Remainder Theorem"));
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Equations:"));
			document.add(new Paragraph(" "));
			for (int i = 0; i < valueA.length; i++) {
				document.add(new Paragraph("x = " + valueA[i] + " mod " + valueModuli[i]));
			}
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Compute"));
			document.add(new Paragraph(" "));

			Chunk space = new Chunk(" ");
			Chunk spaceBig = new Chunk("    ");
			Chunk index = new Chunk("i", fontSupscript);
			index.setTextRise(-3.0f);
			Chunk m = new Chunk("m");
			Chunk equal = new Chunk("=");
			Chunk openB = new Chunk("(");
			Chunk closeB = new Chunk(")");
			Chunk p = new Chunk("P", fontSymbol);
			Chunk comma = new Chunk(",");
			Chunk range = new Chunk("i=0 to n-1");
			Chunk bigM = new Chunk("M");
			Chunk div = new Chunk("/");

			document.add(m);
			document.add(space);
			document.add(equal);
			document.add(space);
			document.add(p);
			document.add(space);
			document.add(openB);
			document.add(space);
			document.add(m);
			document.add(index);
			document.add(space);
			document.add(closeB);
			document.add(comma);
			document.add(spaceBig);
			document.add(bigM);
			document.add(space);
			document.add(equal);
			document.add(space);
			document.add(m);
			document.add(space);
			document.add(div);
			document.add(space);
			document.add(m);
			document.add(index);
			document.add(comma);
			document.add(spaceBig);
			document.add(range);

			document.add(new Paragraph(" "));
			document.add(new Paragraph("m = " + crt.getModulus()));
			document.add(new Paragraph(" "));
			for (int i = 0; i < valueBigM.length; i++) {
				m = new Chunk("m");
				index = new Chunk(String.valueOf(i), fontSupscript);
				index.setTextRise(-3.0f);
				Chunk value = new Chunk(valueBigM[i].toString());

				document.add(m);
				document.add(index);
				document.add(new Chunk(" = "));
				document.add(value);
				document.add(new Paragraph());
			}
			document.add(new Paragraph(" "));
			document.add(new Paragraph("To get the inverse you can use the extended euclidean."));
			document.add(new Paragraph(" "));

			Chunk y = new Chunk("y");
			Chunk equiv = new Chunk("=");
			Chunk mod = new Chunk("mod");
			index = new Chunk("i", fontSupscript);
			index.setTextRise(-3.0f);

			document.add(y);
			document.add(index);
			document.add(bigM);
			document.add(index);
			document.add(space);
			document.add(equiv);
			document.add(space);
			document.add(new Chunk("1"));
			document.add(space);
			document.add(mod);
			document.add(space);
			document.add(m);
			document.add(index);
			document.add(comma);
			document.add(spaceBig);
			document.add(range);

			document.add(new Paragraph(" "));
			for (int i = 0; i < valueBigM.length; i++) {
				index = new Chunk(String.valueOf(i), fontSupscript);
				index.setTextRise(-3.0f);
				Chunk value = new Chunk(valueInverse[i].toString());

				document.add(new Chunk("y"));
				document.add(index);
				document.add(new Chunk(" = "));
				document.add(value);
				document.add(new Paragraph());
			}
			document.add(new Paragraph(" "));
			document.add(new Paragraph("To get one solution of the simultaneous congruences."));
			document.add(new Paragraph(" "));

			Chunk sum = new Chunk("S", fontSymbol);
			index = new Chunk("i", fontSupscript);
			index.setTextRise(-3.0f);

			document.add(new Chunk("x = "));
			document.add(sum);
			document.add(space);
			document.add(new Chunk());
			document.add(new Chunk("a"));
			document.add(index);
			document.add(new Chunk("y"));
			document.add(index);
			document.add(bigM);
			document.add(index);
			document.add(space);
			document.add(new Chunk("mod"));
			document.add(space);
			document.add(m);
			document.add(comma);
			document.add(spaceBig);
			document.add(range);
			document.add(new Chunk("."));

			document.add(new Paragraph(" "));
			document.add(new Paragraph("One solution is: " + crt.getFinalResult()));

			document.close();

		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (DocumentException e) {
		    LogUtil.logError(e);
		}
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
