package org.jcryptool.crypto.ui.background;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class BackgroundJob {

//     	public List<Consumer<Double>> progressListeners = new LinkedList<>(); 
		public List<Consumer<IStatus>> finalizeListeners = new LinkedList<>();

		public String name() {
			return "Background Job"; // TODO: internationalize
		}

		/**
		 * Implement this method to update the progress monitor. return e.g. IStatus.OK.
		 * add a finalizeListener to be notified when the algorithm returns.
		 * 
		 * @param monitor
		 * @return
		 */
		public abstract IStatus computation(IProgressMonitor monitor);

		/**
		 * starts the computation in the background. Be sure to subscribe to
		 * {@link #finalizeListeners} to be notified of the result.
		 */
		public void runInBackground() {
			Job job = new Job(name()) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
					return BackgroundJob.this.computation(monitor);
					// TODO: what to do on exception?
				}

			};
			job.schedule();
//     		PlatformUI.getWorkbench().getProgressService().busyCursorWhile();
			job.addJobChangeListener(new IJobChangeListener() {

				private boolean isDone;

				@Override
				public void sleeping(IJobChangeEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void scheduled(IJobChangeEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void running(IJobChangeEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void done(IJobChangeEvent event) {
					this.isDone = true;
					finalizeListeners.forEach(listener -> listener.accept(event.getResult()));

				}

				@Override
				public void awake(IJobChangeEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void aboutToRun(IJobChangeEvent event) {
					// TODO Auto-generated method stub

				}
			});
		}

		private Shell imposedNoClickShell = null;
		
		public void liftNoClickDisplaySynced(Display display) {
			display.syncExec(() -> {
				imposedNoClickShell.setCursor(display.getSystemCursor(SWT.CURSOR_ARROW));
				imposedNoClickShell = null;
			});
		}

		public void imposeNoClickDisplayCurrentShellSynced(Display display) {
			display.syncExec(() -> {
				BackgroundJob.this.imposedNoClickShell = display.getActiveShell();
				imposedNoClickShell.setCursor(imposedNoClickShell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
			});
		}

//		public static void liftNoClick(Shell shell) {
//			shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
//		}
//
//		public static void imposeNoClick(Shell shell) {
//			shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
//		}
	}