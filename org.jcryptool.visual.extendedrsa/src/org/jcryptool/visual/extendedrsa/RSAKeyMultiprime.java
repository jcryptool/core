package org.jcryptool.visual.extendedrsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

public class RSAKeyMultiprime implements RSAKey{
	
	
	private BigInteger e, n, phin, d;
	private HashMap<Prime, Boolean> enabledPrimeMask;
	private LinkedList<Prime> enabledPrimes;
	
	private Map<Prime, BigInteger> primeMap;	

	public RSAKeyMultiprime(Prime[] enabledPrimes) {
		this.enabledPrimes = new LinkedList<Prime>();
		enabledPrimeMask = new HashMap<Prime, Boolean>();
		primeMap = new HashMap<RSAKey.Prime, BigInteger>();
        primeMap.put(Prime.P, null);
        primeMap.put(Prime.Q, null);
        primeMap.put(Prime.R, null);
        primeMap.put(Prime.S, null);
        primeMap.put(Prime.T, null);

		var tmpList = Arrays.asList(enabledPrimes);
		for (var prime : Prime.values()) {
			if (tmpList.contains(prime)) {
				this.enabledPrimeMask.put(prime, true);
				this.enabledPrimes.add(prime);
			} else {
				this.enabledPrimeMask.put(prime, false);
			}
		}
	}
	
	@Override
	public void setPrime(Prime prime, BigInteger value) {
		checkAndSet(prime, value);
		
		if(allPrimesSet()) {
			calculateN();
		}
	}

	@Override
	public BigInteger getPrime(Prime prime) {
		return checkAndGet(prime);
	}

	@Override
	public boolean isPrimeSet(Prime prime) {
		return checkAndGet(prime) != null;
	}

	@Override
	public boolean allPrimesSet() {
		for (var prime : primes()) {
			if (prime == null || prime.compareTo(one) < 0)
				return false;
		}
		return true;
	}
	
	@Override
	public void setE(BigInteger e) {
		this.e = e;
	}
	
	@Override
	public BigInteger getE() {
		return e;
	}
	
	@Override
	public boolean isESet() {
		return e != null;
	}
	
	@Override
	public void calculateN() {
		if (!allPrimesSet())
			return;
		n = one;
		for (var prime : primes())
			n = n.multiply(prime);
	
		phin = one;
		for (var prime : primes())
			phin = phin.multiply(prime.subtract(one));
	}
	
	@Override
	public void resetN() {
		n = null;
		phin = null;
	}
	
	@Override
	public BigInteger getN() {
		if (n != null)
			return n;
		else
			return Constants.MINUS_ONE;
	}
	
	@Override
	public BigInteger getPhiN() {
		if (phin != null)
			return phin;
		else
			return Constants.MINUS_ONE;
	}
	
	@Override
	public boolean isNSet() {
		return n != null && phin != null;
	}
	
	@Override
	public void calculateD() {
		if (isValid()) 
			d = e.modInverse(phin);
		else
			d = null;
	}
	
	@Override
	public BigInteger getD() {
		if (isSetD())
			return d;
		if (isValid()) {
			calculateD();
			return d;
		}
		return null;
	}
	
	@Override
	public boolean isSetD() {
		return d != null;
	}
	
	@Override
	public boolean isValid() {
		return isNSet() && isESet();
	}

	@Override
	public void completeReset() {
        primeMap.put(Prime.P, null);
        primeMap.put(Prime.Q, null);
        primeMap.put(Prime.R, null);
        primeMap.put(Prime.S, null);
        primeMap.put(Prime.T, null);
        setE(null);
        resetN();
        d = null;
	}
	
	@Override
	public BigInteger getRandomE() {
		var randomValue = RandomBigInteger(phin);

		// Set the stopping point to the value below the random one
		// In the edge case of random being 2, set it to (phin - 1)
		BigInteger stopPoint;
		if (randomValue.compareTo(two) <= 0) {
			stopPoint = phin.subtract(one);
		}
		else {
			stopPoint = randomValue.subtract(one);
		}
		
		// Iterate over all numbers beginning from the starting point.
		// Return the first valid number found.
		while (randomValue.compareTo(stopPoint) != 0) {
			var gcd = randomValue.gcd(phin);
			if (gcd.compareTo(one) == 0) {
				return randomValue;
			}
			randomValue = randomValue.add(one);
			// If we hit the maximum (phi(n)), we just go to 2
			if (randomValue.compareTo(phin) >= 0) {
				randomValue = new BigInteger("2");
			}
		}
		return Constants.MINUS_ONE;
	}
	
	@Override
	public TreeSet<BigInteger> getPossibleEs() {
		var output = new TreeSet<BigInteger>();
	
		// Return an empty list if this is invoked illegaly.
		if (!isNSet())
			return output;

		var phin = getPhiN();
	
		// First check out the basic Fermat's primes.
		for (var prime : possiblePrimesE) {
			var gcd = prime.gcd(phin);
			if (!(prime.compareTo(phin) >= 0 || gcd.compareTo(one) != 0)) {
				output.add(prime);
			}
		}
	
		// If there are very few E's as Fermat's prime, we try some normal numbers,
		// until the list is reasonably filled.
		if (output.size() < 10) {
			var primes = Lib.PRIMES;
			var it = primes.iterator();
			// We have to skip first element, because this List contains 1 as prime.
			it.next();  
			while (it.hasNext()) {
				var number_as_int = it.next();
				var number = new BigInteger(number_as_int.toString());

				// Exit if we have enough or can't find more numbers.
				if (output.size() >= 10 || number.compareTo(phin) >= 0) {
						break;
				}
				
				// Check if valid e.
				var gcd = number.gcd(phin);
				if (gcd.compareTo(one) == 0) {
					output.add(number);
				}
			}
		}
		return output;
	}
	
	@Override
	public LinkedList<Prime> getEnabledPrimes() {
		return enabledPrimes;
	}

	private BigInteger checkAndGet(Prime prime) {
		if (!enabledPrimeMask.get(prime)) 
			throw new IllegalArgumentException(
					"Requested illegal prime " + prime.name() + " which was not initialized in constructor"
			);
		return primeMap.get(prime);
	}
	
	private void checkAndSet(Prime prime, BigInteger value) {
		if (!enabledPrimeMask.get(prime)) 
			throw new IllegalArgumentException(
					"Requested illegal prime " + prime.name() + " which was not initialized in constructor"
			);
		primeMap.put(prime, value);
	}
	
	
	/**
	 * Provide an iterable over the given primes.
	 * 
	 * @return an object of type Iterable
	 */
	private Iterable<BigInteger> primes() {
		var base_it = enabledPrimes.iterator();
		Iterator<BigInteger> it = new Iterator<BigInteger>() {

			@Override
            public boolean hasNext() {
                return base_it.hasNext(); 
            }

            @Override
            public BigInteger next() {
                return primeMap.get(base_it.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

		var iterable = new Iterable<BigInteger>() {
			@Override
			public Iterator<BigInteger> iterator() { 
				return it; 
			} 
		};
		return iterable;
	}
	
	
	private BigInteger RandomBigInteger(BigInteger upperLimit) {
		BigInteger randomNumber;
		var randomSource = new Random();
		do {
			randomNumber = new BigInteger(upperLimit.bitLength(), randomSource);
		} while (randomNumber.compareTo(upperLimit) >= 0 || randomNumber.compareTo(one) <= 0);
		return randomNumber;
	}

}