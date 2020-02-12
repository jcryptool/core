package org.jcryptool.analysis.fleissner.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class OutputDialogExample {

	public static void main(String[] args) throws FileNotFoundException {
		Display display = new Display();
        Shell shell = new Shell(display);
        shell.setVisible(false);

		ArrayList<String> outputDialogStrings = getOutputDialogStrings();
		OutputDialog dialog = new OutputDialog(shell);
		for (int i = 0; i < outputDialogStrings.size(); i++) {
			String outputString = outputDialogStrings.get(i);
			TextPresentation presentation = new TextPresentation();
			presentation.text = outputString;
			if (i == 0) { // Das erste Textfeld...
				presentation.withScrollHorizontal = true; // hat keine hrz. Scrollbar
				presentation.expandedInitially = true; // ... ist direkt aufgeklappt
				presentation.tryFullLineWidth = true; // ... wird versucht, in voller Textbreite anzuzeigen
			} else {
				presentation.withScrollHorizontal = true;
				presentation.expandedInitially = false;
			}
			dialog.addText(presentation);
		}
		dialog.create(Messages.FleissnerWindow_label_dialogOutput, Messages.FleissnerWindow_label_dialogDescription);
		dialog.getShell().pack();
		dialog.open();

	}
	
	private static ArrayList<String> getOutputDialogStrings() throws FileNotFoundException {
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0; i<=10; i++) {
			File source = new File("/home/simon/sandbox/fleissner-texts/"+i);
			System.out.println("reading file " + source);
			result.add(new Scanner(source).useDelimiter("\\Z").next());
		}
		return result;
	}

}
