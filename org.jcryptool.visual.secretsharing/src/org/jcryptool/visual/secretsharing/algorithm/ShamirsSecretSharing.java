//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.algorithm;

import java.math.BigInteger;
import java.util.Vector;

import org.jcryptool.visual.secretsharing.views.Constants.Mode;

/**
 * @author Oryal Inel
 * @version 1.0
 */
public class ShamirsSecretSharing {
    private BigInteger modul;
    private BigInteger[] coefficient;
    private Point[] shares;
    private Vector<BigInteger[]> subPolynomial;
    private Mode mode;

    /**
     * @param numberOfPersons
     * @param numberOfNecessaryPersons
     * @param modul
     */
    public ShamirsSecretSharing(BigInteger numberOfNecessaryPersons, BigInteger numberOfPersons, BigInteger modul, Mode mode) {
        this.modul = modul;
        this.shares = new Point[numberOfPersons.intValue()];
        this.mode = mode;
    }

    /**
     * compute the corresponding shares to the given points
     *
     * @param p is the point set
     * @return the shares to the given points in an array
     */
    public Point[] computeShares(Point p[]) {

        for (int i = 0; i < p.length; i++) {

            BigInteger value = BigInteger.ZERO;
            for (int j = coefficient.length - 1; j >= 0; j--) {
                value = value.multiply(p[i].getX());
                value = value.add(coefficient[j]);
            }

            switch (mode) {
                case NUMERICAL:
                    p[i].setY(value.mod(modul));
                    break;

                case GRAPHICAL:
                    p[i].setY(value);
                    break;
            }
        }
        shares = p;

        return shares;
    }

    /**
     * compute the corresponding share to the given point value
     *
     * @param p the point which y value is computing
     * @return the point p which represent the share
     */
    public Point computeSharePoint(Point p) {
        BigInteger value = BigInteger.ZERO;
        for (int j = coefficient.length - 1; j >= 0; j--) {
            value = value.multiply(p.getX());
            value = value.add(coefficient[j]);
        }

        switch (mode) {
            case NUMERICAL:
                p.setY(value.mod(modul));

                break;

            case GRAPHICAL:
                p.setY(value);

                break;
        }

        return p;
    }

    /**
     * @param pointSet
     * @param modul
     * @return
     */
    public BigInteger interpolate(Point[] pointSet, BigInteger modul) {
        BigInteger s[] = new BigInteger[pointSet.length];
        BigInteger tmpZaehler;
        BigInteger tmpNenner;
        BigInteger tmpQuotient;
        BigInteger tmpValue = BigInteger.ZERO;

        for (int i = 0; i < pointSet.length; i++) {
            s[i] = BigInteger.ONE;
        }

        for (int i = 0; i < pointSet.length; i++) {
            for (int j = 0; j < pointSet.length; j++) {
                if (i != j && pointSet[i] != null && pointSet[j] != null) {
                    tmpZaehler = pointSet[j].getX();
                    tmpNenner = pointSet[j].getX().subtract(pointSet[i].getX());
                    switch (mode) {
                        case NUMERICAL:
                            tmpNenner = tmpNenner.modInverse(modul);
                            tmpQuotient = tmpZaehler.multiply(tmpNenner);
                            s[i] = s[i].multiply(tmpQuotient).mod(modul);

                            break;

                        case GRAPHICAL:
                            tmpQuotient = tmpZaehler.multiply(tmpNenner);
                            s[i] = s[i].multiply(tmpQuotient);

                            break;
                    }
                }
            }
        }

        for (int i = 0; i < pointSet.length; i++) {
            if (pointSet[i] != null) {
                switch (mode) {
                    case NUMERICAL:
                        tmpValue = tmpValue.add(s[i].multiply(pointSet[i].getY())).mod(modul);
                        break;

                    case GRAPHICAL:
                        tmpValue = tmpValue.add(s[i].multiply(pointSet[i].getY()));
                        break;
                }
            }
        }

        return tmpValue;

    }

    /**
     * @param pointSet the point which have to interpolated
     * @param modul
     * @return the coefficients of the polynomial
     */
    public BigInteger[] interpolatePoints(Point[] pointSet, BigInteger modul) {
        BigInteger first[];
        BigInteger second[];
        BigInteger[] product;
        BigInteger[] tmpNumerical;
        coefficient = new BigInteger[pointSet.length];
        BigInteger tmpNennerNumerical = BigInteger.ONE;
        Vector<BigInteger[]> equationSet = new Vector<BigInteger[]>();

        if (pointSet.length >= 2) {
            for (int i = 0; i < coefficient.length; i++) {
                coefficient[i] = BigInteger.ZERO;
            }

            subPolynomial = new Vector<BigInteger[]>();

            for (int i = 0; i < pointSet.length; i++) {
                for (int j = 0; j < pointSet.length; j++) {
                    if (i != j) {
                        tmpNumerical = new BigInteger[2];

                        tmpNennerNumerical = (pointSet[i].getX().subtract(pointSet[j].getX())).modInverse(modul);
                        tmpNumerical[0] = (tmpNennerNumerical.multiply(pointSet[j].getX().negate())).mod(modul);
                        tmpNumerical[1] = tmpNennerNumerical;
                        equationSet.add(tmpNumerical);
                    }
                }

                while (equationSet.size() > 1) {
                    first = equationSet.remove(0);
                    second = equationSet.remove(0);

                    equationSet.add(product(first, second));
                }
                product = equationSet.remove(0);
                for (int j = 0; j < product.length; j++) {
                    product[j] = product[j].multiply(pointSet[i].getY()).mod(modul);
                }

                subPolynomial.add(product);
            }

            for (int j = 0; j < coefficient.length; j++) {
                for (BigInteger[] value : subPolynomial) {
                    coefficient[j] = coefficient[j].add(value[j]).mod(modul);
                }
            }
        }

        return coefficient;
    }

    /**
     * Interpolation for double values used for the graphical visualization
     *
     * @param x is the x values of the point
     * @param y is the y values of the point, f(x)
     * @param coe is the interpolated polynomial
     */
    public BigInteger[] interpolation(Point pointSet[]) {
        double s[] = new double[pointSet.length];
        double coe[] = new double[pointSet.length];
        BigInteger result[] = new BigInteger[pointSet.length];
        int n = pointSet.length - 1;

        for (int i = 0; i <= n; i++)
            s[i] = pointSet[i].getY().doubleValue();
        for (int i = 1; i <= n; i++) {
            for (int j = n; j >= i; j--) {
                s[j] = (s[j] - s[j - 1]) / (pointSet[j].getX().doubleValue() - pointSet[j - i].getX().doubleValue());
            }
        }
        for (int i = 0; i <= n; i++)
            coe[i] = s[i];
        for (int i = 0; i <= n - 1; i++) {
            for (int j = n - 1; j >= i; j--) {
                coe[j] = coe[j] - pointSet[j - i].getX().doubleValue() * coe[j + 1];
            }
        }

        for (int i = 0; i < coe.length; i++) {
            result[i] = new BigInteger(String.valueOf((int) Math.ceil(coe[i])));
        }

        return result;

    }

    /**
     * returns the coefficient array which represent the polynomial
     *
     * @return the coefficient array
     */
    public BigInteger[] getCoefficient() {
        return coefficient;
    }

    /**
     * sets the coefficients of the polynomial
     *
     * @param coefficient array
     */
    public void setCoefficient(BigInteger[] coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * Computes the product of two polynomials in BigInteger type
     *
     * @param a coefficients of polynomial a
     * @param b coefficients of polynomial b
     * @return the product of two polynomials
     */
    private BigInteger[] product(BigInteger a[], BigInteger b[]) {
        BigInteger result[] = new BigInteger[a.length + b.length - 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = BigInteger.ZERO;
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                switch (mode) {
                    case NUMERICAL:
                        result[i + j] = (result[i + j].add(a[i].multiply(b[j]))).mod(modul);

                        break;

                    case GRAPHICAL:
                        result[i + j] = (result[i + j].add(a[i].multiply(b[j])));

                        break;
                }
            }
        }
        return result;
    }

    /**
     * Get the sub polynomials to display in the reconstruction view
     *
     * @return the subpolynomial
     */
    public Vector<BigInteger[]> getSubPolynomialNumerical() {
        return subPolynomial;
    }

}
