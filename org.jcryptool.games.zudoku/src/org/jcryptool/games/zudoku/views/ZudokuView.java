package org.jcryptool.games.zudoku.views;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import edu.kit.iks.zudoku.Zudoku;

public class ZudokuView extends ViewPart {
	public final Zudoku zudoku = new Zudoku();

    @Override
    public void createPartControl(Composite parent) {
    	Composite c = new Composite(parent, SWT.EMBEDDED);
    	Frame frame = SWT_AWT.new_Frame(c);    	
	    JComponent content = zudoku; 
	    content.setOpaque(true);
	    JScrollPane scroller = new JScrollPane(content);  
	    frame.add(scroller, BorderLayout.CENTER);  
	    frame.pack();
	    frame.setVisible(true); 
    }

    @Override
    public void setFocus() {}
}
