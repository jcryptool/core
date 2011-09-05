// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;

/**
 * An implementation of the protocol described
 * in "Coin Flipping by Telephone" by Manuel Blum,
 * ACM SIGACT News 15 (1), Winter-Spring 1983, pp. 23 - 27.
 *
 * @author Rob Simmons
 * @author Andrew Appel
 **/

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

class Calculations {

    /** A few useful "global" BigIntegers **/
    BigInteger zero = BigInteger.ZERO;
    BigInteger one = BigInteger.ONE;
    BigInteger two =    BigInteger.valueOf(2);
    BigInteger three =  BigInteger.valueOf(3);
    BigInteger four =   BigInteger.valueOf(4);
    BigInteger seven =  BigInteger.valueOf(7);
    BigInteger eight =  BigInteger.valueOf(8);
    BigInteger neg_one = one.negate();

    /** Random number generator (provided by Java) **/
    Random rand = new Random();

    /** Find an acceptable prime (equal to 3 mod 4) **/
    BigInteger makeBlumInt(int size) {
        for(;;) {
            BigInteger p = BigInteger.probablePrime(size,rand);
            if (p.mod(four).equals(three))
                return p;
        }
    }

    /** Calculate x^2 mod n **/
    BigInteger squareMod(BigInteger x, BigInteger n) {
	return x.multiply(x).mod(n);
    }

    /** Calculate the Jacobi Number of x and n **/
    int jacobi(BigInteger x, BigInteger n) {
        if (! x.gcd(n).equals(one)) return 0;
        if (x.equals(one))
            return 1;
        if (x.equals(two)) {
            BigInteger nmod8 = n.mod(eight);
            if (nmod8.equals(one) || nmod8.equals(seven))
                return 1;
            else return -1;
        }
        if (x.compareTo(n) >= 0)
            return  jacobi (x.mod(n), n);
        if (x.mod(two).equals(zero))
            return (jacobi(x.divide(two),n)*jacobi(two,n));
        if (n.mod(two).equals(zero))
            return (jacobi(x,n.divide(two))*jacobi(x,two));
        if (x.mod(four).equals(one) ||
            n.mod(four).equals(one))
            return jacobi(n,x);
        if (x.mod(four).equals(three) &&
            n.mod(four).equals(three))
            return - jacobi(n,x);
        if (x.equals(neg_one)) {
            if (n.mod(four).equals(one))
                return 1;
            else return -1;
        }
        throw new RuntimeException ("Error in jacobi");
    }
}


public class Toss implements ActionListener, CaretListener {

    /** The components of the user interface **/
    JFrame coinFrame;
    JPanel coinPanel, bigPanel;
    JLabel resultLabel, pLabel, qLabel, x2Label;
    JTextField kField, nField, xField, x2modField;
    JButton makeN;

    /** There will be roughly KEYLENGTH*2/3 decimal digits. **/
    final int DEFAULT_KEYLENGTH = 10;
    int KEYLENGTH = DEFAULT_KEYLENGTH;
    final int FIELD_WIDTH = 10;

    /** The three BigIntegers the program keeps track of. **/
    BigInteger n = null, p = null, q = null;
    Calculations calc = new Calculations();

    /** The constructor function starts the user interface. **/
    Toss() {
        // Create and set up the window.
        coinFrame = new JFrame("Coin Toss");
        coinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	coinFrame.setLocation(200, 200);

        // Build the user interface inside of the window - separated
	// because it's long and messy
        buildUserInterface();

	// Receive notification of important events.
	xField.addActionListener(this);
	nField.addActionListener(this);
	makeN.addActionListener(this);
	xField.addCaretListener(this);
	nField.addCaretListener(this);

        // Put the user interface (which is in bigPanel) in the window.
        coinFrame.getContentPane().add(bigPanel, BorderLayout.CENTER);

        // Display the window.
        coinFrame.pack();
        coinFrame.setVisible(true);
    }

    /** Build the user interface. **/
    void buildUserInterface() {
        // Create and set up the panel.
        coinPanel = new JPanel(new GridLayout(6, 1));

	JPanel temp;

        // Create the fields, buttons, and labels we need
	pLabel = new JLabel("    p = ?");
	qLabel = new JLabel("    q = ?");
        x2Label = new JLabel("", SwingConstants.RIGHT);
        kField = new JTextField(5);
        nField = new JTextField(FIELD_WIDTH);
	xField = new JTextField(FIELD_WIDTH);
	resultLabel = new JLabel("");
	makeN = new JButton("Generate N");

	// K
	temp = new JPanel(new FlowLayout());
	temp.add(new JLabel("Bits = ", SwingConstants.RIGHT));
	temp.add(kField);
	temp.add(makeN);
	coinPanel.add(temp);

	// The P and Q indicators
	temp = new JPanel(new GridLayout(1, 2));
	temp.add(pLabel);
	temp.add(qLabel);
	coinPanel.add(temp);

	// N
	temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	temp.add(new JLabel("N = ", SwingConstants.RIGHT));
	temp.add(nField);
	coinPanel.add(temp);

	// X
	temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	temp.add(new JLabel("X = ", SwingConstants.RIGHT));
	temp.add(xField);
	coinPanel.add(temp);

	// X^2 mod N
	temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
	temp.add(new JLabel("    X^2 mod N = "));
	temp.add(x2Label);
	coinPanel.add(temp);

	// Result
	temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
	temp.add(new JLabel("    Jacobi(n,x) = "));
	temp.add(resultLabel);
	coinPanel.add(temp);

	bigPanel = new JPanel(new BorderLayout());
	bigPanel.add(coinPanel, BorderLayout.NORTH);
    }

    /** Listen for text movements in the N & X fields **/
    public void caretUpdate(CaretEvent e) {

	clearResidueAndJacobi();

	if (e.getSource() == nField) {
	    p = null;
	    q = null;
	    pLabel.setText("    p = ?");
	    qLabel.setText("    q = ?");
	}
    }

    /** Listen for Button events. **/
    public void actionPerformed(ActionEvent event) {
	// Make a new n - set p, q, n, and labels.
	if (event.getSource() == makeN) {
	    try {
		KEYLENGTH = Integer.parseInt(kField.getText());
	    } catch (NumberFormatException e) {
		KEYLENGTH = DEFAULT_KEYLENGTH;
		kField.setText(Integer.toString(KEYLENGTH));
	    }
	    p = calc.makeBlumInt(KEYLENGTH);
	    q = calc.makeBlumInt(KEYLENGTH);
	    n = p.multiply(q);

	    pLabel.setText("    p = " + p);
	    qLabel.setText("    q = " + q);

	    nField.removeCaretListener(this);
	    nField.setText(n.toString());
	    nField.addCaretListener(this);

	    xField.setText("");
	    clearResidueAndJacobi();
	}
       else if (event.getSource() == xField ||
		event.getSource() == nField)
	   calculateResidueAndJacobi();
    }

    void clearResidueAndJacobi() {
	    resultLabel.setText("");
	    x2Label.setText("");
    }

    /** Calculate the residue and the jacobi number **/
    void calculateResidueAndJacobi() {
	try {
	    BigInteger n = new BigInteger(nField.getText());
	    BigInteger x = new BigInteger(xField.getText());

	    x2Label.setText(calc.squareMod(x,n).toString());

	    resultLabel.setText("" + calc.jacobi(x, n));
	} catch (NumberFormatException e) {
	    clearResidueAndJacobi();
	}
    }

    /** Run the Toss program **/
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		new Toss();
            }
        });
    }
}
