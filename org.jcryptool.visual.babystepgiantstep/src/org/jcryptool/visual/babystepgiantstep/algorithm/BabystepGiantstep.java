package org.jcryptool.visual.babystepgiantstep.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

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
		this.multInv = berechneMultiplikativeInverse(this.zyklischeGruppe);
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
		// if (zyklischeGruppe.isProbablePrime(100)) {
		BigInteger gruppenOrdnung = zyklischeGruppe.subtract(BigInteger.ONE);
		// } else {

		// }

		return gruppenOrdnung;
	}

	private BigInteger computeUpperLimit(BigInteger gruppenOrdnung) {
		// double wurzel = Math.sqrt(gruppenOrdnung);
		BigInteger wurzel = sqrt(gruppenOrdnung);
		BigInteger obereSchranke = wurzel.add(BigInteger.ONE);
		return obereSchranke;
	}

	private BigInteger berechneMultiplikativeInverse(BigInteger zyklischeGruppe) {
		BigInteger multInv = erzeuger.modInverse(zyklischeGruppe);
//		System.out.println("die multiplikative Inverse lautet:" + multInv);
		return multInv;
	}

	public void computeBabySteps() {
		BigInteger babystep = gruppenElement.mod(zyklischeGruppe);

		babystepMenge.put(babystep, BigInteger.ZERO);

		for (BigInteger r = BigInteger.ONE; r.compareTo(m) < 0; r = r.add(BigInteger.ONE)) {
			babystep = (babystep.multiply(multInv)).mod(zyklischeGruppe);
			babystepMenge.put(babystep, r);
//			System.out.println(r + " = " + babystep);
			if (babystep.equals(BigInteger.ONE)) {
				x = r;

				return;
			}
		}
	}

	public void computeGiantSteps() {

		BigInteger giantstep = erzeuger.modPow(m, zyklischeGruppe);

		giantstepMenge.add(giantstep);

		boolean found = false;
		q = BigInteger.ONE;
		while (!found && q.compareTo(m.subtract(BigInteger.ONE)) < 0) {

			giantstep = (giantstep.multiply(erzeuger.modPow(m, zyklischeGruppe))).mod(zyklischeGruppe);

			giantstepMenge.add(giantstep);

			r = babystepMenge.get(giantstep);

			if (r != null) {
//				System.out.println("giantstep = babystep = " + giantstep);

				x = q.add(BigInteger.ONE).multiply(m).add(r);
//				System.out.println("x = q*m+r =" + x + "=" + q.add(BigInteger.ONE) + "*" + m + "+" + r);
				found = true;
			}
			q = q.add(BigInteger.ONE);
		}

	}

	public void ausgebenBabysteps(HashMap<BigInteger, BigInteger> menge) {
		Set<Entry<BigInteger, BigInteger>> a = menge.entrySet();

		for (Iterator<Entry<BigInteger, BigInteger>> iterator = a.iterator(); iterator.hasNext();) {
			Entry<BigInteger, BigInteger> entry = iterator.next();

//			System.out.println(entry.getValue() + " = " + entry.getKey());

		}
	}

	public void ausgebenGiantsteps(ArrayList<BigInteger> menge) {

		for (int k = 0; k < menge.size(); k++) {
			if (menge.get(k) != BigInteger.ZERO);
//				System.out.println("[" + k + "]=" + menge.get(k));
		}

	}

	public BigInteger sqrt(BigInteger n) {
		int i = 0;
		BigInteger a = BigInteger.ONE;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		while (b.compareTo(a) >= 0) {
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if (mid.multiply(mid).compareTo(n) > 0)
				b = mid.subtract(BigInteger.ONE);
			else
				a = mid.add(BigInteger.ONE);
			
			i++;
		}
//		System.out.println("counter " + i);
		return a.subtract(BigInteger.ONE);
	}

	public static void main(String[] args) {

		final BigInteger zyklischeGruppe = new BigInteger("23");
		final BigInteger erzeuger = new BigInteger("3");
		final BigInteger gruppenElement = new BigInteger("69");

		BabystepGiantstep babystepgiantstep = new BabystepGiantstep(zyklischeGruppe, erzeuger, gruppenElement);
		babystepgiantstep.computeBabySteps();
		
//		System.out.println(babystepgiantstep.getX());

		// babystepgiantstep.berechneBabysteps();
		// System.out.println("Folgend werden die Babysteps berechnet:");
		// babystepgiantstep.ausgebenBabysteps(babystepgiantstep.babystepMenge);
		// babystepgiantstep.berechneGiantsteps();
		// System.out.println("Folgend werden die Giantsteps berechnet:");
		// babystepgiantstep.ausgebenGiantsteps(babystepgiantstep.giantstepMenge);
//		System.out.println("ergebnis:" + zyklischeGruppe.isProbablePrime(100));

//		BigInteger a = new BigInteger("345346352727486482365726587264918749164864823658659274196487365874659876457568874365487654345678987654323456");
//		BigInteger a = new BigInteger("11481306964576875688768748876567890987654323456789876578987654334567527425452423283320117768198402231770208869520047764273682576626139237031385665948631650626991844596463898746277344711896086305533142593135616665318539129989145312280000688779148240044871428926990063486244781615463646388363947317026040466353970904996558162398808944629605623311649536164221970332681344168908984458505602379484807914058900934776500429002716706625830522008132236281291761267883317206598995396418127021779858404042159853183251540889433902091920554957783589672039160081957216630582755380425583726015528348786419432054508915275783882625175435528800822842770817965453762184851149029376");
//		BigInteger a = new BigInteger("1");
//		System.out.println();
//		System.out.println();
		
//		long start = System.currentTimeMillis();
//		System.out.println(babystepgiantstep.sqrt(a));
//		System.out.println("Duration: " + (System.currentTimeMillis() - start));
//		start = System.currentTimeMillis();
//		System.out.println(BigSquareRoot.get(a));
//		System.out.println("Duration: " + (System.currentTimeMillis() - start));

//		System.out.println(Integer.MAX_VALUE);

	}
}
