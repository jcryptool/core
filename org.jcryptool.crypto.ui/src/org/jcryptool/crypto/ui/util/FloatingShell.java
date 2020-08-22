package org.jcryptool.crypto.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

/**
 * @author Simon Leischnig
 *
 */
/**
 * @author snuc
 *
 */
/**
 * @author snuc
 *
 */
public class FloatingShell extends Shell {

	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			FloatingShell shell = new FloatingShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	SWT.BORDER
	SWT.CLOSE
	SWT.MIN
	SWT.MAX
	SWT.RESIZE
	SWT.TITLE
	SWT.TOOL
	SWT.NO_TRIM
	SWT.NO_MOVE
	SWT.SHELL_TRIM
	SWT.DIALOG_TRIM
	SWT.ON_TOP
	SWT.MODELESS
	SWT.PRIMARY_MODAL
	SWT.APPLICATION_MODAL
	SWT.SYSTEM_MODAL
	SWT.SHEET
	 */

	public static int DEFAULT_STYLE = SWT.RESIZE | SWT.MIN | SWT.MAX | SWT.ON_TOP | SWT.MODELESS | SWT.CLOSE | SWT.NO_TRIM;
	private Composite contentsComposite;
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public FloatingShell(Display display, int style) {
		super(display, style);
		GridLayout thisLayout = new GridLayout(1, false);
		setLayout(thisLayout);

		setText(getShellTitle());
		setSize(getInitialSize());


		Layout initialShellLayout = this.getInitialShellLayout();
		this.setLayout(initialShellLayout);
 		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		this.contentsComposite = new Composite(this, SWT.NONE);
		Layout initialContentsLayout = this.getInitialContentsLayout();
		this.contentsComposite.setLayout(initialContentsLayout);
		GridData contentsGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.contentsComposite.setLayoutData(contentsGridData);
		
		this.createContents(contentsComposite);
		this.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				FloatingShell.this.onActiveLost(e);
			}
			@Override
			public void shellActivated(ShellEvent e) {
				FloatingShell.this.onActiveGained(e);
			}
		});
		this.requestFocus();
	}

	private Layout getInitialContentsLayout() {
		GridLayout contentsLayout = new GridLayout(1, false);
		return contentsLayout;
	}

	private Layout getInitialShellLayout() {
		GridLayout shellLayout = new GridLayout(1, false);
		shellLayout.marginWidth = 0;
		shellLayout.marginHeight = 0;
		return shellLayout;
	}

	protected void onActiveGained(ShellEvent e) {
		// default: do nothing
	}

	protected void onActiveLost(ShellEvent e) {
		this.close();
	}

	protected void requestFocus() {
		this.setActive();
	}

	public FloatingShell(Display display) {
		this(display, DEFAULT_STYLE);
	}

	/**
	 * Create contents of the shell. Barring overridden methods, the layout is GridLayout(1, false).
	 * @param contentsComposite2 
	 */
	protected void createContents(Composite contentsComposite) {
	}
	/**
	 * Adapts the layout to content right before layouting / opening the first time
	 */
	protected void beforeOpen() {
	}
	
	protected Point getInitialSize() {
		return new Point(150, 50);
	}

	protected String getShellTitle() {
		return "";
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	@Override
	public void open() {
		this.beforeOpen();
		super.open();
	}

}
