// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2009, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.algorithm;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;

/**
 * This class is used to calculate the order of EC and points above it.

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */
public class ECCOrderAndPoints {


	// x y a b p meaning:  x,y temp values; a,b the parameter of the elliptic curve equation;
	     BigInteger x;
	     BigInteger y;
	     BigInteger a;
	     BigInteger b;
	     BigInteger p;


	     public ECCOrderAndPoints(BigInteger a,BigInteger b, BigInteger p) {

	    	 this.a = a;
	    	 this.b = b;
	    	 this.p = p;

		}

	  public int getStepsofCurve()
	     {
	         int n=0;

	         for(x=BigInteger.valueOf(0);;x=x.add(BigInteger.valueOf(1)))
	         {
	             for(y=BigInteger.valueOf(0);;y=y.add(BigInteger.valueOf(1)))
	             {


 //  y^2 = x^3+a*x+b mod p
	                 if(((y.pow(2)).mod(p)).compareTo(((x.pow(3)).add(a.multiply(x)).add(b)).mod(p))==0)
	                 {

	                     ++n;
	                 }
	                 if(y.equals(p.subtract(BigInteger.valueOf(1))))
	                 {
	                     break;
	                 }
	             }
	             if(x.equals(p.subtract(BigInteger.valueOf(1))))
	             {
	                 break;
	             }
	         }

//  plus the infinity point of the curve
	         return n+1;
	     }

//  to get all points on the curve and save them in array ECPoint[]
	     public ECPoint[] getAllPoints()throws ArrayIndexOutOfBoundsException,NullPointerException
	     {
	         long count=this.getStepsofCurve()-1;
	         ECPoint allPoints[]=new ECPoint[Long.valueOf(count).intValue()];
	         int i;

	         //to get point on curve satisfied the curve equation
	         i=0;
	         for(x=BigInteger.valueOf(0);;x=x.add(BigInteger.valueOf(1)))
	         {
	             for(y=BigInteger.valueOf(0);;y=y.add(BigInteger.valueOf(1)))
	             {
	                 if(((y.pow(2)).mod(p)).compareTo(((x.pow(3)).add(a.multiply(x)).add(b)).mod(p))==0)
	                 {
	                     allPoints[i]=new ECPoint(x,y);
	                     i++;
	                 }
	                 if(y.equals(p.subtract(BigInteger.valueOf(1))))
	                 {
	                     break;
	                 }
	             }
	             if(x.equals(p.subtract(BigInteger.valueOf(1))))
	             {
	                 break;
	             }
	         }
	return allPoints;
}
//  the method is used to get the steps of point
	     public int getStepsOfPoint (ECPoint p,int a, ECFieldFp efp)  {

	    	 int k=2;

	    	 ECCAdd eccAdd = new ECCAdd();

	    	 ECCDouble eccDouble = new ECCDouble();

	    	 ECPoint kp = eccDouble.eccDouble(p, a, efp);

	    	 k++;

	    	 while(!kp.equals(ECPoint.POINT_INFINITY)){

	    		 if(k/2==0){

	    	    	 kp = eccDouble.eccDouble(kp, a, efp);
	    	    	 k++;

	    		 }
	    		 else{

	    			 kp = eccAdd.ecAddition(p, kp, efp);
	    			 k++;

	    		 }

	    	 }

    		 return k-1;

	     }

}

