package algorithmstool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import algorithmstool.ui.MainComposite;

public class View extends ViewPart {
	public static final String ID = "algorithmstool.view";
	private MainComposite mainView;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		this.mainView = new MainComposite(parent, SWT.NONE);
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		mainView.setFocus();
	}
}