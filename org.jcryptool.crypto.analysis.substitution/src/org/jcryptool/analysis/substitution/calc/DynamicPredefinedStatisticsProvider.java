package org.jcryptool.analysis.substitution.calc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.jcryptool.analysis.substitution.Activator;
import org.jcryptool.analysis.substitution.ui.modules.utils.PredefinedStatisticsProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.editors.EditorUtils;
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
	
	private static Map<Info, String> predefinedTextFiles;
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
		predefinedTextFiles = new LinkedHashMap<DynamicPredefinedStatisticsProvider.Info, String>();
		
		String kafka = "refTexts/Die_Verwandlung_1_2.txt"; //$NON-NLS-1$
		String poe = "refTexts/The_Murders_in_the_Rue_Morgue.txt"; //$NON-NLS-1$
		
			predefinedTextFiles.put(new Info("Die Verwandlung (Kafka)", Messages.DynamicPredefinedStatisticsProvider_3), kafka); //$NON-NLS-1$
			predefinedTextFiles.put(new Info("The Murders in the Rue Morgue (Poe)", Messages.DynamicPredefinedStatisticsProvider_5), poe); //$NON-NLS-1$
		
		if(predefinedTextFiles.isEmpty()) {
			String message = "Could not locate reference text files for statistics"; //$NON-NLS-1$
			LogUtil.logError(Activator.PLUGIN_ID, message);
			System.out.println(message);
			
		}
	}
	
	private static InputStream openMyTestStream(final String filename) {
        try {
            URL installURL = Activator.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            URL url = new URL(installURL, filename);
            return (url.openStream());
        } catch (MalformedURLException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        return null;
    }
	
	private static String getTextFromFile(String path) throws FileNotFoundException {
		InputStream s = openMyTestStream(path);
		return EditorUtils.inputStreamToString(s);
	}
	
	public DynamicPredefinedStatisticsProvider() {
		Map<AbstractAlphabet, TransformData> alphabets = getAlphabetTransformDataMapping();
		long startTime = System.currentTimeMillis();
		statistics = analyzeFiles(predefinedTextFiles, alphabets);
		long endTime = System.currentTimeMillis();
//		System.out.println(String.format("############# Dauer: %d", endTime-startTime));
	}
	
	private static List<TextStatistic> analyzeFiles(Map<Info, String> predefinedTextFiles,
			Map<AbstractAlphabet, TransformData> alphabets) {
		List<TextStatistic> result = new LinkedList<TextStatistic>();
		for (Entry<Info, String> fileEntry : predefinedTextFiles.entrySet()) {
			String f = fileEntry.getValue();
			Info i = fileEntry.getKey();
			try {
				String fileText = getTextFromFile(f);
				for (Entry<AbstractAlphabet, TransformData> alphaEntry : alphabets.entrySet()) {
					AbstractAlphabet alpha = alphaEntry.getKey();
					TransformData tr = alphaEntry.getValue();

					String transformedText = prepareFileTextForAnalysis(fileText, tr, alpha);
					TextStatistic statistic = new TextStatistic(transformedText, i.getName(), i.getLanguage());
					result.add(statistic);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String prepareFileTextForAnalysis(String fileText, TransformData tr, AbstractAlphabet alpha) {
		String text = Transform.transformText(fileText, tr);
		text = text.replaceAll(Pattern.quote("\n"), "");
		text = text.replaceAll(Pattern.quote("\r"), "");
		text = text.replaceAll(Pattern.quote("\t"), "");
		return text;
	}

	/** prooves, if a specific node in the transformation settings exists
	 * @param myNode parent node
	 * @return whether the node exists or not
	 */
	private static boolean nodeExists(Preferences myNode) {
		if(! myNode.get(TransformationPreferenceSet.ID_TRANSFORM_DATA, "default").equals("default")) return true; //$NON-NLS-1$ //$NON-NLS-2$
		return false;
	}
	
	private Map<AbstractAlphabet, TransformData> getAlphabetTransformDataMapping() {
		LinkedHashMap<AbstractAlphabet, TransformData> result = new LinkedHashMap<AbstractAlphabet, TransformData>();
		AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
		for(AbstractAlphabet alpha: alphas) {
			if(!isExcludedAlphabet(alpha)) {
				result.put(alpha, TransformationPreferenceSet.getDefaultByHeuristic(alpha));
			}
		}
		
		return result;
	}

	private boolean isExcludedAlphabet(AbstractAlphabet alpha) {
		if(!alpha.isBasic()) return true;
		if(alpha.getName().toLowerCase().contains("xor")) return true;
		if(alpha.getName().toLowerCase().contains("adfgvx")) return true;
		return false;
	}

	@Override
	public List<TextStatistic> getPredefinedStatistics() {
		return new LinkedList<TextStatistic>(statistics);
	}

}
