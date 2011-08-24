package org.jcryptool.analysis.kegver.test;

import static org.junit.Assert.assertSame;

import org.junit.Test;


public class TestUtilsPolynomial {
	
	@Test
	public void first(){
		this.testPolynomial();
	}

	@Test
	public void testPolynomial() {
		Polynomial p1 = new Polynomial(1, 0);
		Polynomial p2 = new Polynomial(2, 1);
		Polynomial p3 = new Polynomial(3, 2);
		Polynomial p4 = p1.plus(p2).plus(p3);
		assertSame(86, p4.evaluate(5));
	}
	

}
