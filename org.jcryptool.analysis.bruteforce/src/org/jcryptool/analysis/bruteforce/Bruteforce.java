package org.jcryptool.analysis.bruteforce;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.utils.LogUtil;

public class Bruteforce {
	private Set<Character> plaintextCharacters;
	private int keySearchSpace;
	private String ciphertext;
	private String keyPattern;
	private View view;
	private char[] possibleCharacters;
	private boolean cancel;

	public Bruteforce(String keyPattern, String ciphertext, View view) {
		this.view = view;
		plaintextCharacters = new HashSet<Character>();
		for(Character c : "0123456789".toCharArray())
			plaintextCharacters.add(c);
		for(Character c : "abcdefghijklmnopqrstuvwxyz".toLowerCase().toCharArray())
			plaintextCharacters.add(c);
		for(Character c : "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray())
			plaintextCharacters.add(c);

		possibleCharacters = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		Arrays.sort(possibleCharacters);

		this.keyPattern = keyPattern;

		int ciphertextLength = ciphertext.length()-ciphertext.length()%32;
		this.ciphertext = ciphertext.substring(0, ciphertextLength);

		keySearchSpace= keyPattern.length() - keyPattern.replaceAll("\\*", "").length();
	}

	public String searchKey() {
		String fillPattern;
		StringBuilder currentKey;
		String mostLikeliKey = "";
		double maxLikelihood = 0;
		cancel = false;
		for(int i=0; i < Math.pow(possibleCharacters.length, keySearchSpace); i++){
			if(cancel)
				break;
			fillPattern = Integer.toHexString(i).toUpperCase();
			for(int l = fillPattern.length(); l < keySearchSpace; l++)
				fillPattern = "0" + fillPattern;

			currentKey = new StringBuilder();
			currentKey.append(keyPattern);
			int fillPosition = 0;
			for(int patternPosition = keyPattern.indexOf('*'); patternPosition < currentKey.length(); patternPosition++){
				if(patternPosition != -1 && currentKey.charAt(patternPosition) == '*'){
					currentKey.replace(patternPosition, patternPosition+1, ""+fillPattern.charAt(fillPosition++));
				}
			}

			String plaintext = decrypt(ciphertext, currentKey.toString());

			double likelihood = calculateLikelihood(plaintext);
			if(likelihood > maxLikelihood){
				maxLikelihood = likelihood;
				mostLikeliKey = currentKey.toString();
				if(view!=null) {
					Display.getDefault().asyncExec(new UpdateKey(mostLikeliKey, maxLikelihood));
				}

			}

			if(view!=null)
				view.setProgress();
		}

		return mostLikeliKey;
	}

	private double calculateLikelihood(String plaintext) {
		Set<Character> allChars = new HashSet<Character>();
		Set<Character> regularChars = new HashSet<Character>();

		for(Character c : plaintext.toCharArray()){
			allChars.add(c);
			if(isPlaintextCharacter(c))
				regularChars.add(c);
		}

		return (double) regularChars.size() / allChars.size();
	}

	private boolean isPlaintextCharacter(Character c) {
		return plaintextCharacters.contains(c);
	}

	private String decrypt(String ciphertext,String key){
		byte[] keyByteArray = hex2byte(key);
		byte[] ciphertextByteArray = hex2byte(ciphertext);

		String originalMessage = null;
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(keyByteArray, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			originalMessage = new String(cipher.doFinal(ciphertextByteArray));
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return originalMessage;
	}

	private byte[] hex2byte(String input){
		byte[] output = new byte[input.length()/2];
		for(int i=0; i<output.length; i++)
			output[i] = (byte) Integer.parseInt(""+input.charAt(2*i)+input.charAt(2*i+1), 16);
		return output;
	}

	protected int getKeySearchSpace() {
		return (int) Math.pow(possibleCharacters.length, keySearchSpace);
	}

	protected void cancel(){
		cancel = true;
	}

	private class UpdateKey implements Runnable {

		private String mostLikeliKey;
		private double likelihood;

		public UpdateKey(String mostLikeliKey, double likelihood) {
			this.mostLikeliKey = mostLikeliKey;
			this.likelihood = likelihood;
		}

		@Override
		public void run() {
			view.setKey(mostLikeliKey, likelihood);
		}

	}
}
