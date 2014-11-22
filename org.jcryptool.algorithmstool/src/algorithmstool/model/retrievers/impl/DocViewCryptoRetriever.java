package algorithmstool.model.retrievers.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jcryptool.core.operations.AbstractOperationsManager;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.OperationsPlugin;

import algorithmstool.model.AlgorithmDescr;
import algorithmstool.model.IAlgorithmDescr;
import algorithmstool.model.retrievers.BasePathRetriever;

@SuppressWarnings("serial")
public class DocViewCryptoRetriever extends BasePathRetriever {

	private static Map<String, String> DEFAULT_SUBCAT_NAMES;
	
	static {
		DEFAULT_SUBCAT_NAMES = new HashMap<String, String>(){{
			put("classic"	,	"Klassisch");
			put("symmetric"	,	"Symmetrisch");
			put("asymmetric",	"Asymmetrisch");
			put("mac"		,	"MAC");
			put("prng"		,	"Zufallszahlengenerator");
			put("hash"		,	"Hash");
			put("xml"		,	"XML Sicherheit");
			put("signature"	,	"Signatur");
		}};
	}
	
	private Map<String, String> subcatNames;

	private DocViewCryptoRetriever(List<String> basePath, Map<String, String> subcatNames, String descr) {
		super(basePath, BasePathRetriever.PERSPECTIVE_DOC, descr);
		this.subcatNames = subcatNames;
	}
	
	public DocViewCryptoRetriever(List<String> basePath, String descr) {
		this(basePath, DEFAULT_SUBCAT_NAMES, descr);
	}
	

	@Override
	public List<? extends IAlgorithmDescr> retrieve() {
		List<AlgorithmDescr> algorithms = new LinkedList<AlgorithmDescr>();
		
		AbstractOperationsManager algorithmsManager = OperationsPlugin.getDefault().getAlgorithmsManager();
		CommandInfo[] commands = algorithmsManager.getShadowAlgorithmCommands();
		for(CommandInfo command: commands) {
			String text = command.getText();
			String type = OperationsPlugin.getDefault().getAlgorithmsManager().getAlgorithmType(command);
			
			AlgorithmDescr algorithm = this.makeAlgorithm(text, type);
			algorithms.add(algorithm);
		}
		
		return algorithms;
	}
	
	protected AlgorithmDescr makeAlgorithm(String displayedName, String catID) {
		List<String> subBasePath = new LinkedList<String>(getBasePath());
		subBasePath.add(getNameFor(catID));
		
		return makeBasePathAlgorithm(subBasePath, displayedName, displayedName, getPerspective());
	}

	private String getNameFor(String subcatID) {
		String mapResult = this.subcatNames.get(subcatID);
		if(mapResult != null) return mapResult;
		return String.valueOf(subcatID);
	}

}
