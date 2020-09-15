package org.jcryptool.devtools;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Various methods to convert from and to byte representation of user input
 * 
 * @author simlei
 *
 */
public class HexEditorDebugLogic {

	public static void main(String[] args) {
		List<Byte> bytetextToBytes = bytetextToBytes("ff");
		System.out.println(bytetextToBytes);
		String stringrepr = bytesToString(bytetextToBytes);
		System.out.println(stringrepr);
	}

	public static List<Byte> bytetextToBytes(String bytestext) {
				LinkedList<Byte> result = new LinkedList<Byte>();
				String[] split = bytestext.split(" ");
				for(String s : split) {
					if (s.length() == 0) {
						continue;
					}
					if (s.length() != 2) {
						throw new RuntimeException("bytes should be written in pairs of 0-f, e.g. 'a3'");
					}
					int parsedInt = Integer.parseInt(s, 16);
					System.out.println("INT: " + parsedInt);
					byte parsed = (byte) (parsedInt);
					System.out.println("BYT: " + parsed);

//					byte parsed = Byte.parseByte(s, 16);
					result.add(parsed);
				}
				return result;
			}

	public static byte[] listToArr(List<Byte> bytes) {
		byte[] result = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			byte b = bytes.get(i);
			result[i] = b;
		}
		return result;
	}
	public static String bytesToString(List<Byte> content) {
		return bytesToString(listToArr(content));
	}
	public static String bytesToString(byte[] content) {
		LinkedList<String> result = new LinkedList<String>();
		for (byte b : content) {
			String byteToString = byteToString(b);
			result.add(byteToString);
		}
		String collect = result.stream().collect(Collectors.joining(" "));
		return collect;
	}

	private static String byteToString(byte b) {
		String hexString = Integer.toString(Byte.toUnsignedInt(b), 16);
		return hexString.length() == 1 ? "0"+hexString : hexString;
	}

	public static List<Byte> utf8ToBytes(String utf8) {
		LinkedList<Byte> result = new LinkedList<>();
		for(byte byteelement: utf8.getBytes(Charset.forName("UTF-8"))) {
			result.add(byteelement);
		}
		return result;
	}
	
}
