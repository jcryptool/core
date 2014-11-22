package algorithmstool.model.retrievers.impl;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.providers.FlexiProviderAlgorithmsViewContentProvider;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;

import algorithmstool.model.AlgorithmDescr;
import algorithmstool.model.IAlgorithmDescr;
import algorithmstool.model.retrievers.BasePathRetriever;

public class FlexiProviderRetriever extends BasePathRetriever {

	private ITreeNode baseNode;

	public static boolean canInitialize() {
		return FlexiProviderAlgorithmsViewContentProvider._invisibleRoot != null;
	}
	
	public FlexiProviderRetriever(List<String> basePath, ITreeNode baseNode, String descr) {
		super(basePath, BasePathRetriever.PERSPECTIVE_ALG, descr);
		this.baseNode = baseNode;
	}
	
	@Override
	public List<? extends IAlgorithmDescr> retrieve() {
		List<AlgorithmDescr> algoList = new LinkedList<AlgorithmDescr>();
		nodeOut(this.baseNode, getBasePath(), algoList);
		return algoList;
	}
	
	private boolean isLeaf(ITreeNode n) {
		return n.getChildrenArray().length == 0;
	}
	
	private void nodeOut(ITreeNode n, List<String> path, List<AlgorithmDescr> algoList) {
		Object[] children = n.getChildrenArray();
		for(Object c: children) {
			ITreeNode tn = (ITreeNode) c;
			if(isLeaf(tn)) {
				addAlgoDescr(algoList, tn, path);
			} else {
				LinkedList<String> newPath = new LinkedList<String>(path);
				newPath.add(n.getName());
				
				nodeOut(tn, newPath, algoList);
			}
		}
	}
	
	private void addAlgoDescr(List<AlgorithmDescr> algoList, ITreeNode tn,
			List<String> path) {
		AlgorithmDescr a = makeAlgorithm(tn.getName(), path);
		algoList.add(a);
	}

	protected AlgorithmDescr makeAlgorithm(String displayedName, List<String> basePath) {
		return makeBasePathAlgorithm(basePath, displayedName, displayedName, getPerspective());
	}

	public ITreeNode getBaseNode() {
		return baseNode;
	}
	
}
