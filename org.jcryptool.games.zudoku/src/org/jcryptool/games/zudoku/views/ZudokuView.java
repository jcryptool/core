package org.jcryptool.games.zudoku.views;

import java.awt.Frame;

import javax.swing.JComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import edu.kit.iks.zudoku.Zudoku;

public class ZudokuView extends ViewPart {

    @Override
    public void createPartControl(Composite parent) {
    	Composite c = new Composite(parent, SWT.EMBEDDED);
    	Frame frame = SWT_AWT.new_Frame(c);
	    JComponent content = new Zudoku(); 
	    content.setOpaque(true);
	    frame.add(content);
	    frame.pack();
	    frame.setVisible(true); 
    }

    @Override
    public void setFocus() {}
}
