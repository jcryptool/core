package org.jcryptool.visual.extendedrsa;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.HashMap;

import org.jcryptool.visual.extendedrsa.Identity.InputField;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

public class KeyInputValidator {
	
	public class InputResult {
		
		public boolean valid;
		public BigInteger value;
		public String errorMessage;
		public boolean empty;
		
		public InputResult(boolean valid, BigInteger value, String errorMessage, boolean empty) {
			this.valid = valid;
			this.value = value;
			this.errorMessage = errorMessage;
			this.empty = empty;
		}
	}
	
	private Identity parent;
	private final BigInteger minimum = new BigInteger(Messages.Identity_168);
	private final BigInteger one = new BigInteger("1");

	private HashMap<InputField, String> notPrimeMapping;
	
	public KeyInputValidator(Identity parent) {
		this.parent = parent;
		
		notPrimeMapping = new HashMap<InputField, String>() {{
			put(InputField.P, parent.NO_PRIME_P);
			put(InputField.Q, parent.NO_PRIME_Q);
			put(InputField.R, parent.NO_PRIME_R);
			put(InputField.S, parent.NO_PRIME_S);
			put(InputField.T, parent.NO_PRIME_T);
		}};
	}
	
	
	public InputResult validatePrime(InputField field, RSAKey rsaKey, String input) {
		
		var result = new InputResult(false, null, null, false);
	
		// check if input present.
		if (input.length() == 0) {
			result.value = Constants.MINUS_ONE;
			result.valid = false;
			result.errorMessage = parent.NOTHING;
			result.empty = true;
			return result;
		}
		
		result.value = new BigInteger(input);
		
		// Check if value larger than minimum.
		if (result.value.compareTo(minimum) <= 0) {
			result.valid = false;
			result.errorMessage = parent.VALUE_TOO_SMALL;
			return result;
		}
		
		// Check if duplicate
		if (isDuplicatePrime(field, result.value, rsaKey)) {
			result.valid = false;
			result.errorMessage = parent.PRIMES_EQUAL;
			return result;
		}
	
		// Check if prime.
		if (Lib.isPrime(result.value)) {
			result.valid = true;
			result.errorMessage = "";
			return result;
		}
		
		// Here we end up with the case, that the number is in range, but not a prime number.
		result.valid = false;
		result.errorMessage = notPrimeMapping.get(field);
		return result;
	}
	
	public InputResult validateE(RSAKey key, String input) {
		var phin = key.getPhiN();
		var result = new InputResult(false, null, null, false);
	
		// Check if input present.
		if (input.length() == 0) {
			result.value = Constants.MINUS_ONE;
			result.valid = false;
			result.errorMessage = parent.NOTHING;
			result.empty = true;
			return result;
		}
		var possible_e = new BigInteger(input);
	
		// Check if 1 < e < phi(n).
		if (possible_e.compareTo(one) <= 0 || possible_e.compareTo(phin) >= 0) {
			result.value = possible_e;
			result.valid = false;
			result.errorMessage = MessageFormat.format(parent.NO_VALID_E, phin.toString());
			result.empty= false;
			return result;
			
		}
		var gcd = possible_e.gcd(phin);
	
		// Check if relatively prime.
		if (gcd.compareTo(one) != 0) {
			result.value = possible_e;
			result.valid = false;
			var msgArguments = new Object[] {phin.toString(), gcd.toString()};
			result.errorMessage = MessageFormat.format(parent.NO_VALID_GcdE, msgArguments);
			result.empty = false;
			return result;
		}
	
		// Return successfully.
		result.value = possible_e;
		result.valid = true;
		result.errorMessage = "";
		result.empty = false;
		return result;
	}

	/**
	 * Checks if a user wants to set two times the same prime like P=17 and Q=17
	 * 
	 * @return true on already present prime in key, false elsewise
	 */
	private boolean isDuplicatePrime(InputField field, BigInteger value, RSAKey rsaKey) {
		var targetPrime = parent.InputFieldMapping.get(field);
		var enabledPrimes = rsaKey.getEnabledPrimes();
		
		for (var prime : enabledPrimes) {
			if (prime != targetPrime) {
				var primeValue = rsaKey.getPrime(prime);
				if (primeValue != null && value.compareTo(primeValue) == 0) {
					return true;
				}
			}
		}
		return false;
		
	}
}
