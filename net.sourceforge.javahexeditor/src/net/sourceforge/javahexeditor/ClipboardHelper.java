package net.sourceforge.javahexeditor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.utils.LogUtil;

import net.sourceforge.javahexeditor.plugin.editors.HexEditor;

public final class ClipboardHelper {
	
	/**
	 * The systems clipboard
	 */
	private static Clipboard clipboard = new Clipboard(Display.getCurrent());
	
	/**
	 * Copies the selected hex to the clipboard.
	 * @param content The content of the editor.
	 * @param start The position to start coping data.
	 * @param length The amount of data to be copied.
	 * @return True, if data is copied to the clipboard, false if no data is copied,
	 * due to an exception or no data selected.
	 */
	public static boolean setHexContentToClipboard(BinaryContent content, long start, long length) {
		
		if (length < 1L) {
			return false;
		}
		
		Object[] data = null;
		Transfer[] transfers = null;
		
		try {
			byte[] byteArrayData = new byte[(int) length];
			content.get(ByteBuffer.wrap(byteArrayData), start);
			StringBuilder sb = new StringBuilder();
			
			int tempByteValue;
			for (Byte b : byteArrayData) {
				// Sometimes the integer representation of the hex values are negative
				// due to javas signed ints. This causes problems when using Integer.toHexString.
				// There I exlicitly convert them to an unsigned int. 
				tempByteValue = Byte.toUnsignedInt(b);
				// This adds a preceding '0' when the hex value is less than F.
				if (tempByteValue <= 16) {
					sb.append(0);
				}
				sb.append(Integer.toHexString(tempByteValue).toUpperCase());
			}
			
			transfers = new Transfer[] { TextTransfer.getInstance() };
			data = new Object[] { sb.toString() };
			
		} catch (IOException e) {
			LogUtil.logError(HexEditor.ID, e);
			return false;
		}
		
		clipboard.setContents(data, transfers);
		return true;
	}
	
	
	/**
	 * Copies the selected text to the clipboard.
	 * @param content The content of the editor.
	 * @param start The position to start coping data.
	 * @param length The amount of data to be copied.
	 * @return True, if data is copied to the clipboard, false if no data is copied,
	 * due to an exception or no data selected.
	 */
	public static boolean setContentsText(BinaryContent content, long start, long length) {
		if (length < 1L) {
			return false;
		}

		Object[] data = null;
		Transfer[] transfers = null;
		
		try {
			byte[] byteArrayData = new byte[(int) length];
			content.get(ByteBuffer.wrap(byteArrayData), start);
			String textData = new String(byteArrayData);
			transfers = new Transfer[] { TextTransfer.getInstance() };
			data = new Object[] { textData };
		} catch (IOException e) {
			LogUtil.logError(HexEditor.ID, e);
			return false;
		}
		clipboard.setContents(data, transfers);
		return true;
	}
	
	/**
	 * Checks if pasteable content is in the clipboard. Files or texts.
	 * @return True, if a file or a text is in the clipboard.
	 */
	public static boolean hasContents() {
		
		TransferData[] available = clipboard.getAvailableTypes();
		
		for (int i = 0; i < available.length; ++i) {
			if (TextTransfer.getInstance().isSupportedType(available[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Paste hex chars to the editor
	 * @param content The content of the editor
	 * @param start Point to start inserting
	 * @param insert Insert or overwrite data.
	 * @return Length of the inserted text. -1 for error and 0 if nothing happend.
	 */
	public static long tryGettingHex(BinaryContent content, long start, boolean insert) {
		// Seems to be used to copy data from the "real" clipboard
		// to javahexeditor.
		byte[] byteArray = null;
			String text = (String) clipboard.getContents(TextTransfer.getInstance());
			System.out.println("Text from clipboard: " + text);
			if (text != null) {
					// Paste the hex values to the editor
					
					// Remove all non hex chars from the string.
					String onlyHexChars = text.replaceAll("[^a-eA-E0-9]", "");

					// ungerade anzahl an hex Zeichen -> kein komplettes letztes Byte.
					// Zum Beispiel 5 Hex zeichen = 2,5 Byte.
					// Pad a trailing 0 to the hex chars to fill the last byte.
					if (onlyHexChars.length() % 2 == 1) {
						onlyHexChars = onlyHexChars + "0";
					}
					
					try {
						byteArray = Hex.decodeHex(onlyHexChars);
					} catch (DecoderException e) {
						LogUtil.logError(HexEditor.ID, e);
					}
				
			}
		if (byteArray == null) {
			return -1L;
		}

		long total = byteArray.length;
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		if (insert) {
			content.insert(buffer, start);
		} else if (total <= content.length() - start) {
			content.overwrite(buffer, start);
		} else {
			total = 0L;
		}

		return total;
	}
	
	/**
	 * Paste text to the editor
	 * @param content The content of the editor
	 * @param start Point to start inserting
	 * @param insert Insert or overwrite data.
	 * @return Length of the inserted text. -1 for error and 0 if nothing happend.
	 */
	public static long tryGettingText(BinaryContent content, long start, boolean insert) {
		
		// Seems to be used to copy data from the "real" clipboard
		// to javahexeditor.
		byte[] byteArray = null;
			String text = (String) clipboard.getContents(TextTransfer.getInstance());
			if (text != null) {
					// Paste the text representation to the editor.
					byteArray = text.getBytes(Charset.forName("UTF-8"));
				
			}
		if (byteArray == null) {
			return -1L;
		}

		long total = byteArray.length;
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		if (insert) {
			content.insert(buffer, start);
		} else if (total <= content.length() - start) {
			content.overwrite(buffer, start);
		} else {
			total = 0L;
		}

		return total;
	}

}
