//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.lfsr.algorithm;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.LfsrDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.crypto.modern.stream.lfsr.LfsrPlugin;
import org.jcryptool.crypto.modern.stream.lfsr.ui.LfsrWizard;
import org.jcryptool.crypto.modern.stream.lfsr.ui.LfsrWizardPage.DisplayOption;

/**
 * The LfsrAlgorithmHandler class is a specific implementation of
 * AbstractAlgorithmHandler
 *
 * @see org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler
 *
 * @author Tahir Kacak, Daniel Dwyer
 * @author Holger Friedrich (support for Commands, additional class based on
 *         LfsrAlgorithmAction)
 * @author Thorben Groos (automatic save / load function)
 * @version 0.3
 */
public class LfsrAlgorithmHandler extends AbstractAlgorithmHandler {
	
	private boolean[] seed;
	private boolean[] tapSettings;
	private int lfsrLength;
	private DisplayOption displayOption;
	private String keystreamLengthValue;

	/**
	 * Constructor
	 */
	public LfsrAlgorithmHandler() {
		super();
	}

	/**
	 * This method performs the action
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		LfsrWizard wizard = new LfsrWizard();
		WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setHelpAvailable(true);

		int result = dialog.open();
		
		seed = wizard.getSeed();
		tapSettings = wizard.getTapSettings();
		lfsrLength = wizard.getLfsrLength();
		displayOption = wizard.getDisplayOption();
		keystreamLengthValue = wizard.getKeystreamLengthValue();
		
		saveSeedAndTapSettings();

		if (result == Window.OK) {
			InputStream editorContent = getActiveEditorInputStream();

			if (wizard.getDisplayOption() == DisplayOption.KEYSTREAM_ONLY) {
				outputKeyStream(seed, tapSettings, wizard);
				return (null);
			}

			LfsrAlgorithm algorithm = new LfsrAlgorithm();

			algorithm.init(editorContent, seed, tapSettings);

			super.finalizeRun(algorithm);

			if (wizard.getDisplayOption() == DisplayOption.OUTPUT_AND_KEYSTREAM) {
				IEditorInput keyStream = AbstractEditorService.createOutputFile(Messages.LfsrAlgorithmAction_2, ".bin",
						((LfsrDataObject) algorithm.getDataObject()).getInputStream());
				try {
					getActiveWorkbenchWindow().getActivePage().openEditor(keyStream,
							IOperationsConstants.ID_HEX_EDITOR);
				} catch (PartInitException e) {
					MessageDialog.openError(getActiveWorkbenchWindow().getShell(), Messages.LfsrAlgorithmAction_0,
							NLS.bind(Messages.LfsrAlgorithmAction_1, IOperationsConstants.ID_HEX_EDITOR));
				}
			}
		}
		
		return (null);
	}

	/**
	 * Save the seed and the tap setting to a file to be able 
	 * to reset the plugin to its previous state.
	 */
	private boolean saveSeedAndTapSettings() {
		if (seed == null || tapSettings == null) {
			return false;
		}
		try {
			//Create the directory LFSR in folder .jcryptool if it doesn't exist.
			// The folder .jcryptool exists by default.
			File dir = new File(System.getProperty("user.home") + "/Documents/.jcryptool/LFSR");
			if (!dir.exists()) {
				dir.mkdir();
			}
			//create the file savedSettings.txt in folder LFSR if it doesn't exist.
			File file = new File(System.getProperty("user.home") + "/Documents/.jcryptool/LFSR/savedSettings.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			//Write the selected tap and seed in this file.
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Saved settings from the LFSR algorithm plugin (Algorithm -> Symmetric -> LFSR):\n");

			bw.write("This is the length of the LFSR:\n");
			bw.write(Integer.toString(lfsrLength) + "\n");
			
			bw.write("This is the seed. 0 represents seed is set to 0, 1 represents seed is set to 1.\n");
			for (int i = 0; i < seed.length; i++) {
				if (seed[i]) {
					bw.write("1");
				} else {
					bw.write("0");
				}
			}

			bw.write("\nThis is the tap setting. 0 represents that the tap is not set, 1 represents a tap that is set.\n");
			for (int i = 0; i < tapSettings.length; i++) {
				if (tapSettings[i]) {
					bw.write("1");
				} else {
					bw.write("0");
				}
			}
			
			bw.write("\nDisplay Option: OUTPUT_ONLY or OUTPUT_AND_KEYSTREAM or KEYSTREAM_ONLY. When KEYSTREAM_ONLY is selected three lines below the keystream length mus be enterd\n");
			bw.write(displayOption.toString() + "\n");
			bw.write("Keystream lenght: (only when KEYSTREAM_ONLY is selected)\n");
			if (displayOption == DisplayOption.KEYSTREAM_ONLY) {
				bw.write(keystreamLengthValue);
			}
			bw.close();
		} catch (IOException e) {
			LogUtil.logError(LfsrPlugin.PLUGIN_ID, e);
			return false;
		}
		return true;
	}

	@Override
	public void run(IDataObject dataobject) {
		LfsrAlgorithm algorithm = new LfsrAlgorithm();

		algorithm.dataObject = (LfsrDataObject) dataobject;

		super.finalizeRun(algorithm);
	}

	/**
	 * Opens an editor to display keystream of given length.
	 *
	 * @param seed        the keystream generator's seed
	 * @param tapSettings the keystream generator's tap settings
	 * @param wizard      used to retrieve the number of bytes of keystream to
	 *                    generate
	 */
	private void outputKeyStream(boolean[] seed, boolean[] tapSettings, LfsrWizard wizard) {
		int keyStreamByteCount = Integer.valueOf(wizard.getKeystreamLengthValue());

		LfsrKeyStreamGenerator keyStreamGenerator = new LfsrKeyStreamGenerator(seed, tapSettings);

		byte[] keyStream = generateKeyStream(keyStreamByteCount, keyStreamGenerator);
		ByteArrayOutputStream keyStreamOutputStream = new ByteArrayOutputStream();

		try {
			keyStreamOutputStream.write(keyStream);
		} catch (IOException e) {
			LogUtil.logError(LfsrPlugin.PLUGIN_ID, e);
		}

		IEditorInput keyStreamEditor = AbstractEditorService.createOutputFile(Messages.LfsrAlgorithmAction_2, ".bin",
				new BufferedInputStream(new ByteArrayInputStream(keyStreamOutputStream.toByteArray())));

		try {
			getActiveWorkbenchWindow().getActivePage().openEditor(keyStreamEditor, IOperationsConstants.ID_HEX_EDITOR);
		} catch (PartInitException e) {
			MessageDialog.openError(getActiveWorkbenchWindow().getShell(), Messages.LfsrAlgorithmAction_0,
					NLS.bind(Messages.LfsrAlgorithmAction_1, IOperationsConstants.ID_HEX_EDITOR));
		}
	}

	/**
	 * Generates an amount of keystream of given length.
	 *
	 * @param keyStreamByteCount the number of bytes of keystream to generate
	 * @param keyStreamGenerator the generator with which the keystream is produced
	 * @return the keystream as a byte array
	 */
	private byte[] generateKeyStream(int keyStreamByteCount, LfsrKeyStreamGenerator keyStreamGenerator) {
		boolean[] keyStream = new boolean[keyStreamByteCount * 8];

		for (int i = 0; i < keyStream.length; i++) {
			keyStream[i] = keyStreamGenerator.doRound();
		}
		return LfsrEngine.booleanArrayToByteArray(keyStream);
	}
}
