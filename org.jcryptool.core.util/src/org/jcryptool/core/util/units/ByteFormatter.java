package org.jcryptool.core.util.units;

import java.math.BigInteger;

public interface ByteFormatter {
	
	public String format(long bytes);
	public String format(int bytes);
	public String format(short bytes);
	public String format(byte bytes);
	public String format(char bytes);
	public String format(BigInteger bytes);

}
