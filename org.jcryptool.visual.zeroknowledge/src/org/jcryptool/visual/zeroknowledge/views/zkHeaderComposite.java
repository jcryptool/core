package org.jcryptool.visual.zeroknowledge.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;

public class zkHeaderComposite extends Composite{
	private static final Color WHITE = Display.getDefault().getSystemColor(
			SWT.COLOR_WHITE);
	/**
	 * Generates the head of the tab. The head has a title and a description.
	 */
	public zkHeaderComposite(Composite parent) {
		super(parent, SWT.NONE);
		this.setBackground(WHITE);
		this.setLayout(new GridLayout(1, false));

		final Label label = new Label(this, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setText(Messages.Header_title);
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		StyledText stDescription = new StyledText(this, SWT.READ_ONLY | SWT.MULTI
				| SWT.WRAP);
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		stDescription
				.setText(Messages.Header_text);
	}
}
