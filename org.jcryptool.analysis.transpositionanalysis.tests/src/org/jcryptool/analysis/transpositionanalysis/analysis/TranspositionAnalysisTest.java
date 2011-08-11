//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;
import org.jcryptool.analysis.transpositionanalysis.calc.PolledValue;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisCipherlengthDividers;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisInitialization;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisInitializationInput;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisPadding;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisPaddingInput;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.PolledTranspositionKey;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisInput;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisOutput;
import org.junit.Test;

public class TranspositionAnalysisTest implements TranspositionAnalysisOutput {

	Map<Integer, PolledTranspositionKey> keyPolls;
	PolledPositiveInteger keylengthPoll;

	private String ciphertext = "DieisstdeiJCrpyTooBleisipeldtaei.iSekonenenideseaDteiuferenienshcnelelnStratmiJtCryTpooluntze,nindmeSieideDaetiz..BuebredaseMnueA\"lgoirthmne\"vesrchleussenlodedrigiatlsingierne,odreimMneue\"rKyptaonalsye\"vrescheidenAenalsyendraaufnawenedn.NcoheifnachregelnageniSezuasemtilcheknrypotgraifschneOpeartioennueebrdi\"eAlgroithemn\"ScihtafudererchtneSeiet.NahceinmeDopeplklcikaudfengweuencshteEnintargaudfem\"lAgortihme\"nTabeoffntesicehinAssistnetunfduehtrSiecShrittfueSrchrtitducrhdeVnershcluesselugnsprzoessD.asEtnschulessleneroflgtpsaetreaufidesebleArutndWiese.lAleagneboetnenlAgortihmeunndacuhdiKerypotanaylsebneoetgienimmereniegeeoffnteeDaetiinienemedrJCyrpToloEdiotrenV.isulaisireungneundpSielseindadgegneindreRegleunahbaeniggvoeninegreoeffnetneDatie.WouachimmerenieDaetibeonetitgwir,dkoennenSeidieesBeipsieladteievrwednenoedreienbeleibigeeigeenDatieoefnfen.hIreOirginladatieblebitdaebisttesunnagetsatetrehaletn,jdeekrpytogarfishceOpreatinogenreieretineenueAbreitdsate.iBeiedrSuhcenahceinmebesitmmtneAlgroithums(acuhAnlayseV,isulaisireungdoerSipel)ihlfthInenadsFitlerfledobneindre\"Alogritmhen\"iSchtH.ierimtwidrderegradaektievTabuafzuSmuchebgriffpasesndeiEntreageenigeshcraeknt.WieterIenfomratinoenzmuLerenn,AwnendneundrEweietrnvnoJCrpyTooflindneSienidermufanrgeicehnOnilne-iHlfed,ieSeiuebredaseMnueH\"ilf\"e-->I\"nhatlderiHlfeetxtea\"ufrfuenkeonne.n";

	@Test
	public void fullAnalysis() {
		initialization();
		dividerAnalysis1();
		paddingAnalysis1();
	}

	private void initialization() {
		TranspositionAnalysisInitializationInput in = new TranspositionAnalysisInitializationInput() {
			public boolean isUserEstimatedAnalysisWeight() {
				return false;
			}

			public double getUserEstimatedAnalysisWeight() {
				return 1;
			}

			public String getCiphertext() {
				return ciphertext;
			}

			public int getMaxKeylength() {
				return 20;
			}
		};

		TranspositionAnalysisInitialization initializator = new TranspositionAnalysisInitialization();
		initializator.setInput(in);
		initializator.setOutput(this);
		initializator.analyze();
		initializator.combineResultsWithOutput();

		// Proove that keylengths are in /not in the poll
		assertTrue(keylengthPoll.hasChoice(1));
		assertTrue(keylengthPoll.hasChoice(10));
		assertTrue(keylengthPoll.hasChoice(20));
		assertFalse(keylengthPoll.hasChoice(50));

		// Proove, that the PolledKeys have the right length.
		assertEquals(10, keyPolls.get(10).getLength());
		assertEquals(15, keyPolls.get(15).getLength());
		assertEquals(20, keyPolls.get(20).getLength());
	}

	private void dividerAnalysis1() {
		// Set the input object
		TranspositionAnalysisInput in = new TranspositionAnalysisInput() {
			public boolean isUserEstimatedAnalysisWeight() {
				return false;
			}

			public double getUserEstimatedAnalysisWeight() {
				return 1;
			}

			public String getCiphertext() { // JCT german standard editor text,
											// blanks/linebreaks deleted and
											// encrypted with "SIMON"
				return ciphertext;
			}
		};

		// Analysis object
		TranspositionAnalysisCipherlengthDividers analysis = new TranspositionAnalysisCipherlengthDividers();

		analysis.setInput(in);
		analysis.setOutput(this);
		analysis.analyze();

		analysis.combineResultsWithOutput();

		// make sure the conclusion contains the right values
		assertTrue(analysis.getConclusion().toString().contains("4"));
		assertTrue(analysis.getConclusion().toString().contains("5"));
		assertTrue(analysis.getConclusion().toString().contains("10"));
		assertTrue(analysis.getConclusion().toString().contains("20"));

		// from the 5th length, the possibilities should be lower than default
		// possibility
		assertTrue(keylengthPoll.getPossibility(keylengthPoll
				.getValuesSortedByPossibility().get(4)) < PolledValue.POSSIBILITY_DEFAULT);

	}

	private void paddingAnalysis1() {
		TranspositionAnalysisPaddingInput in = new TranspositionAnalysisPaddingInput() {
			public boolean isUserEstimatedAnalysisWeight() {
				return false;
			}

			public double getUserEstimatedAnalysisWeight() { // TODO: Implement
																// user
																// estimated
																// analysis
																// weight impact
																// everywhere
				return 1;
			}

			public String getCiphertext() {
				return ciphertext;
			}

			public int getSelectedPaddingLengthFromEnd() {
				return 0;
			}
		};

		TranspositionAnalysisPadding analysis = new TranspositionAnalysisPadding();
		analysis.setInput(in);
		analysis.setOutput(this);

		// In the ciphertext, there is no padding (chance: 1 out of 5).
		// For the test sake, we will behave, like the end of the ciphertext
		// looks
		// like: #n#, so we say the padding distance to text end is 3.

		final int userDetectedPaddingDistance = 2;

		in = new TranspositionAnalysisPaddingInput() {
			public boolean isUserEstimatedAnalysisWeight() {
				return false;
			}

			public double getUserEstimatedAnalysisWeight() {
				return 1;
			}

			public String getCiphertext() {
				return ciphertext;
			}

			public int getSelectedPaddingLengthFromEnd() {
				return userDetectedPaddingDistance;
			}
		};

		analysis.setInput(in);
		analysis.analyze();
		analysis.combineResultsWithOutput();

		for (int i = 1; i < userDetectedPaddingDistance; i++) {
			assertTrue(keylengthPoll.getPossibility(i) < PolledValue.POSSIBILITY_DEFAULT);
		}
	}

	public TranspositionAnalysisTest() {
		keyPolls = new HashMap<Integer, PolledTranspositionKey>();
		keylengthPoll = new PolledPositiveInteger();
	}

	public Map<Integer, PolledTranspositionKey> getKeyPolls() {
		return keyPolls;
	}

	public void setKeyPolls(Map<Integer, PolledTranspositionKey> keys) {
		this.keyPolls = keys;
	}

	public PolledPositiveInteger getKeylengthPoll() {
		return keylengthPoll;
	}

	public void setKeylengthPoll(PolledPositiveInteger keylength) {
		this.keylengthPoll = keylength;
	}

}
