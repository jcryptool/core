package org.jcryptool.core.util.ui;

import org.eclipse.ui.IStartup;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

public class StartupPartTracker implements IStartup {
	
	public static HelpPartActiveTracker parttracker = null;
	private static HelpPartActiveTracker2 parttrackerVisibleHidden = null;

	public static class HelpPartActiveTracker2 implements IPartListener2 {
		public HelpPartActiveTracker mainTracker;

		public HelpPartActiveTracker2(HelpPartActiveTracker mainTracker) {
			super();
			this.mainTracker = mainTracker;
		}

		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
			if(partRef.getId().equals("org.eclipse.help.ui.HelpView")) {
				setVisible(false);
			}
		}
		
		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
			// TODO Auto-generated method stub
			if(partRef.getId().equals("org.eclipse.help.ui.HelpView")) {
				setVisible(true);
			}
		}

		private void setVisible(boolean b) {
			this.mainTracker.isContextHelpVisible = b;
		}
	}
	
	public static class HelpPartActiveTracker implements IPartListener {

		public boolean isContextHelpVisible = false;
		
		@Override
		public void partActivated(IWorkbenchPart part) {
// 			System.out.println("Part: " + part.getSite().getId());
			if (part.getSite().getId().equals("org.eclipse.help.ui.HelpView")) {
				setVisible(true);
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPart part) {
// 			System.out.println("Part:TOP: " + part.getSite().getId());
			if (part.getSite().getId().equals("org.eclipse.help.ui.HelpView")) {
				setVisible(true);
			}

		}

		@Override
		public void partClosed(IWorkbenchPart part) {
// 			System.out.println("Part:CLOSED: " + part.getSite().getId());
			if (part.getSite().getId().equals("org.eclipse.help.ui.HelpView")) {
				setVisible(false);
			}

		}

		@Override
		public void partDeactivated(IWorkbenchPart part) {
//			System.out.println("Part:DEAC: " + part.getSite().getId());
//			if (part.getSite().getId().equals("org.eclipse.help.ui.HelpView")) {
//				setVisible(false);
//			}
		}

		private void setVisible(boolean b) {
			this.isContextHelpVisible = b;
// 			System.out.println("VISI:"+b);
		}

		@Override
		public void partOpened(IWorkbenchPart part) {
// 			System.out.println("Part:OPEN: " + part.getSite().getId());
			if (part.getSite().getId().equals("org.eclipse.help.ui.HelpView")) {
				setVisible(true);
			}
		}

	}
	@Override
	public void earlyStartup() {
		org.eclipse.ui.IWorkbenchWindow wbwin = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		parttracker = new HelpPartActiveTracker();
		parttrackerVisibleHidden = new HelpPartActiveTracker2(parttracker);
		wbwin.getPartService().addPartListener(parttracker);
		wbwin.getPartService().addPartListener(parttrackerVisibleHidden);
		
	}

}
