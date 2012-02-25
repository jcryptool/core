//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.verifiablesecretsharing.views;

import org.eclipse.osgi.util.NLS;

/**
 *
 * This class defines variables, which are used in the GUI. The values of this
 * variables are defined in the messages.properties and messages_de.properties.
 * This is an easy way to change text printed in the GUI, without doing anything
 * in the actual code.
 *
 * @author Dulghier Christoph, Reisinger Kerstin, Tiefenbacher Stefan, Wagner Thomas
 */

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.verifiablesecretsharing.views.messages"; //$NON-NLS-1$

	public static String VerifiableSecretSharingComposite_tab_title;
	public static String VerifiableSecretSharingComposite_description;
	public static String VerifiableSecretSharingComposite_restart_button;
	public static String VerifiableSecretSharingComposite_parameters_title;
	public static String VerifiableSecretSharingComposite_coefficients_title;
	public static String VerifiableSecretSharingComposite_commitments_title;
	public static String VerifiableSecretSharingComposite_shares_title;
	public static String VerifiableSecretSharingComposite_reconstruction_title;

	//texts for descriptions
	public static String VerifiableSecretSharingComposite_description_title;
	public static String VerifiableSecretSharingComposite_description_tooltip;
	
	//texts for parameters
	public static String VerifiableSecretSharingComposite_parameters_players;
	public static String VerifiableSecretSharingComposite_parameters_reconstructors;
	public static String VerifiableSecretSharingComposite_parameters_secret;
	public static String VerifiableSecretSharingComposite_parameters_primeMod;
	public static String VerifiableSecretSharingComposite_parameters_primeFactorMod;
	public static String VerifiableSecretSharingComposite_parameters_primitiveRoot;
	public static String VerifiableSecretSharingComposite_parameters_determineCoefficients;
	
	//Next step button for Parameters and Coefficients
	public static String VerifiableSecretSharingComposite_nextStep_button;
	public static String VerifiableSecretSharingComposite_coefficients_generate_button;
	public static String VerifiableSecretSharingComposite_coefficients_commit_button;
	public static String VerifiableSecretSharingComposite_coefficients_calculateShares_button;
	
	public static String VerifiableSecretSharingComposite_commitments_coefficient_subtitle;
	public static String VerifiableSecretSharingComposite_commitments_commitment_subtitle;
	public static String VerifiableSecretSharingComposite_shares_shareNModP_subtitle;

	//playerX for Shares and Reconstruction
	public static String VerifiableSecretSharingComposite_playerX;
	public static String VerifiableSecretSharingComposite_shares_check_button;
	
	public static String VerifiableSecretSharingComposite_reconstruction_reconstruct_button;
	public static String VerifiableSecretSharingComposite_reconstruction_reconstruct_dialog_title;
	public static String VerifiableSecretSharingComposite_reconstruction_reconstruct_dialog_text;
		
	//errors general
	public static String VerifiableSecretSharingComposite_error_start;
	
	
	//errors for setting parameters
	public static String VerifiableSecretSharingComposite_param_player_n_positive;
	public static String VerifiableSecretSharingComposite_param_player_n_bigger_2;
	public static String VerifiableSecretSharingComposite_param_player_t_smaller_n;
	public static String VerifiableSecretSharingComposite_param_secret_s_positive;
	public static String VerifiableSecretSharingComposite_param_module_p_bigger_s;
	public static String VerifiableSecretSharingComposite_param_module_p_isPrime;
	public static String VerifiableSecretSharingComposite_param_primitive_g;
	public static String VerifiableSecretSharingComposite_param_set_all;
	public static String VerifiableSecretSharingComposite_param_p_not_safe_prime;
	
	//errors for setting the coefficients
	public static String VerifiableSecretSharingComposite_coefficient_positive;
	public static String VerifiableSecretSharingComposite_coefficient_smaller_p;
	public static String VerifiableSecretSharingComposite_coefficient_set_all;
	
	public static String VerifiableSecretSharingComposite_commitment_not_calculated;
	
	
	//reconstruction chart
	public static String ChartComposite_tab_title;
	public static String ChartComposite_reconstruct_success;
	public static String ChartComposite_reconstruct_failure;
	public static String ChartComposite_noGraph;

	public static String VerifiableSecretSharingComposite_reconstruct_no_players;
	
	//boxes with further explanations
	public static String VerifiableSecretSharingComposite_description_parameters_left;
	public static String VerifiableSecretSharingComposite_description_parameters_right;

	public static String VerifiableSecretSharingComposite_description_coefficients_left;
	public static String VerifiableSecretSharingComposite_description_coefficients_right;
	
	public static String VerifiableSecretSharingComposite_description_commitments_left;
	public static String VerifiableSecretSharingComposite_description_commitments_right;

	public static String VerifiableSecretSharingComposite_description_shares_left;
	public static String VerifiableSecretSharingComposite_description_shares_right;
	
	public static String VerifiableSecretSharingComposite_description_reconstruction_left;
	public static String VerifiableSecretSharingComposite_description_reconstruction_right;
	public static String VerifiableSecretSharingComposite_description_reconstruction_right_part2;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
