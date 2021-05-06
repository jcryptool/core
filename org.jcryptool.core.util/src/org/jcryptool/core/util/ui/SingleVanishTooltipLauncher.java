// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.ui;

import java.util.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.jcryptool.core.util.DynamicOnetimeTask;

/**
 * A balloon tooltip, which is able to have a scheduled hide time, and is able to be reshown at any time.
 * 
 * @author Simon L
 */
public class SingleVanishTooltipLauncher {

    private ToolTip actualTip;
    private DynamicOnetimeTask timerTask;
    private Timer timer;

    private Shell shell;

    public SingleVanishTooltipLauncher(Shell shell) {
        this.shell = shell;
    }

    private void makeTipDisappaer(final ToolTip tip) {
        if (shell != null && !shell.isDisposed()) {
            if (tip != null) {
                if (!tip.isDisposed()) {
                    shell.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            tip.setVisible(false);
                            tip.dispose();
                        }
                    });
                }
            }
        }
    }

    public void showNewTooltip(Point pos, int displayDuration, String title, String message) {
        // kill the actual tip and timer
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        makeTipDisappaer(actualTip);

        // create the new tip
        final ToolTip newTip = new ToolTip(shell, SWT.BALLOON);
        newTip.setLocation(pos);
        newTip.setText(title);
        newTip.setMessage(message);
        newTip.setAutoHide(false);
        newTip.setVisible(true);
        Shell parent = newTip.getParent();
        parent.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                makeTipDisappaer(newTip);
            }
        });

        actualTip = newTip;

        // create the new vanishTimer
        long vanishTime = System.currentTimeMillis() + displayDuration;
        timerTask = new DynamicOnetimeTask(vanishTime) {
            @Override
            protected void doThis() {
                makeTipDisappaer(actualTip);
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 20, 150);
    }

    /**
     * Sets the vanish time of the actually displayed tooltip, if any.
     * 
     * @param vanishTime the vanish time in unix epoch
     */
    public void setVanishTime(long vanishTime) {
        if (timerTask != null) {
            if (!this.timerTask.isExecuted()) {
                this.timerTask.setExecutionTime(vanishTime);
            }
        }
    }

    /**
     * Sets the vanish time of the actually displayed tooltip, if any.
     * 
     * @param vanishTime the vanish time in unix epoch
     */
    public void setTimeToVanish(long timeToVanish) {
        if (timerTask != null) {
            if (!this.timerTask.isExecuted()) {
                this.timerTask.setTimeToExecution(timeToVanish);
            }
        }
    }

    /**
     * @return the vanish time of the actual displayed tooltip
     */
    public long getVanishTime() {
        if (timerTask != null) {
            return timerTask.getExecutionTime();
        }
        return System.currentTimeMillis();
    }

    /**
     * @return the remaining time the actual tooltip has until vanishing.
     */
    public long getTimeToVanish() {
        if (timerTask != null) {
            return timerTask.getTimeToExec();
        }
        return 0;
    }

    public void dispose() {
        timer.cancel();
        makeTipDisappaer(actualTip);
    }

}
