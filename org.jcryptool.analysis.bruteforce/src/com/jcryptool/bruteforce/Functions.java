package com.jcryptool.bruteforce;

import java.security.NoSuchAlgorithmException;
import com.jcryptool.bruteforce.windows.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Hex;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.Preferences;

public class Functions {

	private static final String PLUGIN_ID = "com.JCrypTool.Bruteforce";

	public static class General {
		public static void save(String key, String value) {
			Preferences prefs = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
			prefs.put(key, value);
			try {
				prefs.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		public static String load(String key) {
			return load(key, "");
		}
	
		public static String load(String key, String def) {
			Preferences prefs = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
			return prefs.get(key, def);
		}
		
		public static double entropie(String s){
			char[] c=s.toCharArray();
			List<Character> l=new ArrayList<Character>();
			for(int i=0;i<c.length;i++){
				l.add(c[i]);
			}
			for(int i=0;i<l.size();i++){
				if(i!=l.lastIndexOf(l.get(i))){
					l.remove(i);
					i--;
				}
			}
			return ((double)s.length()/l.size());
		}
		
		public static double charsetPercentage(String s){
			int a=0;
			char[] c=s.toCharArray();
			for(int i=0;i<c.length;i++){
				if((int)c[i]>32 && (int)c[i]<125)a++;
			}
			return (double)((double)a/(double)s.length()*100.0);
		}
		
		public static double getTextVal(String s){
			return charsetPercentage(s)*entropie(s);
		}
	}
	
	public static class crypto {

		private static String CrypMode="AES";
		private static int BlockMode=1;
		private static int Padding=0;
		private static String BlockModeArr[]={"","ECB","CBC"};
		private static String PaddingArr[]={"NoPadding","PKCS5Padding"};

		public static final int NO_BLOCK=0;
		public static final int BLOCK_ECB=1;
		public static final int BLOCK_CBC=2;

		public static final int NO_PADDING=0;
		public static final int PKCS5PADDING=0;
	
		public static void setAES(){
			CrypMode="AES";
		}
		
		public static void setDES(){
			CrypMode="DES";
		}
		
		public static void setBlockMode(int bm){
			if(bm < 0 || bm > 2) throw new IllegalArgumentException();
			else BlockMode=bm;
		}
		
		public static void setPadding(int p){
			if(p < 0 || p > 1) throw new IllegalArgumentException();
			else Padding=p;
		}
		
		public static void showSettings(){
			System.out.println("Mode: "+CrypMode);
			System.out.println("Block Mode: "+((BlockModeArr[BlockMode].equals(""))?"No Block":BlockModeArr[BlockMode]));
			System.out.println("Padding: "+PaddingArr[Padding]+"\n");
		}
		
		public static String genKey(){
			KeyGenerator kgen = null;
			try {
				kgen = KeyGenerator.getInstance(CrypMode);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if(CrypMode=="AES")kgen.init(128);
			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
			String key = new String(org.apache.commons.codec.binary.Hex.encodeHex(raw));
			return key;
		}
		
		private static String getTrans(){
			return CrypMode+((BlockMode>0)?"/"+BlockModeArr[BlockMode]+"/"+PaddingArr[Padding]:"");
		}
		
		public static String encrypt(String text,String key){
			byte[] keyByteArray = null;
			String encryptedMessage = null;
			try {
				keyByteArray = org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
				SecretKeySpec skeySpec = new SecretKeySpec(keyByteArray, CrypMode);
				Cipher cipher = Cipher.getInstance(getTrans());
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
				byte[] encrypted = cipher.doFinal(text.getBytes());
				encryptedMessage = new String(org.apache.commons.codec.binary.Hex.encodeHex(encrypted));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return encryptedMessage;
		}
		
		public static String decrypt(String text,String key){
			byte[] keyByteArray = null;
			String originalMessage = null;
			try {
				keyByteArray = org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
				SecretKeySpec skeySpec = new SecretKeySpec(keyByteArray, CrypMode);
				Cipher cipher = Cipher.getInstance(getTrans());
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				originalMessage = new String(cipher.doFinal(Hex.decodeHex(text.toCharArray())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return originalMessage;
		}

	}

	public static class Bruteforce {
		private static char[] possibleChars;
		private static char[] bf;
		private static String encrypted;
		private static String rawKey;
		private static double maxTextVal;
		
		public static void run_e(char[] chars, String rk, String encryp) {
			possibleChars = chars;
			int stringLength=0;
		    rawKey=rk;
			for (int i=-1 ; ( i=rawKey.indexOf("*",i+1)) != -1 ; stringLength++);
		    encrypted=encryp.substring(0, 32);
		    maxTextVal=0;
		    Arrays.sort(possibleChars);
		    bf = new char[stringLength+1];
		    Arrays.fill(bf, 1, bf.length, possibleChars[0]);
			while (bf[0] == 0) {
				if (test()) break;
				increment();
			}
		}
		
		private static void increment() {
			for (int i = bf.length - 1; i >= 0; i--) {
				int index = Arrays.binarySearch(possibleChars, bf[i]);
				if (index < possibleChars.length - 1) {
					bf[i] = possibleChars[index + 1];
					return;
				}
				bf[i] = possibleChars[0];
			}
		}

		private static boolean test(){
			String tempKey=rawKey;
			for(int i=1;i<bf.length;i++){
				String t=String.valueOf(bf[i]);
				tempKey=tempKey.replaceFirst("\\*",t);
			}
			String decrypted=Functions.crypto.decrypt(encrypted, tempKey);
			double temp=Functions.General.getTextVal(decrypted);
			if(temp>maxTextVal){
				System.out.println(temp + " -> " + tempKey);
				GUI.setKey(tempKey);
				GUI.setEnt(temp);
				maxTextVal=temp;
			}
			return false;
		}
	}
}
