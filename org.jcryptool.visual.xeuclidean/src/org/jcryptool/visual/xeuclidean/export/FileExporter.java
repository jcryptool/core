// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.xeuclidean.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Vector;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.xeuclidean.algorithm.XEuclid;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class FileExporter {
	private static final String cr = "\n";
	private static final String tab = "\t";
	private static final String seperator = ";";

	com.lowagie.text.Font fontValues = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 8,
			com.lowagie.text.Font.NORMAL);
	com.lowagie.text.Font fontValuesItalic = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,
			com.lowagie.text.Font.DEFAULTSIZE, com.lowagie.text.Font.ITALIC);

	private PdfPTable table;
	private Document document;
	private String file;

	private Vector<BigInteger> qTmp;
	private Vector<BigInteger> rTmp;
	private Vector<BigInteger> xTmp;
	private Vector<BigInteger> yTmp;

	public FileExporter(XEuclid xeuclid, String file) {
		document = new Document();
		this.file = file;

		qTmp = xeuclid.getQ();
		rTmp = xeuclid.getR();
		xTmp = xeuclid.getX();
		yTmp = xeuclid.getY();
	}

	public void exportToPDF() {
		table = new PdfPTable(5);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			PdfPCell cell = new PdfPCell();
			cell.setColspan(5);
			float[] width = { 1.2f, 3f, 3f, 3f, 3f };
			table.setTotalWidth(width);
			table.addCell("Index");
			table.addCell("Quotient");
			table.addCell("Remainder");
			table.addCell("X");
			table.addCell("Y");

			String pValue = rTmp.get(0).toString();
			String qValue = rTmp.get(1).toString();
			String yValue = yTmp.lastElement().toString();
			String xValue = xTmp.lastElement().toString();
			String resultValue = rTmp.get(qTmp.size() - 2).toString();
			String resultValueString;

			int size = qTmp.size();
			for (int index = 0; index < size; index++) {
				table.addCell(new Phrase(String.valueOf(index), fontValues));
				if (index == 0 || index == qTmp.size() - 1) {
					table.addCell(new Phrase("", fontValues));
				} else {
					table.addCell(new Phrase(qTmp.get(index).toString(), fontValues));
				}
				table.addCell(new Phrase(rTmp.get(index).toString(), fontValues));
				table.addCell(new Phrase(xTmp.get(index).toString(), fontValues));
				table.addCell(new Phrase(yTmp.get(index).toString(), fontValues));
			}

			document.add(new Paragraph("Extended Euclidian"));
			document.add(new Paragraph("gcd(" + pValue + "," + qValue + ")"));
			document.add(new Paragraph("\n"));
			document.add(table);
			document.add(new Paragraph("\n"));

			if (xTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
				xValue = "( " + xValue + " )";
			}
			if (yTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
				yValue = "( " + yValue + " )";
			}
			String tmpValue = " = " + pValue + " * " + yValue + " + " + qValue + " * " + xValue;
			resultValueString = tmpValue + " = " + resultValue;

			document.add(new Paragraph("The Greates Common Divisor of " + pValue + " and " + qValue + " is "
					+ resultValue + "."));
			document.add(new Paragraph("gcd(p,q) = p * x + q * y", fontValuesItalic));
			document.add(new Paragraph("gcd(" + rTmp.get(0) + "," + rTmp.get(1) + ")" + resultValueString,
					fontValuesItalic));
			document.close();

		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (DocumentException e) {
		    LogUtil.logError(e);
		}
	}

	public void exportToCSV() {
		String pValue = rTmp.get(0).toString();
		String qValue = rTmp.get(1).toString();
		String yValue = yTmp.lastElement().toString();
		String xValue = xTmp.lastElement().toString();
		String resultValue = rTmp.get(qTmp.size() - 2).toString();
		String resultValueString;

		String value;

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

			if (xTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
				xValue = "( " + xValue + " )";
			}
			if (yTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
				yValue = "( " + yValue + " )";
			}

			osw.write("Extended Euclidian");
			osw.write(seperator);
			osw.write(cr);
			osw.write("gcd(" + pValue + "," + qValue + ")");
			osw.write(seperator);
			osw.write(cr);
			osw.write(seperator);
			osw.write(cr);
			osw.write("Index" + seperator + "Quotient" + seperator + "Remainder" + seperator + "X" + seperator + "Y"
					+ seperator);
			osw.write(cr);

			int size = qTmp.size();
			for (int index = 0; index < size; index++) {
				value = String.valueOf(index) + seperator;
				if (index == 0 || index == qTmp.size() - 1) {
					value += seperator;
				} else {
					value += qTmp.get(index).toString() + seperator;
				}
				value += rTmp.get(index).toString() + seperator;
				value += xTmp.get(index).toString() + seperator;
				value += yTmp.get(index).toString() + seperator;
				osw.write(value);
				osw.write(cr);
			}

			String tmpValue = " = " + pValue + " * " + yValue + " + " + qValue + " * " + xValue;
			resultValueString = tmpValue + " = " + resultValue;

			osw.write(cr);
			osw.write("The Greates Common Divisor of " + pValue + " and " + qValue + " is " + resultValue + ".");
			osw.write(seperator);
			osw.write(cr);
			osw.write("gcd(p,q) = p * x + q * y");
			osw.write(seperator);
			osw.write(cr);
			osw.write("gcd(" + rTmp.get(0) + "," + rTmp.get(1) + ")" + resultValueString);
			osw.write(seperator);
			osw.write(cr);

			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (IOException e) {
		    LogUtil.logError(e);
		}

	}

	public void exportToLatex() {
		String pValue = rTmp.get(0).toString();
		String qValue = rTmp.get(1).toString();
		String yValue = yTmp.lastElement().toString();
		String xValue = xTmp.lastElement().toString();
		String resultValue = rTmp.get(qTmp.size() - 2).toString();
		String resultValueString;

		String value;

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

			if (xTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
				xValue = "( " + xValue + " )";
			}
			if (yTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
				yValue = "( " + yValue + " )";
			}

			osw = writeLatexHeader(osw);

			osw.write("Extended Euclidian");
			osw.write(cr);
			osw.write(cr);
			osw.write("$gcd(" + pValue + "," + qValue + ")$");
			osw.write(cr);
			osw.write("\\begin{longtable}{|l|l|l|l|l|}");
			osw.write(cr);
			osw.write(tab);
			osw.write("\\hline");
			osw.write(cr);
			osw.write(tab);
			osw.write("Index & Quotient & Remainder & X & Y\\\\");
			osw.write(cr);
			osw.write(tab);
			osw.write("\\hline");
			osw.write(cr);

			int size = qTmp.size();
			for (int index = 0; index < size; index++) {
				value = String.valueOf(index) + " & ";
				if (index == 0 || index == qTmp.size() - 1) {
					value += " & ";
				} else {
					value += qTmp.get(index).toString() + " & ";
				}
				value += rTmp.get(index).toString() + " & ";
				value += xTmp.get(index).toString() + " & ";
				value += yTmp.get(index).toString() + "\\\\";
				osw.write(tab);
				osw.write(value);
				osw.write(cr);
				osw.write(tab);
				osw.write("\\hline");
				osw.write(cr);
			}

			osw.write(cr);
			osw.write(tab);
			osw.write("\\caption{\\label{tab:ref} caption}");
			osw.write(cr);
			osw.write(tab);
			osw.write("\\end{longtable}");
			osw.write(cr);
			osw.write(cr);

			String tmpValue = " = " + pValue + " * " + yValue + " + " + qValue + " * " + xValue;
			resultValueString = tmpValue + " = " + resultValue;

			osw.write("The Greatest Common Divisor of $" + pValue + "$ and $" + qValue + "$ is $" + resultValue + "$.");
			osw.write(cr);
			osw.write(cr);
			osw.write("$gcd(p,q) = p x + q y$");
			osw.write(cr);
			osw.write(cr);
			osw.write("$gcd(" + rTmp.get(0) + "," + rTmp.get(1) + ")" + resultValueString + "$");
			osw.write(cr);
			osw.write(cr);
			osw.write("\\end{document}");

			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (IOException e) {
		    LogUtil.logError(e);
		}
	}

	private OutputStreamWriter writeLatexHeader(OutputStreamWriter osw) {
		try {

			osw.write("\\documentclass{article}");
			osw.write(cr);
			osw.write(cr);
			osw.write("\\usepackage{longtable}");
			osw.write(cr);
			osw.write(cr);
			osw.write(cr);
			osw.write("\\begin{document}");
			osw.write(cr);
			osw.write(cr);

		} catch (Exception e) {
		}
		return osw;
	}

}
