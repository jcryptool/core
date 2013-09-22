package org.jcryptool.crypto.classic.model.algorithmconfig.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;

public class ClassicAlgorithmConfigViewer extends Shell {
	private IEditorPart openedEditor;
	private ClassicAlgorithmConfiguration config;
	private Composite mainComposite;

	/**
	 * Create the shell.
	 * @param display
	 */
	public ClassicAlgorithmConfigViewer(Display display, IEditorPart openedEditor, ClassicAlgorithmConfiguration config) {
		super(display, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM);
		this.openedEditor = openedEditor;
		this.config = config;
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(Messages.ClassicAlgorithmConfigViewer_0);
		
		
		this.setLayout(new GridLayout());
		mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		config.displayAlgorithmParameters(mainComposite, openedEditor);

		this.pack();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
