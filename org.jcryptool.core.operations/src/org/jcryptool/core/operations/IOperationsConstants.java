//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations;

/**
 * Interface containing various constants.
 * 
 * @author t-kern
 *
 */
public interface IOperationsConstants {

	/** An attribute in an ExtensionPoint. The value is <code>actionClass</code> */
	public static final String ATT_ACTION_CLASS = "actionClass"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>blocklengths</code> */
	public static final String ATT_BLOCKLENGTHS = "blocklengths"; //$NON-NLS-1$
		
	/** An attribute in an ExtensionPoint. The value is <code>cipherModes</code> */
	public static final String ATT_CIPHER_MODES = "cipherModes"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>class</code> */
	public static final String ATT_CLASS = "class"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>cmdClass</code> */
	public static final String ATT_CMD_CLASS = "cmdClass"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>disabledIcon</code> */
	public static final String ATT_DISABLED_ICON = "disabledIcon"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>engineClass</code> */
	public static final String ATT_ENGINE_CLASS = "engineClass"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>contextHelpId</code> */
	public static final String ATT_HELP_CONTEXT_ID = "contextHelpId"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>hoverIcon</code> */
	public static final String ATT_HOVER_ICON = "hoverIcon"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>icon</code> */
	public static final String ATT_ICON = "icon"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>id</code> */
	public static final String ATT_ID = "id"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>keylengths</code> */
	public static final String ATT_KEYLENGTHS = "keylengths"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>name</code> */
	public static final String ATT_NAME = "name"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>paddings</code> */
	public static final String ATT_PADDINGS = "paddings"; //$NON-NLS-1$
		
	/** An attribute in an ExtensionPoint. The value is <code>providerName</code> */
	public static final String ATT_PROVIDER_NAME = "providerName"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>providerInfo</code> */
	public static final String ATT_PROVIDER_INFO = "providerInfo"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>tooltip</code> */
	public static final String ATT_TOOLTIP = "tooltip"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>type</code> */
	public static final String ATT_TYPE = "type"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>user_defined_type</code> */
	public static final String ATT_USER_TYPE = "user_defined_type"; //$NON-NLS-1$
	
	/** An attribute in an ExtensionPoint. The value is <code>wizardClass</code> */
	public static final String ATT_WIZARD_CLASS = "wizardClass"; //$NON-NLS-1$
	
	/** An editor Plugin ID. The value is <code>org.jcryptool.editor.hex</code> */
	public static final String ID_HEX_EDITOR_PLUGIN = "org.jcryptool.editor.hex"; //$NON-NLS-1$
		
	/** An editor ID. The value is <code>org.jcryptool.editors.hex.HexEditor</code> */
	public static final String ID_HEX_EDITOR = "org.jcryptool.editors.hex.HexEditor"; //$NON-NLS-1$
	
	/** An editor ID. The value is <code>org.jcryptool.editor.text.editor.JCTTextEditor</code> */
	public static final String ID_TEXT_EDITOR = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$
	
	/** A Plug-in (ExtensionPoint) identifier. The value is <code>algorithms</code> */
	public static final String PL_ALGORITHMS = "algorithms"; //$NON-NLS-1$
	
	/** A Plug-in (ExtensionPoint) identifier. The value is <code>algorithmWizards</code> */
	public static final String PL_ALGORITHM_WIZARDS = "algorithmWizards"; //$NON-NLS-1$
	
	/** A Plug-in (ExtensionPoint) identifier. The value is <code>alphabets</code> */
	public static final String PL_ALPHABETS = "alphabets"; //$NON-NLS-1$
	
	/** A Plug-in (ExtensionPoint) identifier. The value is <code>editorServices</code> */
	public static final String PL_EDITOR_SERVICES = "editorServices"; //$NON-NLS-1$
	
	/** A Plug-in (ExtensionPoint) identifier. The value is <code>providers</code> */
	public static final String PL_PROVIDERS = "providers";	 //$NON-NLS-1$
	
	/** A Plug-in (ExtensionPoint) identifier. The value is <code>providers2</code> */
	public static final String PL_PROVIDERS2 = "providers2"; //$NON-NLS-1$
	
	/** An identifier. The value is <code>platform</code> */
	public static final String PLATFORM = "platform"; //$NON-NLS-1$
	
	/** A tag for an Extension. The value is <code>algorithm</code> */
	public static final String TAG_ALGORITHM = "algorithm"; //$NON-NLS-1$
	
	/** A tag for an Extension. The value is <code>provider</code> */
	public static final String TAG_PROVIDER = "provider"; //$NON-NLS-1$
	
	/** A tag for an Extension.  The value is <code>provider2</code> */
	public static final String TAG_PROVIDER2 = "provider2"; //$NON-NLS-1$
	
	/** A tag for an Extension. The value is <code>service</code> */
	public static final String TAG_SERVICE = "service"; //$NON-NLS-1$
	
	/** A tag for an Extension. The value is <code>wizard</code> */
	public static final String TAG_WIZARD = "wizard"; //$NON-NLS-1$

	/** A tag for an Extension. The value is <code>isFlexiProvider</code> */
	public static final String ATT_FLEXIPROVIDER = "isFlexiProviderAlgorithm"; //$NON-NLS-1$
}
