// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.constants;

/**
 * Interface for JCrypTool global constants. Use this constants in your plug-in wherever possible.
 * 
 * @author Dominik Schadow
 * @version 0.9.5
 */
public interface IConstants {
    /** Constant file extension for <b>binary</b> files (value is {@value} ). */
    String BIN_FILE_TYPE_EXTENSION = ".bin"; //$NON-NLS-1$
    /** Constant file extension for <b>csv</b> files (value is {@value} ). */
    String CSV_FILE_TYPE_EXTENSION = ".csv"; //$NON-NLS-1$
    /** Constant file extension for <b>pdf</b> files (value is {@value} ). */
    String PDF_FILE_TYPE_EXTENSION = ".pdf"; //$NON-NLS-1$
    /** Constant file extension for <b>tex</b> files (value is {@value} ). */
    String TEX_FILE_TYPE_EXTENSION = ".tex"; //$NON-NLS-1$
    /** Constant file extension for <b>text</b> files (value is {@value} ). */
    String TXT_FILE_TYPE_EXTENSION = ".txt"; //$NON-NLS-1$
    /** Constant file extension for <b>xml</b> files (value is {@value} ). */
    String XML_FILE_TYPE_EXTENSION = ".xml"; //$NON-NLS-1$
    /** Constant regular expression for generated <b>output files</b> (value is {@value} ). */
    String OUTPUT_REGEXP = "out\\d\\d\\d.((xml)|(bin)|(txt))"; //$NON-NLS-1$
    /** Constant filter extension for <b>bin</b> files (value is {@value} ). Use in file dialogs. */
    String BIN_FILTER_EXTENSION = "*.bin"; //$NON-NLS-1$
    /** Constant filter extension for <b>csv</b> files (value is {@value} ). Use in file dialogs. */
    String CSV_FILTER_EXTENSION = "*.csv"; //$NON-NLS-1$
    /** Constant filter extension for <b>pdf</b> files (value is {@value} ). Use in file dialogs. */
    String PDF_FILTER_EXTENSION = "*.pdf"; //$NON-NLS-1$
    /** Constant filter extension for <b>tex</b> files (value is {@value} ). Use in file dialogs. */
    String TEX_FILTER_EXTENSION = "*.tex"; //$NON-NLS-1$
    /** Constant filter extension for <b>text</b> files (value is {@value} ). Use in file dialogs. */
    String TXT_FILTER_EXTENSION = "*.txt"; //$NON-NLS-1$
    /** Constant filter extension for <b>xml</b> files (value is {@value} ). Use in file dialogs. */
    String XML_FILTER_EXTENSION = "*.xml"; //$NON-NLS-1$
    /** Constant filter extension for <b>all</b> files (value is {@value} ). Use in file dialogs. */
    String ALL_FILTER_EXTENSION = "*"; //$NON-NLS-1$
    /** Constant internationalized filter name for <b>bin</b> files. Use in file dialogs. */
    String BIN_FILTER_NAME = Messages.IConstants_0;
    /** Constant internationalized filter name for <b>csv</b> files. Use in file dialogs. */
    String CSV_FILTER_NAME = Messages.IConstants_1;
    /** Constant internationalized filter name for <b>pdf</b> files. Use in file dialogs. */
    String PDF_FILTER_NAME = Messages.IConstants_2;
    /** Constant internationalized filter name for <b>tex</b> files. Use in file dialogs. */
    String TEX_FILTER_NAME = Messages.IConstants_3;
    /** Constant internationalized filter name for <b>text</b> files. Use in file dialogs. */
    String TXT_FILTER_NAME = Messages.IConstants_4;
    /** Constant internationalized filter name for <b>xml</b> files. Use in file dialogs. */
    String XML_FILTER_NAME = Messages.IConstants_5;
    /** Constant internationalized filter name for <b>all</b> files. Use in file dialogs. */
    String ALL_FILTER_NAME = Messages.IConstants_6;
    /** UTF-8 encoding identifier (value is {@value} ). */
    String UTF8_ENCODING = "UTF-8"; //$NON-NLS-1$
}
