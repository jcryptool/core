package algorithmstool.model.retrievers.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import algorithmstool.model.AlgorithmDescr;
import algorithmstool.model.IAlgorithmDescr;
import algorithmstool.model.retrievers.BasePathRetriever;

public class DocViewRestRetriever extends BasePathRetriever {

	private String extPt;

	public DocViewRestRetriever(List<String> basePath, String extPt, String descr) {
		super(basePath, BasePathRetriever.PERSPECTIVE_DOC, descr);
		this.extPt = extPt;
	}
	
	@Override
	public List<? extends IAlgorithmDescr> retrieve() {
        List<AlgorithmDescr> algoList = new LinkedList<AlgorithmDescr>();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                getExtPt());

        for (IConfigurationElement element : extensionPoint.getConfigurationElements()) {
            String name = element.getAttribute("name"); //$NON-NLS-1$
            
            algoList.add(makeAlgorithm(name));
        }
        return algoList;
	}
	
	protected AlgorithmDescr makeAlgorithm(String displayedName) {
		return super.makeAlgorithm(displayedName, displayedName);
	}

	public String getExtPt() {
		return extPt;
	}
	
}
