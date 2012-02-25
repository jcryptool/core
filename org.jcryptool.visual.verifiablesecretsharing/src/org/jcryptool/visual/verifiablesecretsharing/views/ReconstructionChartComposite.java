//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.verifiablesecretsharing.views;

import java.awt.Color;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.verifiablesecretsharing.algorithm.Polynomial;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

public class ReconstructionChartComposite extends Composite {
	private int[] playerID = { 0 };
	private BigInteger[] shares = { new BigInteger("0") };
	private Polynomial reconstructedPolynom = new Polynomial(
			new BigInteger[] { new BigInteger("" + 0) });
	private JFreeChart chart;
	private Composite body;
	private StyledText stDescription;
	private ChartComposite chartComposite;
	private String generatedPolynom;
	private int secret;

	public void setSecret(int secret) {
		this.secret = secret;
	}

	public int getSecret() {
		return secret;
	}

	public String getPolynom() {
		return generatedPolynom;
	}

	public void setPolynom(String polynom) {
		this.generatedPolynom = polynom;
	}

	public int[] getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int[] playerID) {
		this.playerID = playerID;
	}

	public BigInteger[] getShares() {
		return shares;
	}

	public void setShares(BigInteger[] shares) {
		this.shares = shares;
	}

	public void setReconstructedPolynom(Polynomial reconstructedPolynom) {
		this.reconstructedPolynom = reconstructedPolynom;
	}

	public void redrawChart() {
		for (Control control : body.getChildren()) {
			if (control.getData() == null) {
				control.dispose();
			}
		}

		chart = createChart(createDataset());
		chartComposite = new ChartComposite(body, SWT.None, chart, true);
		body.layout();
		if (reconstructedPolynom.toString().compareTo("0") == 0) {
			stDescription.setText(Messages.ChartComposite_noGraph);
		} else {
			if (generatedPolynom.compareTo(reconstructedPolynom.toString()) == 0) {
				stDescription.setText("P'(x) = "
						+ reconstructedPolynom.toString() + "\r\n"
						+ Messages.ChartComposite_reconstruct_success);
			} else {
				stDescription.setText("P'(x) = "
						+ reconstructedPolynom.toString() + "\r\n"
						+ Messages.ChartComposite_reconstruct_failure);

			}
		}
	}

	public ReconstructionChartComposite(final Composite parent,
			final int style,
			VerifiableSecretSharingView verifiableSecretSharingView) {
		super(parent, style);
		setLayout(new GridLayout());

		createHead();
		createBody();
	}

	/**
	 * Generates the head of the tab. The head has a title and a description.
	 */
	private void createHead() {
		final Composite head = new Composite(this, SWT.NONE);
		head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		head.setLayout(new GridLayout());

		final Label label = new Label(head, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setText(Messages.VerifiableSecretSharingComposite_tab_title);
		stDescription = new StyledText(head, SWT.READ_ONLY | SWT.MULTI
				| SWT.WRAP);
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		if (reconstructedPolynom.toString().compareTo("0") == 0) {
			stDescription.setText(Messages.ChartComposite_noGraph);
		} else {
			stDescription.setText(reconstructedPolynom.toString());
		}
	}

	private void createBody() {
		body = new Composite(this, SWT.NONE);
		body.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		body.setLayout(new FillLayout());
		chart = createChart(createDataset());
		chartComposite = new ChartComposite(body, SWT.None, chart, true);
	}

	private XYDataset createDataset() {

		final XYSeries playerAndSharesSeries = new XYSeries("Shares");
		final XYSeries reconstructionSeries = new XYSeries(
				"Reconstructed Polynom");
		final XYSeries secretSeries = new XYSeries("Secret");
		BigInteger[] coef = reconstructedPolynom.getCoef();
		BigInteger y = BigInteger.ZERO;

		for (int i = 0; i < playerID.length && playerID[i] != 0; i++) {
			playerAndSharesSeries.add(playerID[i], shares[i]);
		}
		for (int i = 0; i <= playerID[playerID.length - 1]; i++) {
			for (int j = 0; j < coef.length; j++) {
				y = y.add(coef[j].multiply(new BigInteger(i + "").pow(j)));
			}
			reconstructionSeries.add(i, y);
			y = BigInteger.ZERO;
		}

		secretSeries.add(0, secret);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(playerAndSharesSeries);
		dataset.addSeries(reconstructionSeries);
		dataset.addSeries(secretSeries);

		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...

		final JFreeChart chart = ChartFactory.createXYLineChart("", // chart
																	// title
				"", // x axis label
				"", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				false, // tooltips
				false // urls
				);
		// XYSplineRenderer -- show data points
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		// show no line
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesLinesVisible(3, false);
		// show no points
		renderer.setSeriesShapesVisible(1, false);

		// set range of axis
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		domain.setRange(-0.1, playerID[playerID.length - 1] + 0.1);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setVerticalTickLabels(false);

		// display value
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(0);
		XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
				StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT, format,
				format);
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);

		plot.setRenderer(renderer);

		return chart;

	}

}
