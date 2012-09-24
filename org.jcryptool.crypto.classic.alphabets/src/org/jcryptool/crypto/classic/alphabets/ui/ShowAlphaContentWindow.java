package org.jcryptool.crypto.classic.alphabets.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.BlockAlphabet;

public class ShowAlphaContentWindow extends Shell {
	private Composite composite;
	private Label lblCharactersInThe;
	private Text text;
	private AbstractAlphabet alpha;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ShowAlphaContentWindow shell = new ShowAlphaContentWindow(display, new AtomAlphabet("")); //$NON-NLS-1$
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
	 * Create the shell.
	 * @param display
	 */
	public ShowAlphaContentWindow(Display display, AbstractAlphabet alpha) {
		super(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		this.alpha = alpha;
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(Messages.getString("ShowAlphaContentWindow.1")); //$NON-NLS-1$
		setSize(325, 130);
		setLayout(new GridLayout());
		{
			composite = new Composite(this, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			composite.setLayout(new GridLayout(1, false));
			{
				lblCharactersInThe = new Label(composite, SWT.WRAP);
				GridData layoutData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
				layoutData.widthHint = 130;
				lblCharactersInThe.setLayoutData(layoutData);
				if(alpha instanceof BlockAlphabet) {
					lblCharactersInThe.setText(String.format(Messages.getString("ShowAlphaContentWindow.0"), ((BlockAlphabet) alpha).getBlockName())); //$NON-NLS-1$
				} else {
					lblCharactersInThe.setText(Messages.getString("ShowAlphaContentWindow.2")); //$NON-NLS-1$
				}
			}
			{
				text = new Text(composite, SWT.BORDER|SWT.MULTI);
				text.setEditable(false);
				text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				text.setText(AtomAlphabet.alphabetContentAsString(alpha.getCharacterSet()));
			}
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
