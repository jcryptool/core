package org.jcryptool.games.divide.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.games.divide.logic.IPlayer;

public class ChoosePlayerDialog {

	// instance vars
	private List<IPlayer> players;
	private Shell parent;
	
	// constructor
	public ChoosePlayerDialog(List<IPlayer> players, Shell parent) {
		super();
		this.players = players;
		this.parent = parent;
	}
	
	// methods
	public int open() {
		String title = Messages.ChoosePlayerDialog_0;
		String question = Messages.ChoosePlayerDialog_1;
		String[] labels = new String[] { players.get(0).getName(), players.get(1).getName() };
		MessageDialog dialog = new MessageDialog(parent, title, null, question, MessageDialog.QUESTION, labels, -1);
		
		return dialog.open();
	}
}
