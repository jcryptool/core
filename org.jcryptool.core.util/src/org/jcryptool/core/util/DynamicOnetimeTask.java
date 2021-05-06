// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.jcryptool.core.util;

import java.util.TimerTask;

/**
 * 
 * 
 * @author Simon L
 */
public abstract class DynamicOnetimeTask extends TimerTask {
    private long executionTime;
    private boolean executed = false;

    public DynamicOnetimeTask(long executionTime) {
        setExecutionTime(executionTime);
    }

    /**
     * Sets the execution time in unix epoch
     * 
     * @param executionTime the execution time.
     */
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
        if (!executed) {
            if (System.currentTimeMillis() > executionTime) {
                executeTask();
            }
        }
    }

    /**
     * Sets the execution time relative to the Now.
     * 
     * @param timeToVanish + System.currentTimeMillis() == execution time in unix epoch
     */
    public void setTimeToExecution(long timeToVanish) {
        setExecutionTime(System.currentTimeMillis() + timeToVanish);
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() > executionTime) {
            executeTask();
        }
    }

    private void executeTask() {
        if (!executed) {
            executed = true;
            doThis();
            this.cancel();
        }
    }

    protected abstract void doThis();

    public long getTimeToExec() {
        return executionTime - System.currentTimeMillis();
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public boolean isExecuted() {
        return executed;
    }
};