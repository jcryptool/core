package algorithmstool.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import algorithmstool.model.AlgorithmDescr;

public class PathPage extends WizardPage {

	private static final String PAGENAME_DEFAULT = "path page";
	private Text text;
	private List<String> initialPath;

	/**
	 * Create the wizard.
	 */
	public PathPage(String pathTopic, List<String> initialPath) {
		super(PAGENAME_DEFAULT);
		setTitle("Eingabe des Stammpfades für " + pathTopic);
		setDescription("Bitte den Stammpfad für " + pathTopic + " eingeben, getrennt durch '>' ohne Leerzeichen.");
		this.initialPath = initialPath;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.marginTop = 10;
		container.setLayout(gl_container);
		
		Label lblPfad = new Label(container, SWT.NONE);
		lblPfad.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPfad.setText("Pfad: ");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setText(gluePath(this.initialPath));
	}
	
	private String gluePath(List<String> initialPath) {
		return AlgorithmDescr.makePathString(initialPath, ">");
	}

	public List<String> getPath() {
		LinkedList<String> list = new LinkedList<String>();
		String[] split = this.text.getText().trim().split(">");
		for(String s: split) {
			list.add(s);
		}
		
		return list;
	}

}
