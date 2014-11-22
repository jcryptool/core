package algorithmstool.model.retrievers;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import algorithmstool.model.IAlgorithmDescr;

public interface Retriever {

	public List<? extends IAlgorithmDescr> retrieve();
	public String getName();
	public boolean configure(Shell shell);
	
}
