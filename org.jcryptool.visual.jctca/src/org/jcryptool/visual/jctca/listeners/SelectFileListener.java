package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SelectFileListener implements SelectionListener{
	
	Label lbl_selected_file;
	Text txt_text;
	Color black = Display.getDefault().getSystemColor(
			SWT.COLOR_BLACK);
	Color dark_gray = Display.getDefault().getSystemColor(
			SWT.COLOR_DARK_GRAY);
	Button deselect;
	
	public SelectFileListener(Label lbl_selected_file, Text txt_text, Button deselect) {
		this.lbl_selected_file = lbl_selected_file;
		this.txt_text = txt_text;
		this.deselect = deselect;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Button src = (Button) e.getSource();
		FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		String path = ""; //$NON-NLS-1$
		if(path!=null && src.getData().equals("select")){ //$NON-NLS-1$
			path = fd.open();
			lbl_selected_file.setText(path);
			txt_text.setEnabled(false);
			//set fontcolor to gray - looks like disabled
			txt_text.setForeground(dark_gray);
			deselect.setVisible(true);
		}else if(path!=null && src.getData().equals("deselect")) { //$NON-NLS-1$
			lbl_selected_file.setText(""); //$NON-NLS-1$
			txt_text.setEnabled(true);
			//set fontcolor to black - looks like enabled
			txt_text.setForeground(black);
			deselect.setVisible(false);
		}
		lbl_selected_file.getParent().layout();
		
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
