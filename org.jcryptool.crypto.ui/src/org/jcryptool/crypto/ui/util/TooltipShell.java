package org.jcryptool.crypto.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TooltipShell extends FloatingShell {

	public Text tooltiptext;
	private int maxTextWidth;
	private String content;

	public TooltipShell(Display display, String text, int maxWidth) {
		super(display);
		this.maxTextWidth = maxWidth;
		this.content = text;
		FontData fontmetric = this.tooltiptext.getFont().getFontData()[0];
		this.tooltiptext.setText(text);
	}
	
	@Override
	protected void createContents(Composite contentsComposite) {
		this.tooltiptext = new Text(contentsComposite, SWT.MULTI | SWT.WRAP);
		this.tooltiptext.setEditable(false);
		this.tooltiptext.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
	}
	
	@Override
	protected void beforeOpen() {
		this.tooltiptext.setText(this.content);

		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		Point necessaryHeight = this.tooltiptext.computeSize(this.maxTextWidth, SWT.DEFAULT);
//		System.out.println(necessaryHeight);
		necessaryHeight = this.tooltiptext.computeSize(SWT.DEFAULT, necessaryHeight.y);
//		System.out.println(necessaryHeight);
		layoutData.widthHint = necessaryHeight.x;
//		layoutData.minimumHeight = necessaryHeight.y;
		layoutData.heightHint = necessaryHeight.y;
		this.tooltiptext.setLayoutData(layoutData);
	}
	
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = launch(display, "Langer Text lorem ipsum dolor sit amet consectetur, \nbla blu", 600);
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Shell launch(Display display, String text, int maxWidth) {
		TooltipShell shell = new TooltipShell(display, text, maxWidth);
		Point cursorLoc = display.getCursorLocation();
		shell.setLocation(new Point(cursorLoc.x + 5, cursorLoc.y + 5));
		shell.open();
		shell.layout();
		shell.pack();
		return shell;
	}
	

}
