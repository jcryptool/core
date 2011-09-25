/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.tests.verify;

import junit.framework.TestCase;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.ui.verify.SignatureLabelProvider;
import org.eclipse.wst.xml.security.ui.verify.SignatureView;

/**
 * <p>JUnit tests for the SignatureView class.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class SignatureViewTest extends TestCase {
    private static final String VIEW_ID = "org.eclipse.wst.xml.security.ui.SignatureView";
    private SignatureView testView;

    protected void setUp() throws Exception {
        waitForJobs();
        testView = (SignatureView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VIEW_ID);
        waitForJobs();
        delay(3000);
    }

    protected void tearDown() throws Exception {
        waitForJobs();
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(testView);
    }

    public void testView() {
        TableViewer viewer = testView.getSignaturesViewer();
        assertNotNull(viewer);

        ArrayContentProvider contentProvider = (ArrayContentProvider) viewer.getContentProvider();
        assertNotNull(contentProvider);

        SignatureLabelProvider labelProvider = (SignatureLabelProvider) viewer.getLabelProvider();
        assertNotNull(labelProvider);

        assertEquals(4, viewer.getTable().getColumnCount());

        assertEquals("0 Signatures: 0 valid, 0 invalid, 0 unknown", testView.getContentDescription());
    }

    /**
     * Wait until all background tasks are complete.
     */
    private void waitForJobs() {
        while(!Job.getJobManager().isIdle()) {
            delay(1000);
        }
    }

    private void delay(int waitTimeMillis) {
        Display display = Display.getCurrent();

        if (display != null) {
            long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;

            while (System.currentTimeMillis() < endTimeMillis) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            display.update();
        } else {
            try {
                Thread.sleep(waitTimeMillis);
            } catch (InterruptedException ex) {
                // ignore
            }
        }
    }

}
