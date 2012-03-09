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
package org.jcryptool.visual.verifiablesecretsharing.algorithm;

import java.math.BigInteger;

/**
 * Implements the algorithms for the Verifiable Secret Sharing.
 * 
 * @author Dulghier Christoph, Reisinger Kerstin, Tiefenbacher Stefan, Wagner Thomas
 *
 */
public class VerifiableSecretSharing {
	
	/*Commitments for the output*/
	private int[] commitments;
	/*Shares for the output without modulo prime p*/
	private double[] shares;
	/*Shares for the output with modulo prime p*/
	private int[] sharesModQ;
	/*Shares for the calculation*/
	private BigInteger[] sharesBig;
	/*Commitments for the calculation*/
	private BigInteger[] commitmentsBig;
	
	/**
	 * Calculates the function f(x) = s + ax + bx^2 + ... + ix^i.
	 * 
	 * @param coefficients --> a, b, ..., i
	 * @param x --> playerId
	 * @return y --> result of the function, for the chosen x
	 */
	private BigInteger calculatePolynom(int[] coefficients, int x){	
		BigInteger y = new BigInteger(coefficients[0]+"");
		
		for(int i=1; i<coefficients.length;i++){
			y = y.add(new BigInteger(coefficients[i]+"").multiply(new BigInteger(x+"").pow(i)));
		}
		return y;
	}
	
	/**
	 * Calculates the shares for all players.
	 * 
	 * share(i) = P(i) mod p [P(x) is the polynomial in calculatePolynom]
	 * 
	 * Sets the global arrays:
	 * double[] shares, int[] sharesModP and BigInteger[] sharesBig.
	 * 
	 * @param coefficients --> a, b, ..., i
	 * @param q --> biggest prime-factor of p-1
	 * @param n --> number of players
	 */
	public void calculateShares(int[] coefficients, int q, int n){
		/*
		 * shares is needed for the output without modulo prime q.
		 * sharesModP is for the output with modulo prime q.
		 * sharesBig is needed for the calculation .
		 * (BigInteger is used, because of the maxValue of double and int)
		 */
		double[] shares = new double[n];
		BigInteger[] sharesBig = new BigInteger[n];
		int[] sharesModQ = new int[n];
		
		for(int i=0, y =1; i < n; i++,y++){
			sharesBig[i] = calculatePolynom(coefficients,y);
			shares[i] = sharesBig[i].doubleValue();
			sharesModQ[i] = (sharesBig[i].mod(new BigInteger((q)+""))).intValue();
		}
		setShares(shares);
		setSharesModQ(sharesModQ);
		setSharesBig(sharesBig);

	}
	
	/**
	 * Calculates the commitments:
	 * 
	 * commitment(i) = g^coefficient(i) mod p
	 * 
	 * Sets the global arrays:
	 * int[] commitments and BigInteger[] commitmentsBig.
	 * 
	 * @param g --> element of Zp* with oder q [q is the biggest primefactor of p]
	 * @param coefficients --> a, b, ..., i
	 * @param p --> prime module
	 * @return commitments --> array including all commitments
	 */
	public int[] commitment(int g, int[] coefficients, int p){
		/*commitments is needed for the output.
		 *commitmentsBig is needed for the calculation.
		 */
		int[] commitments = new int[coefficients.length];
		BigInteger[] commitmentsBig = new BigInteger[coefficients.length];
		
		for(int i=0; i<coefficients.length; i++){
			commitmentsBig[i] = new BigInteger(g+"").pow(coefficients[i]);
			commitmentsBig[i] = commitmentsBig[i].mod(new BigInteger(p+""));
			commitments[i] = commitmentsBig[i].intValue();
		}
		
		setCommitments(commitments);
		setCommitmentsBig(commitmentsBig);
		return commitments;
	}
	
	/**
	 * Calculates the check and compares: 
	 *                  
	 *             (t-1)__
	 * g^Share[i] ?=    || y(j)^(PlayerId^j)
	 *                (j=0) 
	 *                
	 * @param g --> element of Zp* with oder q [q is the biggest primefactor of p]
	 * @param p --> prime
	 * @param playerId
	 * @return true --> check OK; 
	 * 		   false --> check not OK;
	 */
	public boolean check(int g, int p, int playerId){
		int[] sharesModQ = getSharesModQ();
		BigInteger[] sharesBig = new BigInteger[sharesModQ.length];
		for(int i=0; i<sharesModQ.length; i++){
			sharesBig[i] = new BigInteger(sharesModQ[i]+"");
		}
		
		BigInteger[] commitmentsBig = getCommitmentsBig();
		
		boolean checked = false;
		
		BigInteger lValue;
		
		/*Calculates the left Value of the formula.
		 *Reduces the exponent if the exponent and p are relatively prime.
		 */
		if(sharesBig[playerId-1].mod(new BigInteger(p+"")).compareTo(new BigInteger(0+"")) != 0){
			BigInteger help = sharesBig[playerId-1].mod(new BigInteger((p-1)+""));
			lValue = new BigInteger(g+"").modPow(help, new BigInteger(p+""));
		}
		else{
			lValue = new BigInteger(g+"").modPow(sharesBig[playerId-1], new BigInteger(p+""));
		}

		BigInteger rValue = new BigInteger("1");
		
		/*Calculates the left Value of the formula.
		 *Reduces the exponent if the exponent and p are relatively prime.
		 */
		for(int j=0; j<commitmentsBig.length; j++){
			BigInteger help = new BigInteger(playerId+"").pow(j);
			
			if(help.mod(new BigInteger(p+"")).compareTo(new BigInteger(0+"")) != 0){
				help = help.mod(new BigInteger((p-1)+""));
				rValue = rValue.multiply(commitmentsBig[j].modPow(help, new BigInteger(p+"")));
				rValue = rValue.mod(new BigInteger(p+""));
			}
			else{
				rValue = rValue.multiply(commitmentsBig[j].modPow(help, new BigInteger(p+"")));
				rValue = rValue.mod(new BigInteger(p+""));
			}
		}
		
		if(lValue.compareTo(rValue) == 0){
			checked = true;
		}
		return checked;	
	}
	
	/**
	 *Calculates and reconstructs the polynomial with the Langrange Interpolation.
	 *
	 *		(u)			
	 *		 ---		_____
	 *		 \			|   |	  x - x(l)	
	 *f(x) = /	N(k) * 	|   |   ___________
	 *		 ---	  	|	|	x(k) - x(l)
	 *		(k=1)	  (1<=l<=u)
	 * 				   (l!=k)
	 * 
	 * Result is the polynomial P and the coefficient a(0) is the secret.
	 * 
	 * @param playerIds --> from the players selected for the reconstruction
	 * @param q --> biggest prime-factor of p-1
	 * @return polynomial as String
	 */
	public Polynomial reconstruct(int[] playerIds, int q){
		int[] sharesModQ = getSharesModQ();
		int u = playerIds.length;
		
		BigInteger[] helpCoef = {BigInteger.ZERO,BigInteger.ONE};
		Polynomial x = new Polynomial(helpCoef);
		Polynomial resMul = new Polynomial(new BigInteger[]{BigInteger.ONE});
		Polynomial resAdd = new Polynomial(new BigInteger[]{BigInteger.ZERO});
		int inverse;

		for (int k = 0; k < u; k++) {
			for (int l = 0; l < u; l++) {
				if (l != k) {
					helpCoef[0] = BigInteger.ZERO.subtract(new BigInteger(""+playerIds[l]));
					x = new Polynomial(helpCoef).mod(q);
					inverse = new BigInteger(((playerIds[k]) - (playerIds[l]))
							+ "").modInverse(new BigInteger(q + "")).intValue();
					x = x.times(inverse).mod(q);
					resMul = resMul.times(x);
					resMul = resMul.mod(q);
				}
			}
			resMul = resMul.times(sharesModQ[playerIds[k] - 1]).mod(q);
			resAdd = resAdd.add(resMul).mod(q);
			resMul = new Polynomial(new BigInteger[]{BigInteger.ONE});
		}
		return resAdd;

	}
	
	/**
	 * Getter for sharesBig.
	 * @return  sharesBig --> BigInteger array including all shares without modulo p
	 */
	public BigInteger[] getSharesBig() {
		return sharesBig;
	}

	/**
	 * Setter for sharesBig.
	 * @param sharesBig --> BigInteger array including all shares without modulo p
	 */
	public void setSharesBig(BigInteger[] sharesBig) {
		this.sharesBig = sharesBig;
	}

	/**
	 * Getter for sharesModP.
	 * @return  sharesModP --> int array including all shares with modulo p
	 */
	public int[] getSharesModQ() {
		return sharesModQ;
	}

	/**
	 * Setter for sharesModP.
	 * @param sharesModP --> int array including all shares with modulo p
	 */
	public void setSharesModQ(int[] sharesModP) {
		this.sharesModQ = sharesModP;
	}
	
	/**
	 * Setter resetting an element in sharesModP.
	 * @param i --> index
	 * @param x --> value of the element
	 */
	public void setSharesModQ(int i, int x){
		this.sharesModQ[i] = x;
	}

	/**
	 * Getter for shares.
	 * @return  shares --> int array including all shares without modulo p
	 */
	public double[] getShares() {
		return shares;
	}

	/**
	 * Setter for shares.
	 * @param shares --> int array including all shares without modulo p
	 */
	public void setShares(double[] shares) {
		this.shares = shares;
	}

	/**
	 * Getter for commitments.
	 * @return  commitments --> int array including all commitments
	 */
	public int[] getCommitments() {
		return commitments;
	}

	/**
	 * Setter for commitments.
	 * @param  commitments --> int array including all commitments
	 */
	public void setCommitments(int[] commitments) {
		this.commitments = commitments;
	}

	/**
	 * Getter for commitmentsBig.
	 * @return  commitmentsBig --> BigInteger array including all commitments
	 */
	public BigInteger[] getCommitmentsBig() {
		return commitmentsBig;
	}

	/**
	 * Setter for commitmentsBig.
	 * @param  commitmentsBig --> BigInteger array including all commitments
	 */
	public void setCommitmentsBig(BigInteger[] commitmentsBig) {
		this.commitmentsBig = commitmentsBig;
	}
}
