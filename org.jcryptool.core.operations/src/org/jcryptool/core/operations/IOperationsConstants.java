// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations;

/**
 * Interface containing various constants.
 * 
 * @author t-kern
 * @author Holger Friedrich (support for Commands)
 * 
 */
public interface IOperationsConstants {

    /* An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    // String ATT_ACTION_CLASS = "actionClass"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_BLOCKLENGTHS = "blocklengths"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_CIPHER_MODES = "cipherModes"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_CLASS = "class"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_CMD_CLASS = "cmdClass"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_DISABLED_ICON = "disabledIcon"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_ENGINE_CLASS = "engineClass"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_HANDLER_CLASS = "handlerClass"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_HELP_CONTEXT_ID = "contextHelpId"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_HOVER_ICON = "hoverIcon"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_ICON = "icon"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_ID = "id"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_KEYLENGTHS = "keylengths"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_NAME = "name"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_PADDINGS = "paddings"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_PROVIDER_NAME = "providerName"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_PROVIDER_INFO = "providerInfo"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_TOOLTIP = "tooltip"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_TYPE = "type"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_USER_TYPE = "user_defined_type"; //$NON-NLS-1$

    /** An attribute in an ExtensionPoint. The value is <code>{@value}</code> */
    String ATT_WIZARD_CLASS = "wizardClass"; //$NON-NLS-1$

    /** The hex editor plug-in ID. The value is <code>{@value}</code> */
    String ID_HEX_EDITOR_PLUGIN = "org.jcryptool.editor.hex"; //$NON-NLS-1$

    /** An editor ID. The value is <code>{@value}</code> */
    String ID_HEX_EDITOR = "org.jcryptool.editors.hex.HexEditor"; //$NON-NLS-1$

    /** The text editor plug-in ID. The value is <code>{@value}</code> */
    String ID_TEXT_EDITOR_PLUGIN = "org.jcryptool.editor.text"; //$NON-NLS-1$

    /** An editor ID. The value is <code>{@value}</code> */
    String ID_TEXT_EDITOR = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$

    /* A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    //String PL_ALGORITHMS = "algorithms"; //$NON-NLS-1$

    /** A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    String PL_ALGORITHMS_CMD = "algorithms_cmd"; //$NON-NLS-1$

    /** A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    String PL_ALGORITHM_WIZARDS = "algorithmWizards"; //$NON-NLS-1$

    /** A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    String PL_ALPHABETS = "alphabets"; //$NON-NLS-1$

    /** A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    String PL_EDITOR_SERVICES = "editorServices"; //$NON-NLS-1$

    /** A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    String PL_PROVIDERS = "providers"; //$NON-NLS-1$

    /** A Plug-in (ExtensionPoint) identifier. The value is <code>{@value}</code> */
    String PL_PROVIDERS2 = "providers2"; //$NON-NLS-1$

    /** An identifier. The value is <code>{@value}</code> */
    String PLATFORM = "platform"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String TAG_ALGORITHM = "algorithm"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String TAG_ALGORITHM_CMD = "algorithm_cmd"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String TAG_PROVIDER = "provider"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String TAG_PROVIDER2 = "provider2"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String TAG_SERVICE = "service"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String TAG_WIZARD = "wizard"; //$NON-NLS-1$

    /** A tag for an Extension. The value is <code>{@value}</code> */
    String ATT_FLEXIPROVIDER = "isFlexiProviderAlgorithm"; //$NON-NLS-1$
}
