// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.euclid;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jcryptool.core.logging.utils.LogUtil;

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

	com.lowagie.text.Font fontValuesItalic = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,
			com.lowagie.text.Font.DEFAULTSIZE, com.lowagie.text.Font.ITALIC);

	private PdfPTable table;
	private Document document;
	private String file;

	private String[] euclid;

	public FileExporter(String[] array, String file) {
		document = new Document();
		this.file = file;
		euclid = array;
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
			table.addCell("x");
			table.addCell("y");
			
			int size = euclid.length;
			String pValue = euclid[2];
			String qValue = euclid[7];
			String yValue = euclid[size-7];
			String xValue = euclid[size-6];
			String resultValue = euclid[size-8];
			
			for (int i=0; i<size; i++) {
			    table.addCell(new Phrase(euclid[i]));
			    System.out.println("3 "+i);
			}

			document.add(new Paragraph("Extended Euclidian"));
			document.add(new Paragraph("gcd(" + pValue + "," + qValue + ")"));
			document.add(new Paragraph("\n"));
			document.add(table);
			document.add(new Paragraph("\n"));

			if (xValue.charAt(0) == '-') {
				xValue = "( " + xValue + " )";
			}
			if (yValue.charAt(0) == '-') {
				yValue = "( " + yValue + " )";
			}
			String tmpValue = " = " + pValue + " * " + yValue + " + " + qValue + " * " + xValue;
			String resultValueString = tmpValue + " = " + resultValue;

			document.add(new Paragraph("The Greatest Common Divisor of " + pValue + " and " + qValue + " is "
					+ resultValue + "."));
			document.add(new Paragraph("gcd(p,q) = p * x + q * y", fontValuesItalic));
			document.add(new Paragraph("gcd(" + pValue + "," + qValue + ")" + resultValueString,
					fontValuesItalic));
			document.close();

		} catch (FileNotFoundException e) {
		    LogUtil.logError(e);
		} catch (DocumentException e) {
		    LogUtil.logError(e);
		}
	}

	public void exportToCSV() {
        int size = euclid.length;
        String pValue = euclid[2];
        String qValue = euclid[7];
        String yValue = euclid[size-7];
        String xValue = euclid[size-6];
        String resultValue = euclid[size-8];

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

            if (xValue.charAt(0) == '-') {
                xValue = "( " + xValue + " )";
            }
            if (yValue.charAt(0) == '-') {
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
			osw.write("Index" + seperator + "Quotient" + seperator + "Remainder" + seperator + "x" + seperator + "y"
					+ seperator);
			osw.write(cr);

			for (int i=0; i<size; i+=5) {
                osw.write(euclid[i] + seperator);
                osw.write(euclid[i+1] + seperator);
                osw.write(euclid[i+2] + seperator);
                osw.write(euclid[i+3] + seperator);
                osw.write(euclid[i+4] + seperator);
				osw.write(cr);
			}

			String tmpValue = " = " + pValue + " * " + yValue + " + " + qValue + " * " + xValue;
			String resultValueString = tmpValue + " = " + resultValue;

			osw.write(cr);
			osw.write("The Greatest Common Divisor of " + pValue + " and " + qValue + " is " + resultValue + ".");
			osw.write(seperator);
			osw.write(cr);
			osw.write("gcd(p,q) = p * x + q * y");
			osw.write(seperator);
			osw.write(cr);
			osw.write("gcd(" + pValue + "," + qValue + ")" + resultValueString);
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
        int size = euclid.length;
        String pValue = euclid[2];
        String qValue = euclid[7];
        String yValue = euclid[size-7];
        String xValue = euclid[size-6];
        String resultValue = euclid[size-8];
        String value;

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

            if (xValue.charAt(0) == '-') {
                xValue = "( " + xValue + " )";
            }
            if (yValue.charAt(0) == '-') {
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
			osw.write("Index & Quotient & Remainder & x & y\\\\");
			osw.write(cr);
			osw.write(tab);
			osw.write("\\hline");
			osw.write(cr);

			for (int i=0; i<size; i+=5) {
				value = euclid[i] + " & ";
				value += euclid[i+1] + " & ";
				value += euclid[i+2] + " & ";
				value += euclid[i+3] + " & ";
				value += euclid[i+4] + "\\\\";
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
			String resultValueString = tmpValue + " = " + resultValue;

			osw.write("The Greatest Common Divisor of $" + pValue + "$ and $" + qValue + "$ is $" + resultValue + "$.");
			osw.write(cr);
			osw.write(cr);
			osw.write("$gcd(p,q) = p x + q y$");
			osw.write(cr);
			osw.write(cr);
			osw.write("$gcd(" + pValue + "," + qValue + ")" + resultValueString + "$");
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
