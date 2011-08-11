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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.zeroknowledge.Protocol;

/**
 * Group, dass die Auswahl der unterschiedlichen Fälle darstellt. über RadioButtons kann man
 * auswählen, welchen Fall man betrachten möchte: entweder ist das Geheimnis bekannt oder der sich
 * Authentifizierende kennt das Geheimnis nicht
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Introduction {

    private Button firstCase;

    private Group group;

    private Button secondCase;

    private Label labelDesc;

    /**
     * Konstruktor für die Intro, die die beiden Auswahlmöglichkeiten enthält.
     *
     * @param pro Protokoll, in dem der erste oder zweite Fall gesetzt werden soll
     * @param parent Parent der graphischen Oberfläche
     * @param prefix Präfix für der String, so dass der richtige Text aus den Bundles ausgelesen
     *        werden kann
     */
    public Introduction(final Protocol pro, final Composite parent, final String prefix) {
        group = new Group(parent, SWT.None);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.horizontalSpacing = 10;
        group.setText(Messages.Introduction_0);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalAlignment = GridData.CENTER;

        group.setLayout(gridLayout);
        group.setLayoutData(gridData);

        group.setVisible(true);

        firstCase = new Button(group, SWT.RADIO);
        firstCase.setSelection(true);
        firstCase.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom First-Case-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Setzt den ersten Fall (Alice) auf der graphischen Oberfläche und in dem Objekt, das
             * die Modelle speichert.
             */
            public void widgetSelected(SelectionEvent arg0) {
                if(firstCase.getSelection()) {
                    String text = ""; //$NON-NLS-1$
                	if ("FFS".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_3;
                    } else if ("FS".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_5;
                    } else if ("G".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_7;
                    } else if ("M".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_9;
                    }
                    labelDesc.setText(text);
                    secondCase.setSelection(false);
                    firstCase.setSelection(true);
                    pro.setFirstCase(true);
                    parent.getParent().layout();
                }

            }
        });
        Label label = new Label(group, 0);
        label.setText(Messages.Introduction_10);

        // Description label for chosen option
        labelDesc = new Label(group, 0);
        GridData gridData1 = new GridData();
        gridData1.minimumWidth = 400;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalSpan = 2;
        String text = ""; //$NON-NLS-1$
        if ("FFS".equals(prefix)) { //$NON-NLS-1$
            text = Messages.Introduction_3;
        } else if ("FS".equals(prefix)) { //$NON-NLS-1$
            text = Messages.Introduction_5;
        } else if ("G".equals(prefix)) { //$NON-NLS-1$
            text = Messages.Introduction_7;
        } else if ("M".equals(prefix)) { //$NON-NLS-1$
            text = Messages.Introduction_9;
        }
        labelDesc.setText(text);
        labelDesc.setLayoutData(gridData1);

        // RadioButton, mit dem man den zweiten Fall auswaehlen kann, und
        // der Text, der angibt, was hier passiert
        secondCase = new Button(group, SWT.RADIO);
        // secondCase.setBounds(10, 60, 20, 20);
        secondCase.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom Second-Case-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Setzt den zweiten Fall (Carol) auf der graphischen Oberfläche und in dem Objekt, das
             * die Modelle speichert.
             */
            public void widgetSelected(SelectionEvent arg0) {
                if(secondCase.getSelection()) {
                    String text = ""; //$NON-NLS-1$
                	if ("FFS".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_22;
                    } else if ("FS".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_24;
                    } else if ("G".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_26;
                    } else if ("M".equals(prefix)) { //$NON-NLS-1$
                        text = Messages.Introduction_28;
                    }
                    labelDesc.setText(text);
                    firstCase.setSelection(false);
                    secondCase.setSelection(true);
                    pro.setFirstCase(false);
                    parent.getParent().layout();
                }

            }
        });
        label = new Label(group, 0);
        label.setText(Messages.Introduction_29);
        group.setVisible(true);
    }

    /**
     * Methode zum Erhalten des Group-Objektes
     *
     * @return Group-Objekt, das die graphischen Komponenten enthält
     */
    public Composite getGroup() {
        return group;
    }
}
