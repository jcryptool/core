package org.jcryptool.analysis.kegver.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;



public class TestBuildingBlockCloseness {

	@Test
	public void first(){
		this.testEvaluate();
		this.testIsOverwhelming();
	}
	
	@Test
	public void testIsOverwhelming() {
		Polynomial P = new Polynomial(1, 3);
		assertTrue(_Closeness.isOverwhelming(0.97d, 8, P));
	}

	private void testEvaluate() {
		Polynomial P = new Polynomial(1, 3);
		assertTrue(0d == _Closeness.evaluate(1, P));
		assertTrue(-1d == _Closeness.evaluate(0, P));
	}
}
