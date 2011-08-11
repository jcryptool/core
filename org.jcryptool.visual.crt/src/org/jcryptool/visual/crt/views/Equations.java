package org.jcryptool.visual.crt.views;

import java.util.Vector;

import org.eclipse.swt.widgets.Composite;

/**
 *
 * @author Oryal Inel
 * @version 1.0.0
 */
public class Equations {
	private Vector<Equation> equationSet;

	public Equations() {
		equationSet = new Vector<Equation>();
	}

	public void removeEquation(Equation e) {
		this.equationSet.remove(e);
	}

	public void getIndexOf(Equation e) {
		equationSet.indexOf(e);
	}

	public int getNumberOfEquations() {
		return equationSet.size();
	}

	public Equation lastElement() {
		return equationSet.lastElement();
	}

	public void createEquation(int id, Composite equationGroup, CRTGroup mainGroup) {
		Equation e = new Equation(id, this, equationGroup, mainGroup);
		equationSet.add(e);
		e.addEquationToGroup();

	}

	public Vector<Equation> getEquationSet() {
		return equationSet;
	}

}
