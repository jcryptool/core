package org.jcryptool.functionlistgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;
import org.jcryptool.core.ApplicationActionBarAdvisor;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmHandler;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.views.AlgorithmView;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.providers.FlexiProviderAlgorithmsViewContentProvider;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;

public class AtStartup implements IStartup {

	private static final String MESSAGE_DIGESTS = "Message Digests";
	private static final String SIGNATURES = "Signatures";
	private static final String PSEUDO_RANDOM_NUMBER_GENERATORS = "Pseudo Random Number Generators";
	private static final String HYBRID_CIPHERS = "Hybrid Ciphers";
	private static final String MESSAGE_AUTHENTICATION_CODES = "Message Authentication Codes";
	private static final String BLOCK_CIPHERS = "Block Ciphers";
	private static final String PASSWORD_BASED_CIPHERS = "Password-Based Ciphers";
	private static final String ASYMMETRIC_BLOCK_CIPHERS = "Asymmetric Block Ciphers";

	/**
	 * translates the "Algorithm perspective" Algorithms from all languages to English.
	 * This is necessary to use them as secondary sorting IDs so that all entries are written to 
	 * the output in the same order across all languages 
	 */
	private static Map<String, String> flexiproviderTranslations = new HashMap<String, String>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{ 
		put(MESSAGE_DIGESTS                  , MESSAGE_DIGESTS);
		put(SIGNATURES                       , SIGNATURES);
		put(PSEUDO_RANDOM_NUMBER_GENERATORS  , PSEUDO_RANDOM_NUMBER_GENERATORS);
		put(HYBRID_CIPHERS                   , HYBRID_CIPHERS);
		put(MESSAGE_AUTHENTICATION_CODES     , MESSAGE_AUTHENTICATION_CODES);
		put(BLOCK_CIPHERS                    , BLOCK_CIPHERS);
		put(PASSWORD_BASED_CIPHERS           , PASSWORD_BASED_CIPHERS);
		put(ASYMMETRIC_BLOCK_CIPHERS         , ASYMMETRIC_BLOCK_CIPHERS);

		put("Hash-Funktionen"                , MESSAGE_DIGESTS);
		put("Signaturen"                     , SIGNATURES);
		put("Pseudo-Zufallszahlengeneratoren", PSEUDO_RANDOM_NUMBER_GENERATORS);
		put("Hybride Verschlüsselung"        , HYBRID_CIPHERS);
		put("Authentifizierungscodes"        , MESSAGE_AUTHENTICATION_CODES);
		put("Blockchiffren"                  , BLOCK_CIPHERS);
		put("Passwortbasierte Chiffren"      , PASSWORD_BASED_CIPHERS);
		put("Asymmetrische Blockchiffren"    , ASYMMETRIC_BLOCK_CIPHERS);
	}};

	/**
	 * This method is called at JCT startup. It checks the command line arguments for -GenerateFunctionList
	 * and parses them like this:
	 * - no further argument: create files in ~/Documents/.jcryptool
	 * - further arguments: split by ",," and write to all these files
	 * 
	 * further information about the purpose of this is available at https://github.com/simlei/org.cryptool.functionlist
	 */
	@Override
	public void earlyStartup() {
		List<File> outputs = new LinkedList<File>();
		String[] cmdlineargs = Platform.getCommandLineArgs();

		//System.err.println("[JCT-debug] Function List Plugin is processing command line parameters of length " + cmdlineargs.length + "...");
		boolean pluginActivated = false;
		for (int i = 0; i < cmdlineargs.length; i++) {
			String currentArg = cmdlineargs[i];
			if(currentArg.equals("-GenerateFunctionList")) {
				pluginActivated=true;
				if (cmdlineargs.length -1 == i) {
					System.err.println("-GenerateFunctionList takes a target file argument. It was not provided, so the default location will be used.");
					outputs.add(new File(DirectoryService.getWorkspaceDir(), "functionlist_jct_" + Locale.getDefault().toString() + ".csv"));
				} else {
					String nextArg = cmdlineargs[i+1];
					String[] split = nextArg.split(",,");
					for (String sp : split) {
						if( ! Paths.get(sp).isAbsolute() ) {
							System.err.println("-GenerateFunctionList file argument is not an absolute path; it is interpreted as relative to " + DirectoryService.getWorkspaceDir().toString());
							File wsDir = new File(DirectoryService.getWorkspaceDir());
							outputs.add(Paths.get(wsDir.getAbsolutePath(), sp).toFile());
						} else {
							outputs.add(Paths.get(sp).toFile());
						}
					}
				}
			}
		}
		if (! pluginActivated) return;

		List<FunctionalityRecord> records = generateFunctionalities();
		BufferedWriter writer;
		File currentOutput = null;
		try {
			for (File output : outputs) {
				currentOutput = output;
				writeFunctionalitiesToFile(output, records);
			}
		} catch (FileNotFoundException e) {
			System.err.println("cannot write to file " + currentOutput.toString());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("cannot write to file -- " + currentOutput.toString() + " " + e.getMessage());
			System.exit(1);
		}
		System.err.println("Functionality list written successfully to " + outputs.toString());
		System.exit(0);
	}

	/**
	 * @param output the output file
	 * @param records the functionality records
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeFunctionalitiesToFile(File output, List<FunctionalityRecord> records) throws FileNotFoundException, IOException {
		BufferedWriter writer;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
		// currently, JCT has no notion of "Functionalities" that span multiple entries like CT2.
		// These have to be mapped afterwards. One header, one functionality path
		for (FunctionalityRecord record : records) {
			String header = String.format("%s;[n.a.];", record.getFunctionality());
			writer.append(header);
			writer.newLine();
			writer.append(record.createCSVLine());
			writer.newLine();
			writer.newLine();
		}
		writer.close();
	}
	
	/**
	 * crawls JCT extension points and algorithm managers for all algorithms that are interesting to the "functionality list"
	 * @return a list of functionality records
	 */
	private static List<FunctionalityRecord> generateFunctionalities() {
		List<List<String>> flexiPaths = flexiproviderPaths();
		List<List<String>> algoviewPaths = algoviewPaths();
		List<FunctionalityRecord> allRecords = new LinkedList<FunctionalityRecord>();

		// reads all flexiprovider functionality paths and attaches some more information like '[A]'
		for (List<String> flexiPath : flexiPaths) {
			Character howImplementedChar = 'A';
			
			// for the secondary ID, that ensures correct sorting of the flexiprovider ciphers
			String sortingId = flexiPath.get(0);
			if (flexiproviderTranslations.containsKey(sortingId.trim())) {
				sortingId = flexiproviderTranslations.get(sortingId.trim());
			} else {
				System.err.println(String.format("id: %s", sortingId));
				System.err.println("could not map all of flexiProvider Paths to a secondary ID (for sorting) -- this would most likely cause entries to mismatch between languages. Please update the hardcoded flexiprovider secondary id map.");
				System.exit(0);
			}
			
			final String finalSortingId = sortingId;
			FunctionalityRecord entry = new FunctionalityRecord() {{
				setPrimaryId("fpAlgorithms");
				setSecondaryId(finalSortingId);
				setPath(flexiPath);
				setHowImplemented(howImplementedChar);
			}};
			allRecords.add(entry);
		}
		
		// reads the paths from the algorithm view
		int algoviewCounter = 0;
		for (List<String> algoviewPath : algoviewPaths) {
			Character howImplementedChar = 'D';
			String extpointLike = algoviewPath.get(0);
			List<String> realpath = algoviewPath.subList(1, algoviewPath.size());
			
			// these entries are yielded from an extension point
			// an extension point is assumed to yield its extensions in the same order as they were provided
			// therefore, as a secondary id (for consistent sorting between languages), a running number is sufficient
			String runningId = String.format("%04d", algoviewCounter);

			FunctionalityRecord entry = new FunctionalityRecord() {{
				setPrimaryId(extpointLike);
				setSecondaryId(runningId);
				setPath(realpath);
				setHowImplemented(howImplementedChar);
			}};
			allRecords.add(entry);
// 			System.out.println(algoviewPath.toString());
			algoviewCounter++;
		} 
		
		// sort the algorithm list according to the method outlines in FunctionalityRecord.
		// we have made sure to provide a first- and second-order sorting indicator string
		// and that the order of entries is, where those indicators are equal, the initial order
		// of FunctionalityRecord is the same across all languages, so that .sort() as a stable sorting
		// algorithm will preserve the same order everywhere.
		allRecords.sort(new Comparator<FunctionalityRecord>() {
			@Override
			public int compare(FunctionalityRecord o1, FunctionalityRecord o2) {
				return o1.compareTo(o2);
			}
		});
		return allRecords;
	}

	private static List<List<String>> algoviewPaths() {
		String extp;
		String label1;
		
		LinkedList<List<String>> allPaths = new LinkedList<List<String>>();
		LinkedList<List<List<String>>> viewPaths = new LinkedList<List<List<String>>>();

        // algorithm folder
		label1 = AlgorithmView.MENU_TEXT_ALGORITHM;
		List<List<String>> algoPaths = algoFunctionalities();
		allPaths.addAll(prefixWith("notactually.an.extension.point.algorithmsFunctionalities", 
				prefixWith(label1, algoPaths)
				));

        // analysis folder
		label1 = AlgorithmView.MENU_TEXT_ANALYSIS;
		extp = "org.jcryptool.core.operations.analysis";
		List<List<String>> analysisPaths = viewPaths(extp);
		viewPaths.add(analysisPaths);
		allPaths.addAll(prefixWith(extp, analysisPaths));
		
        // visuals folder
		label1=AlgorithmView.MENU_TEXT_VISUALS;
		extp="org.jcryptool.core.operations.visuals";
		List<List<String>> visualPaths = viewPaths(extp);
		viewPaths.add(visualPaths);
		allPaths.addAll(prefixWith(extp, visualPaths));

        // games folder
		label1=AlgorithmView.MENU_TEXT_GAMES;
		extp="org.jcryptool.core.operations.games";
		List<List<String>> gamesPaths = viewPaths(extp);
		viewPaths.add(gamesPaths);
		allPaths.addAll(prefixWith(extp, gamesPaths));
		
		return allPaths;
	}
	
	private static List<List<String>> flexiproviderPaths() {
		FlexiProviderAlgorithmsViewContentProvider fpTreeMan = new FlexiProviderAlgorithmsViewContentProvider(null);
		fpTreeMan.initialize();
		ITreeNode fpRoot = fpTreeMan._invisibleRoot;
		List<List<String>> result = new LinkedList<List<String>>();
		result.addAll(traverseTree(fpRoot, fpRoot));
		result = result.stream().map((path) -> path.subList(1, path.size())).collect(Collectors.toList());
		return result;
	}

	private static List<List<String>> viewPaths(String extp) {
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(extp);

        String label = ""; //$NON-NLS-1$
        if (extensionPoint.getLabel().equals("analysis")) { //$NON-NLS-1$
            label = AlgorithmView.MENU_TEXT_ANALYSIS;
        } else if (extensionPoint.getLabel().equals("games")) { //$NON-NLS-1$
            label = AlgorithmView.MENU_TEXT_GAMES;
        } else if (extensionPoint.getLabel().equals("visuals")) { //$NON-NLS-1$
            label = AlgorithmView.MENU_TEXT_VISUALS;
        }
        LinkedList<List<String>> result = new LinkedList<List<String>>();
        for (IConfigurationElement element : extensionPoint.getConfigurationElements()) {
        	LinkedList<String> path = new LinkedList<String>();
        	path.add(label);
        	path.add(element.getAttribute("name"));
        	result.add(path);
        }
        return result;
	}
	
	
	private static List<List<String>> algoFunctionalities() {
        CommandInfo[] commands = OperationsPlugin.getDefault().getAlgorithmsManager()
                .getShadowAlgorithmCommands();
        
        LinkedList<List<String>> result = new LinkedList<List<String>>();
        for (int i = 0, length = commands.length; i < length; i++) {
        	AbstractHandler handler = commands[i].getHandler();
			ShadowAlgorithmHandler shadowHandler = (ShadowAlgorithmHandler) handler;
        	LinkedList<String> path = new LinkedList<String>();

			String type = shadowHandler.getType();
			String translatedType = ApplicationActionBarAdvisor.getTypeTranslation(type);
			String name = shadowHandler.getText();
			path.add(translatedType);
			path.add(name);
			result.add(path);
        }
        return result;
	}

	private static List<List<String>> traverseTree(ITreeNode el, ITreeNode root) {

// 		System.out.println(String.format("%s - %s", el.getName(), el.toString()));
		if(el.hasChildren()) {
			List<List<String>> result = new LinkedList<List<String>>();
			el.getChildren().forEachRemaining((c) -> result.addAll(prefixWith(el.getName(), traverseTree(c, root))));
			return result;
		} else {
			List<List<String>> result = new LinkedList<List<String>>();
			List<String> pathResult = new LinkedList<String>(); 
			pathResult.add(el.getName());
			result.add(pathResult);
			return result;
		}
	}

	private static List<List<String>> prefixWith(String name, List<List<String>> paths) {
		List<List<String>> result = new LinkedList<List<String>>();
		for (List<String> path : paths) {
			LinkedList<String> appended = new LinkedList<String>();
			appended.add(name);
			appended.addAll(path);
			result.add(appended);
		}
		return result;
	}
}
