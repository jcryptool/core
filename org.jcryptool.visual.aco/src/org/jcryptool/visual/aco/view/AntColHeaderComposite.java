package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;

public class AntColHeaderComposite extends Composite{
	/**
	 * Generates the head of the tab. The head has a title and a description.
	 */
	public AntColHeaderComposite(Composite parent) {
		super(parent, SWT.NONE);
		this.setBackground(ColorService.WHITE);
		this.setLayout(new GridLayout(1, false));

		final Text text_title = new Text(this, SWT.READ_ONLY);
		text_title.setFont(FontService.getHeaderFont());
        text_title.setBackground(ColorService.WHITE);
		text_title.setText(Messages.Header_title);
		
		StyledText stDescription = new StyledText(this, SWT.READ_ONLY  | SWT.WRAP);
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		stDescription.setText(Messages.Header_text);
	}
}
