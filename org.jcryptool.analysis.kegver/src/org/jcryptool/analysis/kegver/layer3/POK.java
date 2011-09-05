// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Vector;

import org.jcryptool.analysis.kegver.KegverPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * We employ here the notation developed by Camenisch and Stadler, in wich a
 * proof statement is written in the form POK{variables : predicate}, where
 * predicate is a monotone boolean formula on statements of knowledge of
 * discrete logs, potentially over multiple bases. For example, a proof of
 * equality of two values represented by commitments C1 and C2 would be written
 * as follows: POK{a,r1,r2 : (C1=(g^a)(h^r1) && (C2=(g^a)(h^r2))}
 *
 * This class does this and provides means of evaluation - one day.
 *
 * @author hkh
 *
 */
public class POK {

	/*
	 * Static methods
	 */

	/**
	 * This method makes it easier to access other classes methods.
	 *
	 * @param inClass Class which contains the method to be gotten.
	 * @param inName Name of method to be gotten
	 * @param inParameterTypes Class array of parameters of method to be gotten.
	 * @return the gotten method
	 */
	public static Method getMethod(Class<?> inClass, String inName, Class<?>[] inParameterTypes) {
		Method aMethod = null;
		try {
			aMethod = inClass.getDeclaredMethod(inName, inParameterTypes);
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return aMethod;
	}

	/*
	 * Instance variables
	 */

	private Vector<Method> predicates = null;
	private Vector<Vector<Class<?>>> classes = null;
	private Vector<Vector<Object>> objects = null;

	/*
	 * Constructor
	 */

	public POK() {
		this.setPredicates(new Vector<Method>());
		this.setClasses(new Vector<Vector<Class<?>>>());
		this.setObjects(new Vector<Vector<Object>>());
	}

	/**
	 * This method adds a static method with return type boolean and its needed
	 * non-null arguments to the POK.
	 *
	 * Filled with all the needed predicates, POK.evaluate will return true if
	 * each of them is true.
	 *
	 */
	public boolean add(Class<?>[] inClasses, Object[] inObjects, Method inMethod) {

		// Test inputs

		if (inClasses == null || inObjects == null || inMethod == null) {
			throw new IllegalArgumentException("something is null:" +
					"inClasses: " + (inClasses == null) + " " +
					"inObjects: " + (inObjects == null) + " " +
					"inMethod: " + (inMethod == null) + "");
		}

		if (! (inMethod.getReturnType().equals(Boolean.TYPE))) {
			throw new IllegalArgumentException("Method has wrong return type");
		}

		if (! Modifier.isStatic(inMethod.getModifiers())) {
			throw new IllegalArgumentException("Method ain't static");
		}

		// Setup

		Vector<Class<?>> classes = new Vector<Class<?>>(Arrays.asList(inClasses));
		Vector<Object> objects = new Vector<Object>(Arrays.asList(inObjects));

		if (classes.size() != objects.size()){
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < objects.size(); i++) {
			Object aObject = objects.get(i);
			if ( aObject == null) {
				throw new IllegalArgumentException();
			}
			if ( ! (aObject.getClass().getCanonicalName().equals(classes.get(i).getCanonicalName()))){
				throw new IllegalArgumentException("Object.class != passed class (Boolean != boolean?)");
			}
		}

		// Add

		return
			this.getPredicates().add(inMethod)
			&& this.getClasses().add(classes)
			&& this.getObjects().add(objects);
	}

	/**
	 * Assuming no errors in input
	 * @return
	 */
	public boolean evaluate() {
		boolean isProven = true;
		Method m = null;
		Vector<Object> objects = null;

		// Evaluate

		for (int i = 0; i < this.predicates.size(); i++) {
			m = this.getPredicates().get(i);
			objects = this.getObjects().get(i);
			isProven = isProven && this.invoke(m, objects);
		}

		return isProven;
	}

	/**
	 * outsourced the method-invocation just to handle ugly exceptions related
	 * to reflection. There should be none (!) because 1. methods have been
	 * checked for correct returnType (boolean) 2. methods have been checked for
	 * static modifier (easier to invoke, might mean more work calling
	 * POK.evaluate()) 3. objects are not null;
	 *
	 * Hence, should the method fail, than because of the method, not because of
	 * reflection.
	 *
	 * @param m
	 * @param objects
	 * @return
	 */
	private boolean invoke(Method m, Vector<Object> objects) {
		boolean returnBoolean = true;
		try {
			returnBoolean = (Boolean) m.invoke(null, objects.toArray());
		} catch (Exception ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
		return returnBoolean;
	}

	/*
	 * Getters and Setters
	 */

	private Vector<Vector<Class<?>>> getClasses() {
		return this.classes;
	}

	private Vector<Vector<Class<?>>> setClasses(
			Vector<Vector<Class<?>>> inClasses) {
		this.classes = inClasses;
		return this.getClasses();
	}

	private Vector<Vector<Object>> getObjects() {
		return this.objects;
	}

	private Vector<Vector<Object>> setObjects(Vector<Vector<Object>> inObjects) {
		this.objects = inObjects;
		return this.getObjects();
	}

	private Vector<Method> getPredicates() {
		return this.predicates;
	}

	private Vector<Method> setPredicates(Vector<Method> predicates) {
		this.predicates = predicates;
		return this.getPredicates();
	}
}