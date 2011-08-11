package org.jcryptool.analysis.kegver.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.jcryptool.visual.xeuclidean.algorithm.XEuclid;


public class TestJCTXEuclid {
	
	private XEuclid aXEuclid = null;
	
	@Before
	public void setup(){
		assertNull(this.getXEuclid());
		this.setXEuclid(new XEuclid());
		assertNotNull(this.getXEuclid());		
	}
	
	@Test
	public void first(){
		this.testXEuclid();
	}

	@Test
	public void testXEuclid() {
		this.getXEuclid().xeuclid(
				BigInteger.valueOf((long) 16), 
				BigInteger.valueOf((long) 13));
		assertSame(0, BigInteger.valueOf((long) 5).compareTo(this.getXEuclid().getInverseY()));
		
		this.getXEuclid().xeuclid(
				BigInteger.valueOf((long) 160),
				BigInteger.valueOf((long) 13));
		assertSame(0, BigInteger.valueOf((long) 37).compareTo(this.getXEuclid().getInverseY()));
	}
	
	/*
	 * Getters and Setters
	 */
	
	private XEuclid getXEuclid() {
		return this.aXEuclid;
	}

	private XEuclid setXEuclid(XEuclid inXEuclid) {
		this.aXEuclid = inXEuclid;
		return this.getXEuclid();
	}
}