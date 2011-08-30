package org.jcryptool.analysis.kegver.layer3;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenData;

public interface CABehavior {

	public KegverData getKegverData();

	public UnigenData getUniGenData();

	public KegverGroup getKegVerGroup();

	public String toString_();

	public UnigenData getUnigenData();

	public BigInteger calc_u();

	public boolean verifyPOK_o();

	public boolean caVerifiesPOK_z();

	public boolean verifyPOK_Cp();

	public boolean verifyPOK_Cq();

	public boolean verifyPOK_N();

	public boolean executeBlum_n();
}
