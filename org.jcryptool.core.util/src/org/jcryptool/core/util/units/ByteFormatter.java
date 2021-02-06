package org.jcryptool.core.util.units;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Methods for nicely formatting bytes. This interface is applied in
 * {@link UnitsService} and a implementation is available at
 * {@link DefaultByteFormatter}, which can be additionally parameterized with a
 * nice builder {@link new
 * DefaultByteFormatter.Builder().yourSettingsHere().build()} This should be
 * enough for everyday requirements in JCrypTool. Take a look at those classes
 * if you need more information.
 */
public interface ByteFormatter {

    public String format(long bytes);

    public String format(int bytes);

    public String format(short bytes);

    public String format(byte bytes);

    public String format(char bytes);

    public String format(BigDecimal bytes);

    public String format(BigInteger bytes);

}
