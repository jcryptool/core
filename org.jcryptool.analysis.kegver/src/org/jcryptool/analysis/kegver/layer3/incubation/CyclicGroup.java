// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.incubation;

import java.util.ArrayList;

public class CyclicGroup {

	private static int[] group;
	private static int primeNumber;
	private static ArrayList<Integer> generators;

	private static boolean isPrime(int n) {
		// 2 is the smallest prime

		if (n <= 2) {
			return n == 2;
		}

		// even numbers other than 2 are not prime

		if (n % 2 == 0) {
			return false;
		}

		// check odd divisors from 3
		// to the square root of n

		for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2) {
			if (n % i == 0) {
				return false;
			}
		}

		return true;
	}

	// find the smallest prime >= n

	private static int getPrime(int n) {
		while (!isPrime(n)) {
			n++;
		}
		return n;
	}

	private static void generateGroup(int inPrime) {
		primeNumber = getPrime(inPrime);
		group = new int[primeNumber - 1];
		for (int i = 0; i < group.length; i++) {
			group[i] = i;
		}
	}

	public static void prepareGenerators(int inPrime) {
		generateGroup(inPrime);
		generators = new ArrayList<Integer>();
		for (int i = 0; i < group.length; i++) {
			boolean generator = true;
			boolean[] elements = new boolean[group.length];
			for (int j = 0; j < group.length; j++)
				elements[j] = false;
			for (int j = 1; j < primeNumber; j++) {
				int temp = (int) Math.pow(group[i], j) % primeNumber;
				for (int k = 0; k < group.length; k++) {
					if (group[k] == temp) {
						if (elements[k] == true) {
							break;
						} else
							elements[k] = true;
					}
				}
			}
			for (int k = 0; k < group.length; k++) {
				if (elements[k] == false)
					generator = false;
			}
			if (generator)
				generators.add(group[i]);
		}
	}

	public static int getGenerator() {
		int r = (int) Math.random();
		int s = generators.size();
		int i = r * s;
		int g = generators.get(i);
		return g;
	}

	public static int getPrimeNumber() {
		return primeNumber;
	}
}