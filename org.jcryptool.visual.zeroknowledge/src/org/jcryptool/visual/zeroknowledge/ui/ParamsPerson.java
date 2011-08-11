// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Superklasse für Panels zur Darstellung von Parametern.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public abstract class ParamsPerson {

    /**
     * Group, die die einzelnen Komponenten enthält
     */
    protected Group group;
    private boolean visible = false;

    /**
     * Konstruktor, der das Layout setzt und das MagicalDoor-Objekt setzt
     * 
     * @param comp Parent-Objekt zu dem graphischen Teil einer Instanz dieser Klasse
     */
    public ParamsPerson(Composite comp) {
        group = new Group(comp, 0);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        // gridLayout.makeColumnsEqualWidth = true;
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;

        group.setLayout(gridLayout);
        group.setLayoutData(gridData);
    }

    /**
     * Methode zum Erhalten der Group
     * 
     * @return Group, die die einzelnen graphischen Komponenten enthält
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Methode zum sichtbar machen des Panels. Wenn das Panel sichtbar wird, wird automatisch ein
     * Update aufgerufen
     * 
     * @param visible true, wenn das Panel sichtbar werden soll, false sonst
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        group.setVisible(visible);
        if (visible)
            update();
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Methode zum updaten des Panels
     */
    public abstract void update();
}
