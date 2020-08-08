// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.introduction.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A Debouncer is responsible for executing a task with a delay, and cancelling
 * any previous unexecuted task before doing so.
 */
public class DebounceExecutor {

	private ScheduledExecutorService executor;
	private ScheduledFuture<?> future;

	public DebounceExecutor() {
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}

	public void debounce(long delay, Runnable task) {
		if (future != null && !future.isDone()) {
			future.cancel(false);
		}

		future = executor.schedule(task, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * This method is used to cancel the current job.<br>
	 * This method is used, when the plugin gets closed.
	 */
	public void cancelAllJobs() {
		if (future != null && !future.isDone()) {
			future.cancel(false);
		}
	}
}
