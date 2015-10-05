package org.jcryptool.visual.babystepgiantstep.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Miray Inel
 *
 */
public class BabystepGiantstep {

	private BigInteger zyklischeGruppe;
	private BigInteger erzeuger;
	private BigInteger gruppenElement;

	private BigInteger n; // Gruppenordnung
	private BigInteger m; // obere Schranke fuer Gruppenordnung
	private BigInteger multInv;

	private HashMap<BigInteger, BigInteger> babystepMenge;
	private ArrayList<BigInteger> giantstepMenge;

	private BigInteger x; // = q*m+r
	private BigInteger q;
	private BigInteger r;

	public BabystepGiantstep(BigInteger zyklischeGruppe, BigInteger erzeuger, BigInteger gruppenElement) {
		super();
		this.zyklischeGruppe = zyklischeGruppe;
		this.erzeuger = erzeuger;
		this.gruppenElement = gruppenElement;
		this.n = computeGroupOrder(this.zyklischeGruppe);
		this.m = computeUpperLimit(this.n);
		this.multInv = computeMultInverse(this.zyklischeGruppe);			
		this.babystepMenge = new HashMap<BigInteger, BigInteger>();
		this.giantstepMenge = new ArrayList<BigInteger>();
	}

	public BigInteger getZyklischeGruppe() {
		return zyklischeGruppe;
	}

	public BigInteger getErzeuger() {
		return erzeuger;
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getM() {
		return m;
	}
	

	public HashMap<BigInteger, BigInteger> getBabystepMenge() {
		return babystepMenge;
	}

	public ArrayList<BigInteger> getGiantstepMenge() {
		return giantstepMenge;
	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getQ() {
		return q;
	}

	public BigInteger getR() {
		return r;
	}

	public BigInteger getMultInv() {
		return multInv;
	}

	public BigInteger computeGroupOrder(BigInteger zyklischeGruppe) {
		BigInteger gruppenOrdnung = zyklischeGruppe.subtract(BigInteger.ONE);
	
		return gruppenOrdnung;
	}

	private BigInteger computeUpperLimit(BigInteger gruppenOrdnung) {
		BigInteger wurzel = sqrt(gruppenOrdnung);
		BigInteger obereSchranke = wurzel.add(BigInteger.ONE);
		return obereSchranke;
	}

	private BigInteger computeMultInverse(BigInteger zyklischeGruppe) throws ArithmeticException {
		BigInteger multInv = erzeuger.modInverse(zyklischeGruppe);
		return multInv;
	}

	public void computeBabySteps() {
		BigInteger babystep = gruppenElement.mod(zyklischeGruppe);

		babystepMenge.put(babystep, BigInteger.ZERO);

		for (BigInteger r = BigInteger.ONE; r.compareTo(m) < 0; r = r.add(BigInteger.ONE)) {
			babystep = (babystep.multiply(multInv)).mod(zyklischeGruppe);
			babystepMenge.put(babystep, r);
			if (babystep.equals(BigInteger.ONE)) {
				x = r;

				return;
			}
		}
	}

	public void computeGiantSteps() {
		boolean found = false;
		BigInteger giantstep = null;

		q = BigInteger.ZERO;
		while (!found && q.compareTo(m.subtract(BigInteger.ONE)) < 0) {
			q = q.add(BigInteger.ONE);

			if (q.compareTo(BigInteger.ONE) == 0) {
				giantstep = erzeuger.modPow(m, zyklischeGruppe);				
			}else {
				giantstep = (giantstep.multiply(erzeuger.modPow(m, zyklischeGruppe))).mod(zyklischeGruppe);				
			}

			giantstepMenge.add(giantstep);

			r = babystepMenge.get(giantstep);

			if (r != null) {
				x = q.multiply(m).add(r);
				found = true;
			}
		}

	}

	public void ausgebenGiantsteps(ArrayList<BigInteger> menge) {

		for (int k = 0; k < menge.size(); k++) {
			if (menge.get(k) != BigInteger.ZERO);
		}

	}

	public BigInteger sqrt(BigInteger n) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		while (b.compareTo(a) >= 0) {
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if (mid.multiply(mid).compareTo(n) > 0)
				b = mid.subtract(BigInteger.ONE);
			else
				a = mid.add(BigInteger.ONE);
		}
		return a.subtract(BigInteger.ONE);
	}
}
