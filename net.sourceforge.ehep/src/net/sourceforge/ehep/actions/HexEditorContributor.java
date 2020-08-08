/*
 * 
 * HexEditorContributor.java
 * 
 * $Author: uwe_ewald $
 * $Revision: 1.5 $
 * $Date: 2011/05/30 20:04:37 $
 * $Name:  $
 *
 * Created on 2005-1-20 10:25:06
 * Created by James Gan (jamesgan@users.sourceforge.net)
 */
package net.sourceforge.ehep.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import net.sourceforge.ehep.editors.HexEditor;

/**
 * @author ganzhi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HexEditorContributor extends EditorActionBarContributor {
	private HexEditor fCurrentEditor;

	/**
	 * 
	 */
	public HexEditorContributor() {
		super();
	}
	
	public void init(IActionBars bars) {
		super.init(bars);
		Action undoAction = new Action() {
		    public void run() {
		    	if (fCurrentEditor != null)
		    		fCurrentEditor.undo();
		    }
		};
		bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		Action redoAction = new Action() {
		    public void run() {
		    	if (fCurrentEditor != null)
		    		fCurrentEditor.redo();
		    }
		};
		bars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		Action findAction = new Action() {
			public void run() {
				if (fCurrentEditor != null)
					fCurrentEditor.getControl().toggleRulerVisibility();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
		
		Action cutAction = new Action() {
			public void run() {
				if (fCurrentEditor != null);
					fCurrentEditor.getControl().cut();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);

		Action copyAction = new Action() {
			public void run() {
				if (fCurrentEditor != null)
					fCurrentEditor.getControl().copy();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		Action pasteAction = new Action() {
			public void run() {
				if (fCurrentEditor != null);
					fCurrentEditor.getControl().paste();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);

		Action deleteAction = new Action() {
			public void run() {
				if (fCurrentEditor != null);
					fCurrentEditor.getControl().deleteSelection();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

		Action selectAllAction = new Action() {
			public void run() {
				if (fCurrentEditor != null);
					fCurrentEditor.getControl().getHexTable().selectAll();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllAction);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IEditorPart targetEditor) {
		super.setActiveEditor(targetEditor);
		
		if (!(targetEditor instanceof HexEditor))
			return;
		fCurrentEditor = (HexEditor) targetEditor;

		getActionBars().updateActionBars();
	}
}
