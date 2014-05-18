package org.jcryptool.visual.huffmanCoding.algorithm;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Miray Inel
 *
 */
public class HuffmanStreamWriter extends OutputStream {
	private int buffer = 0;
	private int bufferOffset = 0;
	private OutputStream stream;

	/**
	 * Construct a new HuffmanStreamWriter upon a given OutputStream.
	 * 
	 * @param stream
	 *            the used OutputStream
	 */
	public HuffmanStreamWriter(OutputStream stream) {
		super();
		this.stream = stream;
	}

	/**
	 * Writes a bit to the file.
	 * 
	 * @param bit
	 *            the bit to wirte
	 * @throws IOException
	 */
	private void writeBit(int bit) throws IOException {
		// append bit to buffer
		buffer |= (bit & 1) << (7 - bufferOffset++);
		if (bufferOffset == 8) {
			flushBitBuffer();
		}
	}

	public void flushBitBuffer() throws IOException {
		if (bufferOffset != 0) {
			this.write(buffer);
			// reset buffer & offset
			bufferOffset = 0;
			buffer = 0;
		}
	}

	/**
	 * Writes a BitString Object to the OutputStream
	 * 
	 * @param bitString
	 *            to wirte
	 * @throws IOException
	 */
	public void write(BitString bitString) throws IOException {
		for (boolean bit : bitString.getBitArray()) {
			this.writeBit(bit ? 1 : 0);
		}
	}

	/**
	 * Flushes BitBuffer and closes the stream.
	 * 
	 * To ensure that no bits are "left in buffer" this method should be called
	 * after all write operations are finished.
	 * 
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		// pad buffer with zeros and flush it
		this.flushBitBuffer();
		// execute super method
		super.close();
	}

	/**
	 * Writes a BitString[] representing the code table to the stream.
	 * 
	 * @param table
	 *            to write.
	 * @throws IOException
	 */
	public void writeCodeTable(BitString[] table) throws IOException {
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				this.write(i);
				this.write(table[i].getLength());
				this.write(table[i]);
				this.flushBitBuffer();
			}
		}
		// write end of table
		this.write((char) 0);
		this.write((char) 0);
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int character) throws IOException {
		stream.write(character);
	}
}
