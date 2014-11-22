package algorithmstool.model.retrievers;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import algorithmstool.model.AlgorithmDescr;
import algorithmstool.ui.PathWizard;

public abstract class BasePathRetriever implements Retriever {

	public static String PERSPECTIVE_DOC = "D";
	public static String PERSPECTIVE_ALG = "A";
	
	private List<String> basePath;
	private String perspective;
	private String descr;

	public BasePathRetriever(List<String> basePath, String perspective, String descr) {
		this.basePath = basePath;
		this.perspective = perspective;
		this.descr = descr;
	}
	
	public String getPerspective() {
		return perspective;
	}
	
	protected List<String> getBasePath() {
		return this.basePath;
	}
	
	protected boolean configure(Shell shell, String topicOfPath) {
		PathWizard wiz = new PathWizard(topicOfPath, this.getBasePath());
		WizardDialog d = new WizardDialog(shell, wiz);
		d.open();
		if(d.getReturnCode() == WizardDialog.OK) {
			this.basePath = wiz.getPath();
			return true;
		}
		return false;
	}
	
	public boolean configure(Shell shell) {
		return this.configure(shell, this.getName());
	}
	
	public static AlgorithmDescr makeBasePathAlgorithm(List<String> basePath, String lastPathElement, String name, String perspective) {
		List<String> newPath = new LinkedList<String>(basePath);
		newPath.add(lastPathElement);
		return new AlgorithmDescr(name, newPath, perspective);
	}
	
	protected AlgorithmDescr makeAlgorithm(String lastPathElement, String name) {
		return makeBasePathAlgorithm(getBasePath(), lastPathElement, name, getPerspective());
	}
	
	@Override
	public String getName() {
		return descr;
	}
	
}
