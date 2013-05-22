package org.jcryptool.crypto.analysis.substitution.calc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.editors.EditorUtils;
import org.jcryptool.crypto.analysis.substitution.Activator;
import org.jcryptool.crypto.analysis.substitution.ui.modules.utils.PredefinedStatisticsProvider;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;
import org.jcryptool.crypto.classic.alphabets.preferences.TransformationPreferenceSet;
import org.jcryptool.crypto.classic.alphabets.preferences.TransformationsPreferencePage;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.Preferences;

public class DynamicPredefinedStatisticsProvider implements PredefinedStatisticsProvider {

	private static class Info {
		private String name;
		private String language;
		private Info(String name, String language) {
			super();
			this.name = name;
			this.language = language;
		}
		public String getName() {
			return name;
		}
		public String getLanguage() {
			return language;
		}
	}
	
	private static Map<Info, File> predefinedTextFiles;
	private List<TextStatistic> statistics;
	
	public static File fileFromPath(String path) {
		Bundle bundle = Activator.getDefault().getBundle();
		URL fileURL = bundle.getEntry(path);
		File file = null;
		try {
		    file = new File(FileLocator.resolve(fileURL).toURI());
		} catch (URISyntaxException e1) {
		    e1.printStackTrace();
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		return file;
	}
	
	static {
		predefinedTextFiles = new LinkedHashMap<DynamicPredefinedStatisticsProvider.Info, File>();
		
		File kafka = fileFromPath("refTexts/Die_Verwandlung_1_2.txt"); //$NON-NLS-1$
		File poe = fileFromPath("refTexts/The_Murders_in_the_Rue_Morgue.txt"); //$NON-NLS-1$
		
		if(kafka != null && kafka.exists()) {
			predefinedTextFiles.put(new Info("Die Verwandlung (Kafka)", Messages.DynamicPredefinedStatisticsProvider_3), kafka); //$NON-NLS-1$
		}
		if(poe != null && poe.exists()) {
			predefinedTextFiles.put(new Info("The Murders in the Rue Morgue (Poe)", Messages.DynamicPredefinedStatisticsProvider_5), poe); //$NON-NLS-1$
		}
		
		if(predefinedTextFiles.isEmpty()) {
			String message = "Could not locate reference text files for statistics"; //$NON-NLS-1$
			LogUtil.logError(Activator.PLUGIN_ID, message);
			System.out.println(message);
			
		}
	}
	
	public static void main(String[] args) {
		for(File f: predefinedTextFiles.values()) System.out.println(f.exists());
	}
	
	private static String getTextFromFile(File f) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		return EditorUtils.inputStreamToString(fis);
	}
	
	public DynamicPredefinedStatisticsProvider() {
		Map<AbstractAlphabet, TransformData> alphabets = getAlphabetTransformDataMapping();
		statistics = analyzeFiles(predefinedTextFiles, alphabets);
	}
	
	private static List<TextStatistic> analyzeFiles(Map<Info, File> predefinedTextFiles, Map<AbstractAlphabet, TransformData> alphabets) {
		List<TextStatistic> result = new LinkedList<TextStatistic>();
		for(Entry<Info, File> fileEntry: predefinedTextFiles.entrySet()) {
			File f = fileEntry.getValue();
			Info i = fileEntry.getKey();
			for(Entry<AbstractAlphabet, TransformData> alphaEntry: alphabets.entrySet()) {
				try {
					String fileText = getTextFromFile(f);
					AbstractAlphabet alpha = alphaEntry.getKey();
					TransformData tr = alphaEntry.getValue();
					
					String transformedText = Transform.transformText(fileText, tr);
					TextStatistic statistic = new TextStatistic(transformedText, i.getName(), i.getLanguage());
					result.add(statistic);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/** prooves, if a specific node in the transformation settings exists
	 * @param myNode parent node
	 * @return whether the node exists or not
	 */
	private static boolean nodeExists(Preferences myNode) {
		if(! myNode.get(TransformationPreferenceSet.ID_TRANSFORM_DATA, "default").equals("default")) return true; //$NON-NLS-1$ //$NON-NLS-2$
		return false;
	}
	
	/** load the standard Transformation for a specified currentAlphabet from the global settings.
	 * @param alphaName the name of the currentAlphabet
	 * @return the Transformation
	 */
	public static TransformData getTransformFromName(String alphaName) {
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(TransformationsPreferencePage.PREFID);
		Preferences mainnode = preferences.node(TransformationsPreferencePage.SUBNODE);
		Preferences myNode = mainnode.node(alphaName);
		if(nodeExists(myNode)) {
			TransformData loadedPreferenceSet = TransformationsPreferencePage.getDataFromNode(myNode);
			return loadedPreferenceSet;
		}
		else return null;
	}
	
	private Map<AbstractAlphabet, TransformData> getAlphabetTransformDataMapping() {
		LinkedHashMap<AbstractAlphabet, TransformData> result = new LinkedHashMap<AbstractAlphabet, TransformData>();
//		for(AbstractAlphabet a: AlphabetsManager.getInstance().getAlphabets()) {
//			TransformData d = getTransformFromName(a.getName());
//			if(d != null) {
//				result.put(a, d);
//			} else if(TransformationPreferenceSet.hasStandardSetting(a.getName())){
//				TransformData defaultSetting = TransformationPreferenceSet.getDefaultSetting(a.getName());
//				result.put(a, defaultSetting);
//			}
//		}
//		return result;
		AtomAlphabet aA09 = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
		AtomAlphabet aA = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		AtomAlphabet a = new AtomAlphabet("abcdefghijklmnopqrstuvwxyz");
		AtomAlphabet A = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		AtomAlphabet printable = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzäöüÄÖÜ01234567890.,-!?= ");
		
		TransformData taA09 = new TransformData(aA09, false, false, true, true, true);
		TransformData taA = new TransformData(aA09, false, false, true, true, true);
		TransformData ta = new TransformData(aA09, true, false, true, true, true);
		TransformData tA = new TransformData(aA09, true, true, true, true, true);
		TransformData tPrintable = new TransformData(printable, false, false, false, true, false);
		
		result.put(aA09, taA09);
		result.put(aA, taA);
		result.put(a, ta);
		result.put(A, tA);
		result.put(printable, tPrintable);
		
		return result;
		
	}

	@Override
	public List<TextStatistic> getPredefinedStatistics() {
		return new LinkedList<TextStatistic>(statistics);
	}

}
