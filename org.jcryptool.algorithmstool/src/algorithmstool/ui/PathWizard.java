package algorithmstool.ui;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class PathWizard extends Wizard {

	private PathPage pathPage;
	private String topicOfPath;
	private List<String> path;
	private List<String> initialPath;

	public PathWizard(String topicOfPath, List<String> initialPath) {
		this.topicOfPath = topicOfPath;
		setWindowTitle("Pfadeingabe");
		this.initialPath = initialPath;
	}

	@Override
	public void addPages() {
		pathPage = new PathPage(topicOfPath, this.initialPath);
		addPage(pathPage);
	}

	@Override
	public boolean performFinish() {
		this.path = this.pathPage.getPath();
		return true;
	}
	
	public List<String> getPath() {
		return path;
	}

}
